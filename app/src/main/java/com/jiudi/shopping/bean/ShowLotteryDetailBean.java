package com.jiudi.shopping.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by zjj on 2018/1/10 <br>
 * 显示所选数字彩票详情实体类
 */

public class ShowLotteryDetailBean implements Parcelable{
    private String mId;

    /**
     * 一个list有几个数字组成
     */
    private ArrayList<TagBean> mDetailNumberList = new ArrayList<TagBean>();
    /**
     * 注数
     */
    private int mNum;

    private int mFirstPosition;
    private int mFirstBravePosition;
    private int mSecondPosition;
    private int mSecondBravePosition;
    private int mThirdPosition;
    private int mForthPosition;
    private int mFifthPosition;
    private int mSixthPosition;
    private int mSeventhPosition;

    public int getFirstPosition() {
        return mFirstPosition;
    }

    public void setFirstPosition(int firstPosition) {
        mFirstPosition = firstPosition;
    }

    public int getSecondPosition() {
        return mSecondPosition;
    }

    public void setSecondPosition(int secondPosition) {
        mSecondPosition = secondPosition;
    }

    public int getFirstBravePosition() {
        return mFirstBravePosition;
    }

    public void setFirstBravePosition(int firstBravePosition) {
        mFirstBravePosition = firstBravePosition;
    }

    public int getSecondBravePosition() {
        return mSecondBravePosition;
    }

    public void setSecondBravePosition(int secondBravePosition) {
        mSecondBravePosition = secondBravePosition;
    }

    public int getThirdPosition() {
        return mThirdPosition;
    }

    public void setThirdPosition(int thirdPosition) {
        mThirdPosition = thirdPosition;
    }

    public int getForthPosition() {
        return mForthPosition;
    }

    public void setForthPosition(int forthPosition) {
        mForthPosition = forthPosition;
    }

    public int getFifthPosition() {
        return mFifthPosition;
    }

    public void setFifthPosition(int fifthPosition) {
        mFifthPosition = fifthPosition;
    }

    public int getSixthPosition() {
        return mSixthPosition;
    }

    public void setSixthPosition(int sixthPosition) {
        mSixthPosition = sixthPosition;
    }

    public int getSeventhPosition() {
        return mSeventhPosition;
    }

    public void setSeventhPosition(int seventhPosition) {
        mSeventhPosition = seventhPosition;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }


    public int getNum() {
        return mNum;
    }

    public void setNum(int num) {
        mNum = num;
    }

    public ArrayList<TagBean> getDetailNumberList() {
        return mDetailNumberList;
    }

    public void setDetailNumberList(ArrayList<TagBean> detailNumberList) {
        mDetailNumberList = detailNumberList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeTypedList(mDetailNumberList);
        dest.writeInt(mNum);
    }

    public ShowLotteryDetailBean() {
    }

    protected ShowLotteryDetailBean(Parcel in) {
        mId = in.readString();
        mDetailNumberList = in.createTypedArrayList(TagBean.CREATOR);
        mNum = in.readInt();
    }

    public static final Creator<ShowLotteryDetailBean> CREATOR = new Creator<ShowLotteryDetailBean>() {
        @Override
        public ShowLotteryDetailBean createFromParcel(Parcel source) {
            return new ShowLotteryDetailBean(source);
        }

        @Override
        public ShowLotteryDetailBean[] newArray(int size) {
            return new ShowLotteryDetailBean[size];
        }
    };
}
