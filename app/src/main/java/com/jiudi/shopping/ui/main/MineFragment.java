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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.UserBean;
import com.jiudi.shopping.event.FlashEvent;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.fenxiao.FenXiaoMenuActivity;
import com.jiudi.shopping.ui.fenxiao.FenXiaoNoActivity;
import com.jiudi.shopping.ui.fenxiao.TuanDuiActivity;
import com.jiudi.shopping.ui.fenxiao.TuiGuangActivity;
import com.jiudi.shopping.ui.user.AddressListActivity;
import com.jiudi.shopping.ui.user.AllOrderActivity;
import com.jiudi.shopping.ui.user.AllQuanActivity;
import com.jiudi.shopping.ui.user.ShopSettingActivity;
import com.jiudi.shopping.ui.user.account.AccountActivity;
import com.jiudi.shopping.ui.user.account.FenXiaoAccountActivity;
import com.jiudi.shopping.ui.user.account.LoginActivity;
import com.jiudi.shopping.ui.user.account.ShouCangActivity;
import com.jiudi.shopping.ui.user.account.TongZhiActivity;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.HttpUrlConnectUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.widget.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.m7.imkfsdk.KfStartHelper;
import com.umeng.analytics.MobclickAgent;

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

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

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
    private Badge noBuyb;
    private Badge noPostageb;
    private Badge noTakeb;
    private Badge noReplyb;
    private LinearLayout mykefu;
    private static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 1001;
    private static final int REQUEST_CODE_UNKNOWN_APP = 100;
    private static final int REQUEST_CODE_OPENCHAT = 60;
    private KfStartHelper helper;
    private TextView dianzhu;
    private TextView zhuce2;
    private TextView dianzhu2;

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
        mykefu = (LinearLayout) findViewById(R.id.mykefu);
        dianzhu = (TextView) findViewById(R.id.dianzhu);
        zhuce2 = (TextView) findViewById(R.id.zhuce2);
        dianzhu2 = (TextView) findViewById(R.id.dianzhu2);
    }

    @Override
    public void initData() {

        helper = new KfStartHelper(mActivity);
        EventBus.getDefault().register(this);
        getMineData();
        if("1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))){
//            startActivity(new Intent(mActivity, FenXiaoMenuActivity.class));
            dianzhu.setText("店主权益");
        }else{
//            startActivity(new Intent(mActivity, FenXiaoNoActivity.class));
            dianzhu.setText("我要开店");
        }
    }

    private void getMineData() {
        if("".equals(SPUtil.get("head", "").toString())){
            AccountManager.sUserBean=null;
        }else{
            Map<String, String> map = new HashMap<>();
            RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getPersonalDate(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
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
                                try {
                                    JSONObject orderStatusNum=res.getJSONObject("data").getJSONObject("orderStatusNum");
                                    bean.noBuy=orderStatusNum.optInt("noBuy");
                                    bean.noPostage=orderStatusNum.optInt("noPostage");
                                    bean.noTake=orderStatusNum.optInt("noTake");
                                    bean.noReply=orderStatusNum.optInt("noReply");
                                    bean.noPink=orderStatusNum.optInt("noPink");
                                    bean.noBuy=orderStatusNum.optInt("noBuy");
                                    bean.noPostage=orderStatusNum.optInt("noPostage");
                                    bean.noTake=orderStatusNum.optInt("noTake");
                                    bean.noReply=orderStatusNum.optInt("noReply");
                                    bean.noPink=orderStatusNum.optInt("noPink");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                AccountManager.sUserBean=bean;
                                bindDataToView();

                            }else{
                                AccountManager.sUserBean=null;
                            }
                        }else{
                            AccountManager.sUserBean=null;
                        }

                    } catch (JSONException e) {
                        AccountManager.sUserBean=null;
                        e.printStackTrace();
                        Toast.makeText(mActivity,"获取用户数据失败请联系管理员",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {

                    Toast.makeText(mActivity,"获取用户数据失败请联系管理员",Toast.LENGTH_SHORT).show();
                    AccountManager.sUserBean=null;
                }
            });
        }

    }

    private void bindDataToView() {
        String shenfen="";
        if("1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))){
            if("2".equals(AccountManager.sUserBean.agent_id)){
                shenfen="钻石店主";
                zhuce2.setVisibility(View.VISIBLE);
            }else if("1".equals(AccountManager.sUserBean.agent_id)){

                shenfen="普通店主";
                zhuce2.setVisibility(View.VISIBLE);
            }else{
                shenfen="普通用户";
                zhuce2.setVisibility(View.GONE);
            }
        }else{
            shenfen="普通用户";
            zhuce2.setVisibility(View.GONE);
        }

        xingming.setText(shenfen);
        zhuce.setText(AccountManager.sUserBean.nickname);
        zhuce2.setText("邀请码:"+AccountManager.sUserBean.uid);
        long longs =Long.parseLong(AccountManager.sUserBean.add_time)*1000L;
        Date date=new Date();
        date.setTime(longs);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
//        zhuce.setText("注册时间 "+simpleDateFormat.format(date));
        if("1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))){
//            startActivity(new Intent(mActivity, FenXiaoMenuActivity.class));
            mylessmoneyvalue.setText(AccountManager.sUserBean.integral);
        }else{
//            startActivity(new Intent(mActivity, FenXiaoNoActivity.class));
            mylessmoneyvalue.setText("加入即享");
        }
        myquanvalue.setText(AccountManager.sUserBean.coupon_num);
        RequestOptions requestOptions = RequestOptions.circleCropTransform().error(R.drawable.head_defuat_circle);
        Glide.with(mActivity).load((AccountManager.sUserBean.avatar.startsWith("http"))?AccountManager.sUserBean.avatar:"http://"+AccountManager.sUserBean.avatar).apply(requestOptions).into(head);

        noBuyb = new QBadgeView(mActivity).setBadgeGravity(Gravity.END | Gravity.TOP).bindTarget(daifukuan).setBadgeText(AccountManager.sUserBean.noBuy+"");
        noPostageb = new QBadgeView(mActivity).setBadgeGravity(Gravity.END | Gravity.TOP).bindTarget(daifahuo).setBadgeText(AccountManager.sUserBean.noPostage+"");
        noTakeb = new QBadgeView(mActivity).setBadgeGravity(Gravity.END | Gravity.TOP).bindTarget(daishouhuo).setBadgeText(AccountManager.sUserBean.noTake+"");
        noReplyb = new QBadgeView(mActivity).setBadgeGravity(Gravity.END | Gravity.TOP).bindTarget(yiwancheng).setBadgeText(AccountManager.sUserBean.noReply+"");

        if(AccountManager.sUserBean.noBuy==0){
            noBuyb.hide(false);
        }
        if(AccountManager.sUserBean.noPostage==0){
            noPostageb.hide(false);

        }
        if(AccountManager.sUserBean.noTake==0){
            noTakeb.hide(false);

        }
        if(AccountManager.sUserBean.noReply==0){
            noReplyb.hide(false);

        }

//        new QBadgeView(mActivity).setBadgeGravity(Gravity.END | Gravity.TOP).bindTarget(daifukuan).setBadgeNumber(AccountManager.sUserBean.noBuy);
//        new QBadgeView(mActivity).setBadgeGravity(Gravity.END | Gravity.TOP).bindTarget(daifahuo).setBadgeNumber(AccountManager.sUserBean.noPostage);
//        new QBadgeView(mActivity).setBadgeGravity(Gravity.END | Gravity.TOP).bindTarget(daishouhuo).setBadgeNumber(AccountManager.sUserBean.noTake);
//        new QBadgeView(mActivity).setBadgeGravity(Gravity.END | Gravity.TOP).bindTarget(yiwancheng).setBadgeNumber(AccountManager.sUserBean.noReply);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFlashEvent(FlashEvent wechatPayEvent) {
        try {
            getMineData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initEvent() {
        mykefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    helper.initSdkChat("e183f850-6650-11e9-b942-bf7a16e827df", "咨询", AccountManager.sUserBean.uid,REQUEST_CODE_OPENCHAT);//陈辰正式
            }
        });
        myquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MobclickAgent.onEvent(mActivity,"C_personal_coupon_head");
                startActivity(new Intent(mActivity, AllQuanActivity.class));
            }
        });
        myaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, AddressListActivity.class));
            }
        });
        allOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mActivity,"C_personal_o_goods");
                startActivity(new Intent(v.getContext(), AllOrderActivity.class));
            }
        });
        daifukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mActivity,"C_personal_o_goods");
                startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("type",1));
            }
        });
        daifahuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mActivity,"C_personal_o_goods");
                startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("type",2));
            }
        });
        daishouhuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mActivity,"C_personal_o_goods");
                startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("type",3));
            }
        });
        yiwancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mActivity,"C_personal_o_goods");
                startActivity(new Intent(v.getContext(), AllOrderActivity.class).putExtra("type",4));
            }
        });
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AccountManager.sUserBean==null){
                    startActivity(new Intent(mActivity, LoginActivity.class));
                }else{
                    changeUserHeadPopwindow();
                }
            }
        });
        mycollet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mActivity,"C_personal_collect");
                startActivity(new Intent(mActivity, ShouCangActivity.class));
            }
        });
        mymoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MobclickAgent.onEvent(mActivity,"C_personal_money");
                try {
                    if("1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))){
                        startActivity(new Intent(mActivity, FenXiaoAccountActivity.class));
                    }else{
                        startActivity(new Intent(mActivity, FenXiaoNoActivity.class));
                    }
                } catch (Exception e) {
                    Toast.makeText(mActivity,"请登录",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });
        myunder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MobclickAgent.onEvent(mActivity,"C_personal_fxzx");
                try {
                    if("1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))){
                        startActivity(new Intent(mActivity, FenXiaoMenuActivity.class));
                    }else{
                        startActivity(new Intent(mActivity, FenXiaoNoActivity.class));
                    }
                } catch (Exception e) {
                    Toast.makeText(mActivity,"请登录",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });
        myup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if("1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))){
                        startActivity(new Intent(mActivity, TuanDuiActivity.class));
                    }else{
                        startActivity(new Intent(mActivity, FenXiaoNoActivity.class));
                    }
                } catch (Exception e) {
                    Toast.makeText(mActivity,"请登录",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        mylessmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(AccountManager.sUserBean==null){

                        Toast.makeText(mActivity,"请登录",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if("1".equals((AccountManager.sUserBean==null?"0":AccountManager.sUserBean.is_promoter))){
                        startActivity(new Intent(mActivity, FenXiaoMenuActivity.class));
                    }else{
                        startActivity(new Intent(mActivity, FenXiaoNoActivity.class));
                    }
                } catch (Exception e) {
                    Toast.makeText(mActivity,"请登录",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
//                MobclickAgent.onEvent(mActivity,"C_personal_balance_head");
//                startActivity(new Intent(mActivity, AccountActivity.class));
            }
        });
        myzxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mActivity, TuiGuangActivity.class));
                MobclickAgent.onEvent(mActivity,"C_personal_tgm");
//                try {
//                    if("1".equals(AccountManager.sUserBean.is_promoter)){
//                    }else{
//                        startActivity(new Intent(mActivity, FenXiaoNoActivity.class));
//                    }
//                } catch (Exception e) {
//                    Toast.makeText(mActivity,"请登录",Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
            }
        });
//        mysetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        passtongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MobclickAgent.onEvent(mActivity,"C_personal_coupon_head");
                startActivity(new Intent(mActivity, TongZhiActivity.class));
            }
        });
        mysetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MobclickAgent.onEvent(mActivity,"C_personal_set");
                startActivity(new Intent(mActivity, ShopSettingActivity.class));
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

        StyledDialog.init(mActivity);
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
//                cutPhoto(mPhotoPath);
                uploadAvatar(images.get(0).path);
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA) {
            cutPhoto(mPhotoPath);
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CROP) {
            DialogUtil.showProgress(mActivity, getString(R.string.prompt_open_modify_head));
            uploadAvatar(mCropPath);
        }
    }

    /**
     * 上传头像
     *
     * @param avatarPath
     */
    private void uploadAvatar(final String avatarPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file=new File(avatarPath);
                Map<String, Object> paramMap=new HashMap<>();
                paramMap.put("file",file);
                try {
                    String result=HttpUrlConnectUtil.uploadFile(RequestManager.mBaseUrl+"api/images/upload.html",paramMap,"");//上传头像
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        String url=jsonObject.optString("src");
                        System.out.println("开始解析:"+url);
                        if("".equals(url)){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(mActivity,"上传头像解析失败,请选择其他头像",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            updateAvatar(url);
                        }
                    } catch (JSONException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(mActivity,"上传头像解析失败,请选择其他头像",Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
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
    public void updateAvatar(String url){
        Map<String, String> map = new HashMap<>();
        map.put("avatar",url);
        map.put("type","1");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).changeavatar(SPUtil.get("head", "").toString(),RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        Toast.makeText(mActivity,"头像修改成功",Toast.LENGTH_SHORT).show();
                        getMineData();
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}