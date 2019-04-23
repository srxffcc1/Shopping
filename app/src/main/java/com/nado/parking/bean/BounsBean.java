package com.nado.parking.bean;

/**
 * Created by zjj on 2018/1/2 <br>
 * Email:1260968684@qq.com <p>
 *     具体奖项实体类
 */

public class BounsBean {
    private String mId;
    private String mBounsName;
    /**
     * 中奖数
     */
    private int mBounsNumber;
    private String mBounsMoney;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getBounsName() {
        return mBounsName;
    }

    public void setBounsName(String bounsName) {
        mBounsName = bounsName;
    }

    public int getBounsNumber() {
        return mBounsNumber;
    }

    public void setBounsNumber(int bounsNumber) {
        mBounsNumber = bounsNumber;
    }

    public String getBounsMoney() {
        return mBounsMoney;
    }

    public void setBounsMoney(String bounsMoney) {
        mBounsMoney = bounsMoney;
    }
}
