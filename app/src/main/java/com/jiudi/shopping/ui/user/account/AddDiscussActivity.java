package com.jiudi.shopping.ui.user.account;

import android.Manifest;
import android.content.Context;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.jiudi.shopping.R;
import com.jiudi.shopping.adapter.recycler.RecyclerCommonAdapter;
import com.jiudi.shopping.adapter.recycler.base.ViewHolder;
import com.jiudi.shopping.base.BaseActivity;
import com.jiudi.shopping.bean.Discuss;
import com.jiudi.shopping.bean.TongZhi;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.manager.RequestManager;
import com.jiudi.shopping.net.RetrofitCallBack;
import com.jiudi.shopping.net.RetrofitRequestInterface;
import com.jiudi.shopping.ui.user.ShowImageActivity;
import com.jiudi.shopping.util.DialogUtil;
import com.jiudi.shopping.util.DisplayUtil;
import com.jiudi.shopping.util.HttpUrlConnectUtil;
import com.jiudi.shopping.util.SPUtil;
import com.jiudi.shopping.util.ToastUtil;
import com.jiudi.shopping.widget.DividerItemDecoration;
import com.jiudi.shopping.widget.GlideImageLoader;
import com.jiudi.shopping.widget.KRatingBar;
import com.jiudi.shopping.widget.NoScrollGridView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddDiscussActivity extends BaseActivity {

    private List<String> pics = new ArrayList<>();
    private List<String> picst = new ArrayList<>();
    private String mPhotoPath;
    private String mCropPath;
    public static final int REQUEST_CODE_IMAGE = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    public static final int REQUEST_CODE_CROP = 3;
    public static final int PERMISSION_CODE_TAKE_PHOTO = 100;
    private android.widget.LinearLayout godss;
    private android.widget.EditText pinjiatext;
    private com.jiudi.shopping.widget.KRatingBar ratingbar1;
    private com.jiudi.shopping.widget.KRatingBar ratingbar2;
    private GridAdapter adapter;
    private android.widget.TextView addDiss;
    private android.widget.GridLayout fujianLayout;
    private int fujian_px;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_adddiscuss;
    }

    @Override
    public void initView() {

        godss = (LinearLayout) findViewById(R.id.godss);
        pinjiatext = (EditText) findViewById(R.id.pinjiatext);
        ratingbar1 = (KRatingBar) findViewById(R.id.ratingbar1);
        ratingbar2 = (KRatingBar) findViewById(R.id.ratingbar2);
        addDiss = (TextView) findViewById(R.id.addDiss);
        StyledDialog.init(this);
        fujianLayout = (GridLayout) findViewById(R.id.fujian_layout);
    }

    @Override
    public void initData() {

        ViewTreeObserver vto = fujianLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int temppx = fujianLayout.getWidth();
                fujianLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                fujian_px = (temppx - 0) / 4;
//                System.out.println("获得的大小" + fujian_px);

                new GridAdapter(mActivity, picst, fujianLayout).build();

            }
        });
        buildCart(godss, getIntent().getStringExtra("gods"));
    }

    private int dp2px(int dp, Context context) {
        if (dp == 0) {
            return 0;
        } else {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                    context.getResources().getDisplayMetrics());
        }
    }

    @Override
    public void initEvent() {
        addDiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDisscuss();
            }
        });
//        addpot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeUserHeadPopwindow();
//            }
//        });
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
     * 上传图片
     *
     * @param avatarPath
     */
    private void uploadAvatar(final List<ImageItem> avatarPath) {
        for (int i = 0; i <avatarPath.size() ; i++) {
            uploadAvatar(avatarPath.get(i).path);
        }
    }
    /**
     * 上传图片
     *
     * @param avatarPath
     */
    private void uploadAvatar(final String avatarPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(avatarPath);
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("file", file);
                try {
                    String result = HttpUrlConnectUtil.uploadFile(RequestManager.mBaseUrl + "api/images/upload.html", paramMap, "");//上传头像
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String url = jsonObject.optString("src");
                        pics.add(url);
                        picst.add(avatarPath);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new GridAdapter(mActivity, picst, fujianLayout).build();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DialogUtil.hideProgress();
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST_CODE_IMAGE) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                DialogUtil.showProgress(mActivity, "提交图片");
                if(images!=null&&images.size()>3){

                    Toast.makeText(mActivity, "最多上传三张图片", Toast.LENGTH_SHORT);
                    return;
                }
                uploadAvatar(images);
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA) {
            cutPhoto(mPhotoPath);
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CROP) {
            DialogUtil.showProgress(mActivity, "提交图片");
            uploadAvatar(mCropPath);
        }
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


    private void addDisscuss() {

        DialogUtil.showUnCancelableProgress(mActivity, "提交评价");
        Map<String, Object> map = new HashMap<>();
        map.put("unique", getIntent().getStringExtra("unique"));
        map.put("comment", pinjiatext.getText().toString());
        String picss = "";
        for (int i = 0; i < pics.size(); i++) {
            map.put("pics[" + i + "]", pics.get(i));
        }
        map.put("product_score", ratingbar1.getRating() + "");
        map.put("service_score", ratingbar2.getRating() + "");
        RequestManager.mRetrofitManager.createRequest(RetrofitRequestInterface.class).addDisscuss(SPUtil.get("head", "").toString(), RequestManager.encryptParamss(map)).enqueue(new RetrofitCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int code = res.getInt("code");
                    String info = res.getString("msg");
                    if (code == 200) {
                        finish();
                    }else{
                        Toast.makeText(mActivity,info,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DialogUtil.hideProgress();
            }

            @Override
            public void onError(Throwable t) {
                DialogUtil.hideProgress();
            }
        });
    }


    /**
     * 更换头像弹窗
     */
    private void changeUserHeadPopwindow() {
        List<String> list = new ArrayList<>();
        list.add("拍照");
        list.add("从相册选");
        list.add("取消");
        StyledDialog.buildIosSingleChoose(list, new MyItemDialogListener() {
            @Override
            public void onItemClick(CharSequence charSequence, int i) {
                if ("拍照".equals(charSequence)) {
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
                    } else {

                        openCamera();
                    }
                }
                if ("从相册选".equals(charSequence)) {
                    Intent intent = new Intent(mActivity, ImageGridActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_IMAGE);
//                    mBottomPopwindow.dismiss();
                }
                if ("取消".equals(charSequence)) {

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

    class GridAdapter {
        public Context context;
        private List<String> images;
        private ViewGroup parent;

        public GridAdapter(Context context, List<String> images, ViewGroup parent) {
            this.context = context;
            this.images = images;
            this.parent = parent;
        }

        public int getCount() {
            return images == null ? 0 : images.size();
        }

        public Object getItem(int position) {
            return images.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public void build() {
            parent.removeAllViews();

            LinearLayout layout = new LinearLayout(context);
            LinearLayout.LayoutParams LL_MW = new LinearLayout.LayoutParams
                    (fujian_px, fujian_px);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);
            layout.setLayoutParams(LL_MW);

            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            LinearLayout.LayoutParams LL_IM = new LinearLayout.LayoutParams
                    (fujian_px - 10, fujian_px - 10);
            imageView.setLayoutParams(LL_IM);
            imageView.setImageResource(R.drawable.start_pot);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (images.size() <= 3) {

                        changeUserHeadPopwindow();
                    } else {
                        Toast.makeText(context, "最多上传三张图片", Toast.LENGTH_SHORT);
                    }
                }
            });
            layout.addView(imageView);
            parent.addView(layout);
            for (int i = 0; i < getCount(); i++) {
                parent.addView(getView(i));
            }
        }

        public View getView(final int position) {
            System.out.println("进来");
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);
            layout.setPadding(5,
                    5,
                    5,
                    5);
            LinearLayout.LayoutParams LL_MW = new LinearLayout.LayoutParams
                    (fujian_px, fujian_px);
            layout.setLayoutParams(LL_MW);
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            LinearLayout.LayoutParams LL_IM = new LinearLayout.LayoutParams
                    (fujian_px - 10, fujian_px - 10);
            imageView.setLayoutParams(LL_IM);
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
            Glide.with(context).load(images.get(position)).apply(options).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ShowImageActivity.class).putExtra("URL", images.get(position)));
                }
            });
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    StyledDialog.init(mActivity);
                    StyledDialog.buildIosAlert("长按操作", "是否确认删除该图片", new MyDialogListener() {
                        @Override
                        public void onFirst() {
                            pics.remove(position);
                            picst.remove(position);
                            build();
                        }

                        @Override
                        public void onSecond() {

                        }

                        @Override
                        public void onThird() {

                        }


                    })
                            //.setBtnText("sure","cancle","hhhh")
                            .setBtnText("确定", "取消")
                            .setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0)
                            //.setForceWidthPercent(0.99f)
                            //.setForceHeightPercent(0.88f)
                            //.setBgRes(R.drawable.leak_canary_icon)
                            .show();
                    return true;
                }
            });
            layout.addView(imageView);
            return layout;
        }
    }

    private void buildCart(ViewGroup viewGroup, String cartInfoInfoString) {
        try {

            View cartinfol = LayoutInflater.from(mActivity).inflate(R.layout.item_cart_item, viewGroup, false);
            JSONObject cartInfo = new JSONObject(cartInfoInfoString);
            ImageView imageView = cartinfol.findViewById(R.id.cart_icon2);
            TextView cart_title = cartinfol.findViewById(R.id.cart_title);
            TextView cart_cunk = cartinfol.findViewById(R.id.cart_cunk);
            TextView cart_count = cartinfol.findViewById(R.id.cart_count);
            TextView cart_money = cartinfol.findViewById(R.id.cart_money);
            String pic = cartInfo.getJSONObject("productInfo").getString("image");
            String title = cartInfo.getJSONObject("productInfo").getString("store_name");
            String unique = "";
            try {
                String cunk = cartInfo.getJSONObject("productInfo").getJSONObject("attrInfo").getString("suk");
                cart_cunk.setText(cunk);
            } catch (JSONException e) {
                cart_cunk.setVisibility(View.INVISIBLE);
                e.printStackTrace();
            }
            TextView pinjia = cartinfol.findViewById(R.id.pingjia);
            pinjia.setVisibility(View.GONE);
            String count = "X" + cartInfo.getString("cart_num");
            String money = "¥" + cartInfo.getString("truePrice");
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存全尺寸
            Glide.with(mActivity).load(pic).apply(options).into(imageView);
            cart_title.setText(title);
            cart_count.setText(count);
            cart_money.setText(money);
            viewGroup.addView(cartinfol);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
