package com.oss_net.choloeksathe.activities;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.driver.DriverCarShareFinalActivity;
import com.oss_net.choloeksathe.adapter.PlaceAutoCompleteAdapter;
import com.oss_net.choloeksathe.entity.PlaceAutocomplete;
import com.oss_net.choloeksathe.utils.CommonConstant;

import java.util.ArrayList;
import java.util.Locale;

public class LocationSearchActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, PlaceAutoCompleteAdapter.PlaceAutocompleteInterface {

    private ImageView ivBack, addStopOverLocations, clearStopOverLocations;
    private EditText sourceLocationName, stopOverLocationPoint1, stopOverLocationPoint2, destinationLocationName;
    private LinearLayout llStopOverAdded, llNoStopOver;
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private GoogleApiClient googleApiClient;
    private RecyclerView rvDestinationLocation;
    private double stopOver1Lat, stopover1Lan, stopOver2Lat, stopover2Lan, stopOver3Lat, stopover3Lan;
    private String stopOver1Name, stopOver2Name, stopOver3Name,sourceLocName, destinationLocName;
    private double sourceLocationLat, sourceLocationLan, destinationLocationLat, destinationLocationLan;
    private Button buttonDone;
    private TextView locationOnMap;

    private short MODE;
    private final static short MODE_NORMAL = 1;
    private final static  short MODE_STOP_OVER = 2;
    private final static short MODE_SOURCE = 3;
    private final static short MODE_DESTINATION = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            sourceLocationLat = bundle.getDouble(CommonConstant.START_LAT);
            sourceLocationLan = bundle.getDouble(CommonConstant.START_LANG);
            sourceLocName = bundle.getString(CommonConstant.START_LOC_NAME);
        }

        initView();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addApi(Places.GEO_DATA_API)
                .build();
        googleApiClient.connect();

        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("BD")
                .build();

        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this,googleApiClient,new LatLngBounds(new LatLng(-0,0),new LatLng(0,0)),filter);
        rvDestinationLocation.setAdapter(placeAutoCompleteAdapter);
        placeAutoCompleteAdapter.setOnPlaceClickListener(this);

        MODE = MODE_NORMAL;
    }

    private void initView() {
        ivBack = findViewById(R.id.ivBack);
        addStopOverLocations = findViewById(R.id.addStopOverLocations);
        clearStopOverLocations = findViewById(R.id.clearStopOverLocations);
        sourceLocationName = findViewById(R.id.sourceLocationName);
        stopOverLocationPoint1 = findViewById(R.id.stopOverLocationPoint1);
        stopOverLocationPoint2 = findViewById(R.id.stopOverLocationPoint2);
        destinationLocationName = findViewById(R.id.destinationLocationName);
        llStopOverAdded = findViewById(R.id.llStopOverAdded);
        llNoStopOver = findViewById(R.id.llNoStopOver);
        buttonDone = findViewById(R.id.buttonDone);
        rvDestinationLocation = findViewById(R.id.rvDestinationLocation);
        rvDestinationLocation.setLayoutManager(new LinearLayoutManager(this));
        locationOnMap = findViewById(R.id.locationOnMap);

        stopOverLocationPoint1.setVisibility(View.GONE);
        stopOverLocationPoint2.setVisibility(View.GONE);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addStopOverLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whenAddStop();
            }
        });

        clearStopOverLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whenClearStop();
            }
        });

        sourceLocationName.setHint(sourceLocName);
        sourceLocationName.setFocusable(false);
        //stopOverLocationPoint1.setFocusable(false);
        //stopOverLocationPoint2.setFocusable(false);
        //destinationLocationName.setFocusable(true);
        sourceLocationName.setBackgroundResource(R.drawable.drawable_selected_not);
        stopOverLocationPoint1.setBackgroundResource(R.drawable.drawable_selected_not);
        stopOverLocationPoint2.setBackgroundResource(R.drawable.drawable_selected_not);
        destinationLocationName.setBackgroundResource(R.drawable.drawable_selected);

        locationOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("MODE", "MAP");
                setResult(1000, intent);
                finish();
            }
        });


        sourceLocationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sourceLocationName.getHint().toString().equals("Enter pickup location")){
                    sourceLocationName.setFocusableInTouchMode(true);
                    sourceLocationName.setFocusable(true);
                    sourceLocationName.setCursorVisible(true);

                    destinationLocationName.setFocusable(false);
                }else{
                    hideSoftKeyboard(sourceLocationName);
                    startActivityForResult(new Intent(LocationSearchActivity.this, SearchActivity.class).putExtra("Number", 99), 99);
                }

            }
        });

        sourceLocationName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    placeAutoCompleteAdapter.getFilter().filter(charSequence);
                    rvDestinationLocation.setVisibility(View.VISIBLE);
                }else {
                    rvDestinationLocation.setVisibility(View.INVISIBLE);
                }
                MODE = MODE_SOURCE;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        destinationLocationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(destinationLocationName.getHint().toString().equals("Where to?")){
                    destinationLocationName.setFocusableInTouchMode(true);
                    destinationLocationName.setFocusable(true);
                    destinationLocationName.setCursorVisible(true);

                    sourceLocationName.setFocusable(false);
                }else{
                    hideSoftKeyboard(destinationLocationName);
                    startActivityForResult(new Intent(LocationSearchActivity.this, SearchActivity.class).putExtra("Number", 102), 102);
                }
            }
        });

        destinationLocationName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    placeAutoCompleteAdapter.getFilter().filter(charSequence);
                    rvDestinationLocation.setVisibility(View.VISIBLE);
                }else {
                    rvDestinationLocation.setVisibility(View.INVISIBLE);
                }

                MODE = MODE_DESTINATION;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        stopOverLocationPoint1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(stopOverLocationPoint1);
                startActivityForResult(new Intent(LocationSearchActivity.this, SearchActivity.class).putExtra("Number", 100), 100);
            }
        });

        stopOverLocationPoint2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(stopOverLocationPoint2);
                startActivityForResult(new Intent(LocationSearchActivity.this, SearchActivity.class).putExtra("Number", 101), 101);
            }
        });

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MODE = MODE_STOP_OVER;
                navigateToFinalPage();
            }
        });

        setOnFocusChanges();
    }

    private void setOnFocusChanges() {
        sourceLocationName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    sourceLocationName.setBackgroundResource(R.drawable.drawable_selected);
                    stopOverLocationPoint1.setBackgroundResource(R.drawable.drawable_selected_not);
                    stopOverLocationPoint2.setBackgroundResource(R.drawable.drawable_selected_not);
                    destinationLocationName.setBackgroundResource(R.drawable.drawable_selected_not);
                    hideSoftKeyboard(sourceLocationName);
                }
            }
        });

        stopOverLocationPoint1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    sourceLocationName.setBackgroundResource(R.drawable.drawable_selected_not);
                    stopOverLocationPoint1.setBackgroundResource(R.drawable.drawable_selected);
                    stopOverLocationPoint2.setBackgroundResource(R.drawable.drawable_selected_not);
                    destinationLocationName.setBackgroundResource(R.drawable.drawable_selected_not);
                    hideSoftKeyboard(stopOverLocationPoint1);
                }
            }
        });

        stopOverLocationPoint2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    sourceLocationName.setBackgroundResource(R.drawable.drawable_selected_not);
                    stopOverLocationPoint1.setBackgroundResource(R.drawable.drawable_selected_not);
                    stopOverLocationPoint2.setBackgroundResource(R.drawable.drawable_selected);
                    destinationLocationName.setBackgroundResource(R.drawable.drawable_selected_not);
                    hideSoftKeyboard(stopOverLocationPoint2);
                }
            }
        });

        destinationLocationName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    sourceLocationName.setBackgroundResource(R.drawable.drawable_selected_not);
                    stopOverLocationPoint1.setBackgroundResource(R.drawable.drawable_selected_not);
                    stopOverLocationPoint2.setBackgroundResource(R.drawable.drawable_selected_not);
                    destinationLocationName.setBackgroundResource(R.drawable.drawable_selected);
                    hideSoftKeyboard(destinationLocationName);
                }
            }
        });
    }

    private void whenAddStop() {
        addStopOverLocations.setVisibility(View.GONE);
        clearStopOverLocations.setVisibility(View.VISIBLE);
        stopOverLocationPoint1.setVisibility(View.VISIBLE);
        stopOverLocationPoint2.setVisibility(View.VISIBLE);
        llStopOverAdded.setVisibility(View.VISIBLE);
        llNoStopOver.setVisibility(View.GONE);
        destinationLocationName.setHint("Add final stop");


        sourceLocationName.setBackgroundResource(R.drawable.drawable_selected_not);
        stopOverLocationPoint1.setBackgroundResource(R.drawable.drawable_selected);
        stopOverLocationPoint2.setBackgroundResource(R.drawable.drawable_selected_not);
        destinationLocationName.setBackgroundResource(R.drawable.drawable_selected_not);

        stopOverLocationPoint1.setFocusable(true);
        destinationLocationName.setFocusable(false);

        hideSoftKeyboard(stopOverLocationPoint1);

        MODE = MODE_STOP_OVER;
    }

    private void whenClearStop(){
        addStopOverLocations.setVisibility(View.VISIBLE);
        clearStopOverLocations.setVisibility(View.GONE);
        stopOverLocationPoint1.setVisibility(View.GONE);
        stopOverLocationPoint2.setVisibility(View.GONE);
        llStopOverAdded.setVisibility(View.GONE);
        llNoStopOver.setVisibility(View.VISIBLE);
        destinationLocationName.setHint("Where to?");

        sourceLocationName.setBackgroundResource(R.drawable.drawable_selected_not);
        //stopOverLocationPoint1.setBackgroundResource(R.drawable.drawable_selected);
        //stopOverLocationPoint2.setBackgroundResource(R.drawable.drawable_selected_not);
        destinationLocationName.setBackgroundResource(R.drawable.drawable_selected);

        sourceLocationName.setFocusable(false);
        destinationLocationName.setFocusable(true);

        hideSoftKeyboard(destinationLocationName);


        MODE = MODE_NORMAL;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onPlaceClick(ArrayList<PlaceAutocomplete> placeAutocompleteArrayList, int position) {
        if(placeAutocompleteArrayList != null){
            String placeId = placeAutocompleteArrayList.get(position).getPlaceId().toString();
            PendingResult<PlaceBuffer> placeBufferPendingResult = Places.GeoDataApi.getPlaceById(googleApiClient,placeId);
            placeBufferPendingResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(@NonNull PlaceBuffer places) {
                    if(MODE == MODE_SOURCE){
                        sourceLocationLat = places.get(0).getLatLng().latitude;
                        sourceLocationLan = places.get(0).getLatLng().longitude;
                        sourceLocName = String.format(Locale.getDefault(),"%s,%s",
                                places.get(0).getName(),places.get(0).getAddress());
                        sourceLocationName.setText("");
                        sourceLocationName.setHint(sourceLocName);
                        rvDestinationLocation.setVisibility(View.GONE);
                        sourceLocationName.setFocusable(false);
                        destinationLocationName.setFocusable(true);
                    }else{
                        destinationLocationLat = places.get(0).getLatLng().latitude;
                        destinationLocationLan = places.get(0).getLatLng().longitude;
                        destinationLocName = String.format(Locale.getDefault(),"%s,%s",
                                places.get(0).getName(),places.get(0).getAddress());
                        destinationLocationName.setHint(destinationLocName);
                        navigateToFinalPage();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            if(requestCode == 99){
                sourceLocationLat = data.getExtras().getDouble("lat");
                sourceLocationLan = data.getExtras().getDouble("lan");
                sourceLocName = data.getExtras().getString("address");
                stopOverLocationPoint1.setHint(sourceLocName);
                if(MODE == MODE_STOP_OVER){
                    sourceLocationName.setFocusable(false);
                    stopOverLocationPoint1.setFocusable(true);
                    stopOverLocationPoint2.setFocusable(false);
                    destinationLocationName.setFocusable(false);
                }else{
                    sourceLocationName.setFocusable(false);
                    stopOverLocationPoint1.setFocusable(false);
                    stopOverLocationPoint2.setFocusable(false);
                    destinationLocationName.setFocusable(true);
                }

            }else if (requestCode == 100) {
                stopOver1Lat = data.getExtras().getDouble("lat");
                stopover1Lan = data.getExtras().getDouble("lan");
                stopOver1Name = data.getExtras().getString("address");
                stopOverLocationPoint1.setHint(stopOver1Name);

                sourceLocationName.setFocusable(false);
                stopOverLocationPoint1.setFocusable(false);
                stopOverLocationPoint2.setFocusable(true);
                destinationLocationName.setFocusable(false);
            } else if (requestCode == 101) {
                stopOver2Lat = data.getExtras().getDouble("lat");
                stopover2Lan = data.getExtras().getDouble("lan");
                stopOver2Name = data.getExtras().getString("address");
                stopOverLocationPoint2.setHint(stopOver2Name);

                sourceLocationName.setFocusable(false);
                stopOverLocationPoint1.setFocusable(false);
                stopOverLocationPoint2.setFocusable(false);
                destinationLocationName.setFocusable(true);
            } else if (requestCode == 102) {
                stopOver3Lat = data.getExtras().getDouble("lat");
                stopover3Lan = data.getExtras().getDouble("lan");
                stopOver3Name = data.getExtras().getString("address");
                destinationLocationName.setHint(stopOver3Name);

                destinationLocationName.setFocusable(false);
            }
        }
    }


    private void navigateToFinalPage(){
        Intent intent = new Intent(this, DriverCarShareFinalActivity.class);
        if(MODE == MODE_NORMAL || MODE == MODE_DESTINATION){
            intent.putExtra(CommonConstant.START_LAT, sourceLocationLat);
            intent.putExtra(CommonConstant.START_LANG, sourceLocationLan);
            intent.putExtra(CommonConstant.START_LOC_NAME, sourceLocName);
            intent.putExtra(CommonConstant.END_LAT, destinationLocationLat);
            intent.putExtra(CommonConstant.END_LANG, destinationLocationLan);
            intent.putExtra(CommonConstant.END_LOC_NAME, destinationLocName);
            intent.putExtra("MODE", "NORMAL");
            startActivity(intent);
        }else if(MODE == MODE_STOP_OVER){
            intent.putExtra(CommonConstant.START_LAT, sourceLocationLat);
            intent.putExtra(CommonConstant.START_LANG, sourceLocationLan);
            intent.putExtra(CommonConstant.START_LOC_NAME, sourceLocName);

            intent.putExtra("STOP1LAT", stopOver1Lat);
            intent.putExtra("STOP1LANG", stopover1Lan);
            intent.putExtra("STOP1NAME", stopOver1Name);

            intent.putExtra("STOP2LAT", stopOver2Lat);
            intent.putExtra("STOP2LANG", stopover2Lan);
            intent.putExtra("STOP2NAME", stopOver2Name);


            intent.putExtra(CommonConstant.END_LAT, stopOver3Lat);
            intent.putExtra(CommonConstant.END_LANG, stopover3Lan);
            intent.putExtra(CommonConstant.END_LOC_NAME, stopOver3Name);

            intent.putExtra("MODE", "STOPOVER");

            startActivity(intent);
        }
        finish();
    }

    public void hideSoftKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
