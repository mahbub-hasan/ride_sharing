package com.oss_net.choloeksathe.activities.driver;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.PaymentWithRatingActivity;
import com.oss_net.choloeksathe.activities.RegistrationActivity;
import com.oss_net.choloeksathe.activities.passenger.PassengerHomePageActivity;
import com.oss_net.choloeksathe.adapter.CarShareListInfoAdapter;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.remote_model.BaseResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarShareListInfo;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarShareListInfoRoot;
import com.oss_net.choloeksathe.entity.databases.remote_model.DriverNavigation;
import com.oss_net.choloeksathe.entity.databases.remote_model.GoogleMapDrawingRoot;
import com.oss_net.choloeksathe.entity.databases.remote_model.Route;
import com.oss_net.choloeksathe.entity.databases.remote_model.StopOverInfo;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.GetLocation;
import com.oss_net.choloeksathe.utils.RuntimePermissionHandler;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;

public class DriverNavigationActivity extends AppCompatActivity implements RuntimePermissionHandler.RuntimePermissionListener, GetLocation.Listener, OnMapReadyCallback, CarShareListInfoAdapter.onClickEvents {

    private SlidingUpPanelLayout slidingPanel;
    private GoogleMap map;
    private MapView mapView;
    private ProgressDialog progress;
    private RuntimePermissionHandler runtimePermissionHandler;
    private RecyclerView recyclerView;
    private DriverNavigation navigation;
    private ProgressDialog progressDialog;
    private MenuItem menuStartJourney, menuStopJourney, menuCancelRequest;
    private ImageView slideUpImage, slideDownImage;
    private TextView noPassengerMessage, automaticStartTime;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancel(1);

        runtimePermissionHandler = new RuntimePermissionHandler(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION},CommonConstant.RUNTIME_PERMISSION, this);

        slidingPanel = findViewById(R.id.slidingPanel);
        slideUpImage = findViewById(R.id.imageSlideUp);
        slideDownImage = findViewById(R.id.imageSlideDown);
        noPassengerMessage = findViewById(R.id.tvNoPassengerMessage);
        automaticStartTime = findViewById(R.id.automaticStartTime);
        mapView = findViewById(R.id.NavigationMap);
        mapView.onCreate(savedInstanceState);
        TextView googleMapNavigation = findViewById(R.id.googleMapNavigation);

        recyclerView = findViewById(R.id.interestedPassengerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        googleMapNavigation.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);


        slidingPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float slideOffset) {
                CommonTask.showLog(String.valueOf(slideOffset));
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(slidingPanel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    slideDownImage.setVisibility(View.GONE);
                    slideUpImage.setVisibility(View.VISIBLE);
                }else{
                    slideUpImage.setVisibility(View.GONE);
                    slideDownImage.setVisibility(View.VISIBLE);
                }
            }
        });

        slidingPanel.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonTask.goSettingsActivity(DriverNavigationActivity.this);
                    slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            }
        });

        googleMapNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call google map default map function
                CommonTask.goSettingsActivity(DriverNavigationActivity.this);

                    Uri mapNavigatorURI = null;
                    if (navigation.stopOverInfos != null) {
                        String points = "";
                        for (StopOverInfo stopOverInfo : navigation.stopOverInfos) {
                            points = points + stopOverInfo.startLatitude + "," + stopOverInfo.startLongitude + "|";
                        }
                        points = points.substring(0, points.length() - 1);
                        mapNavigatorURI = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + navigation.startLocationLat + "," + navigation.startLocationLan + "&destination=" + navigation.stopLocationLat + "," + navigation.stopLocationLan + "&waypoints=" + points + "&travelmode=driving");

                    } else {
                        mapNavigatorURI = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + navigation.startLocationLat + "," + navigation.startLocationLan + "&destination=" + navigation.stopLocationLat + "," + navigation.stopLocationLan + "&travelmode=driving");
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, mapNavigatorURI);
                    try {
                        startActivity(intent);
                    } catch (Exception ex) {
                        try {
                            Intent intent1 = new Intent(Intent.ACTION_VIEW, mapNavigatorURI);
                            startActivity(intent1);
                        } catch (Exception e) {
                            CommonTask.showToast(getApplicationContext(), "Please install a map application");
                        }
                    }


            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        runtimePermissionHandler.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if(map != null)
            getInformationFromServer();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_driver_navigation, menu);
        menuStartJourney = menu.findItem(R.id.action_driver_journey_start);
        menuStopJourney = menu.findItem(R.id.action_driver_journey_stop);
        menuCancelRequest = menu.findItem(R.id.action_driver_journey_cancel);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_driver_journey_start:
                menuCancelRequest.setVisible(false);
                menuStartJourney.setVisible(false);
                menuStopJourney.setVisible(true);
                driverJourneyStart();
                return true;
            case R.id.action_driver_journey_stop:
                driverJourneyStop();
                return true;
            case R.id.action_driver_journey_cancel:
                driverJourneyCancel();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void driverJourneyCancel() {
        try {
            progress = new ProgressDialog(this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();

            String url = String.format(CommonURL.getInstance().driverRequestCancel, Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.DRIVER_REQUEST_ID)));
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        CommonTask.showToast(DriverNavigationActivity.this, "Request cancel by car owner");
                        CommonTask.saveDataIntoPreference(DriverNavigationActivity.this, CommonConstant.DRIVER_REQUEST_ID, "");
                        Intent intent = new Intent(DriverNavigationActivity.this, DriverHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    CommonTask.analyzeVolleyError("Driver main activity", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    private void driverJourneyStop() {
        try {
            progress = new ProgressDialog(this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();


            String url = String.format(CommonURL.getInstance().driverStatusChange, Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.DRIVER_REQUEST_ID)), 7);
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        CommonTask.showToast(DriverNavigationActivity.this, "your journey is complete now");
                        CommonTask.saveDataIntoPreference(DriverNavigationActivity.this, CommonConstant.DRIVER_REQUEST_ID, "");
                        Intent intent = new Intent(DriverNavigationActivity.this, DriverHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    CommonTask.analyzeVolleyError("Driver main activity", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    private void driverJourneyStart() {
        try {
            progress = new ProgressDialog(this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();

            String url = String.format(CommonURL.getInstance().driverStatusChange, Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.DRIVER_REQUEST_ID)),6);
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        CommonTask.showToast(DriverNavigationActivity.this, "your journey is starting now");
                        getInformationFromServer();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    CommonTask.analyzeVolleyError("Driver main activity", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    private void getInformationFromServer() {
        try {

            progress = new ProgressDialog(this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();

            String url = String.format(Locale.getDefault(), CommonURL.getInstance().driverNavigation,Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.DRIVER_REQUEST_ID)));
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    navigation = new Gson().fromJson(response.toString(), DriverNavigation.class);
                    if(navigation.isSuccess){
                        showInformationIntoView(navigation);
                    }else{
                        CommonTask.showToast(DriverNavigationActivity.this, navigation.message);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    CommonTask.analyzeVolleyError("Driver Navigation", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_PASSWORD));
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
        try {
            // slide up panel function
            if(navigation.isSuccess && navigation.showInterestLists != null && navigation.showInterestLists.size()>0){
                CarShareListInfoAdapter adapter = new CarShareListInfoAdapter(navigation.showInterestLists, this);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemsClickEvents(this);
                noPassengerMessage.setVisibility(View.GONE);
            }else{
                recyclerView.setVisibility(View.GONE);
                noPassengerMessage.setVisibility(View.VISIBLE);
            }

            // setTime
            if(navigation.requestStatusCode != 6) {
                driverTimerCountDown(navigation);
            }else{
                // stop timer
                if(timer != null){
                    timer.cancel();
                    timer = null;
                }
                // hide text
                automaticStartTime.setVisibility(View.GONE);
                // change menu
                menuCancelRequest.setVisible(false);
                menuStartJourney.setVisible(false);
                menuStopJourney.setVisible(true);
            }

            // map function
            showPath(navigation.startLocationLat, navigation.startLocationLan,
                    navigation.stopLocationLat, navigation.stopLocationLan);
        }catch (Exception ex){
             ex.printStackTrace();
            String err = (ex.getMessage()==null)?"SD Card failed":ex.getMessage();
            CommonTask.showLog(err);
        }
    }

    private void driverTimerCountDown(final DriverNavigation navigation) {
        long timeRemaining = navigation.startTime-System.currentTimeMillis();
        timer = new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onTick(long l) {
                long seconds = (l/1000);
                long min = (seconds%3600)/60;
                long hour = seconds/3600;
                seconds %= 60;
                automaticStartTime.setText(String.format(Locale.getDefault(), "Your journey will automatically start after %02d:%02d:%02d", hour,min,seconds));
                //send local notification
                if(hour==0 && min==1 && seconds==59){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(DriverNavigationActivity.this, "Local")
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentTitle("Starting journey")
                            .setContentText("Your journey will be start within a few minutes...")
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setVibrate(new long[]{1000,1000,1000,1000,1000});


                    Intent resultIntent = new Intent(DriverNavigationActivity.this, DriverNavigationActivity.class);
                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(DriverNavigationActivity.this);
                    taskStackBuilder.addParentStack(DriverHomeActivity.class);
                    taskStackBuilder.addNextIntent(resultIntent);
                    PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);

                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(1, builder.build());
                }
            }

            @Override
            public void onFinish() {
                if(navigation.requestStatusCode != 6){
                    driverJourneyStart();
                }
            }
        };
        timer.start();
    }

    private void showPath(double startLocationLat, double startLocationLan, double stopLocationLat, double stopLocationLan) {
        if(map != null) {
            Bitmap startBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_source);
            map.addMarker(new MarkerOptions().position(new LatLng(startLocationLat, startLocationLan))
                    .icon(BitmapDescriptorFactory.fromBitmap(startBitmap)));

            Bitmap stopBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_destination);
            map.addMarker(new MarkerOptions().position(new LatLng(stopLocationLat, stopLocationLan))
                    .icon(BitmapDescriptorFactory.fromBitmap(stopBitmap)));

            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(new LatLng(startLocationLat, startLocationLan));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

            map.moveCamera(center);
            map.animateCamera(zoom);

            drawMap(startLocationLat, startLocationLan, stopLocationLat, stopLocationLan);
        }
    }

    private void drawMap(double startLocationLat, double startLocationLan, double stopLocationLat, double stopLocationLan) {
        try {

            String url = String.format(CommonURL.getInstance().googleMapRoute,startLocationLat,startLocationLan,stopLocationLat,stopLocationLan,"false");
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    CommonTask.showLog(response.toString());
                    GoogleMapDrawingRoot root = new Gson().fromJson(response.toString(), GoogleMapDrawingRoot.class);
                    if(root.status.equals("OK")){
                        drawLine(root);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    CommonTask.analyzeVolleyError("Map path draw request", error);
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    private void drawLine(GoogleMapDrawingRoot root) {
        try {
            if(root.routes.size()>0){
                for (Route route: root.routes) {
                    List<LatLng> polyLine = CommonTask.decodePoly(route.overviewPolyline.points);
                    PolylineOptions options = new PolylineOptions();
                    options.geodesic(true);
                    options.width(7.0f);
                    options.color(Color.DKGRAY);
                    assert polyLine != null;
                    options.addAll(polyLine);
                    map.addPolyline(options);


                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (LatLng latLng : polyLine) {
                        builder.include(latLng);
                    }

                    final LatLngBounds bounds = builder.build();

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 250);
                    map.animateCamera(cu);
                }
            }
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    @Override
    public void onAllow() {
        GetLocation getLocation = new GetLocation(this);
        getLocation.getLocation(this);
    }

    @Override
    public void onDeny() {
        CommonTask.showToast(this, "Permission deny");
    }

    @Override
    public void onLocationFound(Location location) {
        mapView.getMapAsync(this);
    }

    @Override
    public void onLocationNotFound() {
        CommonTask.showToast(this, "Location not found");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        getInformationFromServer();
    }


    @Override
    public void onItemClickListener(int position, String mode) {
        final CarShareListInfo carShareListInfo = navigation.showInterestLists.get(position);
        if (carShareListInfo != null) {
            switch (mode) {
                case "REQUEST_ACCEPT":
                    passengetAcceptRequest(carShareListInfo.requestId, carShareListInfo.activityId);
                    break;
                case "REQUEST_REJECT":
                    passengerRejectRequest(carShareListInfo.requestId, carShareListInfo.activityId);
                    break;
                case "JOURNEY_START":
                    passengerJourneyStart(carShareListInfo.activityId);
                    break;
                case "JOURNEY_STOP":
                    passengerJourneyStop(carShareListInfo.activityId);
                    break;
                case "CALL":
                    RuntimePermissionHandler permissionHandler = new RuntimePermissionHandler(this,
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

    private void passengerRejectRequest(int requestId, int activityId) {
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
                        CommonTask.showToast(DriverNavigationActivity.this, "Request accepted");
                        getInformationFromServer();
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
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_PASSWORD));
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

    private void passengetAcceptRequest(int requestId, int activityId) {
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
                        CommonTask.showToast(DriverNavigationActivity.this, "Request accepted");
                        getInformationFromServer();

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
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_PASSWORD));
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
                        CommonTask.showToast(DriverNavigationActivity.this, "Journey started");
                        //onRefresh();
                        getInformationFromServer();
                    }else{
                        CommonTask.showToast(DriverNavigationActivity.this, baseResponse.message);
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
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_PASSWORD));
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

    private void passengerJourneyStop(final int activityId){
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
                        CommonTask.showToast(DriverNavigationActivity.this, "Journey completed");
                        showRatingView(activityId);
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
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_PASSWORD));
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

    private void showRatingView(final int activityId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_driver_rating,null,false);
        builder.setView(view);

        final RatingBar ratingBar = view.findViewById(R.id.rating);

        builder.setTitle("Rating");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendInformationToServer(ratingBar.getRating(), activityId, dialogInterface);
            }
        });

        builder.create().show();
    }

    private void sendInformationToServer(float rating, int activityId, final DialogInterface dialogInterface) {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Calendar calendar = Calendar.getInstance();
            JSONObject object = new JSONObject();
            object.put("ratingPoint", rating);
            object.put("comments", "OK");
            object.put("ratingDate", calendar.getTimeInMillis());

            JSONObject activityInfoObject = new JSONObject();
            activityInfoObject.put("activityId", activityId);

            JSONObject userInfoObject = new JSONObject();
            userInfoObject.put("userId",Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.USER_ID)));

            object.put("activityInfoByActivityId", activityInfoObject);
            object.put("userInfoByRatingBy", userInfoObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, CommonURL.getInstance().driverRating, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        dialogInterface.dismiss();
                        getInformationFromServer();
                    }else{
                        CommonTask.showToast(DriverNavigationActivity.this, baseResponse.message);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    CommonTask.analyzeVolleyError(DriverNavigationActivity.class.getName(),error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverNavigationActivity.this, CommonConstant.USER_PASSWORD));
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
    public void onBackPressed() {
        if(slidingPanel != null && (slidingPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
            slidingPanel.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)){
            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }
}
