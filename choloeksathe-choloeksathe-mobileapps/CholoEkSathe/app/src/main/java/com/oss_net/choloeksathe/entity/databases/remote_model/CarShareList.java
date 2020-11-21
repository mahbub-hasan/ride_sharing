package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahbubhasan on 12/11/17.
 */

public class CarShareList {
    @SerializedName("requestId")
    @Expose
    public int requestId;
    @SerializedName("startTime")
    @Expose
    public long startTime;
    @SerializedName("returnTime")
    @Expose
    public long returnTime;
    @SerializedName("requestType")
    @Expose
    public String requestType;
    @SerializedName("requestStatus")
    @Expose
    public String requestStatus;
    @SerializedName("journeyType")
    @Expose
    public String journeyType;
    @SerializedName("stopOverListResponses")
    @Expose
    public List<StopOverList> stopOverListResponses = new ArrayList<>();
    @SerializedName("active")
    @Expose
    public Boolean active;
    @SerializedName("sourceLocation")
    @Expose
    public String sourceLocation;
    @SerializedName("destinationLocation")
    @Expose
    public String destinationLocation;
    @SerializedName("driveDate")
    @Expose
    public String driveDate;
}
