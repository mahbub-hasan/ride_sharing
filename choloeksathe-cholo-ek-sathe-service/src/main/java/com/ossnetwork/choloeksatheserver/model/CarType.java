package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class CarType {
    private int carTypeId;
    private String carTypeName;
    private String carTypeDetails;
    private Integer createdBy;
    private Timestamp createdDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CarTypeId", nullable = false)
    public int getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(int carTypeId) {
        this.carTypeId = carTypeId;
    }

    @Basic
    @Column(name = "CarTypeName", nullable = true, length = 150)
    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    @Basic
    @Column(name = "CarTypeDetails", nullable = true, length = 250)
    public String getCarTypeDetails() {
        return carTypeDetails;
    }

    public void setCarTypeDetails(String carTypeDetails) {
        this.carTypeDetails = carTypeDetails;
    }

    @Basic
    @Column(name = "CreatedBy", nullable = true)
    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
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

        CarType carType = (CarType) o;

        if (carTypeId != carType.carTypeId) return false;
        if (carTypeName != null ? !carTypeName.equals(carType.carTypeName) : carType.carTypeName != null) return false;
        if (carTypeDetails != null ? !carTypeDetails.equals(carType.carTypeDetails) : carType.carTypeDetails != null)
            return false;
        if (createdBy != null ? !createdBy.equals(carType.createdBy) : carType.createdBy != null) return false;
        if (createdDate != null ? !createdDate.equals(carType.createdDate) : carType.createdDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = carTypeId;
        result = 31 * result + (carTypeName != null ? carTypeName.hashCode() : 0);
        result = 31 * result + (carTypeDetails != null ? carTypeDetails.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }
}
