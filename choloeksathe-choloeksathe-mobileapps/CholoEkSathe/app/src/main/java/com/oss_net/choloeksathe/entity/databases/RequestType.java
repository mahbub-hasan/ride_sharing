package com.oss_net.choloeksathe.entity.databases;

import io.realm.RealmObject;

/**
 * Created by mahbubhasan on 12/6/17.
 */

public class RequestType extends RealmObject {
    private int id;
    private String requestType;
    private boolean isActive;

    public RequestType() {
    }

    public RequestType(int id, String requestType, boolean isActive) {
        this.id = id;
        this.requestType = requestType;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
