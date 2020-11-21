package com.ossnetwork.choloeksatheserver.model.response;

import com.ossnetwork.choloeksatheserver.model.CarType;

import java.util.List;

public class CarTypeList extends BaseResponse{

    private List<CarType> carTypeList;

    public CarTypeList(int statusCode, String message, boolean is_success, List<CarType> carTypeList) {
        super(statusCode, message, is_success);
        this.carTypeList = carTypeList;
    }

    public CarTypeList(int statusCode, String message, boolean is_success) {
        super(statusCode, message, is_success);
    }

    public List<CarType> getCarTypeList() {
        return carTypeList;
    }

    public void setCarTypeList(List<CarType> carTypeList) {
        this.carTypeList = carTypeList;
    }
}
