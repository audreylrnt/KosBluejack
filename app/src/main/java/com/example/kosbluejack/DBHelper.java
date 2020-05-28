package com.example.kosbluejack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.RowId;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DB_VER = 1;
    public static final String DB_NAME = "KostManager";

    public static final String TABLE_USER = "users";
    public static final String TABLE_BOOKING = "bookings";

    public static final String USER_USER_ID = "userId";
    public static final String USER_USER_NAME = "userName";
    public static final String USER_PASSWORD = "userPassword";
    public static final String USER_PHONE = "userPhone";
    public static final String USER_GENDER = "userGender";
    public static final String USER_DOB = "userDOB";

    public static final String BOOKING_BOOKING_ID = "bookingId";
    public static final String BOOKING_BOOKING_DATE = "bookingDate";
    public static final String BOOKING_USER_ID = "bookingUserId";
    public static final String BOOKING_KOST_ID = "bookingKostId";

    public static final String createTableUser = "CREATE TABLE " + TABLE_USER + "(" +
            USER_USER_ID + " TEXT, " +
            USER_USER_NAME + " TEXT, " +
            USER_PASSWORD + " TEXT, " +
            USER_PHONE + " TEXT, " +
            USER_GENDER + " TEXT, " +
            USER_DOB + " TEXT );";

    public static final String createTableBooking = "CREATE TABLE " + TABLE_BOOKING + " (" +
            BOOKING_BOOKING_ID + " TEXT, " +
            BOOKING_BOOKING_DATE + " TEXT, " +
            BOOKING_USER_ID + " TEXT, " +
            BOOKING_KOST_ID + " TEXT );";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableUser);
        db.execSQL(createTableBooking);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING);
        onCreate(db);
    }

    public boolean insertTableUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_USER_ID, user.getUserId());
        cv.put(USER_USER_NAME, user.getUserName());
        cv.put(USER_PASSWORD, user.getUserPassword());
        cv.put(USER_PHONE, user.getUserPhone());
        cv.put(USER_GENDER, user.getUserGender());
        cv.put(USER_DOB, user.getUserDOB());
        long result = db.insert(TABLE_USER, null, cv);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean insertTableBooking(Booking booking){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BOOKING_BOOKING_ID, booking.getBookingId());
        cv.put(BOOKING_BOOKING_DATE, booking.getBookingDate());
        cv.put(BOOKING_USER_ID, booking.getBookingUserId());
        cv.put(BOOKING_KOST_ID, booking.getBookingKostId());
        long result = db.insert(TABLE_BOOKING, null, cv);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public long getTableUserRow(){
        SQLiteDatabase db = this.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(db,TABLE_USER);
        return count;
    }

    public long getTableBookingRow(){
        SQLiteDatabase db = this.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_BOOKING);
        return count;
    }

    public Cursor getUserName(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + USER_USER_NAME + " FROM " + TABLE_USER, null);
        return res;
    }

    public Cursor getUserPassword(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + USER_PASSWORD + " FROM " + TABLE_USER, null);
        return res;
    }

    public Cursor getBookingKostId(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + BOOKING_KOST_ID + " FROM " + TABLE_BOOKING, null);
        return res;
    }

    public Cursor getUserId(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + USER_USER_ID + " FROM " + TABLE_USER, null);
        return res;
    }

    public Cursor getUserBookings(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + BOOKING_USER_ID + " FROM " + TABLE_BOOKING, null);
        return res;
    }

    public Cursor getCertainData(String currUser){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = BOOKING_USER_ID + " LIKE ?";
        String[] selArgs = {currUser};
        Cursor res = db.query(TABLE_BOOKING, null, selection, selArgs, null, null, null);
        return res;
    }

    public Integer deleteFromTableBooking(String userId, String kostId){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = BOOKING_USER_ID + " LIKE ? AND " + BOOKING_KOST_ID + " LIKE ?";
        String[] selArgs = {userId, kostId};
        Integer res = db.delete(TABLE_BOOKING, selection, selArgs);
        return res;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKING, null, null);
    }
}
