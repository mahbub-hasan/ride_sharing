package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 12/30/17.
 */

public class PassengerHomePageRequestRoot extends BaseResponse {
    @SerializedName("passengerHomePageRequest")
    @Expose
    public PassengerHomePageRequest passengerHomePageRequest;
}
