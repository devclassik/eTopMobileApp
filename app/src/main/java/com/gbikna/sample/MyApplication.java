package com.gbikna.sample;

import android.app.Application;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.gbikna.sample.utils.AppLog;
import com.horizonpay.smartpossdk.PosAidlDeviceServiceUtil;
import com.horizonpay.smartpossdk.aidl.IAidlDevice;

import com.horizonpay.utils.BaseUtils;


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

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static MyApplication INSTANCE;
    private IAidlDevice device;

    public static MyApplication getINSTANCE(){
        Log.i(TAG,"Getting App Instance");
        return INSTANCE;
    }

    public IAidlDevice getDevice(){
        Log.i(TAG,"GettingDevice");
        return device;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Getting App Oncreate");
        INSTANCE = this;
        Log.i(TAG,"Getting App Oncreate 1");
        BaseUtils.init(this);
        Log.i(TAG,"Getting App Oncreate 2");
        AppLog.debug(true);
        Log.i(TAG,"Getting App Oncreate 4");
        bindDriverService();
    }



    private void bindDriverService() {
        PosAidlDeviceServiceUtil.connectDeviceService(this, new PosAidlDeviceServiceUtil.DeviceServiceListen() {
            @Override
            public void onConnected(IAidlDevice device) {
                Log.i(TAG,"Getting App connected");
                MyApplication.this.device = device;
                try {
                    Log.i(TAG,"Getting App connected 2");
                    MyApplication.this.device.asBinder().linkToDeath(deathRecipient, 0);
                    Log.i(TAG,"Getting App connected 3");
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.i(TAG,"Getting App connected error");
                }
            }

            @Override
            public void error(int errorcode) {
                Log.i(TAG,"Getting App Error: " + errorcode);
            }

            @Override
            public void onDisconnected() {
                Log.i(TAG,"Getting App onDiscounted");
            }

            @Override
            public void onUnCompatibleDevice() {
                Log.i(TAG,"Getting App notCapable");
            }
        });
    }

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {

            if (MyApplication.this.device == null) {
                Log.i(TAG,"binderDied device is null");
                return;
            }

            Log.i(TAG,"Getting App binddied");

            MyApplication.this.device.asBinder().unlinkToDeath(deathRecipient, 0);
            MyApplication.this.device = null;

            //reBind driver Service
            bindDriverService();
        }
    };

}
