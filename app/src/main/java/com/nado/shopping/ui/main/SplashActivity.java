package com.nado.shopping.ui.main;

import android.content.Intent;
import android.os.Handler;

import com.nado.shopping.R;
import com.nado.shopping.base.BaseActivity;

public class SplashActivity extends BaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    public boolean isLoginActivity() {
        return true;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(mActivity,MainActivity.class));
                finish();
            }
        },2000);

    }

    @Override
    public void initEvent() {

    }
}
