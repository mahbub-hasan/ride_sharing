package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahbubhasan on 12/26/17.
 */

public class PassengerHistoryRoot extends BaseResponse {
    @SerializedName("passengerHistories")
    @Expose
    public List<PassengerHistory> passengerHistories = new ArrayList<>();
}
