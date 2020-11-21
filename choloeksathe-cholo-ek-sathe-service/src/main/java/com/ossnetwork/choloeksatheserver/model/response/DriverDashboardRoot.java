package com.ossnetwork.choloeksatheserver.model.response;

public class DriverDashboardRoot extends BaseResponse{

    private DriverDashboard driverDashboard;

    public DriverDashboardRoot(int statusCode, String message, boolean is_success, DriverDashboard driverDashboard) {
        super(statusCode, message, is_success);
        this.driverDashboard = driverDashboard;
    }

    public DriverDashboardRoot(int statusCode, String message, boolean is_success) {
        super(statusCode, message, is_success);
    }

    public DriverDashboard getDriverDashboard() {
        return driverDashboard;
    }

    public void setDriverDashboard(DriverDashboard driverDashboard) {
        this.driverDashboard = driverDashboard;
    }
}
