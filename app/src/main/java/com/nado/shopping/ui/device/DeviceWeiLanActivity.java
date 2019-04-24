package com.nado.shopping.ui.device;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.nado.shopping.R;
import com.nado.shopping.base.BaseActivity;
import com.nado.shopping.constant.HomepageConstant;
import com.nado.shopping.manager.AccountManager;
import com.nado.shopping.manager.RequestManager;
import com.nado.shopping.net.RetrofitCallBack;
import com.nado.shopping.net.RetrofitRequestInterface;
import com.nado.shopping.util.DialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：Constantine on 2018/9/5.
 * 邮箱：2534159288@qq.com
 */
public class DeviceWeiLanActivity extends BaseActivity {

    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private MapView map;
    private TextView edit;
    private RadioButton mapjin;
    private RadioButton mapchu;
    private RadioButton mapjinchu;
    private RadioButton mapguanbi;
    private TextView sub;
    private SeekBar seek;
    private TextView add;
    private TextView submit;
    private AMap aMap;
    private GeocodeSearch geocoderSearch;


    private String address;
    private String lng;
    private int radii;
    private String lat;
    private TextView railNamet;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device_weilan;
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
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        map = (MapView) findViewById(R.id.map);
        edit = (TextView) findViewById(R.id.edit);
        mapjin = (RadioButton) findViewById(R.id.mapjin);
        mapchu = (RadioButton) findViewById(R.id.mapchu);
        mapjinchu = (RadioButton) findViewById(R.id.mapjinchu);
        mapguanbi = (RadioButton) findViewById(R.id.mapguanbi);
        sub = (TextView) findViewById(R.id.sub);
        seek = (SeekBar) findViewById(R.id.seek);
        add = (TextView) findViewById(R.id.add);
        submit = (TextView) findViewById(R.id.submit);
        railNamet = (TextView) findViewById(R.id.rail_namet);
    }

    public void saveWeiLan() {
        String rail_name = railNamet.getText().toString();
        String report_type = "";
        if (mapjin.isChecked()) {
            report_type = 1 + "";
        }
        if (mapchu.isChecked()) {
            report_type = 2 + "";

        }
        if (mapjinchu.isChecked()) {
            report_type = 3 + "";

        }
        if (mapguanbi.isChecked()) {
            report_type = 4 + "";

        }
        Map<String, String> map = new HashMap<>();
        if (getIntent().getStringExtra("id") != null) {

            map.put("id", getIntent().getStringExtra("id"));
        }
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("rail_name", rail_name);
        map.put("lng", lng);
        map.put("lat", lat);
        map.put("radii", radii + "");
        map.put("address", address);
        map.put("report_type", report_type);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).saveWeiLanDy(getIntent().getStringExtra("id") != null ? "index.php?g=app&m=appv1&a=UpdateRail" : "index.php?g=app&m=appv1&a=AddRail", RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        Toast.makeText(getBaseContext(),"保存围栏成功",Toast.LENGTH_SHORT).show();
                        finish();
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

    @Override
    public void initData() {
        StyledDialog.init(this);
        aMap = null;
        if (aMap == null) {
            aMap = map.getMap();
        }
        geocoderSearch = new GeocodeSearch(this);

    }

    @Override
    public void initEvent() {
        findViewById(R.id.backli).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radii-100>=100){
                    radii-=100;
                }
                seek.setProgress(radii);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radii+100<=1000){
                    radii+=100;
                }
                seek.setProgress(radii);

            }
        });
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                initDeviceMark(latLng.latitude, latLng.longitude);

            }
        });
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radii = progress;
                add.setText(radii+"米");
                circle.setRadius(radii);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (i == AMapException.CODE_AMAP_SUCCESS) {
                    if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null
                            && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                        address = regeocodeResult.getRegeocodeAddress().getFormatAddress()
                                + "附近";
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StyledDialog.buildNormalInput("名称修改", "输入名称", "", "确定", "取消", new MyDialogListener() {
                    @Override
                    public void onGetInput(CharSequence input1, CharSequence input2) {
                        super.onGetInput(input1, input2);
                        railNamet.setText(input1);
                    }

                    @Override
                    public void onFirst() {

                    }

                    @Override
                    public void onSecond() {

                    }
                }).show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWeiLan();
            }
        });
        if(getIntent().getStringExtra("radii")!=null){
            railNamet.setText(getIntent().getStringExtra("rail_name"));
            initDeviceMarkFirst(Double.parseDouble(getIntent().getStringExtra("lat")), Double.parseDouble(getIntent().getStringExtra("lng")));
            seek.setProgress(Integer.parseInt(getIntent().getStringExtra("radii")));
            radii = Integer.parseInt(getIntent().getStringExtra("radii"));
            add.setText(radii+"米");
            circle.setRadius(radii);


            if("1".equals(getIntent().getStringExtra("report_type"))){
                mapjin.setChecked(true);
            }
            if("2".equals(getIntent().getStringExtra("report_type"))){
                mapchu.setChecked(true);

            }
            if("3".equals(getIntent().getStringExtra("report_type"))){
                mapjinchu.setChecked(true);

            }
            if("4".equals(getIntent().getStringExtra("report_type"))){
                mapguanbi.setChecked(true);

            }

        }else{
            railNamet.setText(AccountManager.sUserBean.getTelNumber());
            initDeviceMarkFirst(HomepageConstant.mLatitude, HomepageConstant.mLongitude);
            seek.setProgress(100);
        }
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    private Circle circle;

    private void initWeiLan(double latitude, double longitude) {
        LatLng marker1 = new LatLng(latitude, longitude);
        final Marker marker = aMap.addMarker(new MarkerOptions().position(marker1).title("北京").snippet("DefaultMarker"));
        LatLng latLng = new LatLng(latitude, longitude);
        circle = aMap.addCircle(new CircleOptions().
                center(latLng).
                radius(radii).
                fillColor(Color.parseColor("#559BC0FF")).
                strokeColor(Color.parseColor("#55808080")).
                strokeWidth(15));
    }

    private void initDeviceMarkFirst(double latitude, double longitude) {

        initDeviceMark(latitude, longitude);
        LatLng marker1 = new LatLng(latitude, longitude);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    private void initDeviceMark(double latitude, double longitude) {



        DialogUtil.showUnCancelableProgress(mActivity, getString(R.string.location_setting));
        aMap.clear();
        lng = longitude + "";
        lat = latitude + "";
        //设置中心点和缩放比例
        initWeiLan(latitude, longitude);
        getAddress(new LatLonPoint(latitude, longitude));

        DialogUtil.hideProgress();
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
