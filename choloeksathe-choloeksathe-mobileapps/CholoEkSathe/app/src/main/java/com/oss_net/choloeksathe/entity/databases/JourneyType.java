package com.oss_net.choloeksathe.entity.databases;

import io.realm.RealmObject;

/**
 * Created by mahbubhasan on 12/6/17.
 */

public class JourneyType extends RealmObject {
    private int id;
    private String journeyType;
    private boolean isActive;

    public JourneyType() {
    }

    public JourneyType(int id, String journeyType, boolean isActive) {
        this.id = id;
        this.journeyType = journeyType;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
