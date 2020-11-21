package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 12/23/17.
 */

public class Polyline {
    @SerializedName("points")
    @Expose
    public String points;
}
