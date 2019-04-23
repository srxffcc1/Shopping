package com.nado.parking.bean;

import java.io.Serializable;

/**
 * Created by maqing on 2017/11/27.
 * Email:2856992713@qq.com
 */

public class UserBean implements Serializable {
    private String mId;
    private String bestGood;
    private String mNickName;
    private String mRegisterTime;
    private String mTelNumber;//手机号码
    private double mIntegral;//积分
    private String mRecommend;//推荐级数
    private String mVipLevel;
    private String mIntegralDetailTime;//积分明细时间
    private String mIntegralDetail;//积分明细
    private String mPassWord;
    private String mHeadPortrait;//用户头像地址
    private boolean mCanSign = false; //是否可以签到
    private boolean mApply = false;//是否大神认证
    private int mFaceScore;  //颜值
    private double mRedPackage; //红包
    private double earnings;//收益
    private boolean mHaveNewMessage;//是否有新消息  0表示没有false
    private String mWxName="";
    private String mQRCode;//二维码
    private String customer_no;//hui元编号

    private double mWealth;//我的财富

    public void setBestGood(String bestGood) {
        this.bestGood = bestGood;
    }

    private int mIntegerType; //积分类型
    public static final int TYPE_GET_INTEGER_FIRST=1; //获得积分
    public static final int TYPE_GET_INTEGER_SECOND=4;//获得积分
    public static final int TYPE_DEDUCT_INTEGER=6;//扣除积分

    private String mEmoney="0";
    private String mBank="";
    private String mBank_card="";
    private String mRealName="";
    private String mPersonNumber="";

    private String mShopName="";
    private int mInfoFull=0;

    private String mUserIntroduce;
    private String mUserGoodAt;

    public String id;
    public String phone;
    public String nicename;
    public String obd_pass;
    public String avatar;
    public String obd_macid;
    public String obd_id;
    public String obd_mds;
    public String obd_user_id;
    public String obd_isbind;
    public String jpush_id;

    public int getInfoFull() {
        return mInfoFull;
    }

    public void setInfoFull(int infoFull) {
        mInfoFull = infoFull;
    }

    public String getShopName() {
        return mShopName;
    }

    public void setShopName(String shopName) {
        mShopName = shopName;
    }

    public String getBank() {
        return mBank;
    }

    public void setBank(String bank) {
        mBank = bank;
    }

    public String getBank_card() {
        return mBank_card;
    }

    public void setBank_card(String bank_card) {
        mBank_card = bank_card;
    }

    public String getRealName() {
        return mRealName;
    }

    public void setRealName(String realName) {
        mRealName = realName;
    }

    public String getPersonNumber() {
        return mPersonNumber;
    }

    public void setPersonNumber(String personNumber) {
        mPersonNumber = personNumber;
    }

    public String getEmoney() {
        return mEmoney;
    }

    public void setEmoney(String emoney) {
        mEmoney = emoney;
    }

    public String getCustomer_no() {
        return customer_no;
    }

    public void setCustomer_no(String customer_no) {
        this.customer_no = customer_no;
    }

    public String getWxName() {
        return mWxName;
    }

    public void setWxName(String wxName) {
        mWxName = wxName;
    }

    /**
     * 签今日签到颜值
     */
    private int mSignFaceScore;//签到颜值

    /**
     * 性别 1:女 2：男 3:保密
     */
    private String mSex;
    /**
     * 出生日期
     */
    private String mBirthday;

    public boolean isHaveNewMessage() {
        return mHaveNewMessage;
    }

    public void setHaveNewMessage(boolean haveNewMessage) {
        mHaveNewMessage = haveNewMessage;
    }

    public String getHeadPortrait() {
        return mHeadPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        mHeadPortrait = headPortrait;
    }

    public String getPassWord() {
        return mPassWord;
    }

    public void setPassWord(String passWord) {
        mPassWord = passWord;
    }

    public String getIntegralDetailTime() {
        return mIntegralDetailTime;
    }

    public void setIntegralDetailTime(String integralDetailTime) {
        mIntegralDetailTime = integralDetailTime;
    }

    public String getIntegralDetail() {
        return mIntegralDetail;
    }

    public void setIntegralDetail(String integralDetail) {
        mIntegralDetail = integralDetail;
    }

    public String getVipLevel() {
        return mVipLevel;
    }

    public void setVipLevel(String vipLevel) {
        mVipLevel = vipLevel;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickName) {
        mNickName = nickName;
    }

    public String getRegisterTime() {
        return mRegisterTime;
    }

    public void setRegisterTime(String registerTime) {
        mRegisterTime = registerTime;
    }

    public String getTelNumber() {
        return mTelNumber;
    }

    public void setTelNumber(String telNumber) {
        mTelNumber = telNumber;
    }

    public double getIntegral() {
        return mIntegral;
    }

    public void setIntegral(double integral) {
        mIntegral = integral;
    }

    public String getRecommend() {
        return mRecommend;
    }

    public void setRecommend(String recommend) {
        mRecommend = recommend;
    }

    public boolean isCanSign() {
        return mCanSign;
    }

    public void setCanSign(boolean canSign) {
        mCanSign = canSign;
    }

    public int getFaceScore() {
        return mFaceScore;
    }

    public void setFaceScore(int faceScore) {
        mFaceScore = faceScore;
    }

    public double getRedPackage() {
        return mRedPackage;
    }

    public void setRedPackage(double redPackage) {
        mRedPackage = redPackage;
    }

    public double getEarnings() {
        return earnings;
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }

    public int getSignFaceScore() {
        return mSignFaceScore;
    }

    public void setSignFaceScore(int signFaceScore) {
        mSignFaceScore = signFaceScore;
    }

//    public String getSex() {
//        return mSex;
//    }

//    public void setSex(String sex) {
//        mSex = sex;
//    }

    public String getBirthday() {
        return mBirthday;
    }


    public void setBirthday(String birthday) {
        mBirthday = birthday;
    }

    public String getQRCode() {
        return mQRCode;
    }

    public void setQRCode(String QRCode) {
        mQRCode = QRCode;
    }

    public int getIntegerType() {
        return mIntegerType;
    }

    public void setIntegerType(int integerType) {
        mIntegerType = integerType;
    }

//    public double getWealth() {
//        return mWealth;
//    }
//
//    public void setWealth(double wealth) {
//        mWealth = wealth;
//    }

    public String getUserIntroduce() {
        return mUserIntroduce;
    }

    public void setUserIntroduce(String userIntroduce) {
        mUserIntroduce = userIntroduce;
    }

    public String getUserGoodAt() {
        return mUserGoodAt;
    }

    public void setUserGoodAt(String userGoodAt) {
        mUserGoodAt = userGoodAt;
    }

    public boolean isApply() {
        return mApply;
    }

    public void setApply(boolean apply) {
        mApply = apply;
    }
}
