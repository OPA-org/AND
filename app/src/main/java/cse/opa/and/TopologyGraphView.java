package cse.opa.and;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;


public class TopologyGraphView extends View {

    private final Paint p;
    public String[] Nodename = new String[]{"Router0","Router1","Router2","Router3","Router4","Switch1","Switch2","Switch3","LocalPC","PC0","PC1","PC2","EthernetSwitch0"};

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
        ProcessInput();
    }
    //======================================================
    public TopologyGraphView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(attrs,context);
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(4);
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
        ProcessInput();
    }
    //======================================================
    public TopologyGraphView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs,context);
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(4);
        ProcessInput();
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
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();

        nodes.add(new Node("Router0"));
        nodes.add(new Node("Router1"));
        nodes.add(new Node("Router2"));
        nodes.add(new Node("Router3"));
        nodes.add(new Node("Router4"));
        nodes.add(new Node("Switch1"));
        nodes.add(new Node("Switch2"));
        nodes.add(new Node("Switch3"));
        nodes.add(new Node("LocalPC"));
        nodes.add(new Node("PC0"));
        nodes.add(new Node("PC1"));
        nodes.add(new Node("PC2"));
        nodes.add(new Node("EthernetSwitch"));

        Edge edge = new Edge(get_vertex_by_ID(nodes, "Router0"), get_vertex_by_ID(nodes, "LocalPC"));
        edges.add(edge);
        get_vertex_by_ID(nodes, "Router0").addEdge(edge);
        get_vertex_by_ID(nodes, "LocalPC").addEdge(edge);

        edge = new Edge(get_vertex_by_ID(nodes, "Router0"), get_vertex_by_ID(nodes, "PC0"));
        edges.add(edge);
        get_vertex_by_ID(nodes, "Router0").addEdge(edge);
        get_vertex_by_ID(nodes, "PC0").addEdge(edge);

        edge = new Edge(get_vertex_by_ID(nodes, "Router0"), get_vertex_by_ID(nodes, "Router1"));
        edges.add(edge);
        get_vertex_by_ID(nodes, "Router0").addEdge(edge);
        get_vertex_by_ID(nodes, "Router1").addEdge(edge);

        edge = new Edge(get_vertex_by_ID(nodes, "Router1"), get_vertex_by_ID(nodes, "Router2"));
        edges.add(edge);
        get_vertex_by_ID(nodes, "Router1").addEdge(edge);
        get_vertex_by_ID(nodes, "Router2").addEdge(edge);

        edge = new Edge(get_vertex_by_ID(nodes, "Router2"), get_vertex_by_ID(nodes, "Switch1"));
        edges.add(edge);
        get_vertex_by_ID(nodes, "Router2").addEdge(edge);
        get_vertex_by_ID(nodes, "Switch1").addEdge(edge);

        edge = new Edge(get_vertex_by_ID(nodes, "Router2"), get_vertex_by_ID(nodes, "EthernetSwitch"));
        edges.add(edge);
        get_vertex_by_ID(nodes, "Router2").addEdge(edge);
        get_vertex_by_ID(nodes, "EthernetSwitch").addEdge(edge);

        edge = new Edge(get_vertex_by_ID(nodes, "Router3"), get_vertex_by_ID(nodes, "EthernetSwitch"));
        edges.add(edge);
        get_vertex_by_ID(nodes, "Router3").addEdge(edge);
        get_vertex_by_ID(nodes, "EthernetSwitch").addEdge(edge);

        edge = new Edge(get_vertex_by_ID(nodes, "Router4"), get_vertex_by_ID(nodes, "EthernetSwitch"));
        edges.add(edge);
        get_vertex_by_ID(nodes, "Router4").addEdge(edge);
        get_vertex_by_ID(nodes, "EthernetSwitch").addEdge(edge);

        edge = new Edge(get_vertex_by_ID(nodes, "Switch1"), get_vertex_by_ID(nodes, "Switch2"));
        edges.add(edge);
        get_vertex_by_ID(nodes, "Switch1").addEdge(edge);
        get_vertex_by_ID(nodes, "Switch2").addEdge(edge);

        edge = new Edge(get_vertex_by_ID(nodes, "Switch1"), get_vertex_by_ID(nodes, "Switch3"));
        edges.add(edge);
        get_vertex_by_ID(nodes, "Switch1").addEdge(edge);
        get_vertex_by_ID(nodes, "Switch3").addEdge(edge);

        edge = new Edge(get_vertex_by_ID(nodes, "Switch2"), get_vertex_by_ID(nodes, "PC1"));
        edges.add(edge);
        get_vertex_by_ID(nodes, "Switch2").addEdge(edge);
        get_vertex_by_ID(nodes, "PC1").addEdge(edge);

        edge = new Edge(get_vertex_by_ID(nodes, "Switch3"), get_vertex_by_ID(nodes, "PC2"));
        edges.add(edge);
        get_vertex_by_ID(nodes, "Switch3").addEdge(edge);
        get_vertex_by_ID(nodes, "PC2").addEdge(edge);

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

    }

    public void Scale()
    {
        double XMin= Resources.getSystem().getDisplayMetrics().widthPixels;
        double YMin= Resources.getSystem().getDisplayMetrics().heightPixels;
        double XMax= Integer.MIN_VALUE+20;
        double YMax= Integer.MIN_VALUE+100;


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
            double tempx = nodeX*(2000.0 / length);
            double tempy = nodeY*(2000.0 / length);
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
        setMeasuredDimension(resolveSize(SCREEN_SIZE_WIDTH*2, heightMeasureSpec), resolveSize(SCREEN_SIZE_HEIGHT+SCREEN_SIZE_HEIGHT/2, heightMeasureSpec));

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
            System.out.println("Node: " + v.getId());
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

}