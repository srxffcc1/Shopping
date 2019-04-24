package com.nado.shopping.ui.device;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fondesa.recyclerviewdivider.RecyclerViewDivider;
import com.nado.shopping.R;
import com.nado.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.nado.shopping.adapter.recycler.base.ViewHolder;
import com.nado.shopping.base.BaseActivity;
import com.nado.shopping.bean.CheKuang;
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

/**
 * 作者：Constantine on 2018/9/5.
 * 邮箱：2534159288@qq.com
 */
public class DeviceCarDetailActivity extends BaseActivity {



    private RecyclerCommonAdapter<CheKuang> myAdapter;
    private List<CheKuang> mBeanList = new ArrayList<>();
    private android.support.v7.widget.RecyclerView recycler;
    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarStart;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device_chekuang;
    }


    @Override
    public void initView() {


        recycler = (RecyclerView) findViewById(R.id.recycler);
        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        tvLayoutTopBackBarTitle.setText("车况");
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
        map.put("method", "OBDLatestCarCondition");
        map.put("mds", AccountManager.sUserBean.obd_mds);
        map.put("macid", AccountManager.sUserBean.obd_macid);
        RequestManager.mRetrofitManager2.createRequest(RetrofitRequestInterface.class).getCheKuang(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {

            @Override
            public void onSuccess(String response) {


                try {
                    JSONObject res = new JSONObject(response);
                    String info = res.getString("success");
                    mBeanList.clear();
                    if ("true".equals(info)) {
                        JSONArray jsonArray=res.getJSONArray("data");
                        for (int i = 0; i <jsonArray.length() ; i++) {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            CheKuang bean=new CheKuang();
                            bean.Number = jsonObject.optString("Number");
                            bean.Key = jsonObject.optString("Key");
                            bean.Value = jsonObject.optString("Value");
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

            myAdapter = new RecyclerCommonAdapter<CheKuang>(mActivity, R.layout.item_device_chekuang, mBeanList) {
                @Override
                protected void convert(ViewHolder holder, final CheKuang carChoiceBean, int position) {
                    holder.setText(R.id.carkey,carChoiceBean.Key);
                    holder.setText(R.id.carvalue,carChoiceBean.Value);


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
