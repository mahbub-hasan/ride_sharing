package com.ossnetwork.choloeksatheserver.model.response;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class CarShareListResponse {
    private int requestId;
    private Timestamp startTime;
    private Timestamp returnTime;
    private String requestType;
    private String requestStatus;
    private String journeyType;
    private boolean isActive;
    private String sourceLocation;
    private String destinationLocation;
    private Date driveDate;
    private int interestShowCount;
    private List<StopOverListResponse> stopOverListResponses;

    public CarShareListResponse() {
    }

    public CarShareListResponse(int requestId, Timestamp startTime, Timestamp returnTime, String requestType, String requestStatus, String journeyType, boolean isActive, String sourceLocation, String destinationLocation, Date driveDate, int interestShowCount, List<StopOverListResponse> stopOverListResponses) {
        this.requestId = requestId;
        this.startTime = startTime;
        this.returnTime = returnTime;
        this.requestType = requestType;
        this.requestStatus = requestStatus;
        this.journeyType = journeyType;
        this.isActive = isActive;
        this.sourceLocation = sourceLocation;
        this.destinationLocation = destinationLocation;
        this.driveDate = driveDate;
        this.interestShowCount = interestShowCount;
        this.stopOverListResponses = stopOverListResponses;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Timestamp returnTime) {
        this.returnTime = returnTime;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getJourneyType() {
        return journeyType;
    }

    public void setJourneyType(String journeyType) {
        this.journeyType = journeyType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<StopOverListResponse> getStopOverListResponses() {
        return stopOverListResponses;
    }

    public void setStopOverListResponses(List<StopOverListResponse> stopOverListResponses) {
        this.stopOverListResponses = stopOverListResponses;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public Date getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(Date driveDate) {
        this.driveDate = driveDate;
    }

    public int getInterestShowCount() {
        return interestShowCount;
    }

    public void setInterestShowCount(int interestShowCount) {
        this.interestShowCount = interestShowCount;
    }
}
