package com.jiudi.shopping.bean;

import java.util.ArrayList;

/**
 * Created by zjj on 2018/3/13 0013.
 * 赔率实体类
 */

public class RecommendOddsBean {
    private boolean mShowMore = false;
    private String mId;
    private String mTeamName;
    private String mFirst;
    private String mSecond;
    private String mThird;
    private String mForth;
    private String mFifth;
    private String mSixth;
    private ArrayList<RecommendOddsItemBean> oddsList = new ArrayList();

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public void setTeamName(String teamName) {
        mTeamName = teamName;
    }

    public String getFirst() {
        return mFirst;
    }

    public void setFirst(String first) {
        mFirst = first;
    }

    public String getSecond() {
        return mSecond;
    }

    public void setSecond(String second) {
        mSecond = second;
    }

    public String getThird() {
        return mThird;
    }

    public void setThird(String third) {
        mThird = third;
    }

    public String getForth() {
        return mForth;
    }

    public void setForth(String forth) {
        mForth = forth;
    }

    public String getFifth() {
        return mFifth;
    }

    public void setFifth(String fifth) {
        mFifth = fifth;
    }

    public String getSixth() {
        return mSixth;
    }

    public void setSixth(String sixth) {
        mSixth = sixth;
    }

    public ArrayList<RecommendOddsItemBean> getOddsList() {
        return oddsList;
    }

    public void setOddsList(ArrayList<RecommendOddsItemBean> oddsList) {
        this.oddsList = oddsList;
    }

    public boolean isShowMore() {
        return mShowMore;
    }

    public void setShowMore(boolean showMore) {
        mShowMore = showMore;
    }
}
