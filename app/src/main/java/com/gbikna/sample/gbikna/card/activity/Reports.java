package com.gbikna.sample.gbikna.card.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gbikna.sample.etop.More;
import com.gbikna.sample.etop.util.SignupVr;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.util.database.DATABASEHANDLER;
import com.gbikna.sample.gbikna.util.database.EOD;
import com.gbikna.sample.gbikna.util.update.DemoActivity;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.gbikna.util.utilities.Utilities;
import com.gbikna.sample.etop.Dashboard;
import com.gbikna.sample.utils.AppLog;
import com.gbikna.sample.utils.CombBitmap;
import com.gbikna.sample.utils.GenerateBitmap;
import com.horizonpay.smartpossdk.aidl.printer.AidlPrinterListener;
import com.horizonpay.smartpossdk.aidl.printer.IAidlPrinter;
import com.horizonpay.smartpossdk.data.PrinterConst;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.APPVERSION;
import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BRAND;
import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.MODEL;
import static com.gbikna.sample.gbikna.util.utilities.Utils.TCMIP;

public class Reports extends AppCompatActivity {
    private Calendar calendar;
    AutoCompleteTextView autoCompleteTextView, autoCompleteMode;
    TextInputLayout type, rptNum, daterange;
    Button val;
    private static final String TAG = Reports.class.getSimpleName();
    IAidlPrinter printer;
    int i = 0 ;
    String enddate = "", startdate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        ButterKnife.bind(this);
        try {
            printer = MyApplication.getINSTANCE().getDevice().getPrinter();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

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

        //materialDatePicker.show(getParentFragmentManager(), "DATE PICKER");

        materialDatePicker.addOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        materialDatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long,Long> selection) {
                daterange.setHint("");
                enddate = formatter.format(selection.second);
                startdate = formatter.format(selection.first);
                daterange.getEditText().setText(materialDatePicker.getHeaderText());
            }
        });



        type = (TextInputLayout) findViewById(R.id.dropdown);
        type.setHint("Options");

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteText);
        String[] option = {"RECENT RECEIPT REPRINT", "REPRINT BY NUMBER", "EOD THIS DAY",
                "EOD ALL STORED", "EOD BY DATE RANGE", "PRINT DETAILS", "DOWNLOAD LOGO", "REMOTE DOWNLOAD"};

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
        //autoCompleteTextView.setText(adapter.getItem(0).toString(), false);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showDropDown();
            }
        });


        autoCompleteMode = (AutoCompleteTextView) findViewById(R.id.autoCompleteMode);
        String[] option2 = {"ALL", "CARD", "USSD", "QR"};

        ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.option_item, option2);
        autoCompleteMode.setAdapter(adapter2);
        autoCompleteMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteMode.showDropDown();
            }
        });



        daterange = (TextInputLayout)findViewById(R.id.date_range);

        rptNum = (TextInputLayout) findViewById(R.id.inv_no);
        rptNum.setHintEnabled(false);

        val = (Button)findViewById(R.id.button_val);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Reports.this, Dashboard.class);
                startActivity(intent);
                finish();
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

        val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val.setVisibility(View.INVISIBLE);
                String action = autoCompleteTextView.getText().toString();
                if(action.equals("RECENT RECEIPT REPRINT"))
                {
                    ConfirmPin(0);
                    val.setVisibility(View.VISIBLE);
                }else if(action.equals("REPRINT BY NUMBER"))
                {
                    if(rptNum.getEditText().getText().toString().trim().length() < 1)
                    {
                        rptNum.requestFocus();
                        val.setVisibility(View.VISIBLE);
                    }else {
                        ConfirmPin(1);
                        val.setVisibility(View.VISIBLE);
                    }
                }else if(action.equals("EOD THIS DAY"))
                {
                    ConfirmPinEod("EOD THIS DAY");
                    val.setVisibility(View.VISIBLE);
                }else if(action.equals("EOD ALL STORED"))
                {
                    ConfirmPinEod("EOD ALL STORED");
                    val.setVisibility(View.VISIBLE);
                }else if(action.equals("EOD BY DATE RANGE"))
                {
                    if(startdate.length() < 8
                    || enddate.length() < 8)
                    {
                        daterange.requestFocus();
                        val.setVisibility(View.VISIBLE);
                    }else {
                        ConfirmPinEod2(autoCompleteMode.getText().toString(), "EOD BY DATE RANGE");
                        val.setVisibility(View.VISIBLE);
                    }
                }else if(action.equals("REMOTE DOWNLOAD"))
                {
                    if(ProfileParser.appversion.equals(APPVERSION) || ProfileParser.appbrand.equals("null")
                        || ProfileParser.appmodel.equals("null") || ProfileParser.appversion.equals("null"))
                    {
                        Toast.makeText(getApplicationContext(), "NO APP TO DOWNLOAD", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(Reports.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "DOWNLOADING APP", Toast.LENGTH_SHORT).show();
                        SystemClock.sleep(1000);
                        Intent intent = new Intent();
                        intent.setClass(Reports.this, DemoActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    val.setVisibility(View.VISIBLE);
                }else if(action.equals("DOWNLOAD LOGO"))
                {
                    new GetMethodDemo().execute("http://"+ TCMIP + "/tms/logodownload/download/" + ProfileParser.logobankcode
                            + "/" + ProfileParser.logoversion + "/true");
                }else if(action.equals("PRINT DETAILS"))
                {
                    printdetails pp = new printdetails(Reports.this);
                    pp.print();
                    val.setVisibility(View.VISIBLE);
                }
            }
        });

        ConfirmPin();
    }

    void ConfirmPin()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setCancelable(false);
        builder.setTitle("ENTER ADMIN PIN");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = input.getText().toString();
                Log.i(TAG, "Admin Pin is: " + ProfileParser.adminpin);
                Log.i(TAG, "Admin Pin enterred: " + password);
                if(ProfileParser.adminpin.equals(password))
                {
                    dialog.cancel();
                }else
                {
                    Toast.makeText(getApplicationContext(), "WRONG PIN...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(Reports.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "USER CANCELED...", Toast.LENGTH_SHORT).show();
                dialog.cancel();
                Intent intent = new Intent();
                intent.setClass(Reports.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }

    void getSelectedFeature(String sel) {
        String sub = sel;
        Log.i(TAG, "STRING: " + sub);

        if (sub.equals("EOD THIS DAY")) {
            EodPrint pp = new EodPrint(Reports.this);
            pp.Today();
        }else if (sub.equals("EOD ALL STORED"))
        {
            EodPrint pp = new EodPrint(Reports.this);
            pp.All(2);
        }
    }

    void getSelectedFeature2(String mode, String sel) {
        String sub = sel;
        Log.i(TAG, "STRING: " + sub);
        Log.i(TAG, "MODE: " + mode);
        Log.i(TAG, "START: " + startdate);
        Log.i(TAG, "END: " + enddate);

        EodPrint pp = new EodPrint(Reports.this);
        pp.StartDate(mode, startdate,
                    enddate);
    }

    void ConfirmPinEod2(String mode, final String value)
    {
        getSelectedFeature2(mode, value);
    }

    void ConfirmPinEod(final String value)
    {
        getSelectedFeature(value);
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
                    outputFile = new File(getApplication().getFilesDir(), "logo.bmp");
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    InputStream is = urlConnection.getInputStream();
                    byte[] buffer = new byte[4096];
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
                            val.setVisibility(View.VISIBLE);
                            val.setText("LOGO DOWNLOAD SUCCESSFUL");
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
                            intent.setClass(Reports.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }else
                {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            val.setVisibility(View.VISIBLE);
                            val.setText("LOGO DOWNLOAD FAILED");
                            SystemClock.sleep(3000);
                            Intent intent = new Intent();
                            intent.setClass(Reports.this, Dashboard.class);
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
                        val.setVisibility(View.VISIBLE);
                        val.setText("LOGO DOWNLOAD FAILEd");
                        SystemClock.sleep(3000);
                        Intent intent = new Intent();
                        intent.setClass(Reports.this, Dashboard.class);
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

            //Log.e("Response", " Length: " + server_response.length());


        }
    }


    void ConfirmPin(int cont)
    {
        if(cont == 1)
        {
            receiptNumber();
        }else
        {
            DATABASEHANDLER db = new DATABASEHANDLER(getApplicationContext());
            eod = db.getLastReceipt();
            if(eod.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "NO DATA AVAILABLE", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(Reports.this, Reports.class);
                startActivity(intent);
                finish();
            }else {
                processPrint();
            }
        }
    }


    void receiptNumber()
    {
        String rptNu = rptNum.getEditText().getText().toString().trim();
        DATABASEHANDLER db = new DATABASEHANDLER(getApplicationContext());
        eod = db.getByReceipt(rptNu);
        if(eod.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "NO DATA AVAILABLE", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(Reports.this, Dashboard.class);
            startActivity(intent);
            finish();
        }else
            processPrint();
    }

    private List<EOD> eod;

    void processPrint()
    {
        printDetail();
    }


    private void printDetail() {
        getConsumePrintResponse();
    }

    private String formatExp(String dat)
    {
        if(dat == null)
            return "**/**";
        String db = dat.substring(2, 4) + "/" + dat.substring(0, 2);
        return db;
    }

    int getSecondDot(String pay)
    {
        int i = 0, d = 0;
        for(i = 0; i < pay.length(); i++)
        {
            if(pay.charAt(i) == '.')
                d = d + 1;

            if(d == 2)
                return i;
        }
        return 0;
    }

    private String getDate(String dat)
    {
        String db = String.valueOf(Calendar.getInstance().get(Calendar.YEAR))
                + "/" + dat.substring(0, 2) + "/" + dat.substring(2, 4) + " "
                + dat.substring(4, 6) + ":" + dat.substring(6, 8) + ":"
                + dat.substring(8, 10);
        return db;
    }


    private void PrintDataOk() {
        try {
            printer.printBmp(true, false, generateBitmap(), 0, new AidlPrinterListener.Stub() {
                @Override
                public void onError(int i) throws RemoteException {
                    switch (i) {
                        case PrinterConst.RetCode.ERROR_PRINT_NOPAPER:
                            Toast.makeText(Reports.this, getString(R.string.msg_print_paper), Toast.LENGTH_LONG).show();
                            break;
                        case PrinterConst.RetCode.ERROR_DEV:
                            Toast.makeText(Reports.this, getString(R.string.msg_print_device), Toast.LENGTH_LONG).show();
                            break;
                        case PrinterConst.RetCode.ERROR_DEV_IS_BUSY:
                            Toast.makeText(Reports.this, getString(R.string.msg_print_busy), Toast.LENGTH_LONG).show();
                            break;
                        default:
                        case PrinterConst.RetCode.ERROR_OTHER:
                            Toast.makeText(Reports.this, getString(R.string.msg_print_other), Toast.LENGTH_LONG).show();
                            break;
                    }
                }

                @Override
                public void onPrintSuccess() throws RemoteException {
                    Log.i(TAG,"onPrintSuccess: ");
                    //Toast.makeText(Reports.this, getString(R.string.msg_print_succ), Toast.LENGTH_LONG).show();
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppLog.d(TAG, "bitMap  =" + image);
        return image;
    }

    private Bitmap generateBitmap() {
        CombBitmap combBitmap = new CombBitmap();
        File fi = new File(getApplicationContext().getFilesDir(), "logo.bmp");
        if (ProfileParser.rptshowlogo && fi.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(fi.getAbsolutePath());
            combBitmap.addBitmap(bitmap);
        }

        // print one line
        combBitmap.addBitmap(GenerateBitmap.generateLine(1));

        for (EOD cn : eod) {
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap(SignupVr.businessname, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap(SignupVr.businessaddress, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
            combBitmap.addBitmap(GenerateBitmap.generateLine(1));

            Log.i(TAG, "getConsumePrintResponse()");
            //Content
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("REPRINT COPY", 24, GenerateBitmap.AlignEnum.CENTER, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.bnkname, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.merchantname, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.merchantaddress, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("RECEIPT NO:", cn.getReceiptno(), 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap(cn.getTxntype(), 24, GenerateBitmap.AlignEnum.CENTER, true, false));
            combBitmap.addBitmap(GenerateBitmap.generateLine(1)); // print one line

            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TERMINAL:",  ProfileParser.tid, 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("SERIAL NUMBER:", Utilities.getSerialNumber(), 24, true, false));

            Log.i(TAG, "OLUBAYO REPRINT: " + cn.getLocaldatetime());
            String sDate1 = getDate(cn.getLocaldatetime());
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(sDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String dt = date.toString();
            String result = dt.substring(0, 10) + " " + dt.substring(30, dt.length());
            String time = dt.substring(11, 19);
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("DATE:",  result, 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TIME:",  time, 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("EXPIRY DATE:",  cn.getExpdate(), 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("PAN:",  cn.getMaskedpan(), 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("AMOUNT PAID:",  ProfileParser.curabbreviation + " " + cn.getAmount(), 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.generateLine(1)); // print one line
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.getResponseDetails(cn.getRespcode()).toUpperCase(), 24, GenerateBitmap.AlignEnum.CENTER, true, false));
            combBitmap.addBitmap(GenerateBitmap.generateLine(1)); // print one line
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("RESPONSE CODE:",  cn.getRespcode(), 24, true, false));

            String print;
            if (cn.getAuthcode() == null)
                print = "NA";
            else
                print = cn.getAuthcode();
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("AUTHCODE:",  print, 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("STAN:",  cn.getStan(), 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("RRN:",  cn.getRrn(), 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Powered By Etop", 24, GenerateBitmap.AlignEnum.CENTER, true, false));
            combBitmap.addBitmap(GenerateBitmap.generateLine(1)); // print one line

            combBitmap.addBitmap(GenerateBitmap.generateGap(60)); // print row gap

            Bitmap bp = combBitmap.getCombBitmap();

            return bp;
        }
        return null;
    }

    private void getConsumePrintResponse() {
        PrintDataOk();
    }


    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        Intent intent = new Intent();
        intent.setClass(Reports.this, More.class);
        startActivity(intent);
        finish();
    }
}