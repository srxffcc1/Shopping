package com.jiudi.shopping.ui.user.account;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.event.UpdateLoginStateEvent;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.ui.device.DeviceListActivity;
import com.jiudi.shopping.ui.user.AddressListActivity;
import com.jiudi.shopping.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

public class UserSettingActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout mBackLL;
    private TextView mTitleTV;

    private LinearLayout mUpdatePwdLL;

    private LinearLayout mFeedBackLL;
    private LinearLayout mExitLoginLL;

    private LinearLayout mAboutUsLL;

    private PopupWindow mBottomPopwindow;
    private TextView mVersionNameTV;
    private android.widget.RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarStart;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private LinearLayout llActivityUserSetNicheng;
    private TextView textnicheng;
    private LinearLayout llActivityUserSetPhone;
    private TextView textphone;
    private LinearLayout llActivityUserSetUpdatePwd;
    private LinearLayout llActivityUserSetDizhi;
    private LinearLayout llActivityUserSetAboutUs;
    private TextView tvActivityUserAboutUs;
    private LinearLayout llActivityUserSetFeedBack;
    private TextView tvActivityUserSettingVersionName;
    private LinearLayout llActivityUserSetExitLogin;
    private TextView tvActivityUserSettingExit;
    private LinearLayout llActivityUserSetObd;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_setting;
    }

    @Override
    public void initView() {
        mBackLL = byId(R.id.ll_layout_top_back_bar_back);
        mTitleTV = byId(R.id.tv_layout_top_back_bar_title);
        mTitleTV.setText(getString(R.string.user_setting));

        mUpdatePwdLL = byId(R.id.ll_activity_user_set_update_pwd);
        mFeedBackLL = byId(R.id.ll_activity_user_set_feed_back);


        mExitLoginLL = byId(R.id.ll_activity_user_set_exit_login);
        mVersionNameTV=byId(R.id.tv_activity_user_setting_version_name);
        mVersionNameTV.setText("V"+getPackageInfo(mActivity).versionName);
        mAboutUsLL=byId(R.id.ll_activity_user_set_about_us);
        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        llActivityUserSetNicheng = (LinearLayout) findViewById(R.id.ll_activity_user_set_nicheng);
        textnicheng = (TextView) findViewById(R.id.textnicheng);
        llActivityUserSetPhone = (LinearLayout) findViewById(R.id.ll_activity_user_set_phone);
        textphone = (TextView) findViewById(R.id.textphone);
        llActivityUserSetUpdatePwd = (LinearLayout) findViewById(R.id.ll_activity_user_set_update_pwd);
        llActivityUserSetDizhi = (LinearLayout) findViewById(R.id.ll_activity_user_set_dizhi);
        llActivityUserSetAboutUs = (LinearLayout) findViewById(R.id.ll_activity_user_set_about_us);
        tvActivityUserAboutUs = (TextView) findViewById(R.id.tv_activity_user_about_us);
        llActivityUserSetFeedBack = (LinearLayout) findViewById(R.id.ll_activity_user_set_feed_back);
        tvActivityUserSettingVersionName = (TextView) findViewById(R.id.tv_activity_user_setting_version_name);
        llActivityUserSetExitLogin = (LinearLayout) findViewById(R.id.ll_activity_user_set_exit_login);
        tvActivityUserSettingExit = (TextView) findViewById(R.id.tv_activity_user_setting_exit);
        llActivityUserSetObd = (LinearLayout) findViewById(R.id.ll_activity_user_set_obd);
    }

    @Override
    public void initData() {
        textnicheng.setText(AccountManager.sUserBean.nicename);
        textphone.setText(AccountManager.sUserBean.phone);
    }

    @Override
    public void initEvent() {
        mBackLL.setOnClickListener(this);
        mUpdatePwdLL.setOnClickListener(this);
        mFeedBackLL.setOnClickListener(this);
        mExitLoginLL.setOnClickListener(this);
        mAboutUsLL.setOnClickListener(this);
        llActivityUserSetDizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, AddressListActivity.class));
            }
        });
        llActivityUserSetObd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, DeviceListActivity.class));
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.ll_layout_top_back_bar_back:
                finish();
                break;
            case R.id.ll_activity_user_set_update_pwd:
                intent = new Intent(mActivity, UpdatePwdActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_activity_user_set_feed_back:
                intent = new Intent(mActivity, FeedBackActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_activity_user_set_exit_login:
                showOutExitPopwindow();
                break;

            case R.id.ll_activity_user_set_about_us:
//                intent = new Intent(mActivity, AboutUsActivity.class);
//                startActivity(intent);
                AboutUsActivity.open(mActivity,AboutUsActivity.TYPE_ABOUT_US,"关于我们","");
                break;
        }

    }
    private void showOutExitPopwindow() {
        if (mBottomPopwindow != null && mBottomPopwindow.isShowing()) {
            mBottomPopwindow.dismiss();
        } else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.popup_window_exit_login, null, false);
            TextView mOkTv = (TextView) view.findViewById(R.id.tv_popwindow_exit);
            TextView mCancelTv = (TextView) view.findViewById(R.id.tv_popwindow_cancel);
            mOkTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccountManager.logout(mActivity);
                    EventBus.getDefault().post(new UpdateLoginStateEvent());
                    ToastUtil.showShort(mContext, getString(R.string.exit_login));
                    mBottomPopwindow.dismiss();
                    finish();
                }
            });
            mCancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomPopwindow.dismiss();
                }
            });
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mBottomPopwindow != null && mBottomPopwindow.isShowing()) {
                        mBottomPopwindow.dismiss();
                    }
                    return false;
                }
            });
            mBottomPopwindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

            mBottomPopwindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
        }
    }
    private static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
