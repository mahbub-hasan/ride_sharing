package com.ossnetwork.choloeksatheserver.repository;

import com.ossnetwork.choloeksatheserver.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

    UserInfo findByUserName(String email);

    UserInfo findByMobileNumberIsLike(String mobileNo);

    Optional<UserInfo> findByMobileNumberAndPassword(String mobileNumber, String password);

    Optional<UserInfo> findByUserId(int id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE UserInfo userinfo SET userinfo.fcmKey =:fcm where userinfo.userId =:id")
    void updateFcmKey(@Param(value = "fcm") String fcm, @Param(value = "id") int id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE UserInfo user_info set user_info.fcmKey='' where user_info.userId =:id")
    void userLogout(@Param(value = "id") int id);
}
