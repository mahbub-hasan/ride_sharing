package com.ossnetwork.choloeksatheserver.model.response;

public class JourneyTypeResponse {
    private int journeyTypeId;
    private String journeyType;
    private boolean isActive;

    public JourneyTypeResponse(int journeyTypeId, String journeyType, boolean isActive) {
        this.journeyTypeId = journeyTypeId;
        this.journeyType = journeyType;
        this.isActive = isActive;
    }

    public int getJourneyTypeId() {
        return journeyTypeId;
    }

    public void setJourneyTypeId(int journeyTypeId) {
        this.journeyTypeId = journeyTypeId;
    }

    public String getJourneyType() {
        return journeyType;
    }

    public void setJourneyType(String journeyType) {
        this.journeyType = journeyType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
