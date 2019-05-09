package com.jiudi.shopping.net;

import com.jiudi.shopping.manager.RequestManager;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by maqing on 2017/8/9.
 * Email:2856992713@qq.com
 * 网络请求描述接口
 */
public interface RetrofitRequestInterface {

    /*首页轮播图*/
    @FormUrlEncoded
    @POST("api/index/get_index")
    Call<String> getFlash(@Header("Authorization") String auth,@FieldMap Map<String, String> params);


    /*获得订单*/
    @FormUrlEncoded
    @POST("api/auth_api/get_user_order_list")
    Call<String> getGoodOrder(@Header("Authorization") String auth,@FieldMap Map<String, String> params);


    /*获得优惠券*/
    @FormUrlEncoded
    @POST("api/my/coupon")
    Call<String> getQuan(@Header("Authorization") String auth,@FieldMap Map<String, String> params);

    /*取消订单*/
    @FormUrlEncoded
    @POST("index.php?g=app&m=appv1&a=CancelOrder")
    Call<String> cancelOrder(@FieldMap Map<String, String> params);



    /*获得地址列表*/
    @FormUrlEncoded
    @POST("api/my/address")
    Call<String> getAddressList(@Header("Authorization") String auth,@FieldMap Map<String, String> params);

    /*获得默认地址*/
    @FormUrlEncoded
    @POST("api/auth_api/user_default_address")
    Call<String> getDefaultAddress(@Header("Authorization") String auth,@FieldMap Map<String, String> params);


    /*获得充值金额*/
    @FormUrlEncoded
    @POST("index.php?g=app&m=telephone&a=telephone_note")
    Call<String> getCanPhonePay(@FieldMap Map<String, String> params);


    /*提现接口*/
    @FormUrlEncoded
    @POST("api/auth_api/user_extract")
    Call<String> tixian(@Header("Authorization") String auth,@FieldMap Map<String, String> params);

    /*获得提现*/
    @FormUrlEncoded
    @POST("api/my/extract")
    Call<String> gettixian(@Header("Authorization") String auth,@FieldMap Map<String, String> params);

    /*获得团队总人数*/
    @FormUrlEncoded
    @POST("api/my/spread_list")
    Call<String> getTuanDuiNumber(@Header("Authorization") String auth,@FieldMap Map<String, String> params);

    /*获得购物车数量*/
    @FormUrlEncoded
    @POST("api/auth_api/get_cart_num")
    Call<String> getCartNum(@Header("Authorization") String auth,@FieldMap Map<String, String> params);

    /*收藏商品*/
    @FormUrlEncoded
    @POST("api/auth_api/collect_product")
    Call<String> shoucang(@Header("Authorization") String auth,@FieldMap Map<String, String> params);

    /*取消收藏商品*/
    @FormUrlEncoded
    @POST("api/auth_api/uncollect_product")
    Call<String> unshoucang(@Header("Authorization") String auth,@FieldMap Map<String, String> params);


    /*立即支付*/
    @FormUrlEncoded
    @POST("api/auth_api/pay_order")
    Call<String> lijizhifu(@Header("Authorization") String auth,@FieldMap Map<String, String> params);


    /*获得收藏*/
    @FormUrlEncoded
    @POST("api/auth_api/get_user_collect_product")
    Call<String> getShouCang(@Header("Authorization") String auth,@FieldMap Map<String, String> params);


    /*获得通知信息*/
    @FormUrlEncoded
    @POST("api/auth_api/get_notice_list")
    Call<String> getTongZhi(@Header("Authorization") String auth,@FieldMap Map<String, String> params);

    /*获得佣金列表*/
    @FormUrlEncoded
    @POST("api/auth_api/user_balance_list")
    Call<String> getYongJinList(@Header("Authorization") String auth,@FieldMap Map<String, String> params);


    /*获得全部评价*/
    @FormUrlEncoded
    @POST("api/auth_api/product_reply_list")
    Call<String> getAllDiscuss(@Header("Authorization") String auth,@FieldMap Map<String, String> params);

    /*获得团队*/
    @FormUrlEncoded
    @POST("api/auth_api/get_spread_list")
    Call<String> getTuanDui(@Header("Authorization") String auth,@FieldMap Map<String, String> params);


    /*获得分销信息*/
    @FormUrlEncoded
    @POST("api/my/user_pro")
    Call<String> getFenXiao(@Header("Authorization") String auth,@FieldMap Map<String, String> params);

    /*获得物流信息*/
    @FormUrlEncoded
    @POST("api/my/express")
    Call<String> getWuLiu(@Header("Authorization") String auth,@FieldMap Map<String, String> params);

    /*获得订单详情*/
    @FormUrlEncoded
    @POST("api/my/order")
    Call<String> getOrderById(@Header("Authorization") String auth,@FieldMap Map<String, String> params);

    /*获得购物车列表*/
    @FormUrlEncoded
    @POST("api/auth_api/get_cart_list")
    Call<String> getShopList(@Header("Authorization") String auth,@FieldMap Map<String, String> params);


    /*修改购物车数量*/
    @FormUrlEncoded
    @POST("api/auth_api/change_cart_num")
    Call<String> changecartNum(@Header("Authorization") String auth,@FieldMap Map<String, String> params);

    /*删除购物车*/
    @FormUrlEncoded
    @POST("api/auth_api/remove_cart")
    Call<String> deleteCart(@Header("Authorization") String auth,@FieldMap Map<String, String> params);


    /*获得优惠券*/
    @FormUrlEncoded
    @POST("api/auth_api/get_use_coupon")
    Call<String> getCoupon(@Header("Authorization") String auth,@FieldMap Map<String, String> params);


    /*获得商品支付信息*/
    @FormUrlEncoded
    @POST("api/store/confirm_order")
    Call<String> getCartPayDetail(@Header("Authorization") String auth,@FieldMap Map<String, String> params);


    /*提交订单*/
    @FormUrlEncoded
    @POST("api/auth_api/create_order")
    Call<String> sendOrder(@Header("Authorization") String auth,@FieldMap Map<String, String> params);


    /*立即购买*/
    @FormUrlEncoded
    @POST("api/auth_api/now_buy")
    Call<String> lijigoumai(@Header("Authorization") String auth,@FieldMap Map<String, String> params);

    /*加入购物*/
    @FormUrlEncoded
    @POST("api/auth_api/set_cart")
    Call<String> addgoumai(@Header("Authorization") String auth,@FieldMap Map<String, String> params);



    /*获得首页商品列表*/
    @FormUrlEncoded
    @POST("api/store/get_best_product_list")
    Call<String> getGodsList(@Header("Authorization") String auth,@FieldMap Map<String, String> params);


    /*获得商品详情*/
    @FormUrlEncoded
    @POST("api/store/detail")
    Call<String> getGodsDetail(@Header("Authorization") String auth,@FieldMap Map<String, String> params);



    /*编辑地址*/
    @FormUrlEncoded
    @POST
    Call<String> saveAddress(@Header("Authorization") String auth,@Url String url, @FieldMap Map<String, String> params);



    /*获得订单详情*/
    @FormUrlEncoded
    @POST("index.php?g=app&m=appv1&a=GetOrderDetails")
    Call<String> getOrderDetail(@FieldMap Map<String, String> params);


    /*支付检查*/
    @FormUrlEncoded
    @POST(RequestManager.mInterfacePrefix + "CheckPay")
    Call<String> checkPay(@FieldMap Map<String, String> params);

    /*支付检查动态设置url*/
    @FormUrlEncoded
    @POST
    Call<String> checkPayDy(@Url String url,  @FieldMap Map<String, String> params);


    /*获取城市*/
    @FormUrlEncoded
    @POST
    Call<String> getAllCityDy(@Url String url,  @FieldMap Map<String, String> params);




    /*获取验证码*/
    @FormUrlEncoded
    @POST("api/login/identifycode")
    Call<String> getCode(@FieldMap Map<String, String> params);

    /*用户注册          页面还有问题需要修改*/
    @FormUrlEncoded
    @POST("api/login/regist")
    Call<String> register(@FieldMap Map<String, String> params);


    /*用户登录*/
    @FormUrlEncoded
    @POST("api/login/login")
    Call<String> login(@FieldMap Map<String, String> params);


    /*发送修改密码验证码，并验证号码是否正确*/
    @FormUrlEncoded
    @POST(RequestManager.mInterfacePrefix + "GetCode2")
    Call<String> sendUpdatePasswordCode(@FieldMap Map<String, String> params);

    /*修改用户密码*/
    @FormUrlEncoded
    @POST(RequestManager.mInterfacePrefix + "ChangePwd")
    Call<String> updatePassword(@FieldMap Map<String, String> params);

    /*修改用户密码*/
    @FormUrlEncoded
    @POST(RequestManager.mInterfacePrefix + "OverLookPass")
    Call<String> forgetPassword(@FieldMap Map<String, String> params);

    /*上传用户头像*/
    @FormUrlEncoded
    @POST("api/images/upload.html")//目前测试
    Call<String> updateHead(@Header("Authorization") String auth,@FieldMap Map<String, String> params);

    /*问题列表*/
    @FormUrlEncoded
    @POST(RequestManager.mInterfacePrefix + "GetHelpList")
    Call<String> questionList(@FieldMap Map<String, String> params);


    /*切换省市区1*/
    @FormUrlEncoded
    @POST(RequestManager.mInterfacePrefix + "ChooseCity")
    Call<String> getChooseCity(@FieldMap Map<String, String> params);


    /*关于我们bug*/
    @FormUrlEncoded
    @POST(RequestManager.mInterfacePrefix + " ")
    Call<String> showAboutUs(@FieldMap Map<String, String> params);

    /*提交意见反馈*/
    @FormUrlEncoded
    @POST(RequestManager.mInterfacePrefix + " ")
    Call<String> addOpinion(@FieldMap Map<String, String> params);



    /*个人信息修改bug*/
    @FormUrlEncoded
    @POST(RequestManager.mInterfacePrefix + "usercapital/changePersonalDate.do")
    Call<String> changePersonalDate(@FieldMap Map<String, String> params);

    /*个人信息*/
    @FormUrlEncoded
    @POST("api/my/index.html")
    Call<String> getPersonalDate(@Header("Authorization") String auth,@FieldMap Map<String, String> params);


    /*获取全部省市区信息*/
    @FormUrlEncoded
    @POST(RequestManager.mInterfacePrefix + "china/showAllChina.do")
    Call<String> showAllChina(@FieldMap Map<String, String> params);


    /*我们的信息*/
    @FormUrlEncoded
    @POST(RequestManager.mInterfacePrefix + " ")
    Call<String> helpCenter(@FieldMap Map<String, String> params);


    /*判断系统版本*/
    @FormUrlEncoded
    @POST(RequestManager.mInterfacePrefix + "CheckVersionforios")
    Call<String> getVersion(@FieldMap Map<String, String> params);


}
