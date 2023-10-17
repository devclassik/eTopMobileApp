package com.gbikna.sample.etop.bills;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.TransactionDetails;
import com.gbikna.sample.gbikna.util.database.DATABASEHANDLER;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.gbikna.util.utilities.Utilities;
import com.gbikna.sample.etop.BillsPayment;
import com.gbikna.sample.etop.Dashboard;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;

public class Vtu extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;
    TextInputLayout amount, reference;
    Button button;
    private static final String TAG = BillsPayment.class.getSimpleName();
    static int length = 0;
    static int validate = 0; //0 for pay, 1 for pull lists, 2 for validation
    String hintDisp = "";
    TextWatcher tt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vtu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Vtu.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        amount = (TextInputLayout) findViewById(R.id.amount);
        reference = (TextInputLayout) findViewById(R.id.reference);
        button = (Button)findViewById(R.id.button);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.biller);

        String[] option = {"AIRTEL", "9MOBILE", "GLO", "MTN"};
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showDropDown();
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
                Log.i("DATA", "Selected Biller: " + autoCompleteTextView.getText().toString());
                if(!autoCompleteTextView.getText().toString().isEmpty())
                {
                    String val = autoCompleteTextView.getText().toString();
                    Log.i(TAG, "SELECTED VALUE: " + val);
                    ProcessSelected(val);
                }else
                {
                    Log.i("TAG", "Nothing was selected");
                }
            }
        });

        reference.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String dt = reference.getEditText().getText().toString();
                Log.i(TAG, "AFTERTEXTCHANGED: " + dt);
                if(dt.length() >= length)
                {
                    amount.setVisibility(View.VISIBLE);
                    amount.setHint("0.00");
                    button.setVisibility(View.VISIBLE);
                    if(autoCompleteTextView.getText().toString().equals("AIRTEL"))
                    {
                        validate = 0;
                        button.setText("LOAD: " + reference.getEditText().getText().toString().trim());
                    }else if(autoCompleteTextView.getText().toString().equals("9MOBILE"))
                    {
                        validate = 0;
                        button.setText("LOAD: " + reference.getEditText().getText().toString().trim());
                    }else if(autoCompleteTextView.getText().toString().equals("GLO"))
                    {
                        validate = 0;
                        button.setText("LOAD: " + reference.getEditText().getText().toString().trim());
                    }else if(autoCompleteTextView.getText().toString().equals("MTN"))
                    {
                        validate = 0;
                        button.setText("LOAD: " + reference.getEditText().getText().toString().trim());
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String dt = reference.getEditText().getText().toString();
                Log.i(TAG, "BEFORETEXTCHANGED: " + dt);
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String dt = reference.getEditText().getText().toString();
                if(dt.length() > 0)
                    reference.setHint("");
                else
                    reference.setHint(hintDisp);
                Log.i(TAG, "ONTEXTCHANGED: " + dt);
            }
        });


        amount.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String dt = amount.getEditText().getText().toString();
                Log.i(TAG, "AFTERTEXTCHANGED: " + dt);
                //amount.getEditText().setText(String.format("%.2f", Double.parseDouble(dt)));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String dt = amount.getEditText().getText().toString();
                Log.i(TAG, "BEFORETEXTCHANGED: " + dt);
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String dt = amount.getEditText().getText().toString();
                if(dt.length() > 0)
                    amount.setHint("");
                else
                    amount.setHint("Amount");
                Log.i(TAG, "ONTEXTCHANGED: " + dt);

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate == 0)
                {
                    String amt = amount.getEditText().getText().toString().trim();
                    if(amt.isEmpty())
                    {
                        amount.setFocusable(true);
                        Toast.makeText(Vtu.this, "Input Amount", Toast.LENGTH_LONG).show();
                    }else if(Double.parseDouble(amt) < 1)
                    {
                        amount.setFocusable(true);
                        Toast.makeText(Vtu.this, "Amount Less than 1", Toast.LENGTH_LONG).show();
                    }else
                    {
                        DecimalFormat f = new DecimalFormat("##.00");
                        Double amtt = Double.parseDouble(amt);
                        amount.getEditText().setText(f.format(amtt));
                        paymentProcessor();
                    }
                }
            }
        });

    }

    void ProcessSelected(String val)
    {
        if(val.equals("AIRTEL"))
        {
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER AIRTEL NUMBER");
            hintDisp = "ENTER AIRTEL NUMBER";
            reference.setCounterMaxLength(11);
            button.setText("PAY AIRTEL");
            length = 11;
        }else if(val.equals("9MOBILE"))
        {
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER 9MOBILE NUMBER");
            hintDisp = "ENTER 9MOBILE NUMBER";
            reference.setCounterMaxLength(11);
            button.setText("PAY 9MOBILE");
            length = 11;
        }else if(val.equals("GLO"))
        {
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER GLO NUMBER");
            hintDisp = "ENTER GLO NUMBER";
            reference.setCounterMaxLength(11);
            button.setText("PAY GLO");
            length = 11;
        }else if(val.equals("MTN"))
        {
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER MTN NUMBER");
            hintDisp = "ENTER MTN NUMBER";
            reference.setCounterMaxLength(11);
            button.setText("PAY MTN");
            length = 11;
        }
    }


    private void paymentProcessor()
    {
        try {
            if(autoCompleteTextView.getText().toString().equals("AIRTEL"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(Vtu.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(Vtu.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    String form = "PHONE NUMBER: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "PROVIDER: \t" +  "AIRTEL" + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("9MOBILE"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(Vtu.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(Vtu.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    String form = "PHONE NUMBER: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "PROVIDER: \t" +  "9MOBILE" + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("GLO"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(Vtu.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(Vtu.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    String form = "PHONE NUMBER: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "PROVIDER: \t" +  "GLO" + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("MTN"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(Vtu.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(Vtu.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    String form = "PHONE NUMBER: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "PROVIDER: \t" +  "MTN" + "\n";
                    ConfirmPay(form);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Vtu.this, "PAYMENT FAILED", Toast.LENGTH_LONG).show();
        }
    }

    public static String uuid = "";
    void ConfirmPay(String display)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setCancelable(false);
        builder.setTitle("ENTER PIN");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        ProfileParser.destination = reference.getEditText().getText().toString().trim();
        // Set up the input
        final TextView vw = new TextView(this);
        vw.setText(display);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        layout.addView(input);
        layout.addView(vw);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pin = input.getText().toString();
                try {
                    JSONObject obj = new JSONObject();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    String pre = dateFormat.format(new Date());
                    uuid = pre.substring(2);
                    if(autoCompleteTextView.getText().toString().equals("AIRTEL"))
                    {
                        obj.put("amount", Utilities.getWholeAmount(amount.getEditText().getText().toString().trim()));
                        obj.put("billsname", "AIRTEL");
                        obj.put("channel", "11");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("telco", "airtel");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("userReference", uuid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        button.setText("PLEASE WAIT");
                        new Pay().execute(BASEURL + "/apis/etop/bills/pay/vtu", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("9MOBILE"))
                    {
                        obj.put("amount", Utilities.getWholeAmount(amount.getEditText().getText().toString().trim()));
                        obj.put("billsname", "9MOBILE");
                        obj.put("channel", "1143");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("telco", "9MOBILEAIRTIME");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("userReference", uuid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        button.setText("PLEASE WAIT");
                        new Pay().execute(BASEURL + "/apis/etop/bills/pay/vtu", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("GLO"))
                    {
                        obj.put("amount", Utilities.getWholeAmount(amount.getEditText().getText().toString().trim()));
                        obj.put("billsname", "GLO");
                        obj.put("channel", "61");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("telco", "glo");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("userReference", uuid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        button.setText("PLEASE WAIT");
                        new Pay().execute(BASEURL + "/apis/etop/bills/pay/vtu", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("MTN"))
                    {
                        obj.put("amount", Utilities.getWholeAmount(amount.getEditText().getText().toString().trim()));
                        obj.put("billsname", "MTN");
                        obj.put("channel", "60");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("telco", "mtn");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("userReference", uuid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        button.setText("PLEASE WAIT");
                        new Pay().execute(BASEURL + "/apis/etop/bills/pay/vtu", obj.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Vtu.this, "PAYMENT FAILED", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "USER CANCELED...", Toast.LENGTH_SHORT).show();
                dialog.cancel();
                Intent intent = new Intent();
                intent.setClass(Vtu.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }


    public class Pay extends AsyncTask<String, Void, String> {
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

                            try {
                                JSONObject obj = new JSONObject(server_response);
                                if(obj.has("errormessage"))
                                {
                                    Toast.makeText(Vtu.this, obj.getString("errormessage"), Toast.LENGTH_LONG).show();
                                    ProfileParser.sending = new String[128];
                                    ProfileParser.receiving = new String[128];
                                    ProfileParser.sending[62] = server_response;
                                    ProfileParser.totalAmount = amount.getEditText().getText().toString().trim();
                                    ProfileParser.Amount = amount.getEditText().getText().toString().trim();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String pre = dateFormat.format(new Date());
                                    String rrn = pre.substring(2);
                                    ProfileParser.sending[37] = uuid;
                                    ProfileParser.receiving[39] = ProfileParser.processError(obj.getString("errormessage"));
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
                                    ProfileParser.sending[11] = stan;

                                    DATABASEHANDLER db = new DATABASEHANDLER(Vtu.this);
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

                                    ProfileParser.txnName = autoCompleteTextView.getText().toString();
                                    Intent intent = new Intent();
                                    Bundle data = new Bundle();
                                    data.putString("response", server_response);
                                    intent.putExtras(data);
                                    intent.setClass(Vtu.this, TransactionDetails.class);
                                    startActivity(intent);
                                    finish();
                                }else if(obj.has("responseCode"))
                                {
                                    ////{
                                    //            //    "": "00",
                                    //            //    "responseMessage": "Success: SUCCESSFUL",
                                    //            //    "transRef": "8224150966346689",
                                    //            //    "otherInfo": null,
                                    //            //    "responseData": null,
                                    //            //    "userAcct": null,
                                    //            //    "acctBalance": null
                                    //            //}
                                    ProfileParser.sending = new String[128];
                                    ProfileParser.receiving = new String[128];
                                    ProfileParser.sending[62] = server_response;
                                    ProfileParser.totalAmount = amount.getEditText().getText().toString().trim();
                                    ProfileParser.Amount = amount.getEditText().getText().toString().trim();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String pre = dateFormat.format(new Date());
                                    String rrn = pre.substring(2);
                                    ProfileParser.sending[37] = uuid;
                                    if(obj.has("responseCode") && obj.getString("responseCode").equals("00"))
                                        ProfileParser.receiving[39] = "00";
                                    else
                                        ProfileParser.receiving[39] = "06";
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
                                    ProfileParser.sending[11] = stan;

                                    DATABASEHANDLER db = new DATABASEHANDLER(Vtu.this);
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

                                    ProfileParser.txnName = autoCompleteTextView.getText().toString();
                                    Intent intent = new Intent();
                                    Bundle data = new Bundle();
                                    data.putString("response", server_response);
                                    intent.putExtras(data);
                                    intent.setClass(Vtu.this, TransactionDetails.class);
                                    startActivity(intent);
                                    finish();
                                }else
                                {
                                    Toast.makeText(Vtu.this, "TRANSACTION FAILED", Toast.LENGTH_LONG).show();
                                    ProfileParser.sending = new String[128];
                                    ProfileParser.receiving = new String[128];
                                    ProfileParser.sending[62] = server_response;
                                    ProfileParser.totalAmount = amount.getEditText().getText().toString().trim();
                                    ProfileParser.Amount = amount.getEditText().getText().toString().trim();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String pre = dateFormat.format(new Date());
                                    String rrn = pre.substring(2);
                                    ProfileParser.sending[37] = uuid;
                                    ProfileParser.receiving[39] = "06";
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
                                    ProfileParser.sending[11] = stan;

                                    DATABASEHANDLER db = new DATABASEHANDLER(Vtu.this);
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

                                    ProfileParser.txnName = autoCompleteTextView.getText().toString();
                                    Intent intent = new Intent();
                                    Bundle data = new Bundle();
                                    data.putString("response", server_response);
                                    intent.putExtras(data);
                                    intent.setClass(Vtu.this, TransactionDetails.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Vtu.this, "TRANSACTION FAILED", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                intent.setClass(Vtu.this, Dashboard.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "NOT SUCCESSFUL: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            Log.i(TAG, "KEY EXCHANGE NOT SUCCESSFUL");

                            Toast.makeText(Vtu.this, "TRANSACTION FAILED", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.setClass(Vtu.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(Vtu.this, "TRANSACTION FAILED", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(Vtu.this, Dashboard.class);
                startActivity(intent);
                finish();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Vtu.this, "TRANSACTION FAILED", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(Vtu.this, Dashboard.class);
                startActivity(intent);
                finish();
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
        intent.setClass(Vtu.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}