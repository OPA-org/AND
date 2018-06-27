package cse.opa.and.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import cse.opa.and.R;

public class TopologyActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_save_topology, btn_generate_report;
    String m_Text;
    LinearLayout ll_topology;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topology);
        MainActivity.setStatusBarGradiant(this);
        //===========================================================
        btn_save_topology = findViewById(R.id.btn_save_topology);
        btn_generate_report = findViewById(R.id.btn_generate_report);
        //===========================================================
        btn_save_topology.setOnClickListener(this);
        btn_generate_report.setOnClickListener(this);
        //===========================================================
        ll_topology = findViewById(R.id.ll_topology);
        //TopologyGraphView myView = new TopologyGraphView(this);
        //ZoomView zoomView = new ZoomView(this);
        //zoomView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
       // myView.setLayoutParams(new LinearLayout.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels));
//        zoomView.requestLayout();
       // zoomView.addView(myView);
        //ll_topology.addView(zoomView);
        //ll_topology.addView(myView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_generate_report: {
                startActivity(new Intent(TopologyActivity.this, ReportActivity.class));
                break;
            }
            //===========================================================
            case R.id.btn_save_topology: {
                AlertDialog.Builder builder = new AlertDialog.Builder(TopologyActivity.this);
                builder.setTitle("File Name");

                // Set up the input
                final EditText input = new EditText(TopologyActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(TopologyActivity.this, "TopologyActivity successfully saved",
                                Toast.LENGTH_LONG).show();
                        m_Text = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                break;
            }
            //===========================================================

        }
    }
}
