package com.gbikna.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.utils.AppLog;
import com.gbikna.sample.utils.HexUtil;
import com.horizonpay.smartpossdk.aidl.pinpad.AidlPinPadInputListener;
import com.horizonpay.smartpossdk.aidl.pinpad.DukptEncryptObj;
import com.horizonpay.smartpossdk.aidl.pinpad.DukptObj;
import com.horizonpay.smartpossdk.aidl.pinpad.IAidlPinpad;
import com.horizonpay.smartpossdk.data.PinpadConst;
import com.horizonpay.utils.FormatUtils;
import com.horizonpay.utils.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import java.io.ByteArrayOutputStream;
import java.security.spec.KeySpec;

import static com.gbikna.sample.R2.string.pan;

/***************************************************************************************************
 *                          Copyright (C),  Shenzhen Horizon Technology Limited                    *
 *                                   http://www.horizonpay.cn                                      *
 ***************************************************************************************************
 * usage           :
 * Version         : 1
 * Author          : Alex
 * Date            : 2020/09/12
 * Modify          : create file
 **************************************************************************************************/
public class DukptActivity extends BaseActivity {
    private String encrypt;
    IAidlPinpad pinpad;
    private boolean isSupport;
    private static final String TAG = "DukptActivity";
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dukpt);
        ButterKnife.bind(this);
        setButtonName();
        try {
            pinpad = MyApplication.getINSTANCE().getDevice().getPinpad(true);
            isSupport = pinpad.isSupport();
        } catch (RemoteException e) {
            showResult(textView, e.getMessage());
        }
    }

    @OnClick({R.id.inputPin, R.id.dukptInit, R.id.dukptDeccrypt, R.id.dukptEncrypt, R.id.calcMac, R.id.increaseKsn, R.id.getKsn})
    public void onClick(View view) {

        if (!isSupport) {
            ToastUtils.showShort(R.string.err_not_support_api);
            return;
        }
        switch (view.getId()) {
            case R.id.inputPin:
                inputPin();
                break;
            case R.id.increaseKsn:
                increaseKsn();
                break;
            case R.id.getKsn:
                getCurrentKsn();
                break;
            case R.id.dukptInit:
                dukptInit();
                break;
            case R.id.dukptEncrypt:
                dukptEncrypt();
                break;
            case R.id.dukptDeccrypt:
                dukptDecrypt();
                break;
            case R.id.calcMac:
                calcMac();
                break;
        }
    }

    private void increaseKsn() {

        try {
            String ksn = pinpad.dukptKsnIncrease(PinpadConst.DukptKeyIndex.DUKPT_KEY_INDEX_0);
            showResult(textView, "ksn:" + ksn);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    private void getCurrentKsn() {

        try {
            String ksn = pinpad.dukptCurrentKsn(PinpadConst.DukptKeyIndex.DUKPT_KEY_INDEX_0);
            showResult(textView, "ksn:" + ksn);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void calcMac() {
        try {
            byte[] data = HexUtil.hexStringToByte("00000000005010016222620910029130840241205100100367FD3414057DB801BE18A309A544C5174CC777525974CBD467BCC56EA16629F3B016488A6C314921485C75F57066D4682FEDC1F910C5C8136A201279B590898B40D7098461D345168810CCFEBC61204B3E6F364A95175EF54C7EBAAEC2A6AEE44D9783747124D313B78A3F754C5ECC611533C4957377DD2067DF927C80461C4E4C20A8A4CC57EF1CCE2BC1AEEA442431256F66A25AB855912BA82FB8AD308F0EDE358CDDDEA63C95401B8335C8689E5735E0FB96733426FD71A7248E140A95CB4B4313AC0DBDA1E70EA8800000000000");

            byte[] xor = new byte[8];

            for (int i = 0; i < 8; i++) {
                xor[i % 8] = (byte) (xor[i % 8] ^ data[i]);
            }

            int type = PinpadConst.DukptType.DUKPT_DES_KEY_MAC1;
            int alg = PinpadConst.AlgoMode.ALG_CBC;
            int oper = PinpadConst.EncryptMode.MODE_ENCRYPT;


            DukptEncryptObj dukptEncryptObj = new DukptEncryptObj(type, oper, alg, HexUtil.bytesToHexString(xor));
            Bundle bundle = pinpad.dukptCalcDes(dukptEncryptObj);
            showResult(textView, "dukpt mac:" + bundle.getString(dukptEncryptObj.DUKPT_DATA));
            showResult(textView, "ksn:" + bundle.getString(dukptEncryptObj.DUKPT_KSN));
        } catch (RemoteException e) {

        }
    }

    private void dukptDecrypt() {
        try {
            String data = encrypt;

            int type = PinpadConst.DukptType.DUKPT_DES_KEY_DATA1;
            int alg = PinpadConst.AlgoMode.ALG_CBC;
            int oper = PinpadConst.EncryptMode.MODE_DECRYPT;

            DukptEncryptObj dukptEncryptObj = new DukptEncryptObj(type, oper, alg, data);
            Bundle bundle = pinpad.dukptCalcDes(dukptEncryptObj);


            showResult(textView, "dukpt decrypt:" + bundle.getString(dukptEncryptObj.DUKPT_DATA));
            showResult(textView, "ksn:" + bundle.getString(dukptEncryptObj.DUKPT_KSN));

        } catch (RemoteException e) {

        }
    }

    private void dukptEncrypt() {
        try {
            String data = "12345678ABCDEFGH";

            int alg = PinpadConst.AlgoMode.ALG_CBC;
            int oper = PinpadConst.EncryptMode.MODE_ENCRYPT;
            int type = PinpadConst.DukptType.DUKPT_DES_KEY_DATA1;
            int index = PinpadConst.DukptKeyIndex.DUKPT_KEY_INDEX_0;

            DukptEncryptObj dukptEncryptObj = new DukptEncryptObj(index, type, oper, alg, data);
            Bundle bundle = pinpad.dukptCalcDes(dukptEncryptObj);
            encrypt = bundle.getString(dukptEncryptObj.DUKPT_DATA);
            showResult(textView, "dukpt encrypt:" + encrypt);
            showResult(textView, "ksn:" + bundle.getString(dukptEncryptObj.DUKPT_KSN));

        } catch (RemoteException e) {

        }
    }

    private void getRandom() {
        try {
            byte[] random = pinpad.getRandom();
        } catch (RemoteException e) {

        }
    }

    private void dukptInit() {
        try {


            String key = "C1D0F8FB4958670DBA40AB1F3752EF0D";
            String ksn = "FFFF9876543210" + "000000";

            pinpad.setKeyAlgorithm(PinpadConst.KeyAlgorithm.DUKPT);

            DukptObj dukptObj = new DukptObj(key, ksn, PinpadConst.DukptKeyType.DUKPT_IPEK_PLAINTEXT, PinpadConst.DukptKeyIndex.DUKPT_KEY_INDEX_0);
            int ret = pinpad.dukptKeyLoad(dukptObj);


            showResult(textView, "Dukpt init:" + ret);
        } catch (RemoteException e) {
            showResult(textView, e.getMessage());
        }
    }

    private void inputPin() {

        Bundle bundle = new Bundle();
        bundle.putString(PinpadConst.PinpadShow.TITLE_HEAD_CONTENT, "Please input the online pin");
        try {
            pinpad.inputOnlinePin(bundle, new int[]{4, 6}, 300, "6225751180527297", 0, PinpadConst.PinAlgorithmMode.ISO9564FMT1, new AidlPinPadInputListener.Stub() {
                @Override
                public void onConfirm(byte[] data, boolean noPin, String ksn) throws RemoteException {
                    StringBuilder builder = new StringBuilder();
                    builder.append("Is Pin input:" + (noPin == true ? "No" : "Yes"));
                    builder.append("\npinBlock:" + HexUtil.bytesToHexString(data));
                    builder.append("\nksn:" + ksn);
                    showResult(textView, builder.toString());
                }


                @Override
                public void onSendKey(int keyCode) throws RemoteException {
                }

                @Override
                public void onCancel() throws RemoteException {
                    showResult(textView, "inputOnlinePin: onCancel");
                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    showResult(textView, "inputOnlinePin:" + "onError:" + errorCode);

                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, "inputOnlinePin:" + "RemoteException");
        }

    }

    @Override
    protected void setButtonName() {

    }


}