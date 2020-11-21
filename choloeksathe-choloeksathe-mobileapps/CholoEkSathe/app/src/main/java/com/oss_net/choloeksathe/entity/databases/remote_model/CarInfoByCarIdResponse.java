package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mahbubhasan on 12/25/17.
 */

public class CarInfoByCarIdResponse implements Serializable {
    @SerializedName("carInfoId")
    @Expose
    public int carInfoId;
    @SerializedName("carNumber")
    @Expose
    public String carNumber;
    @SerializedName("carModels")
    @Expose
    public String carModels;
    @SerializedName("carType")
    @Expose
    public int carType;
    @SerializedName("owner")
    @Expose
    public String owner;
    @SerializedName("ownerContact")
    @Expose
    public String ownerContact;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("startDate")
    @Expose
    public long startDate;
    @SerializedName("detailsSpecification")
    @Expose
    public String detailsSpecification;
    @SerializedName("driverName")
    @Expose
    public String driverName;
    @SerializedName("driverMobile")
    @Expose
    public String driverMobile;
    @SerializedName("driverRegNo")
    @Expose
    public String driverRegNo;
    @SerializedName("ac")
    @Expose
    public boolean ac;
    @SerializedName("active")
    @Expose
    public boolean active;
    @SerializedName("sitNumber")
    public int sitNumber;
}
