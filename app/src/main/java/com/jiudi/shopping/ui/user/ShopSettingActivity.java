package com.jiudi.shopping.ui.user;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.event.FinishEvent;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.user.account.AboutUsActivity;
import com.jiudi.shopping.ui.user.account.LoginActivity;
import com.jiudi.shopping.util.DataCleanManager;
import com.jiudi.shopping.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShopSettingActivity extends BaseActivity {
    private android.widget.LinearLayout userinfo;
    private android.widget.LinearLayout clear;
    private android.widget.LinearLayout address;
    private android.widget.LinearLayout aboutus;
    private android.widget.TextView logout;
    private TextView userspace;
    private TextView usernick;
    private Dialog dialogchosetext;
    private String chosetext;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_shopsetting;
    }

    @Override
    public void initView() {

        userinfo = (LinearLayout) findViewById(R.id.userinfo);
        clear = (LinearLayout) findViewById(R.id.clear);
        address = (LinearLayout) findViewById(R.id.address);
        aboutus = (LinearLayout) findViewById(R.id.aboutus);
        logout = (TextView) findViewById(R.id.logout);
        userspace = (TextView) findViewById(R.id.userspace);
        usernick = (TextView) findViewById(R.id.usernick);
    }

    @Override
    public void initData() {
        try {
            userspace.setText(DataCleanManager.getTotalCacheSize(this)+"M");
        } catch (Exception e) {
            e.printStackTrace();
        }
        usernick.setText(AccountManager.sUserBean.nickname);
    }

    public boolean isNoNeedLogin(){
        return true;
    }
    @Override
    public void initEvent() {
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, AboutUsActivity.class));
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, AddressListActivity.class));

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.put("head","");
                EventBus.getDefault().post(new FinishEvent());
                startActivity(new Intent(mActivity, LoginActivity.class));
                finish();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCleanManager.clearAllCache(mActivity);
                userspace.setText("0.0M");
                Toast.makeText(mActivity,"清除缓存成功",Toast.LENGTH_SHORT).show();

                SPUtil.put("head","");
                EventBus.getDefault().post(new FinishEvent());
                AccountManager.sUserBean=null;
                startActivity(new Intent(mActivity, LoginActivity.class));
                finish();

            }
        });
        userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StyledDialog.init(mActivity);
                dialogchosetext = StyledDialog.buildNormalInput("个人信息", "输入昵称", "",
                        "确定", "取消",  new MyDialogListener() {
                            @Override
                            public void onFirst() {
                                change(chosetext);
                            }

                            @Override
                            public void onSecond() {

                            }

                            @Override
                            public void onGetInput(CharSequence input1, CharSequence input2) {
                                super.onGetInput(input1, input2);
                                chosetext=input1.toString();
                            }
                        }).setCancelable(true,true).show();
            }
        });

    }

    private void change(final String chosetext) {
        Map<String, String> map = new HashMap<>();
//        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("type","2");
        map.put("nickname",chosetext);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).changeNick(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        usernick.setText(chosetext);
                        Toast.makeText(mActivity,"修改完成",Toast.LENGTH_SHORT).show();
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
}
