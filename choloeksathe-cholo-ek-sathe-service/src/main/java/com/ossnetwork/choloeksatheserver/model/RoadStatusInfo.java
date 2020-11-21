package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class RoadStatusInfo {
    private int roadStatusId;
    private String roadStatus;
    private int type;
    private boolean isActive;
    private Collection<RoadStatusLog> roadStatusLogsByRoadStatusId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoadStatusID", nullable = false)
    public int getRoadStatusId() {
        return roadStatusId;
    }

    public void setRoadStatusId(int roadStatusId) {
        this.roadStatusId = roadStatusId;
    }

    @Basic
    @Column(name = "RoadStatus", nullable = false, length = 150)
    public String getRoadStatus() {
        return roadStatus;
    }

    public void setRoadStatus(String roadStatus) {
        this.roadStatus = roadStatus;
    }

    @Basic
    @Column(name = "Type", nullable = false)
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

        RoadStatusInfo that = (RoadStatusInfo) o;

        if (roadStatusId != that.roadStatusId) return false;
        if (type != that.type) return false;
        if (isActive != that.isActive) return false;
        if (roadStatus != null ? !roadStatus.equals(that.roadStatus) : that.roadStatus != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = roadStatusId;
        result = 31 * result + (roadStatus != null ? roadStatus.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }

    @OneToMany(mappedBy = "roadStatusInfoByRoadStatusId")
    public Collection<RoadStatusLog> getRoadStatusLogsByRoadStatusId() {
        return roadStatusLogsByRoadStatusId;
    }

    public void setRoadStatusLogsByRoadStatusId(Collection<RoadStatusLog> roadStatusLogsByRoadStatusId) {
        this.roadStatusLogsByRoadStatusId = roadStatusLogsByRoadStatusId;
    }
}
