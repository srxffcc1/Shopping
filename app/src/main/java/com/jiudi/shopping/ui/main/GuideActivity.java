package com.jiudi.shopping.ui.main;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.chezi008.libguide.BaseGuideActivity;
import com.chezi008.libguide.BezierBannerDot;
import com.jiudi.shopping.R;

public class GuideActivity extends BaseGuideActivity {


    @Override
    protected void initData() {
        addView(R.layout.activity_guide1);
        addView(R.layout.activity_guide2);
        addView(R.layout.activity_guide3);
    }

    @Override
    protected void setIndicator(BezierBannerDot bezierBannerDot) {

    }
    public void passapp(View view){
        SharedPreferences sp=getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putBoolean("isfirstinstall",false).commit();
        startActivity(new Intent(view.getContext(),MainActivity.class));
        finish();
    }
}
