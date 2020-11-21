package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 12/8/17.
 */

public class CarSetupRoot extends BaseResponse {
    @SerializedName("carId")
    @Expose
    public Integer carId;
}
