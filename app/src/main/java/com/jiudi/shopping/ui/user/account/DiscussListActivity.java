package com.jiudi.shopping.ui.user.account;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.CartDiscussBean;
import com.jiudi.shopping.bean.Discuss;
import com.jiudi.shopping.bean.TongZhi;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.DisplayUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.widget.DividerItemDecoration;
import com.jiudi.shopping.widget.KRatingBar;
import com.jiudi.shopping.widget.NoScrollGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscussListActivity extends BaseActivity {
    private android.support.v7.widget.RecyclerView recycler;
    private List<Discuss> mCarChoiceList = new ArrayList<>();
    private RecyclerCommonAdapter<Discuss> mCarBeanAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_discuss;
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
        map.put("productId",getIntent().getStringExtra("productId"));
        map.put("limit","10");
        map.put("filter","all");
        map.put("first",0+"");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getAllDiscuss(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
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
                        Type cartStatusType = new TypeToken<List<Discuss>>() {
                        }.getType();
                        mCarChoiceList=gson.fromJson(res.getJSONArray("data").toString(),cartStatusType);
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


            mCarBeanAdapter = new RecyclerCommonAdapter<Discuss>(mActivity, R.layout.item_cart_discuss, mCarChoiceList) {

                @Override
                protected void convert(ViewHolder holder, final Discuss carChoiceBean, int position) {
                    Discuss bean=carChoiceBean;
                    KRatingBar ratingBar=holder.itemView.findViewById(R.id.ratingbar2);
                    ratingBar.setRating(carChoiceBean.star);
                    ((TextView)holder.itemView.findViewById(R.id.comment)).setText(""+bean.comment);
                    ((TextView)holder.itemView.findViewById(R.id.nickname)).setText(bean.nickname);
                    NoScrollGridView gridView=holder.itemView.findViewById(R.id.function_grid);
                    gridView.setAdapter(new GridAdapter(mActivity,bean.getPics()));
                }

            };


            recycler.addItemDecoration(RecyclerViewDivider.with(this).color(Color.parseColor("#909090")).build());
            recycler.setAdapter(mCarBeanAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {

            mCarBeanAdapter.notifyDataSetChanged();
        }

    }
    class GridAdapter extends BaseAdapter {
        public GridAdapter(Context context, List<String> images) {
            this.context = context;
            this.images = images;
        }

        public Context context;
        private List<String> images;

        @Override
        public int getCount() {
            return images==null?0:images.size();
        }

        @Override
        public Object getItem(int position) {
            return images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView=new ImageView(context);
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
            Glide.with(context).load(images.get(position)).apply(options).into(imageView);
            return imageView;
        }
    }
}
