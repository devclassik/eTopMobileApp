package com.gbikna.sample.bean;

import com.google.gson.annotations.SerializedName;

public class BackEndRequest {
    @SerializedName("TransRequest")
    public TransRequest transRequest;
    @SerializedName("CfgBean")
    public CfgBean cfgBean;

    // 配置参数
    public static class CfgBean {
        @SerializedName("TPDU")
        public String TPDU;
        @SerializedName("NII")
        public String NII;
        @SerializedName("MID")
        public String MID;
        @SerializedName("TID")
        public String TID;
        @SerializedName("Ip")
        public String Ip;
        @SerializedName("Port")
        public String Port;
        @SerializedName("SSL")
        public boolean SSL = false;
        @SerializedName("SubmitterID")
        public String SubmitterID;
    }

    public static class TransRequest {
        @SerializedName("MsgUuid")
        public String msgUuid;
        @SerializedName("OrgUuid")
        public String orgUuid;

        //Transaction ID
        @SerializedName("TransType")
        public int transType;
        @SerializedName("SN")
        public String SN;
        @SerializedName("BankTransInfoRequest")
        public BankTransInfoRequest bankTransInfoRequest;

        // ***********************************************请求数据模型***************************************
        public static class BankTransInfoRequest {
            //Card Information
            @SerializedName("CardDetectMode")
            public int CardDetectMode; // card reading mode: manual, magstripe, contact, contactless, MSD
            @SerializedName("CardFallback")
            public boolean CardFallback;   // if it is Fallback
            @SerializedName("CardNo")
            public String CardNo;        // card number
            @SerializedName("CardHolderName")
            public String CardHolderName; // card holder name
            @SerializedName("CardExpDate")
            public String CardExpDate;    // card expired date
            @SerializedName("CardSN")
            public String CardSN;         // card sn

            //EMV Information
            @SerializedName("IccData")
            public String IccData;

            /* request message */
            @SerializedName("MsgAmount")
            public String MsgAmount;         // #04 Amount
            @SerializedName("MsgCurrencyCode")
            public String MsgCurrencyCode;   // #49 Transaction Currency Code

            //Transaciton Message Data
            @SerializedName("MsgCashierNO")
            public String MsgCashierNO; // Cashier NO.

            @SerializedName("StatusOffline")
            public boolean StatusOffline = false; // offline sale

            // tk
            @SerializedName("CardTk1")
            public String msgTk1;
            @SerializedName("CardTk2")
            public String msgTk2;
            @SerializedName("CardTk3")
            public String msgTk3;
            @SerializedName("MsgPin")
            public String msgPin;
        }
    }
}
