package com.jiudi.shopping.ui.user;

import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.TabEntity;
import com.jiudi.shopping.manager.AccountManager;

import java.util.ArrayList;

public class AllQuanActivity extends BaseActivity {
    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private CommonTabLayout tl;
    private String[] mTitles = {"全部", "未使用", "已使用", "已过期"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private FrameLayout flChange;

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
        tvLayoutTopBackBarTitle.setText("我的优惠券");
    }

    @Override
    public void initData() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        mFragments.add(new QuanFragment().setArgumentz("type","全部"));
        mFragments.add(new QuanFragment().setArgumentz("type","0"));
        mFragments.add(new QuanFragment().setArgumentz("type","1"));
        mFragments.add(new QuanFragment().setArgumentz("type","2"));
        tl.setTabData(mTabEntities,this,R.id.fl_change,mFragments);
        tl.setCurrentTab(getIntent().getIntExtra("type",0));

    }

    @Override
    public void initEvent() {
        if (AccountManager.sUserBean != null) {

        }else{
            finish();
        }

    }
}
