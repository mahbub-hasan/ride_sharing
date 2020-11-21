package com.oss_net.choloeksathe.activities.passenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.MobileVerificationActivity;
import com.oss_net.choloeksathe.activities.RegistrationActivity;
import com.oss_net.choloeksathe.activities.driver.DriverNavigationActivity;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.remote_model.UserProfileRoot;
import com.oss_net.choloeksathe.utils.CircularImageView;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.InputValidation;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PassengerProfileActivity extends AppCompatActivity {

    private CircleImageView userImage;
    private TextView fullName, mobileNo, email, nid, gender, dob, address, homeAddress, companyName;
    private ImageView emailVerify, mobileNumberVerify;
    private ProgressDialog progress;
    private InputValidation inputValidation = new InputValidation();
    private  UserProfileRoot root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // view profile
        userImage = findViewById(R.id.userImage);
        fullName = findViewById(R.id.userFullName);
        mobileNo = findViewById(R.id.userMobileNo);
        email = findViewById(R.id.userEmail);
        nid = findViewById(R.id.userNID);
        gender = findViewById(R.id.userGender);
        dob = findViewById(R.id.userDOB);
        address = findViewById(R.id.userAddress);
        homeAddress = findViewById(R.id.userHomeAddress);
        companyName = findViewById(R.id.companyName);
        emailVerify = findViewById(R.id.imageEmailVerify);
        mobileNumberVerify = findViewById(R.id.imageMobileVerify);

        mobileNumberVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonTask.goSettingsActivity(PassengerProfileActivity.this);
                    Intent intent = new Intent(PassengerProfileActivity.this, MobileVerificationActivity.class);
                    intent.putExtra("MOBILE_NUMBER", mobileNo.getText().toString());
                    startActivity(intent);
                }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentUserInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_driver_profile_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_edit){
            if(root != null && root.isSuccess){
                Intent intent = new Intent(this, PassengerProfileUpdateActivity.class);
                intent.putExtra("UserProfile", root);
                startActivity(intent);
            }
        }else if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }




    private void getCurrentUserInfo() {
        try {
            progress = new ProgressDialog(this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();

            String url = String.format(CommonURL.getInstance().profileInfo, Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.USER_ID)));

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    root = new Gson().fromJson(response.toString(), UserProfileRoot.class);
                    if(root.isSuccess){
                        setDataIntoView(root);
                    }else{
                        CommonTask.showToast(PassengerProfileActivity.this, "Something error. Please try again.");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    CommonTask.analyzeVolleyError("User Profile", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(PassengerProfileActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(PassengerProfileActivity.this, CommonConstant.USER_PASSWORD));
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

    private void setDataIntoView(UserProfileRoot root) {
        try {
            if(root.userProfileResponse.imageLocation!= null){
                Glide.with(this).load(CommonTask.getGlideUrl(root.userProfileResponse.imageLocation, this)).into(userImage);
            }else {
                Glide.with(this).load(CommonURL.getInstance().emptyPictureUrl).into(userImage);
            }
            fullName.setText(root.userProfileResponse.firstName);
            mobileNo.setText(root.userProfileResponse.mobileNumber);
            email.setText(String.format("Email\n%s",root.userProfileResponse.email));
            nid.setText(String.format("National identification number\n%s",root.userProfileResponse.nid));
            gender.setText(String.format("Gender\n%s",root.userProfileResponse.gender));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy");
            dob.setText(String.format("Date of birth\n%s",simpleDateFormat.format(new Date(root.userProfileResponse.dob))));
            address.setText(String.format("Address\n%s",root.userProfileResponse.address));
            homeAddress.setText(String.format("Home location\n%s",CommonTask.getAddress(this, root.userProfileResponse.homeLatitude, root.userProfileResponse.homeLongitude)));
            companyName.setText(String.format(Locale.getDefault(), "%s\n%s", "Company Name", root.userProfileResponse.companyName));
            if(root.userProfileResponse.emailVerified){
                emailVerify.setImageDrawable(getResources().getDrawable(R.drawable.ic_verified_user_black_24px));
            }else{
                emailVerify.setImageDrawable(getResources().getDrawable(R.drawable.ic_not_verified_black_24dp));

            }

            if(root.userProfileResponse.mobileVerified){
                mobileNumberVerify.setImageDrawable(getResources().getDrawable(R.drawable.ic_verified_user_black_24px));
            }else{
                mobileNumberVerify.setImageDrawable(getResources().getDrawable(R.drawable.ic_not_verified_black_24dp));
            }
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }


}
