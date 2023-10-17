package com.gbikna.sample.gbikna.card.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gbikna.sample.R;
import com.gbikna.sample.etop.Dashboard;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AccountType extends AppCompatActivity {
    private static final String TAG = AccountType.class.getSimpleName();

    ListView listView;
    Button cancel;
    List<String> LISTSTRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);
        String[] typ = new String[4];
        typ[0] = "1. DEFAULT ACCOUNT";
        typ[1] = "2. SAVINGS ACCOUNT";
        typ[2] = "3. CURRENT ACCOUNT";
        typ[3] = "4. CREDIT ACCOUNT";

        Log.i(TAG, "WISDOM NUMBER 1A: " + ProfileParser.txnNumber);

        Intent intent = getIntent();
        final String txn = intent.getStringExtra("transaction");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AccountType.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        listView = (ListView)findViewById(R.id.listV);
        cancel = (Button) findViewById(R.id.button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AccountType.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        LISTSTRING = new ArrayList<String>(Arrays.asList(typ));
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2, android.R.id.text1, LISTSTRING)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView ListItemShow = (TextView) view.findViewById(android.R.id.text1);
                ListItemShow.setTextColor(Color.parseColor("#342815"));
                ListItemShow.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                return view;
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String value=adapter.getItem(position);
                Log.i(TAG, "ACCOUNT TYPE SELECTED: " + value);
                Log.i(TAG, "TRANSACTION: " + txn);
                DisplayTransaction(txn, value);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //finish();
    String getAccountType(String acct)
    {
        if(acct.equals("1. DEFAULT ACCOUNT"))
            return "00";
        else if(acct.equals("2. SAVINGS ACCOUNT"))
            return "10";
        else if(acct.equals("3. CURRENT ACCOUNT"))
            return "20";
        else
            return "30";
    }

    private void DisplayTransaction(String name, String acct)
    {
        Intent intent = new Intent();
        Bundle data = new Bundle();
        Log.i(TAG, "WISDOM NUMBER 2: " + ProfileParser.txnNumber);

        switch(ProfileParser.txnNumber)
        {
            case "1"://PURCHASE
            case "2"://CASH OUT
            case "3"://CASH IN
            case "4"://ACCOUNT TRANSFER
            case "5"://ACCOUNT TRANSFER
                ProfileParser.field3 = "00" + getAccountType(acct) + "00";
                intent.setClass(this, AmountInputActivity.class);
                intent.putExtras(data);
                startActivity(intent);
                finish();
                break;

            default:
                Toast.makeText(getApplicationContext(), "TRANSACTION UNKNOWN", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        Intent intent = new Intent();
        intent.setClass(AccountType.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

}
