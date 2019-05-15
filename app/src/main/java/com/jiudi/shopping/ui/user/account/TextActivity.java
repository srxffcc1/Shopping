package com.jiudi.shopping.ui.user.account;

import android.text.Html;
import android.widget.TextView;

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

public class TextActivity extends BaseActivity {
    private android.widget.TextView need;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_about_uslink;
    }

    @Override
    public void initView() {

        need = (TextView) findViewById(R.id.need);
    }

    @Override
    public void initData() {
        getUrl();
    }

    @Override
    public void initEvent() {

    }
    private void getUrl() {
        Map<String, String> map = new HashMap<>();

        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).auto(SPUtil.get("head", "").toString(),getIntent().getStringExtra("url")+"",RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        String content=res.getJSONObject("data").getJSONObject("content").getString("content");
                        need.setText(Html.fromHtml(content));
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
