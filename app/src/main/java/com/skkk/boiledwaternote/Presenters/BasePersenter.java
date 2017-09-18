package com.skkk.boiledwaternote.Presenters;

import java.lang.ref.WeakReference;

/**
 * Created by admin on 2017/6/11.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/6/11$ 19:38$.
*/
public abstract class BasePersenter<T> {
    /**
     * 持有UI接口的弱引用
     */
    protected WeakReference<T> mViewRef;


    public void attachView(T view) {
        mViewRef = new WeakReference<T>(view);
    }

    /**
     * 解绑
     */
    public void detach()
    {
        if(mViewRef!=null)
        {
            mViewRef.clear();
            mViewRef=null;
        }
    }

    /**
     * 获取视图层
     * @return
     */
    public T getView(){
        return mViewRef.get();
    }
}
