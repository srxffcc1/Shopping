package com.jiudi.shopping.adapter.vl;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.jiudi.shopping.R;
import com.jiudi.shopping.bean.RecommendTabBean;

import java.util.List;

/**
 * Created by admin on 2017/5/16.
 */

public class VHotTabAdapter extends DelegateAdapter.Adapter {
    public Context context;
    private LayoutHelper helper;
    private List<RecommendTabBean> mRecommendTabList;
    private TabSelectedListener listener;
    public VHotTabAdapter(Context context, LayoutHelper helper, List<RecommendTabBean> mRecommendTabList,TabSelectedListener listener) {
        this.context = context;
        this.helper = helper;
        this.mRecommendTabList=mRecommendTabList;
        this.listener=listener;

    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_hot_tab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final TabLayout mainTab;
        mainTab = (TabLayout) holder.itemView.findViewById(R.id.main_tab);

        if(mainTab.getTag()==null){
            for (int i = 0; i < mRecommendTabList.size(); i++) {
                //插入tab标签
                mainTab.addTab(mainTab.newTab().setText(mRecommendTabList.get(i).cate_name));
            }
            mainTab.setTabMode(TabLayout.MODE_SCROLLABLE);
            mainTab.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    listener.onTabClick(mRecommendTabList.get(tab.getPosition()).id);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            mainTab.setTag("1");
        }

    }



    @Override
    public int getItemCount() {
        return 1;
    }

    private void initView() {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
    public interface TabSelectedListener{
         void onTabClick(String cid);
    }
}
