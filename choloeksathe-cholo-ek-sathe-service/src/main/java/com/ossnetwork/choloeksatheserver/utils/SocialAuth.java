package com.ossnetwork.choloeksatheserver.utils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SocialAuth extends Authenticator {
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(CommonConstant.FROM_ADDRESS, CommonConstant.PASSWORD);
    }
}
