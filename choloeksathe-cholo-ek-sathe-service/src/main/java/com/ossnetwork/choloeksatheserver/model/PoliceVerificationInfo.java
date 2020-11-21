package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class PoliceVerificationInfo {
    private int verificationId;
    private String verifiedBy;
    private String verifiedLogs;
    private String verificationNumber;
    private String updatedBy;
    private Timestamp updatedDate;
    private UserInfo userInfoByUserId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VerificationID", nullable = false)
    public int getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(int verificationId) {
        this.verificationId = verificationId;
    }

    @Basic
    @Column(name = "VerifiedBy", nullable = false, length = 150)
    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    @Basic
    @Column(name = "VerifiedLogs", nullable = false, length = 2147483647)
    public String getVerifiedLogs() {
        return verifiedLogs;
    }

    public void setVerifiedLogs(String verifiedLogs) {
        this.verifiedLogs = verifiedLogs;
    }

    @Basic
    @Column(name = "VerificationNumber", nullable = false, length = 50)
    public String getVerificationNumber() {
        return verificationNumber;
    }

    public void setVerificationNumber(String verificationNumber) {
        this.verificationNumber = verificationNumber;
    }

    @Basic
    @Column(name = "UpdatedBy", nullable = true, length = 50)
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Basic
    @Column(name = "UpdatedDate", nullable = true)
    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PoliceVerificationInfo that = (PoliceVerificationInfo) o;

        if (verificationId != that.verificationId) return false;
        if (verifiedBy != null ? !verifiedBy.equals(that.verifiedBy) : that.verifiedBy != null) return false;
        if (verifiedLogs != null ? !verifiedLogs.equals(that.verifiedLogs) : that.verifiedLogs != null) return false;
        if (verificationNumber != null ? !verificationNumber.equals(that.verificationNumber) : that.verificationNumber != null)
            return false;
        if (updatedBy != null ? !updatedBy.equals(that.updatedBy) : that.updatedBy != null) return false;
        if (updatedDate != null ? !updatedDate.equals(that.updatedDate) : that.updatedDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = verificationId;
        result = 31 * result + (verifiedBy != null ? verifiedBy.hashCode() : 0);
        result = 31 * result + (verifiedLogs != null ? verifiedLogs.hashCode() : 0);
        result = 31 * result + (verificationNumber != null ? verificationNumber.hashCode() : 0);
        result = 31 * result + (updatedBy != null ? updatedBy.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", nullable = false)
    public UserInfo getUserInfoByUserId() {
        return userInfoByUserId;
    }

    public void setUserInfoByUserId(UserInfo userInfoByUserId) {
        this.userInfoByUserId = userInfoByUserId;
    }
}
