package com.gbikna.sample.gbikna.card.activity;

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
import com.gbikna.sample.etop.Dashboard;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.etop.Transfer;

import java.text.DecimalFormat;

public class AmountInputActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = AmountInputActivity.class.getSimpleName();

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

        setContentView(R.layout.activity_amount_input);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AmountInputActivity.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        Log.i(TAG, "WISDOM NUMBER 3: " + ProfileParser.txnNumber);
        if(Integer.parseInt(ProfileParser.txnNumber) == 5)
        {
            Log.i(TAG, "This is Balance Enquiry");
            Intent intent = new Intent(this, SearchCardActivity.class);
            startActivity(intent);
            finish();
        }


        if(ProfileParser.curabbreviation.equals("NGN"))
            curr = "₦";
        else if(ProfileParser.curabbreviation.equals("USD"))
            curr = "$";
        else if(ProfileParser.curabbreviation.equals("GBP"))
            curr = "£";
        else if(ProfileParser.curabbreviation.equals("RMB"))
            curr = "￥";

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

    private final Handler mHandle = new Handler() {
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
                //Check based on transaction types here
                ProfileParser.Amount = mAmount.substring(1);
                ProfileParser.Fee = "0.00";
                DecimalFormat f = new DecimalFormat("##.00");
                //DecimalFormat df2 = new DecimalFormat("#.##");
                Double amt = Double.parseDouble(ProfileParser.Amount);
                Double fee = Double.parseDouble(ProfileParser.Fee);
                Double tt = amt + fee;
                ProfileParser.totalAmount = f.format(tt);
                Log.i(TAG, "GOING TO GET CARD");
                Log.i(TAG, "FOR ICC, MAG SWIPE, NFC");
                Log.i(TAG, "AMOUNT: " + ProfileParser.Amount);
                Log.i(TAG, "FEE: " + ProfileParser.Fee);
                Log.i(TAG, "TOTAL: " + ProfileParser.totalAmount);
                if(ProfileParser.txnNumber.equals("3"))
                {
                    //transfer
                    Intent intent = new Intent(this, Transfer.class);
                    startActivity(intent);
                    finish();
                }else
                {
                    Intent intent = new Intent(this, SearchCardActivity.class);
                    startActivity(intent);
                    finish();
                }
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

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        Intent intent = new Intent();
        intent.setClass(AmountInputActivity.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}