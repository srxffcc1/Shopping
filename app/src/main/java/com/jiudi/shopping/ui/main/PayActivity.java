package com.jiudi.shopping.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
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
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.AlipayPayResultBean;
import com.jiudi.shopping.event.WechatPayEvent;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.util.WechatUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayActivity extends BaseActivity {

    private static final String TAG = "PayActivity";
    private TextView mOrderAllTV;
    private TextView mTotalTV;

    private LinearLayout mBackLL;

    private LinearLayout mWechatPayLL;
    private ImageView mWechatPayIV;

    private LinearLayout mAlipayPayLL;
    private ImageView mAlipayPayIV;
    private TextView mConfirmPaymentTV;

    /**
     * 支付失败弹窗
     */
    private PopupWindow mPayFailPopWindow;
    /**
     * 2:微信充值 1：支付宝充值
     */
    private int mPayMethod = TYPE_PAY_ALIPAY;

    /**
     * 微信支付
     */
    public static final int TYPE_PAY_WECHAT = 2;
    /**
     * 支付宝支付
     */
    public static final int TYPE_PAY_ALIPAY = 1;
    private static final String PAY_MONEY = "pay";
    private static final int ZHIFUBAO_SDK_PAY_FLAG = 100;
    private String mPay;
    /**
     * unid 订单编号标志
     */
    private static final String UNID_CODE = "unid";
    private String mUnid;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_pay;
    }

    @Override
    public void initView() {
        mBackLL = byId(R.id.ll_layout_top_back_bar_back);
        mOrderAllTV = byId(R.id.tv_activity_pay_price_second);
        mTotalTV = byId(R.id.tv_activity_pay_price_total);
        mConfirmPaymentTV = byId(R.id.tv_activity_pay_complete_again);

        mWechatPayLL = byId(R.id.ll_activity_pay_wx);
        mWechatPayIV = byId(R.id.iv_activity_pay_wx);
        mAlipayPayLL = byId(R.id.ll_activity_pay_ali);
        mAlipayPayIV = byId(R.id.iv_activity_pay_complete_plate);
    }

    @Override
    public void initData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            mPay = getIntent().getExtras().getString(PAY_MONEY);
            mUnid = getIntent().getExtras().getString(UNID_CODE);
            mOrderAllTV.setText(mPay);
            mTotalTV.setText(mPay);
            mConfirmPaymentTV.setText("确认支付" + mTotalTV.getText().toString().trim() + "元");
        }

    }

    @Override
    public void initEvent() {
        mBackLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mWechatPayLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPayMethod != TYPE_PAY_WECHAT) {
                    mPayMethod = TYPE_PAY_WECHAT;
                    setPayStyle();
                }
            }
        });

        mAlipayPayLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPayMethod != TYPE_PAY_ALIPAY) {
                    mPayMethod = TYPE_PAY_ALIPAY;
                    setPayStyle();
                }
            }
        });


        mConfirmPaymentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                mConfirmPaymentTV.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.btn_blue_circle_square));
//                switch (mPayMethod) {
//                    case TYPE_PAY_WECHAT:
//                        ToastUtil.showLong(mActivity, "微信支付，使用中。。。");
//                        getWechatPayCheck(TYPE_PAY_WECHAT);
//                        break;
//                    case TYPE_PAY_ALIPAY:
//                        ToastUtil.showLong(mActivity, "支付宝支付，使用中。。。");
//                        getAlipayCheck(TYPE_PAY_ALIPAY);
//                        break;
//                }
            }
        });
    }


    /**
     * 获取微信支付签名数据
     */
    private void getWechatPayCheck(int type) {
        Map<String, String> map = new HashMap<>();
//        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("unid", mUnid);
        map.put("paytype",type+"");
        LogUtil.e(TAG, map.toString());
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .checkPay(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                DialogUtil.hideProgress();
                LogUtil.e(TAG, response);
                try {
                    JSONObject res = new JSONObject(response);
                    String info = res.getString("info");
                    int code = res.getInt("code");
                    if (code == 0) {
                        JSONObject data = res.getJSONObject("data");
                        String appId = data.getString("appid");
                        String partnerId = data.getString("partnerid");
                        String prepayId = data.getString("prepayid");
                        String packageValue = data.getString("package");
                        String nonceStr = data.getString("noncestr");
                        String timeStamp = data.getString("timestamp");
                        String sign = data.getString("sign");
                        WechatUtil.wechatPay(appId, partnerId, prepayId, packageValue, nonceStr, timeStamp, sign);
                    } else {
                        ToastUtil.showShort(mActivity, info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e(TAG, e.getMessage());
                    ToastUtil.showShort(mActivity, getString(R.string.json_error));
                }
            }

            @Override
            public void onError(Throwable t) {
                DialogUtil.hideProgress();
                LogUtil.e(TAG, t.getMessage());
                if (!NetworkUtil.isConnected()) {
                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
                } else {
                    ToastUtil.showShort(mActivity, getString(R.string.network_error));
                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWechatPayEvent(WechatPayEvent wechatPayEvent) {
        LogUtil.e(TAG, "onWechatPayEvent---wechatPayEvent");
        int result = wechatPayEvent.getResult();
        switch (result) {
            case 0:
                ToastUtil.showShort(mActivity, "支付成功");
                PayCompleteActivity.open(mActivity,mUnid);
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
     * 获取支付宝支付签名数据
     */
    private void getAlipayCheck(int typePay) {
        Map<String, String> map = new HashMap<>();
//        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("unid", mUnid);
        map.put("paytype", typePay + "");
        LogUtil.e(TAG, map.toString());
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .checkPay(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                DialogUtil.hideProgress();
                LogUtil.e(TAG, response);
                try {
                    JSONObject res = new JSONObject(response);
                    String info = res.getString("info");
                    int code = res.getInt("code");
                    if (code == 0) {
                        String data = res.getString("data");
                        alipayPay(data);
                    } else {
                        ToastUtil.showShort(mActivity, info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e(TAG, e.getMessage());
                    ToastUtil.showShort(mActivity, getString(R.string.json_error));
                }
            }

            @Override
            public void onError(Throwable t) {
                DialogUtil.hideProgress();
                LogUtil.e(TAG, t.getMessage());
                if (!NetworkUtil.isConnected()) {
                    ToastUtil.showShort(mActivity, getString(R.string.network_error));
                } else {
                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
                }
            }
        });
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
                        PayCompleteActivity.open(mActivity,mUnid);
                    } else {
                        ToastUtil.showShort(mActivity, "支付失败");
                        showPayPopWindow();
                    }
                    break;
                }
            }
        }
    };


    private void setPayStyle() {
        switch (mPayMethod) {
            case TYPE_PAY_WECHAT:
                mAlipayPayIV.setImageResource(R.drawable.circle_no);
                mWechatPayIV.setImageResource(R.drawable.circle_blue_new);

                break;
            case TYPE_PAY_ALIPAY:
                mAlipayPayIV.setImageResource(R.drawable.circle_blue_new);
                mWechatPayIV.setImageResource(R.drawable.circle_no);
                break;
        }
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
                    switch (mPayMethod) {
                        case TYPE_PAY_WECHAT:
                            getWechatPayCheck(TYPE_PAY_WECHAT);
                            break;
                        case TYPE_PAY_ALIPAY:
                            getAlipayCheck(TYPE_PAY_ALIPAY);
                            break;
                    }
                }
            });
            mPayFailPopWindow = new PopupWindow(view1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mPayFailPopWindow.setBackgroundDrawable(new BitmapDrawable());
            mPayFailPopWindow.showAtLocation(view1, Gravity.NO_GRAVITY, 0, 0);
        }
    }

    public static void open(Activity activity, String payMoney, String code) {
        Intent intent = new Intent(activity, PayActivity.class);
        intent.putExtra(PAY_MONEY, payMoney);
        intent.putExtra(UNID_CODE, code);
        activity.startActivity(intent);
    }
}
