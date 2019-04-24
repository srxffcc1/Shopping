package com.jiudi.shopping.bean;

/**
 * Created by zjj on 2018/1/3 <br>
 * Email:1260968684@qq.com <p>
 *     竞彩篮球实体类
 */

public class CompeteBasketballBean {
    /**
     * 足球  让球胜平负 的一个+-字段
     */
    private String mConcede;
    /**
     * 足球半全场比分
     */
    private String mHalfScore;
    private String mId;
    private String mCompeteNum;
    private String mCompetePoint;
    private String mCompeteName;
    private String mMemberOne;
    private String mMemberTwo;
    /**
     * 客的分数
     */
    private int mCustomPoint;
    /**
     * 主的分数
     */
    private int mHostPoint;
    private String mPointDescribe;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getCompetePoint() {
        return mCompetePoint;
    }

    public void setCompetePoint(String competePoint) {
        mCompetePoint = competePoint;
    }

    public String getCompeteName() {
        return mCompeteName;
    }

    public void setCompeteName(String competeName) {
        mCompeteName = competeName;
    }

    public String getMemberOne() {
        return mMemberOne;
    }

    public void setMemberOne(String memberOne) {
        mMemberOne = memberOne;
    }

    public String getMemberTwo() {
        return mMemberTwo;
    }

    public void setMemberTwo(String memberTwo) {
        mMemberTwo = memberTwo;
    }

    public int getCustomPoint() {
        return mCustomPoint;
    }

    public void setCustomPoint(int customPoint) {
        mCustomPoint = customPoint;
    }

    public int getHostPoint() {
        return mHostPoint;
    }

    public void setHostPoint(int hostPoint) {
        mHostPoint = hostPoint;
    }

    public String getPointDescribe() {
        return mPointDescribe;
    }

    public void setPointDescribe(String pointDescribe) {
        mPointDescribe = pointDescribe;
    }

    public String getCompeteNum() {
        return mCompeteNum;
    }

    public void setCompeteNum(String competeNum) {
        mCompeteNum = competeNum;
    }

    public String getConcede() {
        return mConcede;
    }

    public void setConcede(String concede) {
        mConcede = concede;
    }

    public String getHalfScore() {
        return mHalfScore;
    }

    public void setHalfScore(String halfScore) {
        mHalfScore = halfScore;
    }
}
