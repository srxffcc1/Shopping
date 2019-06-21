package com.jiudi.shopping.adapter.vl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jiudi.shopping.R;
import com.jiudi.shopping.bean.GodMiaoSha;
import com.jiudi.shopping.bean.MainGodsBean;
import com.jiudi.shopping.ui.cart.CartDetailActivity;
import com.jiudi.shopping.ui.cart.CartDetailActivityCopy;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by admin on 2017/5/16.
 */

public class VMiaoShaAdapter extends DelegateAdapter.Adapter {
    private String[] titles = {"周一场", "周二场","周三场","周四场","周五场","周六场","周日场"};
    public Context context;
    private LayoutHelper helper;
    private List<GodMiaoSha> mhotlist;
    private String miaotype;
    private String dat;
    private View.OnClickListener listener;


    public VMiaoShaAdapter(Context context, LayoutHelper helper, List<GodMiaoSha> mhotlist, String miaotype,String dat,View.OnClickListener listener) {
        this.context = context;
        this.helper = helper;
        this.mhotlist = mhotlist;
        this.miaotype=miaotype;
        this.dat=dat;
        this.listener=listener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = "即将开抢".equals(miaotype)?LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_miaoshag, parent, false):LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_miaosha, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        final GodMiaoSha carChoiceBean=mhotlist.get(position);
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);//缓存全尺寸
        Glide.with(context).load(carChoiceBean.image).apply(options).into(viewHolder.miaoshaImg);
        viewHolder.miaoshaTitle.setText(carChoiceBean.title);
        viewHolder.miaoshaSectitle.setText(carChoiceBean.info);
        viewHolder.miaoshaOldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.miaoshaOldprice.setText("¥"+carChoiceBean.ot_price);
        viewHolder.miaoshaPrice.setText("¥"+carChoiceBean.price);
        viewHolder.yiqianggou.setText("已抢"+carChoiceBean.sales+carChoiceBean.unit_name);
        viewHolder.baifenbi.setText(new DecimalFormat("#0").format((carChoiceBean.sales*1.0/carChoiceBean.stock*100))+"%");
        int bili= (int) (carChoiceBean.sales*1.0/carChoiceBean.stock*1000);
        viewHolder.progressz.setProgress(bili);

        if("即将开抢".equals(miaotype)){
            viewHolder.miaoshaPass.setText("开抢提醒");
            viewHolder.miaoshaPass.setOnClickListener(listener);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("详情id"+carChoiceBean.id);
                    context.startActivity(new Intent(context, CartDetailActivity.class).putExtra("id",carChoiceBean.product_id+""));
                }
            });
        }else if("开抢中".equals(miaotype)){
            viewHolder.miaoshaPass.setText("马上抢");
            viewHolder.miaoshaPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, CartDetailActivityCopy.class).putExtra("id",carChoiceBean.id+""));
                }
            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("详情id"+carChoiceBean.id);
                    context.startActivity(new Intent(context, CartDetailActivityCopy.class).putExtra("id",carChoiceBean.id+""));
                }
            });
        }else {
            viewHolder.miaoshaPass.setText("原价购买");
            viewHolder.miaoshaPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, CartDetailActivity.class).putExtra("id",carChoiceBean.product_id+""));
                }
            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("详情id"+carChoiceBean.id);
                    context.startActivity(new Intent(context, CartDetailActivity.class).putExtra("id",carChoiceBean.product_id+""));
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mhotlist.size();
    }

    private void initView() {


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView miaoshaImg;
        public TextView miaoshaTitle;
        public TextView miaoshaSectitle;
        public ProgressBar progressz;
        public TextView yiqianggou;
        public TextView baifenbi;
        public TextView miaoshaPrice;
        public TextView miaoshaOldprice;
        public TextView miaoshaPass;
        public ViewHolder(View itemView) {
            super(itemView);
            miaoshaImg = (ImageView) itemView.findViewById(R.id.miaosha_img);
            miaoshaTitle = (TextView) itemView.findViewById(R.id.miaosha_title);
            miaoshaSectitle = (TextView) itemView.findViewById(R.id.miaosha_sectitle);
            progressz = (ProgressBar) itemView.findViewById(R.id.progressz);
            yiqianggou = (TextView) itemView.findViewById(R.id.yiqianggou);
            baifenbi = (TextView) itemView.findViewById(R.id.baifenbi);
            miaoshaPrice = (TextView) itemView.findViewById(R.id.miaosha_price);
            miaoshaOldprice = (TextView) itemView.findViewById(R.id.miaosha_oldprice);
            miaoshaPass = (TextView) itemView.findViewById(R.id.miaosha_pass);
        }
    }

}
