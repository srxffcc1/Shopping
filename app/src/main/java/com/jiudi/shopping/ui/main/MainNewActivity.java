package com.jiudi.shopping.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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

import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.vp.VpFragmentAdapter;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.event.CartEvent;
import com.jiudi.shopping.event.FinishEvent;
import com.jiudi.shopping.event.FlashEvent;
import com.jiudi.shopping.event.PassCartEvent;
import com.jiudi.shopping.event.UpdatePayStatusEvent;
import com.jiudi.shopping.global.Constant;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.service.AppUpdateService;
import com.jiudi.shopping.util.CommonUtil;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.widget.NoTouchViewPager;
import com.jiudi.shopping.widget.SpecialTab;
import com.jiudi.shopping.widget.SpecialTabRound;
import com.m7.imkfsdk.KfStartHelper;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;

public class MainNewActivity extends BaseActivity {

    private android.widget.FrameLayout activityMaterialDesign;
    private com.jiudi.shopping.widget.NoTouchViewPager viewPager;
    private me.majiajie.pagerbottomtabstrip.PageNavigationView tab;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_new_main;
    }

    @Override
    public void initView() {

        activityMaterialDesign = (FrameLayout) findViewById(R.id.activity_material_design);
        viewPager = (NoTouchViewPager) findViewById(R.id.viewPager);
        tab = (PageNavigationView) findViewById(R.id.tab);
    }

    @Override
    public void initData() {
        NavigationController navigationController = tab.custom()
                .addItem(newItem(R.drawable.shouyehui,R.drawable.shouyehong,"首页"))
                .addItem(newItem(R.drawable.tingchehui,R.drawable.tingchehong,"分类"))
                .addItem(newRoundItem(R.drawable.fenxiaohui,R.drawable.fenxiaohong,"分销中心"))
                .addItem(newItem(R.drawable.gouwuchehui,R.drawable.gouwuchehong,"购物车"))
                .addItem(newItem(R.drawable.wodehui,R.drawable.wodehong,"我的"))
                .build();
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
}
