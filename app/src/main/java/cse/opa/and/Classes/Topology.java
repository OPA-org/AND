package cse.opa.and.Classes;

import java.util.ArrayList;

public class Topology {
    //TODO : Add Connections Property

    private ArrayList<String> IPs;
    private ArrayList<Agent> agents;
    private ArrayList<Connection> connections;

    public Topology(ArrayList<Agent> agents,ArrayList<Connection> connections) {
        this.agents = agents;
        this.connections = connections;
    }

    public Topology(ArrayList<String> IPs, ArrayList<Agent> agents, ArrayList<Connection> connections) {
        this.IPs = IPs;
        this.agents = agents;
        this.connections = connections;
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    //    public void setAgents(ArrayList<Agent> agents) {
//        this.agents = agents;
//    }

//    public Agent get_Agent_by_ID_in_list(int i){
//        return agents.get(i);
//    }
//    public Agent get_Agent_by_Agent_ID_property(int id){
//        for (int i=0;i<agents.size();i++){
//            if (agents.get(i).getId()==id)
//                return agents.get(i);
//        }
//        return null;
//    }

//    public Agent get_Agent_by_Agent_Serial_number_property(String serial_number){
//        for (int i=0;i<agents.size();i++){
//            if (agents.get(i).getSerial_number().equals(serial_number))
//                return agents.get(i);
//        }
//        return null;
//    }

//    public ArrayList<Agent> get_List_of_Agents_by_Visited_property(boolean visited){
//        ArrayList<Agent> temp_agents=new ArrayList<>();
//        for (int i=0;i<agents.size();i++){
//            if (agents.get(i).isVisited()==visited)
//                 temp_agents.add(agents.get(i));
//        }
//        return temp_agents;
//    }
}
