package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 12/30/17.
 */

public class PassengerHomePageRequest {
    @SerializedName("fullName")
    @Expose
    public String fullName;
    @SerializedName("imageLocation")
    @Expose
    public String imageLocation;
    @SerializedName("mobileNumber")
    @Expose
    public String mobileNumber;
    @SerializedName("activityId")
    @Expose
    public int activityId;
    @SerializedName("totalRating")
    @Expose
    public double totalRating;
    @SerializedName("active")
    @Expose
    public boolean active;
}
