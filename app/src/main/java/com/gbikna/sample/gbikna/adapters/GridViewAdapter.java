package com.gbikna.sample.gbikna.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.core.content.ContextCompat;

import com.gbikna.sample.R;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    Context context;
    List<String> items;

    public GridViewAdapter(Context context, List<String> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridItem = convertView;
        if(gridItem == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridItem = layoutInflater.inflate(R.layout.grid_item, parent, false);
        }

        int purchase = context.getResources().getColor(R.color.purchase);
        int transfer = context.getResources().getColor(R.color.transfer);
        int bill = context.getResources().getColor(R.color.bills);
        int acct = context.getResources().getColor(R.color.acct_open);
        int bvn = context.getResources().getColor(R.color.bvn);
        int cowry = context.getResources().getColor(R.color.cowry);
        int tax = context.getResources().getColor(R.color.tax);
        int admin = context.getResources().getColor(R.color.admin);
        int history = context.getResources().getColor(R.color.history);

        Drawable ip = ContextCompat.getDrawable(context, R.drawable.ic_purchase);
        Drawable it = ContextCompat.getDrawable(context, R.drawable.ic_transfer);
        Drawable ib = ContextCompat.getDrawable(context, R.drawable.ic_bills);
        Drawable ia = ContextCompat.getDrawable(context, R.drawable.ic_open_account);
        Drawable ibvn = ContextCompat.getDrawable(context, R.drawable.ic_bvn);
        Drawable ic = ContextCompat.getDrawable(context, R.drawable.ic_cowry);
        Drawable itax = ContextCompat.getDrawable(context, R.drawable.ic_tax);
        Drawable iadmin = ContextCompat.getDrawable(context, R.drawable.ic_admin);
        Drawable ihistory = ContextCompat.getDrawable(context, R.drawable.ic_history);

        Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.grid_item_bg);
        mDrawable.setColorFilter(new PorterDuffColorFilter(
                position == 0 ? purchase : position == 1 ? transfer : position == 2 ? bill : position == 3 ? acct : position == 4 ? bvn : position == 5 ? cowry :position == 6 ? tax : position == 7 ? admin : history,
                PorterDuff.Mode.MULTIPLY
        ));

        gridItem.setBackground(mDrawable);
        ImageView image = (ImageView) gridItem.findViewById(R.id.item_image);
        TextView text = (TextView) gridItem.findViewById(R.id.item_text);

        image.setImageDrawable(
                position == 0 ? ip : position == 1 ? it : position == 2 ? ib : position == 3 ? ia : position == 4 ? ibvn : position == 5 ? ic :position == 6 ? itax : position == 7 ? iadmin : ihistory
        );
        text.setText(items.get(position));

        return gridItem;
    }
}