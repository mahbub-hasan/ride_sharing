package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;

@Entity
public class OccupationInfo {
    private int occupationId;
    private String occupation;
    private boolean isActive;
    private CompanyInfo companyInfoByCompanyId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OccupationID", nullable = false)
    public int getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(int occupationId) {
        this.occupationId = occupationId;
    }

    @Basic
    @Column(name = "Occupation", nullable = false, length = 150)
    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
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

        OccupationInfo that = (OccupationInfo) o;

        if (occupationId != that.occupationId) return false;
        if (isActive != that.isActive) return false;
        if (occupation != null ? !occupation.equals(that.occupation) : that.occupation != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = occupationId;
        result = 31 * result + (occupation != null ? occupation.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "CompanyID", referencedColumnName = "CompanyID", nullable = false)
    public CompanyInfo getCompanyInfoByCompanyId() {
        return companyInfoByCompanyId;
    }

    public void setCompanyInfoByCompanyId(CompanyInfo companyInfoByCompanyId) {
        this.companyInfoByCompanyId = companyInfoByCompanyId;
    }
}
