package com.dengyi.mobilesafe.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by deng on 2015/11/16.
 * 获取焦点的textView
 */
public class FocusedTextView extends TextView{
    public FocusedTextView(Context context) {
        super(context);
    }

    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 表示有没有获取焦点
     * 跑马灯要有焦点，是ture的话，才会有跑
     *
     * @return
     */

    @Override
    public boolean isFocused() {
        return true;
    }
}
