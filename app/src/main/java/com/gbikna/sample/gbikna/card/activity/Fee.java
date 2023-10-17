package com.gbikna.sample.gbikna.card.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.etop.Dashboard;

import java.text.DecimalFormat;

public class Fee extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = Fee.class.getSimpleName();

    private static final int MSG_TIME_UPDATE = 100;

    private TextView mTextAmount;
    private Button mBtnConfirm;
    private StringBuilder mAmountBuilder;
    private StringBuilder mAmount;

    private int mTime = 30;
    private String curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");



        setContentView(R.layout.activity_fee);
        if(ProfileParser.curabbreviation.equals("NGN"))
            curr = "₦";
        else if(ProfileParser.curabbreviation.equals("USD"))
            curr = "$";
        else if(ProfileParser.curabbreviation.equals("GBP"))
            curr = "£";
        else if(ProfileParser.curabbreviation.equals("RMB"))
            curr = "￥";

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Fee.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        //ActionBar actionBar = getActionBar();
        //actionBar.hide();

        mBtnConfirm = (Button) findViewById(R.id.proceed);
        mTextAmount = (TextView) findViewById(R.id.edit_amount);

        mAmountBuilder = new StringBuilder(curr);
        //New File
        //mBtnConfirm.setText(R.string.amount_input_scan);
        mHandle.sendEmptyMessage(MSG_TIME_UPDATE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIME_UPDATE:
                    if (mTime == 0) {
                        finish();
                    } else {
                        mHandle.sendEmptyMessageDelayed(MSG_TIME_UPDATE, 1000);
                    }
                    mTime--;
                    break;
                default:
                    break;
            }
        }
    };


    public static boolean parser(String rule, String amt)
    {
        int f = rule.indexOf("-", 0);
        String fR = rule.substring(0, f);

        int s = rule.indexOf("=", f);
        String sR = rule.substring(f + 1, s);

        String fee = rule.substring(s + 1);

        if(Double.parseDouble(amt) <= Double.parseDouble(sR))
        {
            ProfileParser.karrabofee = fee;
            System.out.println("CREDIT ASSIST FEE IS: " + fee);
            return true;
        }
        return false;
    }

    public void ProcessRule(String str, String amount)
    {
        String findStr = "###";
        int lastIndex = 0;
        int count = 0;
        int previous = 0;

        while(lastIndex != -1){

            lastIndex = str.indexOf(findStr,lastIndex);
            if(lastIndex == -1)
                break;
            if(parser(str.substring(previous, lastIndex), amount))
                return;

            if(lastIndex != -1)
            {
                count++;
                previous = lastIndex + 3;
                lastIndex += findStr.length();
            }
        }
    }

    public static boolean parserSA(String rule, String amt)
    {
        int f = rule.indexOf("-", 0);
        String fR = rule.substring(0, f);

        int s = rule.indexOf("=", f);
        String sR = rule.substring(f + 1, s);

        String fee = rule.substring(s + 1);

        if(Double.parseDouble(amt) <= Double.parseDouble(sR))
        {
            ProfileParser.superagentfee = fee;
            System.out.println("SUPER AGENT FEE IS: " + fee);
            return true;
        }
        return false;
    }

    public void ProcessRuleSA(String str, String amount)
    {
        String findStr = "###";
        int lastIndex = 0;
        int count = 0;
        int previous = 0;

        while(lastIndex != -1){

            lastIndex = str.indexOf(findStr,lastIndex);
            if(lastIndex == -1)
                break;
            if(parserSA(str.substring(previous, lastIndex), amount))
                return;

            if(lastIndex != -1)
            {
                count++;
                previous = lastIndex + 3;
                lastIndex += findStr.length();
            }
        }
    }

    public static boolean parserSS(String rule, String amt)
    {
        int f = rule.indexOf("-", 0);
        String fR = rule.substring(0, f);

        int s = rule.indexOf("=", f);
        String sR = rule.substring(f + 1, s);

        String fee = rule.substring(s + 1);

        if(Double.parseDouble(amt) <= Double.parseDouble(sR))
        {
            ProfileParser.aggregatorfee = fee;
            System.out.println("SUPER SUPER FEE IS: " + fee);
            return true;
        }
        return false;
    }

    public void ProcessRuleSS(String str, String amount)
    {
        String findStr = "###";
        int lastIndex = 0;
        int count = 0;
        int previous = 0;

        while(lastIndex != -1){

            lastIndex = str.indexOf(findStr,lastIndex);
            if(lastIndex == -1)
                break;
            if(parserSS(str.substring(previous, lastIndex), amount))
                return;

            if(lastIndex != -1)
            {
                count++;
                previous = lastIndex + 3;
                lastIndex += findStr.length();
            }
        }
    }

    void checkRules()
    {
        //msc + stamp + switchfee + kfee + vat + sfee + sfvat + afee;
        //msc + stamp + switchfee + kfee + vat + sfee + sfvat + afee;
        Double msc = ((Double.parseDouble(ProfileParser.msc)/ 100) * Double.parseDouble(ProfileParser.totalAmount));
        Double superpercentage = Double.parseDouble(ProfileParser.aggregatorfee);
        Double switchfee = Double.parseDouble(ProfileParser.switchfee);
        Double stamp = 0.00;
        if(Double.parseDouble(ProfileParser.totalAmount) >= 10000.00)
            stamp = Double.parseDouble(ProfileParser.stampduty);
        Double cafee = Double.parseDouble(ProfileParser.karrabofee);
        Double cafeevat = ((7.5 / 100) * Double.parseDouble(ProfileParser.karrabofee));
        Double safee = Double.parseDouble(ProfileParser.superagentfee);
        Double totalFee = msc + stamp + switchfee + cafee + cafeevat + safee + superpercentage;
        Log.i(TAG, "TOTAL FEE: " + totalFee);

        if(Double.parseDouble(ProfileParser.Fee) < totalFee)
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setCancelable(false);
            builder1.setMessage("FEE TOO SMALL. MIN FEE: " + String.valueOf(totalFee));

            builder1.setPositiveButton(
                    "RETRY",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent intent = new Intent();
                            intent.setClass(Fee.this, Fee.class);
                            startActivity(intent);
                            finish();
                        }
                    });

            builder1.setNegativeButton(
                    "CANCEL",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent intent = new Intent();
                            intent.setClass(Fee.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }else
        {
            if(Integer.parseInt(ProfileParser.txnNumber) == 2)
            {
                //Withdrawal
                Log.i(TAG, "FOR ICC, MAG SWIPE, NFC");
                Intent intent = new Intent(this, SearchCardActivity.class);
                startActivity(intent);
                finish();
            }else if(Integer.parseInt(ProfileParser.txnNumber) == 3)
            {

            }else if(Integer.parseInt(ProfileParser.txnNumber) == 4)
            {

            }
        }
    }

    void checkRulesPercentage()
    {
        Double msc = ((Double.parseDouble(ProfileParser.msc)/ 100) * Double.parseDouble(ProfileParser.totalAmount));
        Double superpercentage = ((Double.parseDouble(ProfileParser.aggregatorfee)/ 100) * Double.parseDouble(ProfileParser.totalAmount));
        Double switchfee = Double.parseDouble(ProfileParser.switchfee);
        Double stamp = 0.00;
        if(Double.parseDouble(ProfileParser.totalAmount) >= 10000.00)
            stamp = Double.parseDouble(ProfileParser.stampduty);
        Double cafee = ((Double.parseDouble(ProfileParser.karrabofee)/ 100) * Double.parseDouble(ProfileParser.totalAmount));
        Double cafeevat = ((7.5 / 100) * cafee);
        Double safee = ((Double.parseDouble(ProfileParser.superagentfee)/ 100) * Double.parseDouble(ProfileParser.totalAmount));
        Double totalFee = msc + stamp + switchfee + cafee + cafeevat + safee + superpercentage;
        Log.i(TAG, "TOTAL FEE: " + totalFee);

        if(Double.parseDouble(ProfileParser.Fee) < totalFee)
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setCancelable(false);
            builder1.setMessage("FEE TOO SMALL. MIN FEE: " + String.valueOf(totalFee));

            builder1.setPositiveButton(
                    "RETRY",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent intent = new Intent();
                            intent.setClass(Fee.this, Fee.class);
                            startActivity(intent);
                            finish();
                        }
                    });

            builder1.setNegativeButton(
                    "CANCEL",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent intent = new Intent();
                            intent.setClass(Fee.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }else
        {
            if(Integer.parseInt(ProfileParser.txnNumber) == 2)
            {
                //Withdrawal
                Log.i(TAG, "FOR ICC, MAG SWIPE, NFC");
                Intent intent = new Intent(this, SearchCardActivity.class);
                startActivity(intent);
                finish();
            }else if(Integer.parseInt(ProfileParser.txnNumber) == 3)
            {

            }else if(Integer.parseInt(ProfileParser.txnNumber) == 4)
            {

            }
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        Intent intent = new Intent();
        intent.setClass(Fee.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    void checkforMax()
    {
        if(Double.parseDouble(ProfileParser.totalAmount) > Double.parseDouble(ProfileParser.maxamount))
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setCancelable(false);
            builder1.setMessage("MAXIMUM AMOUNT EXCEEDED. MAXIMUM AMOUNT: " + ProfileParser.maxamount);

            builder1.setPositiveButton(
                    "DASHBOARD",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent intent = new Intent();
                            intent.setClass(Fee.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    });

            builder1.setNegativeButton(
                    "CANCEL",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent intent = new Intent();
                            intent.setClass(Fee.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }else
        {
            if(Integer.parseInt(ProfileParser.txnNumber) == 2)
            {
                //Withdrawal
                //ProcessRule(ProfileParser.karrabofeerule, ProfileParser.totalAmount);
                //ProcessRuleSA(ProfileParser.superagentfeerule, ProfileParser.totalAmount);
                ProcessRuleSS(ProfileParser.aggrewithdrawalfee, ProfileParser.totalAmount);
                if(ProfileParser.percentagerule.equals("true"))
                {
                    checkRulesPercentage();
                }else
                {
                    checkRules();
                }
            }else if(Integer.parseInt(ProfileParser.txnNumber) == 3)
            {
                //Cash In
                //ProcessRule(ProfileParser.ktransferrule, ProfileParser.totalAmount);
                //ProcessRuleSA(ProfileParser.satfeerule, ProfileParser.totalAmount);
                ProcessRuleSS(ProfileParser.aggretransferfee, ProfileParser.totalAmount);
                if(ProfileParser.percentagerule.equals("true"))
                {
                    checkRulesPercentage();
                }else
                {
                    checkRules();
                }
            }else if(Integer.parseInt(ProfileParser.txnNumber) == 4)
            {
                //Transfer
                //ProcessRule(ProfileParser.ktransferrule, ProfileParser.totalAmount);
                //ProcessRuleSA(ProfileParser.satfeerule, ProfileParser.totalAmount);
                ProcessRuleSS(ProfileParser.aggretransferfee, ProfileParser.totalAmount);
                if(ProfileParser.percentagerule.equals("true"))
                {
                    checkRulesPercentage();
                }else
                {
                    checkRules();
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_0:
                setText("0");
                break;
            case R.id.btn_1:
                setText("1");
                break;
            case R.id.btn_2:
                setText("2");
                break;
            case R.id.btn_3:
                setText("3");
                break;
            case R.id.btn_4:
                setText("4");
                break;
            case R.id.btn_5:
                setText("5");
                break;
            case R.id.btn_6:
                setText("6");
                break;
            case R.id.btn_7:
                setText("7");
                break;
            case R.id.btn_8:
                setText("8");
                break;
            case R.id.btn_9:
                setText("9");
                break;
            case R.id.btn_back:
                if (mAmountBuilder.length() > 1) {
                    mAmountBuilder.delete(mAmountBuilder.length() - 1, mAmountBuilder.length());
                }
                setText(null);
                break;
            case R.id.btn_clear:
                mAmountBuilder.delete(1, mAmountBuilder.length());
                setText(null);
                break;
            case R.id.proceed:
                Log.i(TAG, "GOING TO GET CARD");
                ProfileParser.Fee = mAmount.substring(1);
                DecimalFormat df2 = new DecimalFormat("#.##");
                Double amt = Double.parseDouble(ProfileParser.Amount);
                Double fee = Double.parseDouble(ProfileParser.Fee);
                Double tt = amt + fee;
                Log.i(TAG, "GOING TO GET CARD 2: " + tt);
                ProfileParser.totalAmount = String.valueOf(tt);
                checkforMax();
            default:
                break;
        }
    }

    private String printInAmountFormat(String number)
    {
        double amount = Double.parseDouble(number.substring(1));
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String fin = "";
        Log.i(TAG, "OLUBAYO LENGTH: " + formatter.format(amount).length());
        if(formatter.format(amount).length() < 4)
            fin = ProfileParser.curabbreviation + " 0" + formatter.format(amount);
        else
            fin = ProfileParser.curabbreviation + " " + formatter.format(amount);
        return fin;
    }

    private void setText(String charNum) {
        String temp = mAmountBuilder.toString();
        Log.i(TAG, "temp = "+temp);

        if (temp.length() > 12) {
            return;
        }

        if (charNum != null) {
            mAmountBuilder.append(charNum);
        }

        temp = mAmountBuilder.toString();
        Log.i(TAG, "temp = "+temp);

        if (temp.equals(curr + "0")) {
            temp = curr;
            mAmountBuilder.delete(1, 2);
        }
        mAmount = new StringBuilder(temp);
        Log.i(TAG, "mAmount before = "+mAmount);
        for (int i = 0 ; i < 4 - mAmountBuilder.length(); i++) {
            mAmount.insert(1, "0");
        }
        Log.i(TAG, "mAmount = "+mAmount);
        mAmount.insert(mAmount.length()-2, ".");
        mTextAmount.setText(printInAmountFormat(mAmount.toString()));
        //mTextAmount.setText(mAmount);

        temp = temp.substring(1);
        Log.i(TAG, "temp.isEmpty() = "+temp.isEmpty());
        if (temp.isEmpty() || Long.parseLong(temp) == 0) {
            Log.i(TAG, "false");
            mBtnConfirm.setEnabled(false);
        } else {
            Log.i(TAG, "true");
            mBtnConfirm.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        mHandle.removeMessages(MSG_TIME_UPDATE);
    }
}