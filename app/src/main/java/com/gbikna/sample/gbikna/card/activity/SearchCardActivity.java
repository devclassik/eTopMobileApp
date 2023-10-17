package com.gbikna.sample.gbikna.card.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;


import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.bean.PaymentCardDetectMode;
import com.gbikna.sample.dialog.AppSelectDialog;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.gbikna.util.utilities.Utilities;
import com.gbikna.sample.gbikna.util.utilities.Utils;
import com.gbikna.sample.etop.Dashboard;
import com.gbikna.sample.pay.CardReadMode;
import com.gbikna.sample.pay.CreditCard;
import com.gbikna.sample.pay.OnlineRespEntitiy;
import com.gbikna.sample.pay.PayProcessor;
import com.gbikna.sample.pay.TransactionResultCode;
import com.horizonpay.smartpossdk.aidl.emv.CandidateAID;
import com.horizonpay.smartpossdk.aidl.emv.IAidlEmvL2;

import java.text.DecimalFormat;


import java.util.List;

import butterknife.ButterKnife;


public class SearchCardActivity extends Activity {
    private static final String TAG = Utils.TAGPUBLIC + SearchCardActivity.class.getSimpleName();

    private TextView mTextAmount, tvResult;
    private String mAmount;
    ProgressDialog dialog;
    static int canceller = 0;
    private String printInAmountFormat(String number)
    {
        double amount = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String fin = "";
        Log.i(TAG, "OLUBAYO LENGTH: " + formatter.format(amount).length());
        if(formatter.format(amount).length() < 4)
            fin = ProfileParser.curabbreviation + " 0" + formatter.format(amount);
        else
            fin = ProfileParser.curabbreviation + " " + formatter.format(amount);
        return fin;
    }

    private PayProcessor payProcessor;
    IAidlEmvL2 mEmvL2;
    private boolean isSupport;

    void sanitizeAmount()
    {
        Log.i(TAG, "AMOUNT TO USE: " + ProfileParser.totalAmount);
        if(ProfileParser.totalAmount.charAt(ProfileParser.totalAmount.length() - 2) == '.')
            ProfileParser.totalAmount = ProfileParser.totalAmount + "0";
        Log.i(TAG, "AMOUNT TO USE: " + ProfileParser.totalAmount);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_search_card);
        sanitizeAmount();
        ButterKnife.bind(this);
        tvResult = (TextView) findViewById(R.id.usage);
        Log.i(TAG, "NFC IS: " + ProfileParser.txnNumber);
        tvResult.setText("PLEASE INSERT CARD");
        mTextAmount = (TextView) findViewById(R.id.trad_amount);
        mAmount = printInAmountFormat(ProfileParser.totalAmount);
        mTextAmount.setText(mAmount);
        canceller = 0;

        try {
            mEmvL2 = MyApplication.getINSTANCE().getDevice().getEmvL2();
            isSupport = mEmvL2.isSupport();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        payProcessor = new PayProcessor();

        searchCard();
    }

    private void searchCard() {
        long amount = 0;
        amount = Long.parseLong(ProfileParser.totalAmount.replace(".", ""));
        Log.i(TAG, "AMOUNT GOING: " + amount);
        payProcessor.payNew(amount, processorListener, this);
    }


    private PayProcessor.PayProcessorListener processorListener = new PayProcessor.PayProcessorListener() {

        @Override
        public void onRetry(int retryFlag) {
            if (retryFlag == 0) {
                tvResult.setText("PLEASE INSERT CARD");
            } else {
                tvResult.setText("PLEASE INSERT CARD 2");
            }
        }

        @Override
        public void onCardDetected(final CardReadMode cardReadMode, CreditCard creditCard) {
            Log.i(TAG, ">>>onCardDetected" + cardReadMode);
            //tvResult.setText("PROCESSING...");
        }


        @Override
        public CandidateAID confirmApplicationSelection(List<CandidateAID> candidateList) {
            int selectedIndex = 0;
            try {
                selectedIndex = new AppSelectDialog(SearchCardActivity.this, candidateList).call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i(TAG, ">>>confirmApplicationSelection:" + candidateList.get(selectedIndex).getAid());
            return candidateList.get(selectedIndex);
        }

        @Override
        public OnlineRespEntitiy onPerformOnlineProcessing(CreditCard creditCard) {
            Log.i(TAG, ">>>onPerformOnlineProcessing:");

            Log.i(TAG, String.valueOf(PaymentCardDetectMode.ICC));
            Log.i(TAG, creditCard.getCardNumber());
            Log.i(TAG, creditCard.getExpireDate());
            Log.i(TAG, creditCard.getCardSequenceNumber());
            Log.i(TAG, creditCard.getHolderName());
            if (creditCard.getEmvData() != null) {
                Log.i(TAG, creditCard.getEmvData().getTrack1());
                Log.i(TAG, creditCard.getEmvData().getTrack2());
                Log.i(TAG, creditCard.getEmvData().getIccData());
            }
            return null;
        }

        @Override
        public void onCompleted(TransactionResultCode result, CreditCard creditCard) {
            final StringBuilder resultText = new StringBuilder();
            switch (result) {
                case APPROVED_BY_OFFLINE:
                    resultText.append(TransactionResultCode.APPROVED_BY_OFFLINE.toString());
                    updateICCardData();
                    break;
                case APPROVED_BY_ONLINE:
                    resultText.append(TransactionResultCode.APPROVED_BY_ONLINE.toString());
                    updateICCardData();
                    break;
                case DECLINED_BY_OFFLINE:
                    resultText.append(TransactionResultCode.DECLINED_BY_OFFLINE.toString());
                    goHome();
                    break;
                case DECLINED_BY_ONLINE:
                    resultText.append(TransactionResultCode.DECLINED_BY_ONLINE.toString());
                    goHome();
                    break;
                case DECLINED_BY_TERMINAL_NEED_REVERSE:
                    Log.i(TAG, "TERMINAL CANCELED TXN");
                    canceller = 1;
                    resultText.append(TransactionResultCode.DECLINED_BY_TERMINAL_NEED_REVERSE.toString());
                    goHome();
                    break;
                case ERROR_TRANSCATION_CANCEL:
                    Log.i(TAG, "USER CANCELED TXN");
                    canceller = 1;
                    resultText.append(TransactionResultCode.ERROR_TRANSCATION_CANCEL.toString());
                    goHome();
                    break;
                case ERROR_UNKNOWN:
                    Log.i(TAG, "UNKNOWN ERROR");
                    canceller = 1;
                    resultText.append(TransactionResultCode.ERROR_UNKNOWN.toString());
                    goHome();
                    break;
            }
            Log.i(TAG, ">>>onCompleted:" + resultText);
        }
    };

    private void goHome()
    {
        Intent intent = new Intent(SearchCardActivity.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    String getValueByTag(String tag)
    {
        String value = "";
        try {
            value = MyApplication.getINSTANCE().getDevice().getEmvL2().getTagValue(tag);
            Log.i(TAG, tag + ": TAG VALUE: " + value);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return value;
    }

    void getTags()
    {
        ProfileParser.icc82 = getValueByTag("82");
        ProfileParser.icc9F36 = getValueByTag("9F36");
        ProfileParser.icc9F27 = getValueByTag("9F27");
        ProfileParser.icc9F26 = getValueByTag("9F26");
        ProfileParser.icc9F34 = getValueByTag("9F34");
        ProfileParser.icc9F06 = getValueByTag("9F06");
        ProfileParser.icc9F10 = getValueByTag("9F10");
        ProfileParser.icc95 = getValueByTag("95");
        ProfileParser.icc9F37 = getValueByTag("9F37");
    }

    private void updateICCardData() {
        getTags();
        String track2 = null;
        try {
            track2 = MyApplication.getINSTANCE().getDevice().getEmvL2().getTagValue("57");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(track2) && track2.endsWith("F")) {
            track2 = track2.substring(0, track2.length() - 1);
        }
        ProfileParser.field35 = track2;

        String exp = null;
        try {
            exp = MyApplication.getINSTANCE().getDevice().getEmvL2().getTagValue("5F24");
            ProfileParser.field14 = exp;
            ProfileParser.field14 = ProfileParser.field14.substring(0, 4);
            ProfileParser.field22 = "051";
            ProfileParser.field123 = "510101511344101";
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        String snum = null;
        try {
            snum = MyApplication.getINSTANCE().getDevice().getEmvL2().getTagValue("5F34");
            ProfileParser.field23  = snum;
            Log.i(TAG, "FIELD 23: " + ProfileParser.field23);
            if(ProfileParser.field23.length() == 2)
                ProfileParser.field23 = "0" + ProfileParser.field23;
            ProfileParser.field25 = "00";
            ProfileParser.field26 = "12";
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        String pan = null;
        try {
            pan = MyApplication.getINSTANCE().getDevice().getEmvL2().getTagValue("5A");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (pan.endsWith("F")) {
            pan = pan.substring(0, pan.length() - 1);
        }

        ProfileParser.field2 = pan;
        ProfileParser.field4 = Utilities.getField4(ProfileParser.totalAmount);
        Log.i(TAG, ProfileParser.field2);
        Log.i(TAG, ProfileParser.field14);

        int dd = ProfileParser.field35.indexOf("D" + ProfileParser.field14);
        ProfileParser.field40 = ProfileParser.field35.substring(dd + 5, dd + 8);
        Log.i(TAG, "2. NEW LEN: " + ProfileParser.field40);

        try {
            ProfileParser.cardName = hexToAscii(MyApplication.getINSTANCE().getDevice().getEmvL2().getTagValue("5F20"));
            ProfileParser.cardType = hexToAscii(MyApplication.getINSTANCE().getDevice().getEmvL2().getTagValue("50"));
            ProfileParser.cardAid = MyApplication.getINSTANCE().getDevice().getEmvL2().getTagValue("9F06");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        ProfileParser.field32 = ProfileParser.field2.substring(0, 6);

        String[] arqcTLVTags = new String[]{
                "9F26",
                "9F27",
                "9F10",
                "9F37",
                "9F36",
                "95",
                "9A",
                "9C",
                "9F02",
                "5F2A",
                "82",
                "9F03",
                "5F34",
                "9F1A",
                "9F33",
                "9F34",
                "9F35",
                "9F41"
        };
        try {
            ProfileParser.field55 = mEmvL2.getTlvByTags(arqcTLVTags);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "FIELD 55: " + ProfileParser.field55);

        Log.i(TAG, "B FIELD 52: " + ProfileParser.field52);




        Intent intent = new Intent(SearchCardActivity.this, PacketProcessActivity.class);
        startActivity(intent);
        finish();
    }






    private static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");

        Intent intent = new Intent();
        intent.setClass(SearchCardActivity.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

}
