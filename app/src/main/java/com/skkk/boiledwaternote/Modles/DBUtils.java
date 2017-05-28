package com.skkk.boiledwaternote.Modles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.skkk.boiledwaternote.Modles.gen.DaoMaster;
import com.skkk.boiledwaternote.Modles.gen.DaoSession;

/**
 * Created by admin on 2017/5/28.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/5/28$ 18:26$.
*/
public class DBUtils {
    private Context context;
    private DaoSession session;
    private static DBUtils instance;

    public DBUtils(Context context) {
        this.context=context;
    }

    public static DBUtils getInstance(Context context){
        if (instance==null){
            return new DBUtils(context);
        }
        return instance;
    }

    public DaoSession getSession(){
        if (session==null){
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "notes-db", null);
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            session = daoMaster.newSession();
            return session;
        }
        return session;
    }
}
