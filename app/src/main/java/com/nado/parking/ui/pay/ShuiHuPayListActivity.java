package com.nado.parking.ui.pay;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fondesa.recyclerviewdivider.RecyclerViewDivider;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.nado.parking.R;
import com.nado.parking.adapter.recycler.RecyclerCommonAdapter;
import com.nado.parking.adapter.recycler.base.ViewHolder;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.bean.DianHuHao;
import com.nado.parking.bean.ShuiHuHao;
import com.nado.parking.bean.ShuiQianFei;
import com.nado.parking.manager.AccountManager;
import com.nado.parking.manager.RequestManager;
import com.nado.parking.net.RetrofitCallBack;
import com.nado.parking.net.RetrofitRequestInterface;
import com.nado.parking.util.DialogUtil;
import com.nado.parking.util.DisplayUtil;
import com.nado.parking.widget.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShuiHuPayListActivity extends BaseActivity {


    private android.widget.RelativeLayout rlLayoutTopBackBar;
    private android.widget.LinearLayout llLayoutTopBackBarBack;
    private android.widget.TextView tvLayoutTopBackBarTitle;
    private android.widget.TextView tvLayoutTopBackBarEnd;
    private android.widget.TextView tvLayoutBackTopBarOperate;
    private android.support.v7.widget.RecyclerView list;
    private RecyclerCommonAdapter<ShuiHuHao> myAdapter;
    private List<ShuiHuHao> mBeanList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_sdlist;
    }

    @Override
    public void initView() {


        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        list = (RecyclerView) findViewById(R.id.list);
        tvLayoutTopBackBarTitle.setText("选择户号缴费");
        tvLayoutTopBackBarEnd.setText("添加");
    }

    @Override
    public void initData() {
        StyledDialog.init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    private void getList() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("type", "水".equals(getIntent().getStringExtra("type")) ? "1" : "2");
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).getBindShuiPayCompany(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    mBeanList.clear();
                    if (code == 0) {
                        JSONArray jsonArray = res.getJSONObject("data").getJSONArray("info");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ShuiHuHao bean = new ShuiHuHao();
                            bean.id = jsonObject.optString("id");
                            bean.product_id = jsonObject.optString("product_id");
                            bean.uid = jsonObject.optString("uid");
                            bean.wecaccount = jsonObject.optString("wecaccount");
                            bean.add_time = jsonObject.optString("add_time");
                            bean.type = jsonObject.optString("type");
                            bean.productid = jsonObject.optString("productid");
                            bean.company = jsonObject.optString("company");
                            mBeanList.add(bean);
                        }

                    } else {
                    }

                    showRecycleView();
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    @Override
    public void initEvent() {
        tvLayoutTopBackBarEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, ShuiBindActivity.class).putExtra("type","水"));
            }
        });
    }
    private void showRecycleView() {
        if (myAdapter == null) {


            myAdapter = new RecyclerCommonAdapter<ShuiHuHao>(mActivity, R.layout.item_shuihuhao, mBeanList) {

                @Override
                protected void convert(ViewHolder holder, final ShuiHuHao carChoiceBean, int position) {
                    holder.setText(R.id.huhao, "户号:"+carChoiceBean.wecaccount);
                    holder.setImageResource(R.id.huimage,R.drawable.shuifei);
                    holder.setText(R.id.jgname, "缴费机构:"+carChoiceBean.company);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            DialogUtil.showUnCancelableProgress(mActivity, "查询中");
                            sendSearchOrder(carChoiceBean);
                        }
                    });
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            deleteHuHao(carChoiceBean);
                            return true;
                        }
                    });
                }

            };

            list.addItemDecoration(RecyclerViewDivider.with(this).color(Color.parseColor("#909090")).build());
            list.setAdapter(myAdapter);
            list.setLayoutManager(new LinearLayoutManager(mActivity));
        }else{

            myAdapter.notifyDataSetChanged();
        }
    }

    public long starttime=0;
    public void sendSearchOrder(final ShuiHuHao bean){

        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("id", bean.id);
        map.put("type", "水".equals(getIntent().getStringExtra("type")) ? "1" : "2");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).sendSearchShuiOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        String stockordernumber=res.getJSONObject("data").getString("stockordernumber");
                        starttime=System.currentTimeMillis();
                        chechNeedPay(bean,stockordernumber);

                    }else{
                        DialogUtil.hideProgress();
                        Toast.makeText(getBaseContext(),"查询不成功",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    DialogUtil.hideProgress();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {

            }
        });

    }


    private void deleteHuHao(final ShuiHuHao carChoiceBean) {
        StyledDialog.buildIosAlert("操作", "是否解除绑定该户号", new MyDialogListener() {
            @Override
            public void onFirst() {
                toDelete(carChoiceBean);
            }

            @Override
            public void onSecond() {

            }
        }).setBtnText("确定","取消").show();
    }

    private void toDelete(ShuiHuHao carChoiceBean) {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("id", carChoiceBean.id);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).deleteShui(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        getList();
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


    //需要先用一个下单id
    public void chechNeedPay(final ShuiHuHao hubean, final String stockordernumber){
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("id", hubean.id);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).chechNeedShuiPay(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    String Status = res.getString("Status");
                    if (Status.equals(Status)) {

                        JSONObject jsonObject=res.getJSONObject("Content");
                        String totalamount=jsonObject.optString("totalamount");

                        ShuiQianFei bean=new ShuiQianFei();

                        bean.totalamount=jsonObject.optString("totalamount");
                        bean.wecbalance=jsonObject.optString("wecbalance");
                        bean.useraddress=jsonObject.optString("useraddress");
                        bean.company=hubean.company;
                        bean.company_id=hubean.product_id;
                        bean.wecaccount=hubean.wecaccount;

                        Intent intent=new Intent(getBaseContext(),ShuiPayActivity.class);
                        buildIntent(intent,bean);
                        if (totalamount==null||"0".equals(totalamount)||"null".equals(totalamount)) {
                            //假装欠费
                            intent.putExtra("needpayflag", true);
                            intent.putExtra("totalamount","5");
//                            intent.putExtra("needpayflag",false);
                        }else{
                            intent.putExtra("needpayflag",true);
                        }
                        DialogUtil.hideProgress();
                        startActivity(intent);
                    }else{
                        DialogUtil.hideProgress();
                        if(System.currentTimeMillis()-starttime>10000){//说明查询失败多半第三方问题

                        }else{
                            chechNeedPay(hubean,stockordernumber);
                        }
                    }

                } catch (JSONException e) {
                    DialogUtil.hideProgress();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }


}
