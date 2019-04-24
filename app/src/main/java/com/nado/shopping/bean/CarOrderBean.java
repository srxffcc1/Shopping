package com.nado.shopping.bean;

/**
 * 停车订单实体类
 * 作者：Constantine on 2018/9/10.
 * 邮箱：2534159288@qq.com
 */
public class CarOrderBean {
    /**
     * 停车订单id
     */
    private String mId;
    /**
     * 停车订单车牌
     */
    private String mPlate;
    /**
     * 停车场
     */
    private String mParkName;
    /**
     * 订单编号
     */
    private String mUnid;
    /**
     * 停车费
     */
    private String mTotalFee;
    /**
     * 已付金额
     */
    private String mPaidFee;

    /**
     * 优惠金额
     */
    private String mTicketFee;

    /**
     * 实付金额
     */
    private String mPrice;
    /**
     * 进场时间戳
     */
    private String mEntryTime;
    /**
     * 出场时间
     */
    private String mExitTime;
    /**
     * 状态 0入场待付1预付待出2赊账出场3已完成
     */
    private String mStatus;
    /**
     * 减免时长 分钟
     */
    private String mDerateDuration;
    /**
     * 停车时长 分钟
     */
    private String mDuration;
    /**
     * 支付类型 1支付宝2微信
     */
    private String mPayType;
    /**
     * 支付时间
     */
    private String mPayTime;

    /*停车订单参数信息*/

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

    public String getUnid() {
        return mUnid;
    }

    public void setUnid(String unid) {
        mUnid = unid;
    }

    public String getPlate() {
        return mPlate;
    }

    public void setPlate(String plate) {
        mPlate = plate;
    }

    public String getTotalFee() {
        return mTotalFee;
    }

    public void setTotalFee(String totalFee) {
        mTotalFee = totalFee;
    }

    public String getPaidFee() {
        return mPaidFee;
    }

    public void setPaidFee(String paidFee) {
        mPaidFee = paidFee;
    }

    public String getTicketFee() {
        return mTicketFee;
    }

    public void setTicketFee(String ticketFee) {
        mTicketFee = ticketFee;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getEntryTime() {
        return mEntryTime;
    }

    public void setEntryTime(String entryTime) {
        mEntryTime = entryTime;
    }

    public String getExitTime() {
        return mExitTime;
    }

    public void setExitTime(String exitTime) {
        mExitTime = exitTime;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getPayType() {
        return mPayType;
    }

    public void setPayType(String payType) {
        mPayType = payType;
    }

    public String getPayTime() {
        return mPayTime;
    }

    public void setPayTime(String payTime) {
        mPayTime = payTime;
    }

    public String getDerateDuration() {
        return mDerateDuration;
    }

    public void setDerateDuration(String derateDuration) {
        mDerateDuration = derateDuration;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }
}
