package com.skkk.boiledwaternote.Utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 创建于 2017/6/25
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/6/25$ 22:04$.
*/
public class ImageUtils {

    /* @描述 保存图片然后返回路径 */
    public static String saveBitmapAndReturnPath(Context context,Bitmap bitmap) {
        File f = new File(context.getFilesDir(), com.skkk.boiledwaternote.Utils.Utils.DateUtils.getTime()+"shareBKS");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG,100, out);
            out.flush();
            out.close();
            String sharePath=f.getAbsolutePath();
            return sharePath;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 通过uri获取文件的绝对路径
     *
     * @param uri
     * @return
     */
    public static String getAbsoluteImagePath(Activity context,Uri uri) {
        String imagePath = "";
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.managedQuery(uri, proj, // Which
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                imagePath = cursor.getString(column_index);
            }
        }
        return imagePath;
    }



}
