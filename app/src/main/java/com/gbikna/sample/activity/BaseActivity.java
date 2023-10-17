package com.gbikna.sample.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.gbikna.sample.utils.AppLog;


/***************************************************************************************************
 *                          Copyright (C),  Shenzhen Horizon Technology Limited                    *
 *                                   http://www.horizonpay.cn                                      *
 ***************************************************************************************************
 * usage           :
 * Version         : 1
 * Author          : Atos
 * Date            : 2017/12/18
 * Modify          : create file
 **************************************************************************************************/
public abstract class BaseActivity extends Activity {
    private final String LOG_TAG = BaseActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void showResult(final TextView textView, final String text) {
        AppLog.d(LOG_TAG,text);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(text + "\r\n");
            }
        });
    }

    protected abstract void setButtonName();

    protected boolean checkAppInstalled(String pkgName) {
        if (pkgName == null || pkgName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo(pkgName, 0);
            return packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}