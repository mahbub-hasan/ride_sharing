package com.oss_net.choloeksathe.fragments.driver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.driver.DriverCarShareListInfoActivity;
import com.oss_net.choloeksathe.adapter.CarShareListAdapter;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.remote_model.BaseResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarShareList;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarShareListRoot;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DriverHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Response.Listener<JSONObject>, Response.ErrorListener, CarShareListAdapter.onItemClick {


    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private CarShareListRoot root;
    private CarShareListAdapter adapter;
    private ProgressDialog progress;
    private LinearLayout llMessage;

    public DriverHomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_driver_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        progressBar = view.findViewById(R.id.progressBar);

        recyclerView = view.findViewById(R.id.driverHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        llMessage = view.findViewById(R.id.llMessage);

        getCarShareListFromServer();
    }

    private void getCarShareListFromServer() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CommonURL.getInstance().carShareList+"/"+Integer.parseInt(CommonTask.getDataFromPreference(getActivity(), CommonConstant.USER_ID)),null, this, this){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(getActivity(), CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(getActivity(), CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    @Override
    public void onRefresh() {
        getCarShareListFromServer();
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onResponse(JSONObject response) {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setEnabled(false);
        root = new Gson().fromJson(response.toString(), CarShareListRoot.class);
        if(root.isSuccess && root.responses != null && root.responses.size()>0){
            llMessage.setVisibility(View.GONE);
            adapter = new CarShareListAdapter(root.responses);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(this);


        }else{
            llMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressBar.setVisibility(View.GONE);
        CommonTask.analyzeVolleyError("CarShareListActivity", error);

    }

    @Override
    public void setOnItemClick(int position) {
        CarShareList carShareList = root.responses.get(position);
        if (carShareList != null) {
            Intent intent = new Intent(getActivity(), DriverCarShareListInfoActivity.class);
            intent.putExtra(CommonConstant.CAR_REQUEST_ID, carShareList.requestId);
            startActivity(intent);
        }
    }

    @Override
    public void setOnCancelRequest(int position) {
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Request canceling, please wait...");
        progress.setCancelable(false);
        progress.show();

        CarShareList carShareList = root.responses.get(position);
        if(carShareList != null){
            String url = String.format(CommonURL.getInstance().driverRequestCancel,carShareList.requestId);
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        CommonTask.showToast(getActivity(), "Request cancel by car owner");
                        getCarShareListFromServer();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    CommonTask.analyzeVolleyError("Driver main activity", error);
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }
    }

    @Override
    public void setOnJourneyStartListener(int position) {
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Journey starting, please wait...");
        progress.setCancelable(false);
        progress.show();

        final CarShareList carShareList = root.responses.get(position);
        if(carShareList != null){
            String url = String.format(CommonURL.getInstance().driverStatusChange,carShareList.requestId,6);
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        CommonTask.showToast(getActivity(), "your journey is starting now");

                        // goto another intent for navigation
                        Intent intent = new Intent(getActivity(), DriverCarShareListInfoActivity.class);
                        intent.putExtra(CommonConstant.CAR_REQUEST_ID, carShareList.requestId);
                        startActivity(intent);

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    CommonTask.analyzeVolleyError("Driver main activity", error);
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }
    }

    @Override
    public void setOnJourneyStopListener(int position) {
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Journey complete, please wait...");
        progress.setCancelable(false);
        progress.show();

        CarShareList carShareList = root.responses.get(position);
        if(carShareList != null){
            String url = String.format(CommonURL.getInstance().driverStatusChange,carShareList.requestId,7);
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        CommonTask.showToast(getActivity(), "your journey is complete now");
                        getCarShareListFromServer();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    CommonTask.analyzeVolleyError("Driver main activity", error);
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }
    }
}
