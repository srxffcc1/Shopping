package com.nado.parking.ui.device;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.nado.parking.R;
import com.nado.parking.adapter.recycler.RecyclerCommonAdapter;
import com.nado.parking.adapter.recycler.base.ViewHolder;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.bean.BaoJing;
import com.nado.parking.bean.DeviceBean;
import com.nado.parking.bean.WeiLan;
import com.nado.parking.constant.HomepageConstant;
import com.nado.parking.manager.AccountManager;
import com.nado.parking.manager.RequestManager;
import com.nado.parking.net.RetrofitCallBack;
import com.nado.parking.net.RetrofitRequestInterface;
import com.nado.parking.util.DisplayUtil;
import com.nado.parking.widget.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：Constantine on 2018/9/5.
 * 邮箱：2534159288@qq.com
 */
public class DeviceBaoJingListActivity extends BaseActivity {


    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarStart;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private android.support.v7.widget.RecyclerView recycler;
    private RecyclerCommonAdapter<BaoJing> myAdapter;
    private List<BaoJing> mBeanList = new ArrayList<>();
    private com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout trlFragmentHome;
    public int page=1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device_baojing;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        trlFragmentHome = (TwinklingRefreshLayout) findViewById(R.id.trl_fragment_home);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        tvLayoutTopBackBarTitle.setText("报警");
    }

    @Override
    public void initData() {
        initList();
    }

    @Override
    public void initEvent() {
        trlFragmentHome.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                page=1;
                isnomore=false;
                initList();
                trlFragmentHome.finishLoadmore();
                trlFragmentHome.finishRefreshing();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if(!isnomore){

                    initList();
                }
                trlFragmentHome.finishLoadmore();
                trlFragmentHome.finishRefreshing();

            }
        });
    }
    boolean isnomore=false;
    private void initList() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("obd_macid", AccountManager.sUserBean.obd_macid);
        map.put("page", page+"");
        map.put("limit", "10");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getAlarmList(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {

            @Override
            public void onSuccess(String response) {


                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if(page==1){
                        mBeanList.clear();
                    }
                    if (code == 0) {
                        JSONArray jsonArray=res.getJSONArray("data");
                        if(jsonArray.length()>0){
                            page++;
                        }else{
                            isnomore=true;
                        }
                        for (int i = 0; i <jsonArray.length() ; i++) {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            BaoJing bean=new BaoJing();
                            bean.id = jsonObject.optString("id");
                            bean.classify = jsonObject.optString("classify");
                            bean.pt_time = jsonObject.optString("pt_time");
                            bean.phone = jsonObject.optString("phone");
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

            myAdapter = new RecyclerCommonAdapter<BaoJing>(mActivity, R.layout.item_device_baojing, mBeanList) {
                @Override
                protected void convert(ViewHolder holder, final BaoJing carChoiceBean, int position) {
                    holder.setText(R.id.phone,carChoiceBean.phone);
                    holder.setText(R.id.time,carChoiceBean.pt_time);
                    holder.setText(R.id.status,carChoiceBean.classify);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            startActivity(new Intent(v.getContext(),DevicePostionActivity.class).putExtra("id",carChoiceBean.id));
                        }
                    });

                }

            };

//            recycler.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, (int) DisplayUtil.dpToPx(mActivity, 1), ContextCompat.getColor(mActivity, R.color.colorLine), false, 2));
            recycler.setAdapter(myAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        }else{
            myAdapter.notifyDataSetChanged();
        }
    }

}
