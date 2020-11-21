package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahbubhasan on 12/12/17.
 */

public class PassengerAvailableCarRoot extends BaseResponse {
    @SerializedName("passengerCarRequestResponses")
    @Expose
    public List<PassengerAvailableCar> passengerAvailableCarList = new ArrayList<>();
}
