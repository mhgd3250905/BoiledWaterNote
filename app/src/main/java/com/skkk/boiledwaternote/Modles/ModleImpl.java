package com.skkk.boiledwaternote.Modles;

import java.util.List;

/**
 * Created by admin on 2017/6/11.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/6/11$ 15:24$.
*/
interface ModleImpl<T> {
    List<T> queryAll();
    boolean saveOne(T t);
    boolean updateOne(T t);
    boolean deleteOne(T t);
    List<T> query(String noteType);
    T queryLatestOne();
}
