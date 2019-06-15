package com.jiudi.shopping.adapter.vl;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.RecommendBean;
import com.jiudi.shopping.ui.main.SearchShopActivity;

import java.util.List;

/**
 * Created by admin on 2017/5/16.
 */

public class VRecommendAdapter extends DelegateAdapter.Adapter{
    public Context context;
    private LayoutHelper helper;
    private List<RecommendBean> mcartdiscussbeanlist;

    public VRecommendAdapter(Context context, LayoutHelper helper, List<RecommendBean> mcartdiscussbeanlist) {
        this.context = context;
        this.helper = helper;
        this.mcartdiscussbeanlist = mcartdiscussbeanlist;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_recommend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(mcartdiscussbeanlist.get(position)!=null){
            final RecommendBean bean=mcartdiscussbeanlist.get(position);
            final ImageView imageview=holder.itemView.findViewById(R.id.img);
            final TextView textView=holder.itemView.findViewById(R.id.title);
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .error(R.drawable.fenxiao_head_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);//缓存全尺寸
            Glide.with(context).load(mcartdiscussbeanlist.get(position).pic).apply(options).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    int height=(((BaseActivity)context).px-320)/5;
                    int swidth= (int) ((resource.getIntrinsicWidth()*1.0/resource.getIntrinsicHeight())*height);
                    imageview.setLayoutParams(new LinearLayout.LayoutParams(swidth, height));
                    imageview.setImageDrawable(resource);
                }
            });
            textView.setText(bean.name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.startActivity(new Intent(context, SearchShopActivity.class).putExtra("cId",bean.cid));
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mcartdiscussbeanlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
