package com.skkk.boiledwaternote.Utils.Utils;

import com.skkk.boiledwaternote.Configs;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建于 2017/6/25
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/6/25$ 22:07$.
*/
public class DateUtils {
    /**
     * 根据当前时间获得指定事件格式的字符串
     * @return 事件字符串
     */
    public static String getTime(){
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat(Configs.DATE_TIME_FORMAT_LONG_CN);
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }
}
