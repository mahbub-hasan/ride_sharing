package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 1/14/18.
 */

public class StopOverInfo {
    @SerializedName("stopOverId")
    @Expose
    public int stopOverId;
    @SerializedName("startLatitude")
    @Expose
    public double startLatitude;
    @SerializedName("startLongitude")
    @Expose
    public double startLongitude;
    @SerializedName("stopLatitude")
    @Expose
    public double stopLatitude;
    @SerializedName("stopLongitude")
    @Expose
    public double stopLongitude;
    @SerializedName("startPointName")
    @Expose
    public String startPointName;
    @SerializedName("stopPointName")
    @Expose
    public String stopPointName;
}
