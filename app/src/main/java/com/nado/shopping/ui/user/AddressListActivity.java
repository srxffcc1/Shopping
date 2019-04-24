package com.nado.shopping.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fondesa.recyclerviewdivider.RecyclerViewDivider;
import com.nado.shopping.R;
import com.nado.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.nado.shopping.adapter.recycler.base.ViewHolder;
import com.nado.shopping.base.BaseActivity;
import com.nado.shopping.bean.DiZHi;
import com.nado.shopping.manager.AccountManager;
import com.nado.shopping.manager.RequestManager;
import com.nado.shopping.net.RetrofitCallBack;
import com.nado.shopping.net.RetrofitRequestInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressListActivity extends BaseActivity {
    private android.widget.RelativeLayout rlLayoutTopBackBar;
    private android.widget.LinearLayout llLayoutTopBackBarBack;
    private android.widget.TextView tvLayoutTopBackBarStart;
    private android.widget.TextView tvLayoutTopBackBarTitle;
    private android.widget.TextView tvLayoutTopBackBarEnd;
    private android.widget.TextView tvLayoutBackTopBarOperate;
    private android.support.v7.widget.RecyclerView recycler;
    private RecyclerCommonAdapter<DiZHi> myAdapter;
    private List<DiZHi> mBeanList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_address_list;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        tvLayoutTopBackBarTitle.setText("地址列表");
        tvLayoutTopBackBarEnd.setText("添加");
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();

        getAddressList();
    }

    @Override
    public void initEvent() {
        tvLayoutTopBackBarEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity,AddressActivity.class));
            }
        });
    }
    private void getAddressList() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getAddressList(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
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
                            DiZHi bean=new DiZHi();
                            bean.id = jsonObject.optString("id");
                            bean.receiver_name = jsonObject.optString("receiver_name");
                            bean.receiver_mobile = jsonObject.optString("receiver_mobile");
                            bean.region = jsonObject.optString("region");
                            bean.detailed_address = jsonObject.optString("detailed_address");
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

            myAdapter = new RecyclerCommonAdapter<DiZHi>(mActivity, R.layout.item_address, mBeanList) {
                @Override
                protected void convert(ViewHolder holder, final DiZHi carChoiceBean, int position) {
                    holder.setText(R.id.username,carChoiceBean.receiver_name+" "+carChoiceBean.receiver_mobile);
                    holder.setText(R.id.useraddress,carChoiceBean.detailed_address);
                    if("1".equals(carChoiceBean.is_default)){
                        holder.itemView.findViewById(R.id.default_flag).setVisibility(View.VISIBLE);
                    }else{
                        holder.itemView.findViewById(R.id.default_flag).setVisibility(View.GONE);
                    }
                    holder.setOnClickListener(R.id.passedit, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(buildIntent(new Intent(mActivity,AddressActivity.class),carChoiceBean));
                        }
                    });

                }

            };

            recycler.addItemDecoration(RecyclerViewDivider.with(this).color(Color.parseColor("#909090")).build());
            recycler.setAdapter(myAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        }else{
            myAdapter.notifyDataSetChanged();
        }
    }

}
