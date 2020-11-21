package com.oss_net.choloeksathe.fragments.driver;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.driver.DriverCurrentSessionActivity;
import com.oss_net.choloeksathe.activities.driver.DriverHomeActivity;
import com.oss_net.choloeksathe.activities.driver.DriverNavigationActivity;
import com.oss_net.choloeksathe.adapter.JourneyTypeAdapter;
import com.oss_net.choloeksathe.adapter.RequestStatusAdapter;
import com.oss_net.choloeksathe.adapter.RequestTypeAdapter;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.JourneyType;
import com.oss_net.choloeksathe.entity.databases.RequestStatusInfo;
import com.oss_net.choloeksathe.entity.databases.RequestType;
import com.oss_net.choloeksathe.entity.databases.StopOver;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarShareResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.CompanyList;
import com.oss_net.choloeksathe.fragments.CompanyInfoFragment;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.InputValidation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarShareSettingsFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener, StopOverFragment.onStopOverSelect, CompanyInfoFragment.onPrivateSharing {

    TextInputLayout availableSit, pricePerSit;
    CheckBox luggageAllow, smokeAllow, foodAllow, musicAllow, petAllow;
    TextView setStartTime;
    Button requestForShare, stopover;
    private long startTime;

    private ProgressDialog progressDiaglo;
    private boolean isLuggageAllow, isSmokeAllow, isFoodAllow, isMusicAllow, isPetAllow, isStopOverAllow;
    private double startLocationLatitude, startLocationLongitude, stopLocationLatitude, stopLocationLongitude;
    private String startLocationName, stopLocationName;
    JSONArray stopOverArray;
    private String totalDistance;
    private double totalPrice=0.0;
    private RadioGroup shareRadioGroup;
    private RadioButton privateShare, publicShare;

    private InputValidation inputValidation = new InputValidation();
    private boolean sharingModePrivate;
    private CompanyList mCompanyList;
    private boolean sharingModePublic;

    public CarShareSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null){
            startLocationLatitude = bundle.getDouble(CommonConstant.START_LAT);
            startLocationLongitude = bundle.getDouble(CommonConstant.START_LANG);
            stopLocationLatitude = bundle.getDouble(CommonConstant.END_LAT);
            stopLocationLongitude = bundle.getDouble(CommonConstant.END_LANG);
            startLocationName = bundle.getString(CommonConstant.START_LOC_NAME);
            stopLocationName = bundle.getString(CommonConstant.END_LOC_NAME);
            totalDistance = bundle.getString("Distance");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_share_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        availableSit = view.findViewById(R.id.tilAvailableSit);
        pricePerSit = view.findViewById(R.id.tilPricePerSit);
        luggageAllow = view.findViewById(R.id.cbLuggageAllow);
        smokeAllow = view.findViewById(R.id.cbSmockingAllow);
        foodAllow = view.findViewById(R.id.cbFoodAllow);
        musicAllow = view.findViewById(R.id.cbMusicAllow);
        petAllow = view.findViewById(R.id.cbPetAllow);
        setStartTime = view.findViewById(R.id.setStartTime);
  //      setReturnTime = view.findViewById(R.id.setReturnTime);
//        spinnerRequestType = view.findViewById(R.id.spinnerRequestType);
//        spinnerRequestStatus = view.findViewById(R.id.spinnerRequestStatus);
//        spinnerJourneyType = view.findViewById(R.id.spinnerJourneyType);
        stopover = view.findViewById(R.id.buttonAddStopOver);
        requestForShare = view.findViewById(R.id.buttonRequestForShare);
        shareRadioGroup = view.findViewById(R.id.groupSharing);
        privateShare = view.findViewById(R.id.privateShare);
        publicShare = view.findViewById(R.id.publicShare);

        publicShare.setChecked(true);
        requestForShare.setVisibility(View.GONE);

        stopover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stopover.getText().equals("Stop Over")){
                    new AlertDialog.Builder(getActivity())
                            .setTitle("StopOver")
                            .setMessage("Stopover are paths between start and stop locations. You can add this intermediate path but not be changes.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    showStopOver();
                                }
                            })
                            .create().show();
                } else
                    CommonTask.showToast(getActivity(), "Sorry you cannot change stop overs.");

            }
        });

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
                    double distanceInKM = Double.parseDouble(totalDistance.split(" ")[0]);
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

        setSpinnerDate();

        setCheckboxValue();

        requestForShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if (isValid()) shareCarForAll();
            }
        });
    }
    
    private boolean isValid() {
        boolean valid = true;
        if (!inputValidation.getResult(availableSit)) valid = false;
        if (!inputValidation.getResult(pricePerSit)) valid = false;
        if (setStartTime.getText().toString().toLowerCase().contains("time")){ valid = false;
        CommonTask.showToast(getContext(), "Travel time not set!");}
        return valid;
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

    private void showStopOver() {
        StopOverFragment stopOverFragment = new StopOverFragment();
        stopOverFragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogFragmentTheme);
        stopOverFragment.setOnStopOverSelectListener(this);
        stopOverFragment.show(getActivity().getSupportFragmentManager(), "StopOver");
    }

    private void privateSharingCompanyInfo(){
        CompanyInfoFragment companyInfoFragment = new CompanyInfoFragment();
        companyInfoFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        companyInfoFragment.setOnPrivateSharing(this);
        companyInfoFragment.show(getActivity().getSupportFragmentManager(), "Private Sharing");
    }

    private void setSpinnerDate() {
        try {
            Realm.init(getActivity());
            Realm realm = Realm.getDefaultInstance();
            final RealmResults<JourneyType> journeyTypes = realm.where(JourneyType.class).findAll();
            final RealmResults<RequestStatusInfo> requestStatusInfos = realm.where(RequestStatusInfo.class).findAll();
            final RealmResults<RequestType> requestTypes = realm.where(RequestType.class).findAll();

            JourneyTypeAdapter journeyTypeAdapter = new JourneyTypeAdapter(getActivity(), journeyTypes);
            //spinnerJourneyType.setAdapter(journeyTypeAdapter);

            RequestStatusAdapter requestStatusAdapter = new RequestStatusAdapter(getActivity(), requestStatusInfos);
            //spinnerRequestStatus.setAdapter(requestStatusAdapter);

            RequestTypeAdapter requestTypeAdapter = new RequestTypeAdapter(getActivity(), requestTypes);
           // spinnerRequestType.setAdapter(requestTypeAdapter);

//            spinnerJourneyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    journeyTypeId = journeyTypes.get(i).getId();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
//
//            spinnerRequestStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    requestStatusId = requestStatusInfos.get(i).getId();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
//
//            spinnerRequestType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    requestTypeId = requestTypes.get(i).getId();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
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
            //jsonObject.put("returnTime", returnTime);
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

//            JSONObject requestTypeJson = new JSONObject();
//            requestTypeJson.put("requestTypeId", requestTypeId);

            JSONObject requestStatusJson = new JSONObject();
            requestStatusJson.put("requestStatusId", 1);
//
//            JSONObject journeyTypeJson = new JSONObject();
//            journeyTypeJson.put("journeyTypeId", journeyTypeId);

            jsonObject.put("userInfoByRequesterId", userInfoJson);
           // jsonObject.put("requestTypeByRequestTypeId", requestTypeJson);
           jsonObject.put("requestStatusInfoByRequestStatusId", requestStatusJson);
//            jsonObject.put("journeyTypeInfoByJourneyTypeId", journeyTypeJson);
            jsonObject.put("stopOverInfosByRequestId", stopOverArray);

            CommonTask.showLog(jsonObject.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, CommonURL.getInstance().shareACar, jsonObject, this, this);
            request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
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

    @Override
    public void onStopOverSelectListener(HashMap<Integer, StopOver> stopOvers) {
        try{
            if(stopOvers != null && stopOvers.size()>0){
                isStopOverAllow = true;
                stopOverArray = new JSONArray();
                for(int rowIndex=0;rowIndex<stopOvers.size();rowIndex++){
                    StopOver stopOver = stopOvers.get(rowIndex);
                    if(stopOver != null){
                        JSONObject object = new JSONObject();
                        object.put("startLatitude",Double.parseDouble(String.format(Locale.getDefault(), "%.6f",stopOver.getStartPointLat())));
                        object.put("startLongitude",Double.parseDouble(String.format(Locale.getDefault(), "%.6f",stopOver.getStartPointLan())));
                        object.put("stopLatitude",Double.parseDouble(String.format(Locale.getDefault(), "%.6f",stopOver.getStopPointLat())));
                        object.put("stopLongitude",Double.parseDouble(String.format(Locale.getDefault(), "%.6f",stopOver.getStopPointLan())));
                        object.put("startPointName",stopOver.getStartPointName());
                        object.put("stopPointName",stopOver.getStopPointName());
                        stopOverArray.put(object);

                        stopover.setText("Stop Over ["+stopOvers.size()+"]");
                    }
                }

            }else{
                isStopOverAllow = false;
            }
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    @Override
    public void onStopOverSelectListener(CompanyList companyList) {
        mCompanyList = companyList;
    }
}
