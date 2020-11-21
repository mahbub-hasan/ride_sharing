package com.oss_net.choloeksathe.fragments.passenger;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.adapter.PassengerCarListAdapter;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.remote_model.PassengerAvailableCar;
import com.oss_net.choloeksathe.entity.databases.remote_model.PassengerAvailableCarRoot;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvailableVehicelsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Response.Listener<JSONObject>, Response.ErrorListener, PassengerCarListAdapter.onItemClick {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout linearLayout;

    private double startLocationLatitude;
    private double startLocationLongitude;
    private double stopLocationLatitude;
    private double stopLocationLongitude;
    private String startLocationName;
    private String stopLocationName;

    private PassengerAvailableCarRoot root;
    private PassengerCarListAdapter adapter;
    private ProgressBar progressBar;

    public AvailableVehicelsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            savedInstanceState = getArguments();
            startLocationLatitude = savedInstanceState.getDouble(CommonConstant.START_LAT);
            startLocationLongitude = savedInstanceState.getDouble(CommonConstant.START_LANG);
            stopLocationLatitude = savedInstanceState.getDouble(CommonConstant.END_LAT);
            stopLocationLongitude = savedInstanceState.getDouble(CommonConstant.END_LANG);
            startLocationName = savedInstanceState.getString(CommonConstant.START_LOC_NAME);
            stopLocationName = savedInstanceState.getString(CommonConstant.END_LOC_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_available_vehicels, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Available Vehicles");
        toolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        }

        recyclerView = view.findViewById(R.id.passengerAvailableVehiclesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(this);

        progressBar = view.findViewById(R.id.progressBar);
        linearLayout = view.findViewById(R.id.llAvailableCarAck);
        linearLayout.setVisibility(View.GONE);

        getPassengerCarList();
    }

    private void getPassengerCarList() {
        try{
            progressBar.setVisibility(View.VISIBLE);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("startLatitude", startLocationLatitude);
            jsonObject.put("startLongitude", startLocationLongitude);
            jsonObject.put("stopLatitude", stopLocationLatitude);
            jsonObject.put("stopLongitude", stopLocationLongitude);

            // get timestamp from date at 12:00 AM to current Time
            Calendar startTimeCalendar = Calendar.getInstance();
            startTimeCalendar.set(Calendar.HOUR_OF_DAY,0);
            startTimeCalendar.set(Calendar.MINUTE, 0);
            startTimeCalendar.set(Calendar.SECOND, 0);
            jsonObject.put("startTime", startTimeCalendar.getTimeInMillis());

            Calendar endTimeCalender = Calendar.getInstance();
            endTimeCalender.set(Calendar.HOUR_OF_DAY, 23);
            endTimeCalender.set(Calendar.MINUTE, 59);
            endTimeCalender.set(Calendar.SECOND, 58);
            jsonObject.put("endTime", endTimeCalender.getTimeInMillis());

            CommonTask.showLog(jsonObject.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, CommonURL.getInstance().passengerCarList, jsonObject, this, this){
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

    @Override
    public void onRefresh() {
        getPassengerCarList();
    }

    @Override
    public void onResponse(JSONObject response) {
        progressBar.setVisibility(View.GONE);
        CommonTask.showLog(response.toString());
        root = new Gson().fromJson(response.toString(), PassengerAvailableCarRoot.class);
        if(root.isSuccess && root.passengerAvailableCarList != null && root.passengerAvailableCarList.size()>0){
            // call adapter
            linearLayout.setVisibility(View.GONE);
            adapter = new PassengerCarListAdapter(root.passengerAvailableCarList,getContext());
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(this);
        }else{
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressBar.setVisibility(View.GONE);
        CommonTask.analyzeVolleyError("PassengerCarList", error);
    }

    @Override
    public void setOnItemClick(int position) {
        PassengerAvailableCar passengerAvailableCar = root.passengerAvailableCarList.get(position);
        if(passengerAvailableCar != null){
            Fragment fragment = new ShowInterestFragment();

            Bundle bundle = new Bundle();
            bundle.putDouble(CommonConstant.START_LAT, startLocationLatitude);
            bundle.putDouble(CommonConstant.START_LANG, startLocationLongitude);
            bundle.putDouble(CommonConstant.END_LAT, stopLocationLatitude);
            bundle.putDouble(CommonConstant.END_LANG, stopLocationLongitude);
            bundle.putString(CommonConstant.START_LOC_NAME, startLocationName);
            bundle.putString(CommonConstant.END_LOC_NAME, stopLocationName);
            bundle.putSerializable(CommonConstant.SHOW_INTEREST, passengerAvailableCar);
            fragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.passengerMainPanel, fragment).addToBackStack(null).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return true;
    }
}
