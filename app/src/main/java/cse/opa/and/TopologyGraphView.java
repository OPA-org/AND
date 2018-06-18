package cse.opa.and;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
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
    public TopologyGraphView(Context context) {
        super(context);

        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(10);
        path = new Path();
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
        for (int i=-0;i<pointsStart.size();i++){
            // draw first vertex
            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.GREEN);
            canvas.drawCircle(pointsStart.get(i).x, pointsStart.get(i).y, 15, p);
            // draw the edge
            path.reset();
            path.moveTo(pointsStart.get(i).x, pointsStart.get(i).y);
            path.lineTo(pointsEnd.get(i).x, pointsEnd.get(i).y);
            p.setStyle(Paint.Style.STROKE);
            p.setColor(Color.CYAN);
            canvas.drawPath(path, p);
            // draw second vertex
            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.BLUE);
            canvas.drawCircle(pointsEnd.get(i).x, pointsEnd.get(i).y, 15, p);
        }
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
    }
}