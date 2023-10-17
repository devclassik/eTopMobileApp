package com.gbikna.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import com.gbikna.sample.MyApplication;
import com.gbikna.sample.utils.AppLog;
import com.horizonpay.smartpossdk.aidl.pinpad.AidlPinPadInputListener;
import com.horizonpay.smartpossdk.aidl.pinpad.IAidlPinpad;
import com.horizonpay.smartpossdk.data.PinpadConst;
import com.gbikna.sample.R;
import com.gbikna.sample.utils.HexUtil;

import com.horizonpay.utils.FormatUtils;

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
public class PinPad3DesActivity extends BaseActivity {

    private static final String TAG = "PinPad3DesActivity";
    private static final int KEK_KEY_INDEX = 0;
    private static final int MASTER_KEY_INDEX = 0;
    private static final int WORK_KEY_INDEX = 0;
    IAidlPinpad innerpinpad;
    private boolean isSupport;
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinpad_3des);
        ButterKnife.bind(this);
        setButtonName();
        try {
            innerpinpad = MyApplication.getINSTANCE().getDevice().getPinpad(true);
            isSupport = innerpinpad.isSupport();
            innerpinpad.setKeyAlgorithm(PinpadConst.KeyAlgorithm.DES);
        } catch (RemoteException e) {
            showResult(textView, e.getMessage());
        }
    }

    @OnClick({R.id.loadmasterkey, R.id.loadclearmasterkey, R.id.loadworkkey, R.id.inputPin, R.id.calcMac, R.id.inputoffPin})
    public void onClick(View view) {
        if (!isSupport) {
            showResult(textView, getString(R.string.err_not_support_api));
        }

        switch (view.getId()) {
            case R.id.loadmasterkey:
                loadMasterKey();
                break;
            case R.id.loadclearmasterkey:
                loadClearMasterKey();
                break;
            case R.id.loadworkkey:
                loadWorkKey();
                break;
            case R.id.inputPin:
                inputPin();
                break;
            case R.id.inputoffPin:
                inputOfflinePin();
                break;
            case R.id.calcMac:
                calcMac();
                break;
            default:
                break;
        }
    }

    private void loadClearMasterKey() {
        try {
            //MK: 4B9F30D51BCB6A594B9F30D51BCB6A59
            byte[] masterKey = HexUtil.hexStringToByte("31313131313131313131313131313131");
            boolean result = innerpinpad.injectClearTMK(MASTER_KEY_INDEX, masterKey, new byte[4]);
            if (result) {
                showResult(textView, "loadClearMasterKey [31313131313131313131313131313131] success");
            } else {
                showResult(textView, "loadClearMasterKey [31313131313131313131313131313131] failed");
            }

        } catch (RemoteException e) {
            AppLog.d(TAG,  e.getMessage());
        }
    }

    private void loadMasterKey() {
        try {
            // KEK: FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
            // MK:  31313131313131313131313131313131
            byte[] masterKey = HexUtil.hexStringToByte("E168332B6C49D3C7E168332B6C49D3C7");
            boolean result = innerpinpad.injectSecureTMK(KEK_KEY_INDEX, MASTER_KEY_INDEX, masterKey, new byte[4]);
            if (result) {
                showResult(textView, "injectSecureTMK [31313131313131313131313131313131] success");
            } else {
                showResult(textView, "injectSecureTMK [31313131313131313131313131313131] failed");
            }

        } catch (RemoteException e) {
            AppLog.d(TAG,  e.getMessage());
        }
    }

    private void loadWorkKey() {
        //Pin key: 35353535353535353535353535353535
        try {
            //Encrypted PIN key
            byte[] pinKey = HexUtil.hexStringToByte("D0FB24EA73F599C1D0FB24EA73F599C1");
            byte[] kcv = HexUtil.hexStringToByte("D2DB51F1");
            boolean result = innerpinpad.injectWorkKey(WORK_KEY_INDEX, PinpadConst.PinPadKeyType.TPINK, pinKey, kcv);
            if (result) {
                showResult(textView, "load pin key success");
            } else {
                showResult(textView, "load pin key failed");
            }
        } catch (RemoteException e) {
            AppLog.d(TAG,  e.getMessage());
        }
        //Mac key: 33333333333333333333333333333333
        try {
            //Encrypted Mac key
            byte[] mackey = HexUtil.hexStringToByte("4BF6E91B1E3A9D814BF6E91B1E3A9D81");
            byte[] kcv = HexUtil.hexStringToByte("ADC67D84");
            boolean result = innerpinpad.injectWorkKey(WORK_KEY_INDEX, PinpadConst.PinPadKeyType.TMACK, mackey, kcv);
            if (result) {
                showResult(textView, "load mac key success");
            } else {
                showResult(textView, "load mac key failed");
            }
        } catch (RemoteException e) {
            AppLog.d(TAG,  e.getMessage());
        }
        //Data key: 37373737373737373737373737373737
        try {
            //plain data key
            byte[] datakey = HexUtil.hexStringToByte("16B2CCB944DA2CE916B2CCB944DA2CE9");
            byte[] kcv = new byte[4];
            boolean result = innerpinpad.injectWorkKey(WORK_KEY_INDEX, PinpadConst.PinPadKeyType.TDATAK, datakey, kcv);
            if (result) {
                showResult(textView, "load td key success");
            } else {
                showResult(textView, "load td key failed");
            }
        } catch (RemoteException e) {
            AppLog.d(TAG,  e.getMessage());
        }

    }


    private void inputPin() {
        try {
            innerpinpad.inputOnlinePin(new Bundle(), new int[]{4, 6}, 10, "1234567890123456", 0, PinpadConst.PinAlgorithmMode.ISO9564FMT1, new AidlPinPadInputListener.Stub() {
                @Override
                public void onConfirm(byte[] data, boolean noPin, String s) throws RemoteException {

                    String builder = "Is Pin input:" + (noPin ? "No" : "Yes") +
                            "\npinBlock:" + HexUtil.bytesToHexString(data);
                    showResult(textView, builder);
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

    private void inputOfflinePin() {
        try {
            innerpinpad.inputOfflinePin(new Bundle(), new int[]{4, 6}, 15, new AidlPinPadInputListener.Stub() {
                @Override
                public void onConfirm(byte[] data, boolean noPin, String s) throws RemoteException {


                    byte[] formatedData = FormatUtils.appendArray(data, (data.length + 7) / 8 * 8, true, (byte) 0x00);


                    String builder = "Is Pin input:" + (noPin ? "No" : "Yes") +
                            "\npinBlock:" + HexUtil.bytesToHexString(data) +
                            "\nen pin:" + HexUtil.bytesToHexString(formatedData);
                    showResult(textView, builder);
                }

                @Override
                public void onSendKey(int keyCode) throws RemoteException {
                }

                @Override
                public void onCancel() throws RemoteException {
                    showResult(textView, "inputOfflinePin: onCancel");
                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    showResult(textView, "inputOfflinePin:" + "onError:" + errorCode);

                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, "inputOfflinePin:" + "RemoteException");
        }
    }

    private void calcMac() {
        byte[] data = HexUtil.hexStringToByte("00000000005010016222620910029130840241205100100367FD3414057DB801BE18A309A544C5174CC777525974CBD467BCC56EA16629F3B016488A6C314921485C75F57066D4682FEDC1F910C5C8136A201279B590898B40D7098461D345168810CCFEBC61204B3E6F364A95175EF54C7EBAAEC2A6AEE44D9783747124D313B78A3F754C5ECC611533C4957377DD2067DF927C80461C4E4C20A8A4CC57EF1CCE2BC1AEEA442431256F66A25AB855912BA82FB8AD308F0EDE358CDDDEA63C95401B8335C8689E5735E0FB96733426FD71A7248E140A95CB4B4313AC0DBDA1E70EA8800000000000");
        byte[] mac = new byte[0];
        try {
            mac = innerpinpad.calcMac(WORK_KEY_INDEX, PinpadConst.MacType.TYPE_CUP_ECB, data, new byte[16]);
            //System.out.println("Random Data:"+ HexUtil.bytesToHexString(innerpinpad.getRandom()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        showResult(textView, "Calc mac:" + HexUtil.bcd2Str(mac));

    }


    @Override
    protected void setButtonName() {

    }


}