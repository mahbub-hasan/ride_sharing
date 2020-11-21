package com.oss_net.choloeksathe.activities.driver;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.base.FCMChangeService;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarShareResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.CompanyList;
import com.oss_net.choloeksathe.entity.databases.remote_model.GoogleMapDrawingRoot;
import com.oss_net.choloeksathe.entity.databases.remote_model.Route;
import com.oss_net.choloeksathe.fragments.CompanyInfoFragment;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.InputValidation;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DriverCarShareFinalActivity extends AppCompatActivity implements CompanyInfoFragment.onPrivateSharing, OnMapReadyCallback, Response.Listener<JSONObject>, Response.ErrorListener {

    private TextInputLayout availableSit, pricePerSit;
    private CheckBox luggageAllow, smokeAllow, foodAllow, musicAllow, petAllow;
    private TextView setStartTime;
    private Button requestForShare, stopover;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private long startTime;
    private boolean sharingModePrivate, sharingModePublic, isLuggageAllow, isSmokeAllow, isFoodAllow, isMusicAllow, isPetAllow, isStopOverAllow;
    private CompanyList mCompanyList;
    private InputValidation inputValidation = new InputValidation();
    private ProgressDialog progressDialog;
    private JSONArray stopOverArray;
    private String startLocationName, stopLocationName;
    private double startLocationLatitude, startLocationLongitude,
            stopLocationLatitude, stopLocationLongitude, totalPrice;
    private ProgressDialog progress;
    private GoogleMapDrawingRoot root;
    private MapView mapView;
    private GoogleMap googleMap;
    private String totalDistance;
    private double stopOver1Lat;
    private double stopOver1Lang;
    private double stopOver2Lat;
    private double stopOver2Lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_car_share_final);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String mode = bundle.getString("MODE");
            if(mode.equals("NORMAL")) {
                isStopOverAllow = false;
                startLocationLatitude = bundle.getDouble(CommonConstant.START_LAT);
                startLocationLongitude = bundle.getDouble(CommonConstant.START_LANG);
                stopLocationLatitude = bundle.getDouble(CommonConstant.END_LAT);
                stopLocationLongitude = bundle.getDouble(CommonConstant.END_LANG);
                startLocationName = bundle.getString(CommonConstant.START_LOC_NAME);
                stopLocationName = bundle.getString(CommonConstant.END_LOC_NAME);
                drawMapPath();
            }else{
                isStopOverAllow = true;
                startLocationLatitude = bundle.getDouble(CommonConstant.START_LAT);
                startLocationLongitude = bundle.getDouble(CommonConstant.START_LANG);
                stopLocationLatitude = bundle.getDouble(CommonConstant.END_LAT);
                stopLocationLongitude = bundle.getDouble(CommonConstant.END_LANG);
                startLocationName = bundle.getString(CommonConstant.START_LOC_NAME);
                stopLocationName = bundle.getString(CommonConstant.END_LOC_NAME);


                stopOver1Lat = bundle.getDouble("STOP1LAT");
                stopOver1Lang = bundle.getDouble("STOP1LANG");
                stopOver2Lat = bundle.getDouble("STOP2LAT");
                stopOver2Lang = bundle.getDouble("STOP2LANG");

                stopOverArray = new JSONArray();

                try {
                    JSONObject object = new JSONObject();
                    object.put("startLatitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", bundle.getDouble("STOP1LAT"))));
                    object.put("startLongitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", bundle.getDouble("STOP1LANG"))));
                    object.put("stopLatitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", bundle.getDouble("STOP1LAT"))));
                    object.put("stopLongitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", bundle.getDouble("STOP1LANG"))));
                    object.put("startPointName", bundle.getString("STOP1NAME"));
                    object.put("stopPointName", bundle.getString("STOP1NAME"));
                    stopOverArray.put(object);

                    object = new JSONObject();
                    object.put("startLatitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", bundle.getDouble("STOP2LAT"))));
                    object.put("startLongitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", bundle.getDouble("STOP2LANG"))));
                    object.put("stopLatitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", bundle.getDouble("STOP2LAT"))));
                    object.put("stopLongitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", bundle.getDouble("STOP2LANG"))));
                    object.put("startPointName", bundle.getString("STOP2NAME"));
                    object.put("stopPointName", bundle.getString("STOP2NAME"));
                    stopOverArray.put(object);
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                drawMapPath();
            }
        }

        availableSit = findViewById(R.id.tilAvailableSit);
        pricePerSit = findViewById(R.id.tilPricePerSit);
        luggageAllow = findViewById(R.id.cbLuggageAllow);
        smokeAllow = findViewById(R.id.cbSmockingAllow);
        foodAllow = findViewById(R.id.cbFoodAllow);
        musicAllow = findViewById(R.id.cbMusicAllow);
        petAllow = findViewById(R.id.cbPetAllow);
        setStartTime = findViewById(R.id.setStartTime);
        requestForShare = findViewById(R.id.buttonRequestForShare);
        RadioGroup shareRadioGroup = findViewById(R.id.groupSharing);
        RadioButton privateShare = findViewById(R.id.privateShare);
        RadioButton publicShare = findViewById(R.id.publicShare);

        ImageView ivBack = findViewById(R.id.ivBack);

        mapView = findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        availableSit.setHintAnimationEnabled(true);
        availableSit.setHint("Available sit (1 to "+CommonTask.getDataFromPreference(this, CommonConstant.DRIVER_NUMBER_OF_SIT)+")");

        publicShare.setChecked(true);
        requestForShare.setVisibility(View.INVISIBLE);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverCarShareFinalActivity.this,
                        DriverCarShareActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.startTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final long time = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                new TimePickerDialog(DriverCarShareFinalActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        final Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minutes);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);

                        startTime = calendar.getTimeInMillis();
                        if(!CommonTask.isTimeOK(startTime, time)){
                            requestForShare.setVisibility(View.INVISIBLE);
                            startTime = 0;
                            CommonTask.showToast(DriverCarShareFinalActivity.this, "Invalid time select.");
                        }else{
                            setStartTime.setText(hourOfDay+":"+minutes);
                            requestForShare.setVisibility(View.VISIBLE);
                        }
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        });

        availableSit.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    requestForShare.setVisibility(View.INVISIBLE);
                    pricePerSit.getEditText().setText("");
                    totalPrice = 0.0;
                }else{
                    int sitnumber = Integer.parseInt(availableSit.getEditText().getText().toString());
                    int maxSitLimit = Integer.parseInt(CommonTask.getDataFromPreference(DriverCarShareFinalActivity.this, CommonConstant.DRIVER_NUMBER_OF_SIT));
                    if(sitnumber>=1 && sitnumber<=maxSitLimit){
                        requestForShare.setVisibility(View.VISIBLE);
                        double distanceInKM = Double.parseDouble(totalDistance.split(" ")[0]);
                        totalPrice = (50+(distanceInKM*10)+(distanceInKM*2*0.5))+(15/100);
                        totalPrice  = totalPrice/Integer.parseInt(charSequence.toString());
                        pricePerSit.getEditText().setText(String.format(Locale.getDefault(),"%.2f",Math.ceil(totalPrice)));
                        availableSit.setError(null);
                    }else{
                        requestForShare.setVisibility(View.INVISIBLE);
                        availableSit.setError("over limit sit.");
                        pricePerSit.getEditText().setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        shareRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if(id == R.id.privateShare){
                    sharingModePrivate = true;
                    sharingModePublic = false;

                    //open fragment for private sharing
                    privateSharingCompanyInfo();
                }else if(id == R.id.publicShare){
                    sharingModePublic = true;
                    sharingModePrivate = false;
                }else{
                    CommonTask.showToast(DriverCarShareFinalActivity.this, "Nothing select...");
                }
            }
        });

        requestForShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) shareCarForAll();
            }
        });

        setCheckboxValue();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStopOverSelectListener(CompanyList companyList) {
        mCompanyList = companyList;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialog.dismiss();
        CarShareResponse carShareResponse = new Gson().fromJson(response.toString(), CarShareResponse.class);
        if(carShareResponse.isSuccess){
            CommonTask.showToast(this, "Car share successfully share.");
            CommonTask.saveDataIntoPreference(this, CommonConstant.DRIVER_REQUEST_ID, String.valueOf(carShareResponse.requestId));
            Intent intent = new Intent(this, DriverNavigationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else{
            CommonTask.showToast(this, carShareResponse.message);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        CommonTask.analyzeVolleyError("DriverCarShareActivity", error);
    }

    private boolean isValid() {
        if(setStartTime.getText().toString().isEmpty() || setStartTime.getText().toString().equals("time")){
            CommonTask.showToast(this, "Travel time not set yet.");
            return false;
        }
        if(availableSit.getEditText().getText().toString().isEmpty()){
            availableSit.setError("Number of sit is required");
            return false;
        }

        if(pricePerSit.getEditText().getText().toString().isEmpty()){
            pricePerSit.setError("Price per sit is required.");
            return false;
        }
        return true;
    }

    private void shareCarForAll() {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("startLatitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f",startLocationLatitude)));
            jsonObject.put("startLongitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f",startLocationLongitude)));
            jsonObject.put("stopLatitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f",stopLocationLatitude)));
            jsonObject.put("stopLongitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f",stopLocationLongitude)));
            jsonObject.put("shortStartPlace", startLocationName);
            jsonObject.put("shortEndPlace", stopLocationName);
            jsonObject.put("price", Double.parseDouble(pricePerSit.getEditText().getText().toString().trim()));
            jsonObject.put("noofFreeSeat", Integer.parseInt(availableSit.getEditText().getText().toString().trim()));
            jsonObject.put("stopOver", isStopOverAllow);
            jsonObject.put("isRound", false);
            jsonObject.put("startTime", startTime);
            jsonObject.put("requestDate", new Date(new java.util.Date().getTime()));
            jsonObject.put("luggageSpaceAvailable", isLuggageAllow);
            jsonObject.put("smokingAllowed", isSmokeAllow);
            jsonObject.put("outSideFoodAllowed", isFoodAllow);
            jsonObject.put("musicAllowed", isMusicAllow);
            jsonObject.put("petsAllowed", isPetAllow);


            // add company info in here
            if(sharingModePrivate)
                jsonObject.put("companyId", mCompanyList.companyId);
            else
                jsonObject.put("companyId", 0);


            JSONObject userInfoJson = new JSONObject();
            userInfoJson.put("userId", Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.USER_ID)));

            JSONObject requestStatusJson = new JSONObject();
            requestStatusJson.put("requestStatusId", 1);
            jsonObject.put("userInfoByRequesterId", userInfoJson);
            jsonObject.put("requestStatusInfoByRequestStatusId", requestStatusJson);
            jsonObject.put("stopOverInfosByRequestId", stopOverArray);

            CommonTask.showLog(jsonObject.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, CommonURL.getInstance().shareACar, jsonObject, this, this){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverCarShareFinalActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverCarShareFinalActivity.this, CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    private void drawMapPath() {
        try {
            progress = new ProgressDialog(this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();

            String url = "";

            if(isStopOverAllow){
                url = String.format(CommonURL.getInstance().googleMapRouteWithStopOvers,startLocationLatitude, startLocationLongitude,
                        stopLocationLatitude, stopLocationLongitude, stopOver1Lat, stopOver1Lang, stopOver2Lat, stopOver2Lang, getString(R.string.map_release_key));
            }else{
                url = String.format(CommonURL.getInstance().googleMapRoute,startLocationLatitude,startLocationLongitude,stopLocationLatitude,stopLocationLongitude,false);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    CommonTask.showLog(response.toString());
                    root = new Gson().fromJson(response.toString(), GoogleMapDrawingRoot.class);
                    if(root.status.equals("OK")){
                        drawLine(root);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    CommonTask.analyzeVolleyError("Map path draw request", error);
                    //nextStep.setText("Next");
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
            if(!isStopOverAllow) {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(startLocationLatitude, startLocationLongitude)));
                googleMap.addMarker(new MarkerOptions().position(new LatLng(stopLocationLatitude, stopLocationLongitude)));
            }else{
                googleMap.addMarker(new MarkerOptions().position(new LatLng(startLocationLatitude, startLocationLongitude)));
                googleMap.addMarker(new MarkerOptions().position(new LatLng(stopOver1Lat, stopOver1Lang)));
                googleMap.addMarker(new MarkerOptions().position(new LatLng(stopOver2Lat, stopOver2Lang)));
                googleMap.addMarker(new MarkerOptions().position(new LatLng(stopLocationLatitude, stopLocationLongitude)));
            }
            if(root.routes.size()>0){
                for (Route route: root.routes) {
                    List<LatLng> polyLine = CommonTask.decodePoly(route.overviewPolyline.points);
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.geodesic(true);
                    polylineOptions.width(7.0f);
                    polylineOptions.color(Color.DKGRAY);
                    assert polyLine != null;
                    polylineOptions.addAll(polyLine);
                    googleMap.addPolyline(polylineOptions);


                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (LatLng latLng : polyLine) {
                        builder.include(latLng);
                    }

                    final LatLngBounds bounds = builder.build();

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 250);
                    googleMap.animateCamera(cu);
                    //nextStep.setText("Done");

                    totalDistance = route.legs.get(0).distance.text;
                }
            }
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    private void setCheckboxValue() {

        foodAllow.setChecked(true);
        musicAllow.setChecked(true);
        isFoodAllow = true;
        isMusicAllow = true;


        luggageAllow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    luggageAllow.setChecked(b);
                    isLuggageAllow = b;
                }else{
                    luggageAllow.setChecked(b);
                    isLuggageAllow = b;
                }
            }
        });

        smokeAllow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    smokeAllow.setChecked(b);
                    isSmokeAllow = b;
                }else{
                    smokeAllow.setChecked(b);
                    isSmokeAllow = b;
                }
            }
        });

        foodAllow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    foodAllow.setChecked(b);
                    isFoodAllow = b;
                }else{
                    foodAllow.setChecked(b);
                    isFoodAllow = b;
                }
            }
        });

        musicAllow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    musicAllow.setChecked(b);
                    isMusicAllow = b;
                }else{
                    musicAllow.setChecked(b);
                    isMusicAllow = b;
                }
            }
        });

        petAllow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    petAllow.setChecked(b);
                    isPetAllow = b;
                }else{
                    petAllow.setChecked(b);
                    isPetAllow = b;
                }
            }
        });

    }

    private void privateSharingCompanyInfo(){
        CompanyInfoFragment companyInfoFragment = new CompanyInfoFragment();
        companyInfoFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        companyInfoFragment.setOnPrivateSharing(this);
        companyInfoFragment.show(getSupportFragmentManager(), "Private Sharing");
    }

    @Override
    public void onBackPressed() {

    }
}
