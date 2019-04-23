package com.nado.parking.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Steven on 2018/1/8.
 */

public class MatchChoiceBean  implements Parcelable {
    private ArrayList<TagBean> mWinTagList=new ArrayList<TagBean>();
    private ArrayList<TagBean> mSecondTagList=new ArrayList<TagBean>();
    private ArrayList<TagBean> mThirdTagList=new ArrayList<TagBean>();
    private ArrayList<TagBean> mForthTagList=new ArrayList<TagBean>();
    private ArrayList<TagBean> mFifthTagList=new ArrayList<TagBean>();

    private boolean mFirstSelected = false;


    private String mMainPlayer;
    private String mCustomerPlayer;
    private int mBallType=0;//0足球，1篮球
    private int mMatchStatus=0;//0可投注，1截止
    private double mBigPoint;
    private double mMiddlePoint;
    private double mSmallPoint;
    private String mId;
    private String mNo;
    private String mLeagueName;
    private String mEndtime;
    private String mOverTime;
    private String mScore;
    private String mResult;
    private boolean mSelected=false;
    private boolean mSingle=false;

    private boolean mFirstSingle=false;
    private boolean mSecondSingle=false;
    private boolean mThirdSingle=false;
    private boolean mForthSingle=false;
    private boolean mFifthSingle=false;

    private String mConcede;//让球

    public String getConcede() {
        return mConcede;
    }

    public void setConcede(String concede) {
        mConcede = concede;
    }

    public String getScore() {
        return mScore;
    }

    public void setScore(String score) {
        mScore = score;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        mResult = result;
    }

    public boolean isSingle() {
        return mSingle;
    }

    public void setSingle(boolean single) {
        mSingle = single;
    }

    public boolean isFirstSelected() {
        return mFirstSelected;
    }

    public void setFirstSelected(boolean firstSelected) {
        mFirstSelected = firstSelected;
    }

    public MatchChoiceBean(){}
    protected MatchChoiceBean(Parcel in) {
        mMainPlayer = in.readString();
        mCustomerPlayer = in.readString();
        mBallType = in.readInt();
        mMatchStatus = in.readInt();
        mBigPoint = in.readDouble();
        mMiddlePoint = in.readDouble();
        mSmallPoint = in.readDouble();
        mId = in.readString();
        mNo = in.readString();
        mLeagueName = in.readString();
        mEndtime = in.readString();
        mOverTime = in.readString();
        mSelected = in.readByte() != 0;
        mSingle = in.readByte() != 0;
        mFirstSingle = in.readByte() != 0;
        mSecondSingle = in.readByte() != 0;
        mThirdSingle = in.readByte() != 0;
        mForthSingle = in.readByte() != 0;
        mFifthSingle = in.readByte() != 0;

        mFirstSelected = in.readByte() != 0;
        mScore = in.readString();
        mResult = in.readString();
        mTagList = in.createTypedArrayList(TagBean.CREATOR);
        mWinTagList = in.createTypedArrayList(TagBean.CREATOR);
        mSecondTagList = in.createTypedArrayList(TagBean.CREATOR);
        mThirdTagList = in.createTypedArrayList(TagBean.CREATOR);
        mForthTagList = in.createTypedArrayList(TagBean.CREATOR);
        mFifthTagList = in.createTypedArrayList(TagBean.CREATOR);
    }

    public static final Creator<MatchChoiceBean> CREATOR = new Creator<MatchChoiceBean>() {
        @Override
        public MatchChoiceBean createFromParcel(Parcel in) {
            return new MatchChoiceBean(in);
        }

        @Override
        public MatchChoiceBean[] newArray(int size) {
            return new MatchChoiceBean[size];
        }
    };

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getNo() {
        return mNo;
    }

    public void setNo(String no) {
        mNo = no;
    }

    public String getLeagueName() {
        return mLeagueName;
    }

    public void setLeagueName(String leagueName) {
        mLeagueName = leagueName;
    }

    public String getEndtime() {
        return mEndtime;
    }

    public void setEndtime(String endtime) {
        mEndtime = endtime;
    }

    public String getOverTime() {
        return mOverTime;
    }

    public void setOverTime(String overTime) {
        mOverTime = overTime;
    }

    private ArrayList<TagBean> mTagList=new ArrayList<TagBean>();

    public ArrayList<TagBean> getTagList() {
        return mTagList;
    }

    public void setTagList(ArrayList<TagBean> tagList) {
        mTagList = tagList;
    }

    public String getMainPlayer() {
        return mMainPlayer;
    }

    public void setMainPlayer(String mainPlayer) {
        mMainPlayer = mainPlayer;
    }

    public String getCustomerPlayer() {
        return mCustomerPlayer;
    }

    public void setCustomerPlayer(String customerPlayer) {
        mCustomerPlayer = customerPlayer;
    }

    public int getBallType() {
        return mBallType;
    }

    public void setBallType(int ballType) {
        mBallType = ballType;
    }

    public int getMatchStatus() {
        return mMatchStatus;
    }

    public void setMatchStatus(int matchStatus) {
        mMatchStatus = matchStatus;
    }

    public double getBigPoint() {
        return mBigPoint;
    }

    public void setBigPoint(double bigPoint) {
        mBigPoint = bigPoint;
    }

    public double getMiddlePoint() {
        return mMiddlePoint;
    }

    public void setMiddlePoint(double middlePoint) {
        mMiddlePoint = middlePoint;
    }

    public double getSmallPoint() {
        return mSmallPoint;
    }

    public void setSmallPoint(double smallPoint) {
        mSmallPoint = smallPoint;
    }

    public boolean isFirstSingle() {
        return mFirstSingle;
    }

    public void setFirstSingle(boolean firstSingle) {
        mFirstSingle = firstSingle;
    }

    public boolean isSecondSingle() {
        return mSecondSingle;
    }

    public void setSecondSingle(boolean secondSingle) {
        mSecondSingle = secondSingle;
    }

    public boolean isThirdSingle() {
        return mThirdSingle;
    }

    public void setThirdSingle(boolean thirdSingle) {
        mThirdSingle = thirdSingle;
    }

    public boolean isForthSingle() {
        return mForthSingle;
    }

    public void setForthSingle(boolean forthSingle) {
        mForthSingle = forthSingle;
    }

    public boolean isFifthSingle() {
        return mFifthSingle;
    }

    public void setFifthSingle(boolean fifthSingle) {
        mFifthSingle = fifthSingle;
    }

    public ArrayList<TagBean> getWinTagList() {
        return mWinTagList;
    }

    public void setWinTagList(ArrayList<TagBean> winTagList) {
        mWinTagList = winTagList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ArrayList<TagBean> getSecondTagList() {
        return mSecondTagList;
    }

    public void setSecondTagList(ArrayList<TagBean> secondTagList) {
        mSecondTagList = secondTagList;
    }

    public ArrayList<TagBean> getThirdTagList() {
        return mThirdTagList;
    }

    public void setThirdTagList(ArrayList<TagBean> thirdTagList) {
        mThirdTagList = thirdTagList;
    }

    public ArrayList<TagBean> getForthTagList() {
        return mForthTagList;
    }

    public void setForthTagList(ArrayList<TagBean> forthTagList) {
        mForthTagList = forthTagList;
    }

    public ArrayList<TagBean> getFifthTagList() {
        return mFifthTagList;
    }

    public void setFifthTagList(ArrayList<TagBean> fifthTagList) {
        mFifthTagList = fifthTagList;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mMainPlayer);
        parcel.writeString(mCustomerPlayer);
        parcel.writeInt(mBallType);
        parcel.writeInt(mMatchStatus);
        parcel.writeDouble(mBigPoint);
        parcel.writeDouble(mMiddlePoint);
        parcel.writeDouble(mSmallPoint);
        parcel.writeString(mId);
        parcel.writeString(mNo);
        parcel.writeString(mLeagueName);
        parcel.writeString(mEndtime);
        parcel.writeString(mOverTime);
        parcel.writeByte((byte) (mSelected ? 1 : 0));
        parcel.writeByte((byte) (mSingle ? 1 : 0));
        parcel.writeByte((byte) (mFirstSingle ? 1 : 0));
        parcel.writeByte((byte) (mSecondSingle ? 1 : 0));
        parcel.writeByte((byte) (mThirdSingle ? 1 : 0));
        parcel.writeByte((byte) (mForthSingle ? 1 : 0));
        parcel.writeByte((byte) (mFifthSingle ? 1 : 0));


        parcel.writeByte((byte) (mFirstSelected ? 1 : 0));
        parcel.writeString(mScore);
        parcel.writeString(mResult);
        parcel.writeTypedList(mTagList);
        parcel.writeTypedList(mWinTagList);
        parcel.writeTypedList(mSecondTagList);
        parcel.writeTypedList(mThirdTagList);
        parcel.writeTypedList(mForthTagList);
        parcel.writeTypedList(mFifthTagList);
    }
}
