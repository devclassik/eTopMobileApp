package com.gbikna.sample.utils;


import android.os.RemoteException;
import android.text.TextUtils;

import com.gbikna.sample.MyApplication;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.horizonpay.smartpossdk.aidl.emv.EmvTermConfig;
import java.text.SimpleDateFormat;
import java.util.Date;

/***************************************************************************************************
 *                          Copyright (C),  Shenzhen Horizon Technology Limited                    *
 *                                   http://www.horizonpay.cn                                      *
 ***************************************************************************************************
 * usage           :
 * Version         : 1
 * Author          : Carl
 * Date            : 2019/08/12
 * Modify          : create file
 **************************************************************************************************/
public class EmvUtil {

    private static final String TAG = EmvUtil.class.getName();

    public static final String[] arqcTLVTags = new String[]{
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
            "9F1A",
            "9F33",
            "9F34",
            "9F35",
            "9F1E",
            "84",
            "9F09",
            "9F63"
    };

    public static final String[] tags = new String[]{
            "5F20",
            "5F30",
            "9F03",
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
            "9F1A",
            "9F03",
            "9F33",
            "9F34",
            "9F35",
            "9F1E",
            "84",
            "9F09",
            "9F41",
            "9F63"
    };


    public static byte[] getExampleARPCData() {
        //TODO Data returned by background server ,should be contain 91 tag, if you need to test ARPC
        // such as : 91 0A F9 8D 4B 51 B4 76 34 74 30 30 ,   if need to set 71 and 72  ,Please add this String
        return HexUtil.hexStringToByte("910AF98D4B51B47634743030");
    }


    public static EmvTermConfig getInitTermConfig() {
        EmvTermConfig config = new EmvTermConfig();
        config.setMerchId(ProfileParser.mid);
        config.setTermId(ProfileParser.tid);
        config.setMerchName(ProfileParser.merchantname);
        config.setCapability("E0F8C8"); //"206808"
        config.setExtCapability("E000F0A001");//"F200F0A001"
        config.setTermType(0x22);
        config.setCountryCode("0566");
        config.setTransCurrCode("0566");
        config.setTransCurrExp(2);
        config.setMerchCateCode("0000");
        return config;
    }


    public static String getCurrentTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());
        return df.format(curDate);
    }
    public static String readPan() {
        String pan = null;
        try {
            pan = MyApplication.getINSTANCE().getDevice().getEmvL2().getTagValue("5A");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(pan)) {
            return getPanFromTrack2();
        }
        if (pan.endsWith("F")) {
            return pan.substring(0, pan.length() - 1);
        }
        return pan;
    }
    public static String readTrack2() {

        String track2 = null;
        try {
            track2 = MyApplication.getINSTANCE().getDevice().getEmvL2().getTagValue("6B");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(track2) && track2.endsWith("F")) {
            return track2.substring(0, track2.length() - 1);
        }
        return track2;
    }

    protected static String getPanFromTrack2() {
        String track2 = readTrack2();
        if (track2 != null) {
            for (int i = 0; i < track2.length(); i++) {
                if (track2.charAt(i) == '=' || track2.charAt(i) == 'D') {
                    int endIndex = Math.min(i, 19);
                    return track2.substring(0, endIndex);
                }
            }
        }
        return null;
    }
}
