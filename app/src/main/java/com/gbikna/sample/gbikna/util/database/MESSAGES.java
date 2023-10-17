package com.gbikna.sample.gbikna.util.database;

public class MESSAGES {
    int id;
    String message;
    String priority;
    String walletids;
    String serverid;
    String status;
    String hasview;

    public MESSAGES(String message, String priority, String walletids, String serverid, String status, String hasview) {
        this.message = message;
        this.priority = priority;
        this.walletids = walletids;
        this.serverid = serverid;
        this.status = status;
        this.hasview = hasview;
    }


    public MESSAGES() {

    }

    public String getHasview() {
        return hasview;
    }

    public void setHasview(String hasview) {
        this.hasview = hasview;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getWalletids() {
        return walletids;
    }

    public void setWalletids(String walletids) {
        this.walletids = walletids;
    }

    public String getServerid() {
        return serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = serverid;
    }
}
