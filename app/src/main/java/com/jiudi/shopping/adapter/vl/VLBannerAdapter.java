package com.jiudi.shopping.adapter.vl;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.jiudi.shopping.R;
import com.jiudi.shopping.bean.BannerBean;
import com.jiudi.shopping.ui.cart.CartDetailActivity;
import com.jiudi.shopping.widget.BannerLayout;

import java.util.ArrayList;
import java.util.List;

public class VLBannerAdapter extends DelegateAdapter.Adapter {
    private Context mContext;
    private LayoutHelper mHelper;
    private List<BannerBean> mBannerList;
    private List<String> mBannerUrlList = new ArrayList<>();

    public VLBannerAdapter(Context mContext, LayoutHelper mHelper, List<BannerBean> mBannerList) {
        this.mContext = mContext;
        this.mHelper = mHelper;
        this.mBannerList=mBannerList;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mHelper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_vlbanner, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int postion) {
        ViewHolder recyclerViewHolder = (ViewHolder) viewHolder;

//        System.out.println("获得的banner:");
        mBannerUrlList.clear();
        for (int i = 0; i < mBannerList.size(); i++) {
            mBannerUrlList.add(mBannerList.get(i).pic);
//            System.out.println("获得的banner:" + mBannerList.get(i).pic);
        }
        if (mBannerUrlList.size() > 0) {
            recyclerViewHolder.banner.setViewUrls(mBannerUrlList);
            recyclerViewHolder.banner.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    final BannerBean bannerBean = mBannerList.get(position);
                    mContext.startActivity(new Intent(mContext, CartDetailActivity.class).putExtra("id",bannerBean.url.replace("/wap/store/detail/id/","")));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }
    private class ViewHolder extends RecyclerView.ViewHolder {

        public BannerLayout banner;

        public ViewHolder(View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.bannerlayout);
        }
    }
}
