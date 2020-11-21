package com.ossnetwork.choloeksatheserver.model.response;

import java.util.List;

public class CarShareInfoByUserIdResponse  extends BaseResponse{

    private List<CarShareListResponse> responses;

    public CarShareInfoByUserIdResponse(int statusCode, String message, boolean is_success, List<CarShareListResponse> responses) {
        super(statusCode, message, is_success);
        this.responses = responses;
    }

    public List<CarShareListResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<CarShareListResponse> responses) {
        this.responses = responses;
    }
}
