package com.jiudi.shopping.bean;

import java.util.List;

public class CartAttr {
    private int product_id;
    private String attr_name;
    private List<String> attr_values;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getAttr_name() {
        return attr_name;
    }

    public void setAttr_name(String attr_name) {
        this.attr_name = attr_name;
    }

    public List<String> getAttr_values() {
        return attr_values;
    }

    public void setAttr_values(List<String> attr_values) {
        this.attr_values = attr_values;
    }
}
