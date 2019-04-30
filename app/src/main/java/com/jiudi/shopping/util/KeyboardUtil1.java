package com.jiudi.shopping.util;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.jiudi.shopping.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 全国普通汽车车牌号规则：
 * 第一位必须是省市简称
 * 第二位必须是城市代码表示的大写字母
 * 后余位数是字母数字组合
 */

public class KeyboardUtil1 {
    private static final String TAG = "KeyboardView";

    private Activity mActivity;
    private KeyboardView mKeyboardView;
    private EditText mEdit[];
    private String provinceShort[];
    private String letterAndDigit[];

    private int currentEditText = 0;//默认当前光标在第一个EditText
    private boolean mType;//8位数车牌判别标志
    /**
     * 省份简称键盘
     */
    private Keyboard provinceKeyboard;
    /**
     * 数字与大写字母键盘
     */
    private Keyboard numberKeyboard;
    private Editable editable;

    private String license = "null_plate";

    /**
     * 选中控件后一个是否空值
     */
    private boolean isNullPlate = true;

    private boolean isLisence;

    public KeyboardUtil1(Activity activity, EditText edits[]) {
        mActivity = activity;
        this.mEdit = edits;

        provinceKeyboard = new Keyboard(activity, R.xml.province_abbreviations);
        numberKeyboard = new Keyboard(activity, R.xml.numbers_or_letters);
        mKeyboardView = (KeyboardView) activity.findViewById(R.id.kv_activity_add_car_keyboard);
        if (currentEditText == 0) {
            mKeyboardView.setKeyboard(provinceKeyboard);
        } else {
            mKeyboardView.setKeyboard(numberKeyboard);
        }
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);

        provinceShort = mActivity.getResources().getStringArray(R.array.keyboard_province);
        letterAndDigit = mActivity.getResources().getStringArray(R.array.keyboard_numbers_letters);
    }




    /**
     * 指定切换软键盘 isNumber false表示要切换为省份简称软键盘 true表示要切换为数字软键盘
     */
    public void changeKeyboard(boolean isNumber) {
        if (isNumber) {
            mKeyboardView.setKeyboard(numberKeyboard);
        } else {
            mKeyboardView.setKeyboard(provinceKeyboard);
        }
    }

    /**
     * 软键盘展示状态
     */
    public boolean isShow() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }

    /**
     * 软键盘展示
     */
    public void showKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mKeyboardView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 软键盘隐藏
     */
    public void hideKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            mKeyboardView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 禁掉系统软键盘
     */
    public void hideSoftInputMethod() {
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }
        if (methodName == null) {
            mEdit[currentVersion].setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(mEdit, false);
            } catch (NoSuchMethodException e) {
                mEdit[currentVersion].setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }


    public void setCurrentPosition(int position) {
        currentEditText = position;
        isNullPlate = true;
        if (currentEditText == 0) {
            //如果currentEditText==0代表当前为省份键盘,
            //切换为省市键盘
            changeKeyboard(false);
        } else {
            //切换为字母数字键盘
            changeKeyboard(true);
        }
        isShow();
    }

}