package com.jiudi.shopping.ui.user.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aykj.mustinsert.MustInsert;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.UserBean;
import com.jiudi.shopping.event.UpdateLoginStateEvent;
import com.jiudi.shopping.event.WechatLoginEvent;
import com.jiudi.shopping.event.WechatPayEvent;
import com.jiudi.shopping.global.Constant;
import com.jiudi.shopping.global.LocalApplication;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.main.MainActivity;
import com.jiudi.shopping.util.CommonUtil;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.MD5Util;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.util.WechatUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Steven on 2017/12/21.
 */
public class LoginActivity extends BaseActivity {


    private ImageView back;
    private TextView register;
    private android.widget.LinearLayout phoneL;
    private EditText phone;
    private ImageView pwdI;
    private android.widget.LinearLayout yanzhengmaL;
    private EditText yanzhengma;
    private TextView yanzhengmaT;
    private TextView pwdT;
    private TextView login;
    private ImageView weixinlogin;
    private EditText pwd;
    private LinearLayout pwdL;
    private int loginflag=2;//1-》手机密码 2-》手机验证码 3-》第三方
    private boolean mShowPwd = false;
    private String wx_code="";
    private CountDownTimer mTimer;


    @Override
    public boolean isNoNeedLogin() {
        return true;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }


    @Override
    public void initView() {

        back = (ImageView) findViewById(R.id.back);
        register = (TextView) findViewById(R.id.register);
        phoneL = (LinearLayout) findViewById(R.id.phone_l);
        phone = (EditText) findViewById(R.id.phone);
        pwdI = (ImageView) findViewById(R.id.pwd_i);
        yanzhengmaL = (LinearLayout) findViewById(R.id.yanzhengma_l);
        yanzhengma = (EditText) findViewById(R.id.yanzhengma);
        yanzhengmaT = (TextView) findViewById(R.id.yanzhengma_t);
        pwdT = (TextView) findViewById(R.id.pwd_t);
        login = (TextView) findViewById(R.id.login);
        weixinlogin = (ImageView) findViewById(R.id.weixinlogin);
        pwd = (EditText) findViewById(R.id.pwd);
        pwdL = (LinearLayout) findViewById(R.id.pwd_l);
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
        buildView();
    }

    public void buildView(){
        if(loginflag==1){
            pwdL.setVisibility(View.VISIBLE);
            yanzhengmaL.setVisibility(View.GONE);
            pwdT.setText("使用手机验证码登录");
        }
        if(loginflag==2){
            pwdL.setVisibility(View.GONE);
            yanzhengmaL.setVisibility(View.VISIBLE);
            pwdT.setText("使用密码登录");

        }
        if(loginflag==3){

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initEvent() {



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity,RegisterActivity.class));
            }
        });
        pwdT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginflag==1){
                    loginflag=2;
                }else{
                    loginflag=1;
                }
                buildView();
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
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginflag==1){
                    if(MustInsert.checkAllText(mActivity,phone,pwd)){

                        login();
                    }
                }
                if(loginflag==2){
                    if(MustInsert.checkAllText(mActivity,phone,yanzhengma)){

                        login();
                    }
                }

            }
        });
        weixinlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginflag=3;
                WechatUtil.wechatLogin(LocalApplication.mIWXApi);
            }
        });
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
    }
    private void getCode(final String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("type", "login");
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
                            String info = res.getString("data");

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

    private void login() {
        Map<String, String> map = new HashMap<>();
        if(loginflag==1){
            map.put("account", phone.getText().toString());
            map.put("passwd", MD5Util.getMD5Str(pwd.getText().toString()));
        }
        if(loginflag==2){
            map.put("tel", phone.getText().toString());
            map.put("code", yanzhengma.getText().toString());
        }
        if(loginflag==3){

            map.put("wx_code",wx_code);
        }
        map.put("flag", loginflag+"");
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).login(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        String data=res.optString("data");
                        AccountManager.sUserBean = new UserBean();
//                        AccountManager.sUserBean.account=phone.getText().toString();
//                        AccountManager.sUserBean.passwd=pwd.getText().toString();
                        AccountManager.sUserBean.head=data;
                        System.out.println(data);
                        SPUtil.put("head",data);
                        ToastUtil.showShort(mContext, "登录成功");
                        startActivity(new Intent(mActivity, MainActivity.class));
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

            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWechatLoginEvent(WechatLoginEvent wechatPayEvent) {
        wx_code=wechatPayEvent.getResult();
        System.out.println(wx_code);
        login();
    }
}
