package com.jiudi.shopping.ui.user;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.CarBean;
import com.jiudi.shopping.bean.CarOrderBean;
import com.jiudi.shopping.event.UpdateCarInfoEvent;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.main.PayActivity;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserCarActivity extends BaseActivity {
    private static final String TAG = "UserCarActivity";

    @BindView(R.id.ll_layout_top_back_bar_back)
    LinearLayout mLlLayoutTopBackBarBack;
    @BindView(R.id.tv_layout_top_back_bar_title)
    TextView mTvLayoutTopBackBarTitle;
    @BindView(R.id.tv_layout_back_top_bar_operate)
    TextView mTvLayoutBackTopBarOperate;
    @BindView(R.id.rl_layout_top_back_bar)
    RelativeLayout mRlLayoutTopBackBar;
    @BindView(R.id.tv_activity_user_car_add)
    TextView mTvActivityUserCarAdd;
    @BindView(R.id.tv_activity_user_car_question)
    TextView mTvActivityUserCarQuestion;
    @BindView(R.id.ll_activity_user_car_empty)
    LinearLayout mLlActivityUserCarEmpty;
    @BindView(R.id.ll_activity_user_car_info)
    LinearLayout mLlActivityUserCarInfo;
    @BindView(R.id.rv_activity_user_car_list)
    RecyclerView mListRV;
    @BindView(R.id.ll_activity_car_info_question)
    LinearLayout mCarInfoQuestionLL;
    @BindView(R.id.tv_contact_service)
    TextView mCarInfoQuestionTV;


    private RecyclerCommonAdapter<CarBean> mAdapter;
    private CarBean carBean = new CarBean();
    private static final int REQUEST_CODE_ADD = 1;
    private static final int REQUEST_CODE_INFO = 2;
    private List<CarBean> mCarBeanList = new ArrayList<>();

    private PopupWindow mDeletePW;
    private boolean mHintFlg = true;

    private PopupWindow mExpectPopwindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_car;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(mActivity);

        mTvLayoutTopBackBarTitle.setText(getString(R.string.my_car));
    }

    @Override
    public void initData() {
        DialogUtil.showProgress(mActivity);
        getCarInfo();
    }

    private void getCarInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        LogUtil.e(TAG, map.toString());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getAllCar(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e(TAG, response);
                DialogUtil.hideProgress();
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        mCarBeanList.clear();
                        mCarBeanList.add(carBean);
                        JSONArray data = res.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject dataItem = data.getJSONObject(i);
                            CarBean carBean = new CarBean();
                            carBean.setId(dataItem.getString("id"));
                            carBean.setPlate(dataItem.getString("plate"));
                            JSONArray orderArray=dataItem.optJSONArray("orders");
                            List<CarOrderBean> carOrderBeanList=new ArrayList<>();
                            if(orderArray!=null){
                                for (int j=0;j<orderArray.length();j++){
                                    JSONObject orderItem=orderArray.getJSONObject(j);
                                    CarOrderBean orderBean = new CarOrderBean();
                                    orderBean.setId(orderItem.getString("id"));
                                    orderBean.setParkName(orderItem.getString("park_name"));
                                    orderBean.setStatus(orderItem.getString("status"));
                                    // status：0入场待付1预付待出2赊账待出 3赊账出场4已完成
                                    orderBean.setUnid(orderItem.getString("unid"));
                                    orderBean.setDerateDuration(orderItem.getString("derate_duration"));
                                    orderBean.setPaidFee(orderItem.getString("paid_fee"));
                                    orderBean.setTotalFee(orderItem.getString("total_fee"));
                                    orderBean.setTicketFee(orderItem.getString("ticket_fee"));
                                    orderBean.setPrice(orderItem.getString("price"));
                                    orderBean.setEntryTime(orderItem.getString("entry_time"));
                                    orderBean.setDuration(orderItem.getString("duration"));
                                    carOrderBeanList.add(orderBean);
                                }
                            }
                            if (carOrderBeanList.size()>0){
                                carBean.setOrder1List(true);
                            }else {
                                carBean.setOrder1List(false);
                            }
                            mCarBeanList.add(carBean);
                        }
                        if (mCarBeanList.size() > 1) {
                            mLlActivityUserCarEmpty.setVisibility(View.GONE);
                            mLlActivityUserCarInfo.setVisibility(View.VISIBLE);
                            mCarInfoQuestionLL.setVisibility(View.VISIBLE);
                            showMyCarsInfo();
                        } else {
                            mLlActivityUserCarEmpty.setVisibility(View.VISIBLE);
                            mLlActivityUserCarInfo.setVisibility(View.GONE);
                            mCarInfoQuestionLL.setVisibility(View.GONE);
                        }
                    } else {
                        ToastUtil.showLong(mActivity, info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                DialogUtil.hideProgress();
                if (!NetworkUtil.isConnected()) {
                    ToastUtil.showShort(mActivity, R.string.not_net);
                } else {
                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
                }
            }
        });
    }


    private void showMyCarsInfo() {
        if (mAdapter == null) {
            mAdapter = new RecyclerCommonAdapter<CarBean>(mActivity, R.layout.item_my_car_list, mCarBeanList) {
                @Override
                protected void convert(final ViewHolder holder, final CarBean carBean, final int position) {
//                    holder.setText(R.id.tv_car_plate, carBeanList.get(position).getPlate());
                    final LinearLayout hintCarInfoLL = holder.getView(R.id.ll_car_using);
                    if (position == 0) {
                        mHintFlg = true;
                        holder.setImageDrawable(R.id.iv_activity_car_icon, ContextCompat.getDrawable(mActivity, R.drawable.record_blue_line));
                        holder.setText(R.id.tv_car_plate, getString(R.string.my_car));
                        holder.setText(R.id.tv_car_hint, getString(R.string.car_my_add_hint));
                        holder.setImageDrawable(R.id.iv_activity_car_event_icon, ContextCompat.getDrawable(mActivity, R.drawable.add_blue));
                    } else {
                        mHintFlg = true;
                        holder.setText(R.id.tv_car_plate, mCarBeanList.get(position).getPlate());
                        holder.setImageDrawable(R.id.iv_activity_car_event_icon, ContextCompat.getDrawable(mActivity, R.drawable.delete_blue));
                    }
                    holder.getView(R.id.iv_activity_car_event_icon).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (position) {
                                case 0:
                                    if (mCarBeanList.size() > 3) {
                                        ToastUtil.showLong(mActivity, getString(R.string.add_error));
                                    } else {
                                        AddCarActivity1.open(mActivity);
                                    }
                                    break;
                                case 1:
                                    showDeletePopWindow(carBean.getId());
                                    break;
                                case 2:
                                    showDeletePopWindow(carBean.getId());
                                    break;
                                case 3:
                                    showDeletePopWindow(carBean.getId());
                                    break;
                            }
                        }
                    });
                    if (carBean.isOrder1List()){
                        if (carBean.getOrder1().size()>0){
                            hintCarInfoLL.setVisibility(View.VISIBLE);
                            holder.setText(R.id.tv_item_car_place,getString(R.string.stop_car_place,carBean.getOrder1().get(0).getParkName()));
                            holder.setText(R.id.tv_item_car_time,getString(R.string.stop_car_time,carBean.getOrder1().get(0).getDuration()));
                            holder.setText(R.id.tv_item_cat_money,getString(R.string.stop_car_price,carBean.getOrder1().get(0).getPrice()));

                            holder.getView(R.id.v_item_button_pay).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PayActivity.open( mActivity,carBean.getOrder1().get(0).getPrice(),  carBean.getOrder1().get(0).getUnid());
                                }
                            });

                        }
                    } else {
                        hintCarInfoLL.setVisibility(View.GONE);
                    }
                    holder.getView(R.id.ll_activity_car_info_detail).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mHintFlg = false;
//                            ToastUtil.showShort(mActivity, "后台测试数据空白");

                        }
                    });
                    if (mHintFlg) {
                        hintCarInfoLL.setVisibility(View.GONE);
                        mHintFlg = false;
                    } else {
                        hintCarInfoLL.setVisibility(View.VISIBLE);
                        mHintFlg = true;
                    }
                }

            };
            mListRV.setLayoutManager(new LinearLayoutManager(mActivity));
            mListRV.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void showDeletePopWindow(final String car_id) {
        if (mDeletePW != null && mDeletePW.isShowing()) {
            mDeletePW.dismiss();
        } else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.popwindow_two_operate_car, null, false);
            view.findViewById(R.id.tv_pop_foot_score_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDeletePW.dismiss();
                }
            });
            view.findViewById(R.id.tv_pop_foot_score_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCarInfo(car_id);
                    mDeletePW.dismiss();
                }
            });
            mDeletePW = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mDeletePW.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
        }
    }

    private void deleteCarInfo(String car_id) {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("car_id", car_id);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).deleteCar(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    ToastUtil.showLong(mActivity, getString(R.string.data_success));
                    if (code == 0) {
//                        EventBus.getDefault().post(UpdatCarInfoEvent.class);
                        mCarBeanList.clear();
                        getCarInfo();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                if (!NetworkUtil.isConnected()) {
                    ToastUtil.showShort(mActivity, R.string.not_net);
                } else {
                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
                }
            }
        });
    }

    @Override
    public void initEvent() {
        mTvActivityUserCarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.ll_layout_top_back_bar_back, R.id.tv_activity_user_car_add, R.id.tv_activity_user_car_question, R.id.ll_activity_car_info_question, R.id.tv_contact_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_layout_top_back_bar_back:
                finish();
                break;
            case R.id.tv_activity_user_car_add:
                AddCarActivity1.open(mActivity);
                break;
            case R.id.tv_activity_user_car_question:
                QuestionActivity.open(mActivity, REQUEST_CODE_ADD);
                break;
            case R.id.ll_activity_car_info_question:
                QuestionActivity.open(mActivity, REQUEST_CODE_INFO);
                break;
            case R.id.tv_contact_service:
                showExpectPopWindow();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateCarInfoEvent event) {
        getCarInfo();
    }

    /**
     * 敬请期待弹窗
     */
    private void showExpectPopWindow() {
        if (mExpectPopwindow != null && mExpectPopwindow.isShowing()) {
            mExpectPopwindow.dismiss();
        } else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.pop_window_expect_load, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mExpectPopwindow != null && mExpectPopwindow.isShowing()) {
                        mExpectPopwindow.dismiss();
                    }
                }
            });
            mExpectPopwindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

            mExpectPopwindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mActivity);
    }
}
