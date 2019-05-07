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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.UserBean;
import com.jiudi.shopping.event.FlashEvent;
import com.jiudi.shopping.global.Constant;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.user.AddressListActivity;
import com.jiudi.shopping.ui.user.AllOrderActivity;
import com.jiudi.shopping.ui.user.account.LoginActivity;
import com.jiudi.shopping.util.CommonUtil;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.HttpUrlConnectUtil;
import com.jiudi.shopping.util.ImageUtil;
import com.jiudi.shopping.util.LogUtil;
import com.jiudi.shopping.util.NetworkUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.widget.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Steven on 2017/12/21.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {


    private android.widget.ImageView head;
    private android.widget.TextView xingming;
    private android.widget.TextView zhuce;
    private android.widget.ImageView passtongzhi;
    private android.widget.LinearLayout mylessmoney;
    private android.widget.TextView mylessmoneyvalue;
    private android.widget.LinearLayout myquan;
    private android.widget.TextView myquanvalue;
    private android.widget.LinearLayout allOrder;
    private android.widget.TextView myallorder;
    private android.widget.TextView daifukuan;
    private android.widget.TextView daifahuo;
    private android.widget.TextView daishouhuo;
    private android.widget.TextView yiwancheng;
    private android.widget.LinearLayout mycollet;
    private android.widget.LinearLayout myaddress;
    private android.widget.LinearLayout mymoney;
    private android.widget.LinearLayout myunder;
    private android.widget.LinearLayout myup;
    private android.widget.LinearLayout myzxing;
    private android.widget.LinearLayout mysetting;

    private String mPhotoPath;
    private String mCropPath;
    public static final int REQUEST_CODE_IMAGE = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    public static final int REQUEST_CODE_CROP = 3;
    public static final int PERMISSION_CODE_TAKE_PHOTO = 100;

    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_mine;
    }


    @Override
    public void onClick(View v) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void initView() {


        head = (ImageView) findViewById(R.id.head);
        xingming = (TextView) findViewById(R.id.xingming);
        zhuce = (TextView) findViewById(R.id.zhuce);
        passtongzhi = (ImageView) findViewById(R.id.passtongzhi);
        mylessmoney = (LinearLayout) findViewById(R.id.mylessmoney);
        mylessmoneyvalue = (TextView) findViewById(R.id.mylessmoneyvalue);
        myquan = (LinearLayout) findViewById(R.id.myquan);
        myquanvalue = (TextView) findViewById(R.id.myquanvalue);
        allOrder = (LinearLayout) findViewById(R.id.all_order);
        myallorder = (TextView) findViewById(R.id.myallorder);
        daifukuan = (TextView) findViewById(R.id.daifukuan);
        daifahuo = (TextView) findViewById(R.id.daifahuo);
        daishouhuo = (TextView) findViewById(R.id.daishouhuo);
        yiwancheng = (TextView) findViewById(R.id.yiwancheng);
        mycollet = (LinearLayout) findViewById(R.id.mycollet);
        myaddress = (LinearLayout) findViewById(R.id.myaddress);
        mymoney = (LinearLayout) findViewById(R.id.mymoney);
        myunder = (LinearLayout) findViewById(R.id.myunder);
        myup = (LinearLayout) findViewById(R.id.myup);
        myzxing = (LinearLayout) findViewById(R.id.myzxing);
        mysetting = (LinearLayout) findViewById(R.id.mysetting);
    }

    @Override
    public void initData() {
        StyledDialog.init(mActivity);
        EventBus.getDefault().register(this);
        getMineData();
    }

    private void getMineData() {
        Map<String, String> map = new HashMap<>();
        RequestManager.mRetrofitManager3.createRequest(RetrofitRequestInterface.class).getPersonalDate(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        JSONObject jsonObject=res.getJSONObject("data").optJSONObject("user_info");
                        if(jsonObject!=null){
                            UserBean bean=new UserBean();
                            bean.uid=jsonObject.optString("uid");
                            bean.account=jsonObject.optString("account");
                            bean.pwd=jsonObject.optString("pwd");
                            bean.nickname=jsonObject.optString("nickname");
                            bean.avatar=jsonObject.optString("avatar");
                            bean.phone=jsonObject.optString("phone");
                            bean.add_time=jsonObject.optString("add_time");
                            bean.add_ip=jsonObject.optString("add_ip");
                            bean.last_time=jsonObject.optString("last_time");
                            bean.last_ip=jsonObject.optString("last_ip");
                            bean.now_money=jsonObject.optString("now_money");
                            bean.integral=jsonObject.optString("integral");
                            bean.status=jsonObject.optString("status");
                            bean.level=jsonObject.optString("level");
                            bean.spread_uid=jsonObject.optString("spread_uid");
                            bean.agent_id=jsonObject.optString("agent_id");
                            bean.user_type=jsonObject.optString("user_type");
                            bean.is_promoter=jsonObject.optString("is_promoter");
                            bean.pay_count=jsonObject.optString("pay_count");
                            bean.direct_num=jsonObject.optString("direct_num");
                            bean.team_num=jsonObject.optString("team_num");
                            bean.is_reward=jsonObject.optString("is_reward");
                            bean.allowance_number=jsonObject.optString("allowance_number");
                            try {
                                JSONArray array=res.getJSONObject("data").getJSONArray("coupon_num");
                                bean.coupon_num=array.length()+"";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            AccountManager.sUserBean=bean;
                            bindDataToView();

                        }else{

                        }
                    }else{

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void bindDataToView() {
        xingming.setText(AccountManager.sUserBean.nickname);
        long longs =Long.parseLong(AccountManager.sUserBean.add_time)*1000L;
        Date date=new Date();
        date.setTime(longs);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        zhuce.setText("注册时间 "+simpleDateFormat.format(date));
        mylessmoneyvalue.setText(AccountManager.sUserBean.integral);
        myquanvalue.setText(AccountManager.sUserBean.coupon_num);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFlashEvent(FlashEvent wechatPayEvent) {
        getMineData();
    }
    @Override
    public void initEvent() {
        myaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, AddressListActivity.class));
            }
        });
        allOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AllOrderActivity.class));
            }
        });
        daifukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("type",1));
            }
        });
        daifahuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("type",2));
            }
        });
        daishouhuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("type",3));
            }
        });
        yiwancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("type",4));
            }
        });
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserHeadPopwindow();
            }
        });
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
        Uri photoURI = FileProvider.getUriForFile(mActivity, mActivity.getApplicationInfo().packageName + ".fileprovider", photoFile);
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
        File image = new File(storageDir, AccountManager.sUserBean.uid + ".jpg");
        if (!image.exists()) {
            image.createNewFile();
        }
        mPhotoPath = image.getAbsolutePath();
        File crop = new File(storageDir, AccountManager.sUserBean.uid + "_crop" + ".jpg");

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file=new File(avatarPath);
                Map<String, Object> paramMap=new HashMap<>();
                paramMap.put("file",file);
                try {
                    String result=HttpUrlConnectUtil.uploadFile(RequestManager.mBaseUrl+"api/images/upload.html",paramMap,"");
                    System.out.println(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DialogUtil.hideProgress();
            }
        }).start();

//        Bitmap bitmap = null;
//        bitmap = ImageUtil.decodeSampledBitmapByPath(avatarPath, 400, 400);
//        final String base64 = ImageUtil.bitmapToString(bitmap);
//        Map<String, String> map = new HashMap<>();
//        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).updateHead(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
//            @Override
//            public void onSuccess(String response) {
//                DialogUtil.hideProgress();
//                try {
//                    JSONObject res = new JSONObject(response);
//                    int code = res.getInt("code");
//                    String info = res.getString("msg");
//                    if (code == 200) {
//
//                    } else {
//                        ToastUtil.showShort(mActivity, info);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                DialogUtil.hideProgress();
//                if (!NetworkUtil.isConnected()) {
//                    ToastUtil.showShort(mActivity, R.string.not_net);
//                } else {
//                    ToastUtil.showShort(mActivity, getString(R.string.net_error));
//                }
//            }
//        });
    }

}