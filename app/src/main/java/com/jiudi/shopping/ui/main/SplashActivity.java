package com.jiudi.shopping.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.UserBean;
import com.jiudi.shopping.global.Constant;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.service.AppUpdateService;
import com.jiudi.shopping.ui.user.account.LoginActivity;
import com.jiudi.shopping.util.CommonUtil;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.PushUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

public class SplashActivity extends BaseActivity {
    private android.widget.ImageView splashim;

    /**
     * APK下载URL
     */
    private String mDownloadUrl="http://mall.jiudicar.com/jiudi.apk";
    private PopupWindow mAppUpdatePopupWindow;
    private PopupWindow mUpdateProgressPopupWindow;
    private TextView mProgressTV;
    private ProgressBar mUpdatePB;
    private static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 1001;
    private static final int REQUEST_CODE_UNKNOWN_APP = 100;
    private static final int REQUEST_CODE_OPENCHAT = 60;
    private static final String TAG = "SplashActivity";

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

        splashim = (ImageView) findViewById(R.id.splashim);
    }

    @Override
    public void initData() {
        System.out.println("PushAppKey:"+ PushUtil.getAppKey(getApplicationContext()));
        System.out.println("PushReg:"+ JPushInterface.getRegistrationID(getApplicationContext()));
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                autoLogin();
//            }
//        }, 1000);
        if (sp.getBoolean("isfirstinstall", true)) {
            startActivity(new Intent(mActivity, GuideActivity.class));
            finish();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getVersion();
                }
            }, 1000);
        }
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
        Glide.with(this).asGif().load(R.drawable.splash_g).apply(options).into(splashim);


    }

    @Override
    public void initEvent() {

    }

    public void autoLogin() {
//        SPUtil.put("head","eyJhbGciOiJIUzI1NiIsInR5cCI6Ikp3dFNlcnZpY2UifQ.eyJpc3MiOiJhZG1pbiIsImlhdCI6MTU1OTE4NjAzMSwiZXhwIjoxNTYwOTYzMjMxLCJuYmYiOjE1NTkxODYwMzEsInN1YiI6ImxvY2FsaG9zdCIsImp0aSI6IjFkNzc2ODkwMGVmZWQ3ZmI5YzIwOTViMTQ1NzJhOGFkIiwidXNlcmlkIjo2fQ._a1vB58xx6PVY2KGrB7hK3C7cEU7NWZC3rwGxqBcSB0");
        if (!"".equals(SPUtil.get("head", ""))) {
            Log.v("Head",SPUtil.get("head", "").toString());
            autoLogin(true);
        } else {
            startActivity(new Intent(mActivity, LoginActivity.class));
            finish();
        }
    }

    private void autoLogin(boolean b) {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getPersonalDate(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        JSONObject jsonObject=res.getJSONObject("data").optJSONObject("user_info");
                        if(jsonObject!=null){
                            UserBean bean=new UserBean();
                            bean.uid=jsonObject.optString("uid");
                            bean.account=jsonObject.optString("account");
                            bean.pwd=jsonObject.optString("pwd");
                            bean.nickname=jsonObject.optString("nickname");
                            bean.avatar=jsonObject.optString("avatar");
                            bean.phone=jsonObject.optString("phone");
                            bean.add_time=jsonObject.optString("add_time");
                            bean.add_ip=jsonObject.optString("add_ip");
                            bean.last_time=jsonObject.optString("last_time");
                            bean.last_ip=jsonObject.optString("last_ip");
                            bean.now_money=jsonObject.optString("now_money");
                            bean.integral=jsonObject.optString("integral");
                            bean.status=jsonObject.optString("status");
                            bean.level=jsonObject.optString("level");
                            bean.spread_uid=jsonObject.optString("spread_uid");
                            bean.agent_id=jsonObject.optString("agent_id");
                            bean.user_type=jsonObject.optString("user_type");
                            bean.is_promoter=jsonObject.optString("is_promoter");
                            bean.pay_count=jsonObject.optString("pay_count");
                            bean.direct_num=jsonObject.optString("direct_num");
                            bean.team_num=jsonObject.optString("team_num");
                            bean.is_reward=jsonObject.optString("is_reward");
                            bean.allowance_number=jsonObject.optString("allowance_number");
                            try {
                                JSONArray array=res.getJSONObject("data").getJSONArray("coupon_num");
                                bean.coupon_num=array.length()+"";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONObject orderStatusNum=res.getJSONObject("data").getJSONObject("orderStatusNum");
                                bean.noBuy=orderStatusNum.optInt("noBuy");
                                bean.noPostage=orderStatusNum.optInt("noPostage");
                                bean.noTake=orderStatusNum.optInt("noTake");
                                bean.noReply=orderStatusNum.optInt("noReply");
                                bean.noPink=orderStatusNum.optInt("noPink");
                                bean.noBuy=orderStatusNum.optInt("noBuy");
                                bean.noPostage=orderStatusNum.optInt("noPostage");
                                bean.noTake=orderStatusNum.optInt("noTake");
                                bean.noReply=orderStatusNum.optInt("noReply");
                                bean.noPink=orderStatusNum.optInt("noPink");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            AccountManager.sUserBean=bean;
                            startActivity(new Intent(mActivity, MainNewActivity.class));
                            finish();
                        }else{
                            AccountManager.sUserBean=null;
                            startActivity(new Intent(mActivity, LoginActivity.class));
                            finish();
                        }
                    }else{
                        AccountManager.sUserBean=null;
                        startActivity(new Intent(mActivity, LoginActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    AccountManager.sUserBean=null;
                    startActivity(new Intent(mActivity, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onError(Throwable t) {
                AccountManager.sUserBean=null;
                startActivity(new Intent(mActivity, LoginActivity.class));
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UNKNOWN_APP) {
            downloadAPK();
        }
        if (requestCode == REQUEST_CODE_OPENCHAT) {
            //说明客服关闭了
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
    private void getVersion() {
        Map<String, String> map = new HashMap<>();
        map.put("type","1");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getVersion(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e(TAG, "response=" + response);
                DialogUtil.hideProgress();
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        try {
                            int newversionCode = res.getInt("data");
                            int nowversionCode=CommonUtil.getPackageInfo(mActivity).versionCode;
                            if(newversionCode>nowversionCode){
                                showAppUpdatePopupWindow();
                            }else{

                                autoLogin();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            autoLogin();
                        }
//                        try {
//                            //如果当前版本号小于线上最新版本,则显示更新弹窗
//                            LogUtil.e(TAG, CommonUtil.getPackageInfo(mActivity).versionCode + "," + versionCode);
//                            if (CommonUtil.getPackageInfo(mActivity).versionCode < versionCode) {
//
//                            }
//                        } catch (PackageManager.NameNotFoundException e) {
//                            e.printStackTrace();
//                        }

                    }else{

                        autoLogin();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    autoLogin();
//                    ToastUtil.showShort(mActivity, getString(R.string.json_error));
//                    LogUtil.e(TAG, e.getMessage());
                }


            }

            @Override
            public void onError(Throwable t) {
                autoLogin();
//                LogUtil.e(TAG, "onError=" + t.getMessage());
//                if (!NetworkUtil.isConnected()) {
//                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
//                } else {
//                    ToastUtil.showShort(mActivity, getString(R.string.network_error));
//                }

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
                    autoLogin();
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
                    autoLogin();
                }
            });
            mUpdateProgressPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mUpdateProgressPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
        }
    }
}
