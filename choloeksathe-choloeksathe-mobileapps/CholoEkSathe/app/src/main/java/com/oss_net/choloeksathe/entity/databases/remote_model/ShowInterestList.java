package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 1/14/18.
 */

public class ShowInterestList {
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
    @SerializedName("startPointName")
    @Expose
    public String startPointName;
    @SerializedName("stopPointName")
    @Expose
    public String stopPointName;
    @SerializedName("price")
    @Expose
    public double price;
    @SerializedName("activityStatusId")
    @Expose
    public int activityStatusId;
    @SerializedName("passengerSitRequest")
    @Expose
    public int passengerSitRequest;
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
    public String smoker;
    @SerializedName("songLover")
    @Expose
    public String songLover;
}
