package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;

@Entity
public class UserTypeInfo {
    private int userTypeId;
    private String userType;
    private boolean isActive;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserTypeID", nullable = false)
    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    @Basic
    @Column(name = "UserType", nullable = false, length = 50)
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

        UserTypeInfo that = (UserTypeInfo) o;

        if (userTypeId != that.userTypeId) return false;
        if (isActive != that.isActive) return false;
        if (userType != null ? !userType.equals(that.userType) : that.userType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userTypeId;
        result = 31 * result + (userType != null ? userType.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }
}
