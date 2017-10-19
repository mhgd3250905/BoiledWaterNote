package com.skkk.boiledwaternote.Utils.Utils;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skkk.boiledwaternote.R;

/**
 * 创建于 2017/10/19
 * 作者 admin
 */
/*
* 
* 描    述：吐司工具集
* 作    者：ksheng
* 时    间：2017/10/19$ 22:53$.
*/
public class Toasts {

    private static Toast costomToast;
    private static Handler handler=new Handler();
    private boolean isCancel=true;

    public static Toast costom(@NonNull Activity context, String toastContent, int drawableRes,
                               int backGroundColor, float radius, @NonNull int duration){
        costomToast = new Toast(context);

        View toastlayout = context.getLayoutInflater().inflate(R.layout.layout_toast, (ViewGroup) context.findViewById(R.id.ll_toast));
        TextView tvToast= (TextView) toastlayout.findViewById(R.id.tv_toast);
        ImageView ivToast= (ImageView) toastlayout.findViewById(R.id.iv_toast);

        GradientDrawable toastShape= (GradientDrawable) toastlayout.getBackground();
        toastShape.setCornerRadius(radius);
        toastShape.setColor(backGroundColor);

        tvToast.setText(toastContent);
        ivToast.setImageResource(drawableRes);
        toastlayout.setBackground(toastShape);

        costomToast.setView(toastlayout);
        costomToast.setDuration(duration);

        return costomToast;
    }

    public  void showToast(final Toast toast, int duration){
        if (duration!=-1){
            toast.setDuration(duration);
            toast.show();
        }else {
            if (isCancel){
                return;
            }
            isCancel=false;
            toast.show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                    showToast(toast, -1);
                }
            }, 3000);
        }
    }

    public void stopToast(){
        if (handler!=null){
            isCancel=true;

        }
    }
}