package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class CarDocument {
    private int id;
    private Integer userId;
    private String carDocument;
    private Timestamp createDate;
    private Boolean isApproved;
    private String documentTitle;
    private CarInfo carInfoByCarInfoId;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "UserID", nullable = true)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "CarDocument", nullable = true, length = 2147483647)
    public String getCarDocument() {
        return carDocument;
    }

    public void setCarDocument(String carDocument) {
        this.carDocument = carDocument;
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

    @Basic
    @Column(name = "DocumentTitle", nullable = true, length = 250)
    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarDocument that = (CarDocument) o;

        if (id != that.id) return false;
        if (documentTitle != null ? !documentTitle.equals(that.documentTitle) : that.documentTitle != null)
            return false;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (carDocument != null ? !carDocument.equals(that.carDocument) : that.carDocument != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (isApproved != null ? !isApproved.equals(that.isApproved) : that.isApproved != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (carDocument != null ? carDocument.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (isApproved != null ? isApproved.hashCode() : 0);
        result = 31 * result + (documentTitle != null ? documentTitle.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "CarInfoID", referencedColumnName = "CarInfoID", nullable = false)
    public CarInfo getCarInfoByCarInfoId() {
        return carInfoByCarInfoId;
    }

    public void setCarInfoByCarInfoId(CarInfo carInfoByCarInfoId) {
        this.carInfoByCarInfoId = carInfoByCarInfoId;
    }

}
