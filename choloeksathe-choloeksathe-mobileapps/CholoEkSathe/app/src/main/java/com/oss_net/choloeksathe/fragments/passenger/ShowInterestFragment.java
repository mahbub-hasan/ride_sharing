package com.oss_net.choloeksathe.fragments.passenger;


import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.remote_model.BaseResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.PassengerAvailableCar;
import com.oss_net.choloeksathe.entity.databases.remote_model.StopOverList;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.InputValidation;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowInterestFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    private double startLocationLat;
    private double startLocationLang;
    private double endLocationLat;
    private double endLocationLang;
    private PassengerAvailableCar passengerAvailableCar;
    TextView setTimeView;
    TextInputLayout price, comments, driverPrice, availableSit, sitWant;
    CheckBox cbLuggageAllow, cbSmockingAllow, cbFoodAllow, cbMusicAllow, cbPetAllow;
    LinearLayout stopOverListPanel;
    private long startTimeInMilis;
    private ProgressDialog progressDiaglo;
    private InputValidation inputValidation = new InputValidation();
    private String startLocationName;
    private String stopLocationName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            savedInstanceState = getArguments();
            startLocationLat = savedInstanceState.getDouble(CommonConstant.START_LAT);
            startLocationLang = savedInstanceState.getDouble(CommonConstant.START_LANG);
            endLocationLat = savedInstanceState.getDouble(CommonConstant.END_LAT);
            endLocationLang = savedInstanceState.getDouble(CommonConstant.END_LANG);
            startLocationName = savedInstanceState.getString(CommonConstant.START_LOC_NAME);
            stopLocationName = savedInstanceState.getString(CommonConstant.END_LOC_NAME);
            passengerAvailableCar = (PassengerAvailableCar) savedInstanceState.getSerializable(CommonConstant.SHOW_INTEREST);
        }
    }

    public ShowInterestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_show_interest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Show interest");
        toolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        }


        price = view.findViewById(R.id.price);
        price.setEnabled(false);
        driverPrice = view.findViewById(R.id.driverPrice);
        driverPrice.setEnabled(false);
        availableSit = view.findViewById(R.id.availableSit);
        availableSit.setEnabled(false);
        sitWant = view.findViewById(R.id.sitWant);
        comments = view.findViewById(R.id.comments);
        setTimeView = view.findViewById(R.id.setStartTime);
        stopOverListPanel = view.findViewById(R.id.llStopOverListMain);

        cbLuggageAllow = view.findViewById(R.id.cbLuggageAllow);
        cbSmockingAllow = view.findViewById(R.id.cbSmockingAllow);
        cbFoodAllow = view.findViewById(R.id.cbFoodAllow);
        cbMusicAllow = view.findViewById(R.id.cbMusicAllow);
        cbPetAllow = view.findViewById(R.id.cbPetAllow);

        driverPrice.getEditText().setText(String.valueOf(passengerAvailableCar.price));
        availableSit.getEditText().setText(String.valueOf(passengerAvailableCar.noofFreeSeat));

        setCheckBoxInfo();

        addStopOverInfoIntoView();



        sitWant.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    price.getEditText().setText("");
                }else{
                    double totalPrice = Double.parseDouble(driverPrice.getEditText().getText().toString())*Integer.parseInt(charSequence.toString());
                    price.getEditText().setText(String.format(Locale.getDefault(), "%.2f",totalPrice));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        view.findViewById(R.id.buttonShowInterest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (isValid()){
                      showInterest();


               }

            }
        });

        view.findViewById(R.id.startTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(passengerAvailableCar.startTime);
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        setTimeView.setText(hourOfDay+":"+minutes);
                        final Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minutes);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);

                        startTimeInMilis = calendar.getTimeInMillis();
                        if(!CommonTask.isTimeOK(startTimeInMilis, passengerAvailableCar.startTime)){
                            startTimeInMilis = 0;
                            CommonTask.showToast(getActivity(), "Invalid time select.");
                        }
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), false).show();
            }
        });
    }
                 //price, comments, driverPrice, availableSit, sitWant;
    private boolean isValid() {
        boolean valid = true;
        if (!inputValidation.getResult(price)) valid = false;
        if (!inputValidation.getResult(driverPrice)) valid = false;
        if (!inputValidation.getResult(availableSit)) valid = false;
        if (!inputValidation.getResult(sitWant)) valid = false;

        if(sitWant.getEditText().getText().toString().isEmpty()){
            sitWant.setError("Enter sit number.");
            valid = false;
        }
        if (setTimeView.getText().toString().toLowerCase().contains("time")){
            valid = false;
            CommonTask.showToast(getContext(), "Travel time not set!");
        }



        return valid;
    }

    private void addStopOverInfoIntoView() {
        try {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(passengerAvailableCar.stopOver){
                for(int index=0;index<=passengerAvailableCar.stopOvers.size();index++){
                    View stopOverListView = inflater.inflate(R.layout.custom_stop_over_list, null);
                    TextView stopOverStartPointName = stopOverListView.findViewById(R.id.sourceLocationName);
                    TextView stopOverEndPointName = stopOverListView.findViewById(R.id.destinationLocationName);
                    if(index==0){
                        stopOverStartPointName.setText(passengerAvailableCar.startPlace);
                        stopOverEndPointName.setText(passengerAvailableCar.stopOvers.get(index).startLocationName);
                    }else if(index == passengerAvailableCar.stopOvers.size()){
                        stopOverStartPointName.setText(passengerAvailableCar.stopOvers.get(index-1).stopLocationName);
                        stopOverEndPointName.setText(passengerAvailableCar.endPlace);
                        //stopOverName.setText(String.format(Locale.getDefault(), "%s to %s",sourcePointName, endPointName));
                    }else{
                        stopOverStartPointName.setText(passengerAvailableCar.stopOvers.get(index-1).stopLocationName);
                        stopOverEndPointName.setText(passengerAvailableCar.stopOvers.get(index).startLocationName);
                        //stopOverName.setText(String.format(Locale.getDefault(), "%s to %s",sourcePointName, endPointName));
                    }
                    stopOverListPanel.addView(stopOverListView);
                }
            }else{
                View stopOverListView = inflater.inflate(R.layout.custom_stop_over_list, null);
                stopOverListView.findViewById(R.id.llStopOverNameListMainPanel).setVisibility(View.GONE);
                stopOverListView.findViewById(R.id.stopOverLocationNameNotFound).setVisibility(View.VISIBLE);
                stopOverListPanel.addView(stopOverListView);
            }
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    private void setCheckBoxInfo() {
        cbPetAllow.setChecked(passengerAvailableCar.petsAllowed);
        cbFoodAllow.setChecked(passengerAvailableCar.outSideFoodAllowed);
        cbLuggageAllow.setChecked(passengerAvailableCar.luggageSpaceAvailable);
        cbMusicAllow.setChecked(passengerAvailableCar.musicAllowed);
        cbSmockingAllow.setChecked(passengerAvailableCar.smokingAllowed);
    }

    private void showInterest() {
        try {

            progressDiaglo = new ProgressDialog(getActivity());
            progressDiaglo.setMessage("Please wait...");
            progressDiaglo.setCancelable(false);
            progressDiaglo.show();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("startLatitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f",startLocationLat)));
            jsonObject.put("startLongitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f",startLocationLang)));
            jsonObject.put("stopLatitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f",endLocationLat)));
            jsonObject.put("stopLongitude", Double.parseDouble(String.format(Locale.getDefault(), "%.6f",endLocationLang)));
            jsonObject.put("startPointName", startLocationName);
            jsonObject.put("stopPointName", stopLocationName);

            jsonObject.put("price", price.getEditText().getText().toString().trim());
            jsonObject.put("noofSeat", sitWant.getEditText().getText().toString().trim());
            jsonObject.put("startTime", startTimeInMilis);
            jsonObject.put("description", comments.getEditText().getText().toString().trim());

            JSONObject requestInfoJson = new JSONObject();
            requestInfoJson.put("requestId", passengerAvailableCar.requestId);

            JSONObject driverJson = new JSONObject();
            driverJson.put("userId", passengerAvailableCar.driverId);

            JSONObject passengerJson = new JSONObject();
            passengerJson.put("userId", Integer.parseInt(CommonTask.getDataFromPreference(getActivity(), CommonConstant.USER_ID)));

            JSONObject activityStatusJson = new JSONObject();
            activityStatusJson.put("activityStatusId", 1);

            jsonObject.put("requestInfoByRequestId", requestInfoJson);
            jsonObject.put("userInfoByDriverId", driverJson);
            jsonObject.put("userInfoByPassengerId", passengerJson);
            jsonObject.put("activityStatusInfoByActivityStatusId", activityStatusJson);

            CommonTask.showLog(jsonObject.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, CommonURL.getInstance().passengerShowInterest, jsonObject, this, this){
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
            CommonTask.showLog(ex.getMessage());
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDiaglo.dismiss();
        BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
        if(baseResponse.statusCode==200 && baseResponse.isSuccess){
                CommonTask.showToast(getActivity(), "Show interest done");


            //call waiting fragment and stay until some one accept or reject request
            CommonTask.saveDataIntoPreference(getActivity(),CommonConstant.REQUESTED_BY_PASSENGER,"1");
            Fragment fragment = new ActiveSessionFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.passengerMainPanel, fragment).commit();

            //getActivity().getSupportFragmentManager().putFragment(null,CommonConstant.CURRENT_FRAGMENT,fragment);
        }else if(baseResponse.statusCode==200 && !baseResponse.isSuccess && baseResponse.message.equals("Already requested")){
            CommonTask.showToast(getActivity(), "Already requested");
            CommonTask.saveDataIntoPreference(getActivity(),CommonConstant.REQUESTED_BY_PASSENGER,"1");
            Fragment fragment = new ActiveSessionFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.passengerMainPanel, fragment).commit();
        }else{
            CommonTask.showToast(getActivity(), baseResponse.message);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDiaglo.dismiss();
        CommonTask.analyzeVolleyError("Passenger show interest activity", error);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return true;
    }
}
