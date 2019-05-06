package com.jiudi.shopping.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiudi.shopping.global.LocalApplication;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.user.account.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by licrynoob on 2016/guide_2/13 <br>
 * Copyright (C) 2016 <br>
 * Email:licrynoob@gmail.com <p>
 * BaseFragment
 */
public abstract class BaseFragment extends Fragment implements BaseView {

    protected Fragment mFragment;
    protected LocalApplication mApp;
    protected Activity mActivity;
    protected LayoutInflater mInflater;
    protected ViewGroup mContainer;
    protected View mRootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    private void getTestList() {
        Map<String, String> map = new HashMap<>();
//        map.put("customer_id", AccountManager.sUserBean.getId());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getCanPhonePay(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }
    public static final boolean ping() {

        String result = null;
        try {
            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
            // 读取ping的内容，可以不加
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            Log.d("------ping-----", "result content : " + stringBuffer.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            Log.d("----result---", "result = " + result);
        }
        return false;
    }

    @Override
    public void startActivity(Intent intent) {
        if (AccountManager.sUserBean == null) {
            super.startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        super.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (AccountManager.sUserBean == null) {
            super.startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        super.startActivityForResult(intent, requestCode);
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
            initView();
            initData();
            initEvent();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
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
    protected <T extends View> T findViewById(int viewId) {
        return (T) mRootView.findViewById(viewId);
    }
    public BaseFragment setArgumentz(String key,String value){
        Bundle bundle=new Bundle();
        bundle.putString(key,value);
        this.setArguments(bundle);
        return this;
    }

}
