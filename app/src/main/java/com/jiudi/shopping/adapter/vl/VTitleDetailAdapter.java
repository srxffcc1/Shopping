package com.jiudi.shopping.adapter.vl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.jiudi.shopping.R;
import com.jiudi.shopping.bean.CartTitleBean;
import com.jiudi.shopping.manager.AccountManager;

/**
 * Created by admin on 2017/5/16.
 */

public class VTitleDetailAdapter extends DelegateAdapter.Adapter{
    public Context context;
    private LayoutHelper helper;
    private CartTitleBean mcarttitlebean;

    public VTitleDetailAdapter(Context context, LayoutHelper helper, CartTitleBean mcarttitlebean) {
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
                .inflate(R.layout.item_cart_title, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(mcarttitlebean!=null){
            ((TextView)holder.itemView.findViewById(R.id.money)).setText("¥"+("1".equals(AccountManager.sUserBean.is_promoter)?mcarttitlebean.vip_price:mcarttitlebean.price));
            ((TextView)holder.itemView.findViewById(R.id.sum)).setText("库存："+mcarttitlebean.stock+mcarttitlebean.unit_name);
            ((TextView)holder.itemView.findViewById(R.id.sales)).setText("销量："+mcarttitlebean.sales+mcarttitlebean.unit_name);
            ((TextView)holder.itemView.findViewById(R.id.title)).setText(mcarttitlebean.store_name);
            ((TextView)holder.itemView.findViewById(R.id.second_title)).setText(mcarttitlebean.store_info);
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
