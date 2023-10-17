package com.gbikna.sample.etop;

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

import com.gbikna.sample.gbikna.TransactionDetails;
import com.gbikna.sample.gbikna.util.database.DATABASEHANDLER;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.gbikna.util.utilities.Utilities;
import com.google.android.material.textfield.TextInputLayout;
import com.gbikna.sample.R;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;

public class BillsPayment extends AppCompatActivity {
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
        setContentView(R.layout.activity_bills_payment);

        amount = (TextInputLayout) findViewById(R.id.amount);
        reference = (TextInputLayout) findViewById(R.id.reference);
        button = (Button)findViewById(R.id.button);
        telco = (AutoCompleteTextView) findViewById(R.id.telco);
        fetchbills = (AutoCompleteTextView)findViewById(R.id.fetchbills);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.biller);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BillsPayment.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        String[] option = {"AIRTEL", "9MOBILE", "GLO", "SMILE", "MTN", "STARTIMES",
                "DSTV", "GOTV", "DATA", "INTERNET", "IKEJA ELECTRIC", "IBEDC", "EKEDC", "EEDC", "AEDC", "PHEDC",
                "KEDCOOFFSITE", "KEDCOONSITE"};
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
                }else if(autoCompleteTextView.getText().toString().equals("STARTIMES"))
                {
                    int lv = Arrays.asList(billsnames).indexOf(val);
                    try {
                        JSONObject obj = new JSONObject(billsvalue[lv]);
                        amount.getEditText().setText(obj.getString("monthly"));
                        amount.setEnabled(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(autoCompleteTextView.getText().toString().equals("DSTV"))
                {
                    int lv = Arrays.asList(billsnames).indexOf(val);
                    amount.getEditText().setText(billsvalue[lv]);
                    amount.setEnabled(false);
                }else if(autoCompleteTextView.getText().toString().equals("GOTV"))
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
                    }else if(autoCompleteTextView.getText().toString().equals("SMILE"))
                    {
                        validate = 1;
                        button.setText("GET SMILE LISTS");
                    }else if(autoCompleteTextView.getText().toString().equals("STARTIMES"))
                    {
                        validate = 1;
                        button.setText("GET STARTIMES LISTS");
                    }else if(autoCompleteTextView.getText().toString().equals("DSTV"))
                    {
                        validate = 1;
                        button.setText("GET DSTV LISTS");
                    }else if(autoCompleteTextView.getText().toString().equals("GOTV"))
                    {
                        validate = 1;
                        button.setText("GET GOTV LISTS");
                    }else if(autoCompleteTextView.getText().toString().equals("DATA"))
                    {
                        validate = 1;
                        button.setText("GET DATA LISTS");
                    }else if(autoCompleteTextView.getText().toString().equals("INTERNET"))
                    {
                        validate = 1;
                        button.setText("VALIDATE INTERNET");
                    }else if(autoCompleteTextView.getText().toString().equals("IKEJA ELECTRIC"))
                    {
                        validate = 2;
                        button.setText("VALIDATE IE");
                    }else if(autoCompleteTextView.getText().toString().equals("IBEDC"))
                    {
                        validate = 2;
                        button.setText("VALIDATE IBEDC");
                    }else if(autoCompleteTextView.getText().toString().equals("EKEDC"))
                    {
                        validate = 2;
                        button.setText("VALIDATE EKEDC");
                    }else if(autoCompleteTextView.getText().toString().equals("EEDC"))
                    {
                        validate = 2;
                        button.setText("VALIDATE EEDC");
                    }else if(autoCompleteTextView.getText().toString().equals("AEDC"))
                    {
                        validate = 2;
                        button.setText("VALIDATE AEDC");
                    }else if(autoCompleteTextView.getText().toString().equals("PHEDC"))
                    {
                        validate = 2;
                        button.setText("VALIDATE PHEDC");
                    }else if(autoCompleteTextView.getText().toString().equals("KEDCOOFFSITE"))
                    {
                        validate = 2;
                        button.setText("VALIDATE KEDCOOFFSITE");
                    }else if(autoCompleteTextView.getText().toString().equals("KEDCOONSITE"))
                    {
                        validate = 2;
                        button.setText("VALIDATE KEDCOONSITE");
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
                if(validate == 0)
                {
                    String amt = amount.getEditText().getText().toString().trim();
                    if(amt.isEmpty())
                    {
                        amount.setFocusable(true);
                        Toast.makeText(BillsPayment.this, "Input Amount", Toast.LENGTH_LONG).show();
                    }else if(Double.parseDouble(amt) < 1)
                    {
                        amount.setFocusable(true);
                        Toast.makeText(BillsPayment.this, "Amount Less than 1", Toast.LENGTH_LONG).show();
                    }else
                    {
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
                        Toast.makeText(BillsPayment.this, "Input Amount", Toast.LENGTH_LONG).show();
                    }else if(Double.parseDouble(amt) < 1)
                    {
                        amount.setFocusable(true);
                        Toast.makeText(BillsPayment.this, "Amount Less than 1", Toast.LENGTH_LONG).show();
                    }else {
                        validateProcessor();
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
        }else if(val.equals("SMILE"))
        {
            //pull list
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER ACCOUNT NUMBER");
            reference.setCounterMaxLength(11);
            button.setText("VALIDATE NUMBER");
            length = 11;
        }else if(val.equals("STARTIMES"))
        {
            //pull list
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER SMART CARD CODE");
            reference.setCounterMaxLength(11);
            button.setText("VALIDATE NUMBER");
            length = 10;
        }else if(val.equals("DSTV"))
        {
            //Pull list
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER SMART CARD CODE");
            reference.setCounterMaxLength(11);
            button.setText("VALIDATE NUMBER");
            length = 10;
        }else if(val.equals("GOTV"))
        {
            //Pull list
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER SMART CARD CODE");
            reference.setCounterMaxLength(11);
            button.setText("VALIDATE NUMBER");
            length = 10;
        }else if(val.equals("DATA"))
        {
            //Pull list
            telco.setVisibility(View.VISIBLE);
            String[] option = {"AIRTELDATA", "9MOBILEDATA", "GLODATA", "MTNDATA"};
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
        }else if(val.equals("INTERNET"))
        {
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER ACCOUNT NUMBER");
            reference.setCounterMaxLength(11);
            button.setText("VALIDATE NUMBER");
            length = 11;
        }else if(val.equals("IKEJA ELECTRIC"))
        {
            telco.setVisibility(View.VISIBLE);
            String[] option = {"PREPAID", "POSTPAID"};
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
            telco.setAdapter(adapter);
            telco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    telco.showDropDown();
                }
            });
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER CARD NUMBER");
            reference.setCounterMaxLength(11);
            button.setText("VALIDATE NUMBER");
            length = 11;
        }else if(val.equals("IBEDC"))
        {
            telco.setVisibility(View.VISIBLE);
            String[] option = {"PREPAID", "POSTPAID"};
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
            telco.setAdapter(adapter);
            telco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    telco.showDropDown();
                }
            });
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER CARD NUMBER");
            reference.setCounterMaxLength(11);
            button.setText("VALIDATE NUMBER");
            length = 11;
        }else if(val.equals("EKEDC"))
        {
            telco.setVisibility(View.VISIBLE);
            String[] option = {"PREPAID", "POSTPAID"};
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
            telco.setAdapter(adapter);
            telco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    telco.showDropDown();
                }
            });
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER CARD NUMBER");
            button.setText("VALIDATE NUMBER");
            reference.setCounterMaxLength(11);
            length = 11;
        }else if(val.equals("EEDC"))
        {
            telco.setVisibility(View.VISIBLE);
            String[] option = {"PREPAID", "POSTPAID"};
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
            telco.setAdapter(adapter);
            telco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    telco.showDropDown();
                }
            });
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER CARD NUMBER");
            button.setText("VALIDATE NUMBER");
            reference.setCounterMaxLength(11);
            length = 11;
        }else if(val.equals("AEDC"))
        {
            telco.setVisibility(View.VISIBLE);
            String[] option = {"PREPAID", "POSTPAID"};
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
            telco.setAdapter(adapter);
            telco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    telco.showDropDown();
                }
            });
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER CARD NUMBER");
            button.setText("VALIDATE NUMBER");
            reference.setCounterMaxLength(11);
            length = 11;
        }else if(val.equals("PHEDC"))
        {
            telco.setVisibility(View.VISIBLE);
            String[] option = {"PREPAID", "POSTPAID"};
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
            telco.setAdapter(adapter);
            telco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    telco.showDropDown();
                }
            });
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER CARD NUMBER");
            button.setText("VALIDATE NUMBER");
            reference.setCounterMaxLength(11);
            length = 11;
        }else if(val.equals("KEDCOOFFSITE"))
        {
            telco.setVisibility(View.VISIBLE);
            String[] option = {"PREPAID", "POSTPAID"};
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
            telco.setAdapter(adapter);
            telco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    telco.showDropDown();
                }
            });
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER CARD NUMBER");
            button.setText("VALIDATE NUMBER");
            reference.setCounterMaxLength(11);
            length = 11;
        }else if(val.equals("KEDCOONSITE"))
        {
            telco.setVisibility(View.VISIBLE);
            String[] option = {"PREPAID", "POSTPAID"};
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
            telco.setAdapter(adapter);
            telco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    telco.showDropDown();
                }
            });
            reference.setVisibility(View.VISIBLE);
            reference.setHint("ENTER CARD NUMBER");
            button.setText("VALIDATE NUMBER");
            reference.setCounterMaxLength(11);
            length = 11;
        }
    }


    private void pullList()
    {
        JSONObject obj = new JSONObject();
        try {
            if(autoCompleteTextView.getText().toString().equals("SMILE"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else
                {
                    obj.put("account", reference.getEditText().getText().toString().trim());
                    obj.put("channel", "B2B");
                    obj.put("service", "smile");
                    obj.put("type", "account");
                    Log.i(TAG, "REQUEST: " + obj.toString());
                    button.setEnabled(false);
                    new GetList().execute(BASEURL + "/apis/etop/itex/internet/validation", obj.toString());
                    //Smile own validate
                }
            }else if(autoCompleteTextView.getText().toString().equals("STARTIMES"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Card", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    obj.put("amount", amount.getEditText().getText().toString().trim());
                    obj.put("channel", "B2B");
                    obj.put("service", "startimes");
                    obj.put("smartcardcode", reference.getEditText().getText().toString().trim());
                    obj.put("type", "default");
                    button.setEnabled(false);
                    new GetList().execute(BASEURL + "/apis/etop/itex/startimes/validation", obj.toString());
                    //validate + pull
                }
            }else if(autoCompleteTextView.getText().toString().equals("DSTV"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Card", Toast.LENGTH_LONG).show();
                }else
                {
                    obj.put("channel", "B2B");
                    obj.put("service", "multichoice");
                    obj.put("account", reference.getEditText().getText().toString().trim());
                    obj.put("type", "DSTV");
                    button.setEnabled(false);
                    new GetList().execute(BASEURL + "/apis/etop/itex/multichoice/validation", obj.toString());
                    //validate + pull
                }
            }else if(autoCompleteTextView.getText().toString().equals("GOTV"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Card", Toast.LENGTH_LONG).show();
                }else
                {
                    obj.put("channel", "B2B");
                    obj.put("service", "multichoice");
                    obj.put("account", reference.getEditText().getText().toString().trim());
                    obj.put("type", "GOTV");
                    button.setEnabled(false);
                    new GetList().execute(BASEURL + "/apis/etop/itex/multichoice/validation", obj.toString());
                    //validate + pull
                }
            }else if(autoCompleteTextView.getText().toString().equals("DATA"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Card", Toast.LENGTH_LONG).show();
                }else
                {
                    obj.put("channel", "B2B");
                    obj.put("billsname", telco.getText().toString().toLowerCase());
                    button.setEnabled(false);
                    new GetList().execute(BASEURL + "/apis/etop/itex/data/validation", obj.toString());
                    //pull
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(BillsPayment.this, "GETTING LIST FAILED", Toast.LENGTH_LONG).show();
        }
    }

    String[] billsnames = new String[60];
    String[] billscode = new String[60];
    String[] billsvalue = new String[60];
    public static String billsResponse = "";

    public class GetList extends AsyncTask<String, Void, String> {
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
                                billsnames = new String[60];
                                billscode = new String[60];
                                billsvalue = new String[60];
                                if(autoCompleteTextView.getText().toString().equals("SMILE"))
                                {
                                    JSONObject obj = new JSONObject(server_response);
                                    JSONArray arr = obj.getJSONArray("bundles");
                                    billsResponse = arr.toString();
                                    for(int i = 0; i < arr.length(); i++)
                                    {
                                        JSONObject b2 = arr.getJSONObject(i);
                                        billsnames[i] = b2.getString("name");
                                        billscode[i] = b2.getString("code");
                                        billsvalue[i] = String.valueOf(b2.getInt("price"));
                                    }
                                    fetchbills.setVisibility(View.VISIBLE);
                                    ArrayList<String> lst = new ArrayList<String>(Arrays.asList(billsnames));
                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                            R.layout.option_item, lst);
                                    fetchbills.setAdapter(adapter);
                                    fetchbills.setText("");
                                    validate = 2;
                                    button.setEnabled(true);
                                    button.setText("VALIDATE SMILE ACCOUNT");
                                }else if(autoCompleteTextView.getText().toString().equals("STARTIMES"))
                                {
                                    JSONObject obj = new JSONObject(server_response);
                                    JSONArray arr = obj.getJSONArray("bouquets");
                                    billsResponse = server_response;
                                    for(int i = 0; i < arr.length(); i++)
                                    {
                                        JSONObject b2 = arr.getJSONObject(i);
                                        billsnames[i] = b2.getString("name");
                                        billscode[i] = b2.getJSONObject("cycles").toString();
                                        billsvalue[i] = b2.getJSONObject("cycles").toString();
                                    }
                                    fetchbills.setVisibility(View.VISIBLE);
                                    ArrayList<String> lst = new ArrayList<String>(Arrays.asList(billsnames));
                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                            R.layout.option_item, lst);
                                    fetchbills.setAdapter(adapter);
                                    fetchbills.setText("");
                                    validate = 0;
                                    button.setEnabled(true);
                                    button.setText("PAY STARTIMES ACCOUNT");
                                    //Dont go online again
                                }else if(autoCompleteTextView.getText().toString().equals("DSTV"))
                                {
                                    JSONObject obj = new JSONObject(server_response);
                                    JSONArray arr = obj.getJSONArray("bouquets");
                                    billsResponse = server_response;
                                    for(int i = 0; i < arr.length(); i++)
                                    {
                                        JSONObject b2 = arr.getJSONObject(i);
                                        billsnames[i] = b2.getString("name");
                                        billscode[i] = b2.getString("code");
                                        billsvalue[i] = b2.getString("amount");
                                    }
                                    fetchbills.setVisibility(View.VISIBLE);
                                    ArrayList<String> lst = new ArrayList<String>(Arrays.asList(billsnames));
                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                            R.layout.option_item, lst);
                                    fetchbills.setAdapter(adapter);
                                    fetchbills.setText("");
                                    validate = 0;
                                    button.setEnabled(true);
                                    button.setText("PAY DSTV ACCOUNT");
                                    //Dont go online again
                                }else if(autoCompleteTextView.getText().toString().equals("GOTV"))
                                {
                                    JSONObject obj = new JSONObject(server_response);
                                    JSONArray arr = obj.getJSONArray("bouquets");
                                    billsResponse = server_response;
                                    for(int i = 0; i < arr.length(); i++)
                                    {
                                        JSONObject b2 = arr.getJSONObject(i);
                                        billsnames[i] = b2.getString("name");
                                        billscode[i] = b2.getString("code");
                                        billsvalue[i] = b2.getString("amount");
                                    }
                                    fetchbills.setVisibility(View.VISIBLE);
                                    ArrayList<String> lst = new ArrayList<String>(Arrays.asList(billsnames));
                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                            R.layout.option_item, lst);
                                    fetchbills.setAdapter(adapter);
                                    fetchbills.setText("");
                                    validate = 0;
                                    button.setEnabled(true);
                                    button.setText("PAY GOTV ACCOUNT");
                                    //Dont go online again
                                }else if(autoCompleteTextView.getText().toString().equals("DATA"))
                                {
                                    JSONObject mobj = new JSONObject(server_response);
                                    JSONArray arr = mobj.getJSONArray("data");
                                    billsResponse = server_response;
                                    for(int i = 0; i < arr.length(); i++)
                                    {
                                        JSONObject b2 = arr.getJSONObject(i);
                                        billsnames[i] = b2.getString("description");
                                        billscode[i] = b2.getString("code");
                                        billsvalue[i] = b2.getString("amount");
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
                                    //Dont go online again
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                telco.setVisibility(View.INVISIBLE);
                                fetchbills.setVisibility(View.INVISIBLE);
                                amount.setVisibility(View.INVISIBLE);
                                reference.setVisibility(View.INVISIBLE);
                                button.setVisibility(View.INVISIBLE);
                                Toast.makeText(BillsPayment.this, "FETCHING LIST FAILED", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "NOT SUCCESSFUL: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            telco.setVisibility(View.INVISIBLE);
                            fetchbills.setVisibility(View.INVISIBLE);
                            amount.setVisibility(View.INVISIBLE);
                            reference.setVisibility(View.INVISIBLE);
                            button.setVisibility(View.INVISIBLE);
                            Toast.makeText(BillsPayment.this, "FETCHING LIST FAILED", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                telco.setVisibility(View.INVISIBLE);
                fetchbills.setVisibility(View.INVISIBLE);
                amount.setVisibility(View.INVISIBLE);
                reference.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                Toast.makeText(BillsPayment.this, "FETCHING LIST FAILED", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                telco.setVisibility(View.INVISIBLE);
                fetchbills.setVisibility(View.INVISIBLE);
                amount.setVisibility(View.INVISIBLE);
                reference.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                Toast.makeText(BillsPayment.this, "FETCHING LIST FAILED", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "" + server_response);
        }
    }

    private void validateProcessor()
    {
        JSONObject obj = new JSONObject();
        try {
            if(autoCompleteTextView.getText().toString().equals("SMILE"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else
                {
                    obj.put("account", reference.getEditText().getText().toString().trim());
                    obj.put("channel", "B2B");
                    obj.put("service", "smile");
                    obj.put("type", "account");
                    Log.i(TAG, "REQUEST: " + obj.toString());
                    button.setEnabled(false);
                    new Validation().execute(BASEURL + "/apis/etop/itex/smile/validation", obj.toString());
                }
            }else if(autoCompleteTextView.getText().toString().equals("INTERNET"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else
                {
                    obj.put("account", reference.getEditText().getText().toString().trim());
                    obj.put("channel", "B2B");
                    obj.put("service", "smile");
                    obj.put("type", "account");
                    Log.i(TAG, "REQUEST: " + obj.toString());
                    button.setEnabled(false);
                    new Validation().execute(BASEURL + "/apis/etop/itex/smile/validation", obj.toString());
                }
            }else if(autoCompleteTextView.getText().toString().equals("IKEJA ELECTRIC"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else if(telco.getText().toString().isEmpty())
                {
                    telco.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Select Type", Toast.LENGTH_LONG).show();
                }else
                {
                    obj.put("meterNo", reference.getEditText().getText().toString().trim());
                    obj.put("channel", "B2B");
                    obj.put("service", "ie");
                    obj.put("amount", amount.getEditText().getText().toString().trim());
                    obj.put("accountType", telco.getText().toString().toLowerCase());
                    Log.i(TAG, "REQUEST: " + obj.toString());
                    button.setEnabled(false);
                    new Validation().execute(BASEURL + "/apis/etop/itex/electricity/validation", obj.toString());
                }
            }else if(autoCompleteTextView.getText().toString().equals("IBEDC"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else if(telco.getText().toString().isEmpty())
                {
                    telco.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Select Type", Toast.LENGTH_LONG).show();
                }else
                {
                    obj.put("meterNo", reference.getEditText().getText().toString().trim());
                    obj.put("channel", "B2B");
                    obj.put("service", "ibedc");
                    obj.put("amount", amount.getEditText().getText().toString().trim());
                    obj.put("accountType", telco.getText().toString().toLowerCase());
                    Log.i(TAG, "REQUEST: " + obj.toString());
                    button.setEnabled(false);
                    new Validation().execute(BASEURL + "/apis/etop/itex/electricity/validation", obj.toString());
                }
            }else if(autoCompleteTextView.getText().toString().equals("EKEDC"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else if(telco.getText().toString().isEmpty())
                {
                    telco.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Select Type", Toast.LENGTH_LONG).show();
                }else
                {
                    obj.put("meterNo", reference.getEditText().getText().toString().trim());
                    obj.put("channel", "B2B");
                    obj.put("service", "ekedc");
                    obj.put("amount", amount.getEditText().getText().toString().trim());
                    obj.put("accountType", telco.getText().toString().toLowerCase());
                    Log.i(TAG, "REQUEST: " + obj.toString());
                    button.setEnabled(false);
                    new Validation().execute(BASEURL + "/apis/etop/itex/electricity/validation", obj.toString());
                }
            }else if(autoCompleteTextView.getText().toString().equals("EEDC"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else if(telco.getText().toString().isEmpty())
                {
                    telco.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Select Type", Toast.LENGTH_LONG).show();
                }else
                {
                    obj.put("meterNo", reference.getEditText().getText().toString().trim());
                    obj.put("channel", "B2B");
                    obj.put("service", "eedc");
                    obj.put("amount", amount.getEditText().getText().toString().trim());
                    obj.put("accountType", telco.getText().toString().toLowerCase());
                    Log.i(TAG, "REQUEST: " + obj.toString());
                    button.setEnabled(false);
                    new Validation().execute(BASEURL + "/apis/etop/itex/electricity/validation", obj.toString());
                }
            }else if(autoCompleteTextView.getText().toString().equals("AEDC"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else if(telco.getText().toString().isEmpty())
                {
                    telco.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Select Type", Toast.LENGTH_LONG).show();
                }else
                {
                    obj.put("meterNo", reference.getEditText().getText().toString().trim());
                    obj.put("channel", "B2B");
                    obj.put("service", "aedc");
                    obj.put("amount", amount.getEditText().getText().toString().trim());
                    obj.put("accountType", telco.getText().toString().toLowerCase());
                    Log.i(TAG, "REQUEST: " + obj.toString());
                    button.setEnabled(false);
                    new Validation().execute(BASEURL + "/apis/etop/itex/electricity/validation", obj.toString());
                }
            }else if(autoCompleteTextView.getText().toString().equals("PHEDC"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else if(telco.getText().toString().isEmpty())
                {
                    telco.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Select Type", Toast.LENGTH_LONG).show();
                }else
                {
                    obj.put("meterNo", reference.getEditText().getText().toString().trim());
                    obj.put("channel", "B2B");
                    obj.put("service", "phedc");
                    obj.put("amount", amount.getEditText().getText().toString().trim());
                    obj.put("accountType", telco.getText().toString().toLowerCase());
                    Log.i(TAG, "REQUEST: " + obj.toString());
                    button.setEnabled(false);
                    new Validation().execute(BASEURL + "/apis/etop/itex/electricity/validation", obj.toString());
                }
            }else if(autoCompleteTextView.getText().toString().equals("KEDCOOFFSITE"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else if(telco.getText().toString().isEmpty())
                {
                    telco.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Select Type", Toast.LENGTH_LONG).show();
                }else
                {
                    obj.put("meterNo", reference.getEditText().getText().toString().trim());
                    obj.put("channel", "B2B");
                    obj.put("service", "kedcooffsite");
                    obj.put("amount", amount.getEditText().getText().toString().trim());
                    obj.put("accountType", telco.getText().toString().toLowerCase());
                    Log.i(TAG, "REQUEST: " + obj.toString());
                    button.setEnabled(false);
                    new Validation().execute(BASEURL + "/apis/etop/itex/electricity/validation", obj.toString());
                }
            }else if(autoCompleteTextView.getText().toString().equals("KEDCOONSITE"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else if(telco.getText().toString().isEmpty())
                {
                    telco.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Select Type", Toast.LENGTH_LONG).show();
                }else
                {
                    obj.put("meterNo", reference.getEditText().getText().toString().trim());
                    obj.put("channel", "B2B");
                    obj.put("service", "kedcoonsite");
                    obj.put("amount", amount.getEditText().getText().toString().trim());
                    obj.put("accountType", telco.getText().toString().toLowerCase());
                    Log.i(TAG, "REQUEST: " + obj.toString());
                    button.setEnabled(false);
                    new Validation().execute(BASEURL + "/apis/etop/itex/electricity/validation", obj.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(BillsPayment.this, "VALIDATING FAILED", Toast.LENGTH_LONG).show();
        }
    }

    public class Validation extends AsyncTask<String, Void, String> {
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
                                if(autoCompleteTextView.getText().toString().equals("SMILE"))
                                {
                                    billsResponse = server_response;
                                    validate = 0;
                                    button.setEnabled(true);
                                    button.setText("PAY SMILE");
                                    paymentProcessor();
                                }else if(autoCompleteTextView.getText().toString().equals("INTERNET"))
                                {
                                    billsResponse = server_response;
                                    validate = 0;
                                    button.setEnabled(true);
                                    button.setText("PAY INTERNET");
                                    paymentProcessor();
                                }else if(autoCompleteTextView.getText().toString().equals("IKEJA ELECTRIC"))
                                {
                                    billsResponse = server_response;
                                    validate = 0;
                                    button.setEnabled(true);
                                    button.setText("PAY ELECTRIC");
                                    paymentProcessor();
                                }else if(autoCompleteTextView.getText().toString().equals("IBEDC"))
                                {
                                    billsResponse = server_response;
                                    validate = 0;
                                    button.setEnabled(true);
                                    button.setText("PAY IBEDC");
                                    paymentProcessor();
                                }else if(autoCompleteTextView.getText().toString().equals("EKEDC"))
                                {
                                    billsResponse = server_response;
                                    validate = 0;
                                    button.setEnabled(true);
                                    button.setText("PAY EKEDC");
                                    paymentProcessor();
                                }else if(autoCompleteTextView.getText().toString().equals("EEDC"))
                                {
                                    billsResponse = server_response;
                                    validate = 0;
                                    button.setEnabled(true);
                                    button.setText("PAY EEDC");
                                    paymentProcessor();
                                }else if(autoCompleteTextView.getText().toString().equals("AEDC"))
                                {
                                    billsResponse = server_response;
                                    validate = 0;
                                    button.setEnabled(true);
                                    button.setText("PAY AEDC");
                                    paymentProcessor();
                                }else if(autoCompleteTextView.getText().toString().equals("PHEDC"))
                                {
                                    billsResponse = server_response;
                                    validate = 0;
                                    button.setEnabled(true);
                                    button.setText("PAY PHEDC");
                                    paymentProcessor();
                                }else if(autoCompleteTextView.getText().toString().equals("KEDCOOFFSITE"))
                                {
                                    billsResponse = server_response;
                                    validate = 0;
                                    button.setEnabled(true);
                                    button.setText("PAY KEDCOOFFSITE");
                                    paymentProcessor();
                                }else if(autoCompleteTextView.getText().toString().equals("KEDCOONSITE"))
                                {
                                    billsResponse = server_response;
                                    validate = 0;
                                    button.setEnabled(true);
                                    button.setText("PAY KEDCOONSITE");
                                    paymentProcessor();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                telco.setVisibility(View.INVISIBLE);
                                fetchbills.setVisibility(View.INVISIBLE);
                                amount.setVisibility(View.INVISIBLE);
                                reference.setVisibility(View.INVISIBLE);
                                button.setVisibility(View.INVISIBLE);
                                Toast.makeText(BillsPayment.this, "VALIDATION FAILED", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "NOT SUCCESSFUL: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "KEY EXCHANGE NOT SUCCESSFUL");
                            telco.setVisibility(View.INVISIBLE);
                            fetchbills.setVisibility(View.INVISIBLE);
                            amount.setVisibility(View.INVISIBLE);
                            reference.setVisibility(View.INVISIBLE);
                            button.setVisibility(View.INVISIBLE);
                            Toast.makeText(BillsPayment.this, "VALIDATION FAILED", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                telco.setVisibility(View.INVISIBLE);
                fetchbills.setVisibility(View.INVISIBLE);
                amount.setVisibility(View.INVISIBLE);
                reference.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                Toast.makeText(BillsPayment.this, "VALIDATION FAILED", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                telco.setVisibility(View.INVISIBLE);
                fetchbills.setVisibility(View.INVISIBLE);
                amount.setVisibility(View.INVISIBLE);
                reference.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                Toast.makeText(BillsPayment.this, "VALIDATION FAILED", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "" + server_response);
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
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    String form = "PHONE NUMBER: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "PROVIDER: \t" +  "MTN" + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("SMILE"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    JSONObject obj = new JSONObject(billsResponse);
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "NAME: \t" +  obj.getString("customerName") + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "PROVIDER: \t" +  "SMILE" + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("STARTIMES"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    JSONObject obj = new JSONObject(billsResponse);
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "NAME: \t" +  obj.getString("name") + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "PROVIDER: \t" +  "STARTIMES" + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("DSTV"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    JSONObject obj = new JSONObject(billsResponse);
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "NAME: \t" +  obj.getString("name") + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "PROVIDER: \t" +  "DSTV" + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("GOTV"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    JSONObject obj = new JSONObject(billsResponse);
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "NAME: \t" +  obj.getString("name") + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "PROVIDER: \t" +  "GOTV" + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("DATA"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    JSONObject obj = new JSONObject(billsResponse);
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "NAME: \t" +  fetchbills.getText().toString() + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "PROVIDER: \t" +  telco.getText().toString().toUpperCase() + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("INTERNET"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    JSONObject obj = new JSONObject(billsResponse);
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "NAME: \t" +  obj.getString("customerName") + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "PROVIDER: \t" +  "SMILE" + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("IKEJA ELECTRIC"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    JSONObject obj = new JSONObject(billsResponse);
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "NAME: \t" +  obj.getString("name") + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "ID: \t" +  obj.getString("customerId") + "\n" +
                            "METER NUMBER: \t" +  obj.getString("meterNumber") + "\n" +
                            "ADDRESS: \t" +  obj.getString("address") + "\n" +
                            "PHONE: \t" +  obj.getString("phone") + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("IBEDC"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    JSONObject obj = new JSONObject(billsResponse);
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "NAME: \t" +  obj.getString("name") + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "ID: \t" +  obj.getString("customerId") + "\n" +
                            "METER NUMBER: \t" +  obj.getString("meterNumber") + "\n" +
                            "ADDRESS: \t" +  obj.getString("address") + "\n" +
                            "PHONE: \t" +  obj.getString("phone") + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("EKEDC"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    JSONObject obj = new JSONObject(billsResponse);
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "NAME: \t" +  obj.getString("name") + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "ID: \t" +  obj.getString("customerId") + "\n" +
                            "METER NUMBER: \t" +  obj.getString("meterNumber") + "\n" +
                            "ADDRESS: \t" +  obj.getString("address") + "\n" +
                            "PHONE: \t" +  obj.getString("phone") + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("EEDC"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    JSONObject obj = new JSONObject(billsResponse);
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "NAME: \t" +  obj.getString("name") + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "ID: \t" +  obj.getString("customerId") + "\n" +
                            "METER NUMBER: \t" +  obj.getString("meterNumber") + "\n" +
                            "ADDRESS: \t" +  obj.getString("address") + "\n" +
                            "PHONE: \t" +  obj.getString("phone") + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("AEDC"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    JSONObject obj = new JSONObject(billsResponse);
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "NAME: \t" +  obj.getString("name") + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "ID: \t" +  obj.getString("customerId") + "\n" +
                            "METER NUMBER: \t" +  obj.getString("meterNumber") + "\n" +
                            "ADDRESS: \t" +  obj.getString("address") + "\n" +
                            "PHONE: \t" +  obj.getString("phone") + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("PHEDC"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    JSONObject obj = new JSONObject(billsResponse);
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "NAME: \t" +  obj.getString("name") + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "ID: \t" +  obj.getString("customerId") + "\n" +
                            "METER NUMBER: \t" +  obj.getString("meterNumber") + "\n" +
                            "ADDRESS: \t" +  obj.getString("address") + "\n" +
                            "PHONE: \t" +  obj.getString("phone") + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("KEDCOOFFSITE"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    JSONObject obj = new JSONObject(billsResponse);
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "NAME: \t" +  obj.getString("name") + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "ID: \t" +  obj.getString("customerId") + "\n" +
                            "METER NUMBER: \t" +  obj.getString("meterNumber") + "\n" +
                            "ADDRESS: \t" +  obj.getString("address") + "\n" +
                            "PHONE: \t" +  obj.getString("phone") + "\n";
                    ConfirmPay(form);
                }
            }else if(autoCompleteTextView.getText().toString().equals("KEDCOONSITE"))
            {
                if(reference.getEditText().getText().toString().trim().isEmpty())
                {
                    reference.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Account", Toast.LENGTH_LONG).show();
                }else if(amount.getEditText().getText().toString().trim().isEmpty())
                {
                    amount.setFocusable(true);
                    Toast.makeText(BillsPayment.this, "Please Provide Amount", Toast.LENGTH_LONG).show();
                }else
                {
                    JSONObject obj = new JSONObject(billsResponse);
                    String form = "ACCOUNT: \t" +  reference.getEditText().getText().toString().trim() + "\n" +
                            "NAME: \t" +  obj.getString("name") + "\n" +
                            "AMOUNT: \t" +  amount.getEditText().getText().toString().trim() + "\n" +
                            "ID: \t" +  obj.getString("customerId") + "\n" +
                            "METER NUMBER: \t" +  obj.getString("meterNumber") + "\n" +
                            "ADDRESS: \t" +  obj.getString("address") + "\n" +
                            "PHONE: \t" +  obj.getString("phone") + "\n";
                    ConfirmPay(form);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(BillsPayment.this, "PAYMENT FAILED", Toast.LENGTH_LONG).show();
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
                    if(autoCompleteTextView.getText().toString().equals("AIRTEL"))
                    {
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "airtelvtu");
                        obj.put("channel", "B2B");
                        obj.put("clientReference", uuid);
                        obj.put("paymentmethod", "cash");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("pin", "2424");
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "airtelvtu");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/vtu/purchase", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("9MOBILE"))
                    {
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "9mobilevtu");
                        obj.put("channel", "B2B");
                        obj.put("clientReference", uuid);
                        obj.put("paymentmethod", "cash");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("pin", "2424");
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "9mobilevtu");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/vtu/purchase", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("GLO"))
                    {
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "glovtu");
                        obj.put("channel", "B2B");
                        obj.put("clientReference", uuid);
                        obj.put("paymentmethod", "cash");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("pin", "2424");
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "glovtu");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/vtu/purchase", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("MTN"))
                    {
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "mtnvtu");
                        obj.put("channel", "B2B");
                        obj.put("clientReference", uuid);
                        obj.put("paymentmethod", "cash");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("pin", "2424");
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "mtnvtu");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/vtu/purchase", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("SMILE"))
                    {
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "smile");
                        obj.put("clientReference", uuid);
                        String val = fetchbills.getText().toString();
                        int lv = Arrays.asList(billsnames).indexOf(val);
                        String code = billscode[lv];
                        obj.put("code", code);
                        obj.put("paymentmethod", "cash");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("pin", "2424");
                        JSONObject vf = new JSONObject(billsResponse);
                        obj.put("paymentcode", vf.getString("productCode"));
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "smile");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        obj.put("type", "subscription");
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/internet/payment", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("STARTIMES"))
                    {
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        String val = fetchbills.getText().toString();
                        int lv = Arrays.asList(billsnames).indexOf(val);
                        String code = billscode[lv];
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "STARTIMES");
                        obj.put("bouquet", val);
                        obj.put("clientReference", uuid);
                        obj.put("cycle", "daily");
                        obj.put("paymentmethod", "cash");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("pin", "2424");
                        JSONObject vf = new JSONObject(billsResponse);
                        obj.put("paymentcode", vf.getString("productCode"));
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "STARTIMES");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/internet/payment", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("DSTV"))
                    {
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        String val = fetchbills.getText().toString();
                        int lv = Arrays.asList(billsnames).indexOf(val);
                        String code = billscode[lv];
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "DSTV");
                        obj.put("bouquet", val);
                        obj.put("clientReference", uuid);
                        obj.put("cycle", "monthly");
                        obj.put("paymentmethod", "cash");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("pin", "2424");
                        JSONObject vf = new JSONObject(billsResponse);
                        obj.put("paymentcode", vf.getString("productCode"));
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "DSTV");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/internet/payment", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("GOTV"))
                    {
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        String val = fetchbills.getText().toString();
                        int lv = Arrays.asList(billsnames).indexOf(val);
                        String code = billscode[lv];
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "GOTV");
                        obj.put("bouquet", val);
                        obj.put("clientReference", uuid);
                        obj.put("cycle", "monthly");
                        obj.put("paymentmethod", "cash");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("pin", "2424");
                        JSONObject vf = new JSONObject(billsResponse);
                        obj.put("paymentcode", vf.getString("productCode"));
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "GOTV");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/internet/payment", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("DATA"))
                    {
                        String val = fetchbills.getText().toString();
                        int lv = Arrays.asList(billsnames).indexOf(val);
                        String code = billscode[lv];
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", telco.getText().toString().toLowerCase());
                        obj.put("clientReference", uuid);
                        obj.put("code", code);
                        obj.put("paymentmethod", "cash");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("pin", "2424");
                        JSONObject mk = new JSONObject(billsResponse);
                        obj.put("productCode", mk.getString("productCode"));
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", telco.getText().toString().toLowerCase());
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/data/purchase", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("INTERNET"))
                    {
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "smile");
                        obj.put("clientReference", uuid);
                        String val = fetchbills.getText().toString();
                        int lv = Arrays.asList(billsnames).indexOf(val);
                        String code = billscode[lv];
                        obj.put("code", code);
                        obj.put("paymentmethod", "cash");
                        obj.put("phone", reference.getEditText().getText().toString().trim());
                        obj.put("pin", "2424");
                        JSONObject vf = new JSONObject(billsResponse);
                        obj.put("paymentcode", vf.getString("productCode"));
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "smile");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        obj.put("type", "subscription");
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/internet/payment", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("IKEJA ELECTRIC"))
                    {
                        JSONObject vf = new JSONObject(billsResponse);
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "ie");
                        obj.put("clientReference", uuid);
                        obj.put("customerphone", reference.getEditText().getText().toString().trim());
                        obj.put("paymentcode", vf.getString("productCode"));
                        obj.put("paymentmethod", "cash");
                        obj.put("pin", "2424");
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "ie");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/electricity/payment", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("IBEDC"))
                    {
                        JSONObject vf = new JSONObject(billsResponse);
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "ibedc");
                        obj.put("clientReference", uuid);
                        obj.put("customerphone", reference.getEditText().getText().toString().trim());
                        obj.put("paymentcode", vf.getString("productCode"));
                        obj.put("paymentmethod", "cash");
                        obj.put("pin", "2424");
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "ibedc");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/electricity/payment", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("EKEDC"))
                    {
                        JSONObject vf = new JSONObject(billsResponse);
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "ekedc");
                        obj.put("clientReference", uuid);
                        obj.put("customerphone", reference.getEditText().getText().toString().trim());
                        obj.put("paymentcode", vf.getString("productCode"));
                        obj.put("paymentmethod", "cash");
                        obj.put("pin", "2424");
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "ekedc");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/electricity/payment", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("EEDC"))
                    {
                        JSONObject vf = new JSONObject(billsResponse);
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "eedc");
                        obj.put("clientReference", uuid);
                        obj.put("customerphone", reference.getEditText().getText().toString().trim());
                        obj.put("paymentcode", vf.getString("productCode"));
                        obj.put("paymentmethod", "cash");
                        obj.put("pin", "2424");
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "eedc");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/electricity/payment", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("AEDC"))
                    {
                        JSONObject vf = new JSONObject(billsResponse);
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "aedc");
                        obj.put("clientReference", uuid);
                        obj.put("customerphone", reference.getEditText().getText().toString().trim());
                        obj.put("paymentcode", vf.getString("productCode"));
                        obj.put("paymentmethod", "cash");
                        obj.put("pin", "2424");
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "aedc");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/electricity/payment", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("PHEDC"))
                    {
                        JSONObject vf = new JSONObject(billsResponse);
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "phedc");
                        obj.put("clientReference", uuid);
                        obj.put("customerphone", reference.getEditText().getText().toString().trim());
                        obj.put("paymentcode", vf.getString("productCode"));
                        obj.put("paymentmethod", "cash");
                        obj.put("pin", "2424");
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "phedc");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/electricity/payment", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("KEDCOOFFSITE"))
                    {
                        JSONObject vf = new JSONObject(billsResponse);
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "kedcooffsite");
                        obj.put("clientReference", uuid);
                        obj.put("customerphone", reference.getEditText().getText().toString().trim());
                        obj.put("paymentcode", vf.getString("productCode"));
                        obj.put("paymentmethod", "cash");
                        obj.put("pin", "2424");
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "kedcooffsite");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/electricity/payment", obj.toString());
                    }else if(autoCompleteTextView.getText().toString().equals("KEDCOONSITE"))
                    {
                        JSONObject vf = new JSONObject(billsResponse);
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        obj.put("amount", amount.getEditText().getText().toString().trim());
                        obj.put("billsname", "kedcoonsite");
                        obj.put("clientReference", uuid);
                        obj.put("customerphone", reference.getEditText().getText().toString().trim());
                        obj.put("paymentcode", vf.getString("productCode"));
                        obj.put("paymentmethod", "cash");
                        obj.put("pin", "2424");
                        obj.put("serialnumber", Utilities.getSerialNumber());
                        obj.put("service", "kedcoonsite");
                        obj.put("tid", ProfileParser.tid);
                        obj.put("transferpin", pin);
                        Log.i(TAG, "REQUEST: " + obj.toString());
                        button.setEnabled(false);
                        new Pay().execute(BASEURL + "/apis/etop/itex/electricity/payment", obj.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(BillsPayment.this, "PAYMENT FAILED", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "USER CANCELED...", Toast.LENGTH_SHORT).show();
                dialog.cancel();
                Intent intent = new Intent();
                intent.setClass(BillsPayment.this, Dashboard.class);
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
                                    Toast.makeText(BillsPayment.this, obj.getString("errormessage"), Toast.LENGTH_LONG).show();

                                    ProfileParser.sending = new String[128];
                                    ProfileParser.receiving = new String[128];
                                    ProfileParser.sending[62] = server_response;
                                    ProfileParser.totalAmount = amount.getEditText().getText().toString().trim();
                                    ProfileParser.Amount = amount.getEditText().getText().toString().trim();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String pre = dateFormat.format(new Date());
                                    String rrn = pre.substring(2);
                                    ProfileParser.sending[37] = uuid;
                                    ProfileParser.receiving[39] = "05";
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

                                    DATABASEHANDLER db = new DATABASEHANDLER(BillsPayment.this);
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
                                    intent.setClass(BillsPayment.this, TransactionDetails.class);
                                    startActivity(intent);
                                    finish();
                                }else if(obj.has("error") && obj.getBoolean("error") == false)
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
                                    ProfileParser.receiving[39] = "00";
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

                                    DATABASEHANDLER db = new DATABASEHANDLER(BillsPayment.this);
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
                                    intent.setClass(BillsPayment.this, TransactionDetails.class);
                                    startActivity(intent);
                                    finish();
                                }else
                                {
                                    Toast.makeText(BillsPayment.this, "TRANSACTION FAILED", Toast.LENGTH_LONG).show();
                                    ProfileParser.sending = new String[128];
                                    ProfileParser.receiving = new String[128];
                                    ProfileParser.sending[62] = server_response;
                                    ProfileParser.totalAmount = amount.getEditText().getText().toString().trim();
                                    ProfileParser.Amount = amount.getEditText().getText().toString().trim();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String pre = dateFormat.format(new Date());
                                    String rrn = pre.substring(2);
                                    ProfileParser.sending[37] = uuid;
                                    ProfileParser.receiving[39] = "05";
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

                                    DATABASEHANDLER db = new DATABASEHANDLER(BillsPayment.this);
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
                                    intent.setClass(BillsPayment.this, TransactionDetails.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(BillsPayment.this, "TRANSACTION FAILED", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                intent.setClass(BillsPayment.this, Dashboard.class);
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

                            Toast.makeText(BillsPayment.this, "TRANSACTION FAILED", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.setClass(BillsPayment.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(BillsPayment.this, "TRANSACTION FAILED", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(BillsPayment.this, Dashboard.class);
                startActivity(intent);
                finish();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(BillsPayment.this, "TRANSACTION FAILED", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(BillsPayment.this, Dashboard.class);
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
        intent.setClass(BillsPayment.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}