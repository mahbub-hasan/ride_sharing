package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mahbubhasan on 12/12/17.
 */

public class PassengerAvailableCar implements Serializable{
    @SerializedName("requestId")
    @Expose
    public int requestId;
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
    @SerializedName("startPlace")
    @Expose
    public String startPlace;
    @SerializedName("endPlace")
    @Expose
    public String endPlace;
    @SerializedName("price")
    @Expose
    public double price;
    @SerializedName("noofFreeSeat")
    @Expose
    public int noofFreeSeat;
    @SerializedName("stopOver")
    @Expose
    public boolean stopOver;
    @SerializedName("startTime")
    @Expose
    public long startTime;
    @SerializedName("returnTime")
    @Expose
    public long returnTime;
    @SerializedName("luggageSpaceAvailable")
    @Expose
    public boolean luggageSpaceAvailable;
    @SerializedName("smokingAllowed")
    @Expose
    public boolean smokingAllowed;
    @SerializedName("outSideFoodAllowed")
    @Expose
    public boolean outSideFoodAllowed;
    @SerializedName("musicAllowed")
    @Expose
    public boolean musicAllowed;
    @SerializedName("petsAllowed")
    @Expose
    public boolean petsAllowed;
    @SerializedName("genderRestriction")
    @Expose
    public boolean genderRestriction;
    @SerializedName("requestDate")
    @Expose
    public String requestDate;
    @SerializedName("requestType")
    @Expose
    public String requestType;
    @SerializedName("requestStatus")
    @Expose
    public String requestStatus;
    @SerializedName("journeyType")
    @Expose
    public String journeyType;
    @SerializedName("driverId")
    @Expose
    public int driverId;
    @SerializedName("driverPic")
    @Expose
    public String driverPic;
    @SerializedName("carId")
    @Expose
    public int carId;
    @SerializedName("carPic")
    @Expose
    public String carPic;
    @SerializedName("carNumber")
    @Expose
    public String carNumber;
    @SerializedName("stopOvers")
    @Expose
    public List<StopOverList> stopOvers = null;
    @SerializedName("distance")
    @Expose
    public double distance;
    @SerializedName("round")
    @Expose
    public boolean round;
    @SerializedName("driverName")
    @Expose
    public String driverName;
    @SerializedName("driverMobileNumber")
    @Expose
    public String driverMobileNumber;

    @SerializedName("totalRating")
    @Expose
    public double totalRating;
}
