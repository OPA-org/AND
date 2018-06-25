package cse.opa.and;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by gaafar62 on 18/06/2018.
 */


public class TopologyGraphView extends View {

    private final Paint p;
    private final Path path;
    private final ArrayList<Point> pointsStart = new ArrayList<>();
    private final ArrayList<Point> pointsEnd = new ArrayList<>();
    public int [][] inputTable =new int[][]
            { 	{0,1,1,1,1} ,
                    {1,0,5,0,0} ,
                    {1,5,0,0,1} ,
                    {1,0,0,0,1} ,
                    {1,0,1,1,0} ,
            };
    public String[] Nodename = new String[]{"A","B","C","D","E"};

    ArrayList<Node> all;
    ArrayList<Edge> edges;

    public TopologyGraphView(Context context) {
        super(context);

        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(10);
        path = new Path();



        ProcessInput();
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(true)
                {
                    try
                    {
                        Thread.sleep(50);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    for(int j=0;j< Nodename.length;j++)
                    {
                        all.get(j).acc=new Vector();
                        all.get(j).calForce(all);
                        all.get(j). calForceEdge( );
                        //all.get(j).move();
                    }
                    //scale
                    Scale();
                    //centroid
                    Vector centoid = all.get(0).getCentroid(all);
                    Vector temp = (new Vector(300,300)).sub(centoid);
                    for(int j=0;j< Nodename.length;j++)
                    {
                        all.get(j).posTodraw=all.get(j).posTodraw.add(temp) ;
                    }

                }
            }
        });
        t.start();

    }

    public void GenerateGraph()
    {
        int n= 5;
        inputTable = new int[n][n];
        Nodename = new String[n];
        for(int i=0;i< Nodename.length;i++)
        {
            for(int j=i+1;j< Nodename.length;j++)
            {
                if(Math.random() > 0.6)
                {
                    inputTable[i][j] = (int)(Math.random() * 20);
                    inputTable[j][i] =inputTable[i][j] ;
                }
            }
        }
        for(int i=0;i< Nodename.length;i++)
        {
            Nodename[i]= "" + (char)(65+i);
        }
    }

    public void GenerateGraph2()
    {
        int n= 5;
        inputTable = new int[n][n];
        Nodename = new String[n];
        for(int i=0;i< Nodename.length;i++)
        {
            for(int j=i+1;j< Nodename.length;j++)
            {
                if(Math.random()/Math.log(j-i)*Math.log(2)*2 > 0.5)
                {
                    inputTable[i][j] = (int)(Math.random() * 7*(j-i));
                    inputTable[j][i] =inputTable[i][j] ;
                }
            }
        }
        for(int i=0;i< Nodename.length;i++)
        {
            Nodename[i]= "" + (char)(65+i);
        }
    }

    public void ProcessInput()
    {
        GenerateGraph();
        //GenerateGraph2();
        all = new ArrayList<Node>();
        edges =new ArrayList<Edge>();

        for(int i=0;i< Nodename.length;i++)
        {
            all.add(new Node());
            all.get(i).name = Nodename[i];
            all.get(i).Adj= new ArrayList<Edge>();
        }

        for(int i=0;i< Nodename.length;i++)
        {
            for(int j=i+1;j< Nodename.length;j++)
            {
                if(inputTable[i][j] != 0)
                {
                    Edge e = new Edge();
                    e.a = all.get(i);
                    e.b = all.get(j);
                    e.length = inputTable[i][j] ;
                    edges.add(e);
                    all.get(i).Adj.add(e);
                    all.get(j).Adj.add(e);
                }
            }
        }
    }
    public void Scale()
    {
        double XMin= Resources.getSystem().getDisplayMetrics().widthPixels;
        double YMin= Resources.getSystem().getDisplayMetrics().heightPixels;
        double XMax= Integer.MIN_VALUE+20;
        double YMax= Integer.MIN_VALUE+100;


        for(int j=0;j< Nodename.length;j++)
        {
            if(all.get(j).pos.getX() < XMin)
            {
                XMin=all.get(j).pos.getX();
            }
            if(all.get(j).pos.getY() < YMin)
            {
                YMin=all.get(j).pos.getY();
            }
            if(all.get(j).pos.getX() > XMax)
            {
                XMax=all.get(j).pos.getX();
            }
            if(all.get(j).pos.getY() > YMax)
            {
                YMax=all.get(j).pos.getY();
            }
        }

        double length_x = XMax- XMin;
        double length_y = YMax- YMin;
        double length  = Math.max(length_x, length_y);
        for(int j=0;j< Nodename.length;j++)
        {
            Vector vv = all.get(j).pos;
            vv= vv.Mul(400.0 / length);
            all.get(j).setPosToDraw(vv);
        }

    }
    public void AddPath(int x1,int y1,int x2,int y2){
        pointsStart.add(new Point(x1, y1));
        pointsEnd.add(new Point(x2, y2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw first vertex
//        p.setStyle(Paint.Style.FILL);
//        p.setColor(Color.GREEN);



//        for (int i=-0;i<pointsStart.size();i++){
//            // draw first vertex
//            p.setStyle(Paint.Style.FILL);
//            p.setColor(Color.GREEN);
//            canvas.drawCircle(pointsStart.get(i).x, pointsStart.get(i).y, 15, p);
//            // draw the edge
//            path.reset();
//            path.moveTo(pointsStart.get(i).x, pointsStart.get(i).y);
//            path.lineTo(pointsEnd.get(i).x, pointsEnd.get(i).y);
//            p.setStyle(Paint.Style.STROKE);
//            p.setColor(Color.CYAN);
//            canvas.drawPath(path, p);
//            // draw second vertex
//            p.setStyle(Paint.Style.FILL);
//            p.setColor(Color.BLUE);
//            canvas.drawCircle(pointsEnd.get(i).x, pointsEnd.get(i).y, 15, p);
//        }



        //        canvas.drawCircle(point1.x, point1.y, 15, p);
//
//        // draw the edge
//        path.reset();
//        path.moveTo(point1.x, point1.y);
//        path.lineTo(point2.x, point2.y);
//        p.setStyle(Paint.Style.STROKE);
//        p.setColor(Color.CYAN);
//        canvas.drawPath(path, p);
//
//        // draw second vertex
//        p.setStyle(Paint.Style.FILL);
//        p.setColor(Color.BLUE);
//        canvas.drawCircle(point2.x, point2.y, 15, p);



        for(int j=0;j< Nodename.length;j++)
        {
            all.get(j).Draw(p,canvas);
        }
        for(int j=0;j< edges.size();j++)
        {
            edges.get(j).Draw(p,canvas);
        }
    }
}