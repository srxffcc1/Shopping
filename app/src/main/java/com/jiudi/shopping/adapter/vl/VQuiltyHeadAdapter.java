package com.jiudi.shopping.adapter.vl;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.jiudi.shopping.R;
import com.jiudi.shopping.bean.CartDiscussBean;
import com.jiudi.shopping.bean.CartTitleBean;
import com.jiudi.shopping.bean.RecommendTitleBean;
import com.jiudi.shopping.ui.user.account.DiscussListActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by admin on 2017/5/16.
 */

public class VQuiltyHeadAdapter extends DelegateAdapter.Adapter{
    public Context context;
    private LayoutHelper helper;
    private RecommendTitleBean mcarttitlebean;
    public VQuiltyHeadAdapter(Context context, LayoutHelper helper, RecommendTitleBean mcarttitlebean) {
        this.context = context;
        this.helper = helper;
        this.mcarttitlebean = mcarttitlebean;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_qulity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
