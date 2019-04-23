package com.nado.parking.ui.device;

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
import com.nado.parking.R;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.bean.DeviceBean;
import com.nado.parking.bean.DeviceDetail;
import com.nado.parking.constant.HomepageConstant;
import com.nado.parking.manager.AccountManager;
import com.nado.parking.manager.RequestManager;
import com.nado.parking.net.RetrofitCallBack;
import com.nado.parking.net.RetrofitRequestInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
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
public class DeviceDetailActivity extends BaseActivity {


    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarStart;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private TextView shebeimingcheng;
    private TextView zhongduanleixing;
    private TextView chepaihao;
    private TextView chejiahao;
    private TextView fadongjihao;
    private TextView imei;
    private TextView sim;
    private TextView iccid;
    private TextView lianxiren;
    private TextView lianxidianhua;
    private TextView jihuoriqi;
    private TextView daoqiriqi;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device_detail;
    }


    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        shebeimingcheng = (TextView) findViewById(R.id.shebeimingcheng);
        zhongduanleixing = (TextView) findViewById(R.id.zhongduanleixing);
        chepaihao = (TextView) findViewById(R.id.chepaihao);
        chejiahao = (TextView) findViewById(R.id.chejiahao);
        fadongjihao = (TextView) findViewById(R.id.fadongjihao);
        imei = (TextView) findViewById(R.id.imei);
        sim = (TextView) findViewById(R.id.sim);
        iccid = (TextView) findViewById(R.id.iccid);
        lianxiren = (TextView) findViewById(R.id.lianxiren);
        lianxidianhua = (TextView) findViewById(R.id.lianxidianhua);
        jihuoriqi = (TextView) findViewById(R.id.jihuoriqi);
        daoqiriqi = (TextView) findViewById(R.id.daoqiriqi);
        tvLayoutTopBackBarTitle.setText("设备详情");
    }

    @Override
    public void initData() {
        getDetail();
    }

    @Override
    public void initEvent() {

    }

    private void getDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("method", "loadUser");
        map.put("mds", AccountManager.sUserBean.obd_mds);
        map.put("user_id", AccountManager.sUserBean.obd_user_id);
        RequestManager.mRetrofitManager2.createRequest(RetrofitRequestInterface.class).getOBDDetail(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    String info = res.getString("success");
                    if ("true".equals(info)) {
                        JSONObject jsonObject = res.getJSONArray("data").getJSONObject(0);
                        DeviceDetail bean = new DeviceDetail();
                        bean.user_name = jsonObject.optString("user_name");
                        bean.sys_time = jsonObject.optString("sys_time");
                        bean.datetime = jsonObject.optString("datetime");
                        bean.heart_time = jsonObject.optString("heart_time");
                        bean.sudu = jsonObject.optString("sudu");
                        bean.status = jsonObject.optString("status");
                        bean.create_time = jsonObject.optString("create_time");
                        bean.remark = jsonObject.optString("remark");
                        bean.use_time = jsonObject.optString("use_time");
                        bean.factory_date = jsonObject.optString("factory_date");
                        bean.tel = jsonObject.optString("tel");
                        bean.sex = jsonObject.optString("sex");
                        bean.sim_id = jsonObject.optString("sim_id");
                        bean.phone = jsonObject.optString("phone");
                        bean.user_id = jsonObject.optString("user_id");
                        bean.grade = jsonObject.optString("grade");
                        bean.sale_type = jsonObject.optString("sale_type");
                        bean.service_flag = jsonObject.optString("service_flag");
                        bean.product_type = jsonObject.optString("product_type");
                        bean.iconType = jsonObject.optString("iconType");
                        bean.out_time = jsonObject.optString("out_time");
                        bean.jingdu = jsonObject.optString("jingdu");
                        bean.weidu = jsonObject.optString("weidu");
                        bean.su = jsonObject.optString("su");
                        bean.owner = jsonObject.optString("owner");
                        bean.jingwei = jsonObject.optString("jingwei");
                        bean.alarm = jsonObject.optString("alarm");
                        bean.deviceRemark = jsonObject.optString("deviceRemark");
                        bean.alarmFuel = jsonObject.optString("alarmFuel");
                        bean.fuelPerHKM = jsonObject.optString("fuelPerHKM");
                        bean.fuelSize = jsonObject.optString("fuelSize");
                        bean.maxFuelV = jsonObject.optString("maxFuelV");
                        bean.minFuelV = jsonObject.optString("minFuelV");
                        bean.macVersion = jsonObject.optString("macVersion");
                        bean.FuelTotalHeight = jsonObject.optString("FuelTotalHeight");
                        bean.carColor = jsonObject.optString("carColor");
                        bean.VideoConfig = jsonObject.optString("VideoConfig");
                        bean.HighTempAlarm = jsonObject.optString("HighTempAlarm");
                        bean.LowTempAlarm = jsonObject.optString("LowTempAlarm");
                        bean.ActivationCode = jsonObject.optString("ActivationCode");
                        bean.VendorCode = jsonObject.optString("VendorCode");
                        bean.OwnerId = jsonObject.optString("OwnerId");
                        bean.PColour = jsonObject.optString("PColour");
                        bean.TerminalType = jsonObject.optString("TerminalType");
                        bean.DeviceCode = jsonObject.optString("DeviceCode");
                        bean.SpeedDuration = jsonObject.optString("SpeedDuration");
                        bindData(bean);
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

    private void bindData(DeviceDetail bean) {
        shebeimingcheng.setText(bean.user_name);
        zhongduanleixing.setText(bean.TerminalType);
        chepaihao.setText(bean.sex);
        chejiahao.setText(bean.VendorCode);
        fadongjihao.setText(bean.product_type);
        imei.setText(bean.sim_id);
        sim.setText(bean.phone);
        iccid.setText(bean.su);
        lianxiren.setText(bean.owner);
        jihuoriqi.setText(bean.use_time);
        daoqiriqi.setText(bean.out_time);
    }
}
