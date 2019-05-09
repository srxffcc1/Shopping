package com.jiudi.shopping.adapter.vl;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jiudi.shopping.R;
import com.jiudi.shopping.bean.CartDiscussBean;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.widget.KRatingBar;
import com.jiudi.shopping.widget.NoScrollGridView;

import java.util.List;

/**
 * Created by admin on 2017/5/16.
 */

public class VDiscussAdapter extends DelegateAdapter.Adapter{
    public Context context;
    private LayoutHelper helper;
    private List<CartDiscussBean> mcartdiscussbeanlist;

    public VDiscussAdapter(Context context, LayoutHelper helper, List<CartDiscussBean> mcartdiscussbeanlist) {
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
                .inflate(R.layout.item_cart_discuss, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(mcartdiscussbeanlist.get(position)!=null){
            CartDiscussBean bean=mcartdiscussbeanlist.get(position);
            KRatingBar ratingBar=holder.itemView.findViewById(R.id.ratingbar2);
            ratingBar.setRating(bean.getStar());
            ((TextView)holder.itemView.findViewById(R.id.comment)).setText(""+bean.getComment());
            ((TextView)holder.itemView.findViewById(R.id.nickname)).setText(bean.getNickname());
//            GridLayout gridLayout=holder.itemView.findViewById(R.id.function_grid);
//            gridLayout.removeAllViews();
//            for (int i = 0; i <bean.getPics().size() ; i++) {
//                GridLayout.LayoutParams param= new GridLayout.LayoutParams(GridLayout.spec(
//                        GridLayout.UNDEFINED,GridLayout.FILL,1f),
//                        GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f));
//                ImageView imageView=new ImageView(context);
//                imageView.setLayoutParams(param);
//                RequestOptions options = new RequestOptions()
//                        .fitCenter()
//                        .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
//                gridLayout.addView(imageView);
//                Glide.with(context).load(RequestManager.mBaseUrl+bean.getPics().get(i)).apply(options).into(imageView);
//
//            }
            NoScrollGridView gridView=holder.itemView.findViewById(R.id.function_grid);
            gridView.setAdapter(new GridAdapter(context,bean.getPics()));

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
    class GridAdapter extends BaseAdapter{
        public GridAdapter(Context context, List<String> images) {
            this.context = context;
            this.images = images;
        }

        public Context context;
        private List<String> images;

        @Override
        public int getCount() {
            return images==null?0:images.size();
        }

        @Override
        public Object getItem(int position) {
            return images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView=new ImageView(context);
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
            Glide.with(context).load(images.get(position)).apply(options).into(imageView);
            return imageView;
        }
    }

}
