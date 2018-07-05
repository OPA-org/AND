package cse.opa.and;

public class CustomObject {
    private String ReportFullname;
    private String Reportname;
    private String Username;
    private String Date;

    public CustomObject(String reportFullname, String Topologyname, String Username, String Date) {
        ReportFullname = reportFullname;
        this.Reportname = Topologyname;
        this.Username = Username;
        this.Date = Date;
    }


    public String getReportname() {
        return Reportname;
    }

    public void setReportname(String topologyname) {
        Reportname = topologyname;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getReportFullname() {
        return ReportFullname;
    }

    public void setReportFullname(String reportFullname) {
        ReportFullname = reportFullname;
    }
}
