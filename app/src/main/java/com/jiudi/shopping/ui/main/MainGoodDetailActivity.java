package com.jiudi.shopping.ui.main;

import android.content.Intent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.DiZHi;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.device.DeviceBindActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainGoodDetailActivity extends BaseActivity {
    private android.webkit.WebView webView;
    private android.widget.TextView goodSubmit;
    private android.widget.RelativeLayout rlLayoutTopBackBar;
    private android.widget.LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarStart;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private DiZHi dizhi;
    private String goods_id;
    private String goods_price;
    private String format_id;
    private TextView goodBind;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main_gooddetail;
    }

    @Override
    public void initView() {

        webView = (WebView) findViewById(R.id.webView);
        goodSubmit = (TextView) findViewById(R.id.good_submit);

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        goodBind = (TextView) findViewById(R.id.good_bind);
        tvLayoutTopBackBarTitle.setText("商品详情");

        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = webView.getSettings();
//设置 缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
// 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDisplayZoomControls(false);

//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setAllowFileAccess(true);
//        webSettings.setSavePassword(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        if(getIntent().getBooleanExtra("needbind",false)){
            goodBind.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initData() {
        getMainGood();
        getDiZHi();
    }

    private void getDiZHi() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getDefaultAddress(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        JSONObject jsonObject = res.getJSONObject("data");
                        dizhi = new DiZHi();
                        dizhi.id = jsonObject.optString("id");
                        dizhi.receiver_name = jsonObject.optString("receiver_name");
                        dizhi.receiver_mobile = jsonObject.optString("receiver_mobile");
                        dizhi.region = jsonObject.optString("region");
                        dizhi.detailed_address = jsonObject.optString("detailed_address");
                        dizhi.is_default = jsonObject.optString("is_default");


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

    private void getMainGood() {
        Map<String, String> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getGoodDetail(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        JSONObject jsonObject = res.getJSONObject("data");
                        try {
                            goods_id = jsonObject.optString("id");
                            goods_price = jsonObject.optString("show_price");
//                            format_id = jsonObject.getJSONObject("goods_format").optString("format_id");
                            String html = jsonObject.optString("details");
                            System.out.println(html);
                            webView.loadData(html, "text/html", "utf-8");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    @Override
    public void initEvent() {
        goodSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildOrder();
            }
        });
        goodBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, DeviceBindActivity.class));
            }
        });
    }

    private void buildOrder() {
        if(dizhi!=null){//说明有默认地址
            Map<String, String> map = new HashMap<>();
            map.put("customer_id", AccountManager.sUserBean.getId());
            map.put("goods_id", goods_id);
            map.put("goods_number", "1");
            map.put("goods_price", goods_price);
            map.put("address_id", dizhi.id);
            map.put("format_id", "1");//目前写死1后续可能取
            map.put("goods_id", goods_id);
            RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).buildGoodOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject res = new JSONObject(response);
                        int code = res.getInt("code");
                        String info = res.getString("info");
                        if (code == 0) {
                            JSONObject jsonObject=res.getJSONObject("data");
                            String order_id = jsonObject.getString("order_id");//获得的本司订单号
                            String paytypekey = "pay_type";
                            String url = "index.php?g=app&m=appv1&a=goodsPay";
                            Map<String, String> postmap = new HashMap<>();
                            postmap.put("customer_id",AccountManager.sUserBean.getId());
                            postmap.put("url",url);
                            postmap.put("paymm",goods_price);
                            postmap.put("paytypekey",paytypekey);
                            postmap.put("order_id",order_id);
                            PayAllReleaseActivity.open(mActivity, postmap);//打开充值界面 选择支付类型 然后会访问url交换对应的sign或appid来完成充值
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
}
