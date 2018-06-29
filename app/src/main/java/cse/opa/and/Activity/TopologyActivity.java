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

import java.util.ArrayList;

import cse.opa.and.Classes.MiscellaneousMethods;
import cse.opa.and.Classes.SNMPManager;
import cse.opa.and.Classes.Topology;
import cse.opa.and.Edge;
import cse.opa.and.Node;
import cse.opa.and.R;
import cse.opa.and.TopologyGraphView;
import cse.opa.and.ZoomView;

public class TopologyActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_save_topology, btn_generate_report;
    String m_Text;
    LinearLayout ll_topology;
    ZoomView zv_zoomview;
    TopologyGraphView tgv_topology;
    public static ArrayList<Node> nodes;
    public static ArrayList<Edge> edges;
    public static String[] Nodename = new String[]{"Router0","Router1","Router2","Router3","Router4","Switch1","Switch2","Switch3","LocalPC","PC0","PC1","PC2","EthernetSwitch0"};

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
        zv_zoomview = findViewById(R.id.zv_zoomview);
        tgv_topology = findViewById(R.id.tgv_topology);
        //TopologyGraphView myView = new TopologyGraphView(this);
        //ZoomView zoomView = new ZoomView(this);
        //zoomView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
       // myView.setLayoutParams(new LinearLayout.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels));
//        zoomView.requestLayout();
       // zoomView.addView(myView);
        //ll_topology.addView(zoomView);
        //testgentopology();

        //tgv_topology = new TopologyGraphView(this);
        zv_zoomview.setSmoothZoomX(tgv_topology.getZoomCenterX());
        zv_zoomview.setSmoothZoomY(tgv_topology.getZoomCenterY());
        //zv_zoomview.addView(tgv_topology);
        //ll_topology.addView(zv_zoomview);

        //ll_topology.addView(myView);
    }
//    public void testgentopology(){
//        nodes = new ArrayList<Node>();
//        edges = new ArrayList<Edge>();
//
//        nodes.add(new Node("Router0"));
//        nodes.add(new Node("Router1"));
//        nodes.add(new Node("Router2"));
//        nodes.add(new Node("Router3"));
//        nodes.add(new Node("Router4"));
//        nodes.add(new Node("Switch1"));
//        nodes.add(new Node("Switch2"));
//        nodes.add(new Node("Switch3"));
//        nodes.add(new Node("LocalPC"));
//        nodes.add(new Node("PC0"));
//        nodes.add(new Node("PC1"));
//        nodes.add(new Node("PC2"));
//        nodes.add(new Node("EthernetSwitch"));
//        //==============
////        nodes.add(new Node("Switch4"));
////        nodes.add(new Node("PC3"));
//
//        //==============
//        Edge edge = new Edge(get_vertex_by_ID(nodes, "Router0"), get_vertex_by_ID(nodes, "LocalPC"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "Router0").addEdge(edge);
//        get_vertex_by_ID(nodes, "LocalPC").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "Router0"), get_vertex_by_ID(nodes, "PC0"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "Router0").addEdge(edge);
//        get_vertex_by_ID(nodes, "PC0").addEdge(edge);
//        //=========================
////        edge = new Edge(get_vertex_by_ID(nodes, "Router0"), get_vertex_by_ID(nodes, "Switch4"));
////        edges.add(edge);
////        get_vertex_by_ID(nodes, "Router0").addEdge(edge);
////        get_vertex_by_ID(nodes, "Switch4").addEdge(edge);
////
////        edge = new Edge(get_vertex_by_ID(nodes, "Switch4"), get_vertex_by_ID(nodes, "PC3"));
////        edges.add(edge);
////        get_vertex_by_ID(nodes, "Switch4").addEdge(edge);
////        get_vertex_by_ID(nodes, "PC3").addEdge(edge);
//        //=========================
//        edge = new Edge(get_vertex_by_ID(nodes, "Router0"), get_vertex_by_ID(nodes, "Router1"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "Router0").addEdge(edge);
//        get_vertex_by_ID(nodes, "Router1").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "Router1"), get_vertex_by_ID(nodes, "Router2"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "Router1").addEdge(edge);
//        get_vertex_by_ID(nodes, "Router2").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "Router2"), get_vertex_by_ID(nodes, "Switch1"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "Router2").addEdge(edge);
//        get_vertex_by_ID(nodes, "Switch1").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "Router2"), get_vertex_by_ID(nodes, "EthernetSwitch"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "Router2").addEdge(edge);
//        get_vertex_by_ID(nodes, "EthernetSwitch").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "Router3"), get_vertex_by_ID(nodes, "EthernetSwitch"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "Router3").addEdge(edge);
//        get_vertex_by_ID(nodes, "EthernetSwitch").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "Router4"), get_vertex_by_ID(nodes, "EthernetSwitch"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "Router4").addEdge(edge);
//        get_vertex_by_ID(nodes, "EthernetSwitch").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "Switch1"), get_vertex_by_ID(nodes, "Switch2"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "Switch1").addEdge(edge);
//        get_vertex_by_ID(nodes, "Switch2").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "Switch1"), get_vertex_by_ID(nodes, "Switch3"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "Switch1").addEdge(edge);
//        get_vertex_by_ID(nodes, "Switch3").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "Switch2"), get_vertex_by_ID(nodes, "PC1"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "Switch2").addEdge(edge);
//        get_vertex_by_ID(nodes, "PC1").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "Switch3"), get_vertex_by_ID(nodes, "PC2"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "Switch3").addEdge(edge);
//        get_vertex_by_ID(nodes, "PC2").addEdge(edge);
//
//    }
//    private static Node get_vertex_by_ID(ArrayList<Node> nodes, String ID) {
//        for (Node v : nodes) {
//            if (v.getId().equals(ID)) {
//                return v;
//            }
//        }
//        return null;
//    }
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
