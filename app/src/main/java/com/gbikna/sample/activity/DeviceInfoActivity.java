package com.gbikna.sample.activity;


import android.os.Bundle;
import android.os.RemoteException;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.gbikna.sample.BuildConfig;
import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.gbikna.sample.utils.AppLog;
import com.horizonpay.smartpossdk.data.SysConst.DeviceInfo;
import com.horizonpay.utils.FormatUtils;
import com.horizonpay.utils.ToastUtils;

import java.math.BigDecimal;


/***************************************************************************************************
 *                          Copyright (C),  Shenzhen Horizon Technology Limited                    *
 *                                   http://www.horizonpay.cn                                      *
 ***************************************************************************************************
 * usage           :
 * Version         : 1
 * Author          : Ashur Liu
 * Date            : 2017/12/18
 * Modify          : create file
 **************************************************************************************************/
public class DeviceInfoActivity extends BaseActivity {
    private static final String TAG = "DeviceInfoActivity";
    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_button);
        ButterKnife.bind(this);

        setButtonName();
    }

    @OnClick({R.id.button})
    public void onClick() {
        getDeviceInfo();
    }

    private void getDeviceInfo() {
        try {
            Bundle devInfo = MyApplication.getINSTANCE().getDevice().getSysHandler().getDeviceInfo();
            String vendor = devInfo.getString(DeviceInfo.DEVICE_VENDOR);
            String model = devInfo.getString(DeviceInfo.DEVICE_MODEL);
            String os_ver = devInfo.getString(DeviceInfo.DEVICE_OS_VER);
            String sn = devInfo.getString(DeviceInfo.DEVICE_SN);
            String sdk_version = devInfo.getString(DeviceInfo.DEVICE_SDK_VER);
            String firmware = devInfo.getString(DeviceInfo.DEVICE_FIRMWARE_VER);
            String hl = devInfo.getString(DeviceInfo.DEVICE_HL_VERSION);

            StringBuilder builder = new StringBuilder();
            builder.append(getString(R.string.vendorInfo) + vendor + "\n");
            builder.append(getString(R.string.model) + model + "\n");
            builder.append(getString(R.string.systemVer) + os_ver + "\n");
            builder.append(getString(R.string.serialNo) + sn + "\n");
            builder.append(getString(R.string.sdkv) + sdk_version + "\n");
            builder.append(getString(R.string.firmware) + firmware + "\n");
            builder.append(getString(R.string.hl) + hl + "\n");
            builder.append(getString(R.string.demo_ver) + BuildConfig.VERSION_NAME + "\n");





            showResult(textView, builder.toString());
        } catch (RemoteException e) {
            ToastUtils.showShort(e.getMessage());
            showResult(textView, e.getMessage());
        }

    }

    @Override
    protected void setButtonName() {
        button.setText(getString(R.string.device_info));
    }


}