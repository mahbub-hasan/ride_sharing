package com.oss_net.choloeksathe.activities.passenger;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.LoginActivity;
import com.oss_net.choloeksathe.activities.RegistrationActivity;
import com.oss_net.choloeksathe.activities.driver.DriverNavigationActivity;
import com.oss_net.choloeksathe.adapter.CompanySpinnerAdapter;
import com.oss_net.choloeksathe.adapter.GenderAdapter;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.remote_model.BaseResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.CompanyList;
import com.oss_net.choloeksathe.entity.databases.remote_model.CompanyRoot;
import com.oss_net.choloeksathe.entity.databases.remote_model.UserProfileRoot;
import com.oss_net.choloeksathe.utils.CircularImageView;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.InputValidation;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PassengerProfileUpdateActivity extends AppCompatActivity {

    private UserProfileRoot userProfileRoot;
    private CircleImageView userImage;
    EditText fullName, mobileNo, email, nid, dob, address, homeAddress;
    AppCompatSpinner gender, spinnerCompany;
    private ProgressDialog progress;
    private GenderAdapter genderAdapter;
    private String genderInfo;
    private long dateOfBirth;
    private double homeLocationLatitude;
    private double homeLocationLongitude;
    private InputValidation inputValidation = new InputValidation();
    private Integer companyID;
    private String base64UserImage=null;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_profile_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        gender = findViewById(R.id.spinnerGender);
        userImage = findViewById(R.id.userImage);
        fullName = findViewById(R.id.userFullName);
        mobileNo = findViewById(R.id.userMobileNo);
        email = findViewById(R.id.userEmail);
        nid = findViewById(R.id.userNID);
        dob = findViewById(R.id.userDOB);
        dob.setInputType(InputType.TYPE_NULL);
        address = findViewById(R.id.userAddress);
        homeAddress = findViewById(R.id.userHomeAddress);
        homeAddress.setInputType(InputType.TYPE_NULL);
        spinnerCompany = findViewById(R.id.spinnerCompany);
        mobileNo.setEnabled(false);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            userProfileRoot = (UserProfileRoot) bundle.getSerializable("UserProfile");
            setIntoIntoView();
            setSpinnerValue();
        }

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonTask.goSettingsActivity(PassengerProfileUpdateActivity.this);


                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(PassengerProfileUpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, month, day);
                            dateOfBirth = newDate.getTimeInMillis();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy");
                            dob.setText(simpleDateFormat.format(new Date(dateOfBirth)));
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();

            }
        });

        homeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonTask.goSettingsActivity(PassengerProfileUpdateActivity.this);

                    try {
                        PlacePicker.IntentBuilder intentBuilder =
                                new PlacePicker.IntentBuilder();
                        Intent intent = intentBuilder.build(PassengerProfileUpdateActivity.this);
                        startActivityForResult(intent, CommonConstant.PLACE_PICKER_START_PICKUP_POINT);
                    } catch (Exception ex) {
                        CommonTask.showErrorLog(ex.getMessage());
                    }



            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoPermission();
            }
        });
    }

    private void takePhotoPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CommonConstant.RUNTIME_PERMISSION);
        }else{
            takePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CommonConstant.RUNTIME_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                takePhoto();
            }else{
                CommonTask.showToast(this, "Please accept permission from settings");
            }
        }
    }

    private void takePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CommonConstant.IMAGE_FROM_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CommonConstant.PLACE_PICKER_START_PICKUP_POINT){
            Place startPlace = PlacePicker.getPlace(PassengerProfileUpdateActivity.this, data);
            homeLocationLatitude = startPlace.getLatLng().latitude;
            homeLocationLongitude = startPlace.getLatLng().longitude;
            homeAddress.setText(String.format("%s, %s", startPlace.getName(), startPlace.getAddress()));
        }

        else if(requestCode == CommonConstant.IMAGE_FROM_CAMERA && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            Bitmap imageBitmap = (Bitmap) bundle.get("data");
            userImage.setImageBitmap(imageBitmap);

            // convert this image into base64 encode string
            if(imageBitmap != null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] byteArrayImage = stream.toByteArray();

                base64UserImage = Base64.encodeToString(byteArrayImage,Base64.NO_WRAP);
            }
        }
    }

    private void setSpinnerValue() {
        final String[] genders = {"Male", "Female"};
        genderAdapter = new GenderAdapter(genders, this);
        gender.setAdapter(genderAdapter);
        gender.setPrompt(getString(R.string.gender));
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genderInfo = genders[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if(userProfileRoot.userProfileResponse.gender.equals("Male")){
            gender.setSelection(0);
        }else if(userProfileRoot.userProfileResponse.gender.equals("Female")){
            gender.setSelection(1);
        }


        // set company info
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, CommonURL.getInstance().companyList, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final CompanyRoot root = new Gson().fromJson(response.toString(), CompanyRoot.class);
                if(root != null && root.isSuccess && root.companyList != null && root.companyList.size()>0){
                    CompanySpinnerAdapter adapter = new CompanySpinnerAdapter(root.companyList, PassengerProfileUpdateActivity.this);
                    spinnerCompany.setAdapter(adapter);

                    for(int i=0;i<root.companyList.size();i++){
                        if(root.companyList.get(i).companyName.equals(userProfileRoot.userProfileResponse.companyName)){
                            spinnerCompany.setSelection(i);
                            break;
                        }
                    }
                    spinnerCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            companyID = root.companyList.get(i).companyId;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            companyID = 0;
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonTask.analyzeVolleyError("Passenger profile update", error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = String.format(Locale.getDefault(), "%s:%s",
                        CommonTask.getDataFromPreference(PassengerProfileUpdateActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                        CommonTask.getDataFromPreference(PassengerProfileUpdateActivity.this, CommonConstant.USER_PASSWORD));
                String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                headers.put("Content-Type","application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(req);
    }

    private void setIntoIntoView() {
        if(userProfileRoot.userProfileResponse.imageLocation!= null){
            Glide.with(this).load(CommonTask.getGlideUrl(userProfileRoot.userProfileResponse.imageLocation, this)).
                    into(userImage);
        }else {
            Glide.with(this).load(CommonURL.getInstance().emptyPictureUrl).into(userImage);
        }

        fullName.setText(userProfileRoot.userProfileResponse.firstName);
        mobileNo.setText(userProfileRoot.userProfileResponse.mobileNumber);
        email.setText(userProfileRoot.userProfileResponse.email);
        nid.setText(userProfileRoot.userProfileResponse.nid);
        SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy");
        dob.setText(format.format(new Date(userProfileRoot.userProfileResponse.dob)));
        address.setText(userProfileRoot.userProfileResponse.address);
        homeAddress.setText(CommonTask.getAddress(this, userProfileRoot.userProfileResponse.homeLatitude, userProfileRoot.userProfileResponse.homeLongitude));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_driver_own_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save){
          if (isValid()) saveInformationIntoServer();
        }else if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    private void saveInformationIntoServer() {
        try {
            progress = new ProgressDialog(this);
            progress.setMessage("Profile updating. Please wait...");
            progress.setCancelable(false);
            progress.show();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.USER_ID)));
            jsonObject.put("firstName", fullName.getText().toString().trim());
            jsonObject.put("userName", email.getText().toString().trim());
            jsonObject.put("mobileNumber", mobileNo.getText().toString().trim());
            jsonObject.put("nid", nid.getText().toString().trim());
            jsonObject.put("gender", genderInfo);
            jsonObject.put("dob", dateOfBirth==0?userProfileRoot.userProfileResponse.dob:dateOfBirth);
            jsonObject.put("address", address.getText().toString().trim());
            jsonObject.put("homeLatitude", homeLocationLatitude==0.0?userProfileRoot.userProfileResponse.homeLatitude:homeLocationLatitude);
            jsonObject.put("homeLongitude", homeLocationLongitude==0?userProfileRoot.userProfileResponse.homeLongitude:homeLocationLongitude);
            jsonObject.put("imageLocation",base64UserImage!=null?base64UserImage:"");

            if(companyID>0){
                JSONObject company = new JSONObject();
                company.put("companyId", companyID);

                jsonObject.put("companyInfoByCompanyInfoId", company);
            }


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, CommonURL.getInstance().profileUpdate, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        CommonTask.showToast(PassengerProfileUpdateActivity.this, baseResponse.message);
                       onBackPressed();
                    }else{
                        CommonTask.showToast(PassengerProfileUpdateActivity.this, baseResponse.message);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    CommonTask.analyzeVolleyError("Driver user profile update", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(PassengerProfileUpdateActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(PassengerProfileUpdateActivity.this, CommonConstant.USER_PASSWORD));
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

    private boolean isValid(){
        boolean valid = true;
        //if (!inputValidation.getResult(facebooklink)) valid = false;
        if (!inputValidation.getResult(mobileNo, "phone number")) valid = false;
        if (!inputValidation.getResult(email, "email")) valid = false;
        //if (!inputValidation.getResult(nid, "nid")) valid = false;
        //if (!inputValidation.getResult(dob)) valid = false;
        //if (!inputValidation.getResult(address)) valid = false;
        //if (!inputValidation.getResult(homeAddress)) valid = false;
        //if (!inputValidation.getResult(fullName)) valid = false;
        return valid;
    }

}
