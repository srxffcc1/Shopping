package com.nado.shopping.bean;

/**
 * Created by zjj on 2018/4/18 0018.
 * 大神推单实体类
 */

public class GreatPushBean {
    private String mId;
    private String mType;
    private String mTitle;
    private String mPayMoney;
    private String mImg;
    private String mNickName;
    private String mWatchPeople;
    private String mTime;
    /**
     * 连红数
     */
    private int highGrade;

    /**
     * 是否关注
     */
    private boolean mCollected;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPayMoney() {
        return mPayMoney;
    }

    public void setPayMoney(String payMoney) {
        mPayMoney = payMoney;
    }

    public String getImg() {
        return mImg;
    }

    public void setImg(String img) {
        mImg = img;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickName) {
        mNickName = nickName;
    }

    public String getWatchPeople() {
        return mWatchPeople;
    }

    public void setWatchPeople(String watchPeople) {
        mWatchPeople = watchPeople;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public int getHighGrade() {
        return highGrade;
    }

    public void setHighGrade(int highGrade) {
        this.highGrade = highGrade;
    }

    public boolean isCollected() {
        return mCollected;
    }

    public void setCollected(boolean collected) {
        mCollected = collected;
    }
}
