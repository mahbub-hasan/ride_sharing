package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 2/17/18.
 */

public class DriverDashboard {
    @SerializedName("fullName")
    @Expose
    public String fullName;
    @SerializedName("contactNumber")
    @Expose
    public String contactNumber;
    @SerializedName("imageUrl")
    @Expose
    public String imageUrl;
    @SerializedName("totalIncome")
    @Expose
    public double totalIncome;
    @SerializedName("lastRequestIncome")
    @Expose
    public double lastRequestIncome;
    @SerializedName("currentRequestIncome")
    @Expose
    public double currentRequestIncome;
    @SerializedName("totalRating")
    @Expose
    public double totalRating;
    @SerializedName("active")
    @Expose
    public boolean active;
    @SerializedName("adminActive")
    @Expose
    public boolean adminActive;
}
