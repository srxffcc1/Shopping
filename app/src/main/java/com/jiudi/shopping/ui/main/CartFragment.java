package com.jiudi.shopping.ui.main;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.fondesa.recyclerviewdivider.RecyclerViewDivider;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.adapter.vl.VBannerAdapter;
import com.jiudi.shopping.adapter.vl.VDiscussAdapter;
import com.jiudi.shopping.adapter.vl.VDiscussEndAdapter;
import com.jiudi.shopping.adapter.vl.VDiscussHeadAdapter;
import com.jiudi.shopping.adapter.vl.VIntroduceAdapter;
import com.jiudi.shopping.adapter.vl.VIntroduceHeadAdapter;
import com.jiudi.shopping.adapter.vl.VTitleDetailAdapter;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.BannerBean;
import com.jiudi.shopping.bean.CartDiscussBean;
import com.jiudi.shopping.bean.CartIntroduceBean;
import com.jiudi.shopping.bean.CartShopCart;
import com.jiudi.shopping.bean.CartTitleBean;
import com.jiudi.shopping.bean.DianCompany;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.StatusBarUtil;
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
 * 购物车
 */
public class CartFragment extends BaseFragment {


    private RecyclerView recycler;
    private RecyclerCommonAdapter<CartShopCart> myAdapter;
    private List<CartShopCart> mBeanList = new ArrayList<>();
    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_cart;
    }

    @Override
    public void initView() {

        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    public void initData() {
        for (int i = 0; i <10 ; i++) {
            mBeanList.add(new CartShopCart());
        }
        showRecycleView();
    }

    @Override
    public void initEvent() {

    }
    private void showRecycleView() {
        if (myAdapter == null) {


            myAdapter = new RecyclerCommonAdapter<CartShopCart>(mActivity, R.layout.item_cart_list, mBeanList) {

                @Override
                protected void convert(ViewHolder holder, final CartShopCart carChoiceBean, int position) {

                }

            };

            recycler.addItemDecoration(RecyclerViewDivider.with(getActivity()).color(Color.parseColor("#909090")).build());
            recycler.setAdapter(myAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        }else{

            myAdapter.notifyDataSetChanged();
        }
    }
}
