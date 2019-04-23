package com.nado.parking.bean;

/**
 * Created by zjj on 2018/1/11 <br>
 * 充值记录实体类
 */

public class RecordBean {
    private String mId;
    private double mMoney;
    private String mAccount;
    private String mRecordTime;
    private String mRemark;
    private int mStates;
    /**
     * 提现记录的具体体现类型，如支付宝还是银行卡
     */
    private int mComeFromName;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public double getMoney() {
        return mMoney;
    }

    public void setMoney(double money) {
        mMoney = money;
    }

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String account) {
        mAccount = account;
    }

    public String getRecordTime() {
        return mRecordTime;
    }

    public void setRecordTime(String recordTime) {
        mRecordTime = recordTime;
    }

    public int getStates() {
        return mStates;
    }

    public void setStates(int states) {
        mStates = states;
    }

    public int getComeFromName() {
        return mComeFromName;
    }

    public void setComeFromName(int comeFromName) {
        mComeFromName = comeFromName;
    }

    public String getRemark() {
        return mRemark;
    }

    public void setRemark(String remark) {
        mRemark = remark;
    }
}
