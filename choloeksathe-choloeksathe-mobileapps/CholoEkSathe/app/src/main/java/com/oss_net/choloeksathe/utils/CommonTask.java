package com.oss_net.choloeksathe.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.oss_net.choloeksathe.activities.driver.DriverCarShareFinalActivity;
import com.oss_net.choloeksathe.entity.databases.remote_model.GoogleMapDrawingRoot;
import com.oss_net.choloeksathe.entity.databases.remote_model.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by mahbubhasan on 11/29/17.
 */

public class CommonTask {

    public static void showLog(String message){
        Log.d(CommonConstant.TAG, message);
    }

    public static void showErrorLog(String message){
        Log.e(CommonConstant.TAG, message);
    }

    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static void showAlert(Context context, String title, String message){
        new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    public static void saveDataIntoPreference(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getDataFromPreference(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void analyzeVolleyError(String from, VolleyError volleyError) {
        try {
            showLog("AnalyzeVolleyError--> " + from);
            if (volleyError != null && volleyError.networkResponse != null) {
                showLog("AnalyzeVolleyError-->Response Code:" + volleyError.networkResponse.statusCode);
                byte[] data = volleyError.networkResponse.data;
                StringBuilder stringBuilder = new StringBuilder(data.length);
                for (int x = 0; x < data.length; x++) {
                    stringBuilder.append(((char) data[x]));
                }
                showLog("AnalyzeVolleyError-->Server error data:" + stringBuilder.toString());
            }
        } catch (Exception e) {
            showLog("AnalyzeVolleyError Catch -->" + e.getMessage());
        }
    }


    public static String getAddress(Context context, double latitude, double longitude){
        String locationAddress = " ";
        try{
            LatLng Location = new LatLng(latitude, longitude);
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(
                    Location.latitude, Location.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                locationAddress = String.format(Locale.getDefault(), "%s, %s, %s",address.getFeatureName(),
                        address.getLocality(), address.getCountryName());
            }
        }catch(Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
        return locationAddress;
    }

    public static boolean checkInternet(Context context){
        boolean isInternetOK = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager != null){
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()){
                    isInternetOK = true;
                }
            }
        }catch (Exception ex){
            showErrorLog(ex.getMessage());
        }
        return isInternetOK;
    }

    public static void  goSettingsActivity(final Context context){
        if(!checkInternet(context)){
            new AlertDialog.Builder(context)
                    .setMessage("Sorry! Internet is not enable.")
                    .setTitle("Internet Disable")
                    .setCancelable(false)
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(context,intent,null);
                        }
                    })
                    .create()
                    .show();
        }


    }



    public static boolean checkGPS(Context context){
        boolean isGPSOn = false;
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if(locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                isGPSOn = true;
            }
        }catch (Exception ex){
            showErrorLog(ex.getMessage());
        }
        return isGPSOn;
    }

    public static List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public static boolean isTimeOK(long selectedTime, long standardTime){
        if(standardTime <= selectedTime)
            return true;
        return false;
    }

    public static Point getPoint2LatLang(LatLng latLng, GoogleMap map){
        return map.getProjection().toScreenLocation(latLng);
    }

    public static void animateMarker(final Marker marker, final LatLng toPosition, GoogleMap googleMap) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = googleMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 50;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                //long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation(1.0f);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                /*if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    marker.setVisible(true);
                }*/
            }
        });
    }

    public static void updateCameraForMap(LatLng latLng, GoogleMap googleMap) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(16.0f)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),500,null);
    }

    public static String getAddressFromMapLocation(Context context, LatLng latLng) {
        StringBuilder stringBuilder = new StringBuilder();
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(addressList != null && addressList.size()>0){
                Address address = addressList.get(0);
                if(address.getSubThoroughfare() != null){
                    stringBuilder.append(address.getSubThoroughfare()).append(",");
                }
                if(address.getThoroughfare() != null){
                    stringBuilder.append(address.getThoroughfare()).append(",");
                }
                if(address.getLocality() != null){
                    stringBuilder.append(address.getLocality()).append(",");
                }
                if(address.getCountryName() != null){
                    stringBuilder.append(address.getCountryName());
                }
                return stringBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static GlideUrl getGlideUrl(String url, Context context){
        GlideUrl glideUrl = null;
        try {
            glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                    .addHeader("Content-Type","application/json")
                    .addHeader("Authorization",getBase64Auth(context)).build());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return glideUrl;
    }

    private static String getBase64Auth(Context context){
        String credentials = String.format(Locale.getDefault(), "%s:%s",
                CommonTask.getDataFromPreference(context, CommonConstant.USER_MOBILE_NUMBER),
                CommonTask.getDataFromPreference(context, CommonConstant.USER_PASSWORD));
        return String.format(Locale.getDefault(), "Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));
    }
}
