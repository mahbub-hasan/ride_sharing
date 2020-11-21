package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class VerifiedEmailLog {
    private long slNo;
    private String emailAddress;
    private Timestamp createdDate;
    private Boolean isVefified;
    private Timestamp verificationTime;
    private UserInfo userInfoByUserId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SlNo", nullable = false)
    public long getSlNo() {
        return slNo;
    }

    public void setSlNo(long slNo) {
        this.slNo = slNo;
    }

    @Basic
    @Column(name = "EmailAddress", nullable = true, length = 150)
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Basic
    @Column(name = "CreatedDate", nullable = true)
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Basic
    @Column(name = "isVefified", nullable = true)
    public Boolean getVefified() {
        return isVefified;
    }

    public void setVefified(Boolean vefified) {
        isVefified = vefified;
    }

    @Basic
    @Column(name = "VerificationTime", nullable = true)
    public Timestamp getVerificationTime() {
        return verificationTime;
    }

    public void setVerificationTime(Timestamp verificationTime) {
        this.verificationTime = verificationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VerifiedEmailLog that = (VerifiedEmailLog) o;

        if (slNo != that.slNo) return false;
        if (emailAddress != null ? !emailAddress.equals(that.emailAddress) : that.emailAddress != null) return false;
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) return false;
        if (isVefified != null ? !isVefified.equals(that.isVefified) : that.isVefified != null) return false;
        if (verificationTime != null ? !verificationTime.equals(that.verificationTime) : that.verificationTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (slNo ^ (slNo >>> 32));
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (isVefified != null ? isVefified.hashCode() : 0);
        result = 31 * result + (verificationTime != null ? verificationTime.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    public UserInfo getUserInfoByUserId() {
        return userInfoByUserId;
    }

    public void setUserInfoByUserId(UserInfo userInfoByUserId) {
        this.userInfoByUserId = userInfoByUserId;
    }
}
