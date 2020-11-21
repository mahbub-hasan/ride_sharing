package com.oss_net.choloeksathe.utils;

/**
 * Created by mahbubhasan on 12/6/17.
 */

public class CommonURL {
    //String BASE_URL = "https://cholo-ek-sathe.herokuapp.com/api/";
    String BASE_URL = "http://107.172.198.125:8000/cholo-ek-sathe/api/";
    //String BASE_URL = "http://192.168.16.100:8000/api/";
    private static final CommonURL ourInstance = new CommonURL();

    public static CommonURL getInstance() {
        return ourInstance;
    }

    public CommonURL() {
    }

    public String backgroundSync = BASE_URL + "start-up/manage";
    public String userRegistration = BASE_URL+"user_info/registration";
    public String userLogin = BASE_URL + "user_info/login/%s/%s";
    public String carSetup = BASE_URL + "car_info/setup";
    public String shareACar = BASE_URL + "driver/car_share_request";
    public String carShareList = BASE_URL+"driver/car_share_info";
    public String carSharePassengerInfo = BASE_URL+"driver/car_share/show_interested/list";
    public String passengerCarList = BASE_URL+"passenger/nearest_car";
    public String passengerShowInterest = BASE_URL+"passenger/show_interest";
    public String driverRequestAcceptOrReject = BASE_URL+"driver/car_share/accept_reject/%d/%d/%d";
    public String driverRequestCancel = BASE_URL+"driver/car_share/request_cancel/%d";
    public String passengerActiveSession = BASE_URL + "passenger/current_session/%d";
    public String passengerCancelRequest = BASE_URL + "passenger/cancel_interest/%d";
    public String driverStatusChange = BASE_URL + "driver/car_share/status_change/%d/%d";
    public String driverIndividualPassengerStatusChange = BASE_URL + "driver/individual_passenger/status_change/%d/%d";
    public String googleMapRoute = "https://maps.googleapis.com/maps/api/directions/json?origin=%f,%f&destination=%f,%f&sensor=%s";
    public String profileInfo = BASE_URL + "user_info/user_profile/%d";
    public String profileUpdate = BASE_URL + "user_info/user_profile/update";
    public String carProfile = BASE_URL + "car_info/car/%d";
    public String carProfileUpdate = BASE_URL + "car_info/car/update";
    public String passengerRideHistory = BASE_URL+"passenger/history/%d";
    public String driverRideHistory = BASE_URL+"driver/history/%d";
    public String passengerHomePage = BASE_URL + "passenger/home/check-all/%d";
    public String passwordChanges = BASE_URL + "user_info/password_change/%s/%d";
    public String companyList = BASE_URL+"company/all-companies";
    public String driverNavigation = BASE_URL+"driver/navigation/%d";
    public String passengerActivityStatus = BASE_URL + "passenger/first-check/%d";
    public String driverActivityStatus = BASE_URL + "driver/first-check/%d";
    public String getVerificationCode = BASE_URL + "user_info/mobile_verification/%d/%s";
    public String verifyMobileNumber = BASE_URL + "user_info/mobile_verification_verify/%d/%s/%s";
    public String driverDashboard = BASE_URL + "driver/dashboard/%d";
    public String passengerRating = BASE_URL+"passenger/rating/add";
    public String driverRating = BASE_URL+"driver/rating/add";
    public String getPassengerRating = BASE_URL+"passenger/check/activity/%d" ;
    public String getCarTypeList = BASE_URL + "car_info/carTypeList";
    public String changeFCMKey = BASE_URL + "user_info/change_fcm_key"; //POST

    public String googleMapRouteWithStopOvers = "https://maps.googleapis.com/maps/api/directions/json?origin=%f,%f&destination=%f,%f&waypoints=via:%f,%f|via:%f,%f&key=%s";
    public String emptyPictureUrl = "https://heatherchristenaschmidt.files.wordpress.com/2011/09/facebook_no_profile_pic2-jpg.gif";
    public String emptyCarPictureUrl = "http://leasingdirectny.com/wp-content/plugins/cars-seller-auto-classifieds-script/images/no-image.png";

    public String userLogout = BASE_URL+"user_info/logout";
}
