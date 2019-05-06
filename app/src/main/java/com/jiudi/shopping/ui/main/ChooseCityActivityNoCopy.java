package com.jiudi.shopping.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.ParkCityBean;
import com.jiudi.shopping.constant.HomepageConstant;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.widget.LetterListView;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocatedCity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：Constantine on 2018/9/6.
 * 邮箱：2534159288@qq.com
 */
public class ChooseCityActivityNoCopy extends BaseActivity {
    private static final String TAG = "ChooseCityActivity";

    private LinearLayout mBackLL;
    private TextView mTitleTV;

    private RecyclerView mRecyclerView;
    private TwinklingRefreshLayout mRefreshLayout;
    private int mDataStatus = STATUS_REFRESH;
    private static final int STATUS_REFRESH = 1;
    private static final int STATUS_LOAD = 2;
    private int mPage = 1;

    private RecyclerCommonAdapter<ParkCityBean.Data> mCityBeanAdapter;
    private ParkCityBean mParkCityBean;
    private List<ParkCityBean> mBeanList = new ArrayList<>();
    private List<ParkCityBean.Data> mCityBeanList = new ArrayList<>();
    private LetterListView mLetterListView;
    public List<City> list=new ArrayList<>();

    private static final String LOCATION = "place";
    public static final int START_CHOICE = 191;
    private String place;

    @Override
    protected int getContentViewId() {
        return -1;
    }

    @Override
    public void initView() {
//        place = getIntent().getStringExtra(LOCATION);
//        mBackLL = byId(R.id.ll_layout_top_back_bar_back);
//        mTitleTV = byId(R.id.tv_layout_top_back_bar_title);
//
//        mRefreshLayout = byId(R.id.trl_activity_choose_city_list);
//        mRecyclerView = byId(R.id.rv_activity_choose_city_list);
//        StyleUtil.setRefreshStyle(mActivity, mRefreshLayout);
//
//        mLetterListView = byId(R.id.llv_activity_choose_city_list);
    }

    @Override
    public void initData() {
//        mTitleTV.setText(getString(R.string.location_place, place));
//        getPCA();
//        initCity();

        showFragment();


    }
    public void initCity(){
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager3
                .createRequest(RetrofitRequestInterface.class)
                .getAllCityDy(getIntent().getStringExtra("url"), RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            String info = res.getString("info");
                            int code = res.getInt("code");
                            list.clear();
                            if (code == 0) {
                                JSONArray jsonArray=res.getJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String pinyin= null;
                                    try {
                                        pinyin = PinyinHelper.getShortPinyin(jsonObject.optString("city"));
                                    } catch (PinyinException e) {
                                        e.printStackTrace();
                                    }
                                    City bean=new City(jsonObject.optString("city"),jsonObject.optString("province"),pinyin,jsonObject.optString("company"));
                                    list.add(bean);
                                }

                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (!NetworkUtil.isConnected()) {
                            ToastUtil.showShort(mActivity, getString(R.string.net_error));
                        } else {
                            ToastUtil.showShort(mActivity, getString(R.string.network_error));
                        }
                    }
                });





    }
    public void showFragment(){
        CityPicker.from(ChooseCityActivityNoCopy.this) //activity或者fragment
                .enableAnimation(false)	//启用动画效果，默认无
                .setLocatedCity(new LocatedCity(getIntent().getStringExtra("now"), HomepageConstant.mLocationProvince, ""))  //APP自身已定位的城市，传null会自动定位（默认）
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
//                        Toast.makeText(getApplicationContext(), data.getName(), Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK,new Intent().putExtra("city",data.getName()).putExtra("province",data.getProvince()));
                        finish();
                    }

                    @Override
                    public void onCancel(){
//                        Toast.makeText(getApplicationContext(), "取消选择", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onLocate() {

                    }
                })
                .show();
    }
    @Override
    public void initEvent() {
//        mBackLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//        mTitleTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getPCA();
//            }
//        });
//        mLetterListView.setOnTouchLetterChangedListener(new LetterListView.OnTouchLetterChangedListener() {
//            @Override
//            public void touchLetterChanged(String s) {
//
//                int position = mCityBeanAdapter.getItemCount();
//                if (position != -1) mRecyclerView.smoothScrollToPosition(position);
//            }
//        });
//
//        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
//                super.onRefresh(refreshLayout);
//                mDataStatus = STATUS_REFRESH;
//                mPage = 1;
////                getProduct();
//            }
//
//            @Override
//            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
//                super.onLoadMore(refreshLayout);
//                mDataStatus = STATUS_LOAD;
//                mPage++;
////                getProduct();
//            }
//        });
    }

    /**
     * 获取城市字典信息
     */
    private void getPCA() {
        Map<String, String> map = new HashMap<>();
//        map.put("customer_id", AccountManager.sUserBean.getId());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getChooseCity(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
//                    switch (mDataStatus) {
//                        case STATUS_REFRESH:
//                            mRefreshLayout.finishRefreshing();
//                            break;
//                        case STATUS_LOAD:
//                            mRefreshLayout.finishLoadmore();
//                            break;
//                    }
                    if (code == 0) {
                        JSONArray data = res.getJSONArray("data");
                        mParkCityBean = new ParkCityBean();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject dataItem = data.getJSONObject(i);
                            ParkCityBean.Data province = new ParkCityBean.Data();
                            province.setId(dataItem.getString("id"));
                            province.setName(dataItem.getString("name"));
                            List<ParkCityBean.Data.Citys> cityList = new ArrayList<ParkCityBean.Data.Citys>();
                            if (dataItem.has("citys")) {
                                JSONArray cityArray = dataItem.getJSONArray("citys");
                                for (int j = 0; j < cityArray.length(); j++) {
                                    JSONObject citysItem = cityArray.getJSONObject(j);
                                    ParkCityBean.Data.Citys city = new ParkCityBean.Data.Citys();
                                    city.setId(citysItem.getString("id"));
                                    city.setName(citysItem.getString("name"));
                                    cityList.add(city);
                                }
                                province.setCitys(cityList);
                            } else {
                                ParkCityBean.Data.Citys citysNull = new ParkCityBean.Data.Citys();
                                citysNull.setId("");
                                citysNull.setName("");
                                cityList.add(citysNull);
                                province.setCitys(cityList);
                            }
                            mCityBeanList.add(province);

                        }
                        mParkCityBean.setData(mCityBeanList);
                        mBeanList.add(mParkCityBean);
                        showCityList();
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


    /**
     * 显示城市列表
     */
    private void showCityList() {
        if (mCityBeanAdapter == null) {
            mCityBeanAdapter = new RecyclerCommonAdapter<ParkCityBean.Data>(mActivity, R.layout.item_select_city, mCityBeanList) {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                protected void convert(final ViewHolder holder, ParkCityBean.Data bean, final int position) {
                    holder.setText(R.id.tv_city_name, mCityBeanList.get(position).getName());
                    holder.setText(R.id.tv_city_code, mCityBeanList.get(position).getId());
//                    if (position > 0) {
//                        try {
//                            String currentCode = PinyinHelper.getShortPinyin(bean.getData().get(position).getName().charAt(0) + "");
//                            String lastCode = PinyinHelper.getShortPinyin(bean.getData().get(position - 1).getName().charAt(0) + "");
//                            LogUtil.e(TAG,"currentCode="+currentCode);
//                            LogUtil.e(TAG,"lastCode="+lastCode);
//                            if (TextUtils.equals(currentCode, lastCode)) {
//                                holder.getView(R.id.tv_city_code).setVisibility(View.GONE);
//                            } else {
//                                holder.getView(R.id.tv_city_code).setVisibility(View.VISIBLE);
//                                holder.setText(R.id.tv_city_code, PinyinHelper.getShortPinyin(currentCode));
//                            }
////                        holder.setText(R.id.tv_city_code, PinyinHelper.getShortPinyin(data.getCitys().get(position).getName().charAt(0) + ""));
//                        } catch (PinyinException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }

            };
            mRecyclerView.setAdapter(mCityBeanAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {
            mCityBeanAdapter.notifyDataSetChanged();
        }
    }

    public static void open(Activity activity, Map<String, String> map) {
        Intent intent = new Intent(activity, PayAllActivity.class);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        activity.startActivityForResult(intent,START_CHOICE);
    }


}
