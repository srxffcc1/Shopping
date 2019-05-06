/**
  * Copyright 2019 bejson.com 
  */
package com.jiudi.shopping.bean;

/**
 * Auto-generated: 2019-05-06 19:5:33
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class CartShopCart {
    private boolean ischeck=true;

    public boolean isIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    private int id;
    private int uid;
    private String type;
    private int product_id;
    private String product_attr_unique;
    private int cart_num;
    private long add_time;
    private int is_pay;
    private int is_del;
    private int is_new;
    private int combination_id;
    private int seckill_id;
    private int bargain_id;
    private ProductInfo productInfo;
    private double truePrice;
    private int costPrice;
    private int trueStock;
    public void setId(int id) {
         this.id = id;
     }
     public int getId() {
         return id;
     }

    public void setUid(int uid) {
         this.uid = uid;
     }
     public int getUid() {
         return uid;
     }

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setProduct_id(int product_id) {
         this.product_id = product_id;
     }
     public int getProduct_id() {
         return product_id;
     }

    public void setProduct_attr_unique(String product_attr_unique) {
         this.product_attr_unique = product_attr_unique;
     }
     public String getProduct_attr_unique() {
         return product_attr_unique;
     }

    public void setCart_num(int cart_num) {
         this.cart_num = cart_num;
     }
     public int getCart_num() {
         return cart_num;
     }

    public void setAdd_time(long add_time) {
         this.add_time = add_time;
     }
     public long getAdd_time() {
         return add_time;
     }

    public void setIs_pay(int is_pay) {
         this.is_pay = is_pay;
     }
     public int getIs_pay() {
         return is_pay;
     }

    public void setIs_del(int is_del) {
         this.is_del = is_del;
     }
     public int getIs_del() {
         return is_del;
     }

    public void setIs_new(int is_new) {
         this.is_new = is_new;
     }
     public int getIs_new() {
         return is_new;
     }

    public void setCombination_id(int combination_id) {
         this.combination_id = combination_id;
     }
     public int getCombination_id() {
         return combination_id;
     }

    public void setSeckill_id(int seckill_id) {
         this.seckill_id = seckill_id;
     }
     public int getSeckill_id() {
         return seckill_id;
     }

    public void setBargain_id(int bargain_id) {
         this.bargain_id = bargain_id;
     }
     public int getBargain_id() {
         return bargain_id;
     }

    public void setProductInfo(ProductInfo productInfo) {
         this.productInfo = productInfo;
     }
     public ProductInfo getProductInfo() {
         return productInfo;
     }

    public void setTruePrice(double truePrice) {
         this.truePrice = truePrice;
     }
     public double getTruePrice() {
         return truePrice;
     }

    public void setCostPrice(int costPrice) {
         this.costPrice = costPrice;
     }
     public int getCostPrice() {
         return costPrice;
     }

    public void setTrueStock(int trueStock) {
         this.trueStock = trueStock;
     }
     public int getTrueStock() {
         return trueStock;
     }

}