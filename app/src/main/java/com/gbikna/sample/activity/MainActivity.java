package com.gbikna.sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.Logout;
import com.gbikna.sample.gbikna.util.database.DATABASEHANDLER;
import com.gbikna.sample.gbikna.util.update.DemoActivity;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.gbikna.util.utilities.Utilities;
import com.gbikna.sample.etop.Profile;
import com.gbikna.sample.etop.uploads.BusinessUpload;
import com.gbikna.sample.etop.uploads.PersonalUpload;
import com.gbikna.sample.etop.util.SignupVr;
import com.gbikna.sample.utils.AidsUtil;
import com.gbikna.sample.utils.HexUtil;
import com.horizonpay.smartpossdk.aidl.emv.AidEntity;
import com.horizonpay.smartpossdk.aidl.emv.CapkEntity;
import com.horizonpay.smartpossdk.aidl.emv.IAidlEmvL2;
import com.horizonpay.smartpossdk.aidl.pinpad.IAidlPinpad;
import com.horizonpay.smartpossdk.aidl.sys.IAidlSys;
import com.horizonpay.smartpossdk.data.PinpadConst;

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
import java.util.List;


import butterknife.ButterKnife;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.APPVERSION;
import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;
import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BRAND;
import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.MODEL;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView disp;
    private IAidlEmvL2 mEmvL2;
    private boolean isSupport;

    IAidlPinpad innerpinpad;
    private static final int KEK_KEY_INDEX = 0;
    private static final int MASTER_KEY_INDEX = 0;
    private static final int WORK_KEY_INDEX = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.logon);
        disp = (TextView)findViewById(R.id.text_id);
        ButterKnife.bind(this);
        SystemClock.sleep(50);


        init();

        new GetSignUp().execute(BASEURL + "/apis/etop/signup/getbyemail/" + ProfileParser.emailaddress + "/");
    }

    private void init() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        System.out.println("2. ignored profileddownload onBackPressed 1111");
        Intent intent = new Intent();
        Bundle data = new Bundle();
        intent.setClass(MainActivity.this, Logout.class);
        intent.putExtras(data);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy()
    {
        android.util.Log.i(TAG, "4. Destroying ProfileDownload");
        super.onDestroy();
    }

    private void loadClearMasterKey(String key) {
        try {
            byte[] masterKey = HexUtil.hexStringToByte(key);
            boolean result = innerpinpad.injectClearTMK(MASTER_KEY_INDEX, masterKey, new byte[4]);
            if (result) {
                Log.i(TAG, "loadClearMasterKey [" + key + "] success");
                loadWorkKey(ProfileParser.clrpinkey);
            } else {
                Log.i(TAG, "loadClearMasterKey [" + key + "] failed");
            }
        } catch (RemoteException e) {
            Log.i(TAG,  e.getMessage());
        }
    }

    private void loadMasterKey(String key) {
        try {
            byte[] masterKey = HexUtil.hexStringToByte(key);
            boolean result = innerpinpad.injectSecureTMK(KEK_KEY_INDEX, MASTER_KEY_INDEX, masterKey, new byte[4]);
            if (result) {
                Log.i(TAG, "injectSecureTMK [" + key + "] success");
                loadWorkKey(ProfileParser.encpinkey);
            } else {
                Log.i(TAG, "injectSecureTMK [" + key + "] failed");
            }
        } catch (RemoteException e) {
            Log.i(TAG,  e.getMessage());
        }
    }

    private void loadWorkKey(String key) {
        try {
            byte[] pinKey = HexUtil.hexStringToByte(key);
            //byte[] kcv = HexUtil.hexStringToByte("00000000");
            boolean result = innerpinpad.injectWorkKey(WORK_KEY_INDEX, PinpadConst.PinPadKeyType.TPINK, pinKey, new byte[4]);
            if (result) {
                Log.i(TAG, "load pin key [" + key + "] success");
            } else {
                Log.i(TAG, "load pin key [" + key + "] failed");
            }
        } catch (RemoteException e) {
            Log.i(TAG,  e.getMessage());
        }
    }

    public class GetSignUp extends AsyncTask<String , Void ,String> {
        String server_response;
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                android.util.Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Bearer " + ProfileParser.token);
                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = readStream(urlConnection.getInputStream());
                    android.util.Log.i(TAG, "Length: " + server_response.length());
                    android.util.Log.i(TAG, "Response: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            disp.setText("PROFILE LOADED");
                            SignupVr.imageurla = "";
                            SignupVr.imageurlb = "";
                            if(SignupVr.setData(server_response))
                            {
                                if(!SignupVr.serialnumber.equals(Utilities.getSerialNumber()))
                                {
                                    disp.setText("PLEASE CONTACT SUPPORT.");
                                    SystemClock.sleep(5000);
                                    Intent intent = new Intent();
                                    Bundle data = new Bundle();
                                    intent.setClass(MainActivity.this, Logout.class);
                                    intent.putExtras(data);
                                    startActivity(intent);
                                    finish();
                                }else if(SignupVr.imageurla.isEmpty())
                                {
                                    disp.setText("PLEASE PERSONAL PICTURES");
                                    SystemClock.sleep(5000);
                                    Intent intent = new Intent();
                                    Bundle data = new Bundle();
                                    intent.setClass(MainActivity.this, PersonalUpload.class);
                                    intent.putExtras(data);
                                    startActivity(intent);
                                    finish();
                                }else if(SignupVr.imageurlb.isEmpty()){
                                    disp.setText("PLEASE BUSINESS PICTURES");
                                    SystemClock.sleep(5000);
                                    Intent intent = new Intent();
                                    Bundle data = new Bundle();
                                    intent.setClass(MainActivity.this, BusinessUpload.class);
                                    intent.putExtras(data);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    java.io.File file = new java.io.File(getApplicationContext().getFilesDir(), "profile.txt");
                                    if (file.exists()) {
                                        disp.setText("Loading...");
                                        android.util.Log.i(TAG, "Let us see how far 1");
                                        String resp = Utilities.readFileAsString("profile.txt", getApplicationContext());
                                        if (resp.charAt(0) != '{') {
                                            java.io.File file2 = new java.io.File(getApplicationContext().getFilesDir(), "tid.txt");
                                            if (file2.exists())
                                                ProfileParser.tid = Utilities.readFileAsString("tid.txt", getApplicationContext());
                                        }
                                        android.util.Log.i(TAG, "OLD PROFILE Length: " + resp.length());
                                        android.util.Log.i(TAG, "OLD PROFILE: " + resp);
                                        android.util.Log.i(TAG, "OLD PROFILE TRANSACTION: " + ProfileParser.transactions);
                                        android.util.Log.i(TAG, "OLD PROFILE HOST 1 CTMK: " + ProfileParser.swkcomponent1);
                                        try {
                                            ProfileParser.parseNewProfile(resp);
                                            JSONObject send = new JSONObject();
                                            try {
                                                send.put("appversion", APPVERSION);
                                                send.put("brand", BRAND);
                                                send.put("model", MODEL);
                                                send.put("serialnumber", Utilities.getSerialNumber());
                                                Log.i(TAG, "REQUEST: " + send.toString());
                                                new PostCallhome().execute(BASEURL + "/apis/etop/signup/profile/download", send.toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            android.util.Log.i(TAG, "Done doing Post");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        disp.setText("Fetching...");
                                        JSONObject send = new JSONObject();
                                        try {
                                            send.put("appversion", APPVERSION);
                                            send.put("brand", BRAND);
                                            send.put("model", MODEL);
                                            send.put("serialnumber", Utilities.getSerialNumber());
                                            Log.i(TAG, "REQUEST: " + send.toString());
                                            new PostCallhome().execute(BASEURL + "/apis/etop/signup/profile/download", send.toString());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        android.util.Log.i(TAG, "Done doing Post");
                                        Utilities.writeStringAsFile("0", "receipt.txt", getApplicationContext());
                                    }
                                }
                            }else
                            {
                                disp.setText("PLEASE CONTACT SUPPORT 2");
                                SystemClock.sleep(5000);
                                Intent intent = new Intent();
                                Bundle data = new Bundle();
                                intent.setClass(MainActivity.this, Logout.class);
                                intent.putExtras(data);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }else
                {
                    server_response = readStream(urlConnection.getErrorStream());
                    android.util.Log.i(TAG, "RESPONSE: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            disp.setText("PLEASE CONTACT SUPPORT 2");
                            SystemClock.sleep(5000);
                            Intent intent = new Intent();
                            Bundle data = new Bundle();
                            intent.setClass(MainActivity.this, Logout.class);
                            intent.putExtras(data);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                disp.setText("PROFILE DOWNLOAD NOT SUCCESSFUL");
                                SystemClock.sleep(1000);
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, Logout.class);
                                intent.putExtra("where", "Profile Download");
                                intent.putExtra("when", "After Successful Profile Download");
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            android.util.Log.i("Response", "" + server_response);
        }
    }

    public class PostCallhome extends AsyncTask<String, Void, String> {
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
                //int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "LOGIN SUCCESSFUL");

                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(server_response);
                                disp.setText("WELCOME");
                                SystemClock.sleep(500);
                                if(!obj.has("tid"))
                                {
                                    Profile.controller = 1;
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this, Logout.class);
                                    startActivity(intent);
                                    finish();
                                }else
                                {
                                    android.util.Log.i(TAG, "LENGTH OF RECIEVE: " + server_response.length());
                                    ProfileParser.parseNewProfile(server_response);
                                    if(ProfileParser.changepin.equals("true"))
                                    {
                                        Utilities.writeStringAsFile(ProfileParser.merchantpin, "merchant.txt", getApplicationContext());
                                    }

//                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
////                                    LocalDateTime dateTime = LocalDateTime.parse("2021-11-04 12:10:35", formatter);
////                                    android.util.Log.i(TAG, dateTime.toString());
////                                    android.util.Log.i(TAG, dateTime.plusHours(1).toString());
////                                    String dtm = dateTime.plusHours(1).toString();
                                    String dtm = ProfileParser.timestamp;
                                    dtm = dtm.replace("T", "");
                                    dtm = dtm.replace("-", "");
                                    dtm = dtm.replace(":", "");
                                    android.util.Log.i(TAG, dtm);

                                    android.util.Log.i(TAG, "wisdom about to inject time");
                                    try {
                                        IAidlSys system = MyApplication.getINSTANCE().getDevice().getSysHandler();
                                        system.setDateTime(dtm);
                                        android.util.Log.i(TAG, "wisdom successful at injecting time");
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                        android.util.Log.i(TAG, "wisdom Failed to inject time");
                                    }


                                    android.util.Log.i(TAG, "DONE PARSING");
                                    android.util.Log.i(TAG, "ABOUT TO SAVE");
                                    Utilities.writeStringAsFile(server_response, "profile.txt", getApplicationContext());
                                    android.util.Log.i(TAG, "DONE SAVING");
                                    android.util.Log.i(TAG, "TRANSACTION: " + ProfileParser.transactions);
                                    android.util.Log.i(TAG, "HOST 1 CTMK: " + ProfileParser.swkcomponent1);
                                    android.util.Log.i(TAG, "HOST 1 SSL: " + ProfileParser.hostssl);
                                    android.util.Log.i(TAG, "HOST 2 SSL: " + ProfileParser.host2ssl);
                                    android.util.Log.i(TAG, "VERSION: " + ProfileParser.appversion);
                                    android.util.Log.i(TAG, "BRAND: " + ProfileParser.appbrand);
                                    android.util.Log.i(TAG, "MODEL: " + ProfileParser.appmodel);
                                    android.util.Log.i(TAG, "FIX: " + ProfileParser.appfix);

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            android.util.Log.i(TAG, "PRESSED Storing host a: " + ProfileParser.hostip);
                                            android.util.Log.i(TAG, "PRESSED Storing host b: " + ProfileParser.hostport);
                                            android.util.Log.i(TAG, "PRESSED Storing host c: " + ProfileParser.hostssl);

                                            if(ProfileParser.clrpinkey != null){
                                                DATABASEHANDLER db2 = new DATABASEHANDLER(MainActivity.this);
                                                db2.deleteHost();
                                                db2.HostInit("HOSTA");
                                                db2.HostInit("HOSTB");

                                                DATABASEHANDLER db = new DATABASEHANDLER(getApplicationContext());
                                                db.HostFirstStep("HOSTA", ProfileParser.hostidname, ProfileParser.hostip, ProfileParser.hostport,
                                                        ProfileParser.hostssl, ProfileParser.hostfriendlyname, "HOSTA");
                                                SystemClock.sleep(500);

                                                db.HostSecondStep("HOSTA", ProfileParser.swkcomponent1, ProfileParser.encmstkey,
                                                        ProfileParser.encseskey,
                                                        ProfileParser.encpinkey, ProfileParser.clrmstkey, ProfileParser.clrseskey, ProfileParser.clrpinkey);

                                                try {
                                                    ProfileParser.clrmstkey = "00000000000000000000000000000000";
                                                    innerpinpad = MyApplication.getINSTANCE().getDevice().getPinpad(true);
                                                    isSupport = innerpinpad.isSupport();
                                                    innerpinpad.setKeyAlgorithm(PinpadConst.KeyAlgorithm.DES);
                                                    Log.i(TAG, "CLEAR MASTER KEY: " + ProfileParser.clrmstkey);
                                                    Log.i(TAG, "CLEAR PIN KEY: " + ProfileParser.clrpinkey);
                                                    loadClearMasterKey(ProfileParser.clrmstkey);
                                                    //uploadAidAndCapk();
                                                } catch (RemoteException e) {
                                                    Log.i(TAG, e.getMessage());
                                                }

                                                if(ProfileParser.paramdownload.length() > 10){
                                                    uploadAidAndCapk();
                                                    String key = ProfileParser.paramdownload;
                                                    String ctmkdatetime = "";
                                                    String mid = "";
                                                    String timeout = "";
                                                    String currencycode = "";
                                                    String countrycode = "";
                                                    String callhome = "";
                                                    String mnl = "";
                                                    String mcc = "";

                                                    String f42 = key;
                                                    int loop = 0, i = 0;
                                                    String t = "";
                                                    String l = "";
                                                    String v = "";
                                                    for(i = 0; i < f42.length(); i++)
                                                    {
                                                        if(loop == 0)
                                                        {
                                                            t = f42.substring(i, i + 2);
                                                            i = i + 1;
                                                            loop++;
                                                        }else if(loop == 1)
                                                        {
                                                            l = f42.substring(i, i + 3);
                                                            i = i + 2;
                                                            loop++;
                                                        }else if(loop == 2)
                                                        {
                                                            int f = Integer.parseInt(l);
                                                            v = f42.substring(i, i + f);
                                                            loop = 0;
                                                            i = i + f - 1;
                                                            switch (t) {
                                                                case "02":
                                                                    ctmkdatetime = v;
                                                                    break;
                                                                case "03":
                                                                    mid = v;
                                                                    break;
                                                                case "04":
                                                                    timeout = v;
                                                                    break;
                                                                case "05":
                                                                    currencycode = v;
                                                                    break;
                                                                case "06":
                                                                    countrycode = v;
                                                                    break;
                                                                case "07":
                                                                    callhome = v;
                                                                    break;
                                                                case "08":
                                                                    mcc = v;
                                                                    break;
                                                                case "52":
                                                                    mnl = v;
                                                                    break;
                                                                default:
                                                                    android.util.Log.i(TAG, "UNKNOWN TLV...");
                                                                    break;
                                                            }


                                                            android.util.Log.i(TAG, "TAG: " + t);
                                                            android.util.Log.i(TAG, "LENGTH: " + l);
                                                            android.util.Log.i(TAG, "VALUE: " + v);
                                                        }

                                                    }

                                                    db.HostThirdStep("HOSTA", ctmkdatetime, mid, timeout, currencycode, countrycode,
                                                            callhome, mnl, mcc);
                                                    ProfileParser.umcc = mcc;
                                                    ProfileParser.umid = mid;
                                                    ProfileParser.umnl = mnl;
                                                    ProfileParser.fhostmid = mid;
                                                }
                                            }

                                            if(ProfileParser.appversion.equals("null")
                                                    && ProfileParser.appbrand.equals("null")
                                                    && ProfileParser.appmodel.equals("null"))
                                            {
                                                Intent intent = new Intent();
                                                intent.setClass(MainActivity.this, com.gbikna.sample.etop.Dashboard.class);
                                                intent.putExtra("where", "Profile Download");
                                                intent.putExtra("when", "After Successful Profile Download");
                                                startActivity(intent);
                                                finish();
                                            }else
                                            {
                                                if(ProfileParser.appversion.equals(APPVERSION))
                                                {
                                                    Intent intent = new Intent();
                                                    intent.setClass(MainActivity.this, com.gbikna.sample.etop.Dashboard.class);
                                                    intent.putExtra("where", "Profile Download");
                                                    intent.putExtra("when", "After Successful Profile Download");
                                                    startActivity(intent);
                                                    finish();
                                                }else
                                                {
                                                    Intent intent = new Intent();
                                                    intent.setClass(MainActivity.this, DemoActivity.class);
                                                    intent.putExtra("where", "Profile Download");
                                                    intent.putExtra("when", "After Successful Profile Download");
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "NOT SUCCESSFUL: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            Log.i(TAG, "LOGIN NOT SUCCESSFUL");
                            disp.setText("CONTACT ETOP FOR SETUP");
                            SystemClock.sleep(500);
                            Profile.controller = 1;
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, Logout.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "" + server_response);
        }
    }


    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }



    private void clearCAPK() {
        try {
            boolean ret = mEmvL2.deleteAllCapks();
            if (ret) {
                Log.i(TAG, "Clear CAPK success!");
            } else {
                Log.i(TAG, "Clear CAPK Failed!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }
    }

    private void clearAID() {
        try {
            boolean ret = mEmvL2.deleteAllAids();
            if (ret) {
                Log.i(TAG, getString(R.string.mesg_aid_clear_succ));
            } else {
                Log.i(TAG, getString(R.string.mesg_aid_clear_fail));
            }
        } catch (RemoteException e) {
            Log.i(TAG, e.getMessage());
        }
    }

    private void downloadAID() {
        List<AidEntity> aidEntityList = AidsUtil.getAllAids();
        boolean ret = false;
        for (int i = 0; i < aidEntityList.size(); i++) {
            String tip = "Download aid" + String.format("(%d)", i);
            Log.i(TAG, tip);
            AidEntity emvAidPara = aidEntityList.get(i);
            try {
                ret = mEmvL2.addAid(emvAidPara);
                if (!ret) {
                    break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            SystemClock.sleep(50);
            Log.i(TAG, "Download aid :" + emvAidPara.getAID() + (ret == true ? " success" : " fail"));
        }
    }

    private void downloadCAPK() {
        List<CapkEntity> capkEntityList = AidsUtil.loadCAPK();
        try {
            mEmvL2.addCapks(capkEntityList);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        boolean ret = false;
        for (int i = 0; i < capkEntityList.size(); i++) {
            String tip = "Download capk" + String.format("(%d)", i);
            Log.i(TAG, tip);
            CapkEntity emvCapkPara = capkEntityList.get(i);
            try {
                ret = mEmvL2.addCapk(emvCapkPara);
                if (!ret) {
                    break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            SystemClock.sleep(50);
            Log.i(TAG, "Download capk :" + emvCapkPara.getRID() + (ret == true ? " success" : " fail"));
        }
        Log.i(TAG, "Download capk " + (ret == true ? "success" : "fail"));
    }

    private void uploadAidAndCapk() {
        try {
            mEmvL2 = MyApplication.getINSTANCE().getDevice().getEmvL2();
            isSupport = mEmvL2.isSupport();
            clearAID();
            downloadAID();
            clearCAPK();
            downloadCAPK();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
