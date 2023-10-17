package com.gbikna.sample.gbikna.util.utilities;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class ProfileParser {
    public static String BASEURL = "http://143.42.102.14:9990";
    public static String BRAND = "HORIZONPAY";
    public static String MODEL = "k11";
    public static String APPVERSION = "1.0.1";

    public static String emailaddress = "";
    public static String password = "";
    public static String token = "";
    public static String balanceResponse = "";

    public static String icc82 = "";
    public static String icc9F36 = "";
    public static String icc9F27 = "";
    public static String icc9F26 = "";
    public static String icc9F34 = "";
    public static String icc9F06 = "";
    public static String icc9F10 = "";
    public static String icc95 = "";
    public static String icc9F37 = "";

    public static String umcc = "";
    public static String umid = "";
    public static String umnl = "";

    public static String tid;
    public static String timestamp;
    public static String mid;
    public static String serialnumber;
    public static String terminalmodel;
    public static String initapplicationversion;
    public static String merchantname;
    public static String merchantaddress;
    public static String adminpin;
    public static String merchantpin;
    public static String changepin;
    public static String contactname;
    public static String contactphone;
    public static String email;
    public static String mcc;
    public static String lga;
    public static String appname;
    public static String country;
    public static String countrycode;
    public static String terminalmanufacturer;
    public static String blocked;
    public static String blockedpin;
    public static String ownerusername;
    public static String superagent;
    public static String dialogheading;
    public static String accountname;
    public static String accountcode;
    public static String accountnumber;
    public static String accountbank;
    public static String stampduty;
    public static String msc;
    public static String switchfee;
    public static String tmo;
    public static String sanef;
    public static String percentagerule;
    public static String maxamount;
    public static String encmstkey;
    public static String clrmstkey;
    public static String encseskey;
    public static String clrseskey;
    public static String encpinkey;
    public static String clrpinkey;
    public static String paramdownload;
    public static String ptspemail;
    public static String ptspaddress;
    public static String ptspnumber;
    public static String ptspname;
    public static String simname;
    public static String simserial;
    public static String simnumber;
    public static String aggretransferfee;
    public static String aggrewithdrawalfee;
    public static String appversion;
    public static String appbrand;
    public static String appdescription;
    public static String appmodel;
    public static String appfix;
    public static String appterminals;
    public static String appupdated;
    public static String appremarks;
    public static String profilename;
    public static String profileremarks;
    public static String profilecallhomeid;
    public static String profilecardschemekeytypes;
    public static String profilekarrabopay;
    public static String profiletmspay;
    public static String chname;
    public static int chinterval;
    public static String chip;
    public static String chport;
    public static String chremotedownloadtime;
    public static String chcount;
    public static String comname;
    public static String comusername;
    public static String comgateway;
    public static String comip;
    public static String comport;
    public static String comapn;
    public static String compassword;
    public static String comremarks;
    public static String comcommstype;
    public static String comipmode;

    public static Boolean rptshowlogo;
    public static int rptnormalfontsize;
    public static int rptheaderfontsize;
    public static int rptamountfontsize;
    public static Boolean rptshowbarcode;
    public static int rptprintmerchantcopynumber;
    public static int rptprintclientcopynumber;
    public static Boolean rptsaveforreceipt;

    public static String rptname;
    public static String rptfootertext;
    public static String rptcustomercopylabel;
    public static String rptmerchantcopylabel;
    public static String rptfootnotelabel;
    public static String hostidname;
    public static String hostip;
    public static int hostport;
    public static Boolean hostssl;
    public static String hostfriendlyname;
    public static String hostmestype;
    public static String swkname;
    public static String swkcomponent1;
    public static String swkcomponent2;
    public static String curname;
    public static String curabbreviation;
    public static int curcode;
    public static String curminorunit;
    public static String curremarks;
    public static String bnkname;
    public static String bnkcode;
    public static String bnkremarks;
    public static String transactions;
    public static String hostarray;
    public static String protectlist;
    public static String hostid2name;
    public static String host2ip;
    public static int host2port;
    public static Boolean host2ssl;
    public static String host2friendlyname;
    public static String host2mestype;
    public static String swk2name;
    public static String swk2component1;
    public static String swk2component2;
    public static String hostswitchamount;


    public static long receiptNum;
    public static String Fee;
    public static String Amount;
    public static String totalAmount;
    public static String tmsfee;
    public static String karrabofee;
    public static String superagentfee;
    public static String aggregatorfee;
    public static String destination;
    public static String code;
    public static String bankname;
    public static String description;
    public static String provider;
    public static String bankcode;
    public static String receivername;
    public static String cardAid;
    public static String cardType;
    public static String cardName;
    public static int cusCopy = 0;
    public static int merCopy = 0;
    public static int totalCopy = 0;
    public static String txnName;
    public static String txnNumber;
    public static String field0;
    public static String field2;
    public static String field3;
    public static String field4;
    public static String rfield7;
    public static String field7;
    public static String field11;
    public static String field12;
    public static String field13;
    public static String field14;
    public static String rfield14;
    public static String field18;
    public static String field22;
    public static String field23;
    public static String field25;
    public static String field26;
    public static String field28;
    public static String field32;
    public static String field35;
    public static String field37;
    public static String field38;
    public static String field40;
    public static String field41;
    public static String field42;
    public static String field43;
    public static String field49;
    public static String field52;
    public static String field55;
    public static String field56;
    public static String field59;
    public static String field60;
    public static String field62;
    public static String field90;
    public static String field95;
    public static String field123;
    public static String field128;
    public static String fhostip;
    public static String fhostport;
    public static String fhostssl;
    public static String fhostfriendlyname;
    public static String fhostctmk;
    public static String fhostencmsk;
    public static String fhostencsk;
    public static String fhostencpk;
    public static String fhostclrmsk;
    public static String fhostclrsk;
    public static String fhostclrpk;
    public static String fhostmid;
    public static String fhostcurrcode;
    public static String fhostmnl;
    public static String fhostmcc;
    public static String orrn;
    public static String tmsusername;
    public static String tmstid;
    public static String phoneNumber;
    public static byte[] response = null;
    public static String[] receiving = null;
    public static String[] sending = null;
    public static String notification = null;


    public static String logoversion = "";
    public static String logofilename = "";
    public static String logodescription = "";
    public static String logobankcode = "";
    public static String logobankname = "";
    public static String logoremarks = "";

    public static String iswtidt = "";
    public static String iswmidt = "";

    public static void parseNewProfile(String response) throws JSONException {
        Log.i("CHECKING", "JSON: " + response);
        JSONObject in = new JSONObject(response);
        timestamp = in.getString("timestamp");
        tid = in.getString("tid");
        iswmidt = in.getString("iswmid");
        iswtidt = in.getString("iswtid");
        mid = in.getString("mid");
        serialnumber = in.getString("serialnumber");
        terminalmodel = in.getString("terminalmodel");
        initapplicationversion = in.getString("initapplicationversion");
        merchantname = in.getString("merchantname");
        merchantaddress = in.getString("merchantaddress");
        adminpin = in.getString("adminpin");
        merchantpin = in.getString("merchantpin");
        changepin = in.getString("changepin");
        contactname = in.getString("contactname");
        contactphone = in.getString("contactphone");
        email = in.getString("email");
        mcc = in.getString("mcc");
        lga = in.getString("lga");
        appname = in.getString("appname");
        country = in.getString("country");
        countrycode = in.getString("countrycode");
        terminalmanufacturer = in.getString("terminalmanufacturer");
        blocked = in.getString("blocked");
        blockedpin = in.getString("blockedpin");
        ownerusername = in.getString("ownerusername");
        superagent = in.getString("superagent");
        dialogheading = in.getString("dialogheading");
        accountname = in.getString("accountname");
        accountcode = in.getString("accountcode");
        accountnumber = in.getString("accountnumber");
        accountbank = in.getString("accountbank");
        stampduty = in.getString("stampduty");
        msc = in.getString("msc");
        switchfee = in.getString("switchfee");

        tmo = in.getString("tmo");
        sanef = in.getString("sanef");
        maxamount = in.getString("maxamount");

        ptspemail = in.getString("email");
        ptspaddress = in.getString("merchantaddress");
        ptspnumber = in.getString("contactphone");
        ptspname = in.getString("merchantname");

        simname = in.getString("simname");
        simserial = in.getString("simserial");
        simnumber = in.getString("simnumber");
        hostswitchamount = in.getString("hostswitchamount");

        appversion = in.getString("appversion");
        appbrand = in.getString("appbrand");
        appdescription = in.getString("appdescription");
        appmodel = in.getString("appmodel");
        appfix = in.getString("appfix");
        appterminals = in.getString("appterminals");
        appupdated = in.getString("appupdated");
        appremarks = in.getString("appremarks");
        profilename = in.getString("profilename");
        profileremarks = in.getString("profileremarks");
        profilecallhomeid = in.getString("profilecallhomeid");
        profilecardschemekeytypes = in.getString("profilecardschemekeytypes");
        profiletmspay = in.getString("profiletmspay");
        chname = in.getString("chname");
        chinterval = in.getInt("chinterval");
        chip = in.getString("chip");
        chport = in.getString("chport");
        chremotedownloadtime = in.getString("chremotedownloadtime");
        chcount = in.getString("chcount");
        comname = in.getString("comname");
        comusername = in.getString("comusername");
        comgateway = in.getString("comgateway");
        comip = in.getString("comip");
        comport = in.getString("comport");
        comapn = in.getString("comapn");
        compassword = in.getString("compassword");
        comremarks = in.getString("comremarks");
        comcommstype = in.getString("comcommstype");
        comipmode = in.getString("comipmode");
        rptname = in.getString("rptname");
        rptfootertext = in.getString("rptfootertext");
        rptcustomercopylabel = in.getString("rptcustomercopylabel");
        rptmerchantcopylabel = in.getString("rptmerchantcopylabel");
        rptfootnotelabel = in.getString("rptfootnotelabel");
        rptshowlogo = in.getBoolean("rptshowlogo");
        rptnormalfontsize = in.getInt("rptnormalfontsize");
        rptheaderfontsize = in.getInt("rptheaderfontsize");
        rptamountfontsize = in.getInt("rptamountfontsize");
        rptshowbarcode = in.getBoolean("rptshowbarcode");
        rptprintmerchantcopynumber = in.getInt("rptprintmerchantcopynumber");
        rptprintclientcopynumber = in.getInt("rptprintclientcopynumber");
        rptsaveforreceipt = in.getBoolean("rptsaveforreceipt");
        hostidname = in.getString("hostidname");
        hostip = in.getString("hostip");
        hostport = in.getInt("hostport");
        hostssl = in.getBoolean("hostssl");
        hostfriendlyname = in.getString("hostfriendlyname");
        hostmestype = in.getString("hostmestype");
        swkname = in.getString("swkname");
        swkcomponent1 = in.getString("swkcomponent1");
        swkcomponent2 = in.getString("swkcomponent2");

        if(in.has("paramdownload"))
        {
            encmstkey = in.getString("encmstkey");
            clrmstkey = in.getString("clrmstkey");
            encseskey = in.getString("encseskey");
            clrseskey = in.getString("clrseskey");
            encpinkey = in.getString("encpinkey");
            clrpinkey = in.getString("clrpinkey");
            paramdownload = in.getString("paramdownload");
        }

        curname = in.getString("curname");
        curabbreviation = in.getString("curabbreviation");
        curcode = in.getInt("curcode");
        curminorunit = in.getString("curminorunit");
        curremarks = in.getString("curremarks");
        bnkname = in.getString("bnkname");
        bnkcode = in.getString("bnkcode");
        bnkremarks = in.getString("bnkremarks");
        transactions = in.getString("transactions");
        hostarray = in.getString("hostarray");
        protectlist = in.getString("protectlist");
        hostid2name = in.getString("hostid2name");
        host2ip = in.getString("host2ip");
        host2port = in.getInt("host2port");
        host2ssl = in.getBoolean("host2ssl");
        host2friendlyname = in.getString("host2friendlyname");
        host2mestype = in.getString("host2mestype");
        swk2name = in.getString("swk2name");
        swk2component1 = in.getString("swk2component1");
        swk2component2 = in.getString("swk2component2");

        if(in.has("clrpinkey"))
        {
            clrpinkey = in.getString("clrpinkey");
        }

        if(in.has("logorversion")) {
            logoversion = in.getString("logorversion");
            logofilename = in.getString("logorfilename");
            logodescription = in.getString("logordescription");
            logobankcode = in.getString("logorbankcode");
            logobankname = in.getString("logorbankname");
        }
    }

    public static void resetFields()
    {
        ProfileParser.Fee = "";
        ProfileParser.Amount = "";
        ProfileParser.totalAmount = "";
        ProfileParser.karrabofee = "";
        ProfileParser.tmsfee = "";
        ProfileParser.superagentfee = "";
        ProfileParser.aggregatorfee = "";
        ProfileParser.destination = "";
        ProfileParser.provider = "";
        ProfileParser.code = "";
        ProfileParser.bankcode = "";
        ProfileParser.bankname = "";
        ProfileParser.description = "";
        ProfileParser.receivername = "";
        ProfileParser.cardAid = "";
        ProfileParser.cardType = "";
        ProfileParser.cardName = "";
        ProfileParser.cusCopy = 0;
        ProfileParser.merCopy = 0;
        ProfileParser.totalCopy = 0;
        ProfileParser.txnName = "";
        ProfileParser.txnNumber = "";
        ProfileParser.field0 = "";
        ProfileParser.field2 = "";
        ProfileParser.field3 = "";
        ProfileParser.field4 = "";
        ProfileParser.rfield7 = "";
        ProfileParser.field7 = "";
        ProfileParser.field11 = "";
        ProfileParser.field12 = "";
        ProfileParser.field13 = "";
        ProfileParser.field14 = "";
        ProfileParser.rfield14 = "";
        ProfileParser.field18 = "";
        ProfileParser.field22 = "";
        ProfileParser.field23 = "";
        ProfileParser.field25 = "";
        ProfileParser.field26 = "";
        ProfileParser.field28 = "";
        ProfileParser.field32 = "";
        ProfileParser.field35 = "";
        ProfileParser.field37 = "";
        ProfileParser.field38 = "";
        ProfileParser.field40 = "";
        ProfileParser.field41 = "";
        ProfileParser.field42 = "";
        ProfileParser.field43 = "";
        ProfileParser.field49 = "";
        ProfileParser.field52 = "";
        ProfileParser.field55 = "";
        ProfileParser.field56 = "";
        ProfileParser.field59 = "";
        ProfileParser.field60 = "";
        ProfileParser.field62 = "";
        ProfileParser.field90 = "";
        ProfileParser.field95 = "";
        ProfileParser.field123 = "";
        ProfileParser.field128 = "";
        ProfileParser.fhostip = "";
        ProfileParser.fhostport = "";
        ProfileParser.fhostssl = "";
        ProfileParser.fhostfriendlyname = "";
        ProfileParser.fhostctmk = "";
        ProfileParser.fhostencmsk = "";
        ProfileParser.fhostencsk = "";
        ProfileParser.fhostencpk = "";
        ProfileParser.fhostclrmsk = "";
        ProfileParser.fhostclrsk = "";
        ProfileParser.fhostclrpk = "";
        ProfileParser.fhostmid = "";
        ProfileParser.fhostcurrcode = "";
        ProfileParser.fhostmnl = "";
        ProfileParser.fhostmcc = "";
        ProfileParser.orrn = "";
        ProfileParser.tmsusername = "";
        ProfileParser.tmstid = "";
        ProfileParser.phoneNumber = "";
        ProfileParser.response = null;
        ProfileParser.receiving = null;
        ProfileParser.sending = null;
        ProfileParser.notification = null;
    }

    public static String getResponseDetails(String res)
    {
        if(res == null)
            return "Please Attempt Again";
        if(res.equals("00"))
        {
            return "Approved..";
        }else if(res.equals("A9"))
        {
            return "Funds Reversed";
        }else if(res.equals("01"))
        {
            return "Refer to card issuer, special condition";
        }else if(res.equals("02"))
        {
            return "Refer to card issuer";
        }else if(res.equals("03"))
        {
            return "Invalid merchant";
        }else if(res.equals("04"))
        {
            return "Pick-up card";
        }else if(res.equals("05"))
        {
            return "Do not honor";
        }else if(res.equals("06"))
        {
            return "Error";
        }else if(res.equals("07"))
        {
            return "Pick-up card, special condition";
        }else if(res.equals("08"))
        {
            return "Honor with identification";
        }else if(res.equals("09"))
        {
            return "Request in progress";
        }else if(res.equals("10"))
        {
            return "Approved, partial";
        }else if(res.equals("11"))
        {
            return "Approved, VIP";
        }else if(res.equals("12"))
        {
            return "Invalid transaction";
        }else if(res.equals("13"))
        {
            return "Invalid amount";
        }else if(res.equals("14"))
        {
            return "Invalid card number";
        }else if(res.equals("15"))
        {
            return "No such issuer";
        }else if(res.equals("16"))
        {
            return "Approved, update track 3";
        }else if(res.equals("17"))
        {
            return "Customer cancellation";
        }else if(res.equals("18"))
        {
            return "Customer dispute";
        }else if(res.equals("19"))
        {
            return "Re-enter transaction";
        }else if(res.equals("20"))
        {
            return "Invalid response";
        }else if(res.equals("21"))
        {
            return "No action taken";
        }else if(res.equals("22"))
        {
            return "Suspected malfunction";
        }else if(res.equals("23"))
        {
            return "Unacceptable transaction fee";
        }else if(res.equals("24"))
        {
            return "File update not supported";
        }else if(res.equals("25"))
        {
            return "Unable to locate record";
        }else if(res.equals("26"))
        {
            return "Duplicate record";
        }else if(res.equals("27"))
        {
            return "File update field edit error";
        }else if(res.equals("28"))
        {
            return "File update file locked";
        }else if(res.equals("29"))
        {
            return "File update failed";
        }else if(res.equals("30"))
        {
            return "Format error";
        }else if(res.equals("31"))
        {
            return "Bank not supported";
        }else if(res.equals("32"))
        {
            return "Completed partially";
        }else if(res.equals("33"))
        {
            return "Expired card, pick-up";
        }else if(res.equals("34"))
        {
            return "Suspected fraud, pick-up";
        }else if(res.equals("35"))
        {
            return "Contact acquirer, pick-up";
        }else if(res.equals("36"))
        {
            return "Restricted card, pick-up";
        }else if(res.equals("37"))
        {
            return "Call acquirer security, pick-up";
        }else if(res.equals("38"))
        {
            return "PIN tries exceeded, pick-up";
        }else if(res.equals("39"))
        {
            return "No credit account";
        }else if(res.equals("40"))
        {
            return "Function not supported";
        }else if(res.equals("41"))
        {
            return "Lost card, pick-up";
        }else if(res.equals("42"))
        {
            return "No universal account";
        }else if(res.equals("43"))
        {
            return "Stolen card, pick-up";
        }else if(res.equals("44"))
        {
            return "No investment account";
        }else if(res.equals("45"))
        {
            return "Account closed";
        }else if(res.equals("46"))
        {
            return "Identification required";
        }else if(res.equals("47"))
        {
            return "Identification cross-check required";
        }else if(res.equals("48"))
        {
            return "Error";
        }else if(res.equals("49"))
        {
            return "Error";
        }else if(res.equals("50"))
        {
            return "Error";
        }else if(res.equals("51"))
        {
            return "Insufficient funds";
        }else if(res.equals("52"))
        {
            return "No check account";
        }else if(res.equals("53"))
        {
            return "No savings account";
        }else if(res.equals("54"))
        {
            return "Expired card";
        }else if(res.equals("55"))
        {
            return "Incorrect PIN";
        }else if(res.equals("56"))
        {
            return "No card record";
        }else if(res.equals("57"))
        {
            return "Transaction not permitted to cardholder";
        }else if(res.equals("58"))
        {
            return "Transaction not permitted on terminal";
        }else if(res.equals("59"))
        {
            return "Suspected fraud";
        }else if(res.equals("60"))
        {
            return "Contact acquirer";
        }else if(res.equals("61"))
        {
            return "Exceeds withdrawal limit";
        }else if(res.equals("62"))
        {
            return "Restricted card";
        }else if(res.equals("63"))
        {
            return "Security violation";
        }else if(res.equals("64"))
        {
            return "Original amount incorrect";
        }else if(res.equals("65"))
        {
            return "Exceeds withdrawal frequency";
        }else if(res.equals("66"))
        {
            return "Call acquirer security";
        }else if(res.equals("67"))
        {
            return "Hard capture";
        }else if(res.equals("68"))
        {
            return "Response received too late";
        }else if(res.equals("69"))
        {
            return "Advice received too late";
        }else if(res.equals("70"))
        {
            return "Error";
        }else if(res.equals("71"))
        {
            return "Error";
        }else if(res.equals("72"))
        {
            return "Error";
        }else if(res.equals("73"))
        {
            return "Error";
        }else if(res.equals("74"))
        {
            return "Error";
        }else if(res.equals("75"))
        {
            return "PIN tries exceeded";
        }else if(res.equals("76"))
        {
            return "Error";
        }else if(res.equals("77"))
        {
            return "Intervene, bank approval required";
        }else if(res.equals("78"))
        {
            return "Intervene, bank approval required for partial amount";
        }else if(res.equals("79"))
        {
            return "Error";
        }else if(res.equals("80"))
        {
            return "Error";
        }else if(res.equals("81"))
        {
            return "Error";
        }else if(res.equals("82"))
        {
            return "Error";
        }else if(res.equals("83"))
        {
            return "Error";
        }else if(res.equals("84"))
        {
            return "Error";
        }else if(res.equals("85"))
        {
            return "Error";
        }else if(res.equals("86"))
        {
            return "Error";
        }else if(res.equals("87"))
        {
            return "Error";
        }else if(res.equals("88"))
        {
            return "Error";
        }else if(res.equals("89"))
        {
            return "Error";
        }else if(res.equals("90"))
        {
            return "Cut-off in progress";
        }else if(res.equals("91"))
        {
            return "Issuer or switch inoperative";
        }else if(res.equals("92"))
        {
            return "Routing error";
        }else if(res.equals("93"))
        {
            return "Violation of law";
        }else if(res.equals("94"))
        {
            return "Duplicate transaction";
        }else if(res.equals("95"))
        {
            return "Reconcile error";
        }else if(res.equals("96"))
        {
            return "System malfunction";
        }else if(res.equals("97"))
        {
            return "Reserved for future Postilion use";
        }else if(res.equals("98"))
        {
            return "Exceeds cash limit";
        }else if(res.equals("99"))
        {
            return "Error";
        }else if(res.equals("100"))
        {
            return "Please Attempt Again";
        }else if(res.equals("10400"))
        {
            return "Bad Request. Unknown Merchant";
        }else if(res.equals("101"))
        {
            return "Reversed";
        }else if(res.equals("CA"))
        {
            return "USER NOT REGISTERED";
        }else if(res.equals("CB"))
        {
            return "INCORRECT PIN";
        }else if(res.equals("CC"))
        {
            return "CAN NOT GET TOKEN";
        }else if(res.equals("CD"))
        {
            return "SERIAL NOT PROFILED";
        }else if(res.equals("CE"))
        {
            return "TERMINAL MISMATCH";
        }else if(res.equals("CF"))
        {
            return "BALANCE IS TOO LOW";
        }else if(res.equals("CG"))
        {
            return "TRANSACTION FAILED";
        }else if(res.equals("CH"))
        {
            return "EXCEPTION OCCURRED";
        }else if(res.equals("CI"))
        {
            return "TID NOT PROFILED";
        }else if(res.equals("CJ"))
        {
            return "TERMINAL BLOCKED. CONTACT ETOP";
        }else if(res.equals("CK"))
        {
            return "NOT SUCCESSFUL";
        }else if(res.equals("CL"))
        {
            return "Error Occurred. Retry Later";
        }
        else
        {
            return "Unknown";
        }
    }

    public static String processError(String mes)
    {
        switch (mes) {
            case "USER NOT REGISTERED":
                return "CA";
            case "INCORRECT PIN":
                return "CB";
            case "CAN NOT GET TOKEN":
                return "CC";
            case "SERIAL NOT PROFILED":
                return "CD";
            case "TERMINAL MISMATCH":
                return "CE";
            case "BALANCE IS TOO LOW":
                return "CF";
            case "WALLET BALANCE LOW":
                return "CF";
            case "TRANSACTION FAILED":
                return "CG";
            case "EXCEPTION OCCURRED":
                return "CH";
            case "TID NOT PROFILED":
                return "CI";
            case "NOT SUCCESSFUL":
                return "Ck";
            case "TERMINAL BLOCKED. CONTACT ETOP":
                return "CJ";
            case "Error Occurred. Retry Later":
                return "CL";
        }
        return "06";
    }

    public static String hexStringToByteArray(String hex) {
        try {
            int l = hex.length();
            byte[] data = new byte[l / 2];
            for (int i = 0; i < l; i += 2) {
                data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                        + Character.digit(hex.charAt(i + 1), 16));
            }
            String st = new String(data, StandardCharsets.UTF_8);
            return st;
        }catch(Exception e)
        {
            return "-/";
        }
    }
}

