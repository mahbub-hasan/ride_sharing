
package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestTypeResponse {

    @SerializedName("requestTypeId")
    @Expose
    public Integer requestTypeId;
    @SerializedName("requestType")
    @Expose
    public String requestType;
    @SerializedName("active")
    @Expose
    public Boolean active;

}
