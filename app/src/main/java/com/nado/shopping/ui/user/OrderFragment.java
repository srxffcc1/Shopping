package com.nado.shopping.ui.user;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nado.shopping.R;
import com.nado.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.nado.shopping.adapter.recycler.base.ViewHolder;
import com.nado.shopping.base.BaseFragment;
import com.nado.shopping.bean.Order;
import com.nado.shopping.bean.OrderEvent;
import com.nado.shopping.manager.AccountManager;
import com.nado.shopping.manager.RequestManager;
import com.nado.shopping.net.RetrofitCallBack;
import com.nado.shopping.net.RetrofitRequestInterface;
import com.nado.shopping.ui.main.PayAllReleaseActivity;
import com.nado.shopping.util.DialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderFragment extends BaseFragment {
    private android.support.v7.widget.RecyclerView rvFragmentHomeAll;
    private RecyclerCommonAdapter<Order> myAdapter;
    private List<Order> mBeanList = new ArrayList<>();

    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_order;
    }

    @Override
    public void initView() {

        rvFragmentHomeAll = (RecyclerView) findViewById(R.id.rv_fragment_home_all);
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        getOrderList(1);
    }

    private void getTestList(int page) {
        Map<String, String> map = new HashMap<>();
        if (AccountManager.sUserBean != null) {
            map.put("customer_id", AccountManager.sUserBean.getId());
            map.put("page", page + "");
            map.put("order_status", getArguments().getString("order_status"));
            map.put("limit", "10");
            RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getGoodOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
                @Override
                public void onSuccess(String response) {

                }

                @Override
                public void onError(Throwable t) {

                }
            });
        } else {

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(OrderEvent wechatPayEvent) {
        getOrderList(1);
        DialogUtil.hideProgress();
    }

    private void getOrderList(int page) {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("page", page + "");
        map.put("order_status", getArguments().getString("order_status"));
        map.put("limit", "10");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getGoodOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
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
                            Order bean = new Order();
                            bean.id = jsonObject.optString("id");
                            bean.out_trade_no = jsonObject.optString("out_trade_no");
                            bean.goods_id = jsonObject.optString("goods_id");
                            bean.goods_price = jsonObject.optString("goods_price");
                            bean.goods_all_price = jsonObject.optString("goods_all_price");
                            bean.goods_name = jsonObject.optString("goods_name");
                            bean.goods_number = jsonObject.optString("goods_number");
                            bean.goods_img = jsonObject.optString("goods_img");
                            bean.goods_format = jsonObject.optString("goods_format");
                            bean.goods_unit = jsonObject.optString("goods_unit");
                            bean.order_status = jsonObject.optString("order_status");
                            bean.pay_status = jsonObject.optString("pay_status");
                            bean.pay_type = jsonObject.optString("pay_type");
                            bean.pay_fee = jsonObject.optString("pay_fee");
                            bean.cutoff_time = jsonObject.optString("cutoff_time");
                            bean.auto_take_time = jsonObject.optString("auto_take_time");
                            bean.create_time = jsonObject.optString("create_time");
                            bean.pay_time = jsonObject.optString("pay_time");
                            bean.action = jsonObject.optString("action");
                            bean.order_status_text = jsonObject.optString("order_status_text");
                            mBeanList.add(bean);
                        }
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void cancelOrder(String order_id) {

        DialogUtil.showUnCancelableProgress(mActivity, "订单取消中");
        Map<String, String> map = new HashMap<>();
        if (AccountManager.sUserBean != null) {
            map.put("customer_id", AccountManager.sUserBean.getId());
            map.put("order_id", order_id);
            RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).cancelOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject res = new JSONObject(response);
                        int code = res.getInt("code");
                        String info = res.getString("info");
                        if (code == 0) {
                            EventBus.getDefault().post(new OrderEvent());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable t) {

                }
            });
        } else {

        }

    }

    @Override
    public void initEvent() {
    }

    private void showRecycleView() {
        if (myAdapter == null) {

            myAdapter = new RecyclerCommonAdapter<Order>(mActivity, R.layout.item_order, mBeanList) {
                @Override
                protected void convert(ViewHolder holder, final Order carChoiceBean, int position) {
                    holder.setText(R.id.order_status, carChoiceBean.action);

                    holder.setText(R.id.order_id, "订单号:"+carChoiceBean.out_trade_no);

                    holder.setText(R.id.order_count, "x"+carChoiceBean.goods_number);

                    holder.setText(R.id.order_money, ""+carChoiceBean.goods_price+"元");

                    holder.setText(R.id.order_date, ""+carChoiceBean.create_time);

                    holder.setText(R.id.order_name, ""+carChoiceBean.goods_name);


                    RequestOptions options = new RequestOptions()
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸

                    Glide.with(mActivity).load(carChoiceBean.goods_img).apply(options).into((ImageView) holder.getView(R.id.order_img));
                    if (Integer.parseInt(carChoiceBean.order_status) == 0) {
                        holder.itemView.findViewById(R.id.order_submit).setVisibility(View.VISIBLE);
                    } else {

                        holder.itemView.findViewById(R.id.order_submit).setVisibility(View.GONE);
                    }
                    if (Integer.parseInt(carChoiceBean.order_status) >= 5) {
                        holder.itemView.findViewById(R.id.order_cancel).setVisibility(View.GONE);
                    } else {

                        holder.itemView.findViewById(R.id.order_cancel).setVisibility(View.VISIBLE);
                    }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(mActivity,OrderDetailActivity.class).putExtra("order_id",carChoiceBean.id));
                        }
                    });
                    holder.setOnClickListener(R.id.order_cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelOrder(carChoiceBean.id);
                        }
                    });

                    holder.setOnClickListener(R.id.order_submit, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String order_id = carChoiceBean.id;//获得的本司订单号
                            String paytypekey = "pay_type";
                            String url = "index.php?g=app&m=appv1&a=goodsPay";
                            Map<String, String> postmap = new HashMap<>();
                            postmap.put("customer_id",AccountManager.sUserBean.getId());
                            postmap.put("url",url);
                            postmap.put("paymm",carChoiceBean.goods_all_price);
                            postmap.put("paytypekey",paytypekey);
                            postmap.put("order_id",order_id);
                            PayAllReleaseActivity.open(mActivity, postmap);//打开充值界面 选择支付类型 然后会访问url交换对应的sign或appid来完成充值
                        }
                    });


                }

            };

//            rvFragmentHomeAll.addItemDecoration(RecyclerViewDivider.with(getActivity()).color(Color.parseColor("#909090")).build());
            rvFragmentHomeAll.setAdapter(myAdapter);
            rvFragmentHomeAll.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {
            myAdapter.notifyDataSetChanged();
        }
    }

}
