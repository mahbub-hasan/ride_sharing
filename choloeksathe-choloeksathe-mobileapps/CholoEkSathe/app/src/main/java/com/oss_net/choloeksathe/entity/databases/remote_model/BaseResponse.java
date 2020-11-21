package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mahbubhasan on 12/6/17.
 */

public class BaseResponse implements Serializable{
    @SerializedName("statusCode")
    public int statusCode;

    @SerializedName("message")
    public String message;

    @SerializedName("is_success")
    public boolean isSuccess;
}
