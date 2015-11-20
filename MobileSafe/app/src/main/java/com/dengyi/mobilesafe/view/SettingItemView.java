package com.dengyi.mobilesafe.view;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dengyi.mobilesafe.R;

/**
 * 设置自定义控件
 * Created by deng on 2015/11/16.
 */
public class SettingItemView extends RelativeLayout{
    private TextView tvTitle;
    private TextView tvDesc;
    private CheckBox cbStatus;

    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化bujv
     */
    private void initView(){
        //将SettingItemView设置当前自定义的
        View.inflate(getContext(), R.layout.view_setting_item,this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        cbStatus=(CheckBox) findViewById(R.id.cb_status);
    }
    public void setTitle(String title){
        tvTitle.setText(title);
    }
    public void setDesc(String desc){
        tvDesc.setText(desc);
    }
    public boolean isChecked(){
        return cbStatus.isChecked();
    }
    public void setChecked(boolean check){
        cbStatus.setChecked(check);
    }

}
