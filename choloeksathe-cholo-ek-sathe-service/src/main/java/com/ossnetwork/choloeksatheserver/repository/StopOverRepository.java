package com.ossnetwork.choloeksatheserver.repository;

import com.ossnetwork.choloeksatheserver.model.StopOverInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StopOverRepository extends JpaRepository<StopOverInfo, Integer> {
}
