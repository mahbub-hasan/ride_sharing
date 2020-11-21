package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class AccountTypeInfo {
    private int accountTypeId;
    private String accountType;
    private String accountTypeProviderName;
    private String providerContact;
    private boolean isActive;
    private Collection<AccountInfo> accountInfosByAccountTypeId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountTypeID", nullable = false)
    public int getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(int accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    @Basic
    @Column(name = "AccountType", nullable = false, length = 50)
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Basic
    @Column(name = "AccountTypeProviderName", nullable = false, length = 150)
    public String getAccountTypeProviderName() {
        return accountTypeProviderName;
    }

    public void setAccountTypeProviderName(String accountTypeProviderName) {
        this.accountTypeProviderName = accountTypeProviderName;
    }

    @Basic
    @Column(name = "ProviderContact", nullable = false, length = 50)
    public String getProviderContact() {
        return providerContact;
    }

    public void setProviderContact(String providerContact) {
        this.providerContact = providerContact;
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

        AccountTypeInfo that = (AccountTypeInfo) o;

        if (accountTypeId != that.accountTypeId) return false;
        if (isActive != that.isActive) return false;
        if (accountType != null ? !accountType.equals(that.accountType) : that.accountType != null) return false;
        if (accountTypeProviderName != null ? !accountTypeProviderName.equals(that.accountTypeProviderName) : that.accountTypeProviderName != null)
            return false;
        if (providerContact != null ? !providerContact.equals(that.providerContact) : that.providerContact != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = accountTypeId;
        result = 31 * result + (accountType != null ? accountType.hashCode() : 0);
        result = 31 * result + (accountTypeProviderName != null ? accountTypeProviderName.hashCode() : 0);
        result = 31 * result + (providerContact != null ? providerContact.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }

    @OneToMany(mappedBy = "accountTypeInfoByAccountTypeId")
    public Collection<AccountInfo> getAccountInfosByAccountTypeId() {
        return accountInfosByAccountTypeId;
    }

    public void setAccountInfosByAccountTypeId(Collection<AccountInfo> accountInfosByAccountTypeId) {
        this.accountInfosByAccountTypeId = accountInfosByAccountTypeId;
    }
}
