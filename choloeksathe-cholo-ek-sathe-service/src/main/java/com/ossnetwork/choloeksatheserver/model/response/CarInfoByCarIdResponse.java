package com.ossnetwork.choloeksatheserver.model.response;

import com.ossnetwork.choloeksatheserver.model.UserInfo;

import java.sql.Timestamp;

public class CarInfoByCarIdResponse {
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
    private int sitNumber;

    public CarInfoByCarIdResponse(int carInfoId, String carNumber, String carModels, Integer carType, String owner, String ownerContact, boolean isAc, String image, Timestamp startDate, String detailsSpecification, String driverName, String driverMobile, String driverRegNo, boolean isActive, int sit) {
        this.carInfoId = carInfoId;
        this.carNumber = carNumber;
        this.carModels = carModels;
        this.carType = carType;
        this.owner = owner;
        this.ownerContact = ownerContact;
        this.isAc = isAc;
        this.image = image;
        this.startDate = startDate;
        this.detailsSpecification = detailsSpecification;
        this.driverName = driverName;
        this.driverMobile = driverMobile;
        this.driverRegNo = driverRegNo;
        this.isActive = isActive;
        this.sitNumber = sit;
    }

    public int getCarInfoId() {
        return carInfoId;
    }

    public void setCarInfoId(int carInfoId) {
        this.carInfoId = carInfoId;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarModels() {
        return carModels;
    }

    public void setCarModels(String carModels) {
        this.carModels = carModels;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerContact() {
        return ownerContact;
    }

    public void setOwnerContact(String ownerContact) {
        this.ownerContact = ownerContact;
    }

    public boolean isAc() {
        return isAc;
    }

    public void setAc(boolean ac) {
        isAc = ac;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public String getDetailsSpecification() {
        return detailsSpecification;
    }

    public void setDetailsSpecification(String detailsSpecification) {
        this.detailsSpecification = detailsSpecification;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverMobile() {
        return driverMobile;
    }

    public void setDriverMobile(String driverMobile) {
        this.driverMobile = driverMobile;
    }

    public String getDriverRegNo() {
        return driverRegNo;
    }

    public void setDriverRegNo(String driverRegNo) {
        this.driverRegNo = driverRegNo;
    }

    public int getSitNumber() {
        return sitNumber;
    }

    public void setSitNumber(int sitNumber) {
        this.sitNumber = sitNumber;
    }
}
