/**
  * Copyright 2019 bejson.com 
  */
package com.jiudi.shopping.bean;

import java.io.Serializable;

/**
 * Auto-generated: 2019-05-06 19:5:33
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class CartInfo implements Serializable {
    private boolean ischeck=true;

    public boolean isIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    private int id;
    private String uid;
    private String type;
    private String user_address;
    private String real_name;
    private String user_phone;

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    private int product_id;
    private String product_attr_unique;
    private int cart_num;
    private String add_time;
    private String is_pay;
    private String is_del;
    private String is_new;
    private String combination_id;
    private String seckill_id;
    private String bargain_id;
    private ProductInfo productInfo;
    private String truePrice;
    private String costPrice;
    private String trueStock;
    private String unique;

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_attr_unique() {
        return product_attr_unique;
    }

    public void setProduct_attr_unique(String product_attr_unique) {
        this.product_attr_unique = product_attr_unique;
    }

    public int getCart_num() {
        return cart_num;
    }

    public void setCart_num(int cart_num) {
        this.cart_num = cart_num;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(String is_pay) {
        this.is_pay = is_pay;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getCombination_id() {
        return combination_id;
    }

    public void setCombination_id(String combination_id) {
        this.combination_id = combination_id;
    }

    public String getSeckill_id() {
        return seckill_id;
    }

    public void setSeckill_id(String seckill_id) {
        this.seckill_id = seckill_id;
    }

    public String getBargain_id() {
        return bargain_id;
    }

    public void setBargain_id(String bargain_id) {
        this.bargain_id = bargain_id;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public String getTruePrice() {
        return truePrice;
    }

    public void setTruePrice(String truePrice) {
        this.truePrice = truePrice;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getTrueStock() {
        return trueStock;
    }

    public void setTrueStock(String trueStock) {
        this.trueStock = trueStock;
    }
}