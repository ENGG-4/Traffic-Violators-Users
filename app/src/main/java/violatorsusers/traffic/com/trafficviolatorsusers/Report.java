package violatorsusers.traffic.com.trafficviolatorsusers;

import java.util.Date;

public class Report {

    private String userID;
    private String vehicleNo;
    private String licenseNo;
    private String reason;
    private String description;
    private int fine;
    private Date datetime;
    private String photoURL;

    public Report() {
    }

    public Report(String vehicleNo, String licenseNo, String reason, String description, int fine, Date datetime, String photoURL, String userID) {
        this.vehicleNo = vehicleNo;
        this.licenseNo = licenseNo;
        this.reason = reason;
        this.description = description;
        this.fine = fine;
        this.datetime = datetime;
        this.photoURL = photoURL;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getPhoto() {
        return photoURL;
    }

    public void setPhoto(String photoURL) {
        this.photoURL = photoURL;
    }
}

