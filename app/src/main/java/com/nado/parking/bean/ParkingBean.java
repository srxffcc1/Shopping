package com.nado.parking.bean;

/**
 * Created by Steven on 2018/6/1.
 */

public class ParkingBean {
    private String mId;
    private String mPrice;
    private String mCarNum;
    private String mParkingName;
    private String mStartTime;
    private String mParkingTime;

    public String getCarNum() {
        return mCarNum;
    }

    public void setCarNum(String carNum) {
        mCarNum = carNum;
    }

    public String getParkingName() {
        return mParkingName;
    }

    public void setParkingName(String parkingName) {
        mParkingName = parkingName;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
        mStartTime = startTime;
    }

    public String getParkingTime() {
        return mParkingTime;
    }

    public void setParkingTime(String parkingTime) {
        mParkingTime = parkingTime;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }
}
