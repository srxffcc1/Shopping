package com.jiudi.shopping.ui.user.account;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.UserBean;
import com.jiudi.shopping.event.UpdateLoginStateEvent;
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

import org.greenrobot.eventbus.EventBus;
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
    private android.widget.LinearLayout pwd;
    private ImageView pwdI;
    private android.widget.LinearLayout yanzhengmaL;
    private EditText yanzhengma;
    private TextView yanzhengmaT;
    private TextView pwdT;
    private TextView login;
    private ImageView weixinlogin;

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
        pwd = (LinearLayout) findViewById(R.id.pwd);
        pwdI = (ImageView) findViewById(R.id.pwd_i);
        yanzhengmaL = (LinearLayout) findViewById(R.id.yanzhengma_l);
        yanzhengma = (EditText) findViewById(R.id.yanzhengma);
        yanzhengmaT = (TextView) findViewById(R.id.yanzhengma_t);
        pwdT = (TextView) findViewById(R.id.pwd_t);
        login = (TextView) findViewById(R.id.login);
        weixinlogin = (ImageView) findViewById(R.id.weixinlogin);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity,RegisterActivity.class));
            }
        });
    }
}
