package com.jiudi.shopping.ui.user.account;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FeedBackActivity extends BaseActivity {
    private LinearLayout mBackLL;
    private TextView mTitleTV;
    private static final String TAG = "FeedBackActivity";
    private EditText mFeedbackContextET;
    private TextView mFeedbackOkTV;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_feed_back;
    }

    @Override
    public void initView() {
        mBackLL = byId(R.id.ll_layout_top_back_bar_back);
        mTitleTV = byId(R.id.tv_layout_top_back_bar_title);
        mTitleTV.setText(R.string.feed_back);
        mFeedbackContextET = byId(R.id.et_activity_feedback_context);
        mFeedbackOkTV = byId(R.id.tv_activity_feedback_ok);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        mFeedbackOkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String context = mFeedbackContextET.getText().toString().trim();
                 if (TextUtils.isEmpty(context)) {
                    ToastUtil.showShort(mContext, getString(R.string.feedback_explain_input));
                } else {
                    if (AccountManager.sUserBean != null) {
                        DialogUtil.showProgress(mActivity,"正在提交意见反馈");
                        submitFeedback(context);
                    } else {
                        startActivity(new Intent(mActivity, LoginActivity.class));
                    }
                }
            }
        });
        mBackLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void submitFeedback(String context) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", AccountManager.sUserBean.getId());
        map.put("content", context);
        Log.e(TAG, map.toString() + "");
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .addOpinion(RequestManager.encryptParams(map))
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
                                ToastUtil.showShort(mContext, info);
                                finish();
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
}
