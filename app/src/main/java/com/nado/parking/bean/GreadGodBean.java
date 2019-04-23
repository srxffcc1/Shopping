package com.nado.parking.bean;

import java.util.ArrayList;

/**
 * Created by zjj on 2018/4/18 0018.
 * 大神实体类
 */

public class GreadGodBean {
    private String mId;
    private String mType;

    private ArrayList<ExpertInfoBean> expertList = new ArrayList<>();


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

    public ArrayList<ExpertInfoBean> getExpertList() {
        return expertList;
    }

    public void setExpertList(ArrayList<ExpertInfoBean> expertList) {
        this.expertList = expertList;
    }
}
