package com.nado.shopping.bean;

/**
 * Created by zjj on 2018/3/13 0013.
 */

public class RecommendOddsItemBean {
    private String mId;
    private String mTime;
    private String mFirstScore;
    private String mSecondScore;
    private String mThirdScore;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getFirstScore() {
        return mFirstScore;
    }

    public void setFirstScore(String firstScore) {
        mFirstScore = firstScore;
    }

    public String getSecondScore() {
        return mSecondScore;
    }

    public void setSecondScore(String secondScore) {
        mSecondScore = secondScore;
    }

    public String getThirdScore() {
        return mThirdScore;
    }

    public void setThirdScore(String thirdScore) {
        mThirdScore = thirdScore;
    }
}
