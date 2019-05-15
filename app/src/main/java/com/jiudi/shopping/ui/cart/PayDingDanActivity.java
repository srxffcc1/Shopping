package com.jiudi.shopping.ui.cart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hss01248.dialog.StyledDialog;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.AlipayPayResultBean;
import com.jiudi.shopping.bean.Quan;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayDingDanActivity extends BaseActivity {
    private static final String TAG = "PayDingDanActivity";
    public String orderKey;
    public String addressId;
    private TextView lijigoumai;
    private BigRadioGroup paytypegroup;
    private String paytype;
    private List<Quan> mBeanList = new ArrayList<>();
    private RecyclerCommonAdapter<Quan> myAdapter;
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
    private ImageView dizhiicon;
    private TextView keyongyouhuiquan;
    private CheckBox yuecheck;
    private RadioButton checkweixin;
    private RadioButton checkxianjin;
    private RadioButton checkzhifubao;
    private String trueprice;
    private EditText mask;
    private TextView yuetext;
    private String yuetexts;
    private String xianjint;
    private TextView nowmoneys;
    private Dialog dialogquan;
    private double orgxiaoji;
    private TextView addAdress;

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
        dizhiicon = (ImageView) findViewById(R.id.dizhiicon);
        keyongyouhuiquan = (TextView) findViewById(R.id.keyongyouhuiquan);
        yuecheck = (CheckBox) findViewById(R.id.yuecheck);
        checkweixin = (RadioButton) findViewById(R.id.checkweixin);
        checkxianjin = (RadioButton) findViewById(R.id.checkxianjin);
        checkzhifubao = (RadioButton) findViewById(R.id.checkzhifubao);
        mask = (EditText) findViewById(R.id.mask);
        yuetext = (TextView) findViewById(R.id.yuetext);
        nowmoneys = (TextView) findViewById(R.id.nowmoneys);
        addAdress = (TextView) findViewById(R.id.add_adress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StyledDialog.init(this);
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
                        JSONArray jsonArray = res.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Quan bean = new Quan();
                            bean.id = jsonObject.optString("id");
                            bean.cid = jsonObject.optString("cid");
                            bean.uid = jsonObject.optString("uid");
                            bean.coupon_title = jsonObject.optString("coupon_title");
                            bean.coupon_price = jsonObject.optString("coupon_price");
                            bean.use_min_price = jsonObject.optString("use_min_price");
                            bean.add_time = jsonObject.optString("add_time");
                            bean.end_time = jsonObject.optString("end_time");
                            bean.use_time = jsonObject.optString("use_time");
                            bean.type = jsonObject.optString("type");
                            bean.status = jsonObject.optString("status");
                            bean.is_fail = jsonObject.optString("is_fail");
                            bean._add_time = jsonObject.optString("_add_time");
                            bean._end_time = jsonObject.optString("_end_time");
                            bean._type = jsonObject.optString("_type");
                            bean._msg = jsonObject.optString("_msg");
//                            if("全部".equals(getArguments().getString("type"))||bean.status.equals(getArguments().getString("type"))){
//                                mBeanList.add(bean);
//                            }

                            mBeanList.add(bean);

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
        keyongyouhuiquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!yuecheck.isChecked()) {
                    if (mBeanList.size() > 0) {

                        showPounList();
                    } else {

                        Toast.makeText(mActivity, "没有可以使用的优惠券", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mActivity, "余额抵扣和优惠券不可同时使用", Toast.LENGTH_SHORT).show();
                }
            }
        });
        yuecheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    keyongyouhuiquan.setText("不可使用优惠券");
                    yuetext.setText("抵扣金额：" + yuetexts);

                    cartGroup.setText("共" + sum_cart + "件商品 小计:" + (orgxiaoji - Double.parseDouble(yuetexts)) + "元");
                    needpaymoney.setText("应付:¥" + (orgxiaoji - Double.parseDouble(yuetexts)) + "");
                } else {
                    cartGroup.setText("共" + sum_cart + "件商品 小计:" + (orgxiaoji) + "元");
                    needpaymoney.setText("应付:¥" + (orgxiaoji) + "");
                    couponId = "";
                    keyongyouhuiquan.setText("点选使用优惠券");
                    yuetext.setText("余额抵扣");
                }
            }
        });
    }

    private void showPounList() {
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (myAdapter == null) {

            myAdapter = new RecyclerCommonAdapter<Quan>(mActivity, R.layout.item_quan4, mBeanList) {
                @Override
                protected void convert(ViewHolder holder, final Quan carChoiceBean, int position) {
                    LinearLayout quanboder1 = holder.getView(R.id.quanboder1);
//                    LinearLayout quanboder2 = holder.getView(R.id.quanboder2);
                    final String newstatus = Double.parseDouble(trueprice) >= Double.parseDouble(carChoiceBean.use_min_price) ? "0" : "1";


                    holder.setText(R.id.min, "满" + carChoiceBean.use_min_price + "元可用");
                    holder.setText(R.id.time, carChoiceBean._add_time + "至" + carChoiceBean._end_time);
                    holder.setText(R.id.money, "¥" + carChoiceBean.coupon_price);
                    CheckBox checkBox = holder.itemView.findViewById(R.id.check);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (!buttonView.isPressed()) {
                                return;
                            }
                            if (isChecked && "0".equals(newstatus)) {
                                setUpon(carChoiceBean);
                            } else {
                                unsetUpon();
                            }
                        }
                    });
                    if (carChoiceBean.id.equals(couponId)) {
                        checkBox.setChecked(true);
                    }
                    if ("0".equals(newstatus)) {
//                        holder.setText(R.id.status, "可使用");
                        quanboder1.setBackgroundColor(getResources().getColor(R.color.colorRed));
//                        quanboder1.setBackgroundResource(R.drawable.card_bg_r);
//                        quanboder2.setBackgroundResource(R.drawable.text_boder_quan_w);
                        checkBox.setVisibility(View.VISIBLE);
                    }
                    if ("1".equals(newstatus)) {
//                        holder.setText(R.id.status, "不可使用");
                        quanboder1.setBackgroundColor(getResources().getColor(R.color.colorGray));
//                        holder.setBackgroundRes(R.id.status, R.drawable.text_boder_quan_r);
//                        holder.setTextColor(R.id.min, Color.parseColor("#E9391C"));
//                        holder.setTextColor(R.id.time, Color.parseColor("#E9391C"));
//                        holder.setTextColor(R.id.money, Color.parseColor("#E9391C"));
//                        holder.setTextColor(R.id.status, Color.parseColor("#E9391C"));
                        holder.itemView.setEnabled(false);
                        checkBox.setVisibility(View.GONE);

                    }
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            setUpon(carChoiceBean);
//                        }
//                    });
//                    if("2".equals(newstatus)){
//                        holder.setText(R.id.status,"已过期");
//                        quanboder1.setBackgroundResource(R.drawable.card_bg_g);
//                        quanboder2.setBackgroundResource(R.drawable.text_boder_quan_w);
//
//                    }
                }

            };
        } else {

        }
        recyclerView.setAdapter(myAdapter);
        //不好建立回调
        dialogquan = StyledDialog.buildCustomBottomSheet(recyclerView).setCancelable(true,true).show();
    }

    String couponId;

    private void setUpon(Quan carChoiceBean) {
        couponId = carChoiceBean.id;
        if (dialogquan != null) {

            dialogquan.dismiss();
        }

        cartGroup.setText("共" + sum_cart + "件商品 小计:" + (orgxiaoji - Double.parseDouble(carChoiceBean.coupon_price)) + "元");
        needpaymoney.setText("应付:¥" + (orgxiaoji - Double.parseDouble(carChoiceBean.coupon_price)) + "");
//        keyongyouhuiquan.setText("优惠券抵扣:" + carChoiceBean.coupon_price + "元");
        keyongyouhuiquan.setText("已选择优惠券，节省" + carChoiceBean.coupon_price + "元");
    }

    private void unsetUpon() {
        couponId = "";
        keyongyouhuiquan.setText("点选使用优惠券");
    }

    private void toPay() {
        if (addressId == null) {
            Toast.makeText(mActivity, "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        DialogUtil.showUnCancelableProgress(mActivity, "开启支付");
        Map<String, String> map = new HashMap<>();
        map.put("key", orderKey);
        map.put("addressId", addressId);
        map.put("bargainId", "");
        map.put("mark", mask.getText().toString());
        if (!yuecheck.isChecked()) {
            if (couponId != null) {
                map.put("couponId", couponId);
            }
        }
        map.put("useIntegral", yuecheck.isChecked() ? "1" : "0");
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
                            if (info.contains("余额") || !res.getJSONObject("data").getJSONObject("result").has("jsConfig")) {
                                Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                toWePay(res);
                            }
                        }
                        if (mPayMethod == TYPE_PAY_YUE) {
                            Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        if (mPayMethod == TYPE_PAY_ALIPAY) {
                            toAliPay(res);

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DialogUtil.hideProgress();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(mActivity, "服务器返回出错", Toast.LENGTH_SHORT).show();
                DialogUtil.hideProgress();
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
                        orgxiaoji = data.getJSONObject("priceGroup").getDouble("totalPrice");
                        cartGroup.setText("共" + sum_cart + "件商品 小计:" + orgxiaoji + "元");
                        trueprice = data.getJSONObject("priceGroup").getString("totalPrice");
                        needpaymoney.setText("应付:¥" + orgxiaoji + "");
                        yuetexts = data.getJSONObject("userInfo").getString("integral");
                        xianjint = data.getJSONObject("userInfo").getString("now_money");
                        nowmoneys.setText("可用现金" + xianjint);

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
            JSONArray cartInfoList = data.getJSONArray("cartInfo");
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
                String money = "¥" + cartInfo.getString("truePrice");
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

    public int ispasstime = 0;

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
                        if (ispasstime == 0) {
                            passToAddAdress();
                            ispasstime = 1;
                        }
                    }

                } catch (JSONException e) {
                    if (ispasstime == 0) {
                        passToAddAdress();
                        ispasstime = 1;
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void passToAddAdress() {
        startActivityForResult(new Intent(mActivity, AddressListActivity.class).putExtra("needpay",true), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getDefaultAddress();
    }

    private void buildAddress(JSONObject data) {
        try {
            dizhiicon.setVisibility(View.VISIBLE);
            addAdress.setVisibility(View.GONE);
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
                ToastUtil.showShort(mActivity, "取消了支付,请重新购买或去未支付订单结算");
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
