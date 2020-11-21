package com.oss_net.choloeksathe.entity.databases;

import io.realm.RealmObject;

/**
 * Created by mahbubhasan on 12/6/17.
 */

public class RequestStatusInfo extends RealmObject {
    private int id;
    private String requestStatus;
    private boolean isActive;

    public RequestStatusInfo() {
    }

    public RequestStatusInfo(int id, String requestStatus, boolean isActive) {
        this.id = id;
        this.requestStatus = requestStatus;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
