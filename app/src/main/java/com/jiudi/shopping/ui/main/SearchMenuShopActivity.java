package com.jiudi.shopping.ui.main;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.alibaba.idst.nls.internal.utils.Base64Encoder;
import com.dengzq.simplerefreshlayout.SimpleRefreshLayout;
import com.github.nukc.LoadMoreWrapper.LoadMoreAdapter;
import com.github.nukc.LoadMoreWrapper.LoadMoreWrapper;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.vl.VHotGridAdapter;
import com.jiudi.shopping.adapter.vl.VHotTabAdapter;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.MainGodsBean;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.SPUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SearchMenuShopActivity extends BaseActivity{


    private ImageView back;
    private ImageView searchPass;
    private EditText searchTag;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycler;
    private InputMethodManager immanager;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_shopsearchmenu;
    }

    @Override
    public void initView() {


        back = (ImageView) findViewById(R.id.back);
        searchPass = (ImageView) findViewById(R.id.search_pass);
        searchTag = (EditText) findViewById(R.id.search_tag);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    public void initData() {

        immanager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        getList("移动");
        searchTag.setText(getIntent().getStringExtra("keyword"));
        getSupportFragmentManager().beginTransaction().replace(R.id.fl3_change,new TypeFragment()).commitAllowingStateLoss();
    }

    @Override
    public void initEvent() {
        searchTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, SearchShopBeforeActivity.class));
                finish();
            }
        });
//        searchPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if("".equals(searchTag.getText().toString())||"null".equals(searchTag.getText().toString())){
//                    Toast.makeText(mActivity,"请输入搜索条件",Toast.LENGTH_SHORT).show();
//                }else{
//                    if (manager.isActive()) {
//                        manager.hideSoftInputFromWindow(searchTag.getApplicationWindowToken(), 0);
//                    }
//                    page=0;
//                    mCarChoiceList.clear();
//                    getList();
//                }
//            }
//        });
//        searchTag.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    //先隐藏键盘
//                    if (manager.isActive()) {
//                        manager.hideSoftInputFromWindow(searchTag.getApplicationWindowToken(), 0);
//                    }
//                    //自己需要的操作
//                    if("".equals(searchTag.getText().toString())||"null".equals(searchTag.getText().toString())){
//                        Toast.makeText(mActivity,"请输入搜索条件",Toast.LENGTH_SHORT).show();
//                    }else{
//
//                        page=0;
//                        mCarChoiceList.clear();
//                        getList();
//                    }
//                }
//                //记得返回false
//                return false;
//            }
//        });

    }
}
