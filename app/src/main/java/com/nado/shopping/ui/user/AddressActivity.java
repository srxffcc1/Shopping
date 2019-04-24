package com.nado.shopping.ui.user;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.nado.shopping.R;
import com.nado.shopping.base.BaseActivity;
import com.nado.shopping.bean.JsonBean;
import com.nado.shopping.manager.AccountManager;
import com.nado.shopping.manager.RequestManager;
import com.nado.shopping.net.RetrofitCallBack;
import com.nado.shopping.net.RetrofitRequestInterface;
import com.nado.shopping.util.GetJsonDataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressActivity extends BaseActivity {
    private android.widget.RelativeLayout rlLayoutTopBackBar;
    private android.widget.LinearLayout llLayoutTopBackBarBack;
    private android.widget.TextView tvLayoutTopBackBarStart;
    private android.widget.TextView tvLayoutTopBackBarTitle;
    private android.widget.TextView tvLayoutTopBackBarEnd;
    private android.widget.TextView tvLayoutBackTopBarOperate;
    private android.widget.LinearLayout llActivityUserSetShouhuoren;
    private android.widget.EditText edshouhuoren;
    private android.widget.LinearLayout llActivityUserSetPhone;
    private android.widget.EditText edphone;
    private android.widget.LinearLayout llActivityUserSetCityd;
    private android.widget.TextView textcity;
    private android.widget.LinearLayout llActivityUserSetAddressDetail;
    private android.widget.EditText edaddressdetail;
    private android.widget.LinearLayout llActivityUserSetDizhi;
    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private android.widget.Switch switchdefault;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_address_setting;
    }

    @Override
    public void initView() {

        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        llActivityUserSetShouhuoren = (LinearLayout) findViewById(R.id.ll_activity_user_set_shouhuoren);
        edshouhuoren = (EditText) findViewById(R.id.edshouhuoren);
        llActivityUserSetPhone = (LinearLayout) findViewById(R.id.ll_activity_user_set_phone);
        edphone = (EditText) findViewById(R.id.edphone);
        llActivityUserSetCityd = (LinearLayout) findViewById(R.id.ll_activity_user_set_cityd);
        textcity = (TextView) findViewById(R.id.textcity);
        llActivityUserSetAddressDetail = (LinearLayout) findViewById(R.id.ll_activity_user_set_address_detail);
        edaddressdetail = (EditText) findViewById(R.id.edaddressdetail);
        llActivityUserSetDizhi = (LinearLayout) findViewById(R.id.ll_activity_user_set_dizhi);
        switchdefault = (Switch) findViewById(R.id.switchdefault);
        tvLayoutTopBackBarTitle.setText("编辑地址");
        tvLayoutTopBackBarEnd.setText("确定");
    }

    @Override
    public void initData() {
        initJsonData();
        if(getIntent().getStringExtra("id") != null){
            edaddressdetail.setText(getIntent().getStringExtra("detailed_address"));
            edphone.setText(getIntent().getStringExtra("receiver_mobile"));
            edshouhuoren.setText(getIntent().getStringExtra("receiver_name"));
            textcity.setText(getIntent().getStringExtra("region"));
            if("1".equals(getIntent().getStringExtra("is_default"))){
                switchdefault.setChecked(true);
            }
        }
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }

    }

    @Override
    public void initEvent() {
        llActivityUserSetCityd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerView();
            }
        });
        tvLayoutTopBackBarEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress();
            }
        });
    }

    private void saveAddress() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        if (getIntent().getStringExtra("id") != null) {
            map.put("id", getIntent().getStringExtra("id"));
        }
        map.put("receiver_name", edshouhuoren.getText().toString());
        map.put("receiver_mobile", edphone.getText().toString());
        map.put("region", textcity.getText().toString());
        map.put("detailed_address", edaddressdetail.getText().toString());
        map.put("is_default", switchdefault.isChecked() ? "1" : "0");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).saveAddress((getIntent().getStringExtra("id") != null)?"index.php?g=app&m=appv1&a=post_updateAddress_json":"index.php?g=app&m=appv1&a=post_addAddress_json",RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
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

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                String tx = opt1tx + opt2tx + opt3tx;
                textcity.setText(tx);
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }
}
