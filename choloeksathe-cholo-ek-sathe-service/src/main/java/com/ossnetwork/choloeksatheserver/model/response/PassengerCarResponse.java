package com.ossnetwork.choloeksatheserver.model.response;

import java.util.List;

public class PassengerCarResponse extends BaseResponse{
    List<PassengerCarRequestResponse> passengerCarRequestResponses;

    public PassengerCarResponse() {
        super();
    }

    public PassengerCarResponse(int statusCode, String message, boolean is_success) {
        super(statusCode, message, is_success);
    }

    public PassengerCarResponse(int statusCode, String message, boolean is_success, List<PassengerCarRequestResponse> passengerCarRequestResponses) {
        super(statusCode, message, is_success);
        this.passengerCarRequestResponses = passengerCarRequestResponses;
    }

    public List<PassengerCarRequestResponse> getPassengerCarRequestResponses() {
        return passengerCarRequestResponses;
    }

    public void setPassengerCarRequestResponses(List<PassengerCarRequestResponse> passengerCarRequestResponses) {
        this.passengerCarRequestResponses = passengerCarRequestResponses;
    }
}
