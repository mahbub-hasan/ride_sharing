package com.ossnetwork.choloeksatheserver.model.response;

public class CarSetupResponse extends BaseResponse {
    public int carId;

    public CarSetupResponse(int statusCode, String message, boolean is_success, int carId) {
        super(statusCode, message, is_success);
        this.carId = carId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
}
