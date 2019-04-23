package com.nado.parking.ui.pay;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.nado.parking.R;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.manager.AccountManager;
import com.nado.parking.manager.RequestManager;
import com.nado.parking.net.RetrofitCallBack;
import com.nado.parking.net.RetrofitRequestInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShuiBindActivity extends BaseActivity {
    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private LinearLayout passjigou;
    private EditText et;
    private CheckBox check;
    private TextView tip;
    private TextView save;
    private TextView pleasechose;
    private String product_id;
    private TextView tvLayoutTopBackBarStart;
    private TextView whichfei;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_sdbind;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        passjigou = (LinearLayout) findViewById(R.id.passjigou);
        et = (EditText) findViewById(R.id.et);
        check = (CheckBox) findViewById(R.id.check);
        tip = (TextView) findViewById(R.id.tip);
        save = (TextView) findViewById(R.id.save);

        pleasechose = (TextView) findViewById(R.id.pleasechose);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        whichfei = (TextView) findViewById(R.id.whichfei);
        tvLayoutTopBackBarTitle.setText("绑定缴费账户");
    }

    @Override
    public void initData() {
        whichfei.setText("水费");
    }

    @Override
    public void initEvent() {

        passjigou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivityForResult(new Intent(v.getContext(), ShuiChoseJGActivity.class).putExtra("type",getIntent().getStringExtra("type")),100);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bind();
            }
        });

    }

    private void bind() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("product_id",product_id);
        map.put("wecaccount",et.getText().toString());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).bindShuiHu(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        Toast.makeText(getBaseContext(),"绑定成功",Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if(resultCode== Activity.RESULT_OK){
                pleasechose.setText(data.getStringExtra("company"));
                product_id=data.getStringExtra("id");
            }
        }
    }
}
