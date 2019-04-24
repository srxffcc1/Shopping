package com.jiudi.shopping.ui.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.event.UpdateCarInfoEvent;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.KeyboardUtil1;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCarActivity1 extends BaseActivity {
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

    private KeyboardUtil1 mKeyboardUtil;

    private BroadcastReceiver receiver;
    public static final String INPUT_LICENSE_COMPLETE = "licensekeyboard";
    public static final String INPUT_LICENSE_KEY = "license";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_car;
    }

    @Override
    public void initView() {
        mTvLayoutTopBackBarTitle.setText(R.string.car_add);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initData() {
        initKeyBoard();
        //输入车牌完成后的intent过滤器
        IntentFilter finishFilter = new IntentFilter(INPUT_LICENSE_COMPLETE);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String license = intent.getStringExtra(INPUT_LICENSE_KEY);
                LogUtil.e(TAG, "++++++++++++++" + license);
                if (license != null && license.length() > 0) {
                    if (TextUtils.equals(license, "start")) {
                        mPlateStatus = true;
                    } else {
                        mPlateStatus = false;
                    }
                }
//                mActivity.unregisterReceiver(this);
            }
        };
        registerReceiver(receiver, finishFilter);
    }

    private void initKeyBoard() {
        if (mKeyboardUtil == null) {
            mKeyboardUtil = new KeyboardUtil1(mActivity, new EditText[]{mEtActivityAddCar1, mEtActivityAddCar2, mEtActivityAddCar3, mEtActivityAddCar4, mEtActivityAddCar5, mEtActivityAddCar6, mEtActivityAddCar7, mEtActivityAddCar8});
            mKeyboardUtil.showKeyboard();
        } else {
            mKeyboardUtil.showKeyboard();
        }
    }


    @Override
    public void initEvent() {
        mEtActivityAddCar1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                disableShowInput(mEtActivityAddCar1);
                mKeyboardUtil.setCurrentPosition(0);
                return false;
            }
        });
        mEtActivityAddCar2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                disableShowInput(mEtActivityAddCar2);
                mKeyboardUtil.setCurrentPosition(1);
                return false;
            }
        });
        mEtActivityAddCar3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                disableShowInput(mEtActivityAddCar3);
                mKeyboardUtil.setCurrentPosition(2);
                return false;
            }
        });
        mEtActivityAddCar4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                disableShowInput(mEtActivityAddCar4);
                mKeyboardUtil.setCurrentPosition(3);
                return false;
            }
        });
        mEtActivityAddCar5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                disableShowInput(mEtActivityAddCar5);
                mKeyboardUtil.setCurrentPosition(4);
                return false;
            }
        });
        mEtActivityAddCar6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                disableShowInput(mEtActivityAddCar6);
                mKeyboardUtil.setCurrentPosition(5);
                return false;
            }
        });
        mEtActivityAddCar7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                disableShowInput(mEtActivityAddCar7);
                mKeyboardUtil.setCurrentPosition(6);
                return false;
            }
        });
        mEtActivityAddCar8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                disableShowInput(mEtActivityAddCar8);
                mKeyboardUtil.setCurrentPosition(7);
                return false;
            }
        });
    }

    public void disableShowInput(EditText editText) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {//TODO: handle exception
            }
            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {//TODO: handle exception
            }
        }
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
            LogUtil.e(TAG, mCarPlate);
        }
        if (!TextUtils.isEmpty(mCarPlate)) {
            DialogUtil.showProgress(mActivity);
            addMyCard(mCarPlate);
        } else {
            ToastUtil.showShort(mActivity, R.string.car_plate_input_null);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        try {
            unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        super.onPause();
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
        map.put("plate", carPlate);
        LogUtil.e(TAG, map.toString());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).addCar(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                DialogUtil.hideProgress();
                LogUtil.e(TAG, response);
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

    /**
     * 隐藏键盘输入
     */
    private void hideKeyword(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
    }


    public static void open(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, AddCarActivity1.class);
        activity.startActivity(intent);
    }

    @OnClick(R.id.et_activity_add_car_8)
    public void onViewClicked() {
        mEtActivityAddCar8.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.square_car_num));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
