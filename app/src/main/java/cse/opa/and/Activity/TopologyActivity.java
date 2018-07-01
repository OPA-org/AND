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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
    boolean details_list_visible= false,device_details_visible=false;
    ProgressDialog dialog;
    Thread processthread;
    LinearLayout ll_devices_details,ll_device_details;
    ListView lv_devices;
    ImageView iv_devices_details_list,iv_back;
    Animation anim_scale_up,anim_scale_down;
    TextView tv_device_details;
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
        ll_devices_details = findViewById(R.id.ll_devices_details);
        ll_device_details = findViewById(R.id.ll_device_details);
        lv_devices = findViewById(R.id.lv_devices);
        iv_devices_details_list = findViewById(R.id.iv_devices_details_list);
        iv_back = findViewById(R.id.iv_back);
        tv_device_details = findViewById(R.id.tv_device_details);
        anim_scale_up = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        anim_scale_down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        //tgv_topology = findViewById(R.id.tgv_topology);
        //TopologyGraphView myView = new TopologyGraphView(this);
        //ZoomView zoomView = new ZoomView(this);
        //zoomView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
       // myView.setLayoutParams(new LinearLayout.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels));
//        zoomView.requestLayout();
       // zoomView.addView(myView);
        //ll_topology.addView(zoomView);
//        ll_devices_details.setVisibility(View.VISIBLE);
//        ll_devices_details.setTranslationY((ll_devices_details.getHeight()));
        String [] list_data={"Router0",
                "Router1",
                "Router2",
                "Router3",
                "Switch0",
                "Switch1",
                "PC 1"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list_data);
        lv_devices.setAdapter(arrayAdapter);
        lv_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                device_details_visible = true;
                details_list_visible = false;
                ll_devices_details.setTranslationX(0);
                ll_devices_details.animate().setDuration(500).translationXBy(-(ll_devices_details.getWidth()+30)).start();
                ll_device_details.setTranslationX((ll_device_details.getWidth()+30));
                ll_device_details.setVisibility(View.VISIBLE);
                ll_device_details.animate().setDuration(500).translationXBy(-(ll_device_details.getWidth()+30)).start();
                tv_device_details.setText(((TextView)lv_devices.getChildAt(position)).getText().toString()+ " Details are here\nIP : 192.168.10.2 \nSubnet : 255.255.255.0\nManufacturer : Cisco");
                tv_device_details.requestFocus();
            }
        });
        iv_back.setOnClickListener(this);
        iv_devices_details_list.setOnClickListener(this);
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
//                    testgentopology();
//                    Scale();
//
//                    while (!finishedProcessing) {
//                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.cancel();
                           // tgv_topology = new TopologyGraphView(TopologyActivity.this);
//                            for(int j=0;j< nodes.size();j++)
//                            {
//                                nodes.get(j).setContext(getBaseContext());
//                                nodes.get(j).setAgentView();
//                                zv_zoomview.addView(nodes.get(j).getAgentView());
//                                nodes.get(j).getAgentView().setOnClickListener(TopologyActivity.this);
//                            }
//                           zv_zoomview.setSmoothZoomX(tgv_topology.getZoomCenterX());
//                           zv_zoomview.setSmoothZoomY(tgv_topology.getZoomCenterY());
//                           zv_zoomview.addView(tgv_topology);
                           iv_devices_details_list.startAnimation(anim_scale_up);
                           iv_devices_details_list.setVisibility(View.VISIBLE);
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
//
//        nodes.add(new Node(0,"Router0", "Router"));
//        nodes.add(new Node(1,"Router1", "Router"));
//        nodes.add(new Node(2,"Router2", "Router"));
//        nodes.add(new Node(3,"Router3", "Router"));
//        nodes.add(new Node(4,"Router4", "Router"));
//        nodes.add(new Node(5,"Switch1", "Switch"));
//        nodes.add(new Node(6,"Switch2", "Switch"));
//        nodes.add(new Node(7,"Switch3", "Switch"));
//        nodes.add(new Node(8,"LocalPC", "Host"));
//        nodes.add(new Node(9,"PC0", "Host"));
//        nodes.add(new Node(10,"PC1", "Host"));
//        nodes.add(new Node(11,"PC2", "Host"));
//        nodes.add(new Node(12,"EthernetSwitch", "Switch"));
//        nodes.add(new Node(13,"PC Alone", "Host"));
//
//        Edge edge = new Edge(get_vertex_by_ID(nodes, "0"), get_vertex_by_ID(nodes, "8"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "0").addEdge(edge);
//        get_vertex_by_ID(nodes, "8").addEdge(edge);
//
//        edge = new Edge(get_vertex_by_ID(nodes, "0"), get_vertex_by_ID(nodes, "9"));
//        edges.add(edge);
//        get_vertex_by_ID(nodes, "0").addEdge(edge);
//        get_vertex_by_ID(nodes, "9").addEdge(edge);
//
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
            case R.id.iv_back:{
                ll_devices_details.setVisibility(View.INVISIBLE);
                ll_device_details.setVisibility(View.INVISIBLE);
                this.finish();
                break;
            }
            //===========================================================
            case R.id.iv_devices_details_list:{
                iv_devices_details_list.startAnimation(anim_scale_down);
                iv_devices_details_list.setVisibility(View.INVISIBLE);
                ll_devices_details.setVisibility(View.VISIBLE);
                ll_devices_details.setTranslationY(ll_devices_details.getHeight());
                ll_devices_details.animate().setDuration(500).translationYBy(-ll_devices_details.getHeight()).start();
                details_list_visible = true;
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
       // super.onBackPressed();
//        processthread.stop();
//        dialog.cancel();
        if (details_list_visible == true){
            //hide devices details Slide Down
            ll_devices_details.setTranslationY(0);
            ll_devices_details.animate().setDuration(500).translationYBy(ll_devices_details.getHeight()).start();
            details_list_visible =false;
            iv_devices_details_list.startAnimation(anim_scale_up);
            iv_devices_details_list.setVisibility(View.VISIBLE);
        }
        else if(device_details_visible == true){
            ll_device_details.setTranslationX(0);
            ll_device_details.animate().translationXBy((ll_device_details.getWidth()+30)).start();
            ll_devices_details.setTranslationX(-(ll_devices_details.getWidth()+30));
            ll_devices_details.animate().translationXBy((ll_devices_details.getWidth()+30)).start();
            device_details_visible= false;
            details_list_visible =true;
        }
    }

    @Override
    protected void onDestroy() {
        processthread.interrupt();
        dialog.cancel();
        super.onDestroy();
    }
}
