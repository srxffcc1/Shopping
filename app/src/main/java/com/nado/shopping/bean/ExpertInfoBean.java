package com.nado.shopping.bean;

import java.util.ArrayList;

/**
 * Created by zjj on 2017/12/22 <br>
 * Email:1260968684@qq.com <p>
 * 专家详情实体类
 */

public class ExpertInfoBean {
    private String mId;
    private String mExpertName;
    private String mExpertImg;


    private String mRecommendResult;

    private MatchBean matchBean = new MatchBean();
    private ArrayList<MatchBean> matchList = new ArrayList<>();
    /**
     * 推荐比赛的战况，正对多串一
     */

    private RecommendMatchTeamBean matchTeamBean = new RecommendMatchTeamBean();
    private ArrayList<RecommendMatchTeamBean> matchTeamList = new ArrayList<>();

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getExpertName() {
        return mExpertName;
    }

    public void setExpertName(String expertName) {
        mExpertName = expertName;
    }

    public String getExpertImg() {
        return mExpertImg;
    }

    public void setExpertImg(String expertImg) {
        mExpertImg = expertImg;
    }

    public RecommendMatchTeamBean getMatchTeamBean() {
        return matchTeamBean;
    }

    public void setMatchTeamBean(RecommendMatchTeamBean matchTeamBean) {
        this.matchTeamBean = matchTeamBean;
    }

    public ArrayList<RecommendMatchTeamBean> getMatchTeamList() {
        return matchTeamList;
    }

    public void setMatchTeamList(ArrayList<RecommendMatchTeamBean> matchTeamList) {
        this.matchTeamList = matchTeamList;
    }

    public MatchBean getMatchBean() {
        return matchBean;
    }

    public void setMatchBean(MatchBean matchBean) {
        this.matchBean = matchBean;
    }

    public ArrayList<MatchBean> getMatchList() {
        return matchList;
    }

    public void setMatchList(ArrayList<MatchBean> matchList) {
        this.matchList = matchList;
    }

    public String getRecommendResult() {
        return mRecommendResult;
    }

    public void setRecommendResult(String recommendResult) {
        mRecommendResult = recommendResult;
    }
}
