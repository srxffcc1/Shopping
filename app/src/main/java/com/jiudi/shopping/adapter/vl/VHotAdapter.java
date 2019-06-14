package com.jiudi.shopping.adapter.vl;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
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
import com.jiudi.shopping.bean.RecommendImgBean;
import com.jiudi.shopping.ui.cart.CartDetailActivity;

import java.util.List;

/**
 * Created by admin on 2017/5/16.
 */

public class VHotAdapter extends DelegateAdapter.Adapter {
    public Context context;
    private LayoutHelper helper;
    private List<RecommendImgBean> recommendImgBeans;


    public VHotAdapter(Context context, LayoutHelper helper, List<RecommendImgBean> recommendImgBeans) {
        this.context = context;
        this.helper = helper;
        this.recommendImgBeans = recommendImgBeans;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_hot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);//缓存全尺寸
        if(recommendImgBeans!=null&&recommendImgBeans.size()>0){
            ViewHolder tagholder= (ViewHolder) holder;
            Glide.with(context).load(recommendImgBeans.get(0).pic).apply(options).into(tagholder.image0);
            Glide.with(context).load(recommendImgBeans.get(1).pic).apply(options).into(tagholder.image1);
            Glide.with(context).load(recommendImgBeans.get(2).pic).apply(options).into(tagholder.image2);
            Glide.with(context).load(recommendImgBeans.get(3).pic).apply(options).into(tagholder.image3);
            Glide.with(context).load(recommendImgBeans.get(4).pic).apply(options).into(tagholder.image4);
            tagholder.hot0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, CartDetailActivity.class).putExtra("id",recommendImgBeans.get(0).product_id));
                }
            });
            tagholder.hot1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, CartDetailActivity.class).putExtra("id",recommendImgBeans.get(1).product_id));
                }
            });
            tagholder.hot2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, CartDetailActivity.class).putExtra("id",recommendImgBeans.get(2).product_id));
                }
            });
            tagholder.hot3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, CartDetailActivity.class).putExtra("id",recommendImgBeans.get(3).product_id));
                }
            });
            tagholder.hot4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, CartDetailActivity.class).putExtra("id",recommendImgBeans.get(4).product_id));
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout hot1;
        public ImageView image0;
        public LinearLayout hot2;
        public ImageView image1;
        public LinearLayout hot3;
        public ImageView image2;
        public LinearLayout hot4;
        public ImageView image3;
        public LinearLayout hot0;
        public ImageView image4;

        public ViewHolder(View itemView) {
            super(itemView);
            hot1 = (LinearLayout) itemView.findViewById(R.id.hot1);
            image0 = (ImageView) itemView.findViewById(R.id.image0);
            hot2 = (LinearLayout) itemView.findViewById(R.id.hot2);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            hot3 = (LinearLayout) itemView.findViewById(R.id.hot3);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            hot4 = (LinearLayout) itemView.findViewById(R.id.hot4);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
            hot0 = (LinearLayout) itemView.findViewById(R.id.hot0);
            image4 = (ImageView) itemView.findViewById(R.id.image4);
        }
    }

}
