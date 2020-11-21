
package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCategoryResponse {

    @SerializedName("userCategoryId")
    @Expose
    public Integer userCategoryId;
    @SerializedName("userCategory")
    @Expose
    public String userCategory;
    @SerializedName("active")
    @Expose
    public Boolean active;

}
