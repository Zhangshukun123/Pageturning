package c_study.shd.com.pageturning;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class PagerTurningView extends View {

    private int mWidth;
    private int mHeight;


    private Point a, f, g, e, h, c, j, b, k, d, i;


    private Paint pointPaint;
    private Paint bgPaint;


    private Paint pathAPaint;//绘制A区域画笔
    private Path pathA;
    private Bitmap bitmap;//缓存bitmap
    private Canvas bitmapCanvas;
    private Paint pathCPaint;
    private Path pathC;
    private Paint pathBPaint;
    private Path pathB;


    public static final String STYLE_TOP_RIGHT = "STYLE_TOP_RIGHT";//f点在右上角
    public static final String STYLE_LOWER_RIGHT = "STYLE_LOWER_RIGHT";//f点在右下角




    public PagerTurningView(Context context) {
        this(context, null);
    }

    public PagerTurningView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerTurningView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();

    }

    private void init() {

//        mWidth = 600;
//        mHeight = 1000;

        a = new Point();
        f = new Point();
        g = new Point();
        e = new Point();
        h = new Point();
        c = new Point();
        j = new Point();
        b = new Point();
        k = new Point();
        d = new Point();
        i = new Point();


        pointPaint = new Paint();
        pointPaint.setColor(Color.RED);
        pointPaint.setTextSize(30);

        bgPaint = new Paint();
        bgPaint.setColor(Color.WHITE);


        pathAPaint = new Paint();
        pathAPaint.setColor(Color.GREEN);
        pathAPaint.setAntiAlias(true);//设置抗锯齿

        pathA = new Path();


        pathCPaint = new Paint();
        pathCPaint.setColor(Color.YELLOW);
        pathCPaint.setAntiAlias(true);//设置抗锯齿
        pathCPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        pathC = new Path();



        pathBPaint = new Paint();
        pathBPaint.setColor(Color.LTGRAY);
        pathBPaint.setAntiAlias(true);//设置抗锯齿
        pathBPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        pathB = new Path();




    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measureSize(1000, heightMeasureSpec);
        int width = measureSize(600, widthMeasureSpec);
        setMeasuredDimension(width, height);

        mWidth = width;
        mHeight = height;
        f.x = width;
        f.y = height;
        calcPointsXY(a,f);//将初始化计算放在这


        a.x = -1;
        a.y = -1;

    }

    private int measureSize(int defaultSize,int measureSpec) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }




    /**
     * 获取f点在右上角的pathA
     * @return
     */
    private Path getPathAFromTopRight(){
        pathA.reset();
        pathA.lineTo(c.x,c.y);//移动到c点
        pathA.quadTo(e.x,e.y,b.x,b.y);//从c到b画贝塞尔曲线，控制点为e
        pathA.lineTo(a.x,a.y);//移动到a点
        pathA.lineTo(k.x,k.y);//移动到k点
        pathA.quadTo(h.x,h.y,j.x,j.y);//从k到j画贝塞尔曲线，控制点为h
        pathA.lineTo(mWidth,mHeight);//移动到右下角
        pathA.lineTo(0, mHeight);//移动到左下角
        pathA.close();
        return pathA;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        mHeight = h;
//        mWidth = w;
    }


    /**
     * 绘制区域B
     * @return
     */
    private Path getPathB(){
        pathB.reset();
        pathB.lineTo(0, mHeight);//移动到左下角
        pathB.lineTo(mWidth,mHeight);//移动到右下角
        pathB.lineTo(mWidth,0);//移动到右上角
        pathB.close();//闭合区域
        return pathB;
    }




    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        bitmap = Bitmap.createBitmap((int) mWidth, (int) mHeight, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        if(a.x==-1 && a.y==-1){
            bitmapCanvas.drawPath(getPathDefault(),pathAPaint);
        }else {
            if(f.x==mWidth && f.y==0){
                bitmapCanvas.drawPath(getPathAFromTopRight(),pathAPaint);
            }else if(f.x==mWidth && f.y==mHeight){
                bitmapCanvas.drawPath(getPathAFromLowerRight(),pathAPaint);
            }

            bitmapCanvas.drawPath(getPathC(),pathCPaint);
            bitmapCanvas.drawPath(getPathB(),pathBPaint);
        }
        canvas.drawBitmap(bitmap,0,0,null);







    }
    /**
     * 设置触摸点
     * @param x
     * @param y
     * @param style
     */
    public void setTouchPoint(float x, float y, String style){
        switch (style){
            case STYLE_TOP_RIGHT:
                f.x = mWidth;
                f.y = 0;
                break;
            case STYLE_LOWER_RIGHT:
                f.x = mWidth;
                f.y = mHeight;
                break;
            default:
                break;
        }
        a.x = x;
        a.y = y;
        calcPointsXY(a,f);
        postInvalidate();
    }

    /**
     * 回到默认状态
     */
    public void setDefaultPath(){
        a.x = -1;
        a.y = -1;
        postInvalidate();
    }

    /**
     * 绘制默认的界面
     * @return
     */
    private Path getPathDefault(){
        pathA.reset();
        pathA.lineTo(0, mHeight);
        pathA.lineTo(mWidth,mHeight);
        pathA.lineTo(mWidth,0);
        pathA.close();
        return pathA;
    }

    public float getViewWidth(){
        return mWidth;
    }

    public float getViewHeight(){
        return mHeight;
    }


    /**
     * 绘制区域C
     * @return
     */
    private Path getPathC(){
        pathC.reset();
        pathC.moveTo(i.x,i.y);//移动到i点
        pathC.lineTo(d.x,d.y);//移动到d点
        pathC.lineTo(b.x,b.y);//移动到b点
        pathC.lineTo(a.x,a.y);//移动到a点
        pathC.lineTo(k.x,k.y);//移动到k点
        pathC.close();//闭合区域
        return pathC;
    }

    /**
     * 获取f点在右下角的pathA
     *
     * @return
     */
    private Path getPathAFromLowerRight() {
        pathA.reset();
        pathA.lineTo(0, mHeight);//移动到左下角
        pathA.lineTo(c.x, c.y);//移动到c点
        pathA.quadTo(e.x, e.y, b.x, b.y);//从c到b画贝塞尔曲线，控制点为e
        pathA.lineTo(a.x, a.y);//移动到a点
        pathA.lineTo(k.x, k.y);//移动到k点
        pathA.quadTo(h.x, h.y, j.x, j.y);//从k到j画贝塞尔曲线，控制点为h
        pathA.lineTo(mWidth, 0);//移动到右上角
        pathA.close();//闭合区域
        return pathA;
    }


    private void calcPointsXY(Point a, Point f) {
        g.x = (a.x + f.x) / 2;
        g.y = (a.y + f.y) / 2;

        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
        e.y = f.y;

        h.x = f.x;
        h.y = g.y - (f.x - g.x) * (f.x - g.x) / (f.y - g.y);

        c.x = e.x - (f.x - e.x) / 2;
        c.y = f.y;

        j.x = f.x;
        j.y = h.y - (f.y - h.y) / 2;

        b = getIntersectionPoint(a, e, c, j);
        k = getIntersectionPoint(a, h, c, j);

        d.x = (c.x + 2 * e.x + b.x) / 4;
        d.y = (2 * e.y + c.y + b.y) / 4;

        i.x = (j.x + 2 * h.x + k.x) / 4;
        i.y = (2 * h.y + j.y + k.y) / 4;
    }

    private Point getIntersectionPoint(Point lineOne_My_pointOne, Point lineOne_My_pointTwo, Point lineTwo_My_pointOne, Point lineTwo_My_pointTwo) {
        float x1, y1, x2, y2, x3, y3, x4, y4;
        x1 = lineOne_My_pointOne.x;
        y1 = lineOne_My_pointOne.y;
        x2 = lineOne_My_pointTwo.x;
        y2 = lineOne_My_pointTwo.y;
        x3 = lineTwo_My_pointOne.x;
        y3 = lineTwo_My_pointOne.y;
        x4 = lineTwo_My_pointTwo.x;
        y4 = lineTwo_My_pointTwo.y;

        float pointX = ((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1))
                / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));
        float pointY = ((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4))
                / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4));


        return new Point(pointX, pointY);

    }
}
