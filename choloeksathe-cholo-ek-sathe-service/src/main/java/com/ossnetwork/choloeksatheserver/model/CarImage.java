package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class CarImage {
    private int id;
    private String carImage;
    private Timestamp createDate;
    private Boolean isApproved;
    private UserInfo userInfoByUserId;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CarImage", nullable = true, length = 2147483647)
    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    @Basic
    @Column(name = "CreateDate", nullable = true)
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "IsApproved", nullable = true)
    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarImage carImage1 = (CarImage) o;

        if (id != carImage1.id) return false;
        if (carImage != null ? !carImage.equals(carImage1.carImage) : carImage1.carImage != null) return false;
        if (createDate != null ? !createDate.equals(carImage1.createDate) : carImage1.createDate != null) return false;
        if (isApproved != null ? !isApproved.equals(carImage1.isApproved) : carImage1.isApproved != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (carImage != null ? carImage.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (isApproved != null ? isApproved.hashCode() : 0);
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
