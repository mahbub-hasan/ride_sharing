package com.ossnetwork.choloeksatheserver.controller;

import com.ossnetwork.choloeksatheserver.model.*;
import com.ossnetwork.choloeksatheserver.model.response.*;
import com.ossnetwork.choloeksatheserver.repository.ActivityInfoRepository;
import com.ossnetwork.choloeksatheserver.repository.RatingRepository;
import com.ossnetwork.choloeksatheserver.repository.RequestInfoRepository;
import com.ossnetwork.choloeksatheserver.repository.UserInfoRepository;
import com.ossnetwork.choloeksatheserver.services.PushNotificationService;
import com.ossnetwork.choloeksatheserver.utils.CommonTask;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping(value = "/api/passenger")
public class PassengerController {

    @Autowired
    RequestInfoRepository requestInfoRepository;
    @Autowired
    ActivityInfoRepository activityInfoRepository;
    @Autowired
    PushNotificationService pushNotificationService;
    @Autowired
    UserInfoRepository userInfoRepository;
    @Autowired
    RatingRepository ratingRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping(value = "/nearest_car")
    public PassengerCarResponse getAvailableAndNearestCarInfo(@RequestBody PassengerCarRequest passengerCarRequest){
        final int[] carId = new int[1];
        final String[] carNumber = new String[1];
        final String[] carPicture = new String[1];
        final int[] sumOfTotalRating = {0};
        final double[] totalRatingPoint = {0.0};
        List<PassengerCarRequestResponse> resultantRequest = new ArrayList<>();
        PassengerCarResponse response = new PassengerCarResponse();
        try {

            RequestStatusInfo requestStatusInfo = new RequestStatusInfo();
            requestStatusInfo.setRequestStatusId(1);

            Collection<RequestInfo> requestInfos = requestInfoRepository.findAllByStartTimeBetweenAndRequestStatusInfoByRequestStatusIdOrderByRequestIdDesc(passengerCarRequest.getStartTime(), passengerCarRequest.getEndTime(), requestStatusInfo);

            requestInfos.forEach(requestInfo -> {
                if(requestInfo.getNoofFreeSeat() > 0){

                    // driver start point arrayList
                    ArrayList<Double> driverStartPoint = new ArrayList<>();
                    driverStartPoint.add(requestInfo.getStartLatitude());
                    driverStartPoint.add(requestInfo.getStartLongitude());

                    // driver end point arrayList
                    ArrayList<Double> driverEndPoint = new ArrayList<>();
                    driverEndPoint.add(requestInfo.getStopLatitude());
                    driverEndPoint.add(requestInfo.getStopLongitude());

                    // add stopOvers if available
                    ArrayList<DriverStopOver> driverStopOversInfo = new ArrayList<>();
                    if(requestInfo.getStopOver()){
                        requestInfo.getStopOverInfosByRequestId().forEach(stopOverInfo -> {
                            driverStopOversInfo.add(new DriverStopOver(stopOverInfo.getStopOverId(), stopOverInfo.getStartLatitude(), stopOverInfo.getStartLongitude(),
                                    stopOverInfo.getStopLatitude(), stopOverInfo.getStopLongitude()));
                        });
                    }

                    boolean isRequestInfoAcceptable = CommonTask.isSourceLocationAccess(
                            passengerCarRequest.getStartLatitude(),passengerCarRequest.getStartLongitude(),
                            passengerCarRequest.getStopLatitude(), passengerCarRequest.getStopLongitude(),
                            driverStartPoint, driverEndPoint, driverStopOversInfo);

                    // if driver found
                    if(isRequestInfoAcceptable){
                        java.util.Collection<DriverStopOver> driverStopOvers = new ArrayList<>();
                        if(requestInfo.getStopOver()) {
                            requestInfo.getStopOverInfosByRequestId().forEach(stopOverInfo -> {
                                driverStopOvers.add(new DriverStopOver(stopOverInfo.getStopOverId(), stopOverInfo.getStartLatitude(),
                                        stopOverInfo.getStartLongitude(), stopOverInfo.getStopLatitude(), stopOverInfo.getStopLongitude(),
                                        stopOverInfo.getStartPointName(), stopOverInfo.getStopPointName()));

                            });
                        }

                        // get car information for specific driver
                        requestInfo.getUserInfoByRequesterId().getCarInfosByUserId().forEach(carInfo -> {
                            if(carInfo.isActive()){
                                carId[0] = carInfo.getCarInfoId();
                                carNumber[0] = carInfo.getCarNumber();
                                carPicture[0] = carInfo.getImage();
                            }
                        });

                        // get driver rating information

                        totalRatingPoint[0] = CommonTask.getRating(entityManager, requestInfo.getUserInfoByRequesterId().getUserId());

                        // find the nearest distance
                        resultantRequest.add(new PassengerCarRequestResponse(requestInfo.getRequestId(),requestInfo.getStartLatitude(),requestInfo.getStartLongitude(),
                                requestInfo.getStopLatitude(),requestInfo.getStopLongitude(),requestInfo.getShortStartPlace(),requestInfo.getShortEndPlace(),requestInfo.getPrice(),
                                requestInfo.getNoofFreeSeat(),requestInfo.getStopOver(),requestInfo.getStartTime(),requestInfo.getReturnTime(),
                                requestInfo.getLuggageSpaceAvailable(),requestInfo.getSmokingAllowed(),requestInfo.getOutSideFoodAllowed(),requestInfo.getMusicAllowed(),
                                requestInfo.getPetsAllowed(),requestInfo.getGenderRestriction(),requestInfo.getRequestDate(),requestInfo.getRequestTypeByRequestTypeId().getRequestType(),
                                requestInfo.getRequestStatusInfoByRequestStatusId().getRequestStatus(),requestInfo.getJourneyTypeInfoByJourneyTypeId().getJourneyType(),
                                requestInfo.getUserInfoByRequesterId().getUserId(),requestInfo.getUserInfoByRequesterId().getImageLocation(), requestInfo.getUserInfoByRequesterId().getFirstName(),
                                requestInfo.getUserInfoByRequesterId().getMobileNumber(), totalRatingPoint[0],
                                carId[0], carPicture[0], carNumber[0],driverStopOvers,
                                0));
                    }
                }
            });
           // Collections.sort(response.getPassengerCarRequestResponses(), Comparator.comparing(o -> String.valueOf(o.getDistance())));
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Request info successfully get from server");
            response.setIs_success(true);
            response.setPassengerCarRequestResponses(resultantRequest);
            return  response;
        }catch (Exception ex){
            return new PassengerCarResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false,  null);
        }

    }

    @PostMapping(value = "/show_interest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse showInterest(@RequestBody ActivityInfo activityInfo){
        try {
            boolean isAlreadyRequested = false;
            // check passenger already show interest
            Collection<ActivityInfo> passengerActivity = activityInfoRepository.findByUserInfoByPassengerId(activityInfo.getUserInfoByPassengerId());
            if(passengerActivity != null){
                isAlreadyRequested = passengerActivity.stream().anyMatch(activityInfo1 -> activityInfo1.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==1 ||
                                    activityInfo1.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==2 ||
                                    activityInfo1.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==4);
                if(isAlreadyRequested){
                    return new BaseResponse(HttpStatus.OK.value(), "Already requested", false);
                }else{
                    RequestInfo requestInfo = requestInfoRepository.findOne(activityInfo.getRequestInfoByRequestId().getRequestId());
                    if(requestInfo.getNoofFreeSeat()>0 && (requestInfo.getNoofFreeSeat()-activityInfo.getNoofSeat())>=0 ){
                        activityInfo.setDriverRatingStatus(false);
                        activityInfo.setPassangerRatingStatus(false);
                        ActivityInfo newlyAddedRequest = activityInfoRepository.save(activityInfo);
                        // update request info sit number
                        requestInfo.setNoofFreeSeat(requestInfo.getNoofFreeSeat()-newlyAddedRequest.getNoofSeat());
                        requestInfoRepository.save(requestInfo);

                        if(newlyAddedRequest.getActivityId() > 0){
                            // get driver fcmKey
                            UserInfo driver = userInfoRepository.findOne(activityInfo.getUserInfoByDriverId().getUserId());

                            // find passenger
                            UserInfo passenger = userInfoRepository.findOne(activityInfo.getUserInfoByPassengerId().getUserId());
                            // make message
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("activity_id", newlyAddedRequest.getActivityId());
                            jsonObject.put("request_id", newlyAddedRequest.getRequestInfoByRequestId().getRequestId());
                            jsonObject.put("passenger_id", newlyAddedRequest.getUserInfoByPassengerId().getUserId());
                            jsonObject.put("details", passenger.getFirstName()+" is showing an interest.");
                            jsonObject.put("type","SHOW_INTEREST");
                            // send push notification to the driver
                            pushNotificationService.sendSingleUserNotificaiton(driver.getFcmKey(),jsonObject);

                            return new BaseResponse(HttpStatus.OK.value(), "Success", true);
                        }
                    }else {
                        return new BaseResponse(HttpStatus.OK.value(), "No available sit found. Available sit number is "+requestInfo.getNoofFreeSeat(),false);
                    }
                }
            }

        }catch (Exception ex){
            return new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false);
        }

        return new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server error", false);
    }

    @GetMapping(value = "/cancel_interest/{activityId}")
    public BaseResponse passengerCancelRequest(@PathVariable(value = "activityId") int activityId){
        try {
            //long currentTime = new java.util.Date().getTime();
            ActivityInfo activityInfo = activityInfoRepository.findOne(activityId);
            if(activityInfo != null){
                int status = activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId();

                if(status==2||status==1){
                    //update request info sit number
                    // cancel now
                    ActivityStatusInfo activityStatusInfo = new ActivityStatusInfo();
                    activityStatusInfo.setActivityStatusId(6);

                    activityInfo.setActivityStatusInfoByActivityStatusId(activityStatusInfo);
                    activityInfoRepository.save(activityInfo);

                    // update sit number of request info table
                    RequestInfo requestInfo = requestInfoRepository.findOne(activityInfo.getRequestInfoByRequestId().getRequestId());
                    requestInfo.setNoofFreeSeat(requestInfo.getNoofFreeSeat()+activityInfo.getNoofSeat());

                    requestInfoRepository.save(requestInfo);

                    //send notification to driver
                    UserInfo userInfo = userInfoRepository.findOne(activityInfo.getUserInfoByDriverId().getUserId());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "INTEREST_CANCEL");

                    pushNotificationService.sendSingleUserNotificaiton(userInfo.getFcmKey(), jsonObject);

                    return new BaseResponse(HttpStatus.OK.value(), "Successfully interest cancel", true);
                }else{

                    return new BaseResponse(HttpStatus.OK.value(), "Interest cancel is not possible right now", false);
                }

                //long requestTime = activityInfo.getStartTime().getTime();

                //find the distance between 2 times
                //long timeDistance = requestTime-currentTime;
                //long hour = timeDistance/(1000*60*60);
                //long mins = timeDistance%(1000*60*60);
                //if(hour >= 0 && mins > 15){
                    // check already paired



               // }else{
                    //cancel time over
                   // return new BaseResponse(HttpStatus.OK.value(), "Cancel time over.", false);
                //}
            }
        }catch (Exception ex){
            return new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false);
        }
        return new BaseResponse(HttpStatus.OK.value(), "Something error", false);
    }

    @GetMapping(value = "/current_session/{user_id}")
    public CurrentSessionResponse getCurrentSession(@PathVariable(value = "user_id") int id){
        final CurrentSessionResponse[] currentSessionResponse = {null};
        try{
            UserInfo passengerUserInfo = new UserInfo();
            passengerUserInfo.setUserId(id);

            Collection<ActivityInfo> activityInfo = activityInfoRepository.findByUserInfoByPassengerId(passengerUserInfo);
            if(activityInfo != null && activityInfo.size()>0){
                activityInfo.forEach(activityInfos -> {
                    if(activityInfos.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==1 ||
                            activityInfos.getActivityStatusInfoByActivityStatusId().getActivityStatusId() == 2 ||
                            activityInfos.getActivityStatusInfoByActivityStatusId().getActivityStatusId() == 4){

                        currentSessionResponse[0] = new CurrentSessionResponse(HttpStatus.OK.value(), "Success", true, activityInfos.getActivityId(),
                                activityInfos.getPrice(), activityInfos.getNoofSeat(), activityInfos.getStartTime(),
                                activityInfos.getActivityStatusInfoByActivityStatusId().getActivityStatus(), activityInfos.getUserInfoByDriverId().getUserId(),
                                activityInfos.getUserInfoByDriverId().getFirstName(), activityInfos.getUserInfoByDriverId().getImageLocation(), activityInfos.getUserInfoByDriverId().getMobileNumber(),
                                activityInfos.getStartLatitude(), activityInfos.getStartLongitude(), activityInfos.getStopLatitude(),activityInfos.getStopLongitude(),
                                activityInfos.getStartPointName(), activityInfos.getStopPointName());
                    }
                });

                if(currentSessionResponse != null && currentSessionResponse[0] != null && currentSessionResponse.length>0){
                    return currentSessionResponse[0];
                }else{
                    return new CurrentSessionResponse(HttpStatus.OK.value(), "No session", false);
                }
            }else{
                return new CurrentSessionResponse(HttpStatus.OK.value(), "No current session", false);
            }
        }catch (Exception ex){
            return new CurrentSessionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false);
        }
    }

    @GetMapping(value = "/history/{passenger_id}")
    public PassengerHistoryRoot getPassengerCompleteRideHistory(@PathVariable(value = "passenger_id") int passenger_id){
        try {
            final int[] sumOfTotalRating = {0};
            final double[] totalRating = {0.0};
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(passenger_id);

            ActivityStatusInfo activityStatusInfo = new ActivityStatusInfo();
            activityStatusInfo.setActivityStatusId(7);

            Collection<ActivityInfo> activityInfo = activityInfoRepository
                    .findByUserInfoByPassengerIdAndActivityStatusInfoByActivityStatusIdOrderByActivityIdDesc(userInfo, activityStatusInfo);
            if(activityInfo != null && activityInfo.size()>0){
                List<PassengerHistory> passengerHistories = activityInfo.stream().map(activityInfos -> {

                    totalRating[0] = CommonTask.getRating(entityManager, activityInfos.getUserInfoByDriverId().getUserId());

                        return new PassengerHistory(activityInfos.getActivityId(), activityInfos.getStartLatitude(),
                            activityInfos.getStartLongitude(), activityInfos.getStopLatitude(), activityInfos.getStopLongitude(),
                            activityInfos.getStartPointName(), activityInfos.getStopPointName(),
                            activityInfos.getPrice(), activityInfos.getNoofSeat(), activityInfos.getStartTime(),
                            activityInfos.getUserInfoByDriverId().getUserId(), activityInfos.getUserInfoByDriverId().getFirstName(),
                            activityInfos.getUserInfoByDriverId().getImageLocation(), activityInfos.getRequestInfoByRequestId().getRequestDate(),
                                totalRating[0]);
                }).collect(Collectors.toList());


                return new PassengerHistoryRoot(HttpStatus.OK.value(), "History retrive successfully", true, passengerHistories);
            }else{
                return new PassengerHistoryRoot(HttpStatus.OK.value(), "No history found.", false);
            }
        }catch (Exception ex){
            return new PassengerHistoryRoot(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false);
        }
    }

    @GetMapping(value = "/home/check-all/{user_id}")
    public PassengerHomePageRequestRoot passengerHomePageRequest(@PathVariable(value = "user_id") int userId){
        final int[] totalRatingSum = {0};
        double totalRating = 0.0;
        try {
            UserInfo userInfo = userInfoRepository.findOne(userId);
            if(userInfo != null){
                totalRating = CommonTask.getRating(entityManager, userInfo.getUserId());
                return new PassengerHomePageRequestRoot(HttpStatus.OK.value(), "Success", true, new PassengerHomePageRequest(userInfo.getFirstName(), userInfo.getImageLocation(), userInfo.getMobileNumber(), totalRating, userInfo.getActive()));
            }
        }catch (Exception ex){
            return new PassengerHomePageRequestRoot(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false);
        }
        return new PassengerHomePageRequestRoot(HttpStatus.OK.value(), "Server error", false);
    }

    @GetMapping(value = "/first-check/{id}")
    public PassengerActivityStatus passengerActivityCheck(@PathVariable(value = "id") int userId){
        PassengerActivityStatus passengerActivityStatus = null;
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);

            ActivityInfo activityInfo = activityInfoRepository.findTop1ByUserInfoByPassengerIdOrderByActivityIdDesc(userInfo);
            if(activityInfo != null){
                if(activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==1 || activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==2 ||
                        activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==4){
                    passengerActivityStatus = new PassengerActivityStatus(HttpStatus.OK.value(), "Already in a session", true, activityInfo.getActivityId());
                }else{
                    passengerActivityStatus = new PassengerActivityStatus(HttpStatus.OK.value(), "No current session found", true, 0);
                }
            }else{
                passengerActivityStatus = new PassengerActivityStatus(HttpStatus.OK.value(), "No data found", false, -1);
            }
        }catch (Exception ex){
            passengerActivityStatus = new PassengerActivityStatus(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server error", false, -1);
        }

        return passengerActivityStatus;
    }

    @PostMapping(value = "/rating/add")
    public BaseResponse addANewRating(@RequestBody Ratings ratings){
        try {
            Ratings newAddedRatings = ratingRepository.save(ratings);
            if(newAddedRatings.getId()>0){
                // change activity info rating for passenger boolean value
                ActivityInfo activityInfo = activityInfoRepository.findOne(newAddedRatings.getActivityInfoByActivityId().getActivityId());
                if(activityInfo != null){
                    activityInfo.setPassangerRatingStatus(true);
                    activityInfoRepository.save(activityInfo);
                }
                return new BaseResponse(HttpStatus.OK.value(), "Success", true);
            }
        }catch (Exception ex){
            return new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false);
        }

        return new BaseResponse(HttpStatus.SEE_OTHER.value(), "Server error" , false);
    }

    @GetMapping(value = "/check/activity/{activity_id}")
    public CheckPassengerActivityResponse checkPassengerCurrentActivityStatus(@PathVariable(value = "activity_id") int activityId){
        ActivityInfo activityInfo = activityInfoRepository.findOne(activityId);
        if(activityInfo != null){
            return new CheckPassengerActivityResponse(HttpStatus.OK.value(), "success", true, activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId());
        }
        return new CheckPassengerActivityResponse(HttpStatus.NOT_FOUND.value(), "failed", false, -1);
    }

}