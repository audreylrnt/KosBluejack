package com.example.kosbluejack;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class KostDetail extends AppCompatActivity {
    public static final String TAG = "KostDetail";
    DBHelper dbHelper;
    ArrayList<String> idtemp;
    ArrayList<String> kosttemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kost_detail);
        TextView tvKostName = findViewById(R.id.tvKostName);
        ImageView imgKost = findViewById(R.id.imgKost);
        TextView tvKostPrice = findViewById(R.id.kostsPrice);
        TextView tvKostFacility = findViewById(R.id.kostsFacilities);
        TextView tvKostAddress = findViewById(R.id.kostsAddress);
        TextView tvKostLat = findViewById(R.id.kostsLat);
        TextView tvKostLng = findViewById(R.id.kostsLng);
        Button btnBooking = findViewById(R.id.btnBooking);
        Button btnMap = findViewById(R.id.btnMap);
        dbHelper = new DBHelper(this);
        idtemp = new ArrayList<>();
        kosttemp = new ArrayList<>();
        final TextView tvBookingDate = findViewById(R.id.tvBookingDate);
        final long rowCount = dbHelper.getTableBookingRow();
        Intent fromKostList = getIntent();
        final String position = fromKostList.getStringExtra("arr");
        final String userId = CurrLogin.currLogin.get(0);
        final int pos = Integer.parseInt(position);
        String kostName = KostDataDB.kostData.get(pos).getName();
        String kostPrice = String.valueOf(KostDataDB.kostData.get(pos).getPrice());
        String thekostAddress = KostDataDB.kostData.get(pos).getAddress();
        String kostFacility = KostDataDB.kostData.get(pos).getFacilities();
        String kostLat = String.valueOf(KostDataDB.kostData.get(pos).getLatitude());
        String kostLng = String.valueOf(KostDataDB.kostData.get(pos).getLongitude());
        String kostImage = KostDataDB.kostData.get(pos).getImage();
        Glide.with(KostDetail.this).load(kostImage).into(imgKost);
        tvKostName.setText(kostName);
        tvKostPrice.setText(kostPrice);
        tvKostAddress.setText(thekostAddress);
        tvKostFacility.setText(kostFacility);
        tvKostLat.setText(kostLat);
        tvKostLng.setText(kostLng);
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeMap = new Intent(KostDetail.this, ViewLocation.class);
                seeMap.putExtra("position", position);
                startActivity(seeMap);
            }
        });

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String bookingKostData = String.valueOf(pos + 1);
                DatePickerDialog datePickerDialog = new DatePickerDialog(KostDetail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date selected = sdf.parse(date);
                            Calendar today = Calendar.getInstance();
                            today.set(Calendar.HOUR_OF_DAY, 0);
                            today.set(Calendar.MINUTE, 0);
                            today.set(Calendar.SECOND, 0);
                            today.set(Calendar.MILLISECOND, 0);
                            today.set(Calendar.YEAR, today.get(Calendar.YEAR));
                            today.set(Calendar.MONTH, today.get(Calendar.MONTH));
                            today.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH));

                            Date dtToday = today.getTime();
                            if(dtToday.compareTo(selected) > 0){
                                Toast.makeText(KostDetail.this, "Selected date must be at least today", Toast.LENGTH_LONG).show();
                            }
                            else{
                                String bookingId = validateOther();
                                Log.d(TAG, "rowcount " + rowCount);
                                if(rowCount == 0){
                                    String selDate = selected.toString();
                                    Log.d(TAG, "date: " + selDate + " curr: " + userId + " bookid: " + bookingId);
                                    Booking booking = new Booking(bookingId, selDate, userId, bookingKostData);
                                    boolean isAdded = dbHelper.insertTableBooking(booking);
                                    if(isAdded){
                                        Toast.makeText(KostDetail.this, "Boarding house successfully booked", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(KostDetail.this, "Boarding house failed to be booked", Toast.LENGTH_LONG).show();
                                    }
                                    Intent toKostList = new Intent(KostDetail.this, KostList.class);
                                    startActivity(toKostList);
                                    finish();
                                }
                                else {
                                    checkDatas();
                                    checkUserBookings();

                                    for(int i = 0; i < rowCount; i++){
                                        if(userId.equals(idtemp.get(i)) && bookingKostData.equals(kosttemp.get(i))){
                                            Toast.makeText(KostDetail.this, "You can't book the same boarding house", Toast.LENGTH_LONG).show();
                                            Log.d(TAG, "current login: " + userId);
                                            return;
                                        }
                                    }
                                    String selDate = selected.toString();
                                    Booking booking = new Booking(bookingId, selDate, userId, bookingKostData);
                                    Log.d(TAG, "bookingId waktu >1" + booking.getBookingId() + " date: " + booking.getBookingDate() + " kos id: " + booking.getBookingKostId()
                                            + " user id: " + booking.getBookingUserId());
                                    boolean isAdded = dbHelper.insertTableBooking(booking);
                                    if(isAdded){
                                        Toast.makeText(KostDetail.this, "Boarding house successfully booked", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(KostDetail.this, "Boarding house failed to be booked", Toast.LENGTH_LONG).show();
                                    }
                                    Intent toKostList = new Intent(KostDetail.this, KostList.class);
                                    startActivity(toKostList);
                                    finish();
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void checkUserBookings() {
        Cursor c = dbHelper.getUserBookings();
        while(c.moveToNext()){
            String userWhoBooked = c.getString(c.getColumnIndex(dbHelper.BOOKING_USER_ID));
            idtemp.add(userWhoBooked);
        }
    }

    private void checkDatas() {
        Cursor cp = dbHelper.getBookingKostId();
        while(cp.moveToNext()){
            String tempkostid = cp.getString(cp.getColumnIndex(dbHelper.BOOKING_KOST_ID));
            kosttemp.add(tempkostid);
        }
    }

    private String validateOther() {
        long countdbrow = dbHelper.getTableBookingRow();
        String bookingId = "";
        bookingId = String.format("BK%03d", countdbrow +1);
        return bookingId;
    }
}
