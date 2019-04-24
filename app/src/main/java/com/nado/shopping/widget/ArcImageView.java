package com.nado.shopping.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nado.shopping.R;


/**
 * Created by maqing on 2017/11/27.
 * Email:2856992713@qq.com
 */

public class ArcImageView extends ImageView {
    /**
     * 弧高
     */
    private int mArcHeight;
    private int mClipWidth;

    public ArcImageView(Context context) {
        this(context, null);
    }

    public ArcImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcImageView);
        mArcHeight = typedArray.getDimensionPixelSize(R.styleable.ArcImageView_arcHeight, 0);
        mClipWidth = typedArray.getDimensionPixelOffset(R.styleable.ArcImageView_clipWidth, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, getHeight());
        path.lineTo(mClipWidth, getHeight());

        RectF leftRectF = new RectF(mClipWidth,getHeight()-mArcHeight,mClipWidth+mArcHeight*2,getHeight()+mArcHeight);
        path.arcTo(leftRectF,-180,90);

        path.lineTo(getWidth()-mClipWidth-mArcHeight*2,getHeight()-mArcHeight);

        RectF rightRectF = new RectF(getWidth()-mClipWidth-mArcHeight*2,getHeight()-mArcHeight,getWidth()-mClipWidth,getHeight()+mArcHeight);
        path.arcTo(rightRectF,-90,90);

        path.lineTo(getWidth(), getHeight());

        path.lineTo(getWidth(), 0);

        path.close();

        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
