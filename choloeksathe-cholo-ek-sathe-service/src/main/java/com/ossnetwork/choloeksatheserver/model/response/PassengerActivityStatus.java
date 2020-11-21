package com.ossnetwork.choloeksatheserver.model.response;

public class PassengerActivityStatus extends BaseResponse{
    private int activityId;

    public PassengerActivityStatus(int statusCode, String message, boolean is_success, int activityId) {
        super(statusCode, message, is_success);
        this.activityId = activityId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }
}
