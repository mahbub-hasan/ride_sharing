package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 3/19/18.
 */

public class ActiveSessionResponse extends BaseResponse {
    @SerializedName("activityStatus")
    public int activityStatus;
}
