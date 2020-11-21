package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahbubhasan on 3/19/18.
 */

public class CarTypeListRoot extends BaseResponse{
    @SerializedName("carTypeList")
    @Expose
    public List<CarTypeList> carTypeList = new ArrayList<>();
}
