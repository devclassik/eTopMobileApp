package com.gbikna.sample.gbikna.util.update;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.gbikna.util.utilities.Utilities;
import com.gbikna.sample.etop.Dashboard;
import com.horizonpay.smartpossdk.aidl.sys.IAidlSys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.ButterKnife;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.APPVERSION;
import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BRAND;
import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.MODEL;


public class DemoActivity extends Activity {
    public static final String TOPWISE_SERVICE_ACTION = "topwise_cloudpos_device_service";
    private static final String TAG = "DemoActivity";
    //private AidlSystem systemInf = null;

    private TextView disp;
    private TextView sync;

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    String[] permissions= new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.remoteupdate);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //ActionBar actionBar = getActionBar();
        //actionBar.hide();
        disp = (TextView) findViewById(R.id.updateauto);
        sync = (TextView) findViewById(R.id.syncdata);

        new GetMethodDemo().execute("http://143.42.102.14:8001/" + "/tms/appdownload/download/" + ProfileParser.appbrand
                + "/" + ProfileParser.appmodel + "/" + ProfileParser.appversion);

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        Intent intent = new Intent();
        intent.setClass(DemoActivity.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public class GetMethodDemo extends AsyncTask<String , Void ,String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("brand", BRAND);
                urlConnection.setRequestProperty("model", MODEL);
                urlConnection.setRequestProperty("serial", Utilities.getSerialNumber());
                urlConnection.setRequestProperty("appversion", APPVERSION);
                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    //server_response = readStream(urlConnection.getInputStream());
                    File outputFile;
                    outputFile = new File(getApplication().getFilesDir(), "app-release.apk");
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    InputStream is = urlConnection.getInputStream();
                    byte[] buffer = new byte[20 * 1024];
                    int len1 = 0;
                    while ((len1 = is.read(buffer)) != -1)
                    {
                        fos.write(buffer, 0, len1);
                    }
                    fos.close();
                    is.close();

                    //InputStream input = urlConnection.getInputStream();
                    //Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    //Log.i(TAG, "Length: " + server_response.length());
                    //Log.i(TAG, "Response: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            sync.setText("APP DOWNLOAD SUCCESSFUL");

                            /*try {
                                String path = Environment.getExternalStorageDirectory().toString() + "/app-release.apk";
                                boolean isSuccess = ((DemoApplication) getApplication()).getDeviceServiceEngine().installApp(path);
                                Toast.makeText(getApplicationContext(), "UPDATE STATUS: " + isSuccess, Toast.LENGTH_LONG).show();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "REMOTE UPDATE FAILED", Toast.LENGTH_LONG).show();
                            }*/

                            try {
                                IAidlSys system = MyApplication.getINSTANCE().getDevice().getSysHandler();
                                String path = Environment.getExternalStorageDirectory().toString() + "/app-release.apk";
                                final String ret = system.installApp(path);
                                System.out.println("install->" + ret);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (ret == null) {
                                            Log.i(TAG, "install App: Successful");
                                            boolean ret = false;
                                            try {
                                                ret = system.reboot();
                                                Log.i(TAG, "Reboot: " + ret);
                                            } catch (RemoteException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.i(TAG, "install App: " + ret);
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    //Log.i(TAG, "LENGTH OF RECIEVE: " + server_response.length());
                    //Write to file
                    Log.i(TAG, "ABOUT TO SAVE");
                    Log.i(TAG, "DONE SAVING");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            SystemClock.sleep(3000);
                            Intent intent = new Intent();
                            intent.setClass(DemoActivity.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }else
                {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            sync.setText("LOGO DOWNLOAD FAILED");
                            SystemClock.sleep(3000);
                            Intent intent = new Intent();
                            intent.setClass(DemoActivity.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        sync.setText("LOGO DOWNLOAD FAILEd");
                        SystemClock.sleep(3000);
                        Intent intent = new Intent();
                        intent.setClass(DemoActivity.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}

