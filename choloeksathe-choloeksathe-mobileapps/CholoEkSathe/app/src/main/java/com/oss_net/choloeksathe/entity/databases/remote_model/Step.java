package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 12/23/17.
 */

public class Step {
    @SerializedName("distance")
    @Expose
    public Distance distance;
    @SerializedName("duration")
    @Expose
    public Distance duration;
    @SerializedName("end_location")
    @Expose
    public EndLocation endLocation;
    @SerializedName("html_instructions")
    @Expose
    public String htmlInstructions;
    @SerializedName("polyline")
    @Expose
    public Polyline polyline;
    @SerializedName("start_location")
    @Expose
    public StartLocation startLocation;
    @SerializedName("travel_mode")
    @Expose
    public String travelMode;
    @SerializedName("maneuver")
    @Expose
    public String maneuver;
}
