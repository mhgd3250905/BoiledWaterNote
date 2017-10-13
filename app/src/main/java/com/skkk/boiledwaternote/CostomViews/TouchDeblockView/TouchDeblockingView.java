package com.skkk.boiledwaternote.CostomViews.TouchDeblockView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Utils.Utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 2016/10/14.
 */
/*
* 
* 描    述：滑动解锁模块
* 作    者：ksheng
* 时    间：2016/10/14$ 22:22$.
*/
public class TouchDeblockingView extends View {

    private Point[][] points = new Point[3][3];
    private boolean inited = false;
    private boolean isDraw = false;


    private ArrayList<Point> pointList = new ArrayList<Point>();
    private ArrayList<Integer> passList = new ArrayList<Integer>();


    private Bitmap bitmapPointNormal;
    private Bitmap bitmapPointPress;
    private Bitmap bitmapPointError;

    private float bitmapR;
    private float touchR;

    private float mouseX, mouseY;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint normalPaint = new Paint();
    Paint pressPaint = new Paint();
    Paint errorPaint = new Paint();

    private OnDrawFinishListener mOnDrawFinishListener;

    public interface OnDrawFinishListener {
        boolean OnDrawFinished(List<Integer> passList);
    }

    public TouchDeblockingView(Context context) {
        super(context);
    }

    public TouchDeblockingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchDeblockingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /* @描述 初始化工作 */
    private void init() {
        normalPaint.setColor(getResources().getColor(R.color.colorTouchNormalLine));
        normalPaint.setStrokeWidth(10);
        normalPaint.setAntiAlias(true);
        normalPaint.setDither(true);

        pressPaint.setColor(getResources().getColor(R.color.colorTouchPressLine));
        pressPaint.setStrokeWidth(10);
        pressPaint.setAntiAlias(true);
        pressPaint.setDither(true);

        errorPaint.setColor(getResources().getColor(R.color.colorTouchErrorLine));
        errorPaint.setStrokeWidth(10);
        errorPaint.setAntiAlias(true);
        errorPaint.setDither(true);


        bitmapPointNormal = ImageUtils.getBitmap(getContext(), R.drawable.ic_touch_normal);
        bitmapPointPress = ImageUtils.getBitmap(getContext(), R.drawable.ic_touch_correct);
        bitmapPointError = ImageUtils.getBitmap(getContext(), R.drawable.ic_touch_error);

        bitmapR = bitmapPointNormal.getHeight() / 2;


        int width = getWidth();
        int height = getHeight();
        int offset = Math.abs(width - height) / 2;
        int offsetX, offsetY;
        int space;
        if (width > height) {
            space = height / 4;
            offsetX = offset;
            offsetY = 0;
        } else {
            space = width / 4;
            offsetX = 0;
            offsetY = offset;
        }

        touchR = space / 2;


        points[0][0] = new Point(offsetX + space, offsetY + space);
        points[0][1] = new Point(offsetX + space * 2, offsetY + space);
        points[0][2] = new Point(offsetX + space * 3, offsetY + space);

        points[1][0] = new Point(offsetX + space, offsetY + space * 2);
        points[1][1] = new Point(offsetX + space * 2, offsetY + space * 2);
        points[1][2] = new Point(offsetX + space * 3, offsetY + space * 2);

        points[2][0] = new Point(offsetX + space, offsetY + space * 3);
        points[2][1] = new Point(offsetX + space * 2, offsetY + space * 3);
        points[2][2] = new Point(offsetX + space * 3, offsetY + space * 3);

        inited = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!inited) {
            init();
        }
        drawPoints(canvas);

        if (pointList.size() > 0) {

            Point a = pointList.get(0);

            for (int i = 1; i < pointList.size(); i++) {
                Point b = pointList.get(i);

                drawLine(canvas, a, b);

                a = b;
            }

            if (isDraw) {
                drawLine(canvas, a, new Point(mouseX, mouseY));
            }
        }
    }

    private void drawPoints(Canvas canvas) {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j].state == Point.STATE_NORMAL) {
                    //Normal
                    canvas.drawBitmap(bitmapPointNormal, points[i][j].x - bitmapR, points[i][j].y - bitmapR, paint);
//                    paint.setColor(Color.LTGRAY);
//                    canvas.drawCircle(points[i][j].x - bitmapR, points[i][j].y - bitmapR, touchR, paint);
                } else if (points[i][j].state == Point.STATE_PRESS) {
                    //Press
                    canvas.drawBitmap(bitmapPointPress, points[i][j].x - bitmapR, points[i][j].y - bitmapR, paint);
                } else {
                    //Error
                    canvas.drawBitmap(bitmapPointError, points[i][j].x - bitmapR, points[i][j].y - bitmapR, paint);
                }
            }
        }
    }


    private void drawLine(Canvas canvas, Point a, Point b) {
        if (a.state == Point.STATE_NORMAL) {
            canvas.drawLine(a.x, a.y, b.x, b.y, normalPaint);
        } else if (a.state == Point.STATE_PRESS) {
            canvas.drawLine(a.x, a.y, b.x, b.y, pressPaint);
        } else if (a.state == Point.STATE_ERROR) {
            canvas.drawLine(a.x, a.y, b.x, b.y, errorPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
        //LogUtils.Log("X: "+mouseX+" Y: "+mouseY);

        int[] ij;
        int i, j;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //重置状态
                resetPoints();
                //手指按下
                ij = getSelectedPoint();
                if (ij != null) {
                    isDraw = true;
                    i = ij[0];
                    j = ij[1];
                    points[i][j].state = Point.STATE_PRESS;
                    pointList.add(points[i][j]);
                    passList.add(i * 3 + j);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDraw) {
                    ij = getSelectedPoint();
                    if (ij != null) {
                        i = ij[0];
                        j = ij[1];
                        if (!pointList.contains(points[i][j])) {
                            points[i][j].state = Point.STATE_PRESS;
                            pointList.add(points[i][j]);
                            passList.add(i * 3 + j);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                boolean valid = false;
                if (mOnDrawFinishListener != null && isDraw) {
                    valid = mOnDrawFinishListener.OnDrawFinished(passList);
                }

                if (!valid) {
                    for (Point p : pointList) {
                        p.state = Point.STATE_ERROR;
                    }
                }

                isDraw = false;

                break;
        }
        //要求界面重新绘制
        this.postInvalidate();
        return true;
    }

    private int[] getSelectedPoint() {
        Point pMouse = new Point(mouseX, mouseY);
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j].distance(pMouse) < touchR) {
                    int[] result = new int[2];
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return null;
    }

    /* @描述 恢复point状态 */
    public void resetPoints() {
        pointList.clear();
        passList.clear();
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                points[i][j].state = Point.STATE_NORMAL;
            }
        }
        this.postInvalidate();
    }

    public void setOnDrawFinishedListener(OnDrawFinishListener listener) {
        mOnDrawFinishListener = listener;
    }

}
