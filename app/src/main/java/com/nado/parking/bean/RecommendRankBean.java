package com.nado.parking.bean;

/**
 * Created by zjj on 2018/3/13 0013.
 * 积分排名实体类
 */

public class RecommendRankBean {
    private int mRankStates;//排名上升下降情况  1上  2降  0不变
    private String mId;
    private String mTeam;
    private String mMatch;
    private String mWin;
    private String mEqual;
    private String mLose;
    private String mWinRate;
    private String mRank;
    private String mIntegral;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTeam() {
        return mTeam;
    }

    public void setTeam(String team) {
        mTeam = team;
    }

    public String getMatch() {
        return mMatch;
    }

    public void setMatch(String match) {
        mMatch = match;
    }

    public String getWin() {
        return mWin;
    }

    public void setWin(String win) {
        mWin = win;
    }

    public String getEqual() {
        return mEqual;
    }

    public void setEqual(String equal) {
        mEqual = equal;
    }

    public String getLose() {
        return mLose;
    }

    public void setLose(String lose) {
        mLose = lose;
    }

    public String getWinRate() {
        return mWinRate;
    }

    public void setWinRate(String winRate) {
        mWinRate = winRate;
    }

    public String getRank() {
        return mRank;
    }

    public void setRank(String rank) {
        mRank = rank;
    }

    public String getIntegral() {
        return mIntegral;
    }

    public void setIntegral(String integral) {
        mIntegral = integral;
    }

    public int getRankStates() {
        return mRankStates;
    }

    public void setRankStates(int rankStates) {
        mRankStates = rankStates;
    }
}
