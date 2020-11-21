package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2/12/18.
 */

public class DriverHistorymodel {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("is_success")
    @Expose
    private Boolean isSuccess;
    @SerializedName("carShareHistories")
    @Expose
    private List<DriverHistory> carShareHistories = new ArrayList<>();

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public List<DriverHistory> getCarShareHistories() {
        return carShareHistories;
    }

    public void setCarShareHistories(List<DriverHistory> carShareHistories) {
        this.carShareHistories = carShareHistories;
    }

}
