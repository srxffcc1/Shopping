package com.jiudi.shopping.wxapi;

/**
 * Created by Home-Pc on 2016-10-12.
 */

import android.app.Activity;
import android.os.Bundle;

import com.jiudi.shopping.global.LocalApplication;
import com.jiudi.shopping.util.LogUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalApplication.mIWXApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        LogUtil.e(TAG, baseResp.errCode + "");
        if (baseResp instanceof SendAuth.Resp) {
            SendAuth.Resp resp = (SendAuth.Resp) baseResp;//需要app自行使用得到的参数去请求 用户信息 帮助登录 代码解析部分再下面的方法
            LogUtil.e(TAG,resp.toString());
            finish();

        }
    }
//    private void getAccessToken(String code) {
//        //获取授权
//        StringBuffer loginUrl = new StringBuffer();
//        loginUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token")
//                .append("?appid=")
//                .append(Constant.APP_ID)
//                .append("&secret=")
//                .append(Constant.SECRET)
//                .append("&code=")
//                .append(code)
//                .append("&grant_type=authorization_code");
//        OkHttpUtils.ResultCallback<String> resultCallback = new OkHttpUtils.ResultCallback<String>() {
//            @Override
//            public void onSuccess(String response) {
//                String access = null;
//                String openId = null;
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    access = jsonObject.getString("access_token");
//                    openId = jsonObject.getString("openid");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                //获取个人信息
//                String getUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" + openId;
//                OkHttpUtils.ResultCallback<String> reCallback = new OkHttpUtils.ResultCallback<String>() {
//                    @Override
//                    public void onSuccess(String responses) {
//
//                        String nickName = null;
//                        String sex = null;
//                        String city = null;
//                        String province = null;
//                        String country = null;
//                        String headimgurl = null;
//                        try {
//                            JSONObject jsonObject = new JSONObject(responses);
//
//                            openid = jsonObject.getString("openid");
//                            nickName = jsonObject.getString("nickname");
//                            sex = jsonObject.getString("sex");
//                            city = jsonObject.getString("city");
//                            province = jsonObject.getString("province");
//                            country = jsonObject.getString("country");
//                            headimgurl = jsonObject.getString("headimgurl");
//                            unionid = jsonObject.getString("unionid");
//                            loadNetData(1, openid, nickName, sex, province,
//                                    city, country, headimgurl, unionid);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Exception e) {
//                        Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                };
//                OkHttpUtils.get(getUserInfo, reCallback);
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        };
//        OkHttpUtils.get(loginUrl.toString(), resultCallback);
//    }


}
