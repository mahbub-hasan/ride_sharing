package com.oss_net.choloeksathe.fragments.passenger;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.PaymentWithRatingActivity;
import com.oss_net.choloeksathe.activities.driver.DriverHomeActivity;
import com.oss_net.choloeksathe.activities.driver.DriverNavigationActivity;
import com.oss_net.choloeksathe.activities.passenger.PassengerHomePageActivity;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.remote_model.ActiveSessionResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.BaseResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.DriverNavigation;
import com.oss_net.choloeksathe.entity.databases.remote_model.GoogleMapDrawingRoot;
import com.oss_net.choloeksathe.entity.databases.remote_model.PassengerCurrentSession;
import com.oss_net.choloeksathe.entity.databases.remote_model.Route;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.GetLocation;
import com.oss_net.choloeksathe.utils.RuntimePermissionHandler;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActiveSessionFragment extends Fragment implements RuntimePermissionHandler.RuntimePermissionListener, GetLocation.Listener, OnMapReadyCallback {

    private TextView tvActivityStatus, driverName, sourceLocation, destinationLocation,time,driverRating;
    private CircleImageView driverPic;
    private MapView mapView;
    private GoogleMap googleMap;
    private RuntimePermissionHandler permissionHandler;
    private ProgressDialog progress;
    private PassengerCurrentSession passengerCurrentSession;
    private Timer timer;


    public ActiveSessionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("com.oss-net.passenger"));
        return inflater.inflate(R.layout.fragment_active_sesstion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Active session");
        toolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_white);

        tvActivityStatus = view.findViewById(R.id.tvActivityStatus);
        time = view.findViewById(R.id.time);
        driverName = view.findViewById(R.id.driverName);
        sourceLocation = view.findViewById(R.id.sourceLocationName);
        destinationLocation = view.findViewById(R.id.destinationLocationName);
        ImageView callDriver = view.findViewById(R.id.callDriver);
        driverRating = view.findViewById(R.id.rating);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        callDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionHandler = new RuntimePermissionHandler(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE}, CommonConstant.RUNTIME_PERMISSION, new RuntimePermissionHandler.RuntimePermissionListener() {
                    @Override
                    public void onAllow() {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", passengerCurrentSession != null ? passengerCurrentSession.driverMobileNumber : "", null));
                        startActivity(intent);
                    }

                    @Override
                    public void onDeny() {
                        CommonTask.showToast(getActivity(), "Sorry permission deny");
                    }
                });
            }
        });

        driverPic = view.findViewById(R.id.driverPicture);


        mapView = view.findViewById(R.id.passengerMap);
        mapView.onCreate(savedInstanceState);

        //supportMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.passengerMap);

        permissionHandler = new RuntimePermissionHandler(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},CommonConstant.RUNTIME_PERMISSION,this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        getActiveSession();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        if(timer != null)
        {
            App.getInstance().cancelPendingRequests(getActivity().getClass().getName());
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        if(timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_passenger_active_session, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            assert ((PassengerHomePageActivity)getActivity()) != null;
            if(((PassengerHomePageActivity)getActivity()).drawer != null){
                ((PassengerHomePageActivity)getActivity()).drawer.openDrawer(Gravity.START);
            }
        }else if(item.getItemId() == R.id.action_refresh){
            getActiveSession();
        }else if(item.getItemId() == R.id.action_cancel_request){
            passengerCancelRequest();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHandler.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    private void getActiveSession() {
        try {

            progress = new ProgressDialog(getActivity());
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();

            String url = String.format(CommonURL.getInstance().passengerActiveSession, Integer.parseInt(CommonTask.getDataFromPreference(getActivity(), CommonConstant.USER_ID)));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    passengerCurrentSession = new Gson().fromJson(response.toString(), PassengerCurrentSession.class);
                    if(passengerCurrentSession.statusCode==200&&passengerCurrentSession.isSuccess){
                        CommonTask.saveDataIntoPreference(getActivity(),CommonConstant.REQUESTED_BY_PASSENGER,"1");
                        CommonTask.saveDataIntoPreference(getActivity(), "ActivityID", String.valueOf(passengerCurrentSession.activityId));
                        setInfoIntoView(passengerCurrentSession);
                    }else if(passengerCurrentSession.statusCode == 200 && !passengerCurrentSession.isSuccess && passengerCurrentSession.message.equals("No session")){
                        CommonTask.saveDataIntoPreference(getActivity(),CommonConstant.REQUESTED_BY_PASSENGER,"");
                        CommonTask.saveDataIntoPreference(getActivity(), "ActivityID", "");
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.passengerMainPanel, new RequestForVehicelsFragment()).commit();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    CommonTask.analyzeVolleyError("ActiveSesstion of passenger",error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(getActivity(), CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(getActivity(), CommonConstant.USER_PASSWORD));
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

    private void passengerCancelRequest(){
        try {
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Canceling request...");
            progress.setCancelable(false);
            progress.show();


            String url = String.format(CommonURL.getInstance().passengerCancelRequest, Integer.parseInt(CommonTask.getDataFromPreference(getContext(), "ActivityID")));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        CommonTask.saveDataIntoPreference(getActivity(), CommonConstant.REQUESTED_BY_PASSENGER, "");
                        CommonTask.showToast(getActivity(), "Request canceled");
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.passengerMainPanel, new RequestForVehicelsFragment()).commit();
                    }
                    
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    CommonTask.analyzeVolleyError("Passenger request cancel", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(getActivity(), CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(getActivity(), CommonConstant.USER_PASSWORD));
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

    private void setInfoIntoView(PassengerCurrentSession passengerCurrentSession) {
        try {
            switch (passengerCurrentSession.activityStatus) {
                case "Requested":
                    tvActivityStatus.setText("Wait for response");
                    break;
                case "Paired":
                    tvActivityStatus.setText("Car owner accept your request");
                    break;
                case "Started-Ongoing":
                    tvActivityStatus.setText("Your journey is starting now");
                    break;
            }


            SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy HH:mm");
            if(passengerCurrentSession.driverProfilePicture!= null){
                Glide.with(this).load(CommonTask.getGlideUrl(passengerCurrentSession.driverProfilePicture, getActivity())).into(driverPic);
            }else {
                Glide.with(this).load(CommonURL.getInstance().emptyPictureUrl).into(driverPic);
            }
            time.setText(format.format(new Date(passengerCurrentSession.startTime)));
            driverName.setText(passengerCurrentSession.driverFullName);
            sourceLocation.setText(passengerCurrentSession.startLocationName);
            destinationLocation.setText(passengerCurrentSession.stopLocationName);
            driverRating.setText(""+passengerCurrentSession.totalRating+"/5");
            // show image into driver

            //show map
            setMapData(passengerCurrentSession);

            // start timer for per 2 mins
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    callServerForUpdateActivity();
                }
            },2000,1000);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void callServerForUpdateActivity() {
        String url = String.format(Locale.getDefault(), CommonURL.getInstance().getPassengerRating,passengerCurrentSession.activityId);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ActiveSessionResponse activeSessionResponse = new Gson().fromJson(response.toString(), ActiveSessionResponse.class);
                if(activeSessionResponse.isSuccess){

                    if(activeSessionResponse.activityStatus==1){
                        tvActivityStatus.setText("Waiting for response");
                    }else if(activeSessionResponse.activityStatus == 2){
                        tvActivityStatus.setText("Car owner accept your request");
                    }else if(activeSessionResponse.activityStatus == 4){
                        tvActivityStatus.setText("Your journey is starting now");
                    }else if(activeSessionResponse.activityStatus == 5){
                        CommonTask.saveDataIntoPreference(getActivity(), CommonConstant.REQUESTED_BY_PASSENGER, "");
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.passengerMainPanel, new RequestForVehicelsFragment()).commit();
                    }else{
                        tvActivityStatus.setText("Your journey is completed successfully");
                        CommonTask.saveDataIntoPreference(getActivity(), CommonConstant.REQUESTED_BY_PASSENGER, "");
                        Intent ratingIntent = new Intent(getActivity(), PaymentWithRatingActivity.class);
                        ratingIntent.putExtra("ACTIVITY_ID", passengerCurrentSession.activityId);
                        ratingIntent.putExtra("AMOUNT", passengerCurrentSession.price);
                        ratingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(ratingIntent);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonTask.analyzeVolleyError(getActivity().getClass().getName(), error);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(request, getActivity().getClass().getName());
    }


    private void setMapData(PassengerCurrentSession passengerCurrentSession) {
        try {
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();

            String url = String.format(CommonURL.getInstance().googleMapRoute,passengerCurrentSession.startLatitude,passengerCurrentSession.startLongitude,passengerCurrentSession.stopLatitude,passengerCurrentSession.stopLongitude,false);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
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
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(getActivity(), CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(getActivity(), CommonConstant.USER_PASSWORD));
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

    private void drawLine(GoogleMapDrawingRoot root) {
        try {
            if(googleMap != null && root.routes.size()>0){
                LatLng source = new LatLng(root.routes.get(0).legs.get(0).startLocation.lat, root.routes.get(0).legs.get(0).startLocation.lng);
                LatLng destination = new LatLng(root.routes.get(0).legs.get(0).endLocation.lat, root.routes.get(0).legs.get(0).endLocation.lng);

                Bitmap startBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_source);
                googleMap.addMarker(new MarkerOptions().position(source)
                        .icon(BitmapDescriptorFactory.fromBitmap(startBitmap)));

                Bitmap stopBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_destination);
                googleMap.addMarker(new MarkerOptions().position(destination)
                        .icon(BitmapDescriptorFactory.fromBitmap(stopBitmap)));

                for (Route route: root.routes) {
                    List<LatLng> polyLine = CommonTask.decodePoly(route.overviewPolyline.points);
                    PolylineOptions options = new PolylineOptions();
                    options.geodesic(true);
                    options.width(7.0f);
                    options.color(Color.DKGRAY);
                    assert polyLine != null;
                    options.addAll(polyLine);
                    googleMap.addPolyline(options);


                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (LatLng latLng : polyLine) {
                        builder.include(latLng);
                    }

                    final LatLngBounds bounds = builder.build();

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 250);
                    googleMap.animateCamera(cu);
                }
            }
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    @Override
    public void onAllow() {
        GetLocation getLocation = new GetLocation(getActivity());
        getLocation.getLocation(this);
    }

    @Override
    public void onDeny() {

    }

    @Override
    public void onLocationFound(Location location) {
        mapView.getMapAsync(this);
    }

    @Override
    public void onLocationNotFound() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setZoomGesturesEnabled(false);
        //this.googleMap.getUiSettings().setScrollGesturesEnabled(false);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.oss-net.passenger")){
                Bundle bundle = intent.getExtras();
                if(bundle != null){
                    String status = bundle.getString("status");
                    assert status != null;
                    switch (status) {
                        case "accepted":
                            tvActivityStatus.setText("Accept your interest");
                            break;
                        case "rejected": {
                            tvActivityStatus.setText("Reject your interest");
                            CommonTask.saveDataIntoPreference(getActivity(), CommonConstant.REQUESTED_BY_PASSENGER, "");
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.passengerMainPanel, new RequestForVehicelsFragment()).commit();
                            break;
                        }
                        case "driver_start":
                            tvActivityStatus.setText("Car owner on the way");
                            break;
                        case "passenger_start":
                            tvActivityStatus.setText("Your journey is starting now");
                            break;
                        case "driver_own_request_cancel": {
                            tvActivityStatus.setText("Car owner cancel his or her own request");
                            CommonTask.saveDataIntoPreference(getActivity(), CommonConstant.REQUESTED_BY_PASSENGER, "");
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.passengerMainPanel, new RequestForVehicelsFragment()).commit();
                            break;
                        }
                    }
                }
            }
        }
    };

    public interface onHomeIconPress{
        void onNavigationDrawerIconPress(boolean press, Toolbar toolbar);
    }

    public void setOnHomeIconPress(onHomeIconPress homeIconPress){
    }
}
