package com.oss_net.choloeksathe.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;

public class FCMInstanceIDService extends FirebaseInstanceIdService {
    public FCMInstanceIDService() {
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        CommonTask.saveDataIntoPreference(this, CommonConstant.FCM_KEY, refreshedToken);

    }
}
