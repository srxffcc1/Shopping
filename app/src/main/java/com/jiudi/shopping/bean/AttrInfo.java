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
public class AttrInfo implements Serializable {

    private int product_id;
    private String suk;
    private int stock;
    private int sales;
    private String price;
    private String vip_price;
    private String image;
    private String unique;
    private String cost;
    public void setProduct_id(int product_id) {
         this.product_id = product_id;
     }
     public int getProduct_id() {
         return product_id;
     }

    public void setSuk(String suk) {
         this.suk = suk;
     }
     public String getSuk() {
         return suk;
     }

    public void setStock(int stock) {
         this.stock = stock;
     }
     public int getStock() {
         return stock;
     }

    public void setSales(int sales) {
         this.sales = sales;
     }
     public int getSales() {
         return sales;
     }

    public void setPrice(String price) {
         this.price = price;
     }
     public String getPrice() {
         return price;
     }

    public void setVip_price(String vip_price) {
         this.vip_price = vip_price;
     }
     public String getVip_price() {
         return vip_price;
     }

    public void setImage(String image) {
         this.image = image;
     }
     public String getImage() {
         return image;
     }

    public void setUnique(String unique) {
         this.unique = unique;
     }
     public String getUnique() {
         return unique;
     }

    public void setCost(String cost) {
         this.cost = cost;
     }
     public String getCost() {
         return cost;
     }

}