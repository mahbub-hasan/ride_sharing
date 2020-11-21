package com.ossnetwork.choloeksatheserver.model.response;

import java.sql.Timestamp;

public class PassengerCarRequest {
    private double startLatitude;
    private double startLongitude;
    private double stopLatitude;
    private double stopLongitude;
    private Timestamp startTime;
    private Timestamp endTime;

    public PassengerCarRequest() {
    }

    public PassengerCarRequest(double startLatitude, double startLongitude, Timestamp startTime, Timestamp endTime) {
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public PassengerCarRequest(double startLatitude, double startLongitude, double stopLatitude, double stopLongitude, Timestamp startTime, Timestamp endTime) {
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.stopLatitude = stopLatitude;
        this.stopLongitude = stopLongitude;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
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
}
