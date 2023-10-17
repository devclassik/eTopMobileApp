package com.gbikna.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;

import android.widget.Button;
import android.widget.TextView;

import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.pay.CardReadMode;
import com.gbikna.sample.utils.AppLog;
import com.gbikna.sample.utils.HexUtil;
import com.gbikna.sample.utils.SweetDialogUtil;
import com.horizonpay.smartpossdk.aidl.cardreader.IAidlCardReader;
import com.horizonpay.smartpossdk.aidl.cpucard.IAidlCpuCard;
import com.horizonpay.smartpossdk.aidl.cpucard.PApduEntity;
import com.horizonpay.smartpossdk.aidl.emv.AidlCheckCardListener;
import com.horizonpay.smartpossdk.aidl.magcard.TrackData;

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
public class CpuCardActivity extends BaseActivity {
    private final String LOG_TAG = CpuCardActivity.class.getSimpleName();
    IAidlCardReader cardreader;
    IAidlCpuCard cpucard;
    private boolean isSupport;
    private CardReadMode mCardReadMode = CardReadMode.MANUAL;
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

        try {
            cardreader = MyApplication.getINSTANCE().getDevice().getCardReader();
            cpucard = MyApplication.getINSTANCE().getDevice().getCpuCard();
            isSupport = cpucard.isSupport();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.button})
    public void onClick() {
        if (!isSupport) {
            ToastUtils.showShort(R.string.err_not_support_api);
            showResult(textView, getString(R.string.err_not_support_api));
        } else {
            showResult(textView, getString(R.string.msg_icorrfid));
            AppLog.d(LOG_TAG,"onClick: Satart search card >>>>>>");
            searchCard();
        }
    }

    private void searchCard() {
        try {
            cardreader.searchCard(false, true, true, 30 * 1000, checkCardListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private AidlCheckCardListener.Stub checkCardListener = new AidlCheckCardListener.Stub() {
        @Override
        public void onFindMagCard(TrackData data) throws RemoteException {
            AppLog.d(LOG_TAG,"onFindMagCard: ");
        }

        @Override
        public void onSwipeCardFail() throws RemoteException {
            AppLog.e(LOG_TAG,"onSwipeCardFail: ");
        }

        @Override
        public void onFindICCard() throws RemoteException {
            showResult(textView, "Find Contact IC Card");
            mCardReadMode = CardReadMode.CONTACT;
            exchangeApdu();
        }

        @Override
        public void onFindRFCard(int ctlsCardType) throws RemoteException {
            showResult(textView, "Find Contactless IC Card");
            mCardReadMode = CardReadMode.CONTACTLESS;
        }

        @Override
        public void onTimeout() throws RemoteException {
            showResult(textView, "onTimeout");
        }

        @Override
        public void onCancelled() throws RemoteException {
            showResult(textView, "onCancelled");
        }

        @Override
        public void onError(int errCode) throws RemoteException {
            showResult(textView, "onError " + errCode);
        }
    };


    private void exchangeApdu() throws RemoteException {
        try {
            String cmd = "00A40400";
            String data = "325041592E5359532E4444463031";
            byte le = 0x00;

            if (cpucard == null) {
                SweetDialogUtil.showError(CpuCardActivity.this, getString(R.string.msg_readfail_retry));
                return;
            }

            if (!cpucard.powerOn(mCardReadMode.ordinal(), 0, new byte[]{0x00, 0x00})) {
                SweetDialogUtil.showError(CpuCardActivity.this, getString(R.string.msg_readfail_retry));
                return;
            }


            byte[] cmdBytes = HexUtil.hexStringToByte(cmd);
            byte[] dataArray = HexUtil.hexStringToByte(data);
            byte[] tmp = new byte[256];

            System.arraycopy(dataArray, 0, tmp, 0, dataArray.length);

            PApduEntity apduEntity = new PApduEntity();

            apduEntity.setCla(cmdBytes[0]);
            apduEntity.setIns(cmdBytes[1]);
            apduEntity.setP1(cmdBytes[2]);
            apduEntity.setP2(cmdBytes[3]);
            apduEntity.setLc(dataArray.length);
            apduEntity.setLe(le);
            apduEntity.setDataIn(tmp);
            boolean ret = cpucard.exchangeApdu(apduEntity);
            cpucard.powerOff();

            if (ret == true) {
                if (apduEntity.getDataOutLen() > 0) {
                    showResult(textView, HexUtil.bytesToHexString(apduEntity.getDataOut()));
                } else {

                }
            } else {
                SweetDialogUtil.showError(CpuCardActivity.this, getString(R.string.msg_readfail_retry));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showResult(textView, e.toString());
        }
    }

    @Override
    protected void setButtonName() {
        button.setText(getString(R.string.menu_apdu));
    }


}