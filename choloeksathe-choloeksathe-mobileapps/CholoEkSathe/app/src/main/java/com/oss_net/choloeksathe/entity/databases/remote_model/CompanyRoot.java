package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahbubhasan on 1/10/18.
 */

public class CompanyRoot extends BaseResponse{
    @SerializedName("companyList")
    @Expose
    public List<CompanyList> companyList = new ArrayList<>();
}
