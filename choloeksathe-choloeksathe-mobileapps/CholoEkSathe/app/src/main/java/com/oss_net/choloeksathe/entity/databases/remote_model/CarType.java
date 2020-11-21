package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 3/19/18.
 */

public class CarType {

    @SerializedName("carTypeId")
    @Expose
    private Integer carTypeId;
    @SerializedName("carTypeName")
    @Expose
    private String carTypeName;
    @SerializedName("carTypeDetails")
    @Expose
    private String carTypeDetails;
    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;
    @SerializedName("createdDate")
    @Expose
    private Object createdDate;
}
