package com.jiudi.shopping.ui.main;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dengzq.simplerefreshlayout.SimpleRefreshLayout;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.BannerBean;
import com.jiudi.shopping.bean.MainGodsBean;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.cart.CartDetailActivity;
import com.jiudi.shopping.ui.fenxiao.FenXiaoMenuActivity;
import com.jiudi.shopping.ui.fenxiao.FenXiaoNoActivity;
import com.jiudi.shopping.ui.fenxiao.TuanDuiActivity;
import com.jiudi.shopping.ui.fenxiao.TuiGuangActivity;
import com.jiudi.shopping.ui.user.account.TongZhiActivity;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.DisplayUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.widget.BannerLayout;
import com.jiudi.shopping.widget.DividerItemDecoration;
import com.jiudi.shopping.widget.GlideImageLoader;
import com.jiudi.shopping.widget.SimpleBottomView;
import com.jiudi.shopping.widget.SimpleLoadView;
import com.jiudi.shopping.widget.SimpleRefreshView;


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
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "HomeFragment";
    private List<String> mBannerUrlList = new ArrayList<>();
    private List<BannerBean> mBannerList = new ArrayList<>();
    private List<MainGodsBean> mCarChoiceList = new ArrayList<>();
    private int mDataStatus = STATUS_REFRESH;
    private static final int STATUS_REFRESH = 1;
    private static final int STATUS_LOAD = 2;
    private int mPage = 1;
    private RecyclerCommonAdapter<MainGodsBean> mCarBeanAdapter;
    private android.widget.TextView changeCity;
    /**
     * 声明AMapLocationClientOption对象
     */
    public AMapLocationClientOption mLocationOption = null;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1001;
    private AMapLocationClient mLocationClient = null;
    /**
     * 是否拿到当前定位信息
     */
    private boolean mLocationSuccessFlag;
    private LinearLayout center1;
    private LinearLayout center2;
    private LinearLayout center3;
    private LinearLayout center4;
    private RecyclerView recycler;
    private BannerLayout blFragmentHome;
    private int page=0;
    private NestedScrollView nest;
    private boolean stoploadmore=false;
    private double oldTime=0;
    private com.jiudi.shopping.widget.SimpleLoadView loadmoreview;
    private int limit=20;
    private com.dengzq.simplerefreshlayout.SimpleRefreshLayout simpleRefresh;


    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 声明定位回调监听器
     */

    @Override
    public void initView() {

        center1 = (LinearLayout) findViewById(R.id.center_1);
        center2 = (LinearLayout) findViewById(R.id.center_2);
        center3 = (LinearLayout) findViewById(R.id.center_3);
        center4 = (LinearLayout) findViewById(R.id.center_4);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        blFragmentHome = (BannerLayout) findViewById(R.id.bl_fragment_home);
        nest = (NestedScrollView) findViewById(R.id.nest);
        simpleRefresh = (SimpleRefreshLayout) findViewById(R.id.simple_refresh);
    }

    @Override
    public void initData() {
        getHomeBanner();
        getGods();
        simpleRefresh.setScrollEnable(true);
        simpleRefresh.setPullUpEnable(true);
        simpleRefresh.setPullDownEnable(true);
        simpleRefresh.setHeaderView(new SimpleRefreshView(mActivity));
        simpleRefresh.setFooterView(new SimpleLoadView(mActivity));
        simpleRefresh.setBottomView(new SimpleBottomView(mActivity));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initEvent() {

        simpleRefresh.setOnSimpleRefreshListener(new SimpleRefreshLayout.OnSimpleRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        simpleRefresh.onRefreshComplete();
                        simpleRefresh.onLoadMoreComplete();
                    }
                },500);
                mCarChoiceList.clear();
                page=0;
                getGodsList();
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        simpleRefresh.onRefreshComplete();
                        simpleRefresh.onLoadMoreComplete();
                    }
                },500);
                if(stoploadmore){

                    Toast.makeText(mActivity,"没有更多",Toast.LENGTH_SHORT).show();
                }else{
                    page=page+limit;
                    getGodsList();
                }

            }
        });


        center1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if("1".equals(AccountManager.sUserBean.is_promoter)){
                    startActivity(new Intent(mActivity, FenXiaoMenuActivity.class));
                    }else{
                        startActivity(new Intent(mActivity, FenXiaoNoActivity.class));
                    }
                } catch (Exception e) {
                    Toast.makeText(mActivity,"请登录",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        center2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if("1".equals(AccountManager.sUserBean.is_promoter)){
                        startActivity(new Intent(mActivity, TuanDuiActivity.class));
                    }else{
                        startActivity(new Intent(mActivity, FenXiaoNoActivity.class));
                    }
                } catch (Exception e) {
                    Toast.makeText(mActivity,"请登录",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        center3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if("1".equals(AccountManager.sUserBean.is_promoter)){
                        startActivity(new Intent(mActivity, TuiGuangActivity.class));
                    }else{
                        startActivity(new Intent(mActivity, FenXiaoNoActivity.class));
                    }
                } catch (Exception e) {
                    Toast.makeText(mActivity,"请登录",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        center4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mActivity, SearchShopActivity.class));
            }
        });
        nest.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

            }
        });
    }

    private void getGods() {
//        mCarChoiceList.clear();
//        MainGodsBean beanCarChoice = new MainGodsBean();
//        mCarChoiceList.add(beanCarChoice);
//        showCarChoiceRecycleView();
        getGodsList();
    }


    private void getGodsList() {
        Map<String, String> map = new HashMap<>();
        map.put("first", page + "");
        map.put("limit", limit+"");
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).getGodsList(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {


//                simpleRefresh.onRefreshComplete();
//                simpleRefresh.onLoadMoreComplete();
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        JSONArray jsonArray = res.getJSONArray("data");
                        if(jsonArray.length()>1){
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                MainGodsBean bean = new MainGodsBean();
                                bean.id = jsonObject.optString("id");
                                bean.image = jsonObject.optString("image");
                                bean.store_name = jsonObject.optString("store_name");
                                bean.keyword = jsonObject.optString("keyword");
                                bean.sales = jsonObject.optString("sales");
                                bean.vip_price = jsonObject.optString("vip_price");
                                bean.price = jsonObject.optString("price");
                                bean.unit_name = jsonObject.optString("unit_name");
                                mCarChoiceList.add(bean);
                            }

                            showCarChoiceRecycleView();
                        }else{
                            stoploadmore=true;
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {

//                simpleRefresh.onRefreshComplete();
//                simpleRefresh.onLoadMoreComplete();
            }
        });
    }

    /**
     * 车主精选
     */
    private void showCarChoiceRecycleView() {
        if (mCarBeanAdapter == null) {


            mCarBeanAdapter = new RecyclerCommonAdapter<MainGodsBean>(mActivity, R.layout.item_carchoice, mCarChoiceList) {

                @Override
                protected void convert(ViewHolder holder, final MainGodsBean carChoiceBean, int position) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(mActivity, CartDetailActivity.class).putExtra("id",carChoiceBean.id));
                        }
                    });
                    holder.setText(R.id.title,carChoiceBean.store_name);
                    holder.setText(R.id.second_title,carChoiceBean.keyword);
                    holder.setText(R.id.show_price,"¥"+("1".equals(AccountManager.sUserBean.is_promoter)?carChoiceBean.vip_price:carChoiceBean.price));
                    RequestOptions options = new RequestOptions()
                            .fitCenter()
                            .placeholder(R.drawable.tmp_gods)
                            .error(R.drawable.tmp_gods)
                            .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
                    Glide.with(mActivity).load(carChoiceBean.image).apply(options).into((ImageView) holder.getView(R.id.picture));
//                    AccountManager.setBestGood(carChoiceBean.id);
//                    new GlideImageLoader().displayImage(mActivity,carChoiceBean.picture, (ImageView) holder.getView(R.id.picture));
                }

            };


//            recycler.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, (int) DisplayUtil.dpToPx(mActivity, 1), ContextCompat.getColor(mActivity, R.color.colorLine), false, 2));
            recycler.setAdapter(mCarBeanAdapter);
            recycler.setLayoutManager(new GridLayoutManager(mActivity, 2));
        } else {

            mCarBeanAdapter.notifyDataSetChanged();
        }

    }

    private void getHomeBanner() {
        Map<String, String> map = new HashMap<>();
//        if (AccountManager.sUserBean != null) {
////            map.put("customer_id", AccountManager.sUserBean.getId());
//        }
//        map.put("pid", "2");
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).getFlash(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e(TAG, response);
//                switch (mDataStatus) {
//                    case STATUS_REFRESH:
//                        break;
//                }
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    JSONArray data = res.getJSONObject("data").getJSONArray("banner");
                    mBannerList.clear();
                    if (code == 200) {
                        if (mDataStatus == STATUS_REFRESH) {
                            mBannerList.clear();
                        }
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);
                            BannerBean bean = new BannerBean();
                            bean.id=jsonObject.optString("id");
                            bean.title=jsonObject.optString("title");
                            bean.url=jsonObject.optString("url");
                            bean.pic=jsonObject.optString("pic");

                            mBannerList.add(bean);
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

    private void showBanner() {
        mBannerUrlList.clear();
        for (int i = 0; i < mBannerList.size(); i++) {
            mBannerUrlList.add(mBannerList.get(i).pic);
        }
        if (mBannerUrlList.size() > 0) {
            blFragmentHome.setViewUrls(mBannerUrlList);
            blFragmentHome.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    final BannerBean bannerBean = mBannerList.get(position);
                    startActivity(new Intent(mActivity, CartDetailActivity.class).putExtra("id",bannerBean.url.replace("/wap/store/detail/id/","")));
                }
            });
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
