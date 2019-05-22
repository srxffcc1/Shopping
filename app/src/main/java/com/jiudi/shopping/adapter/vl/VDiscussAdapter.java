package com.jiudi.shopping.adapter.vl;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jiudi.shopping.R;
import com.jiudi.shopping.bean.CartDiscussBean;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitManager;
import com.jiudi.shopping.ui.user.ShowImageActivity;
import com.jiudi.shopping.util.TimeUtil;
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
    private int fujian_px=0;

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
            final CartDiscussBean bean=mcartdiscussbeanlist.get(position);
            KRatingBar ratingBar=holder.itemView.findViewById(R.id.ratingbar2);
            ratingBar.setRating(bean.getStar());
            ((TextView)holder.itemView.findViewById(R.id.comment)).setText(""+bean.getComment());
            ((TextView)holder.itemView.findViewById(R.id.nickname)).setText(bean.getNickname());
            final ImageView imageView=holder.itemView.findViewById(R.id.head);
            if(bean.getAvatar()!=null){
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .error(R.drawable.cart_head)
                        .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
                Glide.with(context).load(bean.getAvatar().startsWith("http")?bean.getAvatar(): RequestManager.mBaseUrl3+bean.getAvatar()).apply(options).into(imageView);
            }

            ((TextView)holder.itemView.findViewById(R.id.time)).setText(""+bean.getAdd_time());


            final GridLayout fujianLayout = (GridLayout) holder.itemView.findViewById(R.id.fujian_layout);

            ViewTreeObserver vto = fujianLayout.getViewTreeObserver();
            if(fujian_px==0){
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int temppx = fujianLayout.getWidth();
                        fujianLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        fujian_px = (temppx - 0) / 4;
//                        System.out.println("获得的大小" + fujian_px);
                        new GridAdapter(context, bean.getPics(), fujianLayout).build();

                    }
                });
            }else{

                new GridAdapter(context, bean.getPics(), fujianLayout).build();
            }




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
    class GridAdapter {
        public Context context;
        private List<String> images;
        private ViewGroup parent;

        public GridAdapter(Context context, List<String> images, ViewGroup parent) {
            this.context = context;
            this.images = images;
            this.parent = parent;
        }

        public int getCount() {
            return images == null ? 0 : images.size();
        }

        public Object getItem(int position) {
            return images.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public void build() {
            parent.removeAllViews();

//            LinearLayout layout = new LinearLayout(context);
//            LinearLayout.LayoutParams LL_MW = new LinearLayout.LayoutParams
//                    (fujian_px, fujian_px);
//            layout.setOrientation(LinearLayout.VERTICAL);
//            layout.setGravity(Gravity.CENTER);
//            layout.setLayoutParams(LL_MW);
//
//            ImageView imageView = new ImageView(context);
//            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            LinearLayout.LayoutParams LL_IM = new LinearLayout.LayoutParams
//                    (fujian_px - 10, fujian_px - 10);
//            imageView.setLayoutParams(LL_IM);
//            imageView.setImageResource(R.drawable.start_pot);
//            layout.addView(imageView);
//            parent.addView(layout);
            for (int i = 0; i < getCount(); i++) {
                parent.addView(getView(i));
            }
        }

        public View getView(final int position) {
            System.out.println("进来");
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams LL_MW = new LinearLayout.LayoutParams
                    (fujian_px, fujian_px);
            layout.setLayoutParams(LL_MW);
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            LinearLayout.LayoutParams LL_IM = new LinearLayout.LayoutParams
                    (fujian_px - 10, fujian_px - 10);
            imageView.setLayoutParams(LL_IM);
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
            String pic=images.get(position);
            Glide.with(context).load(images.get(position)).apply(options).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ShowImageActivity.class).putExtra("URL",images.get(position)));
                }
            });
            layout.addView(imageView);
            return layout;
        }
    }

}
