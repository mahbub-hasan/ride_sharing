package com.ossnetwork.choloeksatheserver.model.response;

public class RequestTypeResponse {
    private int requestTypeId;
    private String requestType;
    private boolean isActive;

    public RequestTypeResponse(int requestTypeId, String requestType, boolean isActive) {
        this.requestTypeId = requestTypeId;
        this.requestType = requestType;
        this.isActive = isActive;
    }

    public int getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(int requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
