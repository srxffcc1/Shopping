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
import android.widget.Toast;

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
import com.jiudi.shopping.bean.RecommendImgBean;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.cart.CartDetailActivity;
import com.jiudi.shopping.ui.user.AllQuanActivity;
import com.jiudi.shopping.util.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/5/16.
 */

public class VHot2Adapter extends DelegateAdapter.Adapter {
    public Context context;
    private LayoutHelper helper;
    private List<RecommendImgBean> recommendImgBeans;


    public VHot2Adapter(Context context, LayoutHelper helper, List<RecommendImgBean> recommendImgBeans) {
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
                .inflate(R.layout.item_cart_hot2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);//缓存全尺寸
        if(recommendImgBeans!=null&&recommendImgBeans.size()>0){
            final ViewHolder tagholder= (ViewHolder) holder;

            RequestOptions optionsgif = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸


            Glide.with(context).asGif().load(recommendImgBeans.get(0).pic).apply(optionsgif).into(tagholder.image0);

            Glide.with(context).load(recommendImgBeans.get(1).pic).apply(options).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    int swidth= (((BaseActivity)context).px-10);
                    int height= (int) ((resource.getIntrinsicHeight()*1.0/resource.getIntrinsicWidth())*swidth);
                    tagholder.image1.setLayoutParams(new LinearLayout.LayoutParams(swidth, height));
                    tagholder.image1.setImageDrawable(resource);
//                picture.setImageResource(R.drawable.tmp_gods);
                }
            });
            Glide.with(context).load(recommendImgBeans.get(2).pic).apply(options).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    int swidth= (((BaseActivity)context).px-40)/3;
                    int height= (int) ((resource.getIntrinsicHeight()*1.0/resource.getIntrinsicWidth())*swidth);
                    tagholder.image2.setLayoutParams(new LinearLayout.LayoutParams(swidth, height));
                    tagholder.image2.setImageDrawable(resource);
//                picture.setImageResource(R.drawable.tmp_gods);
                }
            });
            Glide.with(context).load(recommendImgBeans.get(3).pic).apply(options).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    int swidth= (((BaseActivity)context).px-40)/3;
                    int height= (int) ((resource.getIntrinsicHeight()*1.0/resource.getIntrinsicWidth())*swidth);
                    tagholder.image3.setLayoutParams(new LinearLayout.LayoutParams(swidth, height));
                    tagholder.image3.setImageDrawable(resource);
//                picture.setImageResource(R.drawable.tmp_gods);
                }
            });
            Glide.with(context).load(recommendImgBeans.get(4).pic).apply(options).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    int swidth= (((BaseActivity)context).px-40)/3;
                    int height= (int) ((resource.getIntrinsicHeight()*1.0/resource.getIntrinsicWidth())*swidth);
                    tagholder.image4.setLayoutParams(new LinearLayout.LayoutParams(swidth, height));
                    tagholder.image4.setImageDrawable(resource);
//                picture.setImageResource(R.drawable.tmp_gods);
                }
            });






            tagholder.hot0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCoupo();
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
    private void getCoupo() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getCoupo(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {


//                simpleRefresh.onRefreshComplete();
//                simpleRefresh.onLoadMoreComplete();
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {

                        Toast.makeText(context,info,Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, AllQuanActivity.class));
                    }else{
                        Toast.makeText(context,info,Toast.LENGTH_SHORT).show();
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
