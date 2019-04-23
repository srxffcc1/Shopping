package com.nado.parking.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nado.parking.global.LocalApplication;


/**
 * Created by licrynoob on 2016/11/15 <br>
 * Copyright (C) 2016 <br>
 * Email:licrynoob@gmail.com <p>
 * Viewpager中的Fragment 用于可见时初始化
 */

public abstract class DelayFragment extends Fragment implements BaseView {

    protected Fragment mFragment;
    protected LocalApplication mApp;
    protected FragmentActivity mActivity;
    protected LayoutInflater mInflater;
    protected ViewGroup mContainer;
    protected View mRootView;
    protected boolean mIsVisible;
    private boolean mIsPrepared;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisible = isVisibleToUser;
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            inVisible();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = this;
        mApp = LocalApplication.getInstance();
        mInflater = inflater;
        mContainer = container;

        initInstanceState(savedInstanceState);
        beforeInflateView();

        if (mRootView == null) {
            mRootView = inflater.inflate(getInflateViewId(), container, false);
            mIsPrepared = true;
            delayLoad();
        }

        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    /**
     * 可见
     */
    protected void onVisible() {
        delayLoad();
    }

    /**
     * 不可见
     */
    protected void inVisible() {

    }

    /**
     * 延迟加载
     */
    protected void delayLoad() {
        if (mIsVisible && mIsPrepared) {
            initView();
            initData();
            initEvent();
            mIsPrepared = false;
        }
    }

    /**
     * 初始化保存的数据
     */
    protected void initInstanceState(Bundle savedInstanceState) {

    }

    /**
     * inflateView之前调用
     */
    protected void beforeInflateView() {

    }

    /**
     * 获取布局Id
     *
     * @return InflateViewId
     */
    protected abstract int getInflateViewId();

    /**
     * 通过id初始化View
     *
     * @param viewId viewId
     * @param <T>    T
     * @return T
     */
    protected <T extends View> T byId(int viewId) {
        return (T) mRootView.findViewById(viewId);
    }

}
