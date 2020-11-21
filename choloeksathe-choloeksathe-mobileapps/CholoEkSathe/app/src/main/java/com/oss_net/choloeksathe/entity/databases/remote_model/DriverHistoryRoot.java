package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2/12/18.
 */

public class DriverHistoryRoot extends BaseResponse {
    @SerializedName("carShareHistories")
    @Expose
    public List<DriverHistory> driverHistoryList = new ArrayList<>();
}
