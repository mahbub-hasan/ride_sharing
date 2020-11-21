package com.ossnetwork.choloeksatheserver.model.response;

public class RequestStatusInfoResponse {
    private int requestStatusId;
    private String requestStatus;
    private boolean isActive;

    public RequestStatusInfoResponse(int requestStatusId, String requestStatus, boolean isActive) {
        this.requestStatusId = requestStatusId;
        this.requestStatus = requestStatus;
        this.isActive = isActive;
    }

    public int getRequestStatusId() {
        return requestStatusId;
    }

    public void setRequestStatusId(int requestStatusId) {
        this.requestStatusId = requestStatusId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
