package com.ossnetwork.choloeksatheserver.model.response;

public class PassengerHomePageRequest {
    private String fullName;
    private String imageLocation;
    private String mobileNumber;
    private double totalRating;
    private boolean active;

    public PassengerHomePageRequest(String fullName, String imageLocation, String mobileNumber, double totalRating, boolean active) {
        this.fullName = fullName;
        this.imageLocation = imageLocation;
        this.mobileNumber = mobileNumber;
        this.totalRating = totalRating;
        this.active = active;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(double totalRating) {
        this.totalRating = totalRating;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}