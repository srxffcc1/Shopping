package com.nado.shopping.bean;

/**
 * Created by zjj on 2017/12/21 <br>
 * Email:1260968684@qq.com <p>
 *     精彩活动实体类
 */

public class ExcitingActionBean {
    private String mId;
    private String mActionImg;
    private String mActionName;
    private String mActionType;
    private int mActionStates;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getActionImg() {
        return mActionImg;
    }

    public void setActionImg(String actionImg) {
        mActionImg = actionImg;
    }

    public String getActionName() {
        return mActionName;
    }

    public void setActionName(String actionName) {
        mActionName = actionName;
    }

    public String getActionType() {
        return mActionType;
    }

    public void setActionType(String actionType) {
        mActionType = actionType;
    }

    public int getActionStates() {
        return mActionStates;
    }

    public void setActionStates(int actionStates) {
        mActionStates = actionStates;
    }
}
