package com.jiudi.shopping.ui.user.account;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.ShouCang;
import com.jiudi.shopping.bean.TongZhi;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.cart.CartDetailActivity;
import com.jiudi.shopping.util.DisplayUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.widget.DividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShouCangActivity extends BaseActivity {
    private List<ShouCang> mCarChoiceList = new ArrayList<>();
    private RecyclerCommonAdapter<ShouCang> mCarBeanAdapter;
    private android.support.v7.widget.RecyclerView recycler;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_shoucang;
    }

    @Override
    public void initView() {

        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    public void initData() {
        getList();
    }

    @Override
    public void initEvent() {

    }
    private void getList() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getShouCang(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    mCarChoiceList.clear();
                    if (code == 200) {
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                return new Date(json.getAsJsonPrimitive().getAsLong());
                            }
                        });
                        Gson gson = builder.create();
                        Type cartStatusType = new TypeToken<List<ShouCang>>() {
                        }.getType();
                        mCarChoiceList.addAll((Collection<? extends ShouCang>) gson.fromJson(res.getJSONArray("data").toString(),cartStatusType));
                        showCarChoiceRecycleView();
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
    private void showCarChoiceRecycleView() {
        if (mCarBeanAdapter == null) {


            mCarBeanAdapter = new RecyclerCommonAdapter<ShouCang>(mActivity, R.layout.item_shoucang_list, mCarChoiceList) {

                @Override
                protected void convert(ViewHolder holder, final ShouCang carChoiceBean, int position) {
                    RequestOptions options = new RequestOptions()
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
                    Glide.with(mActivity).load(carChoiceBean.image).apply(options).into((ImageView) holder.getView(R.id.image));
                    holder.setText(R.id.title,carChoiceBean.store_name);
                    holder.setText(R.id.money,carChoiceBean.price);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(mActivity, CartDetailActivity.class).putExtra("id",carChoiceBean.pid));
                        }
                    });
                    holder.setOnClickListener(R.id.deleteshoucang, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            unshoucang(carChoiceBean.pid);
                        }
                    });
                }

            };


            recycler.addItemDecoration(RecyclerViewDivider.with(this).color(Color.parseColor("#909090")).build());
            recycler.setAdapter(mCarBeanAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {

            mCarBeanAdapter.notifyDataSetChanged();
        }

    }
    private void unshoucang(String id) {
        Map<String, String> map = new HashMap<>();
        map.put("productId", id);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).rmshoucang(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        getList();
                        Toast.makeText(mActivity,"取消收藏",Toast.LENGTH_SHORT).show();
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
