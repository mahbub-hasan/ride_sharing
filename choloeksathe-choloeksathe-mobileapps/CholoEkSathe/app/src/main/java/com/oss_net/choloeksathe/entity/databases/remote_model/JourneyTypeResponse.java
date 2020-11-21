
package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JourneyTypeResponse {

    @SerializedName("journeyTypeId")
    @Expose
    public Integer journeyTypeId;
    @SerializedName("journeyType")
    @Expose
    public String journeyType;
    @SerializedName("active")
    @Expose
    public Boolean active;

}
