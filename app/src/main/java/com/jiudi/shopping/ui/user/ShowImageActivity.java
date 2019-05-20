package com.jiudi.shopping.ui.user;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseActivity;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ShowImageActivity extends BaseActivity {
    private uk.co.senab.photoview.PhotoView ivPhoto;
    private android.widget.ProgressBar photoviewProgressbar;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_photo;
    }

    @Override
    public void initView() {

        ivPhoto = (PhotoView) findViewById(R.id.iv_photo);
        photoviewProgressbar = (ProgressBar) findViewById(R.id.photoview_progressbar);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            String url = intent.getStringExtra("URL");
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
            Glide.with(this)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            photoviewProgressbar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .apply(options)
                    .into(ivPhoto);
        }
        ivPhoto.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });
    }

    @Override
    public void initEvent() {

    }
}
