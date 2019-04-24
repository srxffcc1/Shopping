package com.nado.shopping.ui.main;

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
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.nado.shopping.R;
import com.nado.shopping.base.BaseFragment;
import com.nado.shopping.bean.UserBean;
import com.nado.shopping.event.UpdateLoginStateEvent;
import com.nado.shopping.global.Constant;
import com.nado.shopping.manager.AccountManager;
import com.nado.shopping.manager.RequestManager;
import com.nado.shopping.net.RetrofitCallBack;
import com.nado.shopping.net.RetrofitRequestInterface;
import com.nado.shopping.ui.user.AllOrderActivity;
import com.nado.shopping.ui.user.ParkRecordActivity;
import com.nado.shopping.ui.user.PeccancyQueryActivity;
import com.nado.shopping.ui.user.UserCarActivity;
import com.nado.shopping.ui.user.account.LoginActivity;
import com.nado.shopping.ui.user.account.UpdatePwdActivity;
import com.nado.shopping.ui.user.account.UserSettingActivity;
import com.nado.shopping.util.CommonUtil;
import com.nado.shopping.util.DialogUtil;
import com.nado.shopping.util.ImageUtil;
import com.nado.shopping.util.LogUtil;
import com.nado.shopping.util.NetworkUtil;
import com.nado.shopping.util.SPUtil;
import com.nado.shopping.util.ToastUtil;
import com.nado.shopping.widget.CircleImageView;
import com.nado.shopping.widget.GlideImageLoader;

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
    private LinearLayout mLoginStatusLL, mAlreadyBuyLL, mAwardRecordLL, mRichLL, mRedBagLL, mAwardInfoLL, mScoreMallLL, mSetLL;
    private CircleImageView mAvatarIV;
    private TextView mNicknameTV;
    private TextView mUserPhoneTV;
    private TextView mSexTV;

    private ImageView mServiceIV;
    private TextView mUserInfoTV;
    private static final String TAG = "MineFragment";

    private PopupWindow mBottomPopwindow;
    private PopupWindow mExpectPopwindow;
    private PopupWindow mServicePopwindow;

    /**
     * 获取头像
     */
    private String mPhotoPath;
    private String mCropPath;
    public static final int REQUEST_CODE_IMAGE = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    public static final int REQUEST_CODE_CROP = 3;
    public static final int PERMISSION_CODE_TAKE_PHOTO = 100;
    private TextView myallorder;
    private TextView daifukuan;
    private TextView daifahuo;
    private TextView daishouhuo;
    private TextView yiwancheng;
    private LinearLayout llFragmentOrder;

    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView() {
        mLoginStatusLL = byId(R.id.ll_fragment_mine_login_status);
        mAlreadyBuyLL = byId(R.id.ll_fragment_mine_already_buy);
        mAwardRecordLL = byId(R.id.ll_fragment_mine_award_record);
        mRichLL = byId(R.id.ll_fragment_mine_rich);
        mRedBagLL = byId(R.id.ll_fragment_mine_red_bag);
        mAwardInfoLL = byId(R.id.ll_fragment_mine_award_info);
        mScoreMallLL = byId(R.id.ll_fragment_mine_update_pwd);
        mSetLL = byId(R.id.ll_fragment_mine_set);
        mAvatarIV = byId(R.id.iv_fragment_mine_avatar);
        mNicknameTV = byId(R.id.tv_fragment_mine_name);
        mUserPhoneTV = byId(R.id.tv_fragment_mine_phone);
        mSexTV = byId(R.id.tv_fragment_mine_sex);

        mServiceIV = byId(R.id.tv_fragment_kefu);
        mUserInfoTV = byId(R.id.tv_fragment_mine_info);


        myallorder = (TextView) findViewById(R.id.myallorder);
        daifukuan = (TextView) findViewById(R.id.daifukuan);
        daifahuo = (TextView) findViewById(R.id.daifahuo);
        daishouhuo = (TextView) findViewById(R.id.daishouhuo);
        yiwancheng = (TextView) findViewById(R.id.yiwancheng);
        llFragmentOrder = (LinearLayout) findViewById(R.id.ll_fragment_order);
    }

    @Override
    public void initData() {
        StyledDialog.init(getActivity());
        EventBus.getDefault().register(mFragment);
        initUserInfo();
        initImagePicker();

    }

    private void initUserInfo() {

        if (AccountManager.sUserBean != null) {
            getUserInfo();
        } else {
            setUserInfo();
        }
    }

    @Override
    public void initEvent() {
        mLoginStatusLL.setOnClickListener(this);
        mAlreadyBuyLL.setOnClickListener(this);
        mAwardRecordLL.setOnClickListener(this);
        mRichLL.setOnClickListener(this);
        mRedBagLL.setOnClickListener(this);
        mAwardInfoLL.setOnClickListener(this);
        mScoreMallLL.setOnClickListener(this);
        mSetLL.setOnClickListener(this);
        mUserInfoTV.setOnClickListener(this);
        mAvatarIV.setOnClickListener(this);
        mServiceIV.setOnClickListener(this);

        llFragmentOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AllOrderActivity.class));
            }
        });
        daifukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("order_status",1));
            }
        });
        daifahuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("order_status",2));
            }
        });
        daishouhuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("order_status",3));
            }
        });
        yiwancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("order_status",4));
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (AccountManager.sUserBean == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
        } else {
            switch (view.getId()) {
                case R.id.iv_fragment_mine_avatar:
                    if (AccountManager.sUserBean == null) {
                        startActivity(new Intent(mActivity, LoginActivity.class));
                    } else {
                        changeUserHeadPopwindow();
                    }
                    break;
                case R.id.tv_fragment_mine_info://查看或编辑个人主页

                    break;
                case R.id.tv_fragment_kefu:

//                    if (AccountManager.sUserBean == null) {
//                        startActivity(new Intent(mActivity, LoginActivity.class));
//                    } else {
//                        MQImage.setImageLoader(new MQGlideImageLoader4());
//
//                        HashMap<String, String> clientInfo = new HashMap<>();
//                        clientInfo.put("name", AccountManager.sUserBean.getNickname());
//                        clientInfo.put("avatar", AccountManager.sUserBean.getAvatar());

                        //指定客服
//                Intent intent = new MQIntentBuilder(mActivity)
//                        .setScheduledAgent("42445cf5603e7047e86904e471480aba")
//                        .setScheduleRule(MQScheduleRule.REDIRECT_ENTERPRISE)
//                        .setClientInfo(clientInfo)
//                        .build();


                        //指定客服组
//                        Intent intent = new MQIntentBuilder(mActivity, CustomerMQConversationActivity.class)
//                                .setScheduledGroup("678d5e0d676d01d1ae42253f05c9cb74")
//                                .setScheduleRule(MQScheduleRule.REDIRECT_ENTERPRISE)
//                                .setCustomizedId(AccountManager.sUserBean.getId()) // 相同的 id 会被识别为同一个顾客
//                                .setClientInfo(clientInfo)
//                                .build();


//                        startActivity(intent);
//                        showExpectPopWindow();
//                    }
                    showConectServicePopWindow();
                    break;

                case R.id.ll_fragment_mine_already_buy:
                    startActivity(new Intent(mActivity, UserCarActivity.class));
                    break;
                case R.id.ll_fragment_mine_award_record:
                    startActivity(new Intent(mActivity, ParkRecordActivity.class));
                    break;
                case R.id.ll_fragment_mine_rich:
//                    showExpectPopWindow();
                    startActivity(new Intent(mActivity, PeccancyQueryActivity.class));
                    break;
                case R.id.ll_fragment_mine_red_bag:
                    showExpectPopWindow();
                    break;
                case R.id.ll_fragment_mine_award_info:
                    showExpectPopWindow();
                    break;
                case R.id.ll_fragment_mine_update_pwd:
                    UpdatePwdActivity.open(mActivity, UpdatePwdActivity.EXTRA_UPDATE);
                    break;
                case R.id.ll_fragment_mine_set:
//                    showOutExitPopwindow();

                    startActivity(new Intent(mActivity, UserSettingActivity.class));
                    break;

            }
        }
    }

    /**
     * 敬请期待弹窗
     */
    private void showExpectPopWindow() {
        if (mExpectPopwindow != null && mExpectPopwindow.isShowing()) {
            mExpectPopwindow.dismiss();
        } else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.pop_window_expect_load, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mExpectPopwindow != null && mExpectPopwindow.isShowing()) {
                        mExpectPopwindow.dismiss();
                    }
                }
            });
            mExpectPopwindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

            mExpectPopwindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
        }
    }

    /**
     * 敬联系客服
     */
    private void showConectServicePopWindow() {
        if (mServicePopwindow != null && mServicePopwindow.isShowing()) {
            mServicePopwindow.dismiss();
        } else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.popwindow_contact_service, null);

            final TextView phoneNumberTV=(TextView) view.findViewById(R.id.tv_popwindow_service_phone_number);
            TextView payCancel = (TextView) view.findViewById(R.id.tv_popwindow_service_cancel);
            TextView payConfirm = (TextView) view.findViewById(R.id.tv_popwindow_service_confirm);
            TextView lastUnpaid = view.findViewById(R.id.tv_popwindow_pay_last_unpaid);

            payCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.showLong(mActivity, getString(R.string.cancel));
                    mServicePopwindow.dismiss();
                }
            });

            payConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mServicePopwindow.dismiss();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + phoneNumberTV.getText().toString().trim());
                    intent.setData(data);
                    startActivity(intent);
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mServicePopwindow != null && mServicePopwindow.isShowing()) {
                        mServicePopwindow.dismiss();
                    }
                }
            });
            mServicePopwindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

            mServicePopwindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
        }
    }

    /**
     * 获取个人信息
     */
    private void getUserInfo() {
        Map<String, String> map = new HashMap<>();
        if (AccountManager.sUserBean != null) {
            System.out.println("初始化");
            map.put("phone", AccountManager.sUserBean.getTelNumber());
            map.put("pass", AccountManager.sUserBean.getPassWord());

            LogUtil.e(TAG, map.toString());
            RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).login(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtil.e(TAG, response);
                    DialogUtil.hideProgress();
                    try {
                        JSONObject res = new JSONObject(response);
                        int code = res.getInt("code");
                        String info = res.getString("info");
                        if (code == 0) {
                            JSONObject data = res.getJSONObject("data");
                            AccountManager.sUserBean.setNickName(data.getString("nicename"));
                            AccountManager.sUserBean.setHeadPortrait(data.getString("avatar"));
                            AccountManager.sUserBean.setTelNumber(data.getString("phone"));
//                            AccountManager.sUserBean.setSex(data.getString("sex"));
//                            AccountManager.sUserBean.setWealth(data.getDouble("money"));

                            AccountManager.sUserBean.id=data.getString("id");
                            AccountManager.sUserBean.phone=data.getString("phone");
                            AccountManager.sUserBean.nicename=data.getString("nicename");
                            AccountManager.sUserBean.obd_pass=data.getString("obd_pass");
                            AccountManager.sUserBean.avatar=data.getString("avatar");
                            AccountManager.sUserBean.obd_macid=data.getString("obd_macid");
                            AccountManager.sUserBean.obd_id=data.getString("obd_id");
                            AccountManager.sUserBean.obd_mds=data.getString("obd_mds");
                            AccountManager.sUserBean.obd_user_id=data.getString("obd_user_id");
                            AccountManager.sUserBean.obd_isbind=data.getString("obd_isbind");
                            AccountManager.sUserBean.jpush_id=data.getString("jpush_id");

                            setUserInfo();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.showShort(mActivity, getString(R.string.json_error));
                        LogUtil.e(TAG, e.getMessage());
                    }

                }

                @Override
                public void onError(Throwable t) {
                    DialogUtil.hideProgress();
                    if (!NetworkUtil.isConnected()) {
                        ToastUtil.showShort(mActivity, R.string.not_net);
                    } else {
                        ToastUtil.showShort(mActivity, getString(R.string.net_error));
                    }
                }
            });

        }


    }

    private void setUserInfo() {
        if (AccountManager.sUserBean == null) {
            RequestOptions requestOptions = RequestOptions.circleCropTransform().error(R.drawable.head_defuat_circle);
            Glide.with(mActivity).load(R.drawable.head_defuat_circle).apply(requestOptions).into(mAvatarIV);
            mNicknameTV.setText(R.string.mine_login_regist);
            mNicknameTV.setVisibility(View.VISIBLE);
            mSexTV.setVisibility(View.GONE);
            mUserPhoneTV.setVisibility(View.GONE);
        } else {
            LogUtil.e(TAG,"1="+ AccountManager.sUserBean.getHeadPortrait());
            RequestOptions requestOptions = RequestOptions.circleCropTransform().error(R.drawable.head_defuat_circle);
            Glide.with(mActivity).load(AccountManager.sUserBean.getHeadPortrait()).apply(requestOptions).into(mAvatarIV);

            mNicknameTV.setText(AccountManager.sUserBean.getNickName());
            mUserPhoneTV.setText(AccountManager.sUserBean.getTelNumber());
            LogUtil.e(TAG,"2="+ AccountManager.sUserBean.getTelNumber());
            mUserPhoneTV.setVisibility(View.VISIBLE);
            mNicknameTV.setVisibility(View.GONE);
            mSexTV.setVisibility(View.GONE);
//            switch (AccountManager.sUserBean.getSex()) {
//                case "1":
//                    mSexTV.setText("女");
//                    break;
//                case "2":
//                    mSexTV.setText("男");
//                    break;
//                case "0":
//                    mSexTV.setVisibility(View.GONE);
//                    break;
//            }

        }
    }

    /**
     * 初始化图片编辑器
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(false);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000); //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000); //保存文件的高度。单位像素
    }

    private void showOutExitPopwindow() {
        if (mBottomPopwindow != null && mBottomPopwindow.isShowing()) {
            mBottomPopwindow.dismiss();
        } else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.popup_window_exit_login, null, false);
            TextView mOkTv = (TextView) view.findViewById(R.id.tv_popwindow_exit);
            TextView mCancelTv = (TextView) view.findViewById(R.id.tv_popwindow_cancel);
            mOkTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccountManager.logout(mActivity);
                    mBottomPopwindow.dismiss();
//                    startActivity(new Intent(mActivity,LoginActivity.class));
//                    mActivity.finish();
                    EventBus.getDefault().post(new UpdateLoginStateEvent());

                }
            });
            mCancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomPopwindow.dismiss();
                }
            });
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mBottomPopwindow != null && mBottomPopwindow.isShowing()) {
                        mBottomPopwindow.dismiss();
                    }
                    return false;
                }
            });
            mBottomPopwindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

            mBottomPopwindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
        }
    }

    /**
     * 更换头像弹窗
     */
    private void changeUserHeadPopwindow() {
        List<String> list=new ArrayList<>();
        list.add("拍照");
        list.add("从相册选");
        list.add("取消");
        StyledDialog.buildIosSingleChoose(list, new MyItemDialogListener() {
            @Override
            public void onItemClick(CharSequence charSequence, int i) {
                if("拍照".equals(charSequence)){
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {//用户已拒绝过一次
                                //提示用户如果想要正常使用，要手动去设置中授权。
                                ToastUtil.showShort(mActivity, getString(R.string.prompt_open_read_write_permission));
                            } else {
                                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_CODE_TAKE_PHOTO);
                            }
                        } else if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)) {//用户已拒绝过一次
                                //提示用户如果想要正常使用，要手动去设置中授权。
                                ToastUtil.showShort(mActivity, getString(R.string.prompt_open_camera_permission));
                            } else {
                                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_CODE_TAKE_PHOTO);
                            }
                        } else {
                            openCamera();
                        }
                    }else{

                        openCamera();
                    }
                }
                if("从相册选".equals(charSequence)){
                    Intent intent = new Intent(mActivity, ImageGridActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_IMAGE);
//                    mBottomPopwindow.dismiss();
                }
                if("取消".equals(charSequence)){

                }

            }
        }).show();



//        if (mBottomPopwindow != null && mBottomPopwindow.isShowing()) {
//            mBottomPopwindow.dismiss();
//        } else {
//            View view = LayoutInflater.from(mActivity).inflate(R.layout.popwindow_select_picture, null, false);
//            TextView mPhotoTv = (TextView) view.findViewById(R.id.tv_popwindow_select_picture_take_photo);
//            TextView mAlbumTv = (TextView) view.findViewById(R.id.tv_popwindow_select_picture_album);
//            TextView mCancelTv = (TextView) view.findViewById(R.id.tv_popwindow_select_picture_cancel);
//            mPhotoTv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mBottomPopwindow.dismiss();
//                    if (Build.VERSION.SDK_INT >= 23) {
//                        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {//用户已拒绝过一次
//                                //提示用户如果想要正常使用，要手动去设置中授权。
//                                ToastUtil.showShort(mActivity, getString(R.string.prompt_open_read_write_permission));
//                            } else {
//                                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_CODE_TAKE_PHOTO);
//                            }
//                        } else if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)) {//用户已拒绝过一次
//                                //提示用户如果想要正常使用，要手动去设置中授权。
//                                ToastUtil.showShort(mActivity, getString(R.string.prompt_open_camera_permission));
//                            } else {
//                                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_CODE_TAKE_PHOTO);
//                            }
//                        } else {
//                            openCamera();
//                        }
//                    }
//                }
//            });
//
//            mAlbumTv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(mActivity, ImageGridActivity.class);
//                    startActivityForResult(intent, REQUEST_CODE_IMAGE);
//                    mBottomPopwindow.dismiss();
//                }
//            });
//            mCancelTv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mBottomPopwindow.dismiss();
//                }
//            });
//            view.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (mBottomPopwindow != null && mBottomPopwindow.isShowing()) {
//                        mBottomPopwindow.dismiss();
//                    }
//                    return false;
//                }
//            });
//            mBottomPopwindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
//
//            mBottomPopwindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
//        }
    }

    /**
     * 调用系统照相机拍照
     */
    private void openCamera() {
        System.out.println("打开相机");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "openCamera 的photoFile=" + photoFile.toString());
        Uri photoURI = FileProvider.getUriForFile(mActivity, mActivity.getApplicationInfo().packageName + ".fileprovider", photoFile);
        LogUtil.e(TAG, "openCamera=" + photoURI.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * 创建拍照文件夹
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String imageFileName = "photo";
        //.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, AccountManager.sUserBean.getId() + ".jpg");
        if (!image.exists()) {
            image.createNewFile();
        }
        mPhotoPath = image.getAbsolutePath();
        File crop = new File(storageDir, AccountManager.sUserBean.getId() + "_crop" + ".jpg");

        if (!crop.exists()) {
            crop.createNewFile();
        }
        mCropPath = crop.getAbsolutePath();
        return image;
    }

    /**
     * 裁剪拍摄的照片
     *
     * @param photoPath
     */
    public void cutPhoto(String photoPath) {
        File photoFile = new File(photoPath);
        File cropFile = new File(mCropPath);
        if (!cropFile.getParentFile().exists()) {
            cropFile.getParentFile().mkdirs();
        }
        if (!cropFile.exists()) {
            try {
                cropFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(FileProvider.getUriForFile(mActivity, mActivity.getApplicationInfo().packageName + ".fileprovider", photoFile), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropFile));
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG);
        intent.putExtra("outputX", 720);
        intent.putExtra("outputY", 720);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST_CODE_IMAGE) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                DialogUtil.showProgress(mActivity, getString(R.string.prompt_open_modify_head));
                updateAvatar(images.get(0).path);
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA) {
            LogUtil.e(TAG, mPhotoPath);
            cutPhoto(mPhotoPath);
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CROP) {
            DialogUtil.showProgress(mActivity, getString(R.string.prompt_open_modify_head));
            updateAvatar(mCropPath);
        }
    }

    /**
     * 上传头像
     *
     * @param avatarPath
     */
    private void updateAvatar(final String avatarPath) {
        Bitmap bitmap = null;
        bitmap = ImageUtil.decodeSampledBitmapByPath(avatarPath, 400, 400);
        final String base64 = ImageUtil.bitmapToBase64String(bitmap);

        Map<String, String> map = new HashMap<>();
        map.put("customer_id", AccountManager.sUserBean.getId());
        map.put("customer_img", base64);
        LogUtil.e(TAG,map.toString());
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).updateHead(RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e(TAG, response);
                DialogUtil.hideProgress();
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("info");
                    if (code == 0) {
                        JSONObject data = res.getJSONObject("data");
                        AccountManager.sUserBean = new UserBean();
                        AccountManager.sUserBean.setId(data.getString("id"));
                        AccountManager.sUserBean.setTelNumber(data.getString("phone"));
                        AccountManager.sUserBean.setNickName(data.getString("nicename"));
                        AccountManager.sUserBean.setHeadPortrait(data.getString("avatar"));
                        String base64 = CommonUtil.objectToBase64(AccountManager.sUserBean);
                        SPUtil.put(Constant.USER, base64);
                        setUserInfo();
                    } else {
                        ToastUtil.showShort(mActivity, info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                DialogUtil.hideProgress();
                if (!NetworkUtil.isConnected()) {
                    ToastUtil.showShort(mActivity, R.string.not_net);
                } else {
                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(mFragment);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AccountManager.sUserBean == null) {
            getUserInfo();
        } else {
            setUserInfo();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateLoginStateEvent(UpdateLoginStateEvent event) {
        initUserInfo();
    }
}