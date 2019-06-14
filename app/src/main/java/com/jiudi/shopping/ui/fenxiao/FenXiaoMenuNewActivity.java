package com.jiudi.shopping.ui.fenxiao;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.user.account.FenXiaoAccountActivity;
import com.jiudi.shopping.ui.user.account.TiXianActivity;
import com.jiudi.shopping.util.SPUtil;
import com.m7.imkfsdk.KfStartHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FenXiaoMenuNewActivity extends BaseActivity {
    private ImageView head;
    private TextView name;
    private TextView level;
    private TextView moneyt;
    private TextView money;
    private TextView tixian;
    private TextView weidaozhangyongjin;
    private TextView leijihuode;
    private TextView leijiyi;
    private TextView yongjinmingxi;
    private TextView tuandui;
    private TextView weidaozhangyongjinv;
    private TextView leijihuodev;
    private TextView leijiyiv;
    private TextView yongjinmingxiv;
    private TextView tuanduiv;
    private LinearLayout yongjinmingxil;
    private ImageView back;
    private TextView code;
    private LinearLayout yaoqingrenl;
    private TextView yaoqingren;
    private LinearLayout tuanduil;
    private LinearLayout haohuol;
    private TextView haohuo;
    private TextView haohuov;
    private KfStartHelper helper;
    private static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 1001;
    private static final int REQUEST_CODE_UNKNOWN_APP = 100;
    private static final int REQUEST_CODE_OPENCHAT = 60;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_fenxiaob;
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
        tuandui = (TextView) findViewById(R.id.tuandui);
        weidaozhangyongjinv = (TextView) findViewById(R.id.weidaozhangyongjinv);
        leijihuodev = (TextView) findViewById(R.id.leijihuodev);
        leijiyiv = (TextView) findViewById(R.id.leijiyiv);
        yongjinmingxiv = (TextView) findViewById(R.id.yongjinmingxiv);
        tuanduiv = (TextView) findViewById(R.id.tuanduiv);
        yongjinmingxil = (LinearLayout) findViewById(R.id.yongjinmingxil);
        back = (ImageView) findViewById(R.id.back);
        code = (TextView) findViewById(R.id.code);
        yaoqingrenl = (LinearLayout) findViewById(R.id.yaoqingrenl);
        yaoqingren = (TextView) findViewById(R.id.yaoqingren);
        tuanduil = (LinearLayout) findViewById(R.id.tuanduil);
        haohuol = (LinearLayout) findViewById(R.id.haohuol);
        haohuo = (TextView) findViewById(R.id.haohuo);
        haohuov = (TextView) findViewById(R.id.haohuov);
    }

    @Override
    public void initData() {

        helper = new KfStartHelper(mActivity);
        RequestOptions requestOptions = RequestOptions.circleCropTransform().error(R.drawable.head_defuat_circle);
        Glide.with(mActivity).load(AccountManager.sUserBean.avatar).apply(requestOptions).into(head);
        code.setText("邀请码:"+AccountManager.sUserBean.uid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFenXiao();
    }

    @Override
    public void initEvent() {
        yongjinmingxil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, FenXiaoAccountActivity.class));
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
        tuanduil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, TuanDuiActivity.class));

            }
        });
        haohuol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.initSdkChat("e183f850-6650-11e9-b942-bf7a16e827df", "咨询", AccountManager.sUserBean.uid,REQUEST_CODE_OPENCHAT);//陈辰正式
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
                        level.setText(""+data.getJSONObject("agent").getString("name"));
                        name.setText(""+AccountManager.sUserBean.nickname);
                        weidaozhangyongjinv.setText(data.getString("number")+"元");
                        leijihuodev.setText(data.getString("allnumber")+"元");
                        leijiyiv.setText(data.getString("extractNumber")+"元");
//                        zhishuv.setText(data.getJSONObject("userInfo").getString("direct_num")+"人");
//                        tuanduiv.setText(data.getJSONObject("userInfo").getString("team_num")+"人");
                        try {
                            yaoqingren.setText(data.getJSONObject("userInfo").getJSONObject("spread_name").getString("nickname").replace("null",""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(mActivity,info,Toast.LENGTH_SHORT).show();
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
