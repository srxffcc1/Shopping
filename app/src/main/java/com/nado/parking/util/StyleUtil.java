package com.nado.parking.util;

import android.content.Context;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.BallPulseView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.nado.parking.R;

/**
 * Created by maqing on 2018/3/12.
 * Email:2856992713@qq.com
 */

public class StyleUtil {

    public static void setRefreshStyle(Context context, TwinklingRefreshLayout refreshLayout) {
        ProgressLayout headerView = new ProgressLayout(context);
        headerView.setColorSchemeResources(R.color.colorAccent);
        BallPulseView loadingView = new BallPulseView(context);
        refreshLayout.setHeaderView(headerView);
        refreshLayout.setBottomView(loadingView);
    }

}
