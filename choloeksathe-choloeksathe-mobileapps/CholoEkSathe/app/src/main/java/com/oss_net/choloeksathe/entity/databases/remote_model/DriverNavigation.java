package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahbubhasan on 1/14/18.
 */

public class DriverNavigation {
    @SerializedName("statusCode")
    @Expose
    public int statusCode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("is_success")
    @Expose
    public boolean isSuccess;
    @SerializedName("requestID")
    @Expose
    public int requestID;
    @SerializedName("startLocationLat")
    @Expose
    public double startLocationLat;
    @SerializedName("startLocationLan")
    @Expose
    public double startLocationLan;
    @SerializedName("stopLocationLat")
    @Expose
    public double stopLocationLat;
    @SerializedName("stopLocationLan")
    @Expose
    public double stopLocationLan;
    @SerializedName("startTime")
    @Expose
    public long startTime;
    @SerializedName("stopOverInfos")
    @Expose
    public List<StopOverInfo> stopOverInfos = null;
    @SerializedName("showInterestLists")
    @Expose
    public List<CarShareListInfo> showInterestLists = null;
    @SerializedName("requestStatusCode")
    @Expose
    public int requestStatusCode;
    //public List<ShowInterestList> showInterestLists = null;
}
