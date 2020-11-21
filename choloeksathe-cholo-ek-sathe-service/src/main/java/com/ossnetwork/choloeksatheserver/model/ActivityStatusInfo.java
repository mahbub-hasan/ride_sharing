package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class ActivityStatusInfo {
    private int activityStatusId;
    private String activityStatus;
    private boolean isActive;
    private Collection<ActivityInfo> activityInfosByActivityStatusId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ActivityStatusID", nullable = false)
    public int getActivityStatusId() {
        return activityStatusId;
    }

    public void setActivityStatusId(int activityStatusId) {
        this.activityStatusId = activityStatusId;
    }

    @Basic
    @Column(name = "ActivityStatus", nullable = false, length = 50)
    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
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

        ActivityStatusInfo that = (ActivityStatusInfo) o;

        if (activityStatusId != that.activityStatusId) return false;
        if (isActive != that.isActive) return false;
        if (activityStatus != null ? !activityStatus.equals(that.activityStatus) : that.activityStatus != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = activityStatusId;
        result = 31 * result + (activityStatus != null ? activityStatus.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }

    @OneToMany(mappedBy = "activityStatusInfoByActivityStatusId")
    public Collection<ActivityInfo> getActivityInfosByActivityStatusId() {
        return activityInfosByActivityStatusId;
    }

    public void setActivityInfosByActivityStatusId(Collection<ActivityInfo> activityInfosByActivityStatusId) {
        this.activityInfosByActivityStatusId = activityInfosByActivityStatusId;
    }
}
