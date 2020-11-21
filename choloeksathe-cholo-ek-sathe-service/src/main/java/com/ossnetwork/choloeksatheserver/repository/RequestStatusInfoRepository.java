package com.ossnetwork.choloeksatheserver.repository;

import com.ossnetwork.choloeksatheserver.model.RequestStatusInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestStatusInfoRepository extends JpaRepository<RequestStatusInfo, Integer> {
}
