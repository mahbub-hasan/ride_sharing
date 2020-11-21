package com.oss_net.choloeksathe.activities;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.oss_net.choloeksathe.adapter.PlaceAutoCompleteAdapter;
import com.oss_net.choloeksathe.entity.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, PlaceAutoCompleteAdapter.PlaceAutocompleteInterface {

    private RecyclerView recyclerView;
    private EditText searchText;
    private ImageView backNavigation;
    private GoogleApiClient googleApiClient;
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private int requestNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.locationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchText = findViewById(R.id.searchText);
        backNavigation = findViewById(R.id.ivBack);

        backNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    placeAutoCompleteAdapter.getFilter().filter(charSequence);
                    recyclerView.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
        recyclerView.setAdapter(placeAutoCompleteAdapter);
        placeAutoCompleteAdapter.setOnPlaceClickListener(this);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        requestNumber = savedInstanceState.getInt("Number");
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
        if(placeAutocompleteArrayList != null && placeAutocompleteArrayList.size()>0){
            String placeId = placeAutocompleteArrayList.get(position).getPlaceId().toString();
            PendingResult<PlaceBuffer> placeBufferPendingResult = Places.GeoDataApi.getPlaceById(googleApiClient,placeId);
            placeBufferPendingResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(@NonNull PlaceBuffer places) {
                    Intent intent = new Intent();
                    intent.putExtra("lat", places.get(0).getLatLng().latitude);
                    intent.putExtra("lan", places.get(0).getLatLng().longitude);
                    intent.putExtra("address",String.format(Locale.getDefault(),"%s,%s",
                            places.get(0).getName(),places.get(0).getAddress()));
                    setResult(requestNumber, intent);
                    finish();
                }
            });
        }
    }
}
