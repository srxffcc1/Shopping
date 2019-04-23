package com.nado.parking.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.nado.parking.R;
import com.nado.parking.adapter.recycler.RecyclerCommonAdapter;
import com.nado.parking.adapter.recycler.base.ViewHolder;
import com.nado.parking.base.BaseActivity;
import com.nado.parking.bean.QuestionListBean;
import com.nado.parking.manager.AccountManager;
import com.nado.parking.manager.RequestManager;
import com.nado.parking.net.RetrofitCallBack;
import com.nado.parking.net.RetrofitRequestInterface;
import com.nado.parking.util.DialogUtil;
import com.nado.parking.util.LogUtil;
import com.nado.parking.util.NetworkUtil;
import com.nado.parking.util.StyleUtil;
import com.nado.parking.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：Constantine on 2018/7/17.
 * 邮箱：2534159288@qq.com
 */
public class QuestionActivity extends BaseActivity {
    private static final String TAG = "QuestionActivity";

    private LinearLayout mBackLL;
    private TextView mLayoutTopBackBarTitleTV;

    private TwinklingRefreshLayout mRefreshLayout;
    private int mDataStatus = STATUS_REFRESH;
    private static final int STATUS_REFRESH = 1;
    private static final int STATUS_LOAD = 2;
    private int mPage = 1;
    private RecyclerView mRecyclerView;
    private RecyclerCommonAdapter<QuestionListBean> mCommonAdapter;
    private List<QuestionListBean> mListBeans = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_car_question;
    }

    @Override
    public void initView() {
        mBackLL = byId(R.id.ll_layout_top_back_bar_back);
        mLayoutTopBackBarTitleTV = byId(R.id.tv_layout_top_back_bar_title);

        mRefreshLayout = byId(R.id.trl_activity_question_list);
        mRecyclerView = byId(R.id.rv_activity_question_list);
        StyleUtil.setRefreshStyle(mActivity, mRefreshLayout);
    }

    @Override
    public void initData() {
        mLayoutTopBackBarTitleTV.setText(R.string.questions);
//        mMediaController.addView();
        getQuestionList();
    }

    @Override
    public void initEvent() {
        mBackLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mDataStatus = STATUS_REFRESH;
                mPage = 1;
                getQuestionList();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mDataStatus = STATUS_LOAD;
                mPage++;
                getQuestionList();
            }
        });

    }

    /**
     * 获取问题列表
     */
    private void getQuestionList() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("page", "1");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).questionList(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                DialogUtil.hideProgress();
                switch (mDataStatus) {
                    case STATUS_REFRESH:
                        mRefreshLayout.finishRefreshing();
                        break;
                    case STATUS_LOAD:
                        mRefreshLayout.finishLoadmore();
                        break;
                }
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");

                    if (code == 0) {
                        if (mDataStatus == STATUS_REFRESH) {
                            mListBeans.clear();
                        }
                        JSONArray data = res.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject dataItem = data.getJSONObject(i);
                            QuestionListBean questionListBean = new QuestionListBean();
                            questionListBean.setId(dataItem.getString("hid"));
                            questionListBean.setQuestion(dataItem.getString("title"));
                            questionListBean.setAnswer(dataItem.getString("info"));
                            mListBeans.add(questionListBean);
                        }
                        if (mListBeans.size()==0){
                            ToastUtil.showLong(mActivity,"常见问题为空");
                        }else {
                            showQuestionList();
                        }
                    } else {
                        ToastUtil.showShort(mActivity, info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(mActivity, getString(R.string.data_error));
                }
            }

            @Override
            public void onError(Throwable t) {
                if (!NetworkUtil.isConnected()) {
                    ToastUtil.showShort(mContext, getString(R.string.net_error));
                } else {
                    ToastUtil.showShort(mContext, getString(R.string.network_error));
                }
            }
        });
    }

    private void showQuestionList() {
        if (mCommonAdapter == null) {
            mCommonAdapter = new RecyclerCommonAdapter<QuestionListBean>(mActivity, R.layout.item_question_list, mListBeans) {
                @Override
                protected void convert(ViewHolder holder, QuestionListBean questionListBean, int position) {
                    holder.setText(R.id.tv_item_question, questionListBean.getQuestion());
                    final WebView mShowContentWV = holder.getView(R.id.wv_layout_content);
                    LogUtil.e(mShowContentWV.getTitle());
                    showDetailData(questionListBean.getAnswer(), mShowContentWV);
                }
            };
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            mRecyclerView.setAdapter(mCommonAdapter);
        } else {
            mCommonAdapter.notifyDataSetChanged();
        }
    }

    private void showDetailData(String dertail, WebView view) {
        Document detailDocument = Jsoup.parse(dertail);
        String content = detailDocument.html();
        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        try {
            is = getAssets().open("web.html");
            int length;
            byte[] buf = new byte[1024];
            while ((length = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, length, "utf-8"));
            }
            is.close();
            Document document = Jsoup.parse(sb.toString());
            Element eleDiv = document.getElementsByClass("text-content").first();
            eleDiv.html(content);
            Elements eleTab = document.getElementsByTag("table");
            if (eleTab.size() != 0) {
                for (Element item : eleTab) {
                    item.attr("width", "100%");
                }
            }
            Elements eleImg = document.getElementsByTag("img");
            if (eleImg.size() != 0) {
                for (Element item : eleImg) {
                    item.attr("style", "width:100%;height:auto");
                }
            }
            String newHtml = document.toString();
            LogUtil.e(TAG, newHtml);
            LogUtil.e(TAG, view.getTitle());
            view.loadDataWithBaseURL(null, newHtml, "text/html", "utf-8", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void open(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, QuestionActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }
}
