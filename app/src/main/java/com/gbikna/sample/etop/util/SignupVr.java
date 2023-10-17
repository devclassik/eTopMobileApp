package com.gbikna.sample.etop.util;

import com.gbikna.sample.gbikna.util.utilities.ProfileParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignupVr {

    public static String firstname = "";
    public static String middlename = "";
    public static String lastname = "";
    public static String fullname = "";
    public static String username = "";//Email Address
    public static String phonenumber = "";
    public static String gender = "";
    public static String nin = "";
    public static String dob = "";
    public static String housenumber = "";
    public static String streetname = "";
    public static String city = "";
    public static String lga = "";
    public static String lgacode = "";
    public static String state = "";

    public static String bvn = "";
    public static String accountholder = "";
    public static String accountnumber = "";
    public static String bankcode = "";
    public static String bankname = "";

    public static String businessaddress = "";
    public static String businessname = "";
    public static String businessphone = "";
    public static String businesstype = "";
    public static String cacnumber = "";
    public static String latitude = "";
    public static String longitude = "";

    public static String password = "";
    public static String serialnumber = "";
    public static String maxamountperday = "";

    public static String imageurla = "";
    public static String imageurlb = "";
    public static String kudaactnum = "";
    public static String kudatrack = "";
    public static String unityactnum = "";
    public static String walletid = "";
    public static String approved = "";
    public static String approvedby = "";
    public static String usertype = "";
    public static String pin = "";
    public static String landmark = "";

    public static boolean setData(String resp)
    {
        try {
            JSONArray arr = new JSONArray(resp);
            JSONObject obj = arr.getJSONObject(0);

            SignupVr.username = ProfileParser.emailaddress;
            if(obj.has("firstname"))
            {
                SignupVr.firstname = obj.getString("firstname");
            }

            if(obj.has("middlename"))
            {
                SignupVr.middlename = obj.getString("middlename");
            }

            if(obj.has("lastname"))
            {
                SignupVr.lastname = obj.getString("lastname");
            }

            if(obj.has("fullname"))
            {
                SignupVr.fullname = obj.getString("fullname");
            }

            if(obj.has("phonenumber"))
            {
                SignupVr.phonenumber = obj.getString("phonenumber");
            }

            if(obj.has("gender"))
            {
                SignupVr.gender = obj.getString("gender");
            }

            if(obj.has("nin"))
            {
                SignupVr.nin = obj.getString("nin");
            }

            if(obj.has("dob"))
            {
                SignupVr.dob = obj.getString("dob");
            }

            if(obj.has("housenumber"))
            {
                SignupVr.housenumber = obj.getString("housenumber");
            }

            if(obj.has("streetname"))
            {
                SignupVr.streetname = obj.getString("streetname");
            }

            if(obj.has("city"))
            {
                SignupVr.city = obj.getString("city");
            }

            if(obj.has("lga"))
            {
                SignupVr.lga = obj.getString("lga");
            }

            if(obj.has("lgacode"))
            {
                SignupVr.lgacode = obj.getString("lgacode");
            }

            if(obj.has("state"))
            {
                SignupVr.state = obj.getString("state");
            }

            if(obj.has("bvn"))
            {
                SignupVr.bvn = obj.getString("bvn");
            }

            if(obj.has("accountholder"))
            {
                SignupVr.accountholder = obj.getString("accountholder");
            }

            if(obj.has("accountnumber"))
            {
                SignupVr.accountnumber = obj.getString("accountnumber");
            }

            if(obj.has("bankcode"))
            {
                SignupVr.bankcode = obj.getString("bankcode");
            }

            if(obj.has("bankname"))
            {
                SignupVr.bankname = obj.getString("bankname");
            }

            if(obj.has("businessaddress"))
            {
                SignupVr.businessaddress = obj.getString("businessaddress");
            }

            if(obj.has("businessname"))
            {
                SignupVr.businessname = obj.getString("businessname");
            }

            if(obj.has("businessphone"))
            {
                SignupVr.businessphone = obj.getString("businessphone");
            }

            if(obj.has("businesstype"))
            {
                SignupVr.businesstype = obj.getString("businesstype");
            }

            if(obj.has("cacnumber"))
            {
                SignupVr.cacnumber = obj.getString("cacnumber");
            }

            if(obj.has("latitude"))
            {
                SignupVr.latitude = obj.getString("latitude");
            }

            if(obj.has("longitude"))
            {
                SignupVr.longitude = obj.getString("longitude");
            }

            if(obj.has("password"))
            {
                SignupVr.password = obj.getString("password");
            }

            if(obj.has("serialnumber"))
            {
                SignupVr.serialnumber = obj.getString("serialnumber");
            }

            if(obj.has("maxamountperday"))
            {
                SignupVr.maxamountperday = obj.getString("maxamountperday");
            }

            if(obj.has("imageurla"))
            {
                SignupVr.imageurla = obj.getString("imageurla");
            }

            if(obj.has("imageurlb"))
            {
                SignupVr.imageurlb = obj.getString("imageurlb");
            }

            if(obj.has("kudaactnum"))
            {
                SignupVr.kudaactnum = obj.getString("kudaactnum");
            }

            if(obj.has("kudatrack"))
            {
                SignupVr.kudatrack = obj.getString("kudatrack");
            }

            if(obj.has("unityactnum"))
            {
                SignupVr.unityactnum = obj.getString("unityactnum");
            }

            if(obj.has("walletid"))
            {
                SignupVr.walletid = obj.getString("walletid");
            }

            if(obj.has("approved"))
            {
                SignupVr.approved = obj.getString("approved");
            }

            if(obj.has("usertype"))
            {
                SignupVr.usertype = obj.getString("usertype");
            }

            if(obj.has("pin"))
            {
                SignupVr.pin = obj.getString("pin");
            }

            if(obj.has("landmark"))
            {
                SignupVr.landmark = obj.getString("landmark");
            }

            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }


}
