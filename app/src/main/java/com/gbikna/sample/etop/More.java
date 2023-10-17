package com.gbikna.sample.etop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.card.activity.Reports;
import com.gbikna.sample.etop.wallet.Activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class More extends AppCompatActivity {
    private static final String TAG = More.class.getSimpleName();

    ListView listView;
    List<String> LISTSTRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        String[] typ = new String[7];
        typ[0] = "1. WALLET ACTIVITIES";
        typ[1] = "2. TRANSACTIONS HISTORY";
        typ[2] = "3. RECORDS";
        typ[3] = "4. END OF DAY SUMMARY";
        typ[4] = "5. MY ACCOUNT";
        typ[5] = "6. SECURITY";
        typ[6] = "7. EXPORT WALLET";

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(More.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        listView = (ListView)findViewById(R.id.listV);
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
                Log.i(TAG, "TXN SELECTED: " + value);
                if(value.equals("1. WALLET ACTIVITIES"))
                {
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    intent.setClass(More.this, Activities.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }else if(value.equals("2. TRANSACTIONS HISTORY"))
                {
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    intent.setClass(More.this, TransactionHistory.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }else if(value.equals("3. RECORDS"))
                {
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    intent.setClass(More.this, Reports.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }else if(value.equals("4. END OF DAY SUMMARY"))
                {
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    intent.setClass(More.this, EodSummary.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }else if(value.equals("5. MY ACCOUNT"))
                {
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    intent.setClass(More.this, MyAccounts.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }else if(value.equals("6. SECURITY"))
                {
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    intent.setClass(More.this, Security.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }else if(value.equals("7. EXPORT WALLET"))
                {
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    intent.setClass(More.this, WalletExport.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        Intent intent = new Intent();
        intent.setClass(More.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}