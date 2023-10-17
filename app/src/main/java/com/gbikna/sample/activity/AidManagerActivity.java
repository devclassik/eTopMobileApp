package com.gbikna.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.gbikna.sample.MyApplication;

import com.gbikna.sample.R;
import com.gbikna.sample.utils.AidsUtil;
import com.horizonpay.smartpossdk.aidl.emv.AidEntity;
import com.horizonpay.smartpossdk.aidl.emv.IAidlEmvL2;
import com.horizonpay.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***************************************************************************************************
 *                          Copyright (C),  Shenzhen Horizon Technology Limited                    *
 *                                   http://www.horizonpay.cn                                      *
 ***************************************************************************************************
 * usage           :
 * Version         : 1
 * Author          : Atos
 * Date            : 2017/12/18
 * Modify          : create file
 **************************************************************************************************/
public class AidManagerActivity extends BaseActivity {
    private final String LOG_TAG = AidManagerActivity.class.getSimpleName();
    private final boolean LOG_ENABLE = true;
    private IAidlEmvL2 mEmvL2;
    private boolean isSupport;
    @BindView(R.id.textView)
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aid);
        ButterKnife.bind(this);

        try {
            mEmvL2 = MyApplication.getINSTANCE().getDevice().getEmvL2();
            isSupport = mEmvL2.isSupport();
        } catch (RemoteException e) {
            e.printStackTrace();
            ToastUtils.showShort(e.getMessage());
        }
    }

    @OnClick({R.id.downloadAid, R.id.clearAid, R.id.getAidList})
    public void onClick(View view) {
        if (!isSupport) {
            ToastUtils.showShort(R.string.err_not_support_api);
            return;
        }

        switch (view.getId()) {
            case R.id.downloadAid:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downloadAID();
                    }
                }).start();
                break;
            case R.id.clearAid:
                clearAID();
                break;
            case R.id.getAidList:
                getAIDList();
                break;
        }
    }

    private void downloadAID() {
        List<AidEntity> aidEntityList = AidsUtil.getAllAids();
        boolean ret = false;
        for (int i = 0; i < aidEntityList.size(); i++) {
            String tip = "Download aid" + String.format("(%d)", i);
            showResult(textView, tip);
            AidEntity emvAidPara = aidEntityList.get(i);
            try {
                ret = mEmvL2.addAid(emvAidPara);
                if (!ret) {
                    break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            SystemClock.sleep(50);
            showResult(textView, "Download aid :" + emvAidPara.getAID() + (ret == true ? " success" : " fail"));
        }
    }

    private void clearAID() {
        try {
            boolean ret = mEmvL2.deleteAllAids();
            if (ret) {
                showResult(textView, getString(R.string.mesg_aid_clear_succ));
            } else {
                showResult(textView, getString(R.string.mesg_aid_clear_fail));
            }
        } catch (RemoteException e) {

        }

    }

    private void getAIDList() {

        List<AidEntity> aidEntityList = null;
        try {
            aidEntityList = mEmvL2.getAidParaList();
            StringBuilder builder = new StringBuilder();
            builder.append("\n AID num: " + aidEntityList.size());
            for (int i = 0; i < aidEntityList.size(); i++) {
                AidEntity emvAidPara = aidEntityList.get(i);
                builder.append("\n AID:\n" + emvAidPara.getAID());
                builder.append("\n TermAppVer: " + emvAidPara.getAppVersion());
                builder.append("\n TFL_Domestic: " + emvAidPara.getFloorLimit());
                builder.append("\n TAC_Default: " + emvAidPara.getTacDefault());
                builder.append("\n TAC_Online: " + emvAidPara.getTacOnline());
                builder.append("\n TAC_Denial: " + emvAidPara.getTacDenial());
                builder.append("\n RFOfflineLimit: " + emvAidPara.getRdVisaTransLimit());
                builder.append("\n RFTransLimit: " + emvAidPara.getRdCtlsTransLimit());
                builder.append("\n RFCVMLimit: " + emvAidPara.getRdCtlsCvmLimit());
                builder.append("\n DDOL: " + emvAidPara.getDDOL());
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