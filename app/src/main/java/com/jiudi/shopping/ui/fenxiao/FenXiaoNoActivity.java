package com.jiudi.shopping.ui.fenxiao;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.cart.CartDetailActivity;
import com.jiudi.shopping.util.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FenXiaoNoActivity extends BaseActivity {


    private ImageView passlibao;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_fenxiaono;
    }

    @Override
    public void initView() {

        passlibao = (ImageView) findViewById(R.id.passlibao);
    }

    @Override
    public void initData() {
        startActivity(new Intent(mActivity, CartDetailActivity.class).putExtra("id","4"));
        finish();
    }

    @Override
    public void initEvent() {
//        passlibao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mActivity, CartDetailActivity.class).putExtra("id","4"));
//            }
//        });
    }
}
