package com.nado.shopping.ui.device;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.nado.shopping.R;
import com.nado.shopping.base.BaseActivity;
import com.nado.shopping.manager.AccountManager;
import com.nado.shopping.manager.RequestManager;
import com.nado.shopping.net.RetrofitCallBack;
import com.nado.shopping.net.RetrofitRequestInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeviceListActivity extends BaseActivity {
    private android.widget.RelativeLayout rlLayoutTopBackBar;
    private android.widget.LinearLayout llLayoutTopBackBarBack;
    private android.widget.TextView tvLayoutTopBackBarStart;
    private android.widget.TextView tvLayoutTopBackBarTitle;
    private android.widget.TextView tvLayoutTopBackBarEnd;
    private android.widget.TextView tvLayoutBackTopBarOperate;
    private android.widget.RelativeLayout hasobd;
    private android.widget.TextView obdid;
    private android.widget.TextView obdmacid;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device_list;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        hasobd = (RelativeLayout) findViewById(R.id.hasobd);
        obdid = (TextView) findViewById(R.id.obdid);
        obdmacid = (TextView) findViewById(R.id.obdmacid);
        tvLayoutTopBackBarTitle.setText("我的设备");
        tvLayoutTopBackBarEnd.setText("添加");
    }

    @Override
    public void initData() {
        if (AccountManager.sUserBean != null) {
            if(AccountManager.sUserBean.obd_macid!=null&&!"".equals(AccountManager.sUserBean.obd_macid)){
                hasobd.setVisibility(View.VISIBLE);
                tvLayoutTopBackBarEnd.setVisibility(View.GONE);
                obdmacid.setText(AccountManager.sUserBean.obd_macid);
            }
        }
    }

    @Override
    public void initEvent() {
        hasobd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StyledDialog.buildIosAlert("操作", "是否解除绑定", new MyDialogListener() {
                    @Override
                    public void onFirst() {
                        unbind();
                    }

                    @Override
                    public void onSecond() {

                    }
                }).setBtnText("确定","取消").show();
            }
        });
        tvLayoutTopBackBarEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, DeviceBindActivity.class));
            }
        });
    }

    private void unbind() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("obd_macid", AccountManager.sUserBean.obd_macid);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).unbindOBD(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        finish();
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
