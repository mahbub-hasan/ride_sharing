package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;

@Entity
public class AccountInfo {
    private int accountId;
    private String bankName;
    private String accountNumber;
    private Double earnedPoints;
    private Double totalPoints;
    private String logs;
    private UserInfo userInfoByUserId;
    private AccountTypeInfo accountTypeInfoByAccountTypeId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountID", nullable = false)
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "BankName", nullable = false, length = 250)
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Basic
    @Column(name = "AccountNumber", nullable = false, length = 50)
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Basic
    @Column(name = "EarnedPoints", nullable = true, precision = 0)
    public Double getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(Double earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    @Basic
    @Column(name = "TotalPoints", nullable = true, precision = 0)
    public Double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Double totalPoints) {
        this.totalPoints = totalPoints;
    }

    @Basic
    @Column(name = "Logs", nullable = true, length = 2147483647)
    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountInfo that = (AccountInfo) o;

        if (accountId != that.accountId) return false;
        if (bankName != null ? !bankName.equals(that.bankName) : that.bankName != null) return false;
        if (accountNumber != null ? !accountNumber.equals(that.accountNumber) : that.accountNumber != null)
            return false;
        if (earnedPoints != null ? !earnedPoints.equals(that.earnedPoints) : that.earnedPoints != null) return false;
        if (totalPoints != null ? !totalPoints.equals(that.totalPoints) : that.totalPoints != null) return false;
        if (logs != null ? !logs.equals(that.logs) : that.logs != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = accountId;
        result = 31 * result + (bankName != null ? bankName.hashCode() : 0);
        result = 31 * result + (accountNumber != null ? accountNumber.hashCode() : 0);
        result = 31 * result + (earnedPoints != null ? earnedPoints.hashCode() : 0);
        result = 31 * result + (totalPoints != null ? totalPoints.hashCode() : 0);
        result = 31 * result + (logs != null ? logs.hashCode() : 0);
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

    @ManyToOne
    @JoinColumn(name = "AccountTypeID", referencedColumnName = "AccountTypeID", nullable = false)
    public AccountTypeInfo getAccountTypeInfoByAccountTypeId() {
        return accountTypeInfoByAccountTypeId;
    }

    public void setAccountTypeInfoByAccountTypeId(AccountTypeInfo accountTypeInfoByAccountTypeId) {
        this.accountTypeInfoByAccountTypeId = accountTypeInfoByAccountTypeId;
    }
}
