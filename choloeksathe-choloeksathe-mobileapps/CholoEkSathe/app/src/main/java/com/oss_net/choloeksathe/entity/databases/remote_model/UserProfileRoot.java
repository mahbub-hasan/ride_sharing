package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mahbubhasan on 12/24/17.
 */

public class UserProfileRoot extends BaseResponse implements Serializable {
    @SerializedName("userProfileResponse")
    @Expose
    public UserProfileResponse userProfileResponse;
}
