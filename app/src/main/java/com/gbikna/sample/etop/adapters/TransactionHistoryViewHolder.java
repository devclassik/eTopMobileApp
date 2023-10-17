package com.gbikna.sample.etop.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gbikna.sample.R;


public class TransactionHistoryViewHolder {
    TextView desc, date, amount;
    LinearLayout icon;
    ImageView statusIcon;

    TransactionHistoryViewHolder(View v) {
        desc = (TextView) v.findViewById(R.id.desc);
        date = (TextView) v.findViewById(R.id.date);
        amount = (TextView) v.findViewById(R.id.tran_amount);
        icon = (LinearLayout) v.findViewById(R.id.transaction_icon);
        statusIcon = (ImageView) v.findViewById(R.id.status_icon);
    }
}
