package com.oss_net.choloeksathe.entity.databases;

import io.realm.RealmObject;

/**
 * Created by mahbubhasan on 12/6/17.
 */

public class UserCategory extends RealmObject{
    private int id;
    private String category;
    private boolean isActive;

    public UserCategory() {
    }

    public UserCategory(int id, String category, boolean isActive) {
        this.id = id;
        this.category = category;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
