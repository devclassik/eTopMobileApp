package com.gbikna.sample.activity;


import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.gbikna.sample.MyApplication;
import com.gbikna.sample.bean.BackEndRequest;
import com.gbikna.sample.bean.BackEndResponse;
import com.gbikna.sample.bean.PaymentCardDetectMode;
import com.gbikna.sample.dialog.AppSelectDialog;
import com.gbikna.sample.pay.CardReadMode;
import com.gbikna.sample.pay.CreditCard;
import com.gbikna.sample.pay.OnlineRespEntitiy;
import com.gbikna.sample.pay.PayProcessor;
import com.gbikna.sample.pay.TransactionResultCode;
import com.gbikna.sample.utils.UUIDUtils;
import com.horizonpay.smartpossdk.aidl.emv.CandidateAID;
import com.gbikna.sample.R;
import com.horizonpay.smartpossdk.aidl.emv.IAidlEmvL2;
import com.horizonpay.utils.ToastUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
public class EmvActivity extends BaseActivity {
    private final String TAG = EmvActivity.class.getName();
    private PayProcessor payProcessor;
    IAidlEmvL2 mEmvL2;
    private boolean isSupport;
    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.button)
    Button button;

    private EditText etAmount;
    private long amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv);
        ButterKnife.bind(this);
        etAmount = findViewById(R.id.et_amount);
        setButtonName();
        try {
            mEmvL2 = MyApplication.getINSTANCE().getDevice().getEmvL2();
            isSupport = mEmvL2.isSupport();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        payProcessor = new PayProcessor();

    }


    @OnClick({R.id.button})
    public void onClick() {
//        inputAmount();
        if (!isSupport) {
            ToastUtils.showShort(R.string.err_not_support_api);
            return;
        }
        amount = Long.parseLong(etAmount.getText().toString());
        payProcessor.pay(amount, processorListener);
    }

    @Override
    protected void setButtonName() {
        button.setText(getString(R.string.menu_payment));
    }

    private PayProcessor.PayProcessorListener processorListener = new PayProcessor.PayProcessorListener() {
        @Override
        public void onRetry(int retryFlag) {
            if (retryFlag == 0) {
                showResult(textView, "Please Insert/Tap Card ");
            } else {
                showResult(textView, ">>>onRetry");
            }
        }

        @Override
        public void onCardDetected(final CardReadMode cardReadMode, CreditCard creditCard) {
            showResult(textView, ">>>onCardDetected" + cardReadMode);
        }


        @Override
        public CandidateAID confirmApplicationSelection(List<CandidateAID> candidateList) {
            int selectedIndex = 0;
            try {
                selectedIndex = new AppSelectDialog(EmvActivity.this, candidateList).call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            showResult(textView, ">>>confirmApplicationSelection:" + candidateList.get(selectedIndex).getAid());
            return candidateList.get(selectedIndex);
        }

        @Override
        public OnlineRespEntitiy onPerformOnlineProcessing(CreditCard creditCard) {
            showResult(textView, ">>>onPerformOnlineProcessing:");
            BackEndRequest.TransRequest.BankTransInfoRequest request = new BackEndRequest.TransRequest.BankTransInfoRequest();

            request.CardDetectMode = PaymentCardDetectMode.ICC;
            request.CardNo = creditCard.getCardNumber();
            request.CardExpDate = creditCard.getExpireDate();
            request.CardSN = creditCard.getCardSequenceNumber();
            request.CardHolderName = creditCard.getHolderName();
            request.CardFallback = false;
            request.MsgAmount = String.valueOf(amount);
            request.MsgCurrencyCode = "344";
            if (creditCard.getMagData() != null) {
//                request.msgTk1 = creditCard.getMagData().getTrack1();
                request.msgTk2 = creditCard.getMagData().getTrack2();
            }
            if (creditCard.getEmvData() != null) {
                request.msgTk1 = creditCard.getEmvData().getTrack1();
                request.msgTk2 = creditCard.getEmvData().getTrack2();
                request.IccData = creditCard.getEmvData().getIccData();
            }

            BackEndResponse.BankTransInfoResponse response = OnlineTxn(request);

            OnlineRespEntitiy entitiy = new OnlineRespEntitiy();
//            entitiy.setRespCode("05");
            entitiy.setRespCode("00");
            entitiy.setIccData("");
            if (response != null) {
                entitiy.setRespCode(response.getMsgRespCode());
                entitiy.setIccData(response.getIccData());
            }
            return entitiy;
        }

        @Override
        public void onCompleted(TransactionResultCode result, CreditCard creditCard) {
            final StringBuilder resultText = new StringBuilder();
            switch (result) {
                case APPROVED_BY_OFFLINE:
                    resultText.append(TransactionResultCode.APPROVED_BY_OFFLINE.toString());
                    break;
                case APPROVED_BY_ONLINE:
                    resultText.append(TransactionResultCode.APPROVED_BY_ONLINE.toString());
                    break;
                case DECLINED_BY_OFFLINE:
                    resultText.append(TransactionResultCode.DECLINED_BY_OFFLINE.toString());
                    break;
                case DECLINED_BY_ONLINE:
                    resultText.append(TransactionResultCode.DECLINED_BY_ONLINE.toString());
                    break;
                case DECLINED_BY_TERMINAL_NEED_REVERSE:
                    resultText.append(TransactionResultCode.DECLINED_BY_TERMINAL_NEED_REVERSE.toString());
                    break;
                case ERROR_TRANSCATION_CANCEL:
                    resultText.append(TransactionResultCode.ERROR_TRANSCATION_CANCEL.toString());
                    break;
                case ERROR_UNKNOWN:
                    resultText.append(TransactionResultCode.ERROR_UNKNOWN.toString());
                    break;

            }
            showResult(textView, ">>>onCompleted:" + resultText);

        }
    };

    private BackEndResponse.BankTransInfoResponse OnlineTxn(BackEndRequest.TransRequest.BankTransInfoRequest bankTransInfoRequest) {
        BackEndRequest backEndRequest = new BackEndRequest();
        backEndRequest.cfgBean = new BackEndRequest.CfgBean();

        // BOC config
        backEndRequest.cfgBean.Ip = "202.127.169.216";
        backEndRequest.cfgBean.Port = "6868";
        backEndRequest.cfgBean.TPDU = "6000170000";
        backEndRequest.cfgBean.NII = "017";
        backEndRequest.cfgBean.MID = "010601995000005";
        backEndRequest.cfgBean.TID = "21756604";
        backEndRequest.cfgBean.SSL = true;

        //
        String test = "{\"BankTransInfoRequest\":{\"CardExpDate\":\"1209\",\"CardFallback\":false,\"CardNo\":\"5413330089020011\",\"CardSN\":\"\",\"CardTk2\":\"\",\"CardDetectMode\":1,\"IccData\":\"\",\"MsgAmount\":\"000000012000\",\"MsgCurrencyCode\":\"344\",\"StatusOffline\":false},\"MsgUuid\":\"a21a5f171d6a42e0962cbab7a93d0910\",\"SN\":\"1813CP617659\",\"TransType\":1}\n";
        backEndRequest.transRequest = new Gson().fromJson(test, BackEndRequest.TransRequest.class);
        // backEndRequest.transRequest =new BackEndRequest.TransRequest();
        backEndRequest.transRequest.msgUuid = UUIDUtils.getUUID32();
        backEndRequest.transRequest.SN = "12345678";
        backEndRequest.transRequest.transType = 1;

        backEndRequest.transRequest.bankTransInfoRequest = bankTransInfoRequest;

        // 网络请求
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        //使用Gson 添加 依赖 compile 'com.google.code.gson:gson:2.8.1'
        Gson gson = new Gson();
        //使用Gson将对象转换为json字符串
        String jsonbankTransInfoRequest = gson.toJson(backEndRequest.transRequest.bankTransInfoRequest);
        String json = gson.toJson(backEndRequest);

        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);

        Request request = new Request.Builder()
                .url("http://120.78.128.214:30008/")//请求的url
                .post(requestBody)
                .build();

        //创建/Call
        Call call = okHttpClient.newCall(request);
        System.out.println("开始请求");

        try {
            Response response = call.execute();
            assert response.body() != null;
            String respStr = response.body().string();
            System.out.println(respStr);
            BackEndResponse backEndResponse;
            try {
                backEndResponse = gson.fromJson(respStr, BackEndResponse.class);
                return backEndResponse.getBankTransInfoResponse();

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


}