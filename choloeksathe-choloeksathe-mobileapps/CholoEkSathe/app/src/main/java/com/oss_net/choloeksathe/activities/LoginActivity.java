package com.oss_net.choloeksathe.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.driver.DriverHomeActivity;
import com.oss_net.choloeksathe.activities.driver.DriverNavigationActivity;
import com.oss_net.choloeksathe.activities.passenger.PassengerHomePageActivity;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.base.FCMChangeService;
import com.oss_net.choloeksathe.entity.databases.remote_model.LoginResponse;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.InputValidation;
import com.oss_net.choloeksathe.utils.RuntimePermissionHandler;

import org.json.JSONObject;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements
        Response.Listener<JSONObject>, Response.ErrorListener
        , RuntimePermissionHandler.RuntimePermissionListener {

    EditText userName, password;
    private ProgressDialog progressDiaglo;
    private JsonObjectRequest request;
    private TextView textView;
    private RuntimePermissionHandler handler;
    CheckBox showPassword;

    private InputValidation inputValidation =  new InputValidation();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        textView = findViewById(R.id.forgetPass);
        showPassword = findViewById(R.id.ckShowPassword);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
            }
        });

        userName = findViewById(R.id.userMobileNo);
        password = findViewById(R.id.userPassword);

        

        findViewById(R.id.buttonRegistration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonTask.goSettingsActivity(LoginActivity.this);
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));

            }
        });

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonTask.goSettingsActivity(LoginActivity.this);
                    if (isValid()) doLogin();

            }
        });

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    password.setTransformationMethod(new PasswordTransformationMethod());

                }else{
                    password.setTransformationMethod(null);

                }
            }
        });
    }

    private boolean isValid(){
        if(userName.getText().toString().isEmpty()){
            userName.setError("Mobile number is required");
            return false;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Password is required");
            return false;
        }

        return true;
    }

    private void doLogin() {
        try {
            progressDiaglo = new ProgressDialog(this);
            progressDiaglo.setMessage("Please wait...");
            progressDiaglo.setCancelable(false);
            progressDiaglo.show();

            String url = String.format(Locale.getDefault(), CommonURL.getInstance().userLogin,
                    userName.getText().toString().trim(), password.getText().toString().trim());
            request = new JsonObjectRequest(Request.Method.GET,url,null,this, this);
            request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDiaglo.dismiss();
        LoginResponse loginResponse = new Gson().fromJson(response.toString(), LoginResponse.class);
        if(loginResponse != null && loginResponse.isSuccess){
            if(loginResponse.isActive) {
                CommonTask.saveDataIntoPreference(this, CommonConstant.USER_ID, String.valueOf(loginResponse.userId));
                CommonTask.saveDataIntoPreference(this, CommonConstant.USER_CATEGORY_ID, String.valueOf(loginResponse.userCategoryId));
                CommonTask.saveDataIntoPreference(this, CommonConstant.MOBILE_VERIFY, loginResponse.isMobileVerified ? "1" : "0");

                // save user credential (mobile number and password)
                CommonTask.saveDataIntoPreference(this, CommonConstant.USER_MOBILE_NUMBER, userName.getText().toString().trim());
                CommonTask.saveDataIntoPreference(this, CommonConstant.USER_PASSWORD, password.getText().toString().trim());

                if (loginResponse.isEmailVerified && loginResponse.userCategoryId == 2) {
                    finish();
                    if (loginResponse.activityId > 0) {
                        CommonTask.saveDataIntoPreference(this, CommonConstant.REQUESTED_BY_PASSENGER, "1");
                    } else {
                        CommonTask.saveDataIntoPreference(this, CommonConstant.REQUESTED_BY_PASSENGER, "");
                    }
                    startActivity(new Intent(this, PassengerHomePageActivity.class));
                } else if (loginResponse.isEmailVerified && (loginResponse.userCategoryId == 1 || loginResponse.userCategoryId == 3)) {
                    finish();
                    if (loginResponse.carID > 0)
                        CommonTask.saveDataIntoPreference(this, CommonConstant.USER_CAR_ID, String.valueOf(loginResponse.carID));
                    if(loginResponse.isProfileApprovedByAdmin)
                        CommonTask.saveDataIntoPreference(this, CommonConstant.DRIVER_APPROVED_BY_ADMIN, "1");
                    else
                        CommonTask.saveDataIntoPreference(this, CommonConstant.DRIVER_APPROVED_BY_ADMIN, "0");
                    if(loginResponse.numberOfSit>0)
                        CommonTask.saveDataIntoPreference(this, CommonConstant.DRIVER_NUMBER_OF_SIT, String.valueOf(loginResponse.numberOfSit));
                    if (loginResponse.requestId > 0) {
                        CommonTask.saveDataIntoPreference(this, CommonConstant.DRIVER_REQUEST_ID, String.valueOf(loginResponse.requestId));
                        startActivity(new Intent(this, DriverNavigationActivity.class));
                    } else {
                        startActivity(new Intent(this, DriverHomeActivity.class));
                    }
                } else if (!loginResponse.isEmailVerified) {
                    finish();
                    startActivity(new Intent(this, PasswordChangeActivity.class));
                }
            }else{
                new AlertDialog.Builder(this)
                        .setTitle("Suspended")
                        .setMessage("Your account is temporarily suspended. Please contact with CholoEkSathe admin for further information.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        }else{
            CommonTask.showAlert(this, "Login Failed", "Incorrect mobile number or password!");
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDiaglo.dismiss();
        CommonTask.analyzeVolleyError("Login Activity", error);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }


    @Override
    public void onAllow() {
        CommonTask.saveDataIntoPreference(this, CommonConstant.SPLASH_PERMISSION_ALLOW, "1");
        CommonTask.goSettingsActivity(LoginActivity.this);
    }

    @Override
    public void onDeny() {
        handler = new RuntimePermissionHandler(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, CommonConstant.RUNTIME_PERMISSION, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
