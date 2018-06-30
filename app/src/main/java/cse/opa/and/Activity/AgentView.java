package cse.opa.and.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import cse.opa.and.R;

public class AgentView extends View implements View.OnClickListener{
    private int type;
    private double X;
    private double Y;
    private String name;
    private int image;
    private Paint p;

    public AgentView(Context context,int type,double x,double y,String name,int image) {
        super(context);
        this.type = type;
        this.X =x;
        this.Y=y;
        this.name = name;
        this.image = image;
        this.p=new Paint(Paint.ANTI_ALIAS_FLAG);
        this.generateViewId();
    }

//    public AgentView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public AgentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public AgentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        p.setColor(Color.GREEN);
        canvas.drawCircle((int)(getPositionX()), (int)(getPositionY()), 10, p);
        Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.ic_router);
        canvas.drawBitmap(b, (float) getPositionX()-b.getWidth()/2, (float) getPositionY()-b.getHeight()/2, p);
        p.setColor(Color.BLACK);
        canvas.drawText(name,(int)(getPositionX()-b.getWidth()/2), (int)(getPositionY()+b.getHeight()/2 +15),p);
        //this.setLayoutParams(new FrameLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT ));

        //this.setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPositionX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getPositionY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIv_image() {
        return image;
    }

    public void setimage(int image) {
        this.image = image;
    }

    @Override
    public void onClick(View view) {
        Log.v("CLICKED","TOUCHED!!!!!");
        Toast.makeText(getContext(),this.name,Toast.LENGTH_SHORT).show();
    }
}
