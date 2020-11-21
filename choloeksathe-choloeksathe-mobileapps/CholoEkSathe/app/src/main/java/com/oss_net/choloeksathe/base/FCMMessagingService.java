package com.oss_net.choloeksathe.base;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.driver.DriverCarShareListInfoActivity;
import com.oss_net.choloeksathe.activities.driver.DriverHomeActivity;
import com.oss_net.choloeksathe.activities.driver.DriverNavigationActivity;
import com.oss_net.choloeksathe.activities.passenger.PassengerHomePageActivity;
import com.oss_net.choloeksathe.entity.databases.remote_model.NotificationsRoot;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;

import org.json.JSONObject;

public class FCMMessagingService extends FirebaseMessagingService {

    String notificationChannel = "my_notification_channel_01";
    public FCMMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        JSONObject object = new JSONObject(remoteMessage.getData());
        sendNotification(object.toString());
    }

    private void sendNotification(String body) {
        try {

            /*
                for notification,
                Basically notification indicate pendingIntent and activity, we try to use activity but
                by any change if we use fragment, we use local broadcast for send ack (acknowledge) to that particular
                fragment.
             */
            JSONObject object = new JSONObject(body);
            String messageBody = object.getString("message");

            NotificationsRoot root = new Gson().fromJson(messageBody, NotificationsRoot.class);
            if (root.type.equals("SHOW_INTEREST")) {
                // for driver
                // go to interest list for driver
                //showNotification(root.notificationEntity.details);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannel)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Passenger showing interest")
                        .setContentText(root.details)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{1000,1000,1000,1000,1000});


                Intent resultIntent = new Intent(this, DriverNavigationActivity.class);
                resultIntent.putExtra(CommonConstant.CAR_REQUEST_ID, root.request_id);
                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
                taskStackBuilder.addParentStack(DriverHomeActivity.class);
                taskStackBuilder.addNextIntent(resultIntent);
                PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(1, builder.build());

                //showNotificationToSystem(DriverCarShareListInfoActivity.class,builder,1);

            }else if(root.type.equals("INTEREST_CANCEL")){
                // for driver
                // go to interest list of driver
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannel)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Passenger cancel request")
                        .setContentText("Passenger cancel his own interest")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{1000,1000,1000,1000,1000});
                showNotificationToSystem(DriverNavigationActivity.class, builder,2);
            }else if(root.type.equals("INTEREST_ACCEPT")){
                // for passenger
                // go to passenger activity status fragment

                // call local broadcast manager
                Intent acceptIntent = new Intent();
                acceptIntent.setAction("com.oss-net.passenger");
                acceptIntent.putExtra("status", "accepted");
                LocalBroadcastManager.getInstance(this).sendBroadcast(acceptIntent);


                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannel)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Request accepted")
                        .setContentText("Car owner accept your interest")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{1000,1000,1000,1000,1000});

                CommonTask.saveDataIntoPreference(this, CommonConstant.REQUESTED_BY_PASSENGER, "1");

                showNotificationToSystem(PassengerHomePageActivity.class, builder,3);

            }else if(root.type.equals("INTEREST_REJECT")){
                // for passenger
                // go to passenger activity status fragment then open request for vehicle fragment

                Intent rejectIntent = new Intent();
                rejectIntent.setAction("com.oss-net.passenger");
                rejectIntent.putExtra("status", "rejected");
                LocalBroadcastManager.getInstance(this).sendBroadcast(rejectIntent);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannel)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Request rejected")
                        .setContentText("Car owner reject your interest")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{1000,1000,1000,1000,1000});

                CommonTask.saveDataIntoPreference(this, CommonConstant.REQUESTED_BY_PASSENGER, "");

                showNotificationToSystem(PassengerHomePageActivity.class, builder, 4);
            }else if(root.type.equals("DRIVER_REQUEST_CANCEL")){
                // for passenger
                // go to passenger activity status fragment and then open request for vehicle fragment
                Intent rejectIntent = new Intent();
                rejectIntent.setAction("com.oss-net.passenger");
                rejectIntent.putExtra("status", "driver_own_request_cancel");
                LocalBroadcastManager.getInstance(this).sendBroadcast(rejectIntent);


                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannel)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Request cancel")
                        .setContentText("Oops, car owner cancel this request.")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{1000,1000,1000,1000,1000});

                CommonTask.saveDataIntoPreference(this, CommonConstant.REQUESTED_BY_PASSENGER, "");

                showNotificationToSystem(PassengerHomePageActivity.class, builder, 5);
            }else if(root.type.equals("DRIVER_START_JOURNEY")){
                // for passenger
                // go to passenger activity status fragment

                // call local broadcast manager
                Intent acceptIntent = new Intent();
                acceptIntent.setAction("com.oss-net.passenger");
                acceptIntent.putExtra("status", "driver_start");
                LocalBroadcastManager.getInstance(this).sendBroadcast(acceptIntent);


                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannel)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Journey Start")
                        .setContentText("Car owner start his journey")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{1000,1000,1000,1000,1000});

                CommonTask.saveDataIntoPreference(this, CommonConstant.REQUESTED_BY_PASSENGER, "1");
                showNotificationToSystem(PassengerHomePageActivity.class, builder, 6);
            }else if(root.type.equals("PASSENGER_JOURNEY_STARTED")){
                // for passenger
                // go to passenger activity status fragment

                // call local broadcast manager
                Intent acceptIntent = new Intent();
                acceptIntent.setAction("com.oss-net.passenger");
                acceptIntent.putExtra("status", "passenger_start");
                LocalBroadcastManager.getInstance(this).sendBroadcast(acceptIntent);


                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannel)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Passenger journey start")
                        .setContentText("Your journey is start now.")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{1000,1000,1000,1000,1000});


                CommonTask.saveDataIntoPreference(this, CommonConstant.REQUESTED_BY_PASSENGER, "1");

                showNotificationToSystem(PassengerHomePageActivity.class, builder, 7);
            }else if(root.type.equals("PASSENGER_JOURNEY_COMPLETED")){
                // for passenger
                // go to passenger activity status fragment

                // call local broadcast manager
                Intent acceptIntent = new Intent();
                acceptIntent.setAction("com.oss-net.passenger");
                acceptIntent.putExtra("status", "passenger_completed");
                LocalBroadcastManager.getInstance(this).sendBroadcast(acceptIntent);


                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannel)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Passenger journey complete")
                        .setContentText("Your journey is successfully complete")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{1000,1000,1000,1000,1000});

                CommonTask.saveDataIntoPreference(this, CommonConstant.REQUESTED_BY_PASSENGER, "");
                showNotificationToSystem(PassengerHomePageActivity.class, builder, 8);
            }
        } catch (Exception ex) {
            CommonTask.showErrorLog(ex.getMessage());
        }

    }

    @Override
    public void onMessageSent(String s) {
    }

    private void showNotificationToSystem(Class<?> tClass, NotificationCompat.Builder builder, int id) {
        Intent resultIntent = new Intent(this, tClass);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(DriverHomeActivity.class);
        taskStackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(id, builder.build());
    }
}
