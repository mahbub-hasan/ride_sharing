package com.oss_net.choloeksathe.fragments.driver;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.oss_net.choloeksathe.activities.MobileVerificationActivity;
import com.oss_net.choloeksathe.activities.driver.DriverUserProfileUpdateActivity;
import com.oss_net.choloeksathe.activities.passenger.PassengerProfileActivity;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.remote_model.UserProfileRoot;
import com.oss_net.choloeksathe.utils.CircularImageView;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.InputValidation;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverProfileFragment extends Fragment {



    private boolean currentFragmentVisible, isViewGenerated;
    private CircleImageView userImage;
    private TextView fullName, mobileNo, email, nid, gender, dob, address, homeAddress, companyName;
    private ImageView emailVerify, mobileNumberVerify;
    private ProgressDialog progress;
    private InputValidation inputValidation = new InputValidation();
    private  UserProfileRoot root;

    public DriverProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            currentFragmentVisible = isVisibleToUser;
            if(isViewGenerated)
                getCurrentUserInfo();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_driver_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        userImage = view.findViewById(R.id.userImage);
        fullName = view.findViewById(R.id.userFullName);
        mobileNo = view.findViewById(R.id.userMobileNo);
        email = view.findViewById(R.id.userEmail);
        nid = view.findViewById(R.id.userNID);
        gender = view.findViewById(R.id.userGender);
        dob = view.findViewById(R.id.userDOB);
        address = view.findViewById(R.id.userAddress);
        homeAddress = view.findViewById(R.id.userHomeAddress);
        companyName = view.findViewById(R.id.userCompanyName);
        emailVerify = view.findViewById(R.id.imageEmailVerify);
        mobileNumberVerify = view.findViewById(R.id.imageMobileVerify);


        mobileNumberVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonTask.goSettingsActivity(getActivity());
                Intent intent = new Intent(getActivity(), MobileVerificationActivity.class);
                intent.putExtra("MOBILE_NUMBER", mobileNo.getText().toString());
                startActivity(intent);
            }

        });





        isViewGenerated = true;

        if(currentFragmentVisible){
            getCurrentUserInfo();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_driver_profile_update, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_edit){
            if(root != null && root.isSuccess){
                Intent intent = new Intent(getActivity(), DriverUserProfileUpdateActivity.class);
                intent.putExtra("UserProfile", root);
                startActivity(intent);
            }
        }else if(item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }
        return true;
    }

    private void getCurrentUserInfo() {
        try {
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();

            String url = String.format(CommonURL.getInstance().profileInfo, Integer.parseInt(CommonTask.getDataFromPreference(getActivity(), CommonConstant.USER_ID)));

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    root = new Gson().fromJson(response.toString(), UserProfileRoot.class);
                    if(root.isSuccess){
                        setDataIntoView(root);
                    }else{
                        CommonTask.showToast(getActivity(), "Something error. Please try again.");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    CommonTask.analyzeVolleyError("User Profile", error);
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
            CommonTask.showLog(ex.getMessage());
        }
    }

    private void setDataIntoView(UserProfileRoot root) {
        try {
            if(root.userProfileResponse.imageLocation!= null){
                Glide.with(this).load(CommonTask.getGlideUrl(root.userProfileResponse.imageLocation, getActivity())).into(userImage);
            }else {
                Glide.with(this).load(CommonURL.getInstance().emptyPictureUrl).into(userImage);
            }
            fullName.setText(root.userProfileResponse.firstName);
            mobileNo.setText(root.userProfileResponse.mobileNumber);
            email.setText(String.format("Email\n%s",root.userProfileResponse.email));
            nid.setText(String.format("National identification number\n%s",root.userProfileResponse.nid));
            gender.setText(String.format("Gender\n%s",root.userProfileResponse.gender));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy");
            dob.setText(String.format("Date of birth\n%s",simpleDateFormat.format(new Date(root.userProfileResponse.dob))));
            address.setText(String.format("Address\n%s",root.userProfileResponse.address));
            homeAddress.setText(String.format("Home location\n%s",CommonTask.getAddress(getActivity(), root.userProfileResponse.homeLatitude, root.userProfileResponse.homeLongitude)));
            companyName.setText(String.format(Locale.getDefault(), "%s\n%s", "Company Name", root.userProfileResponse.companyName));
            if(root.userProfileResponse.emailVerified){
                emailVerify.setImageDrawable(getResources().getDrawable(R.drawable.ic_verified_user_black_24px));
            }else{
                emailVerify.setImageDrawable(getResources().getDrawable(R.drawable.ic_not_verified_black_24dp));

            }

            if(root.userProfileResponse.mobileVerified){
                mobileNumberVerify.setImageDrawable(getResources().getDrawable(R.drawable.ic_verified_user_black_24px));
            }else{
                mobileNumberVerify.setImageDrawable(getResources().getDrawable(R.drawable.ic_not_verified_black_24dp));
            }
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }
}
