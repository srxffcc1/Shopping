package com.jiudi.shopping.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.idst.nls.internal.utils.Base64Encoder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dengzq.simplerefreshlayout.SimpleRefreshLayout;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.CartAttr;
import com.jiudi.shopping.bean.MainGodsBean;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.cart.CartDetailActivity;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.widget.ClearEditText;
import com.jiudi.shopping.widget.SimpleBottomView;
import com.jiudi.shopping.widget.SimpleLoadView;
import com.jiudi.shopping.widget.SimpleRefreshView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchShopBeforeActivity extends BaseActivity {


    private ImageView back;
    private ClearEditText searchTag;
    private android.widget.TextView searchPass;
    private LinearLayout need;
    private List<String> historyseach=new ArrayList<>();
    private List<String> hotseach=new ArrayList<>();
    InputMethodManager manager;//输入法管理器

    @Override
    protected int getContentViewId() {
        return R.layout.activity_shopsearch_before;
    }

    @Override
    public void initView() {


        back = (ImageView) findViewById(R.id.back);
        searchTag = (ClearEditText) findViewById(R.id.search_tag);
        searchPass = (TextView) findViewById(R.id.search_pass);
        need = (LinearLayout) findViewById(R.id.need);

    }

    @Override
    public void initData() {


        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        getHotList();
    }
    private void getHotList() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getHotList(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        JSONArray jsonArray=res.getJSONArray("data");
                        for (int i = 0; i <jsonArray.length() ; i++) {
                            hotseach.add(jsonArray.get(i).toString());
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                buildDataToView2();
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void buildDataToView2() {
        String oldhistory=SPUtil.get("seach", "").toString();
        String[] oldarray=oldhistory.split(",");
        if(oldarray!=null){
            for (int i = 0; i <oldarray.length ; i++) {
                if(!"".equals(oldarray[i].trim())){

                    historyseach.add(oldarray[i]);
                }
            }
        }
        buildDataToView();
    }

    private void buildDataToView() {
        need.removeAllViews();
        if(historyseach.size()>0){
            need.addView(buildTagParent("搜索历史",historyseach,true));
        }
        need.addView(buildTagParent("热门搜索",hotseach,false));
    }


    @Override
    public void initEvent() {
        searchPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(searchTag.getText().toString())||"null".equals(searchTag.getText().toString())){
                    Toast.makeText(mActivity,"请输入搜索条件",Toast.LENGTH_SHORT).show();
                }else{
                    if (manager.isActive()) {
                        manager.hideSoftInputFromWindow(searchTag.getApplicationWindowToken(), 0);
                    }
                    try {
                        startActivity(new Intent(mActivity,SearchShopActivity.class).putExtra("keyword",searchTag.getText().toString()));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        searchTag.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //先隐藏键盘
                    if (manager.isActive()) {
                        manager.hideSoftInputFromWindow(searchTag.getApplicationWindowToken(), 0);
                    }
                    //自己需要的操作
                    if("".equals(searchTag.getText().toString())||"null".equals(searchTag.getText().toString())){
                        Toast.makeText(mActivity,"请输入搜索条件",Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            startActivity(new Intent(mActivity,SearchShopActivity.class).putExtra("keyword",searchTag.getText().toString()));
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                //记得返回false
                return false;
            }
        });
    }
    private View buildTagParent(String title, final List<String> needshow,boolean candelete) {

        LinearLayout tagparent = (LinearLayout) View.inflate(mActivity, R.layout.item_carchoiceflow, null);
        TextView textView=tagparent.findViewById(R.id.title);
        ImageView deleteimage=tagparent.findViewById(R.id.needdelete);
        if(candelete){
            deleteimage.setVisibility(View.VISIBLE);
            deleteimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteHistory();
                }
            });
        }
        textView.setText(title);
        TagFlowLayout tagFlowLayout=(TagFlowLayout) tagparent.findViewById(R.id.id_flowlayout);
        tagFlowLayout.setAdapter( new TagAdapter<String>(needshow){

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv_tag2, parent, false);
                tv.setText(s);
                return tv;
            }
            @Override
            public boolean setSelected(int position, String s)
            {
                return position==0;
            }
        });
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                try {
                    startActivity(new Intent(mActivity,SearchShopActivity.class).putExtra("keyword",needshow.get(position)));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        return tagparent;
    }

    private void deleteHistory() {
        historyseach.clear();
        SPUtil.put("seach","");
        buildDataToView();
    }

}
