package com.ossnetwork.choloeksatheserver.repository;

import com.ossnetwork.choloeksatheserver.model.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyInfo, Integer>{

    Collection<CompanyInfo> findByIsActive(int isActive);
}
