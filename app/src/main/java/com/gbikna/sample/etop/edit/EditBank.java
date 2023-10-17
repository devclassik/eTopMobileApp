package com.gbikna.sample.etop.edit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.etop.BankInfo;
import com.gbikna.sample.etop.Dashboard;
import com.gbikna.sample.etop.MainActivity;
import com.gbikna.sample.etop.PersonalInformation;
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
import java.util.Arrays;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;

public class EditBank extends AppCompatActivity {

    private static final String TAG = BankInfo.class.getSimpleName();
    Button button;
    private TextInputLayout bvn, accountholder, accountnumber;
    AutoCompleteTextView bankname;
    int control = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bank);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalInformation.personalControl = 1;
                Intent intent = new Intent();
                intent.setClass(EditBank.this, PersonalInformation.class);
                startActivity(intent);
                finish();
            }
        });

        button = (Button) findViewById(R.id.pro_bank);


        bvn = (TextInputLayout) findViewById(R.id.bvn);
        accountholder = (TextInputLayout) findViewById(R.id.accountholder);
        accountholder.setEnabled(false);
        accountnumber = (TextInputLayout) findViewById(R.id.accountnumber);
        bankname = (AutoCompleteTextView) findViewById(R.id.banks);

        String pro = "[{\"bankCode\":\"000014\",\"bankName\":\"Access Bank\"},{\"bankCode\":\"000005\",\"bankName\":\"Access Bank PLC (Diamond)\"},{\"bankCode\":\"090134\",\"bankName\":\"ACCION MFB\"},{\"bankCode\":\"090148\",\"bankName\":\"Bowen MFB\"},{\"bankCode\":\"100005\",\"bankName\":\"Cellulant\"},{\"bankCode\":\"000009\",\"bankName\":\"Citi Bank\"},{\"bankCode\":\"100032\",\"bankName\":\"Contec Global\"},{\"bankCode\":\"060001\",\"bankName\":\"Coronation\"},{\"bankCode\":\"090156\",\"bankName\":\"e-BARCs MFB\"},{\"bankCode\":\"000010\",\"bankName\":\"Ecobank Bank\"},{\"bankCode\":\"100008\",\"bankName\":\"Ecobank Xpress Account\"},{\"bankCode\":\"090097\",\"bankName\":\"Ekondo MFB\"},{\"bankCode\":\"000003\",\"bankName\":\"FCMB\"},{\"bankCode\":\"000007\",\"bankName\":\"Fidelity Bank\"},{\"bankCode\":\"000016\",\"bankName\":\"First Bank of Nigeria\"},{\"bankCode\":\"110002\",\"bankName\":\"Flutterwave Technology solutions Limited\"},{\"bankCode\":\"100022\",\"bankName\":\"GoMoney\"},{\"bankCode\":\"000013\",\"bankName\":\"GTBank Plc\"},{\"bankCode\":\"000020\",\"bankName\":\"Heritage\"},{\"bankCode\":\"090175\",\"bankName\":\"HighStreet MFB\"},{\"bankCode\":\"000006\",\"bankName\":\"JAIZ Bank\"},{\"bankCode\":\"090003\",\"bankName\":\"JubileeLife\"},{\"bankCode\":\"090191\",\"bankName\":\"KCMB MFB\"},{\"bankCode\":\"000002\",\"bankName\":\"Keystone Bank\"},{\"bankCode\":\"090267\",\"bankName\":\"Kuda.\"},{\"bankCode\":\"090171\",\"bankName\":\"Mainstreet MFB\"},{\"bankCode\":\"100026\",\"bankName\":\"ONE FINANCE\"},{\"bankCode\":\"100002\",\"bankName\":\"Paga\"},{\"bankCode\":\"100033\",\"bankName\":\"PALMPAY\"},{\"bankCode\":\"100003\",\"bankName\":\"Parkway-ReadyCash\"},{\"bankCode\":\"110001\",\"bankName\":\"PayAttitude Online\"},{\"bankCode\":\"100004\",\"bankName\":\"Paycom(OPay)\"},{\"bankCode\":\"000008\",\"bankName\":\"POLARIS BANK\"},{\"bankCode\":\"000023\",\"bankName\":\"Providus Bank \"},{\"bankCode\":\"000024\",\"bankName\":\"Rand Merchant Bank\"},{\"bankCode\":\"000012\",\"bankName\":\"StanbicIBTC Bank\"},{\"bankCode\":\"100007\",\"bankName\":\"StanbicMobileMoney\"},{\"bankCode\":\"000021\",\"bankName\":\"StandardChartered\"},{\"bankCode\":\"000001\",\"bankName\":\"Sterling Bank\"},{\"bankCode\":\"000022\",\"bankName\":\"SUNTRUST BANK\"},{\"bankCode\":\"000018\",\"bankName\":\"Union Bank\"},{\"bankCode\":\"000004\",\"bankName\":\"United Bank for Africa\"},{\"bankCode\":\"000011\",\"bankName\":\"Unity Bank\"},{\"bankCode\":\"090110\",\"bankName\":\"VFD MFB\"},{\"bankCode\":\"000017\",\"bankName\":\"Wema Bank\"},{\"bankCode\":\"000015\",\"bankName\":\"ZENITH BANK PLC\"},{\"bankCode\":\"100025\",\"bankName\":\"Zinternet - KongaPay\"}]";
        String[] option = new String[50];
        String[] code = new String[50];
        try {
            JSONArray arr = new JSONArray(pro);
            int i = 0;
            for (i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                option[i] = obj.getString("bankName");
                code[i] = obj.getString("bankCode");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(SignupVr.approved.isEmpty() || !SignupVr.approved.equals("true"))
        {
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
            bankname.setAdapter(adapter);
            bankname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bankname.showDropDown();
                }
            });
        }

        if(control == 0)
        {
            button.setText("VALIDATE ACCOUNT");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Proceed(option, code);
            }
        });

        accountnumber.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String dt = accountnumber.getEditText().getText().toString();
                Log.i("BANKINFO", "AFTERTEXTCHANGED: " + dt);
                if(dt.length() == 10)
                {
                    Proceed(option, code);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String dt = accountnumber.getEditText().getText().toString();
                Log.i("BANKINFO", "BEFORETEXTCHANGED: " + dt);
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String dt = accountnumber.getEditText().getText().toString();
                Log.i("BANKINFO", "ONTEXTCHANGED: " + dt);
            }
        });


        bvn.getEditText().setText(SignupVr.bvn);
        accountholder.getEditText().setText(SignupVr.accountholder);
        accountnumber.getEditText().setText(SignupVr.accountnumber);
        bankname.setText(SignupVr.bankname);

        if(SignupVr.approved.isEmpty() || !SignupVr.approved.equals("true"))
        {
            button.setVisibility(View.VISIBLE);
        }else
        {
            bvn.setEnabled(false);
            accountholder.setEnabled(false);
            accountnumber.setEnabled(false);
            bankname.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
        }
    }

    public void Proceed(String[] option, String[] code) {
        String bbvn = bvn.getEditText().getText().toString().trim();
        String acthold = accountholder.getEditText().getText().toString().trim();
        String actnum = accountnumber.getEditText().getText().toString().trim();
        Log.i(TAG, "ACCT NUMBER: " + actnum + ".");
        String bnkname = "", bnkcode = "";
        String action = bankname.getText().toString();
        if (!action.isEmpty()) {
            bnkname = action;
            int i = Arrays.asList(option).indexOf(action);
            bnkcode = code[i];
        }

        if (bbvn.isEmpty() || acthold.isEmpty() || actnum.isEmpty()) {
            Toast.makeText(EditBank.this, "PROVIDE MISSING INFORMATION", Toast.LENGTH_LONG).show();
        } else {
            if(control == 0)
            {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("bankcode", bnkcode);
                    obj.put("destination", actnum);
                    Log.i(TAG, "VALIDATION REQUEST: " + obj.toString());
                    if(SignupVr.approved.isEmpty() || !SignupVr.approved.equals("true"))
                    {
                        new validateAccount().execute(BASEURL + "/apis/etop/kuda/bankvalidation/", obj.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
            {
                control = 0;
                button.setEnabled(false);
                button.setText("PROCESSING");

                JSONObject send = new JSONObject();
                try {
                    send.put("bvn", bbvn);
                    send.put("accountholder", acthold);
                    send.put("accountnumber", actnum);
                    send.put("bankcode", bnkcode);
                    send.put("bankname", bnkname);
                    Log.i(TAG, "REQUEST: " + send.toString());
                    new updateBankInfo().execute(BASEURL + "/apis/etop/signup/bank", send.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    button.setEnabled(true);
                    button.setText("SIGN UP");
                }
            }
        }
    }

    public class updateBankInfo extends AsyncTask<String, Void, String> {
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
                                if(obj.has("status") && obj.getInt("status") == 200)
                                {
                                    button.setText("SUCCESS");
                                    SystemClock.sleep(3000);

                                    Intent intent = new Intent();
                                    intent.setClass(EditBank.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else
                                {
                                    button.setText("RETRY LATER");
                                    button.setEnabled(true);
                                    SystemClock.sleep(3000);

                                    Toast.makeText(EditBank.this, "BANK INFO UPDATE FAILED", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(EditBank.this, "BANK INFO UPDATE FAILED", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "NOT SUCCESSFUL: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            button.setText("RETRY LATER");
                            button.setEnabled(true);
                            SystemClock.sleep(3000);
                            Toast.makeText(EditBank.this, "BANK INFO UPDATE FAILED", Toast.LENGTH_LONG).show();
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

    public class validateAccount extends AsyncTask<String, Void, String> {
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
                //urlConnection.setRequestProperty("Authorization", "Bearer " + ProfileParser.token);
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
                                if(obj.has("beneficiaryName") && !obj.getString("beneficiaryName").isEmpty())
                                {
                                    button.setEnabled(true);
                                    button.setText("PROCEED");
                                    control = 1;
                                    accountholder.getEditText().setText(obj.getString("beneficiaryName"));
                                    SignupVr.accountholder = obj.getString("beneficiaryName");
                                }else
                                {
                                    button.setEnabled(true);
                                    button.setText("FAILED. RETRY");
                                    control = 0;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                button.setEnabled(false);
                                button.setText("RETRY LATER");
                                control = 0;
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "REQUEST FAILED: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "REQUEST NOT SUCCESSFUL");
                            button.setEnabled(false);
                            button.setText("RETRY LATER 2x");
                            control = 0;
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
        intent.setClass(EditBank.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}