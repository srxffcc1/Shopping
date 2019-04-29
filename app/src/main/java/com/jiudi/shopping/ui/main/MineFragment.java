package com.jiudi.shopping.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.jiudi.shopping.global.LocalApplication;
import com.jiudi.shopping.util.WechatUtil;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.UserBean;
import com.jiudi.shopping.event.UpdateLoginStateEvent;
import com.jiudi.shopping.global.Constant;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.user.AllOrderActivity;
import com.jiudi.shopping.ui.user.ParkRecordActivity;
import com.jiudi.shopping.ui.user.PeccancyQueryActivity;
import com.jiudi.shopping.ui.user.UserCarActivity;
import com.jiudi.shopping.ui.user.account.LoginActivity;
import com.jiudi.shopping.ui.user.account.UpdatePwdActivity;
import com.jiudi.shopping.ui.user.account.UserSettingActivity;
import com.jiudi.shopping.util.CommonUtil;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.ImageUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.widget.CircleImageView;
import com.jiudi.shopping.widget.GlideImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Steven on 2017/12/21.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {


    private LinearLayout llFragmentMineLoginStatus;
    private ImageView tvFragmentKefu;
    private CircleImageView ivFragmentMineAvatar;
    private TextView tvFragmentMineName;
    private TextView tvFragmentMinePhone;
    private TextView tvFragmentMineSex;
    private TextView tvFragmentMineInfo;
    private LinearLayout llFragmentOrder;
    private TextView myallorder;
    private TextView daifukuan;
    private TextView daifahuo;
    private TextView daishouhuo;
    private TextView yiwancheng;
    private LinearLayout llFragmentMineAlreadyBuy;
    private LinearLayout llFragmentMineAwardRecord;
    private LinearLayout llFragmentMineRich;
    private LinearLayout llFragmentMineRedBag;
    private LinearLayout llFragmentMineAwardInfo;
    private LinearLayout llFragmentMineUpdatePwd;
    private LinearLayout llFragmentMineSet;

    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_mine;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {

        llFragmentMineLoginStatus = (LinearLayout) findViewById(R.id.ll_fragment_mine_login_status);
        tvFragmentKefu = (ImageView) findViewById(R.id.tv_fragment_kefu);
        ivFragmentMineAvatar = (CircleImageView) findViewById(R.id.iv_fragment_mine_avatar);
        tvFragmentMineName = (TextView) findViewById(R.id.tv_fragment_mine_name);
        tvFragmentMinePhone = (TextView) findViewById(R.id.tv_fragment_mine_phone);
        tvFragmentMineSex = (TextView) findViewById(R.id.tv_fragment_mine_sex);
        tvFragmentMineInfo = (TextView) findViewById(R.id.tv_fragment_mine_info);
        llFragmentOrder = (LinearLayout) findViewById(R.id.ll_fragment_order);
        myallorder = (TextView) findViewById(R.id.myallorder);
        daifukuan = (TextView) findViewById(R.id.daifukuan);
        daifahuo = (TextView) findViewById(R.id.daifahuo);
        daishouhuo = (TextView) findViewById(R.id.daishouhuo);
        yiwancheng = (TextView) findViewById(R.id.yiwancheng);
        llFragmentMineAlreadyBuy = (LinearLayout) findViewById(R.id.ll_fragment_mine_already_buy);
        llFragmentMineAwardRecord = (LinearLayout) findViewById(R.id.ll_fragment_mine_award_record);
        llFragmentMineRich = (LinearLayout) findViewById(R.id.ll_fragment_mine_rich);
        llFragmentMineRedBag = (LinearLayout) findViewById(R.id.ll_fragment_mine_red_bag);
        llFragmentMineAwardInfo = (LinearLayout) findViewById(R.id.ll_fragment_mine_award_info);
        llFragmentMineUpdatePwd = (LinearLayout) findViewById(R.id.ll_fragment_mine_update_pwd);
        llFragmentMineSet = (LinearLayout) findViewById(R.id.ll_fragment_mine_set);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        tvFragmentMineName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WechatUtil.wechatLogin(LocalApplication.mIWXApi);
            }
        });
    }
}