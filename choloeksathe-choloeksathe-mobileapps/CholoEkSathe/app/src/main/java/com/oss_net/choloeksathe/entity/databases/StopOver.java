package com.oss_net.choloeksathe.entity.databases;

/**
 * Created by mahbubhasan on 1/2/18.
 */

public class StopOver {
    private double startPointLat;
    private double startPointLan;
    private double stopPointLat;
    private double stopPointLan;
    private String startPointName;
    private String stopPointName;

    public StopOver(double startPointLat, double startPointLan, double stopPointLat, double stopPointLan, String startPointName, String stopPointName) {
        this.startPointLat = startPointLat;
        this.startPointLan = startPointLan;
        this.stopPointLat = stopPointLat;
        this.stopPointLan = stopPointLan;
        this.startPointName = startPointName;
        this.stopPointName = stopPointName;
    }

    public double getStartPointLat() {
        return startPointLat;
    }

    public void setStartPointLat(double startPointLat) {
        this.startPointLat = startPointLat;
    }

    public double getStartPointLan() {
        return startPointLan;
    }

    public void setStartPointLan(double startPointLan) {
        this.startPointLan = startPointLan;
    }

    public double getStopPointLat() {
        return stopPointLat;
    }

    public void setStopPointLat(double stopPointLat) {
        this.stopPointLat = stopPointLat;
    }

    public double getStopPointLan() {
        return stopPointLan;
    }

    public void setStopPointLan(double stopPointLan) {
        this.stopPointLan = stopPointLan;
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
