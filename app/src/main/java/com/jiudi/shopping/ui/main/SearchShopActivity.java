package com.jiudi.shopping.ui.main;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.idst.nls.internal.utils.Base64Encoder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dengzq.simplerefreshlayout.SimpleRefreshLayout;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.MainGodsBean;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.cart.CartDetailActivity;
import com.jiudi.shopping.util.CommonUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.widget.SimpleBottomView;
import com.jiudi.shopping.widget.SimpleLoadView;
import com.jiudi.shopping.widget.SimpleRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchShopActivity extends BaseActivity {
    private android.widget.ImageView back;
    private android.widget.EditText searchTag;
    private android.widget.ImageView searchPass;
    private android.support.v7.widget.RecyclerView recycler;
    private List<MainGodsBean> mCarChoiceList = new ArrayList<>();
    private RecyclerCommonAdapter<MainGodsBean> mCarBeanAdapter;
    private int page=0;
    private int limit=20;
    private com.dengzq.simplerefreshlayout.SimpleRefreshLayout simpleRefresh;
    private boolean stoploadmore=false;
    InputMethodManager manager;//输入法管理器

    @Override
    protected int getContentViewId() {
        return R.layout.activity_shopsearch;
    }

    @Override
    public void initView() {

        back = (ImageView) findViewById(R.id.back);
        searchTag = (EditText) findViewById(R.id.search_tag);
        searchPass = (ImageView) findViewById(R.id.search_pass);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        simpleRefresh = (SimpleRefreshLayout) findViewById(R.id.simple_refresh);
    }

    @Override
    public void initData() {

        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        getList("移动");
        simpleRefresh.setScrollEnable(true);
        simpleRefresh.setPullUpEnable(true);
        simpleRefresh.setPullDownEnable(true);
        simpleRefresh.setHeaderView(new SimpleRefreshView(mActivity));
        simpleRefresh.setFooterView(new SimpleLoadView(mActivity));
        simpleRefresh.setBottomView(new SimpleBottomView(mActivity));
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
                getList();
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
                    getList();
                }

            }
        });
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
                        if(jsonArray.length()>0){
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
                                mCarChoiceList.add(bean);
                            }
                            showCarChoiceRecycleView();
                        }else{
                            Toast.makeText(mActivity,"没有更多商品",Toast.LENGTH_SHORT).show();
                            stoploadmore=true;
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
}
