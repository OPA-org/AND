package cse.opa.and.Classes;

import java.util.ArrayList;

import cse.opa.and.Node;

public class Host extends Agent {

    private Interface anInterface;

    private static int host_id = 1;

    private int id;
    private int this_host_id;
	
	private String name = "";
    private String descr = "";

    private Node node;

    public Host(Interface anInterface) {
        super(false);
        this.anInterface = anInterface;
        this.id = super.id_no++;
        this.this_host_id = host_id++;
        this.node = new Node(id,"Host "+this_host_id+" : "+ anInterface.getIp_address(),this.getClass().getSimpleName());
    }
	
	public Host(Interface anInterface,String name,String descr) {
        super(false);
        this.anInterface = anInterface;
        this.name = name;
        this.descr = descr;
		this.id = super.id_no++;
		this.node = new Node(id,name,this.getClass().getSimpleName());
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
    public String getName() {
        if(name.equals("")){
            return "Host " + this_host_id;
        }else{
            return name;
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Boolean has_IPaddress(String IP) {
        return this.anInterface.getIp_address().equals(IP);
    }

    @Override
    public String toString() {
        String ss = "";
        if(name.equals("")){
            ss += getName() +"\n";
        }else{
            ss += getName() + "\n" + "Description: \n" + descr + "\n";
        }
        ss += anInterface.toString();
        return ss;
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
    public Interface GetInterface_byindex(String index) {
        return this.anInterface;
    }

    @Override
    public Node getNode() {
        return node;
    }

    public static void reset_HostID(){
        host_id = 1;
    }

}
