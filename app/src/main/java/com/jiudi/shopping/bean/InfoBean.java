package com.jiudi.shopping.bean;

/**
 * Created by Steven on 2017/12/26.
 *
 * oId	int	订单ID
 pName	String	主分类名称
 cName	String	子分类名称 可为空
 opendate	String	开奖日期
 issueno	String	开奖期数
 orderCType	int	子订单类型 1竞彩足球 2 竞彩篮球 3 足球任选九 4 足球胜负彩 5 进球彩 6半全场 7大乐透 8 排列三 9 排列五 10 七星彩 11 十一选五
 totalPrice	double	订单总额
 */

public class InfoBean {
    private String mId;
    private String mName;
    private String mTypeName;
    private int mStatus;
    private String mIssueno;
    private Double mTotalPrice=0.0;
    private String mOpendate;
    private int mOrderCType;
    private String mUrl;
    private String mContent;

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getTypeName() {
        return mTypeName;
    }

    public void setTypeName(String typeName) {
        mTypeName = typeName;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public String getIssueno() {
        return mIssueno;
    }

    public void setIssueno(String issueno) {
        mIssueno = issueno;
    }

    public Double getTotalPrice() {
        return mTotalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        mTotalPrice = totalPrice;
    }

    public String getOpendate() {
        return mOpendate;
    }

    public void setOpendate(String opendate) {
        mOpendate = opendate;
    }

    public int getOrderCType() {
        return mOrderCType;
    }

    public void setOrderCType(int orderCType) {
        mOrderCType = orderCType;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }
}
