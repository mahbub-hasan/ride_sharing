
package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestStatusInfoResponse {

    @SerializedName("requestStatusId")
    @Expose
    public Integer requestStatusId;
    @SerializedName("requestStatus")
    @Expose
    public String requestStatus;
    @SerializedName("active")
    @Expose
    public Boolean active;

}
