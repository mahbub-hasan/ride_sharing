package com.ossnetwork.choloeksatheserver.repository;

import com.ossnetwork.choloeksatheserver.model.ActivityInfo;
import com.ossnetwork.choloeksatheserver.model.ActivityStatusInfo;
import com.ossnetwork.choloeksatheserver.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ActivityInfoRepository extends JpaRepository<ActivityInfo, Integer> {

    Collection<ActivityInfo> findByUserInfoByPassengerId(UserInfo userInfo);

    ActivityInfo findTop1ByUserInfoByPassengerIdOrderByActivityIdDesc(UserInfo userInfo);

    Collection<ActivityInfo> findByUserInfoByPassengerIdAndActivityStatusInfoByActivityStatusIdOrderByActivityIdDesc(UserInfo userInfo, ActivityStatusInfo activityStatusInfo);


}
