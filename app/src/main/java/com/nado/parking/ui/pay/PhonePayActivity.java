package com.nado.parking.ui.pay;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fondesa.recyclerviewdivider.RecyclerViewDivider;
import com.nado.parking.R;
import com.nado.parking.adapter.recycler.RecyclerCommonAdapter;
import com.nado.parking.adapter.recycler.base.ViewHolder;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.bean.Money;
import com.nado.parking.bean.SupportMoney;
import com.nado.parking.manager.AccountManager;
import com.nado.parking.manager.RequestManager;
import com.nado.parking.net.RetrofitCallBack;
import com.nado.parking.net.RetrofitRequestInterface;
import com.nado.parking.ui.main.PayAllActivity;
import com.nado.parking.util.DisplayUtil;
import com.nado.parking.util.LogUtil;
import com.nado.parking.util.NetworkUtil;
import com.nado.parking.util.ToastUtil;
import com.nado.parking.widget.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhonePayActivity extends BaseActivity {
    private static final String TAG = "PhonePayActivity";

    private RecyclerCommonAdapter<Money> mCarBeanAdapter;
    private List<Money> mCarChoiceList = new ArrayList<>();
    private int mDataStatus = STATUS_REFRESH;
    private static final int STATUS_REFRESH = 1;
    private static final int STATUS_LOAD = 2;
    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;
    private RecyclerView rvPayAll;
    private android.support.design.widget.TextInputLayout layoutAccount;
    private android.widget.EditText et;
    private TextView tvLayoutTopBackBarStart;
    private android.widget.ImageView passtongxunlu;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_hfpay;
    }

    @Override
    public void initView() {


        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        layoutAccount = (TextInputLayout) findViewById(R.id.layout_account);
        et = (EditText) findViewById(R.id.et);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        passtongxunlu = (ImageView) findViewById(R.id.passtongxunlu);
        rvPayAll = (RecyclerView) findViewById(R.id.rv_pay_all);
        tvLayoutTopBackBarTitle.setText("话费充值");
        tvLayoutTopBackBarEnd.setText("充值记录");
    }

    @Override
    public void initData() {
        getCanPayList();
    }

    @Override
    public void initEvent() {
        tvLayoutTopBackBarEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity,PhonePayHistoryActivity.class));
            }
        });
        passtongxunlu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent,1000);
            }
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isMobileNO(s.toString())) {
                    //显示错误提示
                    layoutAccount.setHint("请输入手机号");
                    layoutAccount.setError("手机号输入错误");
                    layoutAccount.setErrorEnabled(true);
                } else {
                    layoutAccount.setHint("");
                    layoutAccount.setError("手机号正确");
                    layoutAccount.setErrorEnabled(false);
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000){
            if (resultCode==RESULT_OK){
                if (data!=null){
                    Uri uri=data.getData();
                    String[] contact=getPhoneContacts(uri);
                    if (contact!=null){
                        String name=contact[0];//姓名
                        String number=contact[1].replace("-","");//手机号
                        et.setText(number);
                    }
                }
            }
        }
        if(requestCode==PayAllActivity.START_PAY){
            if(resultCode==Activity.RESULT_OK){
                successPay(data);
            }
        }
    }
    /**
     * 读取联系人信息
     * @param uri
     */
    private String[] getPhoneContacts(Uri uri){
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null&&cursor.moveToFirst()) {
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            contact[1]=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.i("contacts",contact[0]);
            Log.i("contactsUsername",contact[1]);
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "13\\d{9}|14[57]\\d{8}|15[012356789]\\d{8}|18[01256789]\\d{8}|17[0678]\\d{8}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    private void getTestList() {
        Map<String, String> map = new HashMap<>();
        map.put("limit", "2");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getCanPhonePay(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    /**
     * 检查手机和充值金额是否正确
     */
    private void checkPay(final String money) {
        if (!isMobileNO(et.getText().toString())) {
            //显示错误提示
            Toast.makeText(getBaseContext(), "检查手机号", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("telephone", et.getText().toString());
            map.put("pervalue", money);
            RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).checkPhonePay(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
                @Override
                public void onSuccess(String response) {

                    try {
                        JSONObject res = new JSONObject(response);
                        int code = res.getInt("code");
                        String info = res.getString("info");
                        if (code == 0) {
                            buildOrder(money, et.getText().toString());
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

    }

    /**
     * 生成订单
     */
    private void buildOrder(final String money, final String tel) {
        Map<String, String> map = new HashMap<>();
        map.put("telephone", tel);
        map.put("pervalue", money);
        map.put("userid", AccountManager.sUserBean.getId());
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).buildOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {

                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        String orderid = res.get("data").toString();//获得的本司订单号
                        String paytypekey = "pay_type";
                        String pervalue = money;//充值金额
                        String url = "index.php?g=app&m=telephone&a=goodspay";
                        Map<String, String> postmap = new HashMap<>();
                        postmap.put("paymm",money);
                        postmap.put("url",url);
                        postmap.put("pervalue",pervalue);
                        postmap.put("paytypekey",paytypekey);
                        postmap.put("orderid",orderid);
                        PayAllActivity.open(PhonePayActivity.this, postmap);//打开充值界面 选择支付类型 然后会访问url交换对应的sign或appid来完成充值
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



    private void successPay(Intent resultdata) {
        Map<String, String> map = new HashMap<>();
        map.put("orderid",resultdata.getStringExtra("orderid"));
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).successOrder(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        finish();
                    }else{
                        Toast.makeText(getBaseContext(),"扣款成功但平台数据存在延迟稍后查看",Toast.LENGTH_SHORT).show();
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

    /**
     * 获得可以充值的数量
     */
    private void getCanPayList() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).getCanPhonePay(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e(TAG, response);
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    mCarChoiceList.clear();
                    if (code == 0) {
                        JSONArray data = res.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);
                            Money bean = new Money();
                            bean.money = jsonObject.optString("money");
                            bean.old_money = jsonObject.optString("old_money");
                            mCarChoiceList.add(bean);
                        }
                        showRecycleView();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                if (!NetworkUtil.isConnected()) {
                    ToastUtil.showShort(mActivity, R.string.net_error);
                } else {
                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
                }
            }
        });
    }

    /**
     * 车主精选
     */
    private void showRecycleView() {
        if (mCarBeanAdapter == null) {


            mCarBeanAdapter = new RecyclerCommonAdapter<Money>(mActivity, R.layout.item_hfpay, mCarChoiceList) {

                @Override
                protected void convert(ViewHolder holder, final Money carChoiceBean, int position) {
                    holder.setText(R.id.price, "售价："+carChoiceBean.money + "元");
                    holder.setText(R.id.old_price, carChoiceBean.old_money + "元");
//                    new GlideImageLoader().displayImage(mActivity,carChoiceBean.picture, (ImageView) holder.getView(R.id.picture));
//                    new GlideImageLoader().displayImage(mActivity,carChoiceBean.picture, (ImageView) holder.getView(R.id.picture));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkPay(carChoiceBean.money);
                        }
                    });


                }

            };


//            rvPayAll.addItemDecoration(RecyclerViewDivider.with(this).build());
            rvPayAll.setAdapter(mCarBeanAdapter);
            rvPayAll.setLayoutManager(new GridLayoutManager(mActivity, 3));
        } else {

            mCarBeanAdapter.notifyDataSetChanged();
        }

    }
}
