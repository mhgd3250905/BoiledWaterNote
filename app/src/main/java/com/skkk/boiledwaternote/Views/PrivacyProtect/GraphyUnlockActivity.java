package com.skkk.boiledwaternote.Views.PrivacyProtect;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skkk.boiledwaternote.Configs;
import com.skkk.boiledwaternote.CostomViews.TouchDeblockView.TouchDeblockingView;
import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Utils.Utils.SpUtils;

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
        } else if (purpose == Configs.GRAPHY_SET_PASSWORD) {
            tvGraphyUnlockLeft.setVisibility(View.VISIBLE);
            tvGraphyUnlockRight.setVisibility(View.VISIBLE);
            tvGraphyUnlockLeft.setText("取消");
            tvGraphyUnlockRight.setText("继续");
        } else if (purpose == Configs.GRAPHY_RESET_PASSWORD) {
            tvGraphyUnlockLeft.setVisibility(View.VISIBLE);
            tvGraphyUnlockRight.setVisibility(View.VISIBLE);
            tvGraphyUnlockLeft.setText("取消");
            tvGraphyUnlockRight.setText("继续");
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
                    tdvGraphyUnlock.resetPoints();
                    tvGraphyUnlockLeft.setText("取消");
                    tvGraphyUnlockLeft.setOnClickListener(cancelClickListener);
                }
            }
        };

        nextClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tdvGraphyUnlock != null) {
                    tdvGraphyUnlock.resetPoints();
                    unLockStep++;
                    tvGraphyUnlockRight.setOnClickListener(completedClickListener);
                    tvGraphyUnlockLeft.setText("取消");
                    tvGraphyUnlockLeft.setOnClickListener(cancelClickListener);
                }
            }
        };

        completedClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtils.saveString(GraphyUnlockActivity.this, Configs.GRAPHY_UNLOCK_PASSWORD, sbPassword.toString());
                finish();
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
                        return true;
                    } else {
                        return false;
                    }
                } else if (purpose == Configs.GRAPHY_SET_PASSWORD) {
                    if (unLockStep == 0) {
                        sbPassword = list2String(getString(R.string.graphy_unlock_password_key), passList);
                        return true;
                    } else if (unLockStep == 1) {
                        sbPassword = list2String(getString(R.string.graphy_unlock_password_key), passList);
                        if (sbPassword.toString().equals(SpUtils.getString(GraphyUnlockActivity.this, Configs.GRAPHY_UNLOCK_PASSWORD))) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }else {
                    return false;
                }
            }
        });
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


}
