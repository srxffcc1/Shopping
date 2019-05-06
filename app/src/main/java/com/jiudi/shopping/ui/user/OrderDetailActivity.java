package com.jiudi.shopping.ui.user;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.OrderDetail;
import com.jiudi.shopping.bean.OrderEvent;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.main.PayAllReleaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderDetailActivity extends BaseActivity {
    private android.widget.RelativeLayout rlLayoutTopBackBar;
    private android.widget.LinearLayout llLayoutTopBackBarBack;
    private android.widget.TextView tvLayoutTopBackBarStart;
    private android.widget.TextView tvLayoutTopBackBarTitle;
    private android.widget.TextView tvLayoutTopBackBarEnd;
    private android.widget.TextView tvLayoutBackTopBarOperate;
    private android.widget.TextView orderTitle;
    private android.widget.TextView orderAddress;
    private android.widget.ImageView orderImg;
    private android.widget.TextView orderName;
    private android.widget.TextView orderMoney;
    private android.widget.TextView orderCount;
    private android.widget.TextView orderSum;
    private android.widget.TextView orderReal;
    private android.widget.TextView orderTran;
    private android.widget.TextView orderNumber;
    private android.widget.TextView orderTime;
    private android.widget.TextView orderPaytime;
    private android.widget.TextView orderCompany;
    private android.widget.TextView orderTranid;
    private android.widget.LinearLayout canhidepay;
    private android.widget.TextView orderSubmit;
    private android.widget.TextView orderCancel;
    public OrderDetail bean;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        orderTitle = (TextView) findViewById(R.id.order_title);
        orderAddress = (TextView) findViewById(R.id.order_address);
        orderImg = (ImageView) findViewById(R.id.order_img);
        orderName = (TextView) findViewById(R.id.order_name);
        orderMoney = (TextView) findViewById(R.id.order_money);
        orderCount = (TextView) findViewById(R.id.order_count);
        orderSum = (TextView) findViewById(R.id.order_sum);
        orderReal = (TextView) findViewById(R.id.order_real);
        orderTran = (TextView) findViewById(R.id.order_tran);
        orderNumber = (TextView) findViewById(R.id.order_number);
        orderTime = (TextView) findViewById(R.id.order_time);
        orderPaytime = (TextView) findViewById(R.id.order_paytime);
        orderCompany = (TextView) findViewById(R.id.order_company);
        orderTranid = (TextView) findViewById(R.id.order_tranid);
        canhidepay = (LinearLayout) findViewById(R.id.canhidepay);
        orderSubmit = (TextView) findViewById(R.id.order_submit);
        orderCancel = (TextView) findViewById(R.id.order_cancel);
    }

    @Override
    public void initData() {
        getOrderDetail();
    }
    private void cancelOrder(String order_id) {
        Map<String, String> map = new HashMap<>();
        if (AccountManager.sUserBean != null) {
//            map.put("customer_id", AccountManager.sUserBean.getId());
            map.put("order_id", order_id);
            RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).cancelOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject res = new JSONObject(response);
                        int code = res.getInt("code");
                        String info = res.getString("info");
                        if (code == 0) {
                            EventBus.getDefault().post(new OrderEvent());
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable t) {

                }
            });
        } else {

        }

    }
    @Override
    public void initEvent() {
        orderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder(bean.id);
            }
        });

        orderSubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order_id = bean.id;//获得的本司订单号
                String paytypekey = "pay_type";
                String url = "index.php?g=app&m=appv1&a=goodsPay";
                Map<String, String> postmap = new HashMap<>();
//                postmap.put("customer_id",AccountManager.sUserBean.getId());
                postmap.put("url",url);
                postmap.put("paymm",bean.goods_all_price);
                postmap.put("paytypekey",paytypekey);
                postmap.put("order_id",order_id);
                PayAllReleaseActivity.open(mActivity, postmap);//打开充值界面 选择支付类型 然后会访问url交换对应的sign或appid来完成充值
            }
        });
    }
    private void getOrderDetail() {
        Map<String, String> map = new HashMap<>();
//        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("order_id",getIntent().getStringExtra("order_id"));
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getOrderDetail(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        JSONObject jsonObject=res.getJSONObject("data");
                        bean=new OrderDetail();
                        bean.id = jsonObject.optString("id");
                        bean.out_trade_no = jsonObject.optString("out_trade_no");
                        bean.trade_no = jsonObject.optString("trade_no");
                        bean.user_id = jsonObject.optString("user_id");
                        bean.goods_id = jsonObject.optString("goods_id");
                        bean.format_id = jsonObject.optString("format_id");
                        bean.goods_price = jsonObject.optString("goods_price");
                        bean.goods_all_price = jsonObject.optString("goods_all_price");
                        bean.goods_name = jsonObject.optString("goods_name");
                        bean.goods_number = jsonObject.optString("goods_number");
                        bean.goods_img = jsonObject.optString("goods_img");
                        bean.goods_format = jsonObject.optString("goods_format");
                        bean.goods_unit = jsonObject.optString("goods_unit");
                        bean.order_status = jsonObject.optString("order_status");
                        bean.pay_status = jsonObject.optString("pay_status");
                        bean.pay_type = jsonObject.optString("pay_type");
                        bean.postage = jsonObject.optString("postage");
                        bean.address_id = jsonObject.optString("address_id");
                        bean.receiver_name = jsonObject.optString("receiver_name");
                        bean.receiver_address = jsonObject.optString("receiver_address");
                        bean.receiver_mobile = jsonObject.optString("receiver_mobile");
                        bean.express_name = jsonObject.optString("express_name");
                        bean.express_no = jsonObject.optString("express_no");
                        bean.pay_fee = jsonObject.optString("pay_fee");
                        bean.note = jsonObject.optString("note");
                        bean.cutoff_time = jsonObject.optString("cutoff_time");
                        bean.take_time = jsonObject.optString("take_time");
                        bean.auto_take_time = jsonObject.optString("auto_take_time");
                        bean.create_time = jsonObject.optString("create_time");
                        bean.pay_time = jsonObject.optString("pay_time");
                        bean.isdelete = jsonObject.optString("isdelete");
                        bean.action = jsonObject.optString("action");
                        bean.order_status_text = jsonObject.optString("order_status_text");
                        bindData();
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

    private void bindData() {
        orderTitle.setText(bean.receiver_name+" "+bean.receiver_mobile);
        orderAddress.setText(bean.receiver_address);
        orderName.setText(bean.goods_name);
        orderMoney.setText(bean.goods_price);
        orderCount.setText("x"+bean.goods_number);
        orderSum.setText(bean.goods_all_price);
        orderReal.setText(bean.goods_price);
        orderTran.setText(bean.pay_fee);
        orderNumber.setText(bean.out_trade_no);
        orderTime.setText(bean.create_time);
        orderPaytime.setText(bean.pay_time);
        orderCompany.setText(bean.express_name);
        orderTranid.setText(bean.express_no);
        if (Integer.parseInt(bean.order_status) == 0) {
            findViewById(R.id.order_submit).setVisibility(View.VISIBLE);
        } else {

            findViewById(R.id.order_submit).setVisibility(View.GONE);
        }
        if (Integer.parseInt(bean.order_status) >= 5) {
            findViewById(R.id.order_cancel).setVisibility(View.GONE);
        } else {

            findViewById(R.id.order_cancel).setVisibility(View.VISIBLE);
        }
    }
}
