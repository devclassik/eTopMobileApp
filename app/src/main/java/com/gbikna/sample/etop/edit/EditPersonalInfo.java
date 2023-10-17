package com.gbikna.sample.etop.edit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.gbikna.util.utilities.Utilities;
import com.gbikna.sample.etop.Dashboard;
import com.gbikna.sample.etop.MainActivity;
import com.gbikna.sample.etop.util.SignupVr;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;

public class EditPersonalInfo extends AppCompatActivity {

    private static final String TAG = EditPersonalInfo.class.getSimpleName();

    Button button;
    private TextInputLayout firstname, middlename, lastname, username, nin, dob;
    AutoCompleteTextView gender, usertype;

    public static int personalControl = 0;

    public String dateofbirth = "";
    ImageView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EditPersonalInfo.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        imageview = (ImageView)findViewById(R.id.qrcode1);
        java.io.File newfile = new File(getApplicationContext().getFilesDir(), "imagea.jpg");
        Bitmap myBitmap = BitmapFactory.decodeFile(newfile.getAbsolutePath());
        imageview.setImageBitmap(myBitmap);


        button = (Button) findViewById(R.id.pro_pers);
        firstname = (TextInputLayout)findViewById(R.id.firstname);
        middlename = (TextInputLayout)findViewById(R.id.middlename);
        lastname = (TextInputLayout)findViewById(R.id.lastname);
        username = (TextInputLayout)findViewById(R.id.username);
        nin = (TextInputLayout)findViewById(R.id.nin);
        dob = (TextInputLayout)findViewById(R.id.dob);

        usertype = (AutoCompleteTextView)findViewById(R.id.usertype);
        String[] option3 = {"MERCHANT", "AGENT"};
        ArrayAdapter adapter3 = new ArrayAdapter(this, R.layout.option_item, option3);
        usertype.setAdapter(adapter3);
        usertype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usertype.showDropDown();
            }
        });

        gender = (AutoCompleteTextView) findViewById(R.id.gender);
        String[] option = {"MALE", "FEMALE"};
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
        gender.setAdapter(adapter);
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender.showDropDown();
            }
        });



        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.ThemeOverlay_App_MaterialCalendar);
        materialDateBuilder.setTitleText("SELECT DOB");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Log.i("PERSONALINFORMATION", materialDatePicker.getHeaderText());
                        dob.getEditText().setText(materialDatePicker.getHeaderText());
                        dateofbirth = materialDatePicker.getHeaderText();
                    }
                });

        dob.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("PERSONALINFORMATION", "DATE OF BIRTH ENTERED");
                materialDatePicker.show(getSupportFragmentManager(), "DATE PICKER");
            }
        });

        dob.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Proceed();
            }
        });

        firstname.getEditText().setText(SignupVr.firstname);
        middlename.getEditText().setText(SignupVr.middlename);
        lastname.getEditText().setText(SignupVr.lastname);
        username.getEditText().setText(SignupVr.username);
        nin.getEditText().setText(SignupVr.nin);
        dob.getEditText().setText(SignupVr.dob);
        gender.setText(SignupVr.gender);
        usertype.setText(SignupVr.usertype);
        username.setEnabled(false);

        if(SignupVr.approved.isEmpty() || !SignupVr.approved.equals("true"))
        {
            button.setVisibility(View.VISIBLE);
        }else
        {
            firstname.setEnabled(false);
            middlename.setEnabled(false);
            lastname.setEnabled(false);
            username.setEnabled(false);
            nin.setEnabled(false);
            dob.setEnabled(false);
            gender.setEnabled(false);
            usertype.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
        }
    }

    public void Proceed()
    {
        String sn = Utilities.getSerialNumber();
        String fname = firstname.getEditText().getText().toString().trim();
        String mname = middlename.getEditText().getText().toString().trim();
        String lname = lastname.getEditText().getText().toString().trim();
        String fulname = lname + ", " + fname + " " + mname;
        String usrname = username.getEditText().getText().toString().trim();
        String usrtype = usertype.getText().toString();
        String phone = SignupVr.businessphone;
        String dofbirth = dateofbirth;
        String action = gender.getText().toString();
        String sex = "";
        if(action.equals("MALE"))
        {
            sex = "M";
        }else if(action.equals("FEMALE"))
        {
            sex = "F";
        }
        String nn = nin.getEditText().getText().toString().trim();
        String housenumber = "NA";
        String streetname = "NA";

        if(sn.isEmpty() || fname.isEmpty() || mname.isEmpty() || lname.isEmpty()
        || fulname.isEmpty() || usrname.isEmpty() || usrtype.isEmpty() ||
        phone.isEmpty() || dofbirth.isEmpty() || sex.isEmpty() || nn.isEmpty())
        {
            Toast.makeText(EditPersonalInfo.this, "PLEASE COMPLETE THE FORM", Toast.LENGTH_LONG).show();
        }else {
            button.setEnabled(false);
            button.setText("PROCESSING");

            JSONObject obj = new JSONObject();
            try {
                obj.put("firstname", fname);
                obj.put("middlename", mname);
                obj.put("lastname", lname);
                obj.put("fullname", fulname);
                obj.put("username", usrname);
                obj.put("phonenumber", phone);
                obj.put("gender", sex);
                obj.put("nin", nn);
                obj.put("dob", dofbirth);
                obj.put("housenumber", housenumber);
                obj.put("streetname", streetname);
                obj.put("usertype", usrtype);
                obj.put("serialnumber", Utilities.getSerialNumber());
                Log.i("UPDATE", "REQUEST: " + obj.toString());
                new updatePersonalInfo().execute(BASEURL + "/apis/etop/signup/personal", obj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(EditPersonalInfo.this, "PERSONAL UPDATE FAILED", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class updatePersonalInfo extends AsyncTask<String, Void, String> {
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
                urlConnection.setRequestMethod("PUT");
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

                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(server_response);
                                if(obj.has("status") && obj.getInt("status") == 200)
                                {
                                    button.setText("SUCCESS");
                                    SystemClock.sleep(3000);

                                    Intent intent = new Intent();
                                    intent.setClass(EditPersonalInfo.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else
                                {
                                    button.setText("RETRY LATER");
                                    button.setEnabled(true);
                                    SystemClock.sleep(3000);

                                    Toast.makeText(EditPersonalInfo.this, "PERSONAL UPDATE FAILED", Toast.LENGTH_LONG).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(EditPersonalInfo.this, "PERSONAL UPDATE FAILED", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "NOT SUCCESSFUL: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            button.setText("RETRY LATER");
                            button.setEnabled(true);
                            SystemClock.sleep(3000);
                            Toast.makeText(EditPersonalInfo.this, "PERSONAL UPDATE FAILED", Toast.LENGTH_LONG).show();
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
        intent.setClass(EditPersonalInfo.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}