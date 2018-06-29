package cse.opa.and.Classes;

import java.util.ArrayList;
import org.snmp4j.smi.IpAddress;

import cse.opa.and.Node;

public class Host extends Agent {

    private Interface anInterface;

    private int id;

    public Host(Interface anInterface) {
        super(false);
        this.anInterface = anInterface;
        this.id = super.id_no++;
    }

    public Interface getAnInterface() {
        return anInterface;
    }

    public void setAnInterface(Interface anInterface) {
        this.anInterface = anInterface;
    }

    public void set_Mac_Address(String Mac_Address){
        this.anInterface.setMac_address(Mac_Address);
    }
    
    @Override
    public ArrayList<String> get_mac_addresses() {
        ArrayList<String> mac_addresses = new ArrayList<>();
        mac_addresses.add(this.anInterface.getMac_address());
        return mac_addresses;
    }
    
    @Override
    public Boolean has_IPaddress(String IP) {
        return this.anInterface.getIp_address().equals(IP);
    }

    @Override
    public String toString() {
        return id+"- Host of IP: " + anInterface.getIp_address();
    }

    @Override
    public ArrayList<Interface> get_UsedInterfaces() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getIPAddress() {
        return this.anInterface.getIp_address();
    }

    @Override
    public Interface GetInterface_byMacAddress(String mac_address) {
        return this.anInterface;
    }
    
    @Override
    public Interface GetInterface_index(String index) {
        return this.anInterface;
    }

    @Override
    public Node getNode() {
        return new Node(id,this.toString());
    }

}
