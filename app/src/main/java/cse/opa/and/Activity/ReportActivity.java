package cse.opa.and.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cse.opa.and.Classes.Agent;
import cse.opa.and.Classes.Connection;
import cse.opa.and.Classes.Topology;
import cse.opa.and.R;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private TextView tv_report;
    private Button btn_save_report;
    private String output = "";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private StorageReference mStorageRef;
    private FirebaseUser currentUser;
    private ImageView iv_report_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initUIComponents();
        initComponents();
    }
    @Override
    public void onStart() {//checks on start of application if user is already signed in
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        //update UI ->Remove login and add settings
        updateUI(currentUser);
    }
    //======================================================
    private void updateUI(FirebaseUser user){
        if (user != null) {
            if(getIntent().hasExtra("Report")){
                btn_save_report.setVisibility(View.GONE);
            }
            else
                btn_save_report.setVisibility(View.VISIBLE);
        }
        else{
            btn_save_report.setVisibility(View.GONE);
        }
    }
    //======================================================
    private void initComponents(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        if(getIntent().hasExtra("Report")){
            output = getIntent().getExtras().getString("Report");
            tv_report.setText(output);
            //btn_save_report.setVisibility(View.GONE);
        }else{
            generateReport();
            //btn_save_report.setVisibility(View.VISIBLE);
        }
    }
    //======================================================
    private void initUIComponents(){
        MainActivity.setStatusBarGradiant(this);
        tv_report = findViewById(R.id.tv_report);
        progressDialog = new ProgressDialog(this);
        iv_report_back = findViewById(R.id.iv_report_back);
        btn_save_report = findViewById(R.id.btn_save_report);
        iv_report_back.setOnClickListener(this);
        btn_save_report.setOnClickListener(this);
    }
    //===========================================================
    private File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            // Error while creating file
        }
        return file;
    }
    //===========================================================
    private void upload_report() {
        try {
            if (mStorageRef!=null){
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy-hh-mm-aa");
                String date = df.format(Calendar.getInstance().getTime());
                StorageReference userReportRef =
                        mStorageRef.child("Reports/" + currentUser.getUid() + "/Report-"+date+".txt");
                //if we get interrupted for more than 2 seconds, fail the operation.
                mStorageRef.getStorage().setMaxUploadRetryTimeMillis(5000);
                File file = getTempFile(this.getBaseContext(),"Report-"+date+".txt");
                FileOutputStream stream = new FileOutputStream(file);
                stream.write(output.getBytes());
                stream.close();
                if (file != null) {
                    Uri uri_file = Uri.fromFile(file);
                    progressDialog.setMessage("Uploading Report...");//update progress dialog message
                    progressDialog.show();
                    userReportRef.putFile(uri_file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    mDatabase.getReference().child("users").child(currentUser.getUid()).child("Reports").child("Report-"+date).setValue("Report-"+date);
                                    progressDialog.cancel();
                                    Toast.makeText(ReportActivity.this, "Report Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                    file.delete();
                                    TopologyActivity.report_saved = true;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    if (e.getMessage().contains("The operation retry limit has been exceeded.")){
                                        showErrorDialog(null);
                                    }
                                    Toast.makeText(ReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    showErrorDialog(null);
                }
            }
            else
            {
                showErrorDialog(null);
            }
        } catch (IOException e){
            Toast.makeText(ReportActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
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
    //===========================================================
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_save_report){
            if (!TopologyActivity.report_saved){
                upload_report();
            }else{
                Toast.makeText(ReportActivity.this, "This report is already saved", Toast.LENGTH_SHORT).show();
            }
        }
        else if (v.getId()==R.id.iv_report_back){
            finish();
        }
    }

    //======================================================
    private void showErrorDialog(String message){
        if (message==null){
            message = "Please try again.\nIf this happened before check your internet connection.\nif you are connected to the internet and this message keeps appearing contact the app developer";
        }
        LinearLayout.LayoutParams params = (new LinearLayout.LayoutParams(90, 90));
        params.gravity = Gravity.CENTER;
        new LovelyInfoDialog(this)
                .setTopColorRes(R.color.Grey_blue)
                .setIcon(R.drawable.ic_info_white)
                .configureView(rootView -> rootView.findViewById(R.id.ld_icon).setLayoutParams(params))
                .setTitle("Error")
                .setMessage(message)
                .show();
    }
    //======================================================
}
