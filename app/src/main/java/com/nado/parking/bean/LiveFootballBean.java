package com.nado.parking.bean;

/**
 * Created by zjj on 2018/1/5 <br>
 * Email:1260968684@qq.com <p>
 * 足球比分直播实体类
 */

public class LiveFootballBean {
    private String mId;
    /**
     * 区分主客场 0表示在左边，1表示在右边显示
     */
    private int mType;
    private String mName;
    /**
     * 具体的操作 进球 点球黄牌。。。
     */
    private int  mAction;
    private String mIntroduce;
    private String mTime;
    /**
     * 左边显示
     */
    public static int HOSTLEFT = 0;
    /**
     * 右边显示
     */
    public static int CUSTOMRIGHT = 1;

    /**
     * 进球
     */
    public static final int MINFOOTBALL = 1;
    /**
     * 点球
     */
    public static final int MPOINTFOOTBALL = 2;
    /**
     * 乌龙
     */
    public static final int MOOLONG = 3;
    /**
     * 黄牌
     */
    public static final int MYELLOWCARD = 4;
    /**
     * 红牌
     */
    public static final int MREDCARD = 5;
    /**
     * 两黄变红
     */
    public static final int MYELLOWRED = 9;
    /**
     * 两黄变红
     */
    public static final int MEXCHANGEPERSON = 11;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getAction() {
        return mAction;
    }

    public void setAction(int action) {
        mAction = action;
    }

    public String getIntroduce() {
        return mIntroduce;
    }

    public void setIntroduce(String introduce) {
        mIntroduce = introduce;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }
}
