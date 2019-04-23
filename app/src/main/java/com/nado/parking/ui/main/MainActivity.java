package com.nado.parking.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nado.parking.R;
import com.nado.parking.adapter.vp.VpFragmentAdapter;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.event.UpdatePayStatusEvent;
import com.nado.parking.global.Constant;
import com.nado.parking.manager.RequestManager;
import com.nado.parking.net.RetrofitCallBack;
import com.nado.parking.net.RetrofitRequestInterface;
import com.nado.parking.service.AppUpdateService;
import com.nado.parking.util.CommonUtil;
import com.nado.parking.util.DialogUtil;
import com.nado.parking.util.LogUtil;
import com.nado.parking.util.NetworkUtil;
import com.nado.parking.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {
    private ViewPager mMainVP;
    private VpFragmentAdapter mMainAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();

    private TabLayout mMainTL;
    private String[] mTabTitleArray;
    private static final String TAG = "MainActivity";
    private long exitTime = 0;
    private int mCurrentPosition = 0;
    /**
     * APK下载URL
     */
    private String mDownloadUrl;
    private PopupWindow mAppUpdatePopupWindow;
    private PopupWindow mUpdateProgressPopupWindow;
    private TextView mProgressTV;
    private ProgressBar mUpdatePB;
    private static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 1001;
    private static final int REQUEST_CODE_UNKNOWN_APP = 100;

    @Override
    public void onBackPressed() {
        exit();

    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), getString(R.string.exit_app), Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(mActivity);

        mMainVP = byId(R.id.vp_activity_main);
        mMainTL = byId(R.id.tl_activity_main);
    }

    @Override
    public void initData() {
        initTabData();
        //获取版本信息
        //getVersion();
    }

    @Override
    public void initEvent() {

    }

    private void initTabData() {
        mTabTitleArray = getResources().getStringArray(R.array.main_tab);
        for (int i = 0; i < mTabTitleArray.length; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tab, null);
            TextView titleTV = view.findViewById(R.id.tv_layout_tab_title);
            ImageView iconIV = view.findViewById(R.id.iv_layout_tab_icon);
            titleTV.setText(mTabTitleArray[i]);
            int position = i;
            switch (position) {
                case 0:
                    iconIV.setImageResource(R.drawable.tab_icon_one);
                    break;
                case 1:
                    iconIV.setImageResource(R.drawable.tab_icon_three);
                    break;
                case 2:
                    iconIV.setImageResource(R.drawable.tab_icon_four);
                    break;
//                case 3:
//                    iconIV.setImageResource(R.drawable.tab_icon_four);
//                    break;

            }
            TabLayout.Tab tab = mMainTL.newTab();
            tab.setCustomView(view);
            mMainTL.addTab(tab);
        }
        mFragmentList.add(new HomeFragment());
//        mFragmentList.add(new PackFragment());
        mFragmentList.add(new DeviceFragment());
        mFragmentList.add(new MineFragment());
        mMainAdapter = new VpFragmentAdapter(getSupportFragmentManager(), mFragmentList);

        mMainTL.setTabMode(TabLayout.MODE_FIXED);
        mMainTL.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.colorWhite));
        mMainTL.setTabTextColors(ContextCompat.getColor(mActivity, R.color.colorWhite), ContextCompat.getColor(mActivity, R.color.colorWhite));

        mMainVP.setAdapter(mMainAdapter);
        mMainVP.setCurrentItem(mCurrentPosition);
        mMainVP.setOffscreenPageLimit(4);
        mMainTL.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mMainVP.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mMainVP.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mMainTL));
        mMainVP.setCurrentItem(mCurrentPosition);
    }

    private void getVersion() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getVersion(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e(TAG, "response=" + response);
                DialogUtil.hideProgress();
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    if (code == 0) {
                        int versionCode = res.getInt("version");
                        JSONObject data = res.getJSONObject("data");
                        int versiononLocal = 0;
                        try {
                            //如果当前版本号小于线上最新版本,则显示更新弹窗
                            LogUtil.e(TAG, CommonUtil.getPackageInfo(mActivity).versionCode + "," + versionCode);
                            if (CommonUtil.getPackageInfo(mActivity).versionCode < versionCode) {
                                mDownloadUrl = res.getString("data");
                                showAppUpdatePopupWindow();
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(mActivity, getString(R.string.json_error));
                    LogUtil.e(TAG, e.getMessage());
                }

            }

            @Override
            public void onError(Throwable t) {
                LogUtil.e(TAG, "onError=" + t.getMessage());
                if (!NetworkUtil.isConnected()) {
                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
                } else {
                    ToastUtil.showShort(mActivity, getString(R.string.network_error));
                }
            }
        });
    }

    private void showAppUpdatePopupWindow() {
        if (mAppUpdatePopupWindow != null && mAppUpdatePopupWindow.isShowing()) {
            mAppUpdatePopupWindow.dismiss();
        } else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.popwindow_app_update, null, false);

            TextView downloadTV = (TextView) view.findViewById(R.id.tv_popwindow_pay_now_pay);
            TextView cancelTV=(TextView) view.findViewById(R.id.tv_popwindow_pay_cancel);
            cancelTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAppUpdatePopupWindow.dismiss();
                }
            });
            downloadTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mAppUpdatePopupWindow.dismiss();

                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//用户已拒绝过一次
                            //提示用户如果想要正常使用，要手动去设置中授权。
                            ToastUtil.showShort(mActivity, getString(R.string.prompt_open_read_write_permission));
                        } else {
                            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                            }, REQUEST_CODE_PERMISSION_WRITE_STORAGE);
                        }
                    } else {
                        downloadAPK();
                    }
                }
            });
            mAppUpdatePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mAppUpdatePopupWindow.showAsDropDown(view);
        }
    }


    private void downloadAPK() {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                //显示更新进度弹窗
                showUpdateProgressPopupWindow();
                //安装应用的逻辑
                AppUpdateService.start(mContext, Constant.APK_SAVE_PATH, mDownloadUrl, new AppUpdateService.OnDownloadProgressChangeListener() {

                    @Override
                    public void onProgressChange(final int progress) {
                        if (progress >= 100) {
                            mUpdateProgressPopupWindow.dismiss();
                        } else {
                            mUpdatePB.setProgress(progress);
                            mProgressTV.setText(progress + "%");
                        }
                    }
                });
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                startActivityForResult(intent, REQUEST_CODE_UNKNOWN_APP);
            }
        } else {
            //显示更新进度弹窗
            showUpdateProgressPopupWindow();
            //开启更新服务
            AppUpdateService.start(mContext, Constant.APK_SAVE_PATH, mDownloadUrl, new AppUpdateService.OnDownloadProgressChangeListener() {
                @Override
                public void onProgressChange(final int progress) {
                    if (progress >= 100) {
                        mUpdateProgressPopupWindow.dismiss();
                    } else {
                        mUpdatePB.setProgress(progress);
                        mProgressTV.setText(progress + "%");
                    }
                }
            });
        }
    }

    /**
     * 显示更新进度弹窗
     */
    private void showUpdateProgressPopupWindow() {
        if (mUpdateProgressPopupWindow != null && mUpdateProgressPopupWindow.isShowing()) {
            mUpdateProgressPopupWindow.dismiss();
        } else {
            View view = mInflater.inflate(R.layout.popwindow_update_progress, null);
            mProgressTV = view.findViewById(R.id.tv_popwindow_update_progress);
            mUpdatePB = view.findViewById(R.id.pb_popwindow_update_progress);
            mUpdatePB.setMax(100);
            TextView cancelTV = view.findViewById(R.id.tv_popwindow_update_progress_cancel);
            cancelTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.e(TAG, "cancelTV");
                    AppUpdateService.stop(mActivity);
                    mUpdateProgressPopupWindow.dismiss();
                }
            });
            mUpdateProgressPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mUpdateProgressPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UNKNOWN_APP) {
            downloadAPK();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_WRITE_STORAGE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToastUtil.showShort(mActivity, getString(R.string.prompt_open_read_write_permission));
                return;
            }
            downloadAPK();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateLoginStateEvent(UpdatePayStatusEvent event) {
        initTabData();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(mActivity);
        super.onDestroy();
    }
}
