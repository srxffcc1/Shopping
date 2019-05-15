package com.jiudi.shopping.global;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.jiudi.shopping.bean.UserBean;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitManager;
import com.jiudi.shopping.util.CommonUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.widget.GlideImageLoader;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.uuzuche.lib_zxing.ZApplication;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;


/**
 * Created by maqing on 2017/8/11.
 * Email:2856992713@qq.com
 * LocalApplication
 */
public class LocalApplication extends ZApplication {
    private static LocalApplication sApp;
    /**
     * 微信相关
     */
    public static IWXAPI mIWXApi;




    private Context mContext;

    private static final String TAG = "LocalApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        mContext = getApplicationContext();
        initRetrofit();
        regToWX();
        initImagePicker();
        initLoginStatus();
        initOKHttp();

        UMConfigure.init(this,"5cdb78543fc1958a2f000296","umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
//        UMConfigure.setLogEnabled(true);
        PlatformConfig.setWeixin("wx095bafdd282d2421", "");//微信APPID和AppSecret
//        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
//        //QQ
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
//        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
//        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
//        PlatformConfig.setAlipay("2015111700822536");
//        PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
//        PlatformConfig.setPinterest("1439206");
//        PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f");
//        PlatformConfig.setDing("dingoalmlnohc0wggfedpk");
//        PlatformConfig.setVKontakte("5764965", "5My6SNliAaLxEm3Lyd9J");
//        PlatformConfig.setDropbox("oz8v5apet3arcdy", "h7p2pjbzkkxt02a");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initOKHttp() {
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());
    }
    /**
     * 获取MyApplication实例
     */
    public static LocalApplication getInstance() {
        return sApp;
    }

    /**
     * 初始Retrofit
     */
    private void initRetrofit() {
        RequestManager.mRetrofitManager = new RetrofitManager.Builder()
                .baseUrl(RequestManager.mBaseUrl)
                .build();
        RequestManager.mRetrofitManager2 = new RetrofitManager.Builder()
                .baseUrl(RequestManager.mBaseUrl2)
                .build();
        RequestManager.mRetrofitManager3 = new RetrofitManager.Builder()
                .baseUrl(RequestManager.mBaseUrl3)
                .build();
    }
    /**
     * 注册到微信
     */
    private void regToWX() {
        //通过WXAPIFactory工厂获取IWXAPI实例
        mIWXApi = WXAPIFactory.createWXAPI(sApp, Constant.WECHAT_APP_ID, false);
        //将应用的APP_ID注册到微信
        mIWXApi.registerApp(Constant.WECHAT_APP_ID);
    }

    /**
     * 初始化图片选择器
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }
    private void initLoginStatus() { //保存登录状态
        String userBase64 = (String) SPUtil.get(Constant.USER, "");
        if (!"".equals(userBase64)) {
            AccountManager.sUserBean = (UserBean) CommonUtil.base64ToObject(userBase64);
        }
    }
}
