package com.jiudi.shopping.ui.main;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.CartAttr;
import com.jiudi.shopping.bean.GodTypeC;
import com.jiudi.shopping.bean.GodTypeP;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.SPUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客服
 */
public class TypeFragment extends BaseFragment {


    private android.widget.LinearLayout need;
    private List<GodTypeP> godTypePList=new ArrayList<>();

    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_type;
    }

    @Override
    public void initView() {


        need = (LinearLayout) findViewById(R.id.need);
    }

    @Override
    public void initData() {
        getHotList();
    }

    @Override
    public void initEvent() {

    }
    private void getHotList() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getTypeList(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
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
                        Type godtype = new TypeToken<GodTypeP>() {
                        }.getType();
                        JSONArray jsonArray=res.getJSONArray("data");
                        for (int i = 0; i <jsonArray.length() ; i++) {
                            String god=jsonArray.get(i).toString();
                            GodTypeP bean = gson.fromJson(god, godtype);
                            godTypePList.add(bean);

                        }
                        buildDataToView();
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

    private void buildDataToView() {
        need.removeAllViews();
        need.addView(buildTagParent2("全部商品",new ArrayList<GodTypeC>(),false));
        for (int i = 0; i <godTypePList.size() ; i++) {
            GodTypeP godTypeP=godTypePList.get(i);
            need.addView(buildTagParent(godTypeP.cate_name,godTypeP.child,false));
        }
    }
    private View buildTagParent(final String title, final List<GodTypeC> needshow, boolean candelete) {

        LinearLayout tagparent = (LinearLayout) View.inflate(mActivity, R.layout.item_carchoiceflowpass, null);
        TextView textView=tagparent.findViewById(R.id.title);
        ImageView deleteimage=tagparent.findViewById(R.id.needdelete);
        if(candelete){
            deleteimage.setVisibility(View.VISIBLE);
            deleteimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        textView.setText(title);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity,SearchShopActivity.class).putExtra("keyword",title));
            }
        });
        TagFlowLayout tagFlowLayout=(TagFlowLayout) tagparent.findViewById(R.id.id_flowlayout);
        tagFlowLayout.setAdapter( new TagAdapter<GodTypeC>(needshow){

            @Override
            public View getView(FlowLayout parent, int position, GodTypeC s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv_tag3, parent, false);
                tv.setText(s.cate_name);
                return tv;
            }
            @Override
            public boolean setSelected(int position, GodTypeC s)
            {
                return position==0;
            }
        });
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                try {
                    startActivity(new Intent(mActivity,SearchShopActivity.class).putExtra("keyword",needshow.get(position).cate_name));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        return tagparent;
    }
    private View buildTagParent2(String title, final List<GodTypeC> needshow, boolean candelete) {

        LinearLayout tagparent = (LinearLayout) View.inflate(mActivity, R.layout.item_carchoiceflowpasssingle, null);
        TextView textView=tagparent.findViewById(R.id.title);
        ImageView deleteimage=tagparent.findViewById(R.id.needdelete);
        if(candelete){
            deleteimage.setVisibility(View.VISIBLE);
            deleteimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity,SearchShopActivity.class).putExtra("keyword",""));
            }
        });
        textView.setText(title);
        return tagparent;
    }
}
