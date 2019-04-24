package com.jiudi.shopping.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.jiudi.shopping.util.LogUtil;


/**
 * Created by maqing on 2017/11/28.
 * Email:2856992713@qq.com
 */

public class WrapContentHeightViewPager extends ViewPager {
    private static final String TAG = "WrapContentHeightViewPa";
    public WrapContentHeightViewPager(Context context) {
        super(context);
    }
    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogUtil.e(TAG,"onMeasure");
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
