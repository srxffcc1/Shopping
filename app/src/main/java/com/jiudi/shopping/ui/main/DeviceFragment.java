package com.jiudi.shopping.ui.main;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiudi.shopping.R;
import com.jiudi.shopping.base.BaseFragment;
import com.jiudi.shopping.manager.AccountManager;
import com.jiudi.shopping.ui.device.DeviceActivity;

/**
 * 主页
 */
public class DeviceFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "DeviceFragment";
    private android.widget.LinearLayout passdevice;
    private android.widget.TextView device;
    private android.widget.RelativeLayout rlMore;
    private android.widget.LinearLayout liClgz;
    private android.widget.LinearLayout liCltj;
    private android.widget.LinearLayout liQcfd;
    private android.widget.LinearLayout liDyjk;
    private android.widget.LinearLayout liXcgj;
    private android.widget.LinearLayout liJscp;
    private android.widget.LinearLayout liWmjs;
    private android.widget.LinearLayout liDh;
    private android.widget.LinearLayout dzg;
    private android.widget.LinearLayout jyzj;
    private LinearLayout needhide;
    private LinearLayout liWl;
    private LinearLayout liGj;
    private LinearLayout liXq;
    private LinearLayout liXc;
    private LinearLayout liJc;
    private LinearLayout liCk;
    private LinearLayout liBj;
    private RelativeLayout rlLayoutTopBackBar;
    private LinearLayout llLayoutTopBackBarBack;
    private TextView tvLayoutTopBackBarStart;
    private TextView tvLayoutTopBackBarTitle;
    private TextView tvLayoutTopBackBarEnd;
    private TextView tvLayoutBackTopBarOperate;


    @Override
    protected int getInflateViewId() {
        return R.layout.fragment_device;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        passdevice = (LinearLayout) byId(R.id.passdevice);
        device = (TextView) byId(R.id.device);
        rlMore = (RelativeLayout) byId(R.id.rl_more);
        liDh = (LinearLayout) byId(R.id.li_dh);

        needhide = (LinearLayout) findViewById(R.id.needhide);
        liWl = (LinearLayout) findViewById(R.id.li_wl);
        liGj = (LinearLayout) findViewById(R.id.li_gj);
        liXq = (LinearLayout) findViewById(R.id.li_xq);
        liXc = (LinearLayout) findViewById(R.id.li_xc);
        liJc = (LinearLayout) findViewById(R.id.li_jc);
        liCk = (LinearLayout) findViewById(R.id.li_ck);
        liBj = (LinearLayout) findViewById(R.id.li_bj);
        rlLayoutTopBackBar = (RelativeLayout) findViewById(R.id.rl_layout_top_back_bar);
        llLayoutTopBackBarBack = (LinearLayout) findViewById(R.id.ll_layout_top_back_bar_back);
        tvLayoutTopBackBarStart = (TextView) findViewById(R.id.tv_layout_top_back_bar_start);
        tvLayoutTopBackBarTitle = (TextView) findViewById(R.id.tv_layout_top_back_bar_title);
        tvLayoutTopBackBarEnd = (TextView) findViewById(R.id.tv_layout_top_back_bar_end);
        tvLayoutBackTopBarOperate = (TextView) findViewById(R.id.tv_layout_back_top_bar_operate);
        tvLayoutTopBackBarTitle.setText("设备");
    }

    @Override
    public void initData() {
        initDevice();

    }
    public void initDevice(){
        if (AccountManager.sUserBean != null) {
            if(AccountManager.sUserBean.obd_isbind!=null&&!"0".equals(AccountManager.sUserBean.obd_isbind)){
                device.setText("查看OBD详情");
            }else{
                passdevice.setBackgroundResource(R.drawable.noobd);
            }
        }else{
            passdevice.setBackgroundResource(R.drawable.noobd);
        }
    }

    @Override
    public void initEvent() {
        passdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!device.getText().toString().contains("绑定")){

                    startActivity(new Intent(v.getContext(), DeviceActivity.class));
                }else{

                    startActivity(new Intent(v.getContext(), MainGoodDetailActivity.class).putExtra("id",AccountManager.bestGood).putExtra("needbind",true));
                }
            }
        });


        liGj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!device.getText().toString().contains("绑定")){

                    startActivity(new Intent(v.getContext(), DeviceActivity.class));
                }else{

                    startActivity(new Intent(v.getContext(), MainGoodDetailActivity.class).putExtra("id",AccountManager.bestGood).putExtra("needbind",true));
                }
            }
        });
        liDh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!device.getText().toString().contains("绑定")){

                    startActivity(new Intent(v.getContext(), DeviceActivity.class));
                }else{

                    startActivity(new Intent(v.getContext(), MainGoodDetailActivity.class).putExtra("id",AccountManager.bestGood).putExtra("needbind",true));
                }
            }
        });
        liJc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!device.getText().toString().contains("绑定")){

                    startActivity(new Intent(v.getContext(), DeviceActivity.class));
                }else{

                    startActivity(new Intent(v.getContext(), MainGoodDetailActivity.class).putExtra("id",AccountManager.bestGood).putExtra("needbind",true));
                }
            }
        });
        liWl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!device.getText().toString().contains("绑定")){

                    startActivity(new Intent(v.getContext(), DeviceActivity.class));
                }else{

                    startActivity(new Intent(v.getContext(), MainGoodDetailActivity.class).putExtra("id",AccountManager.bestGood).putExtra("needbind",true));
                }

            }
        });
        liXc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!device.getText().toString().contains("绑定")){

                    startActivity(new Intent(v.getContext(), DeviceActivity.class));
                }else{

                    startActivity(new Intent(v.getContext(), MainGoodDetailActivity.class).putExtra("id",AccountManager.bestGood).putExtra("needbind",true));
                }
            }
        });
        liBj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!device.getText().toString().contains("绑定")){

                    startActivity(new Intent(v.getContext(), DeviceActivity.class));
                }else{

                    startActivity(new Intent(v.getContext(), MainGoodDetailActivity.class).putExtra("id",AccountManager.bestGood).putExtra("needbind",true));
                }
            }
        });
        liXq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!device.getText().toString().contains("绑定")){

                    startActivity(new Intent(v.getContext(), DeviceActivity.class));
                }else{

                    startActivity(new Intent(v.getContext(), MainGoodDetailActivity.class).putExtra("id",AccountManager.bestGood).putExtra("needbind",true));
                }
            }
        });
        liCk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!device.getText().toString().contains("绑定")){

                    startActivity(new Intent(v.getContext(), DeviceActivity.class));
                }else{

                    startActivity(new Intent(v.getContext(), MainGoodDetailActivity.class).putExtra("id",AccountManager.bestGood).putExtra("needbind",true));
                }
            }
        });
    }
}
