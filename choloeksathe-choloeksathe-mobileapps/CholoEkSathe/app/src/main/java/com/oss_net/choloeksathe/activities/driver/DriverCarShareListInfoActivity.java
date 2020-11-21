package com.oss_net.choloeksathe.activities.driver;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.RegistrationActivity;
import com.oss_net.choloeksathe.adapter.CarShareListInfoAdapter;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.base.FCMChangeService;
import com.oss_net.choloeksathe.entity.databases.remote_model.BaseResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarShareListInfo;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarShareListInfoRoot;
import com.oss_net.choloeksathe.entity.databases.remote_model.DriverNavigation;
import com.oss_net.choloeksathe.entity.databases.remote_model.StopOverInfo;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.RuntimePermissionHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DriverCarShareListInfoActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, Response.Listener<JSONObject>, Response.ErrorListener, CarShareListInfoAdapter.onClickEvents {

    private int requestId;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private CarShareListInfoRoot root;
    private CarShareListInfoAdapter adapter;
    private RuntimePermissionHandler permissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_car_share_list_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.rvPassengerInfo);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(this);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            requestId = bundle.getInt(CommonConstant.CAR_REQUEST_ID);

            getCarShareInfoByRequestId();
        }
    }

    private void getCarShareInfoByRequestId() {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CommonURL.getInstance().carSharePassengerInfo + "/" + requestId, null, this, this){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverCarShareListInfoActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverCarShareListInfoActivity.this, CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        } catch (Exception ex) {
            CommonTask.showErrorLog(ex.getMessage());
        }

    }

    @Override
    public void onRefresh() {
        getCarShareInfoByRequestId();
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialog.dismiss();
        swipeRefreshLayout.setEnabled(false);
        root = new Gson().fromJson(response.toString(), CarShareListInfoRoot.class);
        if (root.isSuccess && root.carShareListInfos != null && root.carShareListInfos.size() > 0) {
            findViewById(R.id.llMessage).setVisibility(View.GONE);
            adapter = new CarShareListInfoAdapter(root.carShareListInfos, this);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemsClickEvents(this);
        } else {
            adapter = new CarShareListInfoAdapter(new ArrayList<CarShareListInfo>(), this);
            recyclerView.setAdapter(adapter);
            findViewById(R.id.llMessage).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        CommonTask.analyzeVolleyError("CarShareListInfoActivity", error);
        progressDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClickListener(int position, String mode) {
        final CarShareListInfo carShareListInfo = root.carShareListInfos.get(position);
        if (carShareListInfo != null) {
            switch (mode) {
                case "REQUEST_ACCEPT":
                    acceptRequest(carShareListInfo.requestId, carShareListInfo.activityId);
                    break;
                case "REQUEST_REJECT":
                    rejectRequest(carShareListInfo.requestId, carShareListInfo.activityId);
                    break;
                case "JOURNEY_START":
                    passengerJourneyStart(carShareListInfo.activityId);
                    break;
                case "JOURNEY_STOP":
                    passengerJourneyStop(carShareListInfo.activityId);
                    break;
                case "CALL":
                    permissionHandler = new RuntimePermissionHandler(this,
                            new String[]{Manifest.permission.CALL_PHONE}, CommonConstant.RUNTIME_PERMISSION, new RuntimePermissionHandler.RuntimePermissionListener() {
                        @Override
                        public void onAllow() {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", carShareListInfo.mobileNumber, null));
                            startActivity(intent);
                        }

                        @Override
                        public void onDeny() {
                            CommonTask.showToast(getApplicationContext(), "Sorry permission deny");
                        }
                    });
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHandler.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    private void rejectRequest(int requestId, int activityId) {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Request rejected. Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();


            String url = String.format(CommonURL.getInstance().driverRequestAcceptOrReject,activityId,3,requestId);
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        CommonTask.showToast(DriverCarShareListInfoActivity.this, "Request accepted");
                        onBackPressed();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    CommonTask.analyzeVolleyError("Driver request accept", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverCarShareListInfoActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverCarShareListInfoActivity.this, CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void acceptRequest(int requestId, int activityId) {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Request accepted. Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            String url = String.format(CommonURL.getInstance().driverRequestAcceptOrReject,activityId,2,requestId);
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        CommonTask.showToast(DriverCarShareListInfoActivity.this, "Request accepted");
                        onRefresh();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    CommonTask.analyzeVolleyError("Driver request accept", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverCarShareListInfoActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverCarShareListInfoActivity.this, CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void passengerJourneyStart(int activityId){
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Starting journey. Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            String url = String.format(CommonURL.getInstance().driverIndividualPassengerStatusChange,activityId, 4);
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        CommonTask.showToast(DriverCarShareListInfoActivity.this, "Journey started");
                        onRefresh();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    CommonTask.analyzeVolleyError("Driver request accept", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverCarShareListInfoActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverCarShareListInfoActivity.this, CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void passengerJourneyStop(int activityId){
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Stop journey. Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            String url = String.format(CommonURL.getInstance().driverIndividualPassengerStatusChange,activityId, 7);
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        CommonTask.showToast(DriverCarShareListInfoActivity.this, "Journey completed");
                        onBackPressed();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   progressDialog.dismiss();
                    CommonTask.analyzeVolleyError("Driver request accept", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverCarShareListInfoActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverCarShareListInfoActivity.this, CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_driver_car_share_passenger_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_navigate){
            getInformationFromServer();
        }
        return true;
    }

    private void getInformationFromServer() {
        try {

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            String url = String.format(Locale.getDefault(), CommonURL.getInstance().driverNavigation,requestId);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    DriverNavigation navigation = new Gson().fromJson(response.toString(), DriverNavigation.class);
                    if(navigation.isSuccess){
                        showInformationIntoView(navigation);
                    }else{
                        CommonTask.showToast(DriverCarShareListInfoActivity.this, navigation.message);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    CommonTask.analyzeVolleyError("Driver Navigation", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverCarShareListInfoActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverCarShareListInfoActivity.this, CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(jsonObjectRequest);
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    private void showInformationIntoView(DriverNavigation navigation) {
        Uri mapNavigatorURI = null;
        if(navigation.stopOverInfos != null) {
            String points = "";
            for (StopOverInfo stopOverInfo : navigation.stopOverInfos) {
                points = points + stopOverInfo.startLatitude + "," + stopOverInfo.startLongitude + "|";
            }
            points = points.substring(0, points.length() - 1);
            mapNavigatorURI = Uri.parse("https://www.google.com/maps/dir/?api=1&origin="+navigation.startLocationLat+","+navigation.startLocationLan+"&destination="+navigation.stopLocationLat+","+navigation.stopLocationLan+"&waypoints="+points+"&travelmode=driving");

        }else{
            mapNavigatorURI = Uri.parse("https://www.google.com/maps/dir/?api=1&origin="+navigation.startLocationLat+","+navigation.startLocationLan+"&destination="+navigation.stopLocationLat+","+navigation.stopLocationLan+"&travelmode=driving");
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, mapNavigatorURI);
        try {
            startActivity(intent);
        }catch (Exception ex){
            try {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, mapNavigatorURI);
                startActivity(intent1);
            }catch (Exception e){
                CommonTask.showToast(getApplicationContext(), "Please install a map application");
            }
        }
    }
}
