package com.jiudi.shopping.bean;

/**
 * Created by zjj on 2018/1/8 <br>
 * 篮球比分直播实体类
 */

public class LiveBasketballBean {
    private String mId;
    /**
     * 比赛场次，显示为第几节，加时，总分的标题
     */
    private String mMatchNumber;
    private String mFirstTeamName;
    private String mSecondTeamName;
    private String mFirstTeamScore;
    private String mSecondTeamScore;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getMatchNumber() {
        return mMatchNumber;
    }

    public void setMatchNumber(String matchNumber) {
        mMatchNumber = matchNumber;
    }

    public String getFirstTeamName() {
        return mFirstTeamName;
    }

    public void setFirstTeamName(String firstTeamName) {
        mFirstTeamName = firstTeamName;
    }

    public String getSecondTeamName() {
        return mSecondTeamName;
    }

    public void setSecondTeamName(String secondTeamName) {
        mSecondTeamName = secondTeamName;
    }

    public String getFirstTeamScore() {
        return mFirstTeamScore;
    }

    public void setFirstTeamScore(String firstTeamScore) {
        mFirstTeamScore = firstTeamScore;
    }

    public String getSecondTeamScore() {
        return mSecondTeamScore;
    }

    public void setSecondTeamScore(String secondTeamScore) {
        mSecondTeamScore = secondTeamScore;
    }
}
