package com.gbikna.sample.etop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.Logout;
import com.gbikna.sample.gbikna.card.activity.AccountType;
import com.gbikna.sample.gbikna.util.database.DATABASEHANDLER;
import com.gbikna.sample.gbikna.util.database.MESSAGES;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.etop.adapters.GridViewAdapter;
import com.gbikna.sample.etop.bills.Data;
import com.gbikna.sample.etop.bills.Disco;
import com.gbikna.sample.etop.bills.PayTv;
import com.gbikna.sample.etop.bills.Vtu;
import com.gbikna.sample.etop.util.SignupVr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;


public class Dashboard extends AppCompatActivity {
    GridView gridView;
    LinearLayout profile;
    ImageView pict, ImageView;
    //LinearLayout gotoprofile, records;
    TextView name, walletid, kudaid, balance, messageinfo, ImageViewText;
    private static final String TAG = Dashboard.class.getSimpleName();
    public static String useBalance = "";
    public static int mesCount = 0;
    private List<String> getTransaction(String txn) {
        List<String> scripts = new ArrayList<String>();
        //options.add(0, "Purchase");
        //ArrayList<String> scripts = new ArrayList<String>();
        //Log.i(TAG, "Transaction: " + txn);
        String findStr = "name - ";
        int lastIndex = 0;
        int count = 0;
        while (lastIndex != -1) {
            lastIndex = txn.indexOf(findStr, lastIndex);
            if (lastIndex != -1) {
                int j;
                StringBuilder sb = new StringBuilder();
                //count++;
                lastIndex += findStr.length();
                j = lastIndex;
                for (; ; j++) {
                    if (txn.charAt(j) == '#') {
                        scripts.add(count, sb.toString());
                        count++;
                        //scripts.add(count + ". " + sb.toString());
                        break;
                    } else {
                        sb.append(txn.charAt(j));
                    }
                }
            }
        }
//        scripts.add(count, "TERMINAL");
//        count++;
        scripts.add(count, "MORE");
        count++;
        scripts.add(count, "LOGOUT");
        count++;
        return scripts;
    }

    Handler handler;
    Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        pict = (ImageView)findViewById(R.id.picture);
        ImageView= (ImageView)findViewById(R.id.ImageView);
        //gotoprofile = (LinearLayout)findViewById(R.id.gotoprofile);
        //records = (LinearLayout)findViewById(R.id.records);
        ImageViewText = (TextView)findViewById(R.id.ImageViewText);
        java.io.File newfile = new File(getApplicationContext().getFilesDir(), "imagea.jpg");
        Bitmap myBitmap = BitmapFactory.decodeFile(newfile.getAbsolutePath());
        pict.setImageBitmap(myBitmap);


        mesCount = 0;
        messageinfo = (TextView)findViewById(R.id.messages);
        name = (TextView)findViewById(R.id.name);
        name.setText("WELCOME " + formatName(SignupVr.businessname.toUpperCase()));
        walletid = (TextView)findViewById(R.id.walletid);
        kudaid = (TextView)findViewById(R.id.kudaid);
        //busname = (TextView)findViewById(R.id.busname);
        walletid.setText("WALLET ID: " + SignupVr.walletid);
        balance = (TextView)findViewById(R.id.balance);
        balance.setText("₦ X.XX");

        kudaid.setText("LOTUS ACT: " + SignupVr.kudaactnum);
        //busname.setText(SignupVr.businessname.toUpperCase());

        new GetBalance().execute(BASEURL + "/apis/etop/records/wallet/balance");
        new GetMessages().execute(BASEURL + "/apis/etop/messages/all");

        if(ProfileParser.blocked.equals("true"))
        {
            Toast.makeText(Dashboard.this, "YOU HAVE BEEN BLOCKED", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            Bundle data = new Bundle();
            intent.setClass(Dashboard.this, Logout.class);
            intent.putExtras(data);
            startActivity(intent);
            finish();
        }

        gridView = (GridView) findViewById(R.id.dash_menu);
        profile = (LinearLayout) findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile.controller = 2;
                Intent intent = new Intent();
                intent.setClass(Dashboard.this, MyAccounts.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile.controller = 2;
                Intent intent = new Intent();
                intent.setClass(Dashboard.this, Messages.class);
                startActivity(intent);
                finish();
            }
        });

        balance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(balance.getText().equals("₦ X.XX"))
                    balance.setText(useBalance);
                else
                    balance.setText("₦ X.XX");
            }
        });

        /*gotoprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile.controller = 2;
                Intent intent = new Intent();
                intent.setClass(Dashboard.this, Profile.class);
                startActivity(intent);
                finish();
            }
        });*/

        /*records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //intent.setClass(Dashboard.this, Reports.class);
                intent.setClass(Dashboard.this, Messages.class);
                startActivity(intent);
                finish();
            }
        });*/


        final List<String> options = getTransaction(ProfileParser.transactions);


        GridViewAdapter gridViewAdapter = new GridViewAdapter(this, options);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "POSITION: " + position);
                Log.i(TAG, "STRING: " + options.get(position));
                ProfileParser.sending = new String[128];

                Intent intent = new Intent();
                if(options.get(position).equals("PURCHASE"))
                {
                    ProfileParser.resetFields();
                    ProfileParser.txnNumber = "1";
                    ProfileParser.txnName = "PURCHASE";
                    ProfileParser.field0 = "0200";
                    ProfileParser.field28 = "C00000000";
                    ProfileParser.field32 = "111129";
                    ProfileParser.field123 = "510101511344101";
                    Log.i(TAG, "WISDOM NUMBER 1: " + ProfileParser.txnNumber);
                    intent.setClass(Dashboard.this, AccountType.class);
                    startActivity(intent);
                    finish();
                }else if(options.get(position).equals("CASHOUT"))
                {
                    ProfileParser.resetFields();
                    ProfileParser.txnNumber = "2";
                    ProfileParser.txnName = "CASH WITHDRAWAL";
                    ProfileParser.field0 = "0200";
                    ProfileParser.field28 = "C00000000";
                    ProfileParser.field32 = "111129";
                    ProfileParser.field123 = "510101511344101";
                    Log.i(TAG, "WISDOM NUMBER 1: " + ProfileParser.txnNumber);
                    intent.setClass(Dashboard.this, AccountType.class);
                    startActivity(intent);
                    finish();
                }else if(options.get(position).equals("TRANSFER"))
                {
                    if(SignupVr.approved.isEmpty() || !SignupVr.approved.equals("true"))
                    {
                        Toast.makeText(Dashboard.this, "PLEASE VERIFY YOUR ACCOUNT", Toast.LENGTH_LONG);
                    }else
                    {
                        ProfileParser.resetFields();
                        ProfileParser.txnNumber = "3";
                        ProfileParser.txnName = "TRANSFER";
                        ProfileParser.field0 = "0200";
                        ProfileParser.field28 = "C00000000";
                        ProfileParser.field32 = "111129";
                        ProfileParser.field123 = "510101511344101";
                        Log.i(TAG, "WISDOM NUMBER 1: " + ProfileParser.txnNumber);
                        ProfileParser.field3 = "000000";
                        intent.setClass(Dashboard.this, Transfer.class);
                        startActivity(intent);
                        finish();
                    }
                }/*else if(options.get(position).equals("BILLS PAYMENT"))
                {
                    if(SignupVr.approved.isEmpty() || !SignupVr.approved.equals("true"))
                    {
                        Toast.makeText(Dashboard.this, "PLEASE VERIFY YOUR ACCOUNT", Toast.LENGTH_LONG);
                    }else {
                        ProfileParser.resetFields();
                        ProfileParser.txnNumber = "4";
                        ProfileParser.txnName = "BILLS PAYMENT";
                        intent.setClass(Dashboard.this, BillsPayment.class);
                        startActivity(intent);
                        finish();
                    }
                }else*/ if(options.get(position).equals("VTU"))
                {
                    if(SignupVr.approved.isEmpty() || !SignupVr.approved.equals("true"))
                    {
                        Toast.makeText(Dashboard.this, "PLEASE VERIFY YOUR ACCOUNT", Toast.LENGTH_LONG);
                    }else {
                        ProfileParser.resetFields();
                        ProfileParser.txnNumber = "4";
                        ProfileParser.txnName = "BILLS PAYMENT";
                        intent.setClass(Dashboard.this, Vtu.class);
                        startActivity(intent);
                        finish();
                    }
                }else if(options.get(position).equals("INTERNET"))
                {
                    if(SignupVr.approved.isEmpty() || !SignupVr.approved.equals("true"))
                    {
                        Toast.makeText(Dashboard.this, "PLEASE VERIFY YOUR ACCOUNT", Toast.LENGTH_LONG);
                    }else {
                        ProfileParser.resetFields();
                        ProfileParser.txnNumber = "4";
                        ProfileParser.txnName = "BILLS PAYMENT";
                        intent.setClass(Dashboard.this, Data.class);
                        startActivity(intent);
                        finish();
                    }
                }else if(options.get(position).equals("ELECTRICITY"))
                {
                    if(SignupVr.approved.isEmpty() || !SignupVr.approved.equals("true"))
                    {
                        Toast.makeText(Dashboard.this, "PLEASE VERIFY YOUR ACCOUNT", Toast.LENGTH_LONG);
                    }else {
                        ProfileParser.resetFields();
                        ProfileParser.txnNumber = "4";
                        ProfileParser.txnName = "BILLS PAYMENT";
                        intent.setClass(Dashboard.this, Disco.class);
                        startActivity(intent);
                        finish();
                    }
                }else if(options.get(position).equals("PAY TV"))
                {
                    if(SignupVr.approved.isEmpty() || !SignupVr.approved.equals("true"))
                    {
                        Toast.makeText(Dashboard.this, "PLEASE VERIFY YOUR ACCOUNT", Toast.LENGTH_LONG);
                    }else {
                        ProfileParser.resetFields();
                        ProfileParser.txnNumber = "4";
                        ProfileParser.txnName = "BILLS PAYMENT";
                        intent.setClass(Dashboard.this, PayTv.class);
                        startActivity(intent);
                        finish();
                    }
                }else if(options.get(position).equals("MORE"))
                {
                    Bundle data = new Bundle();
                    intent.setClass(Dashboard.this, More.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }else if(options.get(position).equals("TERMINAL"))
                {
                    Bundle data = new Bundle();
                    intent.setClass(Dashboard.this, BuyTerminal.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }else if(options.get(position).equals("LOGOUT"))
                {
                    Bundle data = new Bundle();
                    intent.setClass(Dashboard.this, Login.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }
            }
        });

        /*
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                ProfileParser.token = "";
                stopHandler();
                //Toast.makeText(Dashboard.this, "YOU ARE LOGGED OFF",Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                Bundle data = new Bundle();
                intent.setClass(Dashboard.this, Login.class);
                intent.putExtras(data);
                startActivity(intent);
                finish();
            }
        };
        startHandler();*/
    }

    private String formatName(String toUpperCase) {
        if(toUpperCase == null)
            return "";

        if(toUpperCase.length() < 11)
        {
            return toUpperCase;
        }
        return toUpperCase.substring(0, 11);
    }


    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        //stopHandler();//stop first and then start
        //startHandler();
    }
    /*public void stopHandler() {
        handler.removeCallbacks(r);
    }
    public void startHandler() {
        handler.postDelayed(r, 5*60*1000); //for 3 minutes
    }*/

    public class GetBalance extends AsyncTask<String , Void ,String> {
        String server_response;
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                android.util.Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Bearer " + ProfileParser.token);
                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = readStream(urlConnection.getInputStream());
                    android.util.Log.i(TAG, "Length: " + server_response.length());
                    android.util.Log.i(TAG, "Response: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            ProfileParser.balanceResponse = server_response;
                            try {
                                JSONArray arr = new JSONArray(server_response);
                                JSONObject obj = arr.getJSONObject(0);
                                useBalance = "₦ " + obj.getString("amount");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else
                {
                    server_response = readStream(urlConnection.getErrorStream());
                    android.util.Log.i(TAG, "ERROR RESPONSE: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            balance.setText("RETRY LATER.");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        balance.setText("RETRY LATER...");
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            android.util.Log.i("Response", "" + server_response);
        }
    }

    public class GetMessages extends AsyncTask<String , Void ,String> {
        String server_response;
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                android.util.Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Bearer " + ProfileParser.token);
                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = readStream(urlConnection.getInputStream());
                    android.util.Log.i(TAG, "Length: " + server_response.length());
                    android.util.Log.i(TAG, "Response: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray arr = new JSONArray(server_response);
                                if(arr.length() > 0)
                                {
                                    DATABASEHANDLER db2 = new DATABASEHANDLER(Dashboard.this);
                                    List<MESSAGES> mes = db2.getAllMessage();
                                    for(int i = 0; i < arr.length(); i++)
                                    {
                                        int st = 0;
                                        for(int j = 0; j < mes.size(); j++)
                                        {
                                            if(arr.getJSONObject(i).getInt("id") == Integer.valueOf(mes.get(j).getServerid()))
                                            {
                                                st = 1;
                                            }
                                        }

                                        if(st == 0 && !arr.getJSONObject(i).has("walletids"))
                                        {
                                            db2.InsertMessages(arr.getJSONObject(i).getString("message"),
                                                    arr.getJSONObject(i).getString("priority"),
                                                    "NA",
                                                    String.valueOf(arr.getJSONObject(i).getInt("id")),
                                                    "viewed", "false");

                                            //Define Notification Manager
                                            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                            //Define sound URI
                                            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                                    .setSmallIcon(R.drawable.logo)
                                                    .setContentTitle("NOTIFICATION")
                                                    .setContentText(arr.getJSONObject(i).getString("message").substring(0, 40) + "...")
                                                    .setSound(soundUri); //This sets the sound to play
                                            //Display notification
                                            notificationManager.notify(0, mBuilder.build());

                                            //Toast.makeText(getApplicationContext(),
                                            //        arr.getJSONObject(i).getString("message"), Toast.LENGTH_LONG);
                                        }else if(st == 0 && arr.getJSONObject(i).has("walletids"))
                                        {
                                            if(arr.getJSONObject(i).getString("walletids").contains(SignupVr.walletid)) {
                                                db2.InsertMessages(arr.getJSONObject(i).getString("message"),
                                                        arr.getJSONObject(i).getString("priority"),
                                                        arr.getJSONObject(i).getString("walletids"),
                                                        String.valueOf(arr.getJSONObject(i).getInt("id")),
                                                        "viewed", "false");
                                                //Define Notification Manager
                                                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                                //Define sound URI
                                                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                                        .setSmallIcon(R.drawable.logo)
                                                        .setContentTitle("NOTIFICATION")
                                                        .setContentText(arr.getJSONObject(i).getString("message").substring(0, 40) + "...")
                                                        .setSound(soundUri); //This sets the sound to play
                                                //Display notification
                                                notificationManager.notify(0, mBuilder.build());

                                                //Toast.makeText(getApplicationContext(),
                                                //        arr.getJSONObject(i).getString("message"), Toast.LENGTH_LONG);
                                            }
                                        }

                                        mes = db2.getAllMessage();
                                        for(int j = 0; j < mes.size(); j++)
                                        {
                                            if(mes.get(j).getHasview() == null || mes.get(j).getHasview().equals("false"))
                                            {
                                                mesCount = mesCount + 1;
                                            }
                                        }

                                        if(mesCount <= 1)
                                        {
                                            ImageViewText.setTextColor(Color.parseColor("#4A5297"));
                                        }else
                                        {
                                            ImageViewText.setTextColor(Color.parseColor("#FF0000"));
                                        }
                                        ImageViewText.setText(String.valueOf(mesCount));
                                        ImageViewText.setTypeface(null, Typeface.BOLD_ITALIC);

                                        String verified = "ACCOUNT NOT VERIFIED";
                                        int c = 0;
                                        if(SignupVr.approved.isEmpty() || !SignupVr.approved.equals("true"))
                                        {
                                            verified = "ACCOUNT NOT VERIFIED";
                                        }else
                                        {
                                            verified = "ACCOUNT VERIFIED";
                                            messageinfo.setTextColor(Color.parseColor("#4A5297"));
                                        }
                                        messageinfo.setText(verified);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else
                {
                    server_response = readStream(urlConnection.getErrorStream());
                    android.util.Log.i(TAG, "ERROR RESPONSE: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            balance.setText("RETRY LATER.");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        balance.setText("RETRY LATER...");
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            android.util.Log.i("Response", "" + server_response);
        }
    }

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

    @Override
    public void onBackPressed() {
        System.out.println("2. ignored profileddownload onBackPressed 1111");
        Intent intent = new Intent();
        Bundle data = new Bundle();
        intent.setClass(Dashboard.this, Login.class);
        intent.putExtras(data);
        startActivity(intent);
        finish();
    }


}