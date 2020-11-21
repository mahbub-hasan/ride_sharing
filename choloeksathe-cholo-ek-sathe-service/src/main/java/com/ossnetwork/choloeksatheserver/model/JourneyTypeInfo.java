package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class JourneyTypeInfo {
    private int journeyTypeId;
    private String journeyType;
    private boolean isActive;
    private Collection<RequestInfo> requestInfosByJourneyTypeId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JourneyTypeID", nullable = false)
    public int getJourneyTypeId() {
        return journeyTypeId;
    }

    public void setJourneyTypeId(int journeyTypeId) {
        this.journeyTypeId = journeyTypeId;
    }

    @Basic
    @Column(name = "JourneyType", nullable = false, length = 150)
    public String getJourneyType() {
        return journeyType;
    }

    public void setJourneyType(String journeyType) {
        this.journeyType = journeyType;
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

        JourneyTypeInfo that = (JourneyTypeInfo) o;

        if (journeyTypeId != that.journeyTypeId) return false;
        if (isActive != that.isActive) return false;
        if (journeyType != null ? !journeyType.equals(that.journeyType) : that.journeyType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = journeyTypeId;
        result = 31 * result + (journeyType != null ? journeyType.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }

    @OneToMany(mappedBy = "journeyTypeInfoByJourneyTypeId")
    public Collection<RequestInfo> getRequestInfosByJourneyTypeId() {
        return requestInfosByJourneyTypeId;
    }

    public void setRequestInfosByJourneyTypeId(Collection<RequestInfo> requestInfosByJourneyTypeId) {
        this.requestInfosByJourneyTypeId = requestInfosByJourneyTypeId;
    }
}
