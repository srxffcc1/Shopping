package com.jiudi.shopping.ui.device;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.DialogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：Constantine on 2018/9/5.
 * 邮箱：2534159288@qq.com
 */
public class DeviceGuiJiActivity extends BaseActivity {


    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarStart;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private MapView map;
    private Switch deviceSwitch;
    private AMap aMap;
    private List<LatLng> latLngs;
    private ImageView playkey;
    private ImageView speedkey;
    private SeekBar seek;
    private SmoothMoveMarker smoothMarker;
    private FrameLayout guijierrorneedhide;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device_guiji;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        map.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        map = (MapView) findViewById(R.id.map);
        deviceSwitch = (Switch) findViewById(R.id.device_switch);
        playkey = (ImageView) findViewById(R.id.playkey);
        speedkey = (ImageView) findViewById(R.id.speedkey);
        seek = (SeekBar) findViewById(R.id.seek);
        guijierrorneedhide = (FrameLayout) findViewById(R.id.guijierrorneedhide);

    }

    @Override
    public void initData() {
        StyledDialog.init(this);
        aMap = null;
        if (aMap == null) {
            aMap = map.getMap();
        }

        if (getIntent().getStringExtra("from") != null) {

            initGuiJi(getIntent().getStringExtra("from"), getIntent().getStringExtra("to"));
            tvLayoutTopBackBarEnd.setVisibility(View.GONE);
        } else {
            initGuiJi(getTimeZero(0) + "", new Date().getTime() + "");
        }
    }

    private void initDeviceMark(double latitude, double longitude) {
        LatLng marker1 = new LatLng(latitude, longitude);
        //设置中心点和缩放比例
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        final Marker marker = aMap.addMarker(new MarkerOptions().position(marker1).title("北京").snippet("DefaultMarker"));
    }

    private void syncCamera(double latitude, double longitude) {
        if (deviceSwitch.isChecked()) {
            LatLng marker1 = new LatLng(latitude, longitude);
            //设置中心点和缩放比例
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
        }

    }

    @Override
    public void initEvent() {
        findViewById(R.id.backli).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        seek.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        playkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGuiJi();
            }
        });
        speedkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.chosetime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> strings = new ArrayList<>();
                strings.add("前天");
                strings.add("昨天");
                strings.add("今天");
                strings.add("前一小时");
                StyledDialog.buildBottomItemDialog(strings, "取消", new MyItemDialogListener() {
                    @Override
                    public void onItemClick(CharSequence charSequence, int i) {
                        if ("前天".equals(charSequence.toString())) {

                            initGuiJi(getTimeZero(2) + "", getTimeZero(1) + "");
                        }
                        if ("昨天".equals(charSequence.toString())) {
                            initGuiJi(getTimeZero(1) + "", getTimeZero(0) + "");

                        }
                        if ("今天".equals(charSequence.toString())) {
                            initGuiJi(getTimeZero(0) + "", new Date().getTime() + "");
                        }
                        if ("前一小时".equals(charSequence.toString())) {
                            initGuiJi(getTimeBeforeHour(1) + "", new Date().getTime() + "");
                        }
                    }
                }).show();
            }
        });
    }

    private static Long getTimeZero(int fixday) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - fixday, 0, 0, 0);
        Date beginOfDate = cal.getTime();

        return beginOfDate.getTime();
    }

    private static Long getTimeBeforeHour(int fixhour) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) - fixhour, 0, 0);
        Date beginOfDate = cal.getTime();

        return beginOfDate.getTime();
    }

    private void initGuiJi(String from, String to) {
        ispaly=false;
        iscomplete = false;
        deviceSwitch.setEnabled(true);
        index=0;
        playkey.setImageResource(R.drawable.device_play);
        DialogUtil.showUnCancelableProgress(mActivity, getString(R.string.location_setting));
        aMap.clear();
        Map<String, String> map = new HashMap<>();
        map.put("method", "getHistoryMByMUtc");
        map.put("from", from);
        map.put("to", to);
        map.put("playLBS", "true");
        map.put("mapType", "GAODE");
        map.put("macid", AccountManager.sUserBean.obd_macid);
        map.put("mds", AccountManager.sUserBean.obd_mds);
        System.out.println(map);
        RequestManager.mRetrofitManager2.createRequest(RetrofitRequestInterface.class).getGuiJiDate(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {

            @Override
            public void onSuccess(String response) {

                try {
                    guijierrorneedhide.setVisibility(View.VISIBLE);
                    latLngs = new ArrayList<LatLng>();
                    String[] array = response.replace("\"", "").split(";");
                    for (int i = 0; i < array.length; i++) {
                        String[] ltarray = array[i].split(",");
                        latLngs.add(new LatLng(Double.parseDouble(ltarray[1]), Double.parseDouble(ltarray[0])));
                    }

                    initDeviceMark(latLngs.get(0).latitude, latLngs.get(0).longitude);

                    aMap.addPolyline(new PolylineOptions().
                            addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "未获得到轨迹，请选择正确时间", Toast.LENGTH_SHORT).show();
                    guijierrorneedhide.setVisibility(View.GONE);
                    e.printStackTrace();
                }

                DialogUtil.hideProgress();

            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    boolean ispaly = false;
    boolean iscomplete = false;
    double smlat;
    double smlot;
    int index = 0;
    double maxdes=0;
    public  int checkNowIndex(){
        if(index==0){
            return 0;
        }else{
            for (int i = 0; i < latLngs.size(); i++) {
                if((int)smlat==(int)latLngs.get(i).latitude&&(int)smlot==(int)latLngs.get(i).longitude){
                    return i;
                }
            }
        }
        return 0;
    }
    public void playGuiJi() {
        if (!ispaly) {
            if (smoothMarker != null) {
                smoothMarker.removeMarker();
            }
            deviceSwitch.setEnabled(false);
            playkey.setImageResource(R.drawable.device_pause);
//            LatLngBounds bounds = new LatLngBounds(latLngs.get(index), latLngs.get(latLngs.size() - 2));
//            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

            smoothMarker = new SmoothMoveMarker(aMap);
// 设置滑动的图标
            smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.icon_car));

            LatLng drivePoint = latLngs.get(index);
            Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(latLngs, drivePoint);
            latLngs.set(pair.first, drivePoint);
            final List<LatLng> subList = latLngs.subList(pair.first, latLngs.size());
// 设置滑动的轨迹左边点
            smoothMarker.setPoints(subList);
// 设置滑动的总时间
            smoothMarker.setTotalDuration(80);
            if(iscomplete){
                seek.setProgress(0);
            }
            System.out.println("总长"+latLngs.size()+":"+subList.size());
            smoothMarker.setMoveListener(new SmoothMoveMarker.MoveListener() {
                @Override
                public void move(double v) {
                    if(v<100){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iscomplete=true;
                                seek.setProgress(1000);
                                deviceSwitch.setEnabled(true);
                                playkey.setImageResource(R.drawable.device_play);
                                smoothMarker.stopMove();
                                ispaly=false;
                                index=0;
                            }
                        });
                    }
                    if(index==0){
                        maxdes=v;
                    }
                    seek.setProgress((int) (((maxdes-v)*1.0/maxdes)*1000));
                    index++;
                    System.out.println("步进" + index+":"+v);
                    smlat=smoothMarker.getPosition().latitude;
                    smlot=smoothMarker.getPosition().longitude;
                    syncCamera(smoothMarker.getPosition().latitude, smoothMarker.getPosition().longitude);


                }
            });

// 开始滑动
            smoothMarker.startSmoothMove();
        } else {
            deviceSwitch.setEnabled(true);
            playkey.setImageResource(R.drawable.device_play);
            smoothMarker.stopMove();
        }
        ispaly = !ispaly;


    }

    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if(smoothMarker!=null){
            smoothMarker.destroy();
            smoothMarker=null;
        }
        aMap.clear();
        map.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        map.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        map.onSaveInstanceState(outState);
    }
}
