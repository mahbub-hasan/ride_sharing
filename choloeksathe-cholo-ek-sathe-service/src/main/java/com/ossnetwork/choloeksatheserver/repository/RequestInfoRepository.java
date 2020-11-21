package com.ossnetwork.choloeksatheserver.repository;

import com.ossnetwork.choloeksatheserver.model.RequestInfo;
import com.ossnetwork.choloeksatheserver.model.RequestStatusInfo;
import com.ossnetwork.choloeksatheserver.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Repository
public interface RequestInfoRepository extends JpaRepository<RequestInfo, Integer> {


    Collection<RequestInfo> findByUserInfoByRequesterIdAndRequestDate(UserInfo userInfo, Date date);

    Collection<RequestInfo> findAllByStartTimeBetweenAndRequestStatusInfoByRequestStatusIdOrderByRequestIdDesc(
            Timestamp startTime, Timestamp endTime, RequestStatusInfo requestStatusInfo);

    RequestInfo findTop1ByUserInfoByRequesterIdOrderByRequestIdDesc(UserInfo userInfo);

    List<RequestInfo> findTop2ByUserInfoByRequesterIdOrderByRequestIdDesc(UserInfo userInfo);

    Collection<RequestInfo> findByUserInfoByRequesterIdOrderByRequestIdDesc(UserInfo userInfo);

}
