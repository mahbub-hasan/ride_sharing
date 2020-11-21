package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;

@Entity
public class StopOverInfo {
    private int stopOverId;
    private double startLatitude;
    private double startLongitude;
    private double stopLatitude;
    private double stopLongitude;
    private String startPointName;
    private String stopPointName;
    private RequestInfo requestInfoByRequestId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StopOverID", nullable = false)
    public int getStopOverId() {
        return stopOverId;
    }

    public void setStopOverId(int stopOverId) {
        this.stopOverId = stopOverId;
    }

    @Basic
    @Column(name = "StartLatitude", nullable = false, precision = 0)
    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    @Basic
    @Column(name = "StartLongitude", nullable = false, precision = 0)
    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    @Basic
    @Column(name = "StopLatitude", nullable = false, precision = 0)
    public double getStopLatitude() {
        return stopLatitude;
    }

    public void setStopLatitude(double stopLatitude) {
        this.stopLatitude = stopLatitude;
    }

    @Basic
    @Column(name = "StopLongitude", nullable = false, precision = 0)
    public double getStopLongitude() {
        return stopLongitude;
    }

    public void setStopLongitude(double stopLongitude) {
        this.stopLongitude = stopLongitude;
    }

    @Basic
    @Column(name = "StartPointName", nullable = true, length = 500)
    public String getStartPointName() {
        return startPointName;
    }

    public void setStartPointName(String startPointName) {
        this.startPointName = startPointName;
    }

    @Basic
    @Column(name = "StopPointName", nullable = true, length = 500)
    public String getStopPointName() {
        return stopPointName;
    }

    public void setStopPointName(String stopPointName) {
        this.stopPointName = stopPointName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StopOverInfo that = (StopOverInfo) o;

        if (stopOverId != that.stopOverId) return false;
        if (Double.compare(that.startLatitude, startLatitude) != 0) return false;
        if (Double.compare(that.startLongitude, startLongitude) != 0) return false;
        if (Double.compare(that.stopLatitude, stopLatitude) != 0) return false;
        if (Double.compare(that.stopLongitude, stopLongitude) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = stopOverId;
        temp = Double.doubleToLongBits(startLatitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(startLongitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(stopLatitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(stopLongitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "RequestID", referencedColumnName = "RequestID", nullable = false)
    public RequestInfo getRequestInfoByRequestId() {
        return requestInfoByRequestId;
    }

    public void setRequestInfoByRequestId(RequestInfo requestInfoByRequestId) {
        this.requestInfoByRequestId = requestInfoByRequestId;
    }
}
