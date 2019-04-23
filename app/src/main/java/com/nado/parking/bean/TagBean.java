package com.nado.parking.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by zjj on 2017/12/29 <br>
 * 彩票数字实体类
 */

public class TagBean implements Parcelable{
    private String mId;
    private String mName;
    private Double mMoneyScale=0.0;
    private int mStatus;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public Double getMoneyScale() {
        return mMoneyScale;
    }

    public void setMoneyScale(Double moneyScale) {
        mMoneyScale = moneyScale;
    }

    private boolean mSelected = false;
    private boolean mCatchChooseed = false;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }
    public ArrayList<TagBean> mTagBeanArrayList =new ArrayList<TagBean>();

    public ArrayList<TagBean> getTagBeanArrayList() {
        return mTagBeanArrayList;
    }

    public void setTagBeanArrayList(ArrayList<TagBean> tagBeanArrayList) {
        mTagBeanArrayList = tagBeanArrayList;
    }

    public boolean isCatchChooseed() {
        return mCatchChooseed;
    }

    public void setCatchChooseed(boolean catchChooseed) {
        mCatchChooseed = catchChooseed;
    }

    public TagBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mName);
        dest.writeValue(this.mMoneyScale);
        dest.writeInt(this.mStatus);
        dest.writeByte(this.mSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mCatchChooseed ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.mTagBeanArrayList);
    }

    protected TagBean(Parcel in) {
        this.mId = in.readString();
        this.mName = in.readString();
        this.mMoneyScale = (Double) in.readValue(Double.class.getClassLoader());
        this.mStatus = in.readInt();
        this.mSelected = in.readByte() != 0;
        this.mCatchChooseed = in.readByte() != 0;
        this.mTagBeanArrayList = in.createTypedArrayList(TagBean.CREATOR);
    }

    public static final Creator<TagBean> CREATOR = new Creator<TagBean>() {
        @Override
        public TagBean createFromParcel(Parcel source) {
            return new TagBean(source);
        }

        @Override
        public TagBean[] newArray(int size) {
            return new TagBean[size];
        }
    };
}
