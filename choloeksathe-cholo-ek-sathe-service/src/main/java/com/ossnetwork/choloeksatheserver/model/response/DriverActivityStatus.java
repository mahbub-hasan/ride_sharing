package com.ossnetwork.choloeksatheserver.model.response;

public class DriverActivityStatus extends BaseResponse{
    private int requestId;

    public DriverActivityStatus(int statusCode, String message, boolean is_success, int requestId) {
        super(statusCode, message, is_success);
        this.requestId = requestId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
}
