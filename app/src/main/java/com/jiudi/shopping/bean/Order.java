package com.jiudi.shopping.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    public String user_address;
    public String real_name;
    public String user_phone;
    public String combination_id;
    public String id;
    public String order_id;
    public String pay_price;
    public String total_num;
    public String total_price;
    public String pay_postage;

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

    public String total_postage;
    public String paid;
    public String status;
    public String refund_status;
    public String pay_type;
    public String coupon_price;
    public String deduction_price;
    public String pink_id;
    public String delivery_type;
    public List<CartInfo> cartInfoList=new ArrayList<>();
    public CartStatus statuz;

    public void addCartInfo(CartInfo cartInfo){
        cartInfoList.add(cartInfo);
    }

    public CartStatus getStatuz() {
        return statuz;
    }

    public void setStatuz(CartStatus statuz) {
        this.statuz = statuz;
    }

    public String getCombination_id() {
        return combination_id;
    }

    public void setCombination_id(String combination_id) {
        this.combination_id = combination_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPay_price() {
        return pay_price;
    }

    public void setPay_price(String pay_price) {
        this.pay_price = pay_price;
    }

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getPay_postage() {
        return pay_postage;
    }

    public void setPay_postage(String pay_postage) {
        this.pay_postage = pay_postage;
    }

    public String getTotal_postage() {
        return total_postage;
    }

    public void setTotal_postage(String total_postage) {
        this.total_postage = total_postage;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(String coupon_price) {
        this.coupon_price = coupon_price;
    }

    public String getDeduction_price() {
        return deduction_price;
    }

    public void setDeduction_price(String deduction_price) {
        this.deduction_price = deduction_price;
    }

    public String getPink_id() {
        return pink_id;
    }

    public void setPink_id(String pink_id) {
        this.pink_id = pink_id;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public List<CartInfo> getCartInfoList() {
        return cartInfoList;
    }

    public void setCartInfoList(List<CartInfo> cartInfoList) {
        this.cartInfoList = cartInfoList;
    }
}
