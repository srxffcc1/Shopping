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
    private static final String TAG = "RegisterActivity";
    private ImageView mBackLL;
    private EditText mPhoneET;

    private EditText mPwdET;
    private EditText mCodeET;
    private TextView mGetCodeTV;
    private CountDownTimer mTimer;
    private TextView mRegisterTV;
    private PopupWindow mSexPopwindow;
    private RecyclerView mSexRV;
    private RecyclerCommonAdapter<TypeBean> mSexAdapter;
    private List<TypeBean> mSexList = new ArrayList<>();
    private TypeBean mTempSex;
    private ImageView mShowPwdIV;
    private boolean mShowPwd = false;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }
    @Override
    public boolean isNoNeedLogin() {
        return true;
    }
    @Override
    public void initView() {
        mBackLL = byId(R.id.iv_activity_register_close);
        mPhoneET = byId(R.id.et_activity_register_phone);

        mPwdET = byId(R.id.et_activity_register_pwd);
        mCodeET = byId(R.id.et_activity_register_code);
        mGetCodeTV = byId(R.id.tv_activity_register_get_code);

        mRegisterTV = byId(R.id.tv_activity_login_login_login);
        mShowPwdIV = byId(R.id.iv_activity_register_show_pwd);
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
        showBranchList();
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
                    mPwdET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mShowPwdIV.setImageResource(R.drawable.eye_close_white);
                } else {
                    mPwdET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mShowPwdIV.setImageResource(R.drawable.eye_open_white);
                }
                mShowPwd = !mShowPwd;
                mPwdET.setSelection(mPwdET.getText().toString().length());
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
                } else {
                    mTimer.start();
                    getCode(mPhoneET.getText().toString().trim());
                }
            }
        });


        mRegisterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhoneET.getText().toString().trim();
                String pwd = mPwdET.getText().toString().trim();
                String code = mCodeET.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_phone_num));
                } else if (!CommonUtil.isPhone(phone)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_right_phone_nun));
                } else if (TextUtils.isEmpty(pwd)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_password));
                } else if (!CommonUtil.isPassword(pwd)) {
                    ToastUtil.showShort(mContext, getString(R.string.password_rule));
                }  else if (TextUtils.isEmpty(code)) {
                    ToastUtil.showShort(mContext, getString(R.string.please_input_verify_code));
                }else {
                    regsiter(phone, code, pwd);
                }
            }
        });

    }

    private void getCode(final String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        LogUtil.e(TAG, map.toString());
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .getCode(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        DialogUtil.hideProgress();
                        LogUtil.e(TAG, response);
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
                        LogUtil.e(TAG, t.getMessage());
                        if (!NetworkUtil.isConnected()) {
                            ToastUtil.showShort(mActivity, getString(R.string.net_error));
                        } else {
                            ToastUtil.showShort(mActivity, getString(R.string.network_error));
                        }
                    }
                });
    }



    private void showBranchList() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .showBranchList(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        DialogUtil.hideProgress();
                        LogUtil.e(TAG, response);
                        try {
                            JSONObject res = new JSONObject(response);
                            int code = res.getInt("code");
                            String info = res.getString("info");

                            if (code == 0) {
                                JSONObject data=res.getJSONObject("data");
                                JSONArray branchList=data.getJSONArray("branchList");
                                mSexList.clear();
                                for (int i=0;i<branchList.length();i++){
                                    TypeBean shop=new TypeBean();
                                    JSONObject shopOb=branchList.getJSONObject(i);
                                    shop.setName(shopOb.getString("branchName"));
                                    shop.setId(shopOb.getString("branchId"));
                                    mSexList.add(shop)           ;
                                    if (i==0){
                                        mTempSex = shop;

                                    }
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Throwable t) {
                        DialogUtil.hideProgress();
                        LogUtil.e(TAG, t.getMessage());
                        if (!NetworkUtil.isConnected()) {
                            ToastUtil.showShort(mActivity, getString(R.string.net_error));
                        } else {
                            ToastUtil.showShort(mActivity, getString(R.string.network_error));
                        }
                    }
                });
    }



    /**
     * 注册
     */
    private void regsiter(final String phone, final String code, final String password) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        map.put("pass", password);
        map.put("client", "1");//客户端类别 android为1，ios为2
        LogUtil.e(TAG, map.toString());
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .register(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {

                    @Override
                    public void onSuccess(String response) {
                        DialogUtil.hideProgress();
                        LogUtil.e(TAG, response);
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
}
