package com.skkk.boiledwaternote.Views.PrivacyProtect;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.skkk.boiledwaternote.CostomViews.TouchDeblockView.TouchDeblockingView;
import com.skkk.boiledwaternote.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GraphyUnlockActivity extends AppCompatActivity {

    @Bind(R.id.tb_graphy_unlock)
    Toolbar tbGraphyUnlock;
    @Bind(R.id.tdv_graphy_unlock)
    TouchDeblockingView tdvGraphyUnlock;
    @Bind(R.id.ll_graphy_unlock)
    LinearLayout llGraphyUnlock;
    @Bind(R.id.activity_graphy_unlock_container)
    CoordinatorLayout activityGraphyUnlockContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphy_unlock);
        ButterKnife.bind(this);
    }
}
