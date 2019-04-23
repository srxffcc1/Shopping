package com.nado.parking.bean;

/**
 * 作者：Constantine on 2018/9/5.
 * 邮箱：2534159288@qq.com
 */
public class ParkBean {
    /**
     * 停车场id
     */
    private String mId;
    /**
     * 云停车场id
     */
    private String mCloudId;

    public String getCloudId() {
        return mCloudId;
    }

    public void setCloudId(String cloudId) {
        mCloudId = cloudId;
    }

    /**
     * 名称
     */

    private String mParkName;
    /**
     * 地址
     */
    private String mParkAddress;


    private String mParkDistance;//暂未使用

    public double getLng() {
        return mLng;
    }

    public void setLng(double lng) {
        mLng = lng;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    /**
     * 经纬度
     */
    private double mLng;
    private double mLat;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getParkName() {
        return mParkName;
    }

    public void setParkName(String parkName) {
        mParkName = parkName;
    }

    public String getParkAddress() {
        return mParkAddress;
    }

    public void setParkAddress(String parkAddress) {
        mParkAddress = parkAddress;
    }

    public String getParkDistance() {
        return mParkDistance;
    }

    public void setParkDistance(String parkDistance) {
        mParkDistance = parkDistance;
    }
}
