package com.nado.parking.ui.device;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.nado.parking.R;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.bean.BaoJingDetail;
import com.nado.parking.bean.DeviceBean;
import com.nado.parking.manager.AccountManager;
import com.nado.parking.manager.RequestManager;
import com.nado.parking.net.RetrofitCallBack;
import com.nado.parking.net.RetrofitRequestInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：Constantine on 2018/9/5.
 * 邮箱：2534159288@qq.com
 */
public class DevicePostionActivity extends BaseActivity {


    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarStart;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private FrameLayout guijierrorneedhide;
    private MapView map;
    private AMap aMap;

    private BaoJingDetail bean;
    private GeocodeSearch geocoderSearch;
    private String address;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device_postion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        map.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        guijierrorneedhide = (FrameLayout) findViewById(R.id.guijierrorneedhide);
        map = (MapView) findViewById(R.id.map);
    }

    @Override
    public void initData() {
        aMap = null;
        if (aMap == null) {
            aMap = map.getMap();
        }
        geocoderSearch = new GeocodeSearch(this);
        getPostionDetail();
    }

    @Override
    public void initEvent() {
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (i == AMapException.CODE_AMAP_SUCCESS) {
                    if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null
                            && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                        address = regeocodeResult.getRegeocodeAddress().getFormatAddress()
                                + "附近";
                        initDeviceMark();
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    private void getPostionDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        map.put("obd_macid", AccountManager.sUserBean.obd_macid);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getAlarmDetails(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        bean = new BaoJingDetail();
                        JSONObject jsonObject = res.getJSONObject("data");
                        bean.id = jsonObject.optString("id");
                        bean.user_id = jsonObject.optString("user_id");
                        bean.obd_macid = jsonObject.optString("obd_macid");
                        bean.pt_time = jsonObject.optString("pt_time");
                        bean.add_time = jsonObject.optString("add_time");
                        bean.lat = jsonObject.optString("lat");
                        bean.lon = jsonObject.optString("lon");
                        bean.map_lat = jsonObject.optString("map_lat");
                        bean.map_lon = jsonObject.optString("map_lon");
                        bean.speed = jsonObject.optString("speed");
                        bean.dir = jsonObject.optString("dir");
                        bean.classify = jsonObject.optString("classify");
                        bean.describe_classify = jsonObject.optString("describe_classify");
                        bean.notea = jsonObject.optString("notea");
                        bean.phone = jsonObject.optString("phone");
                        getAddress(new LatLonPoint(Double.parseDouble(bean.lat), Double.parseDouble(bean.lon)));
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
        LatLng marker1 = new LatLng(Double.parseDouble(bean.map_lat), Double.parseDouble(bean.map_lon));
        //设置中心点和缩放比例
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        final Marker marker = aMap.addMarker(new MarkerOptions().position(marker1).title("报警").snippet("DefaultMarker"));

        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                TextView info = new TextView(DevicePostionActivity.this);
                String infostring = ""
                        + "名称：" + bean.id + "\n"
                        + "状态：" + bean.classify + " " + bean.speed + "km/h\n"
                        + "时间：" + bean.pt_time + "\n"
                        + "" + address + "\n";
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
