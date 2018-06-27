package cse.opa.and.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import cse.opa.and.Classes.OIDS;
import cse.opa.and.Classes.SNMP_methods;
import cse.opa.and.R;

public class ReportActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private TextView tv_report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initUIComponents();
        MainActivity.setStatusBarGradiant(this);
        //generateReport();
    }
    private void initUIComponents(){
        tv_report = findViewById(R.id.tv_report);
        progressDialog = new ProgressDialog(this);
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

        progressDialog.setMessage("Generating Report");
        progressDialog.show();
        try {
            new  AsyncTask<Void, Void, Void>() {
                ArrayList<String> R = new ArrayList<>();
                ArrayList<String> D = new ArrayList<>();
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                }
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        device_discovery("192.168.6.1",R,D);

                    } catch (Exception e) {

                        System.out.println("Exception "+ e.getMessage());
                        return null;
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void Void) {
                    progressDialog.cancel();
                    tv_report.setText("R:-");
                    String temp;
                    for(String s: R){
                        temp =tv_report.getText().toString();
                        tv_report.setText(temp+"\n"+s);
                    }
                    temp =tv_report.getText().toString();
                    tv_report.setText(temp+"\n"+"D:-");
                    for(String s: D){
                        temp =tv_report.getText().toString();
                        tv_report.setText(temp+"\n"+s);
                    }
                }
            }.execute().get();










        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //===========================================================
}
