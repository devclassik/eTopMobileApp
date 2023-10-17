package com.gbikna.sample.bean;

import com.google.gson.annotations.SerializedName;

public class BackEndResponse {

    @SerializedName("MsgUuid")
    private String msgUuid;
    @SerializedName("TransType")
    private int transType;
    @SerializedName("IsApproved")
    private boolean isApproved;
    @SerializedName("MsgErrorCode")
    private int msgErrorCode;
    @SerializedName("MsgErrDecs")
    private String msgErrDesc;          // this is for #39, error code description
    @SerializedName("Rfu")
    private String rfu;
    @SerializedName("BankID")
    private int bankId;


    @SerializedName("BankTransInfoResponse")
    private BankTransInfoResponse bankTransInfoResponse;


    public String getMsgUuid() {
        return msgUuid;
    }

    public void setMsgUuid(String msgUuid) {
        this.msgUuid = msgUuid;
    }

    public int getTransType() {
        return transType;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public int getMsgErrorCode() {
        return msgErrorCode;
    }

    public void setMsgErrorCode(int msgErrorCode) {
        this.msgErrorCode = msgErrorCode;
    }

    public String getMsgErrDesc() {
        return msgErrDesc;
    }

    public void setMsgErrDesc(String msgErrDesc) {
        this.msgErrDesc = msgErrDesc;
    }

    public String getRfu() {
        return rfu;
    }

    public void setRfu(String rfu) {
        this.rfu = rfu;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public BankTransInfoResponse getBankTransInfoResponse() {
        return bankTransInfoResponse;
    }

    public void setBankTransInfoResponse(BankTransInfoResponse bankTransInfoResponse) {
        this.bankTransInfoResponse = bankTransInfoResponse;
    }

    public static class BankTransInfoResponse {

        /* response message */
        @SerializedName("MsgTraceNO")
        private String msgTraceNO;
        @SerializedName("MsgDateTime")
        private String msgDateTime;                    // yyyyMMddhhmmss
        @SerializedName("MsgTermID")
        private String msgTermID;                      // #41 Card Acceptor Terminal ID(TID)
        @SerializedName("MsgMerchID")
        private String msgMerchID;                     // #42 Card Acceptor Identification Code(MID)

        /* Amount related */
        @SerializedName("MsgAmount")
        private String msgAmount;                      // #04 Amount
        @SerializedName("MsgTipAmount")
        private String msgTipAmount;                   // #48/#54 Additional Data – Private: TIP Amount
        @SerializedName("MsgCashbackAmount")
        private String msgCashbackAmount;              // #xx: cashback msgAmount
        @SerializedName("MsgCurrencyCode")
        private String msgCurrencyCode;                // #49 Transaction Currency Code

        @SerializedName("MsgBatchNO")
        private String msgBatchNO;                     // #60.2 System batch Number
        @SerializedName("MsgOrigBatchNO")
        private String msgOrigBatchNO;                 // #61.1 original System batch Number
        @SerializedName("MsgOrigTraceNO")
        private String msgOrigTraceNO;                 // #61.2 original System Trace Audit Number
        @SerializedName("MsgOrigDate")
        private String msgOrigDate;                    // #61.3 original transaction date
        @SerializedName("MsgAcqID")
        private String msgAcqID;                        // #32 Acquiring Institution ID Code
        @SerializedName("MsgRefNO")
        private String msgRefNO;                       // #37 Retrieval Ref. Number
        @SerializedName("MsgAuthCode")
        private String msgAuthCode;                    // #38 Authorisation Identification Res
        @SerializedName("MsgRespCode")
        private String msgRespCode;                    // #39 response code
        @SerializedName("MsgIssuerCode")
        private String msgIssuerCode;                  // #44 Additional Response Data - Issuer code
        @SerializedName("MsgAcquirerCode")
        private String msgAcquirerCode;                // #44 Additional Response Data - Acquirer code

        @SerializedName("TransCUPResponse")
        private TransCupResponse transCupResponse;                // specified data from CUP.

        //EMV Information
        @SerializedName("IccData")
        private String iccData;  // 55 field

        @SerializedName("Rfu")
        private String rfu;

        public String getIccData() {
            return iccData;
        }

        public void setIccData(String iccData) {
            this.iccData = iccData;
        }

        public String getMsgTraceNO() {
            return msgTraceNO;
        }

        public void setMsgTraceNO(String msgTraceNO) {
            this.msgTraceNO = msgTraceNO;
        }

        public String getMsgDateTime() {
            return msgDateTime;
        }

        public void setMsgDateTime(String msgDateTime) {
            this.msgDateTime = msgDateTime;
        }

        public String getMsgTermID() {
            return msgTermID;
        }

        public void setMsgTermID(String msgTermID) {
            this.msgTermID = msgTermID;
        }

        public String getMsgMerchID() {
            return msgMerchID;
        }

        public void setMsgMerchID(String msgMerchID) {
            this.msgMerchID = msgMerchID;
        }

        public String getMsgAmount() {
            return msgAmount;
        }

        public void setMsgAmount(String msgAmount) {
            this.msgAmount = msgAmount;
        }

        public String getMsgTipAmount() {
            return msgTipAmount;
        }

        public void setMsgTipAmount(String msgTipAmount) {
            this.msgTipAmount = msgTipAmount;
        }

        public String getMsgCashbackAmount() {
            return msgCashbackAmount;
        }

        public void setMsgCashbackAmount(String msgCashbackAmount) {
            this.msgCashbackAmount = msgCashbackAmount;
        }

        public String getMsgCurrencyCode() {
            return msgCurrencyCode;
        }

        public void setMsgCurrencyCode(String msgCurrencyCode) {
            this.msgCurrencyCode = msgCurrencyCode;
        }

        public String getMsgBatchNO() {
            return msgBatchNO;
        }

        public void setMsgBatchNO(String msgBatchNO) {
            this.msgBatchNO = msgBatchNO;
        }

        public String getMsgOrigBatchNO() {
            return msgOrigBatchNO;
        }

        public void setMsgOrigBatchNO(String msgOrigBatchNO) {
            this.msgOrigBatchNO = msgOrigBatchNO;
        }

        public String getMsgOrigTraceNO() {
            return msgOrigTraceNO;
        }

        public void setMsgOrigTraceNO(String msgOrigTraceNO) {
            this.msgOrigTraceNO = msgOrigTraceNO;
        }

        public String getMsgOrigDate() {
            return msgOrigDate;
        }

        public void setMsgOrigDate(String msgOrigDate) {
            this.msgOrigDate = msgOrigDate;
        }

        public String getMsgAcqID() {
            return msgAcqID;
        }

        public void setMsgAcqID(String msgAcqID) {
            this.msgAcqID = msgAcqID;
        }

        public String getMsgRefNO() {
            return msgRefNO;
        }

        public void setMsgRefNO(String msgRefNO) {
            this.msgRefNO = msgRefNO;
        }

        public String getMsgAuthCode() {
            return msgAuthCode;
        }

        public void setMsgAuthCode(String msgAuthCode) {
            this.msgAuthCode = msgAuthCode;
        }

        public String getMsgRespCode() {
            return msgRespCode;
        }

        public void setMsgRespCode(String msgRespCode) {
            this.msgRespCode = msgRespCode;
        }

        public String getMsgIssuerCode() {
            return msgIssuerCode;
        }

        public void setMsgIssuerCode(String msgIssuerCode) {
            this.msgIssuerCode = msgIssuerCode;
        }

        public String getMsgAcquirerCode() {
            return msgAcquirerCode;
        }

        public void setMsgAcquirerCode(String msgAcquirerCode) {
            this.msgAcquirerCode = msgAcquirerCode;
        }

        public String getRfu() {
            return rfu;
        }

        public void setRfu(String rfu) {
            this.rfu = rfu;
        }

        public TransCupResponse getTransCupResponse() {
            return transCupResponse;
        }

        public void setTransCupResponse(TransCupResponse transCupResponse) {
            this.transCupResponse = transCupResponse;
        }
    }

    public static class TransCupResponse {
        @SerializedName("CUPRRN")
        private String cupRrn;
        @SerializedName("CUPSettlementDate")
        private String cupSettlementDate;
        @SerializedName("CUPTraceNo")
        private String cupTraceNo;


        public String getCupRrn() {
            return cupRrn;
        }

        public void setCupRrn(String cupRrn) {
            this.cupRrn = cupRrn;
        }

        public String getCupSettlementDate() {
            return cupSettlementDate;
        }

        public void setCupSettlementDate(String cupSettlementDate) {
            this.cupSettlementDate = cupSettlementDate;
        }

        public String getCupTraceNo() {
            return cupTraceNo;
        }

        public void setCupTraceNo(String cupTraceNo) {
            this.cupTraceNo = cupTraceNo;
        }
    }

}
