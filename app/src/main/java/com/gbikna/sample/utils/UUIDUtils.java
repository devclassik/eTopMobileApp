package com.gbikna.sample.utils;

import java.util.UUID;

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
public class UUIDUtils {
    public static String getUUID32() {
        return UUID.randomUUID().toString().toLowerCase();
    }
}
