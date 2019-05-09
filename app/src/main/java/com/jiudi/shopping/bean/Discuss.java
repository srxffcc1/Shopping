package com.jiudi.shopping.bean;

import java.util.ArrayList;
import java.util.List;

public class Discuss {
    public String product_score;
    public String service_score;
    public String comment;
    public String add_time;
    public String nickname;
    public String avatar;
    public String merchant_reply_content;
    public String suk;
    public int star;
    private List<String> pics=new ArrayList<>();

    public List<String> getPics() {
        return pics;
    }
}
