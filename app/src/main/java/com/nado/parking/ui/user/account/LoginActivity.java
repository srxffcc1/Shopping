package com.nado.parking.ui.user.account;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nado.parking.R;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.bean.UserBean;
import com.nado.parking.event.UpdateLoginStateEvent;
import com.nado.parking.global.Constant;
import com.nado.parking.manager.AccountManager;
import com.nado.parking.manager.RequestManager;
import com.nado.parking.net.RetrofitCallBack;
import com.nado.parking.net.RetrofitRequestInterface;
import com.nado.parking.ui.main.MainActivity;
import com.nado.parking.util.CommonUtil;
import com.nado.parking.util.DialogUtil;
import com.nado.parking.util.LogUtil;
import com.nado.parking.util.NetworkUtil;
import com.nado.parking.util.SPUtil;
import com.nado.parking.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Steven on 2017/12/21.
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    private EditText mPhoneET;
    private EditText mPasswordET;

    private ImageView mShowPwdIV;
    private boolean mShowPwd = false;
    private TextView mQuickRegisterTV;
    private TextView mForgetPwdTV;
    private TextView mLoginTV;

    private ImageView mBackIV;
    private long exitTime = 0;

    @Override
    public boolean isLoginActivity() {
        return true;
    }

    @Override
    protected void beforeSetContentView() {
        super.beforeSetContentView();
        if (AccountManager.sUserBean!=null){
            startActivity(new Intent(mActivity,MainActivity.class));
            finish();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

//    @Override
//    public void onBackPressed() {
//        exit();
//    }

//    public void exit() {
//        if ((System.currentTimeMillis() - exitTime) > 2000) {
//            Toast.makeText(getApplicationContext(), getString(R.string.exit_app), Toast.LENGTH_SHORT).show();
//            exitTime = System.currentTimeMillis();
//        } else {
//            finish();
//        }
//    }


    @Override
    public void initView() {
        mPhoneET = byId(R.id.et_activity_login_phone);
        mPasswordET = byId(R.id.et_activity_login_password);
        mShowPwdIV = byId(R.id.iv_activity_login_show_pwd);
        mQuickRegisterTV = byId(R.id.tv_activity_login_quick_register);
        mForgetPwdTV = byId(R.id.tv_activity_login_forget_password);
        mLoginTV = byId(R.id.tv_activity_login_login_login);
        mBackIV=byId(R.id.iv_activity_login_back);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        mShowPwdIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowPwd) {
                    mPasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mShowPwdIV.setImageResource(R.drawable.eye_close_white);
                } else {
                    mPasswordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mShowPwdIV.setImageResource(R.drawable.eye_open_white);
                }
                mShowPwd = !mShowPwd;
                mPasswordET.setSelection(mPasswordET.getText().toString().length());
            }
        });

        mQuickRegisterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(mContext,RegisterActivity.class));
            }
        });

        mForgetPwdTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePwdActivity.open(mActivity,UpdatePwdActivity.EXTRA_FORGET);
            }
        });

        mLoginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhoneET.getText().toString().trim();
                String password = mPasswordET.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_phone_num));
                } else if (!CommonUtil.isPhone(phone)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_right_phone_nun));
                } else if (TextUtils.isEmpty(password)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_password));
                } else {
                    login(phone,password);
                }
            }
        });
        mBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                finish();
            }
        });

    }

    /**
     * 登录
     * @param phone
     * @param password
     */
    private void login(final String phone, final String password) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("pass", password);
        System.out.println("登录");
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .login(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {

                    @Override
                    public void onSuccess(String response) {
                        DialogUtil.hideProgress();
                        LogUtil.e(TAG, response);
                        try {
                            JSONObject res = new JSONObject(response);
                            int code = res.getInt("code");
                            String info = res.getString("info");
                            if (code==0) {
                                ToastUtil.showShort(mActivity, getString(R.string.login_success));
                                JSONObject data = res.getJSONObject("data");
                                AccountManager.sUserBean = new UserBean();
                                AccountManager.sUserBean.setId(data.getString("id"));
                                AccountManager.sUserBean.setTelNumber(phone);
                                AccountManager.sUserBean.setPassWord(password);
                                AccountManager.sUserBean.setNickName(data.getString("nicename"));
                                AccountManager.sUserBean.setHeadPortrait(data.getString("avatar"));

                                AccountManager.sUserBean.id=data.getString("id");
                                AccountManager.sUserBean.phone=data.getString("phone");
                                AccountManager.sUserBean.nicename=data.getString("nicename");
                                AccountManager.sUserBean.obd_pass=data.getString("obd_pass");
                                AccountManager.sUserBean.avatar=data.getString("avatar");
                                AccountManager.sUserBean.obd_macid=data.getString("obd_macid");
                                AccountManager.sUserBean.obd_id=data.getString("obd_id");
                                AccountManager.sUserBean.obd_mds=data.getString("obd_mds");
                                AccountManager.sUserBean.obd_user_id=data.getString("obd_user_id");
                                AccountManager.sUserBean.obd_isbind=data.getString("obd_isbind");
                                AccountManager.sUserBean.jpush_id=data.getString("jpush_id");

                                String userBase64 = CommonUtil.objectToBase64(AccountManager.sUserBean);
                                SPUtil.put(Constant.USER, userBase64);
                                EventBus.getDefault().post(new UpdateLoginStateEvent());
                                Intent intent = new Intent(mActivity, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else  {
                                ToastUtil.showShort(mContext, info);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.e(TAG,t.toString());
                        if (!NetworkUtil.isConnected()) {
                            ToastUtil.showShort(mContext, getString(R.string.net_error));
                        } else {
                            ToastUtil.showShort(mContext, getString(R.string.network_error));
                        }
                        LogUtil.e(TAG,t.toString());
                    }
                });
    }
}
