package com.jiudi.shopping.manager;


import com.jiudi.shopping.net.RetrofitManager;
import com.jiudi.shopping.util.MD5Util;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by maqing on 2017/8/11.
 * Email:2856992713@qq.com
 * RequestManager
 */
public class RequestManager {
    public static RetrofitManager mRetrofitManager;
//    public static RetrofitManager mRetrofitManager2;
//    public static RetrofitManager mRetrofitManager3;
    public static final String mBaseUrl = "http://mall.jiudi.cn/";
//    public static final String mBaseUrl2 = "http://mall.jiudicar.com/";
//    public static final String mBaseUrl3 = "http://www.jiudi.cn/";

//    public static final String mBaseUrl3 = "http://jiudi.youacloud.com/";
//    public static final String mBaseUrl3 = "http://mall.jiudicar.com/";
//    public static final String mInterfacePrefix = "index.php?g=App&m=Appv1&a=";
//    public static final String mInterfacePrefix2 = "index.php?g=App&m=Appv1&a=";
//    RequestManager.mRetrofitManager
//            .createRequest(RetrofitRequestInterface.class)
//            .login(RequestManager.encryptParams(map))
//            .enqueue(new RetrofitCallBack() {
//        @Override
//        public void onSuccess(String response) {
//            DialogUtil.hideProgress();
//            LogUtil.e(TAG, response);
//            try {
//                JSONObject res = new JSONObject(response);
//                String info = res.getString("info");
//                int code = res.getInt("code");
//                ToastUtil.showShort(mActivity, info);
//                if (code == 0) {
//                    String data = res.getString("data");
//
//                }else{
//                       ToastUtil.showShort(mActivity, info);
//                 }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                LogUtil.e(TAG, e.getMessage());
//                ToastUtil.showShort(mActivity, "数据异常");
//            }
//        }
//
//        @Override
//        public void onError(Throwable t) {
//            DialogUtil.hideProgress();
//            LogUtil.e(TAG, t.getMessage());
//            if (!NetworkUtil.isConnected()) {
//                ToastUtil.showShort(mActivity, "网络未连接");
//            } else {
//                ToastUtil.showShort(mActivity, "请求出错");
//            }
//        }
//    });

    /**
     * 加密:增加时间戳 MD5签名
     *
     * @param map Map集合
     * @return 加密之后的Map集合
     */
    public static Map<String, Object> encryptParamss(Map<String, Object> map) {
        String timestamp = Long.toString(System.currentTimeMillis()).substring(0, 10);
        map.put("timestamp", timestamp);
        String[] array = new String[map.size()];
        int i = 0;
        for (String key : map.keySet()) {
            array[i] = key;
            i++;
        }
        Arrays.sort(array);
        String signature = "";
        for (int j = 0; j < array.length; j++) {
            String key = array[j];
            if (signature.equals("")) {
                signature = signature + key + "=" + map.get(key);
            } else {
                signature = signature + "&" + key + "=" + map.get(key);
            }
            if (j == array.length - 1) {
                signature = signature + "&nado";
            }
        }
        map.put("sig", MD5Util.getMD5Str(signature));
        return map;
    }
    /**
     * 加密:增加时间戳 MD5签名
     *
     * @param map Map集合
     * @return 加密之后的Map集合
     */
    public static Map<String, String> encryptParams(Map<String, String> map) {
        String timestamp = Long.toString(System.currentTimeMillis()).substring(0, 10);
        map.put("timestamp", timestamp);
        String[] array = new String[map.size()];
        int i = 0;
        for (String key : map.keySet()) {
            array[i] = key;
            i++;
        }
        Arrays.sort(array);
        String signature = "";
        for (int j = 0; j < array.length; j++) {
            String key = array[j];
            if (signature.equals("")) {
                signature = signature + key + "=" + map.get(key);
            } else {
                signature = signature + "&" + key + "=" + map.get(key);
            }
            if (j == array.length - 1) {
                signature = signature + "&nado";
            }
        }
        map.put("sig", MD5Util.getMD5Str(signature));
        return map;
    }

}
