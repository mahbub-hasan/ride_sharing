package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahbubhasan on 12/23/17.
 */

public class Leg {
    @SerializedName("distance")
    @Expose
    public Distance distance;
    @SerializedName("duration")
    @Expose
    public Duration duration;
    @SerializedName("end_address")
    @Expose
    public String endAddress;
    @SerializedName("end_location")
    @Expose
    public EndLocation endLocation;
    @SerializedName("start_address")
    @Expose
    public String startAddress;
    @SerializedName("start_location")
    @Expose
    public StartLocation startLocation;
    @SerializedName("steps")
    @Expose
    public List<Step> steps = null;
    @SerializedName("traffic_speed_entry")
    @Expose
    public List<Object> trafficSpeedEntry = null;
    @SerializedName("via_waypoint")
    @Expose
    public List<Object> viaWaypoint = null;
}
