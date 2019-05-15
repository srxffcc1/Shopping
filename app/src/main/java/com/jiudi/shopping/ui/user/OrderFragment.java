package com.jiudi.shopping.ui.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.AlipayPayResultBean;
import com.jiudi.shopping.bean.CartAttrValue;
import com.jiudi.shopping.bean.CartInfo;
import com.jiudi.shopping.bean.Order;
import com.jiudi.shopping.bean.OrderEvent;
import com.jiudi.shopping.bean.CartStatus;
import com.jiudi.shopping.event.WechatPayEvent;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.cart.CartDetailActivity;
import com.jiudi.shopping.ui.cart.DingDanActivity;
import com.jiudi.shopping.ui.cart.WuLiuActivity;
import com.jiudi.shopping.ui.user.account.AddDiscussActivity;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.util.WechatUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;

public class OrderFragment extends BaseFragment {
    private static final String TAG = "OrderFragment";
    private android.support.v7.widget.RecyclerView rvFragmentHomeAll;
    private RecyclerCommonAdapter<Order> myAdapter;
    private List<Order> mBeanList = new ArrayList<>();

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
    public String uni;

    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_order;
    }

    @Override
    public void initView() {

        rvFragmentHomeAll = (RecyclerView) findViewById(R.id.rv_fragment_home_all);
    }

    @Override
    public void initData() {

        EventBus.getDefault().register(this);
        getOrderList(0);
    }

    private void getTestList(int page) {
//        Map<String, String> map = new HashMap<>();
//        if (AccountManager.sUserBean != null) {
////            map.put("customer_id", AccountManager.sUserBean.getId());
//            map.put("page", page + "");
//            map.put("order_status", getArguments().getString("order_status"));
//            map.put("limit", "10");
//            RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getGoodOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
//                @Override
//                public void onSuccess(String response) {
//
//                }
//
//                @Override
//                public void onError(Throwable t) {
//
//                }
//            });
//        } else {
//
//        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(OrderEvent wechatPayEvent) {
        getOrderList(0);
        DialogUtil.hideProgress();
    }

    private void getOrderList(int page) {
        Map<String, String> map = new HashMap<>();
//        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("first", page + "");
        map.put("type", getArguments().getString("type"));
        map.put("limit", "10");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getGoodOrder(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    mBeanList.clear();
                    if (code == 200) {
                        JSONArray jsonArray = res.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Order bean = new Order();
                            bean.combination_id=jsonObject.optString("combination_id");
                            bean.id=jsonObject.optString("id");
                            bean.order_id=jsonObject.optString("order_id");
                            bean.pay_price=jsonObject.optString("pay_price");
                            bean.total_num=jsonObject.optString("total_num");
                            bean.total_price=jsonObject.optString("total_price");
                            bean.pay_postage=jsonObject.optString("pay_postage");
                            bean.total_postage=jsonObject.optString("total_postage");
                            bean.paid=jsonObject.optString("paid");
                            bean.status=jsonObject.optString("status");
                            bean.refund_status=jsonObject.optString("refund_status");
                            bean.pay_type=jsonObject.optString("pay_type");
                            bean.coupon_price=jsonObject.optString("coupon_price");
                            bean.deduction_price=jsonObject.optString("deduction_price");
                            bean.pink_id=jsonObject.optString("pink_id");
                            bean.delivery_type=jsonObject.optString("delivery_type");
                            bean.combination_id=jsonObject.optString("combination_id");



                            bean.user_address=jsonObject.optString("user_address");

                            bean.real_name=jsonObject.optString("real_name");

                            bean.user_phone=jsonObject.optString("user_phone");


                            GsonBuilder builder = new GsonBuilder();
                            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                    return new Date(json.getAsJsonPrimitive().getAsLong());
                                }
                            });
                            Gson gson = builder.create();
                            Type cartStatusType = new TypeToken<CartStatus>() {
                            }.getType();
                            Type cartInfoType = new TypeToken<CartInfo>() {
                            }.getType();
                            String cartStatuss=jsonObject.getJSONObject("s_status").toString();
                            CartStatus cartStatus=gson.fromJson(cartStatuss,cartStatusType);
                            bean.setStatuz(cartStatus);

                            JSONObject cartInfoObj=jsonObject.getJSONObject("cartInfo");
                            Iterator iterator = cartInfoObj.keys();
                            while(iterator.hasNext()){
                                String key = iterator.next() + "";
                                CartInfo cartInfo=gson.fromJson(cartInfoObj.getJSONObject(key).toString(),cartInfoType);
                                bean.addCartInfo(cartInfo);
                            }

                            mBeanList.add(bean);
                        }
                    }
                    showRecycleView();

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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void cancelOrder(String order_id) {

//        DialogUtil.showUnCancelableProgress(mActivity, "订单取消中");
//        Map<String, String> map = new HashMap<>();
//        if (AccountManager.sUserBean != null) {
////            map.put("customer_id", AccountManager.sUserBean.getId());
//            map.put("order_id", order_id);
//            RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).cancelOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
//                @Override
//                public void onSuccess(String response) {
//                    try {
//                        JSONObject res = new JSONObject(response);
//                        int code = res.getInt("code");
//                        String info = res.getString("info");
//                        if (code == 0) {
//                            EventBus.getDefault().post(new OrderEvent());
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onError(Throwable t) {
//
//                }
//            });
//        } else {
//
//        }

    }

    @Override
    public void initEvent() {
    }

    private void showRecycleView() {
        if (myAdapter == null) {

            myAdapter = new RecyclerCommonAdapter<Order>(mActivity, R.layout.item_order, mBeanList) {
                @Override
                protected void convert(ViewHolder holder, final Order carChoiceBean, int position) {
                    LinearLayout linearLayout=holder.itemView.findViewById(R.id.allcart);
                    String cartlist=new Gson().toJson(carChoiceBean.getCartInfoList());
                    buildCartList(linearLayout,cartlist,carChoiceBean.getStatuz().getType());
                    String title=carChoiceBean.getStatuz().get_title();
                    String type=carChoiceBean.getStatuz().getType();
                    holder.setText(R.id.order_number,"订单："+carChoiceBean.getOrder_id());
                    holder.setText(R.id.summoney,"商品总价：¥"+carChoiceBean.getTotal_price()+"");
                    holder.setText(R.id.title,carChoiceBean.getStatuz().get_title());
                    final TextView function_0=holder.itemView.findViewById(R.id.function_0);
                    final TextView function_1=holder.itemView.findViewById(R.id.function_1);
                    if("0".equals(type)){//未支付
                        function_0.setVisibility(View.GONE);
                        function_1.setVisibility(View.VISIBLE);
                        function_1.setText("立即付款");
                    }
                    else if("1".equals(type)){//待发货

                        function_0.setVisibility(View.GONE);
                        function_1.setVisibility(View.GONE);
                    }
                    else if("2".equals(type)){//待收货
                        function_0.setVisibility(View.VISIBLE);
                        function_1.setVisibility(View.VISIBLE);
                        function_1.setText("查看物流");
                        function_0.setText("确认收货");

                    }
                    else if("3".equals(type)){//待评价
                        function_0.setVisibility(View.GONE);
                        function_1.setVisibility(View.GONE);
//                        function_0.setText("立即评价");
//                        function_1.setText("再来一单");
                    }else{
                        function_0.setVisibility(View.GONE);
                        function_1.setVisibility(View.GONE);
                    }
                    function_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if("立即付款".equals(function_1.getText().toString())){
                                uni=carChoiceBean.getOrder_id();
                                lijizhifu();
                            }else if("查看物流".equals(function_1.getText().toString())){
                                startActivity(new Intent(mActivity, WuLiuActivity.class).putExtra("uni",carChoiceBean.getOrder_id()));
                            }
                        }
                    });
                    function_0.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if("确认收货".equals(function_0.getText().toString())){
//                                uni=carChoiceBean.getOrder_id();
//                                lijizhifu();

                                qurenshouhuo(carChoiceBean.getOrder_id());
                            }
//                            if("立即评价".equals(function_0.getText().toString())){
//                                startActivity(new Intent(mActivity, AddDiscussActivity.class).putExtra("unique",carChoiceBean.getOrder_id()));
//                            }
                        }
                    });
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(mActivity, DingDanActivity.class).putExtra("uni",carChoiceBean.getOrder_id()).putExtra("bean",carChoiceBean));
                        }
                    });

                }

            };

//            rvFragmentHomeAll.addItemDecoration(RecyclerViewDivider.with(getActivity()).color(Color.parseColor("#909090")).build());
            rvFragmentHomeAll.setAdapter(myAdapter);
            rvFragmentHomeAll.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {
            myAdapter.notifyDataSetChanged();
        }
    }

    private void qurenshouhuo(String order_id) {
        Map<String, String> map = new HashMap<>();
        map.put("uni", order_id);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).qurenshouhuo(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
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

    public void lijizhifu(){
        DialogUtil.showUnCancelableProgress(mActivity, "支付开启中");
        Map<String, String> map = new HashMap<>();
        map.put("uni", uni);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).lijizhifu(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {

                        toWePay(res);
                    }
                    DialogUtil.hideProgress();
                } catch (JSONException e) {
                    DialogUtil.hideProgress();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }
    private void buildCartList(ViewGroup viewGroup,String cartInfoListString,String type) {
        try {
            JSONArray cartInfoList = new JSONArray(cartInfoListString);
            viewGroup.removeAllViews();
            for (int i = 0; i < cartInfoList.length(); i++) {
                View cartinfol = LayoutInflater.from(mActivity).inflate(R.layout.item_cart_item, viewGroup, false);
                final JSONObject cartInfo = cartInfoList.getJSONObject(i);
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
                String unique="";
                unique= cartInfo.getString("unique");
                TextView pinjia=cartinfol.findViewById(R.id.pingjia);
                if("3".equals(type)&&!cartInfo.optBoolean("is_reply")){
                    pinjia.setVisibility(View.VISIBLE);
                }
                final String finalUnique = unique;
                pinjia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(mActivity,AddDiscussActivity.class).putExtra("unique", finalUnique).putExtra("gods",cartInfo.toString()));
                    }
                });
                String count = "X" + cartInfo.getString("cart_num");
                String money = "¥" + cartInfo.getString("truePrice");
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
                Glide.with(mActivity).load(pic).apply(options).into(imageView);
                cart_title.setText(title);
                cart_count.setText(count);
                cart_money.setText(money);
                viewGroup.addView(cartinfol);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWechatPayEvent(WechatPayEvent wechatPayEvent) {
        LogUtil.e(TAG, "onWechatPayEvent---wechatPayEvent");
        int result = wechatPayEvent.getResult();
        switch (result) {
            case 0:
                ToastUtil.showShort(mActivity, "支付成功");
//                PayCompleteActivity.open(mActivity,mUnid);
                break;
            case -1:
                ToastUtil.showShort(mActivity, "支付失败");
                showPayPopWindow();
                break;
            case -2:
                ToastUtil.showShort(mActivity, "取消了支付");
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
                PayTask alipay = new PayTask(getActivity());
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
//    String orderKey;
//    String addressId;
//    String paytype;
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
                    lijizhifu();
                }
            });
            mPayFailPopWindow = new PopupWindow(view1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mPayFailPopWindow.setBackgroundDrawable(new BitmapDrawable());
            mPayFailPopWindow.showAtLocation(view1, Gravity.NO_GRAVITY, 0, 0);

//            changeCanPay(true);
        }
    }
    private void toPay(String orderKey,String addressId,String paytype) {
//        Map<String, String> map = new HashMap<>();
//        map.put("key", orderKey);
//        map.put("addressId", addressId);
//        map.put("bargainId", "");
//        map.put("couponId", "");
//        if ("weixin".equals(paytype)) {
//            mPayMethod = TYPE_PAY_WECHAT;
//        }
//        if ("yue".equals(paytype)) {
//            mPayMethod = TYPE_PAY_YUE;
//
//        }
//        if ("ali".equals(paytype)) {
//            mPayMethod = TYPE_PAY_ALIPAY;
//
//        }
//        map.put("payType", paytype);
//        map.put("seckill_id", "");
//        map.put("useIntegral", "");
//        map.put("mark", "");
//        map.put("combinationId", "");
//
//        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).sendOrder(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
//            @Override
//            public void onSuccess(String response) {
//                try {
//                    JSONObject res = new JSONObject(response);
//                    int code = res.getInt("code");
//                    String info = res.getString("msg");
//                    if (code == 200) {
//                        if (mPayMethod == TYPE_PAY_WECHAT) {
//
//                        }
//                        if (mPayMethod == TYPE_PAY_YUE) {
//
//                        }
//                        if (mPayMethod == TYPE_PAY_ALIPAY) {
//
//
//                        }
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
