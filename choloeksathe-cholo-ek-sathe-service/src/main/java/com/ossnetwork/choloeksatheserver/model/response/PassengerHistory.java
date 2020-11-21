package com.ossnetwork.choloeksatheserver.model.response;

import java.sql.Timestamp;
import java.util.Date;

public class PassengerHistory {
    private int activityId;
    private double startLatitude;
    private double startLongitude;
    private double stopLatitude;
    private double stopLongitude;
    private String startPointName;
    private String stopPointName;
    private double price;
    private int noofSeat;
    private Timestamp startTime;
    private int driverUserId;
    private String driverFullName;
    private String driverPicture;
    private Date requestDate;
    private double driverRating;

    public PassengerHistory() {
    }


    public PassengerHistory(int activityId, double startLatitude, double startLongitude, double stopLatitude, double stopLongitude, double price, int noofSeat, Timestamp startTime, int driverUserId, String driverFullName, String driverPicture, Date requestDate) {
        this.activityId = activityId;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.stopLatitude = stopLatitude;
        this.stopLongitude = stopLongitude;
        this.price = price;
        this.noofSeat = noofSeat;
        this.startTime = startTime;
        this.driverUserId = driverUserId;
        this.driverFullName = driverFullName;
        this.driverPicture = driverPicture;
        this.requestDate = requestDate;
    }

    public PassengerHistory(int activityId, double startLatitude, double startLongitude, double stopLatitude, double stopLongitude, String startPointName, String stopPointName, double price, int noofSeat, Timestamp startTime, int driverUserId, String driverFullName, String driverPicture, Date requestDate) {
        this.activityId = activityId;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.stopLatitude = stopLatitude;
        this.stopLongitude = stopLongitude;
        this.startPointName = startPointName;
        this.stopPointName = stopPointName;
        this.price = price;
        this.noofSeat = noofSeat;
        this.startTime = startTime;
        this.driverUserId = driverUserId;
        this.driverFullName = driverFullName;
        this.driverPicture = driverPicture;
        this.requestDate = requestDate;
    }

    public PassengerHistory(int activityId, double startLatitude, double startLongitude, double stopLatitude, double stopLongitude, String startPointName, String stopPointName, double price, int noofSeat, Timestamp startTime, int driverUserId, String driverFullName, String driverPicture, Date requestDate, double driverRating) {
        this.activityId = activityId;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.stopLatitude = stopLatitude;
        this.stopLongitude = stopLongitude;
        this.startPointName = startPointName;
        this.stopPointName = stopPointName;
        this.price = price;
        this.noofSeat = noofSeat;
        this.startTime = startTime;
        this.driverUserId = driverUserId;
        this.driverFullName = driverFullName;
        this.driverPicture = driverPicture;
        this.requestDate = requestDate;
        this.driverRating = driverRating;
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

    public String getDriverPicture() {
        return driverPicture;
    }

    public void setDriverPicture(String driverPicture) {
        this.driverPicture = driverPicture;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
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

    public double getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(double driverRating) {
        this.driverRating = driverRating;
    }
}
