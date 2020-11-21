package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahbubhasan on 12/23/17.
 */

public class GeocodedWaypoint {
    @SerializedName("geocoder_status")
    @Expose
    public String geocoderStatus;
    @SerializedName("place_id")
    @Expose
    public String placeId;
    @SerializedName("types")
    @Expose
    public List<String> types = null;
}
