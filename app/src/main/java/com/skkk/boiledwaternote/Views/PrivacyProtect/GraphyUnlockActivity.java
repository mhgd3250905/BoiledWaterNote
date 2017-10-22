package com.skkk.boiledwaternote.Views.PrivacyProtect;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skkk.boiledwaternote.Configs;
import com.skkk.boiledwaternote.CostomViews.TouchDeblockView.TouchDeblockingView;
import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Utils.Utils.SpUtils;
import com.skkk.boiledwaternote.Utils.Utils.Toasts;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GraphyUnlockActivity extends BasePrivacyActivity {

    @Bind(R.id.tdv_graphy_unlock)
    TouchDeblockingView tdvGraphyUnlock;
    @Bind(R.id.ll_graphy_unlock)
    LinearLayout llGraphyUnlock;
    @Bind(R.id.activity_graphy_unlock_container)
    CoordinatorLayout activityGraphyUnlockContainer;
    @Bind(R.id.tv_graphy_unlock_left)
    TextView tvGraphyUnlockLeft;
    @Bind(R.id.tv_graphy_unlock_right)
    TextView tvGraphyUnlockRight;
    @Bind(R.id.tv_unlock_title)
    TextView tvUnlockTitle;
    private StringBuffer sbPassword;//密码
    private int purpose;
    private int unLockStep = 0;
    private View.OnClickListener cancelClickListener, againClickListener, nextClickListener, completedClickListener;
    private boolean checkResult = false;//验证结果


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphy_unlock);
        ButterKnife.bind(this);
        initData();
        sbPassword = new StringBuffer();
        initUI();
        initEvent();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        purpose = intent.getIntExtra(Configs.KEY_GRAPHY_PURPOSE, 0);
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        if (purpose == Configs.GRAPHY_UNLOCK) {
            tvGraphyUnlockLeft.setVisibility(View.GONE);
            tvGraphyUnlockRight.setVisibility(View.GONE);
            setTvUnlockTitle("请绘制图形密码",R.color.colorPrimary);      //显示标题
        } else if (purpose == Configs.GRAPHY_SET_PASSWORD) {
            tvGraphyUnlockLeft.setVisibility(View.VISIBLE);
            tvGraphyUnlockRight.setVisibility(View.VISIBLE);
            tvGraphyUnlockLeft.setText("取消");
            tvGraphyUnlockRight.setText("继续");
            setTvUnlockTitle("请设置图形密码",R.color.colorPrimary);      //显示标题
        } else if (purpose == Configs.GRAPHY_RESET_PASSWORD) {
            tvGraphyUnlockLeft.setVisibility(View.VISIBLE);
            tvGraphyUnlockRight.setVisibility(View.VISIBLE);
            tvGraphyUnlockLeft.setText("取消");
            tvGraphyUnlockRight.setText("继续");
            setTvUnlockTitle("请绘制图形密码",R.color.colorPrimary);      //显示标题
        }
    }


    /**
     * 初始化界面
     */
    private void initEvent() {
        cancelClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };

        againClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tdvGraphyUnlock != null) {
                    resetGraphy();//重置图形
                    if (purpose==Configs.GRAPHY_UNLOCK){
                        setTvUnlockTitle("请绘制图形密码",R.color.colorPrimary);      //显示标题
                    }else if (purpose==Configs.GRAPHY_SET_PASSWORD){
                        if (unLockStep==0){
                            setTvUnlockTitle("请绘制图形密码",R.color.colorPrimary);      //显示标题
                        }else if (unLockStep==1){
                            setTvUnlockTitle("请再次绘制图形密码",R.color.colorPrimary);
                        }
                    }else if (purpose==Configs.GRAPHY_RESET_PASSWORD){
                        setTvUnlockTitle("请绘制图形密码",R.color.colorPrimary);
                    }
                }
            }
        };

        nextClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tdvGraphyUnlock != null) {
                    if (purpose == Configs.GRAPHY_SET_PASSWORD) {
                        tdvGraphyUnlock.resetPoints();
                        unLockStep++;
                        tvGraphyUnlockRight.setOnClickListener(completedClickListener);
                        tvGraphyUnlockLeft.setText("取消");
                        tvGraphyUnlockLeft.setOnClickListener(cancelClickListener);
                        setTvUnlockTitle("请再次绘制图形密码",R.color.colorPrimary);
                    } else if (purpose == Configs.GRAPHY_RESET_PASSWORD) {
                        if (checkResult) {
                            purpose=Configs.GRAPHY_SET_PASSWORD;
                            tdvGraphyUnlock.resetPoints();
                            setTvUnlockTitle("请绘制新的图形密码",R.color.colorPrimary);
                        } else {
                            Toasts.costom(GraphyUnlockActivity.this, "图形密码错误，请重新绘制！", R.drawable.vector_drawable_pen_blue,
                                    Color.WHITE, 5f, Toast.LENGTH_SHORT).show();
                            resetGraphy();//重置图形
                        }
                    }
                }
            }
        };

        completedClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkResult) {
                    SpUtils.saveString(GraphyUnlockActivity.this, Configs.GRAPHY_UNLOCK_PASSWORD, sbPassword.toString());
                    finish();
                } else {
                    Toasts.costom(GraphyUnlockActivity.this, "图形密码错误，请重新绘制！", R.drawable.vector_drawable_pen_blue,
                            Color.WHITE, 5f, Toast.LENGTH_SHORT).show();
                    resetGraphy();//重置图形
                }
            }
        };


        tvGraphyUnlockLeft.setOnClickListener(cancelClickListener);
        tvGraphyUnlockRight.setOnClickListener(nextClickListener);

        /**
         * 图形解锁监听
         */
        tdvGraphyUnlock.setOnDrawFinishedListener(new TouchDeblockingView.OnDrawFinishListener() {
            @Override
            public boolean OnDrawFinished(List<Integer> passList) {
                tvGraphyUnlockLeft.setText("重画");
                tvGraphyUnlockLeft.setOnClickListener(againClickListener);
                if (purpose == Configs.GRAPHY_UNLOCK) {
                    sbPassword = list2String(getString(R.string.graphy_unlock_password_key), passList);
                    if (sbPassword.toString().equals(SpUtils.getString(GraphyUnlockActivity.this, Configs.GRAPHY_UNLOCK_PASSWORD))) {
                        setTvUnlockTitle("解锁成功",R.color.colorTouchPressLine);  //显示标题
                        return true;
                    } else {
                        setTvUnlockTitle("解锁失败",R.color.colorTouchErrorLine);      //显示标题
                        return false;
                    }
                } else if (purpose == Configs.GRAPHY_SET_PASSWORD) {        //设置密码
                    if (unLockStep == 0) {
                        sbPassword = list2String(getString(R.string.graphy_unlock_password_key), passList);
                        setTvUnlockTitle("密码设置完毕",R.color.colorPrimary);      //显示标题
                        return true;
                    } else if (unLockStep == 1) {
                        if (sbPassword.toString().equals(list2String(getString(R.string.graphy_unlock_password_key), passList).toString())) {
                            checkResult = true;
                            setTvUnlockTitle("密码验证成功",R.color.colorTouchPressLine);      //显示标题
                            return checkResult;
                        } else {
                            checkResult = false;
                            setTvUnlockTitle("密码验证失败",R.color.colorTouchErrorLine);      //显示标题
                            return checkResult;
                        }
                    } else {
                        return false;
                    }
                } else if (purpose == Configs.GRAPHY_RESET_PASSWORD) {      //重置密码
                    if (unLockStep == 0) {
                        sbPassword = list2String(getString(R.string.graphy_unlock_password_key), passList);
                        if (sbPassword.toString().equals(SpUtils.getString(GraphyUnlockActivity.this, Configs.GRAPHY_UNLOCK_PASSWORD))) {
                            setTvUnlockTitle("密码验证成功",R.color.colorTouchPressLine);      //显示标题
                            checkResult=true;
                            return checkResult;
                        } else {
                            setTvUnlockTitle("密码验证失败",R.color.colorTouchErrorLine);      //显示标题
                            checkResult=false;
                            return checkResult;
                        }
                    }
                    return false;
                }
                return false;
            }
        });
    }

    private void resetGraphy() {
        tdvGraphyUnlock.resetPoints();
        tvGraphyUnlockLeft.setText("取消");
        tvGraphyUnlockLeft.setOnClickListener(cancelClickListener);
    }


    /**
     * list 转 string
     *
     * @param key
     * @param passList
     * @return
     */

    private StringBuffer list2String(String key, List<Integer> passList) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < passList.size(); i++) {
            if (i == 0) {
                stringBuffer.append(passList.get(i));
            } else {
                stringBuffer.append(key);
                stringBuffer.append(passList.get(i));
            }
        }
        return stringBuffer;
    }

    /**
     * 设置标题以及样式
     * @param title
     * @param color
     */
    public void setTvUnlockTitle(String title, @ColorRes int color){
        tvUnlockTitle.setText(title);
        tvUnlockTitle.setTextColor(ContextCompat.getColor(getApplicationContext(),color));
    }


}
