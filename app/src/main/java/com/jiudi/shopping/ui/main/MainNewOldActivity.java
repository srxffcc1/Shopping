package com.jiudi.shopping.ui.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.vp.VpFragmentAdapter;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.ui.fenxiao.FenXiaoMenuActivity;
import com.jiudi.shopping.ui.fenxiao.FenXiaoNoActivity;
import com.jiudi.shopping.widget.NoTouchViewPager;
import com.jiudi.shopping.widget.SpecialTab;
import com.jiudi.shopping.widget.SpecialTabRound;

import java.util.ArrayList;
import java.util.List;

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
