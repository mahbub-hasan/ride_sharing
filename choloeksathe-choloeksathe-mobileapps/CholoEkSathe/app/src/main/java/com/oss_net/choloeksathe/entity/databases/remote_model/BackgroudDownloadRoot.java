
package com.oss_net.choloeksathe.entity.databases.remote_model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BackgroudDownloadRoot {

    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("is_success")
    @Expose
    public Boolean isSuccess;
    @SerializedName("userCategoryResponses")
    @Expose
    public List<UserCategoryResponse> userCategoryResponses = null;
    @SerializedName("requestStatusInfoResponses")
    @Expose
    public List<RequestStatusInfoResponse> requestStatusInfoResponses = null;
    @SerializedName("requestTypeResponses")
    @Expose
    public List<RequestTypeResponse> requestTypeResponses = null;
    @SerializedName("journeyTypeResponses")
    @Expose
    public List<JourneyTypeResponse> journeyTypeResponses = null;
    @SerializedName("activityStatusResponses")
    @Expose
    public List<ActivityStatusResponse> activityStatusResponses = null;

}
