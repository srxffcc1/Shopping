package com.jiudi.shopping.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.github.nukc.LoadMoreWrapper.LoadMoreAdapter;
import com.github.nukc.LoadMoreWrapper.LoadMoreWrapper;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.vl.VHotGridAdapter;
import com.jiudi.shopping.adapter.vl.VLBannerAdapter;
import com.jiudi.shopping.adapter.vl.VMiaoShaAdapter;
import com.jiudi.shopping.adapter.vl.VRecommendAdapter;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.Fictitious;
import com.jiudi.shopping.bean.GodMiaoSha;
import com.jiudi.shopping.bean.MainGodsBean;
import com.jiudi.shopping.bean.RecommendTabBean;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.CalendarReminderUtils;
import com.jiudi.shopping.util.DateTimeUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 主页
 */
public class HomeMMMFragment extends BaseFragment implements View.OnClickListener {

    private String[] titles = {"周一场", "周二场","周三场","周四场","周五场","周六场","周日场"};
    private android.support.v7.widget.RecyclerView recycler;
    private int rweek;
    private int nowweek;
    private List<GodMiaoSha> mmiaoshalist=new ArrayList<>();
    private VMiaoShaAdapter vMiaoShaAdapter;
    private VirtualLayoutManager manager;
    private DelegateAdapter adapter;
    final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();
    private String miaotype="";//已开抢，抢购中，即将开抢


    private Timer timer;
    private TextView qinggoutitle;
    private TextView qinggousectitle;
    private TextView day;
    private TextView hour;
    private TextView min;
    private TextView sec;
    private String destime;
    private long destimel;
    private LinearLayout timetextneedshow;
    private int REQUEST_CALENDAR =100;

    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_homemmm;
    }

    @Override
    public void initView() {


        recycler = (RecyclerView) findViewById(R.id.recycler);
        qinggoutitle = (TextView) findViewById(R.id.qinggoutitle);
        qinggousectitle = (TextView) findViewById(R.id.qinggousectitle);
        day = (TextView) findViewById(R.id.day);
        hour = (TextView) findViewById(R.id.hour);
        min = (TextView) findViewById(R.id.min);
        sec = (TextView) findViewById(R.id.sec);
        timetextneedshow = (LinearLayout) findViewById(R.id.timetextneedshow);
    }

    @Override
    public void initData() {
//        for (int i = 0; i <10 ; i++) {
//            GodMiaoSha bean=new GodMiaoSha();
//            mmiaoshalist.add(bean);
//
//        }
//        buildRecycleView();
        rweek=Integer.parseInt(getArguments().getString("rweek"));
        nowweek=Integer.parseInt(getArguments().getString("nowweek"));
        if(nowweek<rweek){//即将开抢
            qinggoutitle.setText("抢购即将开始");
            miaotype="即将开抢";
            qinggousectitle.setText("距本场开始:");
            destime=getTimeZeroString(-(rweek-nowweek));
            destimel=getTimeZero(-(rweek-nowweek));
            startTimer();
        }else if(nowweek==rweek){//开抢中
            qinggoutitle.setText("抢购进行时");
            miaotype="开抢中";
            qinggousectitle.setText("距本场结束:");
            destime=getTimeZeroString(-1);
            destimel=getTimeZero(-1);
            startTimer();
        }else{//已开抢
            miaotype="已开抢";
            qinggoutitle.setText("抢购已结束");
            timetextneedshow.setVisibility(View.GONE);
            qinggousectitle.setText("请查看其他场次抢购");
        }

        getMiaoList();

    }

    public void startTimer(){
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if(getActivity()!=null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    changeTime();
                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 1000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void changeTime() {
        String[] array= DateTimeUtil.getDistanceTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),destime);
        day.setText(array[0]);
        hour.setText(array[1]);
        min.setText(array[2]);
        sec.setText(array[3]);
    }

    @Override
    public void initEvent() {

    }
    public void buildRecycleView() {

        if (adapter == null) {
            manager = new VirtualLayoutManager(mActivity);
            recycler.setLayoutManager(manager);
            adapter = new DelegateAdapter(manager);
            RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
            recycler.setRecycledViewPool(viewPool);
            viewPool.setMaxRecycledViews(0, 10);
            SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
            LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
            GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(5);
            gridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 1;
                }
            });
            gridLayoutHelper.setAutoExpand(false);

            GridLayoutHelper gridLayoutHelper2 = new GridLayoutHelper(2);
            gridLayoutHelper2.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 1;
                }
            });
            gridLayoutHelper2.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 1;
                }
            });

            gridLayoutHelper2.setAutoExpand(false);
            StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper();
            stickyLayoutHelper.setStickyStart(true);

            vMiaoShaAdapter = new VMiaoShaAdapter(mActivity, linearLayoutHelper, mmiaoshalist,miaotype,titles[rweek],this);
            adapters.add(vMiaoShaAdapter);
            adapter.setAdapters(adapters);
            recycler.setAdapter(adapter);

        } else {
            try {
                vMiaoShaAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    private void getMiaoList() {

        Map<String, String> map = new HashMap<>();
        map.put("week",getArguments().getString("week"));
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getSeckill(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        JSONArray data=res.getJSONArray("data");
                        for (int i = 0; i <data.length() ; i++) {
                            JSONObject jsonObject=data.getJSONObject(i);
                            GodMiaoSha bean=new GodMiaoSha();
                            bean.id=jsonObject.optString("id");
                            bean.product_id=jsonObject.optString("product_id");
                            bean.image=jsonObject.optString("image");
                            bean.images=jsonObject.optString("images");
                            bean.title=jsonObject.optString("title");
                            bean.info=jsonObject.optString("info");
                            bean.price=jsonObject.optString("price");
                            bean.cost=jsonObject.optString("cost");
                            bean.ot_price=jsonObject.optString("ot_price");
                            bean.give_integral=jsonObject.optString("give_integral");
                            bean.sort=jsonObject.optString("sort");
                            bean.stock=jsonObject.optInt("stock");
                            bean.sales=jsonObject.optInt("sales");
                            bean.unit_name=jsonObject.optString("unit_name");
                            bean.postage=jsonObject.optString("postage");
                            bean.description=jsonObject.optString("description");
                            bean.start_time=jsonObject.optString("start_time");
                            bean.stop_time=jsonObject.optString("stop_time");
                            bean.add_time=jsonObject.optString("add_time");
                            bean.status=jsonObject.optString("status");
                            bean.is_postage=jsonObject.optString("is_postage");
                            bean.is_hot=jsonObject.optString("is_hot");
                            bean.is_del=jsonObject.optString("is_del");
                            bean.num=jsonObject.optString("num");
                            bean.is_show=jsonObject.optString("is_show");
                            mmiaoshalist.add(bean);
                        }
                        buildRecycleView();
                    }else{
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
    private  Long getTimeZero(int fixday) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - fixday, 0, 0, 0);
        Date beginOfDate = cal.getTime();

        return beginOfDate.getTime();
    }

    private  String getTimeZeroString(int fixday) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - fixday, 0, 0, 0);
        Date beginOfDate = cal.getTime();

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beginOfDate);
    }
    public void miaoShaPermission() {

        int checkSelfPermission;
        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_CALENDAR);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return;
        }

        // 如果有授权，走正常插入日历逻辑
        if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
//            insertCalendar(); // 该方法的实现在文章的后面
            CalendarReminderUtils.addCalendarEvent(mActivity,"秒杀提醒","商品"+"秒杀即将开始",destimel,1);
            return;
        } else {
            // 如果没有授权，就请求用户授权
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_CALENDAR,
                    Manifest.permission.READ_CALENDAR}, REQUEST_CALENDAR);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALENDAR) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户同意的授权请求
//                insertCalendar();
                CalendarReminderUtils.addCalendarEvent(mActivity,"秒杀提醒","商品"+"秒杀即将开始",destimel,1);
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_CALENDAR)) {
                    // 如果用户不是点击了拒绝就跳转到系统设置页
//                    gotoSettings();
                    ToastUtil.showShort(mActivity, "请在到设置-应用管理中开启此应用的日历授权");
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if("即将开抢".equals(miaotype)){
//            viewHolder.miaoshaPass.setText("开抢提醒");
//            viewHolder.miaoshaPass.setOnClickListener(listener);
            miaoShaPermission();
        }else if("开抢中".equals(miaotype)){
//            viewHolder.miaoshaPass.setText("马上抢");
//            viewHolder.miaoshaPass.setOnClickListener(listener);
        }else {
//            viewHolder.miaoshaPass.setText("原价购买");
//            viewHolder.miaoshaPass.setOnClickListener(listener);
        }
    }
}
