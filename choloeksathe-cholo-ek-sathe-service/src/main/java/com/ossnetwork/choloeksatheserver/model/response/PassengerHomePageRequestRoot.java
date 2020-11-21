package com.ossnetwork.choloeksatheserver.model.response;

public class PassengerHomePageRequestRoot  extends BaseResponse{

    private PassengerHomePageRequest passengerHomePageRequest;

    public PassengerHomePageRequestRoot(int statusCode, String message, boolean is_success) {
        super(statusCode, message, is_success);
    }

    public PassengerHomePageRequestRoot(int statusCode, String message, boolean is_success, PassengerHomePageRequest passengerHomePageRequest) {
        super(statusCode, message, is_success);
        this.passengerHomePageRequest = passengerHomePageRequest;
    }

    public PassengerHomePageRequest getPassengerHomePageRequest() {
        return passengerHomePageRequest;
    }

    public void setPassengerHomePageRequest(PassengerHomePageRequest passengerHomePageRequest) {
        this.passengerHomePageRequest = passengerHomePageRequest;
    }
}
