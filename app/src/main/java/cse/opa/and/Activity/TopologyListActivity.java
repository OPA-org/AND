package cse.opa.and.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cse.opa.and.CustomAdapter;
import cse.opa.and.CustomObject;
import cse.opa.and.R;

public class TopologyListActivity extends AppCompatActivity {
    private ListView lv_toloplogy_list;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private StorageReference mStorageRef;
    private FirebaseUser currentUser;
    DatabaseReference Reportsref ;
    ArrayList<CustomObject> reports=new ArrayList<>();
    CustomAdapter customAdapter;
    private static boolean connected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_topologies);
        initUIComponents();
        initComponents();
    }
    private void initUIComponents(){
        MainActivity.setStatusBarGradiant(this);
        lv_toloplogy_list = findViewById(R.id.lv);
        lv_toloplogy_list.setAdapter(customAdapter);
        lv_toloplogy_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                String reportname = reports.get(position).getReportFullname();
                StorageReference userReportRef = mStorageRef.child("Reports/" + currentUser.getUid() + "/"+reportname+".txt");
                mStorageRef.getStorage().setMaxDownloadRetryTimeMillis(5000);
                File localFile = null;
                localFile = File.createTempFile("report_name", "txt");
                    File finalLocalFile = localFile;
                    userReportRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                // Successfully downloaded data to local file
                                FileInputStream fin = null;
                                try {
                                    fin = new FileInputStream(finalLocalFile);
                                    String output = convertStreamToString(fin);
                                    //Make sure you close all streams.
                                    fin.close();
                                    Intent i = new Intent(TopologyListActivity.this, ReportActivity.class);
                                    i.putExtra("Report",output);
                                    startActivity(i);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(TopologyListActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle failed download
                        if (exception.getMessage().contains("The operation retry limit has been exceeded.")){
                            showErrorDialog(null);
                        }
                         Toast.makeText(TopologyListActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                    }
                });

        }
        catch (IOException e) {
            e.printStackTrace();
        }
            }});
    }
    //======================================================
    private void initComponents(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();
        customAdapter = new CustomAdapter(this, R.layout.listview, reports);
        lv_toloplogy_list.setAdapter(customAdapter);
        initConnectionListener();
        fillListview();
    }
    //======================================================
    private void initConnectionListener(){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }
    //======================================================
    private void fillListview(){
        if (mDatabase!=null){
            Reportsref = mDatabase.getReference().child("users").child(currentUser.getUid()).child("Reports");
            Reportsref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.v("Update","CALL_UPDATE!!  "+(String) dataSnapshot.getValue());
                    updateListView(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.v("Update","CALL_UPDATE!!");
                    updateListView(dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    //======================================================
    private void updateListView(DataSnapshot dataSnapshot){
        //Iterator i = dataSnapshot.getChildren().iterator();
        //Log.v("updateList",i.hasNext()+"");
        String report_name;
       // while (i.hasNext()){
            report_name = (String)dataSnapshot.getValue();
            Log.v("Report","Report :"+report_name);
            reports.add(new CustomObject(report_name, report_name.replace(report_name.substring(6,17),""),currentUser.getEmail(),report_name.replace("Report-","").substring(0,10).replace("-","/")));
            customAdapter.notifyDataSetChanged();
          //  }

    }
    //======================================================
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
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
