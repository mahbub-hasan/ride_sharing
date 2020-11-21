package com.ossnetwork.choloeksatheserver.model.response;

public class CompanyList {
    private int companyId;
    private String companyName;
    private String companyDetails;

    public CompanyList(int companyId, String companyName, String companyDetails) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.companyDetails = companyDetails;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDetails() {
        return companyDetails;
    }

    public void setCompanyDetails(String companyDetails) {
        this.companyDetails = companyDetails;
    }
}
