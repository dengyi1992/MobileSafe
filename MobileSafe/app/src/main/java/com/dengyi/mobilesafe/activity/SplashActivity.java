package com.dengyi.mobilesafe.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.dengyi.mobilesafe.R;

public class SplashActivity extends Activity {

    private TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        tvVersion= (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("版本号："+getVersionName());
    }
    private String getVersionName(){
        int versionCode;
        String versionName = null;
        PackageManager packageManager= getPackageManager();
        try {
            PackageInfo packageInfo=packageManager.getPackageInfo(getPackageName(),0);
            //获取包的信息
            versionCode=packageInfo.versionCode;
            versionName=packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
