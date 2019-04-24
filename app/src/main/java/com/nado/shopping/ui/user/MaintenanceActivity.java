package com.nado.shopping.ui.user;

import android.view.View;
import android.widget.LinearLayout;

import com.nado.shopping.R;
import com.nado.shopping.base.BaseActivity;

/**
 * 作者：Constantine on 2018/7/20.
 * 邮箱：2534159288@qq.com
 */
public class MaintenanceActivity extends BaseActivity {
    private LinearLayout mBackLL;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_maintenance;
    }

    @Override
    public void initView() {
        mBackLL=byId(R.id.ll_layout_top_back_bar_back);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        mBackLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
