package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
public class MobileVerificationCode {
    private int slno;
    private int userLoginInfoId;
    private String verificationCode;
    private String mobileNo;
    private String remarks;
    private Timestamp createdDate;
    private Collection<VerifiedMobileLog> verifiedMobileLogsBySlno;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slno", nullable = false)
    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    @Basic
    @Column(name = "UserLoginInfoId", nullable = false)
    public int getUserLoginInfoId() {
        return userLoginInfoId;
    }

    public void setUserLoginInfoId(int userLoginInfoId) {
        this.userLoginInfoId = userLoginInfoId;
    }

    @Basic
    @Column(name = "VerificationCode", nullable = false, length = 15)
    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Basic
    @Column(name = "MobileNo", nullable = true, length = 50)
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Basic
    @Column(name = "Remarks", nullable = true, length = 200)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

        MobileVerificationCode that = (MobileVerificationCode) o;

        if (slno != that.slno) return false;
        if (userLoginInfoId != that.userLoginInfoId) return false;
        if (verificationCode != null ? !verificationCode.equals(that.verificationCode) : that.verificationCode != null)
            return false;
        if (mobileNo != null ? !mobileNo.equals(that.mobileNo) : that.mobileNo != null) return false;
        if (remarks != null ? !remarks.equals(that.remarks) : that.remarks != null) return false;
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = slno;
        result = 31 * result + userLoginInfoId;
        result = 31 * result + (verificationCode != null ? verificationCode.hashCode() : 0);
        result = 31 * result + (mobileNo != null ? mobileNo.hashCode() : 0);
        result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "mobileVerificationCodeByMobileVerificationCodeId")
    public Collection<VerifiedMobileLog> getVerifiedMobileLogsBySlno() {
        return verifiedMobileLogsBySlno;
    }

    public void setVerifiedMobileLogsBySlno(Collection<VerifiedMobileLog> verifiedMobileLogsBySlno) {
        this.verifiedMobileLogsBySlno = verifiedMobileLogsBySlno;
    }
}
