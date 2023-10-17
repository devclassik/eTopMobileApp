package com.gbikna.sample.etop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gbikna.sample.R;
import com.gbikna.sample.etop.transactions.CardsFragment;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory extends AppCompatActivity {

    private static final String TAG = TransactionHistory.class.getSimpleName();
    TextView error_text, tref, tid, type, amt, destination, mainAmount, ref;
    LinearLayout barrier, details;
    ProgressBar loader;
    ListView listView;
    Button date_picker;
    com.gbikna.sample.etop.transactions.TransactionHistory transaction, transactionDetail;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = LayoutInflater.from(this).inflate(R.layout.fragment_cards, null);

        error_text = (TextView) vi.findViewById(R.id.error_text);
        loader = (ProgressBar) findViewById(R.id.loader);
        listView = (ListView) vi.findViewById(R.id.transaction_history_list);
        date_picker = (Button) vi.findViewById(R.id.date_picker);
        tref = (TextView) findViewById(R.id.tref) ;
        tid = (TextView) findViewById(R.id.tid);
        type = (TextView) findViewById(R.id.type);
        amt = (TextView) findViewById(R.id.detail_amount);
        destination = (TextView) findViewById(R.id.destination);
        mainAmount = (TextView) findViewById(R.id.main_amount);
        ref = (TextView) findViewById(R.id.ref);
        //time = (TextView) findViewById(R.id.date2);

        barrier = (LinearLayout) findViewById(R.id.barrier);
        details = (LinearLayout) findViewById(R.id.details);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);


        //TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BussinessInformation.busControl = 1;
                Intent intent = new Intent();
                intent.setClass(TransactionHistory.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });


        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Transactions");
        //arrayList.add("Wallet");

        preparePageView(viewPager, arrayList);


        tabLayout.setupWithViewPager(viewPager);

        //TransactionDetailViewModel transactionDetailViewModel = new ViewModelProvider(this).get(TransactionDetailViewModel.class);

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker()
            .setTheme(R.style.ThemeOverlay_App_MaterialCalendar);
        builder.setTitleText("Select Date Range");
        long today = MaterialDatePicker.todayInUtcMilliseconds();


        CalendarConstraints.Builder con = new CalendarConstraints.Builder();

        CalendarConstraints.DateValidator dateValidator = DateValidatorPointBackward.now();

        con.setValidator(dateValidator);

        con.setEnd(today);

        builder.setCalendarConstraints(con.build());
        final MaterialDatePicker materialDatePicker = builder.build();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BussinessInformation.busControl = 1;
                Intent intent = new Intent();
                intent.setClass(TransactionHistory.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_picker.setEnabled(false);
                materialDatePicker.show(getSupportFragmentManager(), "DATE PICKER");
            }
        });

        materialDatePicker.addOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                date_picker.setEnabled(true);
            }
        });

        materialDatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_picker.setEnabled(true);
            }
        });
    }

    private void preparePageView(ViewPager viewPager, ArrayList<String> arrayList) {
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        adapter.addFragment( new CardsFragment(), "Transactions");
        //adapter.addFragment( new WalletFragment(), "Wallet");
        viewPager.setAdapter(adapter);
    }

    private class MainAdapter extends FragmentPagerAdapter {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();
        public void addFragment(Fragment fragment, String title) {
            arrayList.add(title);
            fragmentList.add(fragment);
        }

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return arrayList.get(position);
        }
    }


    @Override
    public void onBackPressed() {
        System.out.println("2. ignored profileddownload onBackPressed 1111");
        Intent intent = new Intent();
        Bundle data = new Bundle();
        intent.setClass(TransactionHistory.this, More.class);
        intent.putExtras(data);
        startActivity(intent);
        finish();
    }
}