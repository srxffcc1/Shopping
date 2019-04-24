package com.nado.shopping.bean;

import java.util.ArrayList;

/**
 * Created by zjj on 2017/12/29 <br>
 * Email:1260968684@qq.com <p>
 *     推荐一组比赛实体类
 */

public class RecommendMatchTeamBean {
    private String mId;
    /**
     * 推荐的该组比赛的比赛场次
     */
    private int mMatchNumber;
    /**
     * 推荐的该组比赛的综合结果
     */
    private String mRecommendResult;

    /**
     * 推荐比赛的战况，正对多串一
     */

    private ArrayList<MatchBean> matchList = new ArrayList<>();

    private boolean mWin = false;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getMatchNumber() {
        return mMatchNumber;
    }

    public void setMatchNumber(int matchNumber) {
        mMatchNumber = matchNumber;
    }

    public String getRecommendResult() {
        return mRecommendResult;
    }

    public void setRecommendResult(String recommendResult) {
        mRecommendResult = recommendResult;
    }

    public ArrayList<MatchBean> getMatchList() {
        return matchList;
    }

    public void setMatchList(ArrayList<MatchBean> matchList) {
        this.matchList = matchList;
    }

    public boolean isWin() {
        return mWin;
    }

    public void setWin(boolean win) {
        mWin = win;
    }
}
