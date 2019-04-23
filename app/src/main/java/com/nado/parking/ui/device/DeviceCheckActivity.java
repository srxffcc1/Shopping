package com.nado.parking.ui.device;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nado.parking.R;
import com.nado.parking.adapter.recycler.RecyclerCommonAdapter;
import com.nado.parking.adapter.recycler.base.ViewHolder;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.bean.DeviceCheck;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：Constantine on 2018/9/5.
 * 邮箱：2534159288@qq.com
 */
public class DeviceCheckActivity extends BaseActivity {


    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private android.widget.ImageView image;
    private TextView cheliangjiankang;
    private android.support.v7.widget.RecyclerView recycler;
    private TextView startcheck;
    private RecyclerCommonAdapter<DeviceCheck> myAdapter;
    private List<DeviceCheck> mBeanList = new ArrayList<>();
    private int sum=0;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device_jiance;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        image = (ImageView) findViewById(R.id.image);
        cheliangjiankang = (TextView) findViewById(R.id.cheliangjiankang);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        startcheck = (TextView) findViewById(R.id.startcheck);
        tvLayoutTopBackBarTitle.setText("车辆检测");
    }

    @Override
    public void initData() {

    }

    public void checkCar() {
        Map<String, String> map = new HashMap<>();
        map.put("method", "getCurCodeV3");
        map.put("macid", AccountManager.sUserBean.obd_macid);
        map.put("mds", AccountManager.sUserBean.obd_mds);
        RequestManager.mRetrofitManager2.createRequest(RetrofitRequestInterface.class).checkCar(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    String info = res.getString("success");
                    mBeanList.clear();
                    if ("true".equals(info)) {
                        JSONArray jsonArray = res.getJSONArray("obdSys");
                        boolean isallsum=true;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            DeviceCheck bean = new DeviceCheck();
                            bean.sysId = jsonObject.optString("sysId");
                            bean.obdTitle = jsonObject.optString("obdTitle");
                            bean.obdCount = jsonObject.optString("obdCount");
                            if(!"0".equals(bean.obdCount)){

                                isallsum=false;
                            }
                            sum++;
                            mBeanList.add(bean);

                        }
                        if(isallsum){
                            sum=100;
                        }

                    }
                    showRecycleView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                stopGif();
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    @Override
    public void initEvent() {
        startcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGif();
            }
        });
    }

    private void showRecycleView() {
        cheliangjiankang.setText("车辆健康:"+sum);
        if (myAdapter == null) {


            myAdapter = new RecyclerCommonAdapter<DeviceCheck>(mActivity, R.layout.item_device_jiance, mBeanList) {

                @Override
                protected void convert(ViewHolder holder, final DeviceCheck carChoiceBean, int position) {
                    holder.setText(R.id.device_key,carChoiceBean.obdTitle);
                    holder.setImageResource(R.id.device_image,"0".equals(carChoiceBean.obdCount)?R.drawable.device_ok:R.drawable.device_no);
                    holder.setText(R.id.device_value,"0".equals(carChoiceBean.obdCount)?"无故障":"有故障");
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                        }
//                    });
                }

            };

            recycler.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, (int) DisplayUtil.dpToPx(mActivity, 1), ContextCompat.getColor(mActivity, R.color.colorLine), false, 2));
            recycler.setAdapter(myAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {

            myAdapter.notifyDataSetChanged();
        }
    }

    public void stopGif() {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
        startcheck.setVisibility(View.VISIBLE);
        startcheck.setEnabled(true);
        Glide.with(DeviceCheckActivity.this).load(R.drawable.jiance_m).apply(options).into(image);
    }

    private void loadGif() {
        sum=0;
        mBeanList.clear();
        showRecycleView();
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸

        startcheck.setEnabled(false);
        startcheck.setVisibility(View.GONE);
        Glide.with(DeviceCheckActivity.this).asGif().load(R.drawable.jiance).apply(options).into(image);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                checkCar();
            }
        }, 2000);
    }
}
