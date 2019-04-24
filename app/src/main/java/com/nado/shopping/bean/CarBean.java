package com.nado.shopping.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Constantine on 2018/9/4.
 * 邮箱：2534159288@qq.com
 */
public class CarBean {

    /**
     * 车牌ID

     */
    private String mId;
    /**
     * 车牌
     */
    private String mPlate;
    /**
     * 车辆状态
     */
    private String mStatus;
    /**
     * 车辆发动机号6位
     */
    private String mEngine;
    /**
     * 车辆车架号6位
     */
    private String mFrame;

    /**
     * 是否存在订单集合信息
     */
    private boolean mIsOrder1List = false;
    private boolean mIsOrder2List  = false;
    /**
     * 停车纪录信息
     * 首页停车集合
     */
    private List<CarOrderBean> mOrder1;
    private List<CarOrderBean> mOrder2;

    /**
     * 停车违章记录
     */
    private ArrayList<PeccancyBean> mPeccancyList;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getPlate() {
        return mPlate;
    }

    public void setPlate(String plate) {
        mPlate = plate;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public List<CarOrderBean> getOrder1() {
        return mOrder1;
    }

    public void setOrder1(List<CarOrderBean> order1) {
        mOrder1 = order1;
    }

    public List<CarOrderBean> getOrder2() {
        return mOrder2;
    }

    public void setOrder2(List<CarOrderBean> order2) {
        mOrder2 = order2;
    }

    public String getEngine() {
        return mEngine;
    }

    public void setEngine(String engine) {
        mEngine = engine;
    }

    public String getFrame() {
        return mFrame;
    }

    public void setFrame(String frame) {
        mFrame = frame;
    }

    public ArrayList<PeccancyBean> getPeccancyList() {
        return mPeccancyList;
    }

    public void setPeccancyList(ArrayList<PeccancyBean> peccancyList) {
        mPeccancyList = peccancyList;
    }

    public boolean isOrder1List() {
        return mIsOrder1List;
    }

    public void setOrder1List(boolean order1List) {
        mIsOrder1List = order1List;
    }

    public boolean isOrder2List() {
        return mIsOrder2List;
    }

    public void setOrder2List(boolean order2List) {
        mIsOrder2List = order2List;
    }
}
