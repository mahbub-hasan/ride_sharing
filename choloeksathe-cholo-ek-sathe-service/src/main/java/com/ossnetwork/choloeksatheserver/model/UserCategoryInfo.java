package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;

@Entity
public class UserCategoryInfo {
    private int userCategoryId;
    private String userCategory;
    private boolean isActive;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserCategoryID", nullable = false)
    public int getUserCategoryId() {
        return userCategoryId;
    }

    public void setUserCategoryId(int userCategoryId) {
        this.userCategoryId = userCategoryId;
    }

    @Basic
    @Column(name = "UserCategory", nullable = false, length = 150)
    public String getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
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

        UserCategoryInfo that = (UserCategoryInfo) o;

        if (userCategoryId != that.userCategoryId) return false;
        if (isActive != that.isActive) return false;
        if (userCategory != null ? !userCategory.equals(that.userCategory) : that.userCategory != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userCategoryId;
        result = 31 * result + (userCategory != null ? userCategory.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }
}
