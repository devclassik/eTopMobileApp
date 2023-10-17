package com.gbikna.sample.etop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.gbikna.sample.R;
import com.gbikna.sample.etop.util.RecoverUtil;

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

public class RecoverySecond extends AppCompatActivity {

    private static final String TAG = RecoverySecond.class.getSimpleName();
    private TextInputLayout text, passworda, passwordb;
    private Button pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_second);

        text = (TextInputLayout)findViewById(R.id.textme);
        passworda = (TextInputLayout)findViewById(R.id.passworda);
        passwordb = (TextInputLayout)findViewById(R.id.passwordb);
        pro = (Button) findViewById(R.id.pro_pass);

        pro.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                pro.setText("Please wait...");
                pro.setEnabled(false);
                RecoverUtil.text = text.getEditText().getText().toString().trim();
                Reset();
            }
        });

    }

    public void Reset() {
        Log.d(TAG, "Login");
        String s1 = passworda.getEditText().getText().toString().trim();
        String s2 = passwordb.getEditText().getText().toString().trim();
        if(RecoverUtil.username.isEmpty())
        {
            Toast.makeText(RecoverySecond.this, "Retry Later", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setClass(RecoverySecond.this, Login.class);
            startActivity(intent);
            finish();
        }else if(s1.isEmpty() || s2.isEmpty() || !s1.equals(s2))
        {
            passworda.setFocusable(true);
        }else
        {
            RecoverUtil.password = s1;
            JSONObject send = new JSONObject();
            try {
                send.put("username", RecoverUtil.username);
                send.put("password", RecoverUtil.password);
                send.put("text", RecoverUtil.text);
                Log.i(TAG, "REQUEST: " + send.toString());
                new PostCallhome().execute(BASEURL + "/apis/etop/signup/recovery/second", send.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                                    Toast.makeText(RecoverySecond.this, "SUCCESS", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent();
                                    intent.setClass(RecoverySecond.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }else
                                {
                                    Toast.makeText(RecoverySecond.this, "NOT SUCCESSFUL", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent();
                                    intent.setClass(RecoverySecond.this, Login.class);
                                    startActivity(intent);
                                    finish();
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
                            pro.setText("LOGIN");
                            pro.setEnabled(true);
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(server_response);
                                Toast.makeText(RecoverySecond.this, "NOT SUCCESSFUL", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                intent.setClass(RecoverySecond.this, Login.class);
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
        Intent intent = new Intent();
        intent.setClass(RecoverySecond.this, Login.class);
        startActivity(intent);
        finish();
    }
}