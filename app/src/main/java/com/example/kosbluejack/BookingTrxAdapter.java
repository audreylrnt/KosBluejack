package com.example.kosbluejack;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookingTrxAdapter extends RecyclerView.Adapter<BookingTrxAdapter.ViewHolder> {
    ArrayList<String> kostidtemp = new ArrayList<>();
    private OnRecycleClickListener mListener;

    public interface OnRecycleClickListener{
        void onItemClick(int position);
    }

    public void setItemClickListener(OnRecycleClickListener listener){
        mListener = listener;
    }

    Context ctx;
    Cursor res;

    public BookingTrxAdapter(Context ctx, Cursor res) {
        this.ctx = ctx;
        this.res = res;
    }

    @NonNull
    @Override
    public BookingTrxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(R.layout.booking_trx_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingTrxAdapter.ViewHolder holder, int position) {
        if(!res.moveToPosition(position)){
            return;
        }

        String bookingId = res.getString(res.getColumnIndex(DBHelper.BOOKING_BOOKING_ID));
        String bookingDate = res.getString(res.getColumnIndex(DBHelper.BOOKING_BOOKING_DATE));
        holder.tvBookingId.setText(bookingId);
        holder.tvBookingDate.setText(bookingDate);
    }

    @Override
    public int getItemCount() {
        return res.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvBookingId, tvBookingDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingId = itemView.findViewById(R.id.textviewBookingId);
            tvBookingDate = itemView.findViewById(R.id.textviewbookingDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
