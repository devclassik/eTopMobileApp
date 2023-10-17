package com.gbikna.sample.etop;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.gbikna.sample.R;

public class Purchase extends AppCompatActivity {
    AutoCompleteTextView autoCompleteTextView;
    TextInputLayout type, amount;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        type = (TextInputLayout) findViewById(R.id.dropdown);
        amount = (TextInputLayout) findViewById(R.id.amount);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteText);
        String[] option = {"Savings Account", "Current Account"};
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
//        autoCompleteTextView.setText(adapter.getItem(0).toString(), false);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                autoCompleteTextView.showDropDown();

            }

        });
    }
}