package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 12/11/17.
 */

public class CarShareListInfo {
    @SerializedName("requestId")
    @Expose
    public int requestId;
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
    @SerializedName("startTime")
    @Expose
    public long startTime;
    @SerializedName("firstName")
    @Expose
    public String firstName;
    @SerializedName("mobileNumber")
    @Expose
    public String mobileNumber;
    @SerializedName("bioDescription")
    @Expose
    public String bioDescription;
    @SerializedName("facebookLink")
    @Expose
    public String facebookLink;
    @SerializedName("linkedInLink")
    @Expose
    public String linkedInLink;
    @SerializedName("imageLocation")
    @Expose
    public String imageLocation;
    @SerializedName("smoker")
    @Expose
    public boolean smoker;
    @SerializedName("songLover")
    @Expose
    public boolean songLover;
    @SerializedName("activityStatusId")
    @Expose
    public int activityStatusId;
    @SerializedName("passengerSitRequest")
    @Expose
    public int passengerSitRequest;
    @SerializedName("startPointName")
    public String startLocationName;
    @SerializedName("stopPointName")
    public String stopLocationName;
    @SerializedName("totalRating")
    @Expose
    public double totalRating;
}
