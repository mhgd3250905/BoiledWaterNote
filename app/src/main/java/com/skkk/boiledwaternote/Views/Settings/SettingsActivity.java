package com.skkk.boiledwaternote.Views.Settings;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.skkk.boiledwaternote.Configs;
import com.skkk.boiledwaternote.CostomViews.SettingItemView;
import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Utils.Utils.SpUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    @Bind(R.id.tb_settings)
    Toolbar tbSettings;
    @Bind(R.id.ll_settings)
    LinearLayout llSettings;
    @Bind(R.id.activity_settings_container)
    CoordinatorLayout activitySettingsContainer;
    @Bind(R.id.settings_item_style)
    SettingItemView settingsItemStyle;
    @Bind(R.id.settings_item_theme)
    SettingItemView settingsItemTheme;
    @Bind(R.id.settings_item_quick_entrance)
    SettingItemView settingsItemQuickEntrance;
    @Bind(R.id.settings_item_privacy_switch)
    SettingItemView settingsItemPrivacySwitch;
    @Bind(R.id.settings_item_privacy_type)
    SettingItemView settingsItemPrivacyType;

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
        //设置笔记列表风格
        int style = SpUtils.getInt(getApplicationContext(), Configs.SP_KEY_NOTE_LIST_LAYOUT_STYLE);
        if (style == Configs.NOTE_LIST_LAYOUT_STYLE_STAGGER) {
            settingsItemStyle.setValue(getString(R.string.note_list_style_stagger));
        } else {
            settingsItemStyle.setValue(getString(R.string.note_list_style_linear));
        }

        //设置隐私是否开启
        boolean isPrivacyEnable = SpUtils.getBoolean(getApplicationContext(), Configs.SP_KEY_PRIVACY_ENABLE);
        settingsItemPrivacySwitch.setValue(isPrivacyEnable?getString(R.string.switch_open):getString(R.string.switch_close));
        settingsItemPrivacyType.setItemEnable(isPrivacyEnable);

        int privacyType=SpUtils.getInt(getApplicationContext(),Configs.SP_KEY_PRIVACY_TYPE);
        if (privacyType==Configs.PRIVACY_TYPE_TOUCH_ID){//指纹识别
            settingsItemPrivacyType.setValue(getString(R.string.privacy_type_touch_id));
        }else if (privacyType==Configs.PRIVACY_TYPE_GRAPHY){//图形解锁
            settingsItemPrivacyType.setValue(getString(R.string.privacy_type_graphy));
        }else {//没有设置
            settingsItemPrivacyType.setValue(getString(R.string.blank));
        }

    }

    @OnClick({R.id.settings_item_style, R.id.settings_item_theme, R.id.settings_item_quick_entrance, R.id.settings_item_privacy_switch, R.id.settings_item_privacy_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.settings_item_style:      //设置首页笔记列表显示风格
                int style = SpUtils.getInt(getApplicationContext(), Configs.SP_KEY_NOTE_LIST_LAYOUT_STYLE);
                if (style == Configs.NOTE_LIST_LAYOUT_STYLE_LINEAR) {
                    SpUtils.saveInt(getApplicationContext(), Configs.SP_KEY_NOTE_LIST_LAYOUT_STYLE, Configs.NOTE_LIST_LAYOUT_STYLE_STAGGER);
                    settingsItemStyle.setValue(getString(R.string.note_list_style_stagger));
                } else {
                    SpUtils.saveInt(getApplicationContext(), Configs.SP_KEY_NOTE_LIST_LAYOUT_STYLE, Configs.NOTE_LIST_LAYOUT_STYLE_LINEAR);
                    settingsItemStyle.setValue(getString(R.string.note_list_style_linear));
                }
                break;
            case R.id.settings_item_theme:      //设置主题
                break;
            case R.id.settings_item_quick_entrance:     //设置快捷入口
                break;
            case R.id.settings_item_privacy_switch:     //设置隐私开关
                boolean isPrivacyEnable = SpUtils.getBoolean(getApplicationContext(), Configs.SP_KEY_PRIVACY_ENABLE);
                if (isPrivacyEnable) {
                    SpUtils.saveBoolean(getApplicationContext(), Configs.SP_KEY_PRIVACY_ENABLE,false);
                    settingsItemPrivacySwitch.setValue(getString(R.string.switch_close));
                } else {
                    SpUtils.saveBoolean(getApplicationContext(), Configs.SP_KEY_PRIVACY_ENABLE,true);
                    settingsItemPrivacySwitch.setValue(getString(R.string.switch_open));
                }
                break;
            case R.id.settings_item_privacy_type:       //设置隐私模式
                int privacyType=SpUtils.getInt(getApplicationContext(),Configs.SP_KEY_PRIVACY_TYPE);
                if (privacyType==Configs.PRIVACY_TYPE_TOUCH_ID){//指纹识别
                    SpUtils.saveInt(getApplicationContext(),Configs.SP_KEY_PRIVACY_TYPE, Configs.PRIVACY_TYPE_GRAPHY);
                    settingsItemPrivacyType.setValue(getString(R.string.privacy_type_graphy));
                }else if (privacyType==Configs.PRIVACY_TYPE_GRAPHY){//图形解锁
                    SpUtils.saveInt(getApplicationContext(),Configs.SP_KEY_PRIVACY_TYPE, Configs.PRIVACY_TYPE_TOUCH_ID);
                    settingsItemPrivacyType.setValue(getString(R.string.privacy_type_touch_id));
                }else {//没有设置
                    SpUtils.saveInt(getApplicationContext(),Configs.SP_KEY_PRIVACY_TYPE, Configs.PRIVACY_TYPE_GRAPHY);
                    settingsItemPrivacyType.setValue(getString(R.string.privacy_type_graphy));
                }
                break;
        }
    }

}
