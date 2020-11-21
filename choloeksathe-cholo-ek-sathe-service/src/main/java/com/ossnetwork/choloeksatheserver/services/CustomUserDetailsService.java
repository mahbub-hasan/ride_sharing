package com.ossnetwork.choloeksatheserver.services;

import com.ossnetwork.choloeksatheserver.model.UserInfo;
import com.ossnetwork.choloeksatheserver.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoRepository.findByMobileNumberIsLike(mobileNumber);
        if(userInfo != null){
            ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new User(userInfo.getMobileNumber(), userInfo.getPassword(), userInfo.getActive(), userInfo.getActive(), userInfo.getActive(), userInfo.getActive(), grantedAuthorities);
        }

        return null;
    }
}
