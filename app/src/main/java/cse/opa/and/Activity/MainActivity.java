package cse.opa.and.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import cse.opa.and.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_create_topology, btn_load_topology, btn_login , btn_logout;
    private LinearLayout ll_login,ll_logout;
    private FirebaseAuth mAuth;
    private static boolean connected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

        setStatusBarGradiant(this);
        setContentView(R.layout.activity_main);
        //===========================================================
        btn_create_topology = findViewById(R.id.btn_create_topology);
        btn_load_topology = findViewById(R.id.btn_load_topology);
        btn_login = findViewById(R.id.btn_login);
        btn_logout = findViewById(R.id.btn_logout);
        ll_login = findViewById(R.id.ll_login);
        ll_logout = findViewById(R.id.ll_logout);
        //===========================================================
        btn_create_topology.setOnClickListener(this);
        btn_load_topology.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        //===========================================================
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {//checks on start of application if user is already signed in
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //update UI ->Remove login and add settings
        updateUI(currentUser);
        TopologyActivity.report_saved = false;
        initConnectionListener();
    }
    //======================================================
    private void updateUI(FirebaseUser user){
        if (user != null) {
            ll_login.setVisibility(View.GONE);
            ll_logout.setVisibility(View.VISIBLE);
            btn_load_topology.setVisibility(View.VISIBLE);
        }
        else{
            ll_login.setVisibility(View.VISIBLE);
            ll_logout.setVisibility(View.GONE);
            btn_load_topology.setVisibility(View.GONE);
        }

    }
    //======================================================
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_create_topology: {
                startActivity(new Intent(MainActivity.this, TopologyActivity.class));
                break;
            }
            //===========================================================
            case R.id.btn_load_topology: {
                if (connected){
                    startActivity(new Intent(MainActivity.this, TopologyListActivity.class));
                }
                else {
                    showErrorDialog(null);
                }
                break;
            }
            //===========================================================
            case R.id.btn_login: {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                //finish();
                break;
            }
            //===========================================================
            case R.id.btn_logout: {
                mAuth.signOut();
//                ll_login.setVisibility(View.VISIBLE);
//                ll_logout.setVisibility(View.GONE);
//                btn_load_topology.setVisibility(View.GONE);
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;
            }
            //===========================================================
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.background_gradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }
    private void initConnectionListener(){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                connected = snapshot.getValue(Boolean.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
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
