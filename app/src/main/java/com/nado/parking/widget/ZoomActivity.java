package com.nado.parking.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.nado.parking.R;
import com.nado.parking.adapter.vp.VpAdapter;
import com.nado.parking.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ZoomActivity extends BaseActivity {
    private ZoomViewPager mZoomViewPager;
    private TextView mTextView;
    private List<View> mViewList = new ArrayList<>();
    private ArrayList<String> mImageList = new ArrayList<>();

    private PopupWindow mPromptPopupWindow;


    @Override
    protected void setSystemUi() {
        if (Build.VERSION.SDK_INT >= 21) {
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            getWindow().getDecorView().setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_zoom;
    }

    @Override
    public void initView() {
        mZoomViewPager = byId(R.id.zvp_activity_zoom);
        mTextView = byId(R.id.tv_activity_zoom);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("position");
        mImageList = bundle.getStringArrayList("image_list");
        for (int i = 0; i < (mImageList != null ? mImageList.size() : 0); i++) {
            mViewList.add(getView(i));
        }
        mZoomViewPager.setAdapter(new VpAdapter(mViewList));
        mZoomViewPager.setCurrentItem(position);
        mTextView.setText((position + 1) + " / " + mImageList.size());
    }

    @Override
    public void initEvent() {
        mZoomViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTextView.setText((position + 1) + " / " + mImageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 启动ZoomActivity
     *
     * @param context   当前Context
     * @param imageList 图片ArrayList
     * @param position  当前选项
     */
    public static void open(Context context, ArrayList<String> imageList, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putStringArrayList("image_list", imageList);
        Intent intent = new Intent();
        intent.setClass(context, ZoomActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 获取View
     *
     * @param position 当前选项
     * @return View
     */
    private View getView(final int position) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(params1);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        final PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imageView);

        Glide.with(this)
                .load(mImageList.get(position))
                .into(new DrawableImageViewTarget(imageView){
                    @Override
                    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        photoViewAttacher.update();
                    }
                });

        photoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });

        photoViewAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });

        linearLayout.addView(imageView);
        return linearLayout;
    }


}
