package com.ossnetwork.choloeksatheserver.model.response;

public class FCMKeyChangeRequest {
    private int userId;
    private String fcmKey;

    public FCMKeyChangeRequest() {
    }

    public FCMKeyChangeRequest(int userId, String fcmKey) {
        this.userId = userId;
        this.fcmKey = fcmKey;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFcmKey() {
        return fcmKey;
    }

    public void setFcmKey(String fcmKey) {
        this.fcmKey = fcmKey;
    }
}
