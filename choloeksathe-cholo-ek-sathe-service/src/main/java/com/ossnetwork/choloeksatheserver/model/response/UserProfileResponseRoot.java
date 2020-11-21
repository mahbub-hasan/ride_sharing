package com.ossnetwork.choloeksatheserver.model.response;

public class UserProfileResponseRoot extends BaseResponse{

    private UserProfileResponse userProfileResponse;

    public UserProfileResponseRoot() {
    }

    public UserProfileResponseRoot(int statusCode, String message, boolean is_success, UserProfileResponse userProfileResponse) {
        super(statusCode, message, is_success);
        this.userProfileResponse = userProfileResponse;
    }

    public UserProfileResponse getUserProfileResponse() {
        return userProfileResponse;
    }

    public void setUserProfileResponse(UserProfileResponse userProfileResponse) {
        this.userProfileResponse = userProfileResponse;
    }
}
