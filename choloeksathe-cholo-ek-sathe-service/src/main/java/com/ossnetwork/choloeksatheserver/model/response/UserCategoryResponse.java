package com.ossnetwork.choloeksatheserver.model.response;

public class UserCategoryResponse {
    private int userCategoryId;
    private String userCategory;
    private boolean isActive;

    public UserCategoryResponse(int userCategoryId, String userCategory, boolean isActive) {
        this.userCategoryId = userCategoryId;
        this.userCategory = userCategory;
        this.isActive = isActive;
    }

    public int getUserCategoryId() {
        return userCategoryId;
    }

    public void setUserCategoryId(int userCategoryId) {
        this.userCategoryId = userCategoryId;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
