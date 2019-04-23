package com.nado.parking.bean;

/**
 * Created by zjj on 2018/1/18 0018.
 */

public class RedPackageBean {
    private String mId;
    private String mName;
    private Double mTotalPrice=0.0;
    private String mUsefulDate;
    private boolean mSelected = false;

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

    public Double getTotalPrice() {
        return mTotalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        mTotalPrice = totalPrice;
    }

    public String getUsefulDate() {
        return mUsefulDate;
    }

    public void setUsefulDate(String usefulDate) {
        mUsefulDate = usefulDate;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }
}
