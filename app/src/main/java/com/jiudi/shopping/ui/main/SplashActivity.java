package com.jiudi.shopping.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.UserBean;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.user.account.LoginActivity;
import com.jiudi.shopping.util.SPUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends BaseActivity {
    private android.widget.ImageView splashim;

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
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        if (sp.getBoolean("isfirstinstall", true)) {
            startActivity(new Intent(mActivity, GuideActivity.class));
            finish();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    autoLogin();
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
        if (!"".equals(SPUtil.get("head", ""))) {
            Log.v("Head",SPUtil.get("head", "").toString());
            autoLogin(true);
        } else {
            startActivity(new Intent(mActivity, MainActivity.class));
            finish();
        }
    }

    private void autoLogin(boolean b) {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).getPersonalDate(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
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
                            startActivity(new Intent(mActivity, MainActivity.class));
                            finish();
                        }else{

                            AccountManager.sUserBean=null;
                            startActivity(new Intent(mActivity, MainActivity.class));
                            finish();
                        }
                    }else{
                        AccountManager.sUserBean=null;
                        startActivity(new Intent(mActivity, MainActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    AccountManager.sUserBean=null;
                    startActivity(new Intent(mActivity, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onError(Throwable t) {
                AccountManager.sUserBean=null;
                startActivity(new Intent(mActivity, MainActivity.class));
                finish();
            }
        });
    }

}
