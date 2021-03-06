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
import com.jiudi.shopping.ui.user.account.DiscussListActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by admin on 2017/5/16.
 */

public class VDiscussHeadAdapter extends DelegateAdapter.Adapter{
    public Context context;
    private LayoutHelper helper;
    private CartTitleBean mcarttitlebean;
    List<CartDiscussBean> mcartdiscussbeanlist;
    public VDiscussHeadAdapter(Context context, LayoutHelper helper, CartTitleBean mcarttitlebean, List<CartDiscussBean> mcartdiscussbeanlist) {
        this.context = context;
        this.helper = helper;
        this.mcarttitlebean = mcarttitlebean;
        this.mcartdiscussbeanlist=mcartdiscussbeanlist;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_discuss_head, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(mcartdiscussbeanlist!=null&&mcartdiscussbeanlist.size()>0){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MobclickAgent.onEvent(context,"B_goods_pingjia");
                    context.startActivity(new Intent(context, DiscussListActivity.class).putExtra("productId",mcarttitlebean.id));
                }
            });
        }else{
            holder.itemView.findViewById(R.id.zanwu).setVisibility(View.VISIBLE);
            holder.itemView.findViewById(R.id.seeall).setVisibility(View.GONE);
        }

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
