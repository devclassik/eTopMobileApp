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

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;

public class ChangePassword extends AppCompatActivity {

    private static final String TAG = ChangePassword.class.getSimpleName();

    Button button;
    private TextInputLayout oldpassword, newpassword1, newpassword2;
    private TextView responsetxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        button = (Button) findViewById(R.id.change_pass);
        oldpassword = (TextInputLayout) findViewById(R.id.oldpassword);
        newpassword1 = (TextInputLayout) findViewById(R.id.newpassword1);
        newpassword2 = (TextInputLayout) findViewById(R.id.newpassword2);
        responsetxt = (TextView)findViewById(R.id.changeresponse);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Proceed();
            }
        });
    }

    public void Proceed() {
        String oldpass = oldpassword.getEditText().getText().toString().trim();
        String p1 = newpassword1.getEditText().getText().toString().trim();
        String p2 = newpassword2.getEditText().getText().toString().trim();
        if (oldpass.isEmpty()) {
            oldpassword.setFocusable(true);
            Toast.makeText(ChangePassword.this, "Provide Old Password", Toast.LENGTH_LONG).show();
        }else if (p1.isEmpty()) {
            newpassword1.setFocusable(true);
            Toast.makeText(ChangePassword.this, "Provide Password", Toast.LENGTH_LONG).show();
        } else if (p2.isEmpty()) {
            newpassword2.setFocusable(true);
            Toast.makeText(ChangePassword.this, "Provide Confirm Password", Toast.LENGTH_LONG).show();
        } else if (!p1.equals(p2)) {
            newpassword1.setFocusable(true);
            Toast.makeText(ChangePassword.this, "Both Password Must Be Same", Toast.LENGTH_LONG).show();
        } else {
            button.setEnabled(false);
            button.setText("PROCESSING");

            JSONObject obj = new JSONObject();
            try {
                obj.put("oldpassword", oldpass);
                obj.put("password", p1);
                obj.put("username", SignupVr.username);
                Log.i(TAG, "REQUEST: " + obj.toString());
                new doPasswordChange().execute(BASEURL + "/apis/etop/signup/change/password", obj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ChangePassword.this, "PASSWORD CHANGE FAILED", Toast.LENGTH_LONG).show();
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
                                intent.setClass(ChangePassword.this, Login.class);
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ChangePassword.this, "PASSWORD CHANGE FAILED", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(ChangePassword.this, "PASSWORD CHANGE FAILED", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent();
                            intent.setClass(ChangePassword.this, Login.class);
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
        intent.setClass(ChangePassword.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

}