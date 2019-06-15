package com.jiudi.shopping.ui.main;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.alibaba.idst.nls.internal.utils.Base64Encoder;
import com.dengzq.simplerefreshlayout.SimpleRefreshLayout;
import com.github.nukc.LoadMoreWrapper.LoadMoreAdapter;
import com.github.nukc.LoadMoreWrapper.LoadMoreWrapper;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.vl.VHotGridAdapter;
import com.jiudi.shopping.adapter.vl.VHotTabAdapter;
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

public class SearchShopActivity extends BaseActivity implements View.OnClickListener, LoadMoreAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, VHotTabAdapter.TabSelectedListener{
    private android.widget.ImageView back;
    private android.widget.EditText searchTag;
    private android.widget.ImageView searchPass;
    private List<MainGodsBean> mCarChoiceList = new ArrayList<>();
    private int page=0;
    private int limit=20;
    private com.dengzq.simplerefreshlayout.SimpleRefreshLayout simpleRefresh;
    private boolean stoploadmore=false;
    InputMethodManager immanager;//输入法管理器
    private android.support.v4.widget.SwipeRefreshLayout swipeRefreshLayout;
    private android.support.v7.widget.RecyclerView recycler;

    private VHotGridAdapter vHotGridAdapter;


    private VirtualLayoutManager manager;
    int nowindex = 0;
    private DelegateAdapter adapter;
    final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();
    private LoadMoreAdapter mLoadMoreAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_shopsearch;
    }

    @Override
    public void initView() {

        back = (ImageView) findViewById(R.id.back);
        searchTag = (EditText) findViewById(R.id.search_tag);
        searchPass = (ImageView) findViewById(R.id.search_pass);
        simpleRefresh = (SimpleRefreshLayout) findViewById(R.id.simple_refresh);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    public void initData() {

        immanager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        getList("移动");
        searchTag.setText(getIntent().getStringExtra("keyword"));
        getList();
    }

    @Override
    public void initEvent() {
        searchTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, SearchShopBeforeActivity.class));
                finish();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
//        searchPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if("".equals(searchTag.getText().toString())||"null".equals(searchTag.getText().toString())){
//                    Toast.makeText(mActivity,"请输入搜索条件",Toast.LENGTH_SHORT).show();
//                }else{
//                    if (manager.isActive()) {
//                        manager.hideSoftInputFromWindow(searchTag.getApplicationWindowToken(), 0);
//                    }
//                    page=0;
//                    mCarChoiceList.clear();
//                    getList();
//                }
//            }
//        });
//        searchTag.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    //先隐藏键盘
//                    if (manager.isActive()) {
//                        manager.hideSoftInputFromWindow(searchTag.getApplicationWindowToken(), 0);
//                    }
//                    //自己需要的操作
//                    if("".equals(searchTag.getText().toString())||"null".equals(searchTag.getText().toString())){
//                        Toast.makeText(mActivity,"请输入搜索条件",Toast.LENGTH_SHORT).show();
//                    }else{
//
//                        page=0;
//                        mCarChoiceList.clear();
//                        getList();
//                    }
//                }
//                //记得返回false
//                return false;
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




//            vbGifAdapter = new VBGifAdapter(mActivity, singleLayoutHelper);
//            adapters.add(vbGifAdapter);
//            nowindex += vbGifAdapter.getItemCount();

//            vQuiltyHeadAdapter = new VQuiltyHeadAdapter(mActivity, singleLayoutHelper, mRecommendtitle);
//            adapters.add(vQuiltyHeadAdapter);
//            nowindex += vQuiltyHeadAdapter.getItemCount();
//
//            vRecommendAdapter = new VRecommendAdapter(mActivity, gridLayoutHelper, mRecommendList);
//            adapters.add(vRecommendAdapter);
//            nowindex += vRecommendAdapter.getItemCount();

//            vHotHeadAdapter = new VHotHeadAdapter(mActivity, singleLayoutHelper);
//            adapters.add(vHotHeadAdapter);
//            nowindex += vHotHeadAdapter.getItemCount();


            vHotGridAdapter = new VHotGridAdapter(mActivity, gridLayoutHelper2, mCarChoiceList);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void getList() {
        Map<String, String> map = new HashMap<>();

        try {
            String keyword=getIntent().getStringExtra("keyword");
            String oldhistory=SPUtil.get("seach", "").toString();
            oldhistory=oldhistory.replace(keyword,"");
            oldhistory=oldhistory+keyword+",";
            SPUtil.put("seach",oldhistory);
            map.put("keyword", new String(Base64Encoder.encode(keyword.getBytes("utf-8"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("first", page + "");
        map.put("limit", limit+"");
        if(getIntent().getStringExtra("cId")!=null&&!"".equals(getIntent().getStringExtra("cId"))){
            map.put("cId", getIntent().getStringExtra("cId"));
        }else{
            map.put("cId", "0");
        }
        map.put("sId", "0");
        map.put("priceOrder", "0");
        map.put("salesOrder", "0");
        map.put("news", "0");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).searchShop(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
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
                                bean.sales = jsonObject.optInt("sales")+jsonObject.optInt("ficti");
                                bean.stock = jsonObject.optInt("stock");
                                bean.vip_price = jsonObject.optString("vip_price");
                                bean.price = jsonObject.optString("price");
                                bean.unit_name = jsonObject.optString("unit_name");
                                mCarChoiceList.add(bean);
                            }
                            buildRecycleView(false);
                        } else {
//
                            noMoreData();
                            buildRecycleView(false);
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
    /**
     * 车主精选
     */
//    private void showCarChoiceRecycleView() {
//        if (mCarBeanAdapter == null) {
//
//
//            mCarBeanAdapter = new RecyclerCommonAdapter<MainGodsBean>(mActivity, R.layout.item_carchoice, mCarChoiceList) {
//
//                @Override
//                protected void convert(ViewHolder holder, final MainGodsBean carChoiceBean, int position) {
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            startActivity(new Intent(mActivity, CartDetailActivity.class).putExtra("id",carChoiceBean.id));
//                        }
//                    });
//                    holder.setText(R.id.title,carChoiceBean.store_name);
//                    holder.setText(R.id.second_title,carChoiceBean.keyword);
//                    holder.setText(R.id.show_price,"¥"+("1".equals(AccountManager.sUserBean.is_promoter)?carChoiceBean.vip_price:carChoiceBean.price));
//                    RequestOptions options = new RequestOptions()
//                            .fitCenter()
//                            .placeholder(R.drawable.tmp_gods)
//                            .error(R.drawable.tmp_gods)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
//                    Glide.with(mActivity).load(carChoiceBean.image).apply(options).into((ImageView) holder.getView(R.id.picture));
////                    AccountManager.setBestGood(carChoiceBean.id);
////                    new GlideImageLoader().displayImage(mActivity,carChoiceBean.picture, (ImageView) holder.getView(R.id.picture));
//                }
//
//            };
//
//
////            recycler.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, (int) DisplayUtil.dpToPx(mActivity, 1), ContextCompat.getColor(mActivity, R.color.colorLine), false, 2));
//            recycler.setAdapter(mCarBeanAdapter);
//            recycler.setLayoutManager(new GridLayoutManager(mActivity, 2));
//        } else {
//
//            mCarBeanAdapter.notifyDataSetChanged();
//        }
//
//    }
    @Override
    public void onRefresh() {
        stoploadmore = false;
        page = 0;
        mCarChoiceList.clear();
        getList();
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onLoadMore(LoadMoreAdapter.Enabled enabled) {
        page = page + limit;
        getList();
    }

//    private boolean resetRefreshing() {
//        if (swipeRefreshLayout.isRefreshing()) {
//            swipeRefreshLayout.setRefreshing(false);
//            return true;
//        }
//        return false;
//    }

    public void noMoreData() {
        if (mLoadMoreAdapter != null) {
            mLoadMoreAdapter.setLoadMoreEnabled(false);
            mLoadMoreAdapter.setShowNoMoreEnabled(true);
            mLoadMoreAdapter.getOriginalAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onTabClick(String cid) {

    }
}
