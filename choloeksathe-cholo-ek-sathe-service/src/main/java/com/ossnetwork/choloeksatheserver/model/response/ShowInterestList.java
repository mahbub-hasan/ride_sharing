package com.ossnetwork.choloeksatheserver.model.response;

import java.sql.Timestamp;

public class ShowInterestList {
    private int requestId;
    private int activityId;
    private double startLatitude;
    private double startLongitude;
    private double stopLatitude;
    private double stopLongitude;
    private String startPointName;
    private String stopPointName;
    private double price;
    private int activityStatusId;
    private int passengerSitRequest;
    private Timestamp startTime;
    private String firstName;
    private String mobileNumber;
    private String bioDescription;
    private Boolean isSmoker;
    private Boolean isSongLover;
    private String facebookLink;
    private String linkedInLink;
    private String imageLocation;
    private double passengerRating;

    public ShowInterestList() {
    }

    public ShowInterestList(int requestId, int activityId, double startLatitude, double startLongitude, double stopLatitude, double stopLongitude, String startPointName, String stopPointName, double price, int activityStatusId, int passengerSitRequest, Timestamp startTime, String firstName, String mobileNumber, String bioDescription, Boolean isSmoker, Boolean isSongLover, String facebookLink, String linkedInLink, String imageLocation, double passengerRating) {
        this.requestId = requestId;
        this.activityId = activityId;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.stopLatitude = stopLatitude;
        this.stopLongitude = stopLongitude;
        this.startPointName = startPointName;
        this.stopPointName = stopPointName;
        this.price = price;
        this.activityStatusId = activityStatusId;
        this.passengerSitRequest = passengerSitRequest;
        this.startTime = startTime;
        this.firstName = firstName;
        this.mobileNumber = mobileNumber;
        this.bioDescription = bioDescription;
        this.isSmoker = isSmoker;
        this.isSongLover = isSongLover;
        this.facebookLink = facebookLink;
        this.linkedInLink = linkedInLink;
        this.imageLocation = imageLocation;
        this.passengerRating = passengerRating;
    }

    public ShowInterestList(int activityId, double startLatitude, double startLongitude, double stopLatitude, double stopLongitude, String startPointName, String stopPointName, double price, int noofSeat, Timestamp startTime, String firstName, String mobileNumber, String imageLocation, int activityStatusId, double v) {
        this.activityId = activityId;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.stopLatitude = stopLatitude;
        this.stopLongitude = stopLongitude;
        this.startPointName = startPointName;
        this.stopPointName = stopPointName;
        this.price = price;
        this.activityStatusId = activityStatusId;
        this.startTime = startTime;
        this.firstName = firstName;
        this.mobileNumber = mobileNumber;
        this.imageLocation = imageLocation;
        this.passengerRating = v;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public double getStopLatitude() {
        return stopLatitude;
    }

    public void setStopLatitude(double stopLatitude) {
        this.stopLatitude = stopLatitude;
    }

    public double getStopLongitude() {
        return stopLongitude;
    }

    public void setStopLongitude(double stopLongitude) {
        this.stopLongitude = stopLongitude;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getBioDescription() {
        return bioDescription;
    }

    public void setBioDescription(String bioDescription) {
        this.bioDescription = bioDescription;
    }

    public Boolean getSmoker() {
        return isSmoker;
    }

    public void setSmoker(Boolean smoker) {
        isSmoker = smoker;
    }

    public Boolean getSongLover() {
        return isSongLover;
    }

    public void setSongLover(Boolean songLover) {
        isSongLover = songLover;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getLinkedInLink() {
        return linkedInLink;
    }

    public void setLinkedInLink(String linkedInLink) {
        this.linkedInLink = linkedInLink;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public int getActivityStatusId() {
        return activityStatusId;
    }

    public void setActivityStatusId(int activityStatusId) {
        this.activityStatusId = activityStatusId;
    }

    public int getPassengerSitRequest() {
        return passengerSitRequest;
    }

    public void setPassengerSitRequest(int passengerSitRequest) {
        this.passengerSitRequest = passengerSitRequest;
    }

    public String getStartPointName() {
        return startPointName;
    }

    public void setStartPointName(String startPointName) {
        this.startPointName = startPointName;
    }

    public String getStopPointName() {
        return stopPointName;
    }

    public void setStopPointName(String stopPointName) {
        this.stopPointName = stopPointName;
    }

    public double getPassengerRating() {
        return passengerRating;
    }

    public void setPassengerRating(double passengerRating) {
        this.passengerRating = passengerRating;
    }
}
