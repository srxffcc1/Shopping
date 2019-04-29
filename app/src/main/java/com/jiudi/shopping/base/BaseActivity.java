package com.jiudi.shopping.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.jiudi.shopping.R;
import com.jiudi.shopping.global.LocalApplication;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.user.account.LoginActivity;
import com.jiudi.shopping.util.ActivityUtil;
import com.jiudi.shopping.util.OSUtil;
import com.jiudi.shopping.util.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;


/**
 * Created by licrynoob on 2016/guide_2/12 <br>
 * Copyright (C) 2016 <br>
 * Email:licrynoob@gmail.com <p>
 * BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    protected Context mContext;
    protected Activity mActivity;
    protected LocalApplication mApp;
    protected ActivityUtil mActivityUtil;
    protected LayoutInflater mInflater;

    private Handler mHandler;
    public boolean isNoNeedLogin(){
        return false;
    }


    private void getTestList() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getCanPhonePay(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {

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



    /* @author suncat
     * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     * @return
     */
//    public  final boolean ping() {
//
//        String result = null;
//        try {
//            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
//            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
//            // 读取ping的内容，可以不加
//            InputStream input = p.getInputStream();
//            BufferedReader in = new BufferedReader(new InputStreamReader(input));
//            StringBuffer stringBuffer = new StringBuffer();
//            String content = "";
//            while ((content = in.readLine()) != null) {
//                stringBuffer.append(content);
//            }
//            Log.d("------ping-----", "result content : " + stringBuffer.toString());
//            // ping的状态
//            int status = p.waitFor();
//            if (status == 0) {
//                result = "success";
//                return true;
//            } else {
//                result = "failed";
//            }
//        } catch (IOException e) {
//            result = "IOException";
//        } catch (InterruptedException e) {
//            result = "InterruptedException";
//        } finally {
//            Log.d("----result---", "result = " + result);
//        }
//        return false;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mActivity = this;
        mApp = LocalApplication.getInstance();
        mInflater = LayoutInflater.from(this);
        mActivityUtil = ActivityUtil.getInstance();
        mActivityUtil.addActivity(this);

        setSystemUi();
        initInstanceState(savedInstanceState);
        beforeSetContentView();
        if(getContentViewId()!=-1){

            setContentView(getContentViewId());
        }
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
        if(findViewById(R.id.ll_layout_top_back_bar_back)!=null){
            findViewById(R.id.ll_layout_top_back_bar_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void startActivity(Intent intent) {
//        if (AccountManager.sUserBean == null&&!isNoNeedLogin()) {
//            super.startActivity(new Intent(mActivity, LoginActivity.class));
//            return;
//        }
        super.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (AccountManager.sUserBean == null&&!isNoNeedLogin()) {
            super.startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mActivityUtil.removeActivity(this);
    }

    /**
     * 设置系统界面
     */
    protected void setSystemUi() {

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(mContext, R.color.colorTopBar));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(ContextCompat.getColor(mContext, R.color.colorTopBar));
        }

        if (OSUtil.getRomType() == OSUtil.ROM_TYPE.MIUI) {
            StatusBarUtil.MIUISetStatusBarLightMode(this.getWindow(), true);
        } else if (OSUtil.getRomType() == OSUtil.ROM_TYPE.FLYME) {
            StatusBarUtil.FlymeSetStatusBarLightMode(this.getWindow(), true);
        }
    }

    protected final Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(getMainLooper());
        }
        return mHandler;
    }

    /**
     * 初始化保存的数据
     */
    protected void initInstanceState(Bundle savedInstanceState) {

    }

    /**
     * setContentView之前调用
     */
    protected void beforeSetContentView() {

    }

    /**
     * 获取布局Id
     *
     * @return
     */
    protected abstract int getContentViewId();

    /**
     * 通过id初始化View
     *
     * @param viewId
     * @param <T>
     * @return
     */
    protected <T extends View> T byId(int viewId) {
        return (T) findViewById(viewId);
    }


    public Map<String,String> getFieleMap(Object object){
        Map<String,String> result=new HashMap<>();

        Field[ ] fields = object.getClass().getDeclaredFields( );
        for ( Field field : fields )
        {
            // 如果不为空，设置可见性，然后返回
            field.setAccessible( true );
            try
            {
                // 设置字段可见，即可用get方法获取属性值。
                 result.put(field.getName( ),field.get( object ).toString());
            }
            catch ( Exception e )
            {
                // System.out.println("error--------"+methodName+".Reason is:"+e.getMessage());
            }
        }
        return result;
    }
    public  Intent buildIntent(Intent intent, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        return intent;
    }
    public  Intent buildIntent(Intent intent, Object object) {

        return buildIntent(intent, getFieleMap(object));
    }
}
