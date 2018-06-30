package cse.opa.and.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import cse.opa.and.AlgorithmParams;
import cse.opa.and.Classes.MiscellaneousMethods;
import cse.opa.and.Classes.SNMPManager;
import cse.opa.and.Classes.Topology;
import cse.opa.and.Edge;
import cse.opa.and.Node;
import cse.opa.and.PhisicEngine;
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
    static boolean finishedProcessing =false;
    ProgressDialog dialog;
    Thread processthread;
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
        //tgv_topology = findViewById(R.id.tgv_topology);
        //TopologyGraphView myView = new TopologyGraphView(this);
        //ZoomView zoomView = new ZoomView(this);
        //zoomView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
       // myView.setLayoutParams(new LinearLayout.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels));
//        zoomView.requestLayout();
       // zoomView.addView(myView);
        //ll_topology.addView(zoomView);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading topology..");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        processthread =new Thread(
            new Runnable(){
                @Override
                public void run() {
                    Log.v("Runnable", "Thread STARTED!");
                    testgentopology();
                    Scale();

                    while (!finishedProcessing) {
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.cancel();
                            tgv_topology = new TopologyGraphView(TopologyActivity.this);
//                            for(int j=0;j< nodes.size();j++)
//                            {
//                                nodes.get(j).setContext(getBaseContext());
//                                nodes.get(j).setAgentView();
//                                zv_zoomview.addView(nodes.get(j).getAgentView());
//                                nodes.get(j).getAgentView().setOnClickListener(TopologyActivity.this);
//                            }
                           zv_zoomview.setSmoothZoomX(tgv_topology.getZoomCenterX());
                            zv_zoomview.setSmoothZoomY(tgv_topology.getZoomCenterY());
                            zv_zoomview.addView(tgv_topology);
                            if(Thread.interrupted()){
                                return;
                            }
                        }
                    });
                }
            });
        processthread.start();



        //ll_topology.addView(zv_zoomview);

        //ll_topology.addView(myView);
    }
    public void testgentopology(){
//        nodes = new ArrayList<>();
//        edges = new ArrayList<>();

//        nodes.add(new Node(0,"Router0"));
//        nodes.add(new Node(1,"Router1"));
//        nodes.add(new Node(2,"Router2"));
//        nodes.add(new Node(3,"Router3"));
//        nodes.add(new Node(4,"Router4"));
//        nodes.add(new Node(5,"Switch1"));
//        nodes.add(new Node(6,"Switch2"));
//        nodes.add(new Node(7,"Switch3"));
//        nodes.add(new Node(8,"LocalPC"));
//        nodes.add(new Node(9,"PC0"));
//        nodes.add(new Node(10,"PC1"));
//        nodes.add(new Node(11,"PC2"));
//        nodes.add(new Node(12,"EthernetSwitch"));
//        //==============
////        nodes.add(new Node("Switch4"));
////        nodes.add(new Node("PC3"));
//
//        //==============
//        Edge edge = new Edge(get_vertex_by_ID(nodes, "0"), get_vertex_by_ID(nodes, "8"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "0").addEdge(edge);
//        get_vertex_by_ID(nodes, "8").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "0"), get_vertex_by_ID(nodes, "9"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "0").addEdge(edge);
//        get_vertex_by_ID(nodes, "9").addEdge(edge);
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
//        edge = new Edge(get_vertex_by_ID(nodes, "0"), get_vertex_by_ID(nodes, "1"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "0").addEdge(edge);
//        get_vertex_by_ID(nodes, "1").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "1"), get_vertex_by_ID(nodes, "2"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "1").addEdge(edge);
//        get_vertex_by_ID(nodes, "2").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "2"), get_vertex_by_ID(nodes, "5"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "2").addEdge(edge);
//        get_vertex_by_ID(nodes, "5").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "2"), get_vertex_by_ID(nodes, "12"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "2").addEdge(edge);
//        get_vertex_by_ID(nodes, "12").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "3"), get_vertex_by_ID(nodes, "12"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "3").addEdge(edge);
//        get_vertex_by_ID(nodes, "12").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "4"), get_vertex_by_ID(nodes, "12"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "4").addEdge(edge);
//        get_vertex_by_ID(nodes, "12").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "5"), get_vertex_by_ID(nodes, "6"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "5").addEdge(edge);
//        get_vertex_by_ID(nodes, "6").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "5"), get_vertex_by_ID(nodes, "7"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "5").addEdge(edge);
//        get_vertex_by_ID(nodes, "7").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "6"), get_vertex_by_ID(nodes, "10"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "6").addEdge(edge);
//        get_vertex_by_ID(nodes, "10").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "7"), get_vertex_by_ID(nodes, "11"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "7").addEdge(edge);
//        get_vertex_by_ID(nodes, "11").addEdge(edge);

        Topology topology = null;
           // Thread t =new Thread(new Runnable(){
               // @Override
                //public void run() {
                    try {

                        nodes = new ArrayList<>();
                        edges = new ArrayList<>();

                        Log.v("Process","entered");
                        topology = SNMPManager.generate_topology();
                        Log.v("Process","After Gen");

                        MiscellaneousMethods.create_Nodes_and_Edges(topology,nodes,edges);
                        Log.v("Process","After CREATE");
                        printNodes(nodes);

                        for(Node N: nodes){
                            Log.d("Debug","Node " +N.getName() +" :"+ N);
                        }

                        for(int i = 0 ;   i < edges.size() ; i++){
                            Log.d("Debug","Edge (" +edges.get(i).getA().getName() +":"+edges.get(i).getA()+" |||| "+ edges.get(i).getB().getName() +":"+edges.get(i).getB()+")");
                        }

                        AlgorithmParams.setNodes(nodes);
                        AlgorithmParams.setEdges(edges);

                        int Min_CrossEdgesCount = Integer.MAX_VALUE;
                        double best_hook = 0,best_gravity = 0;
                        for (double gravity = 10000.0; gravity < 300000.0; gravity += 25000.0) {
                            int c = -1;
                            for (double hookConst = 0.1; hookConst < 1.5; hookConst += 0.1) {
                                System.out.println("TESTING FOR:" + gravity + "," + hookConst);
                                AlgorithmParams.setGravityConst(gravity);
                                AlgorithmParams.setHookConst(hookConst);
                                PhisicEngine p = new PhisicEngine();
                                c = p.getCrossedEdgesCount();
                                System.out.println("CrossedEdgesCount: " + c);
                                if(c < Min_CrossEdgesCount){
                                    best_hook = hookConst;
                                    best_gravity = gravity;
                                    Min_CrossEdgesCount=c;
                                }
                                if(c == 0){
                                    break;
                                }
                            }
                            if (c == 0) {
                                break;
                            }
                        }
                        Log.v("Process","Before FinishedProcessing assignment");
                        finishedProcessing = true;
                        Log.v("Process","After FinishedProcessing assignment");
                        System.out.println("==========================");
                        printCoords(nodes);
                        System.out.println("==========================");
                        System.out.println("BestGravity: "+best_gravity);
                        System.out.println("BestHook: "+best_hook);
                        System.out.println("MinCrossEdgesCount: "+Min_CrossEdgesCount);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                //}


       // });
           // t.start();


    }
    private static void printCoords(ArrayList<Node> nodes) {
        for (Node v : nodes) {
            System.out.println("Node: " + v.getName());
            System.out.println("\tX: " + v.getX());
            System.out.println("\tY: " + v.getY());
            System.out.println("=====");
            System.out.println("\tEdges:");
            for (Edge e : v.getEdges()) {
                System.out.println(e.toString());
            }
            System.out.println("==================");
        }
    }
    private static void printNodes(ArrayList<Node> nodes) {
        for (Node v : nodes) {
            System.out.println("Node: " + v.getName());
            System.out.println("=====");
            System.out.println("\tEdges:");
            for (Edge e : v.getEdges()) {
                System.out.println(e.toString());
            }
            System.out.println("==================");
        }
    }
    private static Node get_vertex_by_ID(ArrayList<Node> nodes, String ID) {
        for (Node v : nodes) {
            if (v.getId().equals(ID)) {
                return v;
            }
        }
        return null;
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
//                default:for (int i=0;i<nodes.size();i++){
//                    if (v.getId()==nodes.get(i).getAgentView().getId()){
//                        Toast.makeText(this.getBaseContext(),nodes.get(i).getAgentView().getName(),Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
        }
    }
    public void Scale()
    {
        double XMin= this.ll_topology.getWidth();
        double YMin= this.ll_topology.getHeight();
        double XMax= Integer.MIN_VALUE+20;
        double YMax= Integer.MIN_VALUE+20;


        for(int j=0;j< nodes.size();j++)
        {
            if(nodes.get(j).getX() < XMin)
            {
                XMin=nodes.get(j).getX();
            }
            if(nodes.get(j).getY() < YMin)
            {
                YMin=nodes.get(j).getY();
            }
            if(nodes.get(j).getX() > XMax)
            {
                XMax=nodes.get(j).getX();
            }
            if(nodes.get(j).getY() > YMax)
            {
                YMax=nodes.get(j).getY();
            }
        }

        double length_x = XMax- XMin;
        double length_y = YMax- YMin;
        double length  = Math.max(length_x, length_y);
        for(int j=0;j< nodes.size();j++)
        {
            double nodeX = nodes.get(j).getX();
            double nodeY = nodes.get(j).getY();
            double tempx = nodeX*(500.0 / length);
            double tempy = nodeY*(500.0 / length);
            nodes.get(j).setX(tempx);
            nodes.get(j).setY(tempy);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        processthread.stop();
//        dialog.cancel();
    }

    @Override
    protected void onDestroy() {
        processthread.interrupt();
        dialog.cancel();
        super.onDestroy();
    }
}
