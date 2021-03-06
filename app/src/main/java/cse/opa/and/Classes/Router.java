package cse.opa.and.Classes;

import java.util.ArrayList;
import cse.opa.and.Node;

public class Router extends Agent {

    private int id;
    private ArrayList<Interface> interfaces;
    private String sysDescr;
    private String sysName;
    private Node node;

    public Router(String sysDescr, String sysName, ArrayList<Interface> interfaces) {
        super(false);
        this.sysDescr = sysDescr;
        this.sysName = sysName;
        this.interfaces = interfaces;
        this.id = super.id_no++;
        this.node = new Node(id,sysName, this.getClass().getSimpleName());
    }

    public Router(String sysDescr, String sysName) {
        super(false);
        this.sysDescr = sysDescr;
        this.sysName = sysName;
        this.interfaces = new ArrayList<>();
        this.id_no = super.id_no++;
        this.node = new Node(id,sysName, this.getClass().getSimpleName());
    }

    public Router() {
        super(false);
        this.sysDescr = "";
        this.sysName = "";
        this.interfaces = new ArrayList<>();
        this.id_no = super.id_no++;
        this.node = new Node(id,sysName, this.getClass().getSimpleName());
    }

    public ArrayList<Interface> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(ArrayList<Interface> interfaces) {
        this.interfaces = interfaces;
    }

    public void add_Interface(Interface anInterface) {
        interfaces.add(anInterface);
    }

    public Interface get_Interface_by_ID_in_list(int i) {
        return interfaces.get(i);
    }

    /*public Interface get_Interface_by_Interface_number_property(int number){
        for (int i=0;i<interfaces.size();i++){
            if (interfaces.get(i).getNumber()==number)
                return interfaces.get(i);
        }
        return null;
    }*/

    @Override
    public ArrayList<String> get_mac_addresses() {
        ArrayList<String> mac_addresses = new ArrayList<>();
        for (Interface intf : interfaces) {
            mac_addresses.add(intf.getMac_address());
        }
        return mac_addresses;
    }

    public Interface get_Interface_by_Interface_IP_address_property(String ip_address) {
        for (int i = 0; i < interfaces.size(); i++) {
            if (interfaces.get(i).getIp_address().equals(ip_address)) {
                return interfaces.get(i);
            }
        }
        return null;
    }

    public Interface get_Interface_by_Interface_MAC_address_property(String mac_address) {
        for (int i = 0; i < interfaces.size(); i++) {
            if (interfaces.get(i).getMac_address() == mac_address) {
                return interfaces.get(i);
            }
        }
        return null;
    }

    public Interface get_Interface_by_Interface_Connected_to_property(String connected_to) {
        for (int i = 0; i < interfaces.size(); i++) {
            if (interfaces.get(i).getConnected_to() == connected_to) {
                return interfaces.get(i);
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return sysName;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
    @Override
    public Boolean has_IPaddress(String IP) {
        for (Interface inter : this.interfaces) {
            if (inter.getIp_address().equals(IP)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String s = "";
        s += sysName + "\n\n\tDescription: \n\t\t" + sysDescr + "\n";
        for(Interface intf : this.interfaces){
            s += "\n"+intf.toString();
        }
        return s;
    }

    @Override
    public Node getNode() {
        return node;
    }

    public ArrayList<Interface> get_UsedInterfaces() {
        ArrayList<Interface> used = new ArrayList<>();
        for (Interface inf : this.interfaces) {
            if (!inf.getIp_address().isEmpty()) {
                used.add(inf);
            }
        }
        return used;
    }

    @Override
    public String getIPAddress() {
        for (Interface aInterface : interfaces) {
            if (!aInterface.getIp_address().isEmpty() && aInterface.getIp_address() != null) {
                return aInterface.getIp_address();
            }
        }
        return "";
    }

    @Override
    public Interface GetInterface_byMacAddress(String mac_address) {
        for (Interface aInterface : interfaces) {
            if (aInterface.getMac_address().equals(mac_address)) {
                return aInterface;
            }
        }
        return null;
    }

    @Override
    public Interface GetInterface_byindex(String index) {
        for (Interface aInterface : interfaces) {
            if (aInterface.getIndex().equals(index)) {
                return aInterface;
            }
        }
        return null;
    }
}
