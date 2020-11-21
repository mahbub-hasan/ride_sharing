package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class TransactionTypeInfo {
    private int transactionTypeId;
    private String transactionType;
    private boolean isActive;
    private Collection<TransactionLog> transactionLogsByTransactionTypeId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransactionTypeID", nullable = false)
    public int getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(int transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    @Basic
    @Column(name = "TransactionType", nullable = false, length = 50)
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
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

        TransactionTypeInfo that = (TransactionTypeInfo) o;

        if (transactionTypeId != that.transactionTypeId) return false;
        if (isActive != that.isActive) return false;
        if (transactionType != null ? !transactionType.equals(that.transactionType) : that.transactionType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = transactionTypeId;
        result = 31 * result + (transactionType != null ? transactionType.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }

    @OneToMany(mappedBy = "transactionTypeInfoByTransactionTypeId")
    public Collection<TransactionLog> getTransactionLogsByTransactionTypeId() {
        return transactionLogsByTransactionTypeId;
    }

    public void setTransactionLogsByTransactionTypeId(Collection<TransactionLog> transactionLogsByTransactionTypeId) {
        this.transactionLogsByTransactionTypeId = transactionLogsByTransactionTypeId;
    }
}
