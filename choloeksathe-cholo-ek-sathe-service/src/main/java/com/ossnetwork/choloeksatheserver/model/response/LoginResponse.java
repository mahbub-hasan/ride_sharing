package com.ossnetwork.choloeksatheserver.model.response;

public class LoginResponse extends BaseResponse {

    private int userId;
    private int carID;
    private int userCategoryId;
    private int requestId;
    private int activityId;
    private boolean isEmailVerified;
    private boolean isMobileVerified;
    private boolean isProfileApprovedByAdmin;
    private boolean isActive;
    private int numberOfSit;


    public LoginResponse(int statusCode, String message, boolean is_success) {
        super(statusCode, message, is_success);
    }

    public LoginResponse(int statusCode, String message, boolean is_success, int userId, int carID, int userCategoryId, int requestId, int activityId, boolean isEmailVerified, boolean isMobileVerified, boolean isProfileApprovedByAdmin, boolean isActive, int numberOfSit) {
        super(statusCode, message, is_success);
        this.userId = userId;
        this.carID = carID;
        this.userCategoryId = userCategoryId;
        this.requestId = requestId;
        this.activityId = activityId;
        this.isEmailVerified = isEmailVerified;
        this.isMobileVerified = isMobileVerified;
        this.isProfileApprovedByAdmin = isProfileApprovedByAdmin;
        this.isActive = isActive;
        this.numberOfSit = numberOfSit;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public int getUserCategoryId() {
        return userCategoryId;
    }

    public void setUserCategoryId(int userCategoryId) {
        this.userCategoryId = userCategoryId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public boolean isMobileVerified() {
        return isMobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        isMobileVerified = mobileVerified;
    }

    public boolean isProfileApprovedByAdmin() {
        return isProfileApprovedByAdmin;
    }

    public void setProfileApprovedByAdmin(boolean profileApprovedByAdmin) {
        isProfileApprovedByAdmin = profileApprovedByAdmin;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getNumberOfSit() {
        return numberOfSit;
    }

    public void setNumberOfSit(int numberOfSit) {
        this.numberOfSit = numberOfSit;
    }
}
