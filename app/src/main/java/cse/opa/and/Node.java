package cse.opa.and;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class Node {

	private String id;
    private String name;

	private double x;
	private double y;
	
	private double dx;
	private double dy;
	private ArrayList<Edge> edges = new ArrayList<Edge>();

	public final static int VERTEX_SIZE = 20;
	
	public Node(int id_no, String name){
		this.id = Integer.toString(id_no);
		this.name = name;
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

	public void Draw(Paint p, Canvas canvas)
	{
		p.setColor(Color.GREEN);
		canvas.drawCircle((int)(getX()), (int)(getY()), 10, p);
		p.setColor(Color.BLACK);
		canvas.drawText(name,(int)(getX()-10), (int)(getY()-10),p);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Node){
			return ((Node)obj).getId().equals(id);
		}
		return false;
	}
	
}
