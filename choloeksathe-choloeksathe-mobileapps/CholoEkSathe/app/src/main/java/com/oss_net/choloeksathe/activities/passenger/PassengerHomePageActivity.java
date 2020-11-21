package com.oss_net.choloeksathe.activities.passenger;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.LoginActivity;
import com.oss_net.choloeksathe.activities.driver.DriverNavigationActivity;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.base.FCMChangeService;
import com.oss_net.choloeksathe.entity.databases.remote_model.BaseResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.PassengerHomePageRequestRoot;
import com.oss_net.choloeksathe.entity.databases.remote_model.UserProfileRoot;
import com.oss_net.choloeksathe.fragments.passenger.ActiveSessionFragment;
import com.oss_net.choloeksathe.fragments.passenger.RequestForVehicelsFragment;
import com.oss_net.choloeksathe.utils.CircularImageView;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.RuntimePermissionHandler;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class  PassengerHomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        RuntimePermissionHandler.RuntimePermissionListener{

    private Fragment fragment;
    FragmentTransaction fragmentTransaction;
    private ProgressDialog progressDialog;
    private CircleImageView passengerImage;
    private TextView passengerFullName, passengerMobileNumber,passengerRating;
    private ImageView profileEdit;
    public DrawerLayout drawer;
    private RuntimePermissionHandler handler;
    private  PassengerHomePageRequestRoot root;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startService(new Intent(this, FCMChangeService.class));


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(3);
        notificationManager.cancel(4);
        notificationManager.cancel(5);
        notificationManager.cancel(6);
        notificationManager.cancel(7);
        notificationManager.cancel(8);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        passengerImage = navigationView.getHeaderView(0).findViewById(R.id.passengerImage);
        passengerFullName = navigationView.getHeaderView(0).findViewById(R.id.passengerFullName);
        passengerMobileNumber = navigationView.getHeaderView(0).findViewById(R.id.passengerMobileNumber);
        profileEdit = navigationView.getHeaderView(0).findViewById(R.id.editProfile);
        passengerRating = navigationView.getHeaderView(0).findViewById(R.id.rating);

        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PassengerHomePageActivity.this,PassengerProfileActivity.class));
            }
        });

        if(!CommonTask.getDataFromPreference(this, CommonConstant.REQUESTED_BY_PASSENGER).isEmpty()){
            fragment = new ActiveSessionFragment();
        }else{
            fragment = new RequestForVehicelsFragment();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if(fragment != null){
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.passengerMainPanel, fragment)
                    .commit();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPassengerBasicInformation();
    }


    private void getPassengerBasicInformation() {
        try {

            String url = String.format(CommonURL.getInstance().passengerHomePage, Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.USER_ID)));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    root = new Gson().fromJson(response.toString(), PassengerHomePageRequestRoot.class);

                    if(root.isSuccess){
                        setNavigatonViewInfo(root);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    CommonTask.analyzeVolleyError("Passenger home page response", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(PassengerHomePageActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(PassengerHomePageActivity.this, CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }




    private void setNavigatonViewInfo(PassengerHomePageRequestRoot root) {

        try {
            if(root.passengerHomePageRequest.imageLocation!= null){
                Glide.with(this).load(CommonTask.getGlideUrl(root.passengerHomePageRequest.imageLocation, this)).
                        into(passengerImage);
            }else {
                Glide.with(this).load(CommonURL.getInstance().emptyPictureUrl).into(passengerImage);
            }
            if(passengerFullName != null)
                passengerFullName.setText(root.passengerHomePageRequest.fullName);
            if(passengerMobileNumber != null)
                passengerMobileNumber.setText(root.passengerHomePageRequest.mobileNumber);
            if(passengerRating != null)
                passengerRating.setText(""+root.passengerHomePageRequest.totalRating+"/5");

            if(!root.passengerHomePageRequest.active){
                new AlertDialog.Builder(this)
                        .setTitle("User Status")
                        .setMessage("Sorry, your account is temporarily inactive.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                userLogoutRequest();
                            }
                        })
                        .create().show();
            }
        }catch (Exception e){
            e.printStackTrace();
            CommonTask.showLog(e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();


        if(id == R.id.nav_logout){
            CommonTask.goSettingsActivity(PassengerHomePageActivity.this);
            userLogoutRequest();
        } else if(id == R.id.nav_history){
            CommonTask.goSettingsActivity(PassengerHomePageActivity.this);
            Intent intent = new Intent(this, PassengerHistoryActivity.class);
            startActivity(intent);
        } else if(id == R.id.nav_profile){
            CommonTask.goSettingsActivity(PassengerHomePageActivity.this);
            Intent intent = new Intent(this, PassengerProfileActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    private void userLogoutRequest() {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CommonURL.getInstance().userLogout + "/" + Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.USER_ID)), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        CommonTask.saveDataIntoPreference(PassengerHomePageActivity.this, CommonConstant.USER_ID, "");
                        CommonTask.saveDataIntoPreference(PassengerHomePageActivity.this, CommonConstant.USER_CATEGORY_ID, "");
                        CommonTask.saveDataIntoPreference(PassengerHomePageActivity.this, "ActivityID", "");
                        CommonTask.saveDataIntoPreference(PassengerHomePageActivity.this, CommonConstant.REQUESTED_BY_PASSENGER, "");
                        CommonTask.saveDataIntoPreference(PassengerHomePageActivity.this, CommonConstant.USER_MOBILE_NUMBER, "");
                        CommonTask.saveDataIntoPreference(PassengerHomePageActivity.this, CommonConstant.USER_PASSWORD, "");

                        Intent intent = new Intent(PassengerHomePageActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    CommonTask.analyzeVolleyError("DriverMainActivty#Logout", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(PassengerHomePageActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(PassengerHomePageActivity.this, CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    @Override
    public void onAllow() {
        CommonTask.saveDataIntoPreference(this, CommonConstant.SPLASH_PERMISSION_ALLOW, "1");
        CommonTask.goSettingsActivity(PassengerHomePageActivity.this);
    }

    @Override
    public void onDeny() {
        handler = new RuntimePermissionHandler(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, CommonConstant.RUNTIME_PERMISSION, this);

    }
}
