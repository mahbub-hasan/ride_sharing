package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class RoadStatusLog {
    private int roadStatusLogId;
    private double latitude;
    private double longitude;
    private Timestamp updatedTime;
    private boolean isActive;
    private RoadStatusInfo roadStatusInfoByRoadStatusId;
    private UserInfo userInfoByUpdatedBy;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoadStatusLogID", nullable = false)
    public int getRoadStatusLogId() {
        return roadStatusLogId;
    }

    public void setRoadStatusLogId(int roadStatusLogId) {
        this.roadStatusLogId = roadStatusLogId;
    }

    @Basic
    @Column(name = "Latitude", nullable = false, precision = 0)
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "Longitude", nullable = false, precision = 0)
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "UpdatedTime", nullable = false)
    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Basic
    @Column(name = "isActive", nullable = false)
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoadStatusLog that = (RoadStatusLog) o;

        if (roadStatusLogId != that.roadStatusLogId) return false;
        if (Double.compare(that.latitude, latitude) != 0) return false;
        if (Double.compare(that.longitude, longitude) != 0) return false;
        if (isActive != that.isActive) return false;
        if (updatedTime != null ? !updatedTime.equals(that.updatedTime) : that.updatedTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = roadStatusLogId;
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (updatedTime != null ? updatedTime.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "RoadStatusID", referencedColumnName = "RoadStatusID", nullable = false)
    public RoadStatusInfo getRoadStatusInfoByRoadStatusId() {
        return roadStatusInfoByRoadStatusId;
    }

    public void setRoadStatusInfoByRoadStatusId(RoadStatusInfo roadStatusInfoByRoadStatusId) {
        this.roadStatusInfoByRoadStatusId = roadStatusInfoByRoadStatusId;
    }

    @ManyToOne
    @JoinColumn(name = "UpdatedBy", referencedColumnName = "UserID", nullable = false)
    public UserInfo getUserInfoByUpdatedBy() {
        return userInfoByUpdatedBy;
    }

    public void setUserInfoByUpdatedBy(UserInfo userInfoByUpdatedBy) {
        this.userInfoByUpdatedBy = userInfoByUpdatedBy;
    }
}
