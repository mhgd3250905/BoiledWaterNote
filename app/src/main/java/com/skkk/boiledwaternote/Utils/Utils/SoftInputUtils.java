package com.skkk.boiledwaternote.Utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * 创建于 2017/9/23
 * 作者 admin
 */
/*
* 
* 描    述：软键盘工具
* 作    者：ksheng
* 时    间：2017/9/23$ 21:40$.
*/
public class SoftInputUtils {
    public static void toggleSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
