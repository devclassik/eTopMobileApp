package com.gbikna.sample.etop.transactions;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gbikna.sample.R;

public class TransactionHistoryViewHolder {
    TextView card, status, amount;
    LinearLayout icon;
    ImageView statusIcon;

    TransactionHistoryViewHolder(View v) {
        card = (TextView) v.findViewById(R.id.card);
        status = (TextView) v.findViewById(R.id.status);
        amount = (TextView) v.findViewById(R.id.tran_amount);
        icon = (LinearLayout) v.findViewById(R.id.transaction_icon);
        statusIcon = (ImageView) v.findViewById(R.id.status_icon);
    }
}
