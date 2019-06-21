package com.jiudi.shopping.ui.main;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
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
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.github.nukc.LoadMoreWrapper.LoadMoreAdapter;
import com.github.nukc.LoadMoreWrapper.LoadMoreWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.vl.VBGifAdapter;
import com.jiudi.shopping.adapter.vl.VHot2Adapter;
import com.jiudi.shopping.adapter.vl.VHotGrid2Adapter;
import com.jiudi.shopping.adapter.vl.VHotHead2Adapter;
import com.jiudi.shopping.adapter.vl.VHotHeadAdapter;
import com.jiudi.shopping.adapter.vl.VHotSingle2Adapter;
import com.jiudi.shopping.adapter.vl.VHotTabAdapter;
import com.jiudi.shopping.adapter.vl.VLBannerAdapter;
import com.jiudi.shopping.adapter.vl.VQuiltyHeadAdapter;
import com.jiudi.shopping.adapter.vl.VRecommendAdapter;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.BannerBean;
import com.jiudi.shopping.bean.Fictitious;
import com.jiudi.shopping.bean.MainGodsBean;
import com.jiudi.shopping.bean.RecommendBean;
import com.jiudi.shopping.bean.RecommendHotBean;
import com.jiudi.shopping.bean.RecommendImgBean;
import com.jiudi.shopping.bean.RecommendTabBean;
import com.jiudi.shopping.bean.RecommendTitleBean;
import com.jiudi.shopping.bean.TabEntity;
import com.jiudi.shopping.bean.TiXian;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.cart.CartDetailActivity;
import com.jiudi.shopping.ui.fenxiao.FenXiaoMenuActivity;
import com.jiudi.shopping.ui.fenxiao.FenXiaoNoActivity;
import com.jiudi.shopping.ui.user.OrderFragment;
import com.jiudi.shopping.ui.user.account.TongZhiActivity;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.widget.NoHScrollViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 主页
 */
public class HomeVVFragment extends BaseFragment {


//    private String[] titles = {"推荐", "日用百货", "个护清洁","数码电器"};
private List<RecommendTabBean> mRecommendTabList = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private LinearLayout needshow;
    private ImageView showhead;
    private TextView content;
    private Fictitious fictitious;
    private String[] titles;
    private FrameLayout flChange;
    private TabLayout mainTab;
    private ImageView tongzhi;
    private ImageView seach;

    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_homevv;
    }

    @Override
    public void initView() {

        needshow = (LinearLayout) findViewById(R.id.needshow);
        showhead = (ImageView) findViewById(R.id.showhead);
        content = (TextView) findViewById(R.id.content);
        flChange = (FrameLayout) findViewById(R.id.fl_change);
        mainTab = (TabLayout) findViewById(R.id.main_tab);
        tongzhi = (ImageView) findViewById(R.id.tongzhi);
        seach = (ImageView) findViewById(R.id.seach);
    }

    @Override
    public void initData() {


        buildFicition();
        getHomeBanner();
    }



    public void buildTab(){
        mFragments.add(new HomeBVFragment());
        for (int i = 1; i <mRecommendTabList.size() ; i++) {
            mFragments.add(new HomeCVFragment().setArgumentz("cId",mRecommendTabList.get(i).id+""));
        }
        titles = new String[mRecommendTabList.size()];
        for (int i = 0; i <mRecommendTabList.size() ; i++) {
            titles[i]=mRecommendTabList.get(i).cate_name;
        }
        for (int i = 0; i < mRecommendTabList.size(); i++) {
            //插入tab标签
            mainTab.addTab(mainTab.newTab().setText(mRecommendTabList.get(i).cate_name));
        }
        if(mRecommendTabList.size()>4){

            mainTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        }else{

            mainTab.setTabMode(TabLayout.MODE_FIXED);
        }
        mainTab.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getFragmentManager().beginTransaction().replace(R.id.fl_change,mFragments.get(tab.getPosition())).commitAllowingStateLoss();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        getFragmentManager().beginTransaction().replace(R.id.fl_change,mFragments.get(0)).commitAllowingStateLoss();
    }

    private void getHomeBanner() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getFlash(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    JSONObject data = res.getJSONObject("data");
                    if (code == 200) {

                        JSONArray category = data.getJSONArray("category");
                        RecommendTabBean hotbean = new RecommendTabBean();
                        hotbean.id = "0";
                        hotbean.cate_name = "推荐";
                        mRecommendTabList.add(hotbean);
                        for (int i = 0; i < category.length(); i++) {
                            JSONObject jsonObject = category.getJSONObject(i);
                            RecommendTabBean bean = new RecommendTabBean();
                            bean.id = jsonObject.optString("id");
                            bean.cate_name = jsonObject.optString("cate_name");
                            mRecommendTabList.add(bean);
                        }
                        buildTab();

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
        needshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fictitious.type==1) {
                    if ("1".equals((AccountManager.sUserBean == null ? "0" : AccountManager.sUserBean.is_promoter))) {
                        startActivity(new Intent(mActivity, FenXiaoMenuActivity.class));
                    } else {
                        startActivity(new Intent(mActivity, FenXiaoNoActivity.class));
                    }
                } else {
                    startActivity(new Intent(mActivity, CartDetailActivity.class).putExtra("id",fictitious.id+""));
                }
            }
        });
        seach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mActivity, SearchMenuShopActivity.class));
            }
        });
        tongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, TongZhiActivity.class));
            }
        });
    }
    private void showGods(){
        needshow.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(needshow, "alpha", 0.0f, 1.0f).setDuration(2000);
        animator.start();
        content.setText("来自"+fictitious.city+"的"+fictitious.nickname+(fictitious.type==1?"升级成为店主":"购买了"+fictitious.store_name)+"");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideGods();
            }
        },5000);

    }
    private void hideGods(){
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
        alpha.setDuration(1000);
        needshow.startAnimation(alpha);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                needshow.setVisibility(View.GONE);
                buildFicition();
            }
        },1000);
    }
    public void buildFicition(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getFictitious();
            }
        },5000);
    }
    private void getFictitious() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getFictitious(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {

                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                return new Date(json.getAsJsonPrimitive().getAsLong());
                            }
                        });
                        Gson gson = builder.create();
                        Type fictitiousType = new TypeToken<Fictitious>() {
                        }.getType();
                        fictitious=gson.fromJson(res.getJSONObject("data").toString(),fictitiousType);
                        showGods();
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
}
