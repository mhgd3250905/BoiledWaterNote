package com.skkk.boiledwaternote.Utils.Utils;

import android.animation.ValueAnimator;

/**
 * 创建于 2017/6/25
 * 作者 admin
 */
/*
* 
* 描    述：动画工具类
* 作    者：ksheng
* 时    间：2017/6/25$ 17:29$.
*/
public class AnimatorUtils {

    /**
     * 在指定时间内改变透明度
     * @param start 起始透明度
     * @param end   结束透明度
     * @param duration 耗时
     */
    public static void backgroundAlpha(float start, float end, int duration, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        ValueAnimator valueAnim=ValueAnimator.ofFloat(start,end);
        valueAnim.setDuration(duration);
        valueAnim.start();
        valueAnim.addUpdateListener(animatorUpdateListener);
    }

}
