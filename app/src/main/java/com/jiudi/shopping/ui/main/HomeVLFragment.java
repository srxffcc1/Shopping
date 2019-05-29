package com.jiudi.shopping.ui.main;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dengzq.simplerefreshlayout.SimpleRefreshLayout;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.BannerBean;
import com.jiudi.shopping.bean.MainGodsBean;
import com.jiudi.shopping.event.FlashEvent;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.cart.CartDetailActivity;
import com.jiudi.shopping.ui.fenxiao.FenXiaoMenuActivity;
import com.jiudi.shopping.ui.fenxiao.FenXiaoNoActivity;
import com.jiudi.shopping.ui.fenxiao.TuanDuiActivity;
import com.jiudi.shopping.ui.fenxiao.TuiGuangActivity;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.widget.BannerLayout;
import com.jiudi.shopping.widget.SimpleBottomView;
import com.jiudi.shopping.widget.SimpleLoadView;
import com.jiudi.shopping.widget.SimpleRefreshView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页
 */
public class HomeVLFragment extends BaseFragment implements View.OnClickListener {


    private ImageView back;
    private android.widget.EditText searchTag;
    private ImageView searchPass;
    private SimpleRefreshLayout simpleRefresh;
    private NestedScrollView nest;
    private RecyclerView recycler;

    @Override
    public void onClick(View v) {

    }

    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_homevl;
    }

    @Override
    public void initView() {

        back = (ImageView) findViewById(R.id.back);
        searchTag = (EditText) findViewById(R.id.search_tag);
        searchPass = (ImageView) findViewById(R.id.search_pass);
        simpleRefresh = (SimpleRefreshLayout) findViewById(R.id.simple_refresh);
        nest = (NestedScrollView) findViewById(R.id.nest);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
