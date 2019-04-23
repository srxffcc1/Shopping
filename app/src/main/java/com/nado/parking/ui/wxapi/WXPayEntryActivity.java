//package com.nado.parking.ui.wxapi;
//
//import android.app.Activity;
//import android.os.Bundle;
//
//import com.nado.parking.event.WechatPayEvent;
//import com.nado.parking.global.LocalApplication;
//import com.nado.parking.util.LogUtil;
//import com.tencent.mm.opensdk.constants.ConstantsAPI;
//import com.tencent.mm.opensdk.modelbase.BaseReq;
//import com.tencent.mm.opensdk.modelbase.BaseResp;
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
//
//import org.greenrobot.eventbus.EventBus;
//
///**
// * 作者：maqing on 2016/12/2 0002 11:22
// * 邮箱：2856992713@qq.com
// */
//public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
//    private static final String TAG = "WXPayEntryActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        LocalApplication.mIWXApi.handleIntent(getIntent(), this);
//    }
//
//    @Override
//    public void onReq(BaseReq baseReq) {
//
//    }
//
//    @Override
//    public void onResp(BaseResp baseResp) {
//        LogUtil.e(TAG, baseResp.errCode + "");
//        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {//微信支付
//            WechatPayEvent wechatPayEvent = new WechatPayEvent();
//            wechatPayEvent.setResult(baseResp.errCode);
//            EventBus.getDefault().post(wechatPayEvent);
//            finish();
//        }
//    }
//
//}
