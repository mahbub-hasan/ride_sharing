package com.oss_net.choloeksathe.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.passenger.PassengerHomePageActivity;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.base.FCMChangeService;
import com.oss_net.choloeksathe.entity.databases.remote_model.BaseResponse;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaymentWithRatingActivity extends AppCompatActivity {
    TextView totalFare;
    Button buttonContinue;
    RatingBar ratingBar;
    private int amount;
    private int activityId;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentwith_rating);

        totalFare = findViewById(R.id.totalFare);
        ratingBar = findViewById(R.id.userRating);
        buttonContinue = findViewById(R.id.buttonContinue);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            amount = bundle.getInt("AMOUNT");
            activityId = bundle.getInt("ACTIVITY_ID");
            totalFare.setText(String.format(Locale.getDefault(), "%d",amount));
        }

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInformationToServer();
            }
        });
    }

    private void sendInformationToServer() {
        try {
            progressBar = new ProgressDialog(this);
            progressBar.setMessage("Please wait...");
            progressBar.setCancelable(false);
            progressBar.show();

            Calendar calendar = Calendar.getInstance();
            JSONObject object = new JSONObject();
            object.put("ratingPoint", ratingBar.getRating());
            object.put("comments", "OK");
            object.put("ratingDate", calendar.getTimeInMillis());

            JSONObject activityInfoObject = new JSONObject();
            activityInfoObject.put("activityId", activityId);

            JSONObject userInfoObject = new JSONObject();
            userInfoObject.put("userId",Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.USER_ID)));

            object.put("activityInfoByActivityId", activityInfoObject);
            object.put("userInfoByRatingBy", userInfoObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, CommonURL.getInstance().passengerRating, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressBar.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        finish();
                        Intent mainIntent = new Intent(PaymentWithRatingActivity.this, PassengerHomePageActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }else{
                        CommonTask.showToast(PaymentWithRatingActivity.this, baseResponse.message);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.dismiss();
                    CommonTask.analyzeVolleyError(PaymentWithRatingActivity.class.getName(),error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(PaymentWithRatingActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(PaymentWithRatingActivity.this, CommonConstant.USER_PASSWORD));
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
    public void onBackPressed() {

    }
}
