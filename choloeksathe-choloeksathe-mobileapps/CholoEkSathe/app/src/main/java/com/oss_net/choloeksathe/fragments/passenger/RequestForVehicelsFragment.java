package com.oss_net.choloeksathe.fragments.passenger;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.oss_net.choloeksathe.activities.passenger.PassengerHomePageActivity;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.remote_model.GoogleMapDrawingRoot;
import com.oss_net.choloeksathe.entity.databases.remote_model.Route;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.GetLocation;
import com.oss_net.choloeksathe.utils.RuntimePermissionHandler;

import org.json.JSONObject;

import java.util.ArrayList;
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
public class RequestForVehicelsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_FOR_PERMISSION = 101;
    private TextView passengerSourceLocation, passengerDestinationLocation;
    private Button passengerRequestVehicles;
    private GoogleMap googleMap;
    private MapView mapView;
    private String startLocationName;
    private double startLocationLatitude;
    private double startLocationLongitude;
    private String stopLocationName;
    private double stopLocationLatitude;
    private double stopLocationLongitude;
    private ImageView myLocation;

    private ProgressDialog progress;
    private GoogleMapDrawingRoot root;
    private ArrayList<Marker> markerList;

    private short MODE;
    private static final short MODE_SOURCE = 1;
    private static final short MODE_DESTINATION = 2;
    private static final short MODE_PATH_DRAW = 3;
    private GoogleApiClient googleApiClient;

    private int sourceLocationClickCount = 0;
    private int destinationLocationClickCount = 0;
    private Polyline polyLines;

    public RequestForVehicelsFragment() {
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
        MODE = MODE_SOURCE;
        return inflater.inflate(R.layout.fragment_request_for_vehicels, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        passengerSourceLocation = view.findViewById(R.id.passengerSourceLocation);
        passengerDestinationLocation = view.findViewById(R.id.passengerDestinationLocation);
        passengerRequestVehicles = view.findViewById(R.id.buttonPassengerRequest);
        passengerRequestVehicles.setVisibility(View.INVISIBLE);
        mapView = view.findViewById(R.id.passengerMap);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
        myLocation = view.findViewById(R.id.passengerLocation);
        ImageView shownavbar = view.findViewById(R.id.shownavbar);
        markerList = new ArrayList<>();

        passengerSourceLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(MODE == MODE_PATH_DRAW){
                        if(polyLines != null)
                            polyLines.remove();
                    }
                    MODE = MODE_SOURCE;
                    if (passengerSourceLocation.getText().toString().isEmpty()) {
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
                    passengerRequestVehicles.setText("Next");
                    myLocation.setVisibility(View.VISIBLE);
                } catch (Exception ex) {
                    CommonTask.showErrorLog(ex.getMessage());
                }
            }
        });

        passengerDestinationLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(MODE == MODE_PATH_DRAW){
                        if(polyLines != null)
                            polyLines.remove();
                    }
                    MODE = MODE_DESTINATION;
                    if (passengerDestinationLocation.getText().toString().isEmpty()) {
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
                    passengerRequestVehicles.setText("Next");
                    myLocation.setVisibility(View.VISIBLE);
                } catch (Exception ex) {
                    CommonTask.showErrorLog(ex.getMessage());
                }
            }
        });

        passengerRequestVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passengerRequestVehicles.getText().toString().equals("Next")) {
                    drawMapPath();
                } else if (passengerRequestVehicles.getText().toString().equals("Search")) {
                    Fragment fragment = new AvailableVehicelsFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString(CommonConstant.START_LOC_NAME, startLocationName);
                    bundle.putDouble(CommonConstant.START_LAT, startLocationLatitude);
                    bundle.putDouble(CommonConstant.START_LANG, startLocationLongitude);
                    bundle.putString(CommonConstant.END_LOC_NAME, stopLocationName);
                    bundle.putDouble(CommonConstant.END_LAT, stopLocationLatitude);
                    bundle.putDouble(CommonConstant.END_LANG, stopLocationLongitude);
                    fragment.setArguments(bundle);
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.passengerMainPanel, fragment).addToBackStack(null).commit();
                }

            }
        });

        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(googleApiClient != null && googleApiClient.isConnected()){
                    FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(googleMap != null && markerList != null && markerList.size()>0){
                                if(MODE == MODE_SOURCE){
                                    Marker sMarker = markerList.get(0);
                                    sMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                                    markerList.remove(0);
                                    markerList.add(0, sMarker);
                                    updateCameraForMap(sMarker.getPosition(), googleMap);
                                }else if(MODE == MODE_DESTINATION){
                                    Marker dMarker = markerList.get(1);
                                    dMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                                    markerList.remove(1);
                                    markerList.add(1, dMarker);
                                    updateCameraForMap(dMarker.getPosition(), googleMap);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override

                        public void onFailure(@NonNull Exception e) {
                            CommonTask.showToast(getActivity(), "Failed to get location");
                        }
                    });
                }
            }
        });

        shownavbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((PassengerHomePageActivity) getActivity()).drawer != null) {
                        ((PassengerHomePageActivity) getActivity()).drawer.openDrawer(Gravity.START);
                }
            }
        });


        checkPermission();

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CommonConstant.PLACE_PICKER_START_PICKUP_POINT:
                if(resultCode == RESULT_OK && data != null){
                    Place startPlace = PlaceAutocomplete.getPlace(getActivity(), data);
                    startLocationName = String.format(Locale.getDefault(), "%s, %s",
                            startPlace.getName(),
                            getAddress(getActivity(), startPlace.getLatLng().latitude,
                                    startPlace.getLatLng().longitude));
                    startLocationLatitude = startPlace.getLatLng().latitude;
                    startLocationLongitude = startPlace.getLatLng().longitude;
                    passengerSourceLocation.setText(startLocationName);

                    Marker marker = markerList.get(0);
                    marker.setPosition(startPlace.getLatLng());
                    markerList.remove(0);
                    markerList.add(0, marker);

                    updateCameraForMap(startPlace.getLatLng(),googleMap);

                    break;
                }

            case CommonConstant.PLACE_PICKER_STOP_PICKUP_POINT:
                if(resultCode == RESULT_OK && data != null) {
                    Place stopPlace = PlaceAutocomplete.getPlace(getActivity(), data);
                    stopLocationName = String.format(Locale.getDefault(), "%s, %s",
                            stopPlace.getName(),
                            getAddress(getActivity(), stopPlace.getLatLng().latitude,
                                    stopPlace.getLatLng().longitude));
                    //stopLocationName = String.format("%s, %s", stopPlace.getName(),stopPlace.getAddress());
                    stopLocationLatitude = stopPlace.getLatLng().latitude;
                    stopLocationLongitude = stopPlace.getLatLng().longitude;
                    passengerDestinationLocation.setText(stopLocationName);

                    Marker marker = markerList.get(1);
                    marker.setPosition(stopPlace.getLatLng());
                    markerList.remove(1);
                    markerList.add(1, marker);

                    updateCameraForMap(stopPlace.getLatLng(),googleMap);

                    passengerRequestVehicles.setVisibility(View.VISIBLE);
                    break;
                }
            default:
                CommonTask.showToast(getActivity(), "GPS not enable.");
        }
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

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FOR_PERMISSION);
        } else {
            connectToGoogleApi();
        }
    }

    private void callGooglePlaceAPI(int requestNumber) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("BD")
                .build();
        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                .setFilter(filter)
                .build(getActivity());
        startActivityForResult(intent, requestNumber);
    }

    private void connectToGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    private void setAddress(LatLng latLng){
        if(MODE == MODE_SOURCE){
            passengerSourceLocation.setText(getAddressFromMapLocation(getActivity(), latLng));
        }else if(MODE == MODE_DESTINATION){
            passengerDestinationLocation.setText(getAddressFromMapLocation(getActivity(), latLng));
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
                    passengerRequestVehicles.setText("Next");
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
                    passengerRequestVehicles.setText("Search");
                    myLocation.setVisibility(View.INVISIBLE);
                }
            }
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }
}
