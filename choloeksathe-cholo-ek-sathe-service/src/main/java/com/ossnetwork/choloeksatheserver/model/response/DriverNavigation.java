package com.ossnetwork.choloeksatheserver.model.response;

import java.sql.Timestamp;
import java.util.Collection;

public class DriverNavigation extends BaseResponse{
    private int requestID;
    private double startLocationLat;
    private double startLocationLan;
    private double stopLocationLat;
    private double stopLocationLan;
    private Timestamp startTime;
    private int requestStatusCode;
    private Collection<StopOverListResponse> stopOverInfos;
    private Collection<ShowInterestList> showInterestLists;

    public DriverNavigation() {
    }

    public DriverNavigation(int statusCode, String message, boolean is_success) {
        super(statusCode, message, is_success);
    }

    public DriverNavigation(int statusCode, String message, boolean is_success, int requestID, double startLocationLat, double startLocationLan, double stopLocationLat, double stopLocationLan, Timestamp startTime, Collection<StopOverListResponse> stopOverInfos, Collection<ShowInterestList> showInterestLists) {
        super(statusCode, message, is_success);
        this.requestID = requestID;
        this.startLocationLat = startLocationLat;
        this.startLocationLan = startLocationLan;
        this.stopLocationLat = stopLocationLat;
        this.stopLocationLan = stopLocationLan;
        this.startTime = startTime;
        this.stopOverInfos = stopOverInfos;
        this.showInterestLists = showInterestLists;
    }

    public DriverNavigation(int statusCode, String message, boolean is_success, int requestID, double startLocationLat, double startLocationLan, double stopLocationLat, double stopLocationLan, Timestamp startTime, int requestStatusCode, Collection<StopOverListResponse> stopOverInfos, Collection<ShowInterestList> showInterestLists) {
        super(statusCode, message, is_success);
        this.requestID = requestID;
        this.startLocationLat = startLocationLat;
        this.startLocationLan = startLocationLan;
        this.stopLocationLat = stopLocationLat;
        this.stopLocationLan = stopLocationLan;
        this.startTime = startTime;
        this.requestStatusCode = requestStatusCode;
        this.stopOverInfos = stopOverInfos;
        this.showInterestLists = showInterestLists;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public double getStartLocationLat() {
        return startLocationLat;
    }

    public void setStartLocationLat(double startLocationLat) {
        this.startLocationLat = startLocationLat;
    }

    public double getStartLocationLan() {
        return startLocationLan;
    }

    public void setStartLocationLan(double startLocationLan) {
        this.startLocationLan = startLocationLan;
    }

    public double getStopLocationLat() {
        return stopLocationLat;
    }

    public void setStopLocationLat(double stopLocationLat) {
        this.stopLocationLat = stopLocationLat;
    }

    public double getStopLocationLan() {
        return stopLocationLan;
    }

    public void setStopLocationLan(double stopLocationLan) {
        this.stopLocationLan = stopLocationLan;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Collection<StopOverListResponse> getStopOverInfos() {
        return stopOverInfos;
    }

    public void setStopOverInfos(Collection<StopOverListResponse> stopOverInfos) {
        this.stopOverInfos = stopOverInfos;
    }

    public Collection<ShowInterestList> getShowInterestLists() {
        return showInterestLists;
    }

    public void setShowInterestLists(Collection<ShowInterestList> showInterestLists) {
        this.showInterestLists = showInterestLists;
    }

    public int getRequestStatusCode() {
        return requestStatusCode;
    }

    public void setRequestStatusCode(int requestStatusCode) {
        this.requestStatusCode = requestStatusCode;
    }
}
