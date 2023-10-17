package com.gbikna.sample.gbikna;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gbikna.sample.R;
import com.gbikna.sample.etop.Login;

public class Logout extends Activity {
    private static final String TAG = Logout.class.getSimpleName();
    private TextView mTextView;
    private TextView exit;
    private TextView proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        mTextView = (TextView) findViewById(R.id.text_lg);
        exit = (TextView) findViewById(R.id.text_cancel);
        proceed = (TextView) findViewById(R.id.text_proceed);

        mTextView.setText("Check Your Internet Connection and Retry.");
        exit.setText("EXIT");
        proceed.setText("RETRY");

        LinearLayout exit = (LinearLayout) findViewById (R.id.button_cancel);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        LinearLayout retry = (LinearLayout) findViewById (R.id.button_proceed);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Logout.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setClass(Logout.this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        Log.i(TAG, "4. Destroying ProfileDownload");
        super.onDestroy();
    }
}