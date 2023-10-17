package com.gbikna.sample.etop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.util.database.DATABASEHANDLER;
import com.gbikna.sample.gbikna.util.database.MESSAGES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Messages extends AppCompatActivity {

    private static final String TAG = Messages.class.getSimpleName();

    ListView listView;
    List<String> LISTSTRING;
    List<MESSAGES> mes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        DATABASEHANDLER db2 = new DATABASEHANDLER(Messages.this);
        mes = db2.getAllMessage();
        String[] typ = new String[mes.size()];
        int i = 0;
        for(int j = mes.size() - 1; j >= 0; j--)
        {
            typ[i] = mes.get(j).getMessage().substring(0, 40);
            i++;
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Messages.this, Dashboard.class);
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
                Log.i(TAG, "Message SELECTED: " + value);
                DisplayTransaction(value);
            }
        });
    }

    void DisplayTransaction(String value)
    {
        for(int j = 0; j < mes.size(); j++)
        {
            if(mes.get(j).getMessage().contains(value))
            {
                DATABASEHANDLER db2 = new DATABASEHANDLER(Messages.this);
                db2.upMessage(mes.get(j).getServerid(), mes.get(j).getMessage());
                viewMessage(mes.get(j).getMessage());
                break;
            }
        }
    }

    void viewMessage(String display)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setCancelable(false);
        builder.setTitle("MESSAGE");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView vw = new TextView(this);
        vw.setText(display);
        layout.addView(vw);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent();
                intent.setClass(Messages.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        Intent intent = new Intent();
        intent.setClass(Messages.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}