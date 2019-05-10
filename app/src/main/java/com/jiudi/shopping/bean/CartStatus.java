/**
  * Copyright 2019 bejson.com 
  */
package com.jiudi.shopping.bean;

import java.io.Serializable;

/**
 * Auto-generated: 2019-05-07 10:38:48
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class CartStatus implements Serializable {

    private String title;
    private String payType;
    private String type;
    private String msg;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public void set_title(String _title) {
         this.title = _title;
     }
     public String get_title() {
         return title;
     }


}