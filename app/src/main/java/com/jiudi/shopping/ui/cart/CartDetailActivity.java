package com.jiudi.shopping.ui.cart;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.BannerBean;
import com.jiudi.shopping.bean.CartAttr;
import com.jiudi.shopping.bean.CartAttrValue;
import com.jiudi.shopping.bean.CartDiscussBean;
import com.jiudi.shopping.bean.CartIntroduceBean;
import com.jiudi.shopping.bean.CartTitleBean;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.SPUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 商品详情
 */
public class CartDetailActivity extends BaseActivity {


    private List<BannerBean> mBannerList = new ArrayList<>();

    final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();
    private List<CartDiscussBean> mcartdiscussbeanlist = new ArrayList<>();
    private List<CartIntroduceBean> mcartdintroducebeanlist=new ArrayList<>();
    private List<CartAttr> mcartattrlist=new ArrayList<>();
    private List<CartAttrValue> mcartattrvaluelist=new ArrayList<>();
    private RecyclerView recycler;

    private DelegateAdapter adapter;
    private SingleLayoutHelper singHelper;
    private SingleLayoutHelper singHelper2;
    private VBannerAdapter vBannerAdapter;
    private VTitleDetailAdapter vDetailAdapter;
    private VDiscussHeadAdapter vDiscussHeadAdapter;
    private VDiscussEndAdapter vDiscussEndAdapter;
    private VIntroduceHeadAdapter vIntroduceHeadAdapter;


    private VDiscussAdapter vDiscussAdapter;
    private VIntroduceAdapter vIntroduceAdapter;
    private CartTitleBean mcarttitlebean;
    private String[] titles = {"宝贝", "评价", "详情"};
    private int[] poss = {0, 3, 8};
    private LinearLayoutHelper linearLayoutHelper;
    private LinearLayout titleBar;
    private TabLayout mainTab;
    private boolean isScrolled = false;
    private VirtualLayoutManager manager;
    private android.widget.TextView topay;
    private TextView togouwu;
    private String productId;//产品id
    private String uniqueId;//商品属性来自 CartAttrValue里的unique


    @Override
    protected int getContentViewId() {
        return R.layout.activity_cart_detail;
    }

    @Override
    public void initView() {

        StyledDialog.init(this);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        titleBar = (LinearLayout) findViewById(R.id.title_bar);
        mainTab = (TabLayout) findViewById(R.id.main_tab);
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
                if(pos==0){
                    mainTab.setVisibility(View.GONE);
                    titleBar.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                }

                    //滑动时不能点击,
                    //第一个参数是指定的位置，锚点
                    // 第二个参数表示 Item 移动到第一项后跟 RecyclerView 上边界或下边界之间的距离（默认是 0）
                    manager.scrollToPositionWithOffset(poss[pos], 0);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if(pos==0){
                    mainTab.setVisibility(View.GONE);
                    titleBar.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                }

                //滑动时不能点击,
                //第一个参数是指定的位置，锚点
                // 第二个参数表示 Item 移动到第一项后跟 RecyclerView 上边界或下边界之间的距离（默认是 0）
                manager.scrollToPositionWithOffset(poss[pos], 0);
            }
        });

        topay = (TextView) findViewById(R.id.topay);
        togouwu = (TextView) findViewById(R.id.togouwu);
    }
    public void buildRecyclerView(){
        manager = new VirtualLayoutManager(this);
        recycler.setLayoutManager(manager);
        adapter = new DelegateAdapter(manager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recycler.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);

        vBannerAdapter = new VBannerAdapter(this,new SingleLayoutHelper(), mBannerList);
        adapters.add(vBannerAdapter);


        vDetailAdapter = new VTitleDetailAdapter(this, new SingleLayoutHelper(), mcarttitlebean);
        adapters.add(vDetailAdapter);


        vDiscussHeadAdapter = new VDiscussHeadAdapter(this, new SingleLayoutHelper());
        adapters.add(vDiscussHeadAdapter);

        vDiscussAdapter = new VDiscussAdapter(this, new LinearLayoutHelper(), mcartdiscussbeanlist);
        adapters.add(vDiscussAdapter);


//        vDiscussEndAdapter=new VDiscussEndAdapter(this, new SingleLayoutHelper());
//        adapter.addAdapter(vDiscussEndAdapter);


        vIntroduceHeadAdapter = new VIntroduceHeadAdapter(this, new SingleLayoutHelper());
        adapters.add(vIntroduceHeadAdapter);

        vIntroduceAdapter = new VIntroduceAdapter(this, new LinearLayoutHelper(), mcartdintroducebeanlist);
        adapters.add(vIntroduceAdapter);


        adapter.setAdapters(adapters);
        recycler.setAdapter(adapter);
    }
    @Override
    public void initData() {

        getGodsDetail();



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
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getGodsDetail(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        JSONObject data=res.getJSONObject("data");
                        JSONObject storeInfo=data.getJSONObject("storeInfo");
                        JSONArray images=storeInfo.getJSONArray("slider_image");
                        for (int i = 0; i <images.length() ; i++) {
                            BannerBean bannerBean=new BannerBean();
                            bannerBean.pic=images.get(i).toString();
                            mBannerList.add(bannerBean);
                        }
                        mcarttitlebean=new CartTitleBean();
                        mcarttitlebean.id=storeInfo.optString("id");
                        mcarttitlebean.mer_id=storeInfo.optString("mer_id");
                        mcarttitlebean.image=storeInfo.optString("image");
                        mcarttitlebean.store_name=storeInfo.optString("store_name");
                        mcarttitlebean.store_info=storeInfo.optString("store_info");
                        mcarttitlebean.keyword=storeInfo.optString("keyword");
                        mcarttitlebean.cate_id=storeInfo.optString("cate_id");
                        mcarttitlebean.price=storeInfo.optString("price");
                        mcarttitlebean.vip_price=storeInfo.optString("vip_price");
                        mcarttitlebean.ot_price=storeInfo.optString("ot_price");
                        mcarttitlebean.postage=storeInfo.optString("postage");
                        mcarttitlebean.basics_commission=storeInfo.optString("basics_commission");
                        mcarttitlebean.parent_commission=storeInfo.optString("parent_commission");
                        mcarttitlebean.unit_name=storeInfo.optString("unit_name");
                        mcarttitlebean.sort=storeInfo.optString("sort");
                        mcarttitlebean.sales=storeInfo.optString("sales");
                        mcarttitlebean.stock=storeInfo.optString("stock");
                        mcarttitlebean.is_show=storeInfo.optString("is_show");
                        mcarttitlebean.is_hot=storeInfo.optString("is_hot");
                        mcarttitlebean.is_benefit=storeInfo.optString("is_benefit");
                        mcarttitlebean.is_best=storeInfo.optString("is_best");
                        mcarttitlebean.is_new=storeInfo.optString("is_new");
                        mcarttitlebean.description=storeInfo.optString("description");
                        mcarttitlebean.add_time=storeInfo.optString("add_time");
                        mcarttitlebean.is_postage=storeInfo.optString("is_postage");
                        mcarttitlebean.is_del=storeInfo.optString("is_del");
                        mcarttitlebean.is_special=storeInfo.optString("is_special");
                        mcarttitlebean.mer_use=storeInfo.optString("mer_use");
                        mcarttitlebean.give_integral=storeInfo.optString("give_integral");
                        mcarttitlebean.cost=storeInfo.optString("cost");
                        mcarttitlebean.is_seckill=storeInfo.optString("is_seckill");
                        mcarttitlebean.is_bargain=storeInfo.optString("is_bargain");
                        mcarttitlebean.ficti=storeInfo.optString("ficti");
                        mcarttitlebean.browse=storeInfo.optString("browse");
                        mcarttitlebean.code_path=storeInfo.optString("code_path");
                        mcarttitlebean.coupon=storeInfo.optString("coupon");
                        mcarttitlebean.userLike=storeInfo.optString("userLike");
                        mcarttitlebean.like_num=storeInfo.optString("like_num");
                        mcarttitlebean.userCollect=storeInfo.optString("userCollect");
                        String replys=data.getJSONArray("reply").toString();
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                return new Date(json.getAsJsonPrimitive().getAsLong());
                            }
                        });
                        Gson gson = builder.create();





                        Type cartdisstype = new TypeToken<List<CartDiscussBean>>() {
                        }.getType();
                        mcartdiscussbeanlist=gson.fromJson(replys, cartdisstype);

                        Pattern pattern=Pattern.compile("src=\\\"(.*?)\\\"");
                        Matcher matcher=pattern.matcher(mcarttitlebean.description);
                        while (matcher.find()){
                            CartIntroduceBean cartIntroduceBean=new CartIntroduceBean();
                            System.out.println("获得的介绍"+matcher.group(1));
                            cartIntroduceBean.url=matcher.group(1);
                            mcartdintroducebeanlist.add(cartIntroduceBean);
                        }
                        String attrs=data.getJSONArray("productAttr").toString();
                        Type cartattrtype = new TypeToken<List<CartAttr>>() {
                        }.getType();
                        mcartattrlist=gson.fromJson(attrs,cartattrtype);
//                        mcartdiscussbeanlist=gson.fromJson(replys, cartdisstype);
                        JSONObject productValueobj=data.getJSONObject("productValue");
                        Iterator iterator = productValueobj.keys();
                        while(iterator.hasNext()){
                            String key = iterator.next() + "";
                            Type cartattrvaluetype = new TypeToken<CartAttrValue>() {
                            }.getType();
                            CartAttrValue cartAttrValue=gson.fromJson(productValueobj.getJSONObject(key).toString(),cartattrvaluetype);
                            mcartattrvaluelist.add(cartAttrValue);
                        }


                        buildRecyclerView();
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

    @Override
    public void initEvent() {
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
//                    System.out.println("滑动到位" + top + "," + bottom);

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
//                    if (bottom == getAllAdapterCount() - 1) {
//                        //先判断滑到底部，tab定位到最后一个
//                        pos = poss.length - 1;
//                    } else if (top == poss[poss.length - 1]) {
//                        //如果top等于指定的位置，对应到tab即可，
//                        pos = poss[poss.length - 1];
//                    } else {
//                        //循环遍历，需要比较i+1的位置，所以循环长度要减1，
//                        //  如果 i<top<i+1,  那么tab应该定位到i位置的字符，不管是向上还是向下滑动
//                        for (int i = 0; i < poss.length - 1; i++) {
//                            if (top == poss[i]) {
//                                pos = i;
//                                break;
//                            } else if (top > poss[i] && top < poss[i + 1]) {
//                                pos = i;
//                                break;
//                            }
//                        }
//                    }

                    //设置tab滑动到第pos个
                    mainTab.setScrollPosition(pos, 0f, true);
                }

            }
        });
        topay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popChose();
            }
        });
        togouwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popChose();
            }
        });

    }
    public int cartNum =1;
    public void popChose(){
        cartNum =1;
        ViewGroup customView2 = (ViewGroup) View.inflate(this,R.layout.popwindow_cart_chose,null);
        final Dialog dialog=StyledDialog.buildCustom(customView2, Gravity.BOTTOM).setForceWidthPercent(1f).show();
        ImageView add=customView2.findViewById(R.id.add);
        final TextView count=customView2.findViewById(R.id.count);
        ImageView sub=customView2.findViewById(R.id.sub);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count.setText(++cartNum +"");
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartNum !=1){
                    count.setText(--cartNum +"");
                }

            }
        });
        ImageView dialog_img=customView2.findViewById(R.id.dialog_img);
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
        Glide.with(mActivity).load(mcarttitlebean.image).apply(options).into(dialog_img);


        TextView dialog_gouwuche=customView2.findViewById(R.id.dialog_gouwuche);
        dialog_gouwuche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productId=mcartattrvaluelist.get(0).getProduct_id();
                uniqueId=mcartattrvaluelist.get(0).getUnique();
                addGouWu();
                dialog.dismiss();
            }
        });

        TextView dialog_lijigoumai=customView2.findViewById(R.id.dialog_lijigoumai);
        dialog_lijigoumai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productId=mcartattrvaluelist.get(0).getProduct_id();
                uniqueId=mcartattrvaluelist.get(0).getUnique();
                lijiGouWu();
                dialog.dismiss();

            }
        });

        TextView money=customView2.findViewById(R.id.money);
        money.setText("¥"+mcarttitlebean.price);
        TextView allcount=customView2.findViewById(R.id.allcount);
        allcount.setText("库存："+mcarttitlebean.stock+mcarttitlebean.unit_name);
        ImageView close=customView2.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        LinearLayout sukall=customView2.findViewById(R.id.sukall);
//        for (int i = 0; i < ; i++) {
//
//        }
    }
    private void addGouWu() {
        Map<String, String> map = new HashMap<>();
        map.put("productId", productId);
        map.put("cartNum", cartNum+"");
        map.put("uniqueId", uniqueId);
        map.put("combinationId", "");
        map.put("secKillId", "");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).addgoumai(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
//                        String cartId=res.getJSONObject("data").getString("cartId");
//                        startActivity(new Intent(mActivity, PayDingDanActivity.class).putExtra("cartId",cartId));
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

    private void lijiGouWu() {
        Map<String, String> map = new HashMap<>();
        map.put("productId", productId);
        map.put("cartNum", cartNum+"");
        map.put("uniqueId", uniqueId);
        map.put("combinationId", "");
        map.put("secKillId", "");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).lijigoumai(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        String cartId=res.getJSONObject("data").getString("cartId");
                        startActivity(new Intent(mActivity, PayDingDanActivity.class).putExtra("cartId",cartId));
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
