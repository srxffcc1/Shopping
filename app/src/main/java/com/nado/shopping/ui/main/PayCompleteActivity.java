package com.nado.shopping.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nado.shopping.R;
import com.nado.shopping.base.BaseActivity;
import com.nado.shopping.bean.CarOrderBean;
import com.nado.shopping.event.UpdatePayStatusEvent;
import com.nado.shopping.manager.AccountManager;
import com.nado.shopping.manager.RequestManager;
import com.nado.shopping.net.RetrofitCallBack;
import com.nado.shopping.net.RetrofitRequestInterface;
import com.nado.shopping.util.DateTimeUtil;
import com.nado.shopping.util.DialogUtil;
import com.nado.shopping.util.LogUtil;
import com.nado.shopping.util.NetworkUtil;
import com.nado.shopping.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者：Constantine on 2018/7/20.
 * 邮箱：2534159288@qq.com
 */
public class PayCompleteActivity extends BaseActivity {
    private static final String TAG = "PayCompleteActivity";
    private LinearLayout mBackLL;
    private TextView mTitleTV;

    private TextView mCarPlateTV;
    private TextView mParkNameTV;
    private TextView mPayPriceTV;
    private TextView mPayTimeTV;
    private TextView mServiceTV;
    private TextView mSubmit;
    private List<CarOrderBean> mOrderList = new ArrayList<>();

    private TextView mLeaveTimeMinTV;
    private TextView mLeaveTimeSecTV;
    private Timer mProductPromotionTimer;
    private TimerTask mProductPromotionTimerTask;

    public static final String EXTRA_UNID = "unid";
    private String mUnid;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_pay_complete;
    }

    @Override
    public void initView() {
        mBackLL = byId(R.id.ll_layout_top_back_bar_back);
        mTitleTV = byId(R.id.tv_layout_top_back_bar_title);
        mTitleTV.setText(R.string.now_pay);

        mLeaveTimeMinTV = byId(R.id.tv_activity_pay_complete_leave_timer_min);
        mLeaveTimeSecTV = byId(R.id.tv_activity_pay_complete_leave_timer_sec);
        mSubmit = byId(R.id.tv_activity_pay_complete_again);
        mServiceTV = byId(R.id.tv_activity_pay_complete_service);

        mCarPlateTV = byId(R.id.tv_activity_pay_complete_plate);
        mParkNameTV = byId(R.id.tv_activity_pay_complete_car_address);
        mPayPriceTV = byId(R.id.tv_activity_pay_complete_car_pay);
        mPayTimeTV = byId(R.id.tv_activity_pay_complete_car_pay_time);
    }

    @Override
    public void initData() {
        mUnid = getIntent().getExtras().getString(EXTRA_UNID);
        if (mUnid == null) {
            ToastUtil.showShort(mActivity, R.string.data_error);
        } else {

            getOrderDetail();
        }

    }

    @Override
    public void initEvent() {
        mBackLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeExit();
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showShort(mActivity, getString(R.string.test_data));
            }
        });
    }

    @Override
    public void onBackPressed() {
        completeExit();
    }

   private void completeExit(){
       EventBus.getDefault().post(new UpdatePayStatusEvent());
       finish();
   }

    /**
     * 我的停车订单详情
     */
    private void getOrderDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("unid", mUnid);
        
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getCarOrderDetail(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e(TAG, response);
                try {
                    JSONObject res = new JSONObject(response);
                    String info = res.getString("info");
                    int code = res.getInt("code");
                    if (code == 0) {
                        mOrderList.clear();
                        JSONObject dataItem = res.getJSONObject("data");
                        CarOrderBean carOrderBean = new CarOrderBean();
                        carOrderBean.setId(dataItem.getString("id"));
                        carOrderBean.setUnid(dataItem.getString("unid"));
                        carOrderBean.setTotalFee(dataItem.getString("total_fee"));
                        carOrderBean.setPaidFee(dataItem.getString("paid_fee"));
                        carOrderBean.setTicketFee(dataItem.getString("ticket_fee"));
                        carOrderBean.setEntryTime(dataItem.getString("entry_time"));
                        carOrderBean.setExitTime(dataItem.getString("exit_time"));
                        carOrderBean.setStatus(dataItem.getString("status"));
                        carOrderBean.setDerateDuration(dataItem.getString("derate_duration"));
                        carOrderBean.setDuration(dataItem.getString("duration"));
                        carOrderBean.setPayType(dataItem.getString("paytype"));
                        mParkNameTV.setText(dataItem.getString("park_name"));
                        mCarPlateTV.setText(dataItem.getString("plate"));
                        mPayPriceTV.setText(dataItem.getString("price"));
                        mPayTimeTV.setText(dataItem.getString("paytime"));

                        startGroupCountDown(dataItem.getString("paytime"));
                    } else {
                        ToastUtil.showShort(mActivity, info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
     * 剩余离场时间
     */
    private void startGroupCountDown(final String payTime) {

        if (mProductPromotionTimer != null) {
            mProductPromotionTimer.cancel();
            mProductPromotionTimer = null;
        }
        mProductPromotionTimer = new Timer();
        if (mProductPromotionTimerTask != null) {
            mProductPromotionTimerTask.cancel();
        }
        mProductPromotionTimerTask = new TimerTask() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setCountDown(payTime);
                    }
                });
            }
        };
        mProductPromotionTimer.schedule(mProductPromotionTimerTask, 0, 1000); //0s后执行task,经过1s再次执行
    }

    private void setCountDown(String endTime) {
        String mCurrentTime = DateTimeUtil.getCurrentDate(DateTimeUtil.FORMAT_YMD_HMS);
        int dateCompare = DateTimeUtil.compareTime(mCurrentTime, endTime, DateTimeUtil.FORMAT_YMD_HMS);
        if (dateCompare == -1) {
            String distanceTime[] = DateTimeUtil.getDistanceTime(mCurrentTime, endTime);
            for (int i = 1; i < distanceTime.length; i++) {
                if (distanceTime[i].length() == 1) {
                    distanceTime[i] = "0" + distanceTime[i];
                }
            }
            mLeaveTimeMinTV.setText(distanceTime[2]);
            mLeaveTimeSecTV.setText(distanceTime[3]);

        } else {
            mLeaveTimeMinTV.setText("00");
            mLeaveTimeSecTV.setText("00");
            //关闭页面
            completeExit();
        }
    }


    public static void open(Activity activity, String code) {
        Intent intent = new Intent(activity, PayCompleteActivity.class);
        intent.putExtra(EXTRA_UNID, code);
        activity.startActivity(intent);
    }
}
