package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
public class UserInfo {
    private int userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private Integer occupationId;
    private Integer userTypeId;
    private String userName;
    private String password;
    private Boolean isEmail;
    private String mobileNumber;
    private Boolean isMobileNumberVerified;
    private String nid;
    private String cid;
    private Boolean isPoliceVerified;
    private int userCategoryId;
    private String gender;
    private Timestamp dob;
    private String address;
    private Double homeLatitude;
    private Double homeLongitude;
    private Double latestLatitude;
    private Double latestLongitude;
    private String bioDescription;
    private Boolean isSmoker;
    private Boolean isSongLover;
    private String facebookLink;
    private String linkedInLink;
    private String imageLocation;
    private String preferences;
    private String fcmKey;
    private Timestamp createdDate;
    private Boolean isProfileApprovedByAdmin;
    private Timestamp adminApprovedDate;
    private Integer approvedBy;
    private Boolean isActive;
    private Collection<AccountInfo> accountInfosByUserId;
    private Collection<ActivityInfo> activityInfosByUserId;
    private Collection<ActivityInfo> activityInfosByUserId_0;
    private Collection<CarInfo> carInfosByUserId;
    private Collection<PoliceVerificationInfo> policeVerificationInfosByUserId;
    private Collection<RequestInfo> requestInfosByUserId;
    private Collection<RequesterCollaborationLog> requesterCollaborationLogsByUserId;
    private Collection<RoadStatusLog> roadStatusLogsByUserId;
    private Collection<TransactionLog> transactionLogsByUserId;
    private Collection<VerifiedEmailLog> verifiedEmailLogsByUserId;
    private Collection<VerifiedMobileLog> verifiedMobileLogsByUserId;
    private Collection<Ratings> ratingsByUserId;
    private Collection<CarImage> carImagesByUserId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "FirstName", nullable = false, length = 150)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "MiddleName", nullable = true, length = 150)
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Basic
    @Column(name = "LastName", nullable = true, length = 150)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "OccupationID", nullable = true)
    public Integer getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(Integer occupationId) {
        this.occupationId = occupationId;
    }

    @Basic
    @Column(name = "UserTypeID", nullable = true)
    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    @Basic
    @Column(name = "UserName", nullable = false, length = 150)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "Password", nullable = false, length = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "isEmail", nullable = true)
    public Boolean getEmail() {
        return isEmail;
    }

    public void setEmail(Boolean email) {
        isEmail = email;
    }

    @Basic
    @Column(name = "MobileNumber", nullable = true, length = 20)
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Basic
    @Column(name = "IsMobileNumberVerified", nullable = true)
    public Boolean getMobileNumberVerified() {
        return isMobileNumberVerified;
    }

    public void setMobileNumberVerified(Boolean mobileNumberVerified) {
        isMobileNumberVerified = mobileNumberVerified;
    }

    @Basic
    @Column(name = "NID", nullable = true, length = 20)
    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    @Basic
    @Column(name = "CID", nullable = true, length = 20)
    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    @Basic
    @Column(name = "isPoliceVerified", nullable = true)
    public Boolean getPoliceVerified() {
        return isPoliceVerified;
    }

    public void setPoliceVerified(Boolean policeVerified) {
        isPoliceVerified = policeVerified;
    }

    @Basic
    @Column(name = "UserCategoryID", nullable = false)
    public int getUserCategoryId() {
        return userCategoryId;
    }

    public void setUserCategoryId(int userCategoryId) {
        this.userCategoryId = userCategoryId;
    }

    @Basic
    @Column(name = "Gender", nullable = false, length = 30)
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "DOB", nullable = true)
    public Timestamp getDob() {
        return dob;
    }

    public void setDob(Timestamp dob) {
        this.dob = dob;
    }

    @Basic
    @Column(name = "Address", nullable = true, length = 2147483647)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "HomeLatitude", nullable = true, precision = 0)
    public Double getHomeLatitude() {
        return homeLatitude;
    }

    public void setHomeLatitude(Double homeLatitude) {
        this.homeLatitude = homeLatitude;
    }

    @Basic
    @Column(name = "HomeLongitude", nullable = true, precision = 0)
    public Double getHomeLongitude() {
        return homeLongitude;
    }

    public void setHomeLongitude(Double homeLongitude) {
        this.homeLongitude = homeLongitude;
    }

    @Basic
    @Column(name = "LatestLatitude", nullable = true, precision = 0)
    public Double getLatestLatitude() {
        return latestLatitude;
    }

    public void setLatestLatitude(Double latestLatitude) {
        this.latestLatitude = latestLatitude;
    }

    @Basic
    @Column(name = "LatestLongitude", nullable = true, precision = 0)
    public Double getLatestLongitude() {
        return latestLongitude;
    }

    public void setLatestLongitude(Double latestLongitude) {
        this.latestLongitude = latestLongitude;
    }

    @Basic
    @Column(name = "BioDescription", nullable = true, length = 2147483647)
    public String getBioDescription() {
        return bioDescription;
    }

    public void setBioDescription(String bioDescription) {
        this.bioDescription = bioDescription;
    }

    @Basic
    @Column(name = "isSmoker", nullable = true)
    public Boolean getSmoker() {
        return isSmoker;
    }

    public void setSmoker(Boolean smoker) {
        isSmoker = smoker;
    }

    @Basic
    @Column(name = "isSongLover", nullable = true)
    public Boolean getSongLover() {
        return isSongLover;
    }

    public void setSongLover(Boolean songLover) {
        isSongLover = songLover;
    }

    @Basic
    @Column(name = "FacebookLink", nullable = true, length = 2147483647)
    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    @Basic
    @Column(name = "LinkedInLink", nullable = true, length = 2147483647)
    public String getLinkedInLink() {
        return linkedInLink;
    }

    public void setLinkedInLink(String linkedInLink) {
        this.linkedInLink = linkedInLink;
    }

    @Basic
    @Column(name = "ImageLocation", nullable = true, length = 2147483647)
    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    @Basic
    @Column(name = "Preferences", nullable = true, length = 250)
    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    @Basic
    @Column(name = "FCMKey", nullable = true, length = -1)
    public String getFcmKey() {
        return fcmKey;
    }

    public void setFcmKey(String fcmKey) {
        this.fcmKey = fcmKey;
    }

    @Basic
    @Column(name = "CreatedDate", nullable = true)
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Basic
    @Column(name = "isProfileApprovedByAdmin", nullable = true)
    public Boolean getProfileApprovedByAdmin() {
        return isProfileApprovedByAdmin;
    }

    public void setProfileApprovedByAdmin(Boolean profileApprovedByAdmin) {
        isProfileApprovedByAdmin = profileApprovedByAdmin;
    }

    @Basic
    @Column(name = "AdminApprovedDate", nullable = true)
    public Timestamp getAdminApprovedDate() {
        return adminApprovedDate;
    }

    public void setAdminApprovedDate(Timestamp adminApprovedDate) {
        this.adminApprovedDate = adminApprovedDate;
    }

    @Basic
    @Column(name = "ApprovedBy", nullable = true)
    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    @Basic
    @Column(name = "isActive", nullable = true)
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (userId != userInfo.userId) return false;
        if (userCategoryId != userInfo.userCategoryId) return false;
        if (firstName != null ? !firstName.equals(userInfo.firstName) : userInfo.firstName != null) return false;
        if (middleName != null ? !middleName.equals(userInfo.middleName) : userInfo.middleName != null) return false;
        if (lastName != null ? !lastName.equals(userInfo.lastName) : userInfo.lastName != null) return false;
        if (occupationId != null ? !occupationId.equals(userInfo.occupationId) : userInfo.occupationId != null)
            return false;
        if (userTypeId != null ? !userTypeId.equals(userInfo.userTypeId) : userInfo.userTypeId != null) return false;
        if (userName != null ? !userName.equals(userInfo.userName) : userInfo.userName != null) return false;
        if (password != null ? !password.equals(userInfo.password) : userInfo.password != null) return false;
        if (isEmail != null ? !isEmail.equals(userInfo.isEmail) : userInfo.isEmail != null) return false;
        if (mobileNumber != null ? !mobileNumber.equals(userInfo.mobileNumber) : userInfo.mobileNumber != null)
            return false;
        if (isMobileNumberVerified != null ? !isMobileNumberVerified.equals(userInfo.isMobileNumberVerified) : userInfo.isMobileNumberVerified != null)
            return false;
        if (nid != null ? !nid.equals(userInfo.nid) : userInfo.nid != null) return false;
        if (cid != null ? !cid.equals(userInfo.cid) : userInfo.cid != null) return false;
        if (isPoliceVerified != null ? !isPoliceVerified.equals(userInfo.isPoliceVerified) : userInfo.isPoliceVerified != null)
            return false;
        if (gender != null ? !gender.equals(userInfo.gender) : userInfo.gender != null) return false;
        if (dob != null ? !dob.equals(userInfo.dob) : userInfo.dob != null) return false;
        if (address != null ? !address.equals(userInfo.address) : userInfo.address != null) return false;
        if (homeLatitude != null ? !homeLatitude.equals(userInfo.homeLatitude) : userInfo.homeLatitude != null)
            return false;
        if (homeLongitude != null ? !homeLongitude.equals(userInfo.homeLongitude) : userInfo.homeLongitude != null)
            return false;
        if (latestLatitude != null ? !latestLatitude.equals(userInfo.latestLatitude) : userInfo.latestLatitude != null)
            return false;
        if (latestLongitude != null ? !latestLongitude.equals(userInfo.latestLongitude) : userInfo.latestLongitude != null)
            return false;
        if (bioDescription != null ? !bioDescription.equals(userInfo.bioDescription) : userInfo.bioDescription != null)
            return false;
        if (isSmoker != null ? !isSmoker.equals(userInfo.isSmoker) : userInfo.isSmoker != null) return false;
        if (isSongLover != null ? !isSongLover.equals(userInfo.isSongLover) : userInfo.isSongLover != null)
            return false;
        if (facebookLink != null ? !facebookLink.equals(userInfo.facebookLink) : userInfo.facebookLink != null)
            return false;
        if (linkedInLink != null ? !linkedInLink.equals(userInfo.linkedInLink) : userInfo.linkedInLink != null)
            return false;
        if (imageLocation != null ? !imageLocation.equals(userInfo.imageLocation) : userInfo.imageLocation != null)
            return false;
        if (preferences != null ? !preferences.equals(userInfo.preferences) : userInfo.preferences != null)
            return false;
        if (fcmKey != null ? !fcmKey.equals(userInfo.fcmKey) : userInfo.fcmKey != null) return false;
        if (createdDate != null ? !createdDate.equals(userInfo.createdDate) : userInfo.createdDate != null)
            return false;
        if (isProfileApprovedByAdmin != null ? !isProfileApprovedByAdmin.equals(userInfo.isProfileApprovedByAdmin) : userInfo.isProfileApprovedByAdmin != null)
            return false;
        if (adminApprovedDate != null ? !adminApprovedDate.equals(userInfo.adminApprovedDate) : userInfo.adminApprovedDate != null)
            return false;
        if (approvedBy != null ? !approvedBy.equals(userInfo.approvedBy) : userInfo.approvedBy != null) return false;
        if (isActive != null ? !isActive.equals(userInfo.isActive) : userInfo.isActive != null) return false;


        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (occupationId != null ? occupationId.hashCode() : 0);
        result = 31 * result + (userTypeId != null ? userTypeId.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (isEmail != null ? isEmail.hashCode() : 0);
        result = 31 * result + (mobileNumber != null ? mobileNumber.hashCode() : 0);
        result = 31 * result + (isMobileNumberVerified != null ? isMobileNumberVerified.hashCode() : 0);
        result = 31 * result + (nid != null ? nid.hashCode() : 0);
        result = 31 * result + (cid != null ? cid.hashCode() : 0);
        result = 31 * result + (isPoliceVerified != null ? isPoliceVerified.hashCode() : 0);
        result = 31 * result + userCategoryId;
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (dob != null ? dob.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (homeLatitude != null ? homeLatitude.hashCode() : 0);
        result = 31 * result + (homeLongitude != null ? homeLongitude.hashCode() : 0);
        result = 31 * result + (latestLatitude != null ? latestLatitude.hashCode() : 0);
        result = 31 * result + (latestLongitude != null ? latestLongitude.hashCode() : 0);
        result = 31 * result + (bioDescription != null ? bioDescription.hashCode() : 0);
        result = 31 * result + (isSmoker != null ? isSmoker.hashCode() : 0);
        result = 31 * result + (isSongLover != null ? isSongLover.hashCode() : 0);
        result = 31 * result + (facebookLink != null ? facebookLink.hashCode() : 0);
        result = 31 * result + (linkedInLink != null ? linkedInLink.hashCode() : 0);
        result = 31 * result + (imageLocation != null ? imageLocation.hashCode() : 0);
        result = 31 * result + (preferences != null ? preferences.hashCode() : 0);
        result = 31 * result + (fcmKey != null ? fcmKey.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (isProfileApprovedByAdmin != null ? isProfileApprovedByAdmin.hashCode() : 0);
        result = 31 * result + (adminApprovedDate != null ? adminApprovedDate.hashCode() : 0);
        result = 31 * result + (approvedBy != null ? approvedBy.hashCode() : 0);
        result = 31 * result + (isActive != null ? isActive.hashCode() : 0);

        return result;
    }

    @OneToMany(mappedBy = "userInfoByUserId")
    public Collection<AccountInfo> getAccountInfosByUserId() {
        return accountInfosByUserId;
    }

    public void setAccountInfosByUserId(Collection<AccountInfo> accountInfosByUserId) {
        this.accountInfosByUserId = accountInfosByUserId;
    }

    @OneToMany(mappedBy = "userInfoByDriverId")
    public Collection<ActivityInfo> getActivityInfosByUserId() {
        return activityInfosByUserId;
    }

    public void setActivityInfosByUserId(Collection<ActivityInfo> activityInfosByUserId) {
        this.activityInfosByUserId = activityInfosByUserId;
    }

    @OneToMany(mappedBy = "userInfoByPassengerId")
    public Collection<ActivityInfo> getActivityInfosByUserId_0() {
        return activityInfosByUserId_0;
    }

    public void setActivityInfosByUserId_0(Collection<ActivityInfo> activityInfosByUserId_0) {
        this.activityInfosByUserId_0 = activityInfosByUserId_0;
    }

    @OneToMany(mappedBy = "userInfoByUserId")
    public Collection<CarInfo> getCarInfosByUserId() {
        return carInfosByUserId;
    }

    public void setCarInfosByUserId(Collection<CarInfo> carInfosByUserId) {
        this.carInfosByUserId = carInfosByUserId;
    }

    @OneToMany(mappedBy = "userInfoByUserId")
    public Collection<PoliceVerificationInfo> getPoliceVerificationInfosByUserId() {
        return policeVerificationInfosByUserId;
    }

    public void setPoliceVerificationInfosByUserId(Collection<PoliceVerificationInfo> policeVerificationInfosByUserId) {
        this.policeVerificationInfosByUserId = policeVerificationInfosByUserId;
    }

    @OneToMany(mappedBy = "userInfoByRequesterId")
    public Collection<RequestInfo> getRequestInfosByUserId() {
        return requestInfosByUserId;
    }

    public void setRequestInfosByUserId(Collection<RequestInfo> requestInfosByUserId) {
        this.requestInfosByUserId = requestInfosByUserId;
    }

    @OneToMany(mappedBy = "userInfoByCommentsBy")
    public Collection<RequesterCollaborationLog> getRequesterCollaborationLogsByUserId() {
        return requesterCollaborationLogsByUserId;
    }

    public void setRequesterCollaborationLogsByUserId(Collection<RequesterCollaborationLog> requesterCollaborationLogsByUserId) {
        this.requesterCollaborationLogsByUserId = requesterCollaborationLogsByUserId;
    }

    @OneToMany(mappedBy = "userInfoByUpdatedBy")
    public Collection<RoadStatusLog> getRoadStatusLogsByUserId() {
        return roadStatusLogsByUserId;
    }

    public void setRoadStatusLogsByUserId(Collection<RoadStatusLog> roadStatusLogsByUserId) {
        this.roadStatusLogsByUserId = roadStatusLogsByUserId;
    }

    @OneToMany(mappedBy = "userInfoByPaidBy")
    public Collection<TransactionLog> getTransactionLogsByUserId() {
        return transactionLogsByUserId;
    }

    public void setTransactionLogsByUserId(Collection<TransactionLog> transactionLogsByUserId) {
        this.transactionLogsByUserId = transactionLogsByUserId;
    }

    @OneToMany(mappedBy = "userInfoByUserId")
    public Collection<VerifiedEmailLog> getVerifiedEmailLogsByUserId() {
        return verifiedEmailLogsByUserId;
    }

    public void setVerifiedEmailLogsByUserId(Collection<VerifiedEmailLog> verifiedEmailLogsByUserId) {
        this.verifiedEmailLogsByUserId = verifiedEmailLogsByUserId;
    }

    @OneToMany(mappedBy = "userInfoByUserId")
    public Collection<VerifiedMobileLog> getVerifiedMobileLogsByUserId() {
        return verifiedMobileLogsByUserId;
    }

    public void setVerifiedMobileLogsByUserId(Collection<VerifiedMobileLog> verifiedMobileLogsByUserId) {
        this.verifiedMobileLogsByUserId = verifiedMobileLogsByUserId;
    }

    @OneToMany(mappedBy = "userInfoByRatingBy")
    public Collection<Ratings> getRatingsByUserId() {
        return ratingsByUserId;
    }

    public void setRatingsByUserId(Collection<Ratings> ratingsByUserId) {
        this.ratingsByUserId = ratingsByUserId;
    }

    @OneToMany(mappedBy = "userInfoByUserId")
    public Collection<CarImage> getCarImagesByUserId() {
        return carImagesByUserId;
    }

    public void setCarImagesByUserId(Collection<CarImage> carImagesByUserId) {
        this.carImagesByUserId = carImagesByUserId;
    }
}
