package com.skkk.boiledwaternote.CostomViews.TouchDeblockView;

/**
 * Created by admin on 2016/10/14.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2016/10/14$ 22:27$.
*/
public class Point {
    public static int STATE_NORMAL=0;
    public static int STATE_PRESS=1;
    public static int STATE_ERROR=2;

    float x;
    float y;
    int state=STATE_NORMAL;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float distance(Point p){
        float distance=(float) Math.sqrt((x-p.x)*(x-p.x)+(y-p.y)*(y-p.y));
        return distance;
    }
}
