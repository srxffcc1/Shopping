package com.nado.parking.ui.pay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fondesa.recyclerviewdivider.RecyclerViewDivider;
import com.nado.parking.R;
import com.nado.parking.adapter.recycler.RecyclerCommonAdapter;
import com.nado.parking.adapter.recycler.base.ViewHolder;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.bean.ShuiCompany;
import com.nado.parking.constant.HomepageConstant;
import com.nado.parking.manager.RequestManager;
import com.nado.parking.net.RetrofitCallBack;
import com.nado.parking.net.RetrofitRequestInterface;
import com.nado.parking.ui.main.ChooseCityActivity;
import com.nado.parking.util.DisplayUtil;
import com.nado.parking.widget.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShuiChoseJGActivity extends BaseActivity {
    private android.widget.RelativeLayout rlLayoutTopBackBar;
    private android.widget.LinearLayout llLayoutTopBackBarBack;
    private android.widget.TextView tvLayoutTopBackBarTitle;
    private android.widget.TextView tvLayoutTopBackBarEnd;
    private android.widget.TextView tvLayoutBackTopBarOperate;
    private com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout tflActivityParkLot;
    private android.widget.EditText etActivitySearchSearch;
    private android.widget.ImageView ivActivitySearchSearch;
    private android.support.v7.widget.RecyclerView list;
    private RecyclerCommonAdapter<ShuiCompany> myAdapter;
    private List<ShuiCompany> mBeanList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_sdjgxz;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
//        tflActivityParkLot = (TwinklingRefreshLayout) findViewById(R.id.tfl_activity_park_lot);
        etActivitySearchSearch = (EditText) findViewById(R.id.et_activity_search_search);
        ivActivitySearchSearch = (ImageView) findViewById(R.id.iv_activity_search_search);
        list = (RecyclerView) findViewById(R.id.list);
        tvLayoutTopBackBarEnd.setText("支持的机构");
        tvLayoutTopBackBarTitle.setText("机构选择");
    }

    @Override
    public void initData() {
        if(HomepageConstant.mLocationCity!=null){
            tvLayoutTopBackBarEnd.setText(HomepageConstant.mLocationCity.replace("市",""));
        }else{
            tvLayoutTopBackBarEnd.setText("苏州");
        }

        initCompanyList(HomepageConstant.mLocationCity.replace("市",""));

    }

    private void initCompanyList(String city) {
        Map<String, String> map = new HashMap<>();
        map.put("city",city);
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).getShuiCompany(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {

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
                            ShuiCompany bean=new ShuiCompany();
                            bean.id=jsonObject.optString("id");
                            bean.city=jsonObject.optString("city");
                            bean.company=jsonObject.optString("company");
                            bean.letter=jsonObject.optString("letter");
                            bean.is_hot=jsonObject.optString("is_hot");
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


            myAdapter = new RecyclerCommonAdapter<ShuiCompany>(mActivity, R.layout.item_jg, mBeanList) {

                @Override
                protected void convert(ViewHolder holder, final ShuiCompany carChoiceBean, int position) {
                    holder.setText(R.id.jgname,carChoiceBean.company);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bindCityCompany(carChoiceBean);
                        }
                    });
                }

            };

            list.addItemDecoration(RecyclerViewDivider.with(this).color(Color.parseColor("#909090")).build());
            list.setAdapter(myAdapter);
            list.setLayoutManager(new LinearLayoutManager(mActivity));
        }else{

            myAdapter.notifyDataSetChanged();
        }
    }

    private void bindCityCompany(ShuiCompany carChoiceBean) {
        setResult(Activity.RESULT_OK,new Intent().putExtra("id",carChoiceBean.id).putExtra("company",carChoiceBean.company));
        finish();
    }

    @Override
    public void initEvent() {
        tvLayoutTopBackBarEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(v.getContext(), ChooseCityActivity.class).putExtra("url","index.php?g=app&m=life&a=get_city"),100);
            }
        });
//        tflActivityParkLot.setEnableRefresh(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if(resultCode== Activity.RESULT_OK){
                tvLayoutTopBackBarEnd.setText(data.getStringExtra("city"));
                initCompanyList(data.getStringExtra("city"));
            }
        }
    }
}
