package com.jiudi.shopping.bean;

/**
 * 微信返回支付的参数
 * 作者：Constantine on 2018/9/11.
 * 邮箱：2534159288@qq.com
 */
public class WeChatBillEntity {


    /**
     * 应用ID
     */
    private String mAppId;
    /**
     * 商户号
     */
    private String mPartnerid;
    /**
     * 随机字符串
     */
    private String mNoncestr;
    /**
     * 时间戳
     */
    private String mTimestamp;
    /**
     * 预支付交易会话id
     */
    private String mPrepayid;
    /**
     * 签名
     */
    private String mSign;

    /**
     * 扩展字段 目前固定为Sign=WXPay
     */
    private String mPackageValue;

    public String getPackageValue() {
        return mPackageValue;
    }

    public String getAppId() {
        return mAppId;
    }

    public void setAppId(String appId) {
        mAppId = appId;
    }

    public void setPackageValue(String packageValue) {
        mPackageValue = packageValue;
    }

    public String getPartnerid() {
        return mPartnerid;

    }

    public void setPartnerid(String partnerid) {
        mPartnerid = partnerid;
    }

    public String getNoncestr() {
        return mNoncestr;
    }

    public void setNoncestr(String noncestr) {
        mNoncestr = noncestr;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String timestamp) {
        mTimestamp = timestamp;
    }

    public String getPrepayid() {
        return mPrepayid;
    }

    public void setPrepayid(String prepayid) {
        mPrepayid = prepayid;
    }

    public String getSign() {
        return mSign;
    }

    public void setSign(String sign) {
        mSign = sign;
    }
}
