package com.gbikna.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.TextView;

import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.utils.AppLog;
import com.horizonpay.smartpossdk.aidl.cardreader.IAidlCardReader;
import com.horizonpay.smartpossdk.aidl.emv.AidlCheckCardListener;
import com.horizonpay.smartpossdk.aidl.magcard.TrackData;


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
public class IcCardActivity extends BaseActivity {
    private static final String TAG = "IcCardActivity";
    IAidlCardReader cardreader;
    private boolean isSupport;
    private CardReadMode mCardReadMode = CardReadMode.MANUAL;

    private enum CardReadMode {
        MANUAL,
        SWIPE,
        FALLBACK_SWIPE,
        CONTACT,
        CONTACTLESS,
        CONTACTLESS_MSD,
    }

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
        if (isSupport) {
            showResult(textView, getString(R.string.msg_icorrfid));
            searchIcCard();
        } else {
            showResult(textView, getString(R.string.err_not_support_api));
        }

    }


    private void searchIcCard() {
        try {
            cardreader.searchCard(false, true, true, 100, new AidlCheckCardListener.Stub() {
                @Override
                public void onFindMagCard(TrackData trackData) throws RemoteException {
                    AppLog.d(TAG,"onFindMagCard: ");
                }

                @Override
                public void onSwipeCardFail() throws RemoteException {
                    AppLog.d(TAG,"onSwipeCardFail: ");
                }

                @Override
                public void onFindICCard() throws RemoteException {
                    mCardReadMode = CardReadMode.CONTACT;
                    showResult(textView, "Find Contacted Card");

                }

                @Override
                public void onFindRFCard(int i) throws RemoteException {
                    showResult(textView, "Find Contactless Card");
                }

                @Override
                public void onTimeout() throws RemoteException {

                }

                @Override
                public void onCancelled() throws RemoteException {

                }

                @Override
                public void onError(int i) throws RemoteException {

                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.toString());
        }

    }

    @Override
    protected void setButtonName() {
        button.setText(getString(R.string.menu_ic_card_reader));
    }


}