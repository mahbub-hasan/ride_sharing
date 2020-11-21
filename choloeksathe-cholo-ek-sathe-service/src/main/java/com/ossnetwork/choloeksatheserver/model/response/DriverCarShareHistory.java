package com.ossnetwork.choloeksatheserver.model.response;

import java.sql.Date;
import java.sql.Timestamp;

public class DriverCarShareHistory{
    private int requestId;
    private String shortStartPlace;
    private String shortEndPlace;
    private Timestamp startTime;
    private Date requestDate;

    public DriverCarShareHistory(int requestId, String shortStartPlace, String shortEndPlace, Timestamp startTime, Date requestDate) {
        this.requestId = requestId;
        this.shortStartPlace = shortStartPlace;
        this.shortEndPlace = shortEndPlace;
        this.startTime = startTime;
        this.requestDate = requestDate;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getShortStartPlace() {
        return shortStartPlace;
    }

    public void setShortStartPlace(String shortStartPlace) {
        this.shortStartPlace = shortStartPlace;
    }

    public String getShortEndPlace() {
        return shortEndPlace;
    }

    public void setShortEndPlace(String shortEndPlace) {
        this.shortEndPlace = shortEndPlace;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
}
