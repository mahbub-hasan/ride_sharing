package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class RequestType {
    private int requestTypeId;
    private String requestType;
    private boolean isActive;
    private Collection<RequestInfo> requestInfosByRequestTypeId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequestTypeID", nullable = false)
    public int getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(int requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    @Basic
    @Column(name = "RequestType", nullable = false, length = 50)
    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
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

        RequestType that = (RequestType) o;

        if (requestTypeId != that.requestTypeId) return false;
        if (isActive != that.isActive) return false;
        if (requestType != null ? !requestType.equals(that.requestType) : that.requestType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestTypeId;
        result = 31 * result + (requestType != null ? requestType.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }

    @OneToMany(mappedBy = "requestTypeByRequestTypeId")
    public Collection<RequestInfo> getRequestInfosByRequestTypeId() {
        return requestInfosByRequestTypeId;
    }

    public void setRequestInfosByRequestTypeId(Collection<RequestInfo> requestInfosByRequestTypeId) {
        this.requestInfosByRequestTypeId = requestInfosByRequestTypeId;
    }
}
