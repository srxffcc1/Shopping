/**
  * Copyright 2019 bejson.com 
  */
package com.jiudi.shopping.bean;
import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2019-05-06 19:5:33
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ProductInfo implements Serializable {

    private int id;
    private String image;
    private List<String> slider_image;
    private String price;
    private String cost;
    private String ot_price;
    private String vip_price;
    private String postage;
    private int mer_id;
    private String give_integral;
    private String cate_id;
    private int sales;
    private int stock;
    private String store_name;
    private String unit_name;
    private int is_show;
    private int is_del;
    private int is_postage;
    private AttrInfo attrInfo;
    public void setId(int id) {
         this.id = id;
     }
     public int getId() {
         return id;
     }

    public void setImage(String image) {
         this.image = image;
     }
     public String getImage() {
         return image;
     }

    public void setSlider_image(List<String> slider_image) {
         this.slider_image = slider_image;
     }
     public List<String> getSlider_image() {
         return slider_image;
     }

    public void setPrice(String price) {
         this.price = price;
     }
     public String getPrice() {
         return price;
     }

    public void setCost(String cost) {
         this.cost = cost;
     }
     public String getCost() {
         return cost;
     }

    public void setOt_price(String ot_price) {
         this.ot_price = ot_price;
     }
     public String getOt_price() {
         return ot_price;
     }

    public void setVip_price(String vip_price) {
         this.vip_price = vip_price;
     }
     public String getVip_price() {
         return vip_price;
     }

    public void setPostage(String postage) {
         this.postage = postage;
     }
     public String getPostage() {
         return postage;
     }

    public void setMer_id(int mer_id) {
         this.mer_id = mer_id;
     }
     public int getMer_id() {
         return mer_id;
     }

    public void setGive_integral(String give_integral) {
         this.give_integral = give_integral;
     }
     public String getGive_integral() {
         return give_integral;
     }

    public void setCate_id(String cate_id) {
         this.cate_id = cate_id;
     }
     public String getCate_id() {
         return cate_id;
     }

    public void setSales(int sales) {
         this.sales = sales;
     }
     public int getSales() {
         return sales;
     }

    public void setStock(int stock) {
         this.stock = stock;
     }
     public int getStock() {
         return stock;
     }

    public void setStore_name(String store_name) {
         this.store_name = store_name;
     }
     public String getStore_name() {
         return store_name;
     }

    public void setUnit_name(String unit_name) {
         this.unit_name = unit_name;
     }
     public String getUnit_name() {
         return unit_name;
     }

    public void setIs_show(int is_show) {
         this.is_show = is_show;
     }
     public int getIs_show() {
         return is_show;
     }

    public void setIs_del(int is_del) {
         this.is_del = is_del;
     }
     public int getIs_del() {
         return is_del;
     }

    public void setIs_postage(int is_postage) {
         this.is_postage = is_postage;
     }
     public int getIs_postage() {
         return is_postage;
     }

    public void setAttrInfo(AttrInfo attrInfo) {
         this.attrInfo = attrInfo;
     }
     public AttrInfo getAttrInfo() {
         return attrInfo;
     }

}