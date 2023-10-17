package com.gbikna.sample.etop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.card.print.PrintData;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class EodSummary extends AppCompatActivity {
    private static final String TAG = EodSummary.class.getSimpleName();

    TextView purhasecount, purchaseamount, purchaseagent, purchasefee, purchase9rapoint;
    TextView cashoutcount, cashoutamount, cashoutagent, cashoutfee, cashout9rapoint;
    TextView transfercount, transferamount, transferagent, transferfee, transfer9rapoint;
    TextView billscount, billsamount, billsagent, billsfee, bills9rapoint;
    TextInputLayout daterange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eod_summary);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EodSummary.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker()
                .setTheme(R.style.ThemeOverlay_App_MaterialCalendar);
        builder.setTitleText("Select Date Range");
        long today = MaterialDatePicker.todayInUtcMilliseconds();


        CalendarConstraints.Builder con = new CalendarConstraints.Builder();

        CalendarConstraints.DateValidator dateValidator = DateValidatorPointBackward.now();

        con.setValidator(dateValidator);

        con.setEnd(today);

        builder.setCalendarConstraints(con.build());
        final MaterialDatePicker materialDatePicker = builder.build();


        materialDatePicker.addOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                daterange.setEnabled(true);
            }
        });

        materialDatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daterange.setEnabled(true);
            }
        });

        daterange = (TextInputLayout) findViewById(R.id.daterange);
        daterange.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String dt = daterange.getEditText().getText().toString();
                Log.i("DATE RANGE", "AFTERTEXTCHANGED: " + dt);

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String dt = daterange.getEditText().getText().toString();
                Log.i("DATE RANGE", "BEFORETEXTCHANGED: " + dt);
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String dt = daterange.getEditText().getText().toString();
                Log.i("DATE RANGE", "ONTEXTCHANGED: " + dt);
            }
        });


        daterange.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("PERSONALINFORMATION", "DATE OF BIRTH ENTERED");
                materialDatePicker.show(getSupportFragmentManager(), "DATE PICKER");
            }
        });

        daterange.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Log.i("PERSONALINFORMATION", " 2....DATE OF BIRTH ENTERED");
                    materialDatePicker.show(getSupportFragmentManager(), "DATE PICKER");
                } else {
                    // Hide your calender here
                    Log.i("PERSONALINFORMATION", " 2....DATE OF BIRTH ENTERED NOTE");
                }
            }
        });


        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long,Long> selection) {
                daterange.setEnabled(true);
                daterange.getEditText().setText(formatter.format(selection.first) + " TO " + formatter.format(selection.second));
                JSONObject send = new JSONObject();
                try {
                    purhasecount.setText("0");
                    cashoutcount.setText("0");
                    transfercount.setText("0");
                    billscount.setText("0");

                    purchaseamount.setText("NGN 0.00");
                    purchaseagent.setText("NGN 0.00");
                    purchasefee.setText("NGN 0.00");
                    purchase9rapoint.setText("NGN 0.00");

                    cashoutamount.setText("NGN 0.00");
                    cashoutagent.setText("NGN 0.00");
                    cashoutfee.setText("NGN 0.00");
                    cashout9rapoint.setText("NGN 0.00");

                    transferamount.setText("NGN 0.00");
                    transferagent.setText("NGN 0.00");
                    transferfee.setText("NGN 0.00");
                    transfer9rapoint.setText("NGN 0.00");

                    billsamount.setText("NGN 0.00");
                    billsagent.setText("NGN 0.00");
                    billsfee.setText("NGN 0.00");
                    bills9rapoint.setText("NGN 0.00");

                    send.put("enddate", formatter.format(selection.second));
                    send.put("startdate", formatter.format(selection.first));
                    Log.i(TAG, "REQUEST: " + send.toString());
                    new PullData().execute(ProfileParser.BASEURL + "/apis/etop/records/get/transactions", send.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        purhasecount = (TextView)findViewById(R.id.purhasecount);
        purchaseamount = (TextView)findViewById(R.id.purchaseamount);
        purchaseagent = (TextView)findViewById(R.id.purchaseagent);
        purchasefee = (TextView)findViewById(R.id.purchasefee);
        purchase9rapoint = (TextView)findViewById(R.id.purchase9rapoint);

        cashoutcount = (TextView)findViewById(R.id.cashoutcount);
        cashoutamount = (TextView)findViewById(R.id.cashoutamount);
        cashoutagent = (TextView)findViewById(R.id.cashoutagent);
        cashoutfee = (TextView)findViewById(R.id.cashoutfee);
        cashout9rapoint = (TextView)findViewById(R.id.cashout9rapoint);

        transfercount = (TextView)findViewById(R.id.transfercount);
        transferamount = (TextView)findViewById(R.id.transferamount);
        transferagent = (TextView)findViewById(R.id.transferagent);
        transferfee = (TextView)findViewById(R.id.transferfee);
        transfer9rapoint = (TextView)findViewById(R.id.transfer9rapoint);

        billscount = (TextView)findViewById(R.id.billscount);
        billsamount = (TextView)findViewById(R.id.billsamount);
        billsagent = (TextView)findViewById(R.id.billsagent);
        billsfee = (TextView)findViewById(R.id.billsfee);
        bills9rapoint = (TextView)findViewById(R.id.bills9rapoint);


    }

    public class PullData extends AsyncTask<String, Void, String> {
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
                            Log.i(TAG, "RECIEVE: " + server_response);
                            Log.i(TAG, "RECIEVE: " + server_response);

                            int ppurchaseCount = 0, ccashoutCount = 0, ttransferCount = 0, bbillsCount = 0;
                            String ppurchaseAmount = "0.00", ccashoutAmount = "0.00", ttransferAmount = "0.00", bbillsAmount = "0.00";
                            String ppurchaseAgent = "0.00", ccashoutAgent = "0.00", ttransferAgent = "0.00", bbillsAgent = "0.00";
                            String ppurchaseFee = "0.00", ccashoutFee = "0.00", ttransferFee = "0.00", bbillsFee = "0.00";
                            String ppurchase9rapoint = "0.00", ccashout9rapoint = "0.00", ttransfer9rapoint = "0.00", bbills9rapoint = "0.00";

                            try {
                                JSONArray arr = new JSONArray(server_response);
                                for (int i = 0; i < arr.length(); i++)
                                {
                                    if(!arr.getJSONObject(i).getString("status").equals("00"))
                                        continue;

                                    if(arr.getJSONObject(i).getString("transname").equals("PURCHASE"))
                                    {
                                        ppurchaseCount = ppurchaseCount + 1;
                                        purhasecount.setText(String.valueOf(ppurchaseCount));

                                        DecimalFormat f = new DecimalFormat("##.00");
                                        Double amt = Double.parseDouble(arr.getJSONObject(i).getString("amount"));
                                        Double fee = Double.parseDouble(ppurchaseAmount);
                                        Double tt = amt + fee;
                                        ppurchaseAmount = f.format(tt);
                                        purchaseamount.setText("₦ " + ppurchaseAmount);

                                        f = new DecimalFormat("##.00");
                                        amt = Double.parseDouble(arr.getJSONObject(i).getString("amount"));
                                        fee = Double.parseDouble(ppurchaseAgent);
                                        tt = amt + fee;
                                        ppurchaseAgent = f.format(tt);
                                        purchaseagent.setText("₦ " + ppurchaseAgent);

                                        f = new DecimalFormat("##.00");
                                        amt = Double.parseDouble(arr.getJSONObject(i).getString("fee"));
                                        fee = Double.parseDouble(ppurchaseFee);
                                        tt = amt + fee;
                                        ppurchaseFee = f.format(tt);
                                        purchasefee.setText("₦ " + ppurchaseFee);

                                        f = new DecimalFormat("##.00");
                                        amt = Double.parseDouble(arr.getJSONObject(i).getString("tmsamount"));
                                        fee = Double.parseDouble(ppurchase9rapoint);
                                        tt = amt + fee;
                                        ppurchase9rapoint = f.format(tt);
                                        purchase9rapoint.setText("₦ " + ppurchase9rapoint);
                                    }else if(arr.getJSONObject(i).getString("transname").equals("CASH OUT"))
                                    {
                                        ccashoutCount = ccashoutCount + 1;
                                        cashoutcount.setText(String.valueOf(ccashoutCount));

                                        DecimalFormat f = new DecimalFormat("##.00");
                                        Double amt = Double.parseDouble(arr.getJSONObject(i).getString("amount"));
                                        Double fee = Double.parseDouble(ccashoutAmount);
                                        Double tt = amt + fee;
                                        ccashoutAmount = f.format(tt);
                                        cashoutamount.setText("₦ " + ccashoutAmount);

                                        f = new DecimalFormat("##.00");
                                        amt = Double.parseDouble(arr.getJSONObject(i).getString("agentamount"));
                                        fee = Double.parseDouble(ccashoutAgent);
                                        tt = amt + fee;
                                        ccashoutAgent = f.format(tt);
                                        cashoutagent.setText("₦ " + ccashoutAgent);

                                        f = new DecimalFormat("##.00");
                                        amt = Double.parseDouble(arr.getJSONObject(i).getString("fee"));
                                        fee = Double.parseDouble(ccashoutFee);
                                        tt = amt + fee;
                                        ccashoutFee = f.format(tt);
                                        cashoutfee.setText("₦ " + "0.00");

                                        f = new DecimalFormat("##.00");
                                        amt = Double.parseDouble(arr.getJSONObject(i).getString("tmsamount"));
                                        fee = Double.parseDouble(ccashout9rapoint);
                                        tt = amt + fee;
                                        ccashout9rapoint = f.format(tt);
                                        cashout9rapoint.setText("₦ " + ccashoutFee);//ccashout9rapoint
                                    }else if(arr.getJSONObject(i).getString("transname").equals("TRANSFER"))
                                    {
                                        ttransferCount = ttransferCount + 1;
                                        transfercount.setText(String.valueOf(ttransferCount));

                                        DecimalFormat f = new DecimalFormat("##.00");
                                        Double amt = Double.parseDouble(arr.getJSONObject(i).getString("amount"));
                                        Double fee = Double.parseDouble(ttransferAmount);
                                        Double tt = amt + fee;
                                        ttransferAmount = f.format(tt);
                                        transferamount.setText("₦ " + ttransferAmount);

                                        f = new DecimalFormat("##.00");
                                        amt = Double.parseDouble(arr.getJSONObject(i).getString("agentamount"));
                                        fee = Double.parseDouble(ttransferAgent);
                                        tt = amt + fee;
                                        ttransferAgent = f.format(tt);
                                        transferagent.setText("₦ " + ttransferAgent);

                                        f = new DecimalFormat("##.00");
                                        amt = Double.parseDouble(arr.getJSONObject(i).getString("fee"));
                                        fee = Double.parseDouble(ttransferFee);
                                        tt = amt + fee;
                                        ttransferFee = f.format(tt);
                                        transferfee.setText("₦ " + "0.00");

                                        f = new DecimalFormat("##.00");
                                        amt = Double.parseDouble(arr.getJSONObject(i).getString("tmsamount"));
                                        fee = Double.parseDouble(ttransfer9rapoint);
                                        tt = amt + fee;
                                        ttransfer9rapoint = f.format(tt);
                                        transfer9rapoint.setText("₦ " + ttransferFee);//ttransfer9rapoint
                                    }else
                                    {
                                        if(!arr.getJSONObject(i).getString("transname").equals("ACCOUNT TOPUP"))
                                        {
                                            bbillsCount = bbillsCount + 1;
                                            billscount.setText(String.valueOf(bbillsCount));

                                            DecimalFormat f = new DecimalFormat("##.00");
                                            Double amt = Double.parseDouble(arr.getJSONObject(i).getString("amount"));
                                            Double fee = Double.parseDouble(bbillsAmount);
                                            Double tt = amt + fee;
                                            bbillsAmount = f.format(tt);
                                            billsamount.setText("₦ " + bbillsAmount);

                                            f = new DecimalFormat("##.00");
                                            amt = Double.parseDouble(arr.getJSONObject(i).getString("agentamount"));
                                            fee = Double.parseDouble(bbillsAgent);
                                            tt = amt + fee;
                                            bbillsAgent = f.format(tt);
                                            billsagent.setText("₦ " + bbillsAgent);

                                            f = new DecimalFormat("##.00");
                                            amt = Double.parseDouble(arr.getJSONObject(i).getString("fee"));
                                            fee = Double.parseDouble(bbillsFee);
                                            tt = amt + fee;
                                            bbillsFee = f.format(tt);
                                            billsfee.setText("₦ " + "0.00");

                                            f = new DecimalFormat("##.00");
                                            amt = Double.parseDouble(arr.getJSONObject(i).getString("tmsamount"));
                                            fee = Double.parseDouble(bbills9rapoint);
                                            tt = amt + fee;
                                            bbills9rapoint = f.format(tt);
                                            bills9rapoint.setText("₦ " + bbillsFee);//bbills9rapoint
                                        }
                                    }
                                }
                                PrintData mPrint = new PrintData(EodSummary.this);
                                mPrint.PrintEodSummary(daterange.getEditText().getText().toString().trim(),
                                        purhasecount.getText().toString(), purchaseamount.getText().toString(),
                                        purchaseagent.getText().toString(),
                                        purchasefee.getText().toString(), purchase9rapoint.getText().toString(),
                                        cashoutcount.getText().toString(), cashoutamount.getText().toString(),
                                        cashoutagent.getText().toString(),
                                        cashoutfee.getText().toString(), cashout9rapoint.getText().toString(),
                                        transfercount.getText().toString(), transferamount.getText().toString(),
                                        transferagent.getText().toString(),
                                        transferfee.getText().toString(), transfer9rapoint.getText().toString(),
                                        billscount.getText().toString(), billsamount.getText().toString(),
                                        billsagent.getText().toString(),
                                        billsfee.getText().toString(), bills9rapoint.getText().toString());
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
        intent.setClass(EodSummary.this, More.class);
        startActivity(intent);
        finish();
    }
}