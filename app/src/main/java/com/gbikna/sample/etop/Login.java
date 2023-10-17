package com.gbikna.sample.etop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.gbikna.sample.R;
import com.gbikna.sample.activity.MainActivity;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.gbikna.util.utilities.Utilities;

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
import java.util.concurrent.Executor;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BASEURL;

public class Login extends AppCompatActivity {
    private static final String TAG = Login.class.getSimpleName();

    private TextInputLayout email;
    private TextInputLayout password;
    private EditText passwordShow;
    private Button pro;
    private ImageView bio_auth;
    private TextView signup, recovery, resfield;
    private Executor executor;
    private androidx.biometric.BiometricPrompt biometricPrompt;
    private androidx.biometric.BiometricPrompt.PromptInfo promptInfo;
    private BiometricManager biometricManager;

    static int ct = 0;
    String sEmail = "";
    String sPassword;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ProfileParser.emailaddress = "";
        ProfileParser.password = "";
        ProfileParser.token = "";

        email = (TextInputLayout)findViewById(R.id.input_email);
        password = (TextInputLayout)findViewById(R.id.input_password);
        pro = (Button) findViewById(R.id.btn_login);
        signup = (TextView)findViewById(R.id.link_signup);
        recovery = (TextView)findViewById(R.id.recovery);
        resfield = (TextView)findViewById(R.id.login_resp);
        passwordShow = (EditText) findViewById(R.id.password_show);
        bio_auth = (ImageView) findViewById(R.id.bio_auth);

        biometricManager = BiometricManager.from(this);

        if(biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            bio_auth.setVisibility(View.VISIBLE);
        } else {
            bio_auth.setVisibility(View.GONE);
        }

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(Login.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(Login.this, "Error"+errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                password.getEditText().setText(sPassword);
                bio_auth.setVisibility(View.GONE);
                pro.setText("Please wait...");
                pro.setEnabled(false);
                ct += 1;
                login();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(Login.this, "Login Failed.", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login with biometrics")
                .setNegativeButtonText("Cancel")
                .setConfirmationRequired(true)
                .build();

        SharedPreferences sharedPreferences = getSharedPreferences(Storage.SHARED_PREFS, MODE_PRIVATE);
        sEmail = sharedPreferences.getString(Storage.EMAIL, "");
        sPassword = sharedPreferences.getString(Storage.PASSWORD, "");

        email.getEditText().setText(sEmail);

        if(sEmail.isEmpty() || sPassword.isEmpty()) {
            bio_auth.setVisibility(View.GONE);
        }

        bio_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ? "Unavailable" : biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ? "no hadware" : biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ? "not enrollec" : "Yeah..");
                biometricPrompt.authenticate(promptInfo);
            }
        });

        passwordShow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getX() >= (passwordShow.getRight() - passwordShow.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        switch ( event.getAction() ) {

                            case MotionEvent.ACTION_UP:
                                passwordShow.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                break;

                            case MotionEvent.ACTION_DOWN:
                                passwordShow.setInputType(InputType.TYPE_CLASS_TEXT);
                                break;

                        }

                        return true;
                    }
                }
                return false;
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CheckSerial().execute(BASEURL + "/apis/etop/records/getbyserial/" + Utilities.getSerialNumber() + "/");
            }
        });

        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Login.this, RecoverPassword.class);
                startActivity(intent);
                finish();
            }
        });

        pro.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                pro.setText("Please wait...");
                pro.setEnabled(false);
                resfield.setText("");
                ct += 1;
                ProfileParser.emailaddress = sEmail;
                ProfileParser.password = sPassword;

                login();
            }
        });
    }

    public class CheckSerial extends AsyncTask<String , Void ,String> {
        String server_response;
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                android.util.Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setRequestProperty("Authorization", "Bearer " + ProfileParser.token);
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
                                if(arr.length() < 1)
                                {
                                    Intent intent = new Intent();
                                    intent.setClass(Login.this, Terms.class);
                                    startActivity(intent);
                                    finish();
                                }else
                                {
                                    signup.setText("SN Taken");
                                    Toast.makeText(getApplicationContext(), "TERMINAL TAKEN ALREADY", Toast.LENGTH_LONG);
                                }
                            }catch (Exception e)
                            {
                                Log.i(TAG, e.getMessage());
                                signup.setText("SN Taken");
                                Toast.makeText(getApplicationContext(), "ERROR OCCURRED. TRY LATER", Toast.LENGTH_LONG);
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
                            signup.setText("SN Taken");
                            Toast.makeText(getApplicationContext(), "ERROR OCCURRED. TRY LATER", Toast.LENGTH_LONG);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        signup.setText("SN Taken");
                        Toast.makeText(getApplicationContext(), "ERROR OCCURRED. TRY LATER", Toast.LENGTH_LONG);
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

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login Failed", Toast.LENGTH_LONG).show();
        pro.setText("LOGIN");
        pro.setEnabled(true);
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        JSONObject send = new JSONObject();
        try {
            send.put("username", ProfileParser.emailaddress);
            send.put("password", ProfileParser.password);
            Log.i(TAG, "REQUEST: " + send.toString());
            new PostCallhome().execute(BASEURL + "/authenticate", send.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean validate() {
        boolean valid = true;

        ProfileParser.emailaddress = email.getEditText().getText().toString().trim();
        ProfileParser.password = password.getEditText().getText().toString().trim();

        if (ProfileParser.emailaddress.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(ProfileParser.emailaddress).matches()) {
            email.setError("Enter a Valid Email");
            valid = false;
        } else {
            email.setError(null);
        }

        if (ProfileParser.password.isEmpty()) {
            password.setError("Password is Empty");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public class PostCallhome extends AsyncTask<String, Void, String> {
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
                            Log.i(TAG, "LOGIN SUCCESSFUL");

                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(server_response);
                                ProfileParser.token = obj.getString("token");
                                SharedPreferences sharedPreferences = getSharedPreferences(Storage.SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Storage.EMAIL, ProfileParser.emailaddress);
                                editor.putString(Storage.PASSWORD, ProfileParser.password);
                                editor.apply();

                                Intent intent = new Intent();
                                intent.setClass(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
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
                            Log.i(TAG, "LOGIN NOT SUCCESSFUL");
                            pro.setText("LOGIN");
                            pro.setEnabled(true);
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(server_response);
                                resfield.setText(obj.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

    private void viewResponse(String input)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setCancelable(false);
        builder1.setMessage(input);

        builder1.setPositiveButton(
                "LOGIN FAILED",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent();
                        intent.setClass(Login.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        dialog.cancel();
                        Intent intent = new Intent();
                        intent.setClass(Login.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}