package com.oss_net.choloeksathe.fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.driver.DriverNavigationActivity;
import com.oss_net.choloeksathe.adapter.CompanyListAdapter;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.remote_model.CompanyList;
import com.oss_net.choloeksathe.entity.databases.remote_model.CompanyRoot;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyInfoFragment extends DialogFragment implements SwipeRefreshLayout.OnRefreshListener {
    private Dialog dialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CompanyList companyList;
    private onPrivateSharing mPrivateSharing;

    public CompanyInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_company_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Company List");
        toolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        }

        recyclerView = view.findViewById(R.id.companyList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        getCompanyList();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        return dialog;
    }

    private void getCompanyList() {
        try {
            swipeRefreshLayout.setRefreshing(true);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CommonURL.getInstance().companyList, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    swipeRefreshLayout.setRefreshing(false);
                    final CompanyRoot root = new Gson().fromJson(response.toString(), CompanyRoot.class);
                    if(root.isSuccess && root.companyList != null && root.companyList.size()>0){
                        CompanyListAdapter adapter = new CompanyListAdapter(root.companyList, getActivity());
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(new CompanyListAdapter.onItemClick() {
                            @Override
                            public void setOnItemClick(int position) {
                                setSelectedItem(root.companyList.get(position));
                            }
                        });
                    }else{
                        CommonTask.showToast(getActivity(), root.message);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    swipeRefreshLayout.setRefreshing(false);
                    CommonTask.analyzeVolleyError("Company list", error);
                }
            }){
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

            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    private void setSelectedItem(CompanyList companyList) {
        this.companyList = companyList;
    }

    @Override
    public void onRefresh() {
        getCompanyList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_fragment_stop_over, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            dialog.dismiss();
        }else if(item.getItemId() == R.id.action_save){
            dialog.dismiss();
            if(mPrivateSharing != null)
                mPrivateSharing.onStopOverSelectListener(this.companyList);
        }
        return true;
    }

    public interface onPrivateSharing{
        void onStopOverSelectListener(CompanyList companyList);
    }

    public void setOnPrivateSharing(onPrivateSharing privateSharing){
        mPrivateSharing = privateSharing;
    }
}
