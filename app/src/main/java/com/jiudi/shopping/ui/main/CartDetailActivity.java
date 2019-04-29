package com.jiudi.shopping.ui.main;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.vl.VBannerAdapter;
import com.jiudi.shopping.adapter.vl.VDiscussAdapter;
import com.jiudi.shopping.adapter.vl.VDiscussEndAdapter;
import com.jiudi.shopping.adapter.vl.VDiscussHeadAdapter;
import com.jiudi.shopping.adapter.vl.VIntroduceAdapter;
import com.jiudi.shopping.adapter.vl.VIntroduceHeadAdapter;
import com.jiudi.shopping.adapter.vl.VTitleDetailAdapter;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.BannerBean;
import com.jiudi.shopping.bean.CartDiscussBean;
import com.jiudi.shopping.bean.CartIntroduceBean;
import com.jiudi.shopping.bean.CartTitleBean;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.NetworkUtil;
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
 * 商品详情
 */
public class CartDetailActivity extends BaseActivity {


    private List<BannerBean> mBannerList = new ArrayList<>();

    final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();
    private List<CartDiscussBean> mcartdiscussbeanlist = new ArrayList<>();
    private List<CartIntroduceBean> mcartdintroducebeanlist;
    private RecyclerView recycler;

    private DelegateAdapter adapter;
    private SingleLayoutHelper singHelper;
    private SingleLayoutHelper singHelper2;
    private VBannerAdapter vBannerAdapter;
    private VTitleDetailAdapter vDetailAdapter;
    private VDiscussHeadAdapter vDiscussHeadAdapter;
    private VDiscussEndAdapter vDiscussEndAdapter;
    private VIntroduceHeadAdapter vIntroduceHeadAdapter;


    private VDiscussAdapter vDiscussAdapter;
    private VIntroduceAdapter vIntroduceAdapter;
    private CartTitleBean mcarttitlebean;
    private String[] titles = {"宝贝", "评价", "详情"};
    private int[] poss = {0, 3, 8};
    private LinearLayoutHelper linearLayoutHelper;
    private LinearLayout titleBar;
    private TabLayout mainTab;
    private boolean isScrolled = false;
    private VirtualLayoutManager manager;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_cart_detail;
    }

    @Override
    public void initView() {


        recycler = (RecyclerView) findViewById(R.id.recycler);
        titleBar = (LinearLayout) findViewById(R.id.title_bar);
        mainTab = (TabLayout) findViewById(R.id.main_tab);
        for (int i = 0; i < titles.length; i++) {
            //插入tab标签
            mainTab.addTab(mainTab.newTab().setText(titles[i]));
        }
        //标签页可以滑动
//        mainTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        mainTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if(pos==0){
                    mainTab.setVisibility(View.GONE);
                    titleBar.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                }

                    //滑动时不能点击,
                    //第一个参数是指定的位置，锚点
                    // 第二个参数表示 Item 移动到第一项后跟 RecyclerView 上边界或下边界之间的距离（默认是 0）
                    manager.scrollToPositionWithOffset(poss[pos], 0);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if(pos==0){
                    mainTab.setVisibility(View.GONE);
                    titleBar.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                }

                //滑动时不能点击,
                //第一个参数是指定的位置，锚点
                // 第二个参数表示 Item 移动到第一项后跟 RecyclerView 上边界或下边界之间的距离（默认是 0）
                manager.scrollToPositionWithOffset(poss[pos], 0);
            }
        });

    }

    @Override
    public void initData() {
        manager = new VirtualLayoutManager(this);
        recycler.setLayoutManager(manager);
        adapter = new DelegateAdapter(manager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recycler.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);


        vBannerAdapter = new VBannerAdapter(this, new SingleLayoutHelper(), mBannerList);
        adapters.add(vBannerAdapter);


        vDetailAdapter = new VTitleDetailAdapter(this, new SingleLayoutHelper(), mcarttitlebean);
        adapters.add(vDetailAdapter);


        vDiscussHeadAdapter = new VDiscussHeadAdapter(this, new SingleLayoutHelper());
        adapters.add(vDiscussHeadAdapter);

        vDiscussAdapter = new VDiscussAdapter(this, new LinearLayoutHelper(), mcartdiscussbeanlist);
        adapters.add(vDiscussAdapter);


//        vDiscussEndAdapter=new VDiscussEndAdapter(this, new SingleLayoutHelper());
//        adapter.addAdapter(vDiscussEndAdapter);


        vIntroduceHeadAdapter = new VIntroduceHeadAdapter(this, new SingleLayoutHelper());
        adapters.add(vIntroduceHeadAdapter);

        vIntroduceAdapter = new VIntroduceAdapter(this, new LinearLayoutHelper(), mcartdintroducebeanlist);
        adapters.add(vIntroduceAdapter);


        adapter.setAdapters(adapters);
        recycler.setAdapter(adapter);
        getHomeBanner();
    }

    public int getAllAdapterCount() {
        int result = 0;
        for (int i = 0; i < adapters.size(); i++) {
            result += adapters.get(i).getItemCount();
        }
        return result;
    }

    private void getHomeBanner() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", "42");
        map.put("pid", "2");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getFlash(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    JSONArray data = res.getJSONObject("data").getJSONArray("banner");
                    mBannerList.clear();
                    if (code == 0) {
                        mBannerList.clear();

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject bannerItem = data.getJSONObject(i);
                            BannerBean bannerBean = new BannerBean();
                            bannerBean.setId(bannerItem.getString("id"));//轮播ID
                            bannerBean.setImage(bannerItem.getString("image"));//轮播图片
                            bannerBean.setRemark(bannerItem.getString("remark"));//轮播信息
                            bannerBean.setUrl(bannerItem.getString("linkurl"));//轮播链接
                            mBannerList.add(bannerBean);
                        }

                        adapter.notifyDataSetChanged();

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
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //重写该方法主要是判断recyclerview是否在滑动
                //0停止 ，12都是滑动
                if (newState == 0) {
                    isScrolled = false;
                } else {
                    isScrolled = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //这个主要是recyclerview滑动时让tab定位的方法
                if (isScrolled) {
                    int top = manager.findFirstVisibleItemPosition();
                    int bottom = manager.findLastVisibleItemPosition();
//                    System.out.println("滑动到位" + top + "," + bottom);

                    int pos = 0;
                    for (int i = 0; i < poss.length; i++) {
                        if (top >= poss[i]) {
                            pos = i;
                        }
                    }
                    if (top > 0) {
                        mainTab.setVisibility(View.VISIBLE);
                        titleBar.setBackgroundColor(Color.parseColor("#ffffff"));
                    } else {
                        mainTab.setVisibility(View.GONE);
                        titleBar.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                    }
//                    if (bottom == getAllAdapterCount() - 1) {
//                        //先判断滑到底部，tab定位到最后一个
//                        pos = poss.length - 1;
//                    } else if (top == poss[poss.length - 1]) {
//                        //如果top等于指定的位置，对应到tab即可，
//                        pos = poss[poss.length - 1];
//                    } else {
//                        //循环遍历，需要比较i+1的位置，所以循环长度要减1，
//                        //  如果 i<top<i+1,  那么tab应该定位到i位置的字符，不管是向上还是向下滑动
//                        for (int i = 0; i < poss.length - 1; i++) {
//                            if (top == poss[i]) {
//                                pos = i;
//                                break;
//                            } else if (top > poss[i] && top < poss[i + 1]) {
//                                pos = i;
//                                break;
//                            }
//                        }
//                    }

                    //设置tab滑动到第pos个
                    mainTab.setScrollPosition(pos, 0f, true);
                }

            }
        });

    }

}
