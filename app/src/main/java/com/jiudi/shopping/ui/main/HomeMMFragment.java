package com.jiudi.shopping.ui.main;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.Fictitious;
import com.jiudi.shopping.bean.RecommendTabBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 主页
 */
public class HomeMMFragment extends BaseFragment {


    private String[] titles = {"周一场", "周二场","周三场","周四场","周五场","周六场","周日场"};
    private List<RecommendTabBean> mRecommendTabList = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private LinearLayout needshow;
    private ImageView showhead;
    private TextView content;
    private Fictitious fictitious;
    private FrameLayout flChange;
    private TabLayout mainTab;
    private ImageView tongzhi;
    private ImageView seach;

    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_homemm;
    }

    @Override
    public void initView() {

        needshow = (LinearLayout) findViewById(R.id.needshow);
        showhead = (ImageView) findViewById(R.id.showhead);
        content = (TextView) findViewById(R.id.content);
        flChange = (FrameLayout) findViewById(R.id.fl_change);
        mainTab = (TabLayout) findViewById(R.id.main_tab);
        tongzhi = (ImageView) findViewById(R.id.tongzhi);
        seach = (ImageView) findViewById(R.id.seach);
    }
    public String getWeekOfDate(Date date) {
        String[] weekDays = { "周日场", "周一场", "周二场", "周三场", "周四场", "周五场", "周六场" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
    public int getWeekOfDateIndex(Date date,String[] titles) {
        String[] weekDays = { "周日场", "周一场", "周二场", "周三场", "周四场", "周五场", "周六场" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        String results=weekDays[w];
        int result=0;
        for (int i = 0; i <titles.length ; i++) {
            if(results.equals(titles[i])){
                result=i;
            }
        }

        return result;
    }
    @Override
    public void initData() {
        buildTab();
    }

    private static Long getTimeZero(int fixday) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - fixday, 0, 0, 0);
        Date beginOfDate = cal.getTime();

        return beginOfDate.getTime();
    }


    public void buildTab(){
        int reallyindex=getWeekOfDateIndex(new Date(),titles);
        for (int i = 0; i < titles.length; i++) {
            //插入tab标签

            View result= LayoutInflater.from(mActivity).inflate(R.layout.item_tab_miaosha,null);
            TextView title = (TextView) result.findViewById(R.id.title);
            TextView second_title = (TextView) result.findViewById(R.id.second_title);
            title.setText(titles[i]);
            String sectitle="";

            if(reallyindex==i){
                sectitle="抢购中";
            }else if(reallyindex<i){
                sectitle="即将开抢";
            }else {
                sectitle="已开抢";
            }
            System.out.println("真实"+reallyindex+"");
            mFragments.add(new HomeMMMFragment().setArgumentz("miaotype",sectitle).setArgumentz("rweek",i+"").setArgumentz("nowweek",reallyindex+"").setArgumentz("week",(i+1)+""));
            second_title.setText(sectitle);
            mainTab.addTab(mainTab.newTab().setText(titles[i]));
            mainTab.getTabAt(i).setCustomView(result);
        }

//        if(mRecommendTabList.size()>4){
//            mainTab.setTabMode(TabLayout.MODE_SCROLLABLE);
//        }else{
//            mainTab.setTabMode(TabLayout.MODE_FIXED);
//        }
        mainTab.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabStatus(tab,true);
                getFragmentManager().beginTransaction().replace(R.id.fl2_change,mFragments.get(tab.getPosition())).commitAllowingStateLoss();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabStatus(tab,false);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        getFragmentManager().beginTransaction().replace(R.id.fl2_change,mFragments.get(0)).commitAllowingStateLoss();
        setDefaultTable(reallyindex,true);
//        mainTab.getTabAt(0).select();
//        mainTab.setScrollPosition(reallyindex, 0f, true);
        changeTabStatus(mainTab.getTabAt(reallyindex),true);
    }
    private void changeTabStatus(TabLayout.Tab tab, boolean selected) {
        View view = tab.getCustomView();
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView second_title = (TextView) view.findViewById(R.id.second_title);
        if (selected) {
            title.setTextColor(Color.parseColor("#FFFFFF"));
            second_title.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            title.setTextColor(Color.parseColor("#FBC8B7"));
            second_title.setTextColor(Color.parseColor("#FBC8B7"));
        }
    }

    @Override
    public void initEvent() {

    }
    public void setDefaultTable(int position, boolean needScroll) {
        if (position < 0)
            position = 0;
        if (position >= titles.length)
            position = titles.length - 1;
        mainTab.getTabAt(position).select();
        if (mainTab.getTabAt(position).isSelected()) {
            View customView = mainTab.getTabAt(position).getCustomView();
            View tab_title =  customView.findViewById(R.id.zeep);
            if (needScroll) {
                int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                tab_title.measure(spec, spec);
                int measuredWidthTicketNum = tab_title.getMeasuredWidth();
                //   measuredWidthTicketNum = px2dp(measuredWidthTicketNum);
                recomputeTlOffset1(position, measuredWidthTicketNum);
            }
        }
    }

    private void recomputeTlOffset1(int index, int viewWidth) {
        if (mainTab.getTabAt(index) != null) mainTab.getTabAt(index).select();
        //加上半个item的宽度（这个需要自己微调，不一定是半个）如果有设置margin还需要加上margin的距离
        int halfWidth = viewWidth / 2; //偏移量
        final int width = ((viewWidth + halfWidth) * index);

        mainTab.post(new Runnable() {
            @Override
            public void run() {
                mainTab.smoothScrollTo(width, 0);
            }
        });
    }

}
