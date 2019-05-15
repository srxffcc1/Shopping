package com.jiudi.shopping.adapter.vl;

import android.content.Context;
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
import com.jiudi.shopping.bean.CartDiscussBean;
import com.jiudi.shopping.bean.CartIntroduceBean;

import java.util.List;

/**
 * Created by admin on 2017/5/16.
 */

public class VIntroduceAdapter extends DelegateAdapter.Adapter{
    public Context context;
    private LayoutHelper helper;
    private List<CartIntroduceBean> mcartdiscussbeanlist;

    public VIntroduceAdapter(Context context, LayoutHelper helper, List<CartIntroduceBean> mcartdiscussbeanlist) {
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
                .inflate(R.layout.item_cart_introduce, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(mcartdiscussbeanlist.get(position)!=null){
            final ImageView imageview=holder.itemView.findViewById(R.id.cart_introduce_img);
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);//缓存全尺寸
            Glide.with(context).load(mcartdiscussbeanlist.get(position).url).apply(options).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    int swidth=((BaseActivity)context).px-10;
                    int height= (int) ((resource.getIntrinsicHeight()*1.0/resource.getIntrinsicWidth())*swidth);
                    imageview.setLayoutParams(new LinearLayout.LayoutParams(swidth, height));
                    imageview.setImageDrawable(resource);
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
