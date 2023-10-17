package com.gbikna.sample.pay;
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
public enum TransactionResultCode {


    /**
     * 交易脱机批准
     */
    APPROVED_BY_OFFLINE,
    /**
     * 交易联机批准
     */
    APPROVED_BY_ONLINE,
    /**
     * 交易脱机拒绝
     */
    DECLINED_BY_OFFLINE,
    /**
     * 交易联机拒绝
     */
    DECLINED_BY_ONLINE,

    /**
     * 交易终端拒绝，需要冲正
     */
    DECLINED_BY_TERMINAL_NEED_REVERSE,

    /**
     * 取消
     */
    ERROR_TRANSCATION_CANCEL,
    /**
     * 未知错误
     */
    ERROR_UNKNOWN,
}
