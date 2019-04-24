package com.jiudi.shopping.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiudi.shopping.R;
import com.jiudi.shopping.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maqing on 2017/8/14.
 * Email:2856992713@qq.com
 * 菜单容器
 */
public class MenuContainer extends LinearLayout {
    private Context mContext;
    private String[] mTitleArray;
    private int[] mIconArray;
    private String[] mIconUrlArray;
    public int mColumnNum = 3;
    private OnItemClickListener mItemClickListener;
    private int mIconWidth;
    private int mIconHeight;
    private ImageView.ScaleType mIconScaleType = ImageView.ScaleType.CENTER_CROP;

    private List<View> mIconViewList=new ArrayList<>();

    private static final String TAG = "MenuContainer";

    public MenuContainer(Context context) {
        this(context, null);
    }

    public MenuContainer(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mIconWidth = (int) DisplayUtil.dpToPx(mContext, 40);
        mIconHeight = (int) DisplayUtil.dpToPx(mContext, 40);
        this.setOrientation(LinearLayout.VERTICAL);
    }

    private void addChild() {

        if (mTitleArray == null || mTitleArray.length == 0) {
            return;
        }

        final int lineNum = mTitleArray.length / mColumnNum;

        for (int i = 0; i < lineNum; i++) {
            LinearLayout container = new LinearLayout(mContext);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            container.setLayoutParams(lp);
            container.setOrientation(LinearLayout.HORIZONTAL);

            for (int j = 0; j < mColumnNum; j++) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_category_menu, null);
                LayoutParams viewLP = new LayoutParams(DisplayUtil.getScreenWidth(mContext) / mColumnNum, LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(viewLP);
                ImageView iconIV = (ImageView) view.findViewById(R.id.iv_item_category_menu_icon);
                LayoutParams iconLP = (LayoutParams) iconIV.getLayoutParams();
                iconLP.width = mIconWidth;
                iconLP.height = mIconHeight;
                iconIV.setLayoutParams(iconLP);
                iconIV.setScaleType(mIconScaleType);

                //添加到icon列表
                mIconViewList.add(iconIV);

                TextView titleTV = (TextView) view.findViewById(R.id.tv_item_category_menu_title);

                if (mIconArray != null && mIconArray.length > 0) {
                    iconIV.setImageResource(mIconArray[i * mColumnNum + j]);
                } else if (mIconUrlArray != null && mIconUrlArray.length > 0) {
                    Glide.with(mContext)
                            .load(mIconUrlArray[i * mColumnNum + j])
                            .into(iconIV);
                }
                titleTV.setText(mTitleArray[i * mColumnNum + j]);
                container.addView(view);

                final int position = i * mColumnNum + j;
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClick(position);
                        }
                    }
                });
            }
            addView(container);
        }

        int restNum = mTitleArray.length % mColumnNum;

        if (restNum > 0) {

            LinearLayout container = new LinearLayout(mContext);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            container.setLayoutParams(lp);
            container.setOrientation(LinearLayout.HORIZONTAL);

            for (int i = 0; i < restNum; i++) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_category_menu, null);
                LayoutParams viewLP = new LayoutParams(DisplayUtil.getScreenWidth(mContext) / mColumnNum, LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(viewLP);

                ImageView iconIV = (ImageView) view.findViewById(R.id.iv_item_category_menu_icon);
                LayoutParams iconLP = (LayoutParams) iconIV.getLayoutParams();
                iconLP.width = mIconWidth;
                iconLP.height = mIconHeight;
                iconIV.setLayoutParams(iconLP);
                iconIV.setScaleType(mIconScaleType);

                //添加到icon列表
                mIconViewList.add(iconIV);

                TextView titleTV = (TextView) view.findViewById(R.id.tv_item_category_menu_title);

                if (mIconArray != null && mIconArray.length > 0) {
                    iconIV.setImageResource(mIconArray[lineNum * mColumnNum + i]);
                } else if (mIconUrlArray != null && mIconUrlArray.length > 0) {
                    Glide.with(mContext)
                            .load(mIconUrlArray[lineNum * mColumnNum + i])
                            .into(iconIV);
                }
                titleTV.setText(mTitleArray[lineNum * mColumnNum + i]);
                container.addView(view);

                final int position = lineNum * mColumnNum + i;
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClick(position);
                        }
                    }
                });
            }
            addView(container);
        }
    }

    public void setTitleArray(String[] titleArray) {
        mTitleArray = titleArray;
    }

    public void setIconArray(int[] iconArray) {
        mIconArray = iconArray;
    }

    public void setIconUrlArray(String[] iconUrlArray) {
        mIconUrlArray = iconUrlArray;
    }

    public void setColumnNum(int columnNum) {
        mColumnNum = columnNum;
    }

    public void setIconWidth(int iconWidth) {
        mIconWidth = iconWidth;
    }

    public void setIconHeight(int iconHeight) {
        mIconHeight = iconHeight;
    }

    public void setIconScaleType(ImageView.ScaleType iconScaleType) {
        mIconScaleType = iconScaleType;
    }

    public void show() {
        removeAllViews();
        addChild();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public View getIconView(int position) {
        if (position < getChildCount()) {
            return mIconViewList.get(position);
        } else {

            return null;
        }
    }

}
