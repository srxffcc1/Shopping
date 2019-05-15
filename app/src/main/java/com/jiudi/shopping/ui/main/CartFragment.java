package com.jiudi.shopping.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.jiudi.shopping.event.FlashEvent;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.cart.PayDingDanActivity;
import com.jiudi.shopping.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车
 */
public class CartFragment extends BaseFragment {


    private RecyclerView recycler;
    private RecyclerCommonAdapter<CartInfo> myAdapter;
    private List<CartInfo> mBeanList = new ArrayList<>();
    private android.widget.TextView topay;
    private android.widget.CheckBox allcheck;
    private TextView heji;
    private TextView guanli;
    private String old1;
    private String old2;

    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_cart;
    }

    @Override
    public void initView() {

        recycler = (RecyclerView) findViewById(R.id.recycler);
        topay = (TextView) findViewById(R.id.topay);
        allcheck = (CheckBox) findViewById(R.id.allcheck);
        heji = (TextView) findViewById(R.id.heji);

        guanli = (TextView) findViewById(R.id.guanli);
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        getShopList();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFlashEvent(FlashEvent wechatPayEvent) {
        getShopList();
    }

    @Override
    public void initEvent() {
        guanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("管理".equals(guanli.getText().toString())){
                    changeWanCheng();
                }else{
                    changeGuanLi();
                }
            }
        });

        topay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("完成".equals(guanli.getText().toString())){
                    String result="";
                    for (int i = 0; i <mBeanList.size() ; i++) {
                        CartInfo bean=mBeanList.get(i);
                        if(bean.isIscheck()){
                            result+=bean.getId()+",";
                        }
                    }
                    deleteCart(result);
                }else{
                    String result="";
                    for (int i = 0; i <mBeanList.size() ; i++) {
                        CartInfo bean=mBeanList.get(i);
                        if(bean.isIscheck()){
                            result+=bean.getId()+",";
                        }
                    }
                    if(result.length()>1){
                        result=result.substring(0,result.length()-1);
                        toPay(result);
                    }else{
                        Toast.makeText(mActivity,"请选择商品",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        allcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < mBeanList.size(); i++) {
                    CartInfo bean=mBeanList.get(i);
                    bean.setIscheck(isChecked);
                }

                showRecycleView();
            }
        });
    }

    private void changeGuanLi() {
        guanli.setText("管理");
        sumMoney();
    }

    private void changeWanCheng() {
        guanli.setText("完成");
        topay.setText("删除");
        heji.setText("");
    }

    public void toPay(String cartId){
        startActivity(new Intent(mActivity, PayDingDanActivity.class).putExtra("cartId",cartId));
    }
    private void getShopList() {
        mBeanList.clear();
        Map<String, String> map = new HashMap<>();
//        map.put("customer_id", AccountManager.sUserBean.getId());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getShopList(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                return new Date(json.getAsJsonPrimitive().getAsLong());
                            }
                        });
                        Gson gson = builder.create();
                        Type cartshoplisttype = new TypeToken<List<CartInfo>>() {
                        }.getType();
                        String s=res.getJSONObject("data").getJSONArray("valid").toString();
                        mBeanList.addAll((Collection<? extends CartInfo>) gson.fromJson(s,cartshoplisttype));
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
    double sum=0.0;
    int selectnum=0;
    public void sumMoney(){
        if("完成".equals(guanli.getText().toString())){

        }else{
            sum=0.0;
            selectnum=0;
            for (int i = 0; i <mBeanList.size() ; i++) {
                CartInfo carChoiceBean=mBeanList.get(i);
                if(carChoiceBean.isIscheck()){
                    sum+=Double.parseDouble(carChoiceBean.getTruePrice())*carChoiceBean.getCart_num();
                    selectnum++;
                }

            }
            heji.setText("合计:¥"+sum);
            topay.setText("去结算("+selectnum+")");
        }

    }
    private void showRecycleView() {
        if (myAdapter == null) {


            myAdapter = new RecyclerCommonAdapter<CartInfo>(mActivity, R.layout.item_cart_list, mBeanList) {

                @Override
                protected void convert(final ViewHolder holder, final CartInfo carChoiceBean, int position) {
                    holder.setText(R.id.title,carChoiceBean.getProductInfo().getStore_name());
                    try {
                        holder.setText(R.id.fuwu,carChoiceBean.getProductInfo().getAttrInfo().getSuk());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    holder.setChecked(R.id.check,carChoiceBean.isIscheck());
                    holder.setOnCheckedChangeListener(R.id.check, new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            carChoiceBean.setIscheck(isChecked);
                            sumMoney();
                        }
                    });
                    holder.setText(R.id.money,"¥"+carChoiceBean.getTruePrice());
                    holder.setText(R.id.count,carChoiceBean.getCart_num()+"");
                    RequestOptions options = new RequestOptions()
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
                    Glide.with(mActivity).load(carChoiceBean.getProductInfo().getImage()).apply(options).into((ImageView) holder.getView(R.id.image));
                    holder.setOnClickListener(R.id.add, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            carChoiceBean.setCart_num(carChoiceBean.getCart_num()+1);
                            holder.setText(R.id.count,carChoiceBean.getCart_num()+"");
                            sumMoney();
                            sendChangeNum(carChoiceBean.getId(),carChoiceBean.getCart_num());
                        }
                    });
                    holder.setOnClickListener(R.id.sub, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(carChoiceBean.getCart_num()!=1){
                                carChoiceBean.setCart_num(carChoiceBean.getCart_num()-1);
                                holder.setText(R.id.count,carChoiceBean.getCart_num()+"");
                                sumMoney();
                                sendChangeNum(carChoiceBean.getId(), carChoiceBean.getCart_num());
                            }
                        }
                    });
                }

            };

            recycler.addItemDecoration(RecyclerViewDivider.with(getActivity()).size(10).color(Color.parseColor("#E9E8ED")).build());
            recycler.setAdapter(myAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        }else{

            myAdapter.notifyDataSetChanged();
        }
        sumMoney();
    }

    private void sendChangeNum(int id, int cart_num) {
        Map<String, String> map = new HashMap<>();
        map.put("cartId", id+"");
        map.put("cartNum", cart_num+"");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).changecartNum(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
//                        getShopList();
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
    private void deleteCart(String ids){
        Map<String, String> map = new HashMap<>();
        map.put("ids", ids);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).deleteCart(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        getShopList();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                changeGuanLi();
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }
}
