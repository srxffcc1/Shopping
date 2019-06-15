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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.github.nukc.LoadMoreWrapper.LoadMoreAdapter;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.vl.VHotGridAdapter;
import com.jiudi.shopping.adapter.vl.VMineAdapter;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.bean.MainGodsBean;
import com.jiudi.shopping.bean.UserBean;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.HttpUrlConnectUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.widget.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Steven on 2017/12/21.
 */
public class MineNewFragment extends BaseFragment implements View.OnClickListener, LoadMoreAdapter.OnLoadMoreListener,SwipeRefreshLayout.OnRefreshListener{


    private android.support.v4.widget.SwipeRefreshLayout swipeRefreshLayout;
    private android.support.v7.widget.RecyclerView recycler;



    private VirtualLayoutManager manager;
    private DelegateAdapter adapter;
    final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();

    private List<MainGodsBean> mHotVlList = new ArrayList<>();
    private VMineAdapter vMineAdapter;
    private VHotGridAdapter vHotGridAdapter;
    private boolean stoploadmore = false;
    private LoadMoreAdapter mLoadMoreAdapter;
    private int page = 0;
    private int limit = 20;
    private String mPhotoPath;
    private String mCropPath;
    public static final int REQUEST_CODE_IMAGE = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    public static final int REQUEST_CODE_CROP = 3;
    public static final int PERMISSION_CODE_TAKE_PHOTO = 100;

    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_minenew;
    }


    @Override
    public void initView() {

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    public void initData() {
        getGodsList(false);
    }

    @Override
    public void initEvent() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }
    public void buildRecycleView(boolean needscroll) {
        if(adapter==null){
            manager = new VirtualLayoutManager(mActivity);
            recycler.setLayoutManager(manager);
            adapter = new DelegateAdapter(manager);
            RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
            recycler.setRecycledViewPool(viewPool);
            viewPool.setMaxRecycledViews(0, 10);
            SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
            LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
            GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(5);
            gridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 1;
                }
            });
            gridLayoutHelper.setAutoExpand(false);

            GridLayoutHelper gridLayoutHelper2 = new GridLayoutHelper(2);
            gridLayoutHelper2.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 1;
                }
            });
            gridLayoutHelper2.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 1;
                }
            });

            gridLayoutHelper2.setAutoExpand(false);
            StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper();
            stickyLayoutHelper.setStickyStart(true);
            vMineAdapter=new VMineAdapter(mActivity,singleLayoutHelper,this);
            vHotGridAdapter = new VHotGridAdapter(mActivity, gridLayoutHelper2, mHotVlList);
            adapters.add(vMineAdapter);
            adapters.add(vHotGridAdapter);
            adapter.setAdapters(adapters);
            recycler.setAdapter(adapter);
//            mLoadMoreAdapter = LoadMoreWrapper.with(adapter)
//                    .setLoadMoreEnabled(!stoploadmore)
//                    .setListener(this)
//                    .into(recycler);
        }else {
            try {
                vHotGridAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private void getGodsList(final boolean needscroll) {
        Map<String, String> map = new HashMap<>();
//        map.put("first", page + "");
//        map.put("limit", limit + "");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).getLoveGodsList(SPUtil.get("head", "").toString(), RequestManager.encryptParams(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {


//                simpleRefresh.onRefreshComplete();
//                simpleRefresh.onLoadMoreComplete();
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        JSONArray jsonArray = res.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                MainGodsBean bean = new MainGodsBean();
                                bean.id = jsonObject.optString("id");
                                bean.image = jsonObject.optString("image");
                                bean.store_name = jsonObject.optString("store_name");
                                bean.keyword = jsonObject.optString("keyword");
                                bean.sales = jsonObject.optInt("sales");
                                bean.stock = jsonObject.optInt("stock");
                                bean.vip_price = jsonObject.optString("vip_price");
                                bean.price = jsonObject.optString("price");
                                bean.unit_name = jsonObject.optString("unit_name");
                                mHotVlList.add(bean);
                            }
                            buildRecycleView(needscroll);
                        } else {
//
                            noMoreData();
                            buildRecycleView(needscroll);
                            stoploadmore = true;
                        }


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
    public void noMoreData() {
        if (mLoadMoreAdapter != null) {
            mLoadMoreAdapter.setLoadMoreEnabled(false);
            mLoadMoreAdapter.setShowNoMoreEnabled(true);
            mLoadMoreAdapter.getOriginalAdapter().notifyDataSetChanged();
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head:
                changeUserHeadPopwindow();
                break;
        }
    }
    @Override
    public void onRefresh() {
        stoploadmore=false;
        page=0;
        mHotVlList.clear();
        getGodsList(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore(LoadMoreAdapter.Enabled enabled) {
//        page=page+limit;
//        getGodsList(false);
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
                                    JSONObject data = res.getJSONObject("data");
                                    bean.remind=data.optString("remind");
                                    bean.collect_number=data.optString("collect_number");
                                    bean.coupon_number=data.optString("coupon_number");
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
                                bindDataToView(AccountManager.sUserBean);

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

    private void bindDataToView(UserBean sUserBean) {
        vMineAdapter.bindDataToView(sUserBean);
    }


    public void initImagePicker() {
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
    public void changeUserHeadPopwindow() {

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
    public void openCamera() {
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
    public File createImageFile() throws IOException {
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
    public void uploadAvatar(final String avatarPath) {
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

}