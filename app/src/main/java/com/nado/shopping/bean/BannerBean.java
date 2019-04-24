package com.nado.shopping.bean;

/**
 * Created by Alex on 2018/3/15.
 */
public class BannerBean {
    private String mId;
    private String mUrl;
    private String mRemark;
    /**
     * 1:内部 2：外部商品 4:商品
     */
    private int mType;
    /**
     * 内部
     */
    public static final int TYPE_INSIDE = 1;
    /**
     * 外部
     */
    public static final int TYPE_EXTERNAL = 2;
    /**
     * 商品
     */
    public static final int TYPE_PRODUCT = 4;
    private String mImage;
    /**
     * 内部：Html代码
     */
    private String mContent;

    private String mProductId;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getRemark() {
        return mRemark;
    }

    public void setRemark(String remark) {
        mRemark = remark;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getProductId() {
        return mProductId;
    }

    public void setProductId(String productId) {
        mProductId = productId;
    }

}
