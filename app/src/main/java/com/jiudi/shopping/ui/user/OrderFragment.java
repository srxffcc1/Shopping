package com.jiudi.shopping.ui.user;

import android.content.Intent;
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
import com.jiudi.shopping.bean.CartAttrValue;
import com.jiudi.shopping.bean.CartInfo;
import com.jiudi.shopping.bean.Order;
import com.jiudi.shopping.bean.OrderEvent;
import com.jiudi.shopping.bean.CartStatus;
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
        getOrderList(0);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(OrderEvent wechatPayEvent) {
        getOrderList(0);
        DialogUtil.hideProgress();
    }

    private void getOrderList(int page) {
        Map<String, String> map = new HashMap<>();
//        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("first", page + "");
        map.put("type", getArguments().getString("type"));
        map.put("limit", "10");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getGoodOrder(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    mBeanList.clear();
                    if (code == 200) {
                        JSONArray jsonArray = res.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Order bean = new Order();
                            bean.combination_id=jsonObject.optString("combination_id");
                            bean.id=jsonObject.optString("id");
                            bean.order_id=jsonObject.optString("order_id");
                            bean.pay_price=jsonObject.optString("pay_price");
                            bean.total_num=jsonObject.optString("total_num");
                            bean.total_price=jsonObject.optString("total_price");
                            bean.pay_postage=jsonObject.optString("pay_postage");
                            bean.total_postage=jsonObject.optString("total_postage");
                            bean.paid=jsonObject.optString("paid");
                            bean.status=jsonObject.optString("status");
                            bean.refund_status=jsonObject.optString("refund_status");
                            bean.pay_type=jsonObject.optString("pay_type");
                            bean.coupon_price=jsonObject.optString("coupon_price");
                            bean.deduction_price=jsonObject.optString("deduction_price");
                            bean.pink_id=jsonObject.optString("pink_id");
                            bean.delivery_type=jsonObject.optString("delivery_type");
                            bean.combination_id=jsonObject.optString("combination_id");



                            bean.user_address=jsonObject.optString("user_address");

                            bean.real_name=jsonObject.optString("real_name");

                            bean.user_phone=jsonObject.optString("user_phone");


                            GsonBuilder builder = new GsonBuilder();
                            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                    return new Date(json.getAsJsonPrimitive().getAsLong());
                                }
                            });
                            Gson gson = builder.create();
                            Type cartStatusType = new TypeToken<CartStatus>() {
                            }.getType();
                            Type cartInfoType = new TypeToken<CartInfo>() {
                            }.getType();
                            String cartStatuss=jsonObject.getJSONObject("_status").toString();
                            CartStatus cartStatus=gson.fromJson(cartStatuss,cartStatusType);
                            bean.setStatuz(cartStatus);

                            JSONObject cartInfoObj=jsonObject.getJSONObject("cartInfo");
                            Iterator iterator = cartInfoObj.keys();
                            while(iterator.hasNext()){
                                String key = iterator.next() + "";
                                CartInfo cartInfo=gson.fromJson(cartInfoObj.getJSONObject(key).toString(),cartInfoType);
                                bean.addCartInfo(cartInfo);
                            }

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

            myAdapter = new RecyclerCommonAdapter<Order>(mActivity, R.layout.item_order, mBeanList) {
                @Override
                protected void convert(ViewHolder holder, final Order carChoiceBean, int position) {
                    LinearLayout linearLayout=holder.itemView.findViewById(R.id.allcart);
                    String cartlist=new Gson().toJson(carChoiceBean.getCartInfoList());
                    buildCartList(linearLayout,cartlist);
                    String title=carChoiceBean.getStatuz().get_title();
                    holder.setText(R.id.order_number,"订单："+carChoiceBean.getOrder_id());
                    holder.setText(R.id.summoney,"商品总价：¥"+carChoiceBean.getTotal_price()+"");
                    holder.setText(R.id.title,carChoiceBean.getStatuz().get_title());
                    TextView function_0=holder.itemView.findViewById(R.id.function_0);
                    final TextView function_1=holder.itemView.findViewById(R.id.function_1);
                    if("未支付".equals(title)){
                        function_0.setVisibility(View.GONE);
                        function_1.setVisibility(View.VISIBLE);
                        function_1.setText("立即付款");
                    }
                    if("未发货".equals(title)){

                        function_0.setVisibility(View.GONE);
                        function_1.setVisibility(View.GONE);
                        function_1.setText("立即付款");
                    }
                    if("待收货".equals(title)){
                        function_0.setVisibility(View.GONE);
                        function_1.setVisibility(View.GONE);
                        function_1.setText("立即付款");

                    }
                    if("待评价".equals(title)){
                        function_0.setVisibility(View.VISIBLE);
                        function_1.setVisibility(View.VISIBLE);
                        function_0.setText("立即评价");
                        function_1.setText("再来一单");
                    }
//                    function_1.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if("立即付款".equals(function_1.getText().toString())){
//                                startActivity(new Intent(mActivity, DingDanActivity.class).putExtra("bean",carChoiceBean));
//                            }
//                        }
//                    });
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(mActivity, DingDanActivity.class).putExtra("bean",carChoiceBean));
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
    private void buildCartList(ViewGroup viewGroup,String cartInfoListString) {
        try {
            JSONArray cartInfoList = new JSONArray(cartInfoListString);
            viewGroup.removeAllViews();
            for (int i = 0; i < cartInfoList.length(); i++) {
                View cartinfol = LayoutInflater.from(mActivity).inflate(R.layout.item_cart_item, viewGroup, false);
                JSONObject cartInfo = cartInfoList.getJSONObject(i);
                ImageView imageView = cartinfol.findViewById(R.id.cart_icon2);
                TextView cart_title = cartinfol.findViewById(R.id.cart_title);
                TextView cart_cunk = cartinfol.findViewById(R.id.cart_cunk);
                TextView cart_count = cartinfol.findViewById(R.id.cart_count);
                TextView cart_money = cartinfol.findViewById(R.id.cart_money);
                String pic = cartInfo.getJSONObject("productInfo").getString("image");
                String title = cartInfo.getJSONObject("productInfo").getString("store_name");
                try {
                    String cunk = cartInfo.getJSONObject("productInfo").getJSONObject("attrInfo").getString("suk");
                    cart_cunk.setText(cunk);
                } catch (JSONException e) {
                    cart_cunk.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
                String count = "X" + cartInfo.getString("cart_num");
                String money = "¥" + cartInfo.getJSONObject("productInfo").getString("price");
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
                Glide.with(mActivity).load(pic).apply(options).into(imageView);
                cart_title.setText(title);
                cart_count.setText(count);
                cart_money.setText(money);
                viewGroup.addView(cartinfol);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
