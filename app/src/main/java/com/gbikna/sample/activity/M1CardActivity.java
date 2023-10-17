package com.gbikna.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.TextView;
import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.utils.AppLog;
import com.gbikna.sample.utils.HexUtil;
import com.gbikna.sample.utils.SweetDialogUtil;
import com.horizonpay.smartpossdk.aidl.cardreader.IAidlCardReader;
import com.horizonpay.smartpossdk.aidl.emv.AidlCheckCardListener;
import com.horizonpay.smartpossdk.aidl.m1card.IAidlM1Card;
import com.horizonpay.smartpossdk.aidl.magcard.TrackData;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.horizonpay.smartpossdk.data.MifareConst;


public class M1CardActivity extends BaseActivity {
    private static final String TAG = "M1CardActivity";
    IAidlCardReader cardreader;
    IAidlM1Card m1card;
    private boolean isSupport;
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
            m1card = MyApplication.getINSTANCE().getDevice().getM1Card();
            isSupport = m1card.isSupport();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.button})
    public void onClick() {
        if (!isSupport) {
            showResult(textView, getString(R.string.err_not_support_api));
        } else {
            showResult(textView, getString(R.string.msg_rfcard));
            searchCard();
        }
    }

    private void searchCard() {
        try {
            cardreader.searchCard(false, false, true, 30 * 1000, checkCardListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private AidlCheckCardListener.Stub checkCardListener = new AidlCheckCardListener.Stub() {
        @Override
        public void onFindMagCard(TrackData data) throws RemoteException {
            AppLog.d(TAG,"onFindMagCard: ");
        }

        @Override
        public void onSwipeCardFail() throws RemoteException {
            AppLog.d(TAG,"onSwipeCardFail: ");
        }

        @Override
        public void onFindICCard() throws RemoteException {
            showResult(textView, "Find Contact IC Card");

        }

        @Override
        public void onFindRFCard(int ctlsCardType) throws RemoteException {
            showResult(textView, "Find Contactless IC Card");
            m1Card();
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


    private void m1Card() {
        try {
            byte[] key = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] uid = new byte[64];

            StringBuilder builder = new StringBuilder();
            if (m1card == null) {
                SweetDialogUtil.showError(M1CardActivity.this, getString(R.string.msg_readfail_retry));
                return;
            }

            int ret = m1card.authority(MifareConst.KeyType.KEYTYPE_A, 0, key, uid);
            if (ret != 0) {
                showResult(textView, "M1 card authority:" + ret);
                return;
            }

            int len = 16;
            byte[] buf = new byte[len];
            byte[] value = new byte[]{(byte) 0x0A};
            byte[] write = new byte[]{(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};

            Arrays.fill(write, (byte) 0x00);

            write[4] = (byte) 0xff;
            write[5] = (byte) 0xff;
            write[6] = (byte) 0xff;
            write[7] = (byte) 0xff;
            write[12] = ~0;
            write[13] = 0;
            write[14] = ~0;
            write[15] = 0;

            builder.append("M1Card Test\n");

            m1card.writeBlock(1, write);
            m1card.readBlock(0, buf);
            builder.append("readBlock0:" + HexUtil.bytesToHexString(buf) + "\n");

            m1card.readBlock(1, buf);
            builder.append("readBlock1:" + HexUtil.bytesToHexString(buf) + "\n");

            m1card.operateBlock(MifareConst.OperType.INCREMENT, 1, value, 0);
            m1card.readBlock(1, buf);
            builder.append("=====operateBlock INCREMENT=====\n");
            builder.append("readBlock1:" + HexUtil.bytesToHexString(buf) + "\n");


            m1card.operateBlock(MifareConst.OperType.DECREMENT, 1, value, 0);
            m1card.readBlock(1, buf);
            builder.append("=====operateBlock DECREMENT=====\n");
            builder.append("readBlock1:" + HexUtil.bytesToHexString(buf) + "\n");

            m1card.operateBlock(MifareConst.OperType.BACKUP, 0, null, 1);
            m1card.readBlock(1, buf);
            builder.append("=====operateBlock BACKUP=====\n");
            builder.append("readBlock1:" + HexUtil.bytesToHexString(buf) + "\n");

            showResult(textView, builder.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setButtonName() {
        button.setText(getString(R.string.menu_m1card));
    }


}