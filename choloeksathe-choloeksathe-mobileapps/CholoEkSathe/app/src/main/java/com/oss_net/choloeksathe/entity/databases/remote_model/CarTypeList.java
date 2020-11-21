package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 3/19/18.
 */

public class CarTypeList {
    @SerializedName("carTypeId")
    @Expose
    public int carTypeId;
    @SerializedName("carTypeName")
    @Expose
    public String carTypeName;
    @SerializedName("carTypeDetails")
    @Expose
    public String carTypeDetails;
    @SerializedName("createdBy")
    @Expose
    public Integer createdBy;
    @SerializedName("createdDate")
    @Expose
    public String createdDate;
}
