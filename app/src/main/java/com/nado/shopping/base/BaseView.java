package com.nado.shopping.base;

/**
 * Created by licrynoob on 2016/guide_2/13 <br>
 * Copyright (C) 2016 <br>
 * Email:licrynoob@gmail.com <p>
 * View初始化接口
 */
public interface BaseView {

    /**
     * 初始化View
     */
    void initView();

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 初始化事件
     */
    void initEvent();
}
