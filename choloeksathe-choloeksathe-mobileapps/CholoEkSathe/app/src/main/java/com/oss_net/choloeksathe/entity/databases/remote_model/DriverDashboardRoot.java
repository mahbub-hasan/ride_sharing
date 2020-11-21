package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 2/17/18.
 */

public class DriverDashboardRoot {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("is_success")
    @Expose
    public Boolean isSuccess;
    @SerializedName("driverDashboard")
    @Expose
    public DriverDashboard driverDashboard;
}
