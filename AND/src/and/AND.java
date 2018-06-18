package and;

import java.io.IOException;
import java.util.ArrayList;

public class AND {

    public static void main(String[] args) throws IOException, Exception {
        ArrayList<String> R = new ArrayList<>();
        ArrayList<String> D = new ArrayList<>();
        device_discovery("192.168.137.93", R, D);
        System.out.println("R:-");
        for (String s : R) {
            System.out.println(s);
        }
        System.out.println("D:-");
        for (String s : D) {
            System.out.println(s);
        }
        //String s = SNMP_methods.getfromsnmpget_single("192.168.5.1",OIDS.sysDescr) ;
        //System.out.println(s);
    }

    static void device_discovery(String gateway_IP, ArrayList<String> R, ArrayList<String> D) throws IOException, Exception {
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
                        if (!R.contains(Next_hops.get(1).get(i))) {
                            R.add(Next_hops.get(1).get(i));
                        }
                        if (!D.contains(Next_hops.get(1).get(i))) {
                            D.add(Next_hops.get(1).get(i));
                        }
                    }
                }
            } else {
                //R[n].type= host
                if (!D.contains(R.get(n))) {
                    D.add(R.get(n));
                }
            }
            n++;
            if (n >= R.size()) {
                break;
            }
        }
        n = 0;
        while (true) {
            if (D.isEmpty()) {
                break;
            }
            if (!SNMP_methods.getfromsnmpget_single(D.get(n), OIDS.sysDescr).equals("error")) {
                ArrayList<String> NetToMediaAddresses = SNMP_methods.getfromwalk_single(D.get(n), OIDS.ipNetToMediaTable, OIDS.ipNetToMediaNetAddress);
                for (String ND : NetToMediaAddresses) {
                    if (!D.contains(ND)) {
                        D.add(ND);
                    }
                }
            }
            n++;
            if (n >= D.size()) {
                break;
            }
        }
    }
}
