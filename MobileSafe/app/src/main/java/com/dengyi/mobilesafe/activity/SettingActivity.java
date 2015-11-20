package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.dengyi.mobilesafe.R;
import com.dengyi.mobilesafe.view.SettingItemView;

/**
 * Created by deng on 2015/11/16.
 * 设置中心
 */
public class SettingActivity extends Activity {
    private SettingItemView sivUpdate;//设置是否升级
    private SharedPreferences mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        boolean autoUpdate = mPref.getBoolean("auto_update", true);
        if (autoUpdate) {
            sivUpdate.setDesc("自动更新已开启");
            sivUpdate.setChecked(true);
        }else {
            sivUpdate.setDesc("自动更新已关闭");
            sivUpdate.setChecked(false);
        }
        sivUpdate.setTitle("自动更新设置");
        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前勾选状态
                if (sivUpdate.isChecked()) {
                    sivUpdate.setChecked(false);
                    sivUpdate.setDesc("自动更新已关闭");
                    mPref.edit().putBoolean("auto_update", false).commit();
                } else {
                    sivUpdate.setChecked(true);
                    sivUpdate.setDesc("自动更新已开启");
                    mPref.edit().putBoolean("auto_update", true).commit();
                }
            }
        });
    }
}
