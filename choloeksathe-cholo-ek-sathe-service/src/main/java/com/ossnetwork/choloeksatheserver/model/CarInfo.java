package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
public class CarInfo {
    private int carInfoId;
    private String carNumber;
    private String carModels;
    private Integer carType;
    private String owner;
    private String ownerContact;
    private boolean isAc;
    private String image;
    private Timestamp startDate;
    private String detailsSpecification;
    private String driverName;
    private String driverMobile;
    private String driverRegNo;
    private boolean isActive;
    private Integer numberofSeat;
    private UserInfo userInfoByUserId;
    private Collection<CarDocument> carDocumentsByCarInfoId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CarInfoID", nullable = false)
    public int getCarInfoId() {
        return carInfoId;
    }

    public void setCarInfoId(int carInfoId) {
        this.carInfoId = carInfoId;
    }

    @Basic
    @Column(name = "CARNumber", nullable = false, length = 150)
    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    @Basic
    @Column(name = "CARModels", nullable = false, length = 150)
    public String getCarModels() {
        return carModels;
    }

    public void setCarModels(String carModels) {
        this.carModels = carModels;
    }

    @Basic
    @Column(name = "CARType", nullable = true)
    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

    @Basic
    @Column(name = "Owner", nullable = true, length = 150)
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Basic
    @Column(name = "OwnerContact", nullable = true, length = 20)
    public String getOwnerContact() {
        return ownerContact;
    }

    public void setOwnerContact(String ownerContact) {
        this.ownerContact = ownerContact;
    }

    @Basic
    @Column(name = "isAC", nullable = true)
    public boolean getAc() {
        return isAc;
    }

    public void setAc(boolean ac) {
        isAc = ac;
    }

    @Basic
    @Column(name = "Image", nullable = true, length = 2147483647)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Basic
    @Column(name = "StartDate", nullable = true)
    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "DetailsSpecification", nullable = true, length = 2147483647)
    public String getDetailsSpecification() {
        return detailsSpecification;
    }

    public void setDetailsSpecification(String detailsSpecification) {
        this.detailsSpecification = detailsSpecification;
    }

    @Basic
    @Column(name = "DriverName", nullable = true, length = 50)
    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    @Basic
    @Column(name = "DriverMobile", nullable = true, length = 20)
    public String getDriverMobile() {
        return driverMobile;
    }

    public void setDriverMobile(String driverMobile) {
        this.driverMobile = driverMobile;
    }

    @Basic
    @Column(name = "DriverRegNo", nullable = true, length = 50)
    public String getDriverRegNo() {
        return driverRegNo;
    }

    public void setDriverRegNo(String driverRegNo) {
        this.driverRegNo = driverRegNo;
    }

    @Basic
    @Column(name = "isActive", nullable = false)
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Basic
    @Column(name = "NumberofSeat", nullable = true)
    public Integer getNumberofSeat() {
        return numberofSeat;
    }

    public void setNumberofSeat(Integer numberofSeat) {
        this.numberofSeat = numberofSeat;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarInfo carInfo = (CarInfo) o;

        if (carInfoId != carInfo.carInfoId) return false;
        if (isActive != carInfo.isActive) return false;
        if (carNumber != null ? !carNumber.equals(carInfo.carNumber) : carInfo.carNumber != null) return false;
        if (carModels != null ? !carModels.equals(carInfo.carModels) : carInfo.carModels != null) return false;
        if (carType != null ? !carType.equals(carInfo.carType) : carInfo.carType != null) return false;
        if (owner != null ? !owner.equals(carInfo.owner) : carInfo.owner != null) return false;
        if (ownerContact != null ? !ownerContact.equals(carInfo.ownerContact) : carInfo.ownerContact != null)
            return false;
        if (isAc != carInfo.isAc) return false;
        if (image != null ? !image.equals(carInfo.image) : carInfo.image != null) return false;
        if (startDate != null ? !startDate.equals(carInfo.startDate) : carInfo.startDate != null) return false;
        if (detailsSpecification != null ? !detailsSpecification.equals(carInfo.detailsSpecification) : carInfo.detailsSpecification != null)
            return false;
        if (driverName != null ? !driverName.equals(carInfo.driverName) : carInfo.driverName != null) return false;
        if (driverMobile != null ? !driverMobile.equals(carInfo.driverMobile) : carInfo.driverMobile != null)
            return false;
        if (driverRegNo != null ? !driverRegNo.equals(carInfo.driverRegNo) : carInfo.driverRegNo != null) return false;

        if (numberofSeat != null ? !numberofSeat.equals(carInfo.numberofSeat) : carInfo.numberofSeat != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = carInfoId;
        result = 31 * result + (carNumber != null ? carNumber.hashCode() : 0);
        result = 31 * result + (carModels != null ? carModels.hashCode() : 0);
        result = 31 * result + (carType != null ? carType.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (ownerContact != null ? ownerContact.hashCode() : 0);
        result = 31 * result + (isAc ? 1: 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (detailsSpecification != null ? detailsSpecification.hashCode() : 0);
        result = 31 * result + (driverName != null ? driverName.hashCode() : 0);
        result = 31 * result + (driverMobile != null ? driverMobile.hashCode() : 0);
        result = 31 * result + (driverRegNo != null ? driverRegNo.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        result = 31 * result + (numberofSeat != null ? numberofSeat.hashCode() : 0);

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

    @OneToMany(mappedBy = "carInfoByCarInfoId")
    public Collection<CarDocument> getCarDocumentsByCarInfoId() {
        return carDocumentsByCarInfoId;
    }

    public void setCarDocumentsByCarInfoId(Collection<CarDocument> carDocumentsByCarInfoId) {
        this.carDocumentsByCarInfoId = carDocumentsByCarInfoId;
    }
}
