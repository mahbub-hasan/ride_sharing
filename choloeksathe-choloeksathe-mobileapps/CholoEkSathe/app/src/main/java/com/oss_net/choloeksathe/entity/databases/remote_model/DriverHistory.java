package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 2/12/18.
 */

public class DriverHistory {
    @SerializedName("requestId")
    @Expose
    public int requestId;
    @SerializedName("shortStartPlace")
    @Expose
    public String shortStartPlace;
    @SerializedName("shortEndPlace")
    @Expose
    public String shortEndPlace;
    @SerializedName("startTime")
    @Expose
    public long startTime;
    @SerializedName("requestDate")
    @Expose
    public String requestDate;
}
