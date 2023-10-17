package com.gbikna.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;

import android.widget.Button;
import android.widget.TextView;

import com.gbikna.sample.MyApplication;
import com.gbikna.sample.utils.AppLog;
import com.horizonpay.smartpossdk.aidl.cardreader.IAidlCardReader;
import com.horizonpay.smartpossdk.aidl.emv.AidlCheckCardListener;
import com.horizonpay.smartpossdk.aidl.magcard.TrackData;
import com.gbikna.sample.R;

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
public class CardReaderActivity extends BaseActivity {
    private final String LOG_TAG = CardReaderActivity.class.getSimpleName();
    IAidlCardReader cardreader;
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
            isSupport = cardreader.isSupport();
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
            showResult(textView, getString(R.string.please_use_card));

            AppLog.d(LOG_TAG,"Satart search card >>>>>>");
            searchCard();
        }
    }

    long startTick;

    private void searchCard() {
        button.setEnabled(false);
        AppLog.d(LOG_TAG,"searchCard:");
        try {
            startTick = System.currentTimeMillis();
            cardreader.searchCard(true, true, true, 30, checkCardListener); //Time-out 30 second
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private AidlCheckCardListener.Stub checkCardListener = new AidlCheckCardListener.Stub() {
        @Override
        public void onFindMagCard(TrackData data) throws RemoteException {
            showResult(textView, "time = " + (System.currentTimeMillis() - startTick) + "ms");
            String builder = "Card: " + data.getCardNo() +
                    "\nTk1: " + data.getTrack1Data() +
                    "\nTk2: " + data.getTrack2Data() +
                    "\nTk3: " + data.getTrack3Data() +
                    "\nExpiryDate: " + data.getExpiryDate() +
                    "\nServiceCode: " + data.getServiceCode() +
                    "\nHolder: " + data.getCardholderName() +
                    "\nIs IC card: " + (data.isIccCard() ? "Yes" : "No");
            showResult(textView, builder);
            stopSearch();
        }

        @Override
        public void onSwipeCardFail() throws RemoteException {
            showResult(textView, "Swip card Failed");
        }

        @Override
        public void onFindICCard() throws RemoteException {
            showResult(textView, "Find Contact IC Card");
            showResult(textView, "time = " + (System.currentTimeMillis() - startTick) + "ms");
            stopSearch();
        }

        @Override
        public void onFindRFCard(int ctlsCardType) throws RemoteException {
            showResult(textView, "Find Contactless IC Card");
            showResult(textView, "time = " + (System.currentTimeMillis() - startTick) + "ms");
            stopSearch();
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


    @Override
    protected void setButtonName() {
        button.setText(getString(R.string.menu_card_reader));
    }

    private void stopSearch() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setEnabled(true);
                try {
                    cardreader.cancelSearchCard();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}