package com.jiudi.shopping.ui.main;

import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.dengzq.simplerefreshlayout.SimpleRefreshLayout;
import com.github.nukc.LoadMoreWrapper.LoadMoreAdapter;
import com.github.nukc.LoadMoreWrapper.LoadMoreWrapper;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.vl.VBGifAdapter;
import com.jiudi.shopping.adapter.vl.VHot2Adapter;
import com.jiudi.shopping.adapter.vl.VHotGridAdapter;
import com.jiudi.shopping.adapter.vl.VHotHead2Adapter;
import com.jiudi.shopping.adapter.vl.VHotHeadAdapter;
import com.jiudi.shopping.adapter.vl.VHotSingle2Adapter;
import com.jiudi.shopping.adapter.vl.VHotTabAdapter;
import com.jiudi.shopping.adapter.vl.VLBannerAdapter;
import com.jiudi.shopping.adapter.vl.VQuiltyHeadAdapter;
import com.jiudi.shopping.adapter.vl.VRecommendAdapter;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.BannerBean;
import com.jiudi.shopping.bean.MainGodsBean;
import com.jiudi.shopping.bean.RecommendBean;
import com.jiudi.shopping.bean.RecommendHotBean;
import com.jiudi.shopping.bean.RecommendImgBean;
import com.jiudi.shopping.bean.RecommendTabBean;
import com.jiudi.shopping.bean.RecommendTitleBean;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 主页
 */
public class HomeCVFragment extends BaseFragment implements View.OnClickListener, LoadMoreAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, VHotTabAdapter.TabSelectedListener {


    private ImageView back;
    private TextView searchTag;
    private ImageView searchPass;
    private SimpleRefreshLayout simpleRefresh;
    private NestedScrollView nest;
    private RecyclerView recycler;
    private VirtualLayoutManager manager;
    private DelegateAdapter adapter;
    final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();
    private LoadMoreAdapter mLoadMoreAdapter;

    private RecommendTitleBean mRecommendtitle;


    private List<BannerBean> mBannerList = new ArrayList<>();
    private List<RecommendBean> mRecommendList = new ArrayList<>();
    private List<RecommendTabBean> mRecommendTabList = new ArrayList<>();
    private List<RecommendImgBean> mRecommendImgList = new ArrayList<>();
    private List<MainGodsBean> mHotVlList = new ArrayList<>();
    private List<RecommendHotBean> mHotRecommendList = new ArrayList<>();


    private int page = 0;
    private int limit = 20;
    private boolean stoploadmore = false;
    private android.support.v4.widget.SwipeRefreshLayout swipeRefreshLayout;
    private String ccid;
    private VLBannerAdapter vlBannerAdapter;
    private VQuiltyHeadAdapter vQuiltyHeadAdapter;
    private VRecommendAdapter vRecommendAdapter;
    private VHotHeadAdapter vHotHeadAdapter;
    private VHot2Adapter vHotAdapter;
    private VHotHead2Adapter vHotHead2Adapter;
    private VHotTabAdapter vHotTabAdapter;
    private VHotGridAdapter vHotGridAdapter;
    private VHotSingle2Adapter vHotSingleAdapter;
    private android.widget.LinearLayout searchTagl;
    int nowindex = 0;
    private VBGifAdapter vbGifAdapter;
    private android.support.design.widget.TabLayout mainTab;

    @Override
    public void onClick(View v) {

    }

    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_homebv;
    }

    @Override
    public void initView() {

        back = (ImageView) findViewById(R.id.back);
//        searchTag = (TextView) findViewById(R.id.search_tag);
//        searchPass = (ImageView) findViewById(R.id.search_pass);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        searchTagl = (LinearLayout) findViewById(R.id.search_tagl);
//        mainTab = (TabLayout) findViewById(R.id.main_tab);
    }

    @Override
    public void initData() {
        getHomeBanner();
//        mainTab.removeAllTabs();
//        for (int i = 0; i < titles.length; i++) {
//            //插入tab标签
//            mainTab.addTab(mainTab.newTab().setText(titles[i]));
//        }
    }

    @Override
    public void initEvent() {
        swipeRefreshLayout.setOnRefreshListener(this);
//        searchTag.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                startActivity(new Intent(mActivity, SearchShopBeforeActivity.class));
//                return true;
//            }
//        });
//        searchTagl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mActivity, SearchShopBeforeActivity.class));
//            }
//        });
    }

    public void buildRecycleView(boolean needscroll) {

        if (adapter == null) {
            nowindex = 0;
            manager = new VirtualLayoutManager(mActivity);
            recycler.setLayoutManager(manager);
            adapter = new DelegateAdapter(manager);
            RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
            recycler.setRecycledViewPool(viewPool);
            viewPool.setMaxRecycledViews(0, 10);
            SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
            LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
            GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(5);
            gridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 1;
                }
            });
            gridLayoutHelper.setAutoExpand(false);

            GridLayoutHelper gridLayoutHelper2 = new GridLayoutHelper(2);
            gridLayoutHelper2.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 1;
                }
            });
            gridLayoutHelper2.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 1;
                }
            });

            gridLayoutHelper2.setAutoExpand(false);
            StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper();
            stickyLayoutHelper.setStickyStart(true);

            vlBannerAdapter = new VLBannerAdapter(mActivity, singleLayoutHelper, mBannerList);
            adapters.add(vlBannerAdapter);
            nowindex += vlBannerAdapter.getItemCount();


//            vbGifAdapter = new VBGifAdapter(mActivity, singleLayoutHelper);
//            adapters.add(vbGifAdapter);
//            nowindex += vbGifAdapter.getItemCount();

//            vQuiltyHeadAdapter = new VQuiltyHeadAdapter(mActivity, singleLayoutHelper, mRecommendtitle);
//            adapters.add(vQuiltyHeadAdapter);
//            nowindex += vQuiltyHeadAdapter.getItemCount();
//
            vRecommendAdapter = new VRecommendAdapter(mActivity, gridLayoutHelper, mRecommendList);
            adapters.add(vRecommendAdapter);
            nowindex += vRecommendAdapter.getItemCount();

//            vHotHeadAdapter = new VHotHeadAdapter(mActivity, singleLayoutHelper);
//            adapters.add(vHotHeadAdapter);
//            nowindex += vHotHeadAdapter.getItemCount();

//            vHotAdapter = new VHot2Adapter(mActivity, singleLayoutHelper, mRecommendImgList);
//            adapters.add(vHotAdapter);
//            nowindex += vHotAdapter.getItemCount();

//            vHotHead2Adapter = new VHotHead2Adapter(mActivity, singleLayoutHelper);
//            adapters.add(vHotHead2Adapter);
//            nowindex += vHotHead2Adapter.getItemCount();

//            vHotSingleAdapter = new VHotSingle2Adapter(mActivity, singleLayoutHelper, mHotRecommendList);
//            adapters.add(vHotSingleAdapter);
//            nowindex += vHotSingleAdapter.getItemCount();

//            vHotTabAdapter = new VHotTabAdapter(mActivity, stickyLayoutHelper, mRecommendTabList, this);
//            adapters.add(vHotTabAdapter);
//            nowindex += vHotTabAdapter.getItemCount();

            vHotGridAdapter = new VHotGridAdapter(mActivity, gridLayoutHelper2, mHotVlList);
            adapters.add(vHotGridAdapter);


            adapter.setAdapters(adapters);
            recycler.setAdapter(adapter);
            mLoadMoreAdapter = LoadMoreWrapper.with(adapter)
                    .setLoadMoreEnabled(!stoploadmore)
                    .setListener(this)
                    .into(recycler);
        } else {
            try {
                vHotGridAdapter.notifyDataSetChanged();
                if (needscroll) {
                    manager.scrollToPositionWithOffset(nowindex, 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void getHomeBanner() {
        System.out.println("OOPO");
        Map<String, String> map = new HashMap<>();
        map.put("cId", getArguments().getString("cId"));
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getTypeRecommend(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    JSONObject data = res.getJSONObject("data");
                    mBannerList.clear();
                    if (code == 200) {
                        JSONArray banner = data.getJSONArray("banner");
                        for (int i = 0; i < banner.length(); i++) {
                            JSONObject jsonObject = banner.getJSONObject(i);
                            BannerBean bean = new BannerBean();
                            bean.id = jsonObject.optString("id");
                            bean.title = jsonObject.optString("title");
                            bean.url = jsonObject.optString("image_input");
                            bean.pic = jsonObject.optString("image_input");
                            mBannerList.add(bean);
                        }
                        JSONArray category_recommend = data.getJSONArray("category");
                        for (int i = 0; i < category_recommend.length(); i++) {
                            JSONObject jsonObject = category_recommend.getJSONObject(i);
                            RecommendBean bean = new RecommendBean();
                            bean.name = jsonObject.optString("cate_name");
                            bean.pic = jsonObject.optString("pic");
                            bean.cid = jsonObject.optString("id");
                            mRecommendList.add(bean);
                        }
//                        JSONArray store_product = data.getJSONArray("store_product");
//                        for (int i = 0; i < store_product.length(); i++) {
//                            JSONObject jsonObject = store_product.getJSONObject(i);
//                            RecommendImgBean bean = new RecommendImgBean();
//                            bean.id = jsonObject.optString("id");
//                            bean.title = jsonObject.optString("title");
//                            bean.pic = jsonObject.optString("pic");
//                            bean.url = jsonObject.optString("url");
//                            bean.product_id = jsonObject.optString("product_id");
//                            mRecommendImgList.add(bean);
//                        }

//                        JSONArray category = data.getJSONArray("category");
//                        RecommendTabBean hotbean = new RecommendTabBean();
//                        hotbean.id = "0";
//                        hotbean.cate_name = "推荐";
//                        mRecommendTabList.add(hotbean);
//                        for (int i = 0; i < category.length(); i++) {
//                            JSONObject jsonObject = category.getJSONObject(i);
//                            RecommendTabBean bean = new RecommendTabBean();
//                            bean.id = jsonObject.optString("id");
//                            bean.cate_name = jsonObject.optString("cate_name");
//                            mRecommendTabList.add(bean);
//                        }

//                        JSONArray store_hot = data.getJSONArray("store_hot");
//                        System.out.println(store_hot.toString());
//                        for (int i = 0; i < store_hot.length(); i++) {
//                            JSONObject jsonObject = store_hot.getJSONObject(i);
//                            RecommendHotBean bean = new RecommendHotBean();
//                            bean.id = jsonObject.optString("id");
//                            bean.title = jsonObject.optString("title");
//                            bean.pic = jsonObject.optString("pic");
//                            bean.url = jsonObject.optString("url");
//                            bean.product_id = jsonObject.optString("product_id");
//                            bean.price = jsonObject.optString("price");
//                            bean.vip_price = jsonObject.optString("vip_price");
////                            mHotRecommendList.add(bean);
////                            mHotRecommendList.add(bean);
//                            mHotRecommendList.add(bean);
//                        }

//                        buildRecycleView();
                        ccid = getArguments().getString("cId");
                        getGodsList(false);

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

    private void getGodsList(final boolean needscroll) {
        Map<String, String> map = new HashMap<>();
        map.put("first", page + "");
        map.put("cId", ccid + "");
        map.put("limit", limit + "");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getGodsList(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
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
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                MainGodsBean bean = new MainGodsBean();
                                bean.id = jsonObject.optString("id");
                                bean.image = jsonObject.optString("image");
                                bean.store_name = jsonObject.optString("store_name");
                                bean.keyword = jsonObject.optString("keyword");
                                bean.sales = jsonObject.optInt("sales");
                                bean.stock = jsonObject.optInt("stock");
                                bean.vip_price = jsonObject.optString("vip_price");
                                bean.price = jsonObject.optString("price");
                                bean.unit_name = jsonObject.optString("unit_name");
                                mHotVlList.add(bean);
                            }
                            buildRecycleView(needscroll);
                        } else {
//
                            noMoreData();
                            buildRecycleView(needscroll);
                            stoploadmore = true;
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    @Override
    public void onRefresh() {
        stoploadmore = false;
        page = 0;
        mBannerList.clear();
        mHotVlList.clear();
        mRecommendImgList.clear();
        mRecommendTabList.clear();
        mRecommendList.clear();
        getHomeBanner();
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onLoadMore(LoadMoreAdapter.Enabled enabled) {
        page = page + limit;
        getGodsList(false);
    }

    private boolean resetRefreshing() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
            return true;
        }
        return false;
    }

    public void noMoreData() {
        if (mLoadMoreAdapter != null) {
            mLoadMoreAdapter.setLoadMoreEnabled(false);
            mLoadMoreAdapter.setShowNoMoreEnabled(true);
            mLoadMoreAdapter.getOriginalAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onTabClick(String cid) {
        page = 0;
        stoploadmore = false;
        ccid = cid;
        mHotVlList.clear();
        mLoadMoreAdapter.setLoadMoreEnabled(true);
        System.out.println("ooooo");
        getGodsList(true);

    }
}
