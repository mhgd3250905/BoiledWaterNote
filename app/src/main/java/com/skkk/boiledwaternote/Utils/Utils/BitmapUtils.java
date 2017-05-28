package com.skkk.boiledwaternote.Utils.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by admin on 2017/4/23.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/4/23$ 19:56$.
*/
public class BitmapUtils {

    public static Bitmap getBitmapFromUri(Context context,Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }
}
