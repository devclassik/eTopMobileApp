 package com.gbikna.sample.etop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.gbikna.sample.R;

 public class Terms extends AppCompatActivity {
    Button button;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        button = (Button) findViewById(R.id.pro_terms);
        checkBox = (CheckBox)findViewById(R.id.checkbox);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked())
                {
                    Intent inte = new Intent();
                    inte.setClass(Terms.this, BussinessInformation.class);
                    startActivity(inte);
                }else
                {
                    checkBox.setFocusable(true);
                    Toast.makeText(Terms.this, "Please Accept our Terms", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}