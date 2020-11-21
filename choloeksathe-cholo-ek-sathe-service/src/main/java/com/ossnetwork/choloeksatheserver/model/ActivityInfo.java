package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
public class ActivityInfo {


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
    private Timestamp endTime;
    private String description;
    private boolean passangerRatingStatus;
    private boolean driverRatingStatus;
    private RequestInfo requestInfoByRequestId;
    private UserInfo userInfoByDriverId;
    private UserInfo userInfoByPassengerId;
    private ActivityStatusInfo activityStatusInfoByActivityStatusId;
    private RecommendationTypeInfo recommendationTypeInfoByRecommendationId;
    private Collection<RequesterCollaborationLog> requesterCollaborationLogsByActivityId;
    private Collection<TransactionLog> transactionLogsByActivityId;
    private Collection<Ratings> ratingsByActivityId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ActivityID", nullable = false)
    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
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
    @Column(name = "StartPointName", nullable = true, length = 1000)
    public String getStartPointName() {
        return startPointName;
    }

    public void setStartPointName(String startPointName) {
        this.startPointName = startPointName;
    }

    @Basic
    @Column(name = "StopPointName", nullable = true, length = 1000)
    public String getStopPointName() {
        return stopPointName;
    }

    public void setStopPointName(String stopPointName) {
        this.stopPointName = stopPointName;
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
    @Column(name = "NoofSeat", nullable = false)
    public int getNoofSeat() {
        return noofSeat;
    }

    public void setNoofSeat(int noofSeat) {
        this.noofSeat = noofSeat;
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
    @Column(name = "EndTime", nullable = true)
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "Description", nullable = true, length = 2147483647)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "PassangerRatingStatus", nullable = true)
    public boolean getPassangerRatingStatus() {
        return passangerRatingStatus;
    }

    public void setPassangerRatingStatus(boolean passangerRatingStatus) {
        this.passangerRatingStatus = passangerRatingStatus;
    }

    @Basic
    @Column(name = "DriverRatingStatus", nullable = true)
    public boolean getDriverRatingStatus() {
        return driverRatingStatus;
    }

    public void setDriverRatingStatus(boolean driverRatingStatus) {
        this.driverRatingStatus = driverRatingStatus;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityInfo that = (ActivityInfo) o;

        if (activityId != that.activityId) return false;
        if (Double.compare(that.startLatitude, startLatitude) != 0) return false;
        if (Double.compare(that.startLongitude, startLongitude) != 0) return false;
        if (Double.compare(that.stopLatitude, stopLatitude) != 0) return false;
        if (Double.compare(that.stopLongitude, stopLongitude) != 0) return false;
        if (Double.compare(that.price, price) != 0) return false;
        if (noofSeat != that.noofSeat) return false;
        if (startPointName != null ? !startPointName.equals(that.startPointName) : that.startPointName != null)
            return false;
        if (stopPointName != null ? !stopPointName.equals(that.stopPointName) : that.stopPointName != null)
            return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (passangerRatingStatus != that.passangerRatingStatus) return false;

        if (driverRatingStatus != that.driverRatingStatus) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = activityId;
        temp = Double.doubleToLongBits(startLatitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(startLongitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(stopLatitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(stopLongitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (startPointName != null ? startPointName.hashCode() : 0);
        result = 31 * result + (stopPointName != null ? stopPointName.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + noofSeat;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (passangerRatingStatus ? 1 : 0);
        result = 31 * result + (driverRatingStatus ? 1 : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "RequestID", referencedColumnName = "RequestID", nullable = false)
    public RequestInfo getRequestInfoByRequestId() {
        return requestInfoByRequestId;
    }

    public void setRequestInfoByRequestId(RequestInfo requestInfoByRequestId) {
        this.requestInfoByRequestId = requestInfoByRequestId;
    }

    @ManyToOne
    @JoinColumn(name = "DriverID", referencedColumnName = "UserID", nullable = false)
    public UserInfo getUserInfoByDriverId() {
        return userInfoByDriverId;
    }

    public void setUserInfoByDriverId(UserInfo userInfoByDriverId) {
        this.userInfoByDriverId = userInfoByDriverId;
    }

    @ManyToOne
    @JoinColumn(name = "PassengerID", referencedColumnName = "UserID", nullable = false)
    public UserInfo getUserInfoByPassengerId() {
        return userInfoByPassengerId;
    }

    public void setUserInfoByPassengerId(UserInfo userInfoByPassengerId) {
        this.userInfoByPassengerId = userInfoByPassengerId;
    }

    @ManyToOne
    @JoinColumn(name = "ActivityStatusID", referencedColumnName = "ActivityStatusID", nullable = false)
    public ActivityStatusInfo getActivityStatusInfoByActivityStatusId() {
        return activityStatusInfoByActivityStatusId;
    }

    public void setActivityStatusInfoByActivityStatusId(ActivityStatusInfo activityStatusInfoByActivityStatusId) {
        this.activityStatusInfoByActivityStatusId = activityStatusInfoByActivityStatusId;
    }

    @ManyToOne
    @JoinColumn(name = "RecommendationID", referencedColumnName = "RecommendationID")
    public RecommendationTypeInfo getRecommendationTypeInfoByRecommendationId() {
        return recommendationTypeInfoByRecommendationId;
    }

    public void setRecommendationTypeInfoByRecommendationId(RecommendationTypeInfo recommendationTypeInfoByRecommendationId) {
        this.recommendationTypeInfoByRecommendationId = recommendationTypeInfoByRecommendationId;
    }

    @OneToMany(mappedBy = "activityInfoByActivityId")
    public Collection<RequesterCollaborationLog> getRequesterCollaborationLogsByActivityId() {
        return requesterCollaborationLogsByActivityId;
    }

    public void setRequesterCollaborationLogsByActivityId(Collection<RequesterCollaborationLog> requesterCollaborationLogsByActivityId) {
        this.requesterCollaborationLogsByActivityId = requesterCollaborationLogsByActivityId;
    }

    @OneToMany(mappedBy = "activityInfoByActivityId")
    public Collection<TransactionLog> getTransactionLogsByActivityId() {
        return transactionLogsByActivityId;
    }

    public void setTransactionLogsByActivityId(Collection<TransactionLog> transactionLogsByActivityId) {
        this.transactionLogsByActivityId = transactionLogsByActivityId;
    }

    @OneToMany(mappedBy = "activityInfoByActivityId")
    public Collection<Ratings> getRatingsByActivityId() {
        return ratingsByActivityId;
    }

    public void setRatingsByActivityId(Collection<Ratings> ratingsByActivityId) {
        this.ratingsByActivityId = ratingsByActivityId;
    }
}
