package com.example.kosbluejack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookingTransaction extends AppCompatActivity {
    DBHelper dbHelper;
    private BookingTrxAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvNotif;
    private ArrayList<String> bookId;
    private ArrayList<String> bookKosId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_transaction);
        bookId = new ArrayList<>();
        bookKosId = new ArrayList<>();
        tvNotif = findViewById(R.id.tvNotif);
        dbHelper = new DBHelper(this);
        recyclerView = findViewById(R.id.rvBookingTrx);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final String current = CurrLogin.currLogin.get(0);
        adapter = new BookingTrxAdapter(this, dbHelper.getCertainData(current));
        recyclerView.setAdapter(adapter);
        long filterrow = dbHelper.getCertainData(current).getCount();
        if(filterrow == 0){
            tvNotif.setText("No booking transactions");
        }
        else{
            tvNotif.setText("");
        }

        adapter.setItemClickListener(new BookingTrxAdapter.OnRecycleClickListener() {
            @Override
            public void onItemClick(final int position) {
                AlertDialog.Builder alert = new AlertDialog.Builder(BookingTransaction.this);
                alert.setMessage("Do you want to cancel your booking?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dbHelper.deleteAll();
                        Cursor c = dbHelper.getCertainData(current);
                        int count = c.getCount();
//                        Log.d("Booking", "count: " + count);
                        if(count > 0){
                            while(c.moveToNext()){
                                String kostid = c.getString(c.getColumnIndex(dbHelper.BOOKING_KOST_ID));
                                Log.d("Booking", "kostid: " + kostid);
                                bookKosId.add(kostid);
                            }
                        }
                        int del = dbHelper.deleteFromTableBooking(current, bookKosId.get(position));
                        if(del > 0){
                            Toast.makeText(BookingTransaction.this, "Cancel booking success", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(BookingTransaction.this, "Cancel booking failed", Toast.LENGTH_LONG).show();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.setTitle("Cancel Booking");
                alertDialog.show();
            }
        });
    }
}
