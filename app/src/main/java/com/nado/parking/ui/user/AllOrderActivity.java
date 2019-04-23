package com.nado.parking.ui.user;

import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.nado.parking.R;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.bean.TabEntity;
import com.nado.parking.manager.AccountManager;

import java.util.ArrayList;

public class AllOrderActivity extends BaseActivity {
    private android.widget.RelativeLayout rlLayoutTopBackBar;
    private android.widget.LinearLayout llLayoutTopBackBarBack;
    private android.widget.TextView tvLayoutTopBackBarTitle;
    private android.widget.TextView tvLayoutTopBackBarEnd;
    private android.widget.TextView tvLayoutBackTopBarOperate;
    private com.flyco.tablayout.CommonTabLayout tl;
    private String[] mTitles = {"全部", "待付款", "待发货", "待收货","已完成"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private android.widget.FrameLayout flChange;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ordergood;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        tl = (CommonTabLayout) findViewById(R.id.tl);
        flChange = (FrameLayout) findViewById(R.id.fl_change);
        tvLayoutTopBackBarTitle.setText("我的订单");
    }

    @Override
    public void initData() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        mFragments.add(new OrderFragment().setArgumentz("order_status","all"));
        mFragments.add(new OrderFragment().setArgumentz("order_status","0"));
        mFragments.add(new OrderFragment().setArgumentz("order_status","1"));
        mFragments.add(new OrderFragment().setArgumentz("order_status","3"));
        mFragments.add(new OrderFragment().setArgumentz("order_status","5"));
        tl.setTabData(mTabEntities,this,R.id.fl_change,mFragments);
        tl.setCurrentTab(getIntent().getIntExtra("order_status",0));

    }

    @Override
    public void initEvent() {
        if (AccountManager.sUserBean != null) {

        }else{
            finish();
        }

    }
}
