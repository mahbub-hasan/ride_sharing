package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 12/10/17.
 */

public class NotificationsRoot {

    @SerializedName("activity_id")
    public int activity_id;
    @SerializedName("request_id")
    public int request_id;
    @SerializedName("passenger_id")
    public int passenger_id;
    @SerializedName("details")
    public String details;
    @SerializedName("type")
    public String type;
    @SerializedName("driver_pic")
    public String driver_pic;
}
