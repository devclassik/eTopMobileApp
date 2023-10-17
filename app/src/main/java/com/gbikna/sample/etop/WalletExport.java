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
import android.widget.Button;
import android.widget.Toast;

import com.gbikna.sample.R;
import com.gbikna.sample.etop.util.SignupVr;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class WalletExport extends AppCompatActivity {

    private static final String TAG = WalletExport.class.getSimpleName();
    TextInputLayout daterange;
    Button botton;
    public String sd = "";
    public String ed = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_export);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WalletExport.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        daterange = (TextInputLayout) findViewById(R.id.daterange);
        botton = (Button) findViewById(R.id.pull);
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


        botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(daterange.getEditText().getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "SELECT DATE RANGE", Toast.LENGTH_LONG);
                }else
                {
                    botton.setEnabled(false);
                    JSONObject send = new JSONObject();
                    try {
                        send.put("sd", sd);
                        send.put("ed", ed);
                        send.put("username", SignupVr.username);
                        Log.i(TAG, "REQUEST: " + send.toString());
                        new PullData().execute("http://143.42.102.14:8001/send/reports", send.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long,Long> selection) {
                daterange.setEnabled(true);
                daterange.getEditText().setText(formatter.format(selection.first) + " TO " + formatter.format(selection.second));
                sd = formatter.format(selection.first);
                ed = formatter.format(selection.second);
            }
        });
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
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "RECIEVE: " + server_response);
                            Log.i(TAG, "RECIEVE: " + server_response);

                            try {
                                JSONObject obj = new JSONObject(server_response);
                                if(obj.has("status") && obj.getInt("status") == 200)
                                {
                                    botton.setEnabled(true);
                                    botton.setText("EMAIL SENT");
                                }else
                                {
                                    botton.setEnabled(true);
                                    botton.setText("PLEASE RETRY LATER");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                botton.setEnabled(true);
                                botton.setText("PLEASE RETRY LATER");
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "NOT SUCCESSFUL: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            botton.setEnabled(true);
                            botton.setText("PLEASE RETRY LATER");
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
        intent.setClass(WalletExport.this, More.class);
        startActivity(intent);
        finish();
    }
}