package com.oss_net.choloeksathe.fragments.driver;


import android.Manifest;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.LocationSearchActivity;
import com.oss_net.choloeksathe.activities.driver.DriverNavigationActivity;
import com.oss_net.choloeksathe.base.App;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.oss_net.choloeksathe.utils.CommonTask.animateMarker;
import static com.oss_net.choloeksathe.utils.CommonTask.getAddress;
import static com.oss_net.choloeksathe.utils.CommonTask.getAddressFromMapLocation;
import static com.oss_net.choloeksathe.utils.CommonTask.updateCameraForMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarShareMainFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener, CompanyInfoFragment.onPrivateSharing, Response.Listener<JSONObject>, Response.ErrorListener {


    private static final int REQUEST_FOR_PERMISSION = 100;
    private TextView sourceLocationPoint, destinationLocationPoint, setStartTime;
    private MapView mapView;
    private GoogleMap googleMap;
    private Button nextStep, requestForShare;
    private TextInputLayout availableSit, pricePerSit;
    private CheckBox luggageAllow, smokeAllow, foodAllow, musicAllow, petAllow;

    private String startLocationName, stopLocationName;
    private double startLocationLatitude, startLocationLongitude,
            stopLocationLatitude, stopLocationLongitude, totalPrice;

    private ProgressDialog progress;
    private GoogleMapDrawingRoot root;
    private ArrayList<Marker> markerList;

    private short MODE;
    private static final short MODE_SOURCE = 1;
    private static final short MODE_DESTINATION = 2;
    private static final short MODE_PATH_DRAW = 3;

    private int sourceLocationClickCount = 0, destinationLocationClickCount = 0;
    private Polyline polyLines;

    private SlidingUpPanelLayout slidingUpPanelLayout;


    private long startTime;

    private boolean sharingModePrivate, sharingModePublic, isLuggageAllow, isSmokeAllow, isFoodAllow, isMusicAllow, isPetAllow, isStopOverAllow;
    private CompanyList mCompanyList;

    private InputValidation inputValidation = new InputValidation();
    private ProgressDialog progressDiaglo;

    private JSONArray stopOverArray;


    public CarShareMainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        MapsInitializer.initialize(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MODE = MODE_SOURCE;
        return inflater.inflate(R.layout.fragment_car_share_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sourceLocationPoint = view.findViewById(R.id.driverSourceLocation);
        destinationLocationPoint = view.findViewById(R.id.driverDestinationLocation);
        mapView = view.findViewById(R.id.driverMap);

        nextStep = view.findViewById(R.id.buttonNext);
        ImageView driverLocation = view.findViewById(R.id.driverLocation);
        nextStep.setVisibility(View.INVISIBLE);
        ImageView backpage = view.findViewById(R.id.backpage);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        markerList = new ArrayList<>();

        slidingUpPanelLayout = view.findViewById(R.id.slidingPanel);

        availableSit = view.findViewById(R.id.tilAvailableSit);
        pricePerSit = view.findViewById(R.id.tilPricePerSit);
        luggageAllow = view.findViewById(R.id.cbLuggageAllow);
        smokeAllow = view.findViewById(R.id.cbSmockingAllow);
        foodAllow = view.findViewById(R.id.cbFoodAllow);
        musicAllow = view.findViewById(R.id.cbMusicAllow);
        petAllow = view.findViewById(R.id.cbPetAllow);
        setStartTime = view.findViewById(R.id.setStartTime);
        requestForShare = view.findViewById(R.id.buttonRequestForShare);
        RadioGroup shareRadioGroup = view.findViewById(R.id.groupSharing);
        RadioButton privateShare = view.findViewById(R.id.privateShare);
        RadioButton publicShare = view.findViewById(R.id.publicShare);

        publicShare.setChecked(true);
        requestForShare.setVisibility(View.GONE);

        view.findViewById(R.id.startTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final long time = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        final Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minutes);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);

                        startTime = calendar.getTimeInMillis();
                        if(!CommonTask.isTimeOK(startTime, time)){
                            startTime = 0;
                            CommonTask.showToast(getActivity(), "Invalid time select.");
                        }else{
                            setStartTime.setText(hourOfDay+":"+minutes);
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
                    requestForShare.setVisibility(View.GONE);
                    pricePerSit.getEditText().setText("");
                    totalPrice = 0.0;
                }else{
                    requestForShare.setVisibility(View.VISIBLE);
                    double distanceInKM = Double.parseDouble(root.routes.get(0).legs.get(0).distance.text.split(" ")[0]);
                    totalPrice = (50+(distanceInKM*10)+(distanceInKM*2*0.5))+(15/100);
                    totalPrice  = totalPrice/Integer.parseInt(charSequence.toString());
                    pricePerSit.getEditText().setText(String.format(Locale.getDefault(),"%.2f",Math.ceil(totalPrice)));
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
                    CommonTask.showToast(getActivity(), "Nothing select...");
                }
            }
        });

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });

        sourceLocationPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(MODE == MODE_PATH_DRAW){
                        if(polyLines != null)
                            polyLines.remove();
                    }
                    MODE = MODE_SOURCE;
                    if (sourceLocationPoint.getText().toString().isEmpty()) {
                        callGooglePlaceAPI(CommonConstant.PLACE_PICKER_START_PICKUP_POINT);
                    } else {
                        sourceLocationClickCount++;
                        if (sourceLocationClickCount == 1) {
                            Marker marker = markerList.get(0);
                            updateCameraForMap(marker.getPosition(), googleMap);
                        } else {
                            callGooglePlaceAPI(CommonConstant.PLACE_PICKER_START_PICKUP_POINT);
                            sourceLocationClickCount = 0;
                        }
                    }
                    nextStep.setText("Next");
                } catch (Exception ex) {
                    CommonTask.showErrorLog(ex.getMessage());
                }
            }
        });

        destinationLocationPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(MODE == MODE_PATH_DRAW){
                        if(polyLines != null)
                            polyLines.remove();
                    }
                    MODE = MODE_DESTINATION;
                    if (destinationLocationPoint.getText().toString().isEmpty()) {
                        callGooglePlaceAPI(CommonConstant.PLACE_PICKER_STOP_PICKUP_POINT);
                    } else {
                        destinationLocationClickCount++;
                        if (destinationLocationClickCount == 1) {
                            Marker marker = markerList.get(1);
                            updateCameraForMap(marker.getPosition(), googleMap);
                        } else {
                            callGooglePlaceAPI(CommonConstant.PLACE_PICKER_STOP_PICKUP_POINT);
                            destinationLocationClickCount = 0;
                        }
                    }
                    nextStep.setText("Next");
                } catch (Exception ex) {
                    CommonTask.showErrorLog(ex.getMessage());
                }
            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nextStep.getText().toString().equals("Next")) {
                    drawMapPath();
                } else if (nextStep.getText().toString().equals("Done")) {
                    /*Fragment fragment = new CarShareSettingsFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString(CommonConstant.START_LOC_NAME, startLocationName);
                    bundle.putDouble(CommonConstant.START_LAT, startLocationLatitude);
                    bundle.putDouble(CommonConstant.START_LANG, startLocationLongitude);
                    bundle.putString(CommonConstant.END_LOC_NAME, stopLocationName);
                    bundle.putDouble(CommonConstant.END_LAT, stopLocationLatitude);
                    bundle.putDouble(CommonConstant.END_LANG, stopLocationLongitude);
                    bundle.putString("Distance", root.routes.get(0).legs.get(0).distance.text);
                    fragment.setArguments(bundle);
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.carShareMainPanel, fragment).addToBackStack(null).commit();*/
                }
            }
        });

        driverLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });

        backpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
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
    public void onResume() {
        super.onResume();
        mapView.onResume();
        //connectToGoogleApi();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        //disconnectToGoogleApi();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_FOR_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                connectToGoogleApi();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if(requestCode == 1000 && data != null){
                String mode = data.getExtras().getString("MODE");
                if(mode.equals("NORMAL")){
                    // normal mode like start and end point only
                    setUpInformationIntoView(data);

                    drawMapPath();
                }else{
                    // stopover mode, so in between start and end point some intermediate node connected.
                    setUpInformationIntoView(data);
                    setUpStopOverInformation(data);
                    drawMapPath();
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(googleMap != null){
                    if(markerList.size()>0){
                        Marker sMarker = markerList.get(0);
                        animateMarker(sMarker, new LatLng(location.getLatitude(), location.getLongitude()), googleMap);
                        Marker dMarker = markerList.get(1);
                        animateMarker(dMarker, new LatLng(location.getLatitude(), location.getLongitude()), googleMap);
                    }else{
                        markerList.add(0, googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).draggable(false)));
                        markerList.add(1, googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).draggable(false)));
                    }

                    updateCameraForMap(new LatLng(location.getLatitude(), location.getLongitude()), googleMap);
                    setAddress(new LatLng(location.getLatitude(), location.getLongitude()));

                    startLocationLatitude = stopLocationLatitude = location.getLatitude();
                    startLocationLongitude = stopLocationLongitude = location.getLongitude();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override

            public void onFailure(@NonNull Exception e) {
                CommonTask.showToast(getActivity(), "Failed to get location");
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        CommonTask.showToast(getActivity(), "Connection suspended. Please try again");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        CommonTask.showToast(getActivity(), "Connection failed. Please try again");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnCameraIdleListener(this);
        this.googleMap.setOnCameraMoveListener(this);
    }

    @Override
    public void onCameraIdle() {
        setAddress(googleMap.getCameraPosition().target);
    }

    @Override
    public void onCameraMove() {
        if(markerList.size()>0){
            if(MODE == MODE_SOURCE) {
                animateMarker(markerList.get(0), this.googleMap.getCameraPosition().target, googleMap);
                startLocationLatitude = this.googleMap.getCameraPosition().target.latitude;
                startLocationLongitude = this.googleMap.getCameraPosition().target.longitude;
            }else if(MODE == MODE_DESTINATION){
                animateMarker(markerList.get(1), this.googleMap.getCameraPosition().target, googleMap);
                stopLocationLatitude = this.googleMap.getCameraPosition().target.latitude;
                stopLocationLongitude = this.googleMap.getCameraPosition().target.longitude;
            }
        }
    }

    @Override
    public void onStopOverSelectListener(CompanyList companyList) {
        mCompanyList = companyList;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDiaglo.dismiss();
        CarShareResponse carShareResponse = new Gson().fromJson(response.toString(), CarShareResponse.class);
        if(carShareResponse.isSuccess){
            CommonTask.showToast(getActivity(), "Car share successfully share.");
            CommonTask.saveDataIntoPreference(getActivity(), CommonConstant.DRIVER_REQUEST_ID, String.valueOf(carShareResponse.requestId));
            Intent intent = new Intent(getActivity(), DriverNavigationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else{
            CommonTask.showToast(getActivity(), carShareResponse.message);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDiaglo.dismiss();
        CommonTask.analyzeVolleyError("DriverCarShareActivity", error);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FOR_PERMISSION);
        } else {
            connectToGoogleApi();
        }
    }

    private void callGooglePlaceAPI(int requestNumber) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        /*AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("BD")
                .build();
        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                .setFilter(filter)
                .build(getActivity());
        startActivityForResult(intent, requestNumber);*/
        Intent intent = new Intent(getActivity(), LocationSearchActivity.class);
        if(requestNumber == CommonConstant.PLACE_PICKER_START_PICKUP_POINT) {
            intent.putExtra(CommonConstant.START_LAT, startLocationLatitude);
            intent.putExtra(CommonConstant.START_LANG, startLocationLongitude);
            intent.putExtra(CommonConstant.START_LOC_NAME, startLocationName);
            startActivity(intent);
        }else if(requestNumber == CommonConstant.PLACE_PICKER_STOP_PICKUP_POINT){
            intent.putExtra(CommonConstant.START_LAT, startLocationLatitude);
            intent.putExtra(CommonConstant.START_LANG, startLocationLongitude);
            intent.putExtra(CommonConstant.START_LOC_NAME, startLocationName);
            startActivityForResult(intent, 1000);
        }

    }

    private void connectToGoogleApi() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    private void setAddress(LatLng latLng){
        if(markerList.size()>0) {
            if (MODE == MODE_SOURCE) {
                Marker sMarker = markerList.get(0);
                sMarker.setPosition(latLng);
                markerList.remove(0);
                markerList.add(0, sMarker);
                startLocationName = getAddressFromMapLocation(getActivity(), latLng);
                sourceLocationPoint.setText(startLocationName);
            } else if (MODE == MODE_DESTINATION) {
                Marker dMarker = markerList.get(1);
                dMarker.setPosition(latLng);
                markerList.remove(1);
                markerList.add(1, dMarker);
                stopLocationName = getAddressFromMapLocation(getActivity(), latLng);
                destinationLocationPoint.setText(stopLocationName);
            }
        }
    }

    private void drawMapPath() {
        try {
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();

            String url = String.format(CommonURL.getInstance().googleMapRoute,startLocationLatitude,startLocationLongitude,stopLocationLatitude,stopLocationLongitude,false);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    CommonTask.showLog(response.toString());
                    root = new Gson().fromJson(response.toString(), GoogleMapDrawingRoot.class);
                    if(root.status.equals("OK")){
                        MODE = MODE_PATH_DRAW;
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
            if(root.routes.size()>0){
                for (Route route: root.routes) {
                    List<LatLng> polyLine = CommonTask.decodePoly(route.overviewPolyline.points);
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.geodesic(true);
                    polylineOptions.width(7.0f);
                    polylineOptions.color(Color.DKGRAY);
                    assert polyLine != null;
                    polylineOptions.addAll(polyLine);
                    polyLines = googleMap.addPolyline(polylineOptions);


                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (LatLng latLng : polyLine) {
                        builder.include(latLng);
                    }

                    final LatLngBounds bounds = builder.build();

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 250);
                    googleMap.animateCamera(cu);
                    //nextStep.setText("Done");

                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
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
        companyInfoFragment.show(getActivity().getSupportFragmentManager(), "Private Sharing");
    }

    private boolean isValid() {
        boolean valid = true;
        if (!inputValidation.getResult(availableSit)) valid = false;
        if (!inputValidation.getResult(pricePerSit)) valid = false;
        if (setStartTime.getText().toString().toLowerCase().contains("time")){ valid = false;
            CommonTask.showToast(getContext(), "Travel time not set!");}
        return valid;
    }

    private void shareCarForAll() {
        try {
            progressDiaglo = new ProgressDialog(getActivity());
            progressDiaglo.setMessage("Please wait...");
            progressDiaglo.setCancelable(false);
            progressDiaglo.show();

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
            userInfoJson.put("userId", Integer.parseInt(CommonTask.getDataFromPreference(getActivity(), CommonConstant.USER_ID)));

            JSONObject requestStatusJson = new JSONObject();
            requestStatusJson.put("requestStatusId", 1);
            jsonObject.put("userInfoByRequesterId", userInfoJson);
            jsonObject.put("requestStatusInfoByRequestStatusId", requestStatusJson);
            jsonObject.put("stopOverInfosByRequestId", stopOverArray);

            CommonTask.showLog(jsonObject.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, CommonURL.getInstance().shareACar, jsonObject, this, this);
            request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    private void setUpInformationIntoView(Intent data) {
        startLocationLatitude = data.getExtras().getDouble(CommonConstant.START_LAT);
        startLocationLongitude = data.getExtras().getDouble(CommonConstant.START_LANG);
        startLocationName = data.getExtras().getString(CommonConstant.START_LOC_NAME);
        sourceLocationPoint.setText(startLocationName);

        Marker sMarker = markerList.get(0);
        sMarker.setPosition(new LatLng(startLocationLatitude, startLocationLongitude));
        markerList.remove(0);
        markerList.add(0, sMarker);

                    /*updateCameraForMap(new LatLng(startLocationLatitude, startLocationLongitude),googleMap);


                    // stop 1.5sec
                    Thread.sleep(1500);*/

        stopLocationLatitude = data.getExtras().getDouble(CommonConstant.END_LAT);
        stopLocationLongitude = data.getExtras().getDouble(CommonConstant.END_LANG);
        stopLocationName = data.getExtras().getString(CommonConstant.END_LOC_NAME);
        destinationLocationPoint.setText(stopLocationName);

        Marker dMarker = markerList.get(1);
        dMarker.setPosition(new LatLng(stopLocationLatitude, stopLocationLongitude));
        markerList.remove(1);
        markerList.add(1, dMarker);

        updateCameraForMap(new LatLng(stopLocationLatitude, stopLocationLongitude) ,googleMap);

        //nextStep.setVisibility(View.VISIBLE);
        sourceLocationClickCount = destinationLocationClickCount = 0;
    }

    private void setUpStopOverInformation(Intent data){
        stopOverArray = new JSONArray();

        try {
            JSONObject object = new JSONObject();
            object.put("startLatitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", data.getExtras().getDouble("STOP1LAT"))));
            object.put("startLongitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", data.getExtras().getDouble("stopover1Lan"))));
            object.put("stopLatitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", data.getExtras().getDouble("STOP1LAT"))));
            object.put("stopLongitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", data.getExtras().getDouble("stopover1Lan"))));
            object.put("startPointName", data.getExtras().getDouble("stopOver1Name"));
            object.put("stopPointName", data.getExtras().getDouble("stopOver1Name"));
            stopOverArray.put(object);

            object = new JSONObject();
            object.put("startLatitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", data.getExtras().getDouble("STOP2LAT"))));
            object.put("startLongitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", data.getExtras().getDouble("stopover2Lan"))));
            object.put("stopLatitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", data.getExtras().getDouble("STOP2LAT"))));
            object.put("stopLongitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f", data.getExtras().getDouble("stopover2Lan"))));
            object.put("startPointName", data.getExtras().getDouble("stopOver2Name"));
            object.put("stopPointName", data.getExtras().getDouble("stopOver2Name"));
            stopOverArray.put(object);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
