package com.jiudi.shopping.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.BannerBean;
import com.jiudi.shopping.bean.CarBean;
import com.jiudi.shopping.bean.CarOrderBean;
import com.jiudi.shopping.bean.LocEvent;
import com.jiudi.shopping.constant.HomepageConstant;
import com.jiudi.shopping.event.UpdateLoginStateEvent;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.DisplayUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.widget.BannerLayout;
import com.jiudi.shopping.widget.DividerItemDecoration;

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

/**
 * 主页
 */
public class PackFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "PackFragment";

    private BannerLayout mBannerLayout;
    private List<String> mBannerUrlList = new ArrayList<>();
    private List<BannerBean> mBannerList = new ArrayList<>();

    private TextView mCurrentPositionTV;
    private LinearLayout mNullParkOrderLL;
    private TextView mNullParkOrderTV;

    private TwinklingRefreshLayout mRefreshLayout;
    private int mDataStatus = STATUS_REFRESH;
    private static final int STATUS_REFRESH = 1;
    private static final int STATUS_LOAD = 2;
    private int mPage = 1;

    private RecyclerView mLotteryShoppingRV;
    private RecyclerCommonAdapter<CarBean> mCarBeanAdapter;
    private List<CarBean> mOrderList = new ArrayList<>();
    private List<CarOrderBean> mCarOrderBeans1 = new ArrayList<>();
    private List<CarOrderBean> mCarOrderBeans2 = new ArrayList<>();
    private CarBean mCarBean = new CarBean();
    private List<CarOrderBean> mOrderBeans = new ArrayList<>();
    //标识记录是否存在数据
    private String order = null;
    private boolean mOrder1Flg = false;
    private boolean mOrder2Flg = false;
    private String mPrice = null;
    private String mUnid = null;
    private String mStatus = null;

    private PopupWindow mTitlePopwindow;

    //停车消费历史状态 3为赊账出场标识 4为已完成标识
    private String mOrder2Status = null;
    private static final String mUnpaidStatus = "3";
    private static final String mCompleteStatus = "4";

    private String mParkId;
    public static final String PARK_ID_KEY = "key";
    private String mCityId;

    /**
     * 是否拿到当前定位信息
     */
    private boolean mLocationSuccessFlag;

    /**
     * 声明AMapLocationClientOption对象
     */
    public AMapLocationClientOption mLocationOption = null;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1001;
    private AMapLocationClient mLocationClient = null;

    /**
     * 声明定位回调监听器
     */
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {

            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容
                    DialogUtil.showUnCancelableProgress(mActivity, getString(R.string.location_setting));

                    HomepageConstant.mLongitude = aMapLocation.getLongitude();
                    HomepageConstant.mLatitude = aMapLocation.getLatitude();
                    Log.e(TAG, "aMapLocation" + HomepageConstant.mLatitude);
                    String street = aMapLocation.getStreet();
                    Log.e(TAG, "street=" + street);
                    //定位的地址显示
                    Log.e(TAG, aMapLocation.getCity());
                    Log.e(TAG, aMapLocation.getProvince());
                    if(HomepageConstant.mLocationCity==null){
                        HomepageConstant.mLocationCity = aMapLocation.getCity();
                        HomepageConstant.mLocationProvince = aMapLocation.getProvince();
                    }
                    Log.e(TAG, "mCurrentPositionTV=" + mCurrentPositionTV.getText().toString().trim());
                    mLocationSuccessFlag=true;
                    EventBus.getDefault().post(new LocEvent());
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表
                    LogUtil.e(TAG, "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                }
            }
            if (mLocationSuccessFlag){
                DialogUtil.showProgress(mActivity, getString(R.string.message_loading));
                getCityInfo();
                mLocationSuccessFlag=false;
            }
        }
    };


    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_purchasing;
    }

    @Override
    public void initView() {
        mBannerLayout = byId(R.id.bl_fragment_purchasing);

        mCurrentPositionTV = byId(R.id.tv_activity_current_position);
        mCurrentPositionTV.setOnClickListener(this);

        mNullParkOrderLL = byId(R.id.ll_fragment_park_order_null);
        mNullParkOrderTV = byId(R.id.tv_fragment_park_order_null);
        mRefreshLayout = byId(R.id.trl_fragment_purchasing);
        mLotteryShoppingRV = byId(R.id.rv_fragment_purchasing_menu_project);

    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
//        if (AccountManager.sUserBean != null) {
        initGaoDeLocation();
        getHomeBanner();
//        } else {
//            startActivity(new Intent(mActivity, LoginActivity.class));
//        }

    }

    private void getHomeBanner() {
        Map<String, String> map = new HashMap<>();
        if (AccountManager.sUserBean != null) {
            map.put("customer_id", AccountManager.sUserBean.getId());
        }
        map.put("pid", "2");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getFlash(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e(TAG, response);
                switch (mDataStatus) {
                    case STATUS_REFRESH:
                        mRefreshLayout.finishRefreshing();
                        break;
//                    case STATUS_LOAD:
//                        mRefreshLayout.finishLoadmore();
//                        break;
                }
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    JSONArray data = res.getJSONObject("data").getJSONArray("banner");
                    mBannerList.clear();
                    if (code == 0) {
                        if (mDataStatus == STATUS_REFRESH) {
                            mBannerList.clear();
                        }

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject bannerItem = data.getJSONObject(i);
                            BannerBean bannerBean = new BannerBean();
                            bannerBean.setId(bannerItem.getString("id"));//轮播ID
                            bannerBean.setImage(bannerItem.getString("image"));//轮播图片
                            bannerBean.setRemark(bannerItem.getString("remark"));//轮播信息
                            bannerBean.setUrl(bannerItem.getString("linkurl"));//轮播链接

                            mBannerList.add(bannerBean);
                        }
                        showBanner();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                if (!NetworkUtil.isConnected()) {
                    ToastUtil.showShort(mActivity, R.string.net_error);
                } else {
                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
                }
            }
        });
    }

    @Override
    public void initEvent() {
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mDataStatus = STATUS_REFRESH;
                mPage = 1;
//                getHomeBanner();
                getCityInfo();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mDataStatus = STATUS_LOAD;
                mPage = 1;
//                getHomeBanner();
                getCityInfo();
            }
        });
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_current_position:
                Intent intent = new Intent(mActivity, ParkActivity.class);
                if (!TextUtils.isEmpty(HomepageConstant.mLocationCity) && !TextUtils.isEmpty(mCityId)) {
                    intent.putExtra(ParkActivity.CURRENT_CITY, HomepageConstant.mLocationCity);
                    intent.putExtra(ParkActivity.CITY_CODE, mCityId);

                } else {
                    intent.putExtra(ParkActivity.CURRENT_CITY, R.string.location_city_fail);
                    intent.putExtra(ParkActivity.CITY_CODE, "320500");
                }
                startActivityForResult(intent, 1);
                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getExtras() != null) {
            if (resultCode == ParkActivity.PARK_ID_OK && requestCode == 1) {
                mParkId = data.getStringExtra("park_id");
                EventBus.getDefault().unregister(this);
                initData();//重新加载
            }
        }
    }

    /**
     * 轮播显示/跳转
     */
    private void showBanner() {
        mBannerUrlList.clear();
        for (int i = 0; i < mBannerList.size(); i++) {
            mBannerUrlList.add(mBannerList.get(i).getImage());
        }
        if (mBannerUrlList.size() > 0) {
            mBannerLayout.setViewUrls(mBannerUrlList);
            mBannerLayout.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    final BannerBean bannerBean = mBannerList.get(position);
//                    switch (bannerBean.getType()) {
//                        case BannerBean.TYPE_EXTERNAL:
//                            if (!TextUtils.isEmpty(bannerBean.getContent())) {
//                                CommonUtil.jumpToBrowser(mActivity, bannerBean.getContent());
//                            } else {
//                                ToastUtil.showShort(mActivity, getString(R.string.prompt_no_link_url));
//                            }
//                            break;
//                        case BannerBean.TYPE_INSIDE:
//                            ToastUtil.showShort(mActivity,getString(R.string.jum_other_loading));
//                            break;
//                        case BannerBean.TYPE_PRODUCT:
//                            ToastUtil.showShort(mActivity,getString(R.string.jum_other_loading));
//                            break;
//                    }
                }
            });
        }

    }

    /**
     * 获取城市字典信息
     */
    private void getCityInfo() {
        Map<String, String> map = new HashMap<>();
        if (AccountManager.sUserBean != null) {
            map.put("customer_id", AccountManager.sUserBean.getId());
            RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getChooseCity(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtil.e(TAG, response);
                    try {
                        JSONObject res = new JSONObject(response);
                        int code = res.getInt("code");
                        String info = res.getString("info");
                        if (code == 0) {
                            JSONArray data = res.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject dataItem = data.getJSONObject(i);
                                dataItem.getString("id");
                                if (!TextUtils.isEmpty(HomepageConstant.mLocationProvince) && TextUtils.equals(HomepageConstant.mLocationProvince, dataItem.getString("name"))) {
                                    if (dataItem.has("citys")) {
                                        JSONArray cityArray = dataItem.getJSONArray("citys");
                                        for (int j = 0; j < cityArray.length(); j++) {
                                            JSONObject citysItem = cityArray.getJSONObject(j);
                                            if (!TextUtils.isEmpty(HomepageConstant.mLocationCity) && TextUtils.equals(HomepageConstant.mLocationCity, citysItem.getString("name"))) {
                                                mCityId = citysItem.getString("id");
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            getParkInfo();
                        } else {
                            ToastUtil.showShort(mActivity, info);
                            mNullParkOrderLL.setVisibility(View.VISIBLE);
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

        DialogUtil.hideProgress();
    }


    /**
     * 停车场信息
     */
    private void getParkInfo() {
        Map<String, String> map = new HashMap<>();
        if (AccountManager.sUserBean != null) {
            map.put("customer_id", AccountManager.sUserBean.getId());
        }
        if (TextUtils.isEmpty(mCityId)) {
            map.put("city", mCityId);
        } else {
            map.put("city", "320500");//默认当前为苏州城市ID
        }
        LogUtil.e(TAG,map.toString());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getParkList(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e(TAG, response);
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        LogUtil.e(TAG, "getParkInfo=" + "0");
                        JSONArray data = res.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject dataItem = data.getJSONObject(i);
                            mParkId = dataItem.getString("park_id");
                            mCurrentPositionTV.setText(dataItem.getString("title"));//停车场位置显示
                        }
                        getHomeData();
                    } else {
                        LogUtil.e(TAG, "getParkInfo=" + "!=0");
                        ToastUtil.showShort(mActivity, info);
                        mCurrentPositionTV.setText(getString(R.string.stop_car_place, "请重新选择"));//停车场位置显示
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                DialogUtil.hideProgress();
                if (!NetworkUtil.isConnected()) {
                    ToastUtil.showShort(mActivity, R.string.not_net);
                } else {
                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
                }
            }
        });
    }

    /**
     * 首页数据
     */
    private void getHomeData() {
        Map<String, String> map = new HashMap<>();
        if (AccountManager.sUserBean != null) {
            map.put("customer_id", AccountManager.sUserBean.getId());
        }
        if (mParkId != null) {
            map.put("park_id", mParkId);//云停车场id
        }
        SPUtil.put(PARK_ID_KEY, mParkId);//云停车场id存入SP，全局使用
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getHome(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e(TAG, response);
                DialogUtil.hideProgress();
                switch (mDataStatus) {
                    case STATUS_REFRESH:
                        mRefreshLayout.finishRefreshing();
                        break;
//                    case STATUS_LOAD:
//                        mRefreshLayout.finishLoadmore();
//                        break;
                }
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        if (mDataStatus == STATUS_REFRESH) {
                            mOrderList.clear();
                            mCarOrderBeans1.clear();
                            mCarOrderBeans2.clear();
                        }
                        String data = res.getString("data");
                        JSONObject dataItem = new JSONObject(data);

                        String order1 = dataItem.getString("order1");
                        String order2 = dataItem.getString("order2");
                        CarOrderBean orderBeanNull = new CarOrderBean();
                        //当前的order都可能为空
                        if (order1.length() > 2) {
                            mOrder1Flg = true;
                            try {
                                JSONObject orderItem = new JSONObject(order1);
                                CarOrderBean orderBean = new CarOrderBean();
                                orderBean.setId(orderItem.getString("id"));
                                orderBean.setPlate(orderItem.getString("plate"));
                                orderBean.setParkName(orderItem.getString("park_name"));
                                orderBean.setStatus(orderItem.getString("status"));
                                // status：0入场待付1预付待出2赊账待出 3赊账出场4已完成
                                orderBean.setUnid(orderItem.getString("unid"));
                                orderBean.setDerateDuration(orderItem.getString("derate_duration"));
                                orderBean.setPaidFee(orderItem.getString("paid_fee"));
                                orderBean.setTotalFee(orderItem.getString("total_fee"));
                                orderBean.setTicketFee(orderItem.getString("ticket_fee"));
                                orderBean.setPrice(orderItem.getString("price"));
                                orderBean.setEntryTime(orderItem.getString("entry_time"));
                                orderBean.setDuration(orderItem.getString("duration"));
                                mCarOrderBeans1.add(orderBean);
                                mCarBean.setOrder1(mCarOrderBeans1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else if (order2.length() > 2) {
                            mOrder2Flg = true;
                            try {
                                JSONObject orderItem = new JSONObject(order2);
                                CarOrderBean orderBean = new CarOrderBean();
                                orderBean.setId(orderItem.getString("id"));
                                orderBean.setPlate(orderItem.getString("plate"));
                                orderBean.setParkName(orderItem.getString("park_name"));
                                orderBean.setStatus(orderItem.getString("status"));
                                // status：0入场待付1预付待出2赊账待出 3赊账出场4已完成
                                orderBean.setUnid(orderItem.getString("unid"));
                                orderBean.setDerateDuration(orderItem.getString("derate_duration"));
                                orderBean.setPaidFee(orderItem.getString("paid_fee"));
                                orderBean.setTotalFee(orderItem.getString("total_fee"));
                                orderBean.setTicketFee(orderItem.getString("ticket_fee"));
                                orderBean.setPrice(orderItem.getString("price"));
                                orderBean.setEntryTime(orderItem.getString("entry_time"));
                                orderBean.setDuration(orderItem.getString("duration"));
                                mCarOrderBeans2.add(orderBean);
                                mCarBean.setOrder2(mCarOrderBeans2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            mNullParkOrderLL.setVisibility(View.VISIBLE);
                        }
                        mOrderList.add(mCarBean);
                        LogUtil.e(TAG, mOrderList.toString());

                    } else {
                        ToastUtil.showShort(mActivity, info);
                    }
                    if (mOrderList.size() > 0) {
                        if (mCarOrderBeans1.size() == 0 && mCarOrderBeans2.size() == 0) {
                            mNullParkOrderLL.setVisibility(View.VISIBLE);
                        } else {
                            mNullParkOrderLL.setVisibility(View.GONE);
                        }
                        showParkOrderRecycleView();
                    } else {
                        ToastUtil.showShort(mActivity, getString(R.string.have_no_data));
                        mNullParkOrderLL.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(mActivity, getString(R.string.json_error));
                    LogUtil.e(TAG, e.getMessage());
                }

                mRefreshLayout.finishLoadmore();
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


    /**
     * 页停车纪录信息列表
     */
    private void showParkOrderRecycleView() {

        if (mCarBeanAdapter == null) {
            mCarBeanAdapter = new RecyclerCommonAdapter<CarBean>(mActivity, R.layout.item_parking_wait_pay, mOrderList) {
                @Override
                protected void convert(final ViewHolder holder, final CarBean carBean, final int position) {
                    LinearLayout mNowParkLL = holder.getView(R.id.ll_item_parking_wait_pay_content);
                    //设置上一次停车信息
                    LinearLayout mHistoryLL = holder.getView(R.id.ll_item_parking_wait_pay_second);

                    mNowParkLL.setVisibility(View.GONE);
                    mHistoryLL.setVisibility(View.GONE);
                    if (mOrder1Flg) {//当前停车数据
                        mNowParkLL.setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_item_parking_wait_pay_price, carBean.getOrder1().get(position).getPrice());
                        holder.setText(R.id.tv_item_parking_record_name, carBean.getOrder1().get(position).getParkName());
                        holder.setText(R.id.tv_item_parking_record_num, carBean.getOrder1().get(position).getPlate());
                        holder.setText(R.id.tv_item_parking_record_time, carBean.getOrder1().get(position).getEntryTime());
                        holder.setText(R.id.tv_item_parking_record_status, getString(R.string.tv_item_parking_pay, carBean.getOrder1().get(position).getPrice()));
                        holder.setText(R.id.tv_item_parking_wait_pay_price_three, carBean.getOrder1().get(position).getTicketFee());
                        holder.setText(R.id.tv_item_parking_record_price, carBean.getOrder1().get(position).getPaidFee());
                    } else if (mOrder2Flg) {//上一次停车
                        mHistoryLL.setVisibility(View.VISIBLE);
                        setLastParkStatus(holder, position, carBean);
                        holder.setText(R.id.tv_item_parking_wait_pay_second_num, carBean.getOrder2().get(position).getPlate());
                        holder.setText(R.id.tv_item_parking_wait_pay_second_name, carBean.getOrder2().get(position).getParkName());
                        holder.setText(R.id.tv_item_parking_pay_second_stop_time, carBean.getOrder2().get(position).getDuration());
                        holder.setText(R.id.tv_item_parking_wait_pay_second_time, carBean.getOrder2().get(position).getEntryTime());
                        holder.setText(R.id.tv_item_parking_wait_pay_second_price, getString(R.string.tv_parking_pay_price, carBean.getOrder2().get(position).getPrice()));

                    }
                    mNowParkLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.equals(mOrder2Status, mUnpaidStatus)) {
                                mPrice = carBean.getOrder2().get(position).getPrice();
                                mUnid = carBean.getOrder2().get(position).getUnid();
                                mStatus = carBean.getOrder2().get(position).getStatus();
                            } else {
                                mPrice = carBean.getOrder1().get(position).getPrice();
                                mUnid = carBean.getOrder1().get(position).getUnid();
                                mStatus = carBean.getOrder1().get(position).getStatus();
                            }
                            showPayPopWindow(mPrice, mUnid, mStatus);
                        }
                    });
                    mHistoryLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.equals(mOrder2Status, mUnpaidStatus)) {
                                mPrice = carBean.getOrder2().get(position).getPrice();
                                mUnid = carBean.getOrder2().get(position).getUnid();
                                mStatus = carBean.getOrder2().get(position).getStatus();
                                showPayPopWindow(mPrice, mUnid, mStatus);
                            } else {

                            }

                        }
                    });

                }

                //上次停车状态
                private void setLastParkStatus(final ViewHolder holder, final int position, final CarBean carBean) {
                    TextView statusTV = holder.getView(R.id.tv_item_parking_wait_pay_status);
                    ImageView flgIV = holder.getView(R.id.tv_item_parking_wait_pay_icon);
                    TextView flgTV = holder.getView(R.id.tv_item_parking_wait_pay_text);
                    String flg = carBean.getOrder2().get(position).getStatus();
                    switch (flg) {//状态 0入场待付1预付待出2赊账待出场3赊账出场4已完成
                        case mUnpaidStatus:
                            statusTV.setVisibility(View.VISIBLE);
                            statusTV.setText(getString(R.string.no_pay));
                            flgTV.setText(getString(R.string.now_pay));
                            flgIV.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.no_pay));
                            mOrder2Status = mUnpaidStatus;
                            break;
                        case mCompleteStatus:
                            statusTV.setVisibility(View.GONE);
                            flgTV.setText(getString(R.string.completed));
                            flgIV.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.record_gou));
                            mOrder2Status = mCompleteStatus;
                            break;

                    }
                }
            };

            mLotteryShoppingRV.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, (int) DisplayUtil.dpToPx(mActivity, 1), ContextCompat.getColor(mActivity, R.color.colorLine), false, 2));

            mLotteryShoppingRV.setAdapter(mCarBeanAdapter);
            mLotteryShoppingRV.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {
            mCarBeanAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 支付提醒弹窗
     *
     * @param payfor
     * @param unid
     * @param flg
     */
    private void showPayPopWindow(final String payfor, final String unid, final String flg) {
        if (mTitlePopwindow != null && mTitlePopwindow.isShowing()) {
            mTitlePopwindow.dismiss();
        } else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.popwindow_confirm_pay, null, false);

            TextView payCancel = (TextView) view.findViewById(R.id.tv_popwindow_pay_cancel);
            TextView payConfirm = (TextView) view.findViewById(R.id.tv_popwindow_pay_now_pay);
            TextView lastUnpaid = view.findViewById(R.id.tv_popwindow_pay_last_unpaid);
            if (TextUtils.equals(flg, mUnpaidStatus)) {
                lastUnpaid.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(flg, mCompleteStatus)) {
                ToastUtil.showLong(mActivity, getString(R.string.completed));
            } else {
                lastUnpaid.setVisibility(View.GONE);
            }

            payCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.showLong(mActivity, getString(R.string.cancel));
                    mTitlePopwindow.dismiss();
                }
            });

            payConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PayActivity.open(mActivity, payfor, unid);
                    mTitlePopwindow.dismiss();
                }
            });
            mTitlePopwindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

            mTitlePopwindow.setBackgroundDrawable(new BitmapDrawable());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTitlePopwindow != null && mTitlePopwindow.isShowing()) {
                        mTitlePopwindow.dismiss();
                    }
                }
            });
            mTitlePopwindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
        }
    }


    /**
     * 初始化高德定位
     */
    private void initGaoDeLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) { //用户已拒绝过一次
                    //提示用户如果想要正常使用，要手动去设置中授权。
                    ToastUtil.showShort(mActivity, getString(R.string.prompt_open_read_write_permission));
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE_LOCATION);
                }
            } else if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) { //用户已拒绝过一次
                    //提示用户如果想要正常使用，要手动去设置中授权。
                    ToastUtil.showShort(mActivity, getString(R.string.prompt_open_read_write_permission));
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE_LOCATION);
                }
            } else if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_PHONE_STATE)) { //用户已拒绝过一次
                    //提示用户如果想要正常使用，要手动去设置中授权。
                    ToastUtil.showShort(mActivity, getString(R.string.prompt_open_read_write_permission));
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE_LOCATION);
                }
            } else {
                DialogUtil.showUnCancelableProgress(mActivity, getString(R.string.location_setting));
                gaoDeLocation();
            }
        } else {
            DialogUtil.showUnCancelableProgress(mActivity, getString(R.string.location_setting));
            gaoDeLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.showShort(mActivity, "请在到设置-应用管理中开启此应用的储存授权");
                    return;
                }

                if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.showShort(mActivity, "请在到设置-应用管理中开启此应用的定位权限");
                    return;
                }

                if (grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.showShort(mActivity, "请在到设置-应用管理中开启此应用的获取电话权限");
                    return;
                }

                gaoDeLocation();

                break;
//            case REQUEST_CODE_QRCODE_PERMISSION:
//                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    ToastUtil.showShort(mActivity, getString(R.string.open_the_camera_permissions_in_the_settings));
//                    return;
//                }
//                if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
//                    ToastUtil.showShort(mActivity, getString(R.string.prompt_open_read_write_permission));
//                    return;
//                }
//                startActivity(new Intent(mActivity, ScanningActivity.class));
//                break;
//        }
        }
    }

    /**
     * 定位
     */
    private void gaoDeLocation() {

        DialogUtil.showUnCancelableProgress(mActivity, getString(R.string.location_setting));

        //初始化定位
        mLocationClient = new AMapLocationClient(mActivity.getApplicationContext());

        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //获取一次定位结果：
        mLocationOption.setOnceLocation(true);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        //启动定位
        mLocationClient.startLocation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateLoginStateEvent(UpdateLoginStateEvent event) {
        initGaoDeLocation();
//        getHomeBanner();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
