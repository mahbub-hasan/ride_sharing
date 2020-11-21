package com.ossnetwork.choloeksatheserver.model.response;

import java.util.List;

public class DriverCarShareHistoryRoot extends BaseResponse {
    private List<DriverCarShareHistory> carShareHistories;

    public DriverCarShareHistoryRoot(int statusCode, String message, boolean is_success, List<DriverCarShareHistory> carShareHistories) {
        super(statusCode, message, is_success);
        this.carShareHistories = carShareHistories;
    }

    public List<DriverCarShareHistory> getCarShareHistories() {
        return carShareHistories;
    }

    public void setCarShareHistories(List<DriverCarShareHistory> carShareHistories) {
        this.carShareHistories = carShareHistories;
    }
}
