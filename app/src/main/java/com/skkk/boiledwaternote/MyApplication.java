package com.skkk.boiledwaternote;

import android.app.Application;

// TODO: 2017/6/25 1.笔记编辑界面点击进入时设置为展示状态，手动点击编辑的时候就进入编辑状态--->done
// TODO: 2017/6/25 2.富文本中添加分隔线 ---> done
// TODO: 2017/6/26 3.编辑中焦点的跳转 ---> done
// TODO: 2017/6/26 4.根据bitmap的不同添加不同宽高比的相框
// TODO: 2017/6/27 5.弹射菜单，你懂得--->done
// TODO: 2017/7/3  6.点击编辑框外面，自定定位到最后一个编辑框--->done
// TODO: 2017/7/4  7.修改富文本中的列表和引用为点击按钮另起一行

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

