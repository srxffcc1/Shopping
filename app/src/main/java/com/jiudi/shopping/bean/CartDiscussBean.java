package com.jiudi.shopping.bean;

import java.util.ArrayList;
import java.util.List;

public class CartDiscussBean {
    private int product_score;
    private int service_score;
    private String comment;
    private List<String> pics=new ArrayList<>();
    private String add_time;
    private String nickname;
    private String avatar;
    private String suk;
    private int star;

    public int getProduct_score() {
        return product_score;
    }

    public void setProduct_score(int product_score) {
        this.product_score = product_score;
    }

    public int getService_score() {
        return service_score;
    }

    public void setService_score(int service_score) {
        this.service_score = service_score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSuk() {
        return suk;
    }

    public void setSuk(String suk) {
        this.suk = suk;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
