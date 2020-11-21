package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 2/11/18.
 */

public class DriverActiveSession {
    @SerializedName("statusCode")
    @Expose
    public int statusCode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("is_success")
    @Expose
    public boolean isSuccess;
    @SerializedName("requestId")
    @Expose
    public int requestId;
}
