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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;

public class Data extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView, telco, fetchbills;
    TextInputLayout amount, reference;
    Button button;
    private static final String TAG = BillsPayment.class.getSimpleName();
    static int length = 0;
    static int validate = 0; //0 for pay, 1 for pull lists, 2 for validation
    String hintDisp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        amount = (TextInputLayout) findViewById(R.id.amount);
        reference = (TextInputLayout) findViewById(R.id.reference);
        button = (Button)findViewById(R.id.button);
        telco = (AutoCompleteTextView) findViewById(R.id.telco);
        fetchbills = (AutoCompleteTextView)findViewById(R.id.fetchbills);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.biller);
        amount.setEnabled(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Data.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        String[] option = {"DATA"};
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

        fetchbills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
                String val = fetchbills.getText().toString();
                Log.i(TAG, "SELECTED VALUE: " + val);
                if(autoCompleteTextView.getText().toString().equals("SMILE"))
                {
                    int lv = Arrays.asList(billsnames).indexOf(val);
                    amount.getEditText().setText(billsvalue[lv]);
                    amount.setEnabled(false);
                }else if(autoCompleteTextView.getText().toString().equals("DSTV SHOWMAX"))
                {
                    int lv = Arrays.asList(billsnames).indexOf(val);
                    amount.getEditText().setText(billsvalue[lv]);
                    amount.setEnabled(false);
                }else if(autoCompleteTextView.getText().toString().equals("DATA"))
                {
                    int lv = Arrays.asList(billsnames).indexOf(val);
                    amount.getEditText().setText(billsvalue[lv]);
                    amount.setEnabled(false);
                }
            }

        });

        telco.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
                String val = fetchbills.getText().toString();
                Log.i(TAG, "SELECTED VALUE: " + val);
                validate = 1;
                button.setText("PULLING LIST");
                stimulateButton();
            }

        });

        reference.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String dt = reference.getEditText().getText().toString();
                Log.i(TAG, "AFTERTEXTCHANGED: " + dt);
                if(dt.length() >= length)
                {
                    amount.setVisibility(View.VISIBLE);
                    amount.setHint("");
                    button.setVisibility(View.VISIBLE);
                    if(autoCompleteTextView.getText().toString().equals("SMILE"))
                    {
                        //validate = 1;
                        //button.setText("GET SMILE LISTS");
                        //stimulateButton();
                    }else if(autoCompleteTextView.getText().toString().equals("DSTV SHOWMAX"))
                    {
                        //validate = 1;
                        //button.setText("GET DATA LISTS");
                        //stimulateButton();
                    }else if(autoCompleteTextView.getText().toString().equals("DATA"))
                    {
                        //validate = 1;
                        //button.setText("GET DATA LISTS");
                        //stimulateButton();
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
                stimulateButton();
            }
        });
    }

    void stimulateButton()
    {
        if(validate == 0)
        {
            String amt = amount.getEditText().getText().toString().trim();
            if(amt.isEmpty())
            {
                amount.setFocusable(true);
                Toast.makeText(Data.this, "Input Amount", Toast.LENGTH_LONG).show();
            }else if(Double.parseDouble(amt) < 1)
            {
                amount.setFocusable(true);
                Toast.makeText(Data.this, "Amount Less than 1", Toast.LENGTH_LONG).show();
            }else
            {
                DecimalFormat f = new DecimalFormat("##.00");
                Double amtt = Double.parseDouble(amt);
                amount.getEditText().setText(f.format(amtt));
                paymentProcessor();
            }
        }else if(validate == 1)
        {
            pullList();
        }else if(validate == 2)
        {
            String amt = amount.getEditText().getText().toString().trim();
            if(amt.isEmpty())
            {
                amount.setFocusable(true);
                Toast.makeText(Data.this, "Input Amount", Toast.LENGTH_LONG).show();
            }else if(Double.parseDouble(amt) < 1)
            {
                amount.setFocusable(true);
                Toast.makeText(Data.this, "Amount Less than 1", Toast.LENGTH_LONG).show();
            }else {
                validate = 0;
            }
        }
    }

    void ProcessSelected(String val)
    {
        if(val.equals("SMILE"))
        {
            //pull list
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER ACCOUNT NUMBER");
            reference.setCounterMaxLength(11);
            button.setText("VALIDATE NUMBER");
            length = 11;
        }else if(val.equals("DSTV SHOWMAX"))
        {
            //pull list
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER ACCOUNT NUMBER");
            reference.setCounterMaxLength(11);
            button.setText("VALIDATE NUMBER");
            length = 11;
        }else if(val.equals("DATA"))
        {
            //Pull list
            telco.setVisibility(View.VISIBLE);
            String[] option = {"airtel", "9mobile", "glo", "mtn", "spectranet"};
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
            telco.setAdapter(adapter);
            telco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    telco.showDropDown();
                }
            });
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER PHONE NUMBER");
            reference.setCounterMaxLength(11);
            button.setText("PULL DATA LIST");
            length = 11;
        }
    }


    private void pullList()
    {
        JSONObject obj = new JSONObject();
        try {
            if(autoCompleteTextView.getText().toString().equals("DATA"))
            {
                if(true)
                {
                    button.setEnabled(false);
                    billsnames = new String[0];
                    billscode = new String[0];
                    billsvalue = new String[0];

                    String network = telco.getText().toString().toLowerCase();
                    if(network.equalsIgnoreCase("airtel")){
                        JSONArray arr = new JSONArray(Utilities.airtel);
                        billsResponse = Utilities.airtel;
                        billsnames = new String[arr.length()];
                        billscode = new String[arr.length()];
                        billsvalue = new String[arr.length()];
                        for(int i = 0; i < arr.length(); i++)
                        {
                            JSONObject b2 = arr.getJSONObject(i);
                            billsnames[i] = b2.getString("ItemName");
                            billscode[i] = b2.getString("ItemType");
                            billsvalue[i] = b2.getString("Amount");
                        }
                    }else if(network.equalsIgnoreCase("9mobile")){
                        JSONArray arr = new JSONArray(Utilities.etisalet);
                        billsResponse = Utilities.etisalet;
                        billsnames = new String[arr.length()];
                        billscode = new String[arr.length()];
                        billsvalue = new String[arr.length()];
                        for(int i = 0; i < arr.length(); i++)
                        {
                            JSONObject b2 = arr.getJSONObject(i);
                            billsnames[i] = b2.getString("ItemName");
                            billscode[i] = b2.getString("ItemType");
                            billsvalue[i] = b2.getString("Amount");
                        }
                    }else if(network.equalsIgnoreCase("spectranet")){
                        JSONArray arr = new JSONArray(Utilities.spectranet);
                        billsResponse = Utilities.spectranet;
                        billsnames = new String[arr.length()];
                        billscode = new String[arr.length()];
                        billsvalue = new String[arr.length()];
                        for(int i = 0; i < arr.length(); i++)
                        {
                            JSONObject b2 = arr.getJSONObject(i);
                            billsnames[i] = b2.getString("ItemName");
                            billscode[i] = b2.getString("ItemType");
                            billsvalue[i] = b2.getString("Amount");
                        }
                    }else if(network.equalsIgnoreCase("glo")){
                        JSONArray arr = new JSONArray(Utilities.glo);
                        billsResponse = Utilities.glo;
                        billsnames = new String[arr.length()];
                        billscode = new String[arr.length()];
                        billsvalue = new String[arr.length()];
                        for(int i = 0; i < arr.length(); i++)
                        {
                            JSONObject b2 = arr.getJSONObject(i);
                            billsnames[i] = b2.getString("ItemName");
                            billscode[i] = b2.getString("ItemType");
                            billsvalue[i] = b2.getString("Amount");
                        }
                    }else if(network.equalsIgnoreCase("mtn")){
                        JSONArray arr = new JSONArray(Utilities.mtn);
                        billsResponse = Utilities.mtn;
                        billsnames = new String[arr.length()];
                        billscode = new String[arr.length()];
                        billsvalue = new String[arr.length()];
                        for(int i = 0; i < arr.length(); i++)
                        {
                            JSONObject b2 = arr.getJSONObject(i);
                            billsnames[i] = b2.getString("ItemName");
                            billscode[i] = b2.getString("ItemType");
                            billsvalue[i] = b2.getString("Amount");
                        }
                    }
                    fetchbills.setVisibility(View.VISIBLE);
                    ArrayList<String> lst = new ArrayList<String>(Arrays.asList(billsnames));
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.option_item, lst);
                    fetchbills.setAdapter(adapter);
                    fetchbills.setText("");
                    validate = 0;
                    button.setEnabled(true);
                    button.setText("PAY DATA ACCOUNT");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Data.this, "GETTING LIST FAILED", Toast.LENGTH_LONG).show();
        }
    }

    String[] billsnames = new String[0];
    String[] billscode = new String[0];
    String[] billsvalue = new String[0];
    public static String billsResponse = "";

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

    private void paymentProcessor()
    {
        try {
            if(autoCompleteTextView.getText().toString().equals("SMILE"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(Data.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(Data.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "PROVIDER: \t" +  "SMILE" + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("DSTV SHOWMAX"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(Data.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(Data.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "PROVIDER: \t" +  "DSTV SHOWMAX" + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("DATA"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(Data.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(Data.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    //JSONObject obj = new JSONObject(billsResponse);
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "NAME: \t" +  fetchbills.getText().toString() + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "PROVIDER: \t" +  telco.getText().toString().toUpperCase() + "\n";
                    ConfirmPay(form);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Data.this, "PAYMENT FAILED", Toast.LENGTH_LONG).show();
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
                    if(autoCompleteTextView.getText().toString().equals("SMILE"))
                    {
                        obj.put("amount", Utilities.getWholeAmount(amount.getEditText().getText().toString().trim()));
                        obj.put("billsname", telco.getText().toString().toUpperCase());
                        obj.put("userReference", uuid);
                        obj.put("datacode", "smile");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service_type", "smile");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        button.setText("PLEASE WAIT");
                        new Pay().execute(BASEURL + "/apis/etop/itex/internet/payment", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("DSTV SHOWMAX"))
                    {
                        obj.put("amount", Utilities.getWholeAmount(amount.getEditText().getText().toString().trim()));
                        obj.put("billsname", telco.getText().toString().toUpperCase());
                        obj.put("userReference", uuid);
                        obj.put("datacode", "dstvshowmax");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service_type", "dstvshowmax");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        button.setText("PLEASE WAIT");
                        new Pay().execute(BASEURL + "/apis/etop/itex/internet/payment", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("DATA"))
                    {
                        String val = fetchbills.getText().toString();
                        int lv = Arrays.asList(billsnames).indexOf(val);
                        String code = billscode[lv];
//                        obj.put("amount", Utilities.getWholeAmount(amount.getEditText().getText().toString().trim()));
                        obj.put("amount", billsvalue[lv]);
                        obj.put("billsname", telco.getText().toString().toUpperCase() + "VTU");
                        obj.put("userReference", uuid);
                        obj.put("datacode", code);
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service_type", telco.getText().toString().toLowerCase());
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        button.setText("PLEASE WAIT");
                        new Pay().execute(BASEURL + "/apis/etop/bills/pay/internet", obj.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Data.this, "PAYMENT FAILED", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "USER CANCELED...", Toast.LENGTH_SHORT).show();
                dialog.cancel();
                Intent intent = new Intent();
                intent.setClass(Data.this, Dashboard.class);
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
                                    Toast.makeText(Data.this, obj.getString("errormessage"), Toast.LENGTH_LONG).show();

                                    ProfileParser.sending = new String[128];
                                    ProfileParser.receiving = new String[128];
                                    ProfileParser.sending[62] = server_response;
                                    ProfileParser.totalAmount = amount.getEditText().getText().toString().trim();
                                    ProfileParser.Amount = amount.getEditText().getText().toString().trim();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String pre = dateFormat.format(new Date());
                                    String rrn = pre.substring(2);
                                    ProfileParser.sending[37] = uuid;
                                    ProfileParser.receiving[39] = ProfileParser.processError(obj.getString("errormessage"));;
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

                                    DATABASEHANDLER db = new DATABASEHANDLER(Data.this);
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
                                    intent.setClass(Data.this, TransactionDetails.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(obj.has("responseCode"))
                                {
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

                                    DATABASEHANDLER db = new DATABASEHANDLER(Data.this);
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
                                    intent.setClass(Data.this, TransactionDetails.class);
                                    startActivity(intent);
                                    finish();
                                }else
                                {
                                    Toast.makeText(Data.this, "TRANSACTION FAILED", Toast.LENGTH_LONG).show();
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

                                    DATABASEHANDLER db = new DATABASEHANDLER(Data.this);
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
                                    intent.setClass(Data.this, TransactionDetails.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Data.this, "TRANSACTION FAILED", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                intent.setClass(Data.this, Dashboard.class);
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

                            Toast.makeText(Data.this, "TRANSACTION FAILED", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.setClass(Data.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(Data.this, "TRANSACTION FAILED", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(Data.this, Dashboard.class);
                startActivity(intent);
                finish();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Data.this, "TRANSACTION FAILED", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(Data.this, Dashboard.class);
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
        intent.setClass(Data.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}