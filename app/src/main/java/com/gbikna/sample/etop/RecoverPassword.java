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

import com.google.android.material.textfield.TextInputLayout;
import com.gbikna.sample.R;
import com.gbikna.sample.etop.util.RecoverUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.APPVERSION;
import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;
import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BRAND;
import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.MODEL;

public class RecoverPassword extends AppCompatActivity {
    private static final String TAG = RecoverPassword.class.getSimpleName();
    private TextInputLayout email;
    private Button pro;
    private TextView recovery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        email = (TextInputLayout)findViewById(R.id.input_email1);
        pro = (Button) findViewById(R.id.btn_login1);
        recovery = (TextView)findViewById(R.id.recovery1);

        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(RecoverPassword.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        pro.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                pro.setText("Please wait...");
                pro.setEnabled(false);
                RecoverUtil.username = email.getEditText().getText().toString().trim();
                Reset();
            }
        });
    }

    public void Reset() {
        Log.d(TAG, "Login");
        new GetMethodDemo().execute(BASEURL + "/apis/etop/signup/recover/first/" + RecoverUtil.username + "/");
    }

    public class GetMethodDemo extends AsyncTask<String , Void ,String> {
        String server_response;
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                android.util.Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();
                android.util.Log.i(TAG, "WISDOM VERSION: " + APPVERSION);
                android.util.Log.i(TAG, "WISDOM MODEL: " + MODEL);
                android.util.Log.i(TAG, "WISDOM BRAND: " + BRAND);

                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = readStream(urlConnection.getInputStream());
                    android.util.Log.i(TAG, "Length: " + server_response.length());
                    android.util.Log.i(TAG, "Response: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            pro.setText("EMAIL SENT");
                            SystemClock.sleep(3000);
                            Intent intent = new Intent();
                            intent.setClass(RecoverPassword.this, RecoverySecond.class);
                            intent.putExtra("where", "Profile Download");
                            intent.putExtra("when", "After Successful Profile Download");
                            startActivity(intent);
                            finish();
                        }
                    });
                }else
                {
                    server_response = readStream(urlConnection.getErrorStream());
                    android.util.Log.i(TAG, "RESPONSE: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            pro.setText("PLEASE RETRY LATER");
                            SystemClock.sleep(5000);
                            Intent intent = new Intent();
                            Bundle data = new Bundle();
                            intent.setClass(RecoverPassword.this, Login.class);
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
                                pro.setText("PLEASE RETRY LATER");
                                SystemClock.sleep(5000);
                                Intent intent = new Intent();
                                Bundle data = new Bundle();
                                intent.setClass(RecoverPassword.this, Login.class);
                                intent.putExtras(data);
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


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(RecoverPassword.this, Login.class);
        startActivity(intent);
        finish();
    }
}