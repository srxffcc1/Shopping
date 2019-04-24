package com.jiudi.shopping.util;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Binder;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by licrynoob on 2016/guide_2/12 <br>
 * Copyright (C) 2016 <br>
 * Email:licrynoob@gmail.com <p>
 * 常用工具类
 */
public class CommonUtil {
    /**
     * 文本替换工具
     */
    public static String replace(String from, String to, String source) {
        if (source == null || from == null || to == null)
            return null;
        StringBuffer bf = new StringBuffer("");
        int index = -1;
        while ((index = source.indexOf(from)) != -1) {
            bf.append(source.substring(0, index) + to);
            source = source.substring(index + from.length());
            index = source.indexOf(from);
        }
        bf.append(source);
        return bf.toString();
    }

    /**
     * 判断是否为手机号
     *
     * @param phone 手机号
     * @return true
     */
    public static boolean isPhone(String phone) {
        Pattern p;
        Matcher m;
        boolean b;
        p = Pattern.compile("^[1][0-9][0-9]{9}$");
        m = p.matcher(phone);
        b = m.matches();
        return b;
    }

    /**
     * 密码验证
     *
     * @param password
     * @return
     */
    public static boolean isPassword(String password) {
        Pattern p;
        Matcher m;
        boolean b;
        p = Pattern.compile("[A-Za-z0-9~!@#$%^&*()_+;',.:<>]{6,16}");
        m = p.matcher(password);
        b = m.matches();
        return b;
    }

    /**
     * 判断网络是否连接
     *
     * @return
     */
    public static boolean isNet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断是否是移动数据连接
     */
    public static boolean isMobile(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 打开网络设置界面
     */
    public static void openNetSet(Activity activity, int requestCode) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 对象转为Base64位字符串
     *
     * @param object Object
     * @return base64
     */
    public static String objectToBase64(Object object) {
        String userBase64;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            userBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            return userBase64;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Base64位字符串转为对象
     *
     * @param base64Str 64位字符串
     * @return Object
     */
    public static Object base64ToObject(String base64Str) {
        Object object;
        byte[] buffer = Base64.decode(base64Str, Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
            object = ois.readObject();
            return object;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                bais.close();
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Map 转 JsonStr
     *
     * @param map Map
     * @return JSON字符串
     */
    public static String mapToJson(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "null";
        }
        String json = "{";
        Set<?> keySet = map.keySet();
        for (Object key : keySet) {
            json += "\"" + key + "\":\"" + map.get(key) + "\",";
        }
        json = json.substring(1, json.length() - 2);
        json += "}";
        return json;
    }

    /**
     * JsonStr 转 Map
     *
     * @param json JSON字符串
     * @return Map
     */
    public static Map jsonToMap(String json) {
        String sb = json.substring(1, json.length() - 1);
        String[] name = sb.split("\\\",\\\"");
        String[] nn;
        Map<String, String> map = new HashMap<>();
        for (String aName : name) {
            nn = aName.split("\\\":\\\"");
            map.put(nn[0], nn[1]);
        }
        return map;
    }

    /**
     * 通过资源设置文字颜色
     */
    public static void setTextColorByRes(TextView textView, int colorRes) {
        textView.setTextColor(ContextCompat.getColor(textView.getContext(), colorRes));
    }

    /**
     * 获取非空Str
     *
     * @param tagStr     目标Str
     * @param defaultStr 默认Str
     * @return ResultStr
     */
    public static String setNoNullStr(String tagStr, String defaultStr) {
        if (!TextUtils.isEmpty(tagStr)) {
            return tagStr;
        }
        return defaultStr;
    }

    /**
     * 去除空字符
     *
     * @param param old
     * @return new
     */
    public static String replaceBlank(String param) {
        String dest = "";
        if (!TextUtils.isEmpty(param)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(param);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 获取包信息
     *
     * @return PackageInfo
     */
    public static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {

        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 设置TabLayout的Indicator的长度
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */


    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    public static void openGaoDe(Context context, double lat, double lng, String dName) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("androidamap://route?sourceApplication=FoBenYuan" + "&dlat=" + lat //终点的经度
                        + "&dlon=" + lng + "&dname=" + dName//终点的纬度
                        + "&dev=0" + "&t=2"));
        intent.addCategory("android.intent.category.DEFAULT");
        context.startActivity(intent);
    }

    public static void openBaiDu(Context context, double lat, double lng, String dName) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("baidumap://map/direction?origin=name:我的位置|latlng:" + "93.076171875"
                + "," + "43.6122167682" + "&destination=name:" + dName + "|latlng:" + lat + "," + lng + "&mode=car&sy=0&index=0&target=1"));
        intent.setPackage("com.baidu.BaiduMap");
        context.startActivity(intent); // 启动调用
    }

    public static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    /**
     * 跳转到拨号界面
     *
     * @param activity
     * @param contactNumber
     */
    public static void jumpToDial(Activity activity, String contactNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + contactNumber);
        intent.setData(data);
        activity.startActivity(intent);
    }

    /**
     * 跳转到外部浏览器
     *
     * @param activity
     * @param url
     */
    public static void jumpToBrowser(Activity activity, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    /**
     * 判断闰年
     */
    public static boolean isLeapYear(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否开启浮窗权限,api未公开，使用反射调用
     *
     * @return
     */
    public static boolean hasPermissionFloatWin(Context context) {
        if (android.os.Build.VERSION.SDK_INT < 20) {
            return true;
        }
        try {
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Class c = appOps.getClass();
            Class[] cArg = new Class[3];
            cArg[0] = int.class;
            cArg[1] = int.class;
            cArg[2] = String.class;
            Method lMethod = c.getDeclaredMethod("checkOp", cArg);
            //24是浮窗权限的标记
            return (AppOpsManager.MODE_ALLOWED == (Integer) lMethod.invoke(appOps, 24, Binder.getCallingUid(), context.getPackageName()));
        } catch (Exception e) {
            return false;
        }
    }

    public static Map<String,String> mapStringToMap(String str){
        str=str.substring(1, str.length()-1);
        String[] strs=str.split(",");
        Map<String,String> map = new HashMap<String, String>();
        for (String string : strs) {
            String key=string.split("=")[0];
            String value=string.split("=")[1];
            map.put(key, value);
        }
        return map;
    }


}
