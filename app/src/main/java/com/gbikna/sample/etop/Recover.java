package com.gbikna.sample.etop;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.gbikna.sample.gbikna.util.utilities.ProfileParser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gbikna.sample.R;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;

public class Recover extends AppCompatActivity {

    private static final String TAG = Recover.class.getSimpleName();
    private TextInputLayout email;
    private TextView resfield;
    private Button pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);

        email = (TextInputLayout)findViewById(R.id.input_email);
        pro = (Button) findViewById(R.id.btn_reset);
        resfield = (TextView)findViewById(R.id.reset_res);

        pro.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                if(email.getEditText().getText().toString().trim().isEmpty())
                {
                    resfield.setText("Please Input Email");
                }else
                {
                    resfield.setText("Processing");
                    new ResetEmail().execute(BASEURL + "/apis/etop/signup/recover/first/" + ProfileParser.emailaddress + "/");
                }
            }
        });
    }

    public class ResetEmail extends AsyncTask<String , Void ,String> {
        String server_response;
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                android.util.Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setRequestProperty("Authorization", "Bearer " + ProfileParser.token);
                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = readStream(urlConnection.getInputStream());
                    android.util.Log.i(TAG, "Length: " + server_response.length());
                    android.util.Log.i(TAG, "Response: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            resfield.setText("SUCCESS");
                        }
                    });
                }else
                {
                    server_response = readStream(urlConnection.getErrorStream());
                    android.util.Log.i(TAG, "RESPONSE: " + server_response);
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
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

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
        System.out.println("2. ignored profileddownload onBackPressed 1111");
        Intent intent = new Intent();
        intent.setClass(Recover.this, Login.class);
        startActivity(intent);
        finish();
    }
}