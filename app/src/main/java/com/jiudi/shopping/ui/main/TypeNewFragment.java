package com.jiudi.shopping.ui.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.GodTypeC;
import com.jiudi.shopping.bean.GodTypeP;
import com.jiudi.shopping.bean.RecommendTabBean;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.QTabView;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * 客服
 */
public class TypeNewFragment extends BaseFragment {


    private LinearLayout searchTagl;
    private TextView searchTag;
    private ImageView searchPass;
    private q.rorbin.verticaltablayout.VerticalTabLayout verticaltabLayout;
    private android.support.v7.widget.RecyclerView recycler;
    List<Fragment> mFragments=new ArrayList<>();
    private List<RecommendTabBean> mRecommendTabList = new ArrayList<>();
    private String[] titles;

    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_typenew;
    }


    @Override
    public void initView() {

        searchTagl = (LinearLayout) findViewById(R.id.search_tagl);
        searchTag = (TextView) findViewById(R.id.search_tag);
        searchPass = (ImageView) findViewById(R.id.search_pass);
        verticaltabLayout = (VerticalTabLayout) findViewById(R.id.verticaltabLayout);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    public void initData() {
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
        verticaltabLayout.setupWithFragment(getFragmentManager(), R.id.fragment_container, mFragments
                , new TabAdapter() {
                    @Override
                    public int getCount() {
                        return mFragments.size();
                    }

                    @Override
                    public QTabView.TabBadge getBadge(int position) {
                        return null;
                    }

                    @Override
                    public QTabView.TabIcon getIcon(int position) {
                        return null;
                    }

                    @Override
                    public QTabView.TabTitle getTitle(int position) {
                        return new TabView.TabTitle.Builder()
                                .setContent(titles[position])
                                .setTextColor(0xFFE60012, 0xFF0F0F0F)
                                .build();
                    }

                    @Override
                    public int getBackground(int position) {
                        return 0;
                    }
                });
        verticaltabLayout.setTabSelected(0);
    }
    @Override
    public void initEvent() {

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
}
