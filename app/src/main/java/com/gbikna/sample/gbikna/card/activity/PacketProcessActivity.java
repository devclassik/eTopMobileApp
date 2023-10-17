package com.gbikna.sample.gbikna.card.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.etop.Dashboard;
import com.gbikna.sample.gbikna.TransactionDetails;
import com.gbikna.sample.gbikna.util.database.DATABASEHANDLER;
import com.gbikna.sample.gbikna.util.utilities.PackIso;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.gbikna.util.utilities.Utilities;
import com.gbikna.sample.gbikna.util.utilities.Utils;
import com.gbikna.sample.etop.kimono.CryptoHelper;
import com.gbikna.sample.etop.kimono.Dukpt;
import com.gbikna.sample.etop.kimono.ISOUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;


public class PacketProcessActivity extends Activity {
    private static final String TAG = Utils.TAGPUBLIC + PacketProcessActivity.class.getSimpleName();
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_process);
        txt = (TextView) findViewById(R.id.txnstatus);

        ProfileParser.receiving = new String[128];

        storeFirstData();

        try {
            txt.setText("GOING UPSTREAM");
            formBasedOnHost();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String getValueByTagOld(String tag)
    {
        String value = "";
        try {
            value = MyApplication.getINSTANCE().getDevice().getEmvL2().getTagValue(tag);
            Log.i(TAG, tag + ": TAG VALUE: " + value);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return value;
    }

    String getAccountType(String acct)
    {
        String v = acct.substring(2, 4);
        if(v.equals("00"))
            return "Default";
        else if(v.equals("10"))
            return "Savings";
        else if(v.equals("20"))
            return "Current";
        else
            return "Credit";
    }

    void formBasedOnHost() throws JSONException {
        JSONObject obj = new JSONObject();
        String url = "";
        //if(Double.parseDouble(ProfileParser.totalAmount) > Double.parseDouble(ProfileParser.hostswitchamount))
        if(false)
        {
            //interswitch
            Log.i(TAG, "INSIDE INTERSWITCH");
            obj.put("account", getAccountType(ProfileParser.field3));
            obj.put("sn", Utilities.getSerialNumber());//
            obj.put("aip", ProfileParser.icc82);
            obj.put("isoamount", ProfileParser.field4);
            obj.put("amount", ProfileParser.totalAmount);
            obj.put("atc", ProfileParser.icc9F36);
            obj.put("capabilities", "E0F8C8");
            obj.put("cip", ProfileParser.icc9F27);
            obj.put("clrpin", ProfileParser.clrpinkey);
            obj.put("cryptogram", ProfileParser.icc9F26);
            obj.put("cvm", ProfileParser.icc9F34);
            obj.put("expirydate", ProfileParser.field14);
            obj.put("filename", ProfileParser.icc9F06);
            obj.put("iad", ProfileParser.icc9F10);
            obj.put("iswmidt", ProfileParser.iswmidt);//
            obj.put("iswtidt", ProfileParser.iswtidt);//
            obj.put("mid", ProfileParser.mid);
            obj.put("pan", ProfileParser.field2);
            obj.put("panseqno", ProfileParser.field23.substring(1));
            String PINBLOCK = "";
            Log.i(TAG, "WISDOM PIN KEY: " + ProfileParser.clrpinkey);
            Log.i(TAG, "WISDOM PINBLOCK: " + ProfileParser.field52);
            if(ProfileParser.field52 != null && ProfileParser.field52.length() > 4)
            {
                String clearPinblock = (new CryptoHelper()).getDecryptedValue(ProfileParser.clrpinkey, ProfileParser.field52);
                Log.i(TAG, "CLEAR PIN BLOCK: " + clearPinblock);
                Log.i(TAG, "STAN: " + ProfileParser.sending[11]);
                PINBLOCK = Dukpt.getPinBlock(Dukpt.getWorkingKey("000002DDDDE".concat(ISOUtil.padleft("00001", 5, '0'))), clearPinblock);
                Log.i(TAG, "INTERSWITCH PINBLOCK: " + PINBLOCK);
            }else
            {
                PINBLOCK = "";
            }
            obj.put("pinblock", PINBLOCK);
            obj.put("riid", "627821");
            obj.put("setbank", "2000000001");
            obj.put("rrn", ProfileParser.sending[37]);
            obj.put("stan", ProfileParser.sending[11]);
            obj.put("tid", ProfileParser.tid);
            obj.put("totalamount", ProfileParser.totalAmount);
            obj.put("track", ProfileParser.field35);
            obj.put("tvr", ProfileParser.icc95);
            obj.put("unpredictable", ProfileParser.icc9F37);
            if(ProfileParser.txnNumber.equals("1"))
            {
                obj.put("txnName", "PURCHASE");
            }else
            {
                obj.put("txnName", "CASH OUT");
            }
            url = "/apis/etop/purchase/kimono/";
        }else
        {
            //Nibss
            Log.i(TAG, "GOING TO NIBSS");
            JSONObject body = new JSONObject();
            body.put("amount", ProfileParser.totalAmount);
            body.put("clrsesskey", ProfileParser.clrseskey);
            body.put("host", ProfileParser.hostip);
            body.put("port", String.valueOf(ProfileParser.hostport));
            body.put("sn", Utilities.getSerialNumber());
            body.put("ssl", ProfileParser.hostssl);
            body.put("tid", ProfileParser.tid);
            body.put("clrsesskey", ProfileParser.clrseskey);
            body.put("txnname", "CASH OUT");
            body.put("field0", ProfileParser.sending[0]);
            body.put("field2", ProfileParser.sending[2]);
            body.put("field3", ProfileParser.sending[3]);
            body.put("field4", ProfileParser.sending[4]);
            body.put("field7", ProfileParser.sending[7]);
            body.put("field11", ProfileParser.sending[11]);
            body.put("field12", ProfileParser.sending[12]);
            body.put("field13", ProfileParser.sending[13]);
            body.put("field14", ProfileParser.sending[14]);
            body.put("field18", ProfileParser.sending[18]);
            body.put("field22", ProfileParser.sending[22]);
            body.put("field23", ProfileParser.sending[23]);
            body.put("field25", ProfileParser.sending[25]);
            body.put("field26", ProfileParser.sending[26]);
            body.put("field28", ProfileParser.sending[28]);
            body.put("field32", ProfileParser.sending[32]);
            body.put("field35", ProfileParser.sending[35]);
            body.put("field37", ProfileParser.sending[37]);
            body.put("field38", ProfileParser.sending[38]);
            body.put("field40", ProfileParser.sending[40]);
            body.put("field41", ProfileParser.sending[41]);
            body.put("field42", ProfileParser.sending[42]);
            body.put("field43", ProfileParser.sending[43]);
            body.put("field49", ProfileParser.sending[49]);
            body.put("field52", ProfileParser.sending[52]);
            body.put("field55", ProfileParser.sending[55]);
            body.put("field56", ProfileParser.sending[56]);
            body.put("field59", ProfileParser.sending[59]);
            body.put("field60", ProfileParser.sending[60]);
            body.put("field62", ProfileParser.sending[62]);
            body.put("field123", ProfileParser.sending[123]);
            body.put("field128", ProfileParser.field128);

            Log.i(TAG, "SENDING: " + body);
            obj = body;
            url = "/apis/etop/purchase/iso/send";
        }
        Log.i(TAG, "SENDING: " + obj);
        Log.i(TAG, "URL: " + url);
        new routeTransaction().execute(BASEURL + url, obj.toString());
    }

    void finishR(String response)
    {
        DATABASEHANDLER db = new DATABASEHANDLER(this);
        String resp = Utilities.readFileAsString("receipt.txt", getApplicationContext());
        if(Long.parseLong(resp) > 1000) {
            ProfileParser.receiptNum = 0 + 1;
        }else
            ProfileParser.receiptNum = Long.parseLong(resp) + 1;
        Utilities.writeStringAsFile(String.valueOf(ProfileParser.receiptNum), "receipt.txt", getApplicationContext());
        Log.i(TAG, "LOVE SAID");

        try {
            JSONObject obj = new JSONObject(response);
            if(obj.getInt("code") != 200)
            {
                ProfileParser.receiving[39] = "06";
            }else
            {
                if(obj.has("respcode"))
                {
                    if(obj.has("authcode"))
                    {
                        ProfileParser.receiving[39] = obj.getString("respcode");
                        ProfileParser.receiving[38] = obj.getString("authcode");
                    }else
                    {
                        ProfileParser.receiving[39] = obj.getString("respcode");
                    }
                }
                if(obj.has("responsecode"))
                {
                    if(obj.has("authcode"))
                    {
                        ProfileParser.receiving[39] = obj.getString("responsecode");
                        ProfileParser.receiving[38] = obj.getString("authcode");
                    }else
                    {
                        ProfileParser.receiving[39] = obj.getString("responsecode");
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            ProfileParser.receiving[39] = "06";
        }

        /*
        {
          "aggregatorfee": "string",
          "fees": "string",
          "superagentfee": "string",
          "toWallet": "string"
        }
         */
        db.UPDATEEOD(ProfileParser.receiving[38], ProfileParser.receiving[39], String.valueOf(ProfileParser.receiptNum),
                Utilities.getDATEYYYYMMDD(ProfileParser.sending[13]), ProfileParser.sending[7], ProfileParser.sending[62]);
        Intent intent = new Intent();
        Bundle data = new Bundle();
        data.putString("response", response);
        intent.putExtras(data);
        intent.setClass(PacketProcessActivity.this, TransactionDetails.class);
        startActivity(intent);
        finish();
    }

    public class routeTransaction extends AsyncTask<String, Void, String> {
        String server_response;
        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);
                byte[] bytes = strings[1].getBytes("UTF-8");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", "Bearer " + ProfileParser.token);
                urlConnection.setUseCaches (false);

                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                outputStream.write(bytes);
                // what should I write here to output stream to post params to server ?
                outputStream.flush();
                outputStream.close();

                int responseCode = urlConnection.getResponseCode();
                Log.i(TAG, "RESPONSE CODE: " + responseCode);
                // get response
                InputStream responseStream = null;
                if (responseCode == HttpURLConnection.HTTP_OK)
                    responseStream = new BufferedInputStream(urlConnection.getInputStream());
                else
                    responseStream = new BufferedInputStream(urlConnection.getErrorStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                responseStreamReader.close();
                String response = stringBuilder.toString();
                Log.i(TAG, "RESPONSE: " + response);
                server_response = response;
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "RESPONSE SUCCESSFUL");
                            txt.setText("RESPONSE RECEIVED");
                            finishR(server_response);
                        }
                    });
                } else {
                    Log.i(TAG, "NOT SUCCESSFUL: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "RESPONSE SUCCESSFUL");
                            txt.setText("RESPONSE RECEIVED");
                            finishR(server_response);
                        }
                    });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "RESPONSE NOT SUCCESSFUL");
                        txt.setText("RESPONSE NOT RECEIVED");
                        finishR("");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "RESPONSE NOT SUCCESSFUL");
                        txt.setText("RESPONSE NOT RECEIVED");
                        finishR("");
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "" + server_response);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(PacketProcessActivity.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void storeFirstData() {
        Log.i(TAG, "WISDOM SENDING 3");
        byte[] usedata = PackIso.formIso();
        DATABASEHANDLER db = new DATABASEHANDLER(this);
        db.EODFIRST(ProfileParser.sending[0], ProfileParser.sending[7], ProfileParser.sending[35],
                    ProfileParser.sending[3], ProfileParser.totalAmount, ProfileParser.sending[37],
                    ProfileParser.sending[23], ProfileParser.sending[22], ProfileParser.sending[123],
                    ProfileParser.sending[11], ProfileParser.sending[26], "NA",
                    ProfileParser.sending[13], ProfileParser.txnName, ProfileParser.cardType,
                    ProfileParser.sending[14], Utilities.maskedPan(ProfileParser.sending[2]), ProfileParser.cardName,
                    ProfileParser.cardAid, ProfileParser.sending[28], ProfileParser.totalAmount);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
