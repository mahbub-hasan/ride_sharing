package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 2/10/18.
 */

public class PassengerActiveSession {

    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("is_success")
    @Expose
    public Boolean isSuccess;
    @SerializedName("activityId")
    @Expose
    public Integer activityId;
}
