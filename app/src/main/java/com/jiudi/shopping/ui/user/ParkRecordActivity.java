package com.jiudi.shopping.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.BallPulseView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.CarOrderBean;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.main.PackFragment;
import com.jiudi.shopping.ui.main.PayActivity;
import com.jiudi.shopping.ui.user.account.LoginActivity;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.DisplayUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.StyleUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.widget.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParkRecordActivity extends BaseActivity {


    @BindView(R.id.ll_layout_top_back_bar_back)
    LinearLayout mLlLayoutTopBackBarBack;
    @BindView(R.id.tv_layout_top_back_bar_title)
    TextView mTvLayoutTopBackBarTitle;
    @BindView(R.id.tv_layout_back_top_bar_operate)
    TextView mTvLayoutBackTopBarOperate;
    @BindView(R.id.rl_layout_top_back_bar)
    RelativeLayout mRlLayoutTopBackBar;
    @BindView(R.id.rv_activity_park_record)
    RecyclerView mRvActivityParkRecord;
    @BindView(R.id.tfl_activity_park_record)
    TwinklingRefreshLayout mTflActivityParkRecord;
    @BindView(R.id.ll_activity_park_record_null)
    LinearLayout mNullParkRecordLL;

    private int mDataStatus = STATUS_REFRESH;
    private static final int STATUS_REFRESH = 1;
    private static final int STATUS_LOAD = 2;
    private int mPage = 1;
    private static final String TAG = "ParkRecordActivity";
    private RecyclerCommonAdapter<CarOrderBean> mLotteryShoppingAdapter;
    private List<CarOrderBean> mOrderList = new ArrayList<>();

    private String mParkId;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_park_record;
    }

    @Override
    public void initView() {
        mTvLayoutTopBackBarTitle.setText(R.string.park_order);
        StyleUtil.setRefreshStyle(mActivity, mTflActivityParkRecord);
        setRefreshStyle();
    }

    @Override
    public void initData() {
        if (AccountManager.sUserBean == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
        } else {
            getParkCarData();
        }
    }

    @Override
    public void initEvent() {
        mTflActivityParkRecord.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mDataStatus = STATUS_REFRESH;
                mPage = 1;
                getParkCarData();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mDataStatus = STATUS_LOAD;
                mPage++;
                getParkCarData();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void setRefreshStyle() {
        ProgressLayout headerView = new ProgressLayout(mActivity);
        BallPulseView loadingView = new BallPulseView(mActivity);
        mTflActivityParkRecord.setHeaderView(headerView);
        mTflActivityParkRecord.setBottomView(loadingView);
    }

    @OnClick({R.id.ll_layout_top_back_bar_back})
    public void onViewClicked() {
        finish();
    }

    /**
     * 停车记录
     */
    private void getParkCarData() {
        mParkId = (String) SPUtil.get(PackFragment.PARK_ID_KEY, "5065140070");//默认的云停车场ID为苏州测试
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("park_id", mParkId);
        LogUtil.e(TAG, map.toString());
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .getCarParkList(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e(TAG, response);
                DialogUtil.hideProgress();
                switch (mDataStatus) {
                    case STATUS_REFRESH:
                        mTflActivityParkRecord.finishRefreshing();
                        break;
                }
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    JSONArray data = res.optJSONArray("data");
                    if (code == 0) {
                        if (mDataStatus == STATUS_REFRESH) {
                            mOrderList.clear();
                        }
                        if(data!=null){
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject dataItem = data.getJSONObject(i);
                                CarOrderBean orderBean = new CarOrderBean();
                                orderBean.setId(dataItem.getString("id"));
                                orderBean.setParkName(dataItem.getString("park_name"));
                                orderBean.setPlate(dataItem.getString("plate"));
                                orderBean.setStatus(dataItem.getString("status"));
                                orderBean.setUnid(dataItem.getString("unid"));
                                orderBean.setDerateDuration(dataItem.getString("derate_duration"));
                                orderBean.setPaidFee(dataItem.getString("paid_fee"));
                                orderBean.setTotalFee(dataItem.getString("total_fee"));
                                orderBean.setTicketFee(dataItem.getString("ticket_fee"));
                                orderBean.setPrice(dataItem.getString("price"));
                                orderBean.setEntryTime(dataItem.getString("entry_time"));
                                orderBean.setDuration(dataItem.getString("duration"));
                                mOrderList.add(orderBean);
                            }

                        }
                        if (mOrderList.size()>0){
                            mNullParkRecordLL.setVisibility(View.GONE);
                            showDataRecycleView();
                        }else {
                            mNullParkRecordLL.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ToastUtil.showShort(mActivity, info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(mActivity, getString(R.string.data_error));
                    LogUtil.e(TAG, e.getMessage());
                }

            }

            @Override
            public void onError(Throwable t) {
                DialogUtil.hideProgress();
                if (!NetworkUtil.isConnected()) {
                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
                } else {
                    ToastUtil.showShort(mActivity, getString(R.string.network_error));
                }
            }
        });
    }

    private void showDataRecycleView() {
        if (mLotteryShoppingAdapter == null) {
            mLotteryShoppingAdapter = new RecyclerCommonAdapter<CarOrderBean>(mActivity, R.layout.item_parking_record, mOrderList) {

                @Override
                protected void convert(final ViewHolder holder, final CarOrderBean carBean, int position) {
                    //解决运行的时候不能返回ScrollView顶部
//                    mScrollView.smoothScrollTo(0, 20);
                    //解决RecyclerView和ScrollView的滑动冲突
                    mRvActivityParkRecord.setFocusable(false);
                    holder.setText(R.id.tv_item_parking_record_num, carBean.getPlate());
                    holder.setText(R.id.tv_item_parking_record_name, carBean.getParkName());
                    holder.setText(R.id.tv_item_parking_record_time, carBean.getDuration());
                    holder.setText(R.id.tv_item_parking_record_create_time, carBean.getEntryTime());
                    holder.setText(R.id.tv_item_parking_record_price, getString(R.string.tv_parking_pay_price, carBean.getPrice()));
                    TextView statusTV = holder.getView(R.id.tv_item_parking_record_status);
                    ImageView flgIV = holder.getView(R.id.iv_item_parking_record_flg);
                    TextView flgTV = holder.getView(R.id.tv_item_parking_record_flg);
                    switch (carBean.getStatus()) {//状态 0入场待付1预付待出2赊账待出场3赊账出场4已完成
                        case "0":
                            statusTV.setVisibility(View.VISIBLE);
                            statusTV.setText(getString(R.string.wait_pay));
                            flgTV.setText(getString(R.string.no_pay));
                            flgIV.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.no_pay));
                            break;
                        case "1":
                            statusTV.setVisibility(View.VISIBLE);
                            statusTV.setText(getString(R.string.pay_leave));
                            flgTV.setText(getString(R.string.no_pay));
                            flgIV.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.no_pay));
                            break;
                        case "2":
                            statusTV.setVisibility(View.VISIBLE);
                            statusTV.setText(getString(R.string.not_pay_no_leave));
                            flgTV.setText(getString(R.string.no_pay));
                            flgIV.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.no_pay));
                            break;
                        case "3":
                            statusTV.setVisibility(View.VISIBLE);
                            statusTV.setText(getString(R.string.not_pay_leave));
                            flgTV.setText(getString(R.string.no_pay));
                            flgIV.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.no_pay));
                            break;
                        case "4":
                            statusTV.setVisibility(View.GONE);
                            flgTV.setText(getString(R.string.completed));
                            flgIV.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.record_gou));
                            break;

                    }

                    statusTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PayActivity.open(mActivity,  carBean.getPrice(),  carBean.getUnid());
                        }
                    });
                }
            };

            mRvActivityParkRecord.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, (int) DisplayUtil.dpToPx(mActivity, 1), ContextCompat.getColor(mActivity, R.color.colorLine), false, 2));

            mRvActivityParkRecord.setAdapter(mLotteryShoppingAdapter);
            mRvActivityParkRecord.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {
            mLotteryShoppingAdapter.notifyDataSetChanged();
        }
    }

}
