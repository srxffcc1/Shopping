package com.jiudi.shopping.ui.user;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.event.FinishEvent;
import com.jiudi.shopping.ui.user.account.AboutUsActivity;
import com.jiudi.shopping.ui.user.account.LoginActivity;
import com.jiudi.shopping.util.DataCleanManager;
import com.jiudi.shopping.util.SPUtil;

import org.greenrobot.eventbus.EventBus;

public class ShopSettingActivity extends BaseActivity {
    private android.widget.LinearLayout userinfo;
    private android.widget.LinearLayout clear;
    private android.widget.LinearLayout address;
    private android.widget.LinearLayout aboutus;
    private android.widget.TextView logout;
    private TextView userspace;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_shopsetting;
    }

    @Override
    public void initView() {

        userinfo = (LinearLayout) findViewById(R.id.userinfo);
        clear = (LinearLayout) findViewById(R.id.clear);
        address = (LinearLayout) findViewById(R.id.address);
        aboutus = (LinearLayout) findViewById(R.id.aboutus);
        logout = (TextView) findViewById(R.id.logout);
        userspace = (TextView) findViewById(R.id.userspace);
    }

    @Override
    public void initData() {
        try {
            userspace.setText(DataCleanManager.getTotalCacheSize(this)+"M");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isNoNeedLogin(){
        return true;
    }
    @Override
    public void initEvent() {
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, AboutUsActivity.class));
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, AddressListActivity.class));

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.put("head","");
                EventBus.getDefault().post(new FinishEvent());
                startActivity(new Intent(mActivity, LoginActivity.class));
                finish();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCleanManager.clearAllCache(mActivity);
                userspace.setText("0.0M");
                Toast.makeText(mActivity,"清除缓存成功",Toast.LENGTH_SHORT).show();

                SPUtil.put("head","");
                EventBus.getDefault().post(new FinishEvent());
                startActivity(new Intent(mActivity, LoginActivity.class));
                finish();

            }
        });

    }
}
