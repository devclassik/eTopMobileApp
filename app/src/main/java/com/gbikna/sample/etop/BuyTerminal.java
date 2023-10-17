package com.gbikna.sample.etop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.gbikna.util.utilities.Utilities;
import com.gbikna.sample.etop.util.SignupVr;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
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

public class BuyTerminal extends AppCompatActivity {

    private static final String TAG = BuyTerminal.class.getSimpleName();
    Button button;
    private TextInputLayout amount, serialnumber;
    private TextView res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_terminal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BuyTerminal.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        button = (Button) findViewById(R.id.pro_bus);
        amount = (TextInputLayout) findViewById(R.id.amount);
        serialnumber = (TextInputLayout) findViewById(R.id.serialnumber);
        res = (TextView) findViewById(R.id.res);

        serialnumber.getEditText().setText(Utilities.getSerialNumber());
        serialnumber.setEnabled(false);
        amount.setEnabled(false);

        new CheckAlreadyBought().execute(BASEURL + "/apis/etop/terminals/serialstatus/" + Utilities.getSerialNumber() + "/");
        new GetPricing().execute(BASEURL + "/apis/etop/terminals/pricebymodel/k11/");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Proceed();
            }
        });
    }

    public void Proceed() {
        String amt = amount.getEditText().getText().toString().trim();
        String sn = serialnumber.getEditText().getText().toString().trim();

        if (amt.isEmpty()) {
            Toast.makeText(BuyTerminal.this, "Amount Cant be Empty", Toast.LENGTH_LONG).show();
        } else if (sn.isEmpty()) {
            Toast.makeText(BuyTerminal.this, "Provide Serial Number", Toast.LENGTH_LONG).show();
        } else {
            try {
                button.setText("PROCESSING");
                button.setEnabled(false);
                JSONObject obj = new JSONObject();
                obj.put("amount", amt);
                obj.put("name", "k11");
                obj.put("serial", sn);
                obj.put("username", SignupVr.username);
                obj.put("walletid", SignupVr.walletid);
                Log.i(TAG, "TERMINAL PURCHASE REQUEST: " + obj.toString());
                new BuyT().execute(BASEURL + "/apis/etop/terminals/buy/terminal", obj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class BuyT extends AsyncTask<String, Void, String> {
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
                                if(obj.getInt("code") == 200)
                                {
                                    button.setText("PURCHASE SUCCESSFUL");
                                    button.setEnabled(false);
                                    res.setText("TERMINAL PURCHASE SUCCESS");
                                }else
                                {
                                    button.setText("RETRY LATER");
                                    button.setEnabled(false);
                                    res.setText(obj.getString("message"));
                                }
                                new CheckAlreadyBought().execute(BASEURL + "/apis/etop/terminals/serialstatus/" + Utilities.getSerialNumber() + "/");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                button.setText("RETRY");
                                button.setEnabled(false);
                                res.setText("TERMINAL PURCHASE FAILED");
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "REQUEST FAILED: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "REQUEST NOT SUCCESSFUL");
                            button.setText("RETRY");
                            button.setEnabled(false);
                            res.setText("TERMINAL PURCHASE FAILED");
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

    public class GetPricing extends AsyncTask<String , Void ,String> {
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
                            try {
                                JSONArray arr = new JSONArray(server_response);
                                Log.i(TAG, arr.getJSONObject(0).toString());
                                Log.i(TAG, arr.getJSONObject(0).getString("price"));
                                if(arr.length() > 0) {
                                    amount.getEditText().setText(arr.getJSONObject(0).getString("price"));
                                }
                            }catch (Exception e)
                            {
                                Log.i(TAG, e.getMessage());
                            }
                        }
                    });
                }else
                {
                    server_response = readStream(urlConnection.getErrorStream());
                    android.util.Log.i(TAG, "ERROR RESPONSE: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

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

    public class CheckAlreadyBought extends AsyncTask<String , Void ,String> {
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
                            try {
                                JSONArray arr = new JSONArray(server_response);
                                if(arr.length() > 0)
                                {
                                    Toast.makeText(getApplicationContext(), "TERMINAL TAKEN ALREADY", Toast.LENGTH_LONG);
                                    button.setText("Terminal Bought Already");
                                    button.setEnabled(false);
                                }
                            }catch (Exception e)
                            {
                                Log.i(TAG, e.getMessage());
                                Toast.makeText(getApplicationContext(), "ERROR OCCURRED. TRY LATER", Toast.LENGTH_LONG);
                            }
                        }
                    });
                }else
                {
                    server_response = readStream(urlConnection.getErrorStream());
                    android.util.Log.i(TAG, "ERROR RESPONSE: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "ERROR OCCURRED. TRY LATER", Toast.LENGTH_LONG);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "ERROR OCCURRED. TRY LATER", Toast.LENGTH_LONG);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(BuyTerminal.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}