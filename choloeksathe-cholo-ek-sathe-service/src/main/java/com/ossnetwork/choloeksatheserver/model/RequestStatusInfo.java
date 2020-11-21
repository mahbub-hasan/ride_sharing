package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class RequestStatusInfo {
    private int requestStatusId;
    private String requestStatus;
    private boolean isActive;
    private Collection<RequestInfo> requestInfosByRequestStatusId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequestStatusID", nullable = false)
    public int getRequestStatusId() {
        return requestStatusId;
    }

    public void setRequestStatusId(int requestStatusId) {
        this.requestStatusId = requestStatusId;
    }

    @Basic
    @Column(name = "RequestStatus", nullable = false, length = 50)
    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
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

        RequestStatusInfo that = (RequestStatusInfo) o;

        if (requestStatusId != that.requestStatusId) return false;
        if (isActive != that.isActive) return false;
        if (requestStatus != null ? !requestStatus.equals(that.requestStatus) : that.requestStatus != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestStatusId;
        result = 31 * result + (requestStatus != null ? requestStatus.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }

    @OneToMany(mappedBy = "requestStatusInfoByRequestStatusId")
    public Collection<RequestInfo> getRequestInfosByRequestStatusId() {
        return requestInfosByRequestStatusId;
    }

    public void setRequestInfosByRequestStatusId(Collection<RequestInfo> requestInfosByRequestStatusId) {
        this.requestInfosByRequestStatusId = requestInfosByRequestStatusId;
    }
}
