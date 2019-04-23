package com.nado.parking.bean;

import java.util.ArrayList;

/**
 * Created by zjj on 2017/12/20 <br>
 * Email:1260968684@qq.com <p>
 * 彩票类型实体类
 */

public class LotteryTypeBean {
    public static final int mSports = 0;
    public static final int mBall = 1;

    /**
     * 竞彩足球
     */
    public static final int mFootball = 1001;
    /**
     * 竞彩篮球
     */
    public static final int mBasketball = 1002;
    /**
     * 大乐透
     */
    public static final int mGreateLotto = 5;
    /**
     * 排列三
     */
    public static final int mArrangeThree = 6;
    /**
     * 排列五
     */
    public static final int mArrangeFive = 7;
    /**
     * 足球半全场
     */
    public static final int mFootballHalf = 4;
    /**
     * 足球胜负彩
     */
    public static final int mFootballWin = 2;
    /**
     * 足球任选九
     */
    public static final int footballNine = 1;
    /**
     * 进球彩
     */
    public static final int ballIn = 3;
    /**
     * 七星彩
     */
    public static final int sevenStar = 8;
    /**
     * 新11选5
     */
    public static final int elevenChooseFive = 9;




    private ArrayList<TagBean> mLotteryTag = new ArrayList<>();
    private String mId;
    private String mLotteryTypeImg;
    private String mLotteryTypeName;
    private String mDescribe;
    /**
     * 开奖日期
     */
    private String mOpenningTime;

    /**
     * 彩票期数
     */
    private String mNumber;

    /**
     * 是否开奖
     */
    private boolean mRunALottery = false;

    /**
     * 彩票类型，体彩还是球。。。
     */
    private int mLotteryType;

    /**
     * 彩票详细分类类型 竞彩足球 竞彩篮球 大乐透 排列三 排列五...
     */
    private int mLotteryDetailType;

    /**
     * 开奖结果 比分或者数字
     */


    public String getOpenningTime() {
        return mOpenningTime;
    }

    public void setOpenningTime(String openningTime) {
        mOpenningTime = openningTime;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public boolean isRunALottery() {
        return mRunALottery;
    }

    public void setRunALottery(boolean runALottery) {
        mRunALottery = runALottery;
    }

    public int getLotteryType() {
        return mLotteryType;
    }

    public void setLotteryType(int lotteryType) {
        mLotteryType = lotteryType;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLotteryTypeImg() {
        return mLotteryTypeImg;
    }

    public void setLotteryTypeImg(String lotteryTypeImg) {
        mLotteryTypeImg = lotteryTypeImg;
    }

    public String getLotteryTypeName() {
        return mLotteryTypeName;
    }

    public void setLotteryTypeName(String lotteryTypeName) {
        mLotteryTypeName = lotteryTypeName;
    }

    public String getDescribe() {
        return mDescribe;
    }

    public void setDescribe(String describe) {
        mDescribe = describe;
    }

    public ArrayList<TagBean> getLotteryTag() {
        return mLotteryTag;
    }

    public void setLotteryTag(ArrayList<TagBean> lotteryTag) {
        mLotteryTag = lotteryTag;
    }

    public int getLotteryDetailType() {
        return mLotteryDetailType;
    }

    public void setLotteryDetailType(int lotteryDetailType) {
        mLotteryDetailType = lotteryDetailType;
    }

    @Override
    public String toString() {
        return "LotteryTypeBean{" +
                "mLotteryTag=" + mLotteryTag +
                ", mId='" + mId + '\'' +
                ", mLotteryTypeImg='" + mLotteryTypeImg + '\'' +
                ", mLotteryTypeName='" + mLotteryTypeName + '\'' +
                ", mDescribe='" + mDescribe + '\'' +
                ", mOpenningTime='" + mOpenningTime + '\'' +
                ", mNumber='" + mNumber + '\'' +
                ", mRunALottery=" + mRunALottery +
                ", mLotteryType=" + mLotteryType +
                '}';
    }
}
