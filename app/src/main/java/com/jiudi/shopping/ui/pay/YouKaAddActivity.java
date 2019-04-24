package com.jiudi.shopping.ui.pay;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class YouKaAddActivity extends BaseActivity {


    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private TwinklingRefreshLayout tflActivityParkLot;
    private TextView tvActivityLoginLoginLogin;
    private android.widget.EditText et;
    private TextView tvLayoutTopBackBarStart;
    private android.widget.CheckBox needcheck;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ykadd;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        tflActivityParkLot = (TwinklingRefreshLayout) findViewById(R.id.tfl_activity_park_lot);
        tvActivityLoginLoginLogin = (TextView) findViewById(R.id.tv_activity_login_login_login);
        et = (EditText) findViewById(R.id.et);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        needcheck = (CheckBox) findViewById(R.id.needcheck);
        tvLayoutTopBackBarTitle.setText("添加油卡");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        tvActivityLoginLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(needcheck.isChecked()){

                    addYouKa();
                }else{
                    Toast.makeText(mActivity,"请同意协议",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void addYouKa(){
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("card_number",et.getText().toString());
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).addYouKaCard(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        Toast.makeText(getBaseContext(),"添加成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(getBaseContext(),"添加失败请检查油卡卡号",Toast.LENGTH_SHORT).show();
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
