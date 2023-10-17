package com.gbikna.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.TextView;
import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.horizonpay.smartpossdk.aidl.camera.AidlScanListener;
import com.horizonpay.smartpossdk.aidl.camera.IAidlCamera;
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
public class ScannerActivity extends BaseActivity {

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.button)
    Button button;

    IAidlCamera scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_button);
        ButterKnife.bind(this);

        setButtonName();

        try {
            scanner = MyApplication.getINSTANCE().getDevice().getCamera();
        } catch (RemoteException e) {

        }

    }

    @OnClick({R.id.button})
    public void onClick() {
        scanner();
    }

    private void scanner() {
        try {

            scanner.scan(true, true, true, new AidlScanListener.Stub() {
                @Override
                public void onScanSuccess(String s, int i) throws RemoteException {
                    System.out.println("type " + i);
                    showResult(textView, "type:" + i + "\n" + "result:" + s);
                }

                @Override
                public void onError(int i) throws RemoteException {
                    showResult(textView, "type:" + i + "\n");
                }
            });


        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setButtonName() {
        button.setText(getString(R.string.menu_scanner));
    }


}