package com.skkk.boiledwaternote.Utils.Utils;

import java.io.File;

/**
 * 创建于 2017/10/22
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/10/22$ 0:27$.
*/
public class FileUtils {
    //判断文件是否存在
    public static boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(strFile);
            if(!f.exists())
            {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }
}
