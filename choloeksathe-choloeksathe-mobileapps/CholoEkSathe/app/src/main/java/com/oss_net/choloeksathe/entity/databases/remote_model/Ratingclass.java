package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 3/5/18.
 */

public class Ratingclass {
    @SerializedName("ratingPoint")
    @Expose
    private Integer ratingPoint;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("ratingDate")
    @Expose
    private String ratingDate;
    @SerializedName("activityInfoByActivityId")
    @Expose
    private ActivityInfoByActivityId activityInfoByActivityId;
    @SerializedName("userInfoByRatingBy")
    @Expose
    private UserInfoByRatingBy userInfoByRatingBy;
}
