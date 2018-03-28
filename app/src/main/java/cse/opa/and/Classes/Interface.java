package cse.opa.and.Classes;

/**
 * Created by Peter on 28/3/2018.
 */

public class Interface  {
    private int number;
    private String ip_address;
    private String subnet_mask;
    private String mac_address;
    private String connected_to;

    public Interface(int number, String ip_address, String subnet_mask, String mac_address, String connected_to) {
        this.number = number;
        this.ip_address = ip_address;
        this.subnet_mask = subnet_mask;
        this.mac_address = mac_address;
        this.connected_to = connected_to;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getSubnet_mask() {
        return subnet_mask;
    }

    public void setSubnet_mask(String subnet_mask) {
        this.subnet_mask = subnet_mask;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public String getConnected_to() {
        return connected_to;
    }

    public void setConnected_to(String connected_to) {
        this.connected_to = connected_to;
    }
}
