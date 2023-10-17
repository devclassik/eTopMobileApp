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
import com.gbikna.sample.etop.edit.EditBank;
import com.gbikna.sample.etop.edit.EditBusiness;
import com.gbikna.sample.etop.edit.EditPersonalInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyAccounts extends AppCompatActivity {
    private static final String TAG = MyAccounts.class.getSimpleName();

    ListView listView;
    List<String> LISTSTRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_accounts);

        String[] typ = new String[5];
        typ[0] = "1. PERSONAL PROFILE";
        typ[1] = "2. BUSINESS PROFILE";
        typ[2] = "3. BANK PROFILE";
        typ[3] = "4. LOAD WALLET";
        typ[4] = "5. PICTURE RE-CAPTURE";

        //Log.i(TAG, "WISDOM NUMBER 1A: " + ProfileParser.txnNumber);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MyAccounts.this, Dashboard.class);
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
                if(value.equals("1. PERSONAL PROFILE"))
                {
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    intent.setClass(MyAccounts.this, EditPersonalInfo.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }else if(value.equals("2. BUSINESS PROFILE"))
                {
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    intent.setClass(MyAccounts.this, EditBusiness.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }else if(value.equals("3. BANK PROFILE"))
                {
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    intent.setClass(MyAccounts.this, EditBank.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }else if(value.equals("4. LOAD WALLET"))
                {
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    intent.setClass(MyAccounts.this, LoadWallet.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }else if(value.equals("5. PICTURE RE-CAPTURE"))
                {
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    UploadImages.chk = 1;
                    intent.setClass(MyAccounts.this, UploadImages.class);
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
        intent.setClass(MyAccounts.this, More.class);
        startActivity(intent);
        finish();
    }
}