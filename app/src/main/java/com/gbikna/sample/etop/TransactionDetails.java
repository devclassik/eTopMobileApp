package com.gbikna.sample.etop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gbikna.sample.R;

public class TransactionDetails extends AppCompatActivity {
    TextView status, reference, transType, amount;
    LinearLayout statusHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        status = findViewById(R.id.status);
        reference = findViewById(R.id.refrence);
        transType = findViewById(R.id.trans_type);
        amount = findViewById(R.id.amount);
        statusHolder = findViewById(R.id.status_holder);
    }
}