package com.skkk.boiledwaternote;

import android.app.Application;

import com.skkk.boiledwaternote.Modles.gen.DaoSession;

/**
 * Created by admin on 2017/5/28.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/5/28$ 15:50$.
*/
public class MyApplication extends Application{
    private static DaoSession sessionInstance;

    @Override
    public void onCreate() {
        super.onCreate();

    }

}

