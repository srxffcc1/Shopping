package com.jiudi.shopping.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jiudi.shopping.R;
import com.jiudi.shopping.global.LocalApplication;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.io.InputStream;

/**
 * 作者：maqing on 2016/10/18 0018 23:02
 * 邮箱：2856992713@qq.com
 */
public class WechatUtil {

    private static final String TAG = "WechatUtil";
    /**
     * 0:微信登录 1:绑定微信
     */
    public static int OPERATE_TYPE = 0;

    public static Activity CALL_ACTIVITY;

    /**
     * 微信登录
     */
    public static void wechatLogin(IWXAPI iwxapi) {
        // send oauth request
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "funnyyurbanite_wechat_login";
        boolean result = iwxapi.sendReq(req);
    }

    /**
     * 微信分享到好友
     */
    public static void wechatShare(String webpageUrl, String title, String description, byte[] thumbData, int scene) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = webpageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = title;
        msg.description = description;
        msg.thumbData = thumbData;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = scene;
        LocalApplication.mIWXApi.sendReq(req);
    }


//    * 分享url地址
//     *
//             * @param url            地址
//     * @param title          标题
//     * @param desc           描述
//     * @param wxSceneSession 类型
//     */


    /**
     * 微信支付
     *
     * @param appId        应用ID
     * @param partnerId    商户号
     * @param prepayId     预支付交易会话ID
     * @param packageValue 扩展字段
     * @param nonceStr     随机字符串
     * @param timeStamp    时间戳
     * @param sign         签名
     */
    public static void wechatPay(String appId, String partnerId, String prepayId, String packageValue, String nonceStr, String timeStamp, String sign) {
        PayReq request = new PayReq();
        request.appId = appId;
        request.partnerId = partnerId;
        request.prepayId = prepayId;
        request.packageValue = packageValue;
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        request.sign = sign;
        LocalApplication.mIWXApi.sendReq(request);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

//    /**
//     * 检测是否安装了微信
//     */
//    public static boolean isWechatInstalled(IWXAPI iwxapi) {
//        if (!iwxapi.isWXAppInstalled()) {
//            return false;
//        } else {
//            return true;
//        }
//    }


}
