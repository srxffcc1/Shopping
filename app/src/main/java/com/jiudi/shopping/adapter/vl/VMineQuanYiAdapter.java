package com.jiudi.shopping.adapter.vl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.bumptech.glide.request.RequestOptions;
import com.jiudi.shopping.R;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.ui.fenxiao.TuanDuiActivity;
import com.jiudi.shopping.ui.user.account.FenXiaoAccountActivity;
import com.jiudi.shopping.ui.user.account.TiXianActivity;
import com.m7.imkfsdk.KfStartHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2017/5/16.
 */

public class VMineQuanYiAdapter extends DelegateAdapter.Adapter {
    public Context context;
    private LayoutHelper helper;
    private View.OnClickListener listener;
    private boolean isdianzhu;
    private JSONObject jsondata;
    private ImageView head;
    private TextView name;
    private TextView level;
    private TextView code;
    private TextView zuanshititle;
    private TextView zuanshishouyi;
    private LinearLayout needshowDianzhu;
    private TextView yue;
    private TextView tixian;
    private LinearLayout shouyimingxi;
    private TextView leijishouyi;
    private TextView tleijishouyi;
    private TextView yitixianshouyi;
    private TextView tyitixianshouyi;
    private TextView weidaozhangshouyi;
    private TextView tweidaozhangshouyi;
    private LinearLayout kehuguanli;
    private TextView jiamengshangshuliang;
    private LinearLayout stepl;
    private TextView fensishuliang;
    private LinearLayout quanl;
    private TextView yaoqingren;
    private LinearLayout kefu;
    private LinearLayout needshowFeiudianzhu;
    private KfStartHelper kefuhelper;
    private static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 1001;
    private static final int REQUEST_CODE_UNKNOWN_APP = 100;
    private static final int REQUEST_CODE_OPENCHAT = 60;

    public VMineQuanYiAdapter(Context context, LayoutHelper helper, View.OnClickListener listener, JSONObject jsondata) {
        this.context = context;
        this.helper = helper;
        this.listener = listener;
        this.jsondata = jsondata;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quanyiup, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        head = (ImageView) holder.itemView.findViewById(R.id.head);
        name = (TextView) holder.itemView.findViewById(R.id.name);
        level = (TextView) holder.itemView.findViewById(R.id.level);
        code = (TextView) holder.itemView.findViewById(R.id.code);
        zuanshititle = (TextView) holder.itemView.findViewById(R.id.zuanshititle);
        zuanshishouyi = (TextView) holder.itemView.findViewById(R.id.zuanshishouyi);
        needshowDianzhu = (LinearLayout) holder.itemView.findViewById(R.id.needshow_dianzhu);
        yue = (TextView) holder.itemView.findViewById(R.id.yue);
        tixian = (TextView) holder.itemView.findViewById(R.id.tixian);
        shouyimingxi = (LinearLayout) holder.itemView.findViewById(R.id.shouyimingxi);
        leijishouyi = (TextView) holder.itemView.findViewById(R.id.leijishouyi);
        tleijishouyi = (TextView) holder.itemView.findViewById(R.id.tleijishouyi);
        yitixianshouyi = (TextView) holder.itemView.findViewById(R.id.yitixianshouyi);
        tyitixianshouyi = (TextView) holder.itemView.findViewById(R.id.tyitixianshouyi);
        weidaozhangshouyi = (TextView) holder.itemView.findViewById(R.id.weidaozhangshouyi);
        tweidaozhangshouyi = (TextView) holder.itemView.findViewById(R.id.tweidaozhangshouyi);
        kehuguanli = (LinearLayout) holder.itemView.findViewById(R.id.kehuguanli);
        jiamengshangshuliang = (TextView) holder.itemView.findViewById(R.id.jiamengshangshuliang);
        stepl = (LinearLayout) holder.itemView.findViewById(R.id.stepl);
        fensishuliang = (TextView) holder.itemView.findViewById(R.id.fensishuliang);
        quanl = (LinearLayout) holder.itemView.findViewById(R.id.quanl);
        yaoqingren = (TextView) holder.itemView.findViewById(R.id.yaoqingren);
        kefu = (LinearLayout) holder.itemView.findViewById(R.id.kefu);
        needshowFeiudianzhu = (LinearLayout)holder.itemView. findViewById(R.id.needshow_feiudianzhu);

        tixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, TiXianActivity.class));
            }
        });
        kehuguanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, TuanDuiActivity.class));

            }
        });
        shouyimingxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, FenXiaoAccountActivity.class));
            }
        });
        kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        kefuhelper = new KfStartHelper((Activity) context);
                kefuhelper.initSdkChat("e183f850-6650-11e9-b942-bf7a16e827df", "咨询", AccountManager.sUserBean.uid,REQUEST_CODE_OPENCHAT);//陈辰正式
            }
        });


        if(isdianzhu){
            needshowDianzhu.setVisibility(View.VISIBLE);
            needshowFeiudianzhu.setVisibility(View.GONE);
            bindDataToView(jsondata,true);
        }else{
            needshowDianzhu.setVisibility(View.GONE);
            needshowFeiudianzhu.setVisibility(View.VISIBLE);
            bindDataToView(jsondata,false);
        }
    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public void bindDataToView(JSONObject data, boolean need) {
        String shenfen = "";
        if ("1".equals((AccountManager.sUserBean == null ? "0" : AccountManager.sUserBean.is_promoter))) {
            if ("2".equals(AccountManager.sUserBean.agent_id)) {
                shenfen = "钻石店主";
                code.setVisibility(View.VISIBLE);
            } else if ("1".equals(AccountManager.sUserBean.agent_id)) {

                shenfen = "普通店主";
                code.setVisibility(View.VISIBLE);
            } else {
                shenfen = "普通用户";
                code.setVisibility(View.GONE);
            }
        } else {
            shenfen = "普通用户";
            code.setVisibility(View.GONE);
        }

        level.setText(shenfen);
        code.setText("邀请码："+AccountManager.sUserBean.uid);

        name.setText(AccountManager.sUserBean.nickname);
        RequestOptions requestOptions = RequestOptions.circleCropTransform().error(R.drawable.head_defuat_circle);
        Glide.with(context).load((AccountManager.sUserBean.avatar.startsWith("http")) ? AccountManager.sUserBean.avatar : "http://" + AccountManager.sUserBean.avatar).apply(requestOptions).into(head);
        if (data != null) {
            if(need){
                try {
                    yue.setText(data.getJSONObject("userInfo").getString("now_money")+"元");
                    zuanshishouyi.setText("开店以来已为你赚取￥"+data.getString("allnumber")+"元");
                    leijishouyi.setText(data.getString("allnumber")+"元");
                    yitixianshouyi.setText(data.getString("extractNumber")+"元");
                    weidaozhangshouyi.setText(data.getString("number")+"元");

                    tleijishouyi.setText("今日：+"+data.getString("todayAllNumber")+"元");
                    tyitixianshouyi.setText("今日：+"+data.getString("todayExtractNumber")+"元");
                    tweidaozhangshouyi.setText("今日：+"+data.getString("todayNumber")+"元");

                    jiamengshangshuliang.setText(data.getString("directNum")+"位");
                    fensishuliang.setText(data.getString("teamNum")+"位");
                    try {
                        yaoqingren.setText(data.getJSONObject("userInfo").getJSONObject("spread_name").getString("nickname").replace("null",""));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void initView() {



    }

    public void setIsDianZhu(boolean isdianzhu) {
        this.isdianzhu = isdianzhu;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


}
