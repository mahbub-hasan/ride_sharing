package com.oss_net.choloeksathe.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.base.FCMChangeService;
import com.oss_net.choloeksathe.entity.databases.remote_model.BaseResponse;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MobileVerificationActivity extends AppCompatActivity  {

    private EditText code1,code2,code3,code4,code5,code6;
    private Button verify, buttonResend;
    private String mobileNumber;
    private TextView header, footer;
    int size = 1;
    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mobileNumber = bundle.getString("MOBILE_NUMBER");
        }

        header = findViewById(R.id.header);
        header.setText(String.format(Locale.getDefault(), "We just sent you an SMS with a code to \n%s",mobileNumber));
        footer = findViewById(R.id.footer);

        code1 = findViewById(R.id.code1);
        code2 = findViewById(R.id.code2);
        code3 = findViewById(R.id.code3);
        code4 = findViewById(R.id.code4);
        code5 = findViewById(R.id.code5);
        code6 = findViewById(R.id.code6);
        verify = findViewById(R.id.buttonVerify);
        buttonResend = findViewById(R.id.buttonResend);

        buttonResend.setEnabled(false);




        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(code1.getText().toString().length() == size){
                    code2.requestFocus();
                }else{
                    verify.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(code2.getText().toString().length() == size){
                    code3.requestFocus();
                }else{
                    verify.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(code3.getText().toString().length() == size){
                    code4.requestFocus();
                }else{
                    verify.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(code4.getText().toString().length() == size){
                    code5.requestFocus();
                }else{
                    verify.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(code5.getText().toString().length() == size){
                    code6.requestFocus();
                }else{
                    verify.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        code6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(code6.getText().toString().length() == size){
                    verify.setVisibility(View.VISIBLE);
                }else{
                    verify.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonTask.checkInternet(MobileVerificationActivity.this)){
                    verifyCode();
                }
            }
        });

        sendCodeRequest();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * need for Android 6 real time permissions
     */

    private void sendCodeRequest() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please wait for verification code");
        progress.setCancelable(false);
        progress.show();

        String url = String.format(Locale.getDefault(), CommonURL.getInstance().getVerificationCode,Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.USER_ID)),mobileNumber);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progress.dismiss();
                BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                if(baseResponse.isSuccess){
                    CommonTask.showToast(MobileVerificationActivity.this, "Verification code send to mobile sms");
                    timeCountDown();
                }else{
                    CommonTask.showToast(MobileVerificationActivity.this, baseResponse.message);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                CommonTask.analyzeVolleyError("Mobile verification", error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = String.format(Locale.getDefault(), "%s:%s",
                        CommonTask.getDataFromPreference(MobileVerificationActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                        CommonTask.getDataFromPreference(MobileVerificationActivity.this, CommonConstant.USER_PASSWORD));
                String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                headers.put("Content-Type","application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(request);
    }


    private void verifyCode() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        progress.show();

        String url = String.format(Locale.getDefault(), CommonURL.getInstance().
                        verifyMobileNumber,Integer.parseInt(CommonTask.getDataFromPreference
                        (this, CommonConstant.USER_ID)),mobileNumber,
                String.format(Locale.getDefault(),"%s%s%s%s%s%s",
                code1.getText().toString().trim(),code2.getText().toString().trim(),
                code3.getText().toString().trim(),code4.getText().toString().trim(),
                code5.getText().toString().trim(),code6.getText().toString().trim()));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progress.dismiss();
                BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                if(baseResponse.isSuccess){
                    CommonTask.showToast(MobileVerificationActivity.this, "Mobile verification done...");
                    CommonTask.saveDataIntoPreference(MobileVerificationActivity.this, CommonConstant.MOBILE_VERIFY, "1");
                    onBackPressed();
                }else{
                    CommonTask.showToast(MobileVerificationActivity.this, baseResponse.message);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                CommonTask.analyzeVolleyError("Mobile verification", error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = String.format(Locale.getDefault(), "%s:%s",
                        CommonTask.getDataFromPreference(MobileVerificationActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                        CommonTask.getDataFromPreference(MobileVerificationActivity.this, CommonConstant.USER_PASSWORD));
                String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                headers.put("Content-Type","application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(request);
    }

    /**
     * time count down will start after server confirmation
     * total time 10 minutes.
     * so time calculation will be 9:00 t0 0:00
     * after that reset button will enable
     */
    private void timeCountDown() {
        final SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        timer = new CountDownTimer(600000, 1000){

            @Override
            public void onTick(long millis) {
                footer.setText(String.format(Locale.getDefault(), "cholo ek sathe will sent sms in %s",format.format(new Date(millis))));
            }

            @Override
            public void onFinish() {
                verify.setVisibility(View.GONE);
                buttonResend.setEnabled(true);
            }
        };
        timer.start();
    }


}
