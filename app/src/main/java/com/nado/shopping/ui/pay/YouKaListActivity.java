package com.nado.shopping.ui.pay;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nado.shopping.R;
import com.nado.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.nado.shopping.adapter.recycler.base.ViewHolder;
import com.nado.shopping.base.BaseActivity;
import com.nado.shopping.bean.YouKa;
import com.nado.shopping.manager.AccountManager;
import com.nado.shopping.manager.RequestManager;
import com.nado.shopping.net.RetrofitCallBack;
import com.nado.shopping.net.RetrofitRequestInterface;
import com.nado.shopping.util.DisplayUtil;
import com.nado.shopping.widget.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YouKaListActivity extends BaseActivity {
    private android.widget.RelativeLayout rlLayoutTopBackBar;
    private android.widget.LinearLayout llLayoutTopBackBarBack;
    private android.widget.TextView tvLayoutTopBackBarTitle;
    private android.widget.TextView tvLayoutTopBackBarEnd;
    private android.widget.TextView tvLayoutBackTopBarOperate;
    private android.support.v7.widget.RecyclerView rvPayAll;

    private RecyclerCommonAdapter<YouKa> myAdapter;
    private List<YouKa> mBeanList = new ArrayList<>();


    @Override
    protected int getContentViewId() {
        return R.layout.activity_ykchange;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        rvPayAll = (RecyclerView) findViewById(R.id.rv_pay_all);
        tvLayoutTopBackBarTitle.setText("油卡列表");
        tvLayoutTopBackBarEnd.setText("添加油卡");
    }

    @Override
    public void initData() {
        getYouKaList();
    }

    @Override
    public void initEvent() {
        tvLayoutTopBackBarEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity,YouKaAddActivity.class));
            }
        });
    }
    private void getYouKaList() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("page", "1");
        map.put("limit", "10");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getYouKaList(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    mBeanList.clear();
                    if (code == 0) {
                        JSONArray jsonArray=res.getJSONArray("data");
                        for (int i = 0; i <jsonArray.length() ; i++) {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            YouKa bean=new YouKa();
                            bean.id = jsonObject.optString("id");
                            bean.card_number = jsonObject.optString("card_number");
                            bean.uid = jsonObject.optString("uid");
                            bean.add_time = jsonObject.optString("add_time");
                            bean.update_time = jsonObject.optString("update_time");
                            bean.state = jsonObject.optString("state");
                            bean.type = jsonObject.optString("type");
                            bean.is_default = jsonObject.optString("is_default");
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
    private void showRecycleView() {
        if (myAdapter == null) {


            myAdapter = new RecyclerCommonAdapter<YouKa>(mActivity, R.layout.item_youka, mBeanList) {

                @Override
                protected void convert(ViewHolder holder, final YouKa carChoiceBean, int position) {
                    holder.setText(R.id.kahao,carChoiceBean.card_number);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveDefaultCard(carChoiceBean);
                        }
                    });
                    if("1".equals(carChoiceBean.is_default)){
                        holder.setVisible(R.id.default_flag,true);
                    }else{

                        holder.setVisible(R.id.default_flag,false);
                    }
                }

            };

            rvPayAll.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, (int) DisplayUtil.dpToPx(mActivity, 1), ContextCompat.getColor(mActivity, R.color.colorLine), false, 2));
            rvPayAll.setAdapter(myAdapter);
            rvPayAll.setLayoutManager(new LinearLayoutManager(mActivity));
        }else{

            myAdapter.notifyDataSetChanged();
        }
    }

    private void saveDefaultCard(YouKa bean) {
        Map<String, String> map = new HashMap<>();
        map.put("id", bean.id);
        map.put("customer_id", AccountManager.sUserBean.getId());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).setDefaultCard(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        Toast.makeText(mActivity,"设置成功",Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
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
