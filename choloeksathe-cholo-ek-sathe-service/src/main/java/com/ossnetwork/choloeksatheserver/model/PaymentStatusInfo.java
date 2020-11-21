package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class PaymentStatusInfo {
    private int paymentStatusId;
    private String paymentStatus;
    private boolean isActive;
    private Collection<TransactionLog> transactionLogsByPaymentStatusId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaymentStatusID", nullable = false)
    public int getPaymentStatusId() {
        return paymentStatusId;
    }

    public void setPaymentStatusId(int paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }

    @Basic
    @Column(name = "PaymentStatus", nullable = false, length = 50)
    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
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

        PaymentStatusInfo that = (PaymentStatusInfo) o;

        if (paymentStatusId != that.paymentStatusId) return false;
        if (isActive != that.isActive) return false;
        if (paymentStatus != null ? !paymentStatus.equals(that.paymentStatus) : that.paymentStatus != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = paymentStatusId;
        result = 31 * result + (paymentStatus != null ? paymentStatus.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }

    @OneToMany(mappedBy = "paymentStatusInfoByPaymentStatusId")
    public Collection<TransactionLog> getTransactionLogsByPaymentStatusId() {
        return transactionLogsByPaymentStatusId;
    }

    public void setTransactionLogsByPaymentStatusId(Collection<TransactionLog> transactionLogsByPaymentStatusId) {
        this.transactionLogsByPaymentStatusId = transactionLogsByPaymentStatusId;
    }
}
