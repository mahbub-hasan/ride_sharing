package com.ossnetwork.choloeksatheserver.model.response;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

public class PassengerCarRequestResponse{
    private int requestId;
    private double startLatitude;
    private double startLongitude;
    private double stopLatitude;
    private double stopLongitude;
    private String startPlace;
    private String endPlace;
    private double price;
    private int noofFreeSeat;
    private boolean stopOver;
    private Timestamp startTime;
    private Timestamp returnTime;
    private boolean luggageSpaceAvailable;
    private boolean smokingAllowed;
    private boolean outSideFoodAllowed;
    private boolean musicAllowed;
    private boolean petsAllowed;
    private String genderRestriction;
    private Date requestDate;
    private String requestType;
    private String requestStatus;
    private String journeyType;
    private int driverId;
    private String driverPic;
    private String driverName;
    private String driverMobileNumber;
    private double driverRating;
    private int carId;
    private String carPic;
    private String carNumber;
    private Collection<DriverStopOver> stopOvers;
    private double distance;

    public PassengerCarRequestResponse() {
    }

    public PassengerCarRequestResponse(int requestId, double startLatitude, double startLongitude, double stopLatitude, double stopLongitude, String startPlace, String endPlace, double price, int noofFreeSeat, boolean stopOver, Timestamp startTime, Timestamp returnTime, boolean luggageSpaceAvailable, boolean smokingAllowed, boolean outSideFoodAllowed, boolean musicAllowed, boolean petsAllowed, String genderRestriction, Date requestDate, String requestType, String requestStatus, String journeyType, int driverId, String driverPic, String driverName, String driverMobileNumber, int carId, String carPic, String carNumber, Collection<DriverStopOver> stopOvers, double distance) {
        this.requestId = requestId;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.stopLatitude = stopLatitude;
        this.stopLongitude = stopLongitude;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.price = price;
        this.noofFreeSeat = noofFreeSeat;
        this.stopOver = stopOver;
        this.startTime = startTime;
        this.returnTime = returnTime;
        this.luggageSpaceAvailable = luggageSpaceAvailable;
        this.smokingAllowed = smokingAllowed;
        this.outSideFoodAllowed = outSideFoodAllowed;
        this.musicAllowed = musicAllowed;
        this.petsAllowed = petsAllowed;
        this.genderRestriction = genderRestriction;
        this.requestDate = requestDate;
        this.requestType = requestType;
        this.requestStatus = requestStatus;
        this.journeyType = journeyType;
        this.driverId = driverId;
        this.driverPic = driverPic;
        this.driverName = driverName;
        this.driverMobileNumber = driverMobileNumber;
        this.carId = carId;
        this.carPic = carPic;
        this.carNumber = carNumber;
        this.stopOvers = stopOvers;
        this.distance = distance;
    }

    public PassengerCarRequestResponse(int requestId, double startLatitude, double startLongitude, double stopLatitude, double stopLongitude, String startPlace, String endPlace, double price, int noofFreeSeat, boolean stopOver, Timestamp startTime, Timestamp returnTime, boolean luggageSpaceAvailable, boolean smokingAllowed, boolean outSideFoodAllowed, boolean musicAllowed, boolean petsAllowed, String genderRestriction, Date requestDate, String requestType, String requestStatus, String journeyType, int driverId, String driverPic, String driverName, String driverMobileNumber, double driverRating, int carId, String carPic, String carNumber, Collection<DriverStopOver> stopOvers, double distance) {
        this.requestId = requestId;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.stopLatitude = stopLatitude;
        this.stopLongitude = stopLongitude;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.price = price;
        this.noofFreeSeat = noofFreeSeat;
        this.stopOver = stopOver;
        this.startTime = startTime;
        this.returnTime = returnTime;
        this.luggageSpaceAvailable = luggageSpaceAvailable;
        this.smokingAllowed = smokingAllowed;
        this.outSideFoodAllowed = outSideFoodAllowed;
        this.musicAllowed = musicAllowed;
        this.petsAllowed = petsAllowed;
        this.genderRestriction = genderRestriction;
        this.requestDate = requestDate;
        this.requestType = requestType;
        this.requestStatus = requestStatus;
        this.journeyType = journeyType;
        this.driverId = driverId;
        this.driverPic = driverPic;
        this.driverName = driverName;
        this.driverMobileNumber = driverMobileNumber;
        this.driverRating = driverRating;
        this.carId = carId;
        this.carPic = carPic;
        this.carNumber = carNumber;
        this.stopOvers = stopOvers;
        this.distance = distance;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public double getStopLatitude() {
        return stopLatitude;
    }

    public void setStopLatitude(double stopLatitude) {
        this.stopLatitude = stopLatitude;
    }

    public double getStopLongitude() {
        return stopLongitude;
    }

    public void setStopLongitude(double stopLongitude) {
        this.stopLongitude = stopLongitude;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNoofFreeSeat() {
        return noofFreeSeat;
    }

    public void setNoofFreeSeat(int noofFreeSeat) {
        this.noofFreeSeat = noofFreeSeat;
    }

    public boolean isStopOver() {
        return stopOver;
    }

    public void setStopOver(boolean stopOver) {
        this.stopOver = stopOver;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Timestamp returnTime) {
        this.returnTime = returnTime;
    }

    public boolean isLuggageSpaceAvailable() {
        return luggageSpaceAvailable;
    }

    public void setLuggageSpaceAvailable(boolean luggageSpaceAvailable) {
        this.luggageSpaceAvailable = luggageSpaceAvailable;
    }

    public boolean isSmokingAllowed() {
        return smokingAllowed;
    }

    public void setSmokingAllowed(boolean smokingAllowed) {
        this.smokingAllowed = smokingAllowed;
    }

    public boolean isOutSideFoodAllowed() {
        return outSideFoodAllowed;
    }

    public void setOutSideFoodAllowed(boolean outSideFoodAllowed) {
        this.outSideFoodAllowed = outSideFoodAllowed;
    }

    public boolean isMusicAllowed() {
        return musicAllowed;
    }

    public void setMusicAllowed(boolean musicAllowed) {
        this.musicAllowed = musicAllowed;
    }

    public boolean isPetsAllowed() {
        return petsAllowed;
    }

    public void setPetsAllowed(boolean petsAllowed) {
        this.petsAllowed = petsAllowed;
    }

    public String getGenderRestriction() {
        return genderRestriction;
    }

    public void setGenderRestriction(String genderRestriction) {
        this.genderRestriction = genderRestriction;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getJourneyType() {
        return journeyType;
    }

    public void setJourneyType(String journeyType) {
        this.journeyType = journeyType;
    }

    public Collection<DriverStopOver> getStopOvers() {
        return stopOvers;
    }

    public void setStopOvers(Collection<DriverStopOver> stopOvers) {
        this.stopOvers = stopOvers;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getDriverPic() {
        return driverPic;
    }

    public void setDriverPic(String driverPic) {
        this.driverPic = driverPic;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCarPic() {
        return carPic;
    }

    public void setCarPic(String carPic) {
        this.carPic = carPic;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverMobileNumber() {
        return driverMobileNumber;
    }

    public void setDriverMobileNumber(String driverMobileNumber) {
        this.driverMobileNumber = driverMobileNumber;
    }

    public double getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(double driverRating) {
        this.driverRating = driverRating;
    }
}
