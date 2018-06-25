package cse.opa.and;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;


public class Node
{
	public String name;
	public ArrayList<Edge> Adj;

	public Vector pos,vel,acc;
	public Vector posTodraw;
	public int color;
	public Node()
	{
		//pos= new Vector(300+(int)(Math.random()*300-150), 300+(int)(Math.random()*300-150));
		int MaxX = Resources.getSystem().getDisplayMetrics().widthPixels;
		int MaxY = Resources.getSystem().getDisplayMetrics().heightPixels;
		int MinX = 20;
		int MinY = 300;
		Random rand =  new Random();
		pos= new Vector(rand.nextInt(MaxX - MinX + 1) + MinX, rand.nextInt(MaxY - MinY + 1) + MinY);
		vel = new Vector(0,0);
		acc = new Vector(0,0);
		color = Color.rgb((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
	}
	
	
	public Vector calForce(ArrayList<Node> ll)
	{
		Vector result =new Vector();
		for(int i=0;i<ll.size();i++){
			if(ll.get(i)== this)continue;
			
			Vector temp = this.pos.sub(ll.get(i).pos);
			double temp2 = 10 / Math.pow(temp.Size(), 2);
			
			temp = temp.Unit().Mul(temp2);
			result=result.add(temp);
			
		}
		//System.out.println(result.Size());
		acc = this.acc.add(result); 
		//System.out.println("acc= "+acc.Size());
		return result;
	}
	public Vector calForceEdge( )
	{
		Vector result =new Vector();
		for(int i=0;i<Adj.size();i++){ 
			Vector temp = Adj.get(i). getForce(this); 
			result=result.add(temp);
			
		} 
		acc = this.acc.add(result); 
		//System.out.println("acc 2 = "+acc.Size());
		return result;
	}
	
	public void move()
	{
		vel= vel.add(acc);
		pos= pos.add(vel);
		
		vel=vel.Mul(0.99);
		//System.out.println(pos.getX() + " , " + pos.getY());
	}
	
	public void Draw(Paint p, Canvas canvas)
	{
		p.setColor(color);
		canvas.drawCircle((int)(posTodraw.getX()-20), (int)(posTodraw.getY()-20), 40, p);
		p.setColor(Color.BLACK);
		canvas.drawText(name,(int)(posTodraw.getX()-20), (int)(posTodraw.getY()-20),p);
	}
	
	public void setPosToDraw(Vector v)
	{
		posTodraw = v;
	}
	
	public Vector getCentroid(ArrayList<Node> ll)
	{
		double sx=0,sy=0;
		for(int i=0;i<ll.size();i++){
			 sx += ll.get(i).posTodraw.getX();
			 sy += ll.get(i).posTodraw.getY();
		}
		return new Vector(sx/ll.size(),sy/ll.size());
	}
}
