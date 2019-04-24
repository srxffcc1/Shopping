package com.jiudi.shopping.event;

/**
 * Created by zjj on 2018/4/8 0008.
 * 更新赛事战绩
 */

public class UpdateRecommendEvent {
    private int mTeamType;
    private int mMatchType;

    public int getTeamType() {
        return mTeamType;
    }

    public void setTeamType(int teamType) {
        mTeamType = teamType;
    }

    public int getMatchType() {
        return mMatchType;
    }

    public void setMatchType(int matchType) {
        mMatchType = matchType;
    }
}
