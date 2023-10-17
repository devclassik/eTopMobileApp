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

import com.google.android.material.textfield.TextInputLayout;
import com.gbikna.sample.R;
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

public class Setpassword extends AppCompatActivity {

    private static final String TAG = Setpassword.class.getSimpleName();

    Button button;
    private TextInputLayout password1, password2, pin, pin2;
    private TextView resfield;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpassword);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BankInfo.bankControl = 1;
                Intent intent = new Intent();
                intent.setClass(Setpassword.this, BankInfo.class);
                startActivity(intent);
                finish();
            }
        });

        button = (Button) findViewById(R.id.signup);
        password1 = (TextInputLayout) findViewById(R.id.password1);
        password2 = (TextInputLayout) findViewById(R.id.password2);
        pin = (TextInputLayout) findViewById(R.id.pin);
        pin2 = (TextInputLayout) findViewById(R.id.pin2);
        resfield = (TextView)findViewById(R.id.response);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Proceed();
            }
        });
    }

    public void Proceed() {
        String p1 = password1.getEditText().getText().toString().trim();
        String p2 = password2.getEditText().getText().toString().trim();
        SignupVr.pin = pin.getEditText().getText().toString().trim();
        String pin22 = pin2.getEditText().getText().toString().trim();
        SignupVr.maxamountperday = "50000";
        if (p1.isEmpty()) {
            password1.setFocusable(true);
            Toast.makeText(Setpassword.this, "Provide Password", Toast.LENGTH_LONG).show();
        } else if (p2.isEmpty()) {
            password2.setFocusable(true);
            Toast.makeText(Setpassword.this, "Provide Confirm Password", Toast.LENGTH_LONG).show();
        } else if (!p1.equals(p2)) {
            password1.setFocusable(true);
            Toast.makeText(Setpassword.this, "Both Password Must Be Same", Toast.LENGTH_LONG).show();
        } else if (SignupVr.pin.isEmpty()) {
            pin.setFocusable(true);
            Toast.makeText(Setpassword.this, "Pin must be set", Toast.LENGTH_LONG).show();
        } else if (pin22.isEmpty()) {
            pin2.setFocusable(true);
            Toast.makeText(Setpassword.this, "Pin 2 Must be set", Toast.LENGTH_LONG).show();
        } else if (!SignupVr.pin.equals(pin22)) {
            pin.setFocusable(true);
            Toast.makeText(Setpassword.this, "Both Pin Must Be Same", Toast.LENGTH_LONG).show();
        } else {
            String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
            if(p1.matches(pattern))
            {
                button.setEnabled(false);
                button.setText("PROCESSING");

                JSONObject send = new JSONObject();
                SignupVr.password = p1;
                try {
                    send.put("firstname", SignupVr.firstname);
                    send.put("middlename", SignupVr.middlename);
                    send.put("lastname", SignupVr.lastname);
                    send.put("fullname", SignupVr.fullname);
                    send.put("username", SignupVr.username);
                    send.put("phonenumber", SignupVr.phonenumber);
                    send.put("gender", SignupVr.gender);
                    send.put("nin", SignupVr.nin);
                    send.put("dob", SignupVr.dob);
                    send.put("housenumber", SignupVr.housenumber);
                    send.put("streetname", SignupVr.streetname);
                    send.put("city", SignupVr.city);
                    send.put("lga", SignupVr.lga);
                    send.put("lgacode", SignupVr.lgacode);
                    send.put("state", SignupVr.state);
                    send.put("usertype", SignupVr.usertype);
                    send.put("bvn", SignupVr.bvn);
                    send.put("accountholder", SignupVr.accountholder);
                    send.put("accountnumber", SignupVr.accountnumber);
                    send.put("bankcode", SignupVr.bankcode);
                    send.put("bankname", SignupVr.bankname);
                    send.put("businessaddress", SignupVr.businessaddress);
                    send.put("businessname", SignupVr.businessname);
                    send.put("businessphone", SignupVr.businessphone);
                    send.put("businesstype", SignupVr.businesstype);
                    send.put("cacnumber", SignupVr.cacnumber);
                    send.put("latitude", SignupVr.latitude);
                    send.put("longitude", SignupVr.longitude);
                    send.put("password", SignupVr.password);
                    send.put("serialnumber", SignupVr.serialnumber);
                    send.put("pin", SignupVr.pin);
                    send.put("landmark", SignupVr.landmark);
                    send.put("maxamountperday", SignupVr.maxamountperday);
                    send.put("imageurla", "");
                    send.put("imageurlb", "");
                    send.put("kudaactnum", "");
                    send.put("kudatrack", "");
                    send.put("unityactnum", "");
                    send.put("walletid", "");
                    Log.i(TAG, "REQUEST: " + send.toString());
                    if(Profile.controller == 1)
                        new SigupUser().execute(BASEURL + "/apis/etop/signup/edit", send.toString());
                    else
                        new SigupUser().execute(BASEURL + "/apis/etop/signup/new", send.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    button.setEnabled(true);
                    button.setText("SIGN UP");
                }
            }else
            {
                password1.setFocusable(true);
                Toast.makeText(Setpassword.this, "Password Rules Not Met", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class SigupUser extends AsyncTask<String, Void, String> {
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
                                    Intent intent = new Intent();
                                    intent.setClass(Setpassword.this, UploadImages.class);
                                    startActivity(intent);
                                    finish();
                                }else
                                {
                                    button.setEnabled(true);
                                    button.setText("SIGN UP");
                                    resfield.setText(obj.getString("message"));
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
                            Log.i(TAG, "SIGNUP NOT SUCCESSFUL");
                            button.setEnabled(true);
                            button.setText("SIGN UP");
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(server_response);
                                resfield.setText(obj.getString("message"));
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
        intent.setClass(Setpassword.this, Login.class);
        startActivity(intent);
        finish();
    }
}