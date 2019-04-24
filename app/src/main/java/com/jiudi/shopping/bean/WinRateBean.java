package com.jiudi.shopping.bean;

/**
 * Created by zjj on 2018/3/23 0023.
 * 推荐战绩胜率实体类
 */

public class WinRateBean {

    private String mAllNum;
    private String mWinNum;
    private String mEqualNum;
    private String mFailNum;
    private String mWinRate;

    public String getAllNum() {
        return mAllNum;
    }

    public void setAllNum(String allNum) {
        mAllNum = allNum;
    }

    public String getWinNum() {
        return mWinNum;
    }

    public void setWinNum(String winNum) {
        mWinNum = winNum;
    }

    public String getEqualNum() {
        return mEqualNum;
    }

    public void setEqualNum(String equalNum) {
        mEqualNum = equalNum;
    }

    public String getFailNum() {
        return mFailNum;
    }

    public void setFailNum(String failNum) {
        mFailNum = failNum;
    }

    public String getWinRate() {
        return mWinRate;
    }

    public void setWinRate(String winRate) {
        mWinRate = winRate;
    }
}
