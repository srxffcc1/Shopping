package com.jiudi.shopping.ui.cart;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.LogisticsInfoBean;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.SPUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WuLiuActivity extends BaseActivity {

    private RecyclerView rv_logistics;
    List<LogisticsInfoBean> beans=new ArrayList<>();
    private android.widget.TextView tvDeliverCompany;
    private android.widget.TextView tvWaybillNumber;
    private android.widget.TextView tvOfficialPhone;
    private RecyclerView rvLogistics;
    private LogisticsInfoAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_wuliu;
    }

    @Override
    public void initView() {
        rv_logistics = findViewById(R.id.rv_logistics);
        rv_logistics.setLayoutManager(new LinearLayoutManager(this));
        rv_logistics.setFocusable(false);
        //解决ScrollView嵌套RecyclerView出现的系列问题
        rv_logistics.setNestedScrollingEnabled(false);
        rv_logistics.setHasFixedSize(true);
        adapter = new LogisticsInfoAdapter(this, R.layout.item_logistics, beans);
        rv_logistics.setAdapter(adapter);
        tvDeliverCompany = (TextView) findViewById(R.id.tv_deliver_company);
        tvWaybillNumber = (TextView) findViewById(R.id.tv_waybill_number);
        tvOfficialPhone = (TextView) findViewById(R.id.tv_official_phone);
        rvLogistics = (RecyclerView) findViewById(R.id.rv_logistics);
    }

    @Override
    public void initData() {
        getWuLiu();
//        getbeansTest();

    }

    private void  getbeansTest() {
//        beans.add(new LogisticsInfoBean("2018-05-20 13:37:57", "客户 签收人: 他人代收 已签收 感谢使用圆通速递，期待再次为您服务"));
//        beans.add(new LogisticsInfoBean("2018-05-20 09:03:42", "【广东省深圳市宝安区新安公司】 派件人: 陆黄星 派件中 派件员电话13360979918"));
//        beans.add(new LogisticsInfoBean("2018-05-20 08:27:10", "【广东省深圳市宝安区新安公司】 已收入"));
//        beans.add(new LogisticsInfoBean("2018-05-20 04:38:32", "【深圳转运中心】 已收入"));
//        beans.add(new LogisticsInfoBean("2018-05-19 01:27:49", "【北京转运中心】 已发出 下一站 【深圳转运中心】"));
//        beans.add(new LogisticsInfoBean("2018-05-19 01:17:19", "【北京转运中心】 已收入"));
//        beans.add(new LogisticsInfoBean("2018-05-18 18:34:28", "【河北省保定市容城县公司】 已发出 下一站 【北京转运中心】"));
//        beans.add(new LogisticsInfoBean("2018-05-18 18:33:23", "【河北省保定市容城县公司】 已打包"));
//        beans.add(new LogisticsInfoBean("2018-05-18 18:27:21", "【河北省保定市容城县公司】 已收件"));
    }
    @Override
    public void initEvent() {

    }
    private void getWuLiu() {
        Map<String, String> map = new HashMap<>();
        map.put("uni",getIntent().getStringExtra("uni"));
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getWuLiu(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        JSONObject wuliu=res.getJSONObject("data").getJSONObject("express").getJSONObject("result");
                        tvWaybillNumber.setText(wuliu.getString("number"));
                        tvDeliverCompany.setText(wuliu.getString("expName"));
                        tvOfficialPhone.setText(wuliu.getString("expPhone"));

                        JSONArray jsonArray=wuliu.getJSONArray("list");
                        for (int i = 0; i <jsonArray.length() ; i++) {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            beans.add(new LogisticsInfoBean(jsonObject.getString("time"), jsonObject.getString("status")));
                        }
                        adapter.notifyDataSetChanged();
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
}
