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

    private int _type;
    private String _title;
    private String _msg;
    private String _class;
    private String _payType;
    public void set_type(int _type) {
         this._type = _type;
     }
     public int get_type() {
         return _type;
     }

    public void set_title(String _title) {
         this._title = _title;
     }
     public String get_title() {
         return _title;
     }

    public void set_msg(String _msg) {
         this._msg = _msg;
     }
     public String get_msg() {
         return _msg;
     }

    public void set_class(String _class) {
         this._class = _class;
     }
     public String get_class() {
         return _class;
     }

    public void set_payType(String _payType) {
         this._payType = _payType;
     }
     public String get_payType() {
         return _payType;
     }

}