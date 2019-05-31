package com.jiudi.shopping.ui.fenxiao;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dengzq.simplerefreshlayout.SimpleRefreshLayout;
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
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.TuanDui;
import com.jiudi.shopping.bean.YongJin;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.widget.SimpleBottomView;
import com.jiudi.shopping.widget.SimpleLoadView;
import com.jiudi.shopping.widget.SimpleRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TuanDuiFragment extends BaseFragment {
    private RecyclerView rvFragmentHomeAll;
    private RecyclerCommonAdapter<TuanDui> myAdapter;
    private List<TuanDui> mBeanList = new ArrayList<>();
    private com.dengzq.simplerefreshlayout.SimpleRefreshLayout simpleRefresh;
    private TextView titleCase;

    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_fenxiao_account;
    }

    @Override
    public void initView() {

        rvFragmentHomeAll = (RecyclerView) findViewById(R.id.rv_fragment_home_all);
        simpleRefresh = (SimpleRefreshLayout) findViewById(R.id.simple_refresh);
        titleCase = (TextView) findViewById(R.id.title_case);
    }

    @Override
    public void initData() {
        simpleRefresh.setScrollEnable(true);
        simpleRefresh.setPullUpEnable(true);
        simpleRefresh.setPullDownEnable(true);
        simpleRefresh.setHeaderView(new SimpleRefreshView(mActivity));
        simpleRefresh.setFooterView(new SimpleLoadView(mActivity));
        simpleRefresh.setBottomView(new SimpleBottomView(mActivity));
        getOrderList();
    }
    private int page=0;
    private int limit=20;
    private boolean stoploadmore=false;
    private void getOrderList() {
        Map<String, String> map = new HashMap<>();
        map.put("type", getArguments().getString("type"));
        map.put("first", page + "");
        map.put("limit", limit+"");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getKeHuList(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        JSONObject data = res.getJSONObject("data");
                        JSONArray jsonArray = data.getJSONArray("info");
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                return new Date(json.getAsJsonPrimitive().getAsLong());
                            }
                        });
                        Gson gson = builder.create();
                        Type yongjintype = new TypeToken<List<TuanDui>>() {
                        }.getType();
                        String yonglists=jsonArray.toString();
                        List<TuanDui> tmp=gson.fromJson(yonglists, yongjintype);
                        if(tmp==null||tmp.size()==0){
                            stoploadmore=true;
                        }
                        mBeanList.addAll(tmp);
                        if("1".equals(getArguments().getString("type"))){
                            titleCase.setText("您的粉丝中有"+data.optString("number").replace("null","0")+"位成为了店主，为您分成累计"+data.optString("total_money").replace("null","0")+"元");
                        }else{
                            titleCase.setText("您有"+data.optString("number").replace("null","0")+"位粉丝，为您分成累计"+data.optString("total_money").replace("null","0")+"元");
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

    @Override
    public void initEvent() {
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
                mBeanList.clear();
                page=0;
                getOrderList();
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
                    getOrderList();
                }

            }
        });
    }

    private void showRecycleView() {
        if (myAdapter == null) {

            myAdapter = new RecyclerCommonAdapter<TuanDui>(mActivity, R.layout.item_tuandui, mBeanList) {
                @Override
                protected void convert(ViewHolder holder, final TuanDui carChoiceBean, int position) {
                    holder.setText(R.id.name,carChoiceBean.nickname);
                    holder.setText(R.id.time,carChoiceBean.add_time.replace("/","-"));
                    holder.setText(R.id.member,carChoiceBean.money+"元");
                    ImageView head=holder.itemView.findViewById(R.id.image);
                    RequestOptions requestOptions = RequestOptions.circleCropTransform().error(R.drawable.head_defuat_circle);
                    Glide.with(mActivity).load(carChoiceBean.avatar).apply(requestOptions).into(head);
                    ImageView top=holder.itemView.findViewById(R.id.top);
                    ImageView vip=holder.itemView.findViewById(R.id.vip);
                    if("0".equals(carChoiceBean.is_promoter)){
                        vip.setVisibility(View.GONE);
                    }else{
                        vip.setVisibility(View.VISIBLE);
                    }

                    if("2".equals(carChoiceBean.agent_id)){
                        top.setVisibility(View.VISIBLE);
                    }else{
                        top.setVisibility(View.GONE);
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

