package com.nado.shopping.ui.device;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.nado.shopping.R;
import com.nado.shopping.base.BaseActivity;
import com.nado.shopping.bean.DeviceBean;
import com.nado.shopping.constant.HomepageConstant;
import com.nado.shopping.manager.AccountManager;
import com.nado.shopping.manager.RequestManager;
import com.nado.shopping.net.RetrofitCallBack;
import com.nado.shopping.net.RetrofitRequestInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class DeviceConditionActivity extends BaseActivity {

    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutBackTopBarOperate;
    private MapView map;
    private RadioButton mapmap;
    private RadioButton mapmoom;
    private LinearLayout hidedevicemenu;
    private List<LatLng> latLngs=new ArrayList<>();

    private DeviceBean bean;
    private AMap aMap;
    private LinearLayout liDh;
    private LinearLayout liWl;
    private LinearLayout liGj;
    private LinearLayout liXq;
    private LinearLayout liXc;
    private LinearLayout liJc;
    private LinearLayout liCk;
    private LinearLayout liBj;
    private Circle circle;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        map = (MapView) findViewById(R.id.map);
        mapmap = (RadioButton) findViewById(R.id.mapmap);
        mapmoom = (RadioButton) findViewById(R.id.mapmoom);
        hidedevicemenu = (LinearLayout) findViewById(R.id.hidedevicemenu);

        liDh = (LinearLayout) findViewById(R.id.li_dh);
        liWl = (LinearLayout) findViewById(R.id.li_wl);
        liGj = (LinearLayout) findViewById(R.id.li_gj);
        liXq = (LinearLayout) findViewById(R.id.li_xq);
        liXc = (LinearLayout) findViewById(R.id.li_xc);
        liJc = (LinearLayout) findViewById(R.id.li_jc);
        liCk = (LinearLayout) findViewById(R.id.li_ck);
        liBj = (LinearLayout) findViewById(R.id.li_bj);
    }

    @Override
    public void initData() {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        aMap = null;
        if (aMap == null) {
            aMap = map.getMap();
        }
        initDevice();
    }

    private void initDevice() {
        aMap.clear();
        Map<String, String> map = new HashMap<>();
        map.put("method", "getUserAndGpsInfoByIDsUtc");
        map.put("mapType", "GAODE");
        map.put("macid", AccountManager.sUserBean.obd_macid);
        map.put("user_id", AccountManager.sUserBean.obd_user_id);
        map.put("mds", AccountManager.sUserBean.obd_mds);
        RequestManager.mRetrofitManager2.createRequest(RetrofitRequestInterface.class).getDeviceDate(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {

            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("key");
                    JSONArray array = jsonObject.getJSONArray("records").getJSONArray(0);
                    if (data != null) {
                        bean = new DeviceBean();
                        bean.sys_time = array.get(Integer.parseInt(data.getString("sys_time"))).toString();
                        bean.user_name = array.get(Integer.parseInt(data.getString("user_name"))).toString();
                        bean.jingdu = array.get(Integer.parseInt(data.getString("jingdu"))).toString();
                        bean.weidu = array.get(Integer.parseInt(data.getString("weidu"))).toString();
                        bean.ljingdu = array.get(Integer.parseInt(data.getString("ljingdu"))).toString();
                        bean.lweidu = array.get(Integer.parseInt(data.getString("lweidu"))).toString();
                        bean.datetime = array.get(Integer.parseInt(data.getString("datetime"))).toString();
                        bean.heart_time = array.get(Integer.parseInt(data.getString("heart_time"))).toString();
                        bean.su = array.get(Integer.parseInt(data.getString("su"))).toString();
                        bean.status = array.get(Integer.parseInt(data.getString("status"))).toString();
                        bean.hangxiang = array.get(Integer.parseInt(data.getString("hangxiang"))).toString();
                        bean.sim_id = array.get(Integer.parseInt(data.getString("sim_id"))).toString();
                        bean.user_id = array.get(Integer.parseInt(data.getString("user_id"))).toString();
                        bean.sale_type = array.get(Integer.parseInt(data.getString("sale_type"))).toString();
                        bean.iconType = array.get(Integer.parseInt(data.getString("iconType"))).toString();
                        bean.server_time = array.get(Integer.parseInt(data.getString("server_time"))).toString();
                        bean.product_type = array.get(Integer.parseInt(data.getString("product_type"))).toString();
                        bean.expire_date = array.get(Integer.parseInt(data.getString("expire_date"))).toString();
                        bean.group_id = array.get(Integer.parseInt(data.getString("group_id"))).toString();
                        bean.statenumber = array.get(Integer.parseInt(data.getString("statenumber"))).toString();
                        bean.electric = array.get(Integer.parseInt(data.getString("electric"))).toString();
                        bean.describe = array.get(Integer.parseInt(data.getString("describe"))).toString();
                        bean.sim = array.get(Integer.parseInt(data.getString("sim"))).toString();
                        bean.precision = array.get(Integer.parseInt(data.getString("precision"))).toString();
                        initDeviceMark();
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

    private void initDeviceMark() {
        LatLng marker1 = new LatLng(Double.parseDouble(bean.weidu), Double.parseDouble(bean.jingdu));
        //设置中心点和缩放比例
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        final Marker marker = aMap.addMarker(new MarkerOptions().position(marker1).title("北京").snippet("DefaultMarker"));

        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                TextView info = new TextView(DeviceConditionActivity.this);
                String infostring = ""
                        + "名称：" + bean.user_name + "\n"
                        + "设备号：" + bean.sim_id + "\n"
                        + "设备类型：" + bean.product_type + "\n";
                info.setText(infostring);
                return info;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
        marker.showInfoWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        map.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public void initEvent() {
        liGj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                initGuiJi();
                initWeiLan();
            }
        });
        liDh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav();
            }
        });

    }
    private static Long getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date beginOfDate = cal.getTime();

        return beginOfDate.getTime();
    }

    private void initWeiLan() {

        LatLng latLng = new LatLng(Double.parseDouble(bean.weidu),Double.parseDouble(bean.jingdu));
        circle = aMap.addCircle(new CircleOptions().
                center(latLng).
                radius(1000).
                fillColor(Color.RED).
                strokeColor(Color.GRAY).
                strokeWidth(15));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                circle.setRadius(500);
            }
        },2000);
    }


    private void initGuiJi() {
        aMap.clear();
        Map<String, String> map = new HashMap<>();
        map.put("method", "getHistoryMByMUtc");
        map.put("from", getTimesmorning()+"");
        map.put("to", new Date().getTime()+"");
        map.put("playLBS","true");
        map.put("mapType","GAODE");
        map.put("macid", AccountManager.sUserBean.obd_macid);
        map.put("mds", AccountManager.sUserBean.obd_mds);
        System.out.println(map);
        RequestManager.mRetrofitManager2.createRequest(RetrofitRequestInterface.class).getGuiJiDate(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {

            @Override
            public void onSuccess(String response) {
                List<LatLng> latLngs = new ArrayList<LatLng>();
                String[] array=response.replace("\"","").split(";");
                for (int i = 0; i <array.length ; i++) {
                    String[] ltarray=array[i].split(",");
                    latLngs.add(new LatLng(Double.parseDouble(ltarray[1]),Double.parseDouble(ltarray[0])));
                }
                aMap.addPolyline(new PolylineOptions().
                        addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));


                LatLngBounds bounds = new LatLngBounds(latLngs.get(0), latLngs.get(latLngs.size() - 2));
                aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

                SmoothMoveMarker smoothMarker = new SmoothMoveMarker(aMap);
// 设置滑动的图标
                smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.icon_car));

                LatLng drivePoint = latLngs.get(0);
                Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(latLngs, drivePoint);
                latLngs.set(pair.first, drivePoint);
                List<LatLng> subList = latLngs.subList(pair.first, latLngs.size());

// 设置滑动的轨迹左边点
                smoothMarker.setPoints(subList);
// 设置滑动的总时间
                smoothMarker.setTotalDuration(40);
// 开始滑动
                smoothMarker.startSmoothMove();

                smoothMarker.stopMove();

            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }
    public void Nav(){
        Poi start = new Poi("我的位置", new LatLng(HomepageConstant.mLatitude,HomepageConstant.mLongitude), "");
        Poi end = new Poi("我的车", new LatLng(Double.parseDouble(bean.weidu), Double.parseDouble(bean.jingdu)), "");
        List<Poi> wayList = new ArrayList();//途径点目前最多支持3个。
        AmapNaviPage.getInstance().showRouteActivity(DeviceConditionActivity.this, new AmapNaviParams(start, wayList, end, AmapNaviType.DRIVER), new INaviInfoCallback() {
            @Override
            public void onInitNaviFailure() {

            }

            @Override
            public void onGetNavigationText(String s) {

            }

            @Override
            public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

            }

            @Override
            public void onArriveDestination(boolean b) {

            }

            @Override
            public void onStartNavi(int i) {

            }

            @Override
            public void onCalculateRouteSuccess(int[] ints) {

            }

            @Override
            public void onCalculateRouteFailure(int i) {

            }

            @Override
            public void onStopSpeaking() {

            }

            @Override
            public void onReCalculateRoute(int i) {

            }

            @Override
            public void onExitPage(int i) {

            }

            @Override
            public void onStrategyChanged(int i) {

            }

            @Override
            public View getCustomNaviBottomView() {
                return null;
            }

            @Override
            public View getCustomNaviView() {
                return null;
            }

            @Override
            public void onArrivedWayPoint(int i) {

            }
        });
    }
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
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
