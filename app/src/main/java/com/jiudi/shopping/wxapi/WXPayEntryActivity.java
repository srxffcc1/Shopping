package com.jiudi.shopping.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jiudi.shopping.event.WechatPayEvent;
import com.jiudi.shopping.global.LocalApplication;
import com.jiudi.shopping.util.LogUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者：Constantine on 2018/9/11.
 * 邮箱：2534159288@qq.com
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXPayEntryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalApplication.mIWXApi.handleIntent(getIntent(), this);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        LocalApplication.mIWXApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        LogUtil.e(TAG, baseResp.errCode + "");
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {//微信支付
            WechatPayEvent wechatPayEvent = new WechatPayEvent();
            wechatPayEvent.setResult(baseResp.errCode);
            EventBus.getDefault().post(wechatPayEvent);
        }
        finish();
    }

}
