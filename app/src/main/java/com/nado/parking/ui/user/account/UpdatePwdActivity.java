package com.nado.parking.ui.user.account;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nado.parking.R;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.manager.AccountManager;
import com.nado.parking.manager.RequestManager;
import com.nado.parking.net.RetrofitCallBack;
import com.nado.parking.net.RetrofitRequestInterface;
import com.nado.parking.util.CommonUtil;
import com.nado.parking.util.DialogUtil;
import com.nado.parking.util.LogUtil;
import com.nado.parking.util.NetworkUtil;
import com.nado.parking.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdatePwdActivity extends BaseActivity {
    private static final String TAG = "UpdatePwdActivity";

    private LinearLayout mBackLL;
    private TextView mTitleTV;

    private TextView mConfirmTV;

    private TextView mGetCodeTV;
    private CountDownTimer mTimer;
    private EditText mCodeET;

    private EditText mPasswordET;
    private EditText mPhoneNumberET;

    private ImageView mShowPwdIV;
    private boolean mShowPwd = false;

    private static final String EXTRA_TYPE = "type_password";
    public static final String EXTRA_FORGET = "forget_password";
    public static final String EXTRA_UPDATE = "update_password";
    private String mType;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_update_pwd;
    }

    @Override
    public void initView() {
        mBackLL = byId(R.id.ll_layout_top_back_bar_back);
        mTitleTV = byId(R.id.tv_layout_top_back_bar_title);

        mConfirmTV = byId(R.id.tv_activity_update_pwd_save);

        mGetCodeTV = byId(R.id.tv_activity_update_pwd_get_code);
        mCodeET = byId(R.id.et_activity_update_pwd_input_code);
        mPasswordET = byId(R.id.et_activity_update_pwd_input_pwd);
        mPhoneNumberET = byId(R.id.et_activity_update_pwd_input_phone);
        mShowPwdIV = byId(R.id.iv_activity_update_show_pwd);
        mTitleTV.setText("修改密码");
    }

    @Override
    public void initData() {
        mType = getIntent().getStringExtra(EXTRA_TYPE);

        SpannableString span = new SpannableString(getString(R.string.edit_text_input_null));
        ForegroundColorSpan fcs = new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.colorFontGray5));
        span.setSpan(fcs, 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mPhoneNumberET.setHint(new SpannableString(span));

        if (mType != null) {
            switch (mType) {
                case EXTRA_FORGET:
                    mTitleTV.setText(getString(R.string.forget_password));
                    mConfirmTV.setText(getString(R.string.complete));
                    break;
                case EXTRA_UPDATE:
                    mTitleTV.setText(getString(R.string.update_password));
                    mConfirmTV.setText(getString(R.string.confirm_update));
                    mPhoneNumberET.setText(AccountManager.sUserBean.getTelNumber());
//                    mPhoneNumberET.setT
                    mPhoneNumberET.setEnabled(false);
                    mPhoneNumberET.setFocusable(false);
                    break;
            }
        }

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

        mGetCodeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhoneNumberET.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_phone_num));
                } else if (!CommonUtil.isPhone(phone)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_right_phone_nun));
                } else {
                    getCode(phone);
                }
            }
        });

        mConfirmTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhoneNumberET.getText().toString().trim();
                String code = mCodeET.getText().toString().trim();
                String pwd = mPasswordET.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_phone_num));
                } else if (TextUtils.isEmpty(code)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_verify_code));
                } else if (TextUtils.isEmpty(pwd)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_password));
                } else if (!CommonUtil.isPassword(pwd)) {
                    ToastUtil.showShort(mContext, getString(R.string.password_rule));
                } else {
                    switch (mType) {
                        case EXTRA_FORGET:
                            DialogUtil.showProgress(mActivity, getString(R.string.again_pass_word));
                            againPwd(phone, code, pwd);
                            break;
                        case EXTRA_UPDATE:
                            DialogUtil.showProgress(mActivity, getString(R.string.changing_pass_word));
                            updatePwd(code, pwd);
                            break;
                    }
                }
            }
        });
    }

    /**
     * 重置密码
     *
     * @param code
     * @param password
     */
    private void againPwd(final String phone, final String code, final String password) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        map.put("new_pass", password);
        LogUtil.e(TAG, map.toString());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).forgetPassword(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e(TAG, response);
                DialogUtil.hideProgress();
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        ToastUtil.showShort(mActivity, "密码重置成功");
//                        if (AccountManager.sUserBean != null) {
//                            AccountManager.logout(mActivity);
//                        }
                        finish();
                    } else {
                        ToastUtil.showShort(mActivity, info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(mActivity, getString(R.string.data_error));
                    LogUtil.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onError(Throwable t) {
                LogUtil.e(TAG, t.getMessage());
                DialogUtil.hideProgress();
                if (!NetworkUtil.isConnected()) {
                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
                } else {
                    ToastUtil.showShort(mActivity, getString(R.string.network_error));
                }
            }
        });

    }

    /**
     * 修改密码
     */
    private void updatePwd(final String code, final String password) {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("code", code);
        map.put("new_pass", password);
        LogUtil.e(TAG, map.toString());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).updatePassword(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {

            @Override
            public void onSuccess(String response) {
                DialogUtil.hideProgress();
                LogUtil.e(TAG, response);
                DialogUtil.hideProgress();
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        ToastUtil.showShort(mActivity, "密码修改成功");
//                        if (AccountManager.sUserBean != null) {
//                            AccountManager.logout(mActivity);
//                        }
                        finish();
                    } else {
                        ToastUtil.showShort(mActivity, info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(mActivity, getString(R.string.data_error));
                    LogUtil.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onError(Throwable t) {
                DialogUtil.hideProgress();
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
    private void getCode(String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        LogUtil.e(TAG, map.toString());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).sendUpdatePasswordCode(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                DialogUtil.hideProgress();
                LogUtil.e(TAG, response);
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        mTimer.start();
                        ToastUtil.showShort(mActivity, getString(R.string.send_code_success));
                    } else {
                        ToastUtil.showShort(mActivity, info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(mActivity, getString(R.string.data_error));
                    LogUtil.e(TAG, e.getMessage());
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

    public static void open(Activity activity, String type) {
        Intent intent = new Intent(activity, UpdatePwdActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        activity.startActivity(intent);
    }
}
