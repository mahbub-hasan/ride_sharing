package com.ossnetwork.choloeksatheserver.model.response;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserProfileResponse {
    private int userId;
    private String firstName;
    private String userName;
    private String mobileNumber;
    private String nid;
    private String gender;
    private Timestamp dob;
    private String address;
    private Double homeLatitude;
    private Double homeLongitude;
    private String facebookLink;
    private String imageLocation;
    private String companyName;
    private boolean isEmailVerified;
    private boolean isMobileVerified;

    public UserProfileResponse() {
    }

    public UserProfileResponse(int userId, String firstName, String userName, String mobileNumber, String nid, String gender, Timestamp dob, String address, Double homeLatitude, Double homeLongitude, String facebookLink, String imageLocation, String companyName, boolean emailVerified, boolean mobileVerified) {
        this.userId = userId;
        this.firstName = firstName;
        this.userName = userName;
        this.mobileNumber = mobileNumber;
        this.nid = nid;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.homeLatitude = homeLatitude;
        this.homeLongitude = homeLongitude;
        this.facebookLink = facebookLink;
        this.imageLocation = imageLocation;
        this.companyName = companyName;
        this.isEmailVerified = emailVerified;
        this.isMobileVerified = mobileVerified;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Timestamp getDob() {
        return dob;
    }

    public void setDob(Timestamp dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getHomeLatitude() {
        return homeLatitude;
    }

    public void setHomeLatitude(Double homeLatitude) {
        this.homeLatitude = homeLatitude;
    }

    public Double getHomeLongitude() {
        return homeLongitude;
    }

    public void setHomeLongitude(Double homeLongitude) {
        this.homeLongitude = homeLongitude;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public boolean isMobileVerified() {
        return isMobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        isMobileVerified = mobileVerified;
    }
}
