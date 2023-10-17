package com.gbikna.sample.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.utils.AppLog;
import com.gbikna.sample.utils.CombBitmap;
import com.gbikna.sample.utils.GenerateBitmap;
import com.horizonpay.smartpossdk.aidl.printer.AidlPrinterListener;
import com.horizonpay.smartpossdk.aidl.printer.IAidlPrinter;
import com.horizonpay.smartpossdk.data.PrinterConst;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
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

public class PressurePrintActivity extends BaseActivity {
    private static final String TAG = "PressurePrintActivity";
    IAidlPrinter printer;
    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.currentTimes)
    TextView tvCurrentTime;

    @BindView(R.id.successTime)
    TextView tvSuccessTime;

    @BindView(R.id.failTime)
    TextView tvFailTime;

    @BindView(R.id.TestTimes)
    EditText etTestTime;

    @BindView(R.id.failList)
    TextView tvFailList;


    private int failTimes = 0;
    private int successTimes = 0;
    private int currentTimes = 0;

    private List<String> failList = new ArrayList<>();
    private CountDownLatch startSignal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressure);
        ButterKnife.bind(this);

        setButtonName();
        try {
            printer = MyApplication.getINSTANCE().getDevice().getPrinter();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.button})
    public void onClick() {

        initTest();



        new Thread(() -> {
            Looper.prepare();
            for (currentTimes = 0; currentTimes < Integer.parseInt(etTestTime.getText().toString()); currentTimes++) {
                try {
                    showCurrentTime(currentTimes + 1);

//                    if(printTest() == true){
//                        AppLog.d(TAG,"onClick: finish ");
//                    }

                    while (printTest() == PrinterConst.RetCode.ERROR_DEV_IS_BUSY){
                        AppLog.d(TAG,"onClick: wait to finish");
                    }
                    AppLog.d(TAG,"onClick: finish ");
                } catch (RemoteException e) {
                    showResult(textView, "printTest:" + e.getMessage());
                    startSignal.countDown();
                }

            }
        }).start();


    }

    @Override
    protected void setButtonName() {
        button.setText(getString(R.string.pressure_test));
    }

    @Override
    protected void onDestroy() {
        currentTimes = 100000;
        super.onDestroy();
    }

    private int printRet = 0;

    private int sendPrint(Bitmap bmSlip) {
        try {
            printer.printBmp(true, false, bmSlip, 0, new AidlPrinterListener.Stub() {
                @Override
                public void onError(int i) throws RemoteException {
                    switch (i) {
                        case PrinterConst.RetCode.ERROR_PRINT_NOPAPER:
                            showResult(textView, getString(R.string.msg_print_paper));
                            break;
                        case PrinterConst.RetCode.ERROR_DEV:
                            showResult(textView, getString(R.string.msg_print_device));
                            break;
                        case PrinterConst.RetCode.ERROR_DEV_IS_BUSY:
                            //showResult(textView, getString(R.string.msg_print_busy));
                            AppLog.d(TAG,"printting...");
                            break;
                        default:
                        case PrinterConst.RetCode.ERROR_OTHER:
                            showResult(textView, getString(R.string.msg_print_other));
                            break;
                    }
                    printRet = i;
                    AppLog.d(TAG,"onError: " + i);
                }

                @Override
                public void onPrintSuccess() throws RemoteException {
                    printRet = 0;
                    AppLog.d(TAG,"onPrintSuccess: ");
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return printRet;
    }

    private int printTest() throws RemoteException {
        startSignal = new CountDownLatch(1);
        SystemClock.sleep(500);
        Bitmap bmp = generateTestBitmap();
        setEnable(false);

        int ret = sendPrint(bmp);
        if (ret == 0) {
            AppLog.d(TAG,"printTest: success");
            successTimes++;
            showSuccessTime(successTimes);
        } else if(ret == PrinterConst.RetCode.ERROR_DEV_IS_BUSY){
            AppLog.d(TAG,"printing...");
        }else {
            AppLog.d(TAG,"printTest: Failed");
            failTimes++;
            showFailTime(failTimes);
            showFailList();
        }
        setEnable(true);
        startSignal.countDown();
        try {
            startSignal.await();
        } catch (InterruptedException e) {
            startSignal.countDown();
        }

        return ret;
    }


    private void showCurrentTime(int i) {
        runOnUiThread(() -> tvCurrentTime.setText(getString(R.string.current_count) + ":" + i));
    }

    private void setEnable(boolean enable) {
        runOnUiThread(() -> button.setEnabled(enable));
    }

    private void showSuccessTime(int i) {
        runOnUiThread(() -> tvSuccessTime.setText(getString(R.string.success_count) + ":" + i));
    }

    private void showFailTime(int i) {
        runOnUiThread(() -> tvFailTime.setText(getString(R.string.fail_count) + ":" + i));
        if (i != 0) {
            failList.add(currentTimes + "");
        }
    }

    private void showFailList() {
        if (failList.size() > 0) {
            runOnUiThread(() -> tvFailList.setText(getString(R.string.fail_list) + ":" + failList.toString()));
        }
    }

    private void initTest() {
        showCurrentTime(0);
        showFailTime(0);
        showSuccessTime(0);
        failTimes = 0;
        successTimes = 0;
        currentTimes = 0;
    }
    
    private Bitmap generateTestBitmap() {
        //Print Logo
        CombBitmap combBitmap = new CombBitmap();
//        Bitmap bitmap;
//        bitmap = getImageFromAssetsFile(this,"horizonpayprintlogo.bmp");
//        combBitmap.addBitmap(bitmap );
        //Title
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Horizon Test Slip", 50, GenerateBitmap.AlignEnum.CENTER, true, false));

        combBitmap.addBitmap(GenerateBitmap.generateLine(1)); // print one line

        //Content
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Terminal ID:", "123456", 26, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Merchant ID:", "123456789012345", 26, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Merchant Name", "Demo Test", 26, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Operator ID:", "01", 26, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Issuer code", "12345", 26, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Card NO.", 26, GenerateBitmap.AlignEnum.LEFT, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("62179380*****7654", 36, GenerateBitmap.AlignEnum.RIGHT, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Stan code:", "000012", 26, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Batch NO.:", "000034", 26, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Date/Time:", "2019-09-10 17:06:56", 26, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Reference NO.:", "987654324567", 26, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Amount:", 26, GenerateBitmap.AlignEnum.LEFT, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("USD 123456.89", 40, GenerateBitmap.AlignEnum.RIGHT, true, true));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("--------------------------------------", 32, GenerateBitmap.AlignEnum.CENTER, true, false)); // 打印一行直线
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Card Holder Signature:", 20, GenerateBitmap.AlignEnum.LEFT, true, false));

        combBitmap.addBitmap(GenerateBitmap.generateGap(60)); // print row gap
        combBitmap.addBitmap(GenerateBitmap.generateLine(1)); // print one line

//        combBitmap.addBitmap(GenerateBitmap.formatBitmap(GenerateBitmap.generateQRCodeBitmap("123545678901235456789012354567890", 200), GenerateBitmap.AlignEnum.CENTER));
//        combBitmap.addBitmap(GenerateBitmap.formatBitmap(GenerateBitmap.generateBarCodeBitmap("123456",260,50),GenerateBitmap.AlignEnum.CENTER));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("--X--X--X--X--X--X--X--X--X--X--X--X", 20, GenerateBitmap.AlignEnum.CENTER, true, false));

        combBitmap.addBitmap(GenerateBitmap.generateGap(60)); // print row gap
        return combBitmap.getCombBitmap();

    }

}