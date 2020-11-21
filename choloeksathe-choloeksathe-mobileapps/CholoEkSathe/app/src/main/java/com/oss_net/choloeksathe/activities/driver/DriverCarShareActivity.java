package com.oss_net.choloeksathe.activities.driver;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.LocationSearchActivity;
import com.oss_net.choloeksathe.entity.databases.remote_model.GoogleMapDrawingRoot;
import com.oss_net.choloeksathe.fragments.driver.CarShareMainFragment;
import com.oss_net.choloeksathe.fragments.driver.CarShareSettingsFragment;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import static com.oss_net.choloeksathe.utils.CommonTask.animateMarker;
import static com.oss_net.choloeksathe.utils.CommonTask.getAddressFromMapLocation;
import static com.oss_net.choloeksathe.utils.CommonTask.updateCameraForMap;

public class DriverCarShareActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener {

    private static final int REQUEST_FOR_PERMISSION = 100;
    private TextView sourceLocationPoint, destinationLocationPoint;
    private MapView mapView;
    private GoogleMap googleMap;
    private Button nextStep;

    private String startLocationName, stopLocationName;
    private double startLocationLatitude, startLocationLongitude,
            stopLocationLatitude, stopLocationLongitude;

    private ProgressDialog progress;
    private GoogleMapDrawingRoot root;
    private ArrayList<Marker> markerList;

    private short MODE;
    private static final short MODE_SOURCE = 1;
    private static final short MODE_DESTINATION = 2;
    private static final short MODE_PATH_DRAW = 3;

    private int sourceLocationClickCount = 0, destinationLocationClickCount = 0;
    private Polyline polyLines;
    private JSONArray stopOverArray;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_car_sahre);

        checkPermission();

        sourceLocationPoint = findViewById(R.id.driverSourceLocation);
        destinationLocationPoint = findViewById(R.id.driverDestinationLocation);
        mapView = findViewById(R.id.driverMap);

        nextStep = findViewById(R.id.buttonNext);
        ImageView driverLocation = findViewById(R.id.driverLocation);
        nextStep.setVisibility(View.GONE);
        ImageView backpage = findViewById(R.id.backpage);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        markerList = new ArrayList<>();

        MODE = MODE_SOURCE;


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
                navigateToNextPage(null, "NORMAL");
            }
        });

        driverLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(googleApiClient != null && googleApiClient.isConnected()){
                    FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(DriverCarShareActivity.this);
                    if (ActivityCompat.checkSelfPermission(DriverCarShareActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(DriverCarShareActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                            CommonTask.showToast(DriverCarShareActivity.this, "Failed to get location");
                        }
                    });
                }
            }

        });

        backpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FOR_PERMISSION);
        } else {
            connectToGoogleApi();
        }
    }

    private void connectToGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    if (googleMap != null) {
                        if (markerList.size() > 0) {
                            Marker sMarker = markerList.get(0);
                            animateMarker(sMarker, new LatLng(location.getLatitude(), location.getLongitude()), googleMap);
                            Marker dMarker = markerList.get(1);
                            animateMarker(dMarker, new LatLng(location.getLatitude(), location.getLongitude()), googleMap);
                        } else {
                            markerList.add(0, googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).draggable(false)));
                            markerList.add(1, googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).draggable(false)));
                        }

                        updateCameraForMap(new LatLng(location.getLatitude(), location.getLongitude()), googleMap);
                        setAddress(new LatLng(location.getLatitude(), location.getLongitude()));

                        startLocationLatitude = stopLocationLatitude = location.getLatitude();
                        startLocationLongitude = stopLocationLongitude = location.getLongitude();
                    }
                }else{
                    CommonTask.showToast(DriverCarShareActivity.this, "Location not found. Please try again");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override

            public void onFailure(@NonNull Exception e) {
                CommonTask.showToast(DriverCarShareActivity.this, "Failed to get location");
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

    private void setAddress(LatLng latLng){
        if(markerList.size()>0) {
            if (MODE == MODE_SOURCE) {
                Marker sMarker = markerList.get(0);
                sMarker.setPosition(latLng);
                markerList.remove(0);
                markerList.add(0, sMarker);
                startLocationName = getAddressFromMapLocation(this, latLng);
                sourceLocationPoint.setText(startLocationName);
            } else if (MODE == MODE_DESTINATION) {
                Marker dMarker = markerList.get(1);
                dMarker.setPosition(latLng);
                markerList.remove(1);
                markerList.add(1, dMarker);
                stopLocationName = getAddressFromMapLocation(this, latLng);
                destinationLocationPoint.setText(stopLocationName);
            }
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
        Intent intent = new Intent(this, LocationSearchActivity.class);
        if(requestNumber == CommonConstant.PLACE_PICKER_START_PICKUP_POINT) {
            intent.putExtra(CommonConstant.START_LAT, startLocationLatitude);
            intent.putExtra(CommonConstant.START_LANG, startLocationLongitude);
            intent.putExtra(CommonConstant.START_LOC_NAME, startLocationName);
            startActivityForResult(intent, 1000);
        }else if(requestNumber == CommonConstant.PLACE_PICKER_STOP_PICKUP_POINT){
            intent.putExtra(CommonConstant.START_LAT, startLocationLatitude);
            intent.putExtra(CommonConstant.START_LANG, startLocationLongitude);
            intent.putExtra(CommonConstant.START_LOC_NAME, startLocationName);
            startActivityForResult(intent,1000);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if(requestCode == 1000 && data != null){
                String mode = data.getExtras().getString("MODE");
                assert mode != null;
                if(mode.equals("MAP")){
                    nextStep.setVisibility(View.VISIBLE);
                }else {
                    setUpInformationIntoView(data);
                    navigateToNextPage(data, mode);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void navigateToNextPage(Intent data, String mode) {
        Intent intent = new Intent(this, DriverCarShareFinalActivity.class);
        if(mode.equals("NORMAL")){
            intent.putExtra(CommonConstant.START_LOC_NAME, startLocationName);
            intent.putExtra(CommonConstant.START_LAT, startLocationLatitude);
            intent.putExtra(CommonConstant.START_LANG, startLocationLongitude);
            intent.putExtra(CommonConstant.END_LOC_NAME, stopLocationName);
            intent.putExtra(CommonConstant.END_LAT, stopLocationLatitude);
            intent.putExtra(CommonConstant.END_LANG, stopLocationLongitude);
            intent.putExtra("MODE", "NORMAL");
        }else{
            intent.putExtra(CommonConstant.START_LOC_NAME, startLocationName);
            intent.putExtra(CommonConstant.START_LAT, startLocationLatitude);
            intent.putExtra(CommonConstant.START_LANG, startLocationLongitude);
            intent.putExtra(CommonConstant.END_LOC_NAME, stopLocationName);
            intent.putExtra(CommonConstant.END_LAT, stopLocationLatitude);
            intent.putExtra(CommonConstant.END_LANG, stopLocationLongitude);

            intent.putExtra("STOP1LAT", data.getExtras().getDouble("STOP1LAT"));
            intent.putExtra("STOP1LANG", data.getExtras().getDouble("STOP1LANG"));
            intent.putExtra("STOP1NAME", data.getExtras().getDouble("STOP1NAME"));

            intent.putExtra("STOP2LAT", data.getExtras().getDouble("STOP2LAT"));
            intent.putExtra("STOP2LANG", data.getExtras().getDouble("STOP2LANG"));
            intent.putExtra("STOP2NAME", data.getExtras().getDouble("STOP2NAME"));

            intent.putExtra("MODE", "STOPOVER");
        }

        startActivity(intent);
        finish();
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

        //updateCameraForMap(new LatLng(stopLocationLatitude, stopLocationLongitude) ,googleMap);

        //nextStep.setVisibility(View.VISIBLE);
        sourceLocationClickCount = destinationLocationClickCount = 0;
    }

    /*private void setUpStopOverInformation(Intent data){
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
    }*/
}
