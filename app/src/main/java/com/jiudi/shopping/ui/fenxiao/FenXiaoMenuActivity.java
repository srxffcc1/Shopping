package com.jiudi.shopping.ui.fenxiao;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.user.account.AccountActivity;
import com.jiudi.shopping.ui.user.account.TiXianActivity;
import com.jiudi.shopping.util.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FenXiaoMenuActivity extends BaseActivity {
    private android.widget.ImageView head;
    private android.widget.TextView name;
    private android.widget.TextView level;
    private android.widget.TextView moneyt;
    private android.widget.TextView money;
    private android.widget.TextView tixian;
    private android.widget.TextView weidaozhangyongjin;
    private android.widget.TextView leijihuode;
    private android.widget.TextView leijiyi;
    private android.widget.TextView yongjinmingxi;
    private android.widget.TextView zhishu;
    private android.widget.TextView tuandui;
    private android.widget.TextView weidaozhangyongjinv;
    private android.widget.TextView leijihuodev;
    private android.widget.TextView leijiyiv;
    private android.widget.TextView yongjinmingxiv;
    private android.widget.TextView zhishuv;
    private android.widget.TextView tuanduiv;
    private android.widget.LinearLayout yongjinmingxil;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_fenxiao;
    }

    @Override
    public void initView() {

        head = (ImageView) findViewById(R.id.head);
        name = (TextView) findViewById(R.id.name);
        level = (TextView) findViewById(R.id.level);
        moneyt = (TextView) findViewById(R.id.moneyt);
        money = (TextView) findViewById(R.id.money);
        tixian = (TextView) findViewById(R.id.tixian);
        weidaozhangyongjin = (TextView) findViewById(R.id.weidaozhangyongjin);
        leijihuode = (TextView) findViewById(R.id.leijihuode);
        leijiyi = (TextView) findViewById(R.id.leijiyi);
        yongjinmingxi = (TextView) findViewById(R.id.yongjinmingxi);
        zhishu = (TextView) findViewById(R.id.zhishu);
        tuandui = (TextView) findViewById(R.id.tuandui);
        weidaozhangyongjinv = (TextView) findViewById(R.id.weidaozhangyongjinv);
        leijihuodev = (TextView) findViewById(R.id.leijihuodev);
        leijiyiv = (TextView) findViewById(R.id.leijiyiv);
        yongjinmingxiv = (TextView) findViewById(R.id.yongjinmingxiv);
        zhishuv = (TextView) findViewById(R.id.zhishuv);
        tuanduiv = (TextView) findViewById(R.id.tuanduiv);
        yongjinmingxil = (LinearLayout) findViewById(R.id.yongjinmingxil);
    }

    @Override
    public void initData() {

        getFenXiao();
        RequestOptions requestOptions = RequestOptions.circleCropTransform().error(R.drawable.head_defuat_circle);
        Glide.with(mActivity).load(AccountManager.sUserBean.avatar).apply(requestOptions).into(head);
    }

    @Override
    public void initEvent() {
        yongjinmingxil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, AccountActivity.class));
            }
        });
//        yongjinmingxi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(mActivity, AccountActivity.class));
//            }
//        });
        tixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, TiXianActivity.class));

            }
        });
    }
    private void getFenXiao() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getFenXiao(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        JSONObject data=res.getJSONObject("data");
                        money.setText(data.getJSONObject("userInfo").getString("now_money")+"元");
                        level.setText(data.getJSONObject("agent").getString("name"));
                        name.setText(AccountManager.sUserBean.nickname);
                        weidaozhangyongjinv.setText(data.getString("number")+"元");
                        leijihuodev.setText(data.getString("allnumber")+"元");
                        leijiyiv.setText(data.getString("extractNumber")+"元");
                        zhishuv.setText(data.getJSONObject("userInfo").getString("direct_num")+"人");
                        tuanduiv.setText(data.getJSONObject("userInfo").getString("team_num")+"人");
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
