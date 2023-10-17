package com.gbikna.sample.etop.transactions;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardsFragment extends Fragment {
    private Calendar calendar;
    private static final String TAG = CardsFragment.class.getSimpleName();

    public static JSONArray arr = null;
    //TransactionDetailViewModel transactionDetailViewModel;
    //TransactionViewModel transactionViewModel;
    TextView error_text, rrn, walletid, transname, amount, destination, pan, stan, status, username, tid, date2;
    TextView hrrn, hwalletid, htransname, hamount, hpan, hstan, hstatus, htid, hdate2, hid, id, hfee, fee;
    LinearLayout barrier, details, details2;
    ProgressBar loader;
    ListView listView;
    Button date_picker;
    com.gbikna.sample.etop.transactions.TransactionHistory transaction;
    Button botton, search;
    //TransactionDetailsResponse transactionDetail;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CardsFragment() {
        System.out.println("WE ARE IN CARDSFRAGMENT");
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardsFragment newInstance(String param1, String param2) {
        CardsFragment fragment = new CardsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        error_text = (TextView) view.findViewById(R.id.error_text);
        loader = (ProgressBar) view.findViewById(R.id.loader);
        listView = (ListView) view.findViewById(R.id.transaction_history_list);
        date_picker = (Button) view.findViewById(R.id.date_picker);
        search = (Button)view.findViewById(R.id.search);

        rrn = (TextView) view.findViewById(R.id.rrn) ;
        hrrn = (TextView) view.findViewById(R.id.hrrn) ;
        id = (TextView) view.findViewById(R.id.id) ;
        hid = (TextView) view.findViewById(R.id.hid) ;
        fee = (TextView) view.findViewById(R.id.fee) ;
        hfee = (TextView) view.findViewById(R.id.hfee) ;
        walletid = (TextView) view.findViewById(R.id.walletid);
        hwalletid = (TextView) view.findViewById(R.id.hwalletid);
        transname = (TextView) view.findViewById(R.id.transname);
        htransname = (TextView) view.findViewById(R.id.htransname);
        amount = (TextView) view.findViewById(R.id.amount);
        hamount = (TextView) view.findViewById(R.id.hamount);
        destination = (TextView) view.findViewById(R.id.destination);
        pan = (TextView) view.findViewById(R.id.pan);
        hpan = (TextView) view.findViewById(R.id.hpan);
        stan = (TextView) view.findViewById(R.id.stan);
        hstan = (TextView) view.findViewById(R.id.hstan);
        status = (TextView) view.findViewById(R.id.status);
        hstatus = (TextView) view.findViewById(R.id.hstatus);

        date2 = (TextView)view.findViewById(R.id.date2);
        hdate2 = (TextView)view.findViewById(R.id.hdate2);

        //date = (TextView) view.findViewById(R.id.date);
        username = (TextView) view.findViewById(R.id.username);
        tid = (TextView) view.findViewById(R.id.tid);

        barrier = (LinearLayout) view.findViewById(R.id.barrier);
        details = (LinearLayout) view.findViewById(R.id.details);
        details2 = (LinearLayout) view.findViewById(R.id.details2);
        botton = (Button)view.findViewById(R.id.close);

        botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "I AM BEING CLICKED");
                details2.setVisibility(View.INVISIBLE);
            }
        });

        NumberFormat format = new DecimalFormat("#,###.##");

        calendar = Calendar.getInstance();

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

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "DATE PICKER CLICKED HERE");
                date_picker.setEnabled(false);
                materialDatePicker.show(getParentFragmentManager(), "DATE PICKER");
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterByRef();
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
                date_picker.setText("Filter By Ref");
                JSONObject send = new JSONObject();
                try {
                    send.put("enddate", formatter.format(selection.second));
                    send.put("startdate", formatter.format(selection.first));
                    Log.i(TAG, "REQUEST: " + send.toString());
                    new PullData().execute(ProfileParser.BASEURL + "/apis/etop/records/get/transactions", send.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        JSONObject send = new JSONObject();
        try {
            send.put("enddate", formatter.format(calendar.getTime()));
            send.put("startdate", formatter.format(calendar.getTime()));
            Log.i(TAG, "REQUEST: " + send.toString());
            new PullData().execute(ProfileParser.BASEURL + "/apis/etop/records/get/transactions", send.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        error_text.setText("No Transactions Today");
        listView.setEmptyView(error_text);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i(TAG, "SELECTED VALUE POSITION: " + position);
                //transaction = transactionViewModel.getResponse().getValue().getValue().get(position);
                try {
                    Log.i(TAG, "SELECTED DATA: " + arr.getJSONObject(position));
                    if(true)
                    {
                        details.setVisibility(View.INVISIBLE);
                        loader.setVisibility(View.INVISIBLE);
                        details2.setVisibility(View.VISIBLE);

                        Log.i(TAG, "TRANSACTION NAME: " + arr.getJSONObject(position).getString("transname"));
                        hid.setText("TXN ID: ");
                        id.setText(arr.getJSONObject(position).getString("id"));

                        hfee.setText("TXN FEE: ");
                        fee.setText(arr.getJSONObject(position).getString("fee"));

                        if(arr.getJSONObject(position).getString("transname").equals("PURCHASE"))
                        {
                            hwalletid.setText("WALLETID: ");
                            walletid.setText(arr.getJSONObject(position).getString("walletid"));

                            htransname.setText("TXN NAME:");
                            transname.setText(arr.getJSONObject(position).getString("transname"));

                            hamount.setText("AMOUNT:");
                            amount.setText(arr.getJSONObject(position).getString("amount"));

                            hpan.setText("PAN: ");
                            pan.setText(arr.getJSONObject(position).getString("pan"));

                            hstan.setText("STAN: ");
                            stan.setText(arr.getJSONObject(position).getString("stan"));

                            if(arr.getJSONObject(position).getString("status").equals("00"))
                            {
                                hstatus.setText("STATUS: ");
                                status.setText("TRANSACTION SUCCESS");

                                hrrn.setText("RRN: ");
                                rrn.setText(arr.getJSONObject(position).getString("rrn"));
                            }else
                            {
                                hstatus.setText("STATUS: ");
                                status.setText(ProfileParser.getResponseDetails(arr.getJSONObject(position).getString("status")).toUpperCase());

                                hrrn.setText("REF: ");
                                rrn.setText(String.valueOf(arr.getJSONObject(position).getLong("id")));
                            }

                            hdate2.setText("DATE: ");
                            date2.setText("DATE: " + arr.getJSONObject(position).getString("timestamp").replace('T', ' ').substring(0, 19));
                        }else if(arr.getJSONObject(position).getString("transname").equals("CASH OUT"))
                        {
                            hwalletid.setText("WALLETID: ");
                            walletid.setText(arr.getJSONObject(position).getString("walletid"));

                            htransname.setText("TXN NAME: ");
                            transname.setText(arr.getJSONObject(position).getString("transname"));

                            hamount.setText("AMOUNT: ");
                            amount.setText(arr.getJSONObject(position).getString("amount"));

                            hpan.setText("PAN: ");
                            pan.setText(arr.getJSONObject(position).getString("pan"));

                            hstan.setText("STAN: ");
                            stan.setText(arr.getJSONObject(position).getString("stan"));

                            if(arr.getJSONObject(position).getString("status").equals("00"))
                            {
                                hstatus.setText("STATUS: ");
                                status.setText("TRANSACTION SUCCESS");

                                hrrn.setText("RRN: ");
                                rrn.setText(arr.getJSONObject(position).getString("rrn"));
                            }else
                            {
                                hstatus.setText("STATUS: ");
                                status.setText(ProfileParser.getResponseDetails(arr.getJSONObject(position).getString("status")).toUpperCase());

                                hrrn.setText("RRN: ");
                                rrn.setText(String.valueOf(arr.getJSONObject(position).getLong("rrn")));
                            }

                            hdate2.setText("DATE: ");
                            date2.setText("DATE: " + arr.getJSONObject(position).getString("timestamp").replace('T', ' ').substring(0, 19));
                        }else if(arr.getJSONObject(position).getString("transname").equals("TRANSFER"))
                        {
                            htransname.setText("TXN NAME:");
                            transname.setText(arr.getJSONObject(position).getString("transname"));

                            hamount.setText("AMOUNT:");
                            amount.setText(arr.getJSONObject(position).getString("amount"));

                            hpan.setText("DESTINATION: ");
                            pan.setText(arr.getJSONObject(position).getString("destination"));

                            //TransferRequest{destination='2090711318', amount='100.00', description='self', receivername='IBANGA, WISDOM IBANGA', sessionid='090267210923112005517940150004', tid='2215U461', bankcode='000004', transferpin='1234'}
                            try{
                                String v = arr.getJSONObject(position).getString("request")
                                        .replace("TransferRequest{", "{");
                                JSONObject vv = new JSONObject(v);
                                hstan.setText("BEN: ");
                                stan.setText(vv.getString("receivername"));

                                String pro = "[{\"bankCode\":\"090267\",\"bankName\":\"Kuda.\"},{\"bankCode\":\"000014\",\"bankName\":\"Access Bank\"},{\"bankCode\":\"000005\",\"bankName\":\"Access Bank PLC (Diamond)\"},{\"bankCode\":\"090134\",\"bankName\":\"ACCION MFB\"},{\"bankCode\":\"090148\",\"bankName\":\"Bowen MFB\"},{\"bankCode\":\"100005\",\"bankName\":\"Cellulant\"},{\"bankCode\":\"000009\",\"bankName\":\"Citi Bank\"},{\"bankCode\":\"100032\",\"bankName\":\"Contec Global\"},{\"bankCode\":\"060001\",\"bankName\":\"Coronation\"},{\"bankCode\":\"090156\",\"bankName\":\"e-BARCs MFB\"},{\"bankCode\":\"000010\",\"bankName\":\"Ecobank Bank\"},{\"bankCode\":\"100008\",\"bankName\":\"Ecobank Xpress Account\"},{\"bankCode\":\"090097\",\"bankName\":\"Ekondo MFB\"},{\"bankCode\":\"000003\",\"bankName\":\"FCMB\"},{\"bankCode\":\"000007\",\"bankName\":\"Fidelity Bank\"},{\"bankCode\":\"000016\",\"bankName\":\"First Bank of Nigeria\"},{\"bankCode\":\"110002\",\"bankName\":\"Flutterwave Technology solutions Limited\"},{\"bankCode\":\"100022\",\"bankName\":\"GoMoney\"},{\"bankCode\":\"000013\",\"bankName\":\"GTBank Plc\"},{\"bankCode\":\"000020\",\"bankName\":\"Heritage\"},{\"bankCode\":\"090175\",\"bankName\":\"HighStreet MFB\"},{\"bankCode\":\"000006\",\"bankName\":\"JAIZ Bank\"},{\"bankCode\":\"090003\",\"bankName\":\"JubileeLife\"},{\"bankCode\":\"090191\",\"bankName\":\"KCMB MFB\"},{\"bankCode\":\"000002\",\"bankName\":\"Keystone Bank\"},{\"bankCode\":\"090171\",\"bankName\":\"Mainstreet MFB\"},{\"bankCode\":\"100026\",\"bankName\":\"ONE FINANCE\"},{\"bankCode\":\"100002\",\"bankName\":\"Paga\"},{\"bankCode\":\"100033\",\"bankName\":\"PALMPAY\"},{\"bankCode\":\"100003\",\"bankName\":\"Parkway-ReadyCash\"},{\"bankCode\":\"110001\",\"bankName\":\"PayAttitude Online\"},{\"bankCode\":\"100004\",\"bankName\":\"Paycom(OPay)\"},{\"bankCode\":\"000008\",\"bankName\":\"POLARIS BANK\"},{\"bankCode\":\"000023\",\"bankName\":\"Providus Bank \"},{\"bankCode\":\"000024\",\"bankName\":\"Rand Merchant Bank\"},{\"bankCode\":\"000012\",\"bankName\":\"StanbicIBTC Bank\"},{\"bankCode\":\"100007\",\"bankName\":\"StanbicMobileMoney\"},{\"bankCode\":\"000021\",\"bankName\":\"StandardChartered\"},{\"bankCode\":\"000001\",\"bankName\":\"Sterling Bank\"},{\"bankCode\":\"000022\",\"bankName\":\"SUNTRUST BANK\"},{\"bankCode\":\"000018\",\"bankName\":\"Union Bank\"},{\"bankCode\":\"000004\",\"bankName\":\"United Bank for Africa\"},{\"bankCode\":\"000011\",\"bankName\":\"Unity Bank\"},{\"bankCode\":\"090110\",\"bankName\":\"VFD MFB\"},{\"bankCode\":\"000017\",\"bankName\":\"Wema Bank\"},{\"bankCode\":\"000015\",\"bankName\":\"ZENITH BANK PLC\"},{\"bankCode\":\"100025\",\"bankName\":\"Zinternet - KongaPay\"}]";
                                JSONArray arr = new JSONArray(pro);
                                int i = 0;
                                for (i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    if(obj.getString("bankCode").equals(vv.getString("bankcode")))
                                    {
                                        hwalletid.setText("BANK: ");
                                        walletid.setText(obj.getString("bankName").toUpperCase());
                                        break;
                                    }
                                }
                            }catch (Exception e)
                            {
                                Log.i(TAG, e.getMessage());
                                hstan.setText("STAN: ");
                                stan.setText(arr.getJSONObject(position).getString("stan"));

                                hwalletid.setText("WALLETID: ");
                                walletid.setText(arr.getJSONObject(position).getString("walletid"));
                            }

                            hstatus.setText("STATUS: ");
                            if(arr.getJSONObject(position).getString("status").equals("00")) {
                                status.setText("Transfer Success".toUpperCase());
                                hrrn.setText("SES ID: ");
                                rrn.setText(arr.getJSONObject(position).getString("rrn"));
                            }else
                            {
                                status.setText("Transfer Failed".toUpperCase());
                                hrrn.setText("SES ID: ");
                                rrn.setText("NA");
                            }

                            hdate2.setText("DATE: ");
                            date2.setText("DATE: " + arr.getJSONObject(position).getString("timestamp").replace('T', ' ').substring(0, 19));
                        }else
                        {
                            //hrrn.setText("SES ID: ");
                            //rrn.setText(arr.getJSONObject(position).getString("rrn"));

                            hwalletid.setText("WALLETID: ");
                            walletid.setText(arr.getJSONObject(position).getString("walletid"));

                            htransname.setText("TXN NAME:");
                            transname.setText(arr.getJSONObject(position).getString("transname"));

                            hamount.setText("AMOUNT:");
                            amount.setText(arr.getJSONObject(position).getString("amount"));

                            hpan.setText("DESTINATION: ");
                            pan.setText(arr.getJSONObject(position).getString("destination"));

                            hstan.setText("STAN: ");
                            stan.setText(arr.getJSONObject(position).getString("stan"));


                            hstatus.setText("STATUS: ");
                            if(arr.getJSONObject(position).getString("status").equals("00")) {
                                status.setText("Payment Success".toUpperCase());
                                hrrn.setText("SES ID: ");
                                rrn.setText(arr.getJSONObject(position).getString("rrn"));
                            }else
                            {
                                status.setText("Payment Failed".toUpperCase());
                                hrrn.setText("SES ID: ");
                                rrn.setText("NA");
                            }

                            date2.setText("DATE: " + arr.getJSONObject(position).getString("timestamp").replace('T', ' ').substring(0, 19));
                        }



                        barrier.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                barrier.setVisibility(View.GONE);
                                details.setVisibility(View.GONE);
                                details2.setVisibility(View.GONE);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    void FilterByRef()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setCancelable(false);
        builder.setTitle("ENTER REF, PAN, DEST.");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        // Set up the input
        //final TextView vw = new TextView(this);
        //vw.setText(display);
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        layout.addView(input);
        //layout.addView(vw);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ref = input.getText().toString();
                try {
                    listView.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.INVISIBLE);
                    error_text.setVisibility(View.INVISIBLE);

                    List<com.gbikna.sample.etop.transactions.TransactionHistory> contactList = new ArrayList<com.gbikna.sample.etop.transactions.TransactionHistory>();
                    for (int i = 0; i < arr.length(); i++)
                    {
                        Log.i(TAG, arr.getJSONObject(i).toString());
                        if(arr.getJSONObject(i).getString("ref").contains(ref) ||
                                ((arr.getJSONObject(i).has("destination"))
                                        && (arr.getJSONObject(i).getString("destination").contains(ref))) ||
                                ((arr.getJSONObject(i).has("pan"))
                                        && (arr.getJSONObject(i).getString("pan").contains(ref))))
                        {
                            com.gbikna.sample.etop.transactions.TransactionHistory tns = new
                                    com.gbikna.sample.etop.transactions.TransactionHistory(
                                    arr.getJSONObject(i).getLong("id"),
                                    arr.getJSONObject(i).getString("agentamount"),
                                    arr.getJSONObject(i).getString("amount"),
                                    arr.getJSONObject(i).getString("busname"),
                                    arr.getJSONObject(i).getString("category"),
                                    arr.getJSONObject(i).getString("city"),
                                    arr.getJSONObject(i).getString("destination"),

                                    arr.getJSONObject(i).has("expirydate") ?
                                            arr.getJSONObject(i).getString("expirydate") :
                                            "00/00",

                                    arr.getJSONObject(i).getString("fee"),
                                    arr.getJSONObject(i).getString("lga"),

                                    arr.getJSONObject(i).has("pan") ?
                                            arr.getJSONObject(i).getString("pan") :
                                            "000000******0000",

                                    arr.getJSONObject(i).getString("ref"),
                                    arr.getJSONObject(i).getString("request"),
                                    arr.getJSONObject(i).getString("response"),
                                    arr.getJSONObject(i).getString("rrn"),
                                    arr.getJSONObject(i).getString("stan"),
                                    arr.getJSONObject(i).getString("state"),
                                    arr.getJSONObject(i).getString("status"),
                                    arr.getJSONObject(i).getString("superagentamount"),
                                    arr.getJSONObject(i).getString("tid"),
                                    arr.getJSONObject(i).getString("tmsamount"),
                                    arr.getJSONObject(i).getString("transname"),
                                    arr.getJSONObject(i).getString("username"),
                                    arr.getJSONObject(i).getString("walletid"),
                                    arr.getJSONObject(i).getString("timestamp"));
                            contactList.add(tns);
                        }
                    }
                    TransactionHistoryAdapter transactionHistoryAdapter = new TransactionHistoryAdapter((com.gbikna.sample.etop.TransactionHistory) getActivity(),
                            contactList);
                    listView.setAdapter(transactionHistoryAdapter);
                    transactionHistoryAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
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
                            Log.i(TAG, "RECIEVE: " + server_response);
                            try {
                                arr = new JSONArray(server_response);

                                listView.setVisibility(View.VISIBLE);
                                loader.setVisibility(View.INVISIBLE);
                                error_text.setVisibility(View.INVISIBLE);

                                List<com.gbikna.sample.etop.transactions.TransactionHistory> contactList = new ArrayList<com.gbikna.sample.etop.transactions.TransactionHistory>();
                                for (int i = 0; i < arr.length(); i++)
                                {
                                    Log.i(TAG, arr.getJSONObject(i).toString());
                                    com.gbikna.sample.etop.transactions.TransactionHistory tns = new
                                            com.gbikna.sample.etop.transactions.TransactionHistory(
                                            arr.getJSONObject(i).getLong("id"),
                                                    arr.getJSONObject(i).getString("agentamount"),
                                            arr.getJSONObject(i).getString("amount"),
                                            arr.getJSONObject(i).getString("busname"),
                                            arr.getJSONObject(i).getString("category"),
                                            arr.getJSONObject(i).getString("city"),
                                            arr.getJSONObject(i).getString("destination"),

                                            arr.getJSONObject(i).has("expirydate") ?
                                                arr.getJSONObject(i).getString("expirydate") :
                                                    "00/00",

                                            arr.getJSONObject(i).getString("fee"),
                                            arr.getJSONObject(i).getString("lga"),

                                            arr.getJSONObject(i).has("pan") ?
                                                    arr.getJSONObject(i).getString("pan") :
                                                    "000000******0000",

                                            arr.getJSONObject(i).getString("ref"),
                                            arr.getJSONObject(i).getString("request"),
                                            arr.getJSONObject(i).getString("response"),
                                            arr.getJSONObject(i).getString("rrn"),
                                            arr.getJSONObject(i).getString("stan"),
                                            arr.getJSONObject(i).getString("state"),
                                            arr.getJSONObject(i).getString("status"),
                                            arr.getJSONObject(i).getString("superagentamount"),
                                            arr.getJSONObject(i).getString("tid"),
                                            arr.getJSONObject(i).getString("tmsamount"),
                                            arr.getJSONObject(i).getString("transname"),
                                            arr.getJSONObject(i).getString("username"),
                                            arr.getJSONObject(i).getString("walletid"),
                                            arr.getJSONObject(i).getString("timestamp"));
                                    contactList.add(tns);
                                }
                                TransactionHistoryAdapter transactionHistoryAdapter = new TransactionHistoryAdapter((com.gbikna.sample.etop.TransactionHistory) getActivity(),
                                        contactList);
                                listView.setAdapter(transactionHistoryAdapter);
                                transactionHistoryAdapter.notifyDataSetChanged();
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
                            listView.setVisibility(View.INVISIBLE);
                            error_text.setText("NO DATA");
                            error_text.setVisibility(View.VISIBLE);
                        }
                    });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                loader.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.INVISIBLE);
                error_text.setText("NO DATA");
                error_text.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
                loader.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.INVISIBLE);
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
}
