package com.jiudi.shopping.ui.main;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.BannerBean;
import com.jiudi.shopping.bean.CarChoiceBean;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.DisplayUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.widget.BannerLayout;
import com.jiudi.shopping.widget.DividerItemDecoration;


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
    private List<CarChoiceBean> mCarChoiceList = new ArrayList<>();
    private int mDataStatus = STATUS_REFRESH;
    private static final int STATUS_REFRESH = 1;
    private static final int STATUS_LOAD = 2;
    private int mPage = 1;
    private RecyclerCommonAdapter<CarChoiceBean> mCarBeanAdapter;
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
    }
    @Override
    public void initData() {
        getHomeBanner();
        getChoice();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initEvent() {

    }
    private void getChoice() {
        mCarChoiceList.clear();
        CarChoiceBean beanCarChoice = new CarChoiceBean();
        mCarChoiceList.add(beanCarChoice);
        showCarChoiceRecycleView();
    }
    /**
     * 车主精选
     */
    private void showCarChoiceRecycleView() {
        if (mCarBeanAdapter == null) {


            mCarBeanAdapter = new RecyclerCommonAdapter<CarChoiceBean>(mActivity, R.layout.item_carchoice, mCarChoiceList) {

                @Override
                protected void convert(ViewHolder holder, final CarChoiceBean carChoiceBean, int position) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(mActivity, CartDetailActivity.class).putExtra("id",carChoiceBean.id));
                        }
                    });

//                    new GlideImageLoader().displayImage(mActivity,carChoiceBean.picture, (ImageView) holder.getView(R.id.picture));
//                    new GlideImageLoader().displayImage(mActivity,carChoiceBean.picture, (ImageView) holder.getView(R.id.picture));
                }

            };




            recycler.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, (int) DisplayUtil.dpToPx(mActivity, 1), ContextCompat.getColor(mActivity, R.color.colorLine), false, 2));
            recycler.setAdapter(mCarBeanAdapter);
            recycler.setLayoutManager(new GridLayoutManager(mActivity,2));
        }else{

            mCarBeanAdapter.notifyDataSetChanged();
        }

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
                        break;

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
    private void showBanner() {
        mBannerUrlList.clear();
        for (int i = 0; i < mBannerList.size(); i++) {
            mBannerUrlList.add(mBannerList.get(i).getImage());
        }
        if (mBannerUrlList.size() > 0) {
            blFragmentHome.setViewUrls(mBannerUrlList);
            blFragmentHome.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    final BannerBean bannerBean = mBannerList.get(position);
                }
            });
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
