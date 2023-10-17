package com.gbikna.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.CheckBox;

import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.horizonpay.smartpossdk.aidl.led.IAidlLed;
import com.horizonpay.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
public class LedActivity extends BaseActivity {
    private final String TAG = LedActivity.class.getName();
    IAidlLed led;
    private boolean isSupport;
    @BindView(R.id.button)
    Button button;

    @BindView(R.id.led1)
    CheckBox led1;

    @BindView(R.id.led2)
    CheckBox led2;

    @BindView(R.id.led3)
    CheckBox led3;

    @BindView(R.id.led4)
    CheckBox led4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);
        ButterKnife.bind(this);

        setButtonName();
        try {
            led = MyApplication.getINSTANCE().getDevice().getLED();
            isSupport = led.isSupport();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.button})
    public void onClick() {
        if (!isSupport) {
            ToastUtils.showShort(R.string.err_not_support_api);
        } else {
            setLed();
        }
    }

    private void setLed() {
        try {
            led.PowerLed(led1.isChecked(), led2.isChecked(), led3.isChecked(), led4.isChecked());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setButtonName() {
        button.setText(getString(R.string.menu_led));
    }

}