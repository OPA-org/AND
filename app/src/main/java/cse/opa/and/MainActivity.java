package cse.opa.and;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_create_topology, btn_load_topology, btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_main);
        //===========================================================
        btn_create_topology = findViewById(R.id.btn_create_topology);
        btn_load_topology = findViewById(R.id.btn_load_topology);
        btn_login = findViewById(R.id.btn_login);
        //===========================================================
        btn_create_topology.setOnClickListener(this);
        btn_load_topology.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_create_topology: {
                startActivity(new Intent(MainActivity.this, TopologyActivity.class));
                break;
            }
            //===========================================================
            case R.id.btn_load_topology: {
                startActivity(new Intent(MainActivity.this, ListOfTopologies.class));
                break;
            }
            //===========================================================
            case R.id.btn_login: {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            }
            //===========================================================
        }
    }
}
