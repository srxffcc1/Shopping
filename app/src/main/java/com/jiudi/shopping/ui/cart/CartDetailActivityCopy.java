package com.jiudi.shopping.ui.cart;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hss01248.dialog.StyledDialog;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.vl.VBannerAdapter;
import com.jiudi.shopping.adapter.vl.VDiscussAdapter;
import com.jiudi.shopping.adapter.vl.VDiscussEndAdapter;
import com.jiudi.shopping.adapter.vl.VDiscussHeadAdapter;
import com.jiudi.shopping.adapter.vl.VIntroduceAdapter;
import com.jiudi.shopping.adapter.vl.VIntroduceHeadAdapter;
import com.jiudi.shopping.adapter.vl.VTitleDetailAdapter;
import com.jiudi.shopping.adapter.vl.VTitleDetailCopyAdapter;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.BannerBean;
import com.jiudi.shopping.bean.CartAttr;
import com.jiudi.shopping.bean.CartAttrValue;
import com.jiudi.shopping.bean.CartDiscussBean;
import com.jiudi.shopping.bean.CartIntroduceBean;
import com.jiudi.shopping.bean.CartTitleBean;
import com.jiudi.shopping.event.PassCartEvent;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.main.MainNewActivity;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.WechatUtil;
import com.m7.imkfsdk.KfStartHelper;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 商品详情
 */
public class CartDetailActivityCopy extends BaseActivity {


    private List<BannerBean> mBannerList = new ArrayList<>();

    final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();
    private List<CartDiscussBean> mcartdiscussbeanlist = new ArrayList<>();
    private List<CartIntroduceBean> mcartdintroducebeanlist = new ArrayList<>();
    private List<CartAttr> mcartattrlist = new ArrayList<>();
    private List<CartAttrValue> mcartattrvaluelist = new ArrayList<>();
    private RecyclerView recycler;

    private DelegateAdapter adapter;
    private SingleLayoutHelper singHelper;
    private SingleLayoutHelper singHelper2;
    private VBannerAdapter vBannerAdapter;
    private VTitleDetailCopyAdapter vDetailAdapter;
    private VDiscussHeadAdapter vDiscussHeadAdapter;
    private VDiscussEndAdapter vDiscussEndAdapter;
    private VIntroduceHeadAdapter vIntroduceHeadAdapter;


    private VDiscussAdapter vDiscussAdapter;
    private VIntroduceAdapter vIntroduceAdapter;
    private CartTitleBean mcarttitlebean;
    private String[] titles = {"宝贝", "评价", "详情"};
    private int[] poss = new int[3];
    private LinearLayoutHelper linearLayoutHelper;
    private LinearLayout titleBar;
    private TabLayout mainTab;
    private boolean isScrolled = false;
    private VirtualLayoutManager manager;
    private TextView topay;
    private TextView togouwu;
    private String productId;//产品id
    private String uniqueId;//商品属性来自 CartAttrValue里的unique
    private TextView kefunum;
    private TextView shoucangnum;
    private TextView gouwuchenum;
    private KfStartHelper helper;
    private Badge badgeView;
    private ImageView backIm;
    private ImageView fenxiangim;
    private String urlShare;
    private Dialog dialog;
    private Map<String, String> destmap=new HashMap<>();


    @Override
    protected int getContentViewId() {
        return R.layout.activity_cart_detail;
    }

    @Override
    public void initView() {

        helper = new KfStartHelper(this);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        titleBar = (LinearLayout) findViewById(R.id.title_bar);
        mainTab = (TabLayout) findViewById(R.id.main_tab);
        mainTab.removeAllTabs();
        for (int i = 0; i < titles.length; i++) {
            //插入tab标签
            mainTab.addTab(mainTab.newTab().setText(titles[i]));
        }
        //标签页可以滑动
//        mainTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        mainTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if (pos == 0) {
                    mainTab.setVisibility(View.GONE);
                    titleBar.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                }
                if (!isScrolled) {
                    Log.v("SRX", "出错调用");
                    manager.scrollToPositionWithOffset(poss[pos], 0);
                }
                //滑动时不能点击,
                //第一个参数是指定的位置，锚点
                // 第二个参数表示 Item 移动到第一项后跟 RecyclerView 上边界或下边界之间的距离（默认是 0）


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if (pos == 0) {
                    mainTab.setVisibility(View.GONE);
                    titleBar.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                }
                if (!isScrolled) {
                    Log.v("SRX", "出错调用");
                    manager.scrollToPositionWithOffset(poss[pos], 0);
                }
                //滑动时不能点击,
                //第一个参数是指定的位置，锚点
                // 第二个参数表示 Item 移动到第一项后跟 RecyclerView 上边界或下边界之间的距离（默认是 0）

            }
        });

        topay = (TextView) findViewById(R.id.topay);
        togouwu = (TextView) findViewById(R.id.togouwu);
        kefunum = (TextView) findViewById(R.id.kefunum);
        shoucangnum = (TextView) findViewById(R.id.shoucangnum);
        gouwuchenum = (TextView) findViewById(R.id.gouwuchenum);

        backIm = (ImageView) findViewById(R.id.back_im);
        fenxiangim = (ImageView) findViewById(R.id.fenxiangim);
    }

    public void getCartNum() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getCartNum(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        int data = res.getInt("data");
                        if (badgeView == null) {

                            badgeView = new QBadgeView(mActivity).setBadgeGravity(Gravity.END | Gravity.TOP).bindTarget(gouwuchenum).setBadgeNumber(data);
                        } else {
                            badgeView.setBadgeNumber(data);
                        }
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

    int nowindex = 0;

    public void buildRecyclerView() {

        manager = new VirtualLayoutManager(this);
        recycler.setLayoutManager(manager);
        adapter = new DelegateAdapter(manager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recycler.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        poss[0] = nowindex;
        vBannerAdapter = new VBannerAdapter(this, new SingleLayoutHelper(), mBannerList);
        adapters.add(vBannerAdapter);

        nowindex += vBannerAdapter.getItemCount();

        vDetailAdapter = new VTitleDetailCopyAdapter(this, new SingleLayoutHelper(), mcarttitlebean);
        adapters.add(vDetailAdapter);

        nowindex += vDetailAdapter.getItemCount();

        poss[1] = nowindex;

        vDiscussHeadAdapter = new VDiscussHeadAdapter(this, new SingleLayoutHelper(), mcarttitlebean,mcartdiscussbeanlist);
        adapters.add(vDiscussHeadAdapter);




        nowindex += vDiscussHeadAdapter.getItemCount();

        vDiscussAdapter = new VDiscussAdapter(this, new LinearLayoutHelper(), mcartdiscussbeanlist);
        adapters.add(vDiscussAdapter);

        nowindex += vDiscussAdapter.getItemCount();

//        vDiscussEndAdapter=new VDiscussEndAdapter(this, new SingleLayoutHelper());
//        adapter.addAdapter(vDiscussEndAdapter);

        poss[2] = nowindex;

        vIntroduceHeadAdapter = new VIntroduceHeadAdapter(this, new SingleLayoutHelper());
        adapters.add(vIntroduceHeadAdapter);


        nowindex += vIntroduceHeadAdapter.getItemCount();

        vIntroduceAdapter = new VIntroduceAdapter(this, new LinearLayoutHelper(), mcartdintroducebeanlist);
        adapters.add(vIntroduceAdapter);


        nowindex += vIntroduceAdapter.getItemCount();


        adapter.setAdapters(adapters);
        recycler.setAdapter(adapter);
    }

    @Override
    public void initData() {

        getGodsDetail();
        getCartNum();


//        getHomeBanner();
    }

    public int getAllAdapterCount() {
        int result = 0;
        for (int i = 0; i < adapters.size(); i++) {
            result += adapters.get(i).getItemCount();
        }
        return result;
    }

    private void getGodsDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getGodsDetailSeKill(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        JSONObject data = res.getJSONObject("data");
                        urlShare = data.optString("urlShare");
                        JSONObject storeInfo = data.getJSONObject("storeInfo");
                        JSONArray images = storeInfo.getJSONArray("images");
                        for (int i = 0; i < images.length(); i++) {
                            BannerBean bannerBean = new BannerBean();
                            bannerBean.pic = images.get(i).toString();
                            mBannerList.add(bannerBean);
                        }
                        mcarttitlebean = new CartTitleBean();
                        mcarttitlebean.id = storeInfo.optString("id");
                        mcarttitlebean.mer_id = storeInfo.optString("mer_id");
                        mcarttitlebean.image = storeInfo.optString("image");
                        mcarttitlebean.store_name = storeInfo.optString("title");
                        mcarttitlebean.store_info = storeInfo.optString("info");
                        mcarttitlebean.keyword = storeInfo.optString("keyword");
                        mcarttitlebean.cate_id = storeInfo.optString("cate_id");
                        mcarttitlebean.price = storeInfo.optString("price");
                        mcarttitlebean.vip_price = storeInfo.optString("vip_price");
                        mcarttitlebean.ot_price = storeInfo.optString("ot_price");

                        mcarttitlebean.stop_time = storeInfo.optString("stop_time");
                        mcarttitlebean.start_time = storeInfo.optString("start_time");


                        mcarttitlebean.postage = storeInfo.optString("postage");
                        mcarttitlebean.basics_commission = storeInfo.optString("basics_commission");
                        mcarttitlebean.parent_commission = storeInfo.optString("parent_commission");
                        mcarttitlebean.unit_name = storeInfo.optString("unit_name");
                        mcarttitlebean.sort = storeInfo.optString("sort");
                        mcarttitlebean.sales = storeInfo.optInt("sales")+storeInfo.optInt("ficti");
                        mcarttitlebean.stock = storeInfo.optString("stock");
                        mcarttitlebean.is_show = storeInfo.optString("is_show");
                        mcarttitlebean.is_hot = storeInfo.optString("is_hot");
                        mcarttitlebean.is_benefit = storeInfo.optString("is_benefit");
                        mcarttitlebean.is_best = storeInfo.optString("is_best");
                        mcarttitlebean.is_new = storeInfo.optString("is_new");
                        mcarttitlebean.description = storeInfo.optString("description");
                        mcarttitlebean.add_time = storeInfo.optString("add_time");
                        mcarttitlebean.is_postage = storeInfo.optString("is_postage");
                        mcarttitlebean.is_del = storeInfo.optString("is_del");
                        mcarttitlebean.is_special = storeInfo.optString("is_special");
                        mcarttitlebean.mer_use = storeInfo.optString("mer_use");
                        mcarttitlebean.give_integral = storeInfo.optString("give_integral");
                        mcarttitlebean.cost = storeInfo.optString("cost");
                        mcarttitlebean.is_seckill = storeInfo.optString("is_seckill");
                        mcarttitlebean.is_bargain = storeInfo.optString("is_bargain");
                        mcarttitlebean.is_integral = storeInfo.optString("is_integral");
                        mcarttitlebean.ficti = storeInfo.optInt("ficti");
                        mcarttitlebean.browse = storeInfo.optString("browse");
                        mcarttitlebean.code_path = storeInfo.optString("code_path");
                        mcarttitlebean.coupon = storeInfo.optString("coupon");
                        mcarttitlebean.userLike = storeInfo.optString("userLike");
                        mcarttitlebean.like_num = storeInfo.optString("like_num");
                        mcarttitlebean.userCollect = storeInfo.optString("userCollect");
                        String replys = data.getJSONArray("reply").toString();
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                return new Date(json.getAsJsonPrimitive().getAsLong());
                            }
                        });
                        Gson gson = builder.create();
                        if ("true".equals(mcarttitlebean.userCollect)) {
                            shoucangnum.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.cart_star), null, null);
                        } else {

                            shoucangnum.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.cart_nostar), null, null);
                        }


                        Type cartdisstype = new TypeToken<List<CartDiscussBean>>() {
                        }.getType();
                        mcartdiscussbeanlist = gson.fromJson(replys, cartdisstype);

                        Pattern pattern = Pattern.compile("src=\\\"(.*?)\\\"");
                        Matcher matcher = pattern.matcher(mcarttitlebean.description);
                        while (matcher.find()) {
                            CartIntroduceBean cartIntroduceBean = new CartIntroduceBean();
                            System.out.println("获得的介绍:" + matcher.group(1));
                            cartIntroduceBean.url = matcher.group(1);
                            mcartdintroducebeanlist.add(cartIntroduceBean);
                        }
                        String attrs = data.getJSONArray("productAttr").toString();
                        Type cartattrtype = new TypeToken<List<CartAttr>>() {
                        }.getType();
                        mcartattrlist = gson.fromJson(attrs, cartattrtype);
//                        mcartdiscussbeanlist=gson.fromJson(replys, cartdisstype);
                        try {
                            JSONObject productValueobj = data.getJSONObject("productValue");
                            Iterator iterator = productValueobj.keys();
                            while (iterator.hasNext()) {
                                String key = iterator.next() + "";
                                Type cartattrvaluetype = new TypeToken<CartAttrValue>() {
                                }.getType();
                                CartAttrValue cartAttrValue = gson.fromJson(productValueobj.getJSONObject(key).toString(), cartattrvaluetype);
                                mcartattrvaluelist.add(cartAttrValue);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }


                        buildRecyclerView();
                        if ("1".equals(mcarttitlebean.is_special)||"1".equals(mcarttitlebean.is_integral)) {
                            togouwu.setVisibility(View.GONE);
                        }
                    }else{
                        Toast.makeText(mActivity,info,Toast.LENGTH_SHORT).show();
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


//    private void getHomeBanner() {
//        Map<String, String> map = new HashMap<>();
//        map.put("customer_id", "42");
//        map.put("pid", "2");
//        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getFlash(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
//            @Override
//            public void onSuccess(String response) {
//                try {
//                    JSONObject res = new JSONObject(response);
//                    int code = res.getInt("code");
//                    String info = res.getString("info");
//                    JSONArray data = res.getJSONObject("data").getJSONArray("banner");
//                    mBannerList.clear();
//                    if (code == 0) {
//                        mBannerList.clear();
//
//                        for (int i = 0; i < data.length(); i++) {
//                            JSONObject bannerItem = data.getJSONObject(i);
//                            BannerBean bannerBean = new BannerBean();
////                            bannerBean.setId(bannerItem.getString("id"));//轮播ID
////                            bannerBean.setImage(bannerItem.getString("image"));//轮播图片
////                            bannerBean.setRemark(bannerItem.getString("remark"));//轮播信息
////                            bannerBean.setUrl(bannerItem.getString("linkurl"));//轮播链接
//                            mBannerList.add(bannerBean);
//                        }
//
//                        adapter.notifyDataSetChanged();
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                if (!NetworkUtil.isConnected()) {
//                    ToastUtil.showShort(mActivity, R.string.net_error);
//                } else {
//                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
//                }
//            }
//        });
//    }

public InputStream getImageStream(String path) throws Exception {
    URL url = new URL(path);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setConnectTimeout(5 * 1000);
    conn.setRequestMethod("GET");
    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
        return conn.getInputStream();
    }
    return null;
}
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void initEvent() {



        fenxiangim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MobclickAgent.onEvent(mActivity,"B_goods_right_top");
                new ShareAction(mActivity).setDisplayList(
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

                                if(share_media==SHARE_MEDIA.WEIXIN){
                                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
                                    byte[] tumb=bmpToByteArray(bmp,true);
                                    WechatUtil.wechatShare(urlShare,"优质商品","九弟智选,优质体验",tumb, SendMessageToWX.Req.WXSceneSession);
                                }
                                if(share_media==SHARE_MEDIA.WEIXIN_CIRCLE){
                                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
                                    byte[] tumb=bmpToByteArray(bmp,true);
                                    WechatUtil.wechatShare(urlShare,"优质商品","九弟智选,优质体验",tumb, SendMessageToWX.Req.WXSceneTimeline);
                                }
                            }
                        }).open();


//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "分享商品");
//                intent.putExtra(Intent.EXTRA_TEXT, urlShare);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(Intent.createChooser(intent, "推荐商品"));
            }
        });
        backIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MobclickAgent.onEvent(mActivity,"B_goods_left_top");
                finish();
            }
        });
        shoucangnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MobclickAgent.onEvent(mActivity,"B_goods_bottom_sc");
                if ("true".equals(mcarttitlebean.userCollect)) {
                    unshoucang();
                } else {
                    shoucang();
                }
            }
        });
        gouwuchenum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mActivity, MainNewActivity.class));
//                EventBus.getDefault().post(new CartEvent());
                EventBus.getDefault().post(new PassCartEvent());
            }
        });
        kefunum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MobclickAgent.onEvent(mActivity,"B_goods_kf");
                helper.initSdkChat("e183f850-6650-11e9-b942-bf7a16e827df", "咨询", AccountManager.sUserBean.uid, 60);//陈辰正式
            }
        });
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //重写该方法主要是判断recyclerview是否在滑动
                //0停止 ，12都是滑动
                if (newState == 0) {
                    isScrolled = false;
                } else {
                    isScrolled = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //这个主要是recyclerview滑动时让tab定位的方法
                if (isScrolled) {
                    int top = manager.findFirstVisibleItemPosition();
                    int bottom = manager.findLastVisibleItemPosition();
                    int pos = 0;
                    for (int i = 0; i < poss.length; i++) {
                        if (top >= poss[i]) {
                            pos = i;
                        }
                    }
                    if (top > 0) {
                        mainTab.setVisibility(View.VISIBLE);
                        titleBar.setBackgroundColor(Color.parseColor("#ffffff"));
                    } else {
                        mainTab.setVisibility(View.GONE);
                        titleBar.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                    }

                    //设置tab滑动到第pos个
                    mainTab.setScrollPosition(pos, 0f, true);
                }

            }
        });
        topay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(mcarttitlebean.is_special)||"1".equals(mcarttitlebean.is_integral)) {
                    productId = mcarttitlebean.id;
                    uniqueId = "0";
                    lijiGouWu();
                } else {

                    MobclickAgent.onEvent(mActivity,"B_goods_bottom_ljgm");
                    popChose(2);
                }
            }
        });
        togouwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MobclickAgent.onEvent(mActivity,"B_goods_bottom_jrgwc");
                popChose(1);
            }
        });

    }

    private void shoucang() {
        Map<String, String> map = new HashMap<>();
        map.put("productId", mcarttitlebean.id);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).shoucang(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {

                        mcarttitlebean.userCollect = "true";
                        shoucangnum.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.cart_star), null, null);
                        Toast.makeText(mActivity, "收藏成功", Toast.LENGTH_SHORT).show();
                    }else{

                        Toast.makeText(mActivity,info,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    Toast.makeText(mActivity,"未登录",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {

                Toast.makeText(mActivity,"未登录",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void unshoucang() {
        Map<String, String> map = new HashMap<>();
        map.put("productId", mcarttitlebean.id);
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).unshoucang(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        mcarttitlebean.userCollect = "false";
                        shoucangnum.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.cart_nostar), null, null);
                        Toast.makeText(mActivity, "取消收藏", Toast.LENGTH_SHORT).show();
                    }else{


                        Toast.makeText(mActivity,info,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {

                Toast.makeText(mActivity,"未登录",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int cartNum = 1;

    public void popChose(final int flag) {

        StyledDialog.init(this);
        cartNum = 1;
        ViewGroup customView2 = (ViewGroup) View.inflate(this, R.layout.popwindow_cart_chose, null);
        dialog = StyledDialog.buildCustom(customView2, Gravity.BOTTOM).setForceWidthPercent(1f).setCancelable(true,true).show();
        ImageView add = customView2.findViewById(R.id.add);
        final TextView count = customView2.findViewById(R.id.count);
        ImageView sub = customView2.findViewById(R.id.sub);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count.setText(++cartNum + "");
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartNum != 1) {
                    count.setText(--cartNum + "");
                }

            }
        });
        ImageView dialog_img = customView2.findViewById(R.id.dialog_img);
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
        Glide.with(mActivity).load(mcarttitlebean.image).apply(options).into(dialog_img);


        TextView dialog_gouwuche = customView2.findViewById(R.id.dialog_gouwuche);
        dialog_gouwuche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    productId = mcartattrvaluelist.get(checkWhichChose()).getProduct_id();
                    uniqueId = mcartattrvaluelist.get(checkWhichChose()).getUnique();
                } catch (Exception e) {
                    productId = mcarttitlebean.id;
                    uniqueId = "0";
                    e.printStackTrace();
                }
                addGouWu();
                dialog.dismiss();
            }
        });

        TextView dialog_lijigoumai = customView2.findViewById(R.id.dialog_lijigoumai);
        dialog_lijigoumai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    productId = mcartattrvaluelist.get(checkWhichChose()).getProduct_id();
                    uniqueId = mcartattrvaluelist.get(checkWhichChose()).getUnique();
                } catch (Exception e) {
                    productId = mcarttitlebean.id;
                    uniqueId = "0";
                    e.printStackTrace();
                }
                if(flag==2){
                    lijiGouWu();
                }else{
                    addGouWu();
                }
                dialog.dismiss();

            }
        });
        if ("1".equals(mcarttitlebean.is_special)||"1".equals(mcarttitlebean.is_integral)) {
            dialog_gouwuche.setVisibility(View.INVISIBLE);
        }
        TextView money = customView2.findViewById(R.id.money);
        money.setText("¥"+("1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))?mcarttitlebean.vip_price:mcarttitlebean.price));
        TextView allcount = customView2.findViewById(R.id.allcount);
        allcount.setText("库存：" + mcarttitlebean.stock + mcarttitlebean.unit_name);
        ImageView close = customView2.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        LinearLayout sukall = customView2.findViewById(R.id.sukall);
        for (int i = 0; i <mcartattrlist.size() ; i++) {
            CartAttr cartAttr=mcartattrlist.get(i);
            sukall.addView(buildTagParent(mActivity, destmap,cartAttr,money));
        }

    }

    private int checkWhichChose() {
        for (int i = 0; i <mcartattrvaluelist.size() ; i++) {
            String suk=mcartattrvaluelist.get(i).getSuk();//得到精准规格
            boolean check=true;
            for (Map.Entry<String, String> entry : destmap.entrySet()) {//迭代规格选择
                check=check&suk.contains(entry.getValue());
                if(!check){
                    break;
                }
            }
            if(check){
                return i;
            }
        }
        return 0;
    }

    private View buildTagParent(final Activity mActivity, final Map<String, String> destmap, final CartAttr cartAttr, final TextView money) {

        LinearLayout tagparent = (LinearLayout) View.inflate(mActivity, R.layout.item_carchoiceflow, null);
        TextView textView=tagparent.findViewById(R.id.title);
        textView.setText(cartAttr.getAttr_name());
        TagFlowLayout tagFlowLayout=(TagFlowLayout) tagparent.findViewById(R.id.id_flowlayout);
        tagFlowLayout.setAdapter( new TagAdapter<String>(cartAttr.getAttr_values()){

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv_tag, parent, false);
                tv.setText(s);
                return tv;
            }
            @Override
            public boolean setSelected(int position, String s)
            {
                return position==0;
            }
        });
        destmap.put(cartAttr.getAttr_name(),cartAttr.getAttr_values().get(0));
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                destmap.put(cartAttr.getAttr_name(),cartAttr.getAttr_values().get(position));
                money.setText("¥"+("1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))?mcartattrvaluelist.get(checkWhichChose()).getVip_price():mcartattrvaluelist.get(checkWhichChose()).getPrice()));
                return true;
            }
        });
        return tagparent;
    }

    private void addGouWu() {
        if ("1".equals(mcarttitlebean.is_special) && "1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))) {
            Toast.makeText(mActivity, "已经是会员不可重复购买", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("productId", productId);
        map.put("cartNum", cartNum + "");
        map.put("uniqueId", uniqueId);
        map.put("combinationId", "");
        map.put("secKillId", "");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).addgoumai(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        Toast.makeText(mActivity, "加入购物车成功", Toast.LENGTH_SHORT).show();
//                        String cartId=res.getJSONObject("data").getString("cartId");
//                        startActivity(new Intent(mActivity, PayDingDanActivity.class).putExtra("cartId",cartId));
                        getCartNum();
                    }else{
                        Toast.makeText(mActivity,info,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {

                Toast.makeText(mActivity,"未登录",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void lijiGouWu() {
        if ("1".equals(mcarttitlebean.is_special) && "1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))) {
            Toast.makeText(mActivity, "已经是会员不可重复购买", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("productId", productId);
        map.put("cartNum", cartNum + "");
        map.put("uniqueId", uniqueId);
        map.put("combinationId", "");
        map.put("secKillId", "");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).lijigoumai(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        String cartId = res.getJSONObject("data").getString("cartId");
                        startActivity(new Intent(mActivity, PayDingDanActivity.class).putExtra("cartId", cartId));
                    }else{
                        Toast.makeText(mActivity,info,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(mActivity,"未登录",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
