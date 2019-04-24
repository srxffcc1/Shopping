package com.nado.shopping.manager;

import android.content.Context;

import com.nado.shopping.bean.UserBean;
import com.nado.shopping.event.UpdateLoginStateEvent;
import com.nado.shopping.global.Constant;
import com.nado.shopping.util.SPUtil;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by maqing on 2017/8/11.
 * Email:2856992713@qq.com
 * 账号管理类
 */
public class AccountManager {

    public static String bestGood;
    /**
     * 用户
     */
    public static UserBean sUserBean;

    /**
     * 存储用户cookie
     */
    public static void saveCookie() {
        if (sUserBean == null) {
            return;
        }
        SPUtil.put("phone", sUserBean.getTelNumber());
//        SPUtil.put("password", sUserBean.get());
    }

    /**
     * 清除本地cookie
     */
    public static void removeCookie() {
        SPUtil.remove("phone");
        SPUtil.remove("password");
    }

//    /**
//     * 状态改变
//     */
//    public static void logChange() {
//        sUserBean = null;
//        String userBase64 = CommonUtil.objectToBase64(AccountManager.sUserBean);
//        SPUtil.put(Constant.USER, userBase64);
//    }

    /**
     * 退出登录
     */
    public static void logout(Context context) {
        SPUtil.remove(Constant.USER);
        AccountManager.sUserBean = null;
        EventBus.getDefault().post(new UpdateLoginStateEvent());
    }

    public static void setBestGood(String id) {
        bestGood=id;
    }
}
