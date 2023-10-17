package com.gbikna.sample.utils;

import android.text.TextUtils;


import java.util.ArrayList;
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
public class StringUitls {

    private String transformAmount(String amount) {
        try {
            long lAmount = Long.parseLong(amount);
            amount = String.valueOf(lAmount * 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amount;
    }

    public static <T> ArrayList<T> createArrayList(T... elements) {
        ArrayList<T> list = new ArrayList<T>();
        for (T element : elements) {
            list.add(element);
        }
        return list;
    }

    public static String getServiceCodeFromTrack2(String track2) {
        String ServiceCode24 = "";
        if (TextUtils.isEmpty(track2)){
            return "";
        }
        for (int i = 0; i < track2.length(); i++) {
            if (track2.charAt(i) == '=' || track2.charAt(i) == 'd' || track2.charAt(i) == 'D') {
                ServiceCode24 = track2.substring(i + 5, i + 5 + 3);
                break;
            }
        }
        return ServiceCode24;
    }
}
