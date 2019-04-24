package com.nado.shopping.bean;

/**
 * 作者：Constantine on 2018/10/25.
 * 邮箱：2534159288@qq.com
 * 违章实体类
 */
public class PeccancyBean {
    private String mId;
    private String mDate;
    private String mArea;
    private String mAction;
    private String mFinePoints;
    private String mFineMoney;

    private String mHandle;
    private String mArchiveno;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getArea() {
        return mArea;
    }

    public void setArea(String area) {
        mArea = area;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        mAction = action;
    }

    public String getFinePoints() {
        return mFinePoints;
    }

    public void setFinePoints(String finePoints) {
        mFinePoints = finePoints;
    }

    public String getFineMoney() {
        return mFineMoney;
    }

    public void setFineMoney(String fineMoney) {
        mFineMoney = fineMoney;
    }

    public String getHandle() {
        return mHandle;
    }

    public void setHandle(String handle) {
        mHandle = handle;
    }

    public String getArchiveno() {
        return mArchiveno;
    }

    public void setArchiveno(String archiveno) {
        mArchiveno = archiveno;
    }
}
