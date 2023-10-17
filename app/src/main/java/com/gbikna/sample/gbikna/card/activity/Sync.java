package com.gbikna.sample.gbikna.card.activity;

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
import android.widget.Toast;


import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.activity.MainActivity;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.etop.Login;
import com.gbikna.sample.utils.AidsUtil;
import com.horizonpay.smartpossdk.aidl.emv.AidEntity;
import com.horizonpay.smartpossdk.aidl.emv.CapkEntity;
import com.horizonpay.smartpossdk.aidl.emv.IAidlEmvL2;

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

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;


public class Sync extends Activity {
    private static final String TAG = Sync.class.getSimpleName();
    TextView txt;
    private IAidlEmvL2 mEmvL2;
    private boolean isSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        ButterKnife.bind(this);

        txt = (TextView) findViewById(R.id.text_sync);

        JSONObject body = new JSONObject();
        try {
            body.put("component", ProfileParser.swkcomponent1);
            body.put("ip", ProfileParser.hostip);
            body.put("port", ProfileParser.hostport);
            body.put("ssl", ProfileParser.hostssl);
            body.put("tid", ProfileParser.tid);
            Log.i(TAG, "Going: " + body);
            new doKeyExchange().execute(BASEURL + "/apis/etop/iso/keyexchange/send", body.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class doKeyExchange extends AsyncTask<String, Void, String> {
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

                                txt.setText("SUCCESS");
                                SystemClock.sleep(1000);

                                Intent intent = new Intent();
                                intent.setClass(Sync.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Sync.this, "KEY EXCHANGE FAILED", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "NOT SUCCESSFUL: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            Log.i(TAG, "KEY EXCHANGE NOT SUCCESSFUL");
                            txt.setText("CONTACT ETOP FOR SETUP");
                            SystemClock.sleep(1000);
                            Toast.makeText(Sync.this, "KEY EXCHANGE FAILED", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent();
                            intent.setClass(Sync.this, Login.class);
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



    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        /*Intent intent = new Intent();
        intent.setClass(Sync.this, Dashboard.class);
        startActivity(intent);
        finish();*/
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