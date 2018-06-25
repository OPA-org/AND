package cse.opa.and;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Edge
{
	public Node a,b;
	public double length;
	
	public void Draw(Paint p, Canvas canvas)
	{
		p.setColor(Color.BLACK);
		canvas.drawLine ((int)(a.posTodraw.getX() ), (int)(a.posTodraw.getY() ),(int)(b.posTodraw.getX() ),(int)(b.posTodraw.getY()),p);
		double xx,yy;
		xx= (a.posTodraw.getX()  + b.posTodraw.getX() )/2;
		yy= (a.posTodraw.getY()  + b.posTodraw.getY() )/2;
		
		canvas.drawText(""+length,(int) xx,(int) yy,p);
	}
	
	public Vector getForce(Node toCal)
	{
		Vector dir ;
		if(a== toCal) {
			dir = b.pos.sub(a.pos);
		}
		else
		{
			dir = a.pos.sub(b.pos);
		}
		 double t=dir.Size()-10*length;
		double ss = Math.signum(t) *Math.log( Math.abs(t))*0.01;
		//System.out.println("ss=" + ss);
		dir = dir.Unit().Mul(ss);
		return dir;
	}

}
