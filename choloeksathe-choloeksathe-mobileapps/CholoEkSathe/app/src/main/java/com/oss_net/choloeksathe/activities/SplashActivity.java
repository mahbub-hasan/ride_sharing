package com.oss_net.choloeksathe.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.driver.DriverCurrentSessionActivity;
import com.oss_net.choloeksathe.activities.driver.DriverHomeActivity;
import com.oss_net.choloeksathe.activities.driver.DriverNavigationActivity;
import com.oss_net.choloeksathe.activities.passenger.PassengerHomePageActivity;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.base.DownloadService;
import com.oss_net.choloeksathe.base.FCMChangeService;
import com.oss_net.choloeksathe.entity.databases.remote_model.DriverActiveSession;
import com.oss_net.choloeksathe.entity.databases.remote_model.PassengerActiveSession;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.GetLocation;
import com.oss_net.choloeksathe.utils.RuntimePermissionHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SplashActivity extends AppCompatActivity implements RuntimePermissionHandler.RuntimePermissionListener {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private RuntimePermissionHandler handler;
    private GetLocation getLocation;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("com.oss-net.splash.confirm"));
        if(CommonTask.getDataFromPreference(this, CommonConstant.SPLASH_PERMISSION_ALLOW).isEmpty()){
            handler = new RuntimePermissionHandler(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, CommonConstant.RUNTIME_PERMISSION, this);
        }else{
            navigation();
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("com.oss-net.splash.confirm"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.oss-net.splash.confirm")){
                Bundle bundle = intent.getExtras();
                if(bundle != null){
                    String status = bundle.getString("status");
                    if(status.equals("sync_done")){
                        navigation();
                    }else{
                        CommonTask.showToast(SplashActivity.this, "Something error. Please re-start your application");
                    }
                }
            }
        }
    };

    @Override
    public void onAllow() {
        getLocation = new GetLocation(this);
        CommonTask.saveDataIntoPreference(this, CommonConstant.SPLASH_PERMISSION_ALLOW, "1");
        navigation();
    }

    @Override
    public void onDeny() {
        handler = new RuntimePermissionHandler(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, CommonConstant.RUNTIME_PERMISSION, this);
    }

    private void navigation(){
        final String userId = CommonTask.getDataFromPreference(SplashActivity.this, CommonConstant.USER_ID);
        final String userCategoryId = CommonTask.getDataFromPreference(SplashActivity.this, CommonConstant.USER_CATEGORY_ID);
        boolean isGPSEnable = CommonTask.checkGPS(this);
        boolean isNetEnable = CommonTask.checkInternet(this);
        if(isGPSEnable && isNetEnable){
            // both enable
            if(CommonTask.getDataFromPreference(this, CommonConstant.BACKGROUND_SYNC).isEmpty()){
                startService(new Intent(this, DownloadService.class));
            }else {
                // call server for get active session info
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (userId.isEmpty() && userCategoryId.isEmpty()) {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        } else if (!userId.isEmpty() && !userCategoryId.isEmpty() && Integer.parseInt(userCategoryId) == 2) {
                            checkPassengerActivityStatus(userId);
                        } else {
                            checkDriverActivityStatus(userId);
                        }
                    }
                }, 3000);
            }
        }else if(isGPSEnable && !isNetEnable){
            // net disable
            new AlertDialog.Builder(this)
                    .setMessage("Sorry! Internet is not enable.")
                    .setTitle("Internet Disable")
                    .setCancelable(false)
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .create()
                    .show();
        }else if(!isGPSEnable && isNetEnable){
            // gps disable
            new AlertDialog.Builder(this)
                    .setMessage("Sorry! Your location is not enable.")
                    .setTitle("GPS Disable")
                    .setCancelable(false)
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .create()
                    .show();
        }else{
            // both disable
            new AlertDialog.Builder(this)
                    .setMessage("Sorry! Your internet and gps both disable")
                    .setTitle("GPS & Internet Disable")
                    .setCancelable(false)
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .create()
                    .show();
        }

    }

    private void checkDriverActivityStatus(String userId) {
        String url = String.format(Locale.getDefault(), CommonURL.getInstance().driverActivityStatus,Integer.parseInt(userId));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                DriverActiveSession passengerActiveSession = new Gson().fromJson(response.toString(), DriverActiveSession.class);
                if(passengerActiveSession.isSuccess && passengerActiveSession.requestId>0){
                    // already in a session
                    CommonTask.saveDataIntoPreference(SplashActivity.this, CommonConstant.DRIVER_REQUEST_ID, String.valueOf(passengerActiveSession.requestId));
                    startActivity(new Intent(SplashActivity.this, DriverNavigationActivity.class));
                    finish();
                }else{
                    // no session
                    startActivity(new Intent(SplashActivity.this, DriverHomeActivity.class));
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = String.format(Locale.getDefault(), "%s:%s",
                        CommonTask.getDataFromPreference(SplashActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                        CommonTask.getDataFromPreference(SplashActivity.this, CommonConstant.USER_PASSWORD));
                String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                headers.put("Content-Type","application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(request);
    }

    private void checkPassengerActivityStatus(String userId) {
        String url = String.format(Locale.getDefault(), CommonURL.getInstance().passengerActivityStatus,Integer.parseInt(userId));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                PassengerActiveSession passengerActiveSession = new Gson().fromJson(response.toString(), PassengerActiveSession.class);
                if(passengerActiveSession.isSuccess && passengerActiveSession.activityId>0){
                    // already in a session
                    CommonTask.saveDataIntoPreference(SplashActivity.this,CommonConstant.REQUESTED_BY_PASSENGER,"1");
                    CommonTask.saveDataIntoPreference(SplashActivity.this, "ActivityID", String.valueOf(passengerActiveSession.activityId));
                    startActivity(new Intent(SplashActivity.this, PassengerHomePageActivity.class));
                    finish();
                }else{
                    // no session
                    startActivity(new Intent(SplashActivity.this, PassengerHomePageActivity.class));
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = String.format(Locale.getDefault(), "%s:%s",
                        CommonTask.getDataFromPreference(SplashActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                        CommonTask.getDataFromPreference(SplashActivity.this, CommonConstant.USER_PASSWORD));
                String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                headers.put("Content-Type","application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(request);
    }
}
