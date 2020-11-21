package com.ossnetwork.choloeksatheserver.model.response;

public class CarShareResponse extends BaseResponse{

    private int requestId;

    public CarShareResponse(int statusCode, String message, boolean is_success, int requestId) {
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
