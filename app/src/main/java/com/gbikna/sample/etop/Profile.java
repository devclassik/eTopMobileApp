package com.gbikna.sample.etop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.etop.util.SignupVr;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;

public class Profile extends AppCompatActivity {
    public static int controller = 0;
    ImageView imageview, imageview2;
    TextView firstname, middlename, lastname, username, phonenumber, gender, nin, dob, housenumber, streetname, city,
            lga, lgacode, state, usertype, bvn, accountholder, accountnumber, bankcode, bankname, kudaactnum, kudatrack,
            unityactnum, walletid, serialnumber, maxamountperday, businessaddress, businessname, businessphone,
            businesstype, cacnumber, landmark, heading;
    Button retrykuda, retryunity, editprofile;
    private static final String TAG = Profile.class.getSimpleName();
    public static int check = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Profile.controller == 1)
                {
                    Intent intent = new Intent();
                    intent.setClass(Profile.this, Login.class);
                    startActivity(intent);
                    finish();
                    Profile.controller = 0;
                }else if(Profile.controller == 2)
                {
                    Intent intent = new Intent();
                    intent.setClass(Profile.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                    Profile.controller = 0;
                }
            }
        });
        imageview = (ImageView)findViewById(R.id.qrcode1);
        imageview2 = (ImageView)findViewById(R.id.qrcode2);
        java.io.File newfile = new File(getApplicationContext().getFilesDir(), "imagea.jpg");
        Bitmap myBitmap = BitmapFactory.decodeFile(newfile.getAbsolutePath());
        imageview.setImageBitmap(myBitmap);

        newfile = new File(getApplicationContext().getFilesDir(), "imageb.jpg");
        myBitmap = BitmapFactory.decodeFile(newfile.getAbsolutePath());
        imageview2.setImageBitmap(myBitmap);

        retrykuda = (Button)findViewById(R.id.retry_kuda);
        //retryunity = (Button)findViewById(R.id.retry_unity);
        editprofile = (Button)findViewById(R.id.edit_profile);
        editprofile.setVisibility(View.INVISIBLE);

        heading = (TextView)findViewById(R.id.heading);
        if(Profile.controller == 1)
        {
            editprofile.setVisibility(View.VISIBLE);
            heading.setText("AWAITING APPROVAL");
        }

        if(!SignupVr.kudaactnum.isEmpty())
        {
            retrykuda.setEnabled(false);
            retrykuda.setVisibility(View.INVISIBLE);
        }

        if(!SignupVr.unityactnum.isEmpty())
        {
            retryunity.setEnabled(false);
            retryunity.setVisibility(View.INVISIBLE);
        }

        firstname = (TextView)findViewById(R.id.firstname1);
        middlename = (TextView)findViewById(R.id.middlename1);
        lastname = (TextView)findViewById(R.id.lastname1);
        username = (TextView)findViewById(R.id.username1);
        phonenumber = (TextView)findViewById(R.id.phonenumber1);
        gender = (TextView)findViewById(R.id.gender1);
        nin = (TextView)findViewById(R.id.nin1);
        dob = (TextView)findViewById(R.id.dob1);
        city = (TextView)findViewById(R.id.city1);
        lga = (TextView)findViewById(R.id.lga1);
        state = (TextView)findViewById(R.id.state1);
        usertype = (TextView)findViewById(R.id.usertype1);

        bvn = (TextView)findViewById(R.id.bvn1);
        accountholder = (TextView)findViewById(R.id.accountholder1);
        accountnumber = (TextView)findViewById(R.id.accountnumber1);
        bankname = (TextView)findViewById(R.id.bankname1);
        kudaactnum = (TextView)findViewById(R.id.kudaactnum1);
        kudatrack = (TextView)findViewById(R.id.kudatrack1);
        walletid = (TextView)findViewById(R.id.walletid1);
        serialnumber = (TextView)findViewById(R.id.serialnumber1);
        businessaddress = (TextView)findViewById(R.id.businessaddress1);
        businessname = (TextView)findViewById(R.id.businessname1);
        businesstype = (TextView)findViewById(R.id.businesstype1);
        cacnumber = (TextView)findViewById(R.id.cacnumber1);
        landmark = (TextView)findViewById(R.id.landmark1);


        firstname.setText(SignupVr.firstname);
        middlename.setText(SignupVr.middlename);
        lastname.setText(SignupVr.lastname);
        username.setText(SignupVr.username);
        phonenumber.setText(SignupVr.phonenumber);
        gender.setText(SignupVr.gender);
        nin.setText(SignupVr.nin);
        dob.setText(SignupVr.dob);
        city.setText(SignupVr.city);
        lga.setText(SignupVr.lga);
        state.setText(SignupVr.state);
        usertype.setText(SignupVr.usertype);
        bvn.setText(SignupVr.bvn);
        accountholder.setText(SignupVr.accountholder);
        accountnumber.setText(SignupVr.accountnumber);
        bankname.setText(SignupVr.bankname);
        kudaactnum.setText(SignupVr.kudaactnum);
        kudatrack.setText(SignupVr.kudatrack);
        walletid.setText(SignupVr.walletid);
        serialnumber.setText(SignupVr.serialnumber);
        businessaddress.setText(SignupVr.businessaddress);
        businessname.setText(SignupVr.businessname);
        //businessphone.setText(SignupVr.businessphone);
        businesstype.setText(SignupVr.businesstype);
        cacnumber.setText(SignupVr.cacnumber);
        landmark.setText(SignupVr.landmark);

        retrykuda.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                retrykuda.setText("Please wait...");
                retrykuda.setEnabled(false);
                check = 1;
                new RetryGetAccount().execute(BASEURL + "/apis/etop/signup/retry/kuda");
            }
        });

        editprofile.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                BussinessInformation.busControl = 1;
                PersonalInformation.personalControl = 1;
                BankInfo.bankControl = 1;
                Profile.controller = 1;
                Intent intent = new Intent();
                intent.setClass(Profile.this, BussinessInformation.class);
                startActivity(intent);
                finish();
            }
        });

        /*retryunity.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                retryunity.setText("Please wait...");
                retryunity.setEnabled(false);
                check = 1;
                new RetryGetAccount().execute(BASEURL + "/apis/etop/signup/retry/unity");
            }
        });*/
    }

    public class RetryGetAccount extends AsyncTask<String , Void ,String> {
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
                                JSONObject obj = new JSONObject(server_response);
                                if(obj.getInt("code") == 200)
                                {
                                    if(check == 1)
                                    {
                                        retrykuda.setText("SUCCESS");
                                        retrykuda.setEnabled(false);
                                        Toast.makeText(Profile.this, "KUDA ACCOUNT CREATION SUCCESS", Toast.LENGTH_LONG).show();
                                        //retrykuda.setVisibility(View.INVISIBLE);
                                    }else if(check == 2)
                                    {
                                        retryunity.setText("SUCCESS");
                                        retryunity.setEnabled(false);
                                        Toast.makeText(Profile.this, "KUDA ACCOUNT CREATION SUCCESS", Toast.LENGTH_LONG).show();
                                        //retryunity.setVisibility(View.INVISIBLE);
                                    }
                                    check = 0;
                                }else
                                {
                                    if(check == 1)
                                    {
                                        retrykuda.setText("RETRY KUDA ACCOUNT");
                                        retrykuda.setEnabled(false);
                                        Toast.makeText(Profile.this, "KUDA ACCOUNT CREATION FAILED", Toast.LENGTH_LONG).show();
                                        //retrykuda.setVisibility(View.INVISIBLE);
                                    }else if(check == 2)
                                    {
                                        retryunity.setText("RETRY UNITY ACCOUNT");
                                        retryunity.setEnabled(false);
                                        Toast.makeText(Profile.this, "KUDA ACCOUNT CREATION FAILED", Toast.LENGTH_LONG).show();
                                        //retryunity.setVisibility(View.INVISIBLE);
                                    }
                                    check = 0;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else
                {
                    server_response = readStream(urlConnection.getErrorStream());
                    android.util.Log.i(TAG, "RESPONSE: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(check == 1)
                            {
                                retrykuda.setText("RETRY KUDA ACCOUNT");
                                retrykuda.setEnabled(false);
                                Toast.makeText(Profile.this, "KUDA ACCOUNT CREATION FAILED", Toast.LENGTH_LONG).show();
                                //retrykuda.setVisibility(View.INVISIBLE);
                            }else if(check == 2)
                            {
                                retryunity.setText("RETRY UNITY ACCOUNT");
                                retryunity.setEnabled(false);
                                Toast.makeText(Profile.this, "KUDA ACCOUNT CREATION FAILED", Toast.LENGTH_LONG).show();
                                //retryunity.setVisibility(View.INVISIBLE);
                            }
                            check = 0;
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if(check == 1)
                                {
                                    retrykuda.setText("RETRY KUDA ACCOUNT");
                                    retrykuda.setEnabled(false);
                                    Toast.makeText(Profile.this, "KUDA ACCOUNT CREATION FAILED", Toast.LENGTH_LONG).show();
                                    //retrykuda.setVisibility(View.INVISIBLE);
                                }else if(check == 2)
                                {
                                    retryunity.setText("RETRY UNITY ACCOUNT");
                                    retryunity.setEnabled(false);
                                    Toast.makeText(Profile.this, "KUDA ACCOUNT CREATION FAILED", Toast.LENGTH_LONG).show();
                                    //retryunity.setVisibility(View.INVISIBLE);
                                }
                                check = 0;
                            }
                        });
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
        if(Profile.controller == 1)
        {
            Intent intent = new Intent();
            intent.setClass(Profile.this, Login.class);
            startActivity(intent);
            finish();
            Profile.controller = 0;
        }else if(Profile.controller == 2)
        {
            Intent intent = new Intent();
            intent.setClass(Profile.this, Dashboard.class);
            startActivity(intent);
            finish();
            Profile.controller = 0;
        }
    }
}