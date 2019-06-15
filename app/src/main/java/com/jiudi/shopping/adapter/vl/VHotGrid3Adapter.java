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
import com.jiudi.shopping.bean.MainGodsBean;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.ui.cart.CartDetailActivity;

import java.util.List;

/**
 * Created by admin on 2017/5/16.
 */

public class VHotGrid3Adapter extends DelegateAdapter.Adapter {
    public Context context;
    private LayoutHelper helper;
    private List<MainGodsBean> mhotlist;
    private int fujian_px=0;

    public VHotGrid3Adapter(Context context, LayoutHelper helper, List<MainGodsBean> mhotlist) {
        this.context = context;
        this.helper = helper;
        this.mhotlist = mhotlist;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_hot_gridc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final MainGodsBean carChoiceBean=mhotlist.get(position);

         LinearLayout picturel;
         final ImageView picture;
         TextView title;
         TextView secondTitle;
         TextView showPrice;
        picturel = (LinearLayout) holder.itemView.findViewById(R.id.picturel);
        picture = (ImageView) holder.itemView.findViewById(R.id.picture);
        title = (TextView) holder.itemView.findViewById(R.id.title);
        secondTitle = (TextView) holder.itemView.findViewById(R.id.second_title);
        showPrice = (TextView) holder.itemView.findViewById(R.id.show_price);


        title.setText(carChoiceBean.store_name);
        secondTitle.setText(carChoiceBean.keyword);
        if(carChoiceBean.keyword==null||"".equals(carChoiceBean.keyword)){
            secondTitle.setVisibility(View.GONE);
        }else{
            secondTitle.setVisibility(View.VISIBLE);
        }
        showPrice.setText("¥"+("1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))?carChoiceBean.vip_price:carChoiceBean.price));


        RequestOptions options = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);//缓存全尺寸
//        System.out.println("推荐:"+carChoiceBean.image);
        Glide.with(context).load(carChoiceBean.image).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                int swidth= (((BaseActivity)context).px-40)/2;
                int height= (int) ((resource.getIntrinsicHeight()*1.0/resource.getIntrinsicWidth())*swidth);
                picture.setLayoutParams(new LinearLayout.LayoutParams(swidth, height));
                picture.setImageDrawable(resource);
//                picture.setImageResource(R.drawable.tmp_gods);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, CartDetailActivity.class).putExtra("id",carChoiceBean.id));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mhotlist.size();
    }

    private void initView() {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
