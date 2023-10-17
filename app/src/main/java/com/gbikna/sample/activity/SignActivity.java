package com.gbikna.sample.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;


import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.view.PaintView;
import com.horizonpay.smartpossdk.aidl.printer.AidlPrinterListener;
import com.horizonpay.smartpossdk.data.PrinterConst;
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
public class SignActivity extends BaseActivity {

    private static final String TAG = SignActivity.class.getName();

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.canvas)
    PaintView mPaintView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.clear, R.id.print})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear:
                mPaintView.clearCanvas();
                break;
            case R.id.print:
                print();
                break;
        }
    }

    private void print() {
        try {
            Bitmap signBitmap = mPaintView.getCanvasBitmap();
            MyApplication.getINSTANCE().getDevice().getPrinter().printBmp(true, false, signBitmap, 0, new AidlPrinterListener.Stub() {
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
                    showResult(textView, getString(R.string.msg_print_succ));
                }

            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setButtonName() {

    }
}
