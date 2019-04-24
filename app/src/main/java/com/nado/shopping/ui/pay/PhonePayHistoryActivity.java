package com.nado.shopping.ui.pay;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nado.shopping.R;
import com.nado.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.nado.shopping.adapter.recycler.base.ViewHolder;
import com.nado.shopping.base.BaseActivity;
import com.nado.shopping.bean.HfHistory;
import com.nado.shopping.manager.AccountManager;
import com.nado.shopping.manager.RequestManager;
import com.nado.shopping.net.RetrofitCallBack;
import com.nado.shopping.net.RetrofitRequestInterface;
import com.nado.shopping.util.DisplayUtil;
import com.nado.shopping.widget.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhonePayHistoryActivity extends BaseActivity {

    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarStart;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private RecyclerView recycler;
    private RecyclerCommonAdapter<HfHistory> myAdapter;
    private List<HfHistory> mBeanList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_hf_list;
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
        tvLayoutTopBackBarTitle.setText("充值记录");
    }

    @Override
    public void initData() {
        initList();
    }

    @Override
    public void initEvent() {

    }
    private void initList() {
        Map<String, String> map = new HashMap<>();
        map.put("userid", AccountManager.sUserBean.getId());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getHfList(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {

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
                            HfHistory bean=new HfHistory();
                            bean.id = jsonObject.optString("id");
                            bean.user_id = jsonObject.optString("user_id");
                            bean.telephone = jsonObject.optString("telephone");
                            bean.create_time = jsonObject.optString("create_time");
                            bean.telephone_money = jsonObject.optString("telephone_money");
                            bean.orderid = jsonObject.optString("orderid");
                            bean.status = jsonObject.optString("status");
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

            myAdapter = new RecyclerCommonAdapter<HfHistory>(mActivity, R.layout.item_hflist, mBeanList) {
                @Override
                protected void convert(ViewHolder holder, final HfHistory carChoiceBean, int position) {
                    holder.setText(R.id.shoujihaoma,"手机号码："+carChoiceBean.telephone);
                    Date date=new Date(Long.parseLong(carChoiceBean.create_time));
                    holder.setText(R.id.shoujiriqi,"充值日期："+new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date));
                    holder.setText(R.id.shoujijine,"-"+carChoiceBean.telephone_money);
                }

            };

            recycler.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, (int) DisplayUtil.dpToPx(mActivity, 1), ContextCompat.getColor(mActivity, R.color.colorLine), false, 2));
            recycler.setAdapter(myAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        }else{
            myAdapter.notifyDataSetChanged();
        }
    }

}
