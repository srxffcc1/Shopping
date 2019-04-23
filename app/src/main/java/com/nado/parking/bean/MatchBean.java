package com.nado.parking.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zjj on 2017/12/22 <br>
 * Email:1260968684@qq.com <p>
 * 具体比赛实体类
 */

public class MatchBean implements Parcelable {
    /**
     * 球探网ID
     */
    private String mQtwId;
    /**
     * 比赛进行中
     */
    public static final int MATCHING = 0;
    /**
     * 比赛结束
     */
    public static final int MATCHENG = 1;
    /**
     * 比赛未开始
     */
    public static final int MATCHWAIT = 2;
    private String mId;
    /**
     * 推荐的比赛的名称
     */
    private String mMatchName;
    /**
     * 比赛 星期几
     */
    private String mWeekDay;
    /**
     * 推荐的比赛的时间
     */
    private String mMatchTime;
    /**
     * 比赛的人员
     */
    private String mMatchMember;

    /**
     * 比赛的战况描述
     */
    private String mMatchDescribe;

    /**
     * 比赛场次
     */
    private String mMatchNumber;

    /**
     * 比赛输赢
     */
    private boolean mWin = false;

    /**
     *主队
     */
    private String mHostTeamName;
    /**
     * 主队分数
     */
    private int mHostPoint;
    /**
     * 客队分数
     */
    private int mCustonPoint;

    /**
     * 客队
     */
    private String mCustomTeamName;

    /**
     * 比赛进度
     */
    private int mMatchProgress;
    private String mDate;

    /**
     * 半场
     */
    private String mHalfDetail;
    private String mWinNum;
    private String mEqualNum;
    private String mFailNum;
    private String mShowColor;
    private String mResult;

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        mResult = result;
    }

    public String getShowColor() {
        return mShowColor;
    }

    public void setShowColor(String showColor) {
        mShowColor = showColor;
    }

    public String getHalfDetail() {
        return mHalfDetail;
    }

    public void setHalfDetail(String halfDetail) {
        mHalfDetail = halfDetail;
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

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public int getMatchProgress() {
        return mMatchProgress;
    }

    public void setMatchProgress(int matchProgress) {
        mMatchProgress = matchProgress;
    }

    public boolean isWin() {
        return mWin;
    }

    public void setWin(boolean win) {
        mWin = win;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getMatchName() {
        return mMatchName;
    }

    public void setMatchName(String matchName) {
        mMatchName = matchName;
    }

    public String getMatchTime() {
        return mMatchTime;
    }

    public void setMatchTime(String matchTime) {
        mMatchTime = matchTime;
    }

    public String getMatchMember() {
        return mMatchMember;
    }

    public void setMatchMember(String matchMember) {
        mMatchMember = matchMember;
    }

    public String getMatchDescribe() {
        return mMatchDescribe;
    }

    public void setMatchDescribe(String matchDescribe) {
        mMatchDescribe = matchDescribe;
    }

    public String getMatchNumber() {
        return mMatchNumber;
    }

    public void setMatchNumber(String matchNumber) {
        mMatchNumber = matchNumber;
    }

    public String getWeekDay() {
        return mWeekDay;
    }

    public void setWeekDay(String weekDay) {
        mWeekDay = weekDay;
    }

    public String getHostTeamName() {
        return mHostTeamName;
    }

    public void setHostTeamName(String hostTeamName) {
        mHostTeamName = hostTeamName;
    }

    public String getCustomTeamName() {
        return mCustomTeamName;
    }

    public void setCustomTeamName(String customTeamName) {
        mCustomTeamName = customTeamName;
    }

    public int getHostPoint() {
        return mHostPoint;
    }

    public void setHostPoint(int hostPoint) {
        mHostPoint = hostPoint;
    }

    public int getCustonPoint() {
        return mCustonPoint;
    }

    public void setCustonPoint(int custonPoint) {
        mCustonPoint = custonPoint;
    }

    public String getQtwId() {
        return mQtwId;
    }

    public void setQtwId(String qtwId) {
        mQtwId = qtwId;
    }

    public MatchBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mQtwId);
        dest.writeString(this.mId);
        dest.writeString(this.mMatchName);
        dest.writeString(this.mWeekDay);
        dest.writeString(this.mMatchTime);
        dest.writeString(this.mMatchMember);
        dest.writeString(this.mMatchDescribe);
        dest.writeString(this.mMatchNumber);
        dest.writeByte(this.mWin ? (byte) 1 : (byte) 0);
        dest.writeString(this.mHostTeamName);
        dest.writeInt(this.mHostPoint);
        dest.writeInt(this.mCustonPoint);
        dest.writeString(this.mCustomTeamName);
        dest.writeInt(this.mMatchProgress);
        dest.writeString(this.mDate);
        dest.writeString(this.mHalfDetail);
        dest.writeString(this.mWinNum);
        dest.writeString(this.mEqualNum);
        dest.writeString(this.mFailNum);
        dest.writeString(this.mShowColor);
        dest.writeString(this.mResult);
    }

    protected MatchBean(Parcel in) {
        this.mQtwId = in.readString();
        this.mId = in.readString();
        this.mMatchName = in.readString();
        this.mWeekDay = in.readString();
        this.mMatchTime = in.readString();
        this.mMatchMember = in.readString();
        this.mMatchDescribe = in.readString();
        this.mMatchNumber = in.readString();
        this.mWin = in.readByte() != 0;
        this.mHostTeamName = in.readString();
        this.mHostPoint = in.readInt();
        this.mCustonPoint = in.readInt();
        this.mCustomTeamName = in.readString();
        this.mMatchProgress = in.readInt();
        this.mDate = in.readString();
        this.mHalfDetail = in.readString();
        this.mWinNum = in.readString();
        this.mEqualNum = in.readString();
        this.mFailNum = in.readString();
        this.mShowColor = in.readString();
        this.mResult = in.readString();
    }

    public static final Creator<MatchBean> CREATOR = new Creator<MatchBean>() {
        @Override
        public MatchBean createFromParcel(Parcel source) {
            return new MatchBean(source);
        }

        @Override
        public MatchBean[] newArray(int size) {
            return new MatchBean[size];
        }
    };
}
