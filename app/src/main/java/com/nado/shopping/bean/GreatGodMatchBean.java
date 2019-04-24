package com.nado.shopping.bean;

import java.util.ArrayList;

/**
 * Created by zjj on 2018/4/19 0019.
 */

public class GreatGodMatchBean {
    private String mId;
    private String mLotteryTypeName;
    /**
     * 彩票大分类  数字彩0  和比赛1
     */
    private int mLotteryType;

    private ArrayList<RateBean> rateList = new ArrayList<>();

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLotteryTypeName() {
        return mLotteryTypeName;
    }

    public void setLotteryTypeName(String lotteryTypeName) {
        mLotteryTypeName = lotteryTypeName;
    }

    public int getLotteryType() {
        return mLotteryType;
    }

    public void setLotteryType(int lotteryType) {
        mLotteryType = lotteryType;
    }

    public ArrayList<RateBean> getRateList() {
        return rateList;
    }

    public void setRateList(ArrayList<RateBean> rateList) {
        this.rateList = rateList;
    }
}
