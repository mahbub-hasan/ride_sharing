package com.ossnetwork.choloeksatheserver.model.response;

public class DriverDashboard {
    private String fullName;
    private String contactNumber;
    private String imageUrl;
    private boolean active;
    private boolean adminActive;
    private double totalRating;
    private double totalIncome;
    private double lastRequestIncome;
    private double currentRequestIncome;

    public DriverDashboard() {

    }

    public DriverDashboard(String fullName, String contactNumber, String imageUrl, boolean active, boolean adminActive, double totalRating, double totalIncome, double lastRequestIncome, double currentRequestIncome) {
        this.fullName = fullName;
        this.contactNumber = contactNumber;
        this.imageUrl = imageUrl;
        this.active = active;
        this.adminActive = adminActive;
        this.totalRating = totalRating;
        this.totalIncome = totalIncome;
        this.lastRequestIncome = lastRequestIncome;
        this.currentRequestIncome = currentRequestIncome;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getLastRequestIncome() {
        return lastRequestIncome;
    }

    public void setLastRequestIncome(double lastRequestIncome) {
        this.lastRequestIncome = lastRequestIncome;
    }

    public double getCurrentRequestIncome() {
        return currentRequestIncome;
    }

    public void setCurrentRequestIncome(double currentRequestIncome) {
        this.currentRequestIncome = currentRequestIncome;
    }

    public double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(double totalRating) {
        this.totalRating = totalRating;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAdminActive() {
        return adminActive;
    }

    public void setAdminActive(boolean adminActive) {
        this.adminActive = adminActive;
    }
}
