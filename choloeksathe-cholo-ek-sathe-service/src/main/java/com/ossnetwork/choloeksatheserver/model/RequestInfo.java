package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
public class RequestInfo {
    private int requestId;
    private double startLatitude;
    private double startLongitude;
    private double stopLatitude;
    private double stopLongitude;
    private String longStartPlace;
    private String longEndPlace;
    private String shortStartPlace;
    private String shortEndPlace;
    private double price;
    private int noofFreeSeat;
    private boolean stopOver;
    private boolean isRound;
    private Timestamp startTime;
    private Timestamp returnTime;
    private boolean luggageSpaceAvailable;
    private boolean smokingAllowed;
    private boolean outSideFoodAllowed;
    private boolean musicAllowed;
    private boolean petsAllowed;
    private String genderRestriction;
    private Integer companyId;
    private String rideDetails;
    private Integer visitorCount;
    private Date requestDate;
    private Collection<ActivityInfo> activityInfosByRequestId;
    private UserInfo userInfoByRequesterId;
    private RequestType requestTypeByRequestTypeId;
    private RequestStatusInfo requestStatusInfoByRequestStatusId;
    private JourneyTypeInfo journeyTypeInfoByJourneyTypeId;
    private Collection<RequesterCollaborationLog> requesterCollaborationLogsByRequestId;
    private Collection<StopOverInfo> stopOverInfosByRequestId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequestID", nullable = false)
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    @Basic
    @Column(name = "StartLatitude", nullable = false, precision = 0)
    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    @Basic
    @Column(name = "StartLongitude", nullable = false, precision = 0)
    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    @Basic
    @Column(name = "StopLatitude", nullable = false, precision = 0)
    public double getStopLatitude() {
        return stopLatitude;
    }

    public void setStopLatitude(double stopLatitude) {
        this.stopLatitude = stopLatitude;
    }

    @Basic
    @Column(name = "StopLongitude", nullable = false, precision = 0)
    public double getStopLongitude() {
        return stopLongitude;
    }

    public void setStopLongitude(double stopLongitude) {
        this.stopLongitude = stopLongitude;
    }

    @Basic
    @Column(name = "LongStartPlace", nullable = true, length = 250)
    public String getLongStartPlace() {
        return longStartPlace;
    }

    public void setLongStartPlace(String longStartPlace) {
        this.longStartPlace = longStartPlace;
    }

    @Basic
    @Column(name = "LongEndPlace", nullable = true, length = 250)
    public String getLongEndPlace() {
        return longEndPlace;
    }

    public void setLongEndPlace(String longEndPlace) {
        this.longEndPlace = longEndPlace;
    }

    @Basic
    @Column(name = "ShortStartPlace", nullable = true, length = 150)
    public String getShortStartPlace() {
        return shortStartPlace;
    }

    public void setShortStartPlace(String shortStartPlace) {
        this.shortStartPlace = shortStartPlace;
    }

    @Basic
    @Column(name = "ShortEndPlace", nullable = true, length = 150)
    public String getShortEndPlace() {
        return shortEndPlace;
    }

    public void setShortEndPlace(String shortEndPlace) {
        this.shortEndPlace = shortEndPlace;
    }

    @Basic
    @Column(name = "Price", nullable = false, precision = 0)
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "NoofFreeSeat", nullable = false)
    public int getNoofFreeSeat() {
        return noofFreeSeat;
    }

    public void setNoofFreeSeat(int noofFreeSeat) {
        this.noofFreeSeat = noofFreeSeat;
    }

    @Basic
    @Column(name = "StopOver", nullable = true)
    public boolean getStopOver() {
        return stopOver;
    }

    public void setStopOver(boolean stopOver) {
        this.stopOver = stopOver;
    }

    @Basic
    @Column(name = "isRound", nullable = true)
    public boolean getRound() {
        return isRound;
    }

    public void setRound(boolean round) {
        isRound = round;
    }

    @Basic
    @Column(name = "StartTime", nullable = false)
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "ReturnTime", nullable = true)
    public Timestamp getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Timestamp returnTime) {
        this.returnTime = returnTime;
    }

    @Basic
    @Column(name = "LuggageSpaceAvailable", nullable = true)
    public boolean getLuggageSpaceAvailable() {
        return luggageSpaceAvailable;
    }

    public void setLuggageSpaceAvailable(boolean luggageSpaceAvailable) {
        this.luggageSpaceAvailable = luggageSpaceAvailable;
    }

    @Basic
    @Column(name = "SmokingAllowed", nullable = true)
    public boolean getSmokingAllowed() {
        return smokingAllowed;
    }

    public void setSmokingAllowed(boolean smokingAllowed) {
        this.smokingAllowed = smokingAllowed;
    }

    @Basic
    @Column(name = "OutSideFoodAllowed", nullable = true)
    public boolean getOutSideFoodAllowed() {
        return outSideFoodAllowed;
    }

    public void setOutSideFoodAllowed(boolean outSideFoodAllowed) {
        this.outSideFoodAllowed = outSideFoodAllowed;
    }

    @Basic
    @Column(name = "MusicAllowed", nullable = true)
    public boolean getMusicAllowed() {
        return musicAllowed;
    }

    public void setMusicAllowed(boolean musicAllowed) {
        this.musicAllowed = musicAllowed;
    }

    @Basic
    @Column(name = "PetsAllowed", nullable = true)
    public boolean getPetsAllowed() {
        return petsAllowed;
    }

    public void setPetsAllowed(boolean petsAllowed) {
        this.petsAllowed = petsAllowed;
    }

    @Basic
    @Column(name = "GenderRestriction", nullable = true, length = 50)
    public String getGenderRestriction() {
        return genderRestriction;
    }

    public void setGenderRestriction(String genderRestriction) {
        this.genderRestriction = genderRestriction;
    }

    @Basic
    @Column(name = "CompanyID", nullable = true)
    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @Basic
    @Column(name = "RideDetails", nullable = true, length = 2147483647)
    public String getRideDetails() {
        return rideDetails;
    }

    public void setRideDetails(String rideDetails) {
        this.rideDetails = rideDetails;
    }

    @Basic
    @Column(name = "VisitorCount", nullable = true)
    public Integer getVisitorCount() {
        return visitorCount;
    }

    public void setVisitorCount(Integer visitorCount) {
        this.visitorCount = visitorCount;
    }

    @Basic
    @Column(name = "RequestDate", nullable = true)
    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestInfo that = (RequestInfo) o;

        if (requestId != that.requestId) return false;
        if (Double.compare(that.startLatitude, startLatitude) != 0) return false;
        if (Double.compare(that.startLongitude, startLongitude) != 0) return false;
        if (Double.compare(that.stopLatitude, stopLatitude) != 0) return false;
        if (Double.compare(that.stopLongitude, stopLongitude) != 0) return false;
        if (Double.compare(that.price, price) != 0) return false;
        if (noofFreeSeat != that.noofFreeSeat) return false;
        if (longStartPlace != null ? !longStartPlace.equals(that.longStartPlace) : that.longStartPlace != null)
            return false;
        if (longEndPlace != null ? !longEndPlace.equals(that.longEndPlace) : that.longEndPlace != null) return false;
        if (shortStartPlace != null ? !shortStartPlace.equals(that.shortStartPlace) : that.shortStartPlace != null)
            return false;
        if (shortEndPlace != null ? !shortEndPlace.equals(that.shortEndPlace) : that.shortEndPlace != null)
            return false;
        if (stopOver != this.stopOver) return false;
        if (isRound != that.isRound) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        if (returnTime != null ? !returnTime.equals(that.returnTime) : that.returnTime != null) return false;
        if (luggageSpaceAvailable != that.luggageSpaceAvailable)
            return false;
        if (smokingAllowed != that.smokingAllowed)
            return false;
        if (outSideFoodAllowed != that.outSideFoodAllowed)
            return false;
        if (musicAllowed != that.musicAllowed) return false;
        if (petsAllowed != that.petsAllowed) return false;
        if (genderRestriction != null ? !genderRestriction.equals(that.genderRestriction) : that.genderRestriction != null)
            return false;

        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (rideDetails != null ? !rideDetails.equals(that.rideDetails) : that.rideDetails != null) return false;
        if (visitorCount != null ? !visitorCount.equals(that.visitorCount) : that.visitorCount != null) return false;
        if (requestDate != null ? !requestDate.equals(that.requestDate) : that.requestDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = requestId;
        temp = Double.doubleToLongBits(startLatitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(startLongitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(stopLatitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(stopLongitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (longStartPlace != null ? longStartPlace.hashCode() : 0);
        result = 31 * result + (longEndPlace != null ? longEndPlace.hashCode() : 0);
        result = 31 * result + (shortStartPlace != null ? shortStartPlace.hashCode() : 0);
        result = 31 * result + (shortEndPlace != null ? shortEndPlace.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + noofFreeSeat;
        result = 31 * result + (stopOver ? 1 : 0);
        result = 31 * result + (isRound  ? 1 : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (returnTime != null ? returnTime.hashCode() : 0);
        result = 31 * result + (luggageSpaceAvailable ? 1 : 0);
        result = 31 * result + (smokingAllowed ? 1 : 0);
        result = 31 * result + (outSideFoodAllowed  ? 1 : 0);
        result = 31 * result + (musicAllowed ? 1: 0);
        result = 31 * result + (petsAllowed ? 1: 0);
        result = 31 * result + (genderRestriction != null ? genderRestriction.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (rideDetails != null ? rideDetails.hashCode() : 0);
        result = 31 * result + (visitorCount != null ? visitorCount.hashCode() : 0);
        result = 31 * result + (requestDate != null ? requestDate.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "requestInfoByRequestId")
    public Collection<ActivityInfo> getActivityInfosByRequestId() {
        return activityInfosByRequestId;
    }

    public void setActivityInfosByRequestId(Collection<ActivityInfo> activityInfosByRequestId) {
        this.activityInfosByRequestId = activityInfosByRequestId;
    }

    @ManyToOne
    @JoinColumn(name = "RequesterID", referencedColumnName = "UserID", nullable = false)
    public UserInfo getUserInfoByRequesterId() {
        return userInfoByRequesterId;
    }

    public void setUserInfoByRequesterId(UserInfo userInfoByRequesterId) {
        this.userInfoByRequesterId = userInfoByRequesterId;
    }

    @ManyToOne
    @JoinColumn(name = "RequestTypeID", referencedColumnName = "RequestTypeID")
    public RequestType getRequestTypeByRequestTypeId() {
        return requestTypeByRequestTypeId;
    }

    public void setRequestTypeByRequestTypeId(RequestType requestTypeByRequestTypeId) {
        this.requestTypeByRequestTypeId = requestTypeByRequestTypeId;
    }

    @ManyToOne
    @JoinColumn(name = "RequestStatusID", referencedColumnName = "RequestStatusID")
    public RequestStatusInfo getRequestStatusInfoByRequestStatusId() {
        return requestStatusInfoByRequestStatusId;
    }

    public void setRequestStatusInfoByRequestStatusId(RequestStatusInfo requestStatusInfoByRequestStatusId) {
        this.requestStatusInfoByRequestStatusId = requestStatusInfoByRequestStatusId;
    }

    @ManyToOne
    @JoinColumn(name = "JourneyTypeID", referencedColumnName = "JourneyTypeID")
    public JourneyTypeInfo getJourneyTypeInfoByJourneyTypeId() {
        return journeyTypeInfoByJourneyTypeId;
    }

    public void setJourneyTypeInfoByJourneyTypeId(JourneyTypeInfo journeyTypeInfoByJourneyTypeId) {
        this.journeyTypeInfoByJourneyTypeId = journeyTypeInfoByJourneyTypeId;
    }

    @OneToMany(mappedBy = "requestInfoByRequestId")
    public Collection<RequesterCollaborationLog> getRequesterCollaborationLogsByRequestId() {
        return requesterCollaborationLogsByRequestId;
    }

    public void setRequesterCollaborationLogsByRequestId(Collection<RequesterCollaborationLog> requesterCollaborationLogsByRequestId) {
        this.requesterCollaborationLogsByRequestId = requesterCollaborationLogsByRequestId;
    }

    @OneToMany(mappedBy = "requestInfoByRequestId")
    public Collection<StopOverInfo> getStopOverInfosByRequestId() {
        return stopOverInfosByRequestId;
    }

    public void setStopOverInfosByRequestId(Collection<StopOverInfo> stopOverInfosByRequestId) {
        this.stopOverInfosByRequestId = stopOverInfosByRequestId;
    }
}
