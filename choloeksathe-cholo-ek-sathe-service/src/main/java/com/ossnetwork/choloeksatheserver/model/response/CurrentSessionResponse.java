package com.ossnetwork.choloeksatheserver.model.response;

import java.sql.Timestamp;

public class CurrentSessionResponse extends BaseResponse{
    private int activityId;
    private double price;
    private int noofSeat;
    private Timestamp startTime;
    private String activityStatus;
    private int driverUserId;
    private String driverFullName;
    private String driverProfilePicture;
    private String driverMobileNumber;
    private double startLatitude;
    private double startLongitude;
    private double stopLatitude;
    private double stopLongitude;
    private String startPointName;
    private String stopPointName;

    public CurrentSessionResponse() {
    }

    public CurrentSessionResponse(int statusCode, String message, boolean is_success) {
        super(statusCode, message, is_success);
    }

    public CurrentSessionResponse(int statusCode, String message, boolean is_success, int activityId, double price, int noofSeat, Timestamp startTime, String activityStatus, int driverUserId, String driverFullName, String driverProfilePicture, String driverMobileNumber, double startLatitude, double startLongitude, double stopLatitude, double stopLongitude, String startPointName, String stopPointName) {
        super(statusCode, message, is_success);
        this.activityId = activityId;
        this.price = price;
        this.noofSeat = noofSeat;
        this.startTime = startTime;
        this.activityStatus = activityStatus;
        this.driverUserId = driverUserId;
        this.driverFullName = driverFullName;
        this.driverProfilePicture = driverProfilePicture;
        this.driverMobileNumber = driverMobileNumber;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.stopLatitude = stopLatitude;
        this.stopLongitude = stopLongitude;
        this.startPointName = startPointName;
        this.stopPointName = stopPointName;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNoofSeat() {
        return noofSeat;
    }

    public void setNoofSeat(int noofSeat) {
        this.noofSeat = noofSeat;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public int getDriverUserId() {
        return driverUserId;
    }

    public void setDriverUserId(int driverUserId) {
        this.driverUserId = driverUserId;
    }

    public String getDriverFullName() {
        return driverFullName;
    }

    public void setDriverFullName(String driverFullName) {
        this.driverFullName = driverFullName;
    }

    public String getDriverProfilePicture() {
        return driverProfilePicture;
    }

    public void setDriverProfilePicture(String driverProfilePicture) {
        this.driverProfilePicture = driverProfilePicture;
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

    public String getDriverMobileNumber() {
        return driverMobileNumber;
    }

    public void setDriverMobileNumber(String driverMobileNumber) {
        this.driverMobileNumber = driverMobileNumber;
    }
}
