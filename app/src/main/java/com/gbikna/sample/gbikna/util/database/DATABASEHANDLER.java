package com.gbikna.sample.gbikna.util.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DATABASEHANDLER extends SQLiteOpenHelper {
    private static final String TAG = DATABASEHANDLER.class.getSimpleName();
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "POSDB";
    //Host Database
    private static final String TABLE_NAME = "hosts";
    private static final String HOSTKEYID = "id";
    private static final String HOSTNUMBER = "num";
    private static final String HOSTNAME = "hostname";
    private static final String HOSTIP = "hostip";
    private static final String HOSTPORT = "hostport";
    private static final String HOSTSSL = "hostssl";
    private static final String HOSTFRIENDLYNAME = "hostfrendlyname";
    private static final String HOSTREMARKS = "remarks";
    private static final String HOSTCTMK = "ctmk";
    private static final String HOSTENCMSK = "encMSK";
    private static final String HOSTENCSK = "encSK";
    private static final String HOSTENCPK = "encPK";
    private static final String HOSTCLRMSK = "clrMSK";
    private static final String HOSTCLRSK = "clrSK";
    private static final String HOSTCLRPK = "clrPK";
    private static final String HOSTCTMSDATETIME = "ctmsdatetime";
    private static final String HOSTMID = "mid";
    private static final String HOSTTIMEOUT = "timeout";
    private static final String HOSTCURRENCYCODE = "currencycode";
    private static final String HOSTCOUNTRYCODE = "countrycode";
    private static final String HOSTCALLHOME = "callhome";
    private static final String HOSTMNL = "mnl";
    private static final String HOSTMCC = "mcc";
    private static final String COLUMN_TIMESTAMP = "time";
    String CREATE_HOST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + HOSTKEYID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + HOSTNUMBER + " TEXT NOT NULL UNIQUE,"
            + HOSTNAME + " TEXT UNIQUE,"
            + HOSTIP + " TEXT,"
            + HOSTPORT + " INTEGER,"
            + HOSTSSL + " BOOLEAN,"
            + HOSTFRIENDLYNAME + " TEXT,"
            + HOSTREMARKS + " TEXT,"
            + HOSTCTMK + " TEXT,"
            + HOSTENCMSK + " TEXT,"
            + HOSTENCSK + " TEXT,"
            + HOSTENCPK + " TEXT,"
            + HOSTCLRMSK + " TEXT,"
            + HOSTCLRSK + " TEXT,"
            + HOSTCLRPK + " TEXT,"
            + HOSTCTMSDATETIME + " TEXT,"
            + HOSTMID + " TEXT,"
            + HOSTTIMEOUT + " TEXT,"
            + HOSTCURRENCYCODE + " TEXT,"
            + HOSTCOUNTRYCODE + " TEXT,"
            + HOSTCALLHOME + " TEXT,"
            + HOSTMNL + " TEXT,"
            + HOSTMCC + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
    //EOD DATABASE
    private static final String TABLE_NAME_EOD = "eod";
    private static final String ID = "id";
    private static final String MTI = "mti";
    private static final String LOCALDATETIME = "localdatetime";
    private static final String TRACK2 = "track2";
    private static final String PROCCODE = "proccode";
    private static final String AMOUNT = "amount";
    private static final String RRN = "rrn";
    private static final String PANSEQNUM = "panseqnum";
    private static final String ENTRYMODE = "entrymode";
    private static final String POSDATACODE = "posdatacode";
    private static final String STAN = "stan";
    private static final String AUTHCODE = "authcode";
    private static final String CONDCODE = "condcode";
    private static final String RESPONSE = "respcode";
    private static final String ACQUIRERID = "acquirerid";
    private static final String RECEIPTNO = "receiptno";
    private static final String TXNTYPE = "txntype";
    private static final String RDATE = "rdate";
    private static final String RTIME = "rtime";
    private static final String APPNAME = "appname";
    private static final String EXPDATE = "expdate";
    private static final String MASKEDPAN = "maskedpan";
    private static final String HOLDERNAME = "holdername";
    private static final String AID = "aid";
    private static final String CASHBACK = "cashback";
    private static final String TOTAL = "total";
    private static final String PAYMENTDETAILS = "paymentdetails";
    private static final String DATE = "date";
    String CREATE_HOST_TABLE_EOD = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_EOD + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MTI + " TEXT,"
            + LOCALDATETIME + " TEXT,"
            + TRACK2 + " TEXT,"
            + PROCCODE + " TEXT,"
            + AMOUNT + " TEXT,"
            + RRN + " TEXT,"
            + PANSEQNUM + " TEXT,"
            + ENTRYMODE + " TEXT,"
            + POSDATACODE + " TEXT,"
            + STAN + " TEXT,"
            + AUTHCODE + " TEXT,"
            + CONDCODE + " TEXT,"
            + RESPONSE + " TEXT,"
            + ACQUIRERID + " TEXT,"
            + RECEIPTNO + " TEXT,"
            + TXNTYPE + " TEXT,"
            + RDATE + " TEXT,"
            + RTIME + " TEXT,"
            + APPNAME + " TEXT,"
            + EXPDATE + " TEXT,"
            + MASKEDPAN + " TEXT,"
            + HOLDERNAME + " TEXT,"
            + AID + " TEXT,"
            + CASHBACK + " TEXT,"
            + TOTAL + " TEXT,"
            + PAYMENTDETAILS + " TEXT,"
            + DATE + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
    //CALLHOME DATABASE
    private static final String TABLE_NAME_CH = "callhome";
    private static final String CID = "id";
    private static final String CSTAN = "stan";
    private static final String CMASKEDPAN = "maskedpan";
    private static final String CAUTHCODE = "authcode";
    private static final String CRRN = "rrn";
    private static final String CAMOUNT = "amt";
    private static final String CDATETIME = "datetime";
    private static final String CMTI = "mti";
    private static final String CPROCCODE = "proccode";
    private static final String CRESPONSE = "response";
    private static final String CVENDORID = "vendorid";
    private static final String CFIELD62 = "fieldab";
    private static final String CAPPNAME = "appname";
    private static final String CCARDNAME = "cardname";
    private static final String CPAYMENTDETAILS = "paymentdetails";
    String CREATE_HOST_TABLE_CH = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CH + "("
            + CID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CSTAN + " TEXT,"
            + CMASKEDPAN + " TEXT,"
            + CAUTHCODE + " TEXT,"
            + CRRN + " TEXT,"
            + CAMOUNT + " TEXT,"
            + CDATETIME + " TEXT,"
            + CMTI + " TEXT,"
            + CPROCCODE + " TEXT,"
            + CRESPONSE + " TEXT,"
            + CVENDORID + " TEXT,"
            + CFIELD62 + " TEXT,"
            + CAPPNAME + " TEXT,"
            + CCARDNAME + " TEXT,"
            + CPAYMENTDETAILS + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";


    private static final String TABLE_NAME_MS = "messages";
    private static final String MESSAGEID = "id";
    private static final String MESSAGE = "message";
    private static final String PRIORITY = "priority";
    private static final String WALLETIDS = "walletids";
    private static final String SERVERID = "serverid";
    private static final String STATUS = "status";
    private static final String HASVIEW = "hasview";
    String CREATE_HOST_TABLE_MS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_MS + "("
            + MESSAGEID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MESSAGE + " TEXT,"
            + PRIORITY + " TEXT,"
            + WALLETIDS + " TEXT,"
            + SERVERID + " TEXT,"
            + STATUS + " TEXT,"
            + HASVIEW + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";

    public DATABASEHANDLER(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "DONE WITH DATABASEHANDLER CONSTRUCTOR");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "DATABASE ONCREATE....");
        sqLiteDatabase.execSQL(CREATE_HOST_TABLE);
        sqLiteDatabase.execSQL(CREATE_HOST_TABLE_EOD);
        sqLiteDatabase.execSQL(CREATE_HOST_TABLE_CH);
        sqLiteDatabase.execSQL(CREATE_HOST_TABLE_MS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(TAG, "DATABASE ON UPGRADE....: " + i + ". New: " + i1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EOD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MS);
        onCreate(sqLiteDatabase);
    }

    public void deleteHost()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public void HostInit(String num) {
        Log.i(TAG, "INSERTED HOST INIT: " + num);
        /*if(getHost(num) > 0)
        {
            Log.i(TAG, "HOST ALREADY ADDED...");
            return;
        }*/
        Log.i(TAG, "INSERTED HOST INIT 2");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HOSTNUMBER, num);
        Log.i(TAG, "ABOUT INSERTING: " + num);
        long ret = db.insert(TABLE_NAME, null, values);
        Log.i(TAG, "INSERTED: 1 " + ret);
        db.close();
    }

    public void InsertMessages(String message, String priority, String walletids, String serverid, String status, String hasView) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MESSAGE, message);
        values.put(PRIORITY, priority);
        values.put(WALLETIDS, walletids);
        values.put(SERVERID, serverid);
        values.put(STATUS, status);
        values.put(HASVIEW, hasView);
        long ret = db.insert(TABLE_NAME_MS, null, values);
        Log.i(TAG, "INSERTED: " + ret);
        db.close();
    }

    public List<MESSAGES> getAllMessage() {
        List<MESSAGES> contactList = new ArrayList<MESSAGES>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_MS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {});
        if (cursor.moveToFirst()) {
            do {
                MESSAGES host = new MESSAGES();
                if(cursor.getString(0) != null)
                    host.setId(Integer.parseInt(cursor.getString(0)));
                if(cursor.getString(1) != null)
                    host.setMessage(cursor.getString(1));
                if(cursor.getString(2) != null)
                    host.setPriority(cursor.getString(2));
                if(cursor.getString(3) != null)
                    host.setWalletids(cursor.getString(3));
                if(cursor.getString(4) != null)
                    host.setServerid(cursor.getString(4));
                if(cursor.getString(5) != null)
                    host.setStatus(cursor.getString(5));
                if(cursor.getString(6) != null)
                    host.setHasview(cursor.getString(6));
                contactList.add(host);
            } while (cursor.moveToNext());
        }
        Log.i(TAG, "PRESSED getAllHost: d");
        cursor.close();
        Log.i(TAG, "PRESSED getAllHost: e");
        db.close();
        Log.i(TAG, "PRESSED getAllHost: f");
        return contactList;
    }

    public void upMessage(String serverid, String hasview) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HASVIEW, hasview);
        long ret = db.update(TABLE_NAME_MS, values, SERVERID + " = ?", new String[] { String.valueOf(serverid) });
        db.close();
        Log.i(TAG, "MESSAGES UPDATE: " + ret);
    }


    public void HostFirstStep(String host, String hostname, String hostip, Integer hostport, Boolean hostssl, String hostfriendlyname, String hostremarks) {
        Log.i(TAG, "DATABASE HostFirstStep: 1");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HOSTNAME, hostname);
        values.put(HOSTIP, hostip);
        values.put(HOSTPORT, hostport);
        values.put(HOSTSSL, hostssl);
        values.put(HOSTFRIENDLYNAME, hostfriendlyname);
        values.put(HOSTREMARKS, hostremarks);
        Log.i(TAG, "DATABASE HostFirstStep: 1A - " + HOSTNUMBER);
        long ret = db.update(TABLE_NAME, values, HOSTNUMBER + " = ?", new String[] { String.valueOf(host) });
        Log.i(TAG, "INSERTED HOST FIRST STEP: " + ret + " - " + HOSTNUMBER);
        db.close();
        Log.i(TAG, "DATABASE HostFirstStep: 1C");
    }

    public void HostSecondStep(String host, String ctmk, String encmsk, String encsk, String encpk,
                               String clrmsk, String clrsk, String clrpk) {
        Log.i(TAG, "DATABASE HostFirstStep: 2");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HOSTCTMK, ctmk);
        values.put(HOSTENCMSK, encmsk);
        values.put(HOSTENCSK, encsk);
        values.put(HOSTENCPK, encpk);
        values.put(HOSTCLRMSK, clrmsk);
        values.put(HOSTCLRSK, clrsk);
        values.put(HOSTCLRPK, clrpk);
        Log.i(TAG, "VALUES: " + values);
        long ret = db.update(TABLE_NAME, values, HOSTNUMBER + " = ?", new String[] { String.valueOf(host) });
        Log.i(TAG, "INSERTED HOST FIRST STEP 2: " + ret + " - " + HOSTNUMBER);
        Log.i(TAG, "DATABASE HostFirstStep: 2A");
        Log.i(TAG, "UPDATE FOR: " + host);
        db.close();
        Log.i(TAG, "DATABASE HostFirstStep: 2C");
    }

    public void HostThirdStep(String host, String ctmkdatetime, String mid, String timeout, String currencycode, String countrycode,
                              String callhome, String mnl, String mcc) {
        Log.i(TAG, "DATABASE HostFirstStep: 3");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HOSTCTMSDATETIME, ctmkdatetime);
        values.put(HOSTMID, mid);
        values.put(HOSTTIMEOUT, timeout);
        values.put(HOSTCURRENCYCODE, currencycode);
        values.put(HOSTCOUNTRYCODE, countrycode);
        values.put(HOSTCALLHOME, callhome);
        values.put(HOSTMNL, mnl);
        values.put(HOSTMCC, mcc);
        Log.i(TAG, "DATABASE HostFirstStep: 3B");
        long ret = db.update(TABLE_NAME, values, HOSTNUMBER + " = ?", new String[] { String.valueOf(host) });
        Log.i(TAG, "INSERTED HOST FIRST STEP 2: " + ret);
        db.close();
        Log.i(TAG, "DATABASE HostFirstStep: 3C");
    }

    public void deleteHost(String num) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, HOSTNUMBER + " = ?",
                new String[] { String.valueOf(num) });
        db.close();
    }

    public List<HOSTKEYS> getAllHost(String num) {
        Log.i(TAG, "PRESSED getAllHost: " + num);
        List<HOSTKEYS> contactList = new ArrayList<HOSTKEYS>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + HOSTNUMBER + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(num) });
        Log.i(TAG, "PRESSED getAllHost b: " + cursor);
        if (cursor.moveToFirst()) {
            Log.i(TAG, "PRESSED getAllHost c: ");
            do {
                HOSTKEYS host = new HOSTKEYS();
                if(cursor.getString(0) != null)
                    host.setID(Integer.parseInt(cursor.getString(0)));
                if(cursor.getString(1) != null)
                    host.setNum(cursor.getString(1));
                if(cursor.getString(2) != null)
                    host.sethostname(cursor.getString(2));
                if(cursor.getString(3) != null)
                    host.sethostip(cursor.getString(3));
                if(cursor.getString(4) != null)
                    host.sethostport(Integer.parseInt(cursor.getString(4)));
                if(cursor.getString(5) != null)
                {
                    if(cursor.getString(5).equalsIgnoreCase("1"))
                        host.sethostssl(true);
                    else
                        host.sethostssl(false);
                }
                Log.i("WISDOM", "WISDOM QUERY: " + cursor.getString(5));
                if(cursor.getString(6) != null)
                    host.sethostfrendlyname(cursor.getString(6));
                if(cursor.getString(7) != null)
                    host.setremarks(cursor.getString(7));
                if(cursor.getString(8) != null)
                    host.setctmk(cursor.getString(8));
                if(cursor.getString(9) != null)
                    host.setencMSK(cursor.getString(9));
                if(cursor.getString(10) != null)
                    host.setencSK(cursor.getString(10));
                if(cursor.getString(11) != null)
                    host.setencPK(cursor.getString(11));
                if(cursor.getString(12) != null)
                    host.setclrMSK(cursor.getString(12));
                if(cursor.getString(13) != null)
                    host.setclrSK(cursor.getString(13));
                if(cursor.getString(14) != null)
                    host.setclrPK(cursor.getString(14));
                if(cursor.getString(15) != null)
                    host.setctmsdatetime(cursor.getString(15));
                if(cursor.getString(16) != null)
                    host.setmid(cursor.getString(16));
                if(cursor.getString(17) != null)
                    host.settimeout(cursor.getString(17));
                if(cursor.getString(18) != null)
                    host.setcurrencycode(cursor.getString(18));
                if(cursor.getString(19) != null)
                    host.setcountrycode(cursor.getString(19));
                if(cursor.getString(20) != null)
                    host.setcallhome(cursor.getString(20));
                if(cursor.getString(21) != null)
                    host.setmnl(cursor.getString(21));
                if(cursor.getString(22) != null)
                    host.setmcc(cursor.getString(22));
                contactList.add(host);
            } while (cursor.moveToNext());
        }
        Log.i(TAG, "PRESSED getAllHost: d");
        cursor.close();
        Log.i(TAG, "PRESSED getAllHost: e");
        db.close();
        Log.i(TAG, "PRESSED getAllHost: f");
        return contactList;
    }

    int getHost(String num) {
        Log.i(TAG, "GET HOST DETAILS: " + num);
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + HOSTNUMBER + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(num) });
        int ret = cursor.getCount();
        Log.i(TAG, "GET HOST FIRST STEP: " + ret);
        cursor.close();
        db.close();
        return ret;
    }

    //EOD Database
    public void EODFIRST(String mti, String localdatetime, String track2, String proccode, String amount,
                         String rrn, String panseqnum, String entrymode, String posdatacode, String stan,
                         String condcode, String acquirerid,
                         String date, String txntype, String appname,
                         String expdate, String maskedpan, String holdername, String aid, String cashback, String total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MTI, mti);
        values.put(LOCALDATETIME, localdatetime);
        values.put(TRACK2, track2);
        values.put(PROCCODE, proccode);
        values.put(AMOUNT, amount);
        values.put(RRN, rrn);
        values.put(PANSEQNUM, panseqnum);
        values.put(ENTRYMODE, entrymode);
        values.put(POSDATACODE, posdatacode);
        values.put(STAN, stan);
        values.put(CONDCODE, condcode);
        values.put(ACQUIRERID, acquirerid);//Use this for phone if pay with phone
        values.put(DATE, date);
        values.put(TXNTYPE, txntype);
        values.put(APPNAME, appname);
        values.put(EXPDATE, expdate);
        values.put(MASKEDPAN, maskedpan);
        values.put(HOLDERNAME, holdername);
        values.put(AID, aid);
        values.put(CASHBACK, cashback);
        values.put(TOTAL, total);
        long ret = db.insert(TABLE_NAME_EOD, null, values);
        Log.i(TAG, "INSERTED: 2" + ret);
        db.close();
    }

    public void UPDATEEOD(String authcode, String respcode, String receiptno, String rdate, String rtime, String paymentdetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AUTHCODE, authcode);
        values.put(RESPONSE, respcode);
        values.put(RECEIPTNO, receiptno);
        //USE YYYYMMDD
        values.put(RDATE, rdate);
        values.put(RTIME, rtime);
        values.put(PAYMENTDETAILS, paymentdetails);
        db.update(TABLE_NAME_EOD, values, ID + " = (SELECT MAX(id) FROM " + TABLE_NAME_EOD + ")", null);
        db.close();
    }

    public List<EOD> getByReceipt(String receiptnum) {
        List<EOD> contactList = new ArrayList<EOD>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_EOD + " WHERE " + RECEIPTNO + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(receiptnum) });
        if (cursor.moveToFirst()) {
            do {
                EOD me = new EOD();
                if(cursor.getString(0) != null)
                    me.setId(Integer.parseInt(cursor.getString(0)));
                if(cursor.getString(1) != null)
                    me.setMti(cursor.getString(1));
                if(cursor.getString(2) != null)
                    me.setLocaldatetime(cursor.getString(2));
                if(cursor.getString(3) != null)
                    me.setTrack2(cursor.getString(3));
                if(cursor.getString(4) != null)
                    me.setProccode(cursor.getString(4));
                if(cursor.getString(5) != null)
                    me.setAmount(cursor.getString(5));
                if(cursor.getString(6) != null)
                    me.setRrn(cursor.getString(6));
                if(cursor.getString(7) != null)
                    me.setPanseqnum(cursor.getString(7));
                if(cursor.getString(8) != null)
                    me.setEntrymode(cursor.getString(8));
                if(cursor.getString(9) != null)
                    me.setPosdatacode(cursor.getString(9));
                if(cursor.getString(10) != null)
                    me.setStan(cursor.getString(10));
                if(cursor.getString(11) != null)
                    me.setAuthcode(cursor.getString(11));
                if(cursor.getString(12) != null)
                    me.setCondcode(cursor.getString(12));
                if(cursor.getString(13) != null)
                    me.setRespcode(cursor.getString(13));
                if(cursor.getString(14) != null)
                    me.setAcquirerid(cursor.getString(14));
                if(cursor.getString(15) != null)
                    me.setReceiptno(cursor.getString(15));
                if(cursor.getString(16) != null)
                    me.setTxntype(cursor.getString(16));
                if(cursor.getString(17) != null)
                    me.setRdate(cursor.getString(17));
                if(cursor.getString(18) != null)
                    me.setRtime(cursor.getString(18));
                if(cursor.getString(19) != null)
                    me.setAppname(cursor.getString(19));
                if(cursor.getString(20) != null)
                    me.setExpdate(cursor.getString(20));
                if(cursor.getString(21) != null)
                    me.setMaskedpan(cursor.getString(21));
                if(cursor.getString(22) != null)
                    me.setHoldername(cursor.getString(22));
                if(cursor.getString(23) != null)
                    me.setAid(cursor.getString(23));
                if(cursor.getString(24) != null)
                    me.setCashback(cursor.getString(24));
                if(cursor.getString(25) != null)
                    me.setTotal(cursor.getString(25));
                if(cursor.getString(26) != null)
                    me.setPaymentdetails(cursor.getString(26));
                if(cursor.getString(27) != null)
                    me.setTimestamp(cursor.getString(27));
                if(cursor.getString(28) != null)
                    me.setDate(cursor.getString(28));
                contactList.add(me);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }

    public List<EOD> getLastReceipt() {
        List<EOD> contactList = new ArrayList<EOD>();
        //db.update(TABLE_NAME_EOD, values, ID + " = (SELECT MAX(id) FROM " + TABLE_NAME_EOD + ")", null);
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_EOD + " WHERE " + "ID = (SELECT MAX(id) FROM " + TABLE_NAME_EOD + ")";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                EOD me = new EOD();
                if(cursor.getString(0) != null)
                    me.setId(Integer.parseInt(cursor.getString(0)));
                if(cursor.getString(1) != null)
                    me.setMti(cursor.getString(1));
                if(cursor.getString(2) != null)
                    me.setLocaldatetime(cursor.getString(2));
                if(cursor.getString(3) != null)
                    me.setTrack2(cursor.getString(3));
                if(cursor.getString(4) != null)
                    me.setProccode(cursor.getString(4));
                if(cursor.getString(5) != null)
                    me.setAmount(cursor.getString(5));
                if(cursor.getString(6) != null)
                    me.setRrn(cursor.getString(6));
                if(cursor.getString(7) != null)
                    me.setPanseqnum(cursor.getString(7));
                if(cursor.getString(8) != null)
                    me.setEntrymode(cursor.getString(8));
                if(cursor.getString(9) != null)
                    me.setPosdatacode(cursor.getString(9));
                if(cursor.getString(10) != null)
                    me.setStan(cursor.getString(10));
                if(cursor.getString(11) != null)
                    me.setAuthcode(cursor.getString(11));
                if(cursor.getString(12) != null)
                    me.setCondcode(cursor.getString(12));
                if(cursor.getString(13) != null)
                    me.setRespcode(cursor.getString(13));
                if(cursor.getString(14) != null)
                    me.setAcquirerid(cursor.getString(14));
                if(cursor.getString(15) != null)
                    me.setReceiptno(cursor.getString(15));
                if(cursor.getString(16) != null)
                    me.setTxntype(cursor.getString(16));
                if(cursor.getString(17) != null)
                    me.setRdate(cursor.getString(17));
                if(cursor.getString(18) != null)
                    me.setRtime(cursor.getString(18));
                if(cursor.getString(19) != null)
                    me.setAppname(cursor.getString(19));
                if(cursor.getString(20) != null)
                    me.setExpdate(cursor.getString(20));
                if(cursor.getString(21) != null)
                    me.setMaskedpan(cursor.getString(21));
                if(cursor.getString(22) != null)
                    me.setHoldername(cursor.getString(22));
                if(cursor.getString(23) != null)
                    me.setAid(cursor.getString(23));
                if(cursor.getString(24) != null)
                    me.setCashback(cursor.getString(24));
                if(cursor.getString(25) != null)
                    me.setTotal(cursor.getString(25));
                if(cursor.getString(26) != null)
                    me.setPaymentdetails(cursor.getString(26));
                if(cursor.getString(27) != null)
                    me.setTimestamp(cursor.getString(27));
                if(cursor.getString(28) != null)
                    me.setDate(cursor.getString(28));
                contactList.add(me);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }

    public List<EOD> getByDateRange(String start, String stop) {
        List<EOD> contactList = new ArrayList<EOD>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_EOD + " WHERE " + RDATE + " >= ? AND " + RDATE + " <= ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(start), String.valueOf(stop) });
        if (cursor.moveToFirst()) {
            do {
                EOD me = new EOD();
                if(cursor.getString(0) != null)
                    me.setId(Integer.parseInt(cursor.getString(0)));
                if(cursor.getString(1) != null)
                    me.setMti(cursor.getString(1));
                if(cursor.getString(2) != null)
                    me.setLocaldatetime(cursor.getString(2));
                if(cursor.getString(3) != null)
                    me.setTrack2(cursor.getString(3));
                if(cursor.getString(4) != null)
                    me.setProccode(cursor.getString(4));
                if(cursor.getString(5) != null)
                    me.setAmount(cursor.getString(5));
                if(cursor.getString(6) != null)
                    me.setRrn(cursor.getString(6));
                if(cursor.getString(7) != null)
                    me.setPanseqnum(cursor.getString(7));
                if(cursor.getString(8) != null)
                    me.setEntrymode(cursor.getString(8));
                if(cursor.getString(9) != null)
                    me.setPosdatacode(cursor.getString(9));
                if(cursor.getString(10) != null)
                    me.setStan(cursor.getString(10));
                if(cursor.getString(11) != null)
                    me.setAuthcode(cursor.getString(11));
                if(cursor.getString(12) != null)
                    me.setCondcode(cursor.getString(12));
                if(cursor.getString(13) != null)
                    me.setRespcode(cursor.getString(13));
                if(cursor.getString(14) != null)
                    me.setAcquirerid(cursor.getString(14));
                if(cursor.getString(15) != null)
                    me.setReceiptno(cursor.getString(15));
                if(cursor.getString(16) != null)
                    me.setTxntype(cursor.getString(16));
                if(cursor.getString(17) != null)
                    me.setRdate(cursor.getString(17));
                if(cursor.getString(18) != null)
                    me.setRtime(cursor.getString(18));
                if(cursor.getString(19) != null)
                    me.setAppname(cursor.getString(19));
                if(cursor.getString(20) != null)
                    me.setExpdate(cursor.getString(20));
                if(cursor.getString(21) != null)
                    me.setMaskedpan(cursor.getString(21));
                if(cursor.getString(22) != null)
                    me.setHoldername(cursor.getString(22));
                if(cursor.getString(23) != null)
                    me.setAid(cursor.getString(23));
                if(cursor.getString(24) != null)
                    me.setCashback(cursor.getString(24));
                if(cursor.getString(25) != null)
                    me.setTotal(cursor.getString(25));
                if(cursor.getString(26) != null)
                    me.setPaymentdetails(cursor.getString(26));
                if(cursor.getString(27) != null)
                    me.setTimestamp(cursor.getString(27));
                if(cursor.getString(28) != null)
                    me.setDate(cursor.getString(28));
                contactList.add(me);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }

    public List<EOD> getByDate(String date) {
        List<EOD> contactList = new ArrayList<EOD>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_EOD + " WHERE " + RDATE + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(date) });
        if (cursor.moveToFirst()) {
            do {
                EOD me = new EOD();
                if(cursor.getString(0) != null)
                    me.setId(Integer.parseInt(cursor.getString(0)));
                if(cursor.getString(1) != null)
                    me.setMti(cursor.getString(1));
                if(cursor.getString(2) != null)
                    me.setLocaldatetime(cursor.getString(2));
                if(cursor.getString(3) != null)
                    me.setTrack2(cursor.getString(3));
                if(cursor.getString(4) != null)
                    me.setProccode(cursor.getString(4));
                if(cursor.getString(5) != null)
                    me.setAmount(cursor.getString(5));
                if(cursor.getString(6) != null)
                    me.setRrn(cursor.getString(6));
                if(cursor.getString(7) != null)
                    me.setPanseqnum(cursor.getString(7));
                if(cursor.getString(8) != null)
                    me.setEntrymode(cursor.getString(8));
                if(cursor.getString(9) != null)
                    me.setPosdatacode(cursor.getString(9));
                if(cursor.getString(10) != null)
                    me.setStan(cursor.getString(10));
                if(cursor.getString(11) != null)
                    me.setAuthcode(cursor.getString(11));
                if(cursor.getString(12) != null)
                    me.setCondcode(cursor.getString(12));
                if(cursor.getString(13) != null)
                    me.setRespcode(cursor.getString(13));
                if(cursor.getString(14) != null)
                    me.setAcquirerid(cursor.getString(14));
                if(cursor.getString(15) != null)
                    me.setReceiptno(cursor.getString(15));
                if(cursor.getString(16) != null)
                    me.setTxntype(cursor.getString(16));
                if(cursor.getString(17) != null)
                    me.setRdate(cursor.getString(17));
                if(cursor.getString(18) != null)
                    me.setRtime(cursor.getString(18));
                if(cursor.getString(19) != null)
                    me.setAppname(cursor.getString(19));
                if(cursor.getString(20) != null)
                    me.setExpdate(cursor.getString(20));
                if(cursor.getString(21) != null)
                    me.setMaskedpan(cursor.getString(21));
                if(cursor.getString(22) != null)
                    me.setHoldername(cursor.getString(22));
                if(cursor.getString(23) != null)
                    me.setAid(cursor.getString(23));
                if(cursor.getString(24) != null)
                    me.setCashback(cursor.getString(24));
                if(cursor.getString(25) != null)
                    me.setTotal(cursor.getString(25));
                if(cursor.getString(26) != null)
                    me.setPaymentdetails(cursor.getString(26));
                if(cursor.getString(27) != null)
                    me.setTimestamp(cursor.getString(27));
                if(cursor.getString(28) != null)
                    me.setDate(cursor.getString(28));
                contactList.add(me);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }

    public List<EOD> getAllEod() {
        List<EOD> contactList = new ArrayList<EOD>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_EOD;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                EOD me = new EOD();
                if(cursor.getString(0) != null)
                    me.setId(Integer.parseInt(cursor.getString(0)));
                if(cursor.getString(1) != null)
                    me.setMti(cursor.getString(1));
                if(cursor.getString(2) != null)
                    me.setLocaldatetime(cursor.getString(2));
                if(cursor.getString(3) != null)
                    me.setTrack2(cursor.getString(3));
                if(cursor.getString(4) != null)
                    me.setProccode(cursor.getString(4));
                if(cursor.getString(5) != null)
                    me.setAmount(cursor.getString(5));
                if(cursor.getString(6) != null)
                    me.setRrn(cursor.getString(6));
                if(cursor.getString(7) != null)
                    me.setPanseqnum(cursor.getString(7));
                if(cursor.getString(8) != null)
                    me.setEntrymode(cursor.getString(8));
                if(cursor.getString(9) != null)
                    me.setPosdatacode(cursor.getString(9));
                if(cursor.getString(10) != null)
                    me.setStan(cursor.getString(10));
                if(cursor.getString(11) != null)
                    me.setAuthcode(cursor.getString(11));
                if(cursor.getString(12) != null)
                    me.setCondcode(cursor.getString(12));
                if(cursor.getString(13) != null)
                    me.setRespcode(cursor.getString(13));
                if(cursor.getString(14) != null)
                    me.setAcquirerid(cursor.getString(14));
                if(cursor.getString(15) != null)
                    me.setReceiptno(cursor.getString(15));
                if(cursor.getString(16) != null)
                    me.setTxntype(cursor.getString(16));
                if(cursor.getString(17) != null)
                    me.setRdate(cursor.getString(17));
                if(cursor.getString(18) != null)
                    me.setRtime(cursor.getString(18));
                if(cursor.getString(19) != null)
                    me.setAppname(cursor.getString(19));
                if(cursor.getString(20) != null)
                    me.setExpdate(cursor.getString(20));
                if(cursor.getString(21) != null)
                    me.setMaskedpan(cursor.getString(21));
                if(cursor.getString(22) != null)
                    me.setHoldername(cursor.getString(22));
                if(cursor.getString(23) != null)
                    me.setAid(cursor.getString(23));
                if(cursor.getString(24) != null)
                    me.setCashback(cursor.getString(24));
                if(cursor.getString(25) != null)
                    me.setTotal(cursor.getString(25));
                if(cursor.getString(26) != null)
                    me.setPaymentdetails(cursor.getString(26));
                if(cursor.getString(27) != null)
                    me.setTimestamp(cursor.getString(27));
                if(cursor.getString(28) != null)
                    me.setDate(cursor.getString(28));
                contactList.add(me);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }

    //Callhome Database
    public void CALLHOMEFIRST(String rrn, String stan, String maskedpan, String amt, String datetime,
                              String mti, String proccode, String appname, String cardname, String paymentdetails) {
        Log.i(TAG, "INSERTING CALLHOMEFIRST STEP 1");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CSTAN, stan);
        values.put(CMASKEDPAN, maskedpan);
        values.put(CAMOUNT, amt);
        values.put(CDATETIME, datetime);
        values.put(CRRN, rrn);
        values.put(CMTI, mti);
        values.put(CPROCCODE, proccode);
        values.put(CAPPNAME, appname);
        values.put(CCARDNAME, cardname);
        values.put(CPAYMENTDETAILS, paymentdetails);
        long ret = db.insert(TABLE_NAME_CH, null, values);
        Log.i(TAG, "INSERTED: 3" + ret);
    }

    public void UPDATECALLHOME(String authcode, String response, String vendorId, String field62) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAUTHCODE, authcode);
        values.put(CRESPONSE, response);
        values.put(CVENDORID, vendorId);
        values.put(CFIELD62, field62);
        db.update(TABLE_NAME_CH, values, CID + " = (SELECT MAX(id) FROM " + TABLE_NAME_CH + ")", null);
        db.close();
    }

    public List<CALLHOME> getAllCallhome() {
        List<CALLHOME> contactList = new ArrayList<CALLHOME>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_CH;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                CALLHOME me = new CALLHOME();
                if(cursor.getString(0) != null)
                    me.setId(Integer.parseInt(cursor.getString(0)));
                if(cursor.getString(1) != null)
                    me.setStan(cursor.getString(1));
                if(cursor.getString(2) != null)
                    me.setMaskedpan(cursor.getString(2));
                if(cursor.getString(3) != null)
                    me.setAuthcode(cursor.getString(3));
                if(cursor.getString(4) != null)
                    me.setRrn(cursor.getString(4));
                if(cursor.getString(5) != null)
                    me.setAmt(cursor.getString(5));
                if(cursor.getString(6) != null)
                    me.setDatetime(cursor.getString(6));
                if(cursor.getString(7) != null)
                    me.setMti(cursor.getString(7));
                if(cursor.getString(8) != null)
                    me.setProccode(cursor.getString(8));
                if(cursor.getString(9) != null)
                    me.setResponse(cursor.getString(9));
                if(cursor.getString(10) != null)
                    me.setAppname(cursor.getString(10));
                if(cursor.getString(11) != null)
                    me.setCardname(cursor.getString(11));
                if(cursor.getString(12) != null)
                    me.setvendorid(cursor.getString(12));
                if(cursor.getString(13) != null)
                    me.setfield62(cursor.getString(13));
                if(cursor.getString(14) != null)
                    me.setpaymentdetails(cursor.getString(14));
                contactList.add(me);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }

    public void deleteAllDatabase()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_CH, null, null);
        db.close();

        db = this.getWritableDatabase();
        db.delete(TABLE_NAME_EOD, null, null);
        db.close();
    }


    public void deleteUpload(String[] id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = String.format(CID + " in (%s)", new Object[] { TextUtils.join(",", Collections.nCopies(id.length, "?")) });
        db.delete(TABLE_NAME_CH, whereClause, id);
        db.close();
    }

    public void deleteAllCallhome()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_CH, null, null);
        db.close();
    }
}

