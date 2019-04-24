//package com.nado.parking.ui.wxapi;
//
///**
// * Created by Home-Pc on 2016-10-12.
// */
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.nado.parking.global.LocalApplication;
//import com.tencent.mm.opensdk.modelbase.BaseReq;
//import com.tencent.mm.opensdk.modelbase.BaseResp;
//import com.tencent.mm.opensdk.modelmsg.SendAuth;
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
//
//public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
//
//
//    private static final String TAG = "WXEntryActivity";
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
//        Log.e(TAG, baseResp.errCode + "");
//        if (baseResp instanceof SendAuth.Resp) {
//            SendAuth.Resp resp = (SendAuth.Resp) baseResp;
////            if (WechatUtil.OPERATE_TYPE == 0) {  //微信登录
////                WechatLoginEvent wechatLoginEvent = new WechatLoginEvent();
////                wechatLoginEvent.setBaseResp(baseResp);
////                EventBus.getDefault().post(wechatLoginEvent);
////                finish();
////            } else if (WechatUtil.OPERATE_TYPE == 1) { //绑定微信
////                BindWechatEvent bindWechatEvent = new BindWechatEvent();
////                bindWechatEvent.setBaseResp(baseResp);
////                EventBus.getDefault().post(bindWechatEvent);
////                finish();
////            }
////        } else {
////                //微信分享
////                WechatShareEvent wechatShareEvent = new WechatShareEvent();
////                switch (baseResp.errCode) {
////                    case BaseResp.ErrCode.ERR_OK:
////                        wechatShareEvent.setResult(0);
////                        break;
////                    case BaseResp.ErrCode.ERR_USER_CANCEL:
////                        wechatShareEvent.setResult(1);
////                        break;
////                    default:
////                        wechatShareEvent.setResult(2);
////                        break;
////                }
////                EventBus.getDefault().post(wechatShareEvent);
//            finish();
//        }
//    }
//
//}
