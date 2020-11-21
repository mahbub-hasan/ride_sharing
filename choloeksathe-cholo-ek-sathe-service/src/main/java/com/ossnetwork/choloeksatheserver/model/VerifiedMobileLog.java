package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class VerifiedMobileLog {
    private long slNo;
    private String mobileNo;
    private Timestamp createdDate;
    private MobileVerificationCode mobileVerificationCodeByMobileVerificationCodeId;
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
    @Column(name = "MobileNo", nullable = true, length = 20)
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Basic
    @Column(name = "CreatedDate", nullable = true)
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VerifiedMobileLog that = (VerifiedMobileLog) o;

        if (slNo != that.slNo) return false;
        if (mobileNo != null ? !mobileNo.equals(that.mobileNo) : that.mobileNo != null) return false;
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (slNo ^ (slNo >>> 32));
        result = 31 * result + (mobileNo != null ? mobileNo.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "MobileVerificationCodeId", referencedColumnName = "slno", nullable = false)
    public MobileVerificationCode getMobileVerificationCodeByMobileVerificationCodeId() {
        return mobileVerificationCodeByMobileVerificationCodeId;
    }

    public void setMobileVerificationCodeByMobileVerificationCodeId(MobileVerificationCode mobileVerificationCodeByMobileVerificationCodeId) {
        this.mobileVerificationCodeByMobileVerificationCodeId = mobileVerificationCodeByMobileVerificationCodeId;
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
