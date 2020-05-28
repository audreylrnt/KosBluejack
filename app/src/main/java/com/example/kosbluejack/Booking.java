package com.example.kosbluejack;

public class Booking {
    public String bookingId;
    public String bookingDate;
    public String bookingUserId;
    public String bookingKostId;

    public Booking(String bookingId, String bookingDate, String bookingUserId, String bookingKostId) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.bookingUserId = bookingUserId;
        this.bookingKostId = bookingKostId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingUserId() {
        return bookingUserId;
    }

    public void setBookingUserId(String bookingUserId) {
        this.bookingUserId = bookingUserId;
    }

    public String getBookingKostId() {
        return bookingKostId;
    }

    public void setBookingKostId(String bookingKostId) {
        this.bookingKostId = bookingKostId;
    }
}
