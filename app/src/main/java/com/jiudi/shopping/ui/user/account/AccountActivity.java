package com.jiudi.shopping.ui.user.account;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.jiudi.shopping.bean.TongZhi;
import com.jiudi.shopping.bean.XianJin;
import com.jiudi.shopping.bean.YongJin;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.DisplayUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.widget.DividerItemDecoration;
import com.jiudi.shopping.widget.SimpleBottomView;
import com.jiudi.shopping.widget.SimpleLoadView;
import com.jiudi.shopping.widget.SimpleRefreshView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountActivity extends BaseActivity {
    private android.support.v7.widget.RecyclerView recycler;
    private List<XianJin> mCarChoiceList = new ArrayList<>();
    private RecyclerCommonAdapter<XianJin> mCarBeanAdapter;
    private android.widget.ImageView back;
    private TextView yue;
    private int page=0;
    private int limit=20;
    private boolean stoploadmore=false;
    private android.support.v4.widget.NestedScrollView nest;
    private com.dengzq.simplerefreshlayout.SimpleRefreshLayout simpleRefresh;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_account;
    }

    @Override
    public void initView() {

        recycler = (RecyclerView) findViewById(R.id.recycler);
        back = (ImageView) findViewById(R.id.back);
        yue = (TextView) findViewById(R.id.yue);
        nest = (NestedScrollView) findViewById(R.id.nest);
        simpleRefresh = (SimpleRefreshLayout) findViewById(R.id.simple_refresh);
    }

    @Override
    public void initData() {
        yue.setText(AccountManager.sUserBean.integral);
        getList();
        simpleRefresh.setScrollEnable(true);
        simpleRefresh.setPullUpEnable(true);
        simpleRefresh.setPullDownEnable(true);
        simpleRefresh.setHeaderView(new SimpleRefreshView(mActivity));
        simpleRefresh.setFooterView(new SimpleLoadView(mActivity));
        simpleRefresh.setBottomView(new SimpleBottomView(mActivity));
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
        map.put("first", page + "");
        map.put("limit", limit+"");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getXianJinList(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
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
                        Type cartStatusType = new TypeToken<List<XianJin>>() {
                        }.getType();
                        List<XianJin> tmp=gson.fromJson(res.getJSONArray("data").toString(),cartStatusType);
                        if(tmp==null||tmp.size()==0){
                            stoploadmore=true;
                        }
                        mCarChoiceList.addAll(tmp);

                        showCarChoiceRecycleView();
                    }else{
                        Toast.makeText(mActivity,info,Toast.LENGTH_SHORT).show();
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


            mCarBeanAdapter = new RecyclerCommonAdapter<XianJin>(mActivity, R.layout.item_yongjin, mCarChoiceList) {

                @Override
                protected void convert(ViewHolder holder, final XianJin carChoiceBean, int position) {
                    holder.setText(R.id.content,carChoiceBean.mark);
                    holder.setText(R.id.time,carChoiceBean.add_time);
                    holder.setText(R.id.money,("1".equals(carChoiceBean.pm)?"+":"-")+carChoiceBean.number+"元");
                    holder.setTextColor(R.id.money,"1".equals(carChoiceBean.pm)? Color.parseColor("#1e8f00") :Color.parseColor("#E9391C"));
                }

            };

            recycler.addItemDecoration(RecyclerViewDivider.with(mActivity).size(2).color(Color.parseColor("#E9E8ED")).build());
            recycler.setAdapter(mCarBeanAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {

            mCarBeanAdapter.notifyDataSetChanged();
        }

    }
}
