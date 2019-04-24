package com.nado.shopping.global;

import android.os.Environment;

import java.io.File;

/**
 * Created by maqing on 2017/8/11.
 * Email:2856992713@qq.com
 */
public class Constant {
    /**
     * 应用名
     */
    public static final String APP_NAME = "parking";

    /**
     * SharePreferences文件名
     */
    public static final String SP_NAME = "share_rent_house";

    /**
     * 用户信息
     */
    public static final String USER="user";


    /**
     * 车辆添加信息
     */
    public static final String ADDCAR="addcar";
    /**
     * 停车场信息
     */
    public static final String PARK="park";


    /**
     * SDCard绝对路径
     */
    public static final String SDCARD_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    /**
     * SDCard文件夹
     */
    public static final String SDCARD_PATH = SDCARD_ROOT_PATH + File.separator + "Rent_House";

    /**
     * APK保存路径
     */
    public static final String APK_SAVE_PATH = SDCARD_ROOT_PATH + File.separator + APP_NAME + ".apk";




    /**
     * 微信AppID
     */
    public static final String WECHAT_APP_ID = "wx8869522c15a7955f";

    /**
     * 微信支付地址
     */
    public static final String WECCHAT_PAY_URL = "https://wxpay.wxutil.com/pub_v2/app/app_pay.php";

    public static final String WECHAT_PACKGE="Sign=WXPay";

    /**
     * 每页请求数据条数
     */
    public static final int PAGE_NUM = 10;

}
