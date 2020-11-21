package com.oss_net.choloeksathe.base;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.entity.databases.remote_model.BaseResponse;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FCMChangeService extends IntentService implements Response.Listener<JSONObject>, Response.ErrorListener {

    public FCMChangeService() {
        super("FCMChangeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sendFCMKeyIntoServer();
    }

    private void sendFCMKeyIntoServer() {
        try {
            JSONObject object = new JSONObject();
            object.put("userId", Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.USER_ID)));
            object.put("fcmKey", CommonTask.getDataFromPreference(this, CommonConstant.FCM_KEY));

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, CommonURL.getInstance().changeFCMKey, object, this, this){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(FCMChangeService.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(FCMChangeService.this, CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request, "TAG");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
        if(!baseResponse.isSuccess){
            CommonTask.showToast(this, baseResponse.message);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        CommonTask.analyzeVolleyError(FCMChangeService.class.getName(), error);
    }
}
