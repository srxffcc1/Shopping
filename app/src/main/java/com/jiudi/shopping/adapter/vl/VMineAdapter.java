package com.jiudi.shopping.adapter.vl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.jiudi.shopping.R;
import com.jiudi.shopping.bean.UserBean;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.fenxiao.FenXiaoMenuActivity;
import com.jiudi.shopping.ui.fenxiao.FenXiaoNoActivity;
import com.jiudi.shopping.ui.fenxiao.TuiGuangActivity;
import com.jiudi.shopping.ui.user.AddressListActivity;
import com.jiudi.shopping.ui.user.AllOrderActivity;
import com.jiudi.shopping.ui.user.AllQuanActivity;
import com.jiudi.shopping.ui.user.ShopSettingActivity;
import com.jiudi.shopping.ui.user.account.ShouCangActivity;
import com.jiudi.shopping.ui.user.account.TongZhiActivity;
import com.jiudi.shopping.util.SPUtil;
import com.m7.imkfsdk.KfStartHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by admin on 2017/5/16.
 */

public class VMineAdapter extends DelegateAdapter.Adapter {
    public Context context;
    private LayoutHelper helper;
    private ImageView setting;
    private ImageView tongzhi;
    private ImageView head;
    private TextView name;
    private TextView level;
    private TextView code;
    private ImageView actIcon;
    private TextView actContent;
    private TextView actGo;
    private LinearLayout allOrder;
    private TextView myallorder;
    private TextView daifukuan;
    private TextView daifahuo;
    private TextView daishouhuo;
    private TextView yiwancheng;
    private TextView dianzhuquanyi2;
    private TextView erweima;
    private TextView lianxikefu;
    private TextView dizhi;
    private TextView mylessmoneyvalue;
    private TextView dianzhu2;

    private TextView shoucangnum;
    private TextView quannum;

    private View.OnClickListener listener;
    private Badge noBuyb;
    private Badge noPostageb;
    private Badge noTakeb;
    private Badge noReplyb;
    private LinearLayout dianzhul;
    private LinearLayout collectl;
    private LinearLayout stepl;
    private LinearLayout quanl;
    private String chosetext;
    private LinearLayout gopass;

    private Dialog dialogchosetext;

    public VMineAdapter(Context context, LayoutHelper helper, View.OnClickListener listener) {
        this.context = context;
        this.helper = helper;
        this.listener = listener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_minesingle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setting = (ImageView) holder.itemView.findViewById(R.id.setting);
        tongzhi = (ImageView) holder.itemView.findViewById(R.id.tongzhi);
        head = (ImageView) holder.itemView.findViewById(R.id.head);

        shoucangnum = (TextView) holder.itemView.findViewById(R.id.shoucangnum);
        quannum = (TextView) holder.itemView.findViewById(R.id.quannum);


        name = (TextView) holder.itemView.findViewById(R.id.name);
        level = (TextView) holder.itemView.findViewById(R.id.level);
        code = (TextView) holder.itemView.findViewById(R.id.code);
        actIcon = (ImageView) holder.itemView.findViewById(R.id.act_icon);
        actContent = (TextView) holder.itemView.findViewById(R.id.act_content);
        actGo = (TextView) holder.itemView.findViewById(R.id.act_go);
        allOrder = (LinearLayout) holder.itemView.findViewById(R.id.all_order);
        myallorder = (TextView) holder.itemView.findViewById(R.id.myallorder);
        daifukuan = (TextView) holder.itemView.findViewById(R.id.daifukuan);
        daifahuo = (TextView) holder.itemView.findViewById(R.id.daifahuo);
        daishouhuo = (TextView) holder.itemView.findViewById(R.id.daishouhuo);
        yiwancheng = (TextView) holder.itemView.findViewById(R.id.yiwancheng);
        dianzhuquanyi2 = (TextView) holder.itemView.findViewById(R.id.dianzhuquanyi2);
        erweima = (TextView) holder.itemView.findViewById(R.id.erweima);
        lianxikefu = (TextView) holder.itemView.findViewById(R.id.lianxikefu);
        dizhi = (TextView) holder.itemView.findViewById(R.id.dizhi);
        mylessmoneyvalue = (TextView) holder.itemView.findViewById(R.id.mylessmoneyvalue);
        dianzhu2 = (TextView) holder.itemView.findViewById(R.id.dianzhu2);
        dianzhul = (LinearLayout) holder.itemView.findViewById(R.id.dianzhul);
        collectl = (LinearLayout) holder.itemView.findViewById(R.id.collectl);
        stepl = (LinearLayout) holder.itemView.findViewById(R.id.stepl);
        quanl = (LinearLayout) holder.itemView.findViewById(R.id.quanl);
        gopass=(LinearLayout) holder.itemView.findViewById(R.id.gopass);
        bindDataToView(AccountManager.sUserBean);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ShopSettingActivity.class));
            }
        });
        lianxikefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KfStartHelper((Activity) context).initSdkChat("e183f850-6650-11e9-b942-bf7a16e827df", "咨询", AccountManager.sUserBean.uid, 1315);//陈辰正式
            }
        });
        tongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                MobclickAgent.onEvent(mActivity,"C_personal_coupon_head");
                context.startActivity(new Intent(context, TongZhiActivity.class));
            }
        });
        allOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(v.getContext(), AllOrderActivity.class));
            }
        });
        daifukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("type", 1));
            }
        });
        daifahuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("type", 2));
            }
        });
        daishouhuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("type", 3));
            }
        });
        yiwancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("type", 4));
            }
        });

        erweima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, TuiGuangActivity.class));
            }
        });
        dizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, AddressListActivity.class));
            }
        });
        head.setOnClickListener(listener);
        quanl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, AllQuanActivity.class));
            }
        });
        collectl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ShouCangActivity.class));
            }
        });
        dianzhul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if("1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))){
                        context.startActivity(new Intent(context, FenXiaoMenuActivity.class));
                    }else{
                        context.startActivity(new Intent(context, FenXiaoNoActivity.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        gopass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals((AccountManager.sUserBean == null ? "0" : AccountManager.sUserBean.is_promoter))) {
//                    mylessmoneyvalue.setText(AccountManager.sUserBean.now_money);
//                    dianzhuquanyi2.setText("店主权益");
//                    actContent.setText(AccountManager.sUserBean.remind);
//                    actGo.setText("立即领取");

                    context.startActivity(new Intent(context, TuiGuangActivity.class));
                } else {
//                    mylessmoneyvalue.setText("加入即享");
//                    dianzhuquanyi2.setText("升级店主");
//                    actContent.setText(AccountManager.sUserBean.remind);
//                    actGo.setText("立即开通");

                    context.startActivity(new Intent(context, FenXiaoNoActivity.class));


                }
            }
        });


        dianzhuquanyi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if("1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))){
                        context.startActivity(new Intent(context, FenXiaoMenuActivity.class));
                    }else{
                        context.startActivity(new Intent(context, FenXiaoNoActivity.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StyledDialog.init(context);
                dialogchosetext = StyledDialog.buildNormalInput("个人信息修改", "输入昵称", "",
                        "确定", "取消",  new MyDialogListener() {
                            @Override
                            public void onFirst() {
                                change(chosetext);
                            }

                            @Override
                            public void onSecond() {

                            }

                            @Override
                            public void onGetInput(CharSequence input1, CharSequence input2) {
                                super.onGetInput(input1, input2);
                                chosetext=input1.toString();
                            }
                        }).setCancelable(true,true).show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return 1;
    }
    private void change(final String chosetext) {
        Map<String, String> map = new HashMap<>();
//        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("type","2");
        map.put("nickname",chosetext);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).changeNick(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        name.setText(chosetext);
                        AccountManager.sUserBean.nickname=chosetext;
                        Toast.makeText(context,"修改完成",Toast.LENGTH_SHORT).show();
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

    public void bindDataToView(UserBean sUserBean) {
        if (sUserBean != null) {
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

            name.setText(AccountManager.sUserBean.nickname);


            if ("1".equals((AccountManager.sUserBean == null ? "0" : AccountManager.sUserBean.is_promoter))) {
                mylessmoneyvalue.setText(AccountManager.sUserBean.now_money);
                dianzhuquanyi2.setText("店主权益");
                actContent.setText(AccountManager.sUserBean.remind);
                actGo.setText("立即领取");
            } else {
                mylessmoneyvalue.setText("加入即享");
                dianzhuquanyi2.setText("升级店主");
                actContent.setText(AccountManager.sUserBean.remind);
                actGo.setText("立即开通");
            }
            RequestOptions requestOptions = RequestOptions.circleCropTransform().error(R.drawable.head_defuat_circle);
            Glide.with(context).load((AccountManager.sUserBean.avatar.startsWith("http")) ? AccountManager.sUserBean.avatar : "http://" + AccountManager.sUserBean.avatar).apply(requestOptions).into(head);
            noBuyb = new QBadgeView(context).setBadgeGravity(Gravity.END | Gravity.TOP).bindTarget(daifukuan).setBadgeText(AccountManager.sUserBean.noBuy + "");
            noPostageb = new QBadgeView(context).setBadgeGravity(Gravity.END | Gravity.TOP).bindTarget(daifahuo).setBadgeText(AccountManager.sUserBean.noPostage + "");
            noTakeb = new QBadgeView(context).setBadgeGravity(Gravity.END | Gravity.TOP).bindTarget(daishouhuo).setBadgeText(AccountManager.sUserBean.noTake + "");
            noReplyb = new QBadgeView(context).setBadgeGravity(Gravity.END | Gravity.TOP).bindTarget(yiwancheng).setBadgeText(AccountManager.sUserBean.noReply + "");

            if (AccountManager.sUserBean.noBuy == 0) {
                noBuyb.hide(false);
            }
            if (AccountManager.sUserBean.noPostage == 0) {
                noPostageb.hide(false);

            }
            if (AccountManager.sUserBean.noTake == 0) {
                noTakeb.hide(false);

            }
            if (AccountManager.sUserBean.noReply == 0) {
                noReplyb.hide(false);

            }
            code.setText("邀请码："+AccountManager.sUserBean.uid);
            shoucangnum.setText(AccountManager.sUserBean.collect_number+"");
            quannum.setText(AccountManager.sUserBean.coupon_number+"");

        }
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
