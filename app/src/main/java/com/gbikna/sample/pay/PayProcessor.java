package com.gbikna.sample.pay;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;


import com.gbikna.sample.MyApplication;
import com.gbikna.sample.gbikna.card.activity.SearchCardActivity;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.gbikna.util.utilities.Utils;
import com.gbikna.sample.etop.Dashboard;
import com.gbikna.sample.utils.AppLog;
import com.gbikna.sample.utils.CardUtil;
import com.gbikna.sample.utils.EmvUtil;
import com.gbikna.sample.utils.HexUtil;
import com.gbikna.sample.utils.TlvData;
import com.gbikna.sample.utils.TlvDataList;
import com.horizonpay.smartpossdk.aidl.IAidlDevice;
import com.horizonpay.smartpossdk.aidl.cardreader.IAidlCardReader;
import com.horizonpay.smartpossdk.aidl.emv.AidlCheckCardListener;
import com.horizonpay.smartpossdk.aidl.emv.AidlEmvStartListener;
import com.horizonpay.smartpossdk.aidl.emv.CandidateAID;
import com.horizonpay.smartpossdk.aidl.emv.EmvFinalSelectData;
import com.horizonpay.smartpossdk.aidl.emv.EmvTags;
import com.horizonpay.smartpossdk.aidl.emv.EmvTransData;
import com.horizonpay.smartpossdk.aidl.emv.EmvTransOutputData;
import com.horizonpay.smartpossdk.aidl.emv.IAidlEmvL2;
import com.horizonpay.smartpossdk.aidl.magcard.TrackData;
import com.horizonpay.smartpossdk.aidl.pinpad.AidlPinPadInputListener;
import com.horizonpay.smartpossdk.aidl.pinpad.IAidlPinpad;
import com.horizonpay.smartpossdk.data.EmvConstant;
import com.horizonpay.smartpossdk.data.PinpadConst;

import java.util.List;

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
public class PayProcessor {
    private static final String TAG = Utils.TAGPUBLIC + PayProcessor.class.getSimpleName();
    public interface PayProcessorListener {
        void onRetry(int retryFlag);

        void onCardDetected(CardReadMode cardReadMode, CreditCard creditCard);

        CandidateAID confirmApplicationSelection(List<CandidateAID> candidateList);

        OnlineRespEntitiy onPerformOnlineProcessing(CreditCard creditCard);

        void onCompleted(TransactionResultCode result, CreditCard creditCard);

    }

    private IAidlCardReader mCardReader;
    private IAidlEmvL2 mEmvL2;
    private IAidlPinpad mPinPad;
    private PayProcessorListener mListener;
    private long mAount;
    private CardReadMode mCardReadMode = CardReadMode.MANUAL;
    private OnlineRespEntitiy mOnlineRespEntitiy;
    private long startTick;
    private final String LOG_TAG = PayProcessor.class.getSimpleName();
    private Context mContext;
    private Context mContext2;

    public PayProcessor(Context context) {
        mContext = context;
    }

    public PayProcessor() {
        try {
            IAidlDevice device = MyApplication.getINSTANCE().getDevice();
            if (device != null) {
                mCardReader = device.getCardReader();
                mEmvL2 = device.getEmvL2();
                mPinPad = device.getPinpad(false);
            } else {
                throw new RuntimeException("device is null ");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void initEmvParam() {
        try {
            mEmvL2.setTermConfig(EmvUtil.getInitTermConfig());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void init() {
        mOnlineRespEntitiy = null;
        mCardReadMode = CardReadMode.MANUAL;

    }

    public void pay(long amount, PayProcessorListener listener) {
        init();
        mListener = listener;
        mAount = amount;
        try {
            mListener.onRetry(0);
            startTick = System.currentTimeMillis();
            mCardReader.searchCard(true, true, true, 100, checkCardListener);
        } catch (RemoteException e) {
            mListener.onCompleted(TransactionResultCode.ERROR_UNKNOWN, new CreditCard());
            e.printStackTrace();
        }
    }

    public void payNew(long amount, PayProcessorListener listener, SearchCardActivity consumeSuccessActivity) {
        init();
        mContext2 = consumeSuccessActivity;
        mListener = listener;
        mAount = amount;
        try {
            mListener.onRetry(0);
            startTick = System.currentTimeMillis();
            mCardReader.searchCard(true, true, true, 100, checkCardListener);
        } catch (RemoteException e) {
            mListener.onCompleted(TransactionResultCode.ERROR_UNKNOWN, new CreditCard());
            e.printStackTrace();
        }
    }

    private AidlCheckCardListener.Stub checkCardListener = new AidlCheckCardListener.Stub() {
        @Override
        public void onFindMagCard(TrackData data) throws RemoteException {
            Log.i(TAG,"card NO:" + data.getCardNo());
            mCardReadMode = CardReadMode.SWIPE;
            CreditCard creditCard = new CreditCard();
            creditCard.setCardReadMode(CardReadMode.SWIPE);
            creditCard.setCardNumber(data.getCardNo());
            creditCard.setExpireDate(data.getExpiryDate());
            creditCard.setServiceCode(data.getServiceCode());
            CreditCard.MagData magData = new CreditCard.MagData(data.getTrack1Data(), data.getTrack2Data());
            creditCard.setMagData(magData);
            mListener.onCardDetected(CardReadMode.SWIPE, creditCard);

            StringBuilder builder = new StringBuilder();
            builder.append("Card: " + data.getCardNo());
            builder.append("\nTk1: " + data.getTrack1Data());
            builder.append("\nTk2: " + data.getTrack2Data());
            builder.append("\nTk3: " + data.getTrack3Data());
            builder.append("\nExpiryDate: " + data.getExpiryDate());
            builder.append("\nCardholderName: " + data.getCardholderName());
            Log.i(TAG,"onFindMagCard: \n" + builder.toString());
            stopSearch();
            inputPIN(creditCard.getCardNumber());
            mOnlineRespEntitiy = mListener.onPerformOnlineProcessing(creditCard);
//            TransactionResultCode transactionResultCode = TransactionResultCode.DECLINED_BY_OFFLINE;
            TransactionResultCode transactionResultCode = TransactionResultCode.APPROVED_BY_ONLINE;
            if (mOnlineRespEntitiy != null && "00".equals(mOnlineRespEntitiy.respCode)) {
                transactionResultCode = TransactionResultCode.APPROVED_BY_ONLINE;
            } else {
                transactionResultCode = TransactionResultCode.DECLINED_BY_OFFLINE;

            }

            mListener.onCompleted(transactionResultCode, creditCard);

        }

        @Override
        public void onSwipeCardFail() throws RemoteException {
            TransactionResultCode transactionResultCode = TransactionResultCode.ERROR_UNKNOWN;
            mListener.onCompleted(transactionResultCode, new CreditCard());
        }

        @Override
        public void onFindICCard() throws RemoteException {
            mCardReadMode = CardReadMode.CONTACT;
            Log.i(TAG,"onFindICCard: 0");
            Log.i(TAG,"time = " + (System.currentTimeMillis() - startTick) + "ms");
            CreditCard creditCard = new CreditCard();
            Log.i(TAG,"onFindICCard: 2");
            creditCard.setCardReadMode(CardReadMode.CONTACT);
            Log.i(TAG,"onFindICCard: 3");
            mListener.onCardDetected(CardReadMode.CONTACT, creditCard);
            Log.i(TAG,"onFindICCard: 4");
            stopSearch();
            Log.i(TAG,"onFindICCard: 5");
            startEMVProcess();
            Log.i(TAG,"onFindICCard: 6");
        }

        @Override
        public void onFindRFCard(int ctlsCardType) throws RemoteException {
            mCardReadMode = CardReadMode.CONTACTLESS;
            Log.i(TAG,"onFindRFCard: ");
            Log.i(TAG,"time = " + (System.currentTimeMillis() - startTick) + "ms");
            CreditCard creditCard = new CreditCard();
            creditCard.setCardReadMode(CardReadMode.CONTACTLESS);
            mListener.onCardDetected(CardReadMode.CONTACTLESS, creditCard);
            stopSearch();
            startEMVProcess();
        }

        @Override
        public void onTimeout() throws RemoteException {
            Log.i(TAG,"earchCard = onTimeout ");
            TransactionResultCode transactionResultCode = TransactionResultCode.ERROR_UNKNOWN;
            mListener.onCompleted(transactionResultCode, new CreditCard());

            Intent intent = new Intent(mContext2, Dashboard.class);
            mContext2.startActivity(intent);
        }

        @Override
        public void onCancelled() throws RemoteException {
            Log.i(TAG,"earchCard = onCancelled ");
            TransactionResultCode transactionResultCode = TransactionResultCode.ERROR_UNKNOWN;
            mListener.onCompleted(transactionResultCode, new CreditCard());

            Intent intent = new Intent(mContext2, Dashboard.class);
            mContext2.startActivity(intent);
        }

        @Override
        public void onError(int errCode) throws RemoteException {
            AppLog.e(LOG_TAG,"earchCard = onError ");
            TransactionResultCode transactionResultCode = TransactionResultCode.ERROR_UNKNOWN;
            mListener.onCompleted(transactionResultCode, new CreditCard());

            Intent intent = new Intent(mContext2, Dashboard.class);
            mContext2.startActivity(intent);
        }
    };

    private AidlEmvStartListener.Stub emvStartListener = new AidlEmvStartListener.Stub() {
        @Override
        public void onRequestAmount() throws RemoteException {
            Log.i(TAG,"emvStartListener onRequestAmount: ");
            mEmvL2.requestAmountResp(String.valueOf(mAount));
        }

        @Override
        public void onRequestAidSelect(int times, List<CandidateAID> aids) throws RemoteException {
//            CandidateAID confirmApplicationSelection = mListener.confirmApplicationSelection(aids);
//            int index = aids.indexOf(confirmApplicationSelection);
//            mEmvL2.requestAidSelectResp(index);
            Log.i(TAG,"onRequestAidSelect: ");
            selApp(aids);
        }

        @Override
        public void onFinalSelectAid(EmvFinalSelectData emvFinalSelectData) throws RemoteException {
            Log.i(TAG,"onFinalSelectAid: " + emvFinalSelectData.getAid());
            mEmvL2.requestFinalSelectAidResp("");
        }

        @Override
        public void onConfirmCardNo(final String cardNo) throws RemoteException {
            Log.i(TAG,"onConfirmCardNo: " + cardNo);
            Log.i(TAG,"time = " + (System.currentTimeMillis() - startTick) + "ms");
            mEmvL2.confirmCardNoResp(true);
        }


        @Override
        public void onRequestPin(boolean isOnlinePIN, int leftTimes) throws RemoteException {
            Log.i(TAG,"onCardHolderInputPin isOnlinePin:" + isOnlinePIN + "leftTimes: " + leftTimes);

            String PAN = mEmvL2.getTagValue("5A");
            if (PAN != null) {
                PAN = PAN.replace("F", "");
            }

            if (isOnlinePIN) {
                inputOnlinePIN(PAN);
            } else {
                Log.i(TAG,"onRequestPin: offline PIN");
                inputOfflinePIN(PAN, leftTimes);
            }

        }

        @Override
        public void onResquestOfflinePinDisp(int i) throws RemoteException {
            if (i == 0) {
                Log.i(TAG,"onResquestOfflinePinDisp: PIN OK !");
            } else {
                Log.i(TAG,"WRONG PIN --> " + i + "Chance Left");
            }
        }


        @Override
        public void onRequestOnline(EmvTransOutputData emvTransOutputData) throws RemoteException {
            CreditCard creditCard = getEmvCardInfo();
            Log.i(TAG,"onRequestOnline: true");
            Log.i(TAG,"time = " + (System.currentTimeMillis() - startTick) + "ms");

            //updateICCardData();

            /*if (BuildConfig.DEBUG == true) {
                Log.i(TAG,"onRequestOnline: Simulate Online process>>>>");
                onlineProc();
            } else {
                mOnlineRespEntitiy = mListener.onPerformOnlineProcessing(creditCard);
            }*/
            
            String respCode = "00";
            String iccData = "";
            if (mOnlineRespEntitiy != null) {
                respCode = mOnlineRespEntitiy.getRespCode();
                iccData = mOnlineRespEntitiy.getIccData();
            }
            Log.i(TAG,"resp Code: " + respCode);
            Log.i(TAG,"iccData: " + iccData);
            mEmvL2.requestOnlineResp(respCode, iccData);
        }

        @Override
        public void onFinish(final int emvResult, EmvTransOutputData emvTransOutputData) throws RemoteException {
            Log.i(TAG,"CallBack onFinish: ");
            Log.i(TAG,"time = " + (System.currentTimeMillis() - startTick) + "ms");
            emvFinish(emvResult, emvTransOutputData);
        }

        @Override
        public void onError(int errCode) throws RemoteException {
            AppLog.e(LOG_TAG,"onError: errcode:" + errCode);

            Intent intent = new Intent(mContext2, Dashboard.class);
            mContext2.startActivity(intent);
        }


    };

    public String getEmvRecordTLV() {
        final String[] standard_Tags = {
                "9f26",
                "9f27",
                "9f10",
                "9f37",
                "9f36",
                "95",
                "9a",
                "9c",
                "9f02",
                "5f2a",
                "9f1a",
                "82",
                "9f33",
                "9f34",
                "9f03",
                "84",
                "9F08",
                "9f09",
                "9f35",
                "9f1e",
                "9F53",
                "9f41",
                "9f63",
                "9F6E",
                "9F4C",
                "9F5D",
                "9B",
                "5F34",
                "50",
                "9F12",
                "91",
                "DF31",
                "8F"
        };
        try {
            return mEmvL2.getTlvByTags(standard_Tags);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    private CreditCard getEmvCardInfo() {
        CreditCard creditCard = new CreditCard();
        creditCard.setCardReadMode(mCardReadMode);

        try {
            String cardsn = mEmvL2.getTagValue(EmvTags.EMV_TAG_IC_PANSN);
            if (cardsn != null && !cardsn.isEmpty()) {
                creditCard.setCardSequenceNumber(cardsn);
            }

            String track2 = mEmvL2.getTagValue(EmvTags.EMV_TAG_IC_TRACK2DATA);
            if (track2 == null || track2.isEmpty()) {
                track2 = mEmvL2.getTagValue(EmvTags.M_TAG_IC_9F6B);
            }
            if (track2 != null && track2.length() > 20) {
                if (track2.endsWith("F") || track2.endsWith("f")) {
                    track2 = track2.substring(0, track2.length() - 1);
                }
                String formatTrack2 = track2.toUpperCase().replace('=', 'D');

                int idx = formatTrack2.indexOf('D');
                String expDate = track2.substring(idx + 1, idx + 5);

                creditCard.setExpireDate(expDate);

                String pan = track2.substring(0, idx);
                creditCard.setCardNumber(pan);
                CreditCard.EmvData emvData = new CreditCard.EmvData("", formatTrack2, getEmvRecordTLV());
                creditCard.setEmvData(emvData);

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return creditCard;
    }

    
    private String onlineProc() throws RemoteException {
        StringBuilder builder = new StringBuilder();
        Log.i(TAG,"onlineProc: ");
        String arqcTlv = mEmvL2.getTlvByTags(EmvUtil.arqcTLVTags);
        builder.append(mEmvL2.getTlvByTags(EmvUtil.arqcTLVTags));

        if (!TlvDataList.fromBinary(arqcTlv).contains("9F27")) {
            builder.append(TlvData.fromData("9F27", new byte[]{(byte) 0x80}));
        }

        TlvDataList tlvDataList = TlvDataList.fromBinary(builder.toString());
        arqcTlv = tlvDataList.toString();

        String apn = mEmvL2.getTagValue("9F12");
        String appLabel = mEmvL2.getTagValue("50");

        String resultTlv = arqcTlv + TlvData.fromData("AC", TlvDataList.fromBinary(arqcTlv).getTLV("9F26").getBytesValue())
                + TlvData.fromData("9B", new byte[2])
                + TlvData.fromData("9F34", new byte[3])
                + (TextUtils.isEmpty(appLabel) ? "" : TlvData.fromData("50", HexUtil.hexStringToByte(appLabel)))
                + (TextUtils.isEmpty(apn) ? "" : TlvData.fromData("9F12", HexUtil.hexStringToByte(apn)));

        Log.i(TAG,"onlineProc: applable:" + appLabel);
        Bundle online = new Bundle();
        //TODO onlineRespCode is DE 39—RESPONSE CODE, detail see ISO8583
        String onlineRespCode = "00";
        //TODO DE 55.
        byte[] arpcData = EmvUtil.getExampleARPCData();

        if (arpcData == null) {
            return "";
        }
//        online.putString(EmvOnlineResult.REJCODE, onlineRespCode);
//        online.putByteArray(EmvOnlineResult.RECVARPC_DATA, arpcData);
//
        Log.i(TAG,"result:" + resultTlv);
        return resultTlv;
    }

    private void emvFinish(int emvResult, EmvTransOutputData emvTransOutputData) throws RemoteException {
        TransactionResultCode transactionResultCode = TransactionResultCode.DECLINED_BY_ONLINE;
        CreditCard creditCard = null;
        Log.i(TAG,"emvFinish Result: " + emvResult);
        Log.i(TAG,"emvFinish AC: "+emvTransOutputData.getAcType());
        if (emvResult == EmvConstant.EmvTransResultCode.SUCCESS) { // SUCCESS
            creditCard = new CreditCard();
            switch (emvTransOutputData.getAcType()) {
                case EmvConstant.EmvACType.AAC: //Trans End
                    if (mOnlineRespEntitiy == null) {
                        transactionResultCode = TransactionResultCode.DECLINED_BY_OFFLINE;
                    } else if ("00".equals(mOnlineRespEntitiy.getRespCode())) {
                        transactionResultCode = TransactionResultCode.DECLINED_BY_TERMINAL_NEED_REVERSE;
                    } else {
                        transactionResultCode = TransactionResultCode.DECLINED_BY_ONLINE;
                    }
                    break;
                case EmvConstant.EmvACType.TC:  //Trans accept
                    if (mOnlineRespEntitiy == null) {
                        transactionResultCode = TransactionResultCode.APPROVED_BY_OFFLINE;
                    } else {
                        transactionResultCode = TransactionResultCode.APPROVED_BY_ONLINE;
                    }

                    break;
                case EmvConstant.EmvACType.ARQC: //ARQC
                    Log.i(TAG,"onFinish: ARQC");
                    mOnlineRespEntitiy = mListener.onPerformOnlineProcessing(creditCard);
                    if (mOnlineRespEntitiy != null && "00".equals(mOnlineRespEntitiy.respCode)) {
                        transactionResultCode = TransactionResultCode.APPROVED_BY_ONLINE;
                    } else {
                        transactionResultCode = TransactionResultCode.DECLINED_BY_OFFLINE;

                    }
                    break;
            }
            Log.i(TAG,"emvFinish: " + transactionResultCode.toString());
        } else if (emvResult == EmvConstant.EmvTransResultCode.EMV_RESULT_NOAPP) {  // fallback
            mCardReader.searchCard(true, false, false, 30, new AidlCheckCardListener.Stub() {
                @Override
                public void onFindMagCard(TrackData trackData) throws RemoteException {
                    Log.i(TAG,"card NO:" + trackData.getCardNo());
                    mCardReadMode = CardReadMode.SWIPE;
                    CreditCard creditCard = new CreditCard();
                    creditCard.setCardReadMode(CardReadMode.SWIPE);
                    creditCard.setCardNumber(trackData.getCardNo());
                    creditCard.setExpireDate(trackData.getExpiryDate());
                    creditCard.setServiceCode(trackData.getServiceCode());
                    CreditCard.MagData magData = new CreditCard.MagData(trackData.getTrack1Data(), trackData.getTrack2Data());
                    creditCard.setMagData(magData);
                    mListener.onCardDetected(CardReadMode.SWIPE, creditCard);

                    StringBuilder builder = new StringBuilder();
                    builder.append("Card: " + trackData.getCardNo());
                    builder.append("\nTk1: " + trackData.getTrack1Data());
                    builder.append("\nTk2: " + trackData.getTrack2Data());
                    builder.append("\nTk3: " + trackData.getTrack3Data());
                    builder.append("\ntrackKSN: " + trackData.getKsn());
                    builder.append("\nExpiryDate: " + trackData.getExpiryDate());
                    builder.append("\nCardholderName: " + trackData.getCardholderName());
                    Log.i(TAG,"FallBack onFindMagCard: " + builder.toString());
                    stopSearch();
                    inputPIN(creditCard.getCardNumber());
                    mOnlineRespEntitiy = mListener.onPerformOnlineProcessing(creditCard);
//            TransactionResultCode transactionResultCode = TransactionResultCode.DECLINED_BY_OFFLINE;
                    TransactionResultCode transactionResultCode = TransactionResultCode.APPROVED_BY_ONLINE;
                    if (mOnlineRespEntitiy != null && "00".equals(mOnlineRespEntitiy.respCode)) {
                        transactionResultCode = TransactionResultCode.APPROVED_BY_ONLINE;
                    } else {
                        transactionResultCode = TransactionResultCode.DECLINED_BY_OFFLINE;

                    }
                    mListener.onCompleted(transactionResultCode, creditCard);
                }

                @Override
                public void onSwipeCardFail() throws RemoteException {

                }

                @Override
                public void onFindICCard() throws RemoteException {

                }

                @Override
                public void onFindRFCard(int i) throws RemoteException {

                }

                @Override
                public void onTimeout() throws RemoteException {
                    Log.i(TAG,"fallback onTimeout: ");
                }

                @Override
                public void onCancelled() throws RemoteException {
                    Log.i(TAG,"fallback onCancelled: ");
                }

                @Override
                public void onError(int i) throws RemoteException {
                    Log.i(TAG,"fallback onError: " + i);
                }
            });
        } else if (emvResult == EmvConstant.EmvTransResultCode.EMV_RESULT_STOP) { //Cancel
            Log.i(TAG,"emvFinish: EEMV_RESULT_STOP");
        } else {

            Log.i(TAG,"emvFinish: Other result");
            if (mOnlineRespEntitiy != null && "00".equals(mOnlineRespEntitiy.getRespCode())) {
                transactionResultCode = TransactionResultCode.DECLINED_BY_TERMINAL_NEED_REVERSE;
            } else {
                transactionResultCode = TransactionResultCode.ERROR_UNKNOWN;
            }

        }
        onFinishShow();
        mListener.onCompleted(transactionResultCode, creditCard);

    }


    private void onFinishShow() throws RemoteException {
        StringBuilder builder = new StringBuilder();
        String tlv = mEmvL2.getTlvByTags(EmvUtil.tags);
        TlvDataList tlvDataList = TlvDataList.fromBinary(tlv);
        builder.append("Card No:" + EmvUtil.readPan() + "\n");
        builder.append("Card Org:" + CardUtil.getCardTypFromAid(mEmvL2.getTagValue("4F")) + "\n");
        builder.append("Card Track 2:" + EmvUtil.readTrack2() + "\n");
        for (String tag : EmvUtil.tags) {
            if ("9F4E".equalsIgnoreCase(tag)) {
                builder.append(tag + "=" + tlvDataList.getTLV(tag) + "\n");
            } else if ("5F20".equalsIgnoreCase(tag)) {
                builder.append(tag + "=" + tlvDataList.getTLV(tag) + "\n");
            } else {
                builder.append(tag + "=" + tlvDataList.getTLV(tag) + "\n");
            }
        }
        Log.i(TAG,"onFinishShow: " + builder.toString());
    }

    private Bundle setPinpadUI(boolean isOnline) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(PinpadConst.PinpadShow.COMMON_NEW_LAYOUT, true);
        bundle.putString(PinpadConst.PinpadShow.COMMON_OK_TEXT, "ENTER");
        bundle.putBoolean(PinpadConst.PinpadShow.COMMON_SUPPORT_BYPASS, false);
        bundle.putBoolean(PinpadConst.PinpadShow.COMMON_SUPPORT_KEYVOICE, false);
        bundle.putBoolean(PinpadConst.PinpadShow.COMMON_IS_RANDOM, true);
        if(isOnline){
            bundle.putString(PinpadConst.PinpadShow.TITLE_HEAD_CONTENT, "ENTER PIN");
        }else{
            bundle.putString(PinpadConst.PinpadShow.TITLE_HEAD_CONTENT, "ENTER OFFLINE PIN");
        }
        return bundle;
    }


    private void inputPIN(String sPAN) {

        Bundle bundle = setPinpadUI(true);
        Log.i(TAG, "PAN FOR USE: " + sPAN);
        try {
            mPinPad.inputOnlinePin(bundle, new int[]{0, 4, 5, 6, 7, 8, 9, 10, 11, 12}, 300, sPAN, 0, PinpadConst.PinAlgorithmMode.ISO9564FMT1, new AidlPinPadInputListener.Stub() {
                @Override
                public void onConfirm(byte[] data, boolean noPin, String s) throws RemoteException {
                    mEmvL2.requestPinResp(data, noPin);
                    Log.i(LOG_TAG,"PIN input:" + (noPin == true ? "NO" : "Yes"));
                    Log.i(LOG_TAG,"PIN Block:" + HexUtil.bytesToHexString(data));
                    ProfileParser.field52 = HexUtil.bytesToHexString(data);
                }

                @Override
                public void onSendKey(int keyCode) throws RemoteException {
                    Log.i(TAG,"onSendKey:" + keyCode);
                }

                @Override
                public void onCancel() throws RemoteException {
                    Log.i(TAG,"onCancel: ");
                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    AppLog.e(LOG_TAG,"onError: code:" + errorCode);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private void inputOnlinePIN(String sPAN) {
        Bundle bundle = setPinpadUI(true);
        Log.i(TAG, "ONLINE PIN 2: " + sPAN);
        try {
            mPinPad.inputOnlinePin(bundle, new int[]{4, 6}, 30, sPAN, 0,
                    PinpadConst.PinAlgorithmMode.ISO9564FMT1, new AidlPinPadInputListener.Stub() {
                @Override
                public void onConfirm(byte[] data, boolean noPin, String s) throws RemoteException {
                    StringBuilder builder = new StringBuilder();
                    builder.append("\ntime = " + (System.currentTimeMillis() - startTick) + "ms");
                    builder.append("\nPIN input:" + (noPin == true ? "NO" : "Yes"));
                    builder.append("\nPIN Block:" + HexUtil.bytesToHexString(data));
                    builder.append("\nksn: " + s);
                    Log.i("WISPIN", builder.toString());
                    ProfileParser.field52 = HexUtil.bytesToHexString(data);
                    mEmvL2.requestPinResp(data, noPin);
                }

                @Override
                public void onSendKey(int keyCode) throws RemoteException {
                    Log.i(TAG,"onSendKey:" + keyCode);
                }

                @Override
                public void onCancel() throws RemoteException {
                    Log.i(TAG,"inputOnlinePin onCancel: ");
                    mEmvL2.requestPinResp(null, false);
                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    Log.i(TAG,"onError: code:" + errorCode);
                    mEmvL2.requestPinResp(null, false);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void inputOfflinePIN(String pan, int lefttimes) {
        Log.i(TAG,"inputOfflinePIN: " + pan);
        Bundle bundle = new Bundle();
        bundle.putBoolean(PinpadConst.PinpadShow.COMMON_NEW_LAYOUT, true);
        bundle.putString(PinpadConst.PinpadShow.COMMON_OK_TEXT, "OK");
        bundle.putBoolean(PinpadConst.PinpadShow.COMMON_SUPPORT_BYPASS, false);
        bundle.putBoolean(PinpadConst.PinpadShow.COMMON_SUPPORT_KEYVOICE, false);
        bundle.putBoolean(PinpadConst.PinpadShow.COMMON_IS_RANDOM, true);
        if (lefttimes == 3) {
            bundle.putString(PinpadConst.PinpadShow.TITLE_HEAD_CONTENT, "ENTER PIN:");
        } else if (lefttimes == 2) {
            bundle.putString(PinpadConst.PinpadShow.TITLE_HEAD_CONTENT, "PIN (2 CHANCES Left)");
        } else if (lefttimes == 1) {
            bundle.putString(PinpadConst.PinpadShow.TITLE_HEAD_CONTENT, "PIN (LAST CHANCE)");
        }

        try {
            mPinPad.inputOfflinePin(bundle, new int[]{4}, 30, new AidlPinPadInputListener.Stub() {
                @Override
                public void onConfirm(byte[] data, boolean noPin, String s) throws RemoteException {
                    StringBuilder builder = new StringBuilder();
                    builder.append("\ntime = " + (System.currentTimeMillis() - startTick) + "ms");
                    builder.append("\nPIN input:" + (noPin == true ? "NO" : "Yes"));
                    builder.append("\nPIN Block:" + HexUtil.bytesToHexString(data));
                    builder.append("\nksn: " + s);
                    Log.i("OFFLINE PIN", builder.toString());
                    mEmvL2.requestPinResp(data, noPin);
                }

                @Override
                public void onSendKey(int keyCode) throws RemoteException {
                    Log.i(TAG,"onSendKey: " + keyCode);
                }

                @Override
                public void onCancel() throws RemoteException {
                    Log.i(TAG,"inputOfflinePIN onCancel: ");
                    mEmvL2.requestPinResp(null, false);
                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    AppLog.e(LOG_TAG,"onError: code:" + errorCode);
                    mEmvL2.requestPinResp(null, false);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private void stopSearch() {
        Log.i(TAG, "Stopping Card Search");
        try {
            mCardReader.cancelSearchCard();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void startEMVProcess() {
        initEmvParam();
        EmvTransData emvTransData = new EmvTransData();
        emvTransData.setAmount(mAount);
        emvTransData.setOtherAmount(0);
        emvTransData.setForceOnline(true);
        emvTransData.setEmvFlowType(EmvConstant.EmvTransFlow.FULL);
        try {
            mEmvL2.startEmvProcess(emvTransData, emvStartListener);
        } catch (RemoteException e) {
            Log.i(TAG, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void selApp(List<CandidateAID> appList) {

        String[] options = new String[appList.size()];
        for (int i = 0; i < appList.size(); i++) {
            options[i] = appList.get(i).getAppLabel();
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setTitle("SELECT APP");
        alertBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                try {
                    mEmvL2.requestAidSelectResp(index);
                } catch (RemoteException e) {

                }
            }
        });
        AlertDialog alertDialog1 = alertBuilder.create();
        alertDialog1.show();
    }

}
