package com.nado.parking.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Steven on 2018/1/8.
 */

public class BallDateBean implements Parcelable {
    private boolean mSelected=false;
    private String mDate;
    private String mWeek;
    private ArrayList<MatchChoiceBean> mMatchList=new ArrayList<MatchChoiceBean>();


    public BallDateBean() {
    }

    protected BallDateBean(Parcel in) {
        mSelected = in.readByte() != 0;
        mDate = in.readString();
        mWeek = in.readString();
        mMatchList=new ArrayList<MatchChoiceBean>();
        in.readTypedList(mMatchList,MatchChoiceBean.CREATOR);
    }

    public static final Creator<BallDateBean> CREATOR = new Creator<BallDateBean>() {
        @Override
        public BallDateBean createFromParcel(Parcel in) {
            return new BallDateBean(in);
        }

        @Override
        public BallDateBean[] newArray(int size) {
            return new BallDateBean[size];
        }
    };

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getWeek() {
        return mWeek;
    }

    public void setWeek(String week) {
        mWeek = week;
    }

    public ArrayList<MatchChoiceBean> getMatchList() {
        return mMatchList;
    }

    public void setMatchList(ArrayList<MatchChoiceBean> matchList) {
        mMatchList = matchList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (mSelected ? 1 : 0));
        parcel.writeString(mDate);
        parcel.writeString(mWeek);
        parcel.writeTypedList(mMatchList);
    }
}
