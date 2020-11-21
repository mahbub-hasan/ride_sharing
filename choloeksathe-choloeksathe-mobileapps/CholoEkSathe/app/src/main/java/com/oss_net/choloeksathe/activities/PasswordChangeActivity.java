package com.oss_net.choloeksathe.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.driver.DriverHomeActivity;
import com.oss_net.choloeksathe.activities.passenger.PassengerHomePageActivity;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.base.FCMChangeService;
import com.oss_net.choloeksathe.entity.databases.remote_model.BaseResponse;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PasswordChangeActivity extends AppCompatActivity implements
        Response.Listener<JSONObject>, Response.ErrorListener {

    EditText userPassword, userConfirmPassword;
    Button buttonPasswordChange;
    CheckBox showPassword;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userPassword = findViewById(R.id.userNewPassword);
        userConfirmPassword = findViewById(R.id.userNewConfirmPassword);
        showPassword = findViewById(R.id.ckShowPassword);
        buttonPasswordChange = findViewById(R.id.buttonPasswordChange);
        buttonPasswordChange.setEnabled(false);

        userConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    buttonPasswordChange.setEnabled(true);
                }else{
                    buttonPasswordChange.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    userPassword.setTransformationMethod(new PasswordTransformationMethod());
                    userConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                }else{
                    userPassword.setTransformationMethod(null);
                    userConfirmPassword.setTransformationMethod(null);
                }
            }
        });


        buttonPasswordChange.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                CommonTask.goSettingsActivity(PasswordChangeActivity.this);


                    if (userPassword.getText().toString().isEmpty()) {
                        userPassword.setError("Please enter password");
                        return;
                    }
                    if (userConfirmPassword.getText().toString().isEmpty()) {
                        userConfirmPassword.setError("Please enter confirm password");
                        return;
                    }
                    if(userConfirmPassword.getText().toString().length() > 5 &&
                            userPassword.getText().toString().length() > 5){

                        if (userPassword.getText().toString().equals(userConfirmPassword.getText().toString())) {
                            userPassword.setError(null);
                            userConfirmPassword.setError(null);
                            changePassword();
                        }

                        else {
                            userConfirmPassword.setError("Password not match");
                        }

                    }
                    else {
                        userConfirmPassword.setError("Password length must be minimum six digit");
                    }

            }
        });
    }

    private void changePassword() {
        try {
            progress = new ProgressDialog(this);
            progress.setMessage("Password changes, please wait...");
            progress.setCancelable(false);
            progress.show();

            String url = String.format(Locale.getDefault(), CommonURL.getInstance().passwordChanges,
                    userPassword.getText().toString().trim(),
                    Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.USER_ID)));

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, this, this){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(PasswordChangeActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(PasswordChangeActivity.this, CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        progress.dismiss();
        BaseResponse baseResponse = new Gson().fromJson(response.toString(),
                BaseResponse.class);
        if(baseResponse.isSuccess){
            String userCategoryId = CommonTask.getDataFromPreference(this,
                    CommonConstant.USER_CATEGORY_ID);
            if(userCategoryId.equals("2")){
                startActivity(new Intent(this,
                        PassengerHomePageActivity.class));
            }else{
                startActivity(new Intent(this,
                        DriverHomeActivity.class));
            }
            this.finish();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progress.dismiss();
        CommonTask.analyzeVolleyError("Password change", error);
    }
}
