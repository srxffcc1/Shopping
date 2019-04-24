package com.nado.shopping.ui.pay;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nado.shopping.R;
import com.nado.shopping.base.BaseActivity;
import com.nado.shopping.manager.AccountManager;
import com.nado.shopping.manager.RequestManager;
import com.nado.shopping.net.RetrofitCallBack;
import com.nado.shopping.net.RetrofitRequestInterface;
import com.nado.shopping.ui.main.PayAllActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShuiPayActivity extends BaseActivity {
    private android.widget.RelativeLayout rlLayoutTopBackBar;
    private android.widget.LinearLayout llLayoutTopBackBarBack;
    private android.widget.TextView tvLayoutTopBackBarTitle;
    private android.widget.TextView tvLayoutTopBackBarEnd;
    private android.widget.TextView tvLayoutBackTopBarOperate;
    private android.widget.LinearLayout noneedpay;
    private android.widget.TextView nojiaogfeidanwei;
    private android.widget.TextView nojiaofeihuhao;
    private android.widget.LinearLayout needpay;
    private android.widget.TextView year;
    private android.widget.TextView money;
    private android.widget.TextView jiaogfeidanwei;
    private android.widget.TextView jiaofeihuhao;
    private android.widget.TextView zhuzhi;
    private android.widget.TextView tvActivityLoginLoginLogin;
    private boolean needpayflag=true;
    private TextView tvLayoutTopBackBarStart;
    private TextView whichfei;
    private String order_idnoew;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_shuipay;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        noneedpay = (LinearLayout) findViewById(R.id.noneedpay);
        nojiaogfeidanwei = (TextView) findViewById(R.id.nojiaogfeidanwei);
        nojiaofeihuhao = (TextView) findViewById(R.id.nojiaofeihuhao);
        needpay = (LinearLayout) findViewById(R.id.needpay);
        year = (TextView) findViewById(R.id.year);
        money = (TextView) findViewById(R.id.money);
        jiaogfeidanwei = (TextView) findViewById(R.id.jiaogfeidanwei);
        jiaofeihuhao = (TextView) findViewById(R.id.jiaofeihuhao);
        zhuzhi = (TextView) findViewById(R.id.zhuzhi);
        tvActivityLoginLoginLogin = (TextView) findViewById(R.id.tv_activity_login_login_login);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        whichfei = (TextView) findViewById(R.id.whichfei);
        tvLayoutTopBackBarTitle.setText("缴纳水费");
        whichfei.setText("水费");
    }

    @Override
    public void initData() {
        needpayflag=getIntent().getBooleanExtra("needpayflag", true);
        if (needpayflag) {
            needpay.setVisibility(View.VISIBLE);
            noneedpay.setVisibility(View.GONE);
        } else {
            needpay.setVisibility(View.GONE);
            noneedpay.setVisibility(View.VISIBLE);
        }
        nojiaogfeidanwei.setText(getIntent().getStringExtra("company"));
        nojiaofeihuhao.setText(getIntent().getStringExtra("wecaccount"));
        year.setText(getIntent().getStringExtra(""));
        money.setText(getIntent().getStringExtra("totalamount")+"元");
        jiaogfeidanwei.setText(getIntent().getStringExtra("company"));
        jiaofeihuhao.setText(getIntent().getStringExtra("wecaccount"));
        zhuzhi.setText(getIntent().getStringExtra(""));



    }

    @Override
    public void initEvent() {
        tvActivityLoginLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildOrder();
            }
        });
    }

    /**
     * 生成订单
     */
    private void buildOrder() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("company_id", getIntent().getStringExtra("company_id"));
        map.put("wecaccount", getIntent().getStringExtra("wecaccount"));
        map.put("czmoney", getIntent().getStringExtra("totalamount"));
        map.put("type", "1");
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).getShuiOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        String order_id = res.get("data").toString();//获得的本司订单号
                        String paytypekey = "pay_type";
                        String pervalue = getIntent().getStringExtra("totalamount");//充值金额
                        String url = "index.php?g=app&m=life&a=goodsPay";
                        Map<String, String> postmap = new HashMap<>();
                        order_idnoew=order_id;
                        postmap.put("customer_id",AccountManager.sUserBean.getId());
                        postmap.put("paymm",pervalue);
                        postmap.put("url",url);
                        postmap.put("paytypekey",paytypekey);
                        postmap.put("order_id",order_id);
                        PayAllActivity.open(ShuiPayActivity.this, postmap);//打开充值界面 选择支付类型 然后会访问url交换对应的sign或appid来完成充值
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //充值成功的返回
        if(requestCode==PayAllActivity.START_PAY){
            if(resultCode== Activity.RESULT_OK){
                success();
                finish();
            }
        }
    }
    private void success() {
//        Map<String, String> map = new HashMap<>();
//        map.put("user_id", AccountManager.sUserBean.getId());
//        map.put("order_sn", order_idnoew);
//        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).successDian(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
//            @Override
//            public void onSuccess(String response) {
//                try {
//                    JSONObject res = new JSONObject(response);
//                    int code = res.getInt("code");
//                    String info = res.getString("info");
//                    if (code == 0) {
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//            }
//        });
    }
}
