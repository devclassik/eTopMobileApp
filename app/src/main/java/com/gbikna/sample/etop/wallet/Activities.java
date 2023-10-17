package com.gbikna.sample.etop.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.etop.Dashboard;
import com.gbikna.sample.etop.More;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;


public class Activities extends AppCompatActivity {
    private static final String TAG = Activities.class.getSimpleName();
    private Calendar calendar;
    ExpandableListView expandableListView;
    Button date_picker;
    TextView error_text;
    LinearLayout barrier;
    ProgressBar loader;
    ActivitiesViewModel activitiesViewModel;
    ActivitiesResponse activityResponse;
    ArrayList<String> listGroup = new ArrayList<>();
    HashMap<String, ArrayList<String>> details = new HashMap<>();
    ActivityAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        expandableListView  = (ExpandableListView) findViewById(R.id.activity_list);
        date_picker = (Button) findViewById(R.id.date_picker);
        error_text = (TextView) findViewById(R.id.error_text);
        loader = (ProgressBar) findViewById(R.id.loader);
        barrier = (LinearLayout) findViewById(R.id.barrier);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Typeface RMed = Typeface.createFromAsset(getAssets(), "fonts/Roboto/Roboto-Medium.ttf");

        calendar = Calendar.getInstance();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        activitiesViewModel = new ViewModelProvider(this).get(ActivitiesViewModel.class);

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker()
                .setTheme(R.style.ThemeOverlay_App_MaterialCalendar);
        builder.setTitleText("Select Date Range");
        long today = MaterialDatePicker.todayInUtcMilliseconds();

        CalendarConstraints.Builder con = new CalendarConstraints.Builder();
        CalendarConstraints.DateValidator dateValidator = DateValidatorPointBackward.now();
        con.setValidator(dateValidator);
        con.setEnd(today);
        //builder.setSelection(today);

        builder.setCalendarConstraints(con.build());
        //builder.setTheme(R.style.MaterialCalendarTheme);
        final MaterialDatePicker materialDatePicker = builder.build();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle data = new Bundle();
                intent.setClass(Activities.this, Dashboard.class);
                intent.putExtras(data);
                startActivity(intent);
                finish();
            }
        });

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_picker.setEnabled(false);
                materialDatePicker.show(getSupportFragmentManager(), "DATE PICKER");
            }
        });

        materialDatePicker.addOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                date_picker.setEnabled(true);
            }
        });

        materialDatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_picker.setEnabled(true);
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long,Long> selection) {
                date_picker.setEnabled(true);

                JSONObject send = new JSONObject();
                try {
                    send.put("enddate", formatter.format(selection.second));
                    send.put("startdate", formatter.format(selection.first));
                    Log.i(TAG, "REQUEST: " + send.toString());
                    new PullData().execute(BASEURL + "/apis/etop/records/wallet/impacts", send.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        error_text.setText("No Activity for Today");
        expandableListView.setEmptyView(error_text);

        loader.setVisibility(View.VISIBLE);
        error_text.setVisibility(View.INVISIBLE);
        expandableListView.setVisibility(View.INVISIBLE);
        barrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        JSONObject send = new JSONObject();
        try {
            send.put("enddate", formatter.format(calendar.getTime()));
            send.put("startdate", formatter.format(calendar.getTime()));
            Log.i(TAG, "REQUEST: " + send.toString());
            new PullData().execute(BASEURL + "/apis/etop/records/wallet/impacts", send.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                //int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "RECIEVE: " + server_response);
                            expandableListView.setVisibility(View.VISIBLE);
                            loader.setVisibility(View.INVISIBLE);
                            error_text.setVisibility(View.INVISIBLE);
                            try {
                                JSONArray arr = new JSONArray(server_response);
                                listGroup = new ArrayList<>();
                                for (int i = 0; i < arr.length(); i++)
                                {
                                    listGroup.add(arr.getJSONObject(i).getString("transinfo"));
                                    ArrayList<String> arrayList = new ArrayList<>();
                                    arrayList.add("Transaction Amount: " + arr.getJSONObject(i).getString("amount"));
                                    arrayList.add("Previous Balance: " + arr.getJSONObject(i).getString("oldamount"));
                                    arrayList.add("New Balance: " + arr.getJSONObject(i).getString("newamount"));
                                    arrayList.add("Transaction Mode: " + arr.getJSONObject(i).getString("transmode"));
                                    arrayList.add("Transaction Info: "+ arr.getJSONObject(i).getString("transinfo"));
                                    arrayList.add("TimeStamp: " + arr.getJSONObject(i).getString("timestamp").replace('T', ' ').substring(0, 19));
                                    details.put(String.valueOf(i), arrayList);
                                }
                                Typeface RMed = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
                                adapter = new ActivityAdapter(listGroup, details, RMed);
                                expandableListView.setAdapter(adapter);
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
                            loader.setVisibility(View.INVISIBLE);
                            expandableListView.setVisibility(View.INVISIBLE);
                            error_text.setText("NO DATA");
                            error_text.setVisibility(View.VISIBLE);
                        }
                    });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                loader.setVisibility(View.INVISIBLE);
                expandableListView.setVisibility(View.INVISIBLE);
                error_text.setText("NO DATA");
                error_text.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
                loader.setVisibility(View.INVISIBLE);
                expandableListView.setVisibility(View.INVISIBLE);
                error_text.setText("NO DATA");
                error_text.setVisibility(View.VISIBLE);
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
        intent.setClass(Activities.this, More.class);
        startActivity(intent);
        finish();
    }

}