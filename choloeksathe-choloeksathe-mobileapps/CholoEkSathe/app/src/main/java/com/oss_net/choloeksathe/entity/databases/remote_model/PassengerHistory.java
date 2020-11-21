package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 12/26/17.
 */

public class PassengerHistory {
    @SerializedName("activityId")
    @Expose
    public int activityId;
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
    @SerializedName("price")
    @Expose
    public double price;
    @SerializedName("noofSeat")
    @Expose
    public int noofSeat;
    @SerializedName("startTime")
    @Expose
    public long startTime;
    @SerializedName("driverUserId")
    @Expose
    public int driverUserId;
    @SerializedName("driverFullName")
    @Expose
    public String driverFullName;
    @SerializedName("driverPicture")
    @Expose
    public String driverPicture;
    @SerializedName("requestDate")
    @Expose
    public String requestDate;
    @SerializedName("startPointName")
    public String startLocationName;
    @SerializedName("stopPointName")
    public String stopLocationName;
    @SerializedName("totalRating")
    @Expose
    public double totalRating;

}
