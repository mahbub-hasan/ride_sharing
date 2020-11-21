package com.ossnetwork.choloeksatheserver.model.response;

import java.util.List;

public class PassengerHistoryRoot extends BaseResponse{
    public List<PassengerHistory> passengerHistories;

    public PassengerHistoryRoot() {
    }

    public PassengerHistoryRoot(int statusCode, String message, boolean is_success) {
        super(statusCode, message, is_success);
    }

    public PassengerHistoryRoot(int statusCode, String message, boolean is_success, List<PassengerHistory> passengerHistories) {
        super(statusCode, message, is_success);
        this.passengerHistories = passengerHistories;
    }

    public List<PassengerHistory> getPassengerHistories() {
        return passengerHistories;
    }

    public void setPassengerHistories(List<PassengerHistory> passengerHistories) {
        this.passengerHistories = passengerHistories;
    }
}
