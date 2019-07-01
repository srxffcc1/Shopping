package com.jiudi.shopping.ui.user.account;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aykj.mustinsert.MustInsert;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WorkEachActivity extends BaseActivity {
    private android.widget.RelativeLayout rlLayoutTopBackBar;
    private android.widget.LinearLayout llLayoutTopBackBarBack;
    private android.widget.ImageView back;
    private android.widget.TextView tvLayoutTopBackBarStart;
    private android.widget.TextView tvLayoutTopBackBarTitle;
    private android.widget.TextView tvLayoutTopBackBarEnd;
    private android.widget.TextView tvLayoutBackTopBarOperate;
    private android.widget.EditText comName;
    private android.widget.LinearLayout llActivityUserSetPhone;
    private android.widget.EditText name;
    private android.widget.EditText phone;
    private android.widget.EditText modelType;
    private android.widget.EditText content;
    private android.widget.LinearLayout passphone;
    private android.widget.TextView save;
    private TextView passphonet;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_work;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        back = (ImageView) findViewById(R.id.back);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        comName = (EditText) findViewById(R.id.com_name);
        llActivityUserSetPhone = (LinearLayout) findViewById(R.id.ll_activity_user_set_phone);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        modelType = (EditText) findViewById(R.id.model_type);
        content = (EditText) findViewById(R.id.content);
        passphone = (LinearLayout) findViewById(R.id.passphone);
        save = (TextView) findViewById(R.id.save);
        passphonet = (TextView) findViewById(R.id.passphonet);
        tvLayoutTopBackBarTitle.setText("我有好货");
    }

    @Override
    public void initData() {

    }

    private void saveEach() {
        Map<String, String> map = new HashMap<>();
        map.put("model_type",modelType.getText().toString());
        map.put("com_name",comName.getText().toString());
        map.put("name",name.getText().toString());
        map.put("phone",phone.getText().toString());
        map.put("content",content.getText().toString());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).saveEach(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    Toast.makeText(mActivity,info,Toast.LENGTH_SHORT).show();
                    if (code == 200) {
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {

                Toast.makeText(mActivity,"后台出错",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void initEvent() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MustInsert.checkAllText(mActivity,modelType,comName,name,phone)){

                    saveEach();
                }
            }
        });
        passphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + passphonet.getText().toString());
                intent.setData(data);
                startActivity(intent);
            }
        });
    }
}
