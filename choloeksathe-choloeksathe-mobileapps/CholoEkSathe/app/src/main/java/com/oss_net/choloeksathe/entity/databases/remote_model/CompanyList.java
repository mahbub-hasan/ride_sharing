package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahbubhasan on 1/10/18.
 */

public class CompanyList {
    @SerializedName("companyId")
    @Expose
    public Integer companyId;
    @SerializedName("companyName")
    @Expose
    public String companyName;
    @SerializedName("companyDetails")
    @Expose
    public String companyDetails;
}
