package com.ossnetwork.choloeksatheserver.model.response;

public class CarInfoByCarIdResponseRoot extends BaseResponse {
    private CarInfoByCarIdResponse carInfoByCarIdResponse;

    public CarInfoByCarIdResponseRoot(int statusCode, String message, boolean is_success, CarInfoByCarIdResponse carInfoByCarIdResponse) {
        super(statusCode, message, is_success);
        this.carInfoByCarIdResponse = carInfoByCarIdResponse;
    }

    public CarInfoByCarIdResponse getCarInfoByCarIdResponse() {
        return carInfoByCarIdResponse;
    }

    public void setCarInfoByCarIdResponse(CarInfoByCarIdResponse carInfoByCarIdResponse) {
        this.carInfoByCarIdResponse = carInfoByCarIdResponse;
    }
}
