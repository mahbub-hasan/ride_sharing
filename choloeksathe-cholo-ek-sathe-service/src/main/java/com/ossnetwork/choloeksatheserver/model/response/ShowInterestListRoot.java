package com.ossnetwork.choloeksatheserver.model.response;

import java.util.List;

public class ShowInterestListRoot extends BaseResponse{
    public List<ShowInterestList> showInterestLists;

    public ShowInterestListRoot() {
    }

    public ShowInterestListRoot(int statusCode, String message, boolean is_success, List<ShowInterestList> showInterestLists) {
        super(statusCode, message, is_success);
        this.showInterestLists = showInterestLists;
    }

    public List<ShowInterestList> getShowInterestLists() {
        return showInterestLists;
    }

    public void setShowInterestLists(List<ShowInterestList> showInterestLists) {
        this.showInterestLists = showInterestLists;
    }
}
