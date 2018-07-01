package cse.opa.and.Classes;

import java.util.ArrayList;

import cse.opa.and.Node;

public abstract class Agent {

    protected static int id_no = 0;

    private boolean visited;

    public Agent(boolean visited) {
        this.visited = visited;
    }

    public abstract String getName();

    public abstract String getType();

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public abstract Boolean has_IPaddress(String IP);

    public abstract ArrayList<Interface> get_UsedInterfaces();

    public abstract String getIPAddress();

    public abstract Interface GetInterface_byMacAddress(String mac_address);
    
    public abstract Interface GetInterface_byindex(String index);
    
    public abstract ArrayList<String> get_mac_addresses();

    @Override
    public abstract String toString();

    public abstract Node getNode();

    public static void reset_id(){
        id_no = 0;
    }
}
