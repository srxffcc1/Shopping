package com.jiudi.shopping.ui.user.account;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.event.UpdateLoginStateEvent;
import com.jiudi.shopping.global.Constant;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.CommonUtil;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPwdActivity extends BaseActivity {
    private LinearLayout mBackLL;
    private TextView mTitleTV;

    private TextView mTitleRightTV;

    private EditText mPhoneET;

    private TextView mGetCodeTV;
    private CountDownTimer mTimer;
    private EditText mCodeET;

    private EditText mPasswordET;
    private EditText mPasswordConfirmET;
    private String TAG = "ForgetPwdActivity";
    private ImageView mShowPwdIV;
    private ImageView mShowConfirmPwdIV;
    private boolean mShowPwd = false;
    private boolean mConfirmShowPwd = false;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    public void initView() {
        mBackLL = byId(R.id.ll_layout_top_back_bar_back);
        mTitleTV = byId(R.id.tv_layout_top_back_bar_title);
        mTitleTV.setText(getString(R.string.forget_password));
        mTitleRightTV = byId(R.id.tv_activity_update_pwd_save);

        mGetCodeTV = byId(R.id.tv_activity_update_pwd_get_code);
        mCodeET = byId(R.id.et_activity_update_pwd_input_code);
        mPasswordET = byId(R.id.et_activity_update_pwd_input_pwd);
        mPasswordConfirmET = byId(R.id.et_activity_update_pwd_input_pwd_confirm);
        mPhoneET = byId(R.id.et_activity_update_pwd_input_phone);
        mShowPwdIV = byId(R.id.iv_activity_forget_show_pwd);
        mShowConfirmPwdIV = byId(R.id.iv_activity_forget_show_confirm_pwd);
    }

    @Override
    public void initData() {
        mTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mGetCodeTV.setClickable(false);
                mGetCodeTV.setText(millisUntilFinished / 1000 + " S");
            }
            @Override
            public void onFinish() {
                mGetCodeTV.setClickable(true);
                mGetCodeTV.setText(R.string.get_verify_code);
            }
        };

        changeClick();
    }



    @Override
    public void initEvent() {
        mBackLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mShowPwdIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowPwd) {
                    mPasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mShowPwdIV.setImageResource(R.drawable.eye_close_gray);
                } else {
                    mPasswordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mShowPwdIV.setImageResource(R.drawable.eye_open_gray);
                }
                mShowPwd = !mShowPwd;
                mPasswordET.setSelection(mPasswordET.getText().toString().length());
            }
        });

        mShowConfirmPwdIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConfirmShowPwd) {
                    mPasswordConfirmET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mShowConfirmPwdIV.setImageResource(R.drawable.eye_close_gray);
                } else {
                    mPasswordConfirmET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mShowConfirmPwdIV.setImageResource(R.drawable.eye_open_gray);
                }
                mConfirmShowPwd = !mConfirmShowPwd;
                mPasswordConfirmET.setSelection(mPasswordConfirmET.getText().toString().length());
            }
        });


        mGetCodeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhoneET.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_phone_num));
                } else if (!CommonUtil.isPhone(phone)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_right_phone_nun));
                }else {
                    getCode(phone);
                }


            }
        });

        mTitleRightTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mCodeET.getText().toString().trim();
                String pwd = mPasswordET.getText().toString().trim();
                String confirmPwd = mPasswordConfirmET.getText().toString().trim();
                String phone = mPhoneET.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_phone_num));
                } else if (!CommonUtil.isPhone(phone)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_right_phone_nun));
                }else if (TextUtils.isEmpty(code)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_verify_code));
                } else if (TextUtils.isEmpty(pwd)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_password));
                } else if (!CommonUtil.isPassword(pwd)) {
                    ToastUtil.showShort(mContext, getString(R.string.password_rule));
                }else if (TextUtils.isEmpty(confirmPwd)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_confirm_password));
                } else if (!CommonUtil.isPassword(confirmPwd)) {
                    ToastUtil.showShort(mContext, getString(R.string.password_rule));
                } else {
                    DialogUtil.showProgress(mActivity, "正在修改密码");
                    updatePwd(phone,code, pwd);
                }
            }
        });


    }



    private void changeClick(){
        mPhoneET.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                checkPhone();
                checkForgetPwd();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听
                checkPhone();
                checkForgetPwd();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                checkPhone();
                checkForgetPwd();
            }
        });


        mCodeET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                checkForgetPwd();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听
                checkForgetPwd();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                Log.e("输入结束执行该方法", "输入结束");
                checkForgetPwd();
            }
        });


        mPasswordET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                checkForgetPwd();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听
                checkForgetPwd();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                checkForgetPwd();
            }
        });

        mPasswordConfirmET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                checkForgetPwd();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听
                checkForgetPwd();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                checkForgetPwd();
            }
        });
    }

    /**
     * 监听获取验证码的显示
     */
    private void checkPhone(){
        String phone = mPhoneET.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || !CommonUtil.isPhone(phone)) {
            mGetCodeTV.setTextColor(ContextCompat.getColor(mActivity,R.color.colorgray));
            mGetCodeTV.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.selector_gray_corner_0));
        } else {
            mGetCodeTV.setTextColor(ContextCompat.getColor(mActivity,R.color.colorWhite));
            mGetCodeTV.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.selector_red_corner_0));
        }
    }

    /**
     * 监听重置密码的显示
     */
    private void checkForgetPwd(){
        String phone = mPhoneET.getText().toString().trim();
        String pwd = mPasswordET.getText().toString().trim();
        String confirmPwd = mPasswordConfirmET.getText().toString().trim();
        String code = mCodeET.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || !CommonUtil.isPhone(phone) || TextUtils.isEmpty(code) || TextUtils.isEmpty(pwd) || !CommonUtil.isPassword(pwd) || TextUtils.isEmpty(confirmPwd) || !CommonUtil.isPassword(confirmPwd)) {

            mTitleRightTV.setTextColor(ContextCompat.getColor(mActivity,R.color.colorgray));
            mTitleRightTV.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.selector_gray_corner_4));
        } else {
            mTitleRightTV.setTextColor(ContextCompat.getColor(mActivity,R.color.colorWhite));
            mTitleRightTV.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.selector_red_corner_4));
        }
    }

    /**
     * 修改密码
     */
    private void updatePwd(final String phone,final String code, final String password) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("pass", password);
        map.put("code", code);
        LogUtil.e(TAG,map.toString());
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .forgetPassword(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {

                    @Override
                    public void onSuccess(String s) {
                        DialogUtil.hideProgress();
                        LogUtil.e(TAG,s);
                        try {
                            JSONObject response = new JSONObject(s);
                            int code = response.getInt("code");
                            String info = response.getString("info");
                            if (code == 0) {
                                ToastUtil.showShort(mActivity, getString(R.string.update_password_success));
                                SPUtil.remove(Constant.USER);
                                AccountManager.logout(mActivity);
                                EventBus.getDefault().post(new UpdateLoginStateEvent());
                                startActivity(new Intent(mActivity, LoginActivity.class));
                                finish();
                            } else {
                                ToastUtil.showShort(mActivity, info);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showShort(mActivity, getString(R.string.data_error));
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (!NetworkUtil.isConnected()) {
                            ToastUtil.showShort(mActivity, getString(R.string.net_error));
                        } else {
                            ToastUtil.showShort(mActivity, getString(R.string.network_error));
                        }
                    }
                });




    }
    /**
     * 获取验证码
     */
    private void getCode(final String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .sendUpdatePasswordCode(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {

                    @Override
                    public void onSuccess(String s) {
                        DialogUtil.hideProgress();
                        LogUtil.e(TAG, s);
                        try {
                            JSONObject response = new JSONObject(s);
                            int code = response.getInt("code");
                            String info = response.getString("info");
                            if (code == 0) {
                                mTimer.start();
                                ToastUtil.showShort(mActivity, getString(R.string.send_code_success));
                            } else {
                                ToastUtil.showShort(mActivity, info);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showShort(mActivity, getString(R.string.data_error));
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (!NetworkUtil.isConnected()) {
                            ToastUtil.showShort(mActivity,  getString(R.string.net_error));
                        } else {
                            ToastUtil.showShort(mActivity, getString(R.string.network_error));
                        }
                    }
                });
    }
}
