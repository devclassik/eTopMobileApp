package com.gbikna.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.RadioButton;

import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.horizonpay.smartpossdk.aidl.beeper.IAidlBeeper;
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
public class BeepActivity extends BaseActivity {

    @BindView(R.id.normal)
    RadioButton normal;

    @BindView(R.id.success)
    RadioButton success;

    @BindView(R.id.fail)
    RadioButton fail;

    @BindView(R.id.interval)
    RadioButton interval;

    @BindView(R.id.error)
    RadioButton error;

    private final String TAG = BeepActivity.class.getName();
    public static final int NORMAL = 0;
    public static final int SUCCESS = 1;
    public static final int FAIL = 2;
    public static final int INTERVAL = 3;
    public static final int ERROR = 4;
    IAidlBeeper beeper;
    private boolean isSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beep);
        ButterKnife.bind(this);

        setButtonName();
        try {
            beeper = MyApplication.getINSTANCE().getDevice().getBeeper();
            isSupport = beeper.isSupport();
        } catch (RemoteException e) {
            e.printStackTrace();
            ToastUtils.showShort("Error:" + e.getMessage());
        }
    }

    @OnClick({R.id.button})
    public void onClick() {
        if (!isSupport) ToastUtils.showShort(R.string.err_not_support_api);
        setbeep();
    }

    private void setbeep() {
        int beepType;
        try {
            if (normal.isChecked()) {
                beepType = NORMAL;
            } else if (success.isChecked()) {
                beepType = SUCCESS;
            } else if (fail.isChecked()) {
                beepType = FAIL;
            } else if (interval.isChecked()) {
                beepType = INTERVAL;
            } else {
                beepType = ERROR;
            }

            beeper.beep(beepType, 2);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setButtonName() {

    }

}