package com.skkk.boiledwaternote;

import android.app.Application;

// TODO: 2017/6/25 1.笔记编辑界面点击进入时设置为展示状态，手动点击编辑的时候就进入编辑状态
// TODO: 2017/6/25 2.富文本中添加分隔线
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/5/28$ 15:50$.
*/
public class MyApplication extends Application{
    private static MyApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

}

