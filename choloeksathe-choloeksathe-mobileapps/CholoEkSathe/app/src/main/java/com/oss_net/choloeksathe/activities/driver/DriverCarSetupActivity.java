package com.oss_net.choloeksathe.activities.driver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.adapter.CarTypeAdapter;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.base.FCMChangeService;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarSetupRoot;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarType;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarTypeListRoot;
import com.oss_net.choloeksathe.utils.CommonConstant;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;
import com.oss_net.choloeksathe.utils.InputValidation;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DriverCarSetupActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    ImageView carImage;
    EditText tilCarNumber, tilCarModel, tilOwner, tilOwnerContacts, tilCarDetails,
            tilldriverName, tilldriverContacts, driverRegNo, carNumberOfSit;
    AppCompatCheckBox cbCarAcAvailable, cbCarActive;
    private AppCompatSpinner carType;
    boolean isCarAC, isActive;
    String base64CarImage="";
    private ProgressDialog progressDiaglo;
    public int carTypeId;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_car_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        carImage = findViewById(R.id.driverCarPicture);

        tilCarNumber = findViewById(R.id.tilCarNumber);
        tilCarModel = findViewById(R.id.tilCarModel);
        tilOwner = findViewById(R.id.tilCarOwnerName);
        tilOwnerContacts = findViewById(R.id.tilCarOwnerContact);
        tilCarDetails = findViewById(R.id.tilCarDetails);
        tilldriverName = findViewById(R.id.tilCarDriverName);
        tilldriverContacts = findViewById(R.id.tilCarDriverMobile);
        driverRegNo = findViewById(R.id.tilCarDriverRegNo);
        carType = findViewById(R.id.carType);
        carNumberOfSit = findViewById(R.id.carNumberOfSit);

        cbCarAcAvailable = findViewById(R.id.acAvailable);
        cbCarActive = findViewById(R.id.carActive);

        isActive = isCarAC = true;
        cbCarAcAvailable.setChecked(true);
        cbCarActive.setChecked(true);

        setSpinnerDate();


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonTask.goSettingsActivity(DriverCarSetupActivity.this);
                takePhotoPermission();
            }
        });

        cbCarAcAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CommonTask.goSettingsActivity(DriverCarSetupActivity.this);
                if (b) {
                    cbCarAcAvailable.setChecked(true);
                    isCarAC = b;
                } else {
                    cbCarAcAvailable.setChecked(false);
                    isCarAC = b;
                }
            }
        });

        cbCarActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CommonTask.goSettingsActivity(DriverCarSetupActivity.this);
                if (b) {
                    cbCarActive.setChecked(true);
                    isActive = b;
                } else {
                    cbCarActive.setChecked(false);
                    isActive = b;
                }
            }
        });
    }

    private boolean isValid() {
        if(base64CarImage.isEmpty()){
            CommonTask.showToast(this, "Please select image");
            return false;
        }
        if(tilCarNumber.getText().toString().trim().isEmpty()){
            tilCarNumber.setError("Car number required.");
            return false;
        }
        if(tilCarModel.getText().toString().isEmpty()){
            tilCarModel.setError("Car model required.");
            return false;
        }
        if(tilOwner.getText().toString().isEmpty()){
            tilOwner.setError("Car owner name required.");
            return false;
        }
        if(tilOwnerContacts.getText().toString().isEmpty()){
            tilOwnerContacts.setError("Car owner contact number required.");
            return false;
        }
        if(tilldriverName.getText().toString().isEmpty()){
            tilldriverName.setError("Driver name required.");
            return false;
        }
        if(tilldriverContacts.getText().toString().isEmpty()){
            tilldriverContacts.setError("Driver contacts number required.");
            return false;
        }
        if(driverRegNo.getText().toString().isEmpty()){
            driverRegNo.setError("Driver registration number required.");
            return false;
        }
        if(carNumberOfSit.getText().toString().trim().isEmpty()){
            carNumberOfSit.setError("Number of sit is required.");
            return false;
        }
        return true;
    }

    private void setSpinnerDate() {
        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CommonURL.getInstance().getCarTypeList, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    final CarTypeListRoot root = new Gson().fromJson(response.toString(), CarTypeListRoot.class);
                    if (root.isSuccess) {
                        CarTypeAdapter carTypeAdapter = new CarTypeAdapter(root.carTypeList, DriverCarSetupActivity.this);
                        carType.setAdapter(carTypeAdapter);
                        carType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                carTypeId = root.carTypeList.get(position).carTypeId;
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
                            CommonTask.getDataFromPreference(DriverCarSetupActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                            CommonTask.getDataFromPreference(DriverCarSetupActivity.this, CommonConstant.USER_PASSWORD));
                    String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(CommonConstant.REQUEST_TIMES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(request);
        } catch (Exception ex) {
            CommonTask.showErrorLog(ex.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_driver_car_setup_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (isValid()) carSetup();
            else
                Toast.makeText(DriverCarSetupActivity.this, "Ac available and Active " +
                                "must be checked",
                        Toast.LENGTH_LONG).show();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void carSetup() {
        try {

            progressDiaglo = new ProgressDialog(this);
            progressDiaglo.setMessage("Please wait...");
            progressDiaglo.setCancelable(false);
            progressDiaglo.show();

            if (!base64CarImage.isEmpty()) {
                // check some validation

                // make a json for request
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("carNumber", tilCarNumber.getText().toString().trim());
                jsonObject.put("carModels", tilCarModel.getText().toString().trim());
                jsonObject.put("owner", tilOwner.getText().toString().trim());
                jsonObject.put("ownerContact", tilOwnerContacts.getText().toString().trim());
                jsonObject.put("driverName", tilldriverName.getText().toString().trim());
                jsonObject.put("driverMobile", tilldriverContacts.getText().toString().trim());
                jsonObject.put("driverRegNo", driverRegNo.getText().toString().trim());

                jsonObject.put("isAc", isCarAC);
                jsonObject.put("image", base64CarImage);
                jsonObject.put("detailsSpecification", tilCarDetails.getText().toString().trim());
                jsonObject.put("isActive", isActive);
                jsonObject.put("numberofSeat", Integer.parseInt(carNumberOfSit.getText().toString()));

                JSONObject userInfoJson = new JSONObject();
                userInfoJson.put("userId", Integer.parseInt(CommonTask.getDataFromPreference(this, CommonConstant.USER_ID)));
                jsonObject.put("userInfoByUserId", userInfoJson);
                jsonObject.put("carTypeId", carTypeId);


                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, CommonURL.getInstance().carSetup, jsonObject, this, this){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        String credentials = String.format(Locale.getDefault(), "%s:%s",
                                CommonTask.getDataFromPreference(DriverCarSetupActivity.this, CommonConstant.USER_MOBILE_NUMBER),
                                CommonTask.getDataFromPreference(DriverCarSetupActivity.this, CommonConstant.USER_PASSWORD));
                        String auth = String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
                        headers.put("Content-Type","application/json");
                        headers.put("Authorization", auth);
                        return headers;
                    }
                };
                request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                App.getInstance().addToRequestQueue(request);
            }
        } catch (Exception err) {
            String ex = (err.getMessage() == null) ? "SD Card failed" : err.getMessage();
            CommonTask.showLog(ex);
        }

    }

    private void takePhotoPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CommonConstant.RUNTIME_PERMISSION);
        } else {
            takePhoto();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CommonConstant.RUNTIME_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                CommonTask.showToast(this, "Please accept permission from settings");
            }
        }
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CommonConstant.IMAGE_FROM_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CommonConstant.IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap imageBitmap = (Bitmap) bundle.get("data");
            carImage.setImageBitmap(imageBitmap);

            // convert this image into base64 encode string
            if (imageBitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArrayImage = stream.toByteArray();

                base64CarImage = Base64.encodeToString(byteArrayImage, Base64.NO_WRAP);
            }
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDiaglo.dismiss();
        CarSetupRoot root = new Gson().fromJson(response.toString(), CarSetupRoot.class);
        if (root.isSuccess) {
            CommonTask.saveDataIntoPreference(this, CommonConstant.USER_CAR_ID, String.valueOf(root.carId));
            CommonTask.saveDataIntoPreference(this, CommonConstant.DRIVER_NUMBER_OF_SIT, carNumberOfSit.getText().toString().trim());
            //CommonTask.showToast(this, "You car information has been uploaded.");
            onBackPressed();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDiaglo.dismiss();
        CommonTask.analyzeVolleyError("Driver car setup activity", error);
    }
}
