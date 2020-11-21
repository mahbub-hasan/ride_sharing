package com.ossnetwork.choloeksatheserver.model.response;

import java.util.List;

public class CompanyListRoot extends BaseResponse {

    private List<CompanyList> companyList;

    public CompanyListRoot(int statusCode, String message, boolean is_success) {
        super(statusCode, message, is_success);
    }

    public CompanyListRoot(int statusCode, String message, boolean is_success, List<CompanyList> companyList) {
        super(statusCode, message, is_success);
        this.companyList = companyList;
    }

    public List<CompanyList> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<CompanyList> companyList) {
        this.companyList = companyList;
    }
}
