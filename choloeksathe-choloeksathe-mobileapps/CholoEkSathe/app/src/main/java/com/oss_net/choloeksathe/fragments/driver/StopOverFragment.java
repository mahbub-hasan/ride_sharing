package com.oss_net.choloeksathe.fragments.driver;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.StopOver;
import com.oss_net.choloeksathe.entity.databases.remote_model.GoogleMapDrawingRoot;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class StopOverFragment extends DialogFragment {

    private Dialog dialog;
    Button stopOver;
    LinearLayout stopOverMainPanel;
    TextView locationName;
    ImageView locationClose;
    CardView cardView;
    HashMap<Integer, StopOver> stopOverLocations;
    onStopOverSelect onStopOverSelect;

    public StopOverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_stop_over, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Stop Over");
        toolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        }

        stopOver = view.findViewById(R.id.buttonAddStopOver);
        stopOverMainPanel = view.findViewById(R.id.stopOverMainPanel);
        stopOverLocations = new HashMap<>();

        stopOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AutocompleteFilter filter = new AutocompleteFilter.Builder()
                            .setCountry("BD")
                            .build();
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(filter)
                            .build(getActivity());
                    startActivityForResult(intent, CommonConstant.PLACE_PICKER_STOP_PICKUP_POINT);
                }catch (Exception ex){
                    CommonTask.showErrorLog(ex.getMessage());
                }

                //inflateEditRow();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CommonConstant.PLACE_PICKER_STOP_PICKUP_POINT && data != null){
            Place stopPlace = PlaceAutocomplete.getPlace(getActivity(), data);
            inflateEditRow(String.format("%s, %s", stopPlace.getName(),CommonTask.getAddress(getActivity(), stopPlace.getLatLng().latitude, stopPlace.getLatLng().longitude)),
                    stopPlace.getLatLng());
        }
    }

    private void inflateEditRow(String name, LatLng latLng) {
        try {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View childView = inflater.inflate(R.layout.custom_stop_over, null);
            cardView = childView.findViewById(R.id.cardView);
            locationName = childView.findViewById(R.id.stopOverLocationName);
            locationClose = childView.findViewById(R.id.buttonStopOverClose);
            locationName.setText(name);
            locationClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(stopOverMainPanel.getChildCount()>1) {
                        //CommonTask.showToast(getActivity(), String.valueOf(childView.getTag()));
                        stopOverLocations.remove(Integer.parseInt(childView.getTag().toString()));
                        stopOverMainPanel.removeView(stopOverMainPanel.getChildAt(Integer.parseInt(childView.getTag().toString())));
                    }else if(stopOverMainPanel.getChildCount()==1){
                        stopOverLocations.clear();
                        stopOverMainPanel.removeAllViews();
                    }
                }
            });
            //CommonTask.showToast(getActivity(), String.format("Count : %d", stopOverMainPanel.getChildCount()));
            stopOverLocations.put(stopOverMainPanel.getChildCount(), new StopOver(latLng.latitude, latLng.longitude, latLng.latitude, latLng.longitude, locationName.getText().toString(), locationName.getText().toString()));
            childView.setTag(stopOverMainPanel.getChildCount());
            stopOverMainPanel.addView(childView, stopOverMainPanel.getChildCount());

        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_fragment_stop_over, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            dialog.dismiss();
        }else if(item.getItemId() == R.id.action_save){
            dialog.dismiss();
            if(onStopOverSelect != null && stopOverLocations.size()>0)
                onStopOverSelect.onStopOverSelectListener(stopOverLocations);
        }
        return true;
    }

    interface onStopOverSelect{
        void onStopOverSelectListener(HashMap<Integer, StopOver> stopOvers);
    }

    public void setOnStopOverSelectListener(onStopOverSelect stopOverSelect){
        onStopOverSelect = stopOverSelect;
    }
}
