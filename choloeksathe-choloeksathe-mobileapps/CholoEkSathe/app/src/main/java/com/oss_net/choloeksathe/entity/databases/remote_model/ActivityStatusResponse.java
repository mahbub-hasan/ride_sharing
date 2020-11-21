
package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivityStatusResponse {

    @SerializedName("activityStatusId")
    @Expose
    public Integer activityStatusId;
    @SerializedName("activityStatus")
    @Expose
    public String activityStatus;
    @SerializedName("active")
    @Expose
    public Boolean active;

}
