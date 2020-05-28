package com.example.kosbluejack;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class KostListAdapter extends RecyclerView.Adapter<KostListAdapter.ViewHolder> {
    Context context;

    public KostListAdapter(Context context) {
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, facilities;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.kostName);
            price = itemView.findViewById(R.id.kostPrice);
            facilities = itemView.findViewById(R.id.kostFacility);
            image = itemView.findViewById(R.id.kostImage);
        }
    }

    @NonNull
    @Override
    public KostListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kost_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KostListAdapter.ViewHolder holder, final int position) {
        final KostData kostData = KostDataDB.kostData.get(position);
        holder.name.setText(kostData.getName());
        String lePrice = String.valueOf(kostData.getPrice());
        holder.price.setText(lePrice);
        holder.facilities.setText(kostData.getFacilities());
        Glide.with(context).load(kostData.image).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "pos: " + position + "arrsize: " + KostDataDB.kostData.size(), Toast.LENGTH_LONG).show();
                Intent toKostDetail = new Intent(context, KostDetail.class);
                String pos = String.valueOf(position);
//                Toast.makeText(context, "pos: " + pos, Toast.LENGTH_LONG).show();
                toKostDetail.putExtra("arr", pos);
                context.startActivity(toKostDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }


}