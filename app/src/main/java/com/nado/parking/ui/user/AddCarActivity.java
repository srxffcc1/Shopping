package com.nado.parking.ui.user;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nado.parking.R;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.event.UpdateCarInfoEvent;
import com.nado.parking.manager.AccountManager;
import com.nado.parking.manager.RequestManager;
import com.nado.parking.net.RetrofitCallBack;
import com.nado.parking.net.RetrofitRequestInterface;
import com.nado.parking.util.DialogUtil;
import com.nado.parking.util.NetworkUtil;
import com.nado.parking.util.ToastUtil;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class AddCarActivity extends BaseActivity {
    private static final String TAG = "AddCarActivity";
    @BindView(R.id.ll_layout_top_back_bar_back)
    LinearLayout mLlLayoutTopBackBarBack;
    @BindView(R.id.tv_layout_top_back_bar_title)
    TextView mTvLayoutTopBackBarTitle;
    @BindView(R.id.tv_layout_back_top_bar_operate)
    TextView mTvLayoutBackTopBarOperate;
    @BindView(R.id.rl_layout_top_back_bar)
    RelativeLayout mRlLayoutTopBackBar;
    @BindView(R.id.et_activity_add_car_1)
    EditText mEtActivityAddCar1;
    @BindView(R.id.et_activity_add_car_2)
    EditText mEtActivityAddCar2;
    @BindView(R.id.et_activity_add_car_3)
    EditText mEtActivityAddCar3;
    @BindView(R.id.et_activity_add_car_4)
    EditText mEtActivityAddCar4;
    @BindView(R.id.et_activity_add_car_5)
    EditText mEtActivityAddCar5;
    @BindView(R.id.et_activity_add_car_6)
    EditText mEtActivityAddCar6;
    @BindView(R.id.et_activity_add_car_7)
    EditText mEtActivityAddCar7;
    @BindView(R.id.et_activity_add_car_8)
    EditText mEtActivityAddCar8;
    @BindView(R.id.tv_activity_add_car_save)
    TextView mTvActivityAddCarSave;
    private String mCarPlate = "";
    private PopupWindow mStopPopwindow;
    private boolean mPlateStatus = false;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_car;
    }
    @Override
    public void initView() {
        mTvLayoutTopBackBarTitle.setText(R.string.car_add);
    }
    @Override
    public void initData() {
    }
    @Override
    public void initEvent() {
        mEtActivityAddCar1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (!mEtActivityAddCar1.getText().toString().equals("")) {
                    mEtActivityAddCar2.requestFocus();
                }
                if (!TextUtils.isEmpty(editable.toString())) {
                    mPlateStatus = true;
                } else {
                    mPlateStatus = false;
                }
            }
        });
        mEtActivityAddCar2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (!mEtActivityAddCar2.getText().toString().equals("")) {
                    mEtActivityAddCar3.requestFocus();
                } else {
                    mEtActivityAddCar1.requestFocus();
                }
                if (!TextUtils.isEmpty(editable.toString())) {
                    mPlateStatus = true;
                } else {
                    mPlateStatus = false;
                }
            }
        });
        mEtActivityAddCar3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (!mEtActivityAddCar3.getText().toString().equals("")) {
                    mEtActivityAddCar4.requestFocus();
                } else {
                    mEtActivityAddCar2.requestFocus();
                }
                if (!TextUtils.isEmpty(editable.toString())) {
                    mPlateStatus = true;
                } else {
                    mPlateStatus = false;
                }
            }
        });
        mEtActivityAddCar4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (!mEtActivityAddCar4.getText().toString().equals("")) {
                    mEtActivityAddCar5.requestFocus();
                } else {
                    mEtActivityAddCar3.requestFocus();
                }
                if (!TextUtils.isEmpty(editable.toString())) {
                    mPlateStatus = true;
                } else {
                    mPlateStatus = false;
                }
            }
        });
        mEtActivityAddCar5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (!mEtActivityAddCar5.getText().toString().equals("")) {
                    mEtActivityAddCar6.requestFocus();
                } else {
                    mEtActivityAddCar4.requestFocus();
                }
                if (!TextUtils.isEmpty(editable.toString())) {
                    mPlateStatus = true;
                } else {
                    mPlateStatus = false;
                }
            }
        });
        mEtActivityAddCar6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (!mEtActivityAddCar6.getText().toString().equals("")) {
                    mEtActivityAddCar7.requestFocus();
                } else {
                    mEtActivityAddCar5.requestFocus();
                }
                if (!TextUtils.isEmpty(editable.toString())) {
                    mPlateStatus = true;
                } else {
                    mPlateStatus = false;
                }
            }
        });
        mEtActivityAddCar7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (!mEtActivityAddCar7.getText().toString().equals("")) {
//                    mEtActivityAddCar8.requestFocus();
                } else {
                    mEtActivityAddCar6.requestFocus();
                }
                if (!TextUtils.isEmpty(editable.toString())) {
                    mPlateStatus = true;
                } else {
                    mPlateStatus = false;
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (mPlateStatus) {
            showStopPopWindow();
        } else {
            finish();
            super.onBackPressed();
        }
    }
    /**
     * 得到输入的车牌号
     */
    private void getInputPlate() {
        String mPlate1 = mEtActivityAddCar1.getText().toString().trim();
        String mPlate2 = mEtActivityAddCar2.getText().toString().trim();
        String mPlate3 = mEtActivityAddCar3.getText().toString().trim();
        String mPlate4 = mEtActivityAddCar4.getText().toString().trim();
        String mPlate5 = mEtActivityAddCar5.getText().toString().trim();
        String mPlate6 = mEtActivityAddCar6.getText().toString().trim();
        String mPlate7 = mEtActivityAddCar7.getText().toString().trim();
        String mPlate8 = mEtActivityAddCar8.getText().toString().trim();
        if (TextUtils.isEmpty(mPlate1)) {
            ToastUtil.showLong(mActivity, getString(R.string.car_plate_input_null));
        } else if (TextUtils.isEmpty(mPlate2)) {
            ToastUtil.showLong(mActivity, getString(R.string.car_plate_input_null));
        } else if (TextUtils.isEmpty(mPlate3)) {
            ToastUtil.showLong(mActivity, getString(R.string.car_plate_input_null));
        } else if (TextUtils.isEmpty(mPlate4)) {
            ToastUtil.showLong(mActivity, getString(R.string.car_plate_input_null));
        } else if (TextUtils.isEmpty(mPlate5)) {
            ToastUtil.showLong(mActivity, getString(R.string.car_plate_input_null));
        } else if (TextUtils.isEmpty(mPlate6)) {
            ToastUtil.showLong(mActivity, getString(R.string.car_plate_input_null));
        } else if (TextUtils.isEmpty(mPlate7)) {
            ToastUtil.showLong(mActivity, getString(R.string.car_plate_input_null));
        } else {
            mCarPlate = TextUtils.concat(mPlate1, mPlate2, mPlate3, mPlate4, mPlate5, mPlate6, mPlate7, mPlate8).toString();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    @OnClick({R.id.ll_layout_top_back_bar_back, R.id.tv_activity_add_car_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_layout_top_back_bar_back:
                if (mPlateStatus) {
                    showStopPopWindow();
                } else {
                    finish();
                }
                break;
            case R.id.tv_activity_add_car_save:
                getInputPlate();
                DialogUtil.showProgress(mActivity);
                addMyCard(mCarPlate);
                break;
        }
    }
    /**
     * 添加车辆
     *
     * @param carPlate
     */
    private void addMyCard(String carPlate) {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("plate", mCarPlate);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).addCar(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                DialogUtil.hideProgress();
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        ToastUtil.showLong(mActivity, info);
                        EventBus.getDefault().post(new UpdateCarInfoEvent());
                        finish();
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
    private void showStopPopWindow() {
        if (mStopPopwindow != null && mStopPopwindow.isShowing()) {
            mStopPopwindow.dismiss();
        } else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.popwindow_confirm_pay, null, false);
            TextView continueCancel = (TextView) view.findViewById(R.id.tv_popwindow_pay_cancel);
            TextView finishConfirm = (TextView) view.findViewById(R.id.tv_popwindow_pay_now_pay);
            TextView lastUnpaid = view.findViewById(R.id.tv_popwindow_pay_last_unpaid);
            finishConfirm.setText(R.string.exit_login_sure);
            continueCancel.setText(R.string.continue_write);
            lastUnpaid.setText(R.string.adding_car_exit);
            continueCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mStopPopwindow.dismiss();
                }
            });
            finishConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mStopPopwindow.dismiss();
                    finish();
                }
            });
            mStopPopwindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mStopPopwindow.setBackgroundDrawable(new BitmapDrawable());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStopPopwindow != null && mStopPopwindow.isShowing()) {
                        mStopPopwindow.dismiss();
                    }
                }
            });
            mStopPopwindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
        }
    }
    public static void open(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, AddCarActivity.class);
        activity.startActivity(intent);
    }
    @OnClick(R.id.et_activity_add_car_8)
    public void onViewClicked() {
        mEtActivityAddCar8.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.square_car_num));
    }
}