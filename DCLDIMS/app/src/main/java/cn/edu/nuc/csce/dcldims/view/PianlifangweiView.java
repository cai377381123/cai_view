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
public class PianlifangweiView extends View {
    private int VIEW_WIDTH;
    private float zhishiqi_x=10.605f;
    private float zhishiqi_y=10.605f;
    private int multiple=10;   //缩放倍数

    public PianlifangweiView(Context context){
        super(context);
    }
    public PianlifangweiView(Context context,AttributeSet attrs) {
        super(context, attrs);
    }
    public PianlifangweiView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        VIEW_WIDTH=getWidth();
        Paint paint=new Paint();

        //绘制注释圆点
        paint.setColor(Color.BLACK);
        canvas.drawCircle(20, 280, 15, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        canvas.drawText("吊钩", 45, 250, paint);
        canvas.drawText("鹰嘴", 45, 290, paint);
        canvas.drawText("起重臂", VIEW_WIDTH/2, 260, paint);
        canvas.drawText("司机", VIEW_WIDTH/2-40, 305, paint);
        canvas.drawText("司机右手", VIEW_WIDTH/2+150, 160, paint);

        //绘制注释正方形
        Paint paint_zhushi = new Paint();
        paint_zhushi.setColor(Color.RED);
        paint_zhushi.setShadowLayer(10, 10, 90, Color.rgb(180, 180, 180));
        canvas.drawRect(5, 218, 47, 260, paint_zhushi);

        //指示器

        paint_zhushi.setColor(Color.RED);
        paint_zhushi.setShadowLayer(10, 10, 90, Color.rgb(180, 180, 180));
        canvas.drawRect(zhishiqi_x-10.605f, zhishiqi_y-10.605f, zhishiqi_x+10.605f, zhishiqi_y+10.605f, paint_zhushi);

        // 绘制顶部三角形
        Path path = new Path();
        path.moveTo(VIEW_WIDTH/2, 0);// 此点为多边形的起点
        path.lineTo(VIEW_WIDTH/2-20, 30);
        path.lineTo(VIEW_WIDTH/2+20, 30);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, paint);
//右边三角形箭头
        Path path_right_sjx = new Path();
        path_right_sjx.moveTo(VIEW_WIDTH / 2 + 150, 150);// 此点为多边形的起点
        path_right_sjx.lineTo(VIEW_WIDTH / 2 + 120, 170);
        path_right_sjx.lineTo(VIEW_WIDTH / 2 + 120, 130);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path_right_sjx, paint);


//        //绘制弧
//        paint.setColor(Color.BLACK);
//        RectF rectf=new RectF(70, 0, 150, 70);
//        canvas.drawArc(rectf, 0, 90, true, paint);

        //绘制中心圆点
        paint.setColor(Color.BLACK);
        canvas.drawCircle(VIEW_WIDTH / 2, 150, 15, paint);


        //绘制朝上虚线
        DashPathEffect pathEffect = new DashPathEffect(new float[] { 26,6 }, 1);
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setFakeBoldText(true);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setPathEffect(pathEffect);
        Path path2 = new Path();
        path2.moveTo(VIEW_WIDTH / 2, 30);
        path2.lineTo(VIEW_WIDTH / 2, 150);
        canvas.drawPath(path2, paint);

        //绘制朝左虚线
        DashPathEffect pathEffect_left = new DashPathEffect(new float[] { 26,6 }, 1);
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setFakeBoldText(true);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setPathEffect(pathEffect_left);
        Path path_left = new Path();
        path_left.moveTo(VIEW_WIDTH / 2 - 150, 150);
        path_left.lineTo(VIEW_WIDTH / 2, 150);
        canvas.drawPath(path_left, paint);

        //绘制朝右虚线
        DashPathEffect pathEffect_right = new DashPathEffect(new float[] { 26,6 }, 1);
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setFakeBoldText(true);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setPathEffect(pathEffect_right);
        Path path_right = new Path();
        path_right.moveTo(VIEW_WIDTH / 2 + 120, 150);
        path_right.lineTo(VIEW_WIDTH / 2, 150);
        canvas.drawPath(path_right, paint);

        //绘制朝下实线
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(VIEW_WIDTH/2, 150, VIEW_WIDTH/2, 280, paint);
        super.onDraw(canvas);
    }

    public float getZhishiqi_x() {
        return zhishiqi_x;
    }

    public void setZhishiqi_x(float zhishiqi_x) {
        this.zhishiqi_x = zhishiqi_x/multiple+VIEW_WIDTH / 2;
        System.out.println("新获取的值"+zhishiqi_x);
    }

    public float getZhishiqi_y() {
        return zhishiqi_y;
    }

    public void setZhishiqi_y(float zhishiqi_y) {
        this.zhishiqi_y = 150-zhishiqi_y/multiple;
        System.out.println("新获取的值"+zhishiqi_y);
    }

    @Override
    public void postInvalidate() {
        super.postInvalidate();
        System.out.println("1偏离方位重绘");
    }
}
