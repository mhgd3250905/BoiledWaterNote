package com.skkk.boiledwaternote.Views.Settings;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.skkk.boiledwaternote.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.tb_settings)
    Toolbar tbSettings;
    @Bind(R.id.ll_settings)
    LinearLayout llSettings;
    @Bind(R.id.activity_settings_container)
    CoordinatorLayout activitySettingsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        //初始化UI
        initUI();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        //设置返回按钮以及返回事件
        tbSettings.setNavigationIcon(R.drawable.back_arrow);
        tbSettings.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
