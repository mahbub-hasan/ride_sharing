package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 12/7/17.
 */

public class LoginResponse extends BaseResponse {
    @SerializedName("userId")
    public int userId;
    @SerializedName("carID")
    public int carID;
    @SerializedName("userCategoryId")
    public int userCategoryId;
    @SerializedName("emailVerified")
    public boolean isEmailVerified;
    @SerializedName("requestId")
    public int requestId;
    @SerializedName("activityId")
    public int activityId;
    @SerializedName("mobileVerified")
    public boolean isMobileVerified;
    @SerializedName("profileApprovedByAdmin")
    public boolean isProfileApprovedByAdmin;
    @SerializedName("active")
    public boolean isActive;
    @SerializedName("numberOfSit")
    public int numberOfSit;
}
