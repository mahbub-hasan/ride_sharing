package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class TransactionLog {
    private int transactionLogId;
    private double amount;
    private String description;
    private Timestamp processingTime;
    private TransactionTypeInfo transactionTypeInfoByTransactionTypeId;
    private ActivityInfo activityInfoByActivityId;
    private UserInfo userInfoByPaidBy;
    private PaymentStatusInfo paymentStatusInfoByPaymentStatusId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransactionLogID", nullable = false)
    public int getTransactionLogId() {
        return transactionLogId;
    }

    public void setTransactionLogId(int transactionLogId) {
        this.transactionLogId = transactionLogId;
    }

    @Basic
    @Column(name = "Amount", nullable = false, precision = 0)
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "Description", nullable = false, length = 50)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "ProcessingTime", nullable = false)
    public Timestamp getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Timestamp processingTime) {
        this.processingTime = processingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionLog that = (TransactionLog) o;

        if (transactionLogId != that.transactionLogId) return false;
        if (Double.compare(that.amount, amount) != 0) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (processingTime != null ? !processingTime.equals(that.processingTime) : that.processingTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = transactionLogId;
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (processingTime != null ? processingTime.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "TransactionTypeID", referencedColumnName = "TransactionTypeID", nullable = false)
    public TransactionTypeInfo getTransactionTypeInfoByTransactionTypeId() {
        return transactionTypeInfoByTransactionTypeId;
    }

    public void setTransactionTypeInfoByTransactionTypeId(TransactionTypeInfo transactionTypeInfoByTransactionTypeId) {
        this.transactionTypeInfoByTransactionTypeId = transactionTypeInfoByTransactionTypeId;
    }

    @ManyToOne
    @JoinColumn(name = "ActivityID", referencedColumnName = "ActivityID", nullable = false)
    public ActivityInfo getActivityInfoByActivityId() {
        return activityInfoByActivityId;
    }

    public void setActivityInfoByActivityId(ActivityInfo activityInfoByActivityId) {
        this.activityInfoByActivityId = activityInfoByActivityId;
    }

    @ManyToOne
    @JoinColumn(name = "PaidBy", referencedColumnName = "UserID", nullable = false)
    public UserInfo getUserInfoByPaidBy() {
        return userInfoByPaidBy;
    }

    public void setUserInfoByPaidBy(UserInfo userInfoByPaidBy) {
        this.userInfoByPaidBy = userInfoByPaidBy;
    }

    @ManyToOne
    @JoinColumn(name = "PaymentStatusID", referencedColumnName = "PaymentStatusID", nullable = false)
    public PaymentStatusInfo getPaymentStatusInfoByPaymentStatusId() {
        return paymentStatusInfoByPaymentStatusId;
    }

    public void setPaymentStatusInfoByPaymentStatusId(PaymentStatusInfo paymentStatusInfoByPaymentStatusId) {
        this.paymentStatusInfoByPaymentStatusId = paymentStatusInfoByPaymentStatusId;
    }
}
