package cse.opa.and;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Edge {
	private Node a;
	private Node b;
	
	public Edge(Node a, Node b){
		this.a = a;
		this.b = b;
	}

	public void setA(Node a) {
		this.a = a;
	}
	
	public void setB(Node b) {
		this.b = b;
	}
	
	public Node getA() {
		return a;
	}
	
	public Node getB() {
		return b;
	}
	public void Draw(Paint p, Canvas canvas)
	{
		p.setColor(Color.BLACK);
		canvas.drawLine ((int)(a.getX() ), (int)(a.getY() ),(int)(b.getX() ),(int)(b.getY()),p);
		double xx,yy;
		xx= (a.getX()  + b.getX() )/2;
		yy= (a.getY()  + b.getY() )/2;

		//canvas.drawText(""+length,(int) xx,(int) yy,p);
	}
    @Override
    public String toString() {
        return "\t\tNode A: "+a.getName()+"\n\t\tNode B: "+b.getName(); //To change body of generated methods, choose Tools | Templates.
    }
        
        
}
