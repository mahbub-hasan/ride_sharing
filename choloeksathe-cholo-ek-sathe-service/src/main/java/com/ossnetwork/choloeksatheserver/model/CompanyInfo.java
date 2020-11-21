package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class CompanyInfo {
    private int companyId;
    private String companyName;
    private String companyDetails;
    private int isActive;
    private Collection<OccupationInfo> occupationInfosByCompanyId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CompanyID", nullable = false)
    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    @Basic
    @Column(name = "CompanyName", nullable = false, length = 150)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Basic
    @Column(name = "CompanyDetails", nullable = false, length = 250)
    public String getCompanyDetails() {
        return companyDetails;
    }

    public void setCompanyDetails(String companyDetails) {
        this.companyDetails = companyDetails;
    }

    @Basic
    @Column(name = "isActive", nullable = false)
    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyInfo that = (CompanyInfo) o;

        if (companyId != that.companyId) return false;
        if (isActive != that.isActive) return false;
        if (companyName != null ? !companyName.equals(that.companyName) : that.companyName != null) return false;
        if (companyDetails != null ? !companyDetails.equals(that.companyDetails) : that.companyDetails != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = companyId;
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + (companyDetails != null ? companyDetails.hashCode() : 0);
        result = 31 * result + isActive;
        return result;
    }

    @OneToMany(mappedBy = "companyInfoByCompanyId")
    public Collection<OccupationInfo> getOccupationInfosByCompanyId() {
        return occupationInfosByCompanyId;
    }

    public void setOccupationInfosByCompanyId(Collection<OccupationInfo> occupationInfosByCompanyId) {
        this.occupationInfosByCompanyId = occupationInfosByCompanyId;
    }
}
