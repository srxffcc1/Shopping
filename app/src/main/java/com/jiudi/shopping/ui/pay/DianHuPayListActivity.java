package com.jiudi.shopping.ui.pay;

import android.content.Intent;
import android.graphics.Color;
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
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.DianHuHao;
import com.jiudi.shopping.bean.DianQianFei;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.DialogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DianHuPayListActivity extends BaseActivity {


    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private RecyclerView list;
    private RecyclerCommonAdapter<DianHuHao> myAdapter;
    private List<DianHuHao> mBeanList = new ArrayList<>();

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
        map.put("userid", AccountManager.sUserBean.getId());
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).getBindDianPayCompany(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    mBeanList.clear();
                    if (code == 0) {
                        JSONArray jsonArray = res.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            DianHuHao bean = new DianHuHao();
                            bean.id = jsonObject.optString("id");
                            bean.user_id = jsonObject.optString("user_id");
                            bean.company = jsonObject.optString("company");
                            bean.wecacount = jsonObject.optString("wecacount");
                            bean.productid = jsonObject.optString("productid");
                            bean.status = jsonObject.optString("status");
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
                startActivity(new Intent(mActivity, DianBindActivity.class).putExtra("type","电"));
            }
        });
    }

    private void showRecycleView() {
        if (myAdapter == null) {


            myAdapter = new RecyclerCommonAdapter<DianHuHao>(mActivity, R.layout.item_shuihuhao, mBeanList) {

                @Override
                protected void convert(ViewHolder holder, final DianHuHao carChoiceBean, int position) {
                    holder.setText(R.id.huhao, "户号:"+carChoiceBean.wecacount);
                    holder.setImageResource(R.id.huimage,R.drawable.dianfei);
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
        } else {

            myAdapter.notifyDataSetChanged();
        }
    }

    private void deleteHuHao(final DianHuHao carChoiceBean) {
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

    private void toDelete(DianHuHao carChoiceBean) {
        Map<String, String> map = new HashMap<>();
        map.put("userid", AccountManager.sUserBean.getId());
        map.put("id", carChoiceBean.id);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).deleteDian(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
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

    public long starttime = 0;



    public void sendSearchOrder(final DianHuHao bean) {
        Map<String, String> map = new HashMap<>();
        map.put("company", bean.company);
        map.put("wecaccount", bean.wecacount);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).sendSearchDianOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        String stockordernumber=res.getJSONObject("data").getString("stockordernumber");
                        chechNeedPay(bean,stockordernumber);
                    }else{

                        DialogUtil.hideProgress();
                        Toast.makeText(getBaseContext(), "查询不成功", Toast.LENGTH_SHORT).show();
                    }
//                    if (Status) {
//                        starttime = System.currentTimeMillis();
//                        chechNeedPay(bean);
//                    } else {
//                        Toast.makeText(getBaseContext(), "查询不成功", Toast.LENGTH_SHORT).show();
//                    }

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

    //需要先用一个下单id
    public void chechNeedPay(final DianHuHao hubean,String stockordernumber) {
        Map<String, String> map = new HashMap<>();
        map.put("order", stockordernumber);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).chechNeedDianiPay(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {

                        JSONObject jsonObject = res.getJSONObject("data");
                        String totalamount = jsonObject.optString("totalamount");

                        DianQianFei bean = new DianQianFei();

                        bean.esupordernumber = jsonObject.optString("esupordernumber");
                        bean.failmessage = jsonObject.optString("failmessage");
                        bean.finishedtime = jsonObject.optString("finishedtime");
                        bean.searchstatus = jsonObject.optString("searchstatus");
                        bean.stockordernumber = jsonObject.optString("stockordernumber");
                        bean.totalamount = jsonObject.optString("totalamount");
                        bean.useraddress = jsonObject.optString("useraddress");
                        bean.wecbalance = jsonObject.optString("wecbalance");

                        Intent intent = new Intent(getBaseContext(), DianPayActivity.class);
                        buildIntent(intent, bean);
                        if(jsonObject.optJSONObject("wecbilldata")!=null){
                            intent.putExtra("delayfee",jsonObject.optJSONObject("wecbilldata").getString("delayfee"));
                            intent.putExtra("wecbillmoney",jsonObject.optJSONObject("wecbilldata").getString("wecbillmoney"));
                        }
                        intent.putExtra("productid",hubean.productid);
                        intent.putExtra("company",hubean.company);
                        intent.putExtra("wecaccount",hubean.wecacount);
                        intent.putExtra("totalamount",bean.totalamount);
                        if (totalamount==null||"0".equals(totalamount)||"null".equals(totalamount)) {

                            //假装欠费
                            intent.putExtra("delayfee","5");
                            intent.putExtra("wecbillmoney","5");
                            intent.putExtra("needpayflag", true);
                            intent.putExtra("totalamount","5");


//                            intent.putExtra("needpayflag", false);
                        } else {
                            intent.putExtra("needpayflag", true);
                        }

                        DialogUtil.hideProgress();
                        startActivity(intent);
                    } else {

                        DialogUtil.hideProgress();
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
