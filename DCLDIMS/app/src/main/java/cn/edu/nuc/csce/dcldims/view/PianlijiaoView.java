package cn.edu.nuc.csce.dcldims.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/8/14 0014.
 */
public class PianlijiaoView extends View {

    private int VIEW_WIDTH;
    private double pianli_dushu=0;

    public PianlijiaoView(Context context){
        super(context);
    }
    public PianlijiaoView(Context context,AttributeSet attrs) {
        super(context, attrs);
         }
    public PianlijiaoView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
    VIEW_WIDTH=getWidth();
        Paint paint=new Paint();
//        paint.setColor(Color.RED);
//        paint.setShadowLayer(5, 10, 10, Color.rgb(180, 180, 180));
//        canvas.drawRect(0, 0, 60, 60, paint);

//        //绘制弧
//        paint.setColor(Color.YELLOW);
//        RectF rectf=new RectF(70, 0, 150, 70);
//        canvas.drawArc(rectf, 0, 60, true, paint);


        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        canvas.drawText("鹰嘴", VIEW_WIDTH/2-100, 30, paint);
        canvas.drawText("铅垂线", VIEW_WIDTH/2-100, 240, paint);

        //绘制圆形
        paint.setColor(Color.BLACK);
        canvas.drawCircle(VIEW_WIDTH / 2, 30, 20, paint);


        //绘制一条虚线
        DashPathEffect pathEffect = new DashPathEffect(new float[] { 26,6 }, 1);
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setFakeBoldText(true);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setPathEffect(pathEffect);
        Path path = new Path();
        path.moveTo(VIEW_WIDTH/2, 250);
        path.lineTo(VIEW_WIDTH/2, 30);
        canvas.drawPath(path, paint);

        //绘制一条线
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawLine(VIEW_WIDTH / 2, 30,(int) (220*Math.tan(pianli_dushu*Math.PI/180))+VIEW_WIDTH / 2, 250, paint);
        super.onDraw(canvas);
        System.out.println("1偏离角初始绘图");
    }

    @Override
    public void postInvalidate() {
        super.postInvalidate();
        System.out.println("1偏离角重绘");
    }

    public double getPianli_dushu() {
        return pianli_dushu;
    }

    public void setPianli_dushu(double pianli_dushu) {
        this.pianli_dushu = pianli_dushu;
    }
}
