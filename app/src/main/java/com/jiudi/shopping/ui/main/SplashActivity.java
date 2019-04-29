package com.jiudi.shopping.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;

public class SplashActivity extends BaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    public boolean isNoNeedLogin() {
        return true;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        SharedPreferences sp=getSharedPreferences("config", Context.MODE_PRIVATE);
        if(sp.getBoolean("isfirstinstall",true)){
            startActivity(new Intent(mActivity,GuideActivity.class));
            finish();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(mActivity,MainActivity.class));
                    finish();
                }
            },2000);
        }


    }

    @Override
    public void initEvent() {

    }
}
