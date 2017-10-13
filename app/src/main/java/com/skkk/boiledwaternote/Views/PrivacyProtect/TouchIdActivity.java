package com.skkk.boiledwaternote.Views.PrivacyProtect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skkk.boiledwaternote.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建于 2017/10/12
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/10/12$ 23:17$.
*/
public class TouchIdActivity extends AppCompatActivity {
    private static final String TAG = "TouchIdActivity";
    protected FingerprintManagerCompat fingerprintManagerCompat;
    protected StringBuilder stringBuilder = new StringBuilder("");  // 显示指纹传感器回调信息
    @Bind(R.id.iv_finger_print)
    ImageView ivFingerPrint;
    @Bind(R.id.tb_touch_id)
    Toolbar tbTouchId;
    @Bind(R.id.ll_touch_id)
    LinearLayout llTouchId;
    @Bind(R.id.activity_touch_id_container)
    CoordinatorLayout activityTouchIdContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_id);
        ButterKnife.bind(this);
        fingerprintManagerCompat = FingerprintManagerCompat.from(getApplication());

        initEvent();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        tbTouchId.setNavigationIcon(R.drawable.back_arrow);
        //设置返回按钮点击事件
        tbTouchId.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 开启指纹验证
     */
    private void startCheckFingerPrint() {
        if (mCancellationSignal.isCanceled()) {
            mCancellationSignal = new CancellationSignal(); // 如果执行过cancel(),需要重新生成一个对象才有效.
        }
        mCancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                Toast.makeText(TouchIdActivity.this, "主动取消指纹验证", Toast.LENGTH_LONG).show();
            }
        });
        /**
         * 开始验证，什么时候停止由系统来确定，如果验证成功，那么系统会关闭sensor(指纹传感器)，如果失败，则允许
         * 多次(5次)尝试，如果依旧失败，则会拒绝一段时间，然后关闭sensor，过一段时候之后再重新允许尝试
         *
         * 第四个参数为重点，需要传入一个FingerprintManagerCompat.AuthenticationCallback的子类
         * 并重写一些方法，不同的情况回调不同的函数
         */
        fingerprintManagerCompat.authenticate(null, 0, mCancellationSignal, mAuthenticationCallback, mHandler);
    }

    /**
     * 关闭指纹验证
     */
    private void closeCheckFingerPrint() {
        if (!mCancellationSignal.isCanceled()) {
            mCancellationSignal.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 判断是否有指纹使用权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED) {
            if (!fingerprintManagerCompat.isHardwareDetected()) {
                Toast.makeText(this, "手机不存在指纹模块", Toast.LENGTH_LONG).show();
            } else {
                if (!fingerprintManagerCompat.hasEnrolledFingerprints()) {
                    Toast.makeText(this, "用户没有录入过指纹(没有开启指纹识别)", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "指纹模块正常且可用", Toast.LENGTH_LONG).show();
                    //开启指纹识别
                    startCheckFingerPrint();
                }
            }
        } else {
            Toast.makeText(this, "使用指纹权限被禁用", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == 0) {
            startActivity(new Intent(this, TouchIdActivity.class));
            finish();
        } else {
            Toast.makeText(this, "请到设置中给应用开启指纹权限", Toast.LENGTH_LONG).show();
        }
    }

    // 指纹验证被取消控制对象
    private CancellationSignal mCancellationSignal = new CancellationSignal();

    // 指纹验证结果回调对象
    private final FingerprintManagerCompat.AuthenticationCallback mAuthenticationCallback = new FingerprintManagerCompat.AuthenticationCallback() {
        /**
         * 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息
         * 验证过程错误
         * errMsgId=7 errString会是一个60秒的倒计时 倒计时结束会回调onAuthenticationFailed方法 倒计时结束时才可以再次重新调用authenticate开始第二轮验证
         * errMsgId=5 说明mCancellationSignal.cancel();被调用,主动取消指纹验证.
         */
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            Log.e(TAG, "验证回调 一 onAuthenticationError " + errMsgId + "\t" + errString.toString());
            ivFingerPrint.setImageResource(R.drawable.ic_fingerprint_red);
            stringBuilder.append("\nonAuthenticationError\t").append(errMsgId).append("\t").append(errString);
        }

        /**
         * 回调一些状态信息 魅族验证失败回调onAuthenticationFailed时也会回调该方法 华为不会 华为直接进入onAuthenticationFailed
         * helpString 可以直接显示给用户提示用户如何操作
         */
        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            Log.e(TAG, "验证回调 二 onAuthenticationHelp " + helpMsgId + "\t" + helpString.toString());

            stringBuilder.append("\nonAuthenticationHelp\t").append(helpMsgId).append("\t").append(helpString);
        }

        /**
         * 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
         * 进入这里说明指纹验证成功
         */
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            Log.e(TAG, "验证回调 三 onAuthenticationSucceeded " + result.toString());
            ivFingerPrint.setImageResource(R.drawable.ic_fingerprint_green);
            stringBuilder.append("\nonAuthenticationSucceeded 验证成功 系统停止验证指纹 \t").append(result.toString());
        }

        /**
         * 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
         * 到这里说明当前验证失败
         * 如果回调了5次,目前已知的手机区别.魅族手机指纹验证会停止60秒.60秒后才可以重新验证. 华为要重新调用authenticate方法.
         */
        @Override
        public void onAuthenticationFailed() {
            Log.e(TAG, "验证回调 四 onAuthenticationFailed ");
            ivFingerPrint.setImageResource(R.drawable.ic_fingerprint_red);
            stringBuilder.append("\nonAuthenticationFailed 验证失败");
        }
    };

    // 可选的Handler回调
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.e(TAG, "可选的Handler回调 handleMessage " + msg.toString());
        }
    };

    /**
     * 重写返回按钮关闭指纹识别
     */
    @Override
    public void onBackPressed() {
        closeCheckFingerPrint();
        super.onBackPressed();
    }
}


