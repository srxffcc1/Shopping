package com.nado.parking.ui.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.nado.parking.R;
import com.nado.parking.adapter.recycler.RecyclerCommonAdapter;
import com.nado.parking.adapter.recycler.base.ViewHolder;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.bean.ParkBean;
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
 * 作者：Constantine on 2018/9/5.
 * 邮箱：2534159288@qq.com
 */
public class ParkActivity extends BaseActivity {
    private static final String TAG = "ParkActivity";

    private LinearLayout mBackLL;
    private TextView mTitleTV;


    private RecyclerView mParkRV;
    private RecyclerCommonAdapter<ParkBean> mCommonAdapter;
    private List<ParkBean> mParkBeans = new ArrayList<>();
    private TwinklingRefreshLayout mRefreshLayout;
    private int mDataStatus = STATUS_REFRESH;
    private static final int STATUS_REFRESH = 1;
    private static final int STATUS_LOAD = 2;
    private int mPage = 1;

    public static final String CURRENT_CITY = "current_city";
    public static final String CITY_CODE = "city_code";
    private String current;
    private String cityId;

    private String mParkId;
    public static final int PARK_ID_OK = 0;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_park_lot;
    }

    @Override
    public void initView() {
        mBackLL = byId(R.id.ll_layout_top_back_bar_back);
        mTitleTV = byId(R.id.tv_layout_top_back_bar_title);
        Drawable drawableRight = getResources().getDrawable(
                R.drawable.triangel_white_down);
        /// 这一步必须要做,否则不会显示.
        drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
        mTitleTV.setCompoundDrawables(null,null,drawableRight,null);

        mRefreshLayout = byId(R.id.tfl_activity_park_lot);
        mParkRV = byId(R.id.rv_activity_park_lot);
        StyleUtil.setRefreshStyle(mActivity, mRefreshLayout);
    }

    @Override
    public void initData() {
        current = getIntent().getStringExtra(CURRENT_CITY);
        cityId=getIntent().getStringExtra(CITY_CODE);
        mTitleTV.setText(current);
        getParkInfo(cityId);//测试数据，当前为苏州城市ID

    }

    private void getParkInfo(String city) {
        DialogUtil.showProgress(mActivity);
        Map<String, String> map = new HashMap<>();
        if (AccountManager.sUserBean!=null){
            map.put("customer_id", AccountManager.sUserBean.getId());
        }
        map.put("city", city);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getParkList(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e(TAG,response);
                DialogUtil.hideProgress();
                switch (mDataStatus) {
                    case STATUS_REFRESH:
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
                        if (mDataStatus == STATUS_REFRESH) {
                            mParkBeans.clear();
                        }
                        JSONArray data = res.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject dataItem = data.getJSONObject(i);
                            ParkBean mParkBean = new ParkBean();
                            mParkBean.setId(dataItem.getString("id"));
                            mParkBean.setParkName(dataItem.getString("title"));
                            mParkBean.setParkAddress(dataItem.getString("address"));
                            mParkBean.setCloudId(dataItem.getString("park_id"));
                            mParkBean.setLat(dataItem.getDouble("lat"));
                            mParkBean.setLng(dataItem.getDouble("lng"));
                            mParkBeans.add(mParkBean);
                        }
                        showParkList();
                    } else {
                        ToastUtil.showShort(mActivity, info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                if (!NetworkUtil.isConnected()) {
                    ToastUtil.showShort(mActivity, R.string.not_net);
                } else {
                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
                }
            }
        });
    }

    private void showParkList() {
        if (mCommonAdapter == null) {
            mCommonAdapter = new RecyclerCommonAdapter<ParkBean>(mActivity, R.layout.item_parking_lot, mParkBeans) {
                @Override
                protected void convert(ViewHolder holder, final ParkBean parkBean, int position) {
                    holder.setText(R.id.tv_activity_park_lot_name, parkBean.getParkName());
                    holder.setText(R.id.tv_activity_park_lot_address, parkBean.getParkAddress());
                    holder.setText(R.id.tv_activity_park_lot_distance, getString(R.string.test_distance));//测试数据
                    holder.getView(R.id.iv_activity_park_lot_map).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ToastUtil.showShort(mActivity, getString(R.string.test_data));//测试数据
                        }
                    });
                    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mParkId = parkBean.getCloudId();
                            Intent intent = new Intent();
                            intent.putExtra("park_id", mParkId);
                            setResult(PARK_ID_OK, intent);
                            finish();
                        }
                    });
                }
            };
            mParkRV.setAdapter(mCommonAdapter);
            mParkRV.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {
            mCommonAdapter.notifyDataSetChanged();
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
        mTitleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showShort(mActivity,"当前选择城市仅限于苏州市");
//                ChooseCityActivity.open(mActivity, current);
            }
        });

        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mDataStatus = STATUS_REFRESH;
                mPage = 1;
                getParkInfo(cityId);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mDataStatus = STATUS_LOAD;
                mPage++;
                getParkInfo(cityId);
            }
        });
    }
}
