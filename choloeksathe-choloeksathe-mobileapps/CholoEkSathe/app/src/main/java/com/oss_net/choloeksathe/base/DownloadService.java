package com.oss_net.choloeksathe.base;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.entity.databases.ActivityStatus;
import com.oss_net.choloeksathe.entity.databases.JourneyType;
import com.oss_net.choloeksathe.entity.databases.RequestStatusInfo;
import com.oss_net.choloeksathe.entity.databases.RequestType;
import com.oss_net.choloeksathe.entity.databases.UserCategory;
import com.oss_net.choloeksathe.entity.databases.remote_model.ActivityStatusResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.BackgroudDownloadRoot;
import com.oss_net.choloeksathe.entity.databases.remote_model.JourneyTypeResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.RequestStatusInfoResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.RequestTypeResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.UserCategoryResponse;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonEnum;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;

import org.json.JSONObject;

import io.realm.Realm;

public class DownloadService extends IntentService implements Response.Listener<JSONObject>, Response.ErrorListener {

    private Realm realm;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null){
            Realm.init(DownloadService.this);
            downloadBackgroundService();
        }
    }

    private void downloadBackgroundService() {
        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CommonURL.getInstance().backgroundSync,null,this, this);
            request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            BackgroudDownloadRoot root = new Gson().fromJson(response.toString(),BackgroudDownloadRoot.class);
            if(root != null){
                realm = Realm.getDefaultInstance();
                for (UserCategoryResponse userCategoryResponse: root.userCategoryResponses) {
                    realm.beginTransaction();
                    UserCategory userCategory = realm.createObject(UserCategory.class);
                    userCategory.setId(userCategoryResponse.userCategoryId);
                    userCategory.setCategory(userCategoryResponse.userCategory);
                    userCategory.setActive(userCategoryResponse.active);
                    realm.commitTransaction();
                }

                for (RequestTypeResponse requestTypeResponse :
                        root.requestTypeResponses) {
                    realm.beginTransaction();
                    RequestType requestType = realm.createObject(RequestType.class);
                    requestType.setId(requestTypeResponse.requestTypeId);
                    requestType.setRequestType(requestTypeResponse.requestType);
                    requestType.setActive(requestTypeResponse.active);
                    realm.commitTransaction();
                }

                for (RequestStatusInfoResponse requestStatusInfoResponse: root.requestStatusInfoResponses) {
                    realm.beginTransaction();
                    RequestStatusInfo requestStatusInfo = realm.createObject(RequestStatusInfo.class);
                    requestStatusInfo.setId(requestStatusInfoResponse.requestStatusId);
                    requestStatusInfo.setRequestStatus(requestStatusInfoResponse.requestStatus);
                    requestStatusInfo.setActive(requestStatusInfoResponse.active);
                    realm.commitTransaction();
                }

                for (JourneyTypeResponse journeyTypeResponse: root.journeyTypeResponses) {
                    realm.beginTransaction();
                    JourneyType journeyType = realm.createObject(JourneyType.class);
                    journeyType.setId(journeyTypeResponse.journeyTypeId);
                    journeyType.setJourneyType(journeyTypeResponse.journeyType);
                    journeyType.setActive(journeyTypeResponse.active);
                    realm.commitTransaction();
                }

                for (ActivityStatusResponse activityStatusResponse: root.activityStatusResponses) {
                    realm.beginTransaction();
                    ActivityStatus activityStatus = realm.createObject(ActivityStatus.class);
                    activityStatus.setId(activityStatusResponse.activityStatusId);
                    activityStatus.setActivityStatus(activityStatusResponse.activityStatus);
                    activityStatus.setActive(activityStatusResponse.active);
                    realm.commitTransaction();
                }
                CommonTask.saveDataIntoPreference(DownloadService.this, CommonConstant.BACKGROUND_SYNC, CommonEnum.YES.name());
                CommonTask.showLog("========= Data Sync Completed =============");

                Intent intent = new Intent();
                intent.setAction("com.oss-net.splash.confirm");
                intent.putExtra("status", "sync_done");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
            Intent intent = new Intent();
            intent.setAction("com.oss-net.splash.confirm");
            intent.putExtra("status", "sync_failed");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }finally {
            realm.close();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        CommonTask.analyzeVolleyError("Download Background Service", error);
    }
}
