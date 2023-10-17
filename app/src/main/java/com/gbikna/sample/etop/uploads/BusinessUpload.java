package com.gbikna.sample.etop.uploads;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gbikna.sample.BuildConfig;
import com.gbikna.sample.R;
import com.gbikna.sample.etop.Login;
import com.gbikna.sample.etop.util.SignupVr;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.gbikna.sample.gbikna.util.update.DemoActivity.MULTIPLE_PERMISSIONS;

public class BusinessUpload extends AppCompatActivity {

    Button button;
    private static final String TAG = BusinessUpload.class.getSimpleName();
    private TextView txtimage2;
    private ImageView imageviewb;
    static String dira = "";
    static String dirb = "";

    public static int choose = 0;


    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;

    String[] permissions= new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_upload);

        checkPermissions();

        button = (Button) findViewById(R.id.upload);
        txtimage2 = (TextView)findViewById(R.id.txtimage2);
        imageviewb = (ImageView)findViewById(R.id.imageviewb);

        // Here, we are making a folder named picFolder to store
        // pics taken by the camera using this application.
        java.io.File file = new java.io.File(getApplicationContext().getFilesDir(),"imageb.jpg");
        if(file.exists())
        {
            file.delete();
        }



        txtimage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose = 2;
                count = 0;
                dirb = "imageb.jpg";
                ProcessPictures(dirb);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.io.File file2 = new java.io.File(getApplicationContext().getFilesDir(),"imageb.jpg");

                if(file2.exists())
                {
                    button.setText("Please Wait");
                    button.setEnabled(false);
                    new PostBusiness().execute("http://143.42.102.14:8001/tms/upgrade/signup/second");
                }else
                {
                    Toast.makeText(BusinessUpload.this, "Please Upload Image", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public class PostBusiness extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {

            Log.i(TAG, "IN HERE: ");

            try {
                Log.i(TAG, "URL: " + strings[0]);
                java.io.File newfile1 = new File(getApplicationContext().getFilesDir(), "imageb.jpg");

                HttpURLConnection connection = null;
                DataOutputStream outputStream = null;

                String pathToOurFile = newfile1.getAbsolutePath();

                String urlServer = strings[0];
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";

                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;

                FileInputStream fileInputStream = new FileInputStream(new File(
                        pathToOurFile));

                URL url = new URL(urlServer);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("username", SignupVr.username);
                connection.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);

                outputStream = new DataOutputStream(connection.getOutputStream());



                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"uploada\";filename=\""
                        + pathToOurFile + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                        + lineEnd);

                int responseCode = connection.getResponseCode();
                Log.i(TAG, "RESPONSE CODE: " + responseCode);
                // get response
                InputStream responseStream = null;
                if (responseCode == HttpURLConnection.HTTP_OK)
                    responseStream = new BufferedInputStream(connection.getInputStream());
                else
                    responseStream = new BufferedInputStream(connection.getErrorStream());
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
                                button.setEnabled(true);
                                button.setText("SUCCESS");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "NOT SUCCESSFUL: " + connection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "LOGIN NOT SUCCESSFUL");

                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(server_response);
                                button.setEnabled(true);
                                button.setText("UPLOAD FAILED");
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

    void ProcessPictures(String filename)
    {
        count++;
        String file = filename;
        java.io.File newfile = new File(getApplicationContext().getFilesDir(), file);
        try {

            // check for camera permission
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED)
            {
                newfile.createNewFile();
                Uri outputFileUri = FileProvider.getUriForFile(BusinessUpload.this, BuildConfig.APPLICATION_ID + ".provider", newfile);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }else
            {
                checkPermissions();
            }
        }
        catch (IOException e)
        {
            Log.i(TAG, "ERROR OCCURRED: " + e.getMessage());
        }
    }

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            Log.i(TAG, "RESPONSE: " + result);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS );
            Log.i(TAG, "WISDOM: " + "Returning False");
            return false;
        }
        Log.i(TAG, "WISDOM: " + "Returning True");
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "WISDOM: 1. " + requestCode);
        Log.i(TAG, "WISDOM: 2. " + permissionsList);
        Log.i(TAG, "WISDOM: 3. " + grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                            permissionsDenied += "\n" + per;
                        }
                    }
                    Log.i(TAG, "WISDOM: " + "Finally");
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.i(TAG, "PIC HAS BEEN SAVED");
            java.io.File newfile = new File(getApplicationContext().getFilesDir(), "imageb.jpg");
            Bitmap myBitmap = BitmapFactory.decodeFile(newfile.getAbsolutePath());
            imageviewb.setImageBitmap(myBitmap);
            choose = 0;
        }else
        {
            Log.i(TAG, "PIC HAS NOT BEEN SAVED");
            Log.i(TAG, data.toString());
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle data = new Bundle();
        intent.setClass(BusinessUpload.this, Login.class);
        intent.putExtras(data);
        startActivity(intent);
        finish();
    }


}