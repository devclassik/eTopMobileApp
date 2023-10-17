package com.gbikna.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;

import com.gbikna.sample.utils.AidsUtil;
import com.gbikna.sample.utils.HexUtil;
import com.horizonpay.smartpossdk.aidl.emv.CapkEntity;
import com.horizonpay.smartpossdk.aidl.emv.IAidlEmvL2;
import com.horizonpay.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.internal.operators.flowable.FlowableThrottleFirstTimed;


public class CapkManagerActivity extends BaseActivity {
    private final String LOG_TAG = CapkManagerActivity.class.getSimpleName();
    private final boolean LOG_ENABLE = true;
    private IAidlEmvL2 mEmvL2;
    private boolean isSupport;
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capk);
        ButterKnife.bind(this);
        try {
            mEmvL2 = MyApplication.getINSTANCE().getDevice().getEmvL2();
            isSupport = mEmvL2.isSupport();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.downloadPuk, R.id.clearPuk, R.id.getPukList})
    public void onClick(View view) {
        if (!isSupport) {
            ToastUtils.showShort(R.string.err_not_support_api);
            return;
        }

        switch (view.getId()) {
            case R.id.downloadPuk:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downloadCAPK();
                    }
                }).start();
                break;
            case R.id.clearPuk:
                clearCAPK();
                break;
            case R.id.getPukList:
                getCAPKList();
                break;
        }
    }


    private void downloadCAPK() {
        List<CapkEntity> capkEntityList = AidsUtil.getAllCapks();
        try {
            mEmvL2.addCapks(capkEntityList);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        boolean ret = false;
        for (int i = 0; i < capkEntityList.size(); i++) {
            String tip = "Download capk" + String.format("(%d)", i);
            showResult(textView, tip);
            CapkEntity emvCapkPara = capkEntityList.get(i);
            try {
                ret = mEmvL2.addCapk(emvCapkPara);
                if (!ret) {
                    break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            SystemClock.sleep(50);
            showResult(textView, "Download capk :" + emvCapkPara.getRID() + (ret == true ? " success" : " fail"));

        }
        showResult(textView, "Download capk " + (ret == true ? "success" : "fail"));
    }

    private void clearCAPK() {
        try {
            boolean ret = mEmvL2.deleteAllCapks();
            if (ret) {
                showResult(textView, "Clear CAPK success!");
            } else {
                showResult(textView, "Clear CAPK Failed!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.getMessage());
        }
    }

    private void getCAPKList() {
        try {
            List<CapkEntity> capkEntityList = mEmvL2.getCapkList();
            StringBuilder builder = new StringBuilder();
            builder.append("\n CAPK num: " + capkEntityList.size());
            for (int i = 0; i < capkEntityList.size(); i++) {
                builder.append("\nRID:" + capkEntityList.get(i).getRID());
                String s = HexUtil.encode(capkEntityList.get(i).getCapkIndex());
                if (s.length() == 8) {
                    s = s.substring(6);
                }
                builder.append("\nCAPK Index:" + s);
                builder.append("\nHash Index:" + capkEntityList.get(i).getHashInd());
                builder.append("\natith Index:" + capkEntityList.get(i).getArithInd());
                builder.append("\nModule:" + capkEntityList.get(i).getModul());
                builder.append("\nExponent:" + capkEntityList.get(i).getExponent());
                builder.append("\nExpr Date:" + capkEntityList.get(i).getExpDate());
                builder.append("\nChecksum:" + capkEntityList.get(i).getCheckSum());
            }
            showResult(textView, builder.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.getMessage());
        }
    }


    @Override
    protected void setButtonName() {
    }


}