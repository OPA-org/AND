package cse.opa.and.Classes;

import android.app.ProgressDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SNMPManager {
    //TODO Implement all Methods
    public static Topology generate_topology() throws Exception {
        ArrayList<String> R = new ArrayList<>();
        ArrayList<String> IPs = new ArrayList<>();
        device_discovery("192.168.10.2", R, IPs);
        System.out.println("IPs:-");
        for (String s : IPs) {
            System.out.println(s);
        }
        ArrayList<Agent> agents = new ArrayList<>();
        create_nodes(IPs, agents);
        System.out.println("Agents:-");
        for (Agent a : agents) {
            System.out.println(a.getName());
        }
        ArrayList<Connection> Connections = create_connections(agents);
        Connections = Filter_Connections(Connections);
        System.out.println("Connections:-");
        for(Connection c: Connections){
            System.out.println(c.toString()+
                    "\n===========================");
        }
        Connections = Find_Hidden_Connections(Connections,agents);
        System.out.println("Agents:-");
        for (Agent a : agents) {
            System.out.println(a.getName());
        }
        System.out.println("Connections:-");
        for(Connection c: Connections){
            System.out.println(c.toString()+
                    "\n===========================");
        }

        return new Topology(IPs,agents,Connections);
    }

//    public void request_table(){}
//
//    public void get_updates(){}

    private static void create_nodes(ArrayList<String> IPs, ArrayList<Agent> agents) throws Exception {
        for (String IP : IPs) {
            Boolean visited = false;
            for (Agent agent : agents) {
                if (agent.has_IPaddress(IP)) {
                    visited = true;
                }
            }
            if (visited) {
                continue;
            }
            String sysservresult = SNMP_methods.getfromsnmpget_single(IP, OIDS.sysServices);
            if (!sysservresult.equals("error")) {
                String binsysserv = new StringBuilder(MiscellaneousMethods.converttobinary(Integer.valueOf(sysservresult)).substring(1,8)).reverse().toString();
                if(binsysserv.charAt(0) == '1'){
                    //L1 Yes
                    Interface inter = new Interface("1", "PC port", IP, "");
                    String name = SNMP_methods.getfromsnmpget_single(IP, OIDS.sysName);
                    String descr = SNMP_methods.getfromsnmpget_single(IP, OIDS.sysDescr);
                    Agent host = new Host(inter,name,descr);
                    agents.add(host);
                }else if(binsysserv.charAt(1) == '1'){
                    //L2 yes
                    if(binsysserv.charAt(2) == '1'){
                        //L3 yes
                        //hnshof el bridge mib
                        String basebridgeaddr = SNMP_methods.getfromsnmpget_single(IP, OIDS.dot1dBaseBridgeAddress);
                        if(!basebridgeaddr.equals("error")){
                            //bridge Mib yes
                            //multiple interface with same mac addr
                            if(basebridgeaddr.equals("00:00:00:00:00:00")){
                                Agent router = create_Router(IP);
                                if (!agents.contains(router)) {
                                    agents.add(router);
                                }
                            } else {
                                Agent switchh = create_Switch(IP);
                                if (!agents.contains(switchh)) {
                                    agents.add(switchh);
                                }
                            }
                        }else{
                            //bride mib no
                            if(binsysserv.charAt(6) == '1'){
                                //L7 yes
                                if(SNMP_methods.getfromsnmpget_single(IP, OIDS.dot1dStpProtocolSpecification).equals("error")){
                                    //router
                                    Agent router = create_Router(IP);
                                    if (!agents.contains(router)) {
                                        agents.add(router);
                                    }
                                }else{
                                    //switch
                                    Agent switchh = create_Switch(IP);
                                    if (!agents.contains(switchh)) {
                                        agents.add(switchh);
                                    }
                                }
                            }else{
                                //L7 no
                                Agent router = create_Router(IP);
                                if (!agents.contains(router)) {
                                    agents.add(router);
                                }
                            }
                        }
                    }else{
                        //L3 no
                        //hnshof el bridgemib
                        if(!SNMP_methods.getfromsnmpget_single(IP, OIDS.dot1dBaseBridgeAddress).equals("error")){
                            //bridge Mib yes
                            //switch
                            Agent switchh = create_Switch(IP);
                            if (!agents.contains(switchh)) {
                                agents.add(switchh);
                            }
                        }else{
                            //bride mib no
                            Interface inter = new Interface("1", "PC port", IP, "");
                            Agent host = new Host(inter);
                            agents.add(host);
                        }
                    }
                }else{
                    //L2 No
                    if(binsysserv.charAt(2) == '1'){
                        //L3 yes
                        if(binsysserv.charAt(3) == '1'){
                            //L4 yes
                            //switch
                            Agent switchh = create_Switch(IP);
                            if (!agents.contains(switchh)) {
                                agents.add(switchh);
                            }
                        }else{
                            //L4 no
                            if(binsysserv.charAt(6) == '1'){
                                //L7 yes
                                if(SNMP_methods.getfromsnmpget_single(IP, OIDS.dot1dStpProtocolSpecification).equals("error")){
                                    //router
                                    Agent router = create_Router(IP);
                                    if (!agents.contains(router)) {
                                        agents.add(router);
                                    }
                                }else{
                                    //switch
                                    Agent switchh = create_Switch(IP);
                                    if (!agents.contains(switchh)) {
                                        agents.add(switchh);
                                    }
                                }
                            }else{
                                //L7 no
                                Agent router = create_Router(IP);
                                if (!agents.contains(router)) {
                                    agents.add(router);
                                }
                            }
                        }
                    }else{
                        //L3 no
                    }
                }

            } else {
                Interface inter = new Interface("1", "PC port", IP, "");
                Agent host = new Host(inter);
                agents.add(host);
            }
        }
    }

    private static void device_discovery(String gateway_IP, ArrayList<String> R, ArrayList<String> IPs) throws IOException, Exception {
        R.add(gateway_IP);
        int n = 0;
        while (true) {
            if (!SNMP_methods.getfromsnmpget_single(R.get(n), OIDS.sysDescr).equals("error")) {
                ArrayList<String> oids = new ArrayList<>();
                oids.add(OIDS.ipRouteType);
                oids.add(OIDS.ipRouteNextHop);
                ArrayList<ArrayList<String>> Next_hops = SNMP_methods.getfromwalk_multi(R.get(n), OIDS.ipRouteTable, oids);
                for (int i = 0; i < Next_hops.get(0).size(); i++) {
                    if (Next_hops.get(0).get(i).equals("4")) {
                        if (MiscellaneousMethods.isHostIP(Next_hops.get(1).get(i))) {
                            if (!R.contains(Next_hops.get(1).get(i))) {
                                R.add(Next_hops.get(1).get(i));
                            }
                            if (!IPs.contains(Next_hops.get(1).get(i))) {
                                IPs.add(Next_hops.get(1).get(i));
                            }
                        }
                    }
                }
            } else {
                //R[n].type= host
                if (MiscellaneousMethods.isHostIP(R.get(n))) {
                    if (!IPs.contains(R.get(n))) {
                        IPs.add(R.get(n));
                    }
                }
            }
            n++;
            if (n >= R.size()) {
                break;
            }
        }
        n = 0;
        while (true) {
            if (IPs.isEmpty()) {
                break;
            }
            if (!SNMP_methods.getfromsnmpget_single(IPs.get(n), OIDS.sysDescr).equals("error")) {
                ArrayList<String> NetToMediaAddresses = SNMP_methods.getfromwalk_single(IPs.get(n), OIDS.ipNetToMediaTable, OIDS.ipNetToMediaNetAddress);
                for (String ND : NetToMediaAddresses) {
                    if (MiscellaneousMethods.isHostIP(ND)) {
                        if (!IPs.contains(ND)) {
                            IPs.add(ND);
                        }
                    }
                }
            }
            n++;
            if (n >= IPs.size()) {
                break;
            }
        }
    }

    private static Agent get_Agent_by_Ip(ArrayList<Agent>agents,String ip){
        for (Agent B : agents) {
            if(B.has_IPaddress(ip))
                return B;
        }
        return null;
    }

    private static Boolean has_similar_connection(ArrayList<Connection> connections,Agent A,Agent B, Interface inf_A, Interface inf_B){
        for(Connection c : connections){
            if(c.getAgentA().equals(A) && c.getAgentB().equals(B)){
                if(c.getInterfaceA().equals(inf_A) && c.getInterfaceB().equals(inf_B)){
                    return true;
                }
            }else if(c.getAgentA().equals(B) && c.getAgentB().equals(A)){
                if(c.getInterfaceA().equals(inf_B) && c.getInterfaceB().equals(inf_A)){
                    return true;
                }
            }
        }
        return false;
    }

    private static ArrayList<Connection> create_connections(ArrayList<Agent> agents) throws IOException{
        ArrayList<Switch> switches = get_switches(agents);
        ArrayList<Router> routers = get_routers(agents);
        ArrayList<Host> hosts = get_hosts(agents);

        ArrayList<AgentPair> switchpairs = get_pairs((ArrayList<Agent>)((Object)switches));
        ArrayList<AgentPair> routerpairs = get_pairs((ArrayList<Agent>)((Object)routers));
        ArrayList<AgentPair> switchrouterpairs = get_pairs((ArrayList<Agent>)((Object)switches),(ArrayList<Agent>)((Object)routers));
        ArrayList<AgentPair> routerhostpairs = get_pairs((ArrayList<Agent>)((Object)routers),(ArrayList<Agent>)((Object)hosts));
        ArrayList<AgentPair> switchhostpairs = get_pairs((ArrayList<Agent>)((Object)switches),(ArrayList<Agent>)((Object)hosts));

        ArrayList<Connection> Connections = new ArrayList<>();

        System.out.println("Switch to switch");
        ArrayList<Connection> switch_to_switch = Switch_to_switch_connectivity(switchpairs);
        Connections.addAll(switch_to_switch);
        System.out.println("Switch to router");
        ArrayList<Connection> switch_to_router = Switch_to_router_connectivity(switchrouterpairs,Connections);
        System.out.println("Switch to host");
        ArrayList<Connection> switch_to_host = Switch_to_host_connectivity(switchhostpairs,Connections);
        System.out.println("Router to router");
        ArrayList<Connection> router_to_router = Router_to_router_connectivity((ArrayList<Agent>)((Object)routers),Connections);
        System.out.println("Router to host");
        ArrayList<Connection> router_to_host = Router_to_host_connectivity(routerhostpairs,Connections);

        return Connections;

    }

    private static ArrayList<Switch> get_switches(ArrayList<Agent> agents){
        ArrayList<Switch> switches = new ArrayList<>();
        for(Agent agent:agents){
            if(agent.getClass().getSimpleName().equals("Switch")){
                switches.add((Switch)agent);
            }
        }
        return switches;
    }

    private static ArrayList<Router> get_routers(ArrayList<Agent> agents){
        ArrayList<Router> routers = new ArrayList<>();
        for(Agent agent:agents){
            if(agent.getClass().getSimpleName().equals("Router")){
                routers.add((Router)agent);
            }
        }
        return routers;
    }

    private static ArrayList<Host> get_hosts(ArrayList<Agent> agents){
        ArrayList<Host> hosts = new ArrayList<>();
        for(Agent agent:agents){
            if(agent.getClass().getSimpleName().equals("Host")){
                hosts.add((Host)agent);
            }
        }
        return hosts;
    }

    private static ArrayList<AgentPair> get_pairs(ArrayList<Agent> agents){
        ArrayList<AgentPair> agentspairs = new ArrayList<>();
        for(int i = 0 ; i < agents.size(); i++){
            for(int j = i+1 ; j < agents.size() ; j++){
                AgentPair pair = new AgentPair(agents.get(i),agents.get(j));
                agentspairs.add(pair);
            }
        }
        return agentspairs;
    }

    private static ArrayList<AgentPair> get_pairs(ArrayList<Agent> agents1,ArrayList<Agent> agents2){
        ArrayList<AgentPair> agentspairs = new ArrayList<>();
        for(int i = 0 ; i < agents1.size(); i++){
            for(int j = 0 ; j < agents2.size() ; j++){
                AgentPair pair = new AgentPair(agents1.get(i),agents2.get(j));
                agentspairs.add(pair);
            }
        }
        return agentspairs;
    }

    private static ArrayList<Connection> Switch_to_switch_connectivity(ArrayList<AgentPair> switchpairs) throws IOException{
        ArrayList<Connection> connections = new ArrayList<>();
        for (int i = 0; i < switchpairs.size(); i++) {
            String Switch1_ip = switchpairs.get(i).getAgent1().getIPAddress();
            String Switch2_ip = switchpairs.get(i).getAgent2().getIPAddress();
            ArrayList<String> Agent1_MacList = switchpairs.get(i).getAgent1().get_mac_addresses();
            ArrayList<String> Agent2_MacList = switchpairs.get(i).getAgent2().get_mac_addresses();
            for (String Agent1_MacList_Mac : Agent1_MacList) {
                if (MiscellaneousMethods.Mac_is_connected(Agent1_MacList_Mac, connections)) {
                    continue;
                }
                ArrayList<String> afts_Agent1 = SNMP_methods.getfromwalk_single(Switch1_ip, OIDS.dot1dTpFdbTable, OIDS.dot1dTpFdbAddress);
                for (String Agent2_MacList_Mac : Agent2_MacList) {
                    if (MiscellaneousMethods.Mac_is_connected(Agent2_MacList_Mac, connections)) {
                        continue;
                    }
                    ArrayList<String> afts_Agent2 = SNMP_methods.getfromwalk_single(Switch2_ip, OIDS.dot1dTpFdbTable, OIDS.dot1dTpFdbAddress);
                    if (afts_Agent1.contains(Agent2_MacList_Mac) && afts_Agent2.contains(Agent1_MacList_Mac)) {
                        Interface Switch1_Interface = switchpairs.get(i).getAgent1().GetInterface_byMacAddress(Agent1_MacList_Mac);
                        Interface Switch2_Interface = switchpairs.get(i).getAgent2().GetInterface_byMacAddress(Agent2_MacList_Mac);
                        Connection connection = new Connection(Switch1_Interface, Switch2_Interface, switchpairs.get(i).getAgent1(), switchpairs.get(i).getAgent2(), "Switch_Switch");
                        connections.add(connection);
                    }
                }
            }
        }
        return connections;
    }

    private static ArrayList<Connection> Switch_to_router_connectivity(ArrayList<AgentPair> switchrouterpairs,ArrayList<Connection> connections) throws IOException{
        for (int i = 0; i < switchrouterpairs.size(); i++) {
            String Switch_ip = switchrouterpairs.get(i).getAgent1().getIPAddress();
            String Router_ip = switchrouterpairs.get(i).getAgent2().getIPAddress();
            ArrayList<String> Switch_MacList = switchrouterpairs.get(i).getAgent1().get_mac_addresses();
            ArrayList<String> Router_MacList = switchrouterpairs.get(i).getAgent2().get_mac_addresses();
            //for (String Switch_MacList_Mac : Switch_MacList) {
//                if (MiscellaneousMethods.Mac_is_connected(Switch_MacList_Mac, connections)) {
//                    continue;
//                }
            ArrayList<String> afts_Switch = SNMP_methods.getfromwalk_single(Switch_ip, OIDS.dot1dTpFdbTable, OIDS.dot1dTpFdbAddress);
            ArrayList<String> aftsports_Switch = SNMP_methods.getfromwalk_single(Switch_ip, OIDS.dot1dTpFdbTable, OIDS.dot1dTpFdbPort);
                /*Interface interface_of_MAC = switchrouterpairs.get(i).getAgent1().GetInterface_byMacAddress(Switch_MacList_Mac);
                if(!aftsports_Switch.contains(interface_of_MAC.getIndex())){
                    continue;
                }*/
            ArrayList<Integer> portoccurunces_Switch = new ArrayList<>();
            ArrayList<Integer> removing_indices = new ArrayList<>();
            for(int p = 0 ; p < aftsports_Switch.size() ; p++){
                int count = 0;
                for(int l = 0 ; l < aftsports_Switch.size() ; l++){
                    if(aftsports_Switch.get(p).equals(aftsports_Switch.get(l))){
                        count++;
                    }
                }
                portoccurunces_Switch.add(count);
            }
            for(int p = 0 ; p < afts_Switch.size() ; p++){
                if(portoccurunces_Switch.get(p) > 1){
                    removing_indices.add(p);
                }
            }

            Collections.reverse(removing_indices);

            for(int p = 0 ; p < removing_indices.size() ; p++){
                afts_Switch.remove((int)removing_indices.get(p));
                aftsports_Switch.remove((int)removing_indices.get(p));
            }

            for (String Router_MacList_Mac : Router_MacList) {
                if(afts_Switch.contains(Router_MacList_Mac)){
                    int index = afts_Switch.indexOf(Router_MacList_Mac);
                    String switch_interface_index = aftsports_Switch.get(index);
                    Interface Switch_Interface = switchrouterpairs.get(i).getAgent1().GetInterface_byindex(switch_interface_index);
                    Interface Router_Interface = switchrouterpairs.get(i).getAgent2().GetInterface_byMacAddress(Router_MacList_Mac);
                    if(has_similar_connection(connections, switchrouterpairs.get(i).getAgent1(), switchrouterpairs.get(i).getAgent2(), Switch_Interface, Router_Interface)){
                        continue;
                    }
                    Connection connection = new Connection(Switch_Interface, Router_Interface, switchrouterpairs.get(i).getAgent1(), switchrouterpairs.get(i).getAgent2(), "Switch_Router");
                    connections.add(connection);
                }
            }

        }
        return connections;
    }

    private static ArrayList<Connection> Router_to_router_connectivity(ArrayList<Agent> routers, ArrayList<Connection> Connections) throws IOException {
        for (int i = 0; i < routers.size(); i++) {
            Router A = (Router) routers.get(i);
            ArrayList<Interface> usedinf = A.get_UsedInterfaces();
            ArrayList<String> nexthops = SNMP_methods.getfromwalk_single(A.getIPAddress(),
                    OIDS.ipNetToMediaTable, OIDS.ipNetToMediaNetAddress);
            ArrayList<String> tempnh = (ArrayList<String>) nexthops.clone();
            for (String s : tempnh) {
                if (A.has_IPaddress(s)) {
                    nexthops.remove(s);
                } else if (!MiscellaneousMethods.isHostIP(s)) {
                    nexthops.remove(s);
                }
            }
            if (!usedinf.isEmpty()) {
                for (int j = 0; j < usedinf.size(); j++) {
                        for (String s : nexthops) {
                            String Mask = usedinf.get(j).getSubnet_mask();
                            String infNetAddress = MiscellaneousMethods.getNetworkIP(usedinf.get(j).getIp_address(), Mask);
                            String ipNetAddress = MiscellaneousMethods.getNetworkIP(s, Mask);
                            if (infNetAddress.equals(ipNetAddress)) {
                                Router B = (Router) get_Agent_by_Ip(routers, s);
                                if (B != null) {
                                    Interface B_Interface = B.get_Interface_by_Interface_IP_address_property(s);
                                    if(!has_similar_connection(Connections, A, B, usedinf.get(j), B_Interface)){
                                        Connection interfaceConnection = new Connection(usedinf.get(j), B_Interface, A, B, "Router_Router");
                                        Connections.add(interfaceConnection);
                                    }
                                }
                            }
                        }
                }
            }
        }
        return Connections;
    }

    private static ArrayList<Connection> Router_to_host_connectivity(ArrayList<AgentPair> routerhostpairs, ArrayList<Connection> Connections) throws IOException {
        for (int i = 0; i < routerhostpairs.size(); i++) {
            Host host = (Host)routerhostpairs.get(i).getAgent2();
            if(MiscellaneousMethods.Interface_is_connected(host.getAnInterface(), Connections)){
                continue;
            }
            Router A = (Router) routerhostpairs.get(i).getAgent1();
            ArrayList<String> oids = new ArrayList<>();
            oids.add(OIDS.ipNetToMediaIfIndex);
            oids.add(OIDS.ipNetToMediaNetAddress);
            ArrayList<ArrayList<String>> nexthops = SNMP_methods.getfromwalk_multi(A.getIPAddress(),
                    OIDS.ipNetToMediaTable, oids);
            ArrayList<Integer> removing_indexes = new ArrayList<>();
            
            for (int n = 0 ; n < nexthops.get(0).size() ; n++){
                String ip = nexthops.get(1).get(n);
                if (A.has_IPaddress(ip)) {
                    removing_indexes.add(n);
                } else if (!MiscellaneousMethods.isHostIP(ip)) {
                    removing_indexes.add(n);
                }
            }
            
            Collections.reverse(removing_indexes);
            
            for(int n = 0 ; n < removing_indexes.size() ; n++){
                nexthops.get(0).remove((int)removing_indexes.get(n));
                nexthops.get(1).remove((int)removing_indexes.get(n));
            }
            
            ArrayList<Interface> usedinf = A.get_UsedInterfaces();
//            ArrayList<Interface> tempintfs = (ArrayList<Interface>) usedinf.clone();
//            for (Interface intf : tempintfs) {
//                if(MiscellaneousMethods.IP_is_connected(intf.getIp_address(), Connections)){
//                    usedinf.remove(intf);
//                }
//            }
            
            ArrayList<String> intfindex = new ArrayList<>();
            
            for (Interface intf : usedinf) {
                intfindex.add(intf.getIndex());
            }
            
            removing_indexes = new ArrayList<>();
            
            for (int n = 0 ; n < nexthops.get(0).size() ; n++){
                if (!intfindex.contains(nexthops.get(0).get(n))) {
                    removing_indexes.add(n);
                }
            }
            
            Collections.reverse(removing_indexes);
            
            for(int n = 0 ; n < removing_indexes.size() ; n++){
                nexthops.get(0).remove((int)removing_indexes.get(n));
                nexthops.get(1).remove((int)removing_indexes.get(n));
            }
            
            Host B = (Host) routerhostpairs.get(i).getAgent2();
            for(int n = 0 ; n < nexthops.get(0).size() ; n++){
                String ip = nexthops.get(1).get(n);
                if(ip.equals(B.getIPAddress())){
                    Interface interfaceA = A.GetInterface_byindex(nexthops.get(0).get(n));
                    Interface interfaceB = B.getAnInterface();
                    if (!has_similar_connection(Connections, A, B, interfaceA, interfaceB)) {
                        Connection interfaceconnection = new Connection(interfaceA, interfaceB, A, B, "Router_Host");
                        Connections.add(interfaceconnection);
                        break;
                    }
                }
            }
            
        }
        return Connections;
    }

    private static ArrayList<Connection> Switch_to_host_connectivity(ArrayList<AgentPair> switchhostpairs, ArrayList<Connection> Connections) throws IOException {
        for(AgentPair switchhostpair : switchhostpairs){
            String host_IP = switchhostpair.getAgent2().getIPAddress();
            String switch_IP = switchhostpair.getAgent1().getIPAddress();
            String switch_Mask = ((Switch)switchhostpair.getAgent1()).getMask();
            String switch_NetIP = MiscellaneousMethods.getNetworkIP(switch_IP, switch_Mask);
            if(!MiscellaneousMethods.isIPinSubnet(host_IP, switch_NetIP, switch_Mask)){
                continue;
            }
            ArrayList<String> oids = new ArrayList<>();
            oids.add(OIDS.ipNetToMediaNetAddress);
            oids.add(OIDS.ipNetToMediaPhysAddress);
            ArrayList<ArrayList<String>> switch_arptable = SNMP_methods.getfromwalk_multi(switch_IP, OIDS.ipNetToMediaTable, oids);
            oids = new ArrayList<>();
            oids.add(OIDS.dot1dTpFdbAddress);
            oids.add(OIDS.dot1dTpFdbPort);
            ArrayList<ArrayList<String>> switch_macaddrtable = SNMP_methods.getfromwalk_multi(switch_IP, OIDS.dot1dTpFdbTable, oids);
            ArrayList<Integer> portoccurunces_Switch = new ArrayList<>();
            ArrayList<Integer> removing_indices = new ArrayList<>();
//            for (int p = 0; p < switch_macaddrtable.get(1).size(); p++) {
//                int count = 0;
//                for (int l = 0; l < switch_macaddrtable.get(1).size(); l++) {
//                    if (switch_macaddrtable.get(1).get(p).equals(switch_macaddrtable.get(1).get(l))) {
//                        count++;
//                    }
//                }
//                portoccurunces_Switch.add(count);
//            }
//
//            for (int p = 0; p < switch_macaddrtable.get(1).size(); p++) {
//                if (portoccurunces_Switch.get(p) > 1) {
//                    removing_indices.add(p);
//                }
//            }

            Collections.reverse(removing_indices);

            for (int p = 0; p < removing_indices.size(); p++) {
                switch_macaddrtable.get(0).remove((int) removing_indices.get(p));
                switch_macaddrtable.get(1).remove((int) removing_indices.get(p));
            }

            int indexofhost_in_arptable = switch_arptable.get(0).indexOf(host_IP);

//            if(switch_arptable.get(1).contains(indexofhost_in_arptable))

            String mac_of_host = switch_arptable.get(1).get(indexofhost_in_arptable);

            for(int i = 0; i < switch_macaddrtable.get(0).size() ; i++){
                if(switch_macaddrtable.get(0).get(i).equals(mac_of_host)){
                    Switch switchA = (Switch)switchhostpair.getAgent1();
                    Host hostB = (Host)switchhostpair.getAgent2();
                    Interface switch_interface = switchA.GetInterface_byindex(switch_macaddrtable.get(1).get(i));
//                    if(MiscellaneousMethods.Interface_is_connected(switch_interface, Connections)){
//                        continue;
//                    }
                    hostB.set_Mac_Address(mac_of_host);
                    Interface host_interface = hostB.getAnInterface();
                    if(has_similar_connection(Connections, switchA, hostB, switch_interface, host_interface)){
                        continue;
                    }
                    Connection interfaceConnection = new Connection(switch_interface, host_interface, switchA, hostB, "Switch_Host");
                    Connections.add(interfaceConnection);
                    break;
                }
            }

        }
        return Connections;
    }

    private static Router create_Router(String IP) throws Exception {
        ArrayList<String> oids = new ArrayList<>();
        oids.add(OIDS.sysDescr);
        oids.add(OIDS.sysName);
        oids = SNMP_methods.getfromsnmpget_multi(IP, oids);
        String sysdescr = oids.get(0);
        String sysname = oids.get(1);
        ArrayList<ArrayList<String>> interfaces = new ArrayList<>();
        oids = new ArrayList<>();
        oids.add(OIDS.ifIndex);
        oids.add(OIDS.ifPhyaddress);
        oids.add(OIDS.ifDescr);
        interfaces = SNMP_methods.getfromwalk_multi(IP, OIDS.ifTable, oids);
        oids = new ArrayList<>();
        oids.add(OIDS.ipNetToMediaIfIndex);
        oids.add(OIDS.ipNetToMediaNetAddress);
        oids.add(OIDS.ipNetToMediaPhysAddress);
        ArrayList<ArrayList<String>> ntm = SNMP_methods.getfromwalk_multi(IP, OIDS.ipNetToMediaTable, oids);
        oids = new ArrayList<>();
        oids.add(OIDS.ipRouteIfIndex);
        oids.add(OIDS.ipRouteMask);
        ArrayList<ArrayList<String>> iproutes = SNMP_methods.getfromwalk_multi(IP, OIDS.ipRouteTable, oids);
        ArrayList<String> ips = new ArrayList<>();
        ArrayList<String> masks = new ArrayList<>();
        for (int i = 0; i < interfaces.get(0).size(); i++) {
            if (ntm.get(2).contains(interfaces.get(1).get(i))) {
                int index = ntm.get(2).indexOf(interfaces.get(1).get(i));
                if (ntm.get(0).get(index).equals(interfaces.get(0).get(i))
                        && ntm.get(2).get(index).equals(interfaces.get(1).get(i))) {
                    ips.add(ntm.get(1).get(index));
                } else {
                    ips.add("");
                }
            } else {
                ips.add("");
            }
            if (iproutes.get(0).contains(interfaces.get(0).get(i))) {
                int index = iproutes.get(0).indexOf(interfaces.get(0).get(i));
                masks.add(iproutes.get(1).get(index));
            } else {
                masks.add("");
            }
        }
        interfaces.add(ips);
        interfaces.add(masks);
        ArrayList<Interface> routerifs = new ArrayList<>();
        for (int i = 0; i < interfaces.get(0).size(); i++) {
            Interface routerif = new Interface(interfaces.get(0).get(i),
                    interfaces.get(2).get(i),
                    interfaces.get(3).get(i),
                    interfaces.get(4).get(i),
                    interfaces.get(1).get(i));
            routerifs.add(routerif);
        }
        Router router = new Router(sysdescr, sysname, routerifs);
        return router;
    }

    private static Switch create_Switch(String IP) throws Exception {
        ArrayList<String> oids = new ArrayList<>();
        oids.add(OIDS.sysDescr);
        oids.add(OIDS.sysName);
        oids = SNMP_methods.getfromsnmpget_multi(IP, oids);
        String sysdescr = oids.get(0);
        String sysname = oids.get(1);
        ArrayList<ArrayList<String>> interfaces = new ArrayList<>();
        oids = new ArrayList<>();
        oids.add(OIDS.ifIndex);
        oids.add(OIDS.ifPhyaddress);
        oids.add(OIDS.ifDescr);
        interfaces = SNMP_methods.getfromwalk_multi(IP, OIDS.ifTable, oids);
        oids = new ArrayList<>();
        oids.add(OIDS.ipNetToMediaIfIndex);
        oids.add(OIDS.ipNetToMediaNetAddress);
        oids.add(OIDS.ipNetToMediaPhysAddress);
        ArrayList<ArrayList<String>> ntm = SNMP_methods.getfromwalk_multi(IP, OIDS.ipNetToMediaTable, oids);
        ArrayList<String> ips = new ArrayList<>();
        for (int i = 0; i < interfaces.get(0).size(); i++) {
            if (ntm.get(2).contains(interfaces.get(1).get(i))) {
                int index = ntm.get(2).indexOf(interfaces.get(1).get(i));
                if (ntm.get(0).get(index).equals(interfaces.get(0).get(i))
                        && ntm.get(2).get(index).equals(interfaces.get(1).get(i))) {
                    ips.add(ntm.get(1).get(index));
                } else {
                    ips.add("");
                }
            } else {
                ips.add("");
            }
        }
        interfaces.add(ips);
        ArrayList<Interface> switchifs = new ArrayList<>();
        for (int i = 0; i < interfaces.get(0).size(); i++) {
            Interface switchif = new Interface(interfaces.get(0).get(i),
                    interfaces.get(2).get(i),
                    interfaces.get(3).get(i),
                    interfaces.get(1).get(i));
            switchifs.add(switchif);
        }
        oids = new ArrayList<>();
        oids.add(OIDS.ipAdEntNetMask);
        oids.add(OIDS.ipAdEntIfIndex);
        ArrayList<ArrayList<String>> ip_mask_interface = SNMP_methods.getfromwalk_multi(IP, OIDS.ipAddrTable, oids);
        for(int i = 0; i < ip_mask_interface.get(0).size() ; i++){
            for (Interface intf : switchifs) {
                if(intf.getIndex().equals(ip_mask_interface.get(1).get(i))){
                    intf.setSubnet_mask(ip_mask_interface.get(0).get(i));
                }
            }
        }
        Switch switchh = new Switch(sysdescr, sysname, switchifs);
        for(Interface intf : switchh.getInterfaces()){
            if(intf.getIp_address().equals("")){
                intf.setIp_address(switchh.getIPAddress());
                intf.setSubnet_mask(switchh.getMask());
            }
        }
        return switchh;
    }

    private static ArrayList<Connection> Filter_Connections(ArrayList<Connection> connections){
        ArrayList<Connection> temp = (ArrayList<Connection>) connections.clone();
        for (Connection connection : connections) {
            if(connection.getInterfaceA().getDescription().contains("Vlan") || connection.getInterfaceB().getDescription().contains("Vlan")){
                temp.remove(connection);
            }else if(connection.getInterfaceA().getDescription().contains("vlan") || connection.getInterfaceB().getDescription().contains("vlan")){
                temp.remove(connection);
            }else if(connection.getInterfaceA().getDescription().contains("Null") || connection.getInterfaceB().getDescription().contains("Null")){
                temp.remove(connection);
            }else if(connection.getInterfaceA().getDescription().contains("null") || connection.getInterfaceB().getDescription().contains("null")){
                temp.remove(connection);
            }

            if(connection.getType().equals("Switch_Router")){
                for (Connection connection2 : connections) {
                    if (!connection.equals(connection2)) {
                        if (connection2.getType().equals("Router_Router")) {
                            if(connection.getAgentB().equals(connection2.getAgentA())){
                                if(connection.getInterfaceB().equals(connection2.getInterfaceA())){
                                    temp.remove(connection2);
                                }
                            }else if(connection.getAgentB().equals(connection2.getAgentB())){
                                if(connection.getInterfaceB().equals(connection2.getInterfaceB())){
                                    temp.remove(connection2);
                                }
                            }
                        }
                    }
                }
            }else if(connection.getType().equals("Switch_Switch")){
                for (Connection connection2 : connections){
                    if(!connection.equals(connection2)){
                        if (connection2.getType().equals("Switch_Host")) {
                            if(connection.getAgentA().equals(connection2.getAgentA())){
                                if(connection.getInterfaceA().equals(connection2.getInterfaceA())){
                                    temp.remove(connection2);
                                }
                            }else if(connection.getAgentA().equals(connection2.getAgentB())){
                                if(connection.getInterfaceA().equals(connection2.getInterfaceB())){
                                    temp.remove(connection2);
                                }
                            }else if(connection.getAgentB().equals(connection2.getAgentA())){
                                if(connection.getInterfaceB().equals(connection2.getInterfaceA())){
                                    temp.remove(connection2);
                                }
                            }else if(connection.getAgentB().equals(connection2.getAgentB())){
                                if(connection.getInterfaceB().equals(connection2.getInterfaceB())){
                                    temp.remove(connection2);
                                }
                            }
                        }
                    }
                }
            }

        }
        return temp;
    }

    private static ArrayList<Connection> Find_Hidden_Connections(ArrayList<Connection> connections,ArrayList<Agent> agents) {
        ArrayList<Connection> need_to_edit_connections = new ArrayList<>();
        ArrayList<String> Subnets = new ArrayList<>();
        ArrayList<String> Masks = new ArrayList<>();
        for (int i = 0; i < connections.size(); i++) {
            Connection c1 = connections.get(i);
            for (int j = 0; j < connections.size(); j++) {
                Connection c2 = connections.get(j);
                if (!c1.equals(c2)) {
                    if (c1.getAgentA().equals(c2.getAgentA())) {
//                        if (!c1.getAgentA().getClass().getSimpleName().equals("Host")) {
                        if (c1.getInterfaceA().equals(c2.getInterfaceA())) {
                            need_to_edit_connections.add(c1);
                            break;
                        }
//                        } else {
//                            need_to_edit_connections.add(c1);
//                            break;
//                        }
                    } else if (c1.getAgentA().equals(c2.getAgentB())) {
//                        if (!c1.getAgentA().getClass().getSimpleName().equals("Host")) {
                        if (c1.getInterfaceA().equals(c2.getInterfaceB())) {
                            need_to_edit_connections.add(c1);
                            break;
                        }
//                        } else {
//                            need_to_edit_connections.add(c1);
//                            break;
//                        }
                    } else if (c1.getAgentB().equals(c2.getAgentA())) {
//                        if (!c1.getAgentB().getClass().getSimpleName().equals("Host")) {
                        if (c1.getInterfaceB().equals(c2.getInterfaceA())) {
                            need_to_edit_connections.add(c1);
                            break;
                        }
//                        } else {
//                            need_to_edit_connections.add(c1);
//                            break;
//                        }
                    } else if (c1.getAgentB().equals(c2.getAgentB())) {
//                        if (!c1.getAgentB().getClass().getSimpleName().equals("Host")) {
                        if (c1.getInterfaceB().equals(c2.getInterfaceB())) {
                            need_to_edit_connections.add(c1);
                            break;
                        }
//                        } else {
//                            need_to_edit_connections.add(c1);
//                            break;
//                        }
                    }
                }
            }
        }

        for (Connection connection : need_to_edit_connections) {
            connections.remove(connection);
            if (connection.getType().equals("Switch_Switch")) {
                String subnet = MiscellaneousMethods.getNetworkIP(connection.getAgentA().getIPAddress(), ((Switch) connection.getAgentA()).getMask());
                if (!Subnets.contains(subnet)) {
                    Subnets.add(subnet);
                    Masks.add(((Switch) connection.getAgentA()).getMask());
                }
            } else if (connection.getType().equals("Switch_Router")) {
                String subnet = MiscellaneousMethods.getNetworkIP(connection.getInterfaceB().getIp_address(), connection.getInterfaceB().getSubnet_mask());
                if (!Subnets.contains(subnet)) {
                    Subnets.add(subnet);
                    Masks.add(connection.getInterfaceB().getSubnet_mask());
                }
            } else {
                String subnet = MiscellaneousMethods.getNetworkIP(connection.getInterfaceA().getIp_address(), connection.getInterfaceA().getSubnet_mask());
                if (!Subnets.contains(subnet)) {
                    Subnets.add(subnet);
                    Masks.add(connection.getInterfaceA().getSubnet_mask());
                }
            }
        }

        ArrayList<Switch> hidden_switches = new ArrayList<>();
        ArrayList<Integer> switches_interfaces_index = new ArrayList<>();
        for (int i = 0; i < Subnets.size(); i++) {
            ArrayList<Interface> intfs = new ArrayList<>();
            intfs.add(new Interface("0", "not used interface", Subnets.get(i), Masks.get(i), ""));
            Switch sw = new Switch("Ethernet Switch", "Ethernet Switch" + i, intfs);
            hidden_switches.add(sw);
            switches_interfaces_index.add(1);
        }

        ArrayList<Connection> Hidden_Connections = new ArrayList<>();

        for (Connection connection : need_to_edit_connections) {
            if (connection.getType().equals("Switch_Switch")) {
                if (!MiscellaneousMethods.Interface_is_connected(connection.getInterfaceA(), connections)) {
                    for (int i = 0; i < hidden_switches.size(); i++) {
                        if (MiscellaneousMethods.isIPinSubnet(connection.getAgentA().getIPAddress(), hidden_switches.get(i).getIPAddress(), hidden_switches.get(i).getMask())) {
                            String intindx = switches_interfaces_index.get(i).toString();
                            Interface intf = new Interface(intindx, "Ethernet Port", hidden_switches.get(i).getIPAddress(), hidden_switches.get(i).getMask(), "");
                            hidden_switches.get(i).add_Interface(intf);
                            switches_interfaces_index.set(i, switches_interfaces_index.get(i) + 1);
                            Connection interfaceConnection = new Connection(connection.getInterfaceA(), intf, connection.getAgentA(), hidden_switches.get(i), connection.getAgentA().getClass().getSimpleName() + "_Switch");
                            connections.add(interfaceConnection);
                        }
                    }
                }
                if (!MiscellaneousMethods.Interface_is_connected(connection.getInterfaceB(), connections)) {
                    for (int i = 0; i < hidden_switches.size(); i++) {
                        if (MiscellaneousMethods.isIPinSubnet(connection.getAgentB().getIPAddress(), hidden_switches.get(i).getIPAddress(), hidden_switches.get(i).getMask())) {
                            String intindx = switches_interfaces_index.get(i).toString();
                            Interface intf = new Interface(intindx, "Ethernet Port", hidden_switches.get(i).getIPAddress(), hidden_switches.get(i).getMask(), "");
                            hidden_switches.get(i).add_Interface(intf);
                            switches_interfaces_index.set(i, switches_interfaces_index.get(i) + 1);
                            Connection interfaceConnection = new Connection(connection.getInterfaceB(), intf, connection.getAgentB(), hidden_switches.get(i), connection.getAgentB().getClass().getSimpleName() + "_Switch");
                            connections.add(interfaceConnection);
                        }
                    }
                }
            } else {
                if (!MiscellaneousMethods.Interface_is_connected(connection.getInterfaceA(), connections)) {
                    for (int i = 0; i < hidden_switches.size(); i++) {
                        if (MiscellaneousMethods.isIPinSubnet(connection.getInterfaceA().getIp_address(), hidden_switches.get(i).getIPAddress(), hidden_switches.get(i).getMask())) {
                            String intindx = switches_interfaces_index.get(i).toString();
                            Interface intf = new Interface(intindx, "Ethernet Port", hidden_switches.get(i).getIPAddress(), hidden_switches.get(i).getMask(), "");
                            hidden_switches.get(i).add_Interface(intf);
                            switches_interfaces_index.set(i, switches_interfaces_index.get(i) + 1);
                            Connection interfaceConnection = new Connection(connection.getInterfaceA(), intf, connection.getAgentA(), hidden_switches.get(i), connection.getAgentA().getClass().getSimpleName() + "_Switch");
                            connections.add(interfaceConnection);
                        }
                    }
                }
                if (!MiscellaneousMethods.Interface_is_connected(connection.getInterfaceB(), connections)) {
                    for (int i = 0; i < hidden_switches.size(); i++) {
                        if (MiscellaneousMethods.isIPinSubnet(connection.getInterfaceB().getIp_address(), hidden_switches.get(i).getIPAddress(), hidden_switches.get(i).getMask())) {
                            String intindx = switches_interfaces_index.get(i).toString();
                            Interface intf = new Interface(intindx, "Ethernet Port", hidden_switches.get(i).getIPAddress(), hidden_switches.get(i).getMask(), "");
                            hidden_switches.get(i).add_Interface(intf);
                            switches_interfaces_index.set(i, switches_interfaces_index.get(i) + 1);
                            Connection interfaceConnection = new Connection(connection.getInterfaceB(), intf, connection.getAgentB(), hidden_switches.get(i), connection.getAgentB().getClass().getSimpleName() + "_Switch");
                            connections.add(interfaceConnection);
                        }
                    }
                }

            }
        }

        for (Switch sw : hidden_switches) {
            agents.add(sw);
        }

        return connections;
    }

}
