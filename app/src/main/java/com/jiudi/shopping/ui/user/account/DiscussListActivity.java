package com.jiudi.shopping.ui.user.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dengzq.simplerefreshlayout.SimpleRefreshLayout;
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
import com.jiudi.shopping.ui.user.ShowImageActivity;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.DisplayUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.TimeUtil;
import com.jiudi.shopping.widget.DividerItemDecoration;
import com.jiudi.shopping.widget.KRatingBar;
import com.jiudi.shopping.widget.NoScrollGridView;
import com.jiudi.shopping.widget.SimpleBottomView;
import com.jiudi.shopping.widget.SimpleLoadView;
import com.jiudi.shopping.widget.SimpleRefreshView;

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
    private boolean stoploadmore=false;
    private int page=0;
    private int fujian_px=0;
    private int limit=20;
    private ImageView back;
    private SimpleRefreshLayout simpleRefresh;
    private android.support.v4.widget.NestedScrollView nest;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_discuss;
    }

    @Override
    public void initView() {

        recycler = (RecyclerView) findViewById(R.id.recycler);
        back = (ImageView) findViewById(R.id.back);
        simpleRefresh = (SimpleRefreshLayout) findViewById(R.id.simple_refresh);
        nest = (NestedScrollView) findViewById(R.id.nest);
    }

    @Override
    public void initData() {
        simpleRefresh.setScrollEnable(true);
        simpleRefresh.setPullUpEnable(true);
        simpleRefresh.setPullDownEnable(true);
        simpleRefresh.setHeaderView(new SimpleRefreshView(mActivity));
        simpleRefresh.setFooterView(new SimpleLoadView(mActivity));
        simpleRefresh.setBottomView(new SimpleBottomView(mActivity));
        getList();
    }

    @Override
    public void initEvent() {
//        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if(recycler.canScrollVertically(1)){
//                }else {
//                    if(mCarBeanAdapter!=null &&!stoploadmore){//滑动到底部
//                        page=page+10;
//                        getList();
//                    }
//
//                }
//                if(recycler.canScrollVertically(-1)){
//
//                }else {
//                    //滑动到顶部
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
        simpleRefresh.setOnSimpleRefreshListener(new SimpleRefreshLayout.OnSimpleRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        simpleRefresh.onRefreshComplete();
                        simpleRefresh.onLoadMoreComplete();
                    }
                },500);
                mCarChoiceList.clear();
                page=0;
                getList();
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        simpleRefresh.onRefreshComplete();
                        simpleRefresh.onLoadMoreComplete();
                    }
                },500);
                if(stoploadmore){

                    Toast.makeText(mActivity,"没有更多",Toast.LENGTH_SHORT).show();
                }else{
                    page=page+limit;
                    getList();
                }

            }
        });
    }
    private void getList() {
        Map<String, String> map = new HashMap<>();
        map.put("productId",getIntent().getStringExtra("productId"));
        map.put("first", page + "");
        map.put("limit", limit+"");
        map.put("filter","all");
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
                        if(mCarChoiceList==null||mCarChoiceList.size()<0){
                            stoploadmore=true;
                        }
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

                    ImageView imageView=holder.itemView.findViewById(R.id.head);
                    if(carChoiceBean.avatar!=null){
                        RequestOptions options = new RequestOptions()
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
                        Glide.with(mActivity).load(carChoiceBean.avatar.startsWith("http")?carChoiceBean.avatar:RequestManager.mBaseUrl+carChoiceBean.avatar).apply(options).into(imageView);
                    }

                    ((TextView)holder.itemView.findViewById(R.id.time)).setText(""+ TimeUtil.formatLong(carChoiceBean.add_time));
                    final GridLayout fujianLayout = (GridLayout) holder.itemView.findViewById(R.id.fujian_layout);

                    ViewTreeObserver vto = fujianLayout.getViewTreeObserver();
                    if(fujian_px==0){
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                int temppx = fujianLayout.getWidth();
                                fujianLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                fujian_px = (temppx - 0) / 4;
//                                System.out.println("获得的大小" + fujian_px);
                                new GridAdapter(mActivity, carChoiceBean.getPics(), fujianLayout).build();

                            }
                        });
                    }else{

                        new GridAdapter(mActivity, carChoiceBean.getPics(), fujianLayout).build();
                    }
                }

            };


            recycler.addItemDecoration(RecyclerViewDivider.with(mActivity).size(10).color(Color.parseColor("#E9E8ED")).build());
            recycler.setAdapter(mCarBeanAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {

            mCarBeanAdapter.notifyDataSetChanged();
        }

    }
    class GridAdapter {
        public Context context;
        private List<String> images;
        private ViewGroup parent;

        public GridAdapter(Context context, List<String> images, ViewGroup parent) {
            this.context = context;
            this.images = images;
            this.parent = parent;
        }

        public int getCount() {
            return images == null ? 0 : images.size();
        }

        public Object getItem(int position) {
            return images.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public void build() {
            parent.removeAllViews();

//            LinearLayout layout = new LinearLayout(context);
//            LinearLayout.LayoutParams LL_MW = new LinearLayout.LayoutParams
//                    (fujian_px, fujian_px);
//            layout.setOrientation(LinearLayout.VERTICAL);
//            layout.setGravity(Gravity.CENTER);
//            layout.setLayoutParams(LL_MW);
//
//            ImageView imageView = new ImageView(context);
//            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            LinearLayout.LayoutParams LL_IM = new LinearLayout.LayoutParams
//                    (fujian_px - 10, fujian_px - 10);
//            imageView.setLayoutParams(LL_IM);
//            imageView.setImageResource(R.drawable.start_pot);
//            layout.addView(imageView);
//            parent.addView(layout);
            for (int i = 0; i < getCount(); i++) {
                parent.addView(getView(i));
            }
        }

        public View getView(final int position) {
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams LL_MW = new LinearLayout.LayoutParams
                    (fujian_px, fujian_px);
            layout.setLayoutParams(LL_MW);

            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            LinearLayout.LayoutParams LL_IM = new LinearLayout.LayoutParams
                    (fujian_px - 10, fujian_px - 10);
            imageView.setLayoutParams(LL_IM);
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
            Glide.with(context).load(images.get(position)).apply(options).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ShowImageActivity.class).putExtra("URL",images.get(position)));
                }
            });
            layout.addView(imageView);
            return layout;
        }
    }

}
