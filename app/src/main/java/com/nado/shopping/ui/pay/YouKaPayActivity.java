package com.nado.shopping.ui.pay;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.nado.shopping.R;
import com.nado.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.nado.shopping.adapter.recycler.base.ViewHolder;
import com.nado.shopping.base.BaseActivity;
import com.nado.shopping.bean.SupportMoney;
import com.nado.shopping.manager.AccountManager;
import com.nado.shopping.manager.RequestManager;
import com.nado.shopping.net.RetrofitCallBack;
import com.nado.shopping.net.RetrofitRequestInterface;
import com.nado.shopping.ui.main.PayAllActivity;
import com.nado.shopping.util.DisplayUtil;
import com.nado.shopping.widget.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YouKaPayActivity extends BaseActivity {

    private static final String TAG = "YouKaPayActivity";
    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private TwinklingRefreshLayout tflActivityParkLot;
    private LinearLayout needchange;
    private TextView youkaadd;
    private RecyclerView rvPayAll;
    private List<SupportMoney> mCarChoiceList = new ArrayList<>();
    private RecyclerCommonAdapter<SupportMoney> mCarBeanAdapter;
    private String card_number="";
    private String card_id="";
    private TextView tvLayoutTopBackBarStart;
    private TextView bindcard;
    private android.widget.ImageView jiayoukaimg;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ykpay;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        needchange = (LinearLayout) findViewById(R.id.needchange);
        youkaadd = (TextView) findViewById(R.id.youkaadd);
        rvPayAll = (RecyclerView) findViewById(R.id.rv_pay_all);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        bindcard = (TextView) findViewById(R.id.bindcard);
        jiayoukaimg = (ImageView) findViewById(R.id.jiayoukaimg);
        tvLayoutTopBackBarTitle.setText("油卡充值");
    }

    @Override
    public void initData() {
        checkHasBindYouKa();
    }

    private void checkHasBindYouKa() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).checkHasBindYouKa(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    mCarChoiceList.clear();
                    if (code == 0) {
                        card_number=res.getJSONObject("data").getJSONObject("card").getString("card_number");
                        card_id=res.getJSONObject("data").getJSONObject("card").getString("id");
                        youkaadd.setText("");
                        bindcard.setText("绑定的卡号："+card_number);
//                        youkaadd.setBackgroundResource(R.drawable.jiayouka);
                        youkaadd.setVisibility(View.GONE);
                    }else{
                        jiayoukaimg.setVisibility(View.GONE);
                        passToBindYouKa();
                    }
                    JSONArray data = res.getJSONObject("data").getJSONArray("money_list");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject dataItem = data.getJSONObject(i);
                        SupportMoney bean = new SupportMoney();
                        bean.proid=dataItem.getString("proid");
                        bean.money=dataItem.getString("money");
                        bean.cardnum=dataItem.getString("cardnum");

                        mCarChoiceList.add(bean);
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



    private void passToBindYouKa() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("page", "1");
        map.put("limit", "10");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getYouKaList(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        JSONArray jsonArray=res.getJSONArray("data");
                        if(jsonArray.length()>0){

                            passToChangeYouKa();
                        }else{
                            passToAddYouKa();
                        }
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

    private void passToChangeYouKa() {
        startActivityForResult(new Intent(getBaseContext(),YouKaListActivity.class),100);
    }

    private void passToAddYouKa() {
        startActivityForResult(new Intent(getBaseContext(),YouKaAddActivity.class),200);
    }

    @Override
    public void initEvent() {
        youkaadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passToBindYouKa();
            }
        });
        jiayoukaimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passToBindYouKa();
            }
        });
    }

    private void showRecycleView() {
        if (mCarBeanAdapter == null) {


            mCarBeanAdapter = new RecyclerCommonAdapter<SupportMoney>(mActivity, R.layout.item_hfpay, mCarChoiceList) {

                @Override
                protected void convert(ViewHolder holder, final SupportMoney carChoiceBean, int position) {
                    holder.setText(R.id.price, "售价："+carChoiceBean.money + "元");
                    holder.setText(R.id.old_price, carChoiceBean.money + "元");
//                    new GlideImageLoader().displayImage(mActivity,carChoiceBean.picture, (ImageView) holder.getView(R.id.picture));
//                    new GlideImageLoader().displayImage(mActivity,carChoiceBean.picture, (ImageView) holder.getView(R.id.picture));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendOrder(carChoiceBean);
                        }
                    });


                }

            };


            rvPayAll.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, (int) DisplayUtil.dpToPx(mActivity, 1), ContextCompat.getColor(mActivity, R.color.colorLine), false, 2));
            rvPayAll.setAdapter(mCarBeanAdapter);
            rvPayAll.setLayoutManager(new GridLayoutManager(mActivity, 3));
        } else {

            mCarBeanAdapter.notifyDataSetChanged();
        }

    }

    private void sendOrder(final SupportMoney carChoiceBean) {
        Map<String, String> map = new HashMap<>();
        map.put("card_id", card_id);
        map.put("proid", carChoiceBean.proid);
        map.put("cardnum", carChoiceBean.cardnum);
        map.put("order_amount", carChoiceBean.money);
        map.put("goods_amount", carChoiceBean.money);
        map.put("customer_id", AccountManager.sUserBean.getId());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).sendYouKaOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        String orderid = res.getJSONObject("data").optString("order_id");//获得的本司订单号
                        String paytypekey = "pay_type";
                        String pervalue = carChoiceBean.money;//充值金额
                        String url = "index.php?g=app&m=petrol&a=goodsPay";
                        Map<String, String> postmap = new HashMap<>();
                        postmap.put("paymm",pervalue);
                        postmap.put("url",url);
                        postmap.put("paytypekey",paytypekey);
                        postmap.put("order_id",orderid);
                        postmap.put("customer_id", AccountManager.sUserBean.getId());
                        PayAllActivity.open(YouKaPayActivity.this, postmap);//打开充值界面 选择支付类型 然后会访问url交换对应的sign或appid来完成充值
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
