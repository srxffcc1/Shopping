package com.jiudi.shopping.ui.cart;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.CartStatus;
import com.jiudi.shopping.bean.Order;
import com.jiudi.shopping.bean.OrderDetail;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DingDanActivity extends BaseActivity {
    private Order orderbean;
    private OrderDetail orderDetail;
    private android.widget.ImageView head;
    private android.widget.TextView zhuangtai;
    private android.widget.TextView fahuo;
    private android.widget.ImageView dingwei;
    private android.widget.TextView xingming;
    private android.widget.TextView dianhua;
    private android.widget.TextView dizhi;
    private android.widget.TextView chanpinxinxi;
    private android.widget.LinearLayout godss;
    private android.widget.TextView lianxikefu;
    private android.widget.TextView tuikuan;
    private android.widget.RelativeLayout peisongfangshil;
    private android.widget.TextView peisongxinxinxi;
    private android.widget.TextView peisongfangshi;
    private android.widget.TextView kuaidigongsi;
    private android.widget.TextView kuaididanhao;
    private android.widget.TextView peisongfangshiv;
    private android.widget.TextView kuaidigongsiv;
    private android.widget.TextView kuaididanhaov;
    private android.widget.RelativeLayout jiagel;
    private android.widget.TextView shangpinzongjia;
    private android.widget.TextView yuedikou;
    private android.widget.TextView shifukuan;
    private android.widget.TextView shangpinzongjiav;
    private android.widget.TextView yuedikouv;
    private android.widget.TextView shifukuanv;
    private android.widget.TextView dingdanxinxi;
    private android.widget.TextView dingdanbianhao;
    private android.widget.TextView xiadanshijian;
    private android.widget.TextView zhifufangshi;
    private android.widget.TextView zhifuzhuangtai;
    private android.widget.TextView zhifushijian;
    private android.widget.TextView fanhui;
    private android.widget.TextView chakanwuliu;
    private android.widget.TextView querenshouhuo;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_dingdan;
    }

    @Override
    public void initView() {

        head = (ImageView) findViewById(R.id.head);
        zhuangtai = (TextView) findViewById(R.id.zhuangtai);
        fahuo = (TextView) findViewById(R.id.fahuo);
        dingwei = (ImageView) findViewById(R.id.dingwei);
        xingming = (TextView) findViewById(R.id.xingming);
        dianhua = (TextView) findViewById(R.id.dianhua);
        dizhi = (TextView) findViewById(R.id.dizhi);
        chanpinxinxi = (TextView) findViewById(R.id.chanpinxinxi);
        godss = (LinearLayout) findViewById(R.id.godss);
        lianxikefu = (TextView) findViewById(R.id.lianxikefu);
        tuikuan = (TextView) findViewById(R.id.tuikuan);
        peisongfangshil = (RelativeLayout) findViewById(R.id.peisongfangshil);
        peisongxinxinxi = (TextView) findViewById(R.id.peisongxinxinxi);
        peisongfangshi = (TextView) findViewById(R.id.peisongfangshi);
        kuaidigongsi = (TextView) findViewById(R.id.kuaidigongsi);
        kuaididanhao = (TextView) findViewById(R.id.kuaididanhao);
        peisongfangshiv = (TextView) findViewById(R.id.peisongfangshiv);
        kuaidigongsiv = (TextView) findViewById(R.id.kuaidigongsiv);
        kuaididanhaov = (TextView) findViewById(R.id.kuaididanhaov);
        jiagel = (RelativeLayout) findViewById(R.id.jiagel);
        shangpinzongjia = (TextView) findViewById(R.id.shangpinzongjia);
        yuedikou = (TextView) findViewById(R.id.yuedikou);
        shifukuan = (TextView) findViewById(R.id.shifukuan);
        shangpinzongjiav = (TextView) findViewById(R.id.shangpinzongjiav);
        yuedikouv = (TextView) findViewById(R.id.yuedikouv);
        shifukuanv = (TextView) findViewById(R.id.shifukuanv);
        dingdanxinxi = (TextView) findViewById(R.id.dingdanxinxi);
        dingdanbianhao = (TextView) findViewById(R.id.dingdanbianhao);
        xiadanshijian = (TextView) findViewById(R.id.xiadanshijian);
        zhifufangshi = (TextView) findViewById(R.id.zhifufangshi);
        zhifuzhuangtai = (TextView) findViewById(R.id.zhifuzhuangtai);
        zhifushijian = (TextView) findViewById(R.id.zhifushijian);
        fanhui = (TextView) findViewById(R.id.fanhui);
        chakanwuliu = (TextView) findViewById(R.id.chakanwuliu);
        querenshouhuo = (TextView) findViewById(R.id.querenshouhuo);
    }

    @Override
    public void initData() {
        orderbean= (Order) getIntent().getSerializableExtra("bean");
        bindDataToView();
        getOrderById();
//        getWuLiu();
    }

    private void getOrderById() {
        Map<String, String> map = new HashMap<>();
        map.put("uni",getIntent().getStringExtra("uni"));
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getOrderById(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                return new Date(json.getAsJsonPrimitive().getAsLong());
                            }
                        });
                        Gson gson = builder.create();
                        Type orderDetailType = new TypeToken<OrderDetail>() {
                        }.getType();
                        orderDetail=gson.fromJson(res.getJSONObject("data").toString(),orderDetailType);
                        bindDataToView2();
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

    private void bindDataToView2() {
        xiadanshijian.setText("下单时间："+TimeUtil.formatLong(orderDetail.getAdd_time()));
        if("未支付".equals(orderbean.getStatuz().getTitle())){

            zhifushijian.setVisibility(View.GONE);
        }else{

            zhifushijian.setText("支付时间："+TimeUtil.formatLong(orderDetail.getPay_time()));
        }
    }

    public void bindDataToView(){
        String cartlist=new Gson().toJson(orderbean.getCartInfoList());
        buildCartList(godss,cartlist);
        zhuangtai.setText(orderbean.getStatuz().get_title());
        dizhi.setText(orderbean.getUser_address());
        dianhua.setText(orderbean.getUser_phone());
        xingming.setText(orderbean.getReal_name());
        if("未支付".equals(orderbean.getStatuz().get_title())){
            peisongfangshil.setVisibility(View.GONE);
        }
        fahuo.setText(orderbean.getStatuz().getMsg());
        shifukuanv.setText("¥"+orderbean.getPay_price());
        shangpinzongjiav.setText("¥"+orderbean.getTotal_price());
        dingdanbianhao.setText("订单编号："+orderbean.getOrder_id());
        zhifufangshi.setText("支付方式："+orderbean.getStatuz().getPayType());
        zhifuzhuangtai.setText("支付状态："+orderbean.getStatuz().getTitle());
    }
    private void getWuLiu() {
        Map<String, String> map = new HashMap<>();
        map.put("uni","wx2019042517361710007");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getWuLiu(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {

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
    public void initEvent() {
        chakanwuliu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity,WuLiuActivity.class).putExtra("uni",orderbean.getOrder_id()));
            }
        });
    }
    private void buildCartList(ViewGroup viewGroup, String cartInfoListString) {
        try {
            JSONArray cartInfoList = new JSONArray(cartInfoListString);
            viewGroup.removeAllViews();
            for (int i = 0; i < cartInfoList.length(); i++) {
                View cartinfol = LayoutInflater.from(mActivity).inflate(R.layout.item_cart_item, viewGroup, false);
                JSONObject cartInfo = cartInfoList.getJSONObject(i);
                ImageView imageView = cartinfol.findViewById(R.id.cart_icon2);
                TextView cart_title = cartinfol.findViewById(R.id.cart_title);
                TextView cart_cunk = cartinfol.findViewById(R.id.cart_cunk);
                TextView cart_count = cartinfol.findViewById(R.id.cart_count);
                TextView cart_money = cartinfol.findViewById(R.id.cart_money);
                String pic = cartInfo.getJSONObject("productInfo").getString("image");
                String title = cartInfo.getJSONObject("productInfo").getString("store_name");
                try {
                    String cunk = cartInfo.getJSONObject("productInfo").getJSONObject("attrInfo").getString("suk");
                    cart_cunk.setText(cunk);
                } catch (JSONException e) {
                    cart_cunk.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
                String count = "X" + cartInfo.getString("cart_num");
                String money = "¥" + cartInfo.getString("truePrice");
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
                Glide.with(mActivity).load(pic).apply(options).into(imageView);
                cart_title.setText(title);
                cart_count.setText(count);
                cart_money.setText(money);
                viewGroup.addView(cartinfol);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
