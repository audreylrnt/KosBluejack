package com.example.kosbluejack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KostList extends AppCompatActivity {
    public static final String TAG = "KostList";
    private RecyclerView rvKostList;
    private KostListAdapter kostListAdapter;
    private final String URL = "https://bit.ly/2zd4uhX";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_kostlist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.bookingtrx){
            Intent toBookingTrx = new Intent(KostList.this, BookingTransaction.class);
            startActivity(toBookingTrx);
        }
        if(item.getItemId() == R.id.logout){
            Intent logout = new Intent(KostList.this, MainActivity.class);
            CurrLogin.currLogin.remove(0);
            startActivity(logout);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kost_list);
        rvKostList = findViewById(R.id.rvKostList);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null && response.length() > 0){
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            Integer id, price;
                            String name, facility, address, image;
                            Double latitude, longitude;
                            id = object.getInt("id");
                            price = object.getInt("price");
                            name = object.getString("name");
                            facility = object.getString("facilities");
                            address = object.getString("address");
                            image = object.getString("image");
                            latitude = object.getDouble("LAT");
                            longitude = object.getDouble("LNG");
                            KostData kostDataa = new KostData(id, name, price, facility, image, address, latitude, longitude);
                            KostDataDB.kostData.add(kostDataa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    rvKostList.setHasFixedSize(true);
                    kostListAdapter = new KostListAdapter(KostList.this);

                    rvKostList.setLayoutManager(new LinearLayoutManager(KostList.this));
                    rvKostList.setAdapter(kostListAdapter);

                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KostList.this, "FAILED", Toast.LENGTH_LONG).show();

            }
        };
        Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue requestQueue = new RequestQueue(cache, network);

        requestQueue.start();

        JsonArrayRequest request = new JsonArrayRequest(URL, listener, errorListener);

        requestQueue.add(request);
    }

}
