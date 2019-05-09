package com.jiudi.shopping.ui.user.account;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

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
import com.jiudi.shopping.bean.YongJin;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.DisplayUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.widget.DividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountActivity extends BaseActivity {
    private android.widget.TextView yongjinbiaoti;
    private android.widget.TextView money;
    private android.support.v7.widget.RecyclerView recycler;
    private List<YongJin> mCarChoiceList = new ArrayList<>();
    private RecyclerCommonAdapter<YongJin> mCarBeanAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_account;
    }

    @Override
    public void initView() {

        yongjinbiaoti = (TextView) findViewById(R.id.yongjinbiaoti);
        money = (TextView) findViewById(R.id.money);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    public void initData() {
        money.setText(AccountManager.sUserBean.now_money);
        getList();
    }

    @Override
    public void initEvent() {

    }
    private void getList() {
        Map<String, String> map = new HashMap<>();
        map.put("first", "0");
        map.put("limit", "10");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getYongJinList(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
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
                        Type cartStatusType = new TypeToken<List<YongJin>>() {
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


            mCarBeanAdapter = new RecyclerCommonAdapter<YongJin>(mActivity, R.layout.item_yongjin, mCarChoiceList) {

                @Override
                protected void convert(ViewHolder holder, final YongJin carChoiceBean, int position) {
                    holder.setText(R.id.content,carChoiceBean.mark);
                    holder.setText(R.id.time,carChoiceBean.add_time);
                    holder.setText(R.id.money,"1".equals(carChoiceBean.pm)?"+":"-"+carChoiceBean.number);
                    holder.setTextColor(R.id.money,"1".equals(carChoiceBean.pm)? Color.parseColor("#1e8f00") :Color.parseColor("#E9391C"));
                }

            };


            recycler.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, (int) DisplayUtil.dpToPx(mActivity, 1), ContextCompat.getColor(mActivity, R.color.colorLine), false, 2));
            recycler.setAdapter(mCarBeanAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {

            mCarBeanAdapter.notifyDataSetChanged();
        }

    }
}
