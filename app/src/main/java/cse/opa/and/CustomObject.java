package cse.opa.and;

public class CustomObject {

    private String Topologyname;
    private String Username;
    private String Date;

    public CustomObject(String Topologyname, String Username,String Date) {
        this.Topologyname = Topologyname;
        this.Username = Username;
        this.Date = Date;
    }


    public String getTopologyname() {
        return Topologyname;
    }

    public void setTopologyname(String topologyname) {
        Topologyname = topologyname;
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
}
