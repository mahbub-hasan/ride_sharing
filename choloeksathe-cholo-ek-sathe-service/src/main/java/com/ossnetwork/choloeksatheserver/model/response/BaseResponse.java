package com.ossnetwork.choloeksatheserver.model.response;

public class BaseResponse {
    private int statusCode;
    private String message;
    private boolean is_success;

    public BaseResponse() {
    }

    public BaseResponse(int statusCode, String message, boolean is_success) {
        this.statusCode = statusCode;
        this.message = message;
        this.is_success = is_success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIs_success() {
        return is_success;
    }

    public void setIs_success(boolean is_success) {
        this.is_success = is_success;
    }
}
