package com.jiudi.shopping.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.PeccancyBean;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.StyleUtil;
import com.jiudi.shopping.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 违章历史记录
 */

public class PeccancyQueryDetailsActivity extends BaseActivity {
    private static final String TAG = "PeccancyQueryDetailsActivity";

    private LinearLayout mBackLL;
    private TextView mTitleTV;
    private RecyclerView mRecyclerView;

    private TwinklingRefreshLayout mRefreshLayout;
    private int mDataStatus = STATUS_REFRESH;
    private static final int STATUS_REFRESH = 1;
    private static final int STATUS_LOAD = 2;
    private int mPage = 1;

    private RecyclerCommonAdapter<PeccancyBean> mPeccancyCommonAdapter;
    private List<PeccancyBean> mCarBeanList = new ArrayList<>();

    private static final String EXTRA_CAR_ID = "cardId";
    private static final String EXTRA_CAR_PLATE = "cardPlate";
    private String mCarId;
    private String mCarPlate;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_peccancy_history;
    }

    @Override
    public void initView() {
        mBackLL = byId(R.id.ll_layout_top_back_bar_back);
        mTitleTV = byId(R.id.tv_layout_top_back_bar_title);
        mRecyclerView = byId(R.id.rv_activity_peccancy_history_list);
        mRefreshLayout = byId(R.id.trl_activity_peccancy_history_list);
        StyleUtil.setRefreshStyle(mActivity, mRefreshLayout);
    }

    @Override
    public void initData() {
        mCarId = getIntent().getStringExtra(EXTRA_CAR_ID);
        mCarPlate = getIntent().getStringExtra(EXTRA_CAR_PLATE);

        mTitleTV.setText(mCarPlate);
        getPeccancyHistory();
    }

    @Override
    public void initEvent() {
        mBackLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mDataStatus = STATUS_REFRESH;
                mPage = 1;
                getPeccancyHistory();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mDataStatus = STATUS_LOAD;
                mPage=1;
                getPeccancyHistory();
            }
        });
    }


    private void getPeccancyHistory() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("car_id", mCarId);

        LogUtil.e(TAG, map.toString());
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .getPeccancyHistory(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        LogUtil.e(TAG, response);
                        switch (mDataStatus) {
                            case STATUS_REFRESH:
                                mCarBeanList.clear();
                                mRefreshLayout.finishRefreshing();
                                break;
                            case STATUS_LOAD:
                                mRefreshLayout.finishLoadmore();
                                break;
                        }
                        try {
                            JSONObject res = new JSONObject(response);
                            int code = res.getInt("code");
                            String info = res.getString("info");
                            if (code == 0) {
                                mCarBeanList.clear();
                                JSONArray dataArray = res.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataItem = dataArray.getJSONObject(i);
                                    PeccancyBean peccancyBean = new PeccancyBean();
                                    peccancyBean.setDate(dataItem.getString("date"));
                                    peccancyBean.setArea(dataItem.getString("area"));
                                    peccancyBean.setAction(dataItem.getString("act"));
                                    peccancyBean.setFinePoints(dataItem.getString("fen"));
                                    peccancyBean.setFineMoney(dataItem.getString("money"));
                                    peccancyBean.setHandle(dataItem.getString("handled"));
                                    peccancyBean.setArchiveno(dataItem.getString("archiveno"));
                                    mCarBeanList.add(peccancyBean);
                                }
                                if (mCarBeanList.size() > 0) {
                                    showDataRecycleView();
                                } else {
                                    ToastUtil.showShort(mActivity, R.string.data_null);
                                }
                            } else {
                                ToastUtil.showShort(mActivity, info);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.e(TAG,t.getMessage());
                        switch (mDataStatus) {
                            case STATUS_REFRESH:
                                mRefreshLayout.finishRefreshing();
                                break;
                            case STATUS_LOAD:
                                mRefreshLayout.finishLoadmore();
                                break;
                        }
                        if (!NetworkUtil.isConnected()) {
                            ToastUtil.showShort(mActivity, getString(R.string.net_error));
                        } else {
                            ToastUtil.showShort(mActivity, getString(R.string.network_error));
                        }
                    }
                });
    }

    private void showDataRecycleView() {
        if (mPeccancyCommonAdapter == null) {
            mPeccancyCommonAdapter = new RecyclerCommonAdapter<PeccancyBean>(mActivity, R.layout.item_peccancy_history, mCarBeanList) {
                @Override
                protected void convert(ViewHolder holder, PeccancyBean peccancyBean, int position) {
                    holder.setText(R.id.tv_item_peccancy_history_date, peccancyBean.getDate());
                    holder.setText(R.id.tv_item_peccancy_history_action, peccancyBean.getAction());
                    holder.setText(R.id.tv_item_peccancy_history_place, peccancyBean.getArea());
                    holder.setText(R.id.tv_item_peccancy_history_points, peccancyBean.getFinePoints());
                    holder.setText(R.id.tv_item_peccancy_history_money, getString(R.string.pay_money, peccancyBean.getFineMoney()));
                }
            };
            mRecyclerView.setAdapter(mPeccancyCommonAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {
            mPeccancyCommonAdapter.notifyDataSetChanged();
        }
    }

    public static void open(Activity activity, String carId, String carPlate) {
        Intent intent = new Intent(activity, PeccancyQueryDetailsActivity.class);
        intent.putExtra(EXTRA_CAR_ID, carId);
        intent.putExtra(EXTRA_CAR_PLATE, carPlate);
        activity.startActivity(intent);
    }
}
