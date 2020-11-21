package com.oss_net.choloeksathe.activities;

import android.Manifest;
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
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.adapter.GenderAdapter;
import com.oss_net.choloeksathe.adapter.UserCategoryAdapter;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.UserCategory;
import com.oss_net.choloeksathe.entity.databases.remote_model.BaseResponse;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.InputValidation;
import com.oss_net.choloeksathe.utils.RuntimePermissionHandler;

import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;

public class RegistrationActivity extends AppCompatActivity implements Response.Listener<JSONObject>,
        Response.ErrorListener,RuntimePermissionHandler.RuntimePermissionListener {

    EditText fullName, email, mobileNumber;
    AppCompatSpinner userCategory, gender;
    UserCategoryAdapter adapter;
    GenderAdapter genderAdapter;
    Button registrationButton;
    String genderInfo;
    int userCategoryID;
    private ProgressDialog progressDiaglo;
    private RuntimePermissionHandler handler;

    private InputValidation inputValidation = new InputValidation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fullName = findViewById(R.id.tilFullName);
        email = findViewById(R.id.tilEmail);
        mobileNumber = findViewById(R.id.tilMobileNumber);

        userCategory = findViewById(R.id.spinnerUserCategory);
        gender = findViewById(R.id.spinnerGender);

        registrationButton = findViewById(R.id.buttonRegistration);


        setInfoIntoUserCategory();

        setGenderInformation();


        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!CommonTask.checkInternet(RegistrationActivity.this)){
                    CommonTask.goSettingsActivity(RegistrationActivity.this);
                    return;
                }
                if (isValid())
                    doRegistration();

                }
        });



    }

    private boolean isValid(){
        if(TextUtils.isEmpty(fullName.getText().toString())){
            fullName.setError("Full name is required.");
            return false;
        }else if(TextUtils.isEmpty(mobileNumber.getText().toString())){
            mobileNumber.setError("Mobile number is required");
            return false;
        }else if(!Patterns.PHONE.matcher(mobileNumber.getText().toString()).matches()){
            mobileNumber.setError("Mobile number not matched");
            return false;
        }else if(TextUtils.isEmpty(email.getText().toString())){
            email.setError("Email address is required");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError("Email address not matched");
            return false;
        }
        return true;
    }

    private void setGenderInformation() {
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
    }

    private void setInfoIntoUserCategory() {
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<UserCategory> userCategories = realm.where(UserCategory.class).findAll();
        adapter = new UserCategoryAdapter(this, userCategories);
        userCategory.setAdapter(adapter);

        userCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                userCategoryID = userCategories.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.oss-net.splash.confirm")){
                Bundle bundle = intent.getExtras();
                if(bundle != null){
                    String status = bundle.getString("status");
                    if(status.equals("sync_done")){
                        CommonTask.goSettingsActivity(RegistrationActivity.this);
                    }else{
                        CommonTask.showToast(RegistrationActivity.this, "Something error. Please re-start your application");
                    }
                }
            }
        }
    };



    private void doRegistration() {
        try {
            progressDiaglo = new ProgressDialog(this);
            progressDiaglo.setMessage("Please wait...");
            progressDiaglo.setCancelable(false);
            progressDiaglo.show();


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("firstName", fullName.getText().toString().trim());
            jsonObject.put("userName", email.getText().toString().trim());
            jsonObject.put("mobileNumber", mobileNumber.getText().toString().trim());
            jsonObject.put("gender", genderInfo);
            jsonObject.put("fcmKey", CommonTask.getDataFromPreference(this, CommonConstant.FCM_KEY));
            jsonObject.put("userCategoryId", userCategoryID);

            CommonTask.showLog(jsonObject.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, CommonURL.getInstance().userRegistration,jsonObject,this, this);
            request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDiaglo.dismiss();
        BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
        if(baseResponse.isSuccess){
            confirmationMessage();
        }else{
            CommonTask.showToast(this, baseResponse.message);
        }
    }

    private void confirmationMessage() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("User SignUp")
                .setMessage("Your password has been sent to your email." +
                        " Please check your email for next procedure.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();
                    }
                }).create().show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDiaglo.dismiss();
        CommonTask.analyzeVolleyError("Registration Activity ", error);
    }

    @Override
    public void onAllow() {
        CommonTask.saveDataIntoPreference(this, CommonConstant.SPLASH_PERMISSION_ALLOW, "1");
        CommonTask.goSettingsActivity(RegistrationActivity.this);
    }

    @Override
    public void onDeny() {
        handler = new RuntimePermissionHandler(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, CommonConstant.RUNTIME_PERMISSION, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("com.oss-net.splash.confirm"));
        if(CommonTask.getDataFromPreference(this, CommonConstant.SPLASH_PERMISSION_ALLOW).isEmpty()){
            handler = new RuntimePermissionHandler(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, CommonConstant.RUNTIME_PERMISSION, this);
        }else{
            CommonTask.goSettingsActivity(RegistrationActivity.this);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("com.oss-net.splash.confirm"));
    }
}
