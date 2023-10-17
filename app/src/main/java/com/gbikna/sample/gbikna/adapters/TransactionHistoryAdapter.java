package com.gbikna.sample.gbikna.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import com.gbikna.sample.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionHistoryAdapter extends ArrayAdapter {
    Context context;
    List transactions;


    public TransactionHistoryAdapter(Context context, List transactions) {
        super(context, R.layout.history_item, transactions);
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;
        TransactionHistoryViewHolder holder = null;
        NumberFormat format = new DecimalFormat("#,###.##");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        if(listItem == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listItem = layoutInflater.inflate(R.layout.history_item, parent, false);
            holder = new TransactionHistoryViewHolder(listItem);
            listItem.setTag(holder);
        } else {
            holder = (TransactionHistoryViewHolder) listItem.getTag();
        }

        return listItem;
    }
}
