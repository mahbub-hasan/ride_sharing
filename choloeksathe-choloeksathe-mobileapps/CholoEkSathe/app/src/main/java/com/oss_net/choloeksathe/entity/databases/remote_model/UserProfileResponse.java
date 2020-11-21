package com.oss_net.choloeksathe.entity.databases.remote_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mahbubhasan on 12/24/17.
 */

public class UserProfileResponse implements Serializable{
    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("firstName")
    @Expose
    public String firstName;
    @SerializedName("userName")
    @Expose
    public String email;
    @SerializedName("mobileNumber")
    @Expose
    public String mobileNumber;
    @SerializedName("nid")
    @Expose
    public String nid;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("dob")
    @Expose
    public long dob;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("homeLatitude")
    @Expose
    public double homeLatitude;
    @SerializedName("homeLongitude")
    @Expose
    public double homeLongitude;
    @SerializedName("facebookLink")
    @Expose
    public String facebookLink;
    @SerializedName("imageLocation")
    @Expose
    public String imageLocation;
    @SerializedName("companyName")
    @Expose
    public String companyName;
    @SerializedName("emailVerified")
    @Expose
    public boolean emailVerified;
    @SerializedName("mobileVerified")
    @Expose
    public boolean mobileVerified;

}
