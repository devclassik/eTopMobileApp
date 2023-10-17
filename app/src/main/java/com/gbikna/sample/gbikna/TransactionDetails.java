package com.gbikna.sample.gbikna;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.card.print.PrintData;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.gbikna.util.utilities.Utilities;
import com.gbikna.sample.etop.util.SignupVr;

public class TransactionDetails extends AppCompatActivity {
    private static final String TAG = TransactionDetails.class.getSimpleName();
    TextView reference, status, transType, amount, fee, total, response, meaning, pan;
    TextView hfee, hresponse, hmeaning, hpan;
    LinearLayout statusHolder;
    Button proceed;

    private PrintData mPrint;
    private AlertDialog.Builder mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String hostresponse = b.getString("response");
        //Just in case it is needed tomorrow

        if(ProfileParser.receiving[39] == null)
        {
            ProfileParser.receiving[39] = "06";
        }

        mPrint = new PrintData(this);
        PrintMore("CUSTOMER COPY");

        status = findViewById(R.id.status);
        reference = findViewById(R.id.refrence);
        transType = findViewById(R.id.trans_type);
        amount = findViewById(R.id.amount);
        //TextView hfee, htotal, hresponse, hmeaning, hpan;
        fee = findViewById(R.id.fee);
        hfee = findViewById(R.id.hfee);

        total = findViewById(R.id.total);

        response = findViewById(R.id.responsecode);
        hresponse = findViewById(R.id.hresponsecode);

        meaning = findViewById(R.id.meaning);
        hmeaning = findViewById(R.id.hmeaning);

        pan = findViewById(R.id.pan);
        hpan = findViewById(R.id.hpan);

        statusHolder = findViewById(R.id.status_holder);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TransactionDetails.this, com.gbikna.sample.etop.Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        if(ProfileParser.txnNumber.equals("1") || ProfileParser.txnNumber.equals("2"))
        {
            reference.setText(ProfileParser.sending[11]);
            status.setText(ProfileParser.getResponseDetails(ProfileParser.receiving[39]));
            transType.setText(ProfileParser.txnName);
            amount.setText(ProfileParser.Amount);
            fee.setText("0.00");
            total.setText(ProfileParser.totalAmount);
            response.setText(ProfileParser.receiving[39]);
            meaning.setText(ProfileParser.getResponseDetails(ProfileParser.receiving[39]));
            pan.setText(Utilities.maskedPan(ProfileParser.field2));
        }else if(ProfileParser.txnNumber.equals("3"))
        {
            //transfer
            reference.setText(ProfileParser.sending[11]);
            status.setText(ProfileParser.getResponseDetails(ProfileParser.receiving[39]));
            transType.setText(ProfileParser.txnName);
            amount.setText(ProfileParser.Amount);
            hfee.setText("BANK");
            fee.setText(ProfileParser.accountbank);
            total.setText(ProfileParser.totalAmount);
            hresponse.setText("BEN:");
            response.setText(ProfileParser.receivername);

            meaning.setText(ProfileParser.sending[37]);

            hpan.setText("ACT NUMBER");
            pan.setText(ProfileParser.destination);
        }else
        {
            //bills
            reference.setText(ProfileParser.sending[11]);
            status.setText(ProfileParser.getResponseDetails(ProfileParser.receiving[39]));
            transType.setText(ProfileParser.txnName);
            amount.setText(ProfileParser.Amount);
            total.setText(ProfileParser.totalAmount);

            hfee.setText("NAME");
            fee.setText("BILLS PAYMENT");
            hresponse.setText("ID:");
            response.setText(SignupVr.walletid);

            meaning.setText(ProfileParser.sending[37]);
            hpan.setText("DESTINATION");
            pan.setText(ProfileParser.destination);
        }




        proceed = (Button) findViewById(R.id.proceed_print);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "CLICKING PRINTING.....");
                //Print here
                //Toast.makeText(getApplicationContext(), "PRINTING...", Toast.LENGTH_SHORT).show();
                PrintMore("MERCHANT COPY");
                proceed.setVisibility(View.INVISIBLE);
                proceed.setEnabled(false);
            }
        });

    }

    void PrintMore(String header)
    {
        mPrint.printDetail(header);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(TransactionDetails.this, com.gbikna.sample.etop.Dashboard.class);
        startActivity(intent);
        finish();
    }

}