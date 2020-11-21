package com.oss_net.choloeksathe.fragments.driver;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
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
import com.oss_net.choloeksathe.activities.driver.DriverCarInfoUpdateActivity;
import com.oss_net.choloeksathe.activities.driver.DriverCarSetupActivity;
import com.oss_net.choloeksathe.adapter.CarTypeAdapter;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarInfoRoot;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarTypeList;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarTypeListRoot;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverCarProfileFragment extends Fragment {


    private boolean currentFragmentVisible, isViewGenerated;
    CircleImageView carImage;
    public  int carTypeId;
    TextView carName, carModel, carOwnerName, carOwnerContactNumber, carDetails,cardriverName,cardriverMoblie,cardriverRegNo, carType,
            carNumberOfSit;
    CheckBox isCarActive, isCarAcAvailable;
    private ProgressDialog progress;
    private CarInfoRoot root;

    public DriverCarProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            currentFragmentVisible = isVisibleToUser;
            if(isViewGenerated) {
                getCarInfo();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_driver_car_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        carImage = view.findViewById(R.id.carImage);
        carName = view.findViewById(R.id.carName);
        carModel = view.findViewById(R.id.carModel);
        carOwnerName = view.findViewById(R.id.carOwnerName);
        carOwnerContactNumber = view.findViewById(R.id.carOwnerContactNumber);
        carDetails = view.findViewById(R.id.carDetails);
        cardriverName = view.findViewById(R.id.driverName);
        cardriverMoblie = view.findViewById(R.id.driverMobile);
        cardriverRegNo = view.findViewById(R.id.driverRegNo);
        carType = view.findViewById(R.id.carType);
        isCarActive = view.findViewById(R.id.carActiveStatus);
        isCarAcAvailable = view.findViewById(R.id.carACAvailableStatus);
        carNumberOfSit = view.findViewById(R.id.carNumberOfSit);

        isViewGenerated = true;
        isCarActive.setClickable(false);
        isCarAcAvailable.setClickable(false);

        if(currentFragmentVisible) {
            getCarInfo();
        }
    }

    private void getCarInfo() {
        try {
            if(!CommonTask.getDataFromPreference(getActivity(), CommonConstant.USER_CAR_ID).isEmpty()){
                progress = new ProgressDialog(getActivity());
                progress.setMessage("Please wait...");
                progress.setCancelable(false);
                progress.show();

                String url = String.format(CommonURL.getInstance().carProfile,Integer.parseInt(CommonTask.getDataFromPreference(getActivity(), CommonConstant.USER_CAR_ID)));

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progress.dismiss();
                        root = new Gson().fromJson(response.toString(), CarInfoRoot.class);

                        if(root.isSuccess){
                            setInfoIntoView();

                        }else{
                            CommonTask.showToast(getActivity(), root.message);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

            }else{
                CommonTask.showToast(getActivity(), "No car info available.");
            }
        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    private void setInfoIntoView() {
        try {
            if(root.carInfoByCarIdResponse.image == null || root.carInfoByCarIdResponse.image.isEmpty()){
                Glide.with(getActivity()).load(CommonURL.getInstance().emptyPictureUrl).into(carImage);
            }else{
                Glide.with(getActivity()).load(CommonTask.getGlideUrl(root.carInfoByCarIdResponse.image, getActivity())).into(carImage);
            }
            carName.setText(root.carInfoByCarIdResponse.carNumber);
            carModel.setText(root.carInfoByCarIdResponse.carModels);
            carOwnerName.setText(String.format("Owner Name\n%s", root.carInfoByCarIdResponse.owner));
            carOwnerContactNumber.setText(String.format("Owner Contact\n%s",root.carInfoByCarIdResponse.ownerContact));
            carDetails.setText(String.format("Details\n%s", root.carInfoByCarIdResponse.detailsSpecification));
            cardriverName.setText(String.format("Driver Name\n%s", root.carInfoByCarIdResponse.driverName));
            cardriverMoblie.setText(String.format("Driver Contact\n%s",root.carInfoByCarIdResponse.driverMobile));
            cardriverRegNo.setText(String.format("Driver RegNo\n%s", root.carInfoByCarIdResponse.driverRegNo));
            carNumberOfSit.setText(String.format(Locale.getDefault(),"Number of sit\n%d",root.carInfoByCarIdResponse.sitNumber));


            isCarActive.setChecked(root.carInfoByCarIdResponse.ac);
            isCarAcAvailable.setChecked(root.carInfoByCarIdResponse.active);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CommonURL.getInstance().getCarTypeList, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    final CarTypeListRoot carTypeListRoot = new Gson().fromJson(response.toString(), CarTypeListRoot.class);
                    if (carTypeListRoot.isSuccess) {
                        for (CarTypeList ctList: carTypeListRoot.carTypeList) {
                            if(ctList.carTypeId==root.carInfoByCarIdResponse.carType){
                                carType.setText(ctList.carTypeName);
                                break;
                            }
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CommonTask.analyzeVolleyError(DriverCarSetupActivity.class.getName(), error);
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);


        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_driver_car_info_setup, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_edit){
            if(root != null && root.isSuccess){
                Intent intent = new Intent(getActivity(), DriverCarInfoUpdateActivity.class);
                intent.putExtra("CarProfile", root);
                startActivity(intent);
            }
        }else if(item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }
        return true;
    }
}
