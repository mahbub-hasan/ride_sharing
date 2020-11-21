package com.ossnetwork.choloeksatheserver.model.response;

public class ActivityStatusResponse {
    private int activityStatusId;
    private String activityStatus;
    private boolean isActive;

    public ActivityStatusResponse(int activityStatusId, String activityStatus, boolean isActive) {
        this.activityStatusId = activityStatusId;
        this.activityStatus = activityStatus;
        this.isActive = isActive;
    }

    public int getActivityStatusId() {
        return activityStatusId;
    }

    public void setActivityStatusId(int activityStatusId) {
        this.activityStatusId = activityStatusId;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
