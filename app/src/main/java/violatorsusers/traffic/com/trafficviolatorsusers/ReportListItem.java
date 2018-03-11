package violatorsusers.traffic.com.trafficviolatorsusers;

public class ReportListItem {

    private String ReportID;
    private String VehicleNo;
    private String Reason;
    private String Fine;
    private String ReportDate;
    private String ReportTime;
    private int Photo;

    public ReportListItem(String reportID, String vehicleNo, String reason, String fine,boolean finePaid, String reportDate, String reportTime, int photo) {
        ReportID = reportID;
        VehicleNo = vehicleNo;
        Reason = reason;
        Fine = fine;
        ReportDate = reportDate;
        ReportTime = reportTime;
        Photo = photo;
    }

    public String getReportID() {
        return ReportID;
    }

    public void setReportID(String reportID) {
        ReportID = reportID;
    }

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        VehicleNo = vehicleNo;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getFine() {
        return Fine;
    }

    public void setFine(String fine) {
        Fine = fine;
    }

    public String getReportDate() {
        return ReportDate;
    }

    public void setReportDate(String reportDate) {
        ReportDate = reportDate;
    }

    public String getReportTime() {
        return ReportTime;
    }

    public void setReportTime(String reportTime) {
        ReportTime = reportTime;
    }

    public int getPhoto() {
        return Photo;
    }

    public void setPhoto(int photo) {
        Photo = photo;
    }
}

