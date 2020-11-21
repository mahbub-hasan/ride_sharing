package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mahbubhasan on 12/11/17.
 */

public class StopOverList implements Serializable{
    @SerializedName("stopOverId")
    public int stopOverId;
    @SerializedName("startLatitude")
    public double startLatitude;
    @SerializedName("startLongitude")
    public double startLongitude;
    @SerializedName("stopLatitude")
    public double stopLatitude;
    @SerializedName("stopLongitude")
    public double stopLongitude;
    @SerializedName("startPointName")
    public String startLocationName;
    @SerializedName("stopPointName")
    public String stopLocationName;
}
