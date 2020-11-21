package com.ossnetwork.choloeksatheserver.model.response;

public class DriverStopOver {
    private int stopOverId;
    private double startLatitude;
    private double startLongitude;
    private double stopLatitude;
    private double stopLongitude;
    private String startPointName;
    private String stopPointName;

    public DriverStopOver() {
    }

    public DriverStopOver(int stopOverId, double startLatitude, double startLongitude, double stopLatitude, double stopLongitude) {
        this.stopOverId = stopOverId;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.stopLatitude = stopLatitude;
        this.stopLongitude = stopLongitude;
    }

    public DriverStopOver(int stopOverId, double startLatitude, double startLongitude, double stopLatitude, double stopLongitude, String startPointName, String stopPointName) {
        this.stopOverId = stopOverId;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.stopLatitude = stopLatitude;
        this.stopLongitude = stopLongitude;
        this.startPointName = startPointName;
        this.stopPointName = stopPointName;
    }

    public int getStopOverId() {
        return stopOverId;
    }

    public void setStopOverId(int stopOverId) {
        this.stopOverId = stopOverId;
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
}
