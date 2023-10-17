package com.gbikna.sample.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.utils.AppLog;
import com.horizonpay.smartpossdk.aidl.sys.IAidlSys;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SysActivity extends BaseActivity {
    IAidlSys system;
    private static final String TAG = "SysActivity";
    private static final String Model_T2 = "T2";
    private static final String Model_K11 = "K11";

    @BindView(R.id.textView)
    TextView textView;
//    private IntentFilter intentFilter;
//    private simChangeReceiver SimChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apitest);
        ButterKnife.bind(this);
        setButtonName();
        try {
            system = MyApplication.getINSTANCE().getDevice().getSysHandler();
        } catch (RemoteException e) {
            AppLog.d(TAG, e.getMessage());
        }



    }


    @OnClick({R.id.sn, R.id.model, R.id.firmware, R.id.app0, R.id.app1,
            R.id.gpsoff, R.id.gpson, R.id.updatefm, R.id.reboot, R.id.launcher1,
            R.id.log, R.id.recovery, R.id.isfore, R.id.isnotfore, R.id.install,
            R.id.uninstall, R.id.launcher2, R.id.sim
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sn:
                getsn();
                break;
            case R.id.model:
                getmodel();
                break;
            case R.id.firmware:
                getfm();
                break;
            case R.id.install:
                installApp();
                break;
            case R.id.uninstall:
                uninstallApp();
                break;
            case R.id.app0:
                switchApp("com.horizon.helper", false);
                break;
            case R.id.app1:
                switchApp("com.horizon.helper", true);
                break;
            case R.id.isfore:
                isForeground("com.gbikna.sample");
                break;
            case R.id.isnotfore:
                isForeground("com.horizon.helper");
                break;
            case R.id.log:
                getlog();
                break;
            case R.id.gpson:
                switchGps(true);
                break;
            case R.id.gpsoff:
                switchGps(false);
                break;
            case R.id.reboot:
                reboot();
                break;
            case R.id.recovery:
                recovery();
                break;
            case R.id.updatefm:
                updatefm();
                break;
            case R.id.launcher1:
                setLauncher("com.horizon.up.test", "com.android.horizon.Launcher");
                break;
            case R.id.launcher2:
                setLauncher("com.android.horizonlauncher", "com.android.horizon.Launcher");
                break;
            case R.id.sim:
                switcdSim();
                break;

        }
    }

    private void getsn() {
        try {
            String sn = system.getSn();
            showResult(textView, "SN: " + sn);
            showResult(textView, "Serial: " + Build.SERIAL);
        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.getMessage());
        }
    }

    private String getmodel() {
        String model = null;
        try {
            model = system.getModel();
            showResult(textView, "Model: " + model);
        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.getMessage());
        }

        return model;

    }

    private void getfm() {
        try {
            String fm = system.getFiremware();
            showResult(textView, "Fm: " + fm);
        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.getMessage());
        }
    }

    private void installApp() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    final String ret = system.installApp("//sdcard/Download/horizon_pinpad_ysdk_3.8.5.apk"); /// ///sdcard/Download/test.apk
                    System.out.println("install->" + ret);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (ret == null) {
                                showResult(textView, "install App: Successful");
                            } else {
                                showResult(textView, "install App: " + ret);
                            }
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void uninstallApp() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    final String ret = system.unInstallApp("com.horizon.helper");
                    System.out.println("unInstallApp->" + ret);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (ret == null) {
                                showResult(textView, "unInstall App: Successful");
                            } else {
                                showResult(textView, "unInstall App: " + ret);
                            }

                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void reboot() {
        try {
            boolean ret = system.reboot();
            showResult(textView, "Reboot: " + ret);
        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.getMessage());
        }
    }

    private void recovery() {

        showResult(textView, "Recovery: " + false);

        //Be careful use this API.
//        try {
//            boolean ret = system.recovery();
//            showResult(textView, "Recovery: " + ret);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            showResult(textView, e.getMessage());
//        }
    }

    private void updatefm() {
        try {
            String ret = system.updateFirmware("/mnt/usb_storage/update.img");
            showResult(textView, "updateFirmware: " + ret);
        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.getMessage());
        }
    }

    private void getlog() {
        try {
            String ret = system.getLogcat();
            showResult(textView, "Log Path: " + ret);
        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.getMessage());
        }
    }

    private void isForeground(String pkgName) {
        try {
            boolean ret = system.isForeground(pkgName);
            showResult(textView, pkgName + (ret ? " is Foreground" : " is not Foreground"));
        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.getMessage());
        }
    }

    private void switchApp(String pck, boolean siwtch) {
        try {
            boolean ret = system.switchAppEnable(pck, siwtch);
            showResult(textView, pck + (siwtch ? " Enable:" : " Disable") + (ret ? " Scuccess" : " Failed"));
        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.getMessage());
        }
    }


    private void setLauncher(String pckID, String activity) {
        try {
            boolean ret = system.setLauncherApp(pckID, activity);
            showResult(textView, pckID + " set as default Launcher:" + (ret ? " Scuccess" : " Failed"));
        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.getMessage());
        }
    }


    private void switchGps(boolean sw) {
        try {
            boolean ret = system.switchGPS(sw);
            showResult(textView, (sw ? "Enable GPS:" : "Disable GPS") + (ret ? " Scuccess" : " Failed"));
        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.getMessage());
        }
    }

    private void switcdSim() {
        if (!getmodel().equals(Model_T2)){
            showResult(textView, "Model Not support");
        }

            Intent intent = new Intent();
        intent.setComponent(new ComponentName(
                "com.android.settings",
                "com.android.settings.Settings$SimSettingsActivity"));
        startActivity(intent);
    }


    @Override
    protected void setButtonName() {

    }



}
