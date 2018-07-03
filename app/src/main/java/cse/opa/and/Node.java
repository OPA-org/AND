package cse.opa.and;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class Node implements View.OnClickListener {

	private String id;
    private String name;
    private String type;
	private double x;
	private double y;
	
	private double dx;
	private double dy;
	private ArrayList<Edge> edges = new ArrayList<Edge>();

	public final static int VERTEX_SIZE = 30;
	private Context context;
    //private AgentView view;
    public Node(int id_no, String name, String type){
		this.id = Integer.toString(id_no);
		this.name = name;
        this.type = type;
    }
	
	public void addEdge(Edge e){
		edges.add(e);
	}
    public void resetForce(){
        setDx(0);
        setDy(0);
    }
	public String getId() {
		return id;
	}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getDx() {
		return dx;
	}
	
	public double getDy() {
		return dy;
	}
	
	public void setDx(double dx) {
		this.dx = dx;
	}
	
	public void setDy(double dy) {
		this.dy = dy;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}
//    public void setAgentView(){
//        view =new AgentView(this.context,2,this.getX(),this.getY(),this.getName(),123);
//
//    }
	public void Draw(Paint p, Canvas canvas)
	{
//        p.setColor(Color.GREEN);
//        canvas.drawCircle((int)(getX()), (int)(getY()), 10, p);
        Bitmap b=null;
        if (type.equals("Router")){
            b= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_switch1);
        }
        else if (type.equals("Switch")){
            b= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_router1);
        }else if (type.equals("Host")){
            b= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_pc1);
        }

        canvas.drawBitmap(b, (int) getX()-b.getWidth()/2, (int) getY()-b.getHeight()/2, p);
//		canvas.drawText(name,(int)(getX()), (int)(getY()),p);
        p.setColor(Color.WHITE);
        canvas.drawText(name,(int)(getX()-b.getWidth()/2), (int)(getY()+b.getHeight()/2 +15),p);
//		view =new AgentView(this.context,2,this.getX(),this.getY(),this.getName(),123);
//		view.draw(canvas);
        //this.view.setOnClickListener(this);

//		p.setColor(Color.GREEN);
//		canvas.drawCircle((int)(getX()), (int)(getY()), 10, p);
//		p.setColor(Color.BLACK);
//		canvas.drawText(name,(int)(getX()-10), (int)(getY()-10),p);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Node){
			return ((Node)obj).getId().equals(id);
		}
		return false;
	}

	@Override
	public void onClick(View view) {
		Toast.makeText(context,this.name,Toast.LENGTH_SHORT).show();
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public AgentView getAgentView() {
//        return view;
//    }
}
