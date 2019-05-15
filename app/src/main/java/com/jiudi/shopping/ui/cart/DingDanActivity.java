package com.jiudi.shopping.ui.cart;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.AlipayPayResultBean;
import com.jiudi.shopping.bean.CartStatus;
import com.jiudi.shopping.bean.Order;
import com.jiudi.shopping.bean.OrderDetail;
import com.jiudi.shopping.event.WechatPayEvent;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.user.account.AddDiscussActivity;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.TimeUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.util.WechatUtil;
import com.m7.imkfsdk.KfStartHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DingDanActivity extends BaseActivity {
    private Order orderbean;
    private OrderDetail orderDetail;
    private android.widget.ImageView head;
    private android.widget.TextView zhuangtai;
    private android.widget.TextView fahuo;
    private android.widget.ImageView dingwei;
    private android.widget.TextView xingming;
    private android.widget.TextView dianhua;
    private android.widget.TextView dizhi;
    private android.widget.TextView chanpinxinxi;
    private android.widget.LinearLayout godss;
    private android.widget.TextView lianxikefu;
    private android.widget.TextView tuikuan;
    private android.widget.RelativeLayout peisongfangshil;
    private android.widget.TextView peisongxinxinxi;
    private android.widget.TextView peisongfangshi;
    private android.widget.TextView kuaidigongsi;
    private android.widget.TextView kuaididanhao;
    private android.widget.TextView peisongfangshiv;
    private android.widget.TextView kuaidigongsiv;
    private android.widget.TextView kuaididanhaov;
    private android.widget.RelativeLayout jiagel;
    private android.widget.TextView shangpinzongjia;
    private android.widget.TextView yuedikou;
    private android.widget.TextView shifukuan;
    private android.widget.TextView shangpinzongjiav;
    private android.widget.TextView yuedikouv;
    private android.widget.TextView shifukuanv;
    private android.widget.TextView dingdanxinxi;
    private android.widget.TextView dingdanbianhao;
    private android.widget.TextView xiadanshijian;
    private android.widget.TextView zhifufangshi;
    private android.widget.TextView zhifuzhuangtai;
    private android.widget.TextView zhifushijian;
    private android.widget.TextView fanhui;
    private android.widget.TextView chakanwuliu;
    private android.widget.TextView querenshouhuo;
    private Dialog dialogchosetext;
    private String chosetext;
    private String uni;
    private static final String TAG = "DingDanActivity";
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

    @Override
    protected int getContentViewId() {
        return R.layout.activity_dingdan;
    }

    @Override
    public void initView() {

        head = (ImageView) findViewById(R.id.head);
        zhuangtai = (TextView) findViewById(R.id.zhuangtai);
        fahuo = (TextView) findViewById(R.id.fahuo);
        dingwei = (ImageView) findViewById(R.id.dingwei);
        xingming = (TextView) findViewById(R.id.xingming);
        dianhua = (TextView) findViewById(R.id.dianhua);
        dizhi = (TextView) findViewById(R.id.dizhi);
        chanpinxinxi = (TextView) findViewById(R.id.chanpinxinxi);
        godss = (LinearLayout) findViewById(R.id.godss);
        lianxikefu = (TextView) findViewById(R.id.lianxikefu);
        tuikuan = (TextView) findViewById(R.id.tuikuan);
        peisongfangshil = (RelativeLayout) findViewById(R.id.peisongfangshil);
        peisongxinxinxi = (TextView) findViewById(R.id.peisongxinxinxi);
        peisongfangshi = (TextView) findViewById(R.id.peisongfangshi);
        kuaidigongsi = (TextView) findViewById(R.id.kuaidigongsi);
        kuaididanhao = (TextView) findViewById(R.id.kuaididanhao);
        peisongfangshiv = (TextView) findViewById(R.id.peisongfangshiv);
        kuaidigongsiv = (TextView) findViewById(R.id.kuaidigongsiv);
        kuaididanhaov = (TextView) findViewById(R.id.kuaididanhaov);
        jiagel = (RelativeLayout) findViewById(R.id.jiagel);
        shangpinzongjia = (TextView) findViewById(R.id.shangpinzongjia);
        yuedikou = (TextView) findViewById(R.id.yuedikou);
        shifukuan = (TextView) findViewById(R.id.shifukuan);
        shangpinzongjiav = (TextView) findViewById(R.id.shangpinzongjiav);
        yuedikouv = (TextView) findViewById(R.id.yuedikouv);
        shifukuanv = (TextView) findViewById(R.id.shifukuanv);
        dingdanxinxi = (TextView) findViewById(R.id.dingdanxinxi);
        dingdanbianhao = (TextView) findViewById(R.id.dingdanbianhao);
        xiadanshijian = (TextView) findViewById(R.id.xiadanshijian);
        zhifufangshi = (TextView) findViewById(R.id.zhifufangshi);
        zhifuzhuangtai = (TextView) findViewById(R.id.zhifuzhuangtai);
        zhifushijian = (TextView) findViewById(R.id.zhifushijian);
        fanhui = (TextView) findViewById(R.id.fanhui);
        chakanwuliu = (TextView) findViewById(R.id.chakanwuliu);
        querenshouhuo = (TextView) findViewById(R.id.querenshouhuo);
        StyledDialog.init(this);

    }

    @Override
    public void initData() {
        orderbean = (Order) getIntent().getSerializableExtra("bean");
        bindDataToView();
        getOrderById();
//        getWuLiu();
        if (orderbean != null) {
            uni=orderbean.getOrder_id();
            if ("0".equals(orderbean.status) && "1".equals(orderbean.paid) && "0".equals(orderbean.refund_status)) {
                tuikuan.setVisibility(View.VISIBLE);
            }
        }
        if("2".equals(orderbean.getStatuz().getType())||"3".equals(orderbean.getStatuz().getType())){
            chakanwuliu.setVisibility(View.VISIBLE);
        }
        if("2".equals(orderbean.getStatuz().getType())){
            querenshouhuo.setText("确认收货");
        }
        else if("0".equals(orderbean.getStatuz().getType())){
            querenshouhuo.setText("立即支付");
        }else{
            querenshouhuo.setVisibility(View.GONE);
        }
    }

    private void getOrderById() {
        Map<String, String> map = new HashMap<>();
        map.put("uni", getIntent().getStringExtra("uni"));
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getOrderById(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                return new Date(json.getAsJsonPrimitive().getAsLong());
                            }
                        });
                        Gson gson = builder.create();
                        Type orderDetailType = new TypeToken<OrderDetail>() {
                        }.getType();
                        orderDetail = gson.fromJson(res.getJSONObject("data").toString(), orderDetailType);
                        bindDataToView2();
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

    private void bindDataToView2() {
        xiadanshijian.setText("下单时间：" + TimeUtil.formatLong(orderDetail.getAdd_time()));
        if ("未支付".equals(orderbean.getStatuz().getTitle())) {
            zhifushijian.setVisibility(View.GONE);
        } else {
            zhifushijian.setText("支付时间：" + TimeUtil.formatLong(orderDetail.getPay_time()));
        }
        dizhi.setText(orderDetail.getUser_address());
        dianhua.setText(orderDetail.getUser_phone());
        xingming.setText(orderDetail.getReal_name());
        yuedikouv.setText("-¥"+orderDetail.getUse_integral());
    }

    public void bindDataToView() {
        String cartlist = new Gson().toJson(orderbean.getCartInfoList());
        buildCartList(godss, cartlist,orderbean.getStatuz().getType());
        zhuangtai.setText(orderbean.getStatuz().get_title());

        if ("未支付".equals(orderbean.getStatuz().get_title())) {
            peisongfangshil.setVisibility(View.GONE);
        }else{
            peisongfangshil.setVisibility(View.GONE);
        }
        fahuo.setText(orderbean.getStatuz().getMsg());
        shifukuanv.setText("¥" + orderbean.getPay_price());
        shangpinzongjiav.setText("¥" + orderbean.getTotal_price());
        dingdanbianhao.setText("订单编号：" + orderbean.getOrder_id());
        zhifufangshi.setText("支付方式：" + orderbean.getStatuz().getPayType());
        zhifuzhuangtai.setText("支付状态：" + orderbean.getStatuz().getTitle());
        RequestOptions requestOptions = RequestOptions.circleCropTransform().error(R.drawable.head_defuat_circle);
        Glide.with(mActivity).load((AccountManager.sUserBean.avatar.startsWith("http"))?AccountManager.sUserBean.avatar:"http://"+AccountManager.sUserBean.avatar).apply(requestOptions).into(head);

    }

    //    private void getWuLiu() {
//        Map<String, String> map = new HashMap<>();
//        map.put("uni","wx2019042517361710007");
//        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getWuLiu(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
//            @Override
//            public void onSuccess(String response) {
//                try {
//                    JSONObject res = new JSONObject(response);
//                    int code = res.getInt("code");
//                    String info = res.getString("msg");
//                    if (code == 200) {
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
//    }
    private void tuiKuan(String text) {
        Map<String, String> map = new HashMap<>();
        map.put("uni", orderbean.getOrder_id());
        map.put("text",text);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).tuikuan(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        Toast.makeText(mActivity,"退货申请成功",Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void initEvent() {
        chakanwuliu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, WuLiuActivity.class).putExtra("uni", orderbean.getOrder_id()));
            }
        });
        tuikuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoseText();
            }
        });
        chakanwuliu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, WuLiuActivity.class).putExtra("uni",orderbean.getOrder_id()));
            }
        });
        querenshouhuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("确认收货".equals(querenshouhuo.getText().toString())){
                    qurenshouhuo(orderbean.getOrder_id());
                }
                if("立即支付".equals(querenshouhuo.getText().toString())){
                    lijizhifu();
                }
            }
        });
        lianxikefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KfStartHelper(mActivity).initSdkChat("e183f850-6650-11e9-b942-bf7a16e827df", "测试", "123456789",60);//陈辰正式
            }
        });
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    private void showChoseText() {
        dialogchosetext = StyledDialog.buildNormalInput("退款", "请输入退款理由", "",
                "确定", "取消",  new MyDialogListener() {
                    @Override
                    public void onFirst() {
                        tuiKuan(chosetext);
                    }

                    @Override
                    public void onSecond() {

                    }

                    @Override
                    public void onGetInput(CharSequence input1, CharSequence input2) {
                        super.onGetInput(input1, input2);
                        chosetext=input1.toString();
                    }
                }).setCancelable(true,true).show();
    }

    private void buildCartList(ViewGroup viewGroup, String cartInfoListString,String type) {
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

                        startActivity(new Intent(mActivity, AddDiscussActivity.class).putExtra("unique", finalUnique).putExtra("gods",cartInfo.toString()));
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
                        Toast.makeText(mActivity,"操作成功",Toast.LENGTH_SHORT).show();
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
                PayTask alipay = new PayTask(mActivity);
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
                    lijizhifu();
                }
            });
            mPayFailPopWindow = new PopupWindow(view1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mPayFailPopWindow.setBackgroundDrawable(new BitmapDrawable());
            mPayFailPopWindow.showAtLocation(view1, Gravity.NO_GRAVITY, 0, 0);

//            changeCanPay(true);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
