package com.jiudi.shopping.ui.fenxiao;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.github.nukc.LoadMoreWrapper.LoadMoreAdapter;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.vl.VHotGrid3Adapter;
import com.jiudi.shopping.adapter.vl.VMineQuanYiAdapter;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.MainGodsBean;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.SPUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FenXiaoNo2Activity extends BaseActivity implements View.OnClickListener, LoadMoreAdapter.OnLoadMoreListener,SwipeRefreshLayout.OnRefreshListener{


    private ImageView back;
    private android.support.v4.widget.SwipeRefreshLayout swipeRefreshLayout;
    private android.support.v7.widget.RecyclerView recycler;
    private VirtualLayoutManager manager;
    private DelegateAdapter adapter;
    final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();

    private List<MainGodsBean> mHotVlList = new ArrayList<>();
    private VHotGrid3Adapter vHotGridAdapter;
    private VMineQuanYiAdapter vMineQuanYiAdapter;
    private JSONObject jsondata;
    private boolean stoploadmore = false;
    private LoadMoreAdapter mLoadMoreAdapter;
    private int page = 0;
    private int limit = 20;
    private boolean isdianzhu;
    private android.widget.TextView looktitle;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_up;
    }

    @Override
    public void initView() {

        back = (ImageView) findViewById(R.id.back);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        looktitle = (TextView) findViewById(R.id.looktitle);
    }

    @Override
    public void initData() {
        isdianzhu=getIntent().getBooleanExtra("isdianzhu",false);
        if(isdianzhu){
            looktitle.setText("店主权益");
        }else{
            looktitle.setText("升级店主");
        }
//        getGodsList(false);
        getFenXiao();
    }

    @Override
    public void initEvent() {

        swipeRefreshLayout.setOnRefreshListener(this);
    }
    public void buildRecycleView(boolean needscroll) {
        if(adapter==null){
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
            vMineQuanYiAdapter = new VMineQuanYiAdapter(mActivity,singleLayoutHelper,this,jsondata);
            vMineQuanYiAdapter.setIsDianZhu(isdianzhu);
            adapters.add(vMineQuanYiAdapter);

            if(!isdianzhu){
                vHotGridAdapter = new VHotGrid3Adapter(mActivity, gridLayoutHelper2, mHotVlList);
                adapters.add(vHotGridAdapter);
            }

            adapter.setAdapters(adapters);
            recycler.setAdapter(adapter);
//            mLoadMoreAdapter = LoadMoreWrapper.with(adapter)
//                    .setLoadMoreEnabled(!stoploadmore)
//                    .setListener(this)
//                    .into(recycler);
        }else {
//            try {
//                vHotGridAdapter.notifyDataSetChanged();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

    }

    @Override
    public void onRefresh() {
//        stoploadmore=false;
//        page=0;
//        mHotVlList.clear();
//        getGodsList(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore(LoadMoreAdapter.Enabled enabled) {
//        page=page+limit;
//        getGodsList(false);
    }

    @Override
    public void onClick(View v) {

    }
    private void getGodsList(final boolean needscroll) {
        Map<String, String> map = new HashMap<>();
//        map.put("first", page + "");
//        map.put("limit", limit + "");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getLoveGodsList(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {

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


    private void getFenXiao() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getFenXiao(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {

                        if(isdianzhu){
                            JSONObject data=res.getJSONObject("data");
                            jsondata=data;
                        }else{
                            JSONArray data = res.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject = data.getJSONObject(i);
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
                        }
                        buildRecycleView(false);

                    }else {
                        Toast.makeText(mActivity,info,Toast.LENGTH_SHORT).show();
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


    public void noMoreData() {
        if (mLoadMoreAdapter != null) {
            mLoadMoreAdapter.setLoadMoreEnabled(false);
            mLoadMoreAdapter.setShowNoMoreEnabled(true);
            mLoadMoreAdapter.getOriginalAdapter().notifyDataSetChanged();
        }
    }
}
