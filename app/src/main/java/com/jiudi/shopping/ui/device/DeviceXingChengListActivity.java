package com.jiudi.shopping.ui.device;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.XingCheng;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.DisplayUtil;
import com.jiudi.shopping.widget.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
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
public class DeviceXingChengListActivity extends BaseActivity {


    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarStart;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private TextView sumlicheng;
    private TextView sumyouhao;
    private TextView sumxingshishijian;
    private TextView sumxiaofei;
    private android.widget.ImageView deviceEdit;
    private android.support.v7.widget.RecyclerView recycler;
    private RecyclerCommonAdapter<XingCheng> myAdapter;
    private List<XingCheng> mBeanList = new ArrayList<>();
    private TextView time;
    public double sumlichengt=0;
    public double sumyouhaot=0;
    public double sumxingshishijiant=0;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device_xingcheng;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        sumlicheng = (TextView) findViewById(R.id.sumlicheng);
        sumyouhao = (TextView) findViewById(R.id.sumyouhao);
        sumxingshishijian = (TextView) findViewById(R.id.sumxingshishijian);
        sumxiaofei = (TextView) findViewById(R.id.sumxiaofei);
        deviceEdit = (ImageView) findViewById(R.id.device_edit);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        time = (TextView) findViewById(R.id.time);
        tvLayoutTopBackBarTitle.setText("行程");
        StyledDialog.init(this);
    }
    public void startTimePicker(Activity activity, OnTimeSelectListener listener){
//        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
//        new TimePickerDialog.Builder()
//                .setCallBack(back)
//                .setCancelStringId("取消")
//                .setSureStringId("确认")
//                .setTitleStringId("时间选择器")
//                .setYearText("年")
//                .setMonthText("月")
//                .setDayText("日")
//                .setHourText("时")
//                .setMinuteText("分")
//                .setCyclic(true)
//                .setMinMillseconds(System.currentTimeMillis())
//                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
//                .setCurrentMillseconds(System.currentTimeMillis())
//                .setThemeColor(activity.getResources().getColor(R.color.timepicker_dialog_bg))
//                .setType(Type.ALL)
//                .setWheelItemTextNormalColor(activity.getResources().getColor(R.color.timetimepicker_default_text_color))
//                .setWheelItemTextSelectorColor(activity.getResources().getColor(R.color.timepicker_toolbar_bg))
//                .setWheelItemTextSize(12)
//                .build()
//        .show(((FragmentActivity)activity).getSupportFragmentManager(),"timepicker");

        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()))-2,1,1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()))+2,1,1);

        //正确设置方式 原因：注意事项有说明
//        startDate.set(2013,0,1);
//        endDate.set(2020,11,31);
        TimePickerView timePickerView=new TimePickerBuilder(activity, listener).setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确认")//确认按钮文字
//                .setContentSize(18)//滚轮文字大小
//                .setTitleSize(20)//标题文字大小
                .setTitleText("时间")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setDividerColor(Color.DKGRAY)
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setLabel("","","","","","")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDecorView(null)
                .build();
        timePickerView.show();
    }

    private static Long getTimeZero(String specifiedDay,int fix) {
        System.out.println(specifiedDay);
        Date date=null;
        try {
            date= new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)+fix, 0, 0, 0);
        Date beginOfDate = cal.getTime();

        return beginOfDate.getTime();
    }


    @Override
    public void initData() {
        time.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        initList(getTimeZero(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),0)+"",getTimeZero(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),1)+"");
    }

    @Override
    public void initEvent() {
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimePicker(DeviceXingChengListActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        time.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                        initList(getTimeZero(new SimpleDateFormat("yyyy-MM-dd").format(date),0)+"",getTimeZero(new SimpleDateFormat("yyyy-MM-dd").format(date),1)+"");
                    }
                });
            }
        });
        deviceEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeOli();
            }
        });
    }

    private void changeOli() {
        StyledDialog.buildNormalInput("油价修改", "输入数值", "", "确定", "取消", new MyDialogListener() {
            @Override
            public void onGetInput(CharSequence input1, CharSequence input2) {
                super.onGetInput(input1, input2);
                sumxiaofei.setText((Double.parseDouble(sumyouhao.getText().toString())*Double.parseDouble(input1.toString()))+"");
            }

            @Override
            public void onFirst() {

            }

            @Override
            public void onSecond() {

            }
        }).show();
    }

    public String second2hour(int second){
        String result="";
        int hour = second/3600;
        if(hour!=0){
            result+=hour+"时";
        }
        int minute = (second - hour*3600)/60;
        if(minute!=0){
            result+=minute+"分";
        }
        second = second-hour*300-minute*60;
        return result;
    }
    private void initList(String begintime, String endtime) {

         sumlichengt=0;
         sumyouhaot=0;
         sumxingshishijiant=0;
        Map<String, String> map = new HashMap<>();
        map.put("method", "getStrokeV3");
        map.put("beginTime", begintime);
        map.put("endTime", endtime);
        map.put("mapType", "GAODE");
        map.put("macid", AccountManager.sUserBean.obd_macid);
        map.put("mds", AccountManager.sUserBean.obd_mds);
        RequestManager.mRetrofitManager2.createRequest(RetrofitRequestInterface.class).getGuiJiDate(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {

            @Override
            public void onSuccess(String response) {


                try {
                    JSONObject res = new JSONObject(response);
                    String info = res.getString("success");
                    mBeanList.clear();
                    if ("true".equals(info)) {
                        sumlicheng.setText(res.optString("totalmil"));
                        sumxingshishijian.setText(second2hour(Integer.parseInt(res.optString("totalDriveTime"))));
                        sumyouhao.setText(res.optString("totalfuel"));
                        sumxiaofei.setText((res.optDouble("totalfuel")*res.optDouble("oilPrice"))+"");
                        JSONArray jsonArray = res.getJSONArray("rows");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            XingCheng bean = new XingCheng();
                            bean.totalfuelUsed = jsonObject.optString("totalfuelUsed");
                            bean.distance = jsonObject.optString("distance");
                            bean.fuelHKM = jsonObject.optString("fuelHKM");
                            bean.totalSpeedoverSeconds = jsonObject.optString("totalSpeedoverSeconds");
                            bean.maxspeed = jsonObject.optString("maxspeed");
                            bean.brakeTimes = jsonObject.optString("brakeTimes");
                            bean.emergencyBrakeTimes = jsonObject.optString("emergencyBrakeTimes");
                            bean.speedupTimes = jsonObject.optString("speedupTimes");
                            bean.emergencySpeedupTimes = jsonObject.optString("emergencySpeedupTimes");
                            bean.speed = jsonObject.optString("speed");
                            bean.maxTempc = jsonObject.optString("maxTempc");
                            bean.maxEngRPM = jsonObject.optString("maxEngRPM");
                            bean.btime = jsonObject.optString("btime");
                            bean.etime = jsonObject.optString("etime");
                            bean.strokeTime = jsonObject.optString("strokeTime");
                            bean.jsType = jsonObject.optString("jsType");
                            bean.driveTime = jsonObject.optString("driveTime");
                            bean.fraction = jsonObject.optString("fraction");
                            bean.random = jsonObject.optString("random");
                            bean.coulometric = jsonObject.optString("coulometric");
                            bean.powerv = jsonObject.optString("powerv");
                            bean.overtimeDriverMinutes = jsonObject.optString("overtimeDriverMinutes");
                            bean.BeginTime = jsonObject.optString("BeginTime");
                            bean.EndTime = jsonObject.optString("EndTime");
                            bean.IdlingTime = jsonObject.optString("IdlingTime");

                            mBeanList.add(bean);
                        }
                    }
                    showRecycleView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void showRecycleView() {
        if (myAdapter == null) {

            myAdapter = new RecyclerCommonAdapter<XingCheng>(mActivity, R.layout.item_device_xingcheng, mBeanList) {
                @Override
                protected void convert(ViewHolder holder, final XingCheng carChoiceBean, int position) {
                    holder.setText(R.id.devide_time_text,carChoiceBean.btime+" - "+carChoiceBean.etime);

                    holder.setText(R.id.licheng,carChoiceBean.distance+"km");
                    holder.setText(R.id.youhao,carChoiceBean.fuelHKM+"L");
                    holder.setText(R.id.pingjunsudu,carChoiceBean.speed+"km/h");
                    holder.setText(R.id.jijiasu,carChoiceBean.emergencySpeedupTimes+"次");
                    holder.setText(R.id.jijiansu,carChoiceBean.emergencyBrakeTimes+"次");
                    holder.setOnClickListener(R.id.device_hf, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            startActivity(new Intent(v.getContext(),DeviceGuiJiActivity.class).putExtra("from",carChoiceBean.BeginTime).putExtra("to",carChoiceBean.EndTime));
                        }
                    });



                }

            };

            recycler.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, (int) DisplayUtil.dpToPx(mActivity, 1), ContextCompat.getColor(mActivity, R.color.colorLine), false, 2));
            recycler.setAdapter(myAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {
            myAdapter.notifyDataSetChanged();
        }
    }

}
