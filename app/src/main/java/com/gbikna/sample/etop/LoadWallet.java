package com.gbikna.sample.etop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.etop.util.SignupVr;

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
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;

public class LoadWallet extends AppCompatActivity {
    private static final String TAG = LoadWallet.class.getSimpleName();

    Button button;
    private TextInputLayout qrreference, rqamount;
    private TextView qrresponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_wallet);

        button = (Button) findViewById(R.id.btn_load);
        qrreference = (TextInputLayout) findViewById(R.id.qrreference);
        rqamount = (TextInputLayout) findViewById(R.id.rqamount);
        qrresponse = (TextView)findViewById(R.id.qrresponse);

        if(SignupVr.kudaactnum.isEmpty())
        {
            button.setVisibility(View.INVISIBLE);
            qrresponse.setText("CREATE A KUDA ACCOUNT");
        }

        if(!SignupVr.kudatrack.isEmpty())
        {
            qrreference.getEditText().setText(SignupVr.kudatrack);
            qrreference.setEnabled(false);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Proceed();
            }
        });
    }

    public void Proceed() {
        String ref = qrreference.getEditText().getText().toString().trim();
        String amount = rqamount.getEditText().getText().toString().trim();
        /*if (ref.isEmpty()) {
            qrreference.setFocusable(true);
            Toast.makeText(LoadWallet.this, "Provide Txn Reference", Toast.LENGTH_LONG).show();
        }else */if (amount.isEmpty()) {
            rqamount.setFocusable(true);
            Toast.makeText(LoadWallet.this, "Provide Txn Amount", Toast.LENGTH_LONG).show();
        }else {
            button.setEnabled(false);
            button.setText("PROCESSING");
            JSONObject obj = new JSONObject();
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String pre = dateFormat.format(new Date());
                obj.put("accountName", SignupVr.fullname);
                obj.put("accountNumber", SignupVr.kudaactnum);
                obj.put("amount", amount + "00");//amount
                obj.put("narrations", "SENT FROM ANDROID APP");
                obj.put("payingBank", "NA - MOBILE");
                obj.put("transactionDate", pre);
                obj.put("transactionReference", ref);
                Log.i(TAG, "REQUEST: " + obj.toString());
                new creditWallet().execute(BASEURL + "/apis/etop/kuda/webhook", obj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(LoadWallet.this, "KUDA LOAD FAILED", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class creditWallet extends AsyncTask<String, Void, String> {
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
                            Log.i(TAG, "LOGIN SUCCESSFUL");

                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(server_response);
                                if(obj.getInt("code") == 200)
                                {
                                    Toast.makeText(LoadWallet.this, "WALLET LOAD SUCCESS", Toast.LENGTH_LONG).show();
                                    qrresponse.setText("SUCCESS");
                                    SystemClock.sleep(3000);

                                    Intent intent = new Intent();
                                    intent.setClass(LoadWallet.this, Dashboard.class);
                                    startActivity(intent);
                                    finish();
                                }else
                                {
                                    Toast.makeText(LoadWallet.this, "WALLET LOAD FAILED", Toast.LENGTH_LONG).show();
                                    qrresponse.setText("NOT SUCCESSFUL");
                                    SystemClock.sleep(3000);

                                    Intent intent = new Intent();
                                    intent.setClass(LoadWallet.this, Dashboard.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(LoadWallet.this, "WALLET LOAD FAILED", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "NOT SUCCESSFUL: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            Log.i(TAG, "KEY EXCHANGE NOT SUCCESSFUL");
                            qrresponse.setText("CONTACT ETOP FOR SETUP");
                            SystemClock.sleep(1000);
                            Toast.makeText(LoadWallet.this, "WALLET LOAD FAILED", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent();
                            intent.setClass(LoadWallet.this, Dashboard.class);
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
        Intent intent = new Intent();
        intent.setClass(LoadWallet.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

}