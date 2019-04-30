package com.jiudi.shopping.ui.user.account;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.TypeBean;
import com.jiudi.shopping.bean.UserBean;
import com.jiudi.shopping.global.Constant;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.main.MainActivity;
import com.jiudi.shopping.util.CommonUtil;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends BaseActivity {

    private ImageView back;
    private android.widget.LinearLayout phoneL;
    private EditText phone;
    private android.widget.LinearLayout yanzhengmaL;
    private EditText yanzhengma;
    private TextView yanzhengmaT;
    private android.widget.LinearLayout pwdL;
    private EditText pwd;
    private ImageView pwdI;
    private TextView register;
    private CountDownTimer mTimer;
    private boolean mShowPwd = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {

        back = (ImageView) findViewById(R.id.back);
        phoneL = (LinearLayout) findViewById(R.id.phone_l);
        phone = (EditText) findViewById(R.id.phone);
        yanzhengmaL = (LinearLayout) findViewById(R.id.yanzhengma_l);
        yanzhengma = (EditText) findViewById(R.id.yanzhengma);
        yanzhengmaT = (TextView) findViewById(R.id.yanzhengma_t);
        pwdL = (LinearLayout) findViewById(R.id.pwd_l);
        pwd = (EditText) findViewById(R.id.pwd);
        pwdI = (ImageView) findViewById(R.id.pwd_i);
        register = (TextView) findViewById(R.id.register);
    }
    @Override
    public void initEvent() {
        yanzhengmaT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phones = phone.getText().toString().trim();
                if (TextUtils.isEmpty(phones)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_phone_num));
                } else if (!CommonUtil.isPhone(phones)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_right_phone_nun));
                } else {
                    mTimer.start();
                    getCode(phone.getText().toString().trim());
                }
            }
        });
        pwdI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowPwd) {
                    pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pwdI.setImageResource(R.drawable.eye_close_white);
                } else {
                    pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pwdI.setImageResource(R.drawable.eye_open_white);
                }
                mShowPwd = !mShowPwd;
                pwd.setSelection(pwd.getText().toString().length());
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phones = phone.getText().toString().trim();
                String pwds = pwd.getText().toString().trim();
                String codes = yanzhengma.getText().toString().trim();
                if (TextUtils.isEmpty(phones)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_phone_num));
                } else if (!CommonUtil.isPhone(phones)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_right_phone_nun));
                } else if (TextUtils.isEmpty(pwds)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_password));
                } else if (!CommonUtil.isPassword(pwds)) {
                    ToastUtil.showShort(mContext, getString(R.string.password_rule));
                }  else if (TextUtils.isEmpty(codes)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_verify_code));
                }else {
                    regsiter(phones, codes, pwds);
                }
            }
        });

    }
    @Override
    public void initData() {
        mTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                yanzhengmaT.setClickable(false);
                yanzhengmaT.setText(millisUntilFinished / 1000 + " S");
            }

            @Override
            public void onFinish() {
                yanzhengmaT.setClickable(true);
                yanzhengmaT.setText(R.string.get_verify_code);
            }
        };
    }
    private void regsiter(final String phone, final String code, final String password) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        map.put("pwd", password);
        map.put("client", "1");//客户端类别 android为1，ios为2
        RequestManager.mRetrofitManager3
                .createRequest(RetrofitRequestInterface.class)
                .register(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {

                    @Override
                    public void onSuccess(String response) {
                        DialogUtil.hideProgress();
                        try {

                            JSONObject res = new JSONObject(response);
                            int code = res.getInt("code");
                            String info = res.getString("info");
                            ToastUtil.showShort(mContext, info);
                            if (code == 0) {
                                JSONObject data = res.getJSONObject("data");
                                AccountManager.sUserBean = new UserBean();
                                AccountManager.sUserBean.setId(data.getString("id"));
                                AccountManager.sUserBean.setTelNumber(phone);
                                AccountManager.sUserBean.setPassWord(password);
                                AccountManager.sUserBean.setNickName(data.getString("nicename"));
                                AccountManager.sUserBean.setHeadPortrait(data.getString("avatar"));
                                String userBase64 = CommonUtil.objectToBase64(AccountManager.sUserBean);
                                SPUtil.put(Constant.USER, userBase64);
                                Intent intent = new Intent(mActivity, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void getCode(final String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        RequestManager.mRetrofitManager3
                .createRequest(RetrofitRequestInterface.class)
                .getCode(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        DialogUtil.hideProgress();
                        try {
                            JSONObject res = new JSONObject(response);
                            int code = res.getInt("code");
                            String info = res.getString("info");

                            if (code == 0) {
                                ToastUtil.showShort(mContext, info);

                            } else if (code == 1) {
                                ToastUtil.showShort(mContext, info);
                            } else if (code == 2) {
                                ToastUtil.showShort(mContext, info);
                            } else {
                                ToastUtil.showShort(mContext, info);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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


}
