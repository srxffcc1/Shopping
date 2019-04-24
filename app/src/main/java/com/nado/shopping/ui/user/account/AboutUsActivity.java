package com.nado.shopping.ui.user.account;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nado.shopping.R;
import com.nado.shopping.base.BaseActivity;
import com.nado.shopping.manager.RequestManager;
import com.nado.shopping.net.RetrofitCallBack;
import com.nado.shopping.net.RetrofitRequestInterface;
import com.nado.shopping.util.DialogUtil;
import com.nado.shopping.util.NetworkUtil;
import com.nado.shopping.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AboutUsActivity extends BaseActivity {
    private LinearLayout mBackLL;
    private TextView mTitleTV;
    private WebView mWebView;
    private static final String TAG = "AboutUsActivity";
    private int mType;
    private String mTitle;
    private String mContent;
    public static final int TYPE_ABOUT_US = 10003;//关于我们
    public static final int TYPE_FOOTBALL = 1;//1 竞彩足球
    public static final int TYPE_BASKETBALL = 2;//2 竞彩篮球
    public static final int TYPE_USER_AGGREMENT = 3;//3 彩票用户协议
    public static final int TYPE_REMARK_WITHDRAW = 4;//4 查看提现备注
    public static final int TYPE_REMARK_BUY = 5;//5 查看充值备注
    @Override
    protected int getContentViewId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView() {
        mBackLL = byId(R.id.ll_layout_top_back_bar_back);
        mTitleTV = byId(R.id.tv_layout_top_back_bar_title);
//        mTitleTV.setText("关于我们");
        mWebView=byId(R.id.wv_activity_notice);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mType = intent.getIntExtra("type", 0);
        mTitle = intent.getStringExtra("title");
        mContent = intent.getStringExtra("content");
        mTitleTV.setText(mTitle);

        if(mType == TYPE_ABOUT_US ){
            getAboutUs("1");
        }else if(mType == TYPE_USER_AGGREMENT){
            getAboutUs("2");
        }else if(mType == TYPE_FOOTBALL || mType == TYPE_BASKETBALL ){
            getHelp();
        }else if(mType == TYPE_REMARK_WITHDRAW || mType == TYPE_REMARK_BUY){
            showRemark(mContent);
        }


//        String wvdata = (String) SPUtil.get("aboutus","");
//        String customHtml = replace("&lt;", "<", wvdata);
//        customHtml = replace("&gt;", ">", customHtml);
//        customHtml = replace("&quot;", "\"", customHtml);
//        customHtml = replace("&amp;nbsp;", "  ", customHtml);
//        customHtml=replace(".jpg\"/>",".jpg\" style=\"max-width:100%;height:auto\"/>",customHtml);
//        customHtml=replace(".png\"/>",".png\" style=\"max-width:100%;height:auto\"/>",customHtml);
//
//        mWebView.loadDataWithBaseURL(null, customHtml, "text/html", "utf-8", null);
    }

    @Override
    public void initEvent() {
        mBackLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
//    private static String replace(String from, String to, String source) {
//        if (source == null || from == null || to == null)
//            return null;
//        StringBuffer bf = new StringBuffer("");
//        int index = -1;
//
//
//        while ((index = source.indexOf(from)) != -1) {
//            bf.append(source.substring(0, index) + to);
//            source = source.substring(index + from.length());
//            index = source.indexOf(from);
//        }
//        bf.append(source);
//        return bf.toString();
//    }


    private void getAboutUs(String type) {
        Map<String, String> map = new HashMap<>();
        //1-关于我们，2-彩票协议，3-用户协议
        map.put("type",type+"");
        Log.e(TAG, map.toString());
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .showAboutUs(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {

                    @Override
                    public void onSuccess(String response) {
                        DialogUtil.hideProgress();
                        Log.e(TAG, response + "");
                        try {

                            JSONObject res = new JSONObject(response);
                            int code = res.getInt("code");
                            String info = res.getString("info");
                            if (code==0) {
                                JSONObject data = res.getJSONObject("data");
                                JSONObject contentObject = data.getJSONObject("au");
                                String context = contentObject.getString("content");
                                try {
                                    StringBuilder sb = new StringBuilder();
                                    InputStream is = null;

                                    is = mActivity.getAssets().open("web.html");
                                    int length;
                                    byte[] buf = new byte[1024];
                                    while ((length = is.read(buf)) != -1) {
                                        sb.append(new String(buf, 0, length, "utf-8"));
                                    }
                                    is.close();
                                    Document document = Jsoup.parse(sb.toString());
                                    Element eleDiv = document.getElementsByClass("text-content").first();
                                    eleDiv.html(context);
                                    Elements eleImg = document.getElementsByTag("img");
                                    if (eleImg.size() != 0) {
                                        for (Element item : eleImg) {
                                            item.attr("style", "width:100%;height:auto");
                                        }
                                    }

                                    Elements eleTab = document.getElementsByTag("table");
                                    if (eleTab.size() != 0) {
                                        for (Element item : eleTab) {
                                            item.attr("width", "100%");
                                        }
                                    }
                                    String newHtml = document.toString();
                                    mWebView.loadDataWithBaseURL(null, newHtml, "text/html", "utf-8", null);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else  {
                                ToastUtil.showShort(mContext, info);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (!NetworkUtil.isConnected()) {
                            ToastUtil.showShort(mContext, "网络未连接");
                        } else {
                            ToastUtil.showShort(mContext, getString(R.string.network_error));
                        }
                    }
                });
    }


    private void getHelp() {
        Map<String, String> map = new HashMap<>();
        map.put("helpType",mType+"");
        Log.e(TAG, map.toString() + "");
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .helpCenter(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {

                    @Override
                    public void onSuccess(String response) {
                        DialogUtil.hideProgress();
                        Log.e(TAG, response + "");
                        try {

                            JSONObject res = new JSONObject(response);
                            int code = res.getInt("code");
                            String info = res.getString("info");
                            if (code==0) {
                                JSONObject data = res.getJSONObject("data");
                                String context = data.getString("content");
                                try {
                                    StringBuilder sb = new StringBuilder();
                                    InputStream is = null;

                                    is = mActivity.getAssets().open("web.html");
                                    int length;
                                    byte[] buf = new byte[1024];
                                    while ((length = is.read(buf)) != -1) {
                                        sb.append(new String(buf, 0, length, "utf-8"));
                                    }
                                    is.close();
                                    Document document = Jsoup.parse(sb.toString());
                                    Element eleDiv = document.getElementsByClass("text-content").first();
                                    eleDiv.html(context);
                                    Elements eleImg = document.getElementsByTag("img");
                                    if (eleImg.size() != 0) {
                                        for (Element item : eleImg) {
                                            item.attr("style", "width:100%;height:auto");
                                        }
                                    }

                                    Elements eleTab = document.getElementsByTag("table");
                                    if (eleTab.size() != 0) {
                                        for (Element item : eleTab) {
                                            item.attr("width", "100%");
                                        }
                                    }
                                    String newHtml = document.toString();
                                    mWebView.loadDataWithBaseURL(null, newHtml, "text/html", "utf-8", null);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else  {
                                ToastUtil.showShort(mContext, info);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (!NetworkUtil.isConnected()) {
                            ToastUtil.showShort(mContext, "网络未连接");
                        } else {
                            ToastUtil.showShort(mContext, getString(R.string.network_error));
                        }
                    }
                });
    }


    private void showRemark(String context){
        try {
            StringBuilder sb = new StringBuilder();
            InputStream is = null;

            is = mActivity.getAssets().open("web.html");
            int length;
            byte[] buf = new byte[1024];
            while ((length = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, length, "utf-8"));
            }
            is.close();
            Document document = Jsoup.parse(sb.toString());
            Element eleDiv = document.getElementsByClass("text-content").first();
            eleDiv.html(context);
            Elements eleImg = document.getElementsByTag("img");
            if (eleImg.size() != 0) {
                for (Element item : eleImg) {
                    item.attr("style", "width:100%;height:auto");
                }
            }

            Elements eleTab = document.getElementsByTag("table");
            if (eleTab.size() != 0) {
                for (Element item : eleTab) {
                    item.attr("width", "100%");
                }
            }
            String newHtml = document.toString();
            mWebView.loadDataWithBaseURL(null, newHtml, "text/html", "utf-8", null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void open(Activity activity, int type, String title,String content) {
        Intent intent = new Intent(activity, AboutUsActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        activity.startActivity(intent);
    }
}
