package com.gbikna.sample.etop.transactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.core.content.ContextCompat;

import com.gbikna.sample.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class TransactionHistoryAdapter extends ArrayAdapter<TransactionHistory> {
    Context context;
    List<TransactionHistory> transactions;


    public TransactionHistoryAdapter(com.gbikna.sample.etop.TransactionHistory context, List<TransactionHistory> transactions) {
        super(context, R.layout.transaction_history_item, transactions);
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;
        TransactionHistoryViewHolder holder = null;
        NumberFormat format = new DecimalFormat("#,###.##");

        if(listItem == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listItem = layoutInflater.inflate(R.layout.transaction_history_item, parent, false);
            holder = new TransactionHistoryViewHolder(listItem);
            listItem.setTag(holder);
        } else {
            holder = (TransactionHistoryViewHolder) listItem.getTag();
        }

        holder.card.setText(transactions.get(position).getPan());
        holder.status.setText(transactions.get(position).getStatus());
        holder.amount.setText("₦"+format.format(Double.valueOf(transactions.get(position).getAmount())));
        if(!transactions.get(position).getStatus().equals("00")) {
            holder.icon.setBackground(ContextCompat.getDrawable(context, R.drawable.transaction_failed_bg));
            holder.statusIcon.setImageResource(R.drawable.ic_cancel);
        } else {
            holder.icon.setBackground(ContextCompat.getDrawable(context, R.drawable.transaction_success_bg));
            holder.statusIcon.setImageResource(R.drawable.ic_check);
        }

        return listItem;
    }
}