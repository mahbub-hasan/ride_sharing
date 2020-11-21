package com.ossnetwork.choloeksatheserver.controller;

import com.ossnetwork.choloeksatheserver.model.*;
import com.ossnetwork.choloeksatheserver.model.response.*;
import com.ossnetwork.choloeksatheserver.repository.*;
import com.ossnetwork.choloeksatheserver.services.PushNotificationService;
import com.ossnetwork.choloeksatheserver.utils.CommonTask;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Path;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/driver")
public class DriverController {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    PushNotificationService pushNotificationService;
    @Autowired
    RequestInfoRepository requestInfoRepository;
    @Autowired
    ActivityInfoRepository activityInfoRepository;
    @Autowired
    ActivityStatusRepository activityStatusRepository;
    @Autowired
    RequestStatusInfoRepository requestStatusInfoRepository;
    @Autowired
    StopOverRepository stopOverRepository;
    @Autowired
    UserInfoRepository userInfoRepository;
    @Autowired
    RatingRepository ratingRepository;

    private double totalIncome;
    private double lastIncome;
    private double currentIncome;



    @PostMapping(value = "/car_share_request", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CarShareResponse carShare(@RequestBody RequestInfo requestInfo){
        try {
            // check request already pending for today
            Collection<RequestInfo> requestInfoCollection = requestInfoRepository.findByUserInfoByRequesterIdAndRequestDate(requestInfo.getUserInfoByRequesterId(),new Date(new java.util.Date().getTime()));
            if(requestInfoCollection.size()>0){
                boolean alreadyRequested = requestInfoCollection.stream().anyMatch(requestInfo1 -> requestInfo1.getRequestStatusInfoByRequestStatusId().getRequestStatusId() == 1 ||
                        requestInfo1.getRequestStatusInfoByRequestStatusId().getRequestStatusId() == 6);
                if(!alreadyRequested){
                    RequestInfo newRequestInfo = saveRequestInfo(requestInfo);

                    if(newRequestInfo != null && newRequestInfo.getRequestId()>0)
                        return new CarShareResponse(HttpStatus.OK.value(), "Request submit successfully", true, newRequestInfo.getRequestId());
                }else{
                    return new CarShareResponse(HttpStatus.OK.value(), "Already requested", false, -1);
                }
            }else{
                RequestInfo newRequestInfo = saveRequestInfo(requestInfo);
                if(newRequestInfo != null && newRequestInfo.getRequestId()>0)
                    return new CarShareResponse(HttpStatus.OK.value(), "Request submit successfully", true, newRequestInfo.getRequestId());
            }

        }catch (Exception ex){
            return new CarShareResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false, -1);
        }
        return new CarShareResponse(HttpStatus.OK.value(), "Request not submitted", false, -1);
    }

    private RequestInfo saveRequestInfo(RequestInfo requestInfo) {
        requestInfo.setLongStartPlace(requestInfo.getShortStartPlace());
        requestInfo.setLongEndPlace(requestInfo.getShortEndPlace());
        RequestInfo req = requestInfoRepository.save(requestInfo);
        if(requestInfo.getStopOver()){
            requestInfo.getStopOverInfosByRequestId().forEach(stopOverInfo -> {
                stopOverInfo.setRequestInfoByRequestId(req);
                stopOverRepository.save(stopOverInfo);
            });
        }
        return req;
    }

    @GetMapping(value = "/car_share_info/{userid}")
    public CarShareInfoByUserIdResponse getCarOwnerRequestInfoList(@PathVariable(value = "userid") int userid){
        List<CarShareListResponse> responses = new ArrayList<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userid);
        Collection<RequestInfo> requestInfoCollections = requestInfoRepository.findByUserInfoByRequesterIdAndRequestDate(userInfo, new Date(new java.util.Date().getTime()));
        if(requestInfoCollections!= null && requestInfoCollections.size()>0) {
            requestInfoCollections.forEach(requestInfo -> {
                List<StopOverListResponse> stopOverListResponses = new ArrayList<>();
                for (StopOverInfo stopOverInfo : requestInfo.getStopOverInfosByRequestId()) {
                    stopOverListResponses.add(new StopOverListResponse(stopOverInfo.getStopOverId(), stopOverInfo.getStartLatitude(), stopOverInfo.getStartLongitude(), stopOverInfo.getStopLatitude(), stopOverInfo.getStopLongitude()));
                }
                responses.add(new CarShareListResponse(requestInfo.getRequestId(), requestInfo.getStartTime(), requestInfo.getReturnTime(), requestInfo.getRequestTypeByRequestTypeId().getRequestType(),
                        requestInfo.getRequestStatusInfoByRequestStatusId().getRequestStatus(), requestInfo.getJourneyTypeInfoByJourneyTypeId().getJourneyType(), requestInfo.getRequestStatusInfoByRequestStatusId().isActive(),
                        requestInfo.getShortStartPlace(), requestInfo.getShortEndPlace(), requestInfo.getRequestDate(), requestInfo.getActivityInfosByRequestId()!=null?requestInfo.getActivityInfosByRequestId().size():0,
                        stopOverListResponses));
            });

            responses.sort((o1, o2) -> {
                Integer id1 = o1.getRequestId();
                Integer id2 = o2.getRequestId();
                return id2.compareTo(id1);
            });

            return new CarShareInfoByUserIdResponse(HttpStatus.OK.value(), "Success", true, responses);
        }
        else
            return new CarShareInfoByUserIdResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed", false, null);
    }

    @GetMapping(value = "/car_share/show_interested/list/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ShowInterestListRoot getShowInterestListByRequestId(@PathVariable(value = "requestId") int requestId){
        final int[] sumOfTotalRating = {0};
        final double[] totalRating = {0.0};
        try {
            List<ShowInterestList> showInterestLists = new ArrayList<>();

            RequestInfo requestInfo = requestInfoRepository.findOne(requestId);
            if(requestInfo != null){
                ShowInterestList showInterestList = new ShowInterestList();
                requestInfo.getActivityInfosByRequestId().forEach(activityInfo -> {
                    if(activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==1 ||
                            activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==2 ||
                            activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==4) {
                        showInterestList.setRequestId(requestInfo.getRequestId());
                        showInterestList.setActivityId(activityInfo.getActivityId());
                        showInterestList.setStartLatitude(activityInfo.getStartLatitude());
                        showInterestList.setStartLongitude(activityInfo.getStartLongitude());
                        showInterestList.setStopLatitude(activityInfo.getStopLatitude());
                        showInterestList.setStopLongitude(activityInfo.getStopLongitude());
                        showInterestList.setPrice(activityInfo.getPrice());
                        showInterestList.setStartTime(activityInfo.getStartTime());
                        showInterestList.setFirstName(activityInfo.getUserInfoByPassengerId().getFirstName());
                        showInterestList.setMobileNumber(activityInfo.getUserInfoByPassengerId().getMobileNumber());
                        showInterestList.setBioDescription(activityInfo.getUserInfoByPassengerId().getBioDescription());
                        showInterestList.setSmoker(activityInfo.getUserInfoByPassengerId().getSmoker());
                        showInterestList.setSongLover(activityInfo.getUserInfoByPassengerId().getSongLover());
                        showInterestList.setFacebookLink(activityInfo.getUserInfoByPassengerId().getFacebookLink());
                        showInterestList.setLinkedInLink(activityInfo.getUserInfoByPassengerId().getLinkedInLink());
                        showInterestList.setImageLocation(activityInfo.getUserInfoByPassengerId().getImageLocation());
                        showInterestList.setActivityStatusId(activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId());
                        showInterestList.setPassengerSitRequest(activityInfo.getNoofSeat());
                        showInterestList.setStartPointName(activityInfo.getStartPointName());
                        showInterestList.setStopPointName(activityInfo.getStopPointName());

                        // call sp for getting rating
                        showInterestList.setPassengerRating(CommonTask.getRating(entityManager, activityInfo.getUserInfoByPassengerId().getUserId()));
                        showInterestLists.add(showInterestList);
                    }
                });
                if(showInterestLists.size() > 1){
                    showInterestLists.sort((o1, o2) -> {
                        Integer id1 = o1.getActivityId();
                        Integer id2 = o2.getActivityId();
                        return id1.compareTo(id2);
                    });
                }
                return new ShowInterestListRoot(HttpStatus.OK.value(), "Success", true, showInterestLists);
            }
        }catch (Exception ex){
            return new ShowInterestListRoot(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false, null);
        }

        return new ShowInterestListRoot(HttpStatus.OK.value(), "Failed", false, null);
    }

    @GetMapping(value = "/car_share/accept_reject/{activityId}/{statusid}/{request_id}")
    public BaseResponse carShareAcceptOrReject(@PathVariable(value = "activityId") int activityId, @PathVariable(value = "statusid") int statusid, @PathVariable(value = "request_id") int request_id){
        try {
            ActivityInfo activityInfo = activityInfoRepository.findOne(activityId);
            if(activityInfo != null){
                String passengerFcmKey = activityInfo.getUserInfoByPassengerId().getFcmKey();
                ActivityStatusInfo activityStatusInfo = activityStatusRepository.findOne(statusid);
                String status = activityStatusInfo.getActivityStatus();

                // if driver cancel request then update driver sit number
                if(statusid==5){
                    RequestInfo requestInfo = requestInfoRepository.findOne(request_id);
                    requestInfo.setNoofFreeSeat(requestInfo.getNoofFreeSeat()+activityInfo.getNoofSeat());
                    requestInfoRepository.save(requestInfo);
                }else{
                    //update activity info
                    activityInfo.setActivityStatusInfoByActivityStatusId(activityStatusInfo);
                    activityInfoRepository.save(activityInfo);
                }

                //send push notification to user
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("details",activityInfo.getUserInfoByDriverId().getFirstName()+" "+status+" your request.");
                jsonObject.put("driver_pic", activityInfo.getUserInfoByDriverId().getImageLocation());
                if(statusid == 2){
                    jsonObject.put("type","INTEREST_ACCEPT");
                }else if(statusid == 3){
                    jsonObject.put("type","INTEREST_REJECT");
                }

                pushNotificationService.sendSingleUserNotificaiton(passengerFcmKey,jsonObject);

                return new BaseResponse(HttpStatus.OK.value(), "Success", true);
            }
        }catch (Exception ex){
            return new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false);
        }
        return new BaseResponse(HttpStatus.OK.value(), "failed", false);
    }

    @GetMapping(value = "/car_share/request_cancel/{request_id}")
    public BaseResponse driverCarShareCancel(@PathVariable(value = "request_id") int request_id){
        try{
            RequestInfo requestInfo = requestInfoRepository.findOne(request_id);
            if(requestInfo.getRequestStatusInfoByRequestStatusId().getRequestStatusId()==1&&requestInfo.getActivityInfosByRequestId() != null){
                // change activity status with push notifications
                requestInfo.getActivityInfosByRequestId().forEach(activityInfo -> {
                    ActivityInfo activityInfoById = activityInfoRepository.findOne(activityInfo.getActivityId());

                    ActivityStatusInfo activityStatusInfo = new ActivityStatusInfo();
                    activityStatusInfo.setActivityStatusId(5);
                    activityInfoById.setActivityStatusInfoByActivityStatusId(activityStatusInfo);

                    //update activity status
                    activityInfoRepository.save(activityInfoById);


                    // send notifications
                    UserInfo passenger = activityInfo.getUserInfoByPassengerId();
                    if(passenger != null){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", "DRIVER_REQUEST_CANCEL");

                        pushNotificationService.sendSingleUserNotificaiton(passenger.getFcmKey(),jsonObject);
                    }
                });
            }
            RequestStatusInfo requestStatusInfo = new RequestStatusInfo();
            requestStatusInfo.setRequestStatusId(3);
            requestInfo.setRequestStatusInfoByRequestStatusId(requestStatusInfo);
            requestInfoRepository.save(requestInfo);
            return new BaseResponse(HttpStatus.OK.value(), "Request cancel", true);
        }catch (Exception ex){
            return new BaseResponse(HttpStatus.OK.value(), ex.getMessage(), false);
        }
    }

    @GetMapping(value = "/car_share/status_change/{request_id}/{status_id}")
    public BaseResponse carShareStatusChanges(@PathVariable(value = "request_id") int request_id, @PathVariable(value = "status_id") int status_id){
        try {
            RequestInfo requestInfo = requestInfoRepository.findOne(request_id);
            RequestStatusInfo requestStatusInfo = requestStatusInfoRepository.findOne(status_id);
            if(requestInfo != null && requestInfo.getRequestStatusInfoByRequestStatusId().getRequestStatusId()==1){
                requestInfo.setRequestStatusInfoByRequestStatusId(requestStatusInfo);
                requestInfoRepository.save(requestInfo);

                // send notification to all passenger
                if(requestInfo.getActivityInfosByRequestId() != null && requestInfo.getActivityInfosByRequestId().size()>0 && status_id == 6){
                    List<String> passengersFcmKey = requestInfo.getActivityInfosByRequestId().stream().map(activityInfo -> {
                        return activityInfo.getUserInfoByPassengerId().getFcmKey();
                    }).collect(Collectors.toList());

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "DRIVER_START_JOURNEY");

                    pushNotificationService.sendMultipleNotification(passengersFcmKey,jsonObject);
                }

                return new BaseResponse(HttpStatus.OK.value(), "Success", true);
            }else if(requestInfo != null && requestInfo.getRequestStatusInfoByRequestStatusId().getRequestStatusId()==6){
                requestInfo.setRequestStatusInfoByRequestStatusId(requestStatusInfo);
                requestInfoRepository.save(requestInfo);

                return new BaseResponse(HttpStatus.OK.value(), "Success", true);
            }
        }catch (Exception ex){
            return new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false);
        }
        return new BaseResponse(HttpStatus.OK.value(), "Something error in the server", false);
    }

    @GetMapping(value = "/individual_passenger/status_change/{activity_id}/{status_id}")
    public BaseResponse individualPassengerStatusChange(@PathVariable(value = "activity_id") int activity_id, @PathVariable(value = "status_id") int status_id){
        try {
            ActivityInfo activityInfo = activityInfoRepository.findOne(activity_id);
            if(activityInfo.getRequestInfoByRequestId().getRequestStatusInfoByRequestStatusId().getRequestStatusId()==6){
                ActivityStatusInfo activityStatusInfo = activityStatusRepository.findOne(status_id);
                activityInfo.setActivityStatusInfoByActivityStatusId(activityStatusInfo);
                activityInfoRepository.save(activityInfo);

                // send notification to passenger
                String passengerFcmKey = activityInfo.getUserInfoByPassengerId().getFcmKey();

                JSONObject jsonObject = new JSONObject();
                if(status_id == 4)
                    jsonObject.put("type","PASSENGER_JOURNEY_STARTED");
                else if(status_id == 7)
                    jsonObject.put("type", "PASSENGER_JOURNEY_COMPLETED");

                pushNotificationService.sendSingleUserNotificaiton(passengerFcmKey, jsonObject);

                return new BaseResponse(HttpStatus.OK.value(), "Status changes", true);
            }else{
                return new BaseResponse(HttpStatus.OK.value(), "Driver must be start his or her journey before start passenger journey.", false);
            }


        }catch (Exception ex){
            return new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false);
        }
    }

    @GetMapping(value = "/navigation/{request_id}")
    public DriverNavigation getNavigation(@PathVariable(value = "request_id") int requestId){
        DriverNavigation driverNavigation;
        final int[] sumOfTotalRating = {0};
        final double[] totalRating = {0.0};
        try{
            RequestInfo requestInfo = requestInfoRepository.findOne(requestId);
            if(requestInfo != null){
                driverNavigation = new DriverNavigation();

                driverNavigation.setRequestID(requestInfo.getRequestId());
                driverNavigation.setStartLocationLat(requestInfo.getStartLatitude());
                driverNavigation.setStartLocationLan(requestInfo.getStartLongitude());
                driverNavigation.setStopLocationLat(requestInfo.getStopLatitude());
                driverNavigation.setStopLocationLan(requestInfo.getStopLongitude());
                driverNavigation.setStartTime(requestInfo.getStartTime());
                driverNavigation.setRequestStatusCode(requestInfo.getRequestStatusInfoByRequestStatusId().getRequestStatusId());

                // add stopover info
                if(requestInfo.getStopOver()){
                    Collection<StopOverListResponse> stopOverListResponses = new ArrayList<>();
                    requestInfo.getStopOverInfosByRequestId().forEach(stopOverInfo -> {
                        StopOverListResponse stopOverListResponse = new StopOverListResponse(stopOverInfo.getStopOverId(),
                                stopOverInfo.getStartLatitude(),stopOverInfo.getStartLongitude(),stopOverInfo.getStopLatitude(),stopOverInfo.getStopLongitude(),
                                stopOverInfo.getStartPointName(), stopOverInfo.getStopPointName());
                        stopOverListResponses.add(stopOverListResponse);
                    });
                    driverNavigation.setStopOverInfos(stopOverListResponses);
                }

                // add interest passenger list
                if(requestInfo.getActivityInfosByRequestId() != null && requestInfo
                        .getActivityInfosByRequestId().size()>0){
                    Collection<ShowInterestList> interestLists = new ArrayList<>();
                    requestInfo.getActivityInfosByRequestId().forEach(activityInfo -> {
                        if(activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==1 ||
                                activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==2 ||
                                activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==4){
                            // get total Rating
                           totalRating[0] = CommonTask.getRating(entityManager, activityInfo.getUserInfoByPassengerId().getUserId());
                            ShowInterestList showInterestList = new ShowInterestList(activityInfo.getActivityId(),
                                    activityInfo.getStartLatitude(), activityInfo.getStartLongitude(),
                                    activityInfo.getStopLatitude(), activityInfo.getStopLongitude(),
                                    activityInfo.getStartPointName(), activityInfo.getStopPointName(),
                                    activityInfo.getPrice(), activityInfo.getNoofSeat(),
                                    activityInfo.getStartTime(), activityInfo.getUserInfoByPassengerId().getFirstName(),
                                    activityInfo.getUserInfoByPassengerId().getMobileNumber(), activityInfo.getUserInfoByPassengerId().getImageLocation(),
                                    activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId(), totalRating[0]);
                            interestLists.add(showInterestList);
                        }
                    });
                    if(interestLists.size()>0)
                        driverNavigation.setShowInterestLists(interestLists);
                }

                driverNavigation.setStatusCode(HttpStatus.OK.value());
                driverNavigation.setMessage("Success");
                driverNavigation.setIs_success(true);

                return driverNavigation;
            }
        }catch (Exception ex){
            return new DriverNavigation(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false);
        }

        return new DriverNavigation(HttpStatus.NOT_FOUND.value(), "Something error in server", false);
    }

    @GetMapping(value = "/first-check/{id}")
    public DriverActivityStatus getDriverActivityStatus(@PathVariable(value = "id") int userId){
        DriverActivityStatus driverActivityStatus = null;
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);

            RequestInfo requestInfo = requestInfoRepository.findTop1ByUserInfoByRequesterIdOrderByRequestIdDesc(userInfo);
            if(requestInfo != null){
                if(requestInfo.getRequestStatusInfoByRequestStatusId().getRequestStatusId() == 1 ||
                        requestInfo.getRequestStatusInfoByRequestStatusId().getRequestStatusId() == 4 ||
                        requestInfo.getRequestStatusInfoByRequestStatusId().getRequestStatusId() == 6){
                    driverActivityStatus = new DriverActivityStatus(HttpStatus.OK.value(), "Record found", true, requestInfo.getRequestId());
                }else{
                    driverActivityStatus = new DriverActivityStatus(HttpStatus.OK.value(), "No record found", true, 0);
                }
            }else{
                driverActivityStatus = new DriverActivityStatus(HttpStatus.OK.value(), "No record found", false, 0);
            }
        }catch (Exception ex){
            driverActivityStatus = new DriverActivityStatus(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false, -1);
        }

        return driverActivityStatus;
    }

    @GetMapping(value = "/history/{userid}")
    public DriverCarShareHistoryRoot driverCarShareHistory(@PathVariable(value = "userid") int userId){
        DriverCarShareHistoryRoot history = null;
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);
            Collection<RequestInfo> requestInfos = requestInfoRepository.findByUserInfoByRequesterIdOrderByRequestIdDesc(userInfo);
            if(requestInfos != null && requestInfos.size()>0){
                List<DriverCarShareHistory> carShareHistories = new ArrayList<>();
                requestInfos.forEach(requestInfo -> {
                    carShareHistories.add(new DriverCarShareHistory(requestInfo.getRequestId(),requestInfo.getShortStartPlace(),requestInfo.getShortEndPlace(),requestInfo.getStartTime(),requestInfo.getRequestDate()));
                });

                history = new DriverCarShareHistoryRoot(HttpStatus.OK.value(), "Success", true, carShareHistories);
            }else{
                history = new DriverCarShareHistoryRoot(HttpStatus.OK.value(), "No record found", false, null);
            }
        }catch (Exception ex){
            history = new DriverCarShareHistoryRoot(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false, null);
        }
        return history;
    }

    @GetMapping(value = "/dashboard/{id}")
    public DriverDashboardRoot getDriverDashboard(@PathVariable(value = "id") int id){
        DriverDashboardRoot driverDashboardRoot = null;
        final int[] sumOfTotalRating = {0};
        final double[] totalRating = {0.0};
        try {
            totalIncome=lastIncome=currentIncome=0.0;
            DriverDashboard driverDashboard = new DriverDashboard();
            UserInfo driverInfo = userInfoRepository.findOne(id);
            if(driverInfo != null){

                //set driver basic information
                driverDashboard.setFullName(driverInfo.getFirstName());
                driverDashboard.setContactNumber(driverInfo.getMobileNumber());
                driverDashboard.setImageUrl(driverInfo.getImageLocation());
                driverDashboard.setActive(driverInfo.getActive());
                driverDashboard.setAdminActive(driverInfo.getProfileApprovedByAdmin());

                // get totalRating
                driverDashboard.setTotalRating(CommonTask.getRating(entityManager, driverInfo.getUserId()));

                // find driver total income
                driverInfo.getRequestInfosByUserId().forEach(requestInfo -> {
                    requestInfo.getActivityInfosByRequestId().forEach(activityInfo -> {
                        if(activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==7)
                            totalIncome += (activityInfo.getNoofSeat()*activityInfo.getPrice());
                    });
                });
            }
            // set user Info
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(id);
            // find driver last income
            List<RequestInfo> lastRequest = requestInfoRepository.findTop2ByUserInfoByRequesterIdOrderByRequestIdDesc(userInfo);
            if(lastRequest != null && lastRequest.size()==2){
                for(int lastIndex=1;lastIndex<lastRequest.size();lastIndex++){
                    lastRequest.get(lastIndex).getActivityInfosByRequestId().forEach(activityInfo -> {
                        if(activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==7)
                            lastIncome += (activityInfo.getPrice()*activityInfo.getNoofSeat());
                    });
                }
            }
            // find driver current income
            RequestInfo currentRequest = requestInfoRepository.findTop1ByUserInfoByRequesterIdOrderByRequestIdDesc(userInfo);
            if(currentRequest != null){
                currentRequest.getActivityInfosByRequestId().forEach(activityInfo -> {
                    if(activityInfo.getActivityStatusInfoByActivityStatusId().getActivityStatusId()==7)
                        currentIncome += (activityInfo.getPrice()*activityInfo.getNoofSeat());
                });
            }

            driverDashboard.setTotalIncome(totalIncome);
            driverDashboard.setLastRequestIncome(lastIncome);
            driverDashboard.setCurrentRequestIncome(currentIncome);

            driverDashboardRoot = new DriverDashboardRoot(HttpStatus.OK.value(), "Success", true, driverDashboard);
        }catch (Exception ex){
            driverDashboardRoot = new DriverDashboardRoot(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false);
        }

        return driverDashboardRoot;
    }

    @PostMapping(value = "/rating/add")
    public BaseResponse addANewRating(@RequestBody Ratings ratings){
        try {
            Ratings newAddedRatings = ratingRepository.save(ratings);
            if(newAddedRatings.getId()>0){
                // change activity info rating for passenger boolean value
                ActivityInfo activityInfo = activityInfoRepository.findOne(newAddedRatings.getActivityInfoByActivityId().getActivityId());
                if(activityInfo != null){
                    activityInfo.setDriverRatingStatus(true);
                    activityInfoRepository.save(activityInfo);
                }
                return new BaseResponse(HttpStatus.OK.value(), "Success", true);
            }
        }catch (Exception ex){
            return new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false);
        }

        return new BaseResponse(HttpStatus.SEE_OTHER.value(), "Server error" , false);
    }
}
