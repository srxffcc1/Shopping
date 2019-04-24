package com.jiudi.shopping.ui.device;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.WeiLan;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.DisplayUtil;
import com.jiudi.shopping.widget.DividerItemDecoration;

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
public class DeviceWeiLanListActivity extends BaseActivity {


    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private android.support.v7.widget.RecyclerView recycler;
    private RecyclerCommonAdapter<WeiLan> myAdapter;
    private List<WeiLan> mBeanList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device_weilanlist;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        tvLayoutTopBackBarTitle.setText("围栏列表");
        tvLayoutTopBackBarEnd.setText("添加");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        tvLayoutTopBackBarEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),DeviceWeiLanActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();
    }

    private void initList() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getWeiLanList(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {

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
                            WeiLan bean=new WeiLan();
                            bean.id = jsonObject.optString("id");
                            bean.rail_name = jsonObject.optString("rail_name");
                            bean.lat = jsonObject.optString("lat");
                            bean.lng = jsonObject.optString("lng");
                            bean.radii = jsonObject.optString("radii");
                            bean.address = jsonObject.optString("address");
                            bean.report_type = jsonObject.optString("report_type");
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

            myAdapter = new RecyclerCommonAdapter<WeiLan>(mActivity, R.layout.item_device_weilan, mBeanList) {
                @Override
                protected void convert(ViewHolder holder, final WeiLan carChoiceBean, int position) {
                    holder.setText(R.id.name,"名称："+carChoiceBean.rail_name);
                    holder.setText(R.id.radii,"半径"+carChoiceBean.radii+"米");
                    holder.setText(R.id.address,"地址："+carChoiceBean.address);
                    if("1".equals(carChoiceBean.report_type)){
                        holder.setChecked(R.id.mapjin,true);
                    }
                    if("2".equals(carChoiceBean.report_type)){
                        holder.setChecked(R.id.mapchu,true);

                    }
                    if("3".equals(carChoiceBean.report_type)){
                        holder.setChecked(R.id.mapjinchu,true);

                    }
                    if("4".equals(carChoiceBean.report_type)){
                        holder.setChecked(R.id.mapguanbi,true);

                    }
                    holder.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteWeiLan(carChoiceBean);
                        }
                    });
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(v.getContext(),DeviceWeiLanActivity.class);
                            startActivity(buildIntent(intent,carChoiceBean));
                        }
                    });
                    holder.setOnCheckedChangeListener(R.id.mapjin, new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(!buttonView.isPressed())
                                return;
                            if(isChecked){
                                System.out.println("走了1");
                                saveWeiLan(carChoiceBean,"1");
                            }
                        }
                    });
                    holder.setOnCheckedChangeListener(R.id.mapchu, new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(!buttonView.isPressed())
                                return;
                            if(isChecked){
                                System.out.println("走了2");
                                saveWeiLan(carChoiceBean,"2");
                            }
                        }
                    });
                    holder.setOnCheckedChangeListener(R.id.mapjinchu, new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(!buttonView.isPressed())
                                return;
                            if(isChecked){
                                System.out.println("走了3");
                                saveWeiLan(carChoiceBean,"3");
                            }
                        }
                    });
                    holder.setOnCheckedChangeListener(R.id.mapguanbi, new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(!buttonView.isPressed())
                                return;
                            if(isChecked){
                                System.out.println("走了4");
                                saveWeiLan(carChoiceBean,"4");
                            }
                        }
                    });

                }

            };

            recycler.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, (int) DisplayUtil.dpToPx(mActivity, 1), ContextCompat.getColor(mActivity, R.color.colorLine), false, 2));
            recycler.setAdapter(myAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        }else{
            myAdapter.notifyDataSetChanged();
        }
    }


    public void saveWeiLan(WeiLan bean,String report_type) {
        Map<String, String> map = new HashMap<>();
        map.put("id", bean.id);
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("rail_name", bean.rail_name);
        map.put("lng", bean.lng);
        map.put("lat", bean.lat);
        map.put("radii", bean.radii + "");
        map.put("address", bean.address);
        map.put("report_type", report_type);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).saveWeiLanDy("index.php?g=app&m=appv1&a=UpdateRail", RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        Toast.makeText(getBaseContext(),"更新围栏成功",Toast.LENGTH_SHORT).show();
                        initList();
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


    private void deleteWeiLan(WeiLan carChoiceBean) {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("id", carChoiceBean.id);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).deleteWeiLan(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        Toast.makeText(getBaseContext(),"删除成功",Toast.LENGTH_SHORT).show();
                        initList();
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
