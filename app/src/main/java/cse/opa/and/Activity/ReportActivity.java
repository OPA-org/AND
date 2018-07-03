package cse.opa.and.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;

import cse.opa.and.Classes.Agent;
import cse.opa.and.Classes.Connection;
import cse.opa.and.Classes.OIDS;
import cse.opa.and.Classes.SNMP_methods;
import cse.opa.and.Classes.Switch;
import cse.opa.and.Classes.Topology;
import cse.opa.and.R;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private TextView tv_report;
    private Button btn_save_report;
    private String output = "";
    private FirebaseAuth mAuth;
    ImageView iv_report_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initUIComponents();
        MainActivity.setStatusBarGradiant(this);
        //generateReport();
        mAuth = FirebaseAuth.getInstance();
        generateReport();
    }
    @Override
    public void onStart() {//checks on start of application if user is already signed in
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //update UI ->Remove login and add settings
        updateUI(currentUser);
    }
    //======================================================
    private void updateUI(FirebaseUser user){
        if (user != null) {
            btn_save_report.setVisibility(View.VISIBLE);
        }
        else{
            btn_save_report.setVisibility(View.GONE);
        }

    }
    //======================================================
    private void initUIComponents(){
        tv_report = findViewById(R.id.tv_report);
        progressDialog = new ProgressDialog(this);
        iv_report_back = findViewById(R.id.iv_report_back);
        btn_save_report = findViewById(R.id.btn_save_report);
        iv_report_back.setOnClickListener(this);
        btn_save_report.setOnClickListener(this);
    }
    //===========================================================
    void device_discovery(String gateway_IP,ArrayList<String> R,ArrayList<String> D) throws IOException,Exception{
        R.add(gateway_IP);
        int n = 0;
        while(true){
            if(!SNMP_methods.getfromsnmpget_single(R.get(n), OIDS.sysDescr).equals("error")){
                ArrayList<String> oids = new ArrayList<>();
                oids.add(OIDS.ipRouteType);
                oids.add(OIDS.ipRouteNextHop);
                ArrayList<ArrayList<String>> Next_hops = SNMP_methods.getfromwalk_multi(R.get(n),OIDS.ipRouteTable,oids);
                for(int i =0; i < Next_hops.get(0).size() ; i++){
                    if(!Next_hops.get(0).get(i).equals("4")){
                        continue;
                    }else{
                        if(R.contains(Next_hops.get(1).get(i))){
                            continue;
                        }else{
                            R.add(Next_hops.get(1).get(i));
                        }
                        if(D.contains(Next_hops.get(1).get(i))){
                            continue;
                        }else{
                            D.add(Next_hops.get(1).get(i));
                        }
                    }
                }
            }else{
                //R[n].type= host
                if(!D.contains(R.get(n)))
                    D.add(R.get(n));
            }
            n++;
            if(n >= R.size()){
                break;
            }
        }
        n = 0;
        while(true){
            if(D.isEmpty()){
                break;
            }
            if(!SNMP_methods.getfromsnmpget_single(D.get(n), OIDS.sysDescr).equals("error")){
                ArrayList<String> NetToMediaAddresses = SNMP_methods.getfromwalk_single(D.get(n),OIDS.ipNetToMediaTable,OIDS.ipNetToMediaNetAddress);
                for(String ND: NetToMediaAddresses){
                    if(D.contains(ND)){
                        continue;
                    }else{
                        D.add(ND);
                    }
                }
            }
            n++;
            if(n >= D.size()){
                break;
            }
        }
    }
    //===========================================================
    private void generateReport()  {
        Topology topology = TopologyActivity.topology;
        int routers = 0;
        int switches = 0;
        int hosts = 0;
        for(Agent a : topology.getAgents()){
            switch (a.getType()) {
                case "Router": {
                    routers++;
                    break;
                }
                case "Switch":{
                    switches++;
                    break;
                }
                default:{
                    hosts++;
                }

            }
        }
        output += "This network is consisted of "+topology.getAgents().size()+" device.\n\n\t";
        if(routers > 0) {
            output += "Routers: " + routers + "\n\t";
        }
        if(switches > 0) {
            output += "Switches: " + switches + "\n\t";
        }
        if(hosts > 0) {
            output += "End Hosts: " + hosts + "\n\n";
        }
        output += "____________________________\n\n";
        if(topology.getConnections().size() > 0) {
            output += "Connections: \n\n\t";
            for (Connection c : topology.getConnections()) {
                output += "Connection\n\n";
                output += c.toString() + "\n\n";
            }
            output += "____________________________\n\n";
        }
        if(topology.getAgents().size() > 0) {
            for (Agent a : topology.getAgents()) {
                output += a.toString() + "\n\n";
            }
        }
        tv_report.setText(output);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_save_report){

        }
        else if (v.getId()==R.id.iv_report_back){
            finish();
        }
    }
    //===========================================================
}
