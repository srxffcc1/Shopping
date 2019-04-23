package com.nado.parking.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by zjj on 2018/3/23 0023.
 * 推荐战绩
 */

public class RecommendMatchBean implements Parcelable {
    private ArrayList<MatchBean> mMatchMilitaryList=new ArrayList<MatchBean>();

    public ArrayList<MatchBean> getMatchMilitaryList() {
        return mMatchMilitaryList;
    }

    public void setMatchMilitaryList(ArrayList<MatchBean> matchMilitaryList) {
        mMatchMilitaryList = matchMilitaryList;
    }

    public RecommendMatchBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mMatchMilitaryList);
    }

    protected RecommendMatchBean(Parcel in) {
        this.mMatchMilitaryList = in.createTypedArrayList(MatchBean.CREATOR);
    }

    public static final Creator<RecommendMatchBean> CREATOR = new Creator<RecommendMatchBean>() {
        @Override
        public RecommendMatchBean createFromParcel(Parcel source) {
            return new RecommendMatchBean(source);
        }

        @Override
        public RecommendMatchBean[] newArray(int size) {
            return new RecommendMatchBean[size];
        }
    };
}
