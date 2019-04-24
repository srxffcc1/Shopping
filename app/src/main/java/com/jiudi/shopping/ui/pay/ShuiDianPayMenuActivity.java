package com.jiudi.shopping.ui.pay;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.DialogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShuiDianPayMenuActivity extends BaseActivity {
    private android.widget.RelativeLayout rlLayoutTopBackBar;
    private android.widget.LinearLayout llLayoutTopBackBarBack;
    private android.widget.TextView tvLayoutTopBackBarTitle;
    private android.widget.TextView tvLayoutTopBackBarEnd;
    private android.widget.TextView tvLayoutBackTopBarOperate;
    private android.widget.ImageView passshui;
    private android.widget.ImageView passdian;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_sdpaymenu;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        passshui = (ImageView) findViewById(R.id.passshui);
        passdian = (ImageView) findViewById(R.id.passdian);
        tvLayoutTopBackBarTitle.setText("水电缴费");
    }

    @Override
    public void initData() {

    }


    @Override
    public void initEvent() {
        passshui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkHasBind("水");
            }
        });
        passdian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkHasBind("电");

            }
        });
    }

    private void checkHasBind(final String type) {//1水2电
        DialogUtil.showUnCancelableProgress(mActivity, "账户查找中");
        if("水".equals(type)){
            Map<String, String> map = new HashMap<>();
            map.put("customer_id", AccountManager.sUserBean.getId());
            map.put("type","水".equals(type)?"1":"2");
            RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).getBindShuiPayCompany(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject res = new JSONObject(response);
                        int code = res.getInt("code");
                        String info = res.getString("info");
                        if (code == 0) {
                            JSONArray jsonArray=res.getJSONObject("data").getJSONArray("info");
                            if(jsonArray.length()>0){
                                passCanShuiPay(type);

                                DialogUtil.hideProgress();
                            }else{
                                passShuiBind(type);

                                DialogUtil.hideProgress();
                            }
                        }else{
                            passShuiBind(type);

                            DialogUtil.hideProgress();
                        }

                    } catch (JSONException e) {

                        DialogUtil.hideProgress();
                        e.printStackTrace();
                        passShuiBind(type);

                    }
                }

                @Override
                public void onError(Throwable t) {

                }
            });
        }else{
            Map<String, String> map = new HashMap<>();
            map.put("userid", AccountManager.sUserBean.getId());
            RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).getBindDianPayCompany(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject res = new JSONObject(response);
                        int code = res.getInt("code");
                        String info = res.getString("info");
                        if (code == 0) {
                            JSONArray jsonArray=res.getJSONArray("data");
                            if(jsonArray.length()>0){
                                passCanDianPay(type);

                                DialogUtil.hideProgress();
                            }else{

                                passDianBind(type);

                                DialogUtil.hideProgress();
                            }
                        }else{
                            passDianBind(type);

                            DialogUtil.hideProgress();
                        }

                    } catch (JSONException e) {

                        DialogUtil.hideProgress();
                        e.printStackTrace();
                        passDianBind(type);

                    }
                }

                @Override
                public void onError(Throwable t) {

                }
            });

        }

    }
    //跳转绑定页面
    private void passShuiBind(String type) {
        startActivity(new Intent(this, ShuiBindActivity.class).putExtra("type",type));

    }
    //跳转绑定页面
    private void passDianBind(String type) {
        startActivity(new Intent(this, DianBindActivity.class).putExtra("type",type));

    }
    //跳转缴费
    private void passCanShuiPay(String type) {
        startActivity(new Intent(this, ShuiHuPayListActivity.class).putExtra("type",type));

    }

    //跳转缴费
    private void passCanDianPay(String type) {
        startActivity(new Intent(this, DianHuPayListActivity.class).putExtra("type",type));

    }
}
