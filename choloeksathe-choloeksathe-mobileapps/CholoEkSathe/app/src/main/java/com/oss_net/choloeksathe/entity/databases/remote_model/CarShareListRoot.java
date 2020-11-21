package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahbubhasan on 12/11/17.
 */

public class CarShareListRoot extends BaseResponse {
    @SerializedName("responses")
    public List<CarShareList> responses = new ArrayList<>();
}
