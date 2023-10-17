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
import com.gbikna.sample.gbikna.card.activity.Sync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Security extends AppCompatActivity {
    private static final String TAG = Security.class.getSimpleName();

    ListView listView;
    List<String> LISTSTRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        String[] typ = new String[3];
        typ[0] = "1. SYNC KEYS";
        typ[1] = "2. CHANGE PASSWORD";
        typ[2] = "3. CHANGE PIN";

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Security.this, Dashboard.class);
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

                if(value.equals("1. SYNC KEYS"))
                {
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    intent.setClass(Security.this, Sync.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }else if(value.equals("2. CHANGE PASSWORD"))
                {
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    intent.setClass(Security.this, ChangePassword.class);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }else if(value.equals("3. CHANGE PIN"))
                {
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    intent.setClass(Security.this, ChangePin.class);
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
        intent.setClass(Security.this, More.class);
        startActivity(intent);
        finish();
    }
}