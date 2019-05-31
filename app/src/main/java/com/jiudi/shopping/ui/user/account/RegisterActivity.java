package com.jiudi.shopping.ui.user.account;

import android.app.Activity;
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
import android.widget.Toast;

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
import com.jiudi.shopping.ui.main.MainNewOldActivity;
import com.jiudi.shopping.util.CommonUtil;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.MD5Util;
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
    private LinearLayout ycodeL;
    private EditText ycode;
    private int type=0;//1账号密码注册2验证码登录注册3微信信息绑定

    @Override
    public boolean isNoNeedLogin() {
        return true;
    }
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
        ycodeL = (LinearLayout) findViewById(R.id.ycode_l);
        ycode = (EditText) findViewById(R.id.ycode);
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
        type=getIntent().getIntExtra("type",0);
        if(type==1){
            phone.setText(AccountManager.sUserBean.phone);
            pwdL.setVisibility(View.VISIBLE);
        }
        if(type==2){
            phone.setText(AccountManager.sUserBean.phone);
        }

    }
    private void regsiter(final String phone, final String code, final String password) {
        Map<String, String> map = new HashMap<>();

        if(type==1){
            map.put("invite_code",ycode.getText().toString());
            map.put("phone", phone);
            map.put("code", code);
            map.put("pwd",  MD5Util.getMD5Str(password));
        }else{
            map.put("invite_code",ycode.getText().toString());
            map.put("phone", phone);
            map.put("code", code);
            map.put("passwd", MD5Util.getMD5Str(password));
            map.put("type",type+"");
        }
        if(type==1){
            RequestManager.mRetrofitManager
                    .createRequest(RetrofitRequestInterface.class)
                    .register(RequestManager.encryptParams(map))
                    .enqueue(new RetrofitCallBack() {

                        @Override
                        public void onSuccess(String response) {
                            DialogUtil.hideProgress();
                            try {

                                JSONObject res = new JSONObject(response);
                                int code = res.getInt("code");
                                String info = res.getString("msg");
                                ToastUtil.showShort(mContext, info);
                                if (code == 200) {
//                                JSONObject data = res.getJSONObject("data");
//                                AccountManager.sUserBean = new UserBean();
//                                AccountManager.sUserBean.account=phone;
//                                AccountManager.sUserBean.passwd=password;

//                                AccountManager.sUserBean.setId(data.getString("id"));
//                                AccountManager.sUserBean.setTelNumber(phone);
//                                AccountManager.sUserBean.setPassWord(password);
//                                AccountManager.sUserBean.setNickName(data.getString("nicename"));
//                                AccountManager.sUserBean.setHeadPortrait(data.getString("avatar"));

                                    Toast.makeText(mActivity,res.getString("data"),Toast.LENGTH_SHORT).show();
                                    String userBase64 = CommonUtil.objectToBase64(AccountManager.sUserBean);
                                    SPUtil.put(Constant.USER, userBase64);
//                                    Intent intent = new Intent(mActivity, LoginActivity.class);
////                                    startActivity(intent);
//                                    finish();
                                    login();
                                }else{
                                    Toast.makeText(mActivity,info,Toast.LENGTH_SHORT).show();
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
        }else{
            RequestManager.mRetrofitManager
                    .createRequest(RetrofitRequestInterface.class)
                    .savePhone(SPUtil.get("head2", "").toString(),RequestManager.encryptParams(map))
                    .enqueue(new RetrofitCallBack() {

                        @Override
                        public void onSuccess(String response) {
                            DialogUtil.hideProgress();
                            try {

                                JSONObject res = new JSONObject(response);
                                int code = res.getInt("code");
                                String info = res.getString("msg");
                                ToastUtil.showShort(mContext, info);
                                if (code == 200) {
//                                JSONObject data = res.getJSONObject("data");
//                                AccountManager.sUserBean = new UserBean();
//                                AccountManager.sUserBean.account=phone;
//                                AccountManager.sUserBean.passwd=password;

//                                AccountManager.sUserBean.setId(data.getString("id"));
//                                AccountManager.sUserBean.setTelNumber(phone);
//                                AccountManager.sUserBean.setPassWord(password);
//                                AccountManager.sUserBean.setNickName(data.getString("nicename"));
//                                AccountManager.sUserBean.setHeadPortrait(data.getString("avatar"));

                                    Toast.makeText(mActivity,res.getString("data"),Toast.LENGTH_SHORT).show();
                                    String userBase64 = CommonUtil.objectToBase64(AccountManager.sUserBean);
                                    SPUtil.put(Constant.USER, userBase64);
                                    SPUtil.put("head", SPUtil.get("head2", "").toString());
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                }else{
                                    Toast.makeText(mActivity,info,Toast.LENGTH_SHORT).show();
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
    private void login() {
        Map<String, String> map = new HashMap<>();
            map.put("account", phone.getText().toString());
            map.put("passwd", MD5Util.getMD5Str(pwd.getText().toString()));
        map.put("flag", 1 + "");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).login(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        JSONObject data = res.getJSONObject("data");
                        AccountManager.sUserBean = new UserBean();
                        AccountManager.sUserBean.head = data.getString("token");
                        AccountManager.sUserBean.phone = phone.getText().toString();
                        AccountManager.sUserBean.passwd = pwd.getText().toString();
                        AccountManager.sUserBean.needshowdialog=true;
                        System.out.println(data);
                        int first=data.optInt("first");
                        String token=data.getString("token");
                        System.out.println("临时Token："+token);
//                        if(first==1){
//                            ToastUtil.showShort(mContext, "需要完善信息");
//                            SPUtil.put("head2", token);
//                            AccountManager.sUserBean.needshowdialog=true;
//                            startActivityForResult(new Intent(mActivity, RegisterActivity.class).putExtra("type",loginflag),100);
//                        }else{
//                            ToastUtil.showShort(mContext, "登录成功");
//                            SPUtil.put("head", token);
//                            startActivity(new Intent(mActivity, MainNewOldActivity.class));
//                            finish();
//                        }
                        ToastUtil.showShort(mContext, "登录成功");
                        SPUtil.put("head", token);
                        startActivity(new Intent(mActivity, MainNewOldActivity.class));
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(mActivity, info, Toast.LENGTH_SHORT).show();
//                        if(info.contains("不存在")){
//                            AccountManager.sUserBean = new UserBean();
//                            AccountManager.sUserBean.phone = phone.getText().toString();
//                            AccountManager.sUserBean.passwd = pwd.getText().toString();
//                            startActivity(new Intent(mActivity, RegisterActivity.class).putExtra("type",loginflag));
//                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void getCode(final String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        if(type==1){
            map.put("type", "reg");
        }else{
            map.put("type", "invite");
        }
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .getCode(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        DialogUtil.hideProgress();
                        try {
                            JSONObject res = new JSONObject(response);
                            int code = res.getInt("code");
                            String info = res.getString("data");
                            String msg = res.get("msg").toString();

                            if (code == 200) {
                                ToastUtil.showShort(mContext, info);

                            } else {
                                ToastUtil.showShort(mContext, msg);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        SPUtil.put("head", "");
//        AccountManager.sUserBean.head=null;
    }
}
