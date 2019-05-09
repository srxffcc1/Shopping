package com.jiudi.shopping.adapter.recycler;

import android.content.Context;
import android.view.LayoutInflater;

import com.jiudi.shopping.adapter.recycler.base.ItemViewDelegate;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;

import java.util.List;

/**
 * Created by zhy on 16/4/9.
 */
public abstract class RecyclerCommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected boolean isDeleteLoadingMore=true;

    public RecyclerCommonAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                RecyclerCommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

    public  boolean isDeleteLoadingMore(){
        return isDeleteLoadingMore;
    }

    public void setDeleteLoadingMore(boolean deleteLoadingMore) {
        isDeleteLoadingMore = deleteLoadingMore;
    }
}
