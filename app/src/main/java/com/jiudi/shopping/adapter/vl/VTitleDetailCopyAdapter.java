package com.jiudi.shopping.adapter.vl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
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
import com.jiudi.shopping.util.DateTimeUtil;
import com.jiudi.shopping.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2017/5/16.
 */

public class VTitleDetailCopyAdapter extends DelegateAdapter.Adapter{
    public Context context;
    private LayoutHelper helper;
    private CartTitleBean mcarttitlebean;
    private String destime;
    private Timer timer;

    public VTitleDetailCopyAdapter(Context context, LayoutHelper helper, CartTitleBean mcarttitlebean) {
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
                .inflate(R.layout.item_cart_title_kill, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(mcarttitlebean!=null){
            ((TextView)holder.itemView.findViewById(R.id.money)).setText("¥"+mcarttitlebean.price);
            ((TextView)holder.itemView.findViewById(R.id.sum)).setText("库存："+mcarttitlebean.stock+mcarttitlebean.unit_name);
            ((TextView)holder.itemView.findViewById(R.id.sales)).setText("销量："+mcarttitlebean.sales+mcarttitlebean.unit_name);
            ((TextView)holder.itemView.findViewById(R.id.title)).setText(mcarttitlebean.store_name);
            ((TextView)holder.itemView.findViewById(R.id.second_title)).setText(mcarttitlebean.store_info);
            ((TextView)holder.itemView.findViewById(R.id.oldmoney)).setText("¥"+mcarttitlebean.ot_price);
            ((TextView)holder.itemView.findViewById(R.id.oldmoney)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            destime= TimeUtil.formatLongAll(mcarttitlebean.stop_time);
            if (timer == null) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            if(context!=null){
                                ((Activity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String result= DateTimeUtil.getDistanceTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),destime);
                                        ((TextView)holder.itemView.findViewById(R.id.rtime)).setText("距结束"+result);
                                    }
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 1000);
            }
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
