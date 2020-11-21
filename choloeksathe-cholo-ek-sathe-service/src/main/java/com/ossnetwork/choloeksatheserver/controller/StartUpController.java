package com.ossnetwork.choloeksatheserver.controller;

import com.ossnetwork.choloeksatheserver.model.*;
import com.ossnetwork.choloeksatheserver.model.response.*;
import com.ossnetwork.choloeksatheserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/api/start-up")
public class StartUpController {


    @Autowired
    UserCategoryRepository userCategoryRepository;
    @Autowired
    RequestStatusInfoRepository requestStatusInfoRepository;
    @Autowired
    RequestTypeRepository requestTypeRepository;
    @Autowired
    JourneyTypeRepository journeyTypeRepository;
    @Autowired
    ActivityStatusRepository activityStatusRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/manage")
    @ResponseBody
    public StartUpResponse getAllRootTable(){
        Collection<UserCategoryResponse> userCategoryResponses = new ArrayList<>();
        Collection<RequestStatusInfoResponse> requestStatusInfoResponses = new ArrayList<>();
        Collection<RequestTypeResponse> requestTypeResponses = new ArrayList<>();
        Collection<JourneyTypeResponse> journeyTypeResponses = new ArrayList<>();
        Collection<ActivityStatusResponse> activityStatusResponses = new ArrayList<>();
        try {
            List<UserCategoryInfo> userCategoryInfos = userCategoryRepository.findAll();
            List<RequestStatusInfo> requestStatusInfos = requestStatusInfoRepository.findAll();
            List<RequestType> requestTypes = requestTypeRepository.findAll();
            List<JourneyTypeInfo> journeyTypeInfos = journeyTypeRepository.findAll();
            List<ActivityStatusInfo> activityStatusInfos = activityStatusRepository.findAll();

            userCategoryInfos.forEach(userCategoryInfo -> {
                userCategoryResponses.add(new UserCategoryResponse(userCategoryInfo.getUserCategoryId(), userCategoryInfo.getUserCategory(),userCategoryInfo.isActive()));
            });

            requestStatusInfos.forEach(requestStatusInfo -> {
                requestStatusInfoResponses.add(new RequestStatusInfoResponse(requestStatusInfo.getRequestStatusId(), requestStatusInfo.getRequestStatus(), requestStatusInfo.isActive()));
            });

            requestTypes.forEach(requestType -> {
                requestTypeResponses.add(new RequestTypeResponse(requestType.getRequestTypeId(), requestType.getRequestType(), requestType.isActive()));
            });

            journeyTypeInfos.forEach(journeyTypeInfo -> {
                journeyTypeResponses.add(new JourneyTypeResponse(journeyTypeInfo.getJourneyTypeId(), journeyTypeInfo.getJourneyType(), journeyTypeInfo.isActive()));
            });

            activityStatusInfos.forEach(activityStatusInfo -> {
                activityStatusResponses.add(new ActivityStatusResponse(activityStatusInfo.getActivityStatusId(), activityStatusInfo.getActivityStatus(), activityStatusInfo.isActive()));
            });

            return new StartUpResponse(HttpStatus.OK.value(), "success", true, userCategoryResponses,
                    requestStatusInfoResponses, requestTypeResponses, journeyTypeResponses, activityStatusResponses);
        }catch (Exception ex){
            return new StartUpResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false, null, null, null, null, null);
        }
    }
}
