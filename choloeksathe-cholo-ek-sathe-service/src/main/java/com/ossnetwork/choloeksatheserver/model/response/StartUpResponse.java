package com.ossnetwork.choloeksatheserver.model.response;

import java.util.Collection;

public class StartUpResponse extends BaseResponse{
    private Collection<UserCategoryResponse> userCategoryResponses;
    private Collection<RequestStatusInfoResponse> requestStatusInfoResponses;
    private Collection<RequestTypeResponse> requestTypeResponses;
    private Collection<JourneyTypeResponse> journeyTypeResponses;
    private Collection<ActivityStatusResponse> activityStatusResponses;

    public StartUpResponse(Collection<UserCategoryResponse> userCategoryResponses, Collection<RequestStatusInfoResponse> requestStatusInfoResponses, Collection<RequestTypeResponse> requestTypeResponses, Collection<JourneyTypeResponse> journeyTypeResponses, Collection<ActivityStatusResponse> activityStatusResponses) {
        this.userCategoryResponses = userCategoryResponses;
        this.requestStatusInfoResponses = requestStatusInfoResponses;
        this.requestTypeResponses = requestTypeResponses;
        this.journeyTypeResponses = journeyTypeResponses;
        this.activityStatusResponses = activityStatusResponses;
    }

    public StartUpResponse(int statusCode, String message, boolean is_success, Collection<UserCategoryResponse> userCategoryResponses, Collection<RequestStatusInfoResponse> requestStatusInfoResponses, Collection<RequestTypeResponse> requestTypeResponses, Collection<JourneyTypeResponse> journeyTypeResponses, Collection<ActivityStatusResponse> activityStatusResponses) {
        super(statusCode, message, is_success);
        this.userCategoryResponses = userCategoryResponses;
        this.requestStatusInfoResponses = requestStatusInfoResponses;
        this.requestTypeResponses = requestTypeResponses;
        this.journeyTypeResponses = journeyTypeResponses;
        this.activityStatusResponses = activityStatusResponses;
    }

    public Collection<UserCategoryResponse> getUserCategoryResponses() {
        return userCategoryResponses;
    }

    public void setUserCategoryResponses(Collection<UserCategoryResponse> userCategoryResponses) {
        this.userCategoryResponses = userCategoryResponses;
    }

    public Collection<RequestStatusInfoResponse> getRequestStatusInfoResponses() {
        return requestStatusInfoResponses;
    }

    public void setRequestStatusInfoResponses(Collection<RequestStatusInfoResponse> requestStatusInfoResponses) {
        this.requestStatusInfoResponses = requestStatusInfoResponses;
    }

    public Collection<RequestTypeResponse> getRequestTypeResponses() {
        return requestTypeResponses;
    }

    public void setRequestTypeResponses(Collection<RequestTypeResponse> requestTypeResponses) {
        this.requestTypeResponses = requestTypeResponses;
    }

    public Collection<JourneyTypeResponse> getJourneyTypeResponses() {
        return journeyTypeResponses;
    }

    public void setJourneyTypeResponses(Collection<JourneyTypeResponse> journeyTypeResponses) {
        this.journeyTypeResponses = journeyTypeResponses;
    }

    public Collection<ActivityStatusResponse> getActivityStatusResponses() {
        return activityStatusResponses;
    }

    public void setActivityStatusResponses(Collection<ActivityStatusResponse> activityStatusResponses) {
        this.activityStatusResponses = activityStatusResponses;
    }
}
