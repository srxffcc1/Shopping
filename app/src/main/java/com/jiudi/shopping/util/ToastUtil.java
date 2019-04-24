package com.jiudi.shopping.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Created by licrynoob on 2016/guide_2/12 <br>
 * Copyright (C) 2016 <br>
 * Email:licrynoob@gmail.com <p>
 * Toast工具类
 * 不连续弹出Toast
 * 需初始化sContext
 */
public class ToastUtil {

    private static Toast sToast = null;

    /**
     * 短时间显示Toast
     *
     * @param context context
     * @param message 信息
     */
    public static void showShort(Context context, CharSequence message) {
        if (sToast == null) {
            sToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(message);
        }
        sToast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context context
     * @param message 信息
     */
    public static void showShort(Context context, int message) {
        if (sToast == null) {
            sToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(message);
        }
        sToast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context context
     * @param message 信息
     */
    public static void showLong(Context context, CharSequence message) {
        if (sToast == null) {
            sToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            sToast.setText(message);
        }
        sToast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context context
     * @param message 信息
     */
    public static void showLong(Context context, int message) {
        if (sToast == null) {
            sToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            sToast.setText(message);
        }
        sToast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context  context
     * @param message  信息
     * @param duration 时长
     */
    public static void showDuration(Context context, CharSequence message, int duration) {
        if (sToast == null) {
            sToast = Toast.makeText(context, message, duration);
        } else {
            sToast.setText(message);
        }
        sToast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context  context
     * @param message  信息
     * @param duration 时长
     */
    public static void showDuration(Context context, int message, int duration) {
        if (sToast == null) {
            sToast = Toast.makeText(context, message, duration);
        } else {
            sToast.setText(message);
        }
        sToast.show();
    }

    /**
     * 自定义显示Toast居中带布局背景
     * @param context
     * @param message
     */
    public static void showLocationCenter(Context context, CharSequence message, int ViewId) {
        if (sToast == null) {
            sToast = Toast.makeText(context, message,Toast.LENGTH_LONG);
        } else {
            sToast.setText(message);
        }
        View view = LayoutInflater.from(context).inflate(ViewId, null);
        sToast.setView(view);
        sToast.setGravity(Gravity.CENTER,0,0);
        sToast.show();
    }

}
