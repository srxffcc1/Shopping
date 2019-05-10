package com.jiudi.shopping.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fondesa.recyclerviewdivider.RecyclerViewDivider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.CartInfo;
import com.jiudi.shopping.bean.CartStatus;
import com.jiudi.shopping.bean.Order;
import com.jiudi.shopping.bean.OrderEvent;
import com.jiudi.shopping.bean.Quan;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.cart.DingDanActivity;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QuanFragment extends BaseFragment {
    private RecyclerView rvFragmentHomeAll;
    private RecyclerCommonAdapter<Quan> myAdapter;
    private List<Quan> mBeanList = new ArrayList<>();

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
//        EventBus.getDefault().register(this);
        getOrderList();
    }

    private void getTestList(int page) {
//        Map<String, String> map = new HashMap<>();
//        if (AccountManager.sUserBean != null) {
////            map.put("customer_id", AccountManager.sUserBean.getId());
//            map.put("page", page + "");
//            map.put("order_status", getArguments().getString("order_status"));
//            map.put("limit", "10");
//            RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getGoodOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
//                @Override
//                public void onSuccess(String response) {
//
//                }
//
//                @Override
//                public void onError(Throwable t) {
//
//                }
//            });
//        } else {
//
//        }

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRefreshEvent(OrderEvent wechatPayEvent) {
//        getOrderList(0);
//        DialogUtil.hideProgress();
//    }

    private void getOrderList() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getQuan(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    mBeanList.clear();
                    if (code == 200) {
                        JSONArray jsonArray = res.getJSONObject("data").getJSONArray("couponList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Quan bean = new Quan();
                            bean.id=jsonObject.optString("id");
                            bean.cid=jsonObject.optString("cid");
                            bean.uid=jsonObject.optString("uid");
                            bean.coupon_title=jsonObject.optString("coupon_title");
                            bean.coupon_price=jsonObject.optString("coupon_price");
                            bean.use_min_price=jsonObject.optString("use_min_price");
                            bean.add_time=jsonObject.optString("add_time");
                            bean.end_time=jsonObject.optString("end_time");
                            bean.use_time=jsonObject.optString("use_time");
                            bean.type=jsonObject.optString("type");
                            bean.status=jsonObject.optString("status");
                            bean.is_fail=jsonObject.optString("is_fail");
                            bean._add_time=jsonObject.optString("_add_time");
                            bean._end_time=jsonObject.optString("_end_time");
                            bean._type=jsonObject.optString("_type");
                            bean._msg=jsonObject.optString("_msg");
                            if("全部".equals(getArguments().getString("type"))||bean.status.equals(getArguments().getString("type"))){
                                mBeanList.add(bean);
                            }
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
//        EventBus.getDefault().unregister(this);
    }

    private void cancelOrder(String order_id) {

//        DialogUtil.showUnCancelableProgress(mActivity, "订单取消中");
//        Map<String, String> map = new HashMap<>();
//        if (AccountManager.sUserBean != null) {
////            map.put("customer_id", AccountManager.sUserBean.getId());
//            map.put("order_id", order_id);
//            RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).cancelOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
//                @Override
//                public void onSuccess(String response) {
//                    try {
//                        JSONObject res = new JSONObject(response);
//                        int code = res.getInt("code");
//                        String info = res.getString("info");
//                        if (code == 0) {
//                            EventBus.getDefault().post(new OrderEvent());
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onError(Throwable t) {
//
//                }
//            });
//        } else {
//
//        }

    }

    @Override
    public void initEvent() {
    }

    private void showRecycleView() {
        if (myAdapter == null) {

            myAdapter = new RecyclerCommonAdapter<Quan>(mActivity, R.layout.item_quan, mBeanList) {
                @Override
                protected void convert(ViewHolder holder, final Quan carChoiceBean, int position) {
                    LinearLayout quanboder1=holder.getView(R.id.quanboder1);
                    LinearLayout quanboder2=holder.getView(R.id.quanboder2);
                    holder.setText(R.id.min,"满"+carChoiceBean.use_min_price+"元可用现金券");
                    holder.setText(R.id.time,carChoiceBean._add_time+"至"+carChoiceBean._end_time);
                    holder.setText(R.id.money,"¥"+carChoiceBean.coupon_price);
                    if("0".equals(carChoiceBean.status)){
                        holder.setText(R.id.status,"可使用");
                        quanboder1.setBackgroundResource(R.drawable.card_bg_r);
                        quanboder2.setBackgroundResource(R.drawable.text_boder_quan_w);
                    }
                    if("1".equals(carChoiceBean.status)){
                        holder.setText(R.id.status,"已使用");
                        quanboder1.setBackgroundResource(R.drawable.card_bg_p);
                        quanboder2.setBackgroundResource(R.drawable.text_boder_quan_r);
                        holder.setBackgroundRes(R.id.status,R.drawable.text_boder_quan_r);
                        holder.setTextColor(R.id.min,Color.parseColor("#E9391C"));
                        holder.setTextColor(R.id.time,Color.parseColor("#E9391C"));
                        holder.setTextColor(R.id.money,Color.parseColor("#E9391C"));
                        holder.setTextColor(R.id.status,Color.parseColor("#E9391C"));

                    }
                    if("2".equals(carChoiceBean.status)){
                        holder.setText(R.id.status,"已过期");
                        quanboder1.setBackgroundResource(R.drawable.card_bg_g);
                        quanboder2.setBackgroundResource(R.drawable.text_boder_quan_w);

                    }
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
