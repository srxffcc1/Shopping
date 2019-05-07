package com.jiudi.shopping.ui.cart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.AlipayPayResultBean;
import com.jiudi.shopping.event.WechatPayEvent;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.user.AddressListActivity;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.util.WechatUtil;
import com.jiudi.shopping.widget.BigRadioGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayDingDanActivity extends BaseActivity {
    private static final String TAG = "PayDingDanActivity";
    public String orderKey;
    public String addressId;
    private TextView lijigoumai;
    private BigRadioGroup paytypegroup;
    private String paytype;
    /**
     * 支付失败弹窗
     */
    private PopupWindow mPayFailPopWindow;
    private int mPayMethod = TYPE_PAY_ALIPAY;
    /**
     * 微信支付
     */
    public static final int TYPE_PAY_WECHAT = 2;
    /**
     * 支付宝支付
     */
    public static final int TYPE_PAY_ALIPAY = 1;
    /**
     * 余额支付
     */
    public static final int TYPE_PAY_YUE = 3;

    private static final int ZHIFUBAO_SDK_PAY_FLAG = 100;
    private LinearLayout godss;
    private TextView name;
    private TextView address;
    private TextView cartGroup;
    private TextView needpaymoney;
    private RelativeLayout changeaddress;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_cart_pay;
    }

    @Override
    public void initView() {

        lijigoumai = (TextView) findViewById(R.id.lijigoumai);
        paytypegroup = (BigRadioGroup) findViewById(R.id.paytypegroup);
        godss = (LinearLayout) findViewById(R.id.godss);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        cartGroup = (TextView) findViewById(R.id.cart_group);
        needpaymoney = (TextView) findViewById(R.id.needpaymoney);
        changeaddress = (RelativeLayout) findViewById(R.id.changeaddress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initData() {
        DialogUtil.showUnCancelableProgress(mActivity, "订单生成中");
        getCartPayDetail();
        getDefaultAddress();
        getCoupon();
    }

    private void getCoupon() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getCoupon(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
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

    @Override
    public void initEvent() {
        lijigoumai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPay();
            }
        });
        changeaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passToAddAdress();
            }
        });
    }

    private void toPay() {
        Map<String, String> map = new HashMap<>();
        map.put("key", orderKey);
        map.put("addressId", addressId);
        map.put("bargainId", "");
        map.put("couponId", "");
        if (paytypegroup.getCheckedRadioButtonId() == R.id.checkweixin) {
            mPayMethod = TYPE_PAY_WECHAT;
            paytype = "weixin";
        }
        if (paytypegroup.getCheckedRadioButtonId() == R.id.checkxianjin) {
            mPayMethod = TYPE_PAY_YUE;
            paytype = "yue";

        }
        if (paytypegroup.getCheckedRadioButtonId() == R.id.checkzhifubao) {
            mPayMethod = TYPE_PAY_ALIPAY;
            paytype = "ali";

        }
        map.put("payType", paytype);
        map.put("seckill_id", "");
        map.put("useIntegral", "");
        map.put("mark", "");
        map.put("combinationId", "");

        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).sendOrder(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        if (mPayMethod == TYPE_PAY_WECHAT) {
                            toWePay(res);
                        }
                        if (mPayMethod == TYPE_PAY_YUE) {

                        }
                        if (mPayMethod == TYPE_PAY_ALIPAY) {
                            toAliPay(res);

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

    private void toWePay(JSONObject res) {
        try {
            JSONObject data = res.getJSONObject("data").getJSONObject("result").getJSONObject("jsConfig");
            String appId = data.getString("appid");
            String partnerId = data.getString("partnerid");
            String prepayId = data.getString("prepayid");
            String packageValue = data.getString("package");
            String nonceStr = data.getString("noncestr");
            String timeStamp = data.getString("timestamp");
            String sign = data.getString("sign");
            WechatUtil.wechatPay(appId, partnerId, prepayId, packageValue, nonceStr, timeStamp, sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void toAliPay(JSONObject res) {
        try {
            String data = res.getJSONObject("data").getJSONObject("result").getString("jsConfig");
            alipayPay(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCartPayDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("cartId", getIntent().getStringExtra("cartId"));
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getCartPayDetail(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        JSONObject data = res.getJSONObject("data");
                        orderKey = data.optString("orderKey");
                        buildCartList(data);
                        cartGroup.setText("共" + sum_cart + "件商品 小计:" + data.getJSONObject("priceGroup").getString("totalPrice") + "元");
                        needpaymoney.setText("应付:¥" + data.getJSONObject("priceGroup").getString("totalPrice") + "");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                DialogUtil.hideProgress();
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    public int sum_cart = 0;

    private void buildCartList(JSONObject data) {
        try {
            JSONArray cartInfoList = data.getJSONArray("CartInfo");
            for (int i = 0; i < cartInfoList.length(); i++) {
                View cartinfol = LayoutInflater.from(mActivity).inflate(R.layout.item_cart_item, godss, false);
                JSONObject cartInfo = cartInfoList.getJSONObject(i);
                ImageView imageView = cartinfol.findViewById(R.id.cart_icon2);
                TextView cart_title = cartinfol.findViewById(R.id.cart_title);
                TextView cart_cunk = cartinfol.findViewById(R.id.cart_cunk);
                TextView cart_count = cartinfol.findViewById(R.id.cart_count);
                TextView cart_money = cartinfol.findViewById(R.id.cart_money);
                String pic = cartInfo.getJSONObject("productInfo").getString("image");
                String title = cartInfo.getJSONObject("productInfo").getString("store_name");
                try {
                    String cunk = cartInfo.getJSONObject("productInfo").getJSONObject("attrInfo").getString("suk");
                    cart_cunk.setText(cunk);
                } catch (JSONException e) {
                    cart_cunk.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
                String count = "X" + cartInfo.getString("cart_num");
                sum_cart += Integer.parseInt(cartInfo.getString("cart_num"));
                String money = "¥" + cartInfo.getJSONObject("productInfo").getString("price");
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
                Glide.with(mActivity).load(pic).apply(options).into(imageView);
                cart_title.setText(title);
                cart_count.setText(count);
                cart_money.setText(money);
                godss.addView(cartinfol);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDefaultAddress() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getDefaultAddress(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        JSONObject data = res.getJSONObject("data");
                        addressId = data.getString("id");
                        buildAddress(data);
                    } else {
                        passToAddAdress();
                    }

                } catch (JSONException e) {
                    passToAddAdress();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void passToAddAdress() {
        startActivityForResult(new Intent(mActivity, AddressListActivity.class), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getDefaultAddress();
    }

    private void buildAddress(JSONObject data) {
        try {
            name.setText(data.getString("real_name") + data.getString("phone"));
            address.setText(data.getString("province") + data.getString("city") + data.getString("district") + data.getString("detail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWechatPayEvent(WechatPayEvent wechatPayEvent) {
        LogUtil.e(TAG, "onWechatPayEvent---wechatPayEvent");
        int result = wechatPayEvent.getResult();
        switch (result) {
            case 0:
                ToastUtil.showShort(mActivity, "支付成功");
                finish();
//                PayCompleteActivity.open(mActivity,mUnid);
                break;
            case -1:
                ToastUtil.showShort(mActivity, "支付失败");
                showPayPopWindow();
                finish();
                break;
            case -2:
                ToastUtil.showShort(mActivity, "取消了支付");
                finish();
                break;
        }
    }

    /**
     * 支付宝支付
     */
    private void alipayPay(final String signOrderData) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask((Activity) mContext);
                Map<String, String> result = alipay.payV2(signOrderData, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = ZHIFUBAO_SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ZHIFUBAO_SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked") AlipayPayResultBean payResult = new AlipayPayResultBean((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();//同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    LogUtil.e(TAG, resultInfo.toString());
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtil.showShort(mActivity, "支付成功");
//                        PayCompleteActivity.open(mActivity,mUnid);
                        successPay();
                    } else {
                        ToastUtil.showShort(mActivity, "支付失败");
                        showPayPopWindow();
                    }
                    break;
                }
            }
        }
    };

    //支付成功之后返回刚才的页面
    public void successPay() {
//        Intent intent=new Intent();
//        Bundle bundle = getIntent().getExtras();
//        Set<String> keys = bundle.keySet();
//        Iterator<String> it = keys.iterator();
//        while (it.hasNext()) {
//            String key = it.next();
//            if (!"url".equals(key) && !"paytypekey".equals(key) && !"paymm".equals(key)) {
//
//                String value = bundle.getString(key);
//                intent.putExtra(key, value);
//
//            }
//        }
//        setResult(Activity.RESULT_OK,intent);
//        finish();
    }

    private void showPayPopWindow() {
        if (mPayFailPopWindow != null && mPayFailPopWindow.isShowing()) {
            mPayFailPopWindow.dismiss();
        } else {
            View view1 = LayoutInflater.from(mActivity).inflate(R.layout.popwindow_pay_fail, null, false);

            TextView pay_cancel = (TextView) view1.findViewById(R.id.tv_popwindow_pay_fail_cancel);
            TextView pay_again = (TextView) view1.findViewById(R.id.tv_popwindow_pay_fail_again);
            pay_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPayFailPopWindow.dismiss();
                }
            });

            pay_again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toPay();
                }
            });
            mPayFailPopWindow = new PopupWindow(view1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mPayFailPopWindow.setBackgroundDrawable(new BitmapDrawable());
            mPayFailPopWindow.showAtLocation(view1, Gravity.NO_GRAVITY, 0, 0);

//            changeCanPay(true);
        }
    }
}
