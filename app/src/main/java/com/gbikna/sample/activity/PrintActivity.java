package com.gbikna.sample.activity;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.utils.AppLog;
import com.gbikna.sample.utils.CombBitmap;
import com.gbikna.sample.utils.GenerateBitmap;
import com.horizonpay.smartpossdk.aidl.printer.AidlPrinterListener;
import com.horizonpay.smartpossdk.aidl.printer.IAidlPrinter;
import com.horizonpay.smartpossdk.data.PrinterConst;


import java.io.IOException;
import java.io.InputStream;

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
public class PrintActivity extends BaseActivity {
    private static final String TAG = "PrintActivity";
    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.button)
    Button button;

    IAidlPrinter printer;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        //iv = findViewById(R.id.imageiv);
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
        showResult(textView, getString(R.string.msg_printing));
        button.setEnabled(false);
        print();
        iv.setImageBitmap(generateTestBitmap());
    }


    private void print() {
        try {
            printer.printBmp(true, false, generateTestBitmap(), 0, new AidlPrinterListener.Stub() {
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
                            showResult(textView, getString(R.string.msg_print_busy));
                            break;
                        default:
                        case PrinterConst.RetCode.ERROR_OTHER:
                            showResult(textView, getString(R.string.msg_print_other));
                            break;
                    }
                }

                @Override
                public void onPrintSuccess() throws RemoteException {
                    AppLog.d(TAG,"onPrintSuccess: ");
                    showResult(textView, getString(R.string.msg_print_succ));
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            button.setEnabled(true);
        }
    }

    @Override
    protected void setButtonName() {
        button.setText(getString(R.string.menu_print));
    }

    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppLog.d(TAG, "bitMap  =" + image);
        return image;
    }


    private Bitmap generateTestBitmap() {
        //Print Logo
        CombBitmap combBitmap = new CombBitmap();
        Bitmap bitmap;
        bitmap = getImageFromAssetsFile(this, "horizonpayprintlogo.bmp");
        combBitmap.addBitmap(bitmap);
        //Title
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

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("--------------------------------------", 20, GenerateBitmap.AlignEnum.CENTER, true, false)); // 打印一行直线
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Card Holder Signature:", 20, GenerateBitmap.AlignEnum.LEFT, true, false));

        combBitmap.addBitmap(GenerateBitmap.generateGap(60)); // print row gap
        combBitmap.addBitmap(GenerateBitmap.generateLine(1)); // print one line

        combBitmap.addBitmap(GenerateBitmap.formatBitmap(GenerateBitmap.generateQRCodeBitmap("123545678901235456789012354567890", 200), GenerateBitmap.AlignEnum.CENTER));
        combBitmap.addBitmap(GenerateBitmap.formatBitmap(GenerateBitmap.generateBarCodeBitmap("123456", 260, 50), GenerateBitmap.AlignEnum.CENTER));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("--X--X--X--X--X--X--X--X--X--X--X--X", 20, GenerateBitmap.AlignEnum.CENTER, true, false));

        combBitmap.addBitmap(GenerateBitmap.generateGap(60)); // print row gap


        Bitmap bp = combBitmap.getCombBitmap();

        return bp;
    }

}


