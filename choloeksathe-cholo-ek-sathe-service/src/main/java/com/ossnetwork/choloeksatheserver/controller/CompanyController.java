package com.ossnetwork.choloeksatheserver.controller;

import com.ossnetwork.choloeksatheserver.model.CompanyInfo;
import com.ossnetwork.choloeksatheserver.model.response.CompanyList;
import com.ossnetwork.choloeksatheserver.model.response.CompanyListRoot;
import com.ossnetwork.choloeksatheserver.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/company")
public class CompanyController {

    @Autowired
    CompanyRepository companyRepository;

    @GetMapping(value = "/all-companies")
    public CompanyListRoot getAllCompanies(){
        Collection<CompanyInfo> companyInfos = companyRepository.findByIsActive(1);
        if(companyInfos != null && companyInfos.size()>0){
            List<CompanyList> companyLists = companyInfos.stream()
                    .map(companyInfo -> new CompanyList(companyInfo.getCompanyId(), companyInfo.getCompanyName(), companyInfo.getCompanyDetails()))
                    .collect(Collectors.toList());
            return new CompanyListRoot(HttpStatus.OK.value(), "Success", true, companyLists);
        }else{
            return new CompanyListRoot(HttpStatus.NOT_FOUND.value(), "No company found", false);
        }
    }
}
