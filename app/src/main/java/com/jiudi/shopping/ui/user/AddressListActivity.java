package com.jiudi.shopping.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fondesa.recyclerviewdivider.RecyclerViewDivider;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.DiZHi;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.SPUtil;

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
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).getAddressList(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    mBeanList.clear();
                    if (code == 200) {
                        JSONArray jsonArray=res.getJSONObject("data").getJSONArray("address");
                        for (int i = 0; i <jsonArray.length() ; i++) {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            DiZHi bean=new DiZHi();
                            bean.id=jsonObject.optString("id");
                            bean.real_name=jsonObject.optString("real_name");
                            bean.phone=jsonObject.optString("phone");
                            bean.province=jsonObject.optString("province");
                            bean.city=jsonObject.optString("city");
                            bean.district=jsonObject.optString("district");
                            bean.detail=jsonObject.optString("detail");
                            bean.is_default=jsonObject.optString("is_default");
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
                    holder.setText(R.id.name,"收货人："+carChoiceBean.real_name+" "+carChoiceBean.phone);
                    holder.setText(R.id.address,"收货地址："+carChoiceBean.province+carChoiceBean.city+carChoiceBean.district+carChoiceBean.detail);
                    final CheckBox checkBox=holder.itemView.findViewById(R.id.check);
                    checkBox.setOnKeyListener(null);
                    if("1".equals(carChoiceBean.is_default)){
                        checkBox.setChecked(true);
                    }else{
                        checkBox.setChecked(false);
                    }
                    holder.setOnClickListener(R.id.edit, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(buildIntent(new Intent(mActivity,AddressActivity.class),carChoiceBean));
                        }
                    });
                    holder.setOnClickListener(R.id.delete, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            startActivity(buildIntent(new Intent(mActivity,AddressActivity.class),carChoiceBean));
                            deleteAddress(carChoiceBean);
                        }
                    });
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(!buttonView.isPressed()){
                                return;
                            }if(isChecked){
                                saveAddress(carChoiceBean);
                            }
                        }
                    });
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            saveAddress(carChoiceBean);
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
    private void saveAddress(DiZHi bean) {
        Map<String, String> map = new HashMap<>();
        map.put("id", bean.id);
        map.put("real_name", bean.real_name);
        map.put("phone", bean.phone);
        map.put("province", bean.province);
        map.put("city", bean.city);
        map.put("district", bean.district);
        map.put("detail", bean.detail);
        map.put("is_default", "1");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).saveAddress(SPUtil.get("head", "").toString(),"api/auth_api/edit_user_address",RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        getAddressList();
                        if(getIntent().getBooleanExtra("needpay",false)){
                            finish();
                        }
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
    private void deleteAddress(DiZHi bean) {
        Map<String, String> map = new HashMap<>();
        map.put("addressId", bean.id);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).deleteAddress(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        getAddressList();
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
