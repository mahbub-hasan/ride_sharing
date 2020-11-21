package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 12/18/17.
 */

public class PassengerCurrentSession extends BaseResponse {
    @SerializedName("activityId")
    @Expose
    public int activityId;
    @SerializedName("price")
    @Expose
    public int price;
    @SerializedName("noofSeat")
    @Expose
    public int noofSeat;
    @SerializedName("startTime")
    @Expose
    public long startTime;
    @SerializedName("activityStatus")
    @Expose
    public String activityStatus;
    @SerializedName("driverUserId")
    @Expose
    public int driverUserId;
    @SerializedName("driverFullName")
    @Expose
    public String driverFullName;
    @SerializedName("driverProfilePicture")
    @Expose
    public String driverProfilePicture;
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
    public String startLocationName;
    @SerializedName("stopPointName")
    @Expose
    public String stopLocationName;
    @SerializedName("driverMobileNumber")
    @Expose
    public String driverMobileNumber;

    @SerializedName("totalRating")
    @Expose
    public double totalRating;
}
