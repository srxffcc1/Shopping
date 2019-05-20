package com.jiudi.shopping.ui.user;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.aykj.mustinsert.MustInsert;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.JsonBean;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.GetJsonDataUtil;
import com.jiudi.shopping.util.SPUtil;

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
    private String province;
    private String city;
    private String district;

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
            edaddressdetail.setText(getIntent().getStringExtra("detail"));
            edphone.setText(getIntent().getStringExtra("phone"));
            edshouhuoren.setText(getIntent().getStringExtra("real_name"));
            textcity.setText(getIntent().getStringExtra("province")+getIntent().getStringExtra("city")+getIntent().getStringExtra("district"));
            if("1".equals(getIntent().getStringExtra("is_default"))){
                switchdefault.setChecked(true);
            }
            province=getIntent().getStringExtra("province");
            city=getIntent().getStringExtra("city");
            district=getIntent().getStringExtra("district");
        }
    }
    public void clodeKeyBoard(){
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                clodeKeyBoard();
                showPickerView();
            }
        });
        tvLayoutTopBackBarEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MustInsert.checkAllText(mActivity,edshouhuoren,edphone,edaddressdetail)){

                    saveAddress();
                }
            }
        });
    }

    private void saveAddress() {
        Map<String, String> map = new HashMap<>();
//        map.put("customer_id", AccountManager.sUserBean.getId());
        if (getIntent().getStringExtra("id") != null) {
            map.put("id", getIntent().getStringExtra("id"));
        }
        map.put("real_name", edshouhuoren.getText().toString());
        map.put("phone", edphone.getText().toString());
        map.put("province", province);
        map.put("city", city);
        map.put("district", district);
        map.put("detail", edaddressdetail.getText().toString());
        map.put("is_default", switchdefault.isChecked() ? "1" : "0");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).saveAddress(SPUtil.get("head", "").toString(),(getIntent().getStringExtra("id") != null)?"api/auth_api/edit_user_address":"api/auth_api/edit_user_address",RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
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
                province=opt1tx;
                city=opt2tx;
                district=opt3tx;
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
