package com.oss_net.choloeksathe.activities.driver;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.RegistrationActivity;
import com.oss_net.choloeksathe.adapter.CarTypeAdapter;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.base.FCMChangeService;
import com.oss_net.choloeksathe.entity.databases.remote_model.BaseResponse;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarInfoRoot;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarTypeListRoot;
import com.oss_net.choloeksathe.utils.CircularImageView;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.InputValidation;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverCarInfoUpdateActivity extends AppCompatActivity {

    CarInfoRoot root;
    CircleImageView carImage;
    EditText carName, carModel, carOwnerName, carOwnerContactNumber, carDetails,
              cardriverName,cardriverMobile,cardriverRegNo, carNumberOfSit;
    AppCompatCheckBox carActiveStatus, carACAvailableStatus;
    AppCompatSpinner carType;
    private boolean carStatusChange;
    private boolean carACStatus;
    private ProgressDialog progress;
    private InputValidation inputValidation = new InputValidation();
    private String base64CarImage=null;
    private int carTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_car_info_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        carImage = findViewById(R.id.carImage);
        carName = findViewById(R.id.carName);
        carModel = findViewById(R.id.carModel);
        carOwnerName = findViewById(R.id.carOwnerName);
        carOwnerContactNumber = findViewById(R.id.carOwnerContactNumber);
        carDetails = findViewById(R.id.carDetails);
        carActiveStatus = findViewById(R.id.carActiveStatus);
        carACAvailableStatus = findViewById(R.id.carACAvailableStatus);
        cardriverName = findViewById(R.id.cardriverName);
        cardriverMobile = findViewById(R.id.cardriverContactNumber);
        cardriverRegNo = findViewById(R.id.cardriverRegNo);
        carType = findViewById(R.id.carType);
        carNumberOfSit = findViewById(R.id.carNumberOfSit);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            root = (CarInfoRoot) bundle.getSerializable("CarProfile");

            setIntoIntoView();
        }

        carActiveStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    CommonTask.goSettingsActivity(DriverCarInfoUpdateActivity.this);
                    carStatusChange = b;
                }


            }
        });

        carACAvailableStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    CommonTask.goSettingsActivity(DriverCarInfoUpdateActivity.this);
                    carACStatus = b;
                }



            }
        });

        carImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoPermission();
            }
        });
    }

    private void takePhotoPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CommonConstant.RUNTIME_PERMISSION);
        }else{
            takePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CommonConstant.RUNTIME_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                takePhoto();
            }else{
                CommonTask.showToast(this, "Please accept permission from settings");
            }
        }
    }

    private void takePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CommonConstant.IMAGE_FROM_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CommonConstant.IMAGE_FROM_CAMERA && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            Bitmap imageBitmap = (Bitmap) bundle.get("data");
            carImage.setImageBitmap(imageBitmap);

            // convert this image into base64 encode string
            if(imageBitmap != null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] byteArrayImage = stream.toByteArray();

                base64CarImage = Base64.encodeToString(byteArrayImage,Base64.NO_WRAP);
            }
        }
    }

    private boolean isValid(){
        boolean valid = true;
        if (!inputValidation.getResult(carName)) valid = false;
        if (!inputValidation.getResult(carModel)) valid = false;
        if (!inputValidation.getResult(carOwnerName)) valid = false;
        if (!inputValidation.getResult(cardriverName))valid = false;
        if (!inputValidation.getResult(cardriverMobile))valid = false;
        if (!inputValidation.getResult(cardriverRegNo))valid = false;
        if (!inputValidation.getResult(carOwnerContactNumber,"phone number")) valid = false;
        if (!inputValidation.getResult(carDetails)) valid = false;
        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_driver_car_info_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save){
            if(root != null && root.isSuccess){
              if (isValid()) updateIntoIntoDataBase();
            }
        }else if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    private void updateIntoIntoDataBase() {
        try {
            progress = new ProgressDialog(this);
            progress.setMessage("Updating, please wait...");
            progress.setCancelable(false);
            progress.show();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("carInfoId", Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.USER_CAR_ID)));
            jsonObject.put("carNumber", carName.getText().toString().trim());
            jsonObject.put("carModels", carModel.getText().toString().trim());
            jsonObject.put("owner", carOwnerName.getText().toString().trim());
            jsonObject.put("driverName",cardriverName.getText().toString().trim());
            jsonObject.put("driverMobile",cardriverMobile.getText().toString().trim());
            jsonObject.put("driverRegNo",cardriverRegNo.getText().toString().trim());
            jsonObject.put("ownerContact", carOwnerContactNumber.getText().toString().trim());
            jsonObject.put("isAc", carACStatus);
            jsonObject.put("image",base64CarImage!=null?base64CarImage:"");
            jsonObject.put("startDate", new Date(System.currentTimeMillis()));
            jsonObject.put("detailsSpecification", carDetails.getText().toString().trim());
            jsonObject.put("isActive", carStatusChange);
            jsonObject.put("carType", carTypeId);
            jsonObject.put("numberofSeat", carNumberOfSit.getText().toString().trim());

            JSONObject userInfoJson = new JSONObject();
            userInfoJson.put("userId", Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.USER_ID)));
            jsonObject.put("userInfoByUserId", userInfoJson);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, CommonURL.getInstance().carProfileUpdate, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if(baseResponse.isSuccess){
                        CommonTask.saveDataIntoPreference(DriverCarInfoUpdateActivity.this, CommonConstant.DRIVER_NUMBER_OF_SIT,
                                carNumberOfSit.getText().toString().trim());
                        CommonTask.showToast(DriverCarInfoUpdateActivity.this, "Update successfully completed.");
                        onBackPressed();
                    }else{
                        CommonTask.showToast(DriverCarInfoUpdateActivity.this, "Update failed.");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    CommonTask.analyzeVolleyError("Driver car info update", error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverCarInfoUpdateActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverCarInfoUpdateActivity.this, CommonConstant.USER_PASSWORD));
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

    private void setIntoIntoView() {
        try {
            if(root.carInfoByCarIdResponse.image == null || root.carInfoByCarIdResponse.image.isEmpty())
                Glide.with(this).load(CommonURL.getInstance().emptyCarPictureUrl).into(carImage);
            else
                Glide.with(this).load(CommonTask.getGlideUrl(root.carInfoByCarIdResponse.image, this)).into(carImage);
            carName.setText(root.carInfoByCarIdResponse.carModels);
            carModel.setText(root.carInfoByCarIdResponse.carNumber);
            carOwnerName.setText(root.carInfoByCarIdResponse.owner);
            cardriverName.setText(root.carInfoByCarIdResponse.driverName);
            cardriverMobile.setText(root.carInfoByCarIdResponse.driverMobile);
            cardriverRegNo.setText(root.carInfoByCarIdResponse.driverRegNo);
            carOwnerContactNumber.setText(root.carInfoByCarIdResponse.ownerContact);
            carDetails.setText(root.carInfoByCarIdResponse.detailsSpecification);
            carActiveStatus.setChecked(root.carInfoByCarIdResponse.active);
            carACAvailableStatus.setChecked(root.carInfoByCarIdResponse.ac);
            carNumberOfSit.setText(String.valueOf(root.carInfoByCarIdResponse.sitNumber));

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CommonURL.getInstance().getCarTypeList, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    final CarTypeListRoot carTypeListRoot = new Gson().fromJson(response.toString(), CarTypeListRoot.class);
                    if (carTypeListRoot.isSuccess) {
                        CarTypeAdapter carTypeAdapter = new CarTypeAdapter(carTypeListRoot.carTypeList, DriverCarInfoUpdateActivity.this);
                        carType.setAdapter(carTypeAdapter);
                        carType.setSelection(root.carInfoByCarIdResponse.carType);
                        carType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                carTypeId = carTypeListRoot.carTypeList.get(position).carTypeId;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                carTypeId = -1;
                            }
                        });
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CommonTask.analyzeVolleyError(DriverCarSetupActivity.class.getName(), error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = String.format(Locale.getDefault(), "%s:%s",
                            CommonTask.getDataFromPreference(DriverCarInfoUpdateActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverCarInfoUpdateActivity.this, CommonConstant.USER_PASSWORD));
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
