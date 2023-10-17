package com.gbikna.sample.etop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.TransactionDetails;
import com.gbikna.sample.gbikna.util.database.DATABASEHANDLER;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.gbikna.util.utilities.Utilities;

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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;
import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.destination;

public class Transfer extends AppCompatActivity {
    private static final String TAG = Transfer.class.getSimpleName();
    AutoCompleteTextView autoCompleteTextView;
    TextInputLayout amount, accountNumber, narration, valres, transferpin;
    Button valsend;
    String[] option = new String[50];
    String[] code = new String[50];
    String validationResponse = "";
    int twerk = 0;

    void sanitizeAmount()
    {
        Log.i(TAG, "AMOUNT TO USE: " + ProfileParser.totalAmount);
        if(ProfileParser.totalAmount.charAt(ProfileParser.totalAmount.length() - 2) == '.')
            ProfileParser.totalAmount = ProfileParser.totalAmount + "0";
        Log.i(TAG, "AMOUNT TO USE: " + ProfileParser.totalAmount);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        //sanitizeAmount();

        amount = (TextInputLayout) findViewById(R.id.amount);
        accountNumber = (TextInputLayout) findViewById(R.id.acct_no);
        narration = (TextInputLayout)findViewById(R.id.narration);
        valres = (TextInputLayout)findViewById(R.id.valres);
        valres.setEnabled(false);
        valsend = (Button)findViewById(R.id.btn_validate);
        transferpin = (TextInputLayout)findViewById(R.id.transferpin);
        transferpin.setEnabled(false);

        //amount.getEditText().setText("NGN " + ProfileParser.totalAmount);
        //amount.setEnabled(false);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.banks);
        String pro = "[{\"bankCode\":\"090267\",\"bankName\":\"Kuda.\"},{\"bankCode\":\"000014\",\"bankName\":\"Access Bank\"},{\"bankCode\":\"000005\",\"bankName\":\"Access Bank PLC (Diamond)\"},{\"bankCode\":\"090134\",\"bankName\":\"ACCION MFB\"},{\"bankCode\":\"090148\",\"bankName\":\"Bowen MFB\"},{\"bankCode\":\"100005\",\"bankName\":\"Cellulant\"},{\"bankCode\":\"000009\",\"bankName\":\"Citi Bank\"},{\"bankCode\":\"100032\",\"bankName\":\"Contec Global\"},{\"bankCode\":\"060001\",\"bankName\":\"Coronation\"},{\"bankCode\":\"090156\",\"bankName\":\"e-BARCs MFB\"},{\"bankCode\":\"000010\",\"bankName\":\"Ecobank Bank\"},{\"bankCode\":\"100008\",\"bankName\":\"Ecobank Xpress Account\"},{\"bankCode\":\"090097\",\"bankName\":\"Ekondo MFB\"},{\"bankCode\":\"000003\",\"bankName\":\"FCMB\"},{\"bankCode\":\"000007\",\"bankName\":\"Fidelity Bank\"},{\"bankCode\":\"000016\",\"bankName\":\"First Bank of Nigeria\"},{\"bankCode\":\"110002\",\"bankName\":\"Flutterwave Technology solutions Limited\"},{\"bankCode\":\"100022\",\"bankName\":\"GoMoney\"},{\"bankCode\":\"000013\",\"bankName\":\"GTBank Plc\"},{\"bankCode\":\"000020\",\"bankName\":\"Heritage\"},{\"bankCode\":\"090175\",\"bankName\":\"HighStreet MFB\"},{\"bankCode\":\"000006\",\"bankName\":\"JAIZ Bank\"},{\"bankCode\":\"090003\",\"bankName\":\"JubileeLife\"},{\"bankCode\":\"090191\",\"bankName\":\"KCMB MFB\"},{\"bankCode\":\"000002\",\"bankName\":\"Keystone Bank\"},{\"bankCode\":\"090171\",\"bankName\":\"Mainstreet MFB\"},{\"bankCode\":\"100026\",\"bankName\":\"ONE FINANCE\"},{\"bankCode\":\"100002\",\"bankName\":\"Paga\"},{\"bankCode\":\"100033\",\"bankName\":\"PALMPAY\"},{\"bankCode\":\"100003\",\"bankName\":\"Parkway-ReadyCash\"},{\"bankCode\":\"110001\",\"bankName\":\"PayAttitude Online\"},{\"bankCode\":\"100004\",\"bankName\":\"Paycom(OPay)\"},{\"bankCode\":\"000008\",\"bankName\":\"POLARIS BANK\"},{\"bankCode\":\"000023\",\"bankName\":\"Providus Bank \"},{\"bankCode\":\"000024\",\"bankName\":\"Rand Merchant Bank\"},{\"bankCode\":\"000012\",\"bankName\":\"StanbicIBTC Bank\"},{\"bankCode\":\"100007\",\"bankName\":\"StanbicMobileMoney\"},{\"bankCode\":\"000021\",\"bankName\":\"StandardChartered\"},{\"bankCode\":\"000001\",\"bankName\":\"Sterling Bank\"},{\"bankCode\":\"000022\",\"bankName\":\"SUNTRUST BANK\"},{\"bankCode\":\"000018\",\"bankName\":\"Union Bank\"},{\"bankCode\":\"000004\",\"bankName\":\"United Bank for Africa\"},{\"bankCode\":\"000011\",\"bankName\":\"Unity Bank\"},{\"bankCode\":\"090110\",\"bankName\":\"VFD MFB\"},{\"bankCode\":\"000017\",\"bankName\":\"Wema Bank\"},{\"bankCode\":\"000015\",\"bankName\":\"ZENITH BANK PLC\"},{\"bankCode\":\"100025\",\"bankName\":\"Zinternet - KongaPay\"}]";
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
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showDropDown();
            }
        });

        accountNumber.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String dt = accountNumber.getEditText().getText().toString();
                Log.i("BANKINFO", "AFTERTEXTCHANGED: " + dt);
                if(dt.length() == 10)
                {
                    valsend.setText("VALIDATING");
                    Proceed();
                }else
                {
                    twerk = 0;
                    valsend.setText("VALIDATE");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String dt = accountNumber.getEditText().getText().toString();
                Log.i("BANKINFO", "BEFORETEXTCHANGED: " + dt);
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String dt = accountNumber.getEditText().getText().toString();
                Log.i("BANKINFO", "ONTEXTCHANGED: " + dt);
            }
        });

        valsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Proceed();
            }
        });

        transferpin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    transferpin.setHint("Transfer Pin");
                } else {
                    transferpin.setHint("");
                }
            }
        });

    }

    public void Proceed() {
        String accountnumber = accountNumber.getEditText().getText().toString().trim();
        ProfileParser.destination = accountnumber;
        String description = narration.getEditText().getText().toString().trim();
        if(description.isEmpty())
            ProfileParser.description = "NA";
        else
            ProfileParser.description = description;
        String bankname = autoCompleteTextView.getText().toString();
        ProfileParser.accountbank = bankname;
        String transferp = transferpin.getEditText().getText().toString().trim();

        String bankcode = "";
        if (!bankname.isEmpty()) {
            int i = Arrays.asList(option).indexOf(bankname);
            bankcode = code[i];
        }
        ProfileParser.bankcode = bankcode;

        if (accountnumber.isEmpty()) {
            accountNumber.setFocusable(true);
            Toast.makeText(Transfer.this, "Please Provide Account Number", Toast.LENGTH_LONG).show();
        } else if (bankname.isEmpty()) {
            autoCompleteTextView.setFocusable(true);
            Toast.makeText(Transfer.this, "Please Select Bank", Toast.LENGTH_LONG).show();
        } else if (transferp.isEmpty() & twerk == 1) {
            transferpin.setFocusable(true);
            Toast.makeText(Transfer.this, "Please Input Pin", Toast.LENGTH_LONG).show();
        } /*else if (amount.getEditText().getText().toString().isEmpty()) {
            autoCompleteTextView.setFocusable(true);
            Toast.makeText(Transfer.this, "Please Enter Amount", Toast.LENGTH_LONG).show();
        } */else{
            if(twerk == 0)
            {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("bankcode", bankcode);
                    obj.put("destination", destination);
                    Log.i(TAG, "VALIDATION REQUEST: " + obj.toString());
                    valsend.setEnabled(false);
                    valsend.setText("PLEASE WAIT");
                    new TransferData().execute(BASEURL + "/apis/etop/kuda/bankvalidation/", obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
            {
                if (amount.getEditText().getText().toString().isEmpty()) {
                    autoCompleteTextView.setFocusable(true);
                    Toast.makeText(Transfer.this, "Please Enter Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    DecimalFormat f = new DecimalFormat("##.00");
                    Double amtt = Double.parseDouble(amount.getEditText().getText().toString().trim());
                    amount.getEditText().setText(f.format(amtt));
                    try {
                        ProfileParser.Amount = amount.getEditText().getText().toString().trim();
                        ProfileParser.Fee = "0.00";
                        //DecimalFormat f = new DecimalFormat("##.00");
                        Double amt = Double.parseDouble(ProfileParser.Amount);
                        Double fee = Double.parseDouble(ProfileParser.Fee);
                        Double tt = amt + fee;
                        ProfileParser.totalAmount = f.format(tt);
                        Log.i(TAG, "GOING TO GET CARD");
                        Log.i(TAG, "FOR ICC, MAG SWIPE, NFC");
                        Log.i(TAG, "AMOUNT: " + ProfileParser.Amount);
                        Log.i(TAG, "FEE: " + ProfileParser.Fee);
                        Log.i(TAG, "TOTAL: " + ProfileParser.totalAmount);

                        ProfileParser.sending = new String[128];

                        JSONObject unpack = new JSONObject(validationResponse);

                        JSONObject obj = new JSONObject();
                        obj.put("amount", ProfileParser.totalAmount);
                        obj.put("description", ProfileParser.description);
                        obj.put("bankname", ProfileParser.accountbank);
                        obj.put("bankcode", ProfileParser.bankcode);
                        obj.put("destination", ProfileParser.destination);
                        obj.put("receivername", ProfileParser.receivername);
                        obj.put("sessionid", unpack.getString("sessionID"));
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", transferp);
                        ProfileParser.sending[11] = unpack.getString("sessionID");
                        ProfileParser.sending[62] = validationResponse;
                        Log.i(TAG, "PAYMENT REQUEST: " + obj.toString());
                        valsend.setEnabled(false);
                        valsend.setText("PLEASE WAIT");
                        new TransferData().execute(BASEURL + "/apis/etop/kuda/transfer/", obj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public class TransferData extends AsyncTask<String, Void, String> {
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
                                if(twerk == 2)
                                {
                                    ProfileParser.receiving = new String[128];
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String pre = dateFormat.format(new Date());
                                    String rrn = pre.substring(2);
                                    ProfileParser.sending[37] = rrn;
                                    if(obj.has("message") && obj.getInt("code") == 200)
                                    {
                                        ProfileParser.receiving[39] = "00";
                                        ProfileParser.sending[37] = obj.getString("reference");
                                    }else if(obj.has("message") && obj.getInt("code") == 600)
                                    {
                                        ProfileParser.receiving[39] = "A9";
                                    }else
                                    {
                                        ProfileParser.receiving[39] = ProfileParser.processError(obj.getString("message"));
                                    }
                                    ProfileParser.sending[0] = "0200";
                                    ProfileParser.sending[3] = "000000";
                                    ProfileParser.sending[35] = "0000000000000000D0000";
                                    ProfileParser.sending[23] = "000";
                                    ProfileParser.sending[22] = "051";
                                    ProfileParser.sending[123] = "510101511344101";
                                    ProfileParser.sending[26] = "06";
                                    ProfileParser.sending[14] = "0000";
                                    ProfileParser.sending[2] = "0000000000000000";
                                    ProfileParser.field2 = "0000000000000000";
                                    ProfileParser.cardName = "NA";
                                    ProfileParser.cardAid = "00000000000000";
                                    ProfileParser.sending[28] = "C00000000";
                                    ProfileParser.cardType = "NA";

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddhhmmss");
                                    String datetime = simpleDateFormat.format(new Date());
                                    ProfileParser.sending[7] = datetime;

                                    simpleDateFormat = new SimpleDateFormat("hhmmss");
                                    String stan = simpleDateFormat.format(new Date());
                                    //ProfileParser.sending[11] = stan;

                                    DATABASEHANDLER db = new DATABASEHANDLER(Transfer.this);
                                    db.EODFIRST(ProfileParser.sending[0], ProfileParser.sending[7], ProfileParser.sending[35],
                                            ProfileParser.sending[3], ProfileParser.totalAmount, ProfileParser.sending[37],
                                            ProfileParser.sending[23], ProfileParser.sending[22], ProfileParser.sending[123],
                                            ProfileParser.sending[11], ProfileParser.sending[26], "NA",
                                            ProfileParser.sending[13], ProfileParser.txnName, ProfileParser.cardType,
                                            ProfileParser.sending[14], Utilities.maskedPan(ProfileParser.sending[2]), ProfileParser.cardName,
                                            ProfileParser.cardAid, ProfileParser.sending[28], ProfileParser.totalAmount);

                                    String resp = Utilities.readFileAsString("receipt.txt", getApplicationContext());
                                    if(Long.parseLong(resp) > 1000) {
                                        ProfileParser.receiptNum = 0 + 1;
                                    }else
                                        ProfileParser.receiptNum = Long.parseLong(resp) + 1;
                                    Utilities.writeStringAsFile(String.valueOf(ProfileParser.receiptNum), "receipt.txt", getApplicationContext());
                                    Log.i(TAG, "LOVE SAID");
                                    db.UPDATEEOD(ProfileParser.receiving[38], ProfileParser.receiving[39], String.valueOf(ProfileParser.receiptNum),
                                            Utilities.getDATEYYYYMMDD(ProfileParser.sending[13]), ProfileParser.sending[7], ProfileParser.sending[62]);

                                    Intent intent = new Intent();
                                    Bundle data = new Bundle();
                                    data.putString("response", server_response);
                                    intent.putExtras(data);
                                    intent.setClass(Transfer.this, TransactionDetails.class);
                                    startActivity(intent);
                                    finish();
                                }else
                                {
                                    if(obj.has("beneficiaryName") && !obj.getString("beneficiaryName").isEmpty()
                                    && !obj.getString("beneficiaryName").equals("null"))
                                    {
                                        valsend.setEnabled(true);
                                        valsend.setText("CREDIT BENEFICIARY");
                                        validationResponse = server_response;
                                        valres.getEditText().setText(obj.getString("beneficiaryName"));
                                        ProfileParser.receivername = obj.getString("beneficiaryName");
                                        twerk = 2;
                                        accountNumber.setEnabled(true);
                                        narration.setEnabled(true);
                                        transferpin.setEnabled(true);
                                    }else
                                    {
                                        valsend.setEnabled(true);
                                        valsend.setText("Validate");
                                        valres.getEditText().setText("FAILED");
                                        twerk = 0;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                valsend.setEnabled(false);
                                valsend.setText("RETRY LATER");
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "REQUEST FAILED: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "REQUEST NOT SUCCESSFUL");
                            valsend.setEnabled(false);
                            valsend.setText("RETRY LATER");
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
        intent.setClass(Transfer.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}