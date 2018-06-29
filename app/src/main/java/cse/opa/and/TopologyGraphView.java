package cse.opa.and;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import cse.opa.and.Activity.TopologyActivity;
import cse.opa.and.Classes.MiscellaneousMethods;
import cse.opa.and.Classes.SNMPManager;
import cse.opa.and.Classes.Topology;


public class TopologyGraphView extends View {

    private final Paint p;
    public String[] Nodename = new String[]{"Router0","Router1","Router2","Router3","Router4","Switch1","Switch2","Switch3"/*,"Switch4"*/,"LocalPC","PC0","PC1","PC2"/*,"PC3"*/,"EthernetSwitch0"};

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    ArrayList<Node> nodes;
    ArrayList<Edge> edges;

    private int mSquareWidth,mSquareHeight;
    private int SCREEN_SIZE_WIDTH ;
    private int SCREEN_SIZE_HEIGHT ;
    private float zoomCenterX = 0 , zoomCenterY = 0;

    public TopologyGraphView(Context context) {
        super(context);

        init(null,context);
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(4);
        this.nodes = TopologyActivity.nodes;
        this.edges = TopologyActivity.edges;
        Nodename = TopologyActivity.Nodename;

        ProcessInput();
        Scale();
        set_AverageXY();
    }
    //======================================================
    public TopologyGraphView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(attrs,context);
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(4);
        this.nodes = TopologyActivity.nodes;
        this.edges = TopologyActivity.edges;
        Nodename = TopologyActivity.Nodename;
        ProcessInput();
        Scale();
        set_AverageXY();
    }
    //======================================================
    public TopologyGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs,context);
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(4);
        this.nodes = TopologyActivity.nodes;
        this.edges = TopologyActivity.edges;
        Nodename = TopologyActivity.Nodename;
        ProcessInput();
        Scale();
        set_AverageXY();
    }
    //======================================================
    public TopologyGraphView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs,context);
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(4);
        this.nodes = TopologyActivity.nodes;
        this.edges = TopologyActivity.edges;
        Nodename = TopologyActivity.Nodename;
        ProcessInput();
        Scale();
        set_AverageXY();
    }
    private void init(@Nullable AttributeSet set, Context context) {
        if (set == null)
            return;
        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.TopologyGraphView);
        SCREEN_SIZE_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
        SCREEN_SIZE_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
        mSquareWidth = ta.getDimensionPixelSize(R.styleable.TopologyGraphView_square_size, SCREEN_SIZE_WIDTH);
        mSquareHeight = ta.getDimensionPixelSize(R.styleable.TopologyGraphView_square_size, SCREEN_SIZE_HEIGHT);
        ta.recycle();
    }

    public void ProcessInput()
    {
//        nodes = new ArrayList<Node>();
//        edges = new ArrayList<Edge>();

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

        try {
            new  AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                }
                @Override
                protected Void doInBackground(Void... params) {
                    try {

                        //nodes = new ArrayList<Node>();
                        //edges = new ArrayList<Edge>();

                        //Topology topology = SNMPManager.generate_topology();
                        //MiscellaneousMethods.create_Nodes_and_Edges(topology,nodes,edges);
                        printNodes(nodes);
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

                        System.out.println("==========================");
                        printCoords(nodes);
                        System.out.println("==========================");
                        System.out.println("BestGravity: "+best_gravity);
                        System.out.println("BestHook: "+best_hook);
                        System.out.println("MinCrossEdgesCount: "+Min_CrossEdgesCount);
                    } catch (Exception e) {
                        System.out.println("Exception "+ e.getMessage());
                        return null;
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void Void) {

                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public void Scale()
    {
        double XMin= getRootView().getWidth();
        double YMin= getRootView().getHeight();
        double XMax= Integer.MIN_VALUE+20;
        double YMax= Integer.MIN_VALUE+20;


        for(int j=0;j< Nodename.length;j++)
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
        for(int j=0;j< Nodename.length;j++)
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
    protected void onDraw(Canvas canvas) {
        for(int j=0;j< Nodename.length;j++)
        {
            nodes.get(j).Draw(p,canvas);
        }
        for(int j=0;j< edges.size();j++)
        {
            edges.get(j).Draw(p,canvas);
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //setMeasuredDimension(resolveSize(SCREEN_SIZE_WIDTH, heightMeasureSpec), resolveSize(SCREEN_SIZE_HEIGHT, heightMeasureSpec));
        //getRootView().getParent().

    }

    private static Node get_vertex_by_ID(ArrayList<Node> nodes, String ID) {
        for (Node v : nodes) {
            if (v.getId().equals(ID)) {
                return v;
            }
        }
        return null;
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

    public float getZoomCenterX() {
        return zoomCenterX;
    }

    public void setZoomCenterX(float zoomCenterX) {
        this.zoomCenterX = zoomCenterX;
    }

    public float getZoomCenterY() {
        return zoomCenterY;
    }

    public void setZoomCenterY(float zoomCenterY) {
        this.zoomCenterY = zoomCenterY;
    }

    private void set_AverageXY(){
        float sumX = 0;
        float sumY = 0;
        for(int i = 0 ; i < nodes.size() ; i++){
            sumX += nodes.get(i).getX();
            sumY += nodes.get(i).getY();
        }
        zoomCenterX = sumX/nodes.size();
        zoomCenterY = sumY/nodes.size();
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
}