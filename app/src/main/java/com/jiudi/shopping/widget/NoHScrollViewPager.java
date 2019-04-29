package com.jiudi.shopping.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoHScrollViewPager extends ViewPager {
    public NoHScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoHScrollViewPager(Context context) {
        super(context);
    }

    // 事件拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;// 不拦截子控件的事件
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 重写此方法, 触摸时什么都不做, 从而实现对滑动事件的禁用
        return true;
    }
}

