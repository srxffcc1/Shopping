package com.jiudi.shopping.ui.main;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hss01248.dialog.StyledDialog;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.vp.VpFragmentAdapter;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.UserBean;
import com.jiudi.shopping.event.CloseMainEvent;
import com.jiudi.shopping.event.FinishEvent;
import com.jiudi.shopping.global.Constant;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.service.AppUpdateService;
import com.jiudi.shopping.ui.fenxiao.FenXiaoMenuActivity;
import com.jiudi.shopping.ui.fenxiao.FenXiaoNoActivity;
import com.jiudi.shopping.ui.user.account.LoginActivity;
import com.jiudi.shopping.util.CommonUtil;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.widget.NoTouchViewPager;
import com.jiudi.shopping.widget.SpecialTab;
import com.jiudi.shopping.widget.SpecialTabRound;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.listener.SimpleTabItemSelectedListener;

public class MainNewOldActivity extends BaseActivity {

    private FrameLayout activityMaterialDesign;
    private NoTouchViewPager viewPager;
    private PageNavigationView tab;
    private VpFragmentAdapter mMainAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private boolean isdianzhu=false;
    private NavigationController navigationController;
    private int oldindex=0;
    private Dialog dialog;
    /**
     * APK下载URL
     */
    private String mDownloadUrl;
    private PopupWindow mAppUpdatePopupWindow;
    private PopupWindow mUpdateProgressPopupWindow;
    private TextView mProgressTV;
    private ProgressBar mUpdatePB;
    private static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 1001;
    private static final int REQUEST_CODE_UNKNOWN_APP = 100;
    private static final int REQUEST_CODE_OPENCHAT = 60;
    private static final String TAG = "MainActivity";
    @Override
    protected int getContentViewId() {
        return R.layout.activity_new_main;
    }

    @Override
    public void initView() {

        EventBus.getDefault().register(mActivity);
        activityMaterialDesign = (FrameLayout) findViewById(R.id.activity_material_design);
        viewPager = (NoTouchViewPager) findViewById(R.id.viewPager);
        tab = (PageNavigationView) findViewById(R.id.tab);

    }

    @Override
    public void initData() {
        autoLogin(true);
    }
    private void autoLogin(boolean b) {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getPersonalDate(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        JSONObject jsonObject=res.getJSONObject("data").optJSONObject("user_info");
                        if(jsonObject!=null){
                            UserBean bean;
                            if(AccountManager.sUserBean!=null){
                                bean=AccountManager.sUserBean;
                            }else{

                                bean=new UserBean();
                            }
                            bean.uid=jsonObject.optString("uid");
                            bean.account=jsonObject.optString("account");
                            bean.pwd=jsonObject.optString("pwd");
                            bean.nickname=jsonObject.optString("nickname");
                            bean.avatar=jsonObject.optString("avatar");
                            bean.phone=jsonObject.optString("phone");
                            bean.add_time=jsonObject.optString("add_time");
                            bean.add_ip=jsonObject.optString("add_ip");
                            bean.last_time=jsonObject.optString("last_time");
                            bean.last_ip=jsonObject.optString("last_ip");
                            bean.now_money=jsonObject.optString("now_money");
                            bean.integral=jsonObject.optString("integral");
                            bean.status=jsonObject.optString("status");
                            bean.level=jsonObject.optString("level");
                            bean.spread_uid=jsonObject.optString("spread_uid");
                            bean.agent_id=jsonObject.optString("agent_id");
                            bean.user_type=jsonObject.optString("user_type");
                            bean.is_promoter=jsonObject.optString("is_promoter");
                            bean.pay_count=jsonObject.optString("pay_count");
                            bean.direct_num=jsonObject.optString("direct_num");
                            bean.team_num=jsonObject.optString("team_num");
                            bean.is_reward=jsonObject.optString("is_reward");
                            bean.allowance_number=jsonObject.optString("allowance_number");
                            try {
                                JSONArray array=res.getJSONObject("data").getJSONArray("coupon_num");
                                bean.coupon_num=array.length()+"";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONObject orderStatusNum=res.getJSONObject("data").getJSONObject("orderStatusNum");
                                bean.noBuy=orderStatusNum.optInt("noBuy");
                                bean.noPostage=orderStatusNum.optInt("noPostage");
                                bean.noTake=orderStatusNum.optInt("noTake");
                                bean.noReply=orderStatusNum.optInt("noReply");
                                bean.noPink=orderStatusNum.optInt("noPink");
                                bean.noBuy=orderStatusNum.optInt("noBuy");
                                bean.noPostage=orderStatusNum.optInt("noPostage");
                                bean.noTake=orderStatusNum.optInt("noTake");
                                bean.noReply=orderStatusNum.optInt("noReply");
                                bean.noPink=orderStatusNum.optInt("noPink");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            AccountManager.sUserBean=bean;
                            buildView();
                        }else{
                            AccountManager.sUserBean=null;
                            startActivity(new Intent(mActivity, LoginActivity.class));
                            finish();
                        }
                    }else{
                        AccountManager.sUserBean=null;
                        startActivity(new Intent(mActivity, LoginActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    AccountManager.sUserBean=null;
                    startActivity(new Intent(mActivity, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onError(Throwable t) {
                AccountManager.sUserBean=null;
                startActivity(new Intent(mActivity, LoginActivity.class));
                finish();
            }
        });
    }

    private void buildView() {
        if("1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))){
            isdianzhu=true;
        }else{
            isdianzhu=false;
        }
        if(isdianzhu){
            navigationController = tab.custom()
                    .addItem(newItem(R.drawable.shouyehui,R.drawable.shouyehong,"首页"))
                    .addItem(newItem(R.drawable.tingchehui,R.drawable.tingchehong,"分类"))
                    .addItem(newRoundItem(R.drawable.dianzhuhui,R.drawable.dianzhuhong,"店主权益"))
                    .addItem(newItem(R.drawable.gouwuchehui,R.drawable.gouwuchehong,"购物车"))
                    .addItem(newItem(R.drawable.wodehui,R.drawable.wodehong,"我的"))
                    .build();
        }else{
            navigationController = tab.custom()
                    .addItem(newItem(R.drawable.shouyehui,R.drawable.shouyehong,"首页"))
                    .addItem(newItem(R.drawable.tingchehui,R.drawable.tingchehong,"分类"))
                    .addItem(newRoundItem(R.drawable.kaidianhui,R.drawable.kaidianhong,"我要开店"))
                    .addItem(newItem(R.drawable.gouwuchehui,R.drawable.gouwuchehong,"购物车"))
                    .addItem(newItem(R.drawable.wodehui,R.drawable.wodehong,"我的"))
                    .build();
        }


        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new TypeFragment());
        mFragmentList.add(new QuanYiFragment());
        mFragmentList.add(new CartFragment());
        mFragmentList.add(new MineFragment());
        mMainAdapter = new VpFragmentAdapter(getSupportFragmentManager(), mFragmentList);
        navigationController.setupWithViewPager(viewPager);
        viewPager.setAdapter(mMainAdapter);
        navigationController.addSimpleTabItemSelectedListener(new SimpleTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                if(index!=2){
                    oldindex=index;
                }else{
                    try {
                        if(AccountManager.sUserBean==null){

                            Toast.makeText(mActivity,"请登录",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if("1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))){
                            startActivity(new Intent(mActivity, FenXiaoMenuActivity.class));
                        }else{
                            startActivity(new Intent(mActivity, FenXiaoNoActivity.class));
                        }
                    } catch (Exception e) {
                        Toast.makeText(mActivity,"请登录",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });
//        showFirst();
        if(AccountManager.sUserBean!=null&&AccountManager.sUserBean.needshowdialog){
            showFirst();
        }
    }

    @Override
    public void initEvent() {

    }
    /**
     * 正常tab
     */
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text){
        SpecialTab mainTab = new SpecialTab(this);
        mainTab.initialize(drawable,checkedDrawable,text);
        mainTab.setTextDefaultColor(0xFF373737);
        mainTab.setTextCheckedColor(0xFFE60012);
        return mainTab;
    }

    /**
     * 圆形tab
     */
    private BaseTabItem newRoundItem(int drawable,int checkedDrawable,String text){
        SpecialTabRound mainTab = new SpecialTabRound(this);
        mainTab.initialize(drawable,checkedDrawable,text);
        mainTab.setTextDefaultColor(0xFF373737);
        mainTab.setTextCheckedColor(0xFFE60012);
        return mainTab;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(AccountManager.sUserBean==null){
            finish();
        }else {
            if(navigationController!=null){

                navigationController.setSelect(oldindex);
            }
        }
    }
    boolean isfirftyindaoclick=false;
    public void showFirst(){
        StyledDialog.init(mActivity);
        ViewGroup customView2 = (ViewGroup) View.inflate(this, R.layout.popwindow_first_chose, null);
         final android.widget.ImageView pass;
        final android.widget.ImageView button;
        final android.widget.ImageView close;
        pass = (ImageView) customView2.findViewById(R.id.pass);
        button = (ImageView) customView2.findViewById(R.id.button);
        close=(ImageView) customView2.findViewById(R.id.close);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isfirftyindaoclick){
                    pass.setImageResource(R.drawable.yindao_pass2);
                    button.setImageResource(R.drawable.yindao_kai2);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(mActivity, FenXiaoNoActivity.class));
            }
        });
        dialog = StyledDialog.buildCustom(customView2, Gravity.CENTER).setForceWidthPercent(1f).setCancelable(true,true).show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(AccountManager.sUserBean!=null){
                    AccountManager.sUserBean.needshowdialog=false;
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(mActivity);
        super.onDestroy();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishEvent(CloseMainEvent event) {
        try {
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
