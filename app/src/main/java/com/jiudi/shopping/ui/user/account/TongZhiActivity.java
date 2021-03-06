package com.jiudi.shopping.ui.user.account;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

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
import com.jiudi.shopping.bean.CartStatus;
import com.jiudi.shopping.bean.MainGodsBean;
import com.jiudi.shopping.bean.TongZhi;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.cart.CartDetailActivity;
import com.jiudi.shopping.util.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TongZhiActivity extends BaseActivity {
    private android.support.v7.widget.RecyclerView recycler;
    private List<TongZhi> mCarChoiceList = new ArrayList<>();
    private RecyclerCommonAdapter<TongZhi> mCarBeanAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_tongzhi;
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
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getTongZhi(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
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
                        Type cartStatusType = new TypeToken<List<TongZhi>>() {
                        }.getType();
                        mCarChoiceList=gson.fromJson(res.getJSONObject("data").getJSONArray("list").toString(),cartStatusType);
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


            mCarBeanAdapter = new RecyclerCommonAdapter<TongZhi>(mActivity, R.layout.item_tongzhi, mCarChoiceList) {

                @Override
                protected void convert(ViewHolder holder, final TongZhi carChoiceBean, int position) {
                    holder.setText(R.id.title,carChoiceBean.content);
                    holder.setText(R.id.laizi,carChoiceBean.title);
                    holder.setText(R.id.riqi,carChoiceBean.add_time);
                }

            };


            recycler.addItemDecoration(RecyclerViewDivider.with(this).color(Color.parseColor("#909090")).build());
//            recycler.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, (int) DisplayUtil.dpToPx(mActivity, 1), ContextCompat.getColor(mActivity, R.color.colorLine), false, 2));
            recycler.setAdapter(mCarBeanAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {

            mCarBeanAdapter.notifyDataSetChanged();
        }

    }
}
