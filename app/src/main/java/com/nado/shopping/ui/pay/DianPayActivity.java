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

public class DianPayActivity extends BaseActivity {
    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private LinearLayout noneedpay;
    private TextView nojiaogfeidanwei;
    private TextView nojiaofeihuhao;
    private LinearLayout needpay;
    private TextView year;
    private TextView money;
    private TextView jiaogfeidanwei;
    private TextView jiaofeihuhao;
    private TextView zhuzhi;
    private TextView tvActivityLoginLoginLogin;
    private boolean needpayflag=true;
    private TextView tvLayoutTopBackBarStart;
    private TextView whichfei;
    private String order_idnoew;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_dianpay;
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
        tvLayoutTopBackBarTitle.setText("缴纳电费");
        whichfei.setText("电费");
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
        map.put("user_id",AccountManager.sUserBean.getId());
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).getDianOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        String company=getIntent().getStringExtra("company");
                        String wecbillmoney=getIntent().getStringExtra("wecbillmoney");
                        String delayfee=getIntent().getStringExtra("delayfee");
                        String productid=getIntent().getStringExtra("productid");
                        String paytypekey = "flag";
                        String price = getIntent().getStringExtra("totalamount");//充值金额
                        String wecaccount=getIntent().getStringExtra("wecaccount");
                        String url = "index.php?g=app&m=power&a=pay_power";
                        Map<String, String> postmap = new HashMap<>();
                        postmap.put("company",company);
                        postmap.put("wecbillmoney",wecbillmoney);
                        postmap.put("productid",productid);
                        postmap.put("wecaccount",wecaccount);
                        postmap.put("order_id",res.optString("data"));
                        postmap.put("price",price);
                        postmap.put("delayfee",delayfee);
                        postmap.put("user_id",AccountManager.sUserBean.getId());
                        postmap.put("paymm",price);
                        postmap.put("url",url);
                        postmap.put("paytypekey",paytypekey);
                        order_idnoew=res.optString("data");
                        PayAllActivity.open(DianPayActivity.this, postmap);//打开充值界面 选择支付类型 然后会访问url交换对应的sign或appid来完成充值
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
        Map<String, String> map = new HashMap<>();
        map.put("user_id", AccountManager.sUserBean.getId());
        map.put("order_sn", order_idnoew);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).successDian(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {

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
