package com.gbikna.sample.etop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gbikna.sample.R;

import java.text.DecimalFormat;

public class AmountInput extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = AmountInput.class.getSimpleName();
    private TextView mTextAmount;
    private Button mBtnConfirm;
    private StringBuilder mAmountBuilder;
    private StringBuilder mAmount;
    private String curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_input);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnConfirm = (Button) findViewById(R.id.proceed);
        mTextAmount = (TextView) findViewById(R.id.edit_amount);
        mAmountBuilder = new StringBuilder(3);

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
                finish();
                break;
            default:
                break;
        }
    }

    private String printInAmountFormat(String number)
    {
        double amount = Double.parseDouble(number.substring(1));
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String fin = "";
        if(formatter.format(amount).length() < 4)
            fin = " 0" + formatter.format(amount);
        else
            fin = " " + formatter.format(amount);
        return fin;
    }
}