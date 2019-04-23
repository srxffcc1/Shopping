package com.nado.parking.ui.user;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.nado.parking.R;
import com.nado.parking.adapter.recycler.RecyclerCommonAdapter;
import com.nado.parking.adapter.recycler.base.ViewHolder;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.bean.CarBean;
import com.nado.parking.bean.PeccancyBean;
import com.nado.parking.manager.AccountManager;
import com.nado.parking.manager.RequestManager;
import com.nado.parking.net.RetrofitCallBack;
import com.nado.parking.net.RetrofitRequestInterface;
import com.nado.parking.util.DialogUtil;
import com.nado.parking.util.LogUtil;
import com.nado.parking.util.NetworkUtil;
import com.nado.parking.util.StyleUtil;
import com.nado.parking.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 违章
 */

public class PeccancyQueryActivity extends BaseActivity {
    private static final String TAG = "PeccancyQueryActivity";

    private LinearLayout mBackLL;
    private TextView mTitleTV;
    private RecyclerView mRecyclerView;

    private TwinklingRefreshLayout mRefreshLayout;
    private int mDataStatus = STATUS_REFRESH;
    private static final int STATUS_REFRESH = 1;
    private static final int STATUS_LOAD = 2;
    private int mPage = 1;

    private RecyclerCommonAdapter<CarBean> mCarCommonAdapter;
    private RecyclerCommonAdapter<PeccancyBean> mPeccancyCommonAdapter;
    private List<CarBean> mCarBeanList = new ArrayList<>();

    private LinearLayout mNullPeccancyQueryLL;

    private PopupWindow mTipPopWindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_peccancy_query;
    }

    @Override
    public void initView() {
        mBackLL = byId(R.id.ll_layout_top_back_bar_back);
        mTitleTV = byId(R.id.tv_layout_top_back_bar_title);
        mTitleTV.setText("违章查询");
        mRecyclerView = byId(R.id.rv_activity_peccancy_list);
        mRefreshLayout = byId(R.id.tfl_activity_peccancy_list);
        StyleUtil.setRefreshStyle(mActivity, mRefreshLayout);

        mNullPeccancyQueryLL= byId(R.id.ll_activity_peccancy_null);
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPeccancyData();

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
                getPeccancyData();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mDataStatus = STATUS_LOAD;
                mPage++;
                getPeccancyData();
            }
        });
        findViewById(R.id.tv_activity_user_car_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCarActivity1.open(mActivity);
            }
        });
        findViewById(R.id.tv_activity_user_car_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionActivity.open(mActivity, 12341);
            }
        });
    }

    /**
     * 查询违章记录
     */
    private void getPeccancyData() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());

        LogUtil.e(TAG, map.toString());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getPeccancyData(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
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
                            CarBean carBean = new CarBean();
                            ArrayList<PeccancyBean> mPeccancyList = new ArrayList<>();
                            carBean.setId(dataItem.getString("id"));
                            carBean.setPlate(dataItem.getString("plate"));
                            carBean.setEngine(dataItem.getString("engine"));
                            carBean.setFrame(dataItem.getString("frame"));
                            JSONArray listArray = dataItem.getJSONArray("list");
                            for (int j = 0; j < listArray.length(); j++) {
                                JSONObject listItem = listArray.getJSONObject(j);
                                PeccancyBean peccancyBean = new PeccancyBean();
                                peccancyBean.setDate(listItem.getString("date"));
                                peccancyBean.setArea(listItem.getString("area"));
                                peccancyBean.setAction(listItem.getString("act"));
                                peccancyBean.setFinePoints(listItem.getString("fen"));
                                peccancyBean.setFineMoney(listItem.getString("money"));
                                peccancyBean.setHandle(listItem.getString("handled"));
                                peccancyBean.setArchiveno(listItem.getString("archiveno"));
                                mPeccancyList.add(peccancyBean);
                            }
                            carBean.setPeccancyList(mPeccancyList);
                            mCarBeanList.add(carBean);
                        }

                    } else {
                        ToastUtil.showShort(mActivity, info);
                    }
                    if (mCarBeanList.size() > 0) {
                        mNullPeccancyQueryLL.setVisibility(View.GONE);
                        showDataRecycleView();
                    } else {
                        mNullPeccancyQueryLL.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable t) {
                LogUtil.e(TAG, t.getMessage());
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
        if (mCarCommonAdapter == null) {
            mCarCommonAdapter = new RecyclerCommonAdapter<CarBean>(mActivity, R.layout.item_peccancy_query, mCarBeanList) {

                @Override
                protected void convert(final ViewHolder holder, final CarBean record, final int position) {
                    List<PeccancyBean> littleHistoryList = record.getPeccancyList();
                    holder.setText(R.id.tv_item_peccancy_query_plate, record.getPlate());
                    TextView againQueryTV = holder.getView(R.id.tv_item_peccancy_query_again);
                    LinearLayout listHistoryLL = holder.getView(R.id.ll_item_history_query_list);
                    LinearLayout nullHistoryLL = holder.getView(R.id.ll_item_history_query_null);
                    RecyclerView peccancyListRV = holder.getView(R.id.rv_item_history_peccancy_list);
                    LinearLayout queryAllLL = holder.getView(R.id.ll_item_history_peccancy_all);

                    if (record.getPeccancyList().size() == 0) {
                        listHistoryLL.setVisibility(View.GONE);
                        nullHistoryLL.setVisibility(View.VISIBLE);
                    } else {
                        listHistoryLL.setVisibility(View.VISIBLE);
                        nullHistoryLL.setVisibility(View.GONE);


                        if (record.getPeccancyList().size() > 2) {
                            littleHistoryList.subList(0, 2).clear();
                        }
                        showPeccancyHistory(peccancyListRV, littleHistoryList);
                    }

                    againQueryTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            if (!TextUtils.isEmpty(record.getEngine())&& !TextUtils.isEmpty(record.getFrame())) {
//
//                            }else {
//
//                            }
                            showTipPopWindow(record);
                        }
                    });

                    queryAllLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PeccancyQueryDetailsActivity.open(mActivity, record.getId(), record.getPlate());
                        }
                    });
                }
            };

            mRecyclerView.setAdapter(mCarCommonAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {
            mCarCommonAdapter.notifyDataSetChanged();
        }
    }

    private void showTipPopWindow(final CarBean carBean) {
        if (mTipPopWindow != null && mTipPopWindow.isShowing()) {
            mTipPopWindow.dismiss();
        } else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.popwindow_improve_plate, null, false);
            final EditText carEngineET = view.findViewById(R.id.et_popwindow_improve_plate_engine);
            final EditText carFrameET = view.findViewById(R.id.et_popwindow_improve_plate_frame);
            TextView cancelTV = view.findViewById(R.id.tv_popwindow_improve_plate_cancel);
            TextView confirmTV = view.findViewById(R.id.tv_popwindow_improve_plate_confirm);

            cancelTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTipPopWindow.dismiss();
                }
            });

            confirmTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(carEngineET.getText().toString()) && !TextUtils.isEmpty(carFrameET.getText().toString())) {
                        DialogUtil.showProgress(mActivity);
                        checkUpCarInfo(carBean, carEngineET.getText().toString(), carFrameET.getText().toString());
                        mTipPopWindow.dismiss();
                    } else {
                        ToastUtil.showShort(mActivity, R.string.car_improve_null);
                    }
                }
            });
            mTipPopWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mTipPopWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
        }
    }

    /**
     * 补充车辆信息
     */
    private void checkUpCarInfo(CarBean carBean, String engine, String frame) {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("car_id", carBean.getId());
        map.put("engine", engine);//车辆发动机号6位
        map.put("frame", frame); //车辆车架号6位

        LogUtil.e(TAG, map.toString());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).updateCarInfo(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e(TAG, response);
                DialogUtil.hideProgress();
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        getPeccancyData();
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
                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
                } else {
                    ToastUtil.showShort(mActivity, getString(R.string.network_error));
                }
            }
        });
    }

    private void showPeccancyHistory(RecyclerView historyRecyclerView, List<PeccancyBean> peccancyBeanList) {
        mPeccancyCommonAdapter = new RecyclerCommonAdapter<PeccancyBean>(mActivity, R.layout.item_peccancy_history, peccancyBeanList) {
            @Override
            protected void convert(ViewHolder holder, PeccancyBean peccancyBean, int position) {
                holder.setText(R.id.tv_item_peccancy_history_date, peccancyBean.getDate());
                holder.setText(R.id.tv_item_peccancy_history_action, peccancyBean.getAction());
                holder.setText(R.id.tv_item_peccancy_history_place, peccancyBean.getArea());
                holder.setText(R.id.tv_item_peccancy_history_points, peccancyBean.getFinePoints());
                holder.setText(R.id.tv_item_peccancy_history_money, getString(R.string.pay_money, peccancyBean.getFineMoney()));
            }
        };
        historyRecyclerView.setAdapter(mPeccancyCommonAdapter);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }
}
