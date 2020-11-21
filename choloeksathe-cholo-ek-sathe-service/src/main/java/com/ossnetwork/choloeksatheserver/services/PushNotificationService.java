package com.ossnetwork.choloeksatheserver.services;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.ossnetwork.choloeksatheserver.utils.CommonConstant;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PushNotificationService {


    public void sendSingleUserNotificaiton(String userFCMKey, JSONObject jsonObject){
        try {
            Sender sender = new Sender(CommonConstant.API_KEY);
            Message message = new Message.Builder()
                    .addData("message", jsonObject.toString())
                    .timeToLive(30)
                    .build();
            Result result = sender.send(message,userFCMKey,0);
            if(StringUtils.isEmpty(result.getErrorCodeName())){
                System.out.println("Notification successfully send to user");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    public void sendMultipleNotification(List<String> userFCMKeys, JSONObject jsonObject){
        try {
            Sender sender = new Sender(CommonConstant.API_KEY);
            Message message = new Message.Builder()
                    .addData("message", jsonObject.toString())
                    .timeToLive(30)
                    .build();
            MulticastResult result = sender.send(message, userFCMKeys,0);
            for (Result results : result.getResults()) {
                if(StringUtils.isEmpty(results.getErrorCodeName())){
                    System.out.println("Notification successfully send to user");
                }
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
