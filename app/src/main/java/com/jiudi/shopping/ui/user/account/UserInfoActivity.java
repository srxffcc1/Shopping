package com.jiudi.shopping.ui.user.account;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.AddressBean;
import com.jiudi.shopping.bean.TypeBean;
import com.jiudi.shopping.event.UpdateUserInfoEvent;
import com.jiudi.shopping.global.Constant;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.CommonUtil;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.ImageUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.widget.GlideImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfoActivity extends BaseActivity {
    private static final String TAG = "UserInfoActivity";

    private LinearLayout mBackLL;
    private TextView mTitleTV;
    private TextView mOperateTV;

    private LinearLayout mNicknameLL;
    private EditText mNicknameET;

    private LinearLayout mSexLL;
    private TextView mSexTV;
    private PopupWindow mSexPopwindow;
    private RecyclerView mSexRV;
    private RecyclerCommonAdapter<TypeBean> mSexAdapter;
    private List<TypeBean> mSexList = new ArrayList<>();
    private TypeBean mTempSex;
    private TypeBean mSelectedSex;

    private LinearLayout mAvatarLL;
    private ImageView mAvatarIV;

    private TextView mUserIntroduceTV;
    private TextView mGoodAtTV;
    private LinearLayout mUserIntroduceLL;
    private LinearLayout mUserGoodAtLL;

    private PopupWindow mSelectPopwindow;
    private String mPhotoPath;
    private String mCropPath;

    public static final int REQUEST_CODE_IMAGE = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    public static final int REQUEST_CODE_CROP = 3;
    public static final int REQUEST_CODE_USER_INTRODUCE = 4;


    public static final int PERMISSION_CODE_TAKE_PHOTO = 100;

    public static final int REQUEST_CODE_TYPE = 21;
    private LinearLayout mAreaLL;
    private TextView mAreaTV;
    private OptionsPickerView<String> mAddressPickerView;

    private List<String> mProvinceNameList = new ArrayList<>();
    private List<List<String>> mCityNameList = new ArrayList<>();
    private List<List<List<String>>> mAreaNameList = new ArrayList<>();
    private AddressBean mAddressBean;
    private AddressBean.Province mSelectProvince;
    private AddressBean.City mSelectCity;
    private AddressBean.Area mSelectArea;
    private String introduction="";
    private String mTypeId="";
    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initView() {
        mBackLL = byId(R.id.ll_layout_top_back_bar_back);
        mTitleTV = byId(R.id.tv_layout_top_back_bar_title);
        mTitleTV.setText("个人资料");
        mOperateTV=byId(R.id.tv_layout_back_top_bar_operate);
        mOperateTV.setText("保存");
        mOperateTV.setVisibility(View.VISIBLE);

        mNicknameLL = byId(R.id.ll_activity_user_info_nickname);
        mNicknameET = byId(R.id.tv_activity_user_info_nickname);

        mSexLL = byId(R.id.ll_activity_user_info_sex);
        mSexTV = byId(R.id.tv_activity_user_info_sex);

        mAvatarLL = byId(R.id.ll_activity_user_info_avatar);
        mAvatarIV = byId(R.id.iv_activity_user_info_avatar);

        mAreaLL=byId(R.id.ll_activity_user_info_address);
        mAreaTV=byId(R.id.tv_activity_user_info_address);

        mUserIntroduceLL = byId(R.id.ll_activity_user_info_introduce);
        mUserGoodAtLL = byId(R.id.ll_activity_user_info_good_at);
        mUserIntroduceTV = byId(R.id.tv_activity_user_info_introduce);
        mGoodAtTV = byId(R.id.tv_activity_user_info_good_at);
    }

    @Override
    public void initData() {
        //获取区域数据,接口数据
//        getAreaData();

        initUserInfo();
        //初始化性别数据

        String[] sexArray = getResources().getStringArray(R.array.sex_category);
        for (int i = 0; i < sexArray.length; i++) {
            TypeBean typeBean = new TypeBean();
            typeBean.setName(sexArray[i]);
            typeBean.setId(i + 1 + "");
            if (typeBean.getId().equals("3")){
                typeBean.setId("0");
            }
            mSexList.add(typeBean);
        }
        initImagePicker();
//        getUserInfo();
    }

    @Override
    public void initEvent() {
        mOperateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectProvince==null){
                    ToastUtil.showShort(mActivity,getString(R.string.select_from_place));
                }else {
                    saveInfo();
                }

            }
        });
        mAvatarLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectPopwindow();
            }
        });

        mSexLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSexPopupWindow();
            }
        });


        mBackLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAreaLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressPickerView();
            }
        });
        mUserIntroduceLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        mUserGoodAtLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000); //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000); //保存文件的高度。单位像素
    }
    private void initUserInfo() {

        //头像
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
//        Glide.with(mActivity).load(AccountManager.sUserBean.getHeadPortrait()).apply(requestOptions)
//                .load(R.drawable.head_defuat_circle)
//                .into(mAvatarIV);

        //昵称
//        mNicknameET.setText(AccountManager.sUserBean.getNickName());
        //个人简介
//        if(AccountManager.sUserBean.getUserIntroduce().equals("")){
//            mUserIntroduceTV.setText(getString(R.string.not_input));
//        }else {
//            mUserIntroduceTV.setText(getString(R.string.already_input));
//        }

        //擅长领域
//        if(AccountManager.sUserBean.getUserGoodAt().equals("")){
//            mGoodAtTV.setText(getString(R.string.not_choose));
//        }else {
//            mGoodAtTV.setText(getString(R.string.already_choose));
//        }

        //性别
//        switch (AccountManager.sUserBean.getSex()) {
//            case "1":
//                mTempSex = new TypeBean();
//                mTempSex.setId(1 + "");
//                mTempSex.setName("女");
//                mSelectedSex = mTempSex;
//                break;
//            case "2":
//                mTempSex = new TypeBean();
//                mTempSex.setId(2 + "");
//                mTempSex.setName("男");
//                mSelectedSex = mTempSex;
//                break;
//        }
        if (mSelectedSex != null) {
            mSexTV.setText(mSelectedSex.getName());
        }


    }

    private void showSelectPopwindow() {
        if (mSelectPopwindow != null && mSelectPopwindow.isShowing()) {
            mSelectPopwindow.dismiss();
        } else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.popwindow_select_picture, null);
            TextView takePhotoTV = (TextView) view.findViewById(R.id.tv_popwindow_select_picture_take_photo);
            TextView albumTV = (TextView) view.findViewById(R.id.tv_popwindow_select_picture_album);
            TextView cancelTV = (TextView) view.findViewById(R.id.tv_popwindow_select_picture_cancel);
            takePhotoTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mSelectPopwindow.dismiss();

                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(mContext,
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,
                                    Manifest.permission.READ_EXTERNAL_STORAGE)) {//用户已拒绝过一次
                                //提示用户如果想要正常使用，要手动去设置中授权。
                                ToastUtil.showShort(mActivity, "请到设置-应用管理中开启此应用的读写权限");
                            } else {
                                ActivityCompat.requestPermissions((Activity) mContext,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                        PERMISSION_CODE_TAKE_PHOTO);
                            }
                        } else if (ContextCompat.checkSelfPermission(mContext,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,
                                    Manifest.permission.CAMERA)) {//用户已拒绝过一次
                                //提示用户如果想要正常使用，要手动去设置中授权。
                                ToastUtil.showShort(mActivity, "请到设置-应用管理中开启此应用的相机权限");
                            } else {
                                ActivityCompat.requestPermissions((Activity) mContext,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.CAMERA},
                                        PERMISSION_CODE_TAKE_PHOTO);
                            }
                        } else {
                            Log.e(TAG, "调用系统照相机拍照");
                            //调用系统照相机拍照
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            Uri photoURI = FileProvider.getUriForFile(UserInfoActivity.this, "com.nado.lotteryticket.fileprovider", photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            UserInfoActivity.this.startActivityForResult(intent, REQUEST_CODE_CAMERA);
                        }
                    } else {
                        //调用系统照相机拍照
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Uri photoURI = FileProvider.getUriForFile(UserInfoActivity.this, "com.nado.lotteryticket.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        UserInfoActivity.this.startActivityForResult(intent, REQUEST_CODE_CAMERA);
                    }
                }
            });
            albumTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, ImageGridActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_IMAGE);
                    mSelectPopwindow.dismiss();
                }
            });
            cancelTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectPopwindow.dismiss();
                }
            });
            mSelectPopwindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mSelectPopwindow != null && mSelectPopwindow.isShowing()) {
                        mSelectPopwindow.dismiss();
                    }
                    return false;
                }
            });
            mSelectPopwindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
        }
    }
    private void showSexPopupWindow() {

        if (mSexPopwindow != null && mSexPopwindow.isShowing()) {
            mSexPopwindow.dismiss();
        } else if (mSexPopwindow != null) {
            mSexPopwindow.showAtLocation(mSexTV, Gravity.NO_GRAVITY, 0, 0);
        } else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.popwindow_option, null);
            TextView cancelTV = (TextView) view.findViewById(R.id.tv_popwindow_option_cancel);
            TextView confirmTV = (TextView) view.findViewById(R.id.tv_popwindow_option_confirm);
            cancelTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSexPopwindow.dismiss();
                }
            });
            confirmTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTempSex != null) {
                        mSexPopwindow.dismiss();
//                        DialogUtil.showProgress(mActivity, "正在修改性别");
//                        updateSex(mTempSex);
                        mSelectedSex = mTempSex;
                        mSexTV.setText(mSelectedSex.getName());
                    }
                }
            });
            mSexRV = (RecyclerView) view.findViewById(R.id.rv_popwindow_option);
            showSexRecycleView();
            mSexPopwindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mSexPopwindow.showAtLocation(mSexTV, Gravity.NO_GRAVITY, 0, 0);
        }
    }

    private void showSexRecycleView() {

        if (mSexAdapter == null) {
            mSexAdapter = new RecyclerCommonAdapter<TypeBean>(mActivity, R.layout.item_option, mSexList) {
                @Override
                protected void convert(ViewHolder holder, final TypeBean typeBean, int position) {
                    TextView optionTV = holder.getView(R.id.tv_item_option);
                    ImageView selectIV = holder.getView(R.id.iv_item_option_select);
                    optionTV.setText(typeBean.getName());
                    if (mTempSex != null) {
                        if (typeBean.getName().equals(mTempSex.getName())) {
                            optionTV.setTextColor(ContextCompat.getColor(mActivity, R.color.colorBlueTitle));
                            selectIV.setVisibility(View.VISIBLE);
                        } else {
                            optionTV.setTextColor(ContextCompat.getColor(mActivity, R.color.colorFontDark));
                            selectIV.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        optionTV.setTextColor(ContextCompat.getColor(mActivity, R.color.colorFontDark));
                        selectIV.setVisibility(View.INVISIBLE);
                    }
                    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTempSex = typeBean;
                            mSexAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };
            mSexRV.setAdapter(mSexAdapter);
            mSexRV.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {
            mSexAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE_TAKE_PHOTO) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToastUtil.showShort(mActivity, "请到设置-应用管理中打开应用的读写权限");
                return;
            }
            if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                ToastUtil.showShort(mActivity, "请到设置-应用管理中打开应用的相机权限");
                return;
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri photoURI = FileProvider.getUriForFile(UserInfoActivity.this, "com.nado.lotteryticket.fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            UserInfoActivity.this.startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
    }
    private void updateSex(final TypeBean sex) {
        Map<String, String> map = new HashMap<>();
        if (AccountManager.sUserBean != null) {
//            map.put("userId", AccountManager.sUserBean.getId());
        }
        map.put("nickName", mNicknameET.getText().toString().trim() );
//        map.put("userName", AccountManager.sUserBean.getTelNumber());
        LogUtil.e(TAG, map.toString());
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .changePersonalDate(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {

                    @Override
                    public void onSuccess(String s) {
                        DialogUtil.hideProgress();
                        LogUtil.e(TAG, s);
                        try {
                            JSONObject response = new JSONObject(s);
                            int code = response.getInt("code");
                            String info = response.getString("info");
                            if (code == 0) {
                                ToastUtil.showLong(mActivity, info);
                                mSelectedSex = sex;
                                mSexTV.setText(mSelectedSex.getName());
                            } else {
                                ToastUtil.showShort(mActivity, info);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showShort(mActivity, "数据异常");
                            LogUtil.e(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (!NetworkUtil.isConnected()) {
                            ToastUtil.showShort(mActivity, "网络未连接");
                        } else {
                            ToastUtil.showShort(mActivity, getString(R.string.network_error));
                        }
                    }
                });
    }

    /**
     * 获取个人信息
     */
//    private void getUserInfo() {
//        Map<String, String> map = new HashMap<>();
//        if (AccountManager.sUserBean != null) {
//            map.put("userId", AccountManager.sUserBean.getId());
//        }
//        RequestManager.mRetrofitManager
//                .createRequest(RetrofitRequestInterface.class)
//                .mine(RequestManager.encryptParams(map))
//                .enqueue(new RetrofitCallBack() {
//
//                    @Override
//                    public void onSuccess(String response) {
//                        LogUtil.e(TAG, response);
//                        DialogUtil.hideProgress();
//                        try {
//                            JSONObject res = new JSONObject(response);
//                            int code = res.getInt("code");
//                            String info = res.getString("info");
//                            if (code == 0) {
//                                JSONObject data = res.getJSONObject("data");
//                                mUserIntroduceTV.setText(data.getString("introductionInfo"));
//                                introduction=data.getString("introductionInfo");
//                                mAreaTV.setText(data.getString("address"));
//                                JSONArray strongAreaList=data.getJSONArray("strongAreaList");
//                                if (strongAreaList.length()!=0){
//                                    mGoodAtTV.setText(getString(R.string.selected));
//                                }else {
//                                    mGoodAtTV.setText(getString(R.string.no_select));
//                                }
//                                mTypeId=data.getString("strongArea");
//                                try {
//                                    mSelectProvince=new AddressBean.Province();
//                                    mSelectProvince.setId(data.getString("province"));
//                                    mSelectCity=new AddressBean.City();
//                                    mSelectCity.setId(data.getString("city"));
//                                    mSelectArea=new AddressBean.Area();
//                                    mSelectArea.setId(data.getString("area"));
//
//                                }catch (Exception e){
//
//                                }
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            ToastUtil.showShort(mActivity, getString(R.string.data_error));
//                            LogUtil.e(TAG, e.getMessage());
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        if (!NetworkUtil.isConnected()) {
//                            ToastUtil.showShort(mActivity,  getString(R.string.net_error));
//                        } else {
//                            ToastUtil.showShort(mActivity, getString(R.string.network_error));
//                        }
//                    }
//                });
//    }

    private void saveInfo() {
        Map<String, String> map = new HashMap<>();
//        if (AccountManager.sUserBean != null) {
//            map.put("userId", AccountManager.sUserBean.getId());
//        }
//        map.put("nickName", mNicknameET.getText().toString().trim() );
//        map.put("userName", AccountManager.sUserBean.getTelNumber());
//        if (mTempSex==null){
//            map.put("sex", "");
//        }else {
//            map.put("sex", mTempSex.getId());
//        }

        map.put("province",mSelectProvince.getId());
        map.put("city",mSelectCity.getId());
        map.put("area",mSelectArea.getId());
        map.put("introduction",introduction);
        map.put("strongArea",mTypeId);
        LogUtil.e(TAG, map.toString());
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .changePersonalDate(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {

                    @Override
                    public void onSuccess(String s) {
                        DialogUtil.hideProgress();
                        LogUtil.e(TAG, s);
                        try {
                            JSONObject response = new JSONObject(s);
                            int code = response.getInt("code");
                            String info = response.getString("info");
                            if (code == 0) {
                                ToastUtil.showShort(mActivity, info);
                                finish();
//                                mSelectedSex = sex;
//                                mSexTV.setText(mSelectedSex.getName());
                            } else {
                                ToastUtil.showShort(mActivity, info);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showShort(mActivity, getString(R.string.data_error));
                            LogUtil.e(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (!NetworkUtil.isConnected()) {
                            ToastUtil.showShort(mActivity,  getString(R.string.net_error));
                        } else {
                            ToastUtil.showShort(mActivity, getString(R.string.network_error));
                        }
                    }
                });
    }

    private void updateAvatar(final String avatarPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
//                try {


                    bitmap = ImageUtil.decodeSampledBitmapByPath(avatarPath,400,400);


//                            GlideApp.with(mActivity).asBitmap().load(avatarPath).into(-1, -1).get();
//                    LogUtil.e(TAG, bitmap + "");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }

                final String base64 = ImageUtil.bitmapToBase64String(bitmap);
//                LogUtil.e(TAG, base64);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> map = new HashMap<>();
//                        map.put("customer_id ", AccountManager.sUserBean.getId());
                        map.put("customer_img", base64);
                        LogUtil.e(TAG, map.toString());
                        RequestManager.mRetrofitManager
                                .createRequest(RetrofitRequestInterface.class)
                                .updateHead(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map))
                                .enqueue(new RetrofitCallBack() {

                                    @Override
                                    public void onSuccess(String response) {
                                        DialogUtil.hideProgress();
                                        LogUtil.e(TAG, response);
                                        try {
                                            JSONObject res = new JSONObject(response);
                                            int code = res.getInt("code");
                                            String info = res.getString("info");
                                            if (code == 0) {
                                                ToastUtil.showLong(mActivity, info);
//                                                AccountManager.sUserBean.setHeadPortrait(avatarPath);
                                                String base64 = CommonUtil.objectToBase64(AccountManager.sUserBean);
                                                SPUtil.put(Constant.USER, base64);
                                                RequestOptions requestOptions = RequestOptions.circleCropTransform();
                                                Glide.with(mActivity).load(avatarPath).apply(requestOptions)
                                                        .into(mAvatarIV);


//                                                Glide.with(mActivity)
//                                                        .load(avatarPath)
//                                                        .bitmapTransform(new CropCircleTransformation(mActivity))
//                                                        .into(mAvatarIV);
                                                EventBus.getDefault().post(new UpdateUserInfoEvent());
                                            } else {
                                                ToastUtil.showShort(mActivity, info);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            ToastUtil.showShort(mActivity, getString(R.string.data_error));
                                            LogUtil.e(TAG, e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable t) {
                                        if (!NetworkUtil.isConnected()) {
                                            ToastUtil.showShort(mActivity,  getString(R.string.net_error));
                                        } else {
                                            ToastUtil.showShort(mActivity, getString(R.string.network_error));
                                        }
                                    }
                                });

                    }
                });
            }
        }).start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        }else if (requestCode == REQUEST_CODE_USER_INTRODUCE) {
            introduction=data.getStringExtra("userIntroduce");
            if(data.getStringExtra("userIntroduce").equals("")){
                mUserIntroduceTV.setText(getString(R.string.not_input));
            }else {
                mUserIntroduceTV.setText(getString(R.string.already_input));

            }

        }
        if (requestCode == REQUEST_CODE_TYPE&&resultCode==RESULT_OK) {
            mGoodAtTV.setText(data.getStringExtra("name"));
            mTypeId=data.getStringExtra("id");
        }
    }
    private File createImageFile() throws IOException {
        String imageFileName = "photo";
        //.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = new File(storageDir, AccountManager.sUserBean.getId() + ".jpg");
//        if (!image.exists()) {
//            image.createNewFile();
//        }
//        mPhotoPath = image.getAbsolutePath();
//        File crop = new File(storageDir, AccountManager.sUserBean.getId() + "_crop" + ".jpg");
//
//        if (!crop.exists()) {
//            crop.createNewFile();
//        }
//        mCropPath = crop.getAbsolutePath();
        return null;
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


    private void getAreaData() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager
                .createRequest(RetrofitRequestInterface.class)
                .showAllChina(RequestManager.encryptParams(map))
                .enqueue(new RetrofitCallBack() {

                    @Override
                    public void onSuccess(String s) {
                        DialogUtil.hideProgress();
                        try {
                            JSONObject response = new JSONObject(s);
                            int code = response.getInt("code");
                            String info = response.getString("info");
                            if (code == 0) {

                                JSONObject data=response.getJSONObject("data");

                                JSONArray provinceArray = data.getJSONArray("proList");
                                List<AddressBean.Province> provinceList = new ArrayList<>();
                                for (int i = 0; i < provinceArray.length(); i++) {
                                    JSONObject provinceItem = provinceArray.getJSONObject(i);
                                    AddressBean.Province province = new AddressBean.Province();
                                    province.setId(provinceItem.getString("id"));
                                    province.setName(provinceItem.getString("name"));

                                    JSONArray cityArray = provinceItem.getJSONArray("city");
                                    List<AddressBean.City> cityList = new ArrayList<>();
                                    List<String> secondCityNameList = new ArrayList<>();
                                    List<List<String>> secondAreaNameList = new ArrayList<>();
                                    for (int j = 0; j < cityArray.length(); j++) {
                                        JSONObject cityItem = cityArray.getJSONObject(j);
                                        AddressBean.City city = new AddressBean.City();
                                        city.setId(cityItem.getString("id"));
                                        city.setName(cityItem.getString("name"));

                                        JSONArray areaArray = cityItem.getJSONArray("area");
                                        List<AddressBean.Area> areaList = new ArrayList<>();
                                        List<String> thirdAreaNameList = new ArrayList<>();
                                        for (int k = 0; k < areaArray.length(); k++) {
                                            JSONObject areaItem = areaArray.getJSONObject(k);
                                            AddressBean.Area area = new AddressBean.Area();
                                            area.setId(areaItem.getString("id"));
                                            area.setName(areaItem.getString("name"));
                                            areaList.add(area);
                                            thirdAreaNameList.add(area.getName());
                                        }
                                        city.setAreaList(areaList);
                                        cityList.add(city);
                                        secondCityNameList.add(city.getName());
                                        secondAreaNameList.add(thirdAreaNameList);
                                    }
                                    province.setCityList(cityList);
                                    provinceList.add(province);
                                    mProvinceNameList.add(province.getName());
                                    mCityNameList.add(secondCityNameList);
                                    mAreaNameList.add(secondAreaNameList);
                                }
                                mAddressBean = new AddressBean();
                                mAddressBean.setProvinceList(provinceList);
                            } else {
                                ToastUtil.showShort(mActivity, info);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showShort(mActivity, getString(R.string.data_error));
                            LogUtil.e(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (!NetworkUtil.isConnected()) {
                            ToastUtil.showShort(mActivity, getString(R.string.net_error));
                        } else {
                            ToastUtil.showShort(mActivity, getString(R.string.network_error));
                        }
                    }
                });
    }
    private void showAddressPickerView() {
        if (mAddressPickerView == null) {
            mAddressPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                    //返回的分别是三个级别的选中位置

                    mSelectProvince = mAddressBean.getProvinceList().get(options1);

                    mSelectCity = mAddressBean
                            .getProvinceList()
                            .get(options1)
                            .getCityList()
                            .get(option2);

                    mSelectArea = mAddressBean
                            .getProvinceList()
                            .get(options1)
                            .getCityList()
                            .get(option2)
                            .getAreaList()
                            .get(options3);

                    String tx = mProvinceNameList.get(options1)
                            + mCityNameList.get(options1).get(option2)
                            + mAreaNameList.get(options1).get(option2).get(options3);
                    mAreaTV.setText(tx);
                }
            }).setCancelColor(ContextCompat.getColor(mActivity, R.color.colorFontDark))
                    .setSubmitColor(ContextCompat.getColor(mActivity, R.color.colorBlue))
                    .setTextColorCenter(ContextCompat.getColor(mActivity, R.color.colorBlue))
                    .build();
            mAddressPickerView.setPicker(mProvinceNameList, mCityNameList, mAreaNameList);
            mAddressPickerView.show();
        } else {
            mAddressPickerView.show();
        }
    }
}
