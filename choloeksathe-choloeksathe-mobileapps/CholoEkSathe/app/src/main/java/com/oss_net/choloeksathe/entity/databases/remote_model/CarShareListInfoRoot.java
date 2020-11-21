package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahbubhasan on 12/11/17.
 */

public class CarShareListInfoRoot extends BaseResponse {
    @SerializedName("showInterestLists")
    @Expose
    public List<CarShareListInfo> carShareListInfos = new ArrayList<>();
}
