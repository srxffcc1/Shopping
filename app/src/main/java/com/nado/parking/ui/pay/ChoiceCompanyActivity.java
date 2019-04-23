package com.nado.parking.ui.pay;

import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.nado.parking.R;
import com.nado.parking.base.BaseActivity;

public class ChoiceCompanyActivity extends BaseActivity {
    private android.widget.RelativeLayout rlLayoutTopBackBar;
    private android.widget.LinearLayout llLayoutTopBackBarBack;
    private android.widget.TextView tvLayoutTopBackBarTitle;
    private android.widget.TextView tvLayoutTopBackBarEnd;
    private android.widget.TextView tvLayoutBackTopBarOperate;
    private com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout tflActivityParkLot;
    private android.widget.EditText etActivitySearchSearch;
    private android.widget.ImageView ivActivitySearchSearch;
    private android.support.v7.widget.RecyclerView rvActivityChooseCompanyList;
    private String sCity;
    private String sProvince;

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
        tflActivityParkLot = (TwinklingRefreshLayout) findViewById(R.id.tfl_activity_park_lot);
        etActivitySearchSearch = (EditText) findViewById(R.id.et_activity_search_search);
        ivActivitySearchSearch = (ImageView) findViewById(R.id.iv_activity_search_search);
        rvActivityChooseCompanyList = (RecyclerView) findViewById(R.id.rv_activity_choose_company_list);
    }

    @Override
    public void initData() {
        initCompanyList();
    }

    private void initCompanyList() {
//        Map<String, String> map = new HashMap<>();
//        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getCompany(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
//
//            @Override
//            public void onSuccess(String response) {
//
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//            }
//        });
    }

    @Override
    public void initEvent() {

    }
}
