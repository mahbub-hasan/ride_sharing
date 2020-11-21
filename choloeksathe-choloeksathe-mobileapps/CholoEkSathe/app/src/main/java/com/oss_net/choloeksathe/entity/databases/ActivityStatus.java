package com.oss_net.choloeksathe.entity.databases;

import io.realm.RealmObject;

/**
 * Created by mahbubhasan on 12/6/17.
 */

public class ActivityStatus extends RealmObject {
    private int id;
    private String activityStatus;
    private boolean isActive;

    public ActivityStatus() {
    }

    public ActivityStatus(int id, String activityStatus, boolean isActive) {
        this.id = id;
        this.activityStatus = activityStatus;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
