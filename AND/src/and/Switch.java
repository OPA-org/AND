package and;

import java.util.ArrayList;

public class Switch extends Agent {
    private ArrayList<Interface> interfaces;

    public Switch(int id, String serial_number,ArrayList<Interface> interfaces) {
        super(id,serial_number, false);
        this.interfaces = interfaces;
    }
    public Switch(int id, String serial_number) {
        super(id,serial_number, false);
        this.interfaces = new ArrayList<>();
    }

    public ArrayList<Interface> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(ArrayList<Interface> interfaces) {
        this.interfaces = interfaces;
    }
    public void add_Interface(Interface anInterface){
        interfaces.add(anInterface);
    }
    public Interface get_Interface_by_ID_in_list(int i){
        return interfaces.get(i);
    }
    public Interface get_Interface_by_Interface_number_property(int number){
        for (int i=0;i<interfaces.size();i++){
            if (interfaces.get(i).getNumber()==number)
                return interfaces.get(i);
        }
        return null;
    }
    public Interface get_Interface_by_Interface_IP_address_property(String ip_address){
        for (int i=0;i<interfaces.size();i++){
            if (interfaces.get(i).getIp_address()==ip_address)
                return interfaces.get(i);
        }
        return null;
    }
    public Interface get_Interface_by_Interface_MAC_address_property(String mac_address){
        for (int i=0;i<interfaces.size();i++){
            if (interfaces.get(i).getMac_address()==mac_address)
                return interfaces.get(i);
        }
        return null;
    }
    public Interface get_Interface_by_Interface_Connected_to_property(String connected_to){
        for (int i=0;i<interfaces.size();i++){
            if (interfaces.get(i).getConnected_to()==connected_to)
                return interfaces.get(i);
        }
        return null;
    }
}
