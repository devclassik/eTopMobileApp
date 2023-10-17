package com.gbikna.sample.etop.transactions;

public class TransactionHistory {
    private String agentamount;
    private String amount;
    private String busname;
    private String category;
    private String city;
    private String destination;
    private String expirydate;
    private String fee;
    private String lga;
    private String pan;
    private String ref;
    private String request;
    private String response;
    private String rrn;
    private String stan;
    private String state;
    private String status;
    private String superagentamount;
    private String tid;
    private String tmsamount;
    private String transname;
    private String username;
    private String walletid;
    private String timestamp;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public TransactionHistory(Long id, String agentamount, String amount, String busname, String category, String city, String destination, String expirydate, String fee, String lga, String pan, String ref, String request, String response, String rrn, String stan, String state, String status, String superagentamount, String tid, String tmsamount, String transname, String username, String walletid, String timestamp) {
        this.id = id;
        this.agentamount = agentamount;
        this.amount = amount;
        this.busname = busname;
        this.category = category;
        this.city = city;
        this.destination = destination;
        this.expirydate = expirydate;
        this.fee = fee;
        this.lga = lga;
        this.pan = pan;
        this.ref = ref;
        this.request = request;
        this.response = response;
        this.rrn = rrn;
        this.stan = stan;
        this.state = state;
        this.status = status;
        this.superagentamount = superagentamount;
        this.tid = tid;
        this.tmsamount = tmsamount;
        this.transname = transname;
        this.username = username;
        this.walletid = walletid;
        this.timestamp = timestamp;
    }

    public String getAgentamount() {
        return agentamount;
    }

    public void setAgentamount(String agentamount) {
        this.agentamount = agentamount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBusname() {
        return busname;
    }

    public void setBusname(String busname) {
        this.busname = busname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuperagentamount() {
        return superagentamount;
    }

    public void setSuperagentamount(String superagentamount) {
        this.superagentamount = superagentamount;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTmsamount() {
        return tmsamount;
    }

    public void setTmsamount(String tmsamount) {
        this.tmsamount = tmsamount;
    }

    public String getTransname() {
        return transname;
    }

    public void setTransname(String transname) {
        this.transname = transname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWalletid() {
        return walletid;
    }

    public void setWalletid(String walletid) {
        this.walletid = walletid;
    }
}
