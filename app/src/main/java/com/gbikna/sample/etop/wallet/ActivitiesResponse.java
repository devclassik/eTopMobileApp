package com.gbikna.sample.etop.wallet;

public class ActivitiesResponse {
    String tid;
    String amount;
    String oldamount;
    String newamount;
    String transmode;
    String transinfo;
    String timestamp;
    String tousedate;
    String status;
    String id;

    public ActivitiesResponse(String tid, String amount, String oldamount, String newamount, String transmode, String transinfo, String timestamp, String tousedate, String status, String id) {
        this.tid = tid;
        this.amount = amount;
        this.oldamount = oldamount;
        this.newamount = newamount;
        this.transmode = transmode;
        this.transinfo = transinfo;
        this.timestamp = timestamp;
        this.tousedate = tousedate;
        this.status = status;
        this.id = id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOldamount() {
        return oldamount;
    }

    public void setOldamount(String oldamount) {
        this.oldamount = oldamount;
    }

    public String getNewamount() {
        return newamount;
    }

    public void setNewamount(String newamount) {
        this.newamount = newamount;
    }

    public String getTransmode() {
        return transmode;
    }

    public void setTransmode(String transmode) {
        this.transmode = transmode;
    }

    public String getTransinfo() {
        return transinfo;
    }

    public void setTransinfo(String transinfo) {
        this.transinfo = transinfo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTousedate() {
        return tousedate;
    }

    public void setTousedate(String tousedate) {
        this.tousedate = tousedate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
