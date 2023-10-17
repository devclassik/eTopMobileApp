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

import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.etop.util.SignupVr;
import com.google.android.material.textfield.TextInputLayout;

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

public class ChangePin extends AppCompatActivity {

    private static final String TAG = ChangePin.class.getSimpleName();

    Button button;
    private TextInputLayout oldpin, newpin;
    private TextView responsetxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        button = (Button) findViewById(R.id.change_pin);
        oldpin = (TextInputLayout) findViewById(R.id.oldpin1);
        newpin = (TextInputLayout) findViewById(R.id.newpin1);
        responsetxt = (TextView)findViewById(R.id.changeresponse);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Proceed();
            }
        });
    }

    public void Proceed() {
        String oldp = oldpin.getEditText().getText().toString().trim();
        String p1 = newpin.getEditText().getText().toString().trim();
        if (oldp.isEmpty()) {
            oldpin.setFocusable(true);
            Toast.makeText(ChangePin.this, "Provide Old Pin", Toast.LENGTH_LONG).show();
        }else if (p1.isEmpty()) {
            newpin.setFocusable(true);
            Toast.makeText(ChangePin.this, "Provide New Pin", Toast.LENGTH_LONG).show();
        } else {
            button.setEnabled(false);
            button.setText("PROCESSING");

            JSONObject obj = new JSONObject();
            try {
                obj.put("oldpin", oldp);
                obj.put("newpin", p1);
                obj.put("userame", SignupVr.username);
                Log.i(TAG, "REQUEST: " + obj.toString());
                new doPasswordChange().execute(BASEURL + "/apis/etop/signup/transfer/change", obj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ChangePin.this, "PIN CHANGE FAILED", Toast.LENGTH_LONG).show();
            }
        }
    }


    public class doPasswordChange extends AsyncTask<String, Void, String> {
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
                urlConnection.setRequestMethod("PUT");
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
                                responsetxt.setText("SUCCESS");
                                SystemClock.sleep(3000);

                                Intent intent = new Intent();
                                intent.setClass(ChangePin.this, Login.class);
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ChangePin.this, "PIN CHANGE FAILED", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "NOT SUCCESSFUL: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            Log.i(TAG, "KEY EXCHANGE NOT SUCCESSFUL");
                            responsetxt.setText("CONTACT ETOP FOR SETUP");
                            SystemClock.sleep(1000);
                            Toast.makeText(ChangePin.this, "PIN CHANGE FAILED", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent();
                            intent.setClass(ChangePin.this, Login.class);
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
        intent.setClass(ChangePin.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

}