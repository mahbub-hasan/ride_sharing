package com.oss_net.choloeksathe.activities.driver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
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
import com.oss_net.choloeksathe.activities.LoginActivity;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.base.FCMChangeService;
import com.oss_net.choloeksathe.entity.databases.remote_model.BaseResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.DriverDashboard;
import com.oss_net.choloeksathe.entity.databases.remote_model.DriverDashboardRoot;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.oss_net.choloeksathe.utils.CommonTask.getGlideUrl;

public class DriverHomeActivity extends AppCompatActivity{
    TextView shareACar, carShareHistory, userProfile, carSetup, driverLogout, driverName, driverContactNumber, driverTotalIncome, driverLastIncome, driverCurrentIncome,driverRating;
    private ProgressDialog progressDialog;
    private CircleImageView driverImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        startService(new Intent(this, FCMChangeService.class));

        shareACar = findViewById(R.id.shareACar);
        carShareHistory = findViewById(R.id.carShareHistory);
        userProfile = findViewById(R.id.userProfile);
        carSetup = findViewById(R.id.carSetup);
        driverLogout = findViewById(R.id.settings);
        driverImage = findViewById(R.id.driverImage);
        driverName = findViewById(R.id.driverName);
        driverContactNumber = findViewById(R.id.drivercontactNumber);
        driverTotalIncome = findViewById(R.id.drivertotalIncome);
        driverLastIncome = findViewById(R.id.driverlastIncome);
        driverCurrentIncome = findViewById(R.id.drivercurrentIncome);
        driverRating = findViewById(R.id.rating);

        shareACar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!CommonTask.checkInternet(DriverHomeActivity.this)){
                    CommonTask.goSettingsActivity(DriverHomeActivity.this);
                    return;
                }
                if(CommonTask.getDataFromPreference(DriverHomeActivity.this, CommonConstant.USER_CAR_ID).isEmpty()){
                    CommonTask.showToast(DriverHomeActivity.this, "No car information has been found yet. Please set your car and then post a request.");
                    return;
                }
                if(CommonTask.getDataFromPreference(DriverHomeActivity.this, CommonConstant.MOBILE_VERIFY).equals("0")){
                    CommonTask.showToast(DriverHomeActivity.this, "Please verify your account.");
                    return;
                }
                if(CommonTask.getDataFromPreference(DriverHomeActivity.this, CommonConstant.DRIVER_APPROVED_BY_ADMIN).equals("0")){
                    CommonTask.showToast(DriverHomeActivity.this, "Admin is not approved your account.");
                    return;
                }
                startActivity(new Intent(DriverHomeActivity.this, DriverCarShareActivity.class));
            }
        });

        carShareHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!CommonTask.checkInternet(DriverHomeActivity.this)) {
                    CommonTask.goSettingsActivity(DriverHomeActivity.this);
                    return;
                }
                    startActivity(new Intent(DriverHomeActivity.this,
                            DriverHistoryActivity.class));

            }
        });
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!CommonTask.checkInternet(DriverHomeActivity.this)) {
                    CommonTask.goSettingsActivity(DriverHomeActivity.this);
                    return;
                }
                    startActivity(new Intent(DriverHomeActivity.this,
                            DriverProfileUpdateActivity.class));

            }
        });
        carSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!CommonTask.checkInternet(DriverHomeActivity.this)) {
                    CommonTask.goSettingsActivity(DriverHomeActivity.this);
                    return;
                }
                startActivity(new Intent(DriverHomeActivity.this,
                            DriverCarSetupActivity.class));
            }
        });

        driverLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CommonTask.checkInternet(DriverHomeActivity.this)) {
                    CommonTask.goSettingsActivity(DriverHomeActivity.this);
                    return;
                }

                userLogoutRequest();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!CommonTask.checkInternet(this)){
            CommonTask.goSettingsActivity(this);
            return;
        }
        getBackgroundInformationData();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void getBackgroundInformationData() {
        try {
            String url = String.format(Locale.getDefault(), CommonURL.getInstance().driverDashboard,Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.USER_ID)));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    DriverDashboardRoot root = new Gson().fromJson(response.toString(), DriverDashboardRoot.class);
                    if(root.isSuccess && root.driverDashboard != null){
                        setDataInfoView(root.driverDashboard);
                    }else{
                        CommonTask.showToast(DriverHomeActivity.this, root.message);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CommonTask.analyzeVolleyError("Driver Home page", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverHomeActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverHomeActivity.this, CommonConstant.USER_PASSWORD));
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

    private void setDataInfoView(DriverDashboard driverDashboard) {
        try {
            if(driverDashboard.imageUrl!= null){
                Glide.with(this).load(getGlideUrl(driverDashboard.imageUrl, this)).into(driverImage);
            }else {
                Glide.with(this).load(CommonURL.getInstance().emptyPictureUrl).into(driverImage);
            }
            driverName.setText(driverDashboard.fullName);
            driverContactNumber.setText(driverDashboard.contactNumber);
            driverTotalIncome.setText(String.format(Locale.getDefault(), "%.2f",driverDashboard.totalIncome));
            driverLastIncome.setText(String.format(Locale.getDefault(), "%.2f", driverDashboard.lastRequestIncome));
            driverCurrentIncome.setText(String.format(Locale.getDefault(), "%.2f", driverDashboard.currentRequestIncome));
            driverRating.setText(""+driverDashboard.totalRating+"/5");
            if(!driverDashboard.active){
                new AlertDialog.Builder(this)
                        .setTitle("Activity")
                        .setMessage("Sorry, your account is currently not active. Please contact with admin.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                userLogoutRequest();
                            }
                        })
                        .create()
                        .show();
            }
            CommonTask.saveDataIntoPreference(this, CommonConstant.DRIVER_APPROVED_BY_ADMIN, driverDashboard.adminActive?"1":"0");
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    private void userLogoutRequest() {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();



            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CommonURL.getInstance().userLogout + "/" + Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.USER_ID)), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        CommonTask.saveDataIntoPreference(DriverHomeActivity.this, CommonConstant.USER_ID, "");
                        CommonTask.saveDataIntoPreference(DriverHomeActivity.this, CommonConstant.USER_CATEGORY_ID, "");
                        CommonTask.saveDataIntoPreference(DriverHomeActivity.this, "ActivityID", "");
                        CommonTask.saveDataIntoPreference(DriverHomeActivity.this, CommonConstant.REQUESTED_BY_PASSENGER, "");
                        CommonTask.saveDataIntoPreference(DriverHomeActivity.this, CommonConstant.USER_MOBILE_NUMBER, "");
                        CommonTask.saveDataIntoPreference(DriverHomeActivity.this, CommonConstant.USER_PASSWORD, "");

                        Intent intent = new Intent(DriverHomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    CommonTask.analyzeVolleyError("DriverMainActivty#Logout", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverHomeActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverHomeActivity.this, CommonConstant.USER_PASSWORD));
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



}
