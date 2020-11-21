package com.ossnetwork.choloeksatheserver.model.response;

public class CheckPassengerActivityResponse extends BaseResponse{
    private int activityStatus;

    public CheckPassengerActivityResponse(int statusCode, String message, boolean is_success, int activityStatus) {
        super(statusCode, message, is_success);
        this.activityStatus = activityStatus;
    }

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }
}
